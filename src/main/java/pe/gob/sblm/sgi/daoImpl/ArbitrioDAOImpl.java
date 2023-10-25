package pe.gob.sblm.sgi.daoImpl;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import pe.gob.sblm.sgi.bean.ArbitrioBean;
import pe.gob.sblm.sgi.bean.ComprobanteBean;
import pe.gob.sblm.sgi.bean.CondicionBean;
import pe.gob.sblm.sgi.bean.CuotaArbitrioBean;
import pe.gob.sblm.sgi.bean.PagoArbitrioBean;
import pe.gob.sblm.sgi.bean.PeriodoPagadoBean;
import pe.gob.sblm.sgi.dao.IArbitrioDAO;
import pe.gob.sblm.sgi.entity.Arbitrio;
import pe.gob.sblm.sgi.entity.Cuota_arbitrio;
import pe.gob.sblm.sgi.entity.Upa;

@Repository(value= "arbitrioDAO")
public class ArbitrioDAOImpl implements IArbitrioDAO,Serializable {


/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Autowired
	private SessionFactory sessionFactory;
	Session sesion = null;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
public Arbitrio obteneArbitrioPorClaveUpa(String clave) {
	
	return (Arbitrio)  getSessionFactory().getCurrentSession().createQuery(" select a from Arbitrio a inner join a.upa u where u.clave='"+clave+"' ").uniqueResult();
}
public Arbitrio obteneArbitrioPorId(int idarbitrio) {
	
	return (Arbitrio)  getSessionFactory().getCurrentSession().createQuery(" select a from Arbitrio a where a.idarbitrio='"+idarbitrio+"' ").uniqueResult();
}
public Cuota_arbitrio obtenerCuotaArbitrio(Integer idarbitrio,String mes) {
	
	return (Cuota_arbitrio)  getSessionFactory().getCurrentSession().createQuery(" select c from Cuota_arbitrio c where c.arbitrio.idarbitrio='"+idarbitrio+"' and c.mes='"+mes+"' ").uniqueResult();
}
public Cuota_arbitrio obtenerCuotaArbitrioxId(Integer idcuota_arbitrio) {
	
	return (Cuota_arbitrio)  getSessionFactory().getCurrentSession().createQuery(" select c from Cuota_arbitrio c where c.idcuota_arbitrio='"+idcuota_arbitrio+"' ").uniqueResult();
}
@SuppressWarnings("unchecked")
public List<ArbitrioBean> obtenerListaArbitrioBeanxUpa(String clave) {
	StringBuilder hql=new StringBuilder();
	hql.append("select a.idarbitrio as idarbitrio, u.clave as clave ,a.estado as estado ,a.direccion as direccion, ");
	hql.append(" a.total as total,a.periodo as periodo, a.trim1 as trim1,a.trim2 as trim2,a.trim3 as trim3,a.trim4 as trim4,a.feccre as feccre,a.usrcre as usrcre , a.fecmod as fecmod , a.usrmod as usrmod ");
	hql.append("from Arbitrio  a ");
	hql.append("inner join a.upa  u ");
	hql.append("where u.clave = '"+clave+"' order by a.periodo desc ");
	Query query= sessionFactory.getCurrentSession().createQuery(hql.toString());
	return (List<ArbitrioBean>) query.setResultTransformer(Transformers.aliasToBean(ArbitrioBean.class)).list();
}
public  List<ArbitrioBean> obtenerListaxCondicion(String estado, String sicancelado){
	List<ArbitrioBean> lista = new ArrayList<ArbitrioBean>();
	StringBuilder hql=new StringBuilder();
	hql.append("select a.idarbitrio as idarbitrio, u.clave as clave ,a.estado as estado ,a.direccion as direccion,a.observacion as observacion, ");
	hql.append(" a.total as total, 0 as totalpagado,a.total as totalpendiente,a.periodo as periodo, a.trim1 as trim1,a.trim2 as trim2,a.trim3 as trim3,a.trim4 as trim4,a.feccre as feccre,a.usrcre as usrcre ");
	hql.append("from Arbitrio  a ");
	hql.append("inner join a.upa  u ");
	if(estado.equals("CANCELADO") && sicancelado.equals("todos")){
		hql.append("where a.estado='"+estado+"'");
	}else if(estado.equals("PENDIENTE")&& sicancelado.equals("todos")){
		hql.append("where a.estado='"+estado+"'");
	}else{
		//hql.append("where a.estado='"+estado+"'");
	}
	hql.append(" order by u.clave asc");
	Query query= sessionFactory.getCurrentSession().createQuery(hql.toString());
	
	lista = query.setResultTransformer(Transformers.aliasToBean(ArbitrioBean.class)).list();
	return lista;
}
public List<ArbitrioBean> obtenerListaxCondicionCuotasPagadas(String estado){
	List<ArbitrioBean> lista = new ArrayList<ArbitrioBean>();
	StringBuilder hql=new StringBuilder();
	hql.append("select u.clave as clave,a.idarbitrio as idarbitrio,a.periodo as periodo,sum(ca.monto) as totalpagado ");
	hql.append("from Cuota_arbitrio  ca ");
	hql.append("inner join ca.arbitrio  a ");
	hql.append("inner join a.upa  u ");
	hql.append("where ca.estado='"+estado+"' ");
	hql.append("GROUP BY a.idarbitrio,a.periodo,u.clave ");

   Query query= sessionFactory.getCurrentSession().createQuery(hql.toString());
	
	lista = query.setResultTransformer(Transformers.aliasToBean(ArbitrioBean.class)).list();
	return lista;
}
@Override
public ArbitrioBean obtenerArbitrioPorCondicion(int idarbitrio) {
	
	StringBuilder hql=new StringBuilder();
	hql.append("select a.idarbitrio as idarbitrio, u.clave as clave ,a.estado as estado ,a.direccion as direccion, ");
	hql.append(" a.total as total,a.periodo as periodo, a.trim1 as trim1,a.trim2 as trim2,a.trim3 as trim3,a.trim4 as trim4,a.feccre as feccre,a.usrcre as usrcre , a.observacion as observacion ");
//	hql.append(" uss.descripcion as descripcion, ubi.dist as distrito ");
	hql.append("from Arbitrio  a ");
	hql.append("inner join a.upa  u ");
//	hql.append("left join a.ubigeo ubi ");
//	hql.append("left join a.uso  uss ");
	hql.append("where a.idarbitrio = '"+idarbitrio+"' ");
	Query query= sessionFactory.getCurrentSession().createQuery(hql.toString());
	return (ArbitrioBean) query.setResultTransformer(Transformers.aliasToBean(ArbitrioBean.class)).uniqueResult();
}


@SuppressWarnings("unchecked")
public List<PeriodoPagadoBean> obtenerPeriodosPagados(int idarbitrio) {
	// TODO Auto-generated method stub
	List<PeriodoPagadoBean> lista= new ArrayList<PeriodoPagadoBean>();
	StringBuilder hql= new StringBuilder();
	hql.append("select a.total as total ,cuo.idcuota_arbitrio as idcuota,cuo.mes as mes,cuo.periodo as periodo,cuo.trimestre as trimestre,cuo.monto as monto,cuo.cancelado as sipagado , cuo.nropagos as nropagos,cuo.estado as estado ");
	hql.append("from Cuota_arbitrio cuo ");
	hql.append("inner join cuo.arbitrio a ");
	hql.append("where a.idarbitrio='"+idarbitrio+"' and (cuo.estado='CANCELADO' or cuo.nropagos>0) and cuo.monto>0 order by cuo.periodo,cuo.nromes asc");
	
	Query query=sessionFactory.getCurrentSession().createQuery(hql.toString());
	lista=query.setResultTransformer(Transformers.aliasToBean(PeriodoPagadoBean.class)).list();
	return lista;
}
@SuppressWarnings("unchecked")
public List<CuotaArbitrioBean> obtenerPeriodosPendientes(int idarbitrio) {
	// TODO Auto-generated method stub
	List<CuotaArbitrioBean> lista= new ArrayList<CuotaArbitrioBean>();
	StringBuilder hql= new StringBuilder();
	hql.append("select cuo.idcuota_arbitrio as idcuota,cuo.mes as mes,cuo.periodo as periodo,cuo.trimestre as trimestre,cuo.monto as monto,cuo.monto as subtotal,cuo.cancelado as sipagado,cuo.estado as estado ,cuo.nropagos as nropagos ");
	hql.append(" , u.clave as claveUpa ");
	hql.append("from Cuota_arbitrio cuo ");
	hql.append("inner join cuo.arbitrio a ");
	hql.append("inner join a.upa u ");
	hql.append("where a.idarbitrio='"+idarbitrio+"' and cuo.estado='PENDIENTE'  order by cuo.periodo,cuo.nromes asc");
	
	Query query=sessionFactory.getCurrentSession().createQuery(hql.toString());
	lista=query.setResultTransformer(Transformers.aliasToBean(CuotaArbitrioBean.class)).list();
	return lista;
}
@SuppressWarnings("unchecked")
public List<CuotaArbitrioBean> obtenerListaPeriodosPendientes(int idarbitrio) {
	// TODO Auto-generated method stub
	List<CuotaArbitrioBean> lista= new ArrayList<CuotaArbitrioBean>();
	StringBuilder hql= new StringBuilder();
	hql.append("select cuo.idcuota_arbitrio as idcuota,cuo.mes as mes,cuo.periodo as periodo,cuo.trimestre as trimestre,cuo.monto as monto,cuo.monto as subtotal ,cuo.cancelado as sipagado,cuo.estado as estado ,cuo.nropagos as nropagos ");
	hql.append(" , u.clave as claveUpa ");
	hql.append("from Cuota_arbitrio cuo ");
	hql.append("inner join cuo.arbitrio a ");
	hql.append("inner join a.upa u ");
	hql.append("where a.idarbitrio='"+idarbitrio+"' and cuo.estado='PENDIENTE'  order by cuo.periodo,cuo.nromes asc");
	
	Query query=sessionFactory.getCurrentSession().createQuery(hql.toString());
	lista=query.setResultTransformer(Transformers.aliasToBean(CuotaArbitrioBean.class)).list();
	return lista;
}
@SuppressWarnings("unchecked")
public List<CuotaArbitrioBean> obtenerPeriodosCancelados(int idarbitrio) {
	// TODO Auto-generated method stub
	List<CuotaArbitrioBean> lista= new ArrayList<CuotaArbitrioBean>();
	StringBuilder hql= new StringBuilder();
	try{
	hql.append("select cuo.idcuota_arbitrio as idcuota,cuo.mes as mes,cuo.periodo as periodo,cuo.trimestre as trimestre,cuo.monto as monto,cuo.monto as rentapagada,cuo.monto as subtotal , cuo.cancelado as sipagado,cuo.estado as estado ,cuo.nropagos as nropagos, u.clave as claveUpa ");
	hql.append("from Cuota_arbitrio cuo ");
	hql.append("inner join cuo.arbitrio a ");
	hql.append("inner join a.upa u ");
	hql.append("where a.idarbitrio='"+idarbitrio+"' and cuo.estado='CANCELADO'  order by cuo.periodo,cuo.nromes asc");
	
	Query query=sessionFactory.getCurrentSession().createQuery(hql.toString());
	lista=query.setResultTransformer(Transformers.aliasToBean(CuotaArbitrioBean.class)).list();
	}catch(Exception e){
		e.printStackTrace();
	}
	return lista;
}
@SuppressWarnings("unchecked")
public List<PagoArbitrioBean> obtenerPeriodoxMes(int idarbitrio) {
	// TODO Auto-generated method stub
	List<PagoArbitrioBean> lista= new ArrayList<PagoArbitrioBean>();
	StringBuilder hql= new StringBuilder();
	hql.append("select cuo.idcuota_arbitrio as idcuota,cuo.mes as mes,cuo.periodo as periodo,cuo.trimestre as trimestre,cuo.monto as monto,cuo.cancelado as cancelado,cuo.nromes as nromes ,cuo.nropagos as nropagos ,cuo.estado as estado ");
	hql.append("from Cuota_arbitrio cuo ");
	hql.append("inner join cuo.arbitrio a ");
	hql.append("where a.idarbitrio='"+idarbitrio+"' order by cuo.periodo,cuo.nromes asc");
	
	Query query=sessionFactory.getCurrentSession().createQuery(hql.toString());
	lista=query.setResultTransformer(Transformers.aliasToBean(PagoArbitrioBean.class)).list();
	return lista;
}

@Override
public List<CuotaArbitrioBean> obtenerPagosArbitrios(int idarbitrio) {
	// TODO Auto-generated method stub
	return null;
}

@SuppressWarnings("unchecked")

public List<ComprobanteBean> buscarComprobantesPeriodo(int idcuota) {
	StringBuilder hql = new StringBuilder();	
	List<ComprobanteBean> lista= new ArrayList<ComprobanteBean>();
	hql.append("select cp.nroserie as nroserie,cp.nrocomprobante as nrocomprobante,cp.nroseriecomprobante as nroseriecomprobante,");
	hql.append("ndref.nroseriecomprobante as nronotadebitoref,ncref.nroseriecomprobante as nronotacreditoref,");
	hql.append("td.idtipodocu as idtipdocu,dcp.mora as mora,dcp.montosoles as total,");	
	hql.append("cp.fechaemision as fecEmision,cp.fechacancelacion as fecCancelacion,");
	hql.append("usr.nombrescompletos as nombrecobrador,cp.usuariocreador as usrcre,cp.fechacreacion as feccre ");
	hql.append(" from Detallecuota_arbitrio dcp ");
	hql.append("inner join dcp.cuota_arbitrio cuo ");
	hql.append("inner join dcp.comprobantepago cp ");
	hql.append("left join cp.notadebito ndref ");
	hql.append("left join cp.notacredito ncref ");
//	hql.append("inner join cp.detallecartera dc ");
	hql.append("inner join cp.usuario usr ");
	hql.append("inner join  cp.tipodocu td ");

	hql.append("where cuo.idcuota_arbitrio='"+idcuota+"' and dcp.estado='1' order by cp.fechacancelacion asc");
	
	Query query= sessionFactory.getCurrentSession().createQuery(hql.toString());
	
	lista=query.setResultTransformer(Transformers.aliasToBean(ComprobanteBean.class)).list();
	return lista;
}



public void saveUpdateArbitrio(Arbitrio arb) {
	getSessionFactory().getCurrentSession().saveOrUpdate(arb);	
}





@Override
public boolean verificarExistenciaClaveUpa(String clave) {

	String val=(String) getSessionFactory().getCurrentSession().createQuery("select u.clave from Upa u where u.clave='"+clave+"'").uniqueResult();
	
	if (val!=null) {
		return true;
	} else {
		return false;
	}
}


@Override
public boolean verificarExistenciaClaveUpaArbitrio(String clave) {

	String val=(String) getSessionFactory().getCurrentSession().createQuery("select a.upa.clave from Arbitrio a where a.upa.clave='"+clave+"'").uniqueResult();
	
	if (val!=null) {
		return true;
	} else {
		return false;
	}
}


@Override
public boolean verificarExistenciaClaveUpaPeriodoArbitrio(String clave,String periodo) {
	
	Integer val=(Integer) getSessionFactory().getCurrentSession().createQuery("select a.idarbitrio  from Arbitrio a where a.upa.clave='"+clave+"' and a.periodo='"+periodo+"'").uniqueResult();
	
	if (val!=null) {
		return true;
	} else {
		return false;
	}
}

@SuppressWarnings("unchecked")
public List<ArbitrioBean> buscarArbitrio(String valorbusqueda, String tipo) {
	// TODO Auto-generated method stub
	StringBuilder hql = new StringBuilder();
	hql.append("select a.idarbitrio as idarbitrio , u.idupa as idupa, u.clave as clave ,a.estado as estado, ");
	hql.append("a.direccion as direccion, a.periodo as periodo , a.trim1 as trim1, a.trim2 as trim2 , a.trim3 as trim3 , ");
	hql.append("a.trim4 as trim4 ,a.total as total,a.feccre as feccre , a.fecmod as fecmod, a.usrcre as usrcre , a.usrmod as usrmod,a.observacion as observacion , a.motivomod as motivomod ");
	hql.append("from Arbitrio a ");
	hql.append("inner join a.upa u ");
	
	if(tipo.equalsIgnoreCase("1")){
		hql.append("where u.clave like '"+valorbusqueda+"%' ");
	}else if(tipo.equalsIgnoreCase("2")){
		hql.append("where a.direccion like '%"+valorbusqueda+"%' ");
	}
	hql.append("order by a.periodo desc ");
	Query query= sessionFactory.getCurrentSession().createQuery(hql.toString());
	return (List<ArbitrioBean>) query.setResultTransformer(Transformers.aliasToBean(ArbitrioBean.class)).list();
	
}

@Override
public List<CuotaArbitrioBean> obtenerCuotasArbitrio(Integer idarbitrio) {
	
	StringBuilder hql = new StringBuilder();
	hql.append("select cuo.idcuota_arbitrio as idcuota , cuo.periodo as periodo, cuo.trimestre as trimestre, cuo.mes as mes , ");
	hql.append("cuo.nromes as nromes,cuo.monto as monto, cuo.nropagos as nropagos,cuo.cancelado as cancelado, cuo.estado as estado,cuo.usrcre as usrcre ,cuo.feccre as feccre , ");
	hql.append("cuo.fecmod as fecmod,  cuo.usrmod as usrmod ");
	hql.append("from Cuota_arbitrio cuo ");
	hql.append("inner join cuo.arbitrio a ");
	hql.append("left join cuo.comprobantepago cp ");
	hql.append("where a.idarbitrio ='"+idarbitrio+"' ");
	hql.append("order by cuo.periodo desc, cuo.trimestre asc, cuo.nromes asc ");
	Query query= sessionFactory.getCurrentSession().createQuery(hql.toString());
	return (List<CuotaArbitrioBean>) query.setResultTransformer(Transformers.aliasToBean(CuotaArbitrioBean.class)).list();
	
	
}

@Override
public boolean verificarExistenciaPagosCuotaArbitrio(Integer idarbitrio) {
	// TODO Auto-generated method stub
	Long val=(Long) getSessionFactory().getCurrentSession().createQuery("select count(dcuo.iddetallecuota) from Detallecuota_arbitrio dcuo inner join dcuo.cuota_arbitrio cuo inner join dcuo.comprobantepago cp where cuo.arbitrio.idarbitrio='"+idarbitrio+"'").uniqueResult();
	
	if ( val>0) {
		return true;
	} else {
		return false;
	}
}










}
