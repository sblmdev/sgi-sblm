package pe.gob.sblm.sgi.daoImpl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedProperty;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import pe.gob.sblm.api.commons.utility.FuncionesHelper;
import pe.gob.sblm.sgi.dao.ITipoCambioDAO;
import pe.gob.sblm.sgi.entity.Auditoria;
import pe.gob.sblm.sgi.entity.Estadoauditoria;
import pe.gob.sblm.sgi.entity.Eventoauditoria;
import pe.gob.sblm.sgi.entity.Modulo;
import pe.gob.sblm.sgi.entity.Perfil;
import pe.gob.sblm.sgi.entity.Tipocambio;
import pe.gob.sblm.sgi.entity.Usuario;
import pe.gob.sblm.sgi.service.IUsuarioService;



@Repository(value = "tipocambioDAO")
public class TipoCambioDAO implements ITipoCambioDAO,Serializable{

	
	private static final long serialVersionUID = 1L;

	@Autowired
	private SessionFactory sessionFactory;

	Session sesion = null;
	Transaction txt = null;
	@ManagedProperty(value = "#{usuarioService}")
	private transient IUsuarioService usuarioService;
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	
	public void registrarTipoCambio(Tipocambio tipoCambio) {
		getSessionFactory().getCurrentSession().merge(tipoCambio);
		 try {
			 //idestadoauditoria 0 auditoria / 1 notificacion
			 settingLog(11);
			 } catch (Exception e) {
			 e.printStackTrace();
			 }
	}
	


	public Tipocambio obtenerTipoCambio() {

		//sesion = getSessionFactory().openSession();
		//txt = sesion.beginTransaction();
		// recuperamos el obj con el id.
		Tipocambio t= (Tipocambio) getSessionFactory().openSession().createQuery(
				"select t from Tipocambio t where idtipocambio=( select max(idtipocambio) from Tipocambio)").uniqueResult();
		 
		
		 return t;
	}
	public void settingLog( int ideventoauditoria) {
		String url = FuncionesHelper.getURL().toString();
        int idperfil;
		try {
			int index = url.indexOf("pages/");
			url = url.substring(index + 6, url.length());
//			url = url.substring(0, url.length() - 4);
		} catch (Exception e) {
		}
		Auditoria Adt = new Auditoria();
		Usuario usr = new Usuario();
		Perfil per= new Perfil();
		usr.setIdusuario((Integer) (FuncionesHelper.getUsuario()));
        //perfil
		per.setIdperfil((Integer) (FuncionesHelper.getPerfil()));
		Modulo mod = new Modulo();
		Session session = getSessionFactory().openSession();

//		Query numero = session.createSQLQuery("select  m.IDMODULO from PAGINAMODULO as pm inner join MODULO m on pm.IDMODULO= m.IDMODULO  inner join PAGINA as p on p.IDPAGINA=pm.IDPAGINA where P.NOMBREPAGINA='"
//					+ url + "'");
		Query numero =session
		.createSQLQuery("select  m.IDMODULO from PAGINAMODULO as pm inner join MODULO m on pm.IDMODULO= m.IDMODULO inner join PERFILMODULO pem on m.IDMODULO=pem.IDMODULO inner join PAGINA as p on p.IDPAGINA=pm.IDPAGINA where P.NOMBREPAGINA like'"
				+ url + "' and pem.IDPERFIL='"+per.getIdperfil() +"' ");

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

	
	public Tipocambio listarTipoCambioPorId(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	
	public List<Tipocambio> listarTipoCambios() {
	   return getSessionFactory().getCurrentSession().createQuery(" select tp from Tipocambio tp order by tp.fecha desc").list();
	}
	
	
	public Tipocambio obtenerUltimoTipocambio() {
		return (Tipocambio) getSessionFactory()
				.openSession()
				.createQuery(
						"select t from Tipocambio t where idtipocambio=( select max(idtipocambio) from Tipocambio)")
				.uniqueResult();
	}

	
	@SuppressWarnings("unchecked")
	
	public Double obtenerTipoCambio(String fecha) {
		List<Double> lista= new ArrayList<Double>();
		lista=getSessionFactory().openSession().createQuery("select t.tipocambio from Tipocambio t where t.fecha='"+fecha+"'").list();
		
		if (lista.size()==0) {
			return 0.0;
		} else {
			return lista.get(0);
		}
		
	}
	public Tipocambio getTipoCambio() {
		Tipocambio t= (Tipocambio) getSessionFactory().openSession().createQuery(
				"select t from Tipocambio t where fecha=( select max(fecha) from Tipocambio)").uniqueResult();
		 return t;
	}

	public IUsuarioService getUsuarioService() {
		return usuarioService;
	}

	public void setUsuarioService(IUsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

}
