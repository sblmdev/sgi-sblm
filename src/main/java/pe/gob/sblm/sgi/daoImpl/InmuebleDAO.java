package pe.gob.sblm.sgi.daoImpl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import pe.gob.sblm.api.commons.constants.sgi.Constantes;
import pe.gob.sblm.api.commons.utility.StringUtil;
import pe.gob.sblm.sgi.bean.CondicionBean;
import pe.gob.sblm.sgi.bean.InmuebleBean;
import pe.gob.sblm.sgi.dao.IInmuebleDAO;
import pe.gob.sblm.sgi.entity.Inmueble;
import pe.gob.sblm.sgi.entity.Inmueblemaestro;
import pe.gob.sblm.sgi.entity.Inmueblesaneamiento;
import pe.gob.sblm.sgi.entity.Materialpredominante;
import pe.gob.sblm.sgi.entity.Origendominio;
import pe.gob.sblm.sgi.entity.Saneamiento;
import pe.gob.sblm.sgi.entity.Tipotitularidad;
import pe.gob.sblm.sgi.entity.Tipovia;
import pe.gob.sblm.sgi.entity.Tipozona;
import pe.gob.sblm.sgi.entity.Titularidad;

@Repository(value = "inmuebleDAO")
public class InmuebleDAO implements IInmuebleDAO, Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private SessionFactory sessionFactory;

	Session sesion = null;

	
	public void registrarInmueble(Inmueble inmueble) {
		getSessionFactory().getCurrentSession().merge(inmueble);	
	}

	
	public void actualizarInmueble(Inmueble inmueble) {

	}

	
	public void eliminarInmueble(Inmueble inmueble) {
			//getSessionFactory().getCurrentSession().createSQLQuery("delete from Inmueble WHERE idinmueble='"+ inmueble.getIdinmueble() + "'").executeUpdate();
	          getSessionFactory().getCurrentSession().createSQLQuery("update INMUEBLEMAESTRO SET ESTADO=0 WHERE  idinmueble='"+ inmueble.getIdinmueble() + "'").executeUpdate();
	}
	
	@SuppressWarnings("unchecked")
	public List<Tipovia> listarTipoVia() {
			return getSessionFactory().getCurrentSession().createQuery("from Tipovia order by descripcion ").list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Origendominio> listarOrigenDominio() {
			return getSessionFactory().getCurrentSession().createQuery("from Origendominio order by descripcion ").list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Tipozona> listarTipoZona() {
		return getSessionFactory().getCurrentSession().createQuery("from Tipozona order by descripcion ").list();
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Materialpredominante> listarMaterialPredominante() {
		return getSessionFactory().getCurrentSession().createQuery("from Materialpredominante order by descripcion ").list();
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
			System.out.println("error entró 1 ---- ");
			System.out.println("Departamento: '"+dpto+"'");
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
	

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@SuppressWarnings("unchecked")
	
	public List<InmuebleBean> buscarInmuebles(String valorbusqueda,String tipobusqueda) {
		StringBuilder hql = new StringBuilder();

		hql.append("select inm.idinmueble as idinmueble, inm.clave as clave, ");
		hql.append("inm.codigoinmueble as codigoinmueble,titu.descripcion as codigotitularidad,titu.idtitularidad as idtitularidad,");
		hql.append("titu.nombre as nombretitularidad,tiptitu.descripcion as tipotitularidad,");
		hql.append("inm.numregistrosbn as numregistrosbn,");
		hql.append("ubi.dist as distrito,ubi.prov as provincia,ubi.depa as departamento,tv.idtipovia as idtipovia,inm.nombretipovia as nombretipovia,inm.direccionreferencia as referenciadireccion,");
		
		hql.append("od.idorigendominio as idorigendominio, ");
		
		hql.append("inm.nombretipozona as nombretipozona,tz.idtipozona as idtipozona,");
		hql.append("inm.direccionprincipal as direccionprincipal,inm.direccion as direccion,ubi.ubigeo as idubigeo,");
		hql.append("inm.simonumentohistorico as simonumentohistorico,inm.resolucionmonumentohistorico as resolucionmonumentohistorico,inm.fecharesolucionmonumentohistorico as fecresolucionmonumentohistorico,inm.tipomonumentohistorico as tipomonumentohistorico,");
		hql.append("inm.codigopredio as codigopredio,inm.fechainscripcionregistral as fecinscripcionregistral,inm.asientoregistral as asientoregistral,inm.fojasregistral as fojasregistral,inm.tomoregistral as tomoregistral,inm.ficharegistral as ficharegistral,inm.partidaelectronicaregistral as partidaelectronicaregistral,inm.observacionregistral as observacionregistral,");
		hql.append("inm.sideclaratoriafabrica as sideclaratoriafabrica,inm.asientofabrica as asientofabrica,inm.fojasfabrica as fojasfabrica,inm.tomofabrica as tomofabrica,inm.fichafabrica as fichafabrica,inm.partidaelectronicafabrica as partidaelectronicafabrica,inm.observacionfabrica as observacionfabrica,");
		hql.append("inm.siindependizacion  as siindependizacion,inm.unidadesinmobiliariasindependizacion as unidadesinmobiliariasindependizacion,inm.asientoindependizacion as asientoindependizacion,inm.fojasindependizacion as fojasindependizacion,inm.tomoindependizacion as tomoindependizacion,inm.fichaindependizacion as fichaindependizacion,inm.partidaelectronicaindependizacion as partidaelectronicaindependizacion,inm.fechainscripcionindependizacion as fechainscripcionindependizacion, inm.observacionindependizacion as observacionindependizacion,");
		hql.append("inm.sigravamen as sigravamen,inm.afavordegravamen as afavordegravamen,inm.asientogravamen as asientogravamen,inm.fojasgravamen as fojasgravamen,inm.tomogravamen as tomogravamen,inm.fichagravamen as fichagravamen,inm.partidaelectronicagravamen as partidaelectronicagravamen,inm.fechainscripciongravamen as fechainscripciongravamen,inm.observaciongravamen as observaciongravamen,");
		hql.append("inm.simandascargas as simandascargas,inm.afavordemandascargas as afavordemandascargas,inm.asientomandascargas as asientomandascargas,inm.fojasmandascargas as fojasmandascargas,inm.tomomandascargas as tomomandascargas,inm.fichamandascargas as fichamandascargas,inm.partidaelectronicamandascargas as partidaelectronicamandascargas,inm.fechainscripcionmandascargas as fechainscripcionmandascargas,inm.observacionmandascargas as observacionmandascargas,");
		hql.append("inm.sisaneado as sisaneado,inm.observacionsaneado as observacionsaneado,matpre.idmaterialpredominante as idmaterialpredominante,inm.anioconstruccion as anioconstruccion,inm.numpisos as nropisos,inm.areaterreno as areaterreno,inm.areaconstruida as areaconstruida,");
		hql.append("inm.sipartidaregistral as sipartidaregistral,inm.siplanoubicacion as siplanoubicacion,inm.simemoriadescrip as simemoriadescriptiva,inm.sifotos as sifotografias,inm.sitasacion as sitasacion,inm.fechatasacion as fectasacion,");
		
		hql.append("inm.latitudlocalizacion as latitudlocalizacion,inm.longitudlocalizacion as longitudlocalizacion,inm.descripcionlocalizacion as descripcionlocalizacion,inm.sidocumento as sidocumento ,");
		hql.append("inm.usrcre as usrcre,inm.usrmod as usrmod,inm.fechcre as feccre,inm.fechmod as fecmod ");
		
		
		
		hql.append("from Inmueblemaestro inm ");
		hql.append(" inner join inm.ubigeo ubi ");
		hql.append(" inner join inm.tipovia tv ");
		
		hql.append(" inner join inm.origendominio od ");
		
		hql.append(" left join inm.tipozona tz");
		hql.append(" inner join inm.titularidad titu");
		hql.append(" inner join titu.tipotitularidad tiptitu");
		hql.append(" left join inm.materialpredominante matpre");
		
		
		if (tipobusqueda.equals(Constantes.BUSQUEDA_DIRECCION)) {
			hql.append(" where inm.direccion like '%"+valorbusqueda+"%' ");
		} else {
			hql.append(" where inm.clave like '%"+valorbusqueda+"%' ");
		}
		
		
		
		hql.append(" order by inm.direccionprincipal asc,tv.idtipovia");
		
		Query query= sessionFactory.getCurrentSession().createQuery(hql.toString());
		return (List<InmuebleBean>) query.setResultTransformer(Transformers.aliasToBean(InmuebleBean.class)).list();
	}

@SuppressWarnings("unchecked")
	
	public List<InmuebleBean> obtenerCondicion() {
		List<InmuebleBean> lista = new ArrayList<InmuebleBean>();
		StringBuilder hql = new StringBuilder();
		hql.append("select inm.idinmueble as idinmueble,inm.clave as clave,");
		hql.append("inm.codigoinmueble as codigoinmueble,titu.descripcion as codigotitularidad,titu.idtitularidad as idtitularidad,");
		hql.append("titu.nombre as nombretitularidad,tiptitu.descripcion as tipotitularidad,");
		hql.append("inm.numregistrosbn as numregistrosbn,");
		hql.append("ubi.dist as distrito,ubi.prov as provincia,ubi.depa as departamento,tv.idtipovia as idtipovia,inm.nombretipovia as nombretipovia,inm.direccionreferencia as referenciadireccion,");
		
		hql.append("od.idorigendominio as idorigendominio,od.descripcion as descripcionorigendominio, ");
		
		hql.append("inm.nombretipozona as nombretipozona,tz.idtipozona as idtipozona,");
		hql.append("inm.direccionprincipal as direccionprincipal,inm.direccion as direccion,ubi.ubigeo as idubigeo,");
		hql.append("inm.simonumentohistorico as simonumentohistorico,inm.resolucionmonumentohistorico as resolucionmonumentohistorico,inm.fecharesolucionmonumentohistorico as fecresolucionmonumentohistorico,inm.tipomonumentohistorico as tipomonumentohistorico,");
		hql.append("inm.codigopredio as codigopredio,inm.fechainscripcionregistral as fecinscripcionregistral,inm.asientoregistral as asientoregistral,inm.fojasregistral as fojasregistral,inm.tomoregistral as tomoregistral,inm.ficharegistral as ficharegistral,inm.partidaelectronicaregistral as partidaelectronicaregistral,inm.observacionregistral as observacionregistral,");
		hql.append("inm.sideclaratoriafabrica as sideclaratoriafabrica,inm.asientofabrica as asientofabrica,inm.fojasfabrica as fojasfabrica,inm.tomofabrica as tomofabrica,inm.fichafabrica as fichafabrica,inm.partidaelectronicafabrica as partidaelectronicafabrica,inm.observacionfabrica as observacionfabrica,");
		hql.append("inm.siindependizacion  as siindependizacion,inm.unidadesinmobiliariasindependizacion as unidadesinmobiliariasindependizacion,inm.asientoindependizacion as asientoindependizacion,inm.fojasindependizacion as fojasindependizacion,inm.tomoindependizacion as tomoindependizacion,inm.fichaindependizacion as fichaindependizacion,inm.partidaelectronicaindependizacion as partidaelectronicaindependizacion,inm.fechainscripcionindependizacion as fechainscripcionindependizacion, inm.observacionindependizacion as observacionindependizacion,");
		hql.append("inm.sigravamen as sigravamen,inm.afavordegravamen as afavordegravamen,inm.asientogravamen as asientogravamen,inm.fojasgravamen as fojasgravamen,inm.tomogravamen as tomogravamen,inm.fichagravamen as fichagravamen,inm.partidaelectronicagravamen as partidaelectronicagravamen,inm.fechainscripciongravamen as fechainscripciongravamen,inm.observaciongravamen as observaciongravamen,");
		hql.append("inm.simandascargas as simandascargas,inm.afavordemandascargas as afavordemandascargas,inm.asientomandascargas as asientomandascargas,inm.fojasmandascargas as fojasmandascargas,inm.tomomandascargas as tomomandascargas,inm.fichamandascargas as fichamandascargas,inm.partidaelectronicamandascargas as partidaelectronicamandascargas,inm.fechainscripcionmandascargas as fechainscripcionmandascargas,inm.observacionmandascargas as observacionmandascargas,");
		hql.append("inm.sisaneado as sisaneado,inm.observacionsaneado as observacionsaneado,matpre.idmaterialpredominante as idmaterialpredominante,inm.anioconstruccion as anioconstruccion,inm.numpisos as nropisos,inm.areaterreno as areaterreno,inm.areaconstruida as areaconstruida,");
		hql.append("inm.sipartidaregistral as sipartidaregistral,inm.siplanoubicacion as siplanoubicacion,inm.simemoriadescrip as simemoriadescriptiva,inm.sifotos as sifotografias,inm.sitasacion as sitasacion,inm.fechatasacion as fectasacion,");
		
		hql.append("inm.latitudlocalizacion as latitudlocalizacion,inm.longitudlocalizacion as longitudlocalizacion,inm.descripcionlocalizacion as descripcionlocalizacion,inm.condicion as condicion,");
		hql.append("inm.sidocumento as sidocumento,inm.usrcre as usrcre,inm.usrmod as usrmod,inm.fechcre as feccre,inm.fechmod as fecmod ");
		
		
		
		hql.append("from Inmueblemaestro inm ");
		hql.append(" inner join inm.ubigeo ubi ");
		hql.append(" inner join inm.tipovia tv ");
		
		hql.append(" inner join inm.origendominio od ");
		
		hql.append(" left join inm.tipozona tz");
		hql.append(" inner join inm.titularidad titu");
		hql.append(" inner join titu.tipotitularidad tiptitu");
		hql.append(" left join inm.materialpredominante matpre");
		hql.append(" order by inm.clave asc ");
		
		Query query= sessionFactory.getCurrentSession().createQuery(hql.toString());
		
		lista = query.setResultTransformer(Transformers.aliasToBean(InmuebleBean.class)).list();
		return lista;
	}
	
	@SuppressWarnings("unchecked")
	public List<Inmueble> buscarInmueble(String clave) {
		StringBuilder hql = new StringBuilder();

		hql.append("select inm.idinmueble as idinmueble,inm.clave as clave from Inmueble inm");
		hql.append(" where inm.clave like '"+ clave+"-000"+"%' ");
		Query query= sessionFactory.getCurrentSession().createQuery(hql.toString());
		System.out.println("query lista ="+query.list());
		return (List<Inmueble>) query.setResultTransformer(Transformers.aliasToBean(Inmueble.class)).list();
	}
	public String obtenerIdUbigeo(String depa, String prov, String dist) {
		return (String) getSessionFactory().getCurrentSession().createQuery(" select  ubigeo from Ubigeo  where depa='"+depa+"' and prov='"+prov+"' and dist='"+dist+"' ").uniqueResult();
	}


	@Override
	public void grabarLocalizacion(int idinmueble,String latitud, String longitud,String descripcion) {
		
		StringBuilder hql = new StringBuilder();
		hql.append("update Inmueblemaestro set ");
		hql.append("latitudlocalizacion = :latitud, ");
		hql.append("longitudlocalizacion = :longitud, ");
		hql.append("descripcionlocalizacion  = :descripcion ");
		hql.append("where idinmueble = :idinmueble");
		
		Query query = sessionFactory.getCurrentSession().createQuery(hql.toString());
		query.setParameter("idinmueble", idinmueble);
		query.setParameter("latitudlocalizacion", latitud);
		query.setParameter("longitudlocalizacion", longitud);
		query.setParameter("descripcionlocalizacion", descripcion);
		query.executeUpdate();		
	}



	@Override
	public Titularidad obtenerTitularidad(String titularidad) {
		StringBuilder hql = new StringBuilder();
		hql.append(" select t from Titularidad t");
		hql.append(" left join fetch t.tipotitularidad tt");
		hql.append(" where t.descripcion=:titularidad");
		
		Query query = sessionFactory.getCurrentSession().createQuery(hql.toString());
		query.setParameter("titularidad", titularidad);
		
		return (Titularidad) query.uniqueResult();
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<Saneamiento> listarCondicionesSaneamiento() {
		return getSessionFactory().getCurrentSession().createQuery("from Saneamiento order by idsaneamiento").list();
	}
	@SuppressWarnings("unchecked")


	public String obtenerSgtCodigoInmueble(String codigotitularidad) {
		StringBuffer hql= new StringBuffer();
		hql.append("select i.codigoinmueble from Inmueblemaestro i inner join  i.titularidad t where t.descripcion="+codigotitularidad+"  order by i.codigoinmueble desc");

		Query query=sessionFactory.getCurrentSession().createQuery(hql.toString());
		
		query.setMaxResults(1);
		
		if (query.list().size()!=0) {
			
			return StringUtil.completarCerosIzquierda(4,Integer.parseInt(query.uniqueResult().toString())+1);
		}else {
			return StringUtil.completarCerosIzquierda(4, 1);
		}
		
	}


	@SuppressWarnings("unchecked")
	public List<Inmueblesaneamiento> obtenerCondicionesActivasInmueble(int idinmueble) {
		return (List<Inmueblesaneamiento>)getSessionFactory().getCurrentSession().createQuery("select isa from Inmueblesaneamiento isa inner join isa.inmueble i inner join isa.saneamiento s where i.idinmueble='"+idinmueble+"'").list();
	}


	@Override
	public boolean siExisteClaveInmueble(String clave) {
		
		StringBuffer hql= new StringBuffer();
		hql.append("select i.idinmueble from Inmueblemaestro i where i.clave='"+clave+"'");

		Query query=sessionFactory.getCurrentSession().createQuery(hql.toString());
		
		query.setMaxResults(1);
		
		if (query.list().size()!=0) {
			
			return true;
		}else {
			return false;
		}
	}
	public List<String> listarTipoTitularidad() {
		Session session = getSessionFactory().openSession();

		try {
			return session.createQuery(" select  distinct descripcion from Tipotitularidad order by descripcion ")
					.list();
		} catch (HibernateException e) {
			System.out.println("error listar listarTipoTitularidad:::" + e.getMessage());
			throw e;
		} finally {
			session.close();
		}
	}
	public List<Tipotitularidad> listarTipotitularidad() {
		Session session = getSessionFactory().openSession();

		try {
			return session.createQuery(" select t from Tipotitularidad t order by t.descripcion ")
					.list();
		} catch (HibernateException e) {
			System.out.println("error listar listarTipoTitularidad:::" + e.getMessage());
			throw e;
		} finally {
			session.close();
		}
	}
	public List<InmuebleBean> listarInmueblematriz(String clave,String depa,String prov,String dist,String tipotitularidad){
		List<InmuebleBean> lista = new ArrayList<>();
		String query1="",query2="",query3="",query4="",query5="";
		if(clave!=null ){
			query1=" inm.clave like '"+clave+"%'";
		}else{
			query1=" inm.clave is not null";
		}
		if(depa!=null){
				query2=depa.length()>1? " and ubi.depa='"+depa+"' ":" and ubi.depa is not null";

		}else{
			query2=" and ubi.depa is not null";
		}
		if(prov!=null){
			query3=prov.length()>1? " and ubi.prov='"+prov+"' ":" and ubi.prov is not null";
		}else{
			query3=" and ubi.prov is not null";
		}
		if(dist!=null ){
			query4=dist.length()>1? " and ubi.dist='"+dist+"' ":" and ubi.dist is not null ";
		}else{
			query4=" and ubi.dist is not null ";
		}
		if(tipotitularidad!=null){
			query5=tipotitularidad.length()>1? " and tiptitu.descripcion='"+tipotitularidad+"' ":" and tiptitu.descripcion is not null ";
		}else{
			query5=" and tiptitu.descripcion is not null ";
		}
		StringBuilder hql = new StringBuilder();
		hql.append("select inm.idinmueble as idinmueble,inm.clave as clave,");
		hql.append("inm.codigoinmueble as codigoinmueble,titu.descripcion as codigotitularidad,titu.idtitularidad as idtitularidad,");
		hql.append("titu.nombre as nombretitularidad,tiptitu.descripcion as tipotitularidad,");
		hql.append("inm.numregistrosbn as numregistrosbn,");
		hql.append("ubi.dist as distrito,ubi.prov as provincia,ubi.depa as departamento,tv.idtipovia as idtipovia,inm.nombretipovia as nombretipovia,inm.direccionreferencia as referenciadireccion,");
		
		hql.append("od.idorigendominio as idorigendominio,od.descripcion as descripcionorigendominio, ");
		
		hql.append("inm.nombretipozona as nombretipozona,tz.idtipozona as idtipozona,");
		hql.append("inm.direccionprincipal as direccionprincipal,inm.direccion as direccion,ubi.ubigeo as idubigeo,");
		hql.append("inm.simonumentohistorico as simonumentohistorico,inm.resolucionmonumentohistorico as resolucionmonumentohistorico,inm.fecharesolucionmonumentohistorico as fecresolucionmonumentohistorico,inm.tipomonumentohistorico as tipomonumentohistorico,");
		hql.append("inm.codigopredio as codigopredio,inm.fechainscripcionregistral as fecinscripcionregistral,inm.asientoregistral as asientoregistral,inm.fojasregistral as fojasregistral,inm.tomoregistral as tomoregistral,inm.ficharegistral as ficharegistral,inm.partidaelectronicaregistral as partidaelectronicaregistral,inm.observacionregistral as observacionregistral,");
		hql.append("inm.sideclaratoriafabrica as sideclaratoriafabrica,inm.asientofabrica as asientofabrica,inm.fojasfabrica as fojasfabrica,inm.tomofabrica as tomofabrica,inm.fichafabrica as fichafabrica,inm.partidaelectronicafabrica as partidaelectronicafabrica,inm.observacionfabrica as observacionfabrica,");
		hql.append("inm.siindependizacion  as siindependizacion,inm.unidadesinmobiliariasindependizacion as unidadesinmobiliariasindependizacion,inm.asientoindependizacion as asientoindependizacion,inm.fojasindependizacion as fojasindependizacion,inm.tomoindependizacion as tomoindependizacion,inm.fichaindependizacion as fichaindependizacion,inm.partidaelectronicaindependizacion as partidaelectronicaindependizacion,inm.fechainscripcionindependizacion as fechainscripcionindependizacion, inm.observacionindependizacion as observacionindependizacion,");
		hql.append("inm.sigravamen as sigravamen,inm.afavordegravamen as afavordegravamen,inm.asientogravamen as asientogravamen,inm.fojasgravamen as fojasgravamen,inm.tomogravamen as tomogravamen,inm.fichagravamen as fichagravamen,inm.partidaelectronicagravamen as partidaelectronicagravamen,inm.fechainscripciongravamen as fechainscripciongravamen,inm.observaciongravamen as observaciongravamen,");
		hql.append("inm.simandascargas as simandascargas,inm.afavordemandascargas as afavordemandascargas,inm.asientomandascargas as asientomandascargas,inm.fojasmandascargas as fojasmandascargas,inm.tomomandascargas as tomomandascargas,inm.fichamandascargas as fichamandascargas,inm.partidaelectronicamandascargas as partidaelectronicamandascargas,inm.fechainscripcionmandascargas as fechainscripcionmandascargas,inm.observacionmandascargas as observacionmandascargas,");
		hql.append("inm.sisaneado as sisaneado,inm.observacionsaneado as observacionsaneado,matpre.idmaterialpredominante as idmaterialpredominante,inm.anioconstruccion as anioconstruccion,inm.numpisos as nropisos,inm.areaterreno as areaterreno,inm.areaconstruida as areaconstruida,");
		hql.append("inm.sipartidaregistral as sipartidaregistral,inm.siplanoubicacion as siplanoubicacion,inm.simemoriadescrip as simemoriadescriptiva,inm.sifotos as sifotografias,inm.sitasacion as sitasacion,inm.fechatasacion as fectasacion,");
		
		hql.append("inm.latitudlocalizacion as latitudlocalizacion,inm.longitudlocalizacion as longitudlocalizacion,inm.descripcionlocalizacion as descripcionlocalizacion,inm.condicion as condicion,");
		hql.append("inm.sidocumento as sidocumento,inm.usrcre as usrcre,inm.usrmod as usrmod,inm.fechcre as feccre,inm.fechmod as fecmod ");
		
		
		
		hql.append("from Inmueblemaestro inm ");
		hql.append(" inner join inm.ubigeo ubi ");
		hql.append(" inner join inm.tipovia tv ");
		
		hql.append(" inner join inm.origendominio od ");
		
		hql.append(" left join inm.tipozona tz");
		hql.append(" inner join inm.titularidad titu");
		hql.append(" inner join titu.tipotitularidad tiptitu");
		hql.append(" left join inm.materialpredominante matpre");
		hql.append(" where 1=1 and "+query1+query2+query3+query4+query5+" and 1=1 ");
		hql.append(" order by inm.clave asc ");
		
		Query query= sessionFactory.getCurrentSession().createQuery(hql.toString());
		
		lista = query.setResultTransformer(Transformers.aliasToBean(InmuebleBean.class)).list();
		return lista;

	}
	public List<Inmueblemaestro> listarInmuebleMaestro(){
		return (List<Inmueblemaestro>)getSessionFactory().getCurrentSession().createQuery("select inm from Inmueblemaestro inm  where inm.clave is not null order by inm.clave asc").list();

	}
	@SuppressWarnings("unchecked")
	public List<InmuebleBean> listarInmuebleMaestro(InmuebleBean inm){
		List<InmuebleBean> lista = new ArrayList<>();
		String query="",query1="",query2="",query3="",query4="",query5="",query6="",query7="",query8="";
		//query="select inm from Inmueblemaestro inm  where 1=1 ";
		
		if(inm.getClave()!=null){
			if(inm.getClave().length()>0){
				query1="and inm.clave='"+inm.getClave()+"' ";
				query=query+query1;
			}
			
		}
		if(inm.getCodigopredio()!=null){
			if(inm.getCodigopredio().length()>0){
				query2="and inm.codigopredio LIKE '%"+inm.getCodigopredio()+"%' ";
				query=query+query2;
			}
		}
		if(inm.getDireccion()!=null){
			if(inm.getDireccion().length()>0){
				query3="and inm.direccion like '%"+inm.getDireccion()+"%' ";
				query=query+query3;
			}
		}
		if(inm.getDireccionprincipal()!=null){
			if(inm.getDireccionprincipal().length()>0){
				query4="and inm.direccionprincipal like '%"+inm.getDireccionprincipal()+"%' ";
				query=query+query4;
			}
		}
		if(inm.getIdtipozona()!=null){
			if(inm.getIdtipozona()>0){
				query5="and inm.tipozona.idtipozona='"+inm.getIdtipozona()+"' ";
				query=query+query5;
			}
		}

		if(inm.getUbigeo()!=null){
			if(inm.getUbigeo().length()>0){
				query6="and inm.ubigeo.ubigeo='"+inm.getUbigeo()+"' ";
				query=query+query6;
			}
		}
		if(inm.getSimonumentoHistorico()!=null){
			if(inm.getSimonumentoHistorico().length()>0){
				query7="and inm.simonumentohistorico='"+inm.getSimonumentoHistorico()+"' ";
				query=query+query7;
			}
		}
		if(inm.getTipotitularidad()!=null){
			if(inm.getTipotitularidad().length()>0){
				query8="and inm.titularidad.tipotitularidad.descripcion = '"+inm.getTipotitularidad()+"'";
				query=query+ query8;
			}
		}
		//query=query+ " and inm.clave is not null order by inm.clave asc";
		
		StringBuilder hql = new StringBuilder();
		hql.append("select inm.idinmueble as idinmueble,inm.clave as clave,");
		hql.append("inm.codigoinmueble as codigoinmueble,titu.descripcion as codigotitularidad,titu.idtitularidad as idtitularidad,");
		hql.append("titu.nombre as nombretitularidad,tiptitu.descripcion as tipotitularidad,");
		hql.append("inm.numregistrosbn as numregistrosbn,");
		hql.append("ubi.dist as distrito,ubi.prov as provincia,ubi.depa as departamento,tv.idtipovia as idtipovia,inm.nombretipovia as nombretipovia,inm.direccionreferencia as referenciadireccion,");
		
		hql.append("od.idorigendominio as idorigendominio,od.descripcion as descripcionorigendominio, ");
		
		hql.append("inm.nombretipozona as nombretipozona,tz.idtipozona as idtipozona,");
		hql.append("inm.direccionprincipal as direccionprincipal,inm.direccion as direccion,ubi.ubigeo as idubigeo,");
		hql.append("inm.simonumentohistorico as simonumentohistorico,inm.resolucionmonumentohistorico as resolucionmonumentohistorico,inm.fecharesolucionmonumentohistorico as fecresolucionmonumentohistorico,inm.tipomonumentohistorico as tipomonumentohistorico,");
		hql.append("inm.codigopredio as codigopredio,inm.fechainscripcionregistral as fecinscripcionregistral,inm.asientoregistral as asientoregistral,inm.fojasregistral as fojasregistral,inm.tomoregistral as tomoregistral,inm.ficharegistral as ficharegistral,inm.partidaelectronicaregistral as partidaelectronicaregistral,inm.observacionregistral as observacionregistral,");
		hql.append("inm.sideclaratoriafabrica as sideclaratoriafabrica,inm.asientofabrica as asientofabrica,inm.fojasfabrica as fojasfabrica,inm.tomofabrica as tomofabrica,inm.fichafabrica as fichafabrica,inm.partidaelectronicafabrica as partidaelectronicafabrica,inm.observacionfabrica as observacionfabrica,");
		hql.append("inm.siindependizacion  as siindependizacion,inm.unidadesinmobiliariasindependizacion as unidadesinmobiliariasindependizacion,inm.asientoindependizacion as asientoindependizacion,inm.fojasindependizacion as fojasindependizacion,inm.tomoindependizacion as tomoindependizacion,inm.fichaindependizacion as fichaindependizacion,inm.partidaelectronicaindependizacion as partidaelectronicaindependizacion,inm.fechainscripcionindependizacion as fechainscripcionindependizacion, inm.observacionindependizacion as observacionindependizacion,");
		hql.append("inm.sigravamen as sigravamen,inm.afavordegravamen as afavordegravamen,inm.asientogravamen as asientogravamen,inm.fojasgravamen as fojasgravamen,inm.tomogravamen as tomogravamen,inm.fichagravamen as fichagravamen,inm.partidaelectronicagravamen as partidaelectronicagravamen,inm.fechainscripciongravamen as fechainscripciongravamen,inm.observaciongravamen as observaciongravamen,");
		hql.append("inm.simandascargas as simandascargas,inm.afavordemandascargas as afavordemandascargas,inm.asientomandascargas as asientomandascargas,inm.fojasmandascargas as fojasmandascargas,inm.tomomandascargas as tomomandascargas,inm.fichamandascargas as fichamandascargas,inm.partidaelectronicamandascargas as partidaelectronicamandascargas,inm.fechainscripcionmandascargas as fechainscripcionmandascargas,inm.observacionmandascargas as observacionmandascargas,");
		hql.append("inm.sisaneado as sisaneado,inm.observacionsaneado as observacionsaneado,matpre.idmaterialpredominante as idmaterialpredominante,inm.anioconstruccion as anioconstruccion,inm.numpisos as nropisos,inm.areaterreno as areaterreno,inm.areaconstruida as areaconstruida,");
		hql.append("inm.sipartidaregistral as sipartidaregistral,inm.siplanoubicacion as siplanoubicacion,inm.simemoriadescrip as simemoriadescriptiva,inm.sifotos as sifotografias,inm.sitasacion as sitasacion,inm.fechatasacion as fectasacion,");
		
		hql.append("inm.latitudlocalizacion as latitudlocalizacion,inm.longitudlocalizacion as longitudlocalizacion,inm.descripcionlocalizacion as descripcionlocalizacion,inm.condicion as condicion,");
		hql.append("inm.sidocumento as sidocumento,inm.usrcre as usrcre,inm.usrmod as usrmod,inm.fechcre as feccre,inm.fechmod as fecmod ");
		
		
		
		hql.append("from Inmueblemaestro inm ");
		hql.append(" inner join inm.ubigeo ubi ");
		hql.append(" inner join inm.tipovia tv ");
		
		hql.append(" inner join inm.origendominio od ");
		
		hql.append(" left join inm.tipozona tz");
		hql.append(" inner join inm.titularidad titu");
		hql.append(" inner join titu.tipotitularidad tiptitu");
		hql.append(" left join inm.materialpredominante matpre");
		hql.append(" where 1=1 "+query+"  ");
		hql.append("  and inm.clave is not null order by inm.clave asc ");
			
		Query querys= sessionFactory.getCurrentSession().createQuery(hql.toString());
		
		lista = querys.setResultTransformer(Transformers.aliasToBean(InmuebleBean.class)).list();
		return lista;

	}



}
