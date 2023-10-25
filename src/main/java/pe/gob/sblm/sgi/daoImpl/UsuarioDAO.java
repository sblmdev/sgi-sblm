package pe.gob.sblm.sgi.daoImpl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import pe.gob.sblm.api.commons.utility.FuncionesHelper;
import pe.gob.sblm.sgi.bean.CuotaBean;
import pe.gob.sblm.sgi.dao.IUsuarioDAO;
import pe.gob.sblm.sgi.entity.Auditoria;
import pe.gob.sblm.sgi.entity.Estadoauditoria;
import pe.gob.sblm.sgi.entity.Eventoauditoria;
import pe.gob.sblm.sgi.entity.Modulo;
import pe.gob.sblm.sgi.entity.Sunat_Comprobante_Detalle;
import pe.gob.sblm.sgi.entity.Usuario;

@Repository(value = "usuarioDAO") 
public class UsuarioDAO implements IUsuarioDAO, Serializable {
	private static final long serialVersionUID = -7132329520845816103L;

	@Autowired
	private SessionFactory sessionFactory;

	Session sesion = null;
	Transaction txt = null;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}


	
	public Usuario listarUsuarioPorId(int id) {
		Session session = getSessionFactory().openSession();
		return (Usuario) session.load(Usuario.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<Usuario> listarUsuarios() {
		

		StringBuilder hql= new StringBuilder();
		hql.append("select u from Usuario u");
		
		return (List<Usuario>) sessionFactory.getCurrentSession().createQuery(hql.toString()).list();
	}
	
	
	@SuppressWarnings("unchecked")
	
	public List<Usuario> listarUsuarios(int iduser) {
		Session session = getSessionFactory().openSession();
		return session.createQuery("from Usuario").list();
	}



	public Usuario buscarUsuario(Usuario usuario) {
		Session session = getSessionFactory().openSession();
		try {
			Query query = session
					.createQuery("select u from Usuario u where nombreusr = '"+usuario.getNombreusr()+"' ");
			//.createQuery("select u from Usuario u where nombreusr = '"+usuario.getNombreusr()+"' and contrasenausr = '"+usuario.getContrasenausr()+"' ");
//			query.setParameter("login", usuario.getNombreusr());
//			query.setParameter("password", usuario.getContrasenausr());
			
		
			return (Usuario) query.uniqueResult();

		} catch (HibernateException e) {
			System.out.println("error:::" + e);
			throw e;
		} 
//		finally {
//			session.close();
//		}

	}
	public Sunat_Comprobante_Detalle buscarComprobante(Sunat_Comprobante_Detalle det) {
		Session session = getSessionFactory().openSession();
		System.out.println("2id_compr_articulo="+det.getId_compr_det());
		try {
			Query query = session
					.createQuery("select u.id_compr_det as id_compr_det,u.nroitem as nroitem ,u.descripcion_articulo as descripcion_articulo from Sunat_Comprobante_Detalle u where id_compr_det ="+det.getId_compr_det()+" ");
			//.createQuery("select u from Usuario u where nombreusr = '"+usuario.getNombreusr()+"' and contrasenausr = '"+usuario.getContrasenausr()+"' ");
//			query.setParameter("login", usuario.getNombreusr());
//			query.setParameter("password", usuario.getContrasenausr());
			
		
			//return (Sunat_Comprobante_Detalle) query.uniqueResult();
			return (Sunat_Comprobante_Detalle)query.setResultTransformer(Transformers.aliasToBean(Sunat_Comprobante_Detalle.class)).uniqueResult();

		} catch (HibernateException e) {
			System.out.println("error:::" + e);
			e.printStackTrace();
			throw e;
		} 
//		finally {
//			session.close();
//		}

	}
	

	
	public int obtenerIdPerfil(Usuario usuario) {
		Session session = getSessionFactory().openSession();
		try {
			Query query = session.createSQLQuery("Select pu.idperfil from Perfilusuario pu  where pu.idusuario="+ usuario.getIdusuario() + " and activo='true'");// verificar

			return Integer.parseInt(query.uniqueResult().toString());
		} catch (HibernateException e) {
			System.out.println("error:::" + e);
			throw e;
		} finally {
			session.close();
		}
	}

	@SuppressWarnings("unchecked")
	
	public List<String> loguear(Usuario usuario) {


		return (List<String>) getSessionFactory()
				.getCurrentSession()
				.createSQLQuery(
						" select u.idusuario,u.nombreusr,u.contrasenausr from PERFILUSUARIO as pu inner join USUARIO as u on pu.IDUSUARIO=u.IDUSUARIO  where u.CONTRASENAUSR = '"
								+ usuario.getContrasenausr()
								+ "' and u.NOMBREUSR='"
								+ usuario.getNombreusr() + "' and pu.ACTIVO=1")
				.list();
	}
	
	
	public Usuario buscarUsuarioxId(int parseInt) {
		Session session = getSessionFactory().openSession();
		
		try {
			Query query = session
					.createQuery("select u from Usuario u where u.idusuario = '"+parseInt+"'");
		
			return (Usuario) query.uniqueResult();

		} catch (HibernateException e) {
			System.out.println("error:::" + e);
			throw e;
		} 
	}


		
	
	public  void settingLog(int idestadoauditoria, int ideventoauditoria, int idusuariodestino){
		String url=FuncionesHelper.getURL().toString();
		
		try {
			int index = url.indexOf("pages/");
			url=url.substring(index+6, url.length());
			url=url.substring(0, url.length()-4);
		} catch (Exception e) {
		}
	

	
		Auditoria Adt= new Auditoria();
		Usuario usr= new Usuario();
		usr.setIdusuario((Integer)(FuncionesHelper.getUsuario()));
		
//		Area area = new Area();
//		area.setIdare("10800000");
//		usr.setArea(area);
		
		Modulo mod= new Modulo();
		Session session = getSessionFactory().openSession();
		
		Query numero=session.createSQLQuery("select  m.IDMODULO from PAGINAMODULO as pm inner join MODULO m on pm.IDMODULO= m.IDMODULO  inner join PAGINA as p on p.IDPAGINA=pm.IDPAGINA where P.NOMBREPAGINA='"+url+"'");
		
		int var=(Integer) numero.list().get(0);
		
		mod.setIdmodulo(var);
		Estadoauditoria esa= new Estadoauditoria();
		esa.setIdestadoauditoria(idestadoauditoria);
		Eventoauditoria eva= new Eventoauditoria();
		eva.setIdeventoauditoria(ideventoauditoria);
		Adt.setUsuario(usr);
		Adt.setModulo(mod);
		Adt.setEstadoauditoria(esa);
		Adt.setEventoauditoria(eva);
		Adt.setFecentrada( new Date());
		Adt.setNompantalla(url);
		Adt.setUrl(FuncionesHelper.getURL().toString());
		if(FuncionesHelper.getTerminal().toString().equals("0:0:0:0:0:0:0:1")){
			Adt.setIp("192.");
		}else{
			Adt.setIp(FuncionesHelper.getTerminal().toString());
		}
		Adt.setEstado(true);
		Adt.setCodauditoria(0);
		try {
		getSessionFactory().getCurrentSession().save(Adt);
		
		} catch (Exception e) {
		e.printStackTrace();}
		
		}

	
	public String obtenerNombrePerfilSeleccionado(Usuario u) {
		Session session = getSessionFactory().openSession();
		try {
			Query query = session
					.createSQLQuery("Select p.nombreperfil from Perfilusuario pu  INNER JOIN Usuario u ON pu.idusuario="
							+ u.getIdusuario() + " inner join Perfil p on p.idperfil=pu.idperfil and activo='true'");// verificar
			
			query.setMaxResults(1);


			return String.valueOf(query.uniqueResult());
		} catch (HibernateException e) {
			System.out.println("error:::" + e);
			throw e;
		} finally {
			session.close();
		}
	}

	
	public Usuario obtenerUsuario(String nombreusr) {
		Session session = getSessionFactory().openSession();
		Query query = session
				.createQuery("SELECT u FROM Usuario u where u.nombreusr = :nombreusr");
		query.setParameter("nombreusr", nombreusr);

		return (Usuario) query.uniqueResult();
	}


	
	public boolean siAutorizado(int idperfil, int idpagina) {
		StringBuilder hql = new StringBuilder();
		hql.append("select pp.idpaginaperfil from Paginaperfil pp ");
		hql.append("inner join  pp.pagina pag ");
		hql.append("inner join  pp.perfil per ");
		hql.append(" where per.idperfil='"+idperfil+"' and pag.idpagina='"+idpagina+"'");
		

		Query query= sessionFactory.getCurrentSession().createQuery(hql.toString());
		
		if (query.list().size()!=0){
			return true;
		} else {
			return false;
		}
	}



}
