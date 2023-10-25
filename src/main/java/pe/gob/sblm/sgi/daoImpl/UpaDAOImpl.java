package pe.gob.sblm.sgi.daoImpl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import pe.gob.sblm.api.commons.constants.sgi.Constantes;
import pe.gob.sblm.api.commons.utility.FuncionesHelper;
import pe.gob.sblm.sgi.bean.ProcesojudicialupaBean;
import pe.gob.sblm.sgi.bean.UpaBean;
import pe.gob.sblm.sgi.dao.IUpaDAO;
import pe.gob.sblm.sgi.entity.Auditoria;
import pe.gob.sblm.sgi.entity.Estadoauditoria;
import pe.gob.sblm.sgi.entity.Eventoauditoria;
import pe.gob.sblm.sgi.entity.Inmueble;
import pe.gob.sblm.sgi.entity.Materialpredominante;
import pe.gob.sblm.sgi.entity.Mep;
import pe.gob.sblm.sgi.entity.Modulo;
import pe.gob.sblm.sgi.entity.Procesojudicialupa;
import pe.gob.sblm.sgi.entity.Tipotitularidad;
import pe.gob.sblm.sgi.entity.Tipovia;
import pe.gob.sblm.sgi.entity.Titularidad;
import pe.gob.sblm.sgi.entity.Upa;
import pe.gob.sblm.sgi.entity.Usuario;
import pe.gob.sblm.sgi.entity.Valuacion;
import pe.gob.sblm.sgi.util.Constante;

