package pe.gob.sblm.sgi.daoImpl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import pe.gob.sblm.api.commons.utility.FuncionesHelper;
import pe.gob.sblm.sgi.dao.ISuperUsuarioDAO;
import pe.gob.sblm.sgi.entity.Area;
import pe.gob.sblm.sgi.entity.Auditoria;
import pe.gob.sblm.sgi.entity.Estadoauditoria;
import pe.gob.sblm.sgi.entity.Eventoauditoria;
import pe.gob.sblm.sgi.entity.Modulo;
import pe.gob.sblm.sgi.entity.Perfil;
import pe.gob.sblm.sgi.entity.Perfilusuario;
import pe.gob.sblm.sgi.entity.Usuario;

@Repository(value = "superusuarioDAO")
public class SuperUsuarioDAOImpl implements ISuperUsuarioDAO, Serializable {
	private static final long serialVersionUID = -7132329520845816103L;
	AuditoriaDAOImpl audilog= new AuditoriaDAOImpl();
	
	
	@Autowired
	private SessionFactory sessionFactory;
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}


	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}


	
	public List<Usuario> listarUsuarios() {
		Session session = getSessionFactory().openSession();

		return session.createQuery("from Usuario").list();
	}


	
	public List<Perfilusuario> getPerfilesUsuario(String selectIdRegistroUsuario) {
		Session session = getSessionFactory().openSession();
		
		return session.createQuery("select pu from Perfilusuario pu   inner join pu.usuario u inner join pu.perfil p where u.idusuario='"+selectIdRegistroUsuario+"'").list();
	
	}
	
	
	public List<Usuario> getUsuarioEditSinPerfil(String selectIdRegistroUsuario) {
		Session session = getSessionFactory().openSession();

		return session.createQuery("from Usuario where Idusuario='"+selectIdRegistroUsuario+"'").list();
	}
	
	
	public List getUsuarioEditConPerfil(String selectIdRegistroUsuario) {
		Session session = getSessionFactory().openSession();
		
		List<List> list = null;
		list=session.createQuery(" select new List(U.apellidopat,U.apellidomat,U.nombres, U.emailusr,U.estado,U.cargo,U.nombreusr,U.contrasenausr,p.idperfil,U.fechanacimiento,a.idare)" +
				"from Usuario U  inner join U.perfilusuarios pu inner join pu.perfil p inner join U.area a where U.idusuario='"+selectIdRegistroUsuario+"'").list();
		
		List<Perfilusuario> lista = new ArrayList<Perfilusuario>();

		for(int i=0;i<list.size();i++)
		{
			Usuario usu= new Usuario();
			usu.setApellidopat((String) list.get(i).get(0));
			usu.setApellidomat((String) list.get(i).get(1));
			usu.setNombres((String) list.get(i).get(2));
			usu.setEmailusr((String) list.get(i).get(3));
			usu.setEstado( (Boolean) list.get(i).get(4));
			usu.setCargo((String) list.get(i).get(5));
			usu.setNombreusr((String) list.get(i).get(6));
			usu.setContrasenausr((String) list.get(i).get(7));
			usu.setFechanacimiento((Date) list.get(i).get(9));
			
			Area area= new Area();
			area.setIdare((String) list.get(i).get(10));
			usu.setArea(area);
			
			Perfil perfil= new Perfil();
			perfil.setIdperfil((Integer) (list.get(i).get(8)));

			
			Perfilusuario perfilusuario=new Perfilusuario();
			
			perfilusuario.setUsuario(usu);
			perfilusuario.setPerfil(perfil);
						
			lista.add(perfilusuario);
			
			
		}

		
//		"  Select * from USUARIO INNER JOIN PERFILUSUARIO ON USUARIO.IDUSUARIO=PERFILUSUARIO.IDUSUARIO " +
//		"INNER JOIN PERFIL ON PERFIL.IDPERFIL=PERFILUSUARIO.IDPERFIL WHERE USUARIO.IDUSUARIO='"+selectIdRegistroUsuario+"'"

		return lista;
	
	}
	
	@Transactional(readOnly = true)
	
	public void save(Perfilusuario perfilusuario) {
		getSessionFactory().getCurrentSession().save(perfilusuario);

	}

	@Transactional(readOnly = true)
	
	public void actualizarUsuario(Usuario u, String fechaUpdate) {
		//Session session = getSessionFactory().openSession();

		//getSessionFactory().getCurrentSession().update(u);
		
		
		String updateQuery=	"UPDATE USUARIO  SET [NOMBRES] ='"+u.getNombres()+"'"+
				",[APELLIDOMAT] ='" +u.getApellidomat()+"'"+
				",[APELLIDOPAT] ='" +u.getApellidopat()+"'"+
				",[FECHANACIMIENTO] ='"+fechaUpdate+"'"+
				",[ESTADO] ='" +u.getEstado()+"'"+
				",[NOMBREUSR] ='" +u.getNombreusr()+"'"+
//				",[CONTRASENAUSR] ='" +u.getContrasenausr()+"'"+
		//		",[FECCRE] = <FECCRE, date,>" +
				",[RUTAIMGUSR] ='" +u.getRutaimgusr()+"'"+
				",[USRCRE] ='" +u.getUsrcre()+"'"+
				",[EMAILUSR] ='" +u.getEmailusr()+"'"+
				",[CARGO] ='" +u.getCargo()+"'"+
				" WHERE IDUSUARIO='"+u.getIdusuario()+"'";
		
		
		getSessionFactory().getCurrentSession().createSQLQuery(updateQuery).executeUpdate();
		
	}


	
	public List<Perfil> listPerfilbyNom() {
		Session session = getSessionFactory().openSession();

		return session.createQuery("from Perfil").list();	
	}

	   
	
	public Object getLastIdUsuario() {
			Session session = getSessionFactory().openSession();

			Query numero=session.createSQLQuery("select MAX(USUARIO.IDUSUARIO) FROM USUARIO ");
			
			int var=(Integer) numero.list().get(0);
			

			return var ;
			
	}


	
	public void eliminarUsuario(String selectIdRegistroUsuario) {
		Session session = getSessionFactory().openSession();

		
		String queryDelete="delete Usuario where idusuario=:id";
		session.createQuery(queryDelete).setParameter("id",Integer.parseInt(selectIdRegistroUsuario)).executeUpdate();
	
	
	}


	
	public void eliminarAntiguoPerfilUsuarios(int idusuario) {
		Session session = getSessionFactory().openSession();

		String queryDelete="delete Perfilusuario where idusuario=:id";
		session.createQuery(queryDelete).setParameter("id",idusuario).executeUpdate();				
	}


	@Transactional(readOnly = true)
	
	public void nuevoUsuario(Usuario usr) {
			System.out.println("grabando");

			
			getSessionFactory().getCurrentSession().save(usr);
			
			 try {
				 //idestadoauditoria 0 auditoria / 1 notificacion
				 settingLog(2);
				 } catch (Exception e) {
				 e.printStackTrace();
				 }
	}
	
	public void settingLog( int ideventoauditoria) {
		String url = FuncionesHelper.getURL().toString();

		try {
			int index = url.indexOf("pages/");
			url = url.substring(index + 6, url.length());
			url = url.substring(0, url.length() - 4);
		} catch (Exception e) {
		}
		Auditoria Adt = new Auditoria();
		Usuario usr = new Usuario();
		usr.setIdusuario((Integer) (FuncionesHelper.getUsuario()));

		Modulo mod = new Modulo();
		Session session = getSessionFactory().openSession();

		Query numero = session
				.createSQLQuery("select  m.IDMODULO from PAGINAMODULO as pm inner join MODULO m on pm.IDMODULO= m.IDMODULO  inner join PAGINA as p on p.IDPAGINA=pm.IDPAGINA where P.NOMBREPAGINA='"
						+ url + "'");

		int var = (Integer) numero.uniqueResult();
		System.out.println("###########::::" + var);
		mod.setIdmodulo(var);
		Estadoauditoria esa = new Estadoauditoria();
		esa.setIdestadoauditoria(4);
		Eventoauditoria eva = new Eventoauditoria();
		eva.setIdeventoauditoria(ideventoauditoria);
		Adt.setUsuario(usr);
		Adt.setModulo(mod);
		Adt.setEstadoauditoria(esa);
		Adt.setEventoauditoria(eva);
		Adt.setFecentrada(new Date());
		Adt.setNompantalla(url);
		Adt.setUrl(FuncionesHelper.getURL().toString());
		if (FuncionesHelper.getTerminal().toString().equals("0:0:0:0:0:0:0:1")) {
			Adt.setIp("192.");
		} else {
			Adt.setIp(FuncionesHelper.getTerminal().toString());
		}
		Adt.setEstado(true);
		Adt.setCodauditoria(0);
		try {
			getSessionFactory().getCurrentSession().save(Adt);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	
	public void actualizarUsuario(String nombreUsuario, String nombreRegistro, String pat,
			String mat, String cargoRegistro, String emailRegistro,
			String passRegistro, String formatearFechaString,String ruta) {
		
		String updateQuery=	"UPDATE USUARIO  SET [NOMBRES] ='"+nombreRegistro+"'"+
				",[APELLIDOMAT] ='" +mat+"'"+
				",[APELLIDOPAT] ='" +pat+"'"+
				",[FECHANACIMIENTO] ='"+formatearFechaString+"'"+
				",[NOMBREUSR] ='" +nombreUsuario+"'"+
				",[CONTRASENAUSR] ='" +passRegistro+"'"+
		//		",[FECCRE] = <FECCRE, date,>" +
				",[RUTAIMGUSR] ='" +ruta+"'"+
				",[USRCRE] ='super'"+
				",[EMAILUSR] ='" +emailRegistro+"'"+
				",[CARGO] ='" +cargoRegistro+"'"+
				" WHERE IDUSUARIO='"+FuncionesHelper.getUsuario().toString()+"'";
		
		
		getSessionFactory().getCurrentSession().createSQLQuery(updateQuery).executeUpdate();
		
		
	}


	
	public List<Area> getAllArea() {
		Session session = getSessionFactory().openSession();

		return session.createQuery("from Area").list();
	}


	
	public Usuario getUsuarioaEditar(String selectIdRegistroUsuario) {
		return (Usuario) getSessionFactory().getCurrentSession().createQuery("select u from Usuario u where u.idusuario='"+selectIdRegistroUsuario+"' ").uniqueResult();
	}

	

	
	
}
