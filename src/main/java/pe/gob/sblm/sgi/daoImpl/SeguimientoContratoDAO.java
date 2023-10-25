package pe.gob.sblm.sgi.daoImpl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import pe.gob.sblm.api.commons.utility.FuncionesHelper;
import pe.gob.sblm.sgi.dao.ISeguimientoContratoDAO;
import pe.gob.sblm.sgi.entity.Archivo;
import pe.gob.sblm.sgi.entity.Area;
import pe.gob.sblm.sgi.entity.Auditoria;
import pe.gob.sblm.sgi.entity.Contrato;
import pe.gob.sblm.sgi.entity.Detallesegcontrato;
import pe.gob.sblm.sgi.entity.Estadoauditoria;
import pe.gob.sblm.sgi.entity.Eventoauditoria;
import pe.gob.sblm.sgi.entity.Modulo;
import pe.gob.sblm.sgi.entity.Perfil;
import pe.gob.sblm.sgi.entity.Perfilusuario;
import pe.gob.sblm.sgi.entity.Requisito;
import pe.gob.sblm.sgi.entity.Seguimientocontrato;
import pe.gob.sblm.sgi.entity.Tiporequisito;
import pe.gob.sblm.sgi.entity.Usuario;

@Repository(value = "seguimientocontratoDAO")
public class SeguimientoContratoDAO implements ISeguimientoContratoDAO,
		Serializable {
	private static final long serialVersionUID = 1L;

	@Autowired
	private SessionFactory sessionFactory;

	Session sesion = null;

	
	public void registrarSeguimientoContrato(
			Seguimientocontrato seguimientoContrato) {
		getSessionFactory().getCurrentSession().merge(seguimientoContrato);

	}

	
	public void eliminarSeguimientoContrato(
			Seguimientocontrato seguimientoContrato) {
		try {
			getSessionFactory()
					.getCurrentSession()
					.createSQLQuery(
							"delete from Seguimientocontrato WHERE idsegcontrato='"
									+ seguimientoContrato.getIdsegcontrato()
									+ "'").executeUpdate();
		} catch (Exception e) {
			System.out
					.println("error en dao eliminar ramo:::" + e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	
	public List<Seguimientocontrato> listarSeguimientoContratos() {
		
			Session session = getSessionFactory().openSession();
			return session.createQuery("from Seguimientocontrato sc where sc.siactivopaso=false").list();
			
	}
	
	
	
	@SuppressWarnings("unchecked")
	
	public List<Seguimientocontrato> listarSeguimientoContratosXContrato(int idcontrato) {
		
			Session session = getSessionFactory().openSession();
			return session.createQuery("select sc from Seguimientocontrato sc where sc.numeropaso= (select max(seg.numeropaso) from Seguimientocontrato seg where seg.contrato.idcontrato="+idcontrato+") and sc.contrato.idcontrato="+idcontrato+" ").list();
			
	}
	
	
	@SuppressWarnings("unchecked")
	
	public List<Integer> listarIDContratosDeSeguimientoContrato() {
		
			Session session = getSessionFactory().openSession();
	return session.createQuery("select distinct sc.contrato.idcontrato from Seguimientocontrato sc").list();
			
	}
	
	@SuppressWarnings("unchecked")
	
	public List<Seguimientocontrato> listarSeguimientoContratosXContrato(Contrato con) {
		
			Session session = getSessionFactory().openSession();
			return session.createQuery("select sc from Seguimientocontrato sc left join fetch sc.contrato c where c.idcontrato="+con.getIdcontrato()+" ").list();
	}
	
	@SuppressWarnings("unchecked")
	
	public List<Contrato> listarContratos() {
		Session session = getSessionFactory().openSession();
		return session.createQuery("select con from Contrato con left join fetch  con.upa u  left join fetch u.inmueble i").list();
	}
	
	public List<Tiporequisito> listarTiporequisitos() {
		Session session = getSessionFactory().openSession();
		return session.createQuery("from Tiporequisito").list();
	}
	
	
	public Seguimientocontrato obtenerUltimoSeguimientoContrato() {
		return (Seguimientocontrato) getSessionFactory()
				.openSession()
				.createQuery(
						"select t from Seguimientocontrato t where idsegcontrato=( select max(idsegcontrato) from Seguimientocontrato)")
				.uniqueResult();

	}
	
	public List<Requisito> obteneRequisitos() {
		
		Session session = getSessionFactory().openSession();
		
		return session.createQuery("select R from Requisito R inner join R.tiporequisito TR").list();
	}
	
	public Seguimientocontrato obtenerSeguimientoContratoParametros(int idcontrato, int paso) {
		return (Seguimientocontrato) getSessionFactory()
				.openSession()
				.createQuery(
						"select sc from Seguimientocontrato sc where sc.contrato.idcontrato="+idcontrato+" and sc.numeropaso="+paso+" ")
				.uniqueResult();

	}
	
	
	public Seguimientocontrato obtenerSeguimientoContratoPorId(int id) {
		Session session = getSessionFactory().openSession();
		return (Seguimientocontrato) session
				.load(Seguimientocontrato.class, id);
	}
	
	public  Tiporequisito obtenerTiporequisitoPorId(int id) {
		Session session = getSessionFactory().openSession();
		return (Tiporequisito) session
				.load(Tiporequisito.class, id);
	}
	
	
	public int obtenerNumeroRegistros() {
		Long count = (Long) getSessionFactory().openSession()
				.createQuery("select count(*) from Seguimientocontrato").uniqueResult();
		return count.intValue();
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	
	public void registrarDetalleSeguimientoContrato(Detallesegcontrato detallesegcontrato) {
		getSessionFactory().getCurrentSession().merge(detallesegcontrato);
		
	}
	
	
	public List<Detallesegcontrato> obtenerDetalleSeguimientoContrato(int idcontrato) {
		Session session = getSessionFactory().openSession();
		return session.createQuery("select dsc from Detallesegcontrato dsc inner join dsc.seguimientocontrato sc inner join sc.contrato c  inner join dsc.requisito r inner join r.tiporequisito tr where c.idcontrato='"+idcontrato+"'").list();
	}

	
	public List<Detallesegcontrato> obtenerTodo() {
		Session session = getSessionFactory().openSession();
		return session.createQuery("select dsc from Detallesegcontrato dsc inner join dsc.seguimientocontrato sc inner join sc.contrato c  inner join dsc.requisito r inner join r.tiporequisito tr").list();
	}

	
	public int pasoactualcontrato(int idcontrato) {
		Session session = getSessionFactory().openSession();
		Long a=(Long) session.createQuery("select count(sc.numeropaso) from Seguimientocontrato sc inner join sc.contrato  c where c.idcontrato='"+idcontrato+"'").uniqueResult();
		
		return a.intValue();
	
	}

	
	public void eliminardetalleSeguimientoContrato(int idsegcontrato) {
		getSessionFactory()
		.getCurrentSession()
		.createSQLQuery(
				"delete from Detallesegcontrato WHERE idsegcontrato='"
						+ idsegcontrato + "'")
		.executeUpdate();
		
	}

	
	public Boolean obtenerValorSiFinalizado(int numeropaso,int idcontrato) {
		Session session = getSessionFactory().openSession();
		
		Object query =session.createQuery("select sc.siactivopaso from Seguimientocontrato sc inner join sc.contrato c where sc.numeropaso='"+numeropaso+"' and c.idcontrato='"+idcontrato+"'").uniqueResult();
	
		return Boolean.parseBoolean(query.toString());
	}

	
	public void actualizarPaso(int pasoactualcontrato, String nombresusr, String rutaimagenusr) {
		getSessionFactory()
		.getCurrentSession()
		.createSQLQuery(
				"UPDATE   Seguimientocontrato  SET siactivopaso='TRUE', usercre='"+nombresusr+"',usrruta='"+rutaimagenusr+"' WHERE numeropaso='"
						+ pasoactualcontrato + "'")
		.executeUpdate();
	}
	
	
public  void settingLog(int idestadoauditoria, int ideventoauditoria, int idusuariodestino, String mensajePersonalizado){
		
		String url=FuncionesHelper.getURL().toString();
	
		try {
			int index = url.indexOf("pages/");
			url=url.substring(index+6, url.length());
			url=url.substring(0, url.length()-4);
		} catch (Exception e) {e.getMessage();	}

				Session session = sessionFactory.openSession();
				Auditoria Adt= new Auditoria();
				Usuario usr= new Usuario();
				usr.setIdusuario((Integer)(FuncionesHelper.getUsuario()));
				
				Usuario usrdes= new Usuario();
				usrdes.setIdusuario(idusuariodestino);
				
				Modulo mod= new Modulo();
				
				SQLQuery modulo=session.createSQLQuery("select  m.IDMODULO from PAGINAMODULO as pm inner join MODULO m on pm.IDMODULO= m.IDMODULO  inner join PAGINA as p on p.IDPAGINA=pm.IDPAGINA where P.NOMBREPAGINA='"+url+"'");
				
				if (modulo.list().size()==1) {
					int var=(Integer) modulo.list().get(0);
					mod.setIdmodulo(var);
				} else {
					//mod.setIdmodulo(idmodulo)
				}
				
				
				Estadoauditoria esa= new Estadoauditoria();
				esa.setIdestadoauditoria(idestadoauditoria);
				Eventoauditoria eva= new Eventoauditoria();
				eva.setIdeventoauditoria(ideventoauditoria);
				Adt.setUsuario(usr);
				Adt.setUsuariodestino(usrdes);
				Adt.setModulo(mod);
				Adt.setEstadoauditoria(esa);
				Adt.setEventoauditoria(eva);
				Adt.setFecentrada( new Date());
				Adt.setNompantalla(url);
				Adt.setUrl(FuncionesHelper.getURL().toString());
				Adt.setIp(FuncionesHelper.getTerminal().toString());
				Adt.setEstado(true);
				Adt.setCodauditoria(1);
				Adt.setMensajepersonalizado(mensajePersonalizado);
				
		try {
			sessionFactory.getCurrentSession().save(Adt);
		
		} catch (Exception e) {
		e.printStackTrace();
		}
	
	}


public List<Usuario> obtenerUsuarios() {
	Session session = sessionFactory.openSession();
	List<List> list = null;
	
	
	list = session.createQuery("select new List(U.idusuario,U.rutaimgusr,U.cargo,A.desare,U.nombrescompletos) from Usuario U inner join U.area A").list();
	List<Usuario> lista = new ArrayList<Usuario>();
	
	
	for(int i=0;i<list.size();i++){
		
		Area area= new Area();
		
		area.setDesare((String) list.get(i).get(3));
		
		System.out.println(area.getDesare());
		
		
		Usuario usuario= new Usuario();
		usuario.setRutaimgusr((String) list.get(i).get(1));
		usuario.setCargo((String) list.get(i).get(2));
		usuario.setIdusuario((Integer) list.get(i).get(0));
		usuario.setNombrescompletos((String)list.get(i).get(4));
		
		usuario.setArea(area);
		
		
		lista.add(usuario);
	}
	
	
	return lista;
	
}


public List<Perfilusuario> getPerfilesUsuario(int idUsuarioSeleccionado) {
	Session session = sessionFactory.openSession();
	List<List> list = null;
	Calendar fecha = Calendar.getInstance();
	 int nroMes=fecha.getTime().getMonth()+1;
	 
	 list = session.createQuery("select new List (p.idperfil,p.nombreperfil) from Perfilusuario pu inner join pu.usuario u inner join pu.perfil p where u.idusuario='"+idUsuarioSeleccionado+"'").list();
		
		
		List<Perfilusuario> lista = new ArrayList<Perfilusuario>();
		
		
		for(int i=0;i<list.size();i++){
			
			Perfilusuario pu = new Perfilusuario();
			
			Perfil p= new Perfil();
			
			p.setIdperfil((Integer) list.get(i).get(0));
			p.setNombreperfil((String) list.get(i).get(1));
			

			
			pu.setPerfil(p);
			
			
			lista.add(pu);
		}
		return lista;
}

	
	public List<Perfil> obtenerPerfiles() {
		Session session = sessionFactory.openSession();
		return session.createQuery("from Perfil").list();
	}
	
	
	public void actualizarPaso3YNotificar(int idcontrato, int nroPaso,
			String contenidoMensajePersonalizado,
			List<Usuario> listadousuariosSeleccionados, String destinatarios) {
		
		getSessionFactory()
		.getCurrentSession()
		.createSQLQuery(
				"UPDATE   Seguimientocontrato SET mensajenotificacion='"+contenidoMensajePersonalizado+"', destinatarios='"+destinatarios+"' WHERE numeropaso='"
						+ nroPaso + "' AND idcontrato='"+idcontrato+"'")
		.executeUpdate();
		
		try {
			for (Usuario usuario : listadousuariosSeleccionados) {
				settingLog(1, 12, usuario.getIdusuario(),contenidoMensajePersonalizado);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public void grabarArchivosAdjuntos(Archivo archivodocu) {
		getSessionFactory().getCurrentSession().save(archivodocu);
		
	}

	
	public void eliminarArchivoDocumentoNoEncontrado(int idarchivodocumento) {
		Session session = getSessionFactory().openSession();
		String queryDelete="delete Archivodocumento where idarchivodocumento=:id";
		session.createQuery(queryDelete).setParameter("id",idarchivodocumento).executeUpdate();
	}


	
	public List<Seguimientocontrato> obtenerSeguimientoContrato(int idcontrato) {
		Session session = getSessionFactory().openSession();
		return session.createQuery("select sc from Seguimientocontrato sc inner join sc.contrato c where c.idcontrato='"+idcontrato+"'").list();
	}
}
