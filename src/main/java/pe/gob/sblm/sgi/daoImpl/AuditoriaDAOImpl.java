package pe.gob.sblm.sgi.daoImpl;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import pe.gob.sblm.api.commons.utility.FuncionesHelper;
import pe.gob.sblm.sgi.dao.IAuditoriaDAO;
import pe.gob.sblm.sgi.entity.Auditoria;
import pe.gob.sblm.sgi.entity.Estadoauditoria;
import pe.gob.sblm.sgi.entity.Eventoauditoria;
import pe.gob.sblm.sgi.entity.Modulo;
import pe.gob.sblm.sgi.entity.Pagina;
import pe.gob.sblm.sgi.entity.Perfilusuario;
import pe.gob.sblm.sgi.entity.Usuario;

@Repository(value = "auditoriaDAO")
public class AuditoriaDAOImpl implements IAuditoriaDAO, Serializable {
	private static final long serialVersionUID = -7132329520845816103L;
	
	@Autowired
	private SessionFactory sessionFactory;
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}


	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	

	@Transactional(readOnly = true)
	@SuppressWarnings("unchecked")
	public List<Auditoria> listarAuditoriaSingle() {

		Session session = getSessionFactory().openSession();

		return session.createQuery("from Auditoria  A order by A.fecentrada  desc").list();
	}
 
	
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<Usuario> listUsuariobyNom() {
		Session session = getSessionFactory().openSession();
		return session.createQuery("from Usuario").list();
	}

	
	
	@SuppressWarnings("unchecked")
	public List<Perfilusuario> listPerfilbyNom() {
		Session session = getSessionFactory().openSession();
		return session.createQuery("from Perfil").list();
	}

	
	
	@Transactional(readOnly = true)
	public List<Modulo> listmodulobyNom() {
		Session session = getSessionFactory().openSession();
		return session.createQuery("from Modulo").list();
	}
	@Transactional(readOnly = true)	
	public List<Pagina> listRecursobyNom() {
		Session session = getSessionFactory().openSession();
		return session.createQuery("from Pagina").list();
	}
	

	public Object ultimoModuloVisitado() {
		Session session = getSessionFactory().openSession();
		Query numero=session.createSQLQuery("select TOP 1 M.NOMBREMODULO from AUDITORIA as A inner join MODULO as M ON A.IDMODULO=M.IDMODULO where A.IDUSUARIO='"+FuncionesHelper.getUsuario().toString()+"'  order by FECENTRADA desc");
		
		if (numero.list().size()==0) {
			return "no registrado";
		}else {
			return numero.list().get(0).toString();
		} 
		
		
	}
	

	public Object ultimaPaginaVisitado() {
		Session session = getSessionFactory().openSession();
		Query numero=session.createSQLQuery("select TOP 1 A.NOMPANTALLA from AUDITORIA as A inner join MODULO as M ON A.IDMODULO=M.IDMODULO where A.IDUSUARIO='"+FuncionesHelper.getUsuario().toString()+"'  order by FECENTRADA desc");
		return numero.list().get(0);
	}

	
	
	
	


	@Transactional(readOnly = true)
	public Usuario listUsuarioTop() {
		Session session = getSessionFactory().openSession();

		Calendar fecha = Calendar.getInstance();
		int nroMes=fecha.getTime().getMonth()+1;
		
		String query="SELECT TOP 1 NOMBREUSR, COUNT(NOMBREUSR) maximo FROM AUDITORIA INNER JOIN USUARIO ON AUDITORIA.IDUSUARIO=USUARIO.IDUSUARIO  WHERE MONTH(AUDITORIA.FECENTRADA)='"+(nroMes)+"' AND AUDITORIA.IDEVENTOAUDITORIA!=7   GROUP BY NOMBREUSR  ORDER BY maximo DESC";
		
		
		Query sql = session.createSQLQuery(query);
		
		List<Object>  objectList= new ArrayList<Object>(sql.list());
		
		
		List<Usuario> listUser= new ArrayList<Usuario>();
		Iterator<Object> iterator = objectList.iterator();
		
		
		if(objectList.isEmpty()){
			Usuario user= new Usuario();
			user=null;
			return user;
		}else{
		while(iterator.hasNext()){ 
			Object []obj = (Object[])iterator.next();
			
			Usuario U= new Usuario();

			U.setNombreusr((String) obj[0]);
			listUser.add(U);
			
		}
	
		List<Usuario>  usuarios = session.createQuery("FROM Usuario U where U.nombreusr='"+listUser.get(0).getNombreusr()+"'").list();
		return usuarios.get(0);
		}
		
		
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public String nroConectadosDelDia() {
		Calendar fecha = Calendar.getInstance();
		String hoy=String.format("%1$tY-%1$tm-%1$td", fecha.getTime());

		
		Session session = getSessionFactory().openSession();
		Query Q = session.createSQLQuery("select cast(A.FECENTRADA as date)AS FECHA,U.NOMBREUSR from AUDITORIA AS A inner join usuario AS U on A.IDUSUARIO=U.IDUSUARIO  where IDEVENTOAUDITORIA=7");
		
	
		List<Object> objectList= new ArrayList<Object>(Q.list());
		List<Usuario> listUser= new ArrayList<Usuario>();
		
		
		Iterator<Object> iterator = objectList.iterator();
		while(iterator.hasNext()){ 
			Object []obj = (Object[])iterator.next();
		
			Usuario U= new Usuario();
			U.setContrasenausr((String) obj[0]);
			U.setNombreusr((String) obj[1]);
			listUser.add(U);
		}
		
		for(int i=0;i<listUser.size();i++){
			if(!listUser.get(i).getContrasenausr().equals(hoy)){
				listUser.remove(i);	
				i=i-1;
			}
		}

		
		for(int i=0; i<listUser.size(); i++) {
		    Usuario usrA = (Usuario) listUser.get(i);
		    for(int j=i+1; j<listUser.size(); j++) {
		        Usuario usrB = (Usuario) listUser.get(j);
		        if (usrA.getNombreusr().equals(usrB.getNombreusr())) {
		        	listUser.remove(j);
		        	j=j-1;
		        }
		    }
		}
		
		return String.valueOf(listUser.size());
	}



	@SuppressWarnings("unchecked")
	
	public List listAuditoriaFiltro(Date fechaInicio, Date fechaFin,
			String nombreUsuario, String nomPantalla, String nomPerfil,
			String nomModulo) {
		
		Session session = getSessionFactory().openSession();

		String query="";
		String queryNombreUsuario = "";
		String queryNombrePantalla ="";
		String queryNombrePerfil ="";
		String queryNombreModulo ="";
		String queryFecha ="";
		
		java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat("dd/MM/yyyy");
		String fInicio = sdf.format(fechaInicio);
		Calendar c = Calendar.getInstance();
		String fFin= sdf.format(fechaFin);
		
			try {
				c.setTime(sdf.parse(fFin));
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// TODO Auto-generated catch block
		c.add(Calendar.DATE, 1);
		fFin = sdf.format(c.getTime());
		
		if(!nombreUsuario.equals("")){
			queryNombreUsuario = "u.nombres+' '+u.apellidopat+' '+u.apellidomat = '"+nombreUsuario+"' and "; 
		}
		if(!nomPantalla.equals("")){
			queryNombrePantalla = "nompantalla='"+nomPantalla+"' and ";
		}

		if(!nomModulo.equals("")){
			queryNombreModulo = "R.modulo.idmodulo ='"+nomModulo+"' and ";
		}
		if(fechaInicio!=null && fechaFin!=null){
			long L_23_HOURS_59 = 86400000;
			fechaFin.setTime(fechaFin.getTime()+L_23_HOURS_59);
			queryFecha = "R.fecentrada>=:fecIni and R.fecentrada<:fecFin and ";
			
		}

		List<List> list = null;
		
		query = "" +
		"select new List(R.idauditoria,R.modulo.nombremodulo,R.url,R.fecentrada,EA.tipoevento,u.apellidopat,u.apellidomat,u.nombres,u.rutaimgusr)   from Auditoria R inner join R.usuario u inner join R.eventoauditoria EA" +
		" where "+queryNombreUsuario+"fecentrada>='"+fInicio+"' and fecentrada<'"+fFin+"' and"+" "+queryNombrePantalla+""+queryNombreModulo+"1=1";
		
		System.out.println("mu query"+query);
		
		try {
			list = session.createQuery(query).list();
			session.close();
		} catch (Exception e) {
								e.printStackTrace();
								}
		
		
		
		System.out.println("Tamaño de lista: "+list.size());
		
		List<Auditoria> lista = new ArrayList<Auditoria>();

		for(int i=0;i<list.size();i++){
			
			Usuario usu= new Usuario();
			usu.setApellidopat((String) list.get(i).get(5));
			usu.setApellidomat((String) list.get(i).get(6));
			usu.setNombres((String) list.get(i).get(7));
			usu.setRutaimgusr((String) list.get(i).get(8));
			Modulo modu= new Modulo();
			modu.setNombremodulo((String)list.get(i).get(1));
			
			Eventoauditoria ev= new Eventoauditoria();
			ev.setTipoevento((String) list.get(i).get(4));
			
			Auditoria auditori = new Auditoria();
			auditori.setIdauditoria( (Integer) list.get(i).get(0));
			auditori.setUrl((String) list.get(i).get(2));
			auditori.setFecentrada((Date) list.get(i).get(3));
			
			auditori.setEventoauditoria(ev);
			auditori.setModulo(modu);
			auditori.setUsuario(usu);
			
						
			lista.add(auditori);
		}
		
		return lista;
	
	}


	
	public List listAuditoriaFiltroPerfil(Date fechaInicio, Date fechaFin,
			String nombrePerfil, String recursoBusqueda, String nomPerfil,
			String moduloBusqueda) {

		Session session = getSessionFactory().openSession();

		String query="";
		String queryNombreUsuario = "";
		String queryNombrePantalla ="";
		String queryNombrePerfil ="";
		String queryNombreModulo ="";
		String queryFecha ="";
		
		
		//java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat("MM/dd/yyyy");
		java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat("dd/MM/yyyy");
		String fInicio = sdf.format(fechaInicio);
		String fFin= sdf.format(fechaFin);
		

		if(!recursoBusqueda.equals("")){
			queryNombrePantalla = "nompantalla='"+recursoBusqueda+"' and ";
		}
		if(!nombrePerfil.equals("")){
			queryNombrePerfil = "PERFIL.IDPERFIL ='"+nombrePerfil+"' and ";
		}
		if(!moduloBusqueda.equals("")){
			queryNombreModulo = "MODULO.IDMODULO ='"+moduloBusqueda+"' and ";
		}
		if(fechaInicio!=null && fechaFin!=null){
			long L_23_HOURS_59 = 86400000;
			fechaFin.setTime(fechaFin.getTime()+L_23_HOURS_59);
			queryFecha = "R.fecentrada>=:fecIni and R.fecentrada<:fecFin and ";
			
		}

		List<List> list = null;
		
		query="select DISTINCT AUDITORIA.IDAUDITORIA,AUDITORIA.NOMPANTALLA,EVENTOAUDITORIA.TIPOEVENTO,MODULO.NOMBREMODULO,USUARIO.NOMBREUSR, AUDITORIA.FECENTRADA  from AUDITORIA inner join USUARIO ON USUARIO.IDUSUARIO=AUDITORIA.IDUSUARIO inner join PERFILUSUARIO ON PERFILUSUARIO.IDUSUARIO=USUARIO.IDUSUARIO inner join PERFIL ON PERFIL.IDPERFIL=PERFILUSUARIO.IDPERFIL inner join MODULO ON MODULO.IDMODULO=AUDITORIA.IDMODULO inner join EVENTOAUDITORIA ON AUDITORIA.IDEVENTOAUDITORIA=EVENTOAUDITORIA.IDEVENTOAUDITORIA where "+queryNombrePerfil+"fecentrada>='"+fInicio+"' and fecentrada<'"+fFin+"' and"+" "+queryNombrePantalla+""+queryNombreModulo+"1=1";
		
		Query Q = session.createSQLQuery(query);
		
		List<Object> objectList = Q.list();
		Iterator iterator = objectList.iterator();
		List<Auditoria> lista = new ArrayList<Auditoria>();
		while(iterator.hasNext()){ 
			Object []obj = (Object[])iterator.next();

			
			
			Usuario usu= new Usuario();
			usu.setNombreusr((String) obj[4]);
			
			Modulo modu= new Modulo();
			modu.setNombremodulo((String)obj[3]);
			
			Eventoauditoria ev= new Eventoauditoria();
			ev.setTipoevento((String) obj[2]);
			
			Auditoria auditori = new Auditoria();
			auditori.setIdauditoria((Integer) obj[0]);
			auditori.setNompantalla((String) obj[1]);
			auditori.setFecentrada( (Date) obj[5]);
			
			auditori.setEventoauditoria(ev);
			auditori.setModulo(modu);
			auditori.setUsuario(usu);
			
						
			lista.add(auditori);
			
			}
		
	
		return lista;
	}

	
	
	
	
	
	@Transactional(readOnly = true)
	public  void settingLog(int idusuario, int idmodulo, int idestadoauditoria, int ideventoauditoria, Date fechaentrada, String nombrepantalla,
							String url,Boolean estado, int codauditoria){
		System.out.println("Entro metodo de Clase auditoria");
		
		Auditoria Adt= new Auditoria();
		
		Usuario usr= new Usuario();
		usr.setIdusuario(idusuario);
		
		Modulo mod= new Modulo();
		mod.setIdmodulo(idmodulo);
		
		Estadoauditoria esa= new Estadoauditoria();
		esa.setIdestadoauditoria(idestadoauditoria);
		
		Eventoauditoria eva= new Eventoauditoria();
		eva.setIdeventoauditoria(ideventoauditoria);
		
		Adt.setUsuario(usr);
		Adt.setModulo(mod);
		Adt.setEstadoauditoria(esa);
		Adt.setEventoauditoria(eva);
		
		Adt.setFecentrada(fechaentrada);
		Adt.setNompantalla(nombrepantalla);
		Adt.setUrl(url);
		Adt.setEstado(estado);
		Adt.setCodauditoria(codauditoria);
		
	try {
		
		//sessionFactory.getCurrentSession().createSQLQuery("select * from auditoria").executeUpdate();
		getSessionFactory().getCurrentSession().save(Adt);
	} catch (HibernateException e) 	{
			e.printStackTrace();	}
	}



	

}
