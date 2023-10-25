package pe.gob.sblm.sgi.daoImpl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import pe.gob.sblm.api.commons.constants.sgi.Constantes;
import pe.gob.sblm.api.commons.utility.FuncionesHelper;
import pe.gob.sblm.sgi.bean.ArchivoAdjuntoBean;
import pe.gob.sblm.sgi.dao.AdministrarArchivoDAO;

@Repository(value = "administrarArchivoDAO")
public class AdministrarArchivoDAOImpl implements AdministrarArchivoDAO,Serializable{

	private static final long serialVersionUID = 1L;
   
	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<ArchivoAdjuntoBean> obtenerArchivosReferencia(int identificador,String nombreEntidad) {
		StringBuilder hql = new StringBuilder();	
		List<ArchivoAdjuntoBean> lista= new ArrayList<ArchivoAdjuntoBean>();
		hql.append("select repo.titulo as titulo,repo.descripcion as descripcion,repo.observacion as observacion,repo.tipo as tipo,repo.uid as uuidSftp,repo.uidAlfresco as uuidAlfresco,repo.peso as peso,");
		hql.append("repo.usuariocreador as usrcre,repo.fechacreacion as feccre, repo.nombre as nombre ");
		hql.append(" from Archivo repo ");
		
		if (nombreEntidad.equals(Constantes.INMUEBLE_SGI)) {			
			hql.append("inner join repo.inmueble inm ");
			hql.append("where inm.idinmueble='"+identificador+"' order by repo.fechacreacion desc");
		}else if (nombreEntidad.equals(Constantes.CONDICION_SGI)){
			hql.append("inner join repo.contrato c ");
			hql.append("where c.idcontrato='"+identificador+"' order by repo.fechacreacion desc");
		}else if(nombreEntidad.equals(Constantes.PROCESOJUDICIALUPA_SGI)){
			hql.append("inner join repo.procesojudicialupa pro ");
			hql.append("where pro.idprocesojudicialupa='"+identificador+"' order by repo.fechacreacion desc");
		}else if(nombreEntidad.equals(Constantes.UPA_SGI)){
			hql.append("inner join repo.upa up ");
			hql.append("where up.idupa='"+identificador+"' order by repo.fechacreacion desc");
		}else if(nombreEntidad.equals(Constantes.COMPROBANTEPAGO)){
			hql.append("inner join repo.comprobantepago cp ");
			hql.append("where cp.idcomprobante='"+identificador+"' order by repo.fechacreacion desc");
		}
		
		
		Query query= sessionFactory.getCurrentSession().createQuery(hql.toString());
		
		lista=query.setResultTransformer(Transformers.aliasToBean(ArchivoAdjuntoBean.class)).list();
		
		return lista;
	}
 
	public void actualizarestadodocumentocontrato(int idcontrato){
		String updateQuery="UPDATE CONTRATO SET SIDOCUMENTO='1' WHERE IDCONTRATO='"+idcontrato+"'";
		getSessionFactory().getCurrentSession().createSQLQuery(updateQuery).executeUpdate();
	}
	
	public void actualizarEstadoxDocumentoReferencia(String entidad, Integer id){
		String updateQuery="";
		if (entidad.equals(Constantes.INMUEBLE_SGI)) {
			updateQuery="UPDATE INMUEBLEMAESTRO SET SIDOCUMENTO='1' WHERE IDINMUEBLE='"+id+"'";
		}else if (entidad.equals(Constantes.CONDICION_SGI)){
			updateQuery="UPDATE CONTRATO SET SIDOCUMENTO='1' WHERE IDCONTRATO='"+id+"'";
		}else if(entidad.equals(Constantes.PROCESOJUDICIALUPA_SGI)){
			updateQuery="UPDATE PROCESOJUDICIALUPA SET SIDOCUMENTO='1' WHERE IDPROCESOJUDICIALUPA='"+id+"'";
//		}else if(entidad.equals(Constantes.UPA_SGI)){
//			updateQuery="UPDATE UPA SET SIDOCUMENTO='1' WHERE IDUPA='"+id+"'";
//		}else if(entidad.equals(Constantes.COMPROBANTEPAGO)){
//			updateQuery="UPDATE COMPROBANTEPAGO SET SIDOCUMENTO='1' WHERE IDCOMPROBANTE='"+id+"'";
//		}else if(entidad.equals(Constantes.INQUILINO_GI)){
//			updateQuery="UPDATE INQUILINO SET SIDOCUMENTO='1' WHERE IDINQUILINO='"+id+"'";
//		}
		}
		getSessionFactory().getCurrentSession().createSQLQuery(updateQuery).executeUpdate();
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