@Repository(value = "upaDAO")
public class UpaDAOImpl  implements IUpaDAO, Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private SessionFactory sessionFactory;

	Session sesion = null;

	
	
	public void registrarUpa(Upa upa) {
		getSessionFactory().getCurrentSession().merge(upa);
		
	}
     
	
	public void actualizarUpa(Upa upa) {
		// TODO Auto-generated method stub
		String updateQuery="";
		if(upa.getSiactualizaclave()){
			updateQuery="UPDATE UPA SET ESTADO='"+Constante.ESTADO_B+"' ,CLAVENUEVA='"+upa.getClave()+"', USRMOD='"+upa.getUsrcre()+
					"',FECMOD=GETDATE() WHERE idupa='"+upa.getIdupa_anterior().getIdupa()+"'";
			getSessionFactory().getCurrentSession().createSQLQuery(updateQuery).executeUpdate();
		}

		
	}

	
	public void eliminarUpa(Upa upa) {
//		try {
			getSessionFactory()
					.getCurrentSession()
					.createSQLQuery(
							"delete from Upa WHERE idupa='"
									+ upa.getIdupa() + "'")
					.executeUpdate();
//		} catch (Exception e) {
//			System.out.println("error en dao eliminar upa:::"
//					+ e.getMessage());
//		}
			
			settingLog(33);

	}

	public void grabarInmueble(Inmueble inmueble) {
		getSessionFactory().getCurrentSession().merge(inmueble);
		
	}
	public Upa validarRepetido(String numreg) {
		return (Upa) getSessionFactory()
				.openSession()
				.createQuery(
						"select t from Upa t where numregistrosbn='"+numreg+"'  ")
				.uniqueResult();
	}
	
	public Upa validarClaveRepetido(String clave) {
		return (Upa) getSessionFactory().getCurrentSession().createQuery("select t from Upa t where clave='"+clave+"'  ").uniqueResult();
	}
	
	
	public Upa obtenerUpaPorClave(String clave) {
		return (Upa) getSessionFactory()
				.openSession()
				.createQuery(
						"select u from Upa u where u.clave='"+clave+"'  ")
				.uniqueResult();
	}
	
	public Inmueble obtenerInmueblePorParametro(String valor) {
		Session session = getSessionFactory().openSession();

		try {
			return (Inmueble) session.createQuery("from Inmueble inmu where  inmu.numregistrosbn='"+valor+"' ")
					.uniqueResult();
		} catch (HibernateException e) {
			System.out.println("error  obtenerInmueblePorParametro:::" + e.getMessage());
			throw e;
		} finally {
			session.close();
		}
	}
	
	
	public int obtenerNumeroRegistros() {
		Long count = (Long) getSessionFactory().openSession()
				.createQuery("select count(*) from Upa").uniqueResult();
		return count.intValue();
	}

	
	public List<Tipovia> listarTipoVia() {
		Session session = getSessionFactory().openSession();

		try {
			return session.createQuery("from Tipovia order by descripcion ")
					.list();
		} catch (HibernateException e) {
			System.out.println("error listar Tipovia:::" + e.getMessage());
			throw e;
		} finally {
			session.close();
		}
	}

	
	
	public List<Valuacion> listarValuacion() {
		Session session = getSessionFactory().openSession();

		try {
			return session.createQuery("from Valuacion order by descripcion ")
					.list();
		} catch (HibernateException e) {
			System.out.println("error listar valuacion:::" + e.getMessage());
			throw e;
		} finally {
			session.close();
		}
	}
	
	
	public List<Mep> listarMep() {
		Session session = getSessionFactory().openSession();

		try {
			return session.createQuery("from Mep order by idmep asc ")
					.list();
		} catch (HibernateException e) {
			System.out.println("error listar mep:::" + e.getMessage());
			throw e;
		} finally {
			session.close();
		}
	}
	
	public List<Titularidad> listarTitularidad() {
		Session session = getSessionFactory().openSession();

		try {
			return session.createQuery("from Titularidad order by nombre ")
					.list();
		} catch (HibernateException e) {
			System.out.println("error listar Tipovia:::" + e.getMessage());
			throw e;
		} finally {
			session.close();
		}
	}
	
	public List<Tipotitularidad> listarTipoTitularidad() {
		Session session = getSessionFactory().openSession();

		try {
			return session.createQuery("from Tipotitularidad order by descripcion ")
					.list();
		} catch (HibernateException e) {
			System.out.println("error listar Tipovia:::" + e.getMessage());
			throw e;
		} finally {
			session.close();
		}
	}
	
	public List<Materialpredominante> listarMaterialPredominante() {
		Session session = getSessionFactory().openSession();

		try {
			return session.createQuery("from Materialpredominante order by descripcion ")
					.list();
		} catch (HibernateException e) {
			System.out.println("error listar Materialpredominante:::" + e.getMessage());
			throw e;
		} finally {
			session.close();
		}
	}
	
	
	
	public List<String> listaInmuebles() {
		Session session = getSessionFactory().openSession();

		try {
			return session.createQuery(" select  distinct numregistrosbn from Inmueble order by numregistrosbn ")
					.list();
		} catch (HibernateException e) {
			System.out.println("error listar listarDepartamentos:::" + e.getMessage());
			throw e;
		} finally {
			session.close();
		}
	}
	
	public List<String> listarDepartamentos() {
		Session session = getSessionFactory().openSession();

		try {
			return session.createQuery(" select  distinct depa from Ubigeo order by depa ")
					.list();
		} catch (HibernateException e) {
			System.out.println("error listar listarDepartamentos:::" + e.getMessage());
			throw e;
		} finally {
			session.close();
		}
	}
	
	public List<String> listarProvincias(String dpto) {
		Session session = getSessionFactory().openSession();

		try {
			return session.createQuery(" select  distinct prov from Ubigeo  where depa='"+dpto+"' order by prov ")
					.list();
		} catch (HibernateException e) {
			System.out.println("error listar listarProvincias:::" + e.getMessage());
			throw e;
		} finally {
			session.close();
		}
	}
	
	public List<String> listarDistritos(String prov) {
		Session session = getSessionFactory().openSession();

		try {
			return session.createQuery(" select  distinct dist from Ubigeo  where prov='"+prov+"' order by dist ")
					.list();
		} catch (HibernateException e) {
			System.out.println("error listar listarProvincias:::" + e.getMessage());
			throw e;
		} finally {
			session.close();
		}
	}
	
	public List<String> listarUbigeoPorDistrito( String prov,String dist) {
		Session session = getSessionFactory().openSession();

		try {
			return session.createQuery(" select  ubigeo from Ubigeo  where prov='"+prov+"' and dist='"+dist+"' ")
					.list();
		} catch (HibernateException e) {
			System.out.println("error listar listarUbigeoPorDistrito:::" + e.getMessage());
			throw e;
		} finally {
			session.close();
		}
	}
	
	public List<String> listarUbigeos() {
		Session session = getSessionFactory().openSession();

		try {
			return session.createQuery(" select  ubigeo from Ubigeo order by ubigeo")
					.list();
		} catch (HibernateException e) {
			System.out.println("error listar listarUbigeos:::" + e.getMessage());
			throw e;
		} finally {
			session.close();
		}
	}
	
	public List<String> listarDptoPorUbigeo(String ubigeo) {
		Session session = getSessionFactory().openSession();

		try {
			return session.createQuery(" select  depa from Ubigeo where ubigeo='"+ubigeo+"' ")
					.list();
		} catch (HibernateException e) {
			System.out.println("error listar listarUbigeos:::" + e.getMessage());
			throw e;
		} finally {
			session.close();
		}
	}
	
	public List<String> listarProvinciaPorUbigeo(String ubigeo) {
		Session session = getSessionFactory().openSession();

		try {
			return session.createQuery(" select  prov from Ubigeo where ubigeo='"+ubigeo+"' ")
					.list();
		} catch (HibernateException e) {
			System.out.println("error listar listarUbigeos:::" + e.getMessage());
			throw e;
		} finally {
			session.close();
		}
	}
	
	public List<String> listarDistritoPorUbigeo(String ubigeo) {
		Session session = getSessionFactory().openSession();

		try {
			return session.createQuery(" select  dist from Ubigeo where ubigeo='"+ubigeo+"' ")
					.list();
		} catch (HibernateException e) {
			System.out.println("error listar listarUbigeos:::" + e.getMessage());
			throw e;
		} finally {
			session.close();
		}
	}
	public void settingLog(int ideventoauditoria) {
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

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@SuppressWarnings("unchecked")
	
	public List<Upa> listarUpas(int idinmueble) {
		Session session = getSessionFactory().openSession();
		return session.createQuery(" select  u from Upa u inner join u.inmueble i where  i.idinmueble='"+idinmueble+"' ").list();
	}

	
	public Upa obtenerUpaxClave(String clave) {
		Session session = getSessionFactory().openSession();
		return (Upa) session.createQuery(" select  u from Upa u where  u.clave='"+clave+"' ").uniqueResult();
	}

	
	public void actualizarProcesoJudicial(int idupa, Boolean siprocesojudicial ) {
		String updateQuery="UPDATE UPA SET SIPROCESOJUDICIAL='"+siprocesojudicial+"'WHERE IDUPA='"+idupa+"'";
		
		getSessionFactory().getCurrentSession().createSQLQuery(updateQuery).executeUpdate();
		
	}
	public void actualizarProcesoJudicial(int idupa, Boolean siprocesojudicial,Procesojudicialupa procesojudicial ) {
		
		actualizarProcesoJudicial(idupa,siprocesojudicial );
		String updateQuery;
		if(siprocesojudicial.equals(false)){
			updateQuery="UPDATE PROCESOJUDICIALUPA SET ESTADO='"+procesojudicial.getEstado()+"', OBSERVACIONMODIFICACION='"+
					procesojudicial.getObservacionmodificacion()+"' ,usrmod='"+procesojudicial.getUsrmod()+"',fecmod=getdate() ,HOST_IP_USRMOD='"+procesojudicial.getHost_ip_usrmod()+"',SIFINALIZADO='"+procesojudicial.getSifinalizado()+"'"+
            " WHERE IDUPA='"+idupa+"' and estado='VIGENTE' ";	
		}else{
			updateQuery="UPDATE PROCESOJUDICIALUPA SET  OBSERVACIONMODIFICACION='"+
					procesojudicial.getObservacionmodificacion()+"',usrmod='"+procesojudicial.getUsrmod()+"',fecmod=getdate() ,HOST_IP_USRMOD='"+procesojudicial.getHost_ip_usrmod()+"' WHERE IDUPA='"+idupa+"' and estado='VIGENTE'";	
		}
			
		getSessionFactory().getCurrentSession().createSQLQuery(updateQuery).executeUpdate();
		
	}
	public void actualizarProcesoJudicialUpa(Procesojudicialupa procesojudicialupa) {
		String updateQuery="UPDATE PROCESOJUDICIALUPA SET ESTADO="+procesojudicialupa.getEstado()+" ,USRMOD='"+procesojudicialupa.getUsrmod()+
				"',FECMOD=GETDATE() ,HOST_IP_USRMOD='"+procesojudicialupa.getHost_ip_usrmod()+"' WHERE IDPROCESOJUDICIALUPA='"+procesojudicialupa.getIdprocesojudicialupa()+"'";
		
		getSessionFactory().getCurrentSession().createSQLQuery(updateQuery).executeUpdate();
	}
	
	@SuppressWarnings("unchecked")
	
	public List<Upa> listarUpasxClave(String valorbusqueda) {
		Session session = getSessionFactory().openSession();
		return session.createQuery(" select  u from Upa u  where  u.clave like '"+valorbusqueda+"%' ").list();
	}

	@SuppressWarnings("unchecked")
	
	public List<Upa> listarUpasxDireccion(String valorbusqueda) {
		
		Session session = getSessionFactory().openSession();
		return session.createQuery(" select  u from Upa u left join fetch u.inmueble i left join fetch i.ubigeo ub  where  i.direccion like '%"+valorbusqueda+"%' ").list();
	}

	@SuppressWarnings("unchecked")
	
	public List<Upa> listarUpasxCondicion(String valor1,String valor2,String valor3,String valor4) {
		Session session = getSessionFactory().openSession();
		String query1="",query2="",query3="",query4="";
		if(valor1!=null){
			if(valor1.length()>0){
				query1=query1+valor1.trim();
			}
		}
		if(valor2!=null){
			if(valor2.length()>0){
				query2=query2+valor2.trim();
			}
		}
		if(valor3!=null){
			if(valor3.length()>0){
				query3=query3+valor3.trim();
			}
		}
		if(valor4!=null){
			if(valor4.length()>0){
				query4=query4+valor4.trim();
			}
		}
		
		return session.createQuery(" select  u from Upa u  where  u.clave like '%"+query1+"%' and u.direccion like '%"+query2+"%' and u.numprincipal like '%"+query3+"%' and u.nombrenuminterior like '%"+query4+"%' order by u.clave asc").list();
	}
	/*Consulta UPA*/
	@SuppressWarnings("unchecked")
	@Override
	public List<Upa> listarUpasxConsulta(Upa upa, UpaBean upaBean,String ubigeo) {
		Session session = getSessionFactory().openSession();
		StringBuilder hql = new StringBuilder();
		String query="",query1="",query2="",query3="",query4="",query5="",query6="",query7="",query8="",query9="";
		query="select  u from Upa u where 1=1 ";
		if(upa.getClave()!=null){
			if(upa.getClave().length()>0){
				query1=" and u.clave like '%"+upa.getClave().trim()+"%' ";
				query=query+query1;
			}
		}
		if(upa.getDireccion()!=null){
			if(upa.getDireccion().length()>0){
				query2=" and u.direccion like '%"+upa.getDireccion().trim()+"%' ";
				query=query+query2;
			}
		}
		if(upa.getNumprincipal()!=null){
			if(upa.getNumprincipal().length()>0){
				query3=" and u.numprincipal like '%"+upa.getNumprincipal().trim()+"%' ";
				query=query+query3;
			}
		}
		if(upa.getNombrenuminterior() !=null){
			if(upa.getNombrenuminterior().length()>0){
				query4=" and u.nombrenuminterior like '%"+upa.getNombrenuminterior().trim()+"%' ";
				query=query+query4; 
			}
		}
		if(ubigeo!=null ){
			if(ubigeo.trim().length()>0){
				query5=" and u.inmueble.ubigeo.ubigeo like '" + ubigeo+"' ";
				query=query+query5;
			}			
		}
		if(upa.getTugurizadoruinoso()!=null){
			if(upa.getTugurizadoruinoso().length()>0){
				query6=" and u.tugurizadoruinoso like '"+upa.getTugurizadoruinoso()+"' ";
				query=query+query6;
			}
		}
		if(upa.getUso()!=null){
			if(upa.getUso().length()>0){
				query7=" and u.uso like '"+upa.getUso()+"' ";
				query=query+query7;
			}
		}
		if(upaBean.getDescripciontitularidad()!=null){
			if(upaBean.getDescripciontitularidad().length()>0){
				query8=" and u.inmueblemaestro.titularidad.tipotitularidad.descripcion like '"+upaBean.getDescripciontitularidad()+"' ";
				query=query+query8;
			}
		}
		if(upaBean.getSimonumentoHistorico()!=null){
			if(upaBean.getSimonumentoHistorico().length()>0){
			query9=" and u.inmueblemaestro.simonumentohistorico='"+upaBean.getSimonumentoHistorico()+"'";
			query=query+query9;
			}
		}
		return session.createQuery(query).list();
		//return session.createQuery(" select  u from Upa u  where  u.clave like '%"+query1+"%' and u.direccion like '%"+query2+"%' and u.numprincipal like '%"+query3+"%' and u.nombrenuminterior like '%"+query4+"%' and u.inmueble.ubigeo.ubigeo like '%"+query5+"%' and u.tugurizadoruinoso like '%"+query6+"%' and u.uso like '%"+query7+"%'  and u.inmueblemaestro.titularidad.tipotitularidad.descripcion like '%"+upaBean.getDescripciontitularidad()+"%' order by u.clave asc").list();
	
	}
	public void grabarEventoSeguimiento(Procesojudicialupa segupa) {
		getSessionFactory().getCurrentSession().saveOrUpdate(segupa);		
	}

	@SuppressWarnings("unchecked")
	
	public List<Procesojudicialupa> listarSegUpa(int idupa) {
		return (List<Procesojudicialupa>) getSessionFactory().getCurrentSession().createQuery("select segupa from Procesojudicialupa segupa left join fetch segupa.upa u where u.idupa='"+idupa+"' order by segupa.feccre desc").list();
	 }

	@SuppressWarnings("unchecked")
	
	public List<UpaBean> listarUpasBean(String valorbusquedaUpa,String tipoBusquedaUpa) {
		StringBuilder hql = new StringBuilder();
		
		hql.append("select u.idupa as idupa,u.clave as clave,u.uso as tipouso,u.usrcre as usrcre,u.feccre as feccre,u.fecmod as fecmod,u.usrmod as usrmod,");
		hql.append("u.numprincipal as numeroprincipal,u.direccion as direccionprincipal,u.nombrenuminterior as numerointerior,u.direccion+' '+u.numprincipal as direccion, ");	
		hql.append("u.direccion as direccionprincipal,ubi.dist as dist, ubi.prov as prov ");
		hql.append(" from Upa u ");
		hql.append("left join u.inmueblemaestro inm ");
		hql.append("left join inm.ubigeo ubi ");
		
		
		if (tipoBusquedaUpa.equals(Constantes.BUSQUEDA_DIRECCION)) {
			hql.append("where  u.direccion like '%"+valorbusquedaUpa+"%'");
		} else {
			hql.append("where  u.clave like '%"+valorbusquedaUpa+"%'");
		}
		
		hql.append(" order by u.direccion asc,u.numprincipal asc,u.nombrenuminterior asc");

		
	
		Query query= sessionFactory.getCurrentSession().createQuery(hql.toString());
		

		return  (List<UpaBean>) query.setResultTransformer(Transformers.aliasToBean(UpaBean.class)).list();

	}
	@SuppressWarnings("unchecked")
	
	public List<UpaBean> listarUpasBean(String valorbusquedaUpa,String tipoBusquedaUpa,Upa upa) {
		StringBuilder hql = new StringBuilder();
		
		hql.append("select u.idupa as idupa,u.clave as clave,u.uso as tipouso,u.usrcre as usrcre,u.feccre as feccre,u.fecmod as fecmod,u.usrmod as usrmod,");
		hql.append("inm.numeroprincipal as numeroprincipal,inm.direccion as direccionprincipal,u.nombrenuminterior as numerointerior,u.direccion+' '+u.numprincipal as direccion, ");	
		hql.append("inm.direccion as direccionprincipal,ubi.dist as dist, ubi.prov as prov , u.siprocesojudicial as siprocesojudicial ");
		hql.append(" from Upa u ");
		hql.append("inner join u.inmueble inm ");
		hql.append("inner join inm.ubigeo ubi ");
		
		
		if (tipoBusquedaUpa.equals(Constantes.BUSQUEDA_DIRECCION)) {
			hql.append("where  inm.direccion like '%"+valorbusquedaUpa+"%'");
		} else {
			hql.append("where  u.clave like '%"+valorbusquedaUpa+"%'");
		}
		if(upa !=null){
			if(upa.getSiprocesojudicial()){
			hql.append(" and u.siprocesojudicial=1");
			}
			
		}
		
		hql.append(" order by inm.direccion asc,inm.numeroprincipal asc,u.nombrenuminterior asc");

		
	
		Query query= sessionFactory.getCurrentSession().createQuery(hql.toString());
		

		return  (List<UpaBean>) query.setResultTransformer(Transformers.aliasToBean(UpaBean.class)).list();

	}
	@SuppressWarnings("unchecked")
	
	@Transactional(readOnly=true)
	public List<Upa> listarUpasActivas(){
		StringBuilder hql = new StringBuilder();
		//Session session = getSessionFactory().openSession();
		hql.append("select u from Upa u where u.estado <> '1' and u.clave is  not null order by u.clave asc");
		return	getSessionFactory().getCurrentSession().createQuery(hql.toString()).list();
//		List<Object> objectList = sql.list();
//		Iterator<Object> iterator =objectList.iterator();
//		List<Upa> listUpa= new ArrayList<Upa>();
//		while(iterator.hasNext()){
//			Object []obj=(Object[])iterator.next();
//			
//			Upa u = new Upa();
//			u.setIdupa((Integer) obj[0]);
//			u.setClave((String) obj[1]);
//			listUpa.add(u);
//		}
//		return listUpa;
	}
	public Upa obtenerUpaPorId(int idupa) {
		return (Upa) getSessionFactory().getCurrentSession().get(Upa.class, idupa);
	}
    public ProcesojudicialupaBean obtenerProcesoJudicialUpaxCondicion(int idprocesojudicialupa){
    	StringBuilder hql = new StringBuilder();
    	hql.append("select p.idprocesojudicialupa,u.idupa as idupa ,p.expediente as expediente,p.estado as estado , p.observacion as observacion, ");
    	hql.append("p.usrcre,p.feccre,p.usrmod,p.fecmod from Procesojudicialupa p ");
    	hql.append("inner join p.upa u");
    	hql.append("where p.idprocesojudicialupa="+idprocesojudicialupa);
    	Query query=sessionFactory.getCurrentSession().createQuery(hql.toString());
    	return (ProcesojudicialupaBean)query.setResultTransformer(Transformers.aliasToBean(ProcesojudicialupaBean.class)).uniqueResult();
    }



}
