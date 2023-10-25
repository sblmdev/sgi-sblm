package pe.gob.sblm.sgi.daoImpl;

import java.io.Serializable;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import pe.gob.sblm.api.commons.constants.sgi.Constantes;
import pe.gob.sblm.api.commons.utility.CommonsUtilities;
import pe.gob.sblm.api.commons.utility.FechaUtil;
import pe.gob.sblm.api.commons.utility.FuncionesHelper;
import pe.gob.sblm.sgi.bean.ComprobanteBean;
import pe.gob.sblm.sgi.bean.CondicionBean;
import pe.gob.sblm.sgi.bean.CuotaArbitrioBean;
import pe.gob.sblm.sgi.bean.CuotaBean;
import pe.gob.sblm.sgi.bean.DetalleCuotaBean;
import pe.gob.sblm.sgi.bean.DetallecomprobanteBean;
import pe.gob.sblm.sgi.bean.ItemBean;
import pe.gob.sblm.sgi.bean.MaeDetCondicionDetalleBean;
import pe.gob.sblm.sgi.bean.PeriodoContratoBean;
import pe.gob.sblm.sgi.bean.PeriodoPagadoBean;
import pe.gob.sblm.sgi.bean.RentaBean;
import pe.gob.sblm.sgi.bean.UpaPagadorDeudorBean;
import pe.gob.sblm.sgi.dao.ICarteraDAO;
import pe.gob.sblm.sgi.entity.Arbitrio;
import pe.gob.sblm.sgi.entity.Auditoria;
import pe.gob.sblm.sgi.entity.Cartera;
import pe.gob.sblm.sgi.entity.Comprobantepago;
import pe.gob.sblm.sgi.entity.Contrato;
import pe.gob.sblm.sgi.entity.Cuota;
import pe.gob.sblm.sgi.entity.Cuota_arbitrio;
import pe.gob.sblm.sgi.entity.Detallecartera;
import pe.gob.sblm.sgi.entity.Detallecuota;
import pe.gob.sblm.sgi.entity.Detallecuota_arbitrio;
import pe.gob.sblm.sgi.entity.Estadoauditoria;
import pe.gob.sblm.sgi.entity.Eventoauditoria;
import pe.gob.sblm.sgi.entity.Sunat_Comprobante;
import pe.gob.sblm.sgi.entity.Sunat_Comprobante_Detalle;
import pe.gob.sblm.sgi.entity.Tipocambio;
import pe.gob.sblm.sgi.entity.Tipodocu;
import pe.gob.sblm.sgi.entity.Tipomodalidadpago;
import pe.gob.sblm.sgi.entity.Tipotransaccion;
import pe.gob.sblm.sgi.entity.Tipomovimiento;
import pe.gob.sblm.sgi.entity.Tipopago;
import pe.gob.sblm.sgi.entity.Upa;
import pe.gob.sblm.sgi.entity.Uso;
import pe.gob.sblm.sgi.entity.Usuario;
import pe.gob.sblm.sgi.entity.bean.DetallecarteraBean;
import pe.gob.sblm.sgi.util.Almanaque;
import pe.gob.sblm.sgi.util.Constante;

@Repository(value = "carteraDAO")
public class CarteraDAO implements ICarteraDAO, Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	@Qualifier(value="secondDBSessionFactory")
	private SessionFactory secondDBSessionFactory;

	Session sesion = null;
	
	public void registrarCartera(Cartera cartera) {
		getSessionFactory().getCurrentSession().merge(cartera);

	}
	
	
	@Transactional
	public void actualizarCuota(Cuota cuo) {
		Session session = sessionFactory.openSession();
		session.update(cuo);
		session.flush();
		session.close();
	}
	
	@Transactional
	public void actualizarCuotaArbitrio(Cuota_arbitrio cuo) {
		Session session = sessionFactory.openSession();
		session.update(cuo);
		session.flush();
		session.close();
		
//		getSessionFactory().getCurrentSession().createSQLQuery("update  Cuota_arbitrio set estado='CANCELADO',observacion='Cancelación de Pago' WHERE idarbitrio='"+ a.getIdarbitrio()+"'").executeUpdate();
	}
	public void actualizarCuotaNC(Cuota cuo){
		try{
		getSessionFactory().getCurrentSession().createSQLQuery("update  cuota set montosoles="+cuo.getMontosoles()+",nropagos =nropagos+1 ,usrmod='"+cuo.getUsrmod()+"' ,fecmod=GETDATE() WHERE idcuota="+ cuo.getIdcuota()+"").executeUpdate();
		} catch (Exception e) {
			System.out.println("error en actualizar cuota:::"
					+ e.getMessage());
			e.printStackTrace();
		}
	}
	public void actualizarArbitrio(Arbitrio a){
		try{
		getSessionFactory().getCurrentSession().createSQLQuery("update  arbitrio set estado='CANCELADO',sifinalizado ='1' ,usrmod='"+a.getUsrmod()+"' ,fecmod=GETDATE() WHERE idarbitrio="+ a.getIdarbitrio()+"").executeUpdate();
		} catch (Exception e) {
			System.out.println("error en actualizar arbitrio:::"
					+ e.getMessage());
			e.printStackTrace();
		}
	}
	public void actualizarDetalleCuota(Detallecuota dcuo){
		try{
		getSessionFactory().getCurrentSession().createSQLQuery("update  detallecuota set id_maed_condicion_detalle=null  WHERE iddetallecuota="+ dcuo.getIddetallecuota()+"").executeUpdate();
		} catch (Exception e) {
			System.out.println("error en actualizar detallecuota:::"
					+ e.getMessage());
			e.printStackTrace();
		}
	}
	public void actualizarEstadoDetalleCuota(Detallecuota dcuo){
		try{
			if(dcuo.getEstado().equals(Constante.ESTADO_REC)){
				getSessionFactory().getCurrentSession().createSQLQuery("update  detallecuota set estado='"+dcuo.getEstado()+"', condicion='"+dcuo.getCondicion()+"' WHERE iddetallecuota="+ dcuo.getIddetallecuota()+"").executeUpdate();
			}else if(dcuo.getEstado().equals(Constante.ESTADO_ANU)){
				getSessionFactory().getCurrentSession().createSQLQuery("update  detallecuota set estado='"+dcuo.getEstado()+"', condicion='"+dcuo.getCondicion()+"' ,usuario_anula='"+dcuo.getUsuario_anula()+"',host_ip_anula='"+dcuo.getHost_ip_anula()+"' , descripcion_anula='"+dcuo.getDescripcion_anula() +"', fecha_anula=getDate() WHERE iddetallecuota="+ dcuo.getIddetallecuota()+"").executeUpdate();}
			} catch (Exception e) {
			System.out.println("error en actualizar detallecuota:::"
					+ e.getMessage());
			e.printStackTrace();
		}
	}
	public void actualizarEstadoDetalleCuotaArbitrio(Detallecuota_arbitrio dcuo){
		try{
			if(dcuo.getEstado().equals(Constante.ESTADO_REC)){
				getSessionFactory().getCurrentSession().createSQLQuery("update  detallecuota_arbitrio set estado='"+dcuo.getEstado()+"', condicion='"+dcuo.getCondicion()+"' WHERE iddetallecuota_arbitrio="+ dcuo.getIddetallecuota()+"").executeUpdate();
			}else if(dcuo.getEstado().equals(Constante.ESTADO_ANU)){
				getSessionFactory().getCurrentSession().createSQLQuery("update  detallecuota_arbitrio set estado='"+dcuo.getEstado()+"', condicion='"+dcuo.getCondicion()+"' ,usuario_anula='"+dcuo.getUsuario_anula()+"',host_ip_anula='"+dcuo.getHost_ip_anula()+"' , descripcion_anula='"+dcuo.getDescripcion_anula() +"', fecha_anula=getDate() WHERE iddetallecuota_arbitrio="+ dcuo.getIddetallecuota()+"").executeUpdate();}
			} catch (Exception e) {
			System.out.println("error en actualizar detallecuota_arbitrio:::"
					+ e.getMessage());
			e.printStackTrace();
		}
	}
//	public void actualizarCuotaArbitrio(Cuota_arbitrio cuo){
//		getSessionFactory().getCurrentSession().createSQLQuery("update  Cuota_arbitrio set cancelado='"+cuo.getCancelado()+"',estado='"+cuo.getEstado()+"',usrmod='"+cuo.getUsrmod()+"',fecmod="+cuo.getFecmod()+",observacion='"+cuo.getObservacion()+"' WHERE idcuota_arbitrio='"+ cuo.getIdcuota_arbitrio()+"'").executeUpdate();
//	}
	
    public void actualizarEstadoComprobante(Comprobantepago  cp){

    	sesion= sessionFactory.openSession();
    	sesion.beginTransaction();
    		
		try{
//			getSecondDBSessionFactory().getCurrentSession().createSQLQuery("update  sunat_comprobante set Estado='0' ,Observaciones='"+scp.getObservaciones()+"' ,Usuario_Anula_Documento='"+scp.getUsuario_Anula_Documento()+"' , Host_Ip_Anula_Documento='"+scp.getHost_IP_Anula_Documento()+"', Fecha_Anula_Documento=GETDATE()  where id_comprobante='"+ scp.getId_Comprobante()+"'").executeUpdate();
    		//getSessionFactory().getCurrentSession().createSQLQuery(" update comprobantepago set condicion='RECHAZADO' , estado='3',fechamodificacion=GETDATE() , usuariomodificador='"+cp.getUsuariomodificador()+"' where idcomprobante="+cp.getIdcomprobante()).executeUpdate();;
    		getSessionFactory().getCurrentSession().createSQLQuery(" update comprobantepago set condicion='RECHAZADO' , estado='3' where idcomprobante="+cp.getIdcomprobante()).executeUpdate();

		} catch (HibernateException e) {
			System.out.println("error en actualizar COMPROBANTE:::"
					+ e.getMessage());
			e.printStackTrace();
			sesion.getTransaction().rollback();
		}
		sesion.getTransaction().commit();
    	
    	
    }
	
	public void actualizarComprobante(ComprobanteBean comprobantepago) {
		StringBuilder hqlA = new StringBuilder();
		hqlA.append("update Comprobantepago set ");
		hqlA.append("nroserie = :nroserie, ");
		hqlA.append("nrocomprobante = :nrocomprobante , ");
		hqlA.append("nroseriecomprobante = :nroseriecomprobante, ");
		hqlA.append("nombrecobrador = :nombrecobrador , ");
		hqlA.append("fechaemision = :fechaemision, ");
		hqlA.append("fechacancelacion= :fechacancelacion, ");
		hqlA.append("usuariomodificador = :usuariomodificador, ");
		hqlA.append("fechamodificacion = :fechamodificacion, ");
		hqlA.append("idtipodocu = :idtipodocu, ");
		hqlA.append("idusuario = :idcobrador, ");
		if(comprobantepago.getSiautodetraccion()!=null){
			hqlA.append("siautodetraccion = :siautodetraccion, ");
			hqlA.append("montoautodetraccion = :montoautodetraccion, ");
		}	
		hqlA.append("ipoperacion = :ipoperacion ");
//		hqlA.append("totalsoles = :totalsoles, ");
//		hqlA.append("igvsoles = :igvsoles ");
		hqlA.append("where idcomprobante = :idcomprobante");
		
		Query queryA = sessionFactory.getCurrentSession().createQuery(hqlA.toString());
		queryA.setParameter("nroserie", comprobantepago.getNroserie());
		queryA.setParameter("nrocomprobante", comprobantepago.getNrocomprobante());
		queryA.setParameter("nroseriecomprobante", comprobantepago.getNroseriecomprobante());
		queryA.setParameter("nombrecobrador", comprobantepago.getNombrecobrador());
		queryA.setParameter("fechaemision", comprobantepago.getFecEmision());
		queryA.setParameter("fechacancelacion", comprobantepago.getFecCancelacion());
		queryA.setParameter("usuariomodificador", comprobantepago.getUsrmod());
		queryA.setParameter("fechamodificacion", comprobantepago.getFecmod());
		queryA.setParameter("idtipodocu", comprobantepago.getIdtipdocu());
		queryA.setParameter("idcobrador", comprobantepago.getIdcobrador());
		queryA.setParameter("idcomprobante", comprobantepago.getIdcomprobante());
		if(comprobantepago.getSiautodetraccion()!=null){
			queryA.setParameter("siautodetraccion", comprobantepago.getSiautodetraccion());
			queryA.setParameter("montoautodetraccion", comprobantepago.getMontoautodetraccion());
		}
//		queryA.setParameter("totalsoles", comprobantepago.getTotal());
//		queryA.setParameter("igvsoles", comprobantepago.getIgv());
		queryA.setParameter("ipoperacion", CommonsUtilities.getRemoteIpAddress());
		queryA.executeUpdate();
	
	}

	
	public void editarEstadoPago(Integer idcuota, String estado) {
		     /** Cuota */
			getSessionFactory().getCurrentSession().createSQLQuery("update  Cuota set cancelado='"+estado+"' WHERE idcuota='"+ idcuota+"'").executeUpdate();
			/** Detallecuota*/
			getSessionFactory().getCurrentSession().createSQLQuery("update  Detallecuota set cancelado='"+estado+"' WHERE idcuota='"+ idcuota+"'").executeUpdate();
	}

	
	
	public void registrarDetalleCuota(Detallecuota detallecuota) {
		try {
		getSessionFactory().getCurrentSession().save(detallecuota);
		}catch(Exception e){
			System.out.println("error al insertar :::"
					+ e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void registrarDetalleCuotaArbitrio(Detallecuota_arbitrio detallecuota) {
		getSessionFactory().getCurrentSession().save(detallecuota);
	}
	
	public void registrarDetalleCartera(Detallecartera detallecartera) {
		getSessionFactory().getCurrentSession().merge(detallecartera);
	}
	
	
	
	public void grabarComprobantePago(Comprobantepago cp) {
		getSessionFactory().getCurrentSession().save(cp);
	}
	
	public void grabarComprobanteDetalle(Sunat_Comprobante_Detalle compdetalle) {
		getSessionFactory().getCurrentSession().save(compdetalle);
	}

	
	public void eliminarCartera(Cartera cartera) {
		try {
			getSessionFactory().getCurrentSession().createSQLQuery("delete from Cartera WHERE idcartera='"+ cartera.getIdcartera() + "'").executeUpdate();
		} catch (Exception e) {
			System.out.println("error en dao eliminar cartera:::"
					+ e.getMessage());
		}
		
	}
	
	
	public void eliminarCarteraDetalle(Detallecartera detallecartera) {
		try {
			getSessionFactory().getCurrentSession().createSQLQuery("delete from Detallecartera WHERE iddetallecartera='"+ detallecartera.getIddetallecartera() + "'").executeUpdate();
			
		} catch (Exception e) {
			System.out.println("error en dao eliminar eliminarCarteraDetalle:::"
					+ e.getMessage());
		}
		
	}

	@SuppressWarnings("unchecked")
	
	public List<Tipopago> listartipopagos() {
		return	getSessionFactory().getCurrentSession().createQuery("from Tipopago order by idtipopago asc").list();
	}
	@SuppressWarnings("unchecked")
	
	public List<Tipotransaccion> listartipotransaccion() {
		return	getSessionFactory().getCurrentSession().createQuery("from Tipotransaccion order by idtipotransaccion asc").list();
	}
	@SuppressWarnings("unchecked")
	
	public List<Tipodocu> listartipodocus() {
		return	getSessionFactory().getCurrentSession().createQuery("select td from Tipodocu td order by feccre desc").list();
	}
	
	@SuppressWarnings("unchecked")
	
	@Transactional(readOnly=true)
	public List<Tipomovimiento> listartipomovimientos() {
		return	getSessionFactory().getCurrentSession().createQuery("from Tipomovimiento order by feccre desc").list();
	}
	
	@SuppressWarnings("unchecked")
	
	@Transactional(readOnly=true)
	public List<Tipomodalidadpago> listartipomodalidadpagos() {
		return	getSessionFactory().getCurrentSession().createQuery("from Tipomodalidadpago order by idtipomodalidadpago asc").list();
	}
	
	@SuppressWarnings("unchecked")
	
	@Transactional(readOnly=true)
	public List<Usuario> listarUsuarios() {
		return	getSessionFactory().getCurrentSession().createQuery("select u from Usuario u where u.escobrador='true' order by nombrescompletos").list();
		
	}
	
	@SuppressWarnings("unchecked")
	
	@Transactional(readOnly=true)
	public List<Detallecartera> listarDetalleCarteras() {
		return	getSessionFactory().getCurrentSession().createQuery("from Detallecartera order by feccre desc").list();
	}
	
	@SuppressWarnings("unchecked")
	public List<DetallecarteraBean> listarDetalleCarterasPorIdCartera(int idcartera) {
		
		StringBuilder hql = new StringBuilder();	
		
		hql.append("select c.idcontrato as idcontrato,c.condicion as condicion,c.nrocontrato as numerocontrato,");
		hql.append("u.clave as claveupa,");
		hql.append("car.numero as numerocartera,car.idcartera as idcartera,");	
		hql.append("ubi.dist as distrito,");	
		hql.append("(inm.direccion+' '+inm.numeroprincipal+' '+u.nombrenuminterior) as direccion,");
		hql.append("dc.iddetallecartera as iddetallecartera,dc.feccre as feccre,dc.usrcre as usrcre");	
		hql.append(" from Detallecartera dc ");
		hql.append("inner join  dc.contrato c ");
		hql.append("inner join  dc.cartera car ");
		hql.append("inner join  c.upa u ");
		hql.append("inner join  u.inmueble inm ");
		hql.append("inner join  inm.ubigeo ubi ");
		hql.append("where car.idcartera="+idcartera+" and c.estado='VIGENTE'" );
		hql.append("order by inm.direccion desc");
		
		Query query=getSessionFactory().getCurrentSession().createQuery(hql.toString());
		
		
		return	query.setResultTransformer(Transformers.aliasToBean(DetallecarteraBean.class)).list();
		
	}
	
	@SuppressWarnings("unchecked")
	
	@Transactional(readOnly=true)
	public List<Contrato> listarContratosVigentes() {
		Session session = getSessionFactory().openSession();
		Query sql = session.createSQLQuery("select c.idcontrato,u.clave from CONTRATO as c left  join DETALLECARTERA as dc on dc.IDCONTRATO=c.IDCONTRATO inner join UPA as u ON c.IDUPA=u.IDUPA  where dc.IDCONTRATO IS Null");
		
		
		List<Object>  objectList= sql.list();
		
		Iterator<Object> iterator = objectList.iterator();
		List<Contrato> listContratos= new ArrayList<Contrato>();
		
		
		while(iterator.hasNext()){ 
			Object []obj = (Object[])iterator.next();
			
			Contrato c= new Contrato();
			c.setIdcontrato((Integer) obj[0]);
			
			Upa u= new Upa();
			u.setClave((String) obj[1]);
			
			c.setUpa(u);
			
			listContratos.add(c);
			
			}
		
			return listContratos;
	}

	@SuppressWarnings("unchecked")
	public List<Cuota> listarcuotasxcontrato(int idcontrato) {
		return	getSessionFactory().getCurrentSession().createQuery("select cuo from Cuota cuo inner join cuo.contrato c  where c.idcontrato='"+idcontrato+"'").list();
	}
	@SuppressWarnings("unchecked")
	public List<Cuota> listarcuotascontrato(int idcontrato) {
		// buscar contrato 
		Contrato c = obtenerContratoSeleccionado(idcontrato);
		List<Cuota> listacuota= new ArrayList<Cuota>();
		List<CuotaBean> listacuotabean= new ArrayList<CuotaBean>();
	
					
					listacuota=getSessionFactory().getCurrentSession().createQuery("select cuo from Cuota cuo inner join cuo.contrato c  where c.idcontrato='"+idcontrato+"'").list();
					listacuotabean=obtenerPeriodosPendientesContratoDeuda(idcontrato);
					for(Cuota cuo :listacuota ){
							for(CuotaBean cuobean:listacuotabean){
									if(cuo.getIdcuota()==cuobean.getIdcuota()){
											if(FuncionesHelper.redondear((cuo.getMontosoles()-cuobean.getMonto()),2)>0){
													cuo.setCancelado("0");
														break;	
											}
	    			
									}
							}
					}
			
		
		return	listacuota;
	  
			  
			  
	}
	@SuppressWarnings("unchecked")
	public List<Detallecuota> listardetallecuotasxcontrato(int idcontrato) {
		return	getSessionFactory().getCurrentSession().createQuery("select dcuo from Detallecuota dcuo inner join dcuo.cuota cuo  inner join cuo.contrato c  inner join dcuo.comprobantepago cp where c.idcontrato='"+idcontrato+"' and cp.tipoconcepto.idtipoconcepto<>'17' and (dcuo.estado is null or dcuo.estado='1' )").list();
	}
	/* ********************************** */
	@SuppressWarnings("unchecked")
	public List<Detallecuota> listardetallecuotascontrato(int idcontrato) {
	    List<Detallecuota> listadetalle =new ArrayList<Detallecuota>();
	    List<Detallecuota> listadetallexcancelar =new ArrayList<Detallecuota>();
	    listadetalle=getSessionFactory().getCurrentSession().createQuery("select dcuo  from Detallecuota dcuo inner join dcuo.cuota cuo  inner join cuo.contrato c  inner join dcuo.comprobantepago cp where c.idcontrato='"+idcontrato+"' and cp.tipoconcepto.idtipoconcepto<>'17'").list();
	    listadetallexcancelar=getSessionFactory().getCurrentSession().createQuery("select dcuo  from Detallecuota dcuo inner join dcuo.cuota cuo  inner join cuo.contrato c  inner join dcuo.comprobantepago cp where c.idcontrato='"+idcontrato+"' and cp.tipoconcepto.idtipoconcepto<>'17' and cp.fechacancelacion is null").list();
	    for(Detallecuota dcuo:listadetalle){
	    	for(Detallecuota dcuota:listadetallexcancelar){
	    		if(dcuo.getIddetallecuota()==dcuota.getIddetallecuota()){
	    			dcuo.setCancelado("0");
	    			break;
	    		}
	    	}
	    }
	    
	    return listadetalle;
	
	}
	/* ********************************** */
	@SuppressWarnings("unchecked")
	public List<Cuota_arbitrio> listarcuotasxarbitrio(int idarbitrio) {
		return	getSessionFactory().getCurrentSession().createQuery("select cuo from Cuota_arbitrio cuo inner join cuo.arbitrio c where c.idarbitrio='"+idarbitrio+"'").list();
	}
	/* ********************************** */
	@SuppressWarnings("unchecked")
	
	@Transactional(readOnly=true)
	public List<Detallecartera> listarContratosxcartera(String nrocartera) {
		return	getSessionFactory().getCurrentSession().createQuery("select dc from Detallecartera dc left join fetch dc.contrato  c  left join fetch dc.cartera ca left join fetch c.upa  where ca.numero='"+nrocartera+"'").list();
	}
	
	@SuppressWarnings("unchecked")
	
	@Transactional(readOnly=true)
	public List<Uso> listarUsos() { 
		return	getSessionFactory().getCurrentSession().createQuery("from Uso").list();
	}


	@SuppressWarnings("unchecked")
	
	@Transactional(readOnly=true)
	public List<Detallecartera> listarDetalleCarterasPorNroContrato(String nrocontrato) { 
		return	getSessionFactory().getCurrentSession().createQuery("from Detallecartera detcar where detcar.contrato.nrocontrato='"+nrocontrato+"' ").list();
	}
	
	
	
	@Transactional(readOnly=true)
	public Cartera obtenerUltimoCartera() {
		return (Cartera) getSessionFactory().getCurrentSession().createQuery(
				"select t from Cartera t where idcartera=( select max(idcartera) from Cartera)")
				.uniqueResult();
	}
	
	
	@Transactional(readOnly=true)
	public Double obtenerUltimoIgv() {
		return Double.valueOf((String) getSessionFactory().getCurrentSession().createSQLQuery("select t.valorigv from Igv as t order by t.idigv desc")
				.uniqueResult());
	}

	
	
	@Transactional(readOnly=true)
	public Cartera obtenerCarteraPorId(int id) {
		Session session = getSessionFactory().openSession();
		return (Cartera) session.load(Cartera.class, id);
	}
	
	@Transactional(readOnly=true)
	public Cartera obtenerCarteraPorIdContrato(int idcontrato) {
		
		return (Cartera) getSessionFactory().getCurrentSession().createQuery(
						" from Cartera c where c.idcartera=( select  dc.cartera.idcartera from Detallecartera dc where dc.contrato.idcontrato = "+idcontrato +" )")
				.uniqueResult();
	}
	

	
	
	@Transactional(readOnly=true)
	public Tipocambio buscarTipoCambio(String fecha) {
		return (Tipocambio) getSessionFactory().getCurrentSession().createQuery("select t from Tipocambio t where t.fecha='"+fecha+"'").uniqueResult();
	}
	
	@Transactional(readOnly=true)
	public String buscarobtenerNroContratoxIdTipoCambio(int idcontrato) {
		
		return  (String) getSessionFactory().getCurrentSession().createQuery("select c.nrocontrato from Contrato c where c.idcontrato='"+idcontrato+"'").uniqueResult();
	}
	
	@Transactional(readOnly=true)
	public int obtenerNroPagosCuotaContrato(int idcontrato) {
		return  (Integer) getSessionFactory().getCurrentSession().createQuery("select count(*) from Contrato c where c.idcontrato='"+idcontrato+"'").uniqueResult();
	}
	
	
	@Transactional(readOnly=true)
	public double obtenerSumaAcumuladaFraccionCuota(String nrocuota, int idcontrato) {
		return (Double) getSessionFactory().getCurrentSession().createQuery("select cu.montosoles from Cuota cu  inner join cu.contrato c where c.idcontrato='"+idcontrato+"' and cu.cuotanumero='"+nrocuota+"'").uniqueResult();
	}
	
	
	@Transactional(readOnly=true)
	public Cuota obtenerIdCuota(String nrocuota, int idcontrato) {
		return (Cuota) getSessionFactory().getCurrentSession().createQuery("select cu from Cuota cu  inner join cu.contrato c where c.idcontrato='"+idcontrato+"' and cu.cuotanumero='"+nrocuota+"'").uniqueResult();
	}
	
	
	@Transactional(readOnly=true)
	public Contrato obtenerContratoSeleccionado(int idcontratoseleccionado) {
		return (Contrato) getSessionFactory().getCurrentSession().createQuery("select c from Contrato c where c.idcontrato='"+idcontratoseleccionado+"'").uniqueResult();
	}
	
	
	@Transactional(readOnly=true)
	public int obtenerIddetalleCartera(int idcontratoseleccionado) {
		SQLQuery query=getSessionFactory().getCurrentSession().createSQLQuery("select dc.iddetallecartera from Detallecartera as dc where dc.idcontrato='"+idcontratoseleccionado+"'");
		
		if (query.list().size()==0) {
			return 0;
		} else {
			
			return (Integer) query.list().get(0);
		}
		
		
	}
	@Transactional(readOnly=true)
	public int obtenerIddetalleCarteraAutovaluo(int idseleccionado) {
		SQLQuery query=getSessionFactory().getCurrentSession().createSQLQuery("select dc.iddetallecartera from Arbitrio a  inner join upa u on a.idupa=u.idupa inner join contrato c on c.idupa=u.idupa inner join detallecartera dc on c.idcontrato=dc.idcontrato  where a.idarbitrio='"+idseleccionado+"'");
		
		if (query.list().size()==0) {
			return 0;
		} else {
			
			return (Integer) query.list().get(0);
		}
		
		
	}
	
	
	@Transactional(readOnly=true)
	public int obtenerNroCuotasCanceladas(int idcontratoseleccionado) {
		return  getSessionFactory().getCurrentSession().createQuery("select cuo.idcuota from Cuota cuo inner join cuo.contrato c where c.idcontrato='"+idcontratoseleccionado+"' and cuo.cancelado='true'").list().size();
	}
	
	
	@Transactional(readOnly=true)
	public boolean siExisteCarteraConNombre(String numero) {
		
		Long  val= (Long) getSessionFactory().getCurrentSession().createQuery("select count(*) from Cartera car  where car.numero='"+numero+"' ").uniqueResult();
		
		if (val.intValue()==0) {
			return false;
		} else {
			return true;
		}
	}
	
	@SuppressWarnings("unchecked")
	
	@Transactional(readOnly=true)
	public Contrato obtenerContratoxId(Integer id) {
		List<Contrato> lista= new ArrayList<Contrato>();
		lista= getSessionFactory().getCurrentSession().createQuery("select c from Contrato c left join fetch c.upa u left join fetch c.inquilino i where c.idcontrato='"+id+"' order by c.idcontrato desc").list();
		
		if (lista.size()==0) {
			
			return null;
		} else {
			
			if (lista.get(0).getCondicion().equals("Contrato")) {
				
			}else {
				
			}
			return lista.get(0);
			
		}
		
	}
	
	
	@Transactional(readOnly=true)
	public boolean siExisteUpa(String claveupaAgregarCartera) {
		
		Long val= (Long) getSessionFactory().getCurrentSession().createQuery("select count(*) from Upa u where u.clave='"+claveupaAgregarCartera+"'").uniqueResult();
		
		if (val.intValue()==0) {
			return false;
		} else {
			return true;
		}
		
	}
	
	
	@Transactional(readOnly=true)
	public boolean siPerteneceCartera(int idcontrato) {
		
		Long val=(Long) getSessionFactory().getCurrentSession().createQuery("select count(*) from Detallecartera dc inner join dc.contrato c where c.idcontrato='"+idcontrato+"'").uniqueResult();
		
		if (val.intValue()==0) {
			return false;
		} else {
			return true;
		}
	
	}
	
	@SuppressWarnings("unchecked")
	
	@Transactional(readOnly=true)
	public List<Cartera> listaCarteras() {
		return  getSessionFactory().getCurrentSession().createQuery("select car from Cartera car left join fetch car.usuario u where car.estado='1' order by car.numero desc ").list();
	}
	
	
	@Transactional(readOnly=true)
	public boolean siExisteRegistradoPago(int idcontratoseleccionado) {
		
		Long val= (Long) getSessionFactory().getCurrentSession().createQuery("select count(*) from Cuota cuo inner join cuo.contrato c where c.idcontrato='"+idcontratoseleccionado+"'").uniqueResult();
		
		if (val.intValue()==0) {
			return false;
		} else {
			return true;
		}
		
	}
	
	@SuppressWarnings("unchecked")
	
	@Transactional(readOnly=true)
	public Date obtenerFechaUltimoPago(int idcontratoseleccionado) {
		List<Cuota> cuota= new ArrayList<Cuota>();
		
		cuota= getSessionFactory().getCurrentSession().createQuery("select cuo from Cuota cuo inner join cuo.contrato c where c.idcontrato='"+idcontratoseleccionado+"' order by cuo.idcuota desc").list();
		
		String fecha="01/"+Almanaque.mesanumero(cuota.get(0).getCuotanumero())+"/"+cuota.get(0).getAniocuotames();
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		Date date = null;
		try {
			date = formatter.parse(fecha);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return date;
	}
	
	@SuppressWarnings("unchecked")
	
	@Transactional(readOnly=true)
	public List<Cuota> listarcuotasxSincontrato(int idcontratoseleccionado, int periodoSinContrato) {
		Session session = getSessionFactory().getCurrentSession();
		return session.createQuery("select cuo from Cuota cuo inner join cuo.contrato c where c.idcontrato="+idcontratoseleccionado+" and cuo.aniocuotames='"+periodoSinContrato+"'").list();
	
	}

//	@SuppressWarnings("unchecked")
//	
//	@Transactional(readOnly=true)
	@SuppressWarnings("unchecked")
	@Override
	public List<CondicionBean> obtenerListaContratosDeUpa(String clave) {
		StringBuilder hql = new StringBuilder();	
		
		hql.append("select c.idcontrato as idcontrato, c.sinuevomantenimiento as sinuevomantenimiento,c.condicion as condicion,c.nrocontrato as nrocontrato,c.siocupante as siocupante,c.sinombreocupante as nombreocupante,c.sidniocupante as dniocupante ,c.tipomoneda as moneda,");
		hql.append("c.iniciocontrato as iniciocontrato,c.fincontrato as fincontrato,c.iniciocobro as iniciocobro,c.fincobro as fincobro,");
		hql.append("c.sicuotascanceladas as sicuotascanceladas,c.estado as estado,c.numerocuotas as numerocuotas,");
		hql.append("c.siresuelto as siresuelto,c.siadenda as siadenda,");
		hql.append("c.montocuotasoles as  cuota1,c.montocuotasoles2 as  cuota2,c.montocuotasoles3 as  cuota3,c.montocuotasoles4 as  cuota4,c.montocuotasoles5 as  cuota5,c.montocuotasoles6 as  cuota6,");
		hql.append("u.clave as claveUpa,u.clavenueva as claveupanueva,u.estado as estadoupa,i.nombrescompletos as nombresInquilino,i.ruc as ruc,tper.idtipopersona as idtipopersona , tper.descripcion as tipopersona,i.dni as dni,i.carnetextranjeria as carnetextranjeria ,");	
		hql.append("(inm.direccion+' '+inm.numeroprincipal+' '+u.nombrenuminterior) as direccion , u.siprocesojudicial as siprocesojudicial , CASE  WHEN c.sireferenciareconocimientodeuda IS NULL  then false  else c.sireferenciareconocimientodeuda END  as sireferenciareconocimientodeuda ");
		hql.append(",c.sireconocimientodeuda  as sireconocimientodeuda ");
		hql.append(",c.sicondicionclausula as sicondicionclausula ,fp.id_fechapago as idfechapago , c.tipomoneda as moneda , u.uso as uso ");
		hql.append(" from Contrato c ");
		hql.append("inner join  c.upa u ");
		hql.append("inner join  u.inmueble inm ");
		hql.append("inner join  c.inquilino i ");
		hql.append("inner join  i.tipopersona tper ");
		hql.append("left join c.fechapago fp  ");
		hql.append("where u.clave='"+clave+"'" );
		hql.append(" and c.sianulado <> 1 ");
		hql.append("order by c.iniciocobro desc");
		
		Query query=getSessionFactory().getCurrentSession().createQuery(hql.toString());
		
		
		return	query.setResultTransformer(Transformers.aliasToBean(CondicionBean.class)).list();
	}
	
	@SuppressWarnings("unchecked")
	
	@Transactional(readOnly=true)
	public List<Detallecuota> obtenerDetalleComprobante(int idcomprobante) {
		return	getSessionFactory().getCurrentSession().createQuery("select dc from Detallecuota dc inner join dc.comprobantepago cp  where cp.idcomprobante='"+idcomprobante+"'").list();
	}
    @SuppressWarnings("unchecked")
	
	@Transactional(readOnly=true)
	public List<Detallecuota_arbitrio> obtenerDetalleCuotaArbitrioComprobante(int idcomprobante) {
		return	getSessionFactory().getCurrentSession().createQuery("select dc from Detallecuota_arbitrio dc inner join dc.comprobantepago cp  where cp.idcomprobante='"+idcomprobante+"'").list();
	}
	
    @SuppressWarnings("unchecked")
	
	@Transactional(readOnly=true)
 	public List<Integer> obtenerListaIdSunatComprobanteRechazadosxPeriodo(){
		return	(List<Integer>)getSecondDBSessionFactory().getCurrentSession().createQuery("select scp.id_referencia from Sunat_Comprobante scp where scp.origen_documento='SGI' and scp.respuesta_sunat='RECHAZADO' and YEAR(scp.fecha_emision)=YEAR(getdate()) and MONTH(scp.fecha_emision)=MONTH(getdate())").list();
	}
	
	@SuppressWarnings("unchecked")
	
	@Transactional(readOnly=true)
	public List<Integer> obtenerListaIdComprobanteRechazadosxPeriodo(){
		return	(List<Integer>)getSessionFactory().getCurrentSession().createQuery(" select cp.idcomprobante from Comprobantepago cp where cp.condicion='RECHAZADO' and cp.estado='3'and YEAR(cp.fechaemision)=YEAR(getdate()) and MONTH(cp.fechaemision)=MONTH(getdate())").list();
	}
	
	
	@Transactional(readOnly=true)
	public String obtenerDescripcionComprobantePago(String idtipDocu) {
		return	(String) getSessionFactory().getCurrentSession().createQuery("select td.tipDocu from Tipodocu td   where td.idtipodocu='"+idtipDocu+"'").uniqueResult();
	}
	
	
	@Transactional(readOnly=true)
	public String obtenerDescripcionTipoMovimiento(String idtipomovimiento) {
		return	(String) getSessionFactory().getCurrentSession().createQuery("select td.tipMovi from Tipomovimiento td   where td.idtipomovimiento='"+idtipomovimiento+"'").uniqueResult();
	}
	
	
	@Transactional(readOnly=true)
	public String obtenerDescripcionTipoPago(String idtipopago) {
		return	(String) getSessionFactory().getCurrentSession().createQuery("select td.tipPago from Tipopago td   where td.idtipopago='"+idtipopago+"'").uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	
	@Transactional(readOnly=true)
	public List<Comprobantepago> listaComprobantes(int idcontrato) {
		return	getSessionFactory().getCurrentSession().createQuery("select cp from Comprobantepago cp  inner join cp.detallecartera dc inner join dc.contrato c where c.idcontrato='"+idcontrato+"' and cp.sigeneronotadebito='false'").list();
	}
	
	
	@Transactional(readOnly=true)
	public Double obtenerMoraComprobante(Integer idcomprobantepadre) {
		return (Double) getSessionFactory().getCurrentSession().createQuery("select cp.moradetectada from Comprobantepago cp  where idcomprobante='"+idcomprobantepadre+"'").uniqueResult();
	}
	
	public void apuntarComprobanteAnotadebito(int idcomprobante,int idnotadebito) {
		getSessionFactory().getCurrentSession().createSQLQuery("update  Comprobantepago set idnotadebito='"+idnotadebito+"',sigeneronotadebito=1 WHERE idcomprobante='"+ idcomprobante+"'").executeUpdate();
	}
	public void apuntarComprobanteAnotaDebitoPenalizacion(int idcomprobante,int idnotadebito) {
		getSessionFactory().getCurrentSession().createSQLQuery("update  Comprobantepago set idnotadebitopenalizacion='"+idnotadebito+"',sigeneronotadebito=1 ,sigeneropenalizacion=1 WHERE idcomprobante='"+ idcomprobante+"'").executeUpdate();
	}
	public void apuntarComprobanteAnotadebito(int idcomprobante,int idnotadebito,double mora) {
		getSessionFactory().getCurrentSession().createSQLQuery("update  Comprobantepago set idnotadebito='"+idnotadebito+"',sigeneronotadebito=1,moradetectada="+mora+" WHERE idcomprobante='"+ idcomprobante+"'").executeUpdate();
	}
	public void apuntarComprobanteAnotadebitoPenalizacion(int idcomprobante,int idnotadebito,double montopenalizacion) {
		getSessionFactory().getCurrentSession().createSQLQuery("update  Comprobantepago set idnotadebitopenalizacion='"+idnotadebito+"' , sigeneronotadebito=1 , sigeneropenalizacion=1,montopenalizacion="+montopenalizacion+" WHERE idcomprobante='"+ idcomprobante+"'").executeUpdate();
	}
	@Transactional(readOnly=true)
	public Double sumarComprobanteCartera(String nrocartera,String desde, String hasta) {
		String query= "",query1 = "",query2= "",query3= "";
		
		if(!nrocartera.equals("")){
			query1 = "ctr.numero= '"+nrocartera+"' and ";
		}
		if(!desde.equals("")){
			query2 = "cp.fechacancelacion>='"+desde+ "' and ";
		}
		if(!hasta.equals("")){
			query3 = "cp.fechacancelacion<='"+hasta+"' and ";
		}
		
		
		query = "select sum(cp.totalsoles) from Comprobantepago cp  inner join cp.detallecartera dc  inner join dc.cartera ctr  inner join dc.contrato c inner join cp.tipodocu td where 1=1 and "+query1+query2+query3+" td.idtipodocu<>'00' and 1=1 ";
		Double suma = (Double) sessionFactory.getCurrentSession().createQuery(query).uniqueResult();
		
		if (suma==null) {
			 return 0.0;
		} else {
			return suma;
		}
		
		
		}
	
	@Transactional(readOnly=true)
	public double sumarComprobanteNrodocumento(String nrocontrato,
			String desde, String hasta) {
	String query= "",query1 = "",query2= "",query3= "";
		System.out.println("nrocontrato="+nrocontrato);
		if(!nrocontrato.equals("")){
			//COMENTADO 26-02-2019 
			//query1 = "c.nrocontrato= '"+nrocontrato+"' and ";
			query1 = "cp.nroseriecomprobante like '%"+nrocontrato+"%' and ";
		}
		if(!desde.equals("")){
			query2 = "cp.fechacancelacion>='"+desde+ "' and ";
		}
		if(!hasta.equals("")){
			query3 = "cp.fechacancelacion<='"+hasta+"' and ";
		}
		
		
		query = "select sum(cp.totalsoles) from Comprobantepago cp  inner join cp.detallecartera dc  inner join dc.cartera ctr  inner join dc.contrato c inner join cp.tipodocu td where 1=1 and "+query1+query2+query3+" td.idtipodocu<>'00' and 1=1 ";
		Double suma = (Double) sessionFactory.getCurrentSession().createQuery(query).uniqueResult();
		
		if (suma==null) {
			 return 0.0;
		} else {
			return suma;
		}
		
		
	}
	//nuevo metodo para sumar monto de comprobantes buscados  24-02-2019 
		@Transactional(readOnly=true)
		public double sumarComprobanteCobrador(Integer idusuario,String desde, String hasta,String tipoconsulta,List<String> selectedComprobanteTabCobrador,boolean selectedSiAnuladoTabCobrador,boolean selectedSiRechazadoTabCobrador) {
					String query= "",query1 = "",query2= "",query3= "",query4= "",query5= "";
					List<ComprobanteBean> lista = new ArrayList<ComprobanteBean>();

					/////////////////////////
					if(idusuario!=99999){
						query1 = "u.idusuario= '"+idusuario+"' and ";
					}
					
					if(!desde.equals("")){
						query2 = "cp.fechacancelacion>='"+desde+ "' and ";
					}
		
					if(!hasta.equals("")){
						query3 = "cp.fechacancelacion<'"+hasta+"' ";
					}
					if (selectedComprobanteTabCobrador.size()==0) {
						//query4 = "and (td.idtipodocu='01' or td.idtipodocu='03' or td.idtipodocu='04' or td.idtipodocu='08' or td.idtipodocu='09' )";
						query4 = "and (td.idtipodocu='01' or td.idtipodocu='03'  or td.idtipodocu='08' or td.idtipodocu='09' )";
					}else{
						for(int i=0;i<selectedComprobanteTabCobrador.size();i++){
							if(i<1){
								query4=query4+"and ( td.idtipodocu='"+selectedComprobanteTabCobrador.get(i)+"'";
							}else{
								query4=query4+" or td.idtipodocu='"+selectedComprobanteTabCobrador.get(i)+"'";
							}
							
							
						}
						query4=query4+") ";
						System.out.println("query4="+query4);
					}
					
					if (!selectedSiAnuladoTabCobrador) {
						if(!selectedSiRechazadoTabCobrador){
							query5 = query5+" and cp.sianulado='0' and (cp.estado='1' or cp.estado is null ) ";
						}else{
						query5 = query5+" and (cp.estado is null or cp.estado=1 or cp.estado='3' ) " ; }
					}else{
						if(!selectedSiRechazadoTabCobrador){
							query5=query5 + " and (cp.estado is null or cp.estado=1 or cp.estado='2' )";
						}else{
							//query5=query5 + " and (cp.estado='1' or cp.estado is null )";
						}
					}
					//query = "select sum(cp.totalsoles) from Comprobantepago cp  inner join cp.tipodocu td inner join cp.usuario u where 1=1 and "+query1+query2+query3+" td.idtipodocu<>'00' and 1=1 ";
					query = "select sum(cp.totalsoles) from Comprobantepago cp  inner join cp.tipodocu td inner join cp.usuario u where 1=1 and "+query1+query2+query3+query4+query5+" and 1=1 ";
					Double suma = (Double) sessionFactory.getCurrentSession().createQuery(query).uniqueResult();
					
					if (suma==null) {
						System.out.println("SUMA="+0.0);
						return 0.0;
					} else {
						suma= FuncionesHelper.redondearNum(suma, 2);
						System.out.println("SUMA="+suma);
						return suma;
					}
					
		}
		
	@SuppressWarnings("unchecked")
	
	@Transactional(readOnly=true)
	public List<ComprobanteBean> buscarComprobante(Integer idusuario,String claveupa,String nombreusuario,String nombreinquilino,String nrodocumento, String desde, String hasta,String tipoconsulta,List<String> selectedComprobanteTabCobrador,boolean selectedSiAnuladoTabCobrador) {
		
		List<ComprobanteBean> lista = new ArrayList<ComprobanteBean>();
		String query1 = "",query2= "",query3= "",query4= "",query5= "";
		
		if (tipoconsulta.equals("inquilino")) {
			if(!nombreinquilino.equals("")){
				query1 = "i.nombrescompletos like '%"+nombreinquilino+"%' and ";
			}
			if(!desde.equals("")){
				query2 = "cp.fechacancelacion>='"+desde+ "' and ";
			}
			if(!hasta.equals("")){
				query3 = "cp.fechacancelacion<='"+hasta+"' and td.idtipodocu<>'00'";
			}
		}else if (tipoconsulta.equals("documento")) {
			
			if(!nrodocumento.equals("")){
				query3 = "cp.nroseriecomprobante like '%"+nrodocumento+"%'";
			}
			
		}else if (tipoconsulta.equals("upa")) {
			
			if(desde.equals("") && hasta.equals("") ){
				query1 = "u.clave= '"+claveupa+"' ";
			}
			
			if(!desde.equals("") && !hasta.equals("")){
				query1 = "u.clave= '"+claveupa+"' and cp.fechacancelacion>='"+desde+ "' and cp.fechacancelacion<'"+hasta+"' ";
			}
		}else if (tipoconsulta.equals("Doc.Administrativo")) {
			
			if(desde.equals("") && hasta.equals("") ){
				query1 = "u.clave= '"+claveupa+"' ";
			}
			
			if(!desde.equals("") && !hasta.equals("")){
				query1 = "u.clave= '"+claveupa+"' and cp.fechacancelacion>='"+desde+ "' and cp.fechacancelacion<'"+hasta+"' ";
			}
			if (selectedComprobanteTabCobrador.size()==0) {
				query4 = "and (td.idtipodocu='01' or td.idtipodocu='03' or td.idtipodocu='04' or td.idtipodocu='08' or td.idtipodocu='09' )";
				
			}else{
				for(int i=0;i<selectedComprobanteTabCobrador.size();i++){
					if(i<1){
						query4=query4+"and ( td.idtipodocu='"+selectedComprobanteTabCobrador.get(i)+"'";
					}else{
						query4=query4+" or td.idtipodocu='"+selectedComprobanteTabCobrador.get(i)+"'";
					}
					
					
				}
				query4=query4+") ";
				
			}
		}else if (tipoconsulta.equals("usuario")) {
			query1 = "cp.usuariocreador= '"+nombreusuario+"' and ";

			if(!desde.equals("")){
				query2 = "cp.fechacreacion>='"+desde+ "' and ";
			}
			if(!hasta.equals("")){
				query3 = "cp.fechacreacion<'"+hasta+"' ";
			}
		}else if (tipoconsulta.equals(Constante.TIPO_CONSULTA_DETRACCION)) {
			query1 = "cp.sidetraccion=1 and cp.montodetraccion<>0 and cp.montodetraccion>0 and td.idtipodocu='01' and ";

			if(!desde.equals("")){
				query2 = "cp.fechacancelacion>='"+desde+ "' and ";
			}
			if(!hasta.equals("")){
				query3 = "cp.fechacancelacion<'"+hasta+"' ";
			}
		}else if (tipoconsulta.equals(Constante.TIPO_CONSULTA_AUTODETRACCION)) {
			query1 = "cp.siautodetraccion=1 and cp.montoautodetraccion<>0 and (td.idtipodocu='01' or  td.idtipodocu='09') and ";

			if(!desde.equals("")){
				query2 = "cp.fechacancelacion>='"+desde+ "' and ";
			}
			if(!hasta.equals("")){
				query3 = "cp.fechacancelacion<'"+hasta+"' ";
			}
		}else if (tipoconsulta.equals("cobrador")) {
			
			if(idusuario!=99999){
				query1 = "usr.idusuario= '"+idusuario+"' and ";
			}
			if(!desde.equals("")){
				query2 = "cp.fechacancelacion>='"+desde+ "' and ";
			}
			if(!hasta.equals("")){
				query3 = "cp.fechacancelacion<'"+hasta+"' and td.idtipodocu<>'00' ";
			}
			
			
			if (selectedComprobanteTabCobrador.size()==0) {
				query4 = "and (td.idtipodocu='01' or td.idtipodocu='03' or td.idtipodocu='04' or td.idtipodocu='08' or td.idtipodocu='09' )";
				
			}else{
				for(int i=0;i<selectedComprobanteTabCobrador.size();i++){
					if(i<1){
						query4=query4+"and ( td.idtipodocu='"+selectedComprobanteTabCobrador.get(i)+"'";
					}else{
						query4=query4+" or td.idtipodocu='"+selectedComprobanteTabCobrador.get(i)+"'";
					}
					
					
				}
				query4=query4+") ";
				
			}
		}else if (tipoconsulta.equals("cobradorFE")) {
			
			if(idusuario!=99999){
				query1 = "usr.idusuario= '"+idusuario+"' and ";
			}
			if(!desde.equals("")){
				query2 = "cp.fechaemision>='"+desde+ "' and ";
			}
			if(!hasta.equals("")){
				query3 = "cp.fechaemision<'"+hasta+"' and td.idtipodocu<>'00' ";
			}
			
			
			if (selectedComprobanteTabCobrador.size()==0) {
				query4 = "and (td.idtipodocu='01' or td.idtipodocu='03' or td.idtipodocu='04' or td.idtipodocu='08' or td.idtipodocu='09' )";
				
			}else{
				for(int i=0;i<selectedComprobanteTabCobrador.size();i++){
					if(i<1){
						query4=query4+"and ( td.idtipodocu='"+selectedComprobanteTabCobrador.get(i)+"'";
					}else{
						query4=query4+" or td.idtipodocu='"+selectedComprobanteTabCobrador.get(i)+"'";
					}
				}
				query4=query4+") ";
				System.out.println("query4="+query4);
			}
			query5="and cp.sifacturacionelectronica='1' and cp.fechacancelacion is null ";
		}
		
		if (!selectedSiAnuladoTabCobrador) {
			query5 = query5+" and cp.sianulado='0' ";
		}
		
		
		
		StringBuilder hql = new StringBuilder();
		StringBuilder hqlU = new StringBuilder();	
		StringBuilder hqlUArb = new StringBuilder();	
		
		hql.append("select cp.idcomprobante as idcomprobante,cp.nroserie as nroserie,cp.nrocomprobante as nrocomprobante,cp.nroseriecomprobante as nroseriecomprobante,cp.sianulado as sianulado,");
		hql.append("td.idtipodocu as idtipdocu,tp.idtipopago as idtippago,tm.idtipomovimiento as idtipmovimiento, tc.descripcion as concepto, tc.idtipoconcepto as idtipconcepto,u.clave as claveUpa,i.nombrescompletos as nombreInquilino,i.ruc as ruc,tper.idtipopersona as idtipopersona,i.dni as dni,");	
		hql.append("cp.fechaemision as fecEmision,cp.fechacancelacion as fecCancelacion,cp.fechavencimiento as fecVencimiento,");
		hql.append("cp.subtotalsoles as subtotal,cp.totalsoles as total,cp.igvsoles as igv,cp.moradetectada as mora,cp.observacion  as observacion,");
		hql.append("usr.nombrescompletos as nombrecobrador,usr.idusuario as idcobrador, cp.usuariocreador as usrcre,cp.fechacreacion as feccre,cp.numerooperacion as numerooperacion,");
		hql.append("dc.iddetallecartera as iddetallecartera,");
		hql.append("cp.nrocomprobantepadre as nrocomprobanteref,ref.nroseriecomprobante as nronotadebitoref, cp.sifacturacionelectronica as sifacturacionelectronica,");
		hql.append("c.condicion as condicion,c.nrocontrato as nrocontrato,c.siocupante as siocupante,c.sinombreocupante as nombreocupante,c.sidniocupante as dniocupante ,substring(cart.numero,8,3) as nrocartera,");
		hql.append("(inm.direccion+' '+inm.numeroprincipal+' '+u.nombrenuminterior) as direccion,");
		hql.append("cp.tipodescuentonotacredito as tipodescuentonotacredito, ");
		hql.append("cp.comp_opc as comp_opc,cp.sidetraccion as sidetraccion,cp.montodetraccion as montodetraccion , cp.siautodetraccion as siautodetraccion,cp.montoautodetraccion as montoautodetraccion ");
		hql.append(" from Comprobantepago cp ");
		hql.append("inner join cp.detallecartera dc ");
		hql.append("inner join dc.cartera cart ");
		hql.append("inner join  dc.contrato c ");
		hql.append("inner join  c.upa u ");
		hql.append("inner join  u.inmueble inm ");
		hql.append("inner join  c.inquilino i ");
		hql.append("inner join  i.tipopersona tper ");
		hql.append("inner join  cp.tipodocu td ");
		hql.append("inner join  cp.tipomovimiento tm ");
		hql.append("inner join  cp.tipoconcepto tc ");
		hql.append("inner join  cp.tipopago tp ");
		hql.append("inner join  cp.usuario usr ");
		hql.append("left join   cp.notadebito ref ");
//		hql.append("left join   cp.notacredito refcre ");
//		hql.append("left join   cp.notacredito refcre2 ");
		hql.append("where 1=1 and "+query1+query2+query3+query4+query5+" and 1=1 order by ");
		if (tipoconsulta.equals(Constante.TIPO_CONSULTA_AUTODETRACCION)||tipoconsulta.equals(Constante.TIPO_CONSULTA_DETRACCION)){
			hql.append(" u.clave,td.idtipodocu , cp.nroseriecomprobante asc ");
		}else{
			hql.append(" cp.nroseriecomprobante asc ");
		}
		hqlU.append("select cp.idcomprobante as idcomprobante,cp.nroserie as nroserie,cp.nrocomprobante as nrocomprobante,cp.nroseriecomprobante as nroseriecomprobante,cp.sianulado as sianulado,");
		hqlU.append("td.idtipodocu as idtipdocu,tp.idtipopago as idtippago,tm.idtipomovimiento as idtipmovimiento,tc.idtipoconcepto as idtipconcepto,tc.descripcion as concepto,");	
		hqlU.append("cp.fechaemision as fecEmision,cp.fechacancelacion as fecCancelacion,cp.numerooperacion as numerooperacion,");
		hqlU.append("cp.subtotalsoles as subtotal,cp.totalsoles as total,cp.igvsoles as igv,cp.moradetectada as mora,");
		hqlU.append("usr.nombrescompletos as nombrecobrador,cp.usuariocreador as usrcre,cp.fechacreacion as feccre,cp.nombrepagante as nombreInquilino,");
		hqlU.append("cp.nrocomprobantepadre as nrocomprobanteref,ref.nroseriecomprobante as nronotadebitoref,cp.sifacturacionelectronica as sifacturacionelectronica,");
		hqlU.append("cp.tipodescuentonotacredito as tipodescuentonotacredito,");
		hqlU.append("cp.comp_opc as comp_opc,cp.obs_direccion as direccion, ");
		hqlU.append("cp.sidetraccion as sidetraccion ,cp.montodetraccion as montodetraccion ,cp.siautodetraccion as siautodetraccion,cp.montoautodetraccion as montoautodetraccion ");
		hqlU.append(" from Comprobantepago cp ");
		hqlU.append("inner join  cp.tipodocu td ");
		hqlU.append("inner join  cp.tipomovimiento tm ");
		hqlU.append("inner join  cp.tipopago tp ");
		hqlU.append("inner join  cp.tipoconcepto tc ");
		hqlU.append("inner join  cp.usuario usr ");
		hqlU.append("left join   cp.notadebito ref ");
//		hqlU.append("left join   cp.notacredito refcre ");
//		hqlU.append("left join   cp.notacredito2 refcre2 ");
		hqlU.append("where 1=1 and ");
		
		if (!selectedSiAnuladoTabCobrador) {
			query5 = "and cp.sianulado='0' ";
		}
		
		//hqlU.append(query1+query2+query3+query5+" and ");
		hqlU.append(query1+query2+query3+query4+query5+" and ");
//		hqlU.append(" cp.detallecartera.iddetallecartera is null and 1=1  order by cp.nroseriecomprobante asc");
		hqlU.append(" cp.detallecartera.iddetallecartera is null and 1=1  and cp.tipoconcepto.idtipoconcepto<>'"+Constante.TIPO_CONCEPTO_REC_A+"' order by ");
		if (tipoconsulta.equals(Constante.TIPO_CONSULTA_AUTODETRACCION)||tipoconsulta.equals(Constante.TIPO_CONSULTA_DETRACCION)){
			hqlU.append(" td.idtipodocu , cp.nroseriecomprobante asc ");
		}else{
			hqlU.append(" cp.nroseriecomprobante asc ");
		}
		//arbitrio
		hqlUArb.append("select distinct cp.idcomprobante as idcomprobante,cp.nroserie as nroserie,cp.nrocomprobante as nrocomprobante,cp.nroseriecomprobante as nroseriecomprobante,cp.sianulado as sianulado,");
		hqlUArb.append("td.idtipodocu as idtipdocu,tp.idtipopago as idtippago,tm.idtipomovimiento as idtipmovimiento,tc.idtipoconcepto as idtipconcepto,tc.descripcion as concepto,");	
		hqlUArb.append("cp.fechaemision as fecEmision,cp.fechacancelacion as fecCancelacion,cp.numerooperacion as numerooperacion,");
		hqlUArb.append("cp.subtotalsoles as subtotal,cp.totalsoles as total,cp.igvsoles as igv,cp.moradetectada as mora,");
		hqlUArb.append("usr.nombrescompletos as nombrecobrador,cp.usuariocreador as usrcre,cp.fechacreacion as feccre,cp.nombrepagante as nombreInquilino,");
		hqlUArb.append("cp.nrocomprobantepadre as nrocomprobanteref,ref.nroseriecomprobante as nronotadebitoref,cp.sifacturacionelectronica as sifacturacionelectronica ");	
		hqlUArb.append(" ,u.clave as claveUpa, (u.direccion+' '+u.numprincipal+' '+u.nombrenuminterior) as direccion, ");
		hqlUArb.append("cp.sidetraccion as sidetraccion , cp.montodetraccion as montodetraccion ,cp.siautodetraccion as siautodetraccion,cp.montoautodetraccion as montoautodetraccion ");
		hqlUArb.append(" from Comprobantepago cp ");
		hqlUArb.append("inner join  cp.detallecuota_arbitrio dca ");
		hqlUArb.append("inner join  dca.cuota_arbitrio cuo ");
		hqlUArb.append("inner join  cuo.arbitrio ar ");
		hqlUArb.append("inner join  ar.upa u ");
		hqlUArb.append("inner join  cp.tipodocu td ");
		hqlUArb.append("inner join  cp.tipomovimiento tm ");
		hqlUArb.append("inner join  cp.tipopago tp ");
		hqlUArb.append("inner join  cp.tipoconcepto tc ");
		hqlUArb.append("inner join  cp.usuario usr ");
		hqlUArb.append("left join   cp.notadebito ref ");
//		hqlUArb.append("left join   cp.notacredito refcre ");
//		hqlUArb.append("left join   cp.notacredito2 refcre2 ");
		hqlUArb.append("where 1=1 and ");
		
		if (!selectedSiAnuladoTabCobrador) {
			query5 = "and cp.sianulado='0' ";
		}
		
		hqlUArb.append(query1+query2+query3+query4+query5+" and ");
		hqlUArb.append(" cp.detallecartera.iddetallecartera is null and 1=1 and cp.tipoconcepto.idtipoconcepto='"+Constante.TIPO_CONCEPTO_REC_A+"' order by ");
		if (tipoconsulta.equals(Constante.TIPO_CONSULTA_AUTODETRACCION)||tipoconsulta.equals(Constante.TIPO_CONSULTA_DETRACCION)){
			hqlUArb.append(" u.clave,td.idtipodocu , cp.nroseriecomprobante asc ");
		}else{
			hqlUArb.append(" cp.nroseriecomprobante asc ");
		}
		//fin de arbitrio

		Query query= sessionFactory.getCurrentSession().createQuery(hql.toString());
		lista = query.setResultTransformer(Transformers.aliasToBean(ComprobanteBean.class)).list();
		
        System.out.println("query : "+query);
		if (selectedComprobanteTabCobrador.size() != 0) {
			if (tipoconsulta.equals("cobrador")	|| tipoconsulta.equals("usuario")|| tipoconsulta.equals("documento")|| tipoconsulta.equals(Constante.TIPO_CONSULTA_DETRACCION)||tipoconsulta.equals(Constante.TIPO_CONSULTA_AUTODETRACCION)) {

				Query queryU = sessionFactory.getCurrentSession().createQuery(
						hqlU.toString());
				lista.addAll(queryU.setResultTransformer(
						Transformers.aliasToBean(ComprobanteBean.class)).list());
				Query queryArb= sessionFactory.getCurrentSession().createQuery(hqlUArb.toString());
				lista.addAll(queryArb.setResultTransformer(
						Transformers.aliasToBean(ComprobanteBean.class)).list());
			}
			
		}
		return lista;
	}
	@SuppressWarnings("unchecked")
	
	@Transactional(readOnly=true)
	public List<ComprobanteBean> buscarComprobante(Integer idusuario,String claveupa,String nombreusuario,String nombreinquilino,String nrodocumento, String desde, String hasta,String tipoconsulta,List<String> selectedComprobanteTabCobrador,boolean selectedSiAnuladoTabCobrador,boolean selectedSiRechazadoTabCobrador) {
		
		List<ComprobanteBean> lista = new ArrayList<ComprobanteBean>();
		String query1 = "",query2= "",query3= "",query4= "",query5= "";
		
		if (tipoconsulta.equals("inquilino")) {
			if(!nombreinquilino.equals("")){
				query1 = "i.nombrescompletos like '%"+nombreinquilino+"%' and ";
			}
			if(!desde.equals("")){
				query2 = "cp.fechacancelacion>='"+desde+ "' and ";
			}
			if(!hasta.equals("")){
				query3 = "cp.fechacancelacion<='"+hasta+"' and td.idtipodocu<>'00'";
			}
		}else if (tipoconsulta.equals("documento")) {
			
			if(!nrodocumento.equals("")){
				query3 = "cp.nroseriecomprobante like '%"+nrodocumento+"%'";
			}
			
		}else if (tipoconsulta.equals("upa")) {
			
			if(desde.equals("") && hasta.equals("") ){
				query1 = "u.clave= '"+claveupa+"' ";
			}
			
			if(!desde.equals("") && !hasta.equals("")){
				query1 = "u.clave= '"+claveupa+"' and cp.fechacancelacion>='"+desde+ "' and cp.fechacancelacion<'"+hasta+"' ";
			}
		}else if (tipoconsulta.equals("Doc.Administrativo")) {
			
			if(desde.equals("") && hasta.equals("") ){
				query1 = "u.clave= '"+claveupa+"' ";
			}
			
			if(!desde.equals("") && !hasta.equals("")){
				query1 = "u.clave= '"+claveupa+"' and cp.fechacancelacion>='"+desde+ "' and cp.fechacancelacion<'"+hasta+"' ";
			}
			if (selectedComprobanteTabCobrador.size()==0) {
				query4 = "and (td.idtipodocu='01' or td.idtipodocu='03' or td.idtipodocu='04' or td.idtipodocu='08' or td.idtipodocu='09' )";
				
			}else{
				for(int i=0;i<selectedComprobanteTabCobrador.size();i++){
					if(i<1){
						query4=query4+"and ( td.idtipodocu='"+selectedComprobanteTabCobrador.get(i)+"'";
					}else{
						query4=query4+" or td.idtipodocu='"+selectedComprobanteTabCobrador.get(i)+"'";
					}
					
					
				}
				query4=query4+") ";
				
			}
		}else if (tipoconsulta.equals("usuario")) {
			query1 = "cp.usuariocreador= '"+nombreusuario+"' and ";

			if(!desde.equals("")){
				query2 = "cp.fechacreacion>='"+desde+ "' and ";
			}
			if(!hasta.equals("")){
				query3 = "cp.fechacreacion<'"+hasta+"' ";
			}
		}else if (tipoconsulta.equals(Constante.TIPO_CONSULTA_DETRACCION)) {
			query1 = "cp.sidetraccion=1 and cp.montodetraccion<>0 and cp.montodetraccion>0 and td.idtipodocu='01' and ";

			if(!desde.equals("")){
				query2 = "cp.fechacancelacion>='"+desde+ "' and ";
			}
			if(!hasta.equals("")){
				query3 = "cp.fechacancelacion<'"+hasta+"' ";
			}
		}else if (tipoconsulta.equals(Constante.TIPO_CONSULTA_AUTODETRACCION)) {
			query1 = "cp.siautodetraccion=1 and cp.montoautodetraccion<>0 and (td.idtipodocu='01' or  td.idtipodocu='09') and ";

			if(!desde.equals("")){
				query2 = "cp.fechacancelacion>='"+desde+ "' and ";
			}
			if(!hasta.equals("")){
				query3 = "cp.fechacancelacion<'"+hasta+"' ";
			}
		}else if (tipoconsulta.equals("cobrador")) {
			
			if(idusuario!=99999){
				query1 = "usr.idusuario= '"+idusuario+"' and ";
			}
			if(!desde.equals("")){
				query2 = "cp.fechacancelacion>='"+desde+ "' and ";
			}
			if(!hasta.equals("")){
				query3 = "cp.fechacancelacion<'"+hasta+"' and td.idtipodocu<>'00' ";
			}
			
			
			if (selectedComprobanteTabCobrador.size()==0) {
				query4 = "and (td.idtipodocu='01' or td.idtipodocu='03' or td.idtipodocu='04' or td.idtipodocu='08' or td.idtipodocu='09' )";
				
			}else{
				for(int i=0;i<selectedComprobanteTabCobrador.size();i++){
					if(i<1){
						query4=query4+"and ( td.idtipodocu='"+selectedComprobanteTabCobrador.get(i)+"'";
					}else{
						query4=query4+" or td.idtipodocu='"+selectedComprobanteTabCobrador.get(i)+"'";
					}
					
					
				}
				query4=query4+") ";
				
			}
		}else if (tipoconsulta.equals("cobradorFE")) {
			
			if(idusuario!=99999){
				query1 = "usr.idusuario= '"+idusuario+"' and ";
			}
			if(!desde.equals("")){
				query2 = "cp.fechaemision>='"+desde+ "' and ";
			}
			if(!hasta.equals("")){
				query3 = "cp.fechaemision<'"+hasta+"' and td.idtipodocu<>'00' ";
			}
			
			
			if (selectedComprobanteTabCobrador.size()==0) {
				query4 = "and (td.idtipodocu='01' or td.idtipodocu='03' or td.idtipodocu='04' or td.idtipodocu='08' or td.idtipodocu='09' )";
				
			}else{
				for(int i=0;i<selectedComprobanteTabCobrador.size();i++){
					if(i<1){
						query4=query4+"and ( td.idtipodocu='"+selectedComprobanteTabCobrador.get(i)+"'";
					}else{
						query4=query4+" or td.idtipodocu='"+selectedComprobanteTabCobrador.get(i)+"'";
					}
				}
				query4=query4+") ";
				System.out.println("query4="+query4);
			}
			query5="and cp.sifacturacionelectronica='1' and cp.fechacancelacion is null ";
		}
		
		if (!selectedSiAnuladoTabCobrador) {
			if(!selectedSiRechazadoTabCobrador){
				query5 = query5+" and cp.sianulado='0' and (cp.estado='1' or cp.estado is null ) ";
			}else{
			query5 = query5+" and (cp.estado is null or cp.estado=1 or cp.estado='3' ) " ; }
		}else{
			if(!selectedSiRechazadoTabCobrador){
				query5=query5 + " and (cp.estado is null or cp.estado=1 or cp.estado='2' )";
			}else{
				//query5=query5 + " and (cp.estado='1' or cp.estado is null )";
			}
		}
		
		
		
		StringBuilder hql = new StringBuilder();
		StringBuilder hqlU = new StringBuilder();	
		StringBuilder hqlUArb = new StringBuilder();	
		
		hql.append("select cp.idcomprobante as idcomprobante,cp.nroserie as nroserie,cp.nrocomprobante as nrocomprobante,cp.nroseriecomprobante as nroseriecomprobante,cp.sianulado as sianulado,");
		hql.append("td.idtipodocu as idtipdocu,tp.idtipopago as idtippago,tm.idtipomovimiento as idtipmovimiento, tc.descripcion as concepto, tc.idtipoconcepto as idtipconcepto,u.clave as claveUpa,i.nombrescompletos as nombreInquilino,i.ruc as ruc,tper.idtipopersona as idtipopersona,i.dni as dni,td.tipDocu as  tip_docu ,tp.tipPago as tip_pago ,");	
		hql.append("cp.fechaemision as fecEmision,cp.fechacancelacion as fecCancelacion,cp.fechavencimiento as fecVencimiento,cp.dnirucpagante as dnirucpagante ,cp.textoimportetotal as textoimportetotal ,cp.glosario as glosario,");
		hql.append("cp.subtotalsoles as subtotal,cp.totalsoles as total,cp.igvsoles as igv,cp.moradetectada as mora,cp.observacion  as observacion,cp.nombreusr_cre as nombreusr_cre ,tmp.descripciontipo as descripciontipomodalidadpago, ");
		hql.append("usr.nombrescompletos as nombrecobrador,usr.idusuario as idcobrador, cp.usuariocreador as usrcre,cp.fechacreacion as feccre,cp.usuariomodificador as usrmod, cp.fechamodificacion as fecmod,cp.usuario_anula_documento as usr_anula_documento,cp.fecha_anula_documento as fec_anula_documento,cp.numerooperacion as numerooperacion,");
		hql.append("dc.iddetallecartera as iddetallecartera,cp.textoimportetotal as textoimportetotal ,cp.observacion as observacion ,cp.nombreusr_cre as nombreusr_cre,cp.sidocumento as sidocumento ,");
		hql.append("cp.nrocomprobantepadre as nrocomprobanteref,ref.nroseriecomprobante as nronotadebitoref, cp.sifacturacionelectronica as sifacturacionelectronica,");
		hql.append("c.condicion as condicion,c.nrocontrato as nrocontrato,c.siocupante as siocupante,c.sinombreocupante as nombreocupante,c.sidniocupante as dniocupante ,substring(cart.numero,8,3) as nrocartera,");
		hql.append("(inm.direccion+' '+inm.numeroprincipal+' '+u.nombrenuminterior) as direccion,");
		hql.append("cp.tipodescuentonotacredito as tipodescuentonotacredito, ");
		hql.append("cp_ref.fechaemision as fecEmisionref, ");
		hql.append("cp.comp_opc as comp_opc,cp.sidetraccion as sidetraccion,cp.montodetraccion as montodetraccion , cp.siautodetraccion as siautodetraccion,cp.montoautodetraccion as montoautodetraccion ,cp.estado as estado  ");
		hql.append(" from Comprobantepago cp ");
		hql.append("inner join cp.detallecartera dc ");
		hql.append("inner join dc.cartera cart ");
		hql.append("inner join  dc.contrato c ");
		hql.append("inner join  c.upa u ");
		hql.append("inner join  u.inmueble inm ");
		hql.append("inner join  c.inquilino i ");
		hql.append("inner join  i.tipopersona tper ");
		hql.append("inner join  cp.tipodocu td ");
		hql.append("inner join  cp.tipomovimiento tm ");
		hql.append("inner join  cp.tipoconcepto tc ");
		hql.append("inner join  cp.tipopago tp ");
		hql.append("inner join  cp.usuario usr ");
		hql.append("left join   cp.notadebito ref ");
		hql.append("left join   cp.tipomodalidadpago tmp ");
		hql.append("left join   cp.comprobanteref cp_ref ");
//		hql.append("left join   cp.notacredito refcre ");
//		hql.append("left join   cp.notacredito refcre2 ");
		hql.append("where 1=1 and "+query1+query2+query3+query4+query5+" and 1=1 order by ");
		if (tipoconsulta.equals(Constante.TIPO_CONSULTA_AUTODETRACCION)||tipoconsulta.equals(Constante.TIPO_CONSULTA_DETRACCION)){
			hql.append(" u.clave,td.idtipodocu , cp.nroseriecomprobante asc ");
		}else{
			hql.append(" cp.nroseriecomprobante asc ");
		}
		hqlU.append("select cp.idcomprobante as idcomprobante,cp.nroserie as nroserie,cp.nrocomprobante as nrocomprobante,cp.nroseriecomprobante as nroseriecomprobante,cp.sianulado as sianulado,");
		hqlU.append("td.idtipodocu as idtipdocu,tp.idtipopago as idtippago,tm.idtipomovimiento as idtipmovimiento,tc.idtipoconcepto as idtipconcepto,tc.descripcion as concepto,td.tipDocu as  tip_docu ,tp.tipPago as tip_pago ,");	
		hqlU.append("cp.fechaemision as fecEmision,cp.fechacancelacion as fecCancelacion,cp.numerooperacion as numerooperacion,tmp.descripciontipo as descripciontipomodalidadpago,");
		hqlU.append("cp.subtotalsoles as subtotal,cp.totalsoles as total,cp.igvsoles as igv,cp.moradetectada as mora,cp.glosario as glosario,cp.observacion as observacion ,cp.sidocumento as sidocumento , ");
		hqlU.append("usr.nombrescompletos as nombrecobrador,cp.usuariocreador as usrcre,cp.fechacreacion as feccre,cp.textoimportetotal as textoimportetotal ,cp.nombreusr_cre as nombreusr_cre,");
		hqlU.append("cp.usuariomodificador as usrmod, cp.fechamodificacion as fecmod,cp.usuario_anula_documento as usr_anula_documento,cp.fecha_anula_documento as fec_anula_documento,cp.nombrepagante as nombreInquilino,cp.dnirucpagante as dnirucpagante ,cp.textoimportetotal as textoimportetotal ,");
		hqlU.append("cp.nrocomprobantepadre as nrocomprobanteref,ref.nroseriecomprobante as nronotadebitoref,cp.sifacturacionelectronica as sifacturacionelectronica,");
		hqlU.append("cp.tipodescuentonotacredito as tipodescuentonotacredito,");
		hqlU.append("cp.comp_opc as comp_opc,cp.obs_direccion as direccion, ");
		hqlU.append("cp_ref.fechaemision as fecEmisionref, ");
		hqlU.append("cp.sidetraccion as sidetraccion ,cp.montodetraccion as montodetraccion ,cp.siautodetraccion as siautodetraccion,cp.montoautodetraccion as montoautodetraccion ,cp.estado as estado ");
		hqlU.append(" from Comprobantepago cp ");
		hqlU.append("inner join  cp.tipodocu td ");
		hqlU.append("inner join  cp.tipomovimiento tm ");
		hqlU.append("inner join  cp.tipopago tp ");
		hqlU.append("inner join  cp.tipoconcepto tc ");
		hqlU.append("inner join  cp.usuario usr ");
		hqlU.append("left join   cp.notadebito ref ");
		hqlU.append("left join   cp.tipomodalidadpago tmp ");
		hqlU.append("left join   cp.comprobanteref cp_ref ");
//		hqlU.append("left join   cp.notacredito refcre ");
//		hqlU.append("left join   cp.notacredito2 refcre2 ");
		hqlU.append("where 1=1 and ");
		
//		if (!selectedSiAnuladoTabCobrador) {
//			query5 = "and cp.sianulado='0' ";
//		}
//		if(!selectedSiRechazadoTabCobrador){
//			query5 = " and cp.estado='RECHAZADO'" ;
//		}
		
		//hqlU.append(query1+query2+query3+query5+" and ");
		hqlU.append(query1+query2+query3+query4+query5+" and ");
//		hqlU.append(" cp.detallecartera.iddetallecartera is null and 1=1  order by cp.nroseriecomprobante asc");
		hqlU.append(" cp.detallecartera.iddetallecartera is null and 1=1  and cp.tipoconcepto.idtipoconcepto<>'"+Constante.TIPO_CONCEPTO_REC_A+"' order by ");
		if (tipoconsulta.equals(Constante.TIPO_CONSULTA_AUTODETRACCION)||tipoconsulta.equals(Constante.TIPO_CONSULTA_DETRACCION)){
			hqlU.append(" td.idtipodocu , cp.nroseriecomprobante asc ");
		}else{
			hqlU.append(" cp.nroseriecomprobante asc ");
		}
		//arbitrio
		hqlUArb.append("select distinct cp.idcomprobante as idcomprobante,cp.nroserie as nroserie,cp.nrocomprobante as nrocomprobante,cp.nroseriecomprobante as nroseriecomprobante,cp.sianulado as sianulado,");
		hqlUArb.append("td.idtipodocu as idtipdocu,tp.idtipopago as idtippago,tm.idtipomovimiento as idtipmovimiento,tc.idtipoconcepto as idtipconcepto,tc.descripcion as concepto,td.tipDocu as  tip_docu ,tp.tipPago as tip_pago ,");	
		hqlUArb.append("cp.fechaemision as fecEmision,cp.fechacancelacion as fecCancelacion,cp.numerooperacion as numerooperacion,tmp.descripciontipo as descripciontipomodalidadpago,");
		hqlUArb.append("cp.subtotalsoles as subtotal,cp.totalsoles as total,cp.igvsoles as igv,cp.moradetectada as mora,cp.glosario as glosario, cp.observacion as observacion ,cp.sidocumento as sidocumento ,");
		hqlUArb.append("usr.nombrescompletos as nombrecobrador,cp.usuariocreador as usrcre,cp.fechacreacion as feccre,cp.usuariomodificador as usrmod, cp.fechamodificacion as fecmod,");
		hqlUArb.append("cp.usuario_anula_documento as usr_anula_documento,cp.fecha_anula_documento as fec_anula_documento,cp.nombrepagante as nombreInquilino,cp.dnirucpagante as dnirucpagante ,cp.textoimportetotal as textoimportetotal ,");
		hqlUArb.append("cp.nrocomprobantepadre as nrocomprobanteref,ref.nroseriecomprobante as nronotadebitoref,cp.sifacturacionelectronica as sifacturacionelectronica ");	
		hqlUArb.append(" ,u.clave as claveUpa, cp.obs_direccion as direccion, cp.nombreusr_cre as nombreusr_cre,");
		hqlUArb.append("cp_ref.fechaemision as fecEmisionref, ");
		hqlUArb.append("cp.sidetraccion as sidetraccion , cp.montodetraccion as montodetraccion ,cp.siautodetraccion as siautodetraccion,cp.montoautodetraccion as montoautodetraccion ,cp.estado as estado");
		hqlUArb.append(" from Comprobantepago cp ");
		hqlUArb.append("inner join  cp.detallecuota_arbitrio dca ");
		hqlUArb.append("inner join  dca.cuota_arbitrio cuo ");
		hqlUArb.append("inner join  cuo.arbitrio ar ");
		hqlUArb.append("inner join  ar.upa u ");
		hqlUArb.append("inner join  cp.tipodocu td ");
		hqlUArb.append("inner join  cp.tipomovimiento tm ");
		hqlUArb.append("inner join  cp.tipopago tp ");
		hqlUArb.append("inner join  cp.tipoconcepto tc ");
		hqlUArb.append("inner join  cp.usuario usr ");
		hqlUArb.append("left join   cp.notadebito ref ");
		hqlUArb.append("left join   cp.tipomodalidadpago tmp ");
		hqlUArb.append("left join   cp.comprobanteref cp_ref ");
//		hqlUArb.append("left join   cp.notacredito refcre ");
//		hqlUArb.append("left join   cp.notacredito2 refcre2 ");
		hqlUArb.append("where 1=1 and ");
		
//		if (!selectedSiAnuladoTabCobrador) {
//			query5 = "and cp.sianulado='0' ";
//		}
		
		hqlUArb.append(query1+query2+query3+query4+query5+" and ");
		hqlUArb.append(" cp.detallecartera.iddetallecartera is null and 1=1 and cp.tipoconcepto.idtipoconcepto='"+Constante.TIPO_CONCEPTO_REC_A+"' order by ");
		if (tipoconsulta.equals(Constante.TIPO_CONSULTA_AUTODETRACCION)||tipoconsulta.equals(Constante.TIPO_CONSULTA_DETRACCION)){
			hqlUArb.append(" u.clave,td.idtipodocu , cp.nroseriecomprobante asc ");
		}else{
			hqlUArb.append(" cp.nroseriecomprobante asc ");
		}
		//fin de arbitrio
		System.out.println("hql.toString()= "+hql.toString());
		Query query= sessionFactory.getCurrentSession().createQuery(hql.toString());
		lista = query.setResultTransformer(Transformers.aliasToBean(ComprobanteBean.class)).list();
		
        System.out.println("query : "+query);
		if (selectedComprobanteTabCobrador.size() != 0) {
			if (tipoconsulta.equals("cobrador")	|| tipoconsulta.equals("usuario")|| tipoconsulta.equals("documento")|| tipoconsulta.equals(Constante.TIPO_CONSULTA_DETRACCION)||tipoconsulta.equals(Constante.TIPO_CONSULTA_AUTODETRACCION)) {

				Query queryU = sessionFactory.getCurrentSession().createQuery(hqlU.toString());
				System.out.println("hqlU.toString()= "+hqlU.toString());
				lista.addAll(queryU.setResultTransformer(Transformers.aliasToBean(ComprobanteBean.class)).list());
				Query queryArb= sessionFactory.getCurrentSession().createQuery(hqlUArb.toString());
				System.out.println("hqlUArb.toString()= "+hqlUArb.toString());
				lista.addAll(queryArb.setResultTransformer(Transformers.aliasToBean(ComprobanteBean.class)).list());
			}
			
		}
		return lista;
	}
@SuppressWarnings("unchecked")
	
	@Transactional(readOnly=true)
	public List<ComprobanteBean> buscarComprobante(Comprobantepago cp) {
		
		List<ComprobanteBean> lista = new ArrayList<ComprobanteBean>();
		
		
		StringBuilder hql = new StringBuilder();
		StringBuilder hqlU = new StringBuilder();	
		StringBuilder hqlUArb = new StringBuilder();	
		
		hql.append("select cp.idcomprobante as idcomprobante,cp.nroserie as nroserie,cp.nrocomprobante as nrocomprobante,cp.nroseriecomprobante as nroseriecomprobante,cp.sianulado as sianulado,");
		hql.append("td.idtipodocu as idtipdocu,tp.idtipopago as idtippago,tm.idtipomovimiento as idtipmovimiento, tc.descripcion as concepto, tc.idtipoconcepto as idtipconcepto,u.clave as claveUpa,i.nombrescompletos as nombreInquilino,i.ruc as ruc,tper.idtipopersona as idtipopersona,i.dni as dni,td.tipDocu as  tip_docu ,tp.tipPago as tip_pago ,");	
		hql.append("cp.fechaemision as fecEmision,cp.fechacancelacion as fecCancelacion,cp.fechavencimiento as fecVencimiento,cp.dnirucpagante as dnirucpagante ,cp.textoimportetotal as textoimportetotal ,cp.glosario as glosario,");
		hql.append("cp.subtotalsoles as subtotal,cp.totalsoles as total,cp.igvsoles as igv,cp.moradetectada as mora,cp.observacion  as observacion,cp.nombreusr_cre as nombreusr_cre ,tmp.descripciontipo as descripciontipomodalidadpago, ");
		hql.append("usr.nombrescompletos as nombrecobrador,usr.idusuario as idcobrador, cp.usuariocreador as usrcre,cp.fechacreacion as feccre,cp.usuariomodificador as usrmod, cp.fechamodificacion as fecmod,cp.usuario_anula_documento as usr_anula_documento,cp.fecha_anula_documento as fec_anula_documento,cp.numerooperacion as numerooperacion,");
		hql.append("dc.iddetallecartera as iddetallecartera,cp.textoimportetotal as textoimportetotal ,cp.observacion as observacion ,cp.nombreusr_cre as nombreusr_cre,cp.sidocumento as sidocumento ,");
		hql.append("cp.nrocomprobantepadre as nrocomprobanteref,ref.nroseriecomprobante as nronotadebitoref, cp.sifacturacionelectronica as sifacturacionelectronica,");
		hql.append("c.condicion as condicion,c.nrocontrato as nrocontrato,c.siocupante as siocupante,c.sinombreocupante as nombreocupante,c.sidniocupante as dniocupante ,substring(cart.numero,8,3) as nrocartera,");
		hql.append("(inm.direccion+' '+inm.numeroprincipal+' '+u.nombrenuminterior) as direccion,u.clave as claveUpa,");
		hql.append("cp.tipodescuentonotacredito as tipodescuentonotacredito, ");
		hql.append("cp_ref.fechaemision as fecEmisionref, ");
		hql.append("cp.comp_opc as comp_opc,cp.sidetraccion as sidetraccion,cp.montodetraccion as montodetraccion , cp.siautodetraccion as siautodetraccion,cp.montoautodetraccion as montoautodetraccion ,cp.estado as estado  ");
		hql.append(" from Comprobantepago cp ");
		hql.append("inner join cp.detallecartera dc ");
		hql.append("inner join dc.cartera cart ");
		hql.append("inner join  dc.contrato c ");
		hql.append("inner join  c.upa u ");
		hql.append("inner join  u.inmueble inm ");
		hql.append("inner join  c.inquilino i ");
		hql.append("inner join  i.tipopersona tper ");
		hql.append("inner join  cp.tipodocu td ");
		hql.append("inner join  cp.tipomovimiento tm ");
		hql.append("inner join  cp.tipoconcepto tc ");
		hql.append("inner join  cp.tipopago tp ");
		hql.append("inner join  cp.usuario usr ");
		hql.append("left join   cp.notadebito ref ");
		hql.append("left join   cp.tipomodalidadpago tmp ");
		hql.append("left join   cp.comprobanteref cp_ref ");
//		hql.append("left join   cp.notacredito refcre ");
//		hql.append("left join   cp.notacredito refcre2 ");
		hql.append("where 1=1 and cp.idcomprobante="+cp.getIdcomprobante()+" and cp.estado='1' ");
//		if (tipoconsulta.equals(Constante.TIPO_CONSULTA_AUTODETRACCION)||tipoconsulta.equals(Constante.TIPO_CONSULTA_DETRACCION)){
//			hql.append(" u.clave,td.idtipodocu , cp.nroseriecomprobante asc ");
//		}else{
		//	hql.append(" cp.nroseriecomprobante asc ");
	//	}
		hqlU.append("select cp.idcomprobante as idcomprobante,cp.nroserie as nroserie,cp.nrocomprobante as nrocomprobante,cp.nroseriecomprobante as nroseriecomprobante,cp.sianulado as sianulado,");
		hqlU.append("td.idtipodocu as idtipdocu,tp.idtipopago as idtippago,tm.idtipomovimiento as idtipmovimiento,tc.idtipoconcepto as idtipconcepto,tc.descripcion as concepto,td.tipDocu as  tip_docu ,tp.tipPago as tip_pago ,");	
		hqlU.append("cp.fechaemision as fecEmision,cp.fechacancelacion as fecCancelacion,cp.numerooperacion as numerooperacion,tmp.descripciontipo as descripciontipomodalidadpago,");
		hqlU.append("cp.subtotalsoles as subtotal,cp.totalsoles as total,cp.igvsoles as igv,cp.moradetectada as mora,cp.glosario as glosario,cp.observacion as observacion ,cp.sidocumento as sidocumento , ");
		hqlU.append("usr.nombrescompletos as nombrecobrador,cp.usuariocreador as usrcre,cp.fechacreacion as feccre,cp.textoimportetotal as textoimportetotal ,cp.nombreusr_cre as nombreusr_cre,");
		hqlU.append("cp.usuariomodificador as usrmod, cp.fechamodificacion as fecmod,cp.usuario_anula_documento as usr_anula_documento,cp.fecha_anula_documento as fec_anula_documento,cp.nombrepagante as nombreInquilino,cp.dnirucpagante as dnirucpagante ,cp.textoimportetotal as textoimportetotal ,");
		hqlU.append("cp.nrocomprobantepadre as nrocomprobanteref,ref.nroseriecomprobante as nronotadebitoref,cp.sifacturacionelectronica as sifacturacionelectronica,");
		hqlU.append("cp.tipodescuentonotacredito as tipodescuentonotacredito,");
		hqlU.append("cp.comp_opc as comp_opc,cp.obs_direccion as direccion, ");
		hqlU.append("cp_ref.fechaemision as fecEmisionref, ");
		hqlU.append("cp.sidetraccion as sidetraccion ,cp.montodetraccion as montodetraccion ,cp.siautodetraccion as siautodetraccion,cp.montoautodetraccion as montoautodetraccion ,cp.estado as estado ");
		hqlU.append(" from Comprobantepago cp ");
		hqlU.append("inner join  cp.tipodocu td ");
		hqlU.append("inner join  cp.tipomovimiento tm ");
		hqlU.append("inner join  cp.tipopago tp ");
		hqlU.append("inner join  cp.tipoconcepto tc ");
		hqlU.append("inner join  cp.usuario usr ");
		hqlU.append("left join   cp.notadebito ref ");
		hqlU.append("left join   cp.tipomodalidadpago tmp ");
		hqlU.append("left join   cp.comprobanteref cp_ref ");
//		hqlU.append("left join   cp.notacredito refcre ");
//		hqlU.append("left join   cp.notacredito2 refcre2 ");
		hqlU.append("where 1=1 and cp.idcomprobante="+cp.getIdcomprobante()+" and cp.estado='1' and ");
		hqlU.append(" cp.detallecartera.iddetallecartera is null and 1=1  and cp.tipoconcepto.idtipoconcepto<>'"+Constante.TIPO_CONCEPTO_REC_A+"' ");
		//arbitrio
		hqlUArb.append("select distinct cp.idcomprobante as idcomprobante,cp.nroserie as nroserie,cp.nrocomprobante as nrocomprobante,cp.nroseriecomprobante as nroseriecomprobante,cp.sianulado as sianulado,");
		hqlUArb.append("td.idtipodocu as idtipdocu,tp.idtipopago as idtippago,tm.idtipomovimiento as idtipmovimiento,tc.idtipoconcepto as idtipconcepto,tc.descripcion as concepto,td.tipDocu as  tip_docu ,tp.tipPago as tip_pago ,");	
		hqlUArb.append("cp.fechaemision as fecEmision,cp.fechacancelacion as fecCancelacion,cp.numerooperacion as numerooperacion,tmp.descripciontipo as descripciontipomodalidadpago,");
		hqlUArb.append("cp.subtotalsoles as subtotal,cp.totalsoles as total,cp.igvsoles as igv,cp.moradetectada as mora,cp.glosario as glosario, cp.observacion as observacion ,");
		hqlUArb.append("usr.nombrescompletos as nombrecobrador,cp.usuariocreador as usrcre,cp.fechacreacion as feccre,cp.usuariomodificador as usrmod, cp.fechamodificacion as fecmod,");
		hqlUArb.append("cp.usuario_anula_documento as usr_anula_documento,cp.fecha_anula_documento as fec_anula_documento,cp.nombrepagante as nombreInquilino,cp.dnirucpagante as dnirucpagante ,cp.textoimportetotal as textoimportetotal ,");
		hqlUArb.append("cp.nrocomprobantepadre as nrocomprobanteref,ref.nroseriecomprobante as nronotadebitoref,cp.sifacturacionelectronica as sifacturacionelectronica ");	
		//hqlUArb.append(" ,u.clave as claveUpa, (u.direccion+' '+u.numprincipal+' '+u.nombrenuminterior) as direccion, cp.nombreusr_cre as nombreusr_cre,cp.sidocumento as sidocumento ,");
		hqlUArb.append(", u.clave as claveUpa , cp.obs_direccion as direccion, cp.nombreusr_cre as nombreusr_cre,cp.sidocumento as sidocumento ,");
		hqlUArb.append("cp_ref.fechaemision as fecEmisionref, ");
		hqlUArb.append("cp.sidetraccion as sidetraccion , cp.montodetraccion as montodetraccion ,cp.siautodetraccion as siautodetraccion,cp.montoautodetraccion as montoautodetraccion ,cp.estado as estado");
		hqlUArb.append(" from Comprobantepago cp ");
		hqlUArb.append("inner join  cp.detallecuota_arbitrio dca ");
		hqlUArb.append("inner join  dca.cuota_arbitrio cuo ");
		hqlUArb.append("inner join  cuo.arbitrio ar ");
		hqlUArb.append("inner join  ar.upa u ");
		hqlUArb.append("inner join  cp.tipodocu td ");
		hqlUArb.append("inner join  cp.tipomovimiento tm ");
		hqlUArb.append("inner join  cp.tipopago tp ");
		hqlUArb.append("inner join  cp.tipoconcepto tc ");
		hqlUArb.append("inner join  cp.usuario usr ");
		hqlUArb.append("left join   cp.notadebito ref ");
		hqlUArb.append("left join   cp.tipomodalidadpago tmp ");
		hqlUArb.append("left join   cp.comprobanteref cp_ref ");
		
//		hqlUArb.append("left join   cp.notacredito refcre ");
//		hqlUArb.append("left join   cp.notacredito2 refcre2 ");
		hqlUArb.append("where 1=1 and cp.idcomprobante="+cp.getIdcomprobante()+" and cp.estado='1' and ");
		hqlUArb.append(" cp.detallecartera.iddetallecartera is null and 1=1 and cp.tipoconcepto.idtipoconcepto='"+Constante.TIPO_CONCEPTO_REC_A+"' ");

		//fin de arbitrio

		Query query= sessionFactory.getCurrentSession().createQuery(hql.toString());
		lista = query.setResultTransformer(Transformers.aliasToBean(ComprobanteBean.class)).list();
		
        System.out.println("query : "+query);

				Query queryU = sessionFactory.getCurrentSession().createQuery(
						hqlU.toString());
				lista.addAll(queryU.setResultTransformer(
						Transformers.aliasToBean(ComprobanteBean.class)).list());
				Query queryArb= sessionFactory.getCurrentSession().createQuery(hqlUArb.toString());
				lista.addAll(queryArb.setResultTransformer(
						Transformers.aliasToBean(ComprobanteBean.class)).list());
	
		
		return lista;
	}
    @SuppressWarnings("unchecked")
	
	@Transactional(readOnly=true)
	public List<ComprobanteBean> buscarComprobanteEspecial(Integer idusuario,String claveupa,String nombreusuario,String nombreinquilino,String nrodocumento, String desde, String hasta,String tipoconsulta,List<String> selectedComprobanteTabCobrador,boolean selectedSiAnuladoTabCobrador,boolean selectedSiRechazadoTabCobrador) {
		
		List<ComprobanteBean> lista = new ArrayList<ComprobanteBean>();
		String query1 = "",query2= "",query3= "",query4= "",query5= "";
		
		if (tipoconsulta.equals("inquilino")) {
			if(!nombreinquilino.equals("")){
				query1 = "i.nombrescompletos like '%"+nombreinquilino+"%' and ";
			}
			if(!desde.equals("")){
				query2 = "cp.fechacancelacion>='"+desde+ "' and ";
			}
			if(!hasta.equals("")){
				query3 = "cp.fechacancelacion<='"+hasta+"' and td.idtipodocu<>'00'";
			}
		}else if (tipoconsulta.equals("documento")) {
			
			if(!nrodocumento.equals("")){
				query3 = "cp.nroseriecomprobante like '%"+nrodocumento+"%'";
			}
			
		}else if (tipoconsulta.equals("upa")) {
			
			if(desde.equals("") && hasta.equals("") ){
				query1 = "u.clave= '"+claveupa+"' ";
			}
			
			if(!desde.equals("") && !hasta.equals("")){
				query1 = "u.clave= '"+claveupa+"' and cp.fechacancelacion>='"+desde+ "' and cp.fechacancelacion<'"+hasta+"' ";
			}
		}else if (tipoconsulta.equals("Doc.Administrativo")) {
			
			if(desde.equals("") && hasta.equals("") ){
				query1 = "u.clave= '"+claveupa+"' ";
			}
			
			if(!desde.equals("") && !hasta.equals("")){
				query1 = "u.clave= '"+claveupa+"' and cp.fechacancelacion>='"+desde+ "' and cp.fechacancelacion<'"+hasta+"' ";
			}
			if (selectedComprobanteTabCobrador.size()==0) {
				query4 = "and (td.idtipodocu='01' or td.idtipodocu='03' or td.idtipodocu='04' or td.idtipodocu='08' or td.idtipodocu='09' )";
				
			}else{
				for(int i=0;i<selectedComprobanteTabCobrador.size();i++){
					if(i<1){
						query4=query4+"and ( td.idtipodocu='"+selectedComprobanteTabCobrador.get(i)+"'";
					}else{
						query4=query4+" or td.idtipodocu='"+selectedComprobanteTabCobrador.get(i)+"'";
					}
					
					
				}
				query4=query4+") ";
				
			}
		}else if (tipoconsulta.equals("usuario")) {
			query1 = "cp.usuariocreador= '"+nombreusuario+"' and ";

			if(!desde.equals("")){
				query2 = "cp.fechacreacion>='"+desde+ "' and ";
			}
			if(!hasta.equals("")){
				query3 = "cp.fechacreacion<'"+hasta+"' ";
			}
		}else if (tipoconsulta.equals("cobrador")) {
			
			if(idusuario!=99999){
				query1 = "usr.idusuario= '"+idusuario+"' and ";
			}
			if(!desde.equals("")){
				query2 = "cp.fechacancelacion>='"+desde+ "' and ";
			}
			if(!hasta.equals("")){
				query3 = "cp.fechacancelacion<'"+hasta+"' and td.idtipodocu<>'00' ";
			}
			
			
			if (selectedComprobanteTabCobrador.size()==0) {
				query4 = "and (td.idtipodocu='01' or td.idtipodocu='03' or td.idtipodocu='04' or td.idtipodocu='08' or td.idtipodocu='09' )";
				
			}else{
				for(int i=0;i<selectedComprobanteTabCobrador.size();i++){
					if(i<1){
						query4=query4+"and ( td.idtipodocu='"+selectedComprobanteTabCobrador.get(i)+"'";
					}else{
						query4=query4+" or td.idtipodocu='"+selectedComprobanteTabCobrador.get(i)+"'";
					}
					
					
				}
				query4=query4+") ";
				
			}
		}else if (tipoconsulta.equals("cobradorFE")) {
			
			if(idusuario!=99999){
				query1 = "usr.idusuario= '"+idusuario+"' and ";
			}
			if(!desde.equals("")){
				query2 = "cp.fechaemision>='"+desde+ "' and ";
			}
			if(!hasta.equals("")){
				query3 = "cp.fechaemision<'"+hasta+"' and td.idtipodocu<>'00' ";
			}
			
			
			if (selectedComprobanteTabCobrador.size()==0) {
				query4 = "and (td.idtipodocu='01' or td.idtipodocu='03' or td.idtipodocu='04' or td.idtipodocu='08' or td.idtipodocu='09' )";
				
			}else{
				for(int i=0;i<selectedComprobanteTabCobrador.size();i++){
					if(i<1){
						query4=query4+"and ( td.idtipodocu='"+selectedComprobanteTabCobrador.get(i)+"'";
					}else{
						query4=query4+" or td.idtipodocu='"+selectedComprobanteTabCobrador.get(i)+"'";
					}
				}
				query4=query4+") ";
				System.out.println("query4="+query4);
			}
			query5="and cp.sifacturacionelectronica='1' and cp.fechacancelacion is null ";
		}
		
		if (!selectedSiAnuladoTabCobrador) {
			if(!selectedSiRechazadoTabCobrador){
				query5 = query5+" and cp.sianulado='0' and (cp.estado='1' or cp.estado is null ) ";
			}else{
			query5 = query5+" and (cp.estado is null or cp.estado=1 or cp.estado='3' ) " ; }
		}else{
			if(!selectedSiRechazadoTabCobrador){
				query5=query5 + " and (cp.estado is null or cp.estado=1 or cp.estado='2' )";
			}else{
				//query5=query5 + " and (cp.estado='1' or cp.estado is null )";
			}
		}
		StringBuilder hql = new StringBuilder();
		StringBuilder hqlU = new StringBuilder();	
		StringBuilder hqlUArb = new StringBuilder();	
		
		hql.append("select cp.idcomprobante as idcomprobante,cp.nroserie as nroserie,cp.nrocomprobante as nrocomprobante,cp.nroseriecomprobante as nroseriecomprobante,cp.sianulado as sianulado,");
		hql.append("td.idtipodocu as idtipdocu,tp.idtipopago as idtippago,tm.idtipomovimiento as idtipmovimiento, tc.descripcion as concepto, tc.idtipoconcepto as idtipconcepto,u.clave as claveUpa,i.nombrescompletos as nombreInquilino,i.ruc as ruc,tper.idtipopersona as idtipopersona,i.dni as dni,");	
		hql.append("cp.fechaemision as fecEmision,cp.fechacancelacion as fecCancelacion,cp.fechavencimiento as fecVencimiento,");
		hql.append("cp.subtotalsoles as subtotal,cp.totalsoles as total,cp.igvsoles as igv,cp.moradetectada as mora,cp.observacion  as observacion,");
		hql.append("usr.nombrescompletos as nombrecobrador,usr.idusuario as idcobrador, cp.usuariocreador as usrcre,cp.fechacreacion as feccre,cp.numerooperacion as numerooperacion,");
		hql.append("dc.iddetallecartera as iddetallecartera,");
		hql.append("cp.nrocomprobantepadre as nrocomprobanteref,ref.nroseriecomprobante as nronotadebitoref,cp.sifacturacionelectronica as sifacturacionelectronica,");
		hql.append("c.condicion as condicion,c.nrocontrato as nrocontrato,c.siocupante as siocupante,c.sinombreocupante as nombreocupante,c.sidniocupante as dniocupante ,substring(cart.numero,8,3) as nrocartera,");
		hql.append("(inm.direccion+' '+inm.numeroprincipal+' '+u.nombrenuminterior) as direccion,");
		hql.append("cp.tipodescuentonotacredito as tipodescuentonotacredito, ");
		hql.append("cp.comp_opc as comp_opc ");
		hql.append(" from Comprobantepago cp ");
		hql.append("inner join cp.detallecartera dc ");
		hql.append("inner join dc.cartera cart ");
		hql.append("inner join  dc.contrato c ");
		hql.append("inner join  c.upa u ");
		hql.append("inner join  u.inmueble inm ");
		hql.append("inner join  c.inquilino i ");
		hql.append("inner join  i.tipopersona tper ");
		hql.append("inner join  cp.tipodocu td ");
		hql.append("inner join  cp.tipomovimiento tm ");
		hql.append("inner join  cp.tipoconcepto tc ");
		hql.append("inner join  cp.tipopago tp ");
		hql.append("inner join  cp.usuario usr ");
		hql.append("left join   cp.notadebito ref ");
		hql.append("where 1=1 and cp.comp_opc='D' and "+query1+query2+query3+query4+query5+" and 1=1  order by cp.nroseriecomprobante asc");
		
		hqlU.append("select cp.idcomprobante as idcomprobante,cp.nroserie as nroserie,cp.nrocomprobante as nrocomprobante,cp.nroseriecomprobante as nroseriecomprobante,cp.sianulado as sianulado,");
		hqlU.append("td.idtipodocu as idtipdocu,tp.idtipopago as idtippago,tm.idtipomovimiento as idtipmovimiento,tc.idtipoconcepto as idtipconcepto,tc.descripcion as concepto,");	
		hqlU.append("cp.fechaemision as fecEmision,cp.fechacancelacion as fecCancelacion,cp.numerooperacion as numerooperacion,");
		hqlU.append("cp.subtotalsoles as subtotal,cp.totalsoles as total,cp.igvsoles as igv,cp.moradetectada as mora,");
		hqlU.append("usr.nombrescompletos as nombrecobrador,cp.usuariocreador as usrcre,cp.fechacreacion as feccre,cp.nombrepagante as nombreInquilino,");
		hqlU.append("cp.nrocomprobantepadre as nrocomprobanteref,ref.nroseriecomprobante as nronotadebitoref,cp.sifacturacionelectronica as sifacturacionelectronica,");
		hqlU.append("cp.tipodescuentonotacredito as tipodescuentonotacredito,");
		hqlU.append("cp.comp_opc as comp_opc, ");
		hqlU.append("cp.obs_direccion as direccion ");
		hqlU.append(" from Comprobantepago cp ");
		hqlU.append("inner join  cp.tipodocu td ");
		hqlU.append("inner join  cp.tipomovimiento tm ");
		hqlU.append("inner join  cp.tipopago tp ");
		hqlU.append("inner join  cp.tipoconcepto tc ");
		hqlU.append("inner join  cp.usuario usr ");
		hqlU.append("left join   cp.notadebito ref ");
		hqlU.append("where 1=1 and cp.comp_opc='D' and ");
		
//		if (!selectedSiAnuladoTabCobrador) {
//			query5 = "and cp.sianulado='0' ";
//		}
		
		//hqlU.append(query1+query2+query3+query5+" and ");
		hqlU.append(query1+query2+query3+query4+query5+" and ");
//		hqlU.append(" cp.detallecartera.iddetallecartera is null and 1=1  order by cp.nroseriecomprobante asc");
		hqlU.append(" cp.detallecartera.iddetallecartera is null and 1=1  and cp.tipoconcepto.idtipoconcepto<>'"+Constante.TIPO_CONCEPTO_REC_A+"' order by cp.nroseriecomprobante asc");
		//arbitrio
		hqlUArb.append("select distinct cp.idcomprobante as idcomprobante,cp.nroserie as nroserie,cp.nrocomprobante as nrocomprobante,cp.nroseriecomprobante as nroseriecomprobante,cp.sianulado as sianulado,");
		hqlUArb.append("td.idtipodocu as idtipdocu,tp.idtipopago as idtippago,tm.idtipomovimiento as idtipmovimiento,tc.idtipoconcepto as idtipconcepto,tc.descripcion as concepto,");	
		hqlUArb.append("cp.fechaemision as fecEmision,cp.fechacancelacion as fecCancelacion,cp.numerooperacion as numerooperacion,");
		hqlUArb.append("cp.subtotalsoles as subtotal,cp.totalsoles as total,cp.igvsoles as igv,cp.moradetectada as mora,");
		hqlUArb.append("usr.nombrescompletos as nombrecobrador,cp.usuariocreador as usrcre,cp.fechacreacion as feccre,cp.nombrepagante as nombreInquilino,");
		hqlUArb.append("cp.nrocomprobantepadre as nrocomprobanteref,ref.nroseriecomprobante as nronotadebitoref,cp.sifacturacionelectronica as sifacturacionelectronica ");	
		hqlUArb.append(" ,u.clave as claveUpa, (u.direccion+' '+u.numprincipal+' '+u.nombrenuminterior) as direccion ");
		hqlUArb.append(" from Comprobantepago cp ");
		hqlUArb.append("inner join  cp.detallecuota_arbitrio dca ");
		hqlUArb.append("inner join  dca.cuota_arbitrio cuo ");
		hqlUArb.append("inner join  cuo.arbitrio ar ");
		hqlUArb.append("inner join  ar.upa u ");
		hqlUArb.append("inner join  cp.tipodocu td ");
		hqlUArb.append("inner join  cp.tipomovimiento tm ");
		hqlUArb.append("inner join  cp.tipopago tp ");
		hqlUArb.append("inner join  cp.tipoconcepto tc ");
		hqlUArb.append("inner join  cp.usuario usr ");
		hqlUArb.append("left join   cp.notadebito ref ");
		hqlUArb.append("where 1=1 and cp.comp_opc='D' and  ");
		
//		if (!selectedSiAnuladoTabCobrador) {
//			query5 = "and cp.sianulado='0' ";
//		}
		
		hqlUArb.append(query1+query2+query3+query4+query5+" and ");
		hqlUArb.append(" cp.detallecartera.iddetallecartera is null and 1=1 and cp.tipoconcepto.idtipoconcepto='"+Constante.TIPO_CONCEPTO_REC_A+"' order by cp.nroseriecomprobante asc");
		//fin de arbitrio
		System.out.println("hql.toString()="+hql.toString());
		Query query= sessionFactory.getCurrentSession().createQuery(hql.toString());
		lista = query.setResultTransformer(Transformers.aliasToBean(ComprobanteBean.class)).list();
		
        System.out.println();
		if (selectedComprobanteTabCobrador.size() != 0) {
			if (tipoconsulta.equals("cobrador")	|| tipoconsulta.equals("usuario")|| tipoconsulta.equals("documento")) {
				System.out.println("hqlU.toString()="+hqlU.toString());
				Query queryU = sessionFactory.getCurrentSession().createQuery(hqlU.toString());
				lista.addAll(queryU.setResultTransformer(Transformers.aliasToBean(ComprobanteBean.class)).list());
				System.out.println("hqlUArb.toString()="+hqlUArb.toString());
				Query queryArb= sessionFactory.getCurrentSession().createQuery(hqlUArb.toString());
				lista.addAll(queryArb.setResultTransformer(Transformers.aliasToBean(ComprobanteBean.class)).list());
			}
			
		}
		return lista;
	}
	
@SuppressWarnings("unchecked")

@Transactional(readOnly=true)
public List<ComprobanteBean> buscarDocumento(Integer idusuario,String claveupa,Usuario usuario,String nombreinquilino,String nrodocumento, String desde, String hasta,String tipoconsulta,List<String> selectedComprobanteTabCobrador,Boolean selectedSiDocumentosAnulados,Boolean selectedSiIncluirAnulados,Boolean selectedSiIncluirRechazados) {
	
	List<ComprobanteBean> lista = new ArrayList<ComprobanteBean>();
	String query1 = "",query2= "",query3= "",query4= "",query5= "";
	try{
	if (tipoconsulta.equals("inquilino")) {
		if(!nombreinquilino.equals("")){
			query1 = "i.nombrescompletos like '%"+nombreinquilino+"%' and ";
		}
		if(!desde.equals("")){
			query2 = "cp.fechacancelacion>='"+desde+ "' and ";
		}
		if(!hasta.equals("")){
			query3 = "cp.fechacancelacion<='"+hasta+"' and td.idtipodocu<>'00'";
		}
	}else if (tipoconsulta.equals("documento")) {
		
		if(!nrodocumento.equals("")){
			query3 = "cp.nroseriecomprobante like '%"+nrodocumento+"%'";
		}
		
	}else if (tipoconsulta.equals("upa")) {
		
		if(desde.equals("") && hasta.equals("") ){
			query1 = "u.clave= '"+claveupa+"' ";
		}
		
		if(!desde.equals("") && !hasta.equals("")){
			query1 = "u.clave= '"+claveupa+"' and cp.fechacancelacion>='"+desde+ "' and cp.fechacancelacion<'"+hasta+"' ";
		}
	}else if (tipoconsulta.equals("Doc.Administrativo")) {
		
		if(desde.equals("") && hasta.equals("") ){
			query1 = "u.clave= '"+claveupa+"' ";
		}
		
		if(!desde.equals("") && !hasta.equals("")){
			query1 = "u.clave= '"+claveupa+"' and cp.fechacancelacion>='"+desde+ "' and cp.fechacancelacion<'"+hasta+"' ";
		}
		if (selectedComprobanteTabCobrador.size()==0) {
			query4 = "and (td.idtipodocu='01' or td.idtipodocu='03' or td.idtipodocu='04' or td.idtipodocu='08' or td.idtipodocu='09' )";
			
		}else{
			for(int i=0;i<selectedComprobanteTabCobrador.size();i++){
				if(i<1){
					query4=query4+"and ( td.idtipodocu='"+selectedComprobanteTabCobrador.get(i)+"'";
				}else{
					query4=query4+" or td.idtipodocu='"+selectedComprobanteTabCobrador.get(i)+"'";
				}
				
				
			}
			query4=query4+") ";
			
		}
	}else if (tipoconsulta.equals(Constante.TIPO_CONSULTA_USUARIO)) {
		query1 = "(cp.usuariocreador= '"+usuario.getNombres()+" "+usuario.getApellidopat()+"' or  cp.usuariocreador LIKE SUBSTRING('"+usuario.getNombrescompletos()+"',1,17)+'%' ) and ";

		if(!desde.equals("")){
			query2 = "cp.fechacreacion>='"+desde+ "' and ";
		}
		if(!hasta.equals("")){
			query3 = "cp.fechacreacion<'"+hasta+"' ";
		}
	}else if (tipoconsulta.equals(Constante.TIPO_CONSULTA_DETRACCION)) {
		query1 = "cp.sidetraccion=1 and cp.montodetraccion<>0 and cp.montodetraccion>0 and td.idtipodocu='01' and ";

		if(!desde.equals("")){
			query2 = "cp.fechacancelacion>='"+desde+ "' and ";
		}
		if(!hasta.equals("")){
			query3 = "cp.fechacancelacion<'"+hasta+"' ";
		}
	}else if (tipoconsulta.equals(Constante.TIPO_CONSULTA_AUTODETRACCION)) {
		query1 = "cp.siautodetraccion=1 and cp.montoautodetraccion<>0 and (td.idtipodocu='01' or  td.idtipodocu='09') and ";

		if(!desde.equals("")){
			query2 = "cp.fechacancelacion>='"+desde+ "' and ";
		}
		if(!hasta.equals("")){
			query3 = "cp.fechacancelacion<'"+hasta+"' ";
		}
	}else if (tipoconsulta.equals(Constante.TIPO_CONSULTA_COBRADOR)) {
		
		if(idusuario!=99999){
			query1 = "usr.idusuario= '"+idusuario+"' and ";
		}
		if(!desde.equals("")){
			//query2 = "cp.fechacancelacion>='"+desde+ "' and ";
			query2 = "cp.fechacreacion>='"+desde+ "' and ";
		}
		if(!hasta.equals("")){
			//query3 = "cp.fechacancelacion<'"+hasta+"' and td.idtipodocu<>'00' ";
			query3 = "cp.fechacreacion<'"+hasta+"' and td.idtipodocu<>'00' ";
		}
		
		
		if (selectedComprobanteTabCobrador.size()==0) {
			query4 = "and (td.idtipodocu='01' or td.idtipodocu='03' or td.idtipodocu='04' or td.idtipodocu='08' or td.idtipodocu='09' )";
			
		}else{
			for(int i=0;i<selectedComprobanteTabCobrador.size();i++){
				if(i<1){
					query4=query4+"and ( td.idtipodocu='"+selectedComprobanteTabCobrador.get(i)+"'";
				}else{
					query4=query4+" or td.idtipodocu='"+selectedComprobanteTabCobrador.get(i)+"'";
				}
				
				
			}
			query4=query4+") ";
			
		}
	}else if (tipoconsulta.equals("cobradorFE")) {
		
		if(idusuario!=99999){
			query1 = "usr.idusuario= '"+idusuario+"' and ";
		}
		if(!desde.equals("")){
			query2 = "cp.fechaemision>='"+desde+ "' and ";
		}
		if(!hasta.equals("")){
			query3 = "cp.fechaemision<'"+hasta+"' and td.idtipodocu<>'00' ";
		}
		
		
		if (selectedComprobanteTabCobrador.size()==0) {
			query4 = "and (td.idtipodocu='01' or td.idtipodocu='03' or td.idtipodocu='04' or td.idtipodocu='08' or td.idtipodocu='09' )";
			
		}else{
			for(int i=0;i<selectedComprobanteTabCobrador.size();i++){
				if(i<1){
					query4=query4+"and ( td.idtipodocu='"+selectedComprobanteTabCobrador.get(i)+"'";
				}else{
					query4=query4+" or td.idtipodocu='"+selectedComprobanteTabCobrador.get(i)+"'";
				}
			}
			query4=query4+") ";
			System.out.println("query4="+query4);
		}
		query5="and cp.sifacturacionelectronica='1' and cp.fechacancelacion is null ";
	}
	if (!selectedSiIncluirAnulados) {
		if(!selectedSiIncluirRechazados){
			query5 = query5+" and cp.sianulado='0' and (cp.estado='1' or cp.estado is null ) ";
		}else{
		query5 = query5+" and (cp.estado is null or cp.estado=1 or cp.estado='3' ) " ; }
	}else{
		if(!selectedSiIncluirRechazados){
			query5=query5 + " and (cp.estado is null or cp.estado=1 or cp.estado='2' )";
		}else{
			//query5=query5 + " and (cp.estado='1' or cp.estado is null )";
		}
	}
	
	StringBuilder hql = new StringBuilder();
	StringBuilder hqlU = new StringBuilder();	
	StringBuilder hqlUArb = new StringBuilder();	
	StringBuilder hqlAud = new StringBuilder();
	
	hql.append("select cp.idcomprobante as idcomprobante,cp.nroserie as nroserie,cp.nrocomprobante as nrocomprobante,cp.nroseriecomprobante as nroseriecomprobante,cp.sianulado as sianulado,");
	hql.append("td.idtipodocu as idtipdocu,tp.idtipopago as idtippago,tm.idtipomovimiento as idtipmovimiento, tc.descripcion as concepto, tc.idtipoconcepto as idtipconcepto,u.clave as claveUpa,i.nombrescompletos as nombreInquilino,i.ruc as ruc,tper.idtipopersona as idtipopersona,i.dni as dni,");	
	hql.append("cp.fechaemision as fecEmision,cp.fechacancelacion as fecCancelacion,cp.fechavencimiento as fecVencimiento,");
	hql.append("cp.subtotalsoles as subtotal,cp.totalsoles as total,cp.igvsoles as igv,cp.moradetectada as mora,cp.observacion  as observacion,");
	hql.append("usr.nombrescompletos as nombrecobrador,usr.idusuario as idcobrador, cp.usuariocreador as usrcre,cp.fechacreacion as feccre,cp.numerooperacion as numerooperacion,");
	hql.append("dc.iddetallecartera as iddetallecartera,");
	hql.append("cp.nrocomprobantepadre as nrocomprobanteref,ref.nroseriecomprobante as nronotadebitoref, cp.sifacturacionelectronica as sifacturacionelectronica,");
	hql.append("c.condicion as condicion,c.nrocontrato as nrocontrato,c.siocupante as siocupante,c.sinombreocupante as nombreocupante,c.sidniocupante as dniocupante ,substring(cart.numero,8,3) as nrocartera,");
	hql.append("(inm.direccion+' '+inm.numeroprincipal+' '+u.nombrenuminterior) as direccion,");
	hql.append("cp.tipodescuentonotacredito as tipodescuentonotacredito, ");
	hql.append("cp.comp_opc as comp_opc,cp.sidetraccion as sidetraccion,cp.montodetraccion as montodetraccion , cp.siautodetraccion as siautodetraccion,cp.montoautodetraccion as montoautodetraccion , cp.estado as estado , cp.condicion as condicion ");
	hql.append(" from Comprobantepago cp ");
	hql.append("inner join cp.detallecartera dc ");
	hql.append("inner join dc.cartera cart ");
	hql.append("inner join  dc.contrato c ");
	hql.append("inner join  c.upa u ");
	hql.append("inner join  u.inmueble inm ");
	hql.append("inner join  c.inquilino i ");
	hql.append("inner join  i.tipopersona tper ");
	hql.append("inner join  cp.tipodocu td ");
	hql.append("inner join  cp.tipomovimiento tm ");
	hql.append("inner join  cp.tipoconcepto tc ");
	hql.append("inner join  cp.tipopago tp ");
	hql.append("inner join  cp.usuario usr ");
	hql.append("left join   cp.notadebito ref ");
//	hql.append("left join   cp.notacredito refcre ");
//	hql.append("left join   cp.notacredito refcre2 ");
	hql.append("where 1=1 and "+query1+query2+query3+query4+query5+" and 1=1 order by ");
	if (tipoconsulta.equals(Constante.TIPO_CONSULTA_AUTODETRACCION)||tipoconsulta.equals(Constante.TIPO_CONSULTA_DETRACCION)){
		hql.append(" u.clave,td.idtipodocu , cp.nroseriecomprobante asc ");
	}else{
		hql.append(" cp.nroseriecomprobante asc ");
	}
	
	hqlU.append("select cp.idcomprobante as idcomprobante,cp.nroserie as nroserie,cp.nrocomprobante as nrocomprobante,cp.nroseriecomprobante as nroseriecomprobante,cp.sianulado as sianulado,");
	hqlU.append("td.idtipodocu as idtipdocu,tp.idtipopago as idtippago,tm.idtipomovimiento as idtipmovimiento,tc.idtipoconcepto as idtipconcepto,tc.descripcion as concepto,");	
	hqlU.append("cp.fechaemision as fecEmision,cp.fechacancelacion as fecCancelacion,cp.numerooperacion as numerooperacion,");
	hqlU.append("cp.subtotalsoles as subtotal,cp.totalsoles as total,cp.igvsoles as igv,cp.moradetectada as mora,");
	hqlU.append("usr.nombrescompletos as nombrecobrador,cp.usuariocreador as usrcre,cp.fechacreacion as feccre,cp.nombrepagante as nombreInquilino,");
	hqlU.append("cp.nrocomprobantepadre as nrocomprobanteref,ref.nroseriecomprobante as nronotadebitoref,cp.sifacturacionelectronica as sifacturacionelectronica,");
	hqlU.append("cp.tipodescuentonotacredito as tipodescuentonotacredito,");
	hqlU.append("cp.comp_opc as comp_opc,cp.obs_direccion as direccion, ");
	hqlU.append("cp.sidetraccion as sidetraccion ,cp.montodetraccion as montodetraccion ,cp.siautodetraccion as siautodetraccion,cp.montoautodetraccion as montoautodetraccion , cp.estado as estado , cp.condicion as condicion");
	hqlU.append(" from Comprobantepago cp ");
	hqlU.append("inner join  cp.tipodocu td ");
	hqlU.append("inner join  cp.tipomovimiento tm ");
	hqlU.append("inner join  cp.tipopago tp ");
	hqlU.append("inner join  cp.tipoconcepto tc ");
	hqlU.append("inner join  cp.usuario usr ");
	hqlU.append("left join   cp.notadebito ref ");
//	hqlU.append("left join   cp.notacredito refcre ");
//	hqlU.append("left join   cp.notacredito2 refcre2 ");
	hqlU.append("where 1=1 and ");
	
//	if (!selectedSiIncluirAnulados) {
//		query5 = "and cp.sianulado='0' ";
//	}
	
	//hqlU.append(query1+query2+query3+query5+" and ");
	hqlU.append(query1+query2+query3+query4+query5+" and ");
//	hqlU.append(" cp.detallecartera.iddetallecartera is null and 1=1  order by cp.nroseriecomprobante asc");
	hqlU.append(" cp.detallecartera.iddetallecartera is null and 1=1  and cp.tipoconcepto.idtipoconcepto<>'"+Constante.TIPO_CONCEPTO_REC_A+"' order by ");
	if (tipoconsulta.equals(Constante.TIPO_CONSULTA_AUTODETRACCION)||tipoconsulta.equals(Constante.TIPO_CONSULTA_DETRACCION)){
		hqlU.append(" td.idtipodocu , cp.nroseriecomprobante asc ");
	}else{
		hqlU.append(" cp.nroseriecomprobante asc ");
	}
	//arbitrio
	hqlUArb.append("select distinct cp.idcomprobante as idcomprobante,cp.nroserie as nroserie,cp.nrocomprobante as nrocomprobante,cp.nroseriecomprobante as nroseriecomprobante,cp.sianulado as sianulado,");
	hqlUArb.append("td.idtipodocu as idtipdocu,tp.idtipopago as idtippago,tm.idtipomovimiento as idtipmovimiento,tc.idtipoconcepto as idtipconcepto,tc.descripcion as concepto,");	
	hqlUArb.append("cp.fechaemision as fecEmision,cp.fechacancelacion as fecCancelacion,cp.numerooperacion as numerooperacion,");
	hqlUArb.append("cp.subtotalsoles as subtotal,cp.totalsoles as total,cp.igvsoles as igv,cp.moradetectada as mora,");
	hqlUArb.append("usr.nombrescompletos as nombrecobrador,cp.usuariocreador as usrcre,cp.fechacreacion as feccre,cp.nombrepagante as nombreInquilino,");
	hqlUArb.append("cp.nrocomprobantepadre as nrocomprobanteref,ref.nroseriecomprobante as nronotadebitoref,cp.sifacturacionelectronica as sifacturacionelectronica ");	
	hqlUArb.append(" ,u.clave as claveUpa, (u.direccion+' '+u.numprincipal+' '+u.nombrenuminterior) as direccion, ");
	hqlUArb.append("cp.sidetraccion as sidetraccion , cp.montodetraccion as montodetraccion ,cp.siautodetraccion as siautodetraccion,cp.montoautodetraccion as montoautodetraccion , cp.estado as estado , cp.condicion as condicion ");
	hqlUArb.append(" from Comprobantepago cp ");
	hqlUArb.append("inner join  cp.detallecuota_arbitrio dca ");
	hqlUArb.append("inner join  dca.cuota_arbitrio cuo ");
	hqlUArb.append("inner join  cuo.arbitrio ar ");
	hqlUArb.append("inner join  ar.upa u ");
	hqlUArb.append("inner join  cp.tipodocu td ");
	hqlUArb.append("inner join  cp.tipomovimiento tm ");
	hqlUArb.append("inner join  cp.tipopago tp ");
	hqlUArb.append("inner join  cp.tipoconcepto tc ");
	hqlUArb.append("inner join  cp.usuario usr ");
	hqlUArb.append("left join   cp.notadebito ref ");
//	hqlUArb.append("left join   cp.notacredito refcre ");
//	hqlUArb.append("left join   cp.notacredito2 refcre2 ");
	hqlUArb.append("where 1=1 and ");
	
//	if (!selectedSiIncluirAnulados) {
//		query5 = "and cp.sianulado='0' ";
//	}
	
	hqlUArb.append(query1+query2+query3+query4+query5+" and ");
	hqlUArb.append(" cp.detallecartera.iddetallecartera is null and 1=1 and cp.tipoconcepto.idtipoconcepto='"+Constante.TIPO_CONCEPTO_REC_A+"' order by ");
	if (tipoconsulta.equals(Constante.TIPO_CONSULTA_AUTODETRACCION)||tipoconsulta.equals(Constante.TIPO_CONSULTA_DETRACCION)){
		hqlUArb.append(" u.clave,td.idtipodocu , cp.nroseriecomprobante asc ");
	}else{
		hqlUArb.append(" cp.nroseriecomprobante asc ");
	}
	//fin de arbitrio
	// Auditoria 
	
//	hqlAud.append("select 'ANULADO' as accion_aud,cp.comprobante.idcomprobante as idcomprobante,cp.nroserie as nroserie,cp.nrocomprobante as nrocomprobante,cp.nroseriecomprobante as nroseriecomprobante,cp.sianulado as sianulado,");
//	hqlAud.append("td.idtipodocu as idtipdocu,tp.idtipopago as idtippago,tm.idtipomovimiento as idtipmovimiento, tc.descripcion as concepto, tc.idtipoconcepto as idtipconcepto,u.clave as claveUpa,i.nombrescompletos as nombreInquilino,i.ruc as ruc,tper.idtipopersona as idtipopersona,i.dni as dni,");	
//	hqlAud.append("cp.fechaemision as fecEmision,cp.fechacancelacion as fecCancelacion,cp.fechavencimiento as fecVencimiento,");
//	hqlAud.append("cp.subtotalsoles as subtotal,cp.totalsoles as total,cp.igvsoles as igv,cp.moradetectada as mora,cp.observacion  as observacion,");
//	hqlAud.append("usr.nombrescompletos as nombrecobrador,usr.idusuario as idcobrador, cp.usuariocreador as usrcre,cp.fechacreacion as feccre,cp.numerooperacion as numerooperacion,");
//	hqlAud.append("dc.iddetallecartera as iddetallecartera,");
//	hqlAud.append("cp.nrocomprobantepadre as nrocomprobanteref,ref.nroseriecomprobante as nronotadebitoref, cp.sifacturacionelectronica as sifacturacionelectronica,");
//	hqlAud.append("c.condicion as condicion,c.nrocontrato as nrocontrato,c.siocupante as siocupante,c.sinombreocupante as nombreocupante,c.sidniocupante as dniocupante ,substring(cart.numero,8,3) as nrocartera,");
//	hqlAud.append("(inm.direccion+' '+inm.numeroprincipal+' '+u.nombrenuminterior) as direccion,");
//	hqlAud.append("cp.tipodescuentonotacredito as tipodescuentonotacredito, ");
//	hqlAud.append("cp.comp_opc as comp_opc,cp.sidetraccion as sidetraccion,cp.montodetraccion as montodetraccion , cp.siautodetraccion as siautodetraccion,cp.montoautodetraccion as montoautodetraccion ");
//	hqlAud.append(" from Aud_Comprobantepago cp ");
//	hqlAud.append("left join cp.detallecartera dc ");
//	hqlAud.append("left join dc.cartera cart ");
//	hqlAud.append("left join  dc.contrato c ");
//	hqlAud.append("left join  c.upa u ");
//	hqlAud.append("left join  u.inmueble inm ");
//	hqlAud.append("left join  c.inquilino i ");
//	hqlAud.append("left join  i.tipopersona tper ");
//	hqlAud.append("inner join cp.tipodocu td ");
//	hqlAud.append("inner join cp.tipomovimiento tm ");
//	hqlAud.append("inner join cp.tipoconcepto tc ");
//	hqlAud.append("inner join cp.tipopago tp ");
//	hqlAud.append("left join  cp.usuario usr ");
//	hqlAud.append("left join  cp.notadebito ref ");
//	hqlAud.append("left join  cp.notacredito refcre ");
////	hql.append("left join   cp.notacredito refcre2 ");
//	hqlAud.append("where 1=1 and "+query1+query2+query3+query4+query5+" and  cp.accion_aud='DELETE' and 1=1 order by ");
//	if (tipoconsulta.equals(Constante.TIPO_CONSULTA_AUTODETRACCION)||tipoconsulta.equals(Constante.TIPO_CONSULTA_DETRACCION)){
//		hqlAud.append(" u.clave,td.idtipodocu , cp.nroseriecomprobante asc ");
//	}else{
//		hqlAud.append(" cp.nroseriecomprobante asc ");
//	}
	//fin Auditoria
	
	
	 
	
  
	if (selectedComprobanteTabCobrador.size() != 0) {
		System.out.println("selectedComprobanteTabCobrador="+selectedComprobanteTabCobrador.size());
		if (tipoconsulta.equals("cobrador")	|| tipoconsulta.equals("usuario")|| tipoconsulta.equals("documento")|| tipoconsulta.equals(Constante.TIPO_CONSULTA_DETRACCION)||tipoconsulta.equals(Constante.TIPO_CONSULTA_AUTODETRACCION)) {
           
			
			Query query= sessionFactory.getCurrentSession().createQuery(hql.toString());
			 lista = query.setResultTransformer(Transformers.aliasToBean(ComprobanteBean.class)).list();
			 System.out.println("query : "+query); 
			 
			Query queryU = sessionFactory.getCurrentSession().createQuery(
					hqlU.toString());
			lista.addAll(queryU.setResultTransformer(
					Transformers.aliasToBean(ComprobanteBean.class)).list());
			Query queryArb= sessionFactory.getCurrentSession().createQuery(hqlUArb.toString());
			lista.addAll(queryArb.setResultTransformer(
					Transformers.aliasToBean(ComprobanteBean.class)).list());
			
			
		}
		
		
	}else{
		System.out.println("selectedComprobanteTabCobrador="+selectedComprobanteTabCobrador.size());
	}
	System.out.println("selectedSiAnuladoTabCobrador="+selectedSiIncluirAnulados);

//	if(selectedSiDocumentosAnulados){
//		if(selectedSiIncluirAnulados){
//			Query queryAud=sessionFactory.getCurrentSession().createQuery(hqlAud.toString());
//			lista.addAll(queryAud.setResultTransformer(
//					Transformers.aliasToBean(ComprobanteBean.class)).list()); }
//	}else{
		Query query= sessionFactory.getCurrentSession().createQuery(hql.toString());
		 lista = query.setResultTransformer(Transformers.aliasToBean(ComprobanteBean.class)).list();
		 System.out.println("query : "+query); 
		 
		Query queryU = sessionFactory.getCurrentSession().createQuery(
				hqlU.toString());
		lista.addAll(queryU.setResultTransformer(
				Transformers.aliasToBean(ComprobanteBean.class)).list());
		Query queryArb= sessionFactory.getCurrentSession().createQuery(hqlUArb.toString());
		lista.addAll(queryArb.setResultTransformer(
				Transformers.aliasToBean(ComprobanteBean.class)).list());
		
	
//		if(selectedSiIncluirAnulados){
//			Query queryAud=sessionFactory.getCurrentSession().createQuery(hqlAud.toString());
//			lista.addAll(queryAud.setResultTransformer(
//					Transformers.aliasToBean(ComprobanteBean.class)).list());
//	    }
//	}
	}catch(Exception ex){
		ex.printStackTrace();
		System.out.println(ex.getMessage());
	}
	return lista;
}
	@Transactional(readOnly=true)
	public double sumarComprobanteCobrador(Integer idusuario,String desde, String hasta ,boolean selectedSiAnuladoTabCobrador,boolean selectedSiRechazadoTabCobrador) {
				String query= "",query1 = "",query2= "",query3= "",query5= "";
				
				if(idusuario!=99999){
					query1 = "u.idusuario= '"+idusuario+"' and ";
				}
				
				if(!desde.equals("")){
					query2 = "cp.fechacancelacion>='"+desde+ "' and ";
				}
	
				if(!hasta.equals("")){
					query3 = "cp.fechacancelacion<'"+hasta+"' and ";
				}
				
				if (!selectedSiAnuladoTabCobrador) {
					if(!selectedSiRechazadoTabCobrador){
						query5 = query5+" and cp.sianulado='0' and (cp.estado='1' or cp.estado is null ) ";
					}else{
					query5 = query5+" and (cp.estado is null or cp.estado=1 or cp.estado='3' ) " ; }
				}else{
					if(!selectedSiRechazadoTabCobrador){
						query5=query5 + " and (cp.estado is null or cp.estado=1 or cp.estado='2' )";
					}else{
						//query5=query5 + " and (cp.estado='1' or cp.estado is null )";
					}
				}
				
				
				
				query = "select sum(cp.totalsoles) from Comprobantepago cp  inner join cp.tipodocu td inner join cp.usuario u where 1=1 and "+query1+query2+query3+" td.idtipodocu<>'00' and 1=1 ";
				Double suma = (Double) sessionFactory.getCurrentSession().createQuery(query).uniqueResult();
				
				if (suma==null) {
					System.out.println("SUMA="+0.0);
					return 0.0;
				} else {
					suma=FuncionesHelper.redondearNum(suma, 2);
					System.out.println("SUMA="+suma);
					return suma;
				}
	}

	
	
	@Transactional(readOnly=true)
	public double sumarComprobanteDetraccion(String desde,
			String hasta) {
		
		String query= "",query1 = "",query2= "",query3= "";
		query1 = " cp.sidetraccion=1 and cp.montodetraccion<>0 and cp.montodetraccion>0 and td.idtipodocu='01' and ";

		if(!desde.equals("")){
			query2 = "cp.fechacancelacion>='"+desde+ "' and ";
		}
		if(!hasta.equals("")){
			query3 = "cp.fechacancelacion<='"+hasta+"' and ";
		}
		
		
		query = "select sum(cp.montodetraccion) from Comprobantepago cp  inner join cp.tipodocu td where 1=1 and "+query1+query2+query3+" 1=1 ";
		Double suma = (Double) sessionFactory.getCurrentSession().createQuery(query).uniqueResult();
		System.out.println("suma="+suma);
		
		if (suma==null) {
			 return 0.0;
		} else {
			return suma;
		}
	}
	@Transactional(readOnly=true)
	public double sumarComprobanteAutoDetraccion(String desde,
			String hasta) {
		
		String query= "",query1 = "",query2= "",query3= "";
		query1 = " cp.siautodetraccion=1 and cp.montoautodetraccion<>0 and (td.idtipodocu='01' or td.idtipodocu='09') and ";

		if(!desde.equals("")){
			query2 = "cp.fechacancelacion>='"+desde+ "' and ";
		}
		if(!hasta.equals("")){
			query3 = "cp.fechacancelacion<='"+hasta+"' and ";
		}
		
		
		query = "select sum(cp.montoautodetraccion) from Comprobantepago cp  inner join cp.tipodocu td where 1=1 and "+query1+query2+query3+" 1=1 ";
		Double suma = (Double) sessionFactory.getCurrentSession().createQuery(query).uniqueResult();
		System.out.println("suma="+suma);
		
		if (suma==null) {
			 return 0.0;
		} else {
			return suma;
		}
	}
	
	
	@Transactional(readOnly=true)
	public String obtenerNombreCartera(int iddetallecartera) {
		return (String) sessionFactory.getCurrentSession().createQuery("select cart.numero from Detallecartera dc inner join dc.cartera cart where dc.iddetallecartera='"+iddetallecartera+"'").uniqueResult();
	}
	
	
	
	public void eliminarComprobante(int idcomprobante) {
		settingLog(4, 37,  "Usuario elimino numero de documento "+(String) sessionFactory.getCurrentSession().createQuery("select cp.nroseriecomprobante from Comprobantepago  cp where cp.idcomprobante='"+idcomprobante+"'").uniqueResult());
		getSessionFactory().getCurrentSession().createSQLQuery("delete from Comprobantepago WHERE idcomprobante='"+ idcomprobante+"'").executeUpdate();
	
	
	}
	public void anularComprobante(Comprobantepago cp) {
		settingLog(4, 37,  "Usuario anulo numero de documento "+(String) sessionFactory.getCurrentSession().createQuery("select cp.nroseriecomprobante from Comprobantepago  cp where cp.idcomprobante='"+cp.getIdcomprobante()+"'").uniqueResult());
		//getSessionFactory().getCurrentSession().createSQLQuery("update Comprobantepago WHERE idcomprobante='"+ cp.getIdcomprobante()+"'").executeUpdate();
	
	
	}

	public void anularComprobanteSFE(Sunat_Comprobante scp) {
		//settingLog(4, 37,  "Usuario anulo numero de documento "+(String) sessionFactory.getCurrentSession().createQuery("select cp.nroseriecomprobante from Comprobantepago  cp where cp.idcomprobante='"+scp.getId_referencia()+"'").uniqueResult());
//		getSessionFactory().getCurrentSession().createSQLQuery("update Sunat_Comprobante set Estado='0' ,Observaciones='"+scp.getObservaciones()+"' ,Usuario_Anula_Documento='"+scp.getUsuario_Anula_Documento()+"' , Host_Ip_Anula_Documento='"+scp.getHost_IP_Anula_Documento()+"', Fecha_Anula_Documento= getDate()  where id_comprobante='"+ scp.getId_Comprobante()+"'").executeUpdate();

//		StringBuilder hql = new StringBuilder();
////		StringBuilder hqlRec_A = new StringBuilder();
////		hql.append("update Sunat_Comprobante set Estado='0' ,Observaciones='"+scp.getObservaciones()+"' ,Usuario_Anula_Documento='"+scp.getUsuario_Anula_Documento()+"' , Host_Ip_Anula_Documento='"+scp.getHost_IP_Anula_Documento()+"', Fecha_Anula_Documento= getDate()  where id_comprobante='"+ scp.getId_Comprobante()+"'");	
//		//Query query= sessionFactory.getCurrentSession().createQuery(hql.toString());
//		
		sesion= secondDBSessionFactory.openSession();
		//sesion = sessionFactory.getCurrentSession();
		sesion.beginTransaction();
//		Boolean returnValue = false;
//		try {
//			hql.append("update Sunat_Comprobante set Estado='0' ,Observaciones='"+scp.getObservaciones()+"' ,Usuario_Anula_Documento='"+scp.getUsuario_Anula_Documento()+"' , Host_Ip_Anula_Documento='"+scp.getHost_IP_Anula_Documento()+"', Fecha_Anula_Documento= getDate()  where id_comprobante='"+ scp.getId_Comprobante()+"'");	
//
//		    Query query = sesion.createQuery(hql.toString());
//		    int noOfUpdate = query.executeUpdate();
//		    returnValue = (noOfUpdate > 0);
//		} catch (HibernateException e) {
//		    e.printStackTrace();
//		    sesion.getTransaction().rollback();
//		}
//		sesion.getTransaction().commit();
		///////////////////////////



		
		                   // getSessionFactory().getCurrentSession().createSQLQuery("update Sunat_Comprobante set Estado='0' ,Observaciones='"+scp.getObservaciones()+"' ,Usuario_Anula_Documento='"+scp.getUsuario_Anula_Documento()+"' , Host_Ip_Anula_Documento='"+scp.getHost_IP_Anula_Documento()+"', Fecha_Anula_Documento= getDate()  where id_comprobante='"+ scp.getId_Comprobante()+"'").executeUpdate();

//     getSecondDBSessionFactory().getCurrentSession().createQuery("update Sunat_Comprobante set Estado='0' ,Observaciones='"+scp.getObservaciones()+"' ,Usuario_Anula_Documento='"+scp.getUsuario_Anula_Documento()+"' , Host_Ip_Anula_Documento='"+scp.getHost_IP_Anula_Documento()+"', Fecha_Anula_Documento= getDate()  where id_comprobante='"+ scp.getId_Comprobante()+"'").executeUpdate();	
		 
		try{
			getSecondDBSessionFactory().getCurrentSession().createSQLQuery("update  sunat_comprobante set Estado='0' ,Observaciones='"+scp.getObservaciones()+"' ,Usuario_Anula_Documento='"+scp.getUsuario_Anula_Documento()+"' , Host_Ip_Anula_Documento='"+scp.getHost_IP_Anula_Documento()+"', Fecha_Anula_Documento=GETDATE()  where id_comprobante='"+ scp.getId_Comprobante()+"'").executeUpdate();
		} catch (HibernateException e) {
			System.out.println("error en actualizar SUNAT_COMPROBANTE:::"
					+ e.getMessage());
			e.printStackTrace();
			sesion.getTransaction().rollback();
		}
		sesion.getTransaction().commit();
     
     
	}
	
	@Transactional(readOnly=true)
	public boolean siregistradocomprobantepago(String nrocomprobante) {
		if (getSessionFactory().getCurrentSession().createQuery("select cp from Comprobantepago cp where cp.nroseriecomprobante='"+nrocomprobante+"'").uniqueResult()==null) {
			return false;
		} else {
			return true;
		}
		
	}
	
	
	@Transactional(readOnly=true)
	public Date obtenerFechaIgv() {
		return (Date) getSessionFactory()
				.getCurrentSession()
				.createSQLQuery("select t.feccre from Igv as t where idigv=( select max(idigv) from Igv)")
				.uniqueResult();
	}
	
	
	public Double obtenerTotalValorPagadoDolares(int idcuota) {
		
		    return (Double) getSessionFactory().getCurrentSession().createQuery("select  round(sum( case when dc.tipocambio > 0 then (dc.montosoles/dc.tipocambio) else 0 end ),2)   from Detallecuota dc inner join dc.cuota c  inner join dc.comprobantepago cp where c.idcuota='"+idcuota+"' and cp.tipoconcepto.idtipoconcepto<>'17' and cp.tipoconcepto.idtipoconcepto<>'07' and cp.tipodocu.idtipodocu<>'08' and dc.estado='1'").uniqueResult();
	}

	@SuppressWarnings("unchecked")
	
	public List<Cuota> obtenerPagosContrato(int idcontrato) {
		
		return  getSessionFactory().getCurrentSession().createQuery("select c from Cuota c inner join c.contrato con where con.idcontrato='"+idcontrato+"' order by c.aniocuotames desc,c.nromes desc asc").list();
	}

    @SuppressWarnings("unchecked")
	
	public Cuota obtenerCuota(int idcontrato , String mes , String anio) {
		
		return  (Cuota)getSessionFactory().getCurrentSession().createQuery("select cuo from Cuota cuo inner join cuo.contrato c where c.idcontrato='"+idcontrato+"'and cuo.mespagado='"+mes+" and cuo.aniocuotames="+anio +" order by cuo.aniocuotames ,cuo.nromes  asc").uniqueResult();
	}
	public void eliminarCuota(int idcuota) {
		getSessionFactory().getCurrentSession().createSQLQuery("delete from Cuota WHERE idcuota='"+ idcuota+"'").executeUpdate();
		
	}

	
	public Integer obtenerNroSubpagosMesDetalleCuota(int idcuota) {
		Long val=(Long) getSessionFactory().getCurrentSession().createQuery("select count(dcuo.iddetallecuota) from Detallecuota dcuo inner join dcuo.cuota c where c.idcuota='"+idcuota+"'").uniqueResult();
		return val.intValue();
	}
	public Integer obtenerNroSubpagosMesDetalleCuotaArb(int idcuota) {
		Long val=(Long) getSessionFactory().getCurrentSession().createQuery("select count(dcuo.iddetallecuota) from Detallecuota_arbitrio dcuo inner join dcuo.cuota_arbitrio c where c.idcuota_arbitrio='"+idcuota+"'").uniqueResult();
		return val.intValue();
	}
	
	public Cuota obtenerCuota(int idcuota) {
		return (Cuota) getSessionFactory().getCurrentSession().createQuery("select cuo from Cuota cuo where cuo.idcuota='"+idcuota+"'").uniqueResult();
	}
	public Cuota_arbitrio obtenerCuotaArbitrio(int idcuota) {
		return (Cuota_arbitrio) getSessionFactory().getCurrentSession().createQuery("select cuo from Cuota_arbitrio cuo where cuo.idcuota_arbitrio='"+idcuota+"'").uniqueResult();
	}
	public Cuota obtenerCuota(int idcontrato, String mes , Integer anio) {
		return (Cuota) getSessionFactory().getCurrentSession().createQuery("select cuo from Cuota cuo inner join cuo.contrato c where c.idcontrato='"+idcontrato+"' and cuo.mespagado='"+mes+"' and cuo.aniocuotames="+anio+" ").uniqueResult();
	}


	@SuppressWarnings("unchecked")
	
	public List<Comprobantepago> getIdComprobantesPadreDeNotaDebito_Credito(Integer idcomprobante,String tipo) {
		
		if (tipo.equals("debito")) {
			return getSessionFactory().getCurrentSession().createQuery("select cp from Comprobantepago cp inner join cp.notadebito nd where nd.idcomprobante='"+idcomprobante+"'").list();
		} else {
			return getSessionFactory().getCurrentSession().createQuery("select cp from Comprobantepago cp inner join cp.notacredito nc where nc.idcomprobante='"+idcomprobante+"'").list();
		}
	}

	
	public boolean siExisteComprobanteRegistrado(String nroserie,String nrocomprobante) {
		
		if (getSessionFactory().getCurrentSession().createQuery("select c.idcomprobante from Comprobantepago c where c.nroserie='"+nroserie+"' and c.nrocomprobante='"+nrocomprobante+"' and c.nroserie<>'S/N' and c.nrocomprobante<>'S/N'").list().size()==0) {
			return false;
		} else {
			return true;
		}
	}

	
	public Comprobantepago obtenerComprobanteModel(Integer idcomprobante) {
		return (Comprobantepago) getSessionFactory().getCurrentSession().createQuery("select c from Comprobantepago c where c.idcomprobante='"+idcomprobante+"' ").uniqueResult();
	}
	public Comprobantepago obtenerComprobantexInquilino(Integer idcomprobante,Integer idinquilino) {
		return (Comprobantepago) getSessionFactory().getCurrentSession().createQuery("select cp from Comprobantepago cp inner join cp.detallecartera d  inner join  d.contrato c  inner join c.inquilino i  "
				+ " where cp.idcomprobante='"+idcomprobante+"' and i.idinquilino='"+idinquilino+"' ").uniqueResult();
	}
	public Sunat_Comprobante obtenerSunatComprobanteModel(Integer idcomprobante) {
		return (Sunat_Comprobante) getSessionFactory().getCurrentSession().createQuery("select sc from Sunat_Comprobante sc where sc.id_referencia="+idcomprobante+" ").uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	
	public List<DetallecomprobanteBean> obtenerDetalleComprobanteBean(int idcomprobante) {
		List<DetallecomprobanteBean> lista = new ArrayList<DetallecomprobanteBean>();
		StringBuilder hql = new StringBuilder();
		StringBuilder hqlRec_A = new StringBuilder();
		hql.append("select dcp.mes as mes,dcp.anio as anio,");
		hql.append("CASE ");
		hql.append("WHEN  dcp.comprobantepago.tipodocu.idtipodocu = '08' or dcp.comprobantepago.tipoconcepto.idtipoconcepto = '13'");
		hql.append("THEN (case when dcp.mora is not null and dcp.mora>0 then dcp.mora else (case when dcp.montosoles is not null and dcp.montosoles>0 then dcp.montosoles else 0 end)end) ELSE 0  END  as mora , ");
		hql.append("CASE ");
		hql.append(" WHEN  dcp.comprobantepago.tipodocu.idtipodocu = '08' or dcp.comprobantepago.tipoconcepto.idtipoconcepto = '13' ");
		hql.append(" THEN dcp.montopenalizacion ELSE 0  END  as penalidad ");
		hql.append(", CASE WHEN dcp.comprobantepago.tipoconcepto.idtipoconcepto = '18' THEN (CASE WHEN dcp.maedcondiciondetalle.idcondiciondetalle IS NULL THEN dcp.montosoles ELSE 0 END ) ");
		hql.append(" ELSE (case when dcp.comprobantepago.tipoconcepto.idtipoconcepto = '08'  then (case when dcp.mora is not null and dcp.mora<0   then dcp.mora else (case when dcp.montopenalizacion is not null and dcp.montopenalizacion<0 then dcp.montopenalizacion else dcp.montosoles end) end) else  (case when dcp.comprobantepago.tipoconcepto.idtipoconcepto = '07'  then 0 else dcp.montosoles end) end ) END  as total ");
		hql.append(", CASE WHEN dcp.comprobantepago.tipoconcepto.idtipoconcepto = '17' THEN 1 ELSE 0  END as indicador ");
		hql.append(", dcp.maedcondiciondetalle.idcondiciondetalle as id_maedcondiciondetalle  ");
		hql.append(", dcp.cuota.contrato.numerocuotas as numerocuotas ,dcp.comprobantepago.tipoconcepto.idtipoconcepto as idtipoconcepto ");
		hql.append(" from Detallecuota dcp ");
		hql.append("where dcp.comprobantepago.idcomprobante='"+idcomprobante+"'");
		
		Query query= sessionFactory.getCurrentSession().createQuery(hql.toString());
		
		
		hqlRec_A.append("select dcp.mes as mes,dcp.periodo as anio,dcp.mora as mora, dcp.montosoles as total , 1 as indicador ");
		hqlRec_A.append(" ,dcp.comprobantepago.tipoconcepto.idtipoconcepto as idtipoconcepto , 0.0 as penalidad ");
		hqlRec_A.append(" from Detallecuota_arbitrio dcp ");
		hqlRec_A.append("where dcp.comprobantepago.idcomprobante='"+idcomprobante+"'");

		Query query2= sessionFactory.getCurrentSession().createQuery(hqlRec_A.toString());
		
		///return query.setResultTransformer(Transformers.aliasToBean(DetallecomprobanteBean.class)).list();
		///////////////
		
		lista = query.setResultTransformer(Transformers.aliasToBean(DetallecomprobanteBean.class)).list();
		
		lista.addAll(query2.setResultTransformer(Transformers.aliasToBean(DetallecomprobanteBean.class)).list());
	

		return lista;
		
		
	}
	
	public UpaPagadorDeudorBean obtenerInformacionUltimoPagoRentaxUpa(int idupa) {
		StringBuilder hql = new StringBuilder();
		
		hql.append("select dcp.mes as mes,dcp.anio as anio,");
		hql.append("u.clave as claveUpa,i.nombrescompletos as nombreInquilino,u.siprocesojudicial as siprocesojudicial,");
		hql.append("cp.fechacancelacion as fecCancelacion,");
		hql.append("cp.nroseriecomprobante as nroseriecomprobante,");
		hql.append("cp.nombrecobrador as nombrecobrador,cp.usuariocreador as usrcre,cp.fechacreacion as feccre,");
		hql.append("c.idcontrato as idcontrato,c.condicion as condicion,c.tipomoneda as moneda,c.iniciocobro as iniciocobro, c.fincobro as fincobro,c.sinombreocupante as nombreocupante,");
		hql.append("c.montocuotasoles as cuota1,c.montocuotasoles2 as cuota2,c.montocuotasoles3 as cuota3,c.montocuotasoles4 as cuota4,c.montocuotasoles5 as cuota5,c.montocuotasoles6 as cuota6,");
		hql.append("cart.numero as nrocartera,c.tipomoneda as moneda,c.montocuotasoles as renta,c.sicompromisopago as sicompromisopago,");
		hql.append("(inm.direccion+' '+inm.numeroprincipal+' '+u.nombrenuminterior) as direccion,ubi.dist as distrito,ubi.prov as provincia ");
		hql.append(" from Detallecuota dcp ");
		hql.append(" inner join dcp.comprobantepago cp");
		hql.append(" inner join cp.detallecartera dc ");
		hql.append(" inner join dc.contrato c ");
		hql.append("inner join  c.upa u ");
		hql.append("inner join  u.inmueble inm ");
		hql.append("inner join  inm.ubigeo ubi ");
		hql.append("inner join  c.inquilino i ");
		hql.append("inner join dc.cartera cart ");
		hql.append("inner join  cp.tipodocu td ");
		hql.append(" where u.idupa='"+idupa+"' ") ;
		hql.append(" order by cp.fechacancelacion desc,dcp.anio desc,dcp.numeromes desc" );
		
		Query query= sessionFactory.getCurrentSession().createQuery(hql.toString()).setMaxResults(1);

		return  (UpaPagadorDeudorBean) query.setResultTransformer(Transformers.aliasToBean(UpaPagadorDeudorBean.class)).uniqueResult();

	}

	
	/**
	 * Obtiene informacion en la forma de UpaPagadorDeudorBean de ultima transaccion de la condicion indicada
	 */
	public UpaPagadorDeudorBean obtenerInformacionUltimoPagoRentaxCondicion(int idcondicion, String Desde,String Hasta) {
		StringBuilder hql = new StringBuilder();
		
		hql.append("select dcp.mes as mes,dcp.anio as anio,");
		hql.append("u.clave as claveUpa,i.nombrescompletos as nombreInquilino,u.siprocesojudicial as siprocesojudicial,");
		hql.append("cp.fechacancelacion as fecCancelacion,");
		hql.append("cp.nroseriecomprobante as nroseriecomprobante,");
		hql.append("cp.nombrecobrador as nombrecobrador,cp.usuariocreador as usrcre,cp.fechacreacion as feccre,");
		hql.append("c.idcontrato as idcontrato,c.condicion as condicion,c.tipomoneda as moneda,c.iniciocobro as iniciocobro, c.fincobro as fincobro,c.sinombreocupante as nombreocupante,");
		hql.append("c.montocuotasoles as cuota1,c.montocuotasoles2 as cuota2,c.montocuotasoles3 as cuota3,c.montocuotasoles4 as cuota4,c.montocuotasoles5 as cuota5,c.montocuotasoles6 as cuota6,c.sicuotascanceladas as sicuotascanceladas,");
		hql.append("cart.numero as nrocartera,c.tipomoneda as moneda,c.montocuotasoles as renta,c.sicompromisopago as sicompromisopago,");
		hql.append("(inm.direccion+' '+inm.numeroprincipal+' '+u.nombrenuminterior) as direccion,ubi.dist as distrito,ubi.prov as provincia ");
		hql.append(" from Detallecuota dcp ");
		hql.append(" inner join dcp.comprobantepago cp");
		hql.append(" inner join cp.detallecartera dc ");
		hql.append(" inner join dc.contrato c ");
		hql.append("inner join  c.upa u ");
		hql.append("inner join  u.inmueble inm ");
		hql.append("inner join  inm.ubigeo ubi ");
		hql.append("inner join  c.inquilino i ");
		hql.append("inner join dc.cartera cart ");
		hql.append("inner join  cp.tipodocu td ");
		hql.append(" where c.idcontrato='"+idcondicion+"' and cp.fechacancelacion>='"+Desde+"' and cp.fechacancelacion<='"+Hasta+"'");
		hql.append(" order by cp.fechacancelacion desc,dcp.anio desc,dcp.numeromes desc" );

		Query query= sessionFactory.getCurrentSession().createQuery(hql.toString()).setMaxResults(1);
		

		return  (UpaPagadorDeudorBean) query.setResultTransformer(Transformers.aliasToBean(UpaPagadorDeudorBean.class)).uniqueResult();

	}
	
	/**
	 * Actualiza el contrato si se completo o no todas las cuotas del contrato
	 */
	
	public void detectarSiCompletoCuotasContrato(int idcontrato) {
		StringBuilder hql = new StringBuilder();
		hql.append("update Contrato as c set c.sicuotascanceladas=:valor ");
		hql.append("where c.numerocuotas= (select count(*) from Cuota cuo inner join cuo.contrato con where con.idcontrato='"+idcontrato+"' and cuo.cancelado='true') and c.idcontrato='"+idcontrato+"'");
		Query query = sessionFactory.getCurrentSession().createQuery(hql.toString());
		query.setParameter("valor", true);
		query.executeUpdate();
		
		StringBuilder hql2 = new StringBuilder();
		hql2.append("update Contrato as c set c.sicuotascanceladas=:valor ");
		hql2.append("where c.numerocuotas <>(select count(*) from Cuota cuo inner join cuo.contrato con where con.idcontrato='"+idcontrato+"' and cuo.cancelado='true') and c.idcontrato='"+idcontrato+"'");
		Query query2 = sessionFactory.getCurrentSession().createQuery(hql2.toString());
		query2.setParameter("valor", false);
		query2.executeUpdate();
		
		
	}

	
	public void actualizarSiCompletoCuotasContrato_SinContrato(int idcontrato,boolean value) {
		StringBuilder hql = new StringBuilder();
		hql.append("update Contrato as c set c.sicuotascanceladas=:valor  where c.idcontrato='"+idcontrato+"'");
		Query query = sessionFactory.getCurrentSession().createQuery(hql.toString());
		query.setParameter("valor", value);
		query.executeUpdate();
	}

	
	public String obtenerCarteraCobrador(int idusuario) {
		StringBuilder hql = new StringBuilder();
		hql.append("select car.numero from Cartera car ");
		hql.append("inner join  car.usuario u ");
		hql.append(" where u.idusuario='"+idusuario+"'");

		Query query= sessionFactory.getCurrentSession().createQuery(hql.toString());
			return (String) query.uniqueResult();
	}

	
	public boolean siAsignadoCobrador(int idusuario) {
		StringBuilder hql = new StringBuilder();
		hql.append("select u.idusuario from Usuario u ");
		hql.append(" where u.idusuario='"+idusuario+"' and u.escobrador='true'");

		Query query= sessionFactory.getCurrentSession().createQuery(hql.toString());
		
		if (query.list().size()!=0){
			return true;
		} else {
			return false;
		}
	}
    @SuppressWarnings("unchecked")
    public List<Comprobantepago> listaComprobantes(String tipodocumento){
    	System.out.println("Lista de COmprobantes");
    	Calendar hasta= Calendar.getInstance();
		//hasta.add(Calendar.MONTH, -1);
		hasta.add(Calendar.YEAR, -2); //Las notas de crédito pueden realizarse a comprobantes emitidos hasta 10 años atrás.
		StringBuilder hql = new StringBuilder();
		hql.append("select cp from Comprobantepago cp  inner join cp.tipomovimiento tm ");
		if(tipodocumento.equals("NC")){
			hql.append("where  tm.idtipomovimiento='04' and (cp.nroserie='F002'or cp.nroserie='FN02' or cp.nroserie='BN02') and cp.sigeneronotacredito='false' and cp.detallecartera.iddetallecartera is null ");
		}
		if(tipodocumento.equals("ND")){
			hql.append("where  tm.idtipomovimiento='04' and (cp.nroserie='F002'or cp.nroserie='FN02' or cp.nroserie='BN02') and cp.sigeneronotadebito='false' and cp.detallecartera.iddetallecartera is null ");	
		}
		//hql.append("where  tm.idtipomovimiento='04' and (cp.nroserie='F002'or cp.nroserie='FN02' or cp.nroserie='BN02') and cp.sigeneronotacredito='false' and cp.detallecartera.iddetallecartera is null ");
		hql.append("order by  cp.nroserie,cp.nrocomprobante asc ");
    	return getSessionFactory().getCurrentSession().createQuery(hql.toString()).list();
    }
	@SuppressWarnings("unchecked")
	public List<Comprobantepago> listaComprobantesContrato(int idcontrato, String idtipDocu) {
		System.out.println("listacomporbantescontrato");
		Calendar hasta= Calendar.getInstance();
		//hasta.add(Calendar.MONTH, -1);
		hasta.add(Calendar.YEAR, -3); //Las notas de crédito pueden realizarse a comprobantes emitidos hasta 10 años atrás.
		
		
		StringBuilder hql = new StringBuilder();
		hql.append("select cp from Comprobantepago cp  inner join cp.detallecartera dc inner join dc.contrato c inner join cp.tipodocu td ");
		hql.append("where c.idcontrato='"+idcontrato+"' and td.idtipodocu<>'00' and cp.estado='1' ");
		
//		if (idtipDocu.equals("08")) {
//			hql.append(" and td.idtipodocu<>'08' and td.idtipodocu<>'09' and td.idtipodocu<>'04'  ");
//			hql.append(" and cp.sigeneronotadebito='false' order by cp.fechacancelacion desc");
//		}if(idtipDocu.equals("09")){
//			hql.append(" and td.idtipodocu<>'08' and td.idtipodocu<>'09' and td.idtipodocu<>'04'  ");
//			hql.append(" and cp.sigeneronotacredito='false' order by cp.fechacancelacion desc");
//		}
//		if(idtipDocu.equals("10")){
//			hql.append(" and td.idtipodocu<>'08' and td.idtipodocu<>'09' and td.idtipodocu<>'04'  ");
//			hql.append(" and cp.sigeneronotacredito='false' and cp.fechacancelacion>'"+FechaUtil.fechaHORAtoString(hasta.getTime())+"' order by cp.fechacancelacion desc");
//		} 
//		else {
//			hql.append(" and td.idtipodocu<>'01' and td.idtipodocu<>'03'  and td.idtipodocu<>'09' and td.idtipodocu<>'04'  ");
//			hql.append(" and cp.sigeneronotacredito='false' and cp.fechacancelacion>'"+FechaUtil.fechaHORAtoString(hasta.getTime())+"'  order by cp.fechacancelacion desc");
//		}
		//COMENTADO EL 08-03-2019
//		if (idtipDocu.equals("08")) {
//			hql.append(" and td.idtipodocu<>'08' and td.idtipodocu<>'09' and td.idtipodocu<>'04'  ");
//			hql.append(" and cp.sigeneronotadebito='false' order by cp.fechacancelacion desc");
//		}
		//CONDICION PARA NOTA DE DEBITO  PENDIENTE , NUEVO y PENALIZACION 
		if (idtipDocu.substring(0,2).equals("08")) {
			if(idtipDocu.replace("08","").equals("Pendiente")){
				System.out.println("tipodocu= "+idtipDocu.replace("08",""));
				hql.append(" and td.idtipodocu<>'08' and td.idtipodocu<>'09' and td.idtipodocu<>'04'  ");
				hql.append(" and cp.sigeneronotadebito='false' order by cp.fechacancelacion desc");
			}else{
				if(idtipDocu.replace("08","").equals("Nuevo")){
					System.out.println("idtipoDocu= "+idtipDocu.replace("08",""));
					hql.append(" and td.idtipodocu<>'08' and td.idtipodocu<>'09' and td.idtipodocu<>'04'  ");
					hql.append(" and (cp.moradetectada is null or cp.moradetectada='0') ");
					hql.append(" and cp.sigeneronotadebito='false' order by cp.fechacancelacion desc");
				}else if(idtipDocu.replace("08","").equals(Constante.TIPO_CONCEPTO_PENALIZACION)){
					System.out.println("idtipoDocu= "+idtipDocu.replace("08",""));
					hql.append(" and td.idtipodocu<>'08' and td.idtipodocu<>'09' and td.idtipodocu<>'04'  ");
					hql.append(" and (cp.montopenalizacion is null or cp.montopenalizacion='0') ");
					hql.append(" and cp.sigeneropenalizacion='false' order by cp.fechacancelacion desc");
				}
				
			}
			
		}else{
		if(idtipDocu.substring(0,2).equals("09")){
			hql.append(" and td.idtipodocu<>'08' and td.idtipodocu<>'09' and td.idtipodocu<>'04'  ");
			hql.append(" and cp.sigeneronotacredito='false' order by cp.fechacancelacion desc");
		}
		
		if(idtipDocu.equals("10")){
			hql.append(" and td.idtipodocu<>'08' and td.idtipodocu<>'09' and td.idtipodocu<>'04'  ");
			hql.append(" and cp.sigeneronotacredito='false' and  (cp.fechacancelacion>'"+FechaUtil.fechaHORAtoString(hasta.getTime())+"' or cp.fechacancelacion is NULL) order by cp.fechacancelacion desc");
		} 
		
		else {
			hql.append(" and td.idtipodocu<>'01' and td.idtipodocu<>'03'  and td.idtipodocu<>'09' and td.idtipodocu<>'04'  ");
			hql.append(" and cp.sigeneronotacredito='false' and  (cp.fechacancelacion>'"+FechaUtil.fechaHORAtoString(hasta.getTime())+"' or cp.fechacancelacion is NULL) order by cp.fechacancelacion desc");
			}
		}
		
		return	getSessionFactory().getCurrentSession().createQuery(hql.toString()).list();
	}

	@SuppressWarnings("unchecked")
	public List<Comprobantepago> listaComprobantesContrato(int idcontrato, String idtipDocu, String tipo) {
		System.out.println("listacomporbantescontrato");
		Calendar hasta= Calendar.getInstance();
		//hasta.add(Calendar.MONTH, -1);
		hasta.add(Calendar.YEAR, -3); //Las notas de crédito pueden realizarse a comprobantes emitidos hasta 10 años atrás.
		
		
		StringBuilder hql = new StringBuilder();
		hql.append("select cp from Comprobantepago cp  inner join cp.detallecartera dc inner join dc.contrato c inner join cp.tipodocu td ");
		hql.append("where c.idcontrato='"+idcontrato+"' and td.idtipodocu<>'00' and cp.estado='1' ");
		
//		if (idtipDocu.equals("08")) {
//			hql.append(" and td.idtipodocu<>'08' and td.idtipodocu<>'09' and td.idtipodocu<>'04'  ");
//			hql.append(" and cp.sigeneronotadebito='false' order by cp.fechacancelacion desc");
//		}if(idtipDocu.equals("09")){
//			hql.append(" and td.idtipodocu<>'08' and td.idtipodocu<>'09' and td.idtipodocu<>'04'  ");
//			hql.append(" and cp.sigeneronotacredito='false' order by cp.fechacancelacion desc");
//		}
//		if(idtipDocu.equals("10")){
//			hql.append(" and td.idtipodocu<>'08' and td.idtipodocu<>'09' and td.idtipodocu<>'04'  ");
//			hql.append(" and cp.sigeneronotacredito='false' and cp.fechacancelacion>'"+FechaUtil.fechaHORAtoString(hasta.getTime())+"' order by cp.fechacancelacion desc");
//		} 
//		else {
//			hql.append(" and td.idtipodocu<>'01' and td.idtipodocu<>'03'  and td.idtipodocu<>'09' and td.idtipodocu<>'04'  ");
//			hql.append(" and cp.sigeneronotacredito='false' and cp.fechacancelacion>'"+FechaUtil.fechaHORAtoString(hasta.getTime())+"'  order by cp.fechacancelacion desc");
//		}
		//COMENTADO EL 08-03-2019
//		if (idtipDocu.equals("08")) {
//			hql.append(" and td.idtipodocu<>'08' and td.idtipodocu<>'09' and td.idtipodocu<>'04'  ");
//			hql.append(" and cp.sigeneronotadebito='false' order by cp.fechacancelacion desc");
//		}
		//CONDICION PARA NOTA DE DEBITO  PENDIENTE , NUEVO y PENALIZACION 
		if (idtipDocu.substring(0,2).equals("08")) {
			if(idtipDocu.replace("08","").equals("Pendiente")){
				System.out.println("tipodocu= "+idtipDocu.replace("08",""));
				hql.append(" and td.idtipodocu<>'08' and td.idtipodocu<>'09' and td.idtipodocu<>'04'  ");
				hql.append(" and cp.sigeneronotadebito='false' order by cp.fechacancelacion desc");
			}else{
				if(idtipDocu.replace("08","").equals("Nuevo")){
					System.out.println("idtipoDocu= "+idtipDocu.replace("08",""));
					hql.append(" and td.idtipodocu<>'08' and td.idtipodocu<>'09' and td.idtipodocu<>'04'  ");
					hql.append(" and (cp.moradetectada is null or cp.moradetectada='0') ");
					hql.append(" and cp.sigeneronotadebito='false' order by cp.fechacancelacion desc");
				}else if(idtipDocu.replace("08","").equals(Constante.TIPO_CONCEPTO_PENALIZACION)){
					System.out.println("idtipoDocu= "+idtipDocu.replace("08",""));
					hql.append(" and td.idtipodocu<>'08' and td.idtipodocu<>'09' and td.idtipodocu<>'04'  ");
					hql.append(" and (cp.montopenalizacion is null or cp.montopenalizacion='0') ");
					hql.append(" and cp.sigeneropenalizacion='false' order by cp.fechacancelacion desc");
				}
				
			}
			
		}else{
		if(idtipDocu.substring(0,2).equals("09")){
			hql.append(" and td.idtipodocu<>'08' and td.idtipodocu<>'09' and td.idtipodocu<>'04'  ");
			hql.append(" and cp.sigeneronotacredito='false' order by cp.fechacancelacion desc");
		}
		
		if(idtipDocu.equals("10")){
			if(tipo.equals(Constante.TIPO_NOTA_CREDITO_3)){
				hql.append(" and td.idtipodocu<>'08' and td.idtipodocu<>'09' and td.idtipodocu<>'04'  ");
				hql.append(" and (cp.sigeneronotacredito='true' and (cp.sigeneronotacredito2='false' or cp.sigeneronotacredito2 is null ) ) and  (cp.fechacancelacion>'"+FechaUtil.fechaHORAtoString(hasta.getTime())+"' or cp.fechacancelacion is NULL) order by cp.fechacancelacion desc");
			}else{
			hql.append(" and td.idtipodocu<>'08' and td.idtipodocu<>'09' and td.idtipodocu<>'04'  ");
			hql.append(" and cp.sigeneronotacredito='false' and  (cp.fechacancelacion>'"+FechaUtil.fechaHORAtoString(hasta.getTime())+"' or cp.fechacancelacion is NULL) order by cp.fechacancelacion desc");
		} 		
	  }else {
			hql.append(" and td.idtipodocu<>'01' and td.idtipodocu<>'03'  and td.idtipodocu<>'09' and td.idtipodocu<>'04'  ");
			hql.append(" and cp.sigeneronotacredito='false' and  (cp.fechacancelacion>'"+FechaUtil.fechaHORAtoString(hasta.getTime())+"' or cp.fechacancelacion is NULL) order by cp.fechacancelacion desc");
			}
		}
		
		return	getSessionFactory().getCurrentSession().createQuery(hql.toString()).list();
	}
	public Double obtenerSumaComprobanteEspecializado(String desde,String hasta, String tipo, String uso) {
		String query1 = "";
		StringBuilder hql = new StringBuilder();
		StringBuilder hqlU = new StringBuilder();
		Query query = null;
		Query queryU = null;
		
		if (tipo.equals("uso")) {
			query1="u.uso='"+uso+ "' and td.idtipodocu<>'04' and tm.idtipomovimiento<>'03' ";
			
			hql.append("select sum(cp.totalsoles) from Comprobantepago cp ");
			hql.append("inner join cp.detallecartera dc ");
			hql.append("inner join dc.cartera cart ");
			hql.append("inner join  dc.contrato c ");
			hql.append("inner join  c.upa u ");
			hql.append("inner join  u.inmueble inm ");
			hql.append("inner join  c.inquilino i ");
			hql.append("inner join  i.tipopersona tper ");
			hql.append("inner join  cp.tipodocu td ");
			hql.append("inner join  cp.tipomovimiento tm ");
			hql.append("inner join  cp.tipopago tp ");
			hql.append("left join   cp.notadebito ref ");
			hql.append("where cp.fechacancelacion>='"+desde+ "' and cp.fechacancelacion<'"+hasta+"' and "+query1+" and td.idtipodocu<>'00' ");
			
			query= sessionFactory.getCurrentSession().createQuery(hql.toString());
			
			return (Double) query.uniqueResult();
			
		}else if (tipo.equals("garantia")) {
			query1="td.idtipodocu='04' and tm.idtipomovimiento='03' ";
			
			hql.append("select sum(cp.totalsoles) from Comprobantepago cp ");
			hql.append("inner join cp.detallecartera dc ");
			hql.append("inner join dc.cartera cart ");
			hql.append("inner join  dc.contrato c ");
			hql.append("inner join  c.upa u ");
			hql.append("inner join  u.inmueble inm ");
			hql.append("inner join  c.inquilino i ");
			hql.append("inner join  i.tipopersona tper ");
			hql.append("inner join  cp.tipodocu td ");
			hql.append("inner join  cp.tipomovimiento tm ");
			hql.append("inner join  cp.tipopago tp ");
			hql.append("left join   cp.notadebito ref ");
			hql.append("where cp.fechacancelacion>='"+desde+ "' and cp.fechacancelacion<'"+hasta+"' and "+query1+" and td.idtipodocu<>'00'");
			
			hqlU.append("select sum(cp.totalsoles) from Comprobantepago cp ");
			hqlU.append("inner join  cp.tipodocu td ");
			hqlU.append("inner join  cp.tipomovimiento tm ");
			hqlU.append("inner join  cp.tipopago tp ");
			hqlU.append("left join   cp.notadebito ref ");
			hqlU.append("where ");
			hqlU.append("cp.fechacancelacion>='"+desde+ "' and cp.fechacancelacion<'"+hasta+"' and "+query1+" and td.idtipodocu<>'00'");
			hqlU.append(" and cp.detallecartera.iddetallecartera is null ");
			
			query= sessionFactory.getCurrentSession().createQuery(hql.toString());
			queryU= sessionFactory.getCurrentSession().createQuery(hqlU.toString());

			if (query.uniqueResult()!=null && queryU.uniqueResult()!=null) {
				return (Double) query.uniqueResult()+(Double) queryU.uniqueResult();
			}if(query.uniqueResult()!=null && queryU.uniqueResult()==null) {
				return (Double) query.uniqueResult();
			}if(query.uniqueResult()==null && queryU.uniqueResult()!=null) {
				return (Double) queryU.uniqueResult();
			} else {
				return 0.0;
			}
			
		}else  if(tipo.equals("especial")){
			query1="tm.idtipomovimiento<>'03' ";
			
			hqlU.append("select sum(cp.totalsoles) from Comprobantepago cp ");
			hqlU.append("inner join  cp.tipodocu td ");
			hqlU.append("inner join  cp.tipomovimiento tm ");
			hqlU.append("inner join  cp.tipopago tp ");
			hqlU.append("left join   cp.notadebito ref ");
			hqlU.append("where ");
			hqlU.append("cp.fechacancelacion>='"+desde+ "' and cp.fechacancelacion<'"+hasta+"' and "+query1+" and td.idtipodocu<>'00'");
			hqlU.append(" and cp.detallecartera.iddetallecartera is null");
			queryU= sessionFactory.getCurrentSession().createQuery(hqlU.toString());
			
			return (Double) queryU.uniqueResult();
		}else{
			
			hql.append("select sum(cp.totalsoles) from Comprobantepago cp ");
			hql.append("inner join cp.detallecartera dc ");
			hql.append("inner join dc.cartera cart ");
			hql.append("inner join  dc.contrato c ");
			hql.append("inner join  c.upa u ");
			hql.append("inner join  u.inmueble inm ");
			hql.append("inner join  c.inquilino i ");
			hql.append("inner join  i.tipopersona tper ");
			hql.append("inner join  cp.tipodocu td ");
			hql.append("inner join  cp.tipomovimiento tm ");
			hql.append("inner join  cp.tipopago tp ");
			hql.append("left join   cp.notadebito ref ");
			hql.append("where cp.fechacancelacion>='"+desde+ "' and cp.fechacancelacion<'"+hasta+"' and td.idtipodocu<>'00'");
			
			hqlU.append("select sum(cp.totalsoles) from Comprobantepago cp ");
			hqlU.append("inner join  cp.tipodocu td ");
			hqlU.append("inner join  cp.tipomovimiento tm ");
			hqlU.append("inner join  cp.tipopago tp ");
			hqlU.append("left join   cp.notadebito ref ");
			hqlU.append("where ");
			hqlU.append("cp.fechacancelacion>='"+desde+ "' and cp.fechacancelacion<'"+hasta+"' and  td.idtipodocu<>'00'");
			hqlU.append(" and cp.detallecartera.iddetallecartera is null ");
			
			query= sessionFactory.getCurrentSession().createQuery(hql.toString());
			queryU= sessionFactory.getCurrentSession().createQuery(hqlU.toString());

			if (query.uniqueResult()!=null && queryU.uniqueResult()!=null) {
				return (Double) query.uniqueResult()+(Double) queryU.uniqueResult();
			}if(query.uniqueResult()!=null && queryU.uniqueResult()==null) {
				return (Double) query.uniqueResult();
			}if(query.uniqueResult()==null && queryU.uniqueResult()!=null) {
				return (Double) queryU.uniqueResult();
			} else {
				return 0.0;
			}
			
		}
	}

	
	public void apuntarComprobanteAnotacredito(int idcomprobanteRef,int idnotacredito) {
		getSessionFactory().getCurrentSession().createSQLQuery("update  Comprobantepago set idnotacredito='"+idnotacredito+"',sigeneronotacredito=1 WHERE idcomprobante='"+ idcomprobanteRef+"'").executeUpdate();
	}
	public void apuntarComprobanteAnotacredito2(int idcomprobanteRef,int idnotacredito) {
		getSessionFactory().getCurrentSession().createSQLQuery("update  Comprobantepago set idnotacredito2='"+idnotacredito+"',sigeneronotacredito2=1 WHERE idcomprobante='"+ idcomprobanteRef+"'").executeUpdate();
	}
	
	public ItemBean obtenerNombreCobrador(int idusuario) {
		StringBuilder hql = new StringBuilder();
		hql.append("select u.idusuario as id, u.nombrescompletos as descripcion from Usuario u ");
		hql.append(" where u.idusuario='"+idusuario+"'");
		
		Query query= sessionFactory.getCurrentSession().createQuery(hql.toString());

		return  (ItemBean) query.setResultTransformer(Transformers.aliasToBean(ItemBean.class)).uniqueResult();
		
	}
	
	
public  void settingLog(int idestadoauditoria, int ideventoauditoria, String especificacionevento){
		
		String url=FuncionesHelper.getURL().toString();
	
		try {
			int index = url.indexOf("pages/");
			url=url.substring(index+6, url.length());
			url=url.substring(0, url.length()-4);
		} catch (Exception e) {e.getMessage();	}

				Auditoria Adt= new Auditoria();
				Usuario usr= new Usuario();
				usr.setIdusuario((Integer)(FuncionesHelper.getUsuario()));
				
				
				
				Estadoauditoria esa= new Estadoauditoria();
				esa.setIdestadoauditoria(idestadoauditoria);
				Eventoauditoria eva= new Eventoauditoria();
				eva.setIdeventoauditoria(ideventoauditoria);
				Adt.setUsuario(usr);
				Adt.setEstadoauditoria(esa);
				Adt.setEventoauditoria(eva);
				Adt.setFecentrada( new Date());
				Adt.setNompantalla(url);
				Adt.setUrl(FuncionesHelper.getURL().toString());
				Adt.setIp(FuncionesHelper.getTerminal().toString());
				Adt.setEstado(true);
				Adt.setCodauditoria(1);
				Adt.setMensajepersonalizado("");
				Adt.setEspecificacionevento(especificacionevento);
				
		try {
			getSessionFactory().getCurrentSession().save(Adt);
		
		} catch (Exception e) {
		e.printStackTrace();
		}
	
	}

@SuppressWarnings("unchecked")

public List<ItemBean> listarCobradores() {
	StringBuilder hql = new StringBuilder();
	hql.append("select usr.idusuario as id,usr.nombrescompletos as descripcion ");
	hql.append("from Usuario as usr ");
	hql.append("where  usr.escobrador='true' order by usr.nombrescompletos");
	
	Query query = getSessionFactory().getCurrentSession().createQuery(hql.toString());
	return query.setResultTransformer(Transformers.aliasToBean(ItemBean.class)).list();
}
@SuppressWarnings("unchecked")

@Transactional(readOnly=true)
public List<Usuario> listar_Usuarios() {
	return	getSessionFactory().getCurrentSession().createQuery("select u from Usuario u  order by nombrescompletos").list();
	
}

public int NroUpasVisitadasCobrador(int idusuario, String desde, String hasta) {
	String query1 = null,query2 = null,query3 = null;
	if(idusuario!=99999){
		query1 = "usr.idusuario= '"+idusuario+"' and ";
	}
	
	if(!desde.equals("")){
		query2 = "cp.fechacancelacion>='"+desde+ "' and ";
	}

	if(!hasta.equals("")){
		query3 = "cp.fechacancelacion<'"+hasta+"'   ";
	}
	
	StringBuilder hql = new StringBuilder();
	hql.append("select u.clave ");
	hql.append(" from Comprobantepago cp ");
	hql.append("inner join cp.detallecartera dc ");
	hql.append("inner join dc.cartera cart ");
	hql.append("inner join  dc.contrato c ");
	hql.append("inner join  c.upa u ");
	hql.append("inner join  cp.usuario usr ");
	hql.append("where 1=1 and "+query1+query2+query3+" and 1=1 group by u.clave");
	
	Query query = getSessionFactory().getCurrentSession().createQuery(hql.toString());
	return query.list().size();
}


public Double sumarMoraComprobanteCobrador(int idusuario, String desde,
		String hasta) {
	String query= "",query1 = "",query2= "",query3= "";
	
	if(idusuario!=99999){
		query1 = "u.idusuario= '"+idusuario+"' and ";
	}
	
	if(!desde.equals("")){
		query2 = "cp.fechacancelacion>='"+desde+ "' and ";
	}

	if(!hasta.equals("")){
		query3 = "cp.fechacancelacion<'"+hasta+"' and ";
	}
	
	
	query = "select sum(cp.moradetectada) from Comprobantepago cp  inner join cp.tipodocu td inner join cp.usuario u where 1=1 and "+query1+query2+query3+" td.idtipodocu<>'00' and 1=1 ";
	Double suma = (Double) sessionFactory.getCurrentSession().createQuery(query).uniqueResult();
	
	if (suma==null) {
		 return 0.0;
	} else {
		return suma;
	}
}


public int NroUpasMoraCobradaCobrador(int idusuario, String desde, String hasta) {
	String query1 = null,query2 = null,query3 = null;
	if(idusuario!=99999){
		query1 = "usr.idusuario= '"+idusuario+"' and ";
	}
	
	if(!desde.equals("")){
		query2 = "cp.fechacancelacion>='"+desde+ "' and ";
	}

	if(!hasta.equals("")){
		query3 = "cp.fechacancelacion<'"+hasta+"'   ";
	}
	
	StringBuilder hql = new StringBuilder();
	hql.append("select u.clave ");
	hql.append(" from Comprobantepago cp ");
	hql.append("inner join cp.detallecartera dc ");
	hql.append("inner join dc.cartera cart ");
	hql.append("inner join  dc.contrato c ");
	hql.append("inner join  c.upa u ");
	hql.append("inner join  cp.usuario usr ");
	hql.append("inner join  cp.tipodocu td ");
	
	hql.append("where 1=1 and "+query1+query2+query3+" and td.idtipodocu='08' group by u.clave");
	
	Query query = getSessionFactory().getCurrentSession().createQuery(hql.toString());
	return query.list().size();
}
//public ComprobanteBean obtenerDetalleComprobantePago(int idComprobantePago){
//	StringBuilder hql = new StringBuilder();
//	hql.append("select cp.idcomprobante as idcomprobante,cp.nroserie as nroserie,cp.nrocomprobante as nrocomprobante, ");
//	hql.append("cp.nroseriecomprobante as nroseriecomprobante,cp.tipodocu.idtipodocu as idtipdocu,cp.tipoconcepto.idtipoconcepto as idtipconcepto, ");
//	hql.append("cp.tipomovimiento.idtipomovimiento as idtipmovimiento,cp.nombrecobrador as nombrecobrador,cp.fechaemision as fecEmision, ");
//	hql.append("cp.fechacancelacion as fecCancelacion,cp.fechavencimiento as fecVencimiento,cp.nombrepagante as nombreocupante, ");
//	hql.append("cp.dnirucpagante as dniocupante,cp.sifacturacionelectronica as sifacturacionelectronica,cp.observacion as observacion, ");
//	hql.append("cp.nombrecobrador as nombrecobrador,cp.comp_concepto as comp_concepto from Comprobantepago cp   where cp.idcomprobante='"+idComprobantePago+ "'");
//	Query  query= sessionFactory.getCurrentSession().createQuery(hql.toString());
//	return  (ComprobanteBean) query.setResultTransformer(Transformers.aliasToBean(ComprobanteBean.class)).uniqueResult();
//}
public Comprobantepago obtenerDetalleComprobantePago(int idComprobantePago){
	return (Comprobantepago) getSessionFactory().getCurrentSession().createQuery(
			" from Comprobantepago cp where cp.idcomprobante = "+idComprobantePago +" )")
	.uniqueResult();

}
@SuppressWarnings("unchecked")
public List<CuotaBean> obtenerDetalleComprobantePadre(int idComprobantePagoPadrenotatipo, String tipoDescuentoNotaCreditoSeleccionado) {
	
	
	StringBuilder hql = new StringBuilder();
	
		
		if (tipoDescuentoNotaCreditoSeleccionado.equals("Renta") || tipoDescuentoNotaCreditoSeleccionado.equals("")) {
			hql.append("select dc.iddetallecuota as idcuota, dc.mes as mes ,dc.anio as anio,dc.mora as mora ,dc.montosoles as monto , dc.montodolar as montodolar ,dc.cancelado as cancelado , cp.montopenalizacion as montopenalizacion from Detallecuota dc ");
			hql.append(" inner join dc.comprobantepago cp ");
			hql.append("inner join  cp.tipodocu td ");
			hql.append(" where  cp.idcomprobante='"+idComprobantePagoPadrenotatipo+ "'");
		
		} else if (tipoDescuentoNotaCreditoSeleccionado.equals("Mora")) {
			hql.append("select dc.iddetallecuota as idcuota, dc.mes as mes ,dc.anio as anio,dc.mora as mora  ,dc.montosoles as monto , dc.montodolar as montodolar,dc.cancelado as cancelado , cp.montopenalizacion as montopenalizacion from Detallecuota dc ");
			hql.append(" inner join dc.comprobantepago cp ");
			hql.append(" where  cp.idcomprobante='"+idComprobantePagoPadrenotatipo+ "'");
		}  else{
			
			hql.append("select dc.iddetallecuota as idcuota, dc.mes as mes ,dc.anio as anio,dc.mora as mora  ,dc.montosoles as monto , dc.montodolar as montodolar,dc.cancelado as cancelado , cp.montopenalizacion as montopenalizacion from Detallecuota dc ");
			hql.append(" inner join dc.comprobantepago cp inner join cp.notadebito nd ");
			hql.append(" where  nd.idcomprobante='"+idComprobantePagoPadrenotatipo+ "'");
		}

	
	Query query= sessionFactory.getCurrentSession().createQuery(hql.toString());

	return  query.setResultTransformer(Transformers.aliasToBean(CuotaBean.class)).list();
}
@SuppressWarnings("unchecked")
public List<CuotaBean> obtenerDetalleComprobantePadre(int idComprobantePagoPadrenotatipo, String tipoDescuentoNotaCreditoSeleccionado , int idcontrato) {
	
	
	StringBuilder hql = new StringBuilder();
	
		
		if (tipoDescuentoNotaCreditoSeleccionado.equals("Renta") || tipoDescuentoNotaCreditoSeleccionado.equals("")) {
			hql.append("select dc.iddetallecuota as idcuota, dc.mes as mes ,dc.anio as anio,dc.mora as mora ,dc.montosoles as monto , dc.montodolar as montodolar ,dc.cancelado as cancelado , cp.montopenalizacion as montopenalizacion from Detallecuota dc ");
			hql.append(" inner join dc.comprobantepago cp ");
			hql.append("inner join  cp.tipodocu td ");
			hql.append("inner join  dc.cuota cuo ");
			hql.append(" where  cp.idcomprobante='"+idComprobantePagoPadrenotatipo+ "' and cuo.contrato.idcontrato="+idcontrato);
		
		} else {
			
			hql.append("select dc.iddetallecuota as idcuota, dc.mes as mes ,dc.anio as anio,dc.mora as mora  ,dc.montosoles as monto , dc.montodolar as montodolar,dc.cancelado as cancelado , cp.montopenalizacion as montopenalizacion from Detallecuota dc ");
			hql.append(" inner join dc.comprobantepago cp inner join cp.notadebito nd ");
			hql.append(" inner join  dc.cuota cuo ");
			hql.append(" where  nd.idcomprobante='"+idComprobantePagoPadrenotatipo+ "' and cuo.contrato.idcontrato="+idcontrato);
		}

	
	Query query= sessionFactory.getCurrentSession().createQuery(hql.toString());

	return  query.setResultTransformer(Transformers.aliasToBean(CuotaBean.class)).list();
}

public CuotaBean obtenerValorCuotaPagada(int idcontrato, int anio, String mes) {
	StringBuilder hql = new StringBuilder();
	//return	getSessionFactory().getCurrentSession().createQuery("select cuo from Cuota cuo inner join cuo.contrato c  where c.idcontrato='"+idcontrato+"'").list();
	
	hql.append("select cuo.montosoles  as monto, cuo.morasoles as mora, cuo.nropagos as nropagos,cuo.idcuota as idcuota from Cuota cuo ");
	//hql.append("select cuo from Cuota cuo ");
	hql.append(" inner join cuo.contrato as c ");
	hql.append(" where  c.idcontrato='"+idcontrato+ "' and cuo.cuotanumero='"+mes+"' and cuo.aniocuotames='"+anio+"'" );
	
	Query query= sessionFactory.getCurrentSession().createQuery(hql.toString());

	return  (CuotaBean) query.setResultTransformer(Transformers.aliasToBean(CuotaBean.class)).uniqueResult();
}


@SuppressWarnings("unchecked")
public List<UpaPagadorDeudorBean> obtenerDeterminadosComprobantes(String desde, String hasta,String nombrecartera) {
	StringBuilder hql = new StringBuilder();
	
	hql.append("select dcp.mes as mes,dcp.anio as anio,");
	hql.append("u.clave as claveUpa,i.nombrescompletos as nombreInquilino,u.siprocesojudicial as siprocesojudicial,u.idupa as idupa,");
	hql.append("cp.fechacancelacion as fecCancelacion,");
	hql.append("cp.nroseriecomprobante as nroseriecomprobante,");
	hql.append("cp.nombrecobrador as nombrecobrador,cp.usuariocreador as usrcre,cp.fechacreacion as feccre,");
	hql.append("c.idcontrato as idcontrato,c.condicion as condicion,c.tipomoneda as moneda,c.iniciocobro as iniciocobro, c.fincobro as fincobro,c.sinombreocupante as nombreocupante,c.sicuotascanceladas as sicuotascanceladas,");
	hql.append("c.iniciocontrato as iniciocontrato,");
	hql.append("c.montocuotasoles as cuota1,c.montocuotasoles2 as cuota2,c.montocuotasoles3 as cuota3,c.montocuotasoles4 as cuota4,c.montocuotasoles5 as cuota5,c.montocuotasoles6 as cuota6,");
	hql.append("cart.numero as nrocartera,c.tipomoneda as moneda,c.montocuotasoles as renta,c.sicompromisopago as sicompromisopago,");
	hql.append("(inm.direccion+' '+inm.numeroprincipal+' '+u.nombrenuminterior) as direccion,ubi.dist as distrito,ubi.prov as provincia ");
	hql.append(" from Detallecuota dcp ");
	hql.append(" inner join dcp.comprobantepago cp");
	hql.append(" inner join cp.detallecartera dc ");
	hql.append(" inner join dc.contrato c ");
	hql.append("inner join  c.upa u ");
	hql.append("inner join  u.inmueble inm ");
	hql.append("inner join  inm.ubigeo ubi ");
	hql.append("inner join  c.inquilino i ");
	hql.append("inner join dc.cartera cart ");
	hql.append("inner join  cp.tipodocu td ");
	hql.append(" where   cp.fechacancelacion>='"+desde+"' and cp.fechacancelacion<='"+hasta+"' ") ;
	
	if (!nombrecartera.equals("")) {
		hql.append(" and cart.numero='"+nombrecartera+"'" );	
	}
	
	hql.append(" order by ubi.dist asc ,inm.direccion asc ,u.idupa asc ,cp.fechacancelacion desc,dcp.numeromes desc,dcp.anio desc" );
	
	Query query= sessionFactory.getCurrentSession().createQuery(hql.toString());

	return  query.setResultTransformer(Transformers.aliasToBean(UpaPagadorDeudorBean.class)).list();
}




/**
 * Lista las condiciones que tengan al menos referencia a una condicion.
 * @return Lista de mapas con los registros de los contratos, tipo List.
 */
@SuppressWarnings("unchecked")

public List<Integer> listarUpasConCondicion() {
	StringBuilder hql = new StringBuilder();
	
	hql.append("select u.idupa  from Contrato c ");
	hql.append("inner join  c.upa u ");
	hql.append("inner join  u.inmueble inm ");
	hql.append("inner join  inm.ubigeo ubi  order by inm.direccion asc");

	Query query= sessionFactory.getCurrentSession().createQuery(hql.toString());

	return  query.list();
}

/**
 * Lista las condiciones pendientes de pago de una upa determinada .
 * @return Lista de Id de condiciones, tipo List.
 */
@SuppressWarnings("unchecked")
@Override
public List<CondicionBean> obtenerCondicionesDeUpaConDeuda(int idupa) {
	StringBuilder hql = new StringBuilder();
		
	hql.append("select c.idcontrato as idcontrato,c.condicion as condicion,c.nrocontrato as nrocontrato,c.siocupante as siocupante,c.sinombreocupante as nombreocupante,c.sidniocupante as dniocupante ,c.tipomoneda as moneda,");
	hql.append("c.iniciocontrato as iniciocontrato,c.fincontrato as fincontrato,c.iniciocobro as iniciocobro,c.fincobro as fincobro,");
	hql.append("c.sicuotascanceladas as sicuotascanceladas,c.estado as estado,c.numerocuotas as numerocuotas,");
	hql.append("c.siresuelto as siresuelto,c.siadenda as siadenda,");
	hql.append("c.montocuotasoles as  cuota1,c.montocuotasoles2 as  cuota2,c.montocuotasoles3 as  cuota3,c.montocuotasoles4 as  cuota4,c.montocuotasoles5 as  cuota5,c.montocuotasoles6 as  cuota6,");
	hql.append("u.clave as claveUpa,i.nombrescompletos as nombresInquilino,i.ruc as ruc,tper.descripcion as tipopersona,i.dni as dni,");	
	hql.append("(inm.direccion+' '+inm.numeroprincipal+' '+u.nombrenuminterior) as direccion,ubi.dist as distrito");
	hql.append(" from Contrato c ");
	hql.append("inner join  c.upa u ");
	hql.append("inner join  u.inmueble inm ");
	hql.append("inner join  c.inquilino i ");
	hql.append("inner join  i.tipopersona tper ");
	hql.append("inner join  inm.ubigeo ubi ");
	hql.append("where  u.idupa='"+idupa+"' and c.sicuotascanceladas='false'");
	hql.append("order by c.iniciocobro desc");
	
	

	Query query= sessionFactory.getCurrentSession().createQuery(hql.toString());

	return	query.setResultTransformer(Transformers.aliasToBean(CondicionBean.class)).list();
}



@SuppressWarnings("unchecked")
public List<CuotaBean> listarRentaMensualPagadoCondicionxIntervalo(int idcondicion, Date desde, Date hasta, String moneda) {
	
	StringBuilder hql = new StringBuilder();
	
	if (moneda.equals("S")) {
		hql.append("select dc.anio as anio,dc.mes as mes,sum(dc.montosoles) as monto,cuo.cancelado as sipagado from");
	} else {
		hql.append("select dc.anio as anio,dc.mes as mes,ROUND(sum(dc.montosoles/dc.tipocambio),2) as monto,cuo.cancelado as sipagado  from");
	}
	
	hql.append(" Detallecuota dc");
	hql.append(" inner join Comprobantepago cp on dc.idcomprobante=cp.idcomprobante ");
	hql.append(" inner join Detallecartera dcar on dcar.iddetallecartera=cp.iddetallecartera ");
	hql.append(" inner join Contrato c  on c.idcontrato=dcar.idcontrato ");
	hql.append(" inner join inquilino i on c.idinquilino=i.idinquilino ");
	hql.append(" inner join Tipodocu td on td.idtipodocu=cp.idtipodocu ");
	hql.append(" inner join Cuota cuo on cuo.idcuota=dc.idcuota ");
	hql.append(" where   cp.fechacancelacion<='"+FechaUtil.fechaToString(hasta)+"' ") ;
	hql.append(" and c.idcontrato='"+idcondicion+"' and (td.idtipodocu='01' or td.idtipodocu='03' or td.idtipodocu='00')");
	//hql.append(" and dc.anio='2015' ");
	hql.append(" group by c.idcontrato,dc.anio,dc.mes,cuo.cancelado ");
	
	Query query= sessionFactory.getCurrentSession().createSQLQuery(hql.toString());
	
	
	return (ArrayList<CuotaBean>) query.setResultTransformer(Transformers.aliasToBean(CuotaBean.class)).list();
}


	@SuppressWarnings("unchecked")
	public List<Comprobantepago> listarReciboCajaReferencia(int idcondicion, String idtipoconcepto) {
		StringBuilder hql = new StringBuilder();
		hql.append("select cp from Comprobantepago cp  inner join cp.detallecartera dc inner join dc.contrato c inner join cp.tipodocu td inner join cp.tipoconcepto tc ");
		hql.append("where c.idcontrato="+idcondicion+" and td.idtipodocu='04' and tc.idtipoconcepto='"+idtipoconcepto+"' and cp.sirecibocajausado=false");
		
		return	getSessionFactory().getCurrentSession().createQuery(hql.toString()).list();
	}


	@Override
	public void actualizarComprobantePago(Comprobantepago cp) {
		sesion= sessionFactory.openSession();
		//sesion = sessionFactory.getCurrentSession();
		sesion.beginTransaction();
		try{
			getSessionFactory().getCurrentSession().update(cp);
		}catch (HibernateException e) {
			System.out.println("error en actualizar COMPROBANTE:::"
					+ e.getMessage());
			e.printStackTrace();
			sesion.getTransaction().rollback();
		}
		sesion.getTransaction().commit();
		
	
	}

 /* Comentado debido a la creacion de estado en Cuotas 04-06-2019*/
	@SuppressWarnings("unchecked")
	@Override
	public List<CuotaBean> generarPendientesParaCobroNuevoContrato(int idcontrato, Double valorTipoCambio) {
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select dc.ID_DETALLE_CONDICION as iddetallecondicion,  dc.MES as mes , CAST(dc.ANIO AS int)  as anio , u.clave,");
		hql.append(" CASE");
		hql.append(" WHEN cuo.IDCUOTA  IS NULL THEN dc.MONTOPAGAR");
		//hql.append(" ELSE (dc.MONTOPAGAR-cuo.MONTOSOLES) ");
		//hql.append(" ELSE (case when (cuo.TIPOCAMBIO=0 or cuo.TIPOCAMBIO is null ) then (dc.MONTOPAGAR-cuo.MONTOSOLES) else  (dc.MONTOPAGAR-round(cuo.MONTOSOLES/cuo.TIPOCAMBIO,2)) end)");
		hql.append(" ELSE (case when (cuo.TIPOCAMBIO=0 or cuo.TIPOCAMBIO is null ) then (dc.MONTOPAGAR-sum( (case when dcuo.estado is null or dcuo.estado='1' then dcuo.MONTOSOLES else 0 end ) )) else  (dc.MONTOPAGAR-round( sum( case when (dcuo.TIPOCAMBIO =0 or dcuo.TIPOCAMBIO is null) then 0 else( (case when dcuo.estado is null or dcuo.estado='1' then dcuo.MONTOSOLES else 0 end )/dcuo.TIPOCAMBIO) end) ,2)) end)");
		hql.append(" END as monto, cuo.MONTOSOLES as rentapagada, cuo.morasoles as moraacumulada, ");
		hql.append(" cuo.NROPAGOS as nropagos,cuo.idcuota as idcuota , dc.estado as estado_maed ");
		hql.append(" from MAE_DETALLE_CONDICION dc");
		hql.append(" inner join CONTRATO c ON c.IDCONTRATO=dc.IDCONTRATO ");
		hql.append(" inner join UPA u ON u.IDUPA=c.IDUPA ");
		hql.append(" left join  CUOTA cuo ON cuo.ID_DETALLE_CONDICION=dc.ID_DETALLE_CONDICION ");
		hql.append(" left join DETALLECUOTA dcuo on dcuo.IDCUOTA=cuo.IDCUOTA");
		hql.append(" where c.idcontrato="+idcontrato+" and dc.sirentagenerada=1 and (cuo.CANCELADO='0' or cuo.CANCELADO='2' or cuo.IDCUOTA is null ) and dc.estado='GENERADO'  ");
		//hql.append(" and (dcuo.estado is null or dcuo.estado=1)");
		hql.append(" group by dc.ID_DETALLE_CONDICION ,dc.MES ,dc.ANIO,u.clave,cuo.IDCUOTA, cuo.NROPAGOS ,cuo.MONTOSOLES ,dc.MONTOPAGAR ,cuo.TIPOCAMBIO ,cuo.morasoles ,dc.fechavencimiento,dc.secuencia ,dc.estado");
		hql.append(" order by dc.secuencia asc");
		
		
		Query query= sessionFactory.getCurrentSession().createSQLQuery(hql.toString());
		
		
		return (ArrayList<CuotaBean>) query.setResultTransformer(Transformers.aliasToBean(CuotaBean.class)).list();
	
	}
	 /* Comentado debido a la creacion de estado en Cuotas Reconocimiento Deuda*/
		@SuppressWarnings("unchecked")
		@Override
		public List<CuotaBean> generarPendientesParaCobroReconocimientoDeuda(int idcontrato, Double valorTipoCambio) {
			StringBuilder hql = new StringBuilder();
			
			hql.append(" select dc.ID_DETALLE_CONDICION as iddetallecondicion,dc.nrocuota as mes , CAST(dc.ANIO AS int)  as anio , u.clave,");
			hql.append(" CASE");
			hql.append(" WHEN cuo.IDCUOTA  IS NULL THEN dc.MONTOPAGAR");
			//hql.append(" ELSE (dc.MONTOPAGAR-cuo.MONTOSOLES) ");
			//hql.append(" ELSE (case when (cuo.TIPOCAMBIO=0 or cuo.TIPOCAMBIO is null ) then (dc.MONTOPAGAR-sum(dcuo.MONTOSOLES)) else  (dc.MONTOPAGAR-round( sum( case when (dcuo.TIPOCAMBIO =0 or dcuo.TIPOCAMBIO is null) then 0 else( dcuo.MONTOSOLES/dcuo.TIPOCAMBIO) end) ,2)) end)");
			hql.append(" ELSE (case when (cuo.TIPOCAMBIO=0 or cuo.TIPOCAMBIO is null ) then (dc.MONTOPAGAR-sum( (case when dcuo.estado is null or dcuo.estado='1' then dcuo.MONTOSOLES else 0 end ) )) else  (dc.MONTOPAGAR-round( sum( case when (dcuo.TIPOCAMBIO =0 or dcuo.TIPOCAMBIO is null) then 0 else( (case when dcuo.estado is null or dcuo.estado='1' then dcuo.MONTOSOLES else 0 end )/dcuo.TIPOCAMBIO) end) ,2)) end)");
			hql.append(" END as monto, cuo.MONTOSOLES as rentapagada, cuo.morasoles as moraacumulada, ");
			hql.append(" cuo.NROPAGOS as nropagos,cuo.idcuota as idcuota ");
			hql.append(" ,dc.nrocuota as numerocuota , dc.fechavencimiento as fechavencimiento ");
			hql.append(" from MAE_DETALLE_CONDICION dc");
			hql.append(" inner join CONTRATO c ON c.IDCONTRATO=dc.IDCONTRATO ");
			hql.append(" inner join UPA u ON u.IDUPA=c.IDUPA ");
			hql.append(" left join  CUOTA cuo ON cuo.ID_DETALLE_CONDICION=dc.ID_DETALLE_CONDICION ");
			hql.append(" left join DETALLECUOTA dcuo on dcuo.IDCUOTA=cuo.IDCUOTA");
			hql.append(" where c.idcontrato="+idcontrato+" and dc.sirentagenerada=1 and (cuo.CANCELADO='0' or cuo.CANCELADO='2' or cuo.IDCUOTA is null ) and dc.estado='GENERADO' ");
			hql.append(" group by dc.ID_DETALLE_CONDICION ,dc.nrocuota ,dc.ANIO,u.clave,cuo.IDCUOTA, cuo.NROPAGOS ,cuo.MONTOSOLES ,dc.MONTOPAGAR ,cuo.TIPOCAMBIO ,cuo.morasoles ,dc.fechavencimiento,dc.secuencia ");
			hql.append(" order by dc.secuencia asc");
			
			
			Query query= sessionFactory.getCurrentSession().createSQLQuery(hql.toString());
			
			
			return (ArrayList<CuotaBean>) query.setResultTransformer(Transformers.aliasToBean(CuotaBean.class)).list();
		
		}
		/* Comentado debido a la creacion de estado en Cuotas 04-06-2019*/
		@SuppressWarnings("unchecked")
		@Override
		public List<CuotaBean> generarPendientesCuotasDetalleReconocimientoDeuda(Integer idcondicion,Double tipocambio) {
			StringBuilder hql = new StringBuilder();
			
			hql.append(" select maed.IDDETALLECONDICION as id_maedetallecondicion, maed.id_condicion_detalle as id_maedcondiciondetalle, maed.MES as mes , CAST(maed.ANIO AS int)  as anio , u.clave as clave, 'Pendiente' as condicion ,");
			hql.append(" CASE");
			hql.append(" WHEN cuo.IDCUOTA  IS NULL THEN maed.MONTO");
			//hql.append(" ELSE (case when (cuo.TIPOCAMBIO= 0 or cuo.TIPOCAMBIO is null ) then (maed.MONTO-sum(dcuo.MONTOSOLES)) else  (maed.MONTO-round (  sum( case when (dcuo.TIPOCAMBIO =0 or dcuo.TIPOCAMBIO is null) then 0 else( dcuo.MONTOSOLES/dcuo.TIPOCAMBIO) end) ,2)) end)");
			hql.append(" ELSE (case when (cuo.TIPOCAMBIO= 0 or cuo.TIPOCAMBIO is null ) then (maed.MONTO-sum(case when dcuo.estado is null or dcuo.estado='1'then  dcuo.MONTOSOLES else  0 end)) else  (maed.MONTO-round (  sum( case when (dcuo.TIPOCAMBIO =0 or dcuo.TIPOCAMBIO is null) then 0 else( (case when dcuo.estado is null or dcuo.estado='1'then  dcuo.MONTOSOLES else  0 end)/dcuo.TIPOCAMBIO) end) ,2)) end)");
			//hql.append(" END as monto, sum(dcuo.MONTOSOLES) as rentapagada, sum(dcuo.MORA) as moraacumulada,  ");
			hql.append(" END as monto, sum( case when dcuo.estado is null or dcuo.estado='1'then  dcuo.MONTOSOLES else  0 end) as rentapagada, sum(dcuo.MORA) as moraacumulada,");
			hql.append(" cuo.NROPAGOS as nropagos,cuo.idcuota as idcuota ,maed.IDCONTRATO as idcontrato ,maed.secuencia as nrocuota , maed.CONCEPTO as concepto , maed.TIPOMONEDA as tipomoneda ,maed.tipomoneda as moneda");
			hql.append(" from  MAE_DET_CONDICION_DETALLE maed ");
			hql.append(" inner join CONTRATO c ON c.IDCONTRATO=maed.IDCONTRATO   ");
			hql.append(" inner join UPA u ON u.IDUPA=c.IDUPA  ");
			hql.append(" left join  DETALLECUOTA dcuo on dcuo.ID_MAED_CONDICION_DETALLE=maed.ID_CONDICION_DETALLE ");
			hql.append(" left join CUOTA cuo on cuo.idcuota=dcuo.IDCUOTA ");
			hql.append(" inner join MAE_DETALLE_CONDICION mae on mae.ID_DETALLE_CONDICION=maed.ID_CONDICION  ");
			hql.append(" where maed.ID_CONDICION="+idcondicion+" and (cuo.CANCELADO='0' or cuo.CANCELADO='2' or cuo.IDCUOTA is null ) and maed.estado='REGISTRADO' ");
			hql.append(" group by maed.IDDETALLECONDICION,maed.id_condicion_detalle ,maed.MES,maed.ANIO,u.clave,cuo.IDCUOTA,maed.MONTO,cuo.TIPOCAMBIO,cuo.MONTOSOLES,cuo.morasoles,cuo.NROPAGOS,cuo.idcuota ,maed.CONCEPTO, maed.secuencia , maed.TIPOMONEDA , maed.IDCONTRATO ");
			hql.append(" order by maed.SECUENCIA asc");
			
			
			Query query= sessionFactory.getCurrentSession().createSQLQuery(hql.toString());
			
			
			return (ArrayList<CuotaBean>) query.setResultTransformer(Transformers.aliasToBean(CuotaBean.class)).list();
		
		}
		 /* Comentado debido a la creacion de estado en Cuotas Reconocimiento Deuda*/
		@SuppressWarnings("unchecked")
		@Override
		public List<MaeDetCondicionDetalleBean> generarPendientesDetalleCuotaReconocimientoDeuda(Integer iddetallecondicion) {
			StringBuilder hql = new StringBuilder();
			
			hql.append(" select maedet.id_condicion_detalle as id_condicion_detalle , maedet.id_condicion as id_condicion,");
			hql.append(" maedet.idconcepto as idconcepto , maedet.concepto as concepto,");
			hql.append(" maedet.mes as mes , maedet.anio as anio , maedet.monto as monto,maedet.tipomoneda as tipomoneda ,");
			hql.append(" maedet.numeromes as numeromes , maedet.secuencia as secuencia ,maedet.idcontrato as idcontrato ,");
			hql.append(" maedet.iddetallecondicion as iddetallecondicion ");
			hql.append(" from mae_det_condicion_detalle maedet");
			hql.append(" where maedet.id_condicion="+iddetallecondicion+" and maedet.sianulado='0' ");
			hql.append(" order by maedet.secuencia asc");
			
			
			Query query= sessionFactory.getCurrentSession().createSQLQuery(hql.toString());
			
			
			return (ArrayList<MaeDetCondicionDetalleBean>) query.setResultTransformer(Transformers.aliasToBean(MaeDetCondicionDetalleBean.class)).list();
		
		}
//	@SuppressWarnings("unchecked")
//	@Override
//	public List<CuotaBean> generarPendientesParaCobroNuevoContrato(int idcontrato, Double valorTipoCambio) {
//		StringBuilder hql = new StringBuilder();
//		
//		hql.append(" select dc.ID_DETALLE_CONDICION as iddetallecondicion,  dc.MES as mes , CAST(dc.ANIO AS int)  as anio , u.clave,");
//		hql.append(" CASE");
//		hql.append(" WHEN cuo.estado='GENERADO' OR cuo.IDCUOTA  IS NULL THEN dc.MONTOPAGAR");
//		hql.append(" ELSE (dc.MONTOPAGAR-cuo.MONTOSOLES) ");
//		hql.append(" END as monto, cuo.MONTOSOLES as rentapagada, cuo.morasoles as moraacumulada, ");
//		hql.append(" cuo.NROPAGOS as nropagos,cuo.idcuota as idcuota ");
//		hql.append(" from MAE_DETALLE_CONDICION dc");
//		hql.append(" inner join CONTRATO c ON c.IDCONTRATO=dc.IDCONTRATO ");
//		hql.append(" inner join UPA u ON u.IDUPA=c.IDUPA ");
//		hql.append(" left join  CUOTA cuo ON cuo.ID_DETALLE_CONDICION=dc.ID_DETALLE_CONDICION ");
//		hql.append(" where c.idcontrato="+idcontrato+" and ((cuo.CANCELADO='0' or cuo.IDCUOTA is null )OR cuo.ESTADO='GENERADO')");
//		hql.append(" order by dc.secuencia ,dc.mes ,dc.anio asc");
//		
//		
//		Query query= sessionFactory.getCurrentSession().createSQLQuery(hql.toString());
//		
//		
//		return (ArrayList<CuotaBean>) query.setResultTransformer(Transformers.aliasToBean(CuotaBean.class)).list();
//	
//	}


	@SuppressWarnings("unchecked")
	public List<RentaBean> obtenerPeriodosPagados(int idccontrato) {
			List<RentaBean> lista = new ArrayList<RentaBean>();
			
			StringBuilder hql = new StringBuilder();
			hql.append("select dc.mes as mes, dc.anio as anio,dc.secuencia as secuencia, dc.contraprestacion as contraprestacion,dc.renta as renta, dc.montopagar as montopagar, ");
			hql.append(" dc.siclausulareconocimientorenta as siclausulareconocimientorenta, dc.siclausulaperiodogracia as siclausulaperiodogracia,dc.siclausulareconocimientoinversion as siclausulareconocimientoinversion, dc.siclausulapagoposterior as siclausulapagoposterior, ");
			hql.append(" dc.siclausulasuspensiontemporalrenta as siclausulasuspensiontemporalrenta, ");
			hql.append(" dc.montopagoposterior as montopagoposterior, dc.observacionpagoposterior as observacionpagoposterior,dc.descuentoreconocimientorenta as descuentoreconocimientorenta, dc.observacionreconocimientorenta as observacionreconocimientorenta ");
		//	hql.append(" cuo.idcuota as idcuota,cuo.cuotanumero as mes,cuo.aniocuotames as anio, cuo.montosoles as monto,cuo.morasoles as mora,cuo.cancelado as sipagado,cuo.nropagos as nropagos ");
			hql.append(" , cuo.cancelado as sicancelado");
			hql.append(" from Cuota cuo  ");
			hql.append("inner join cuo.contrato c ");
			hql.append("inner join cuo.maeDetalleCondicion dc ");
			hql.append("where c.idcontrato='"+idccontrato+"' order by cuo.aniocuotames asc,cuo.nromes asc");
			
			Query query= sessionFactory.getCurrentSession().createQuery(hql.toString());
			lista = query.setResultTransformer(Transformers.aliasToBean(RentaBean.class)).list();
			
		
			return lista;
	}


	@SuppressWarnings("unchecked")
	public List<RentaBean> obtenerPeriodosPendientes(int idcontrato) {
		StringBuilder hql = new StringBuilder();
		
		hql.append("select dc.mes as mes, dc.anio as anio,dc.secuencia as secuencia, dc.contraprestacion as contraprestacion,dc.renta as renta, dc.montopagar as montopagar, ");
		hql.append(" dc.siclausulareconocimientorenta as siclausulareconocimientorenta, dc.siclausulaperiodogracia as siclausulaperiodogracia,dc.siclausulareconocimientoinversion as siclausulareconocimientoinversion, dc.siclausulapagoposterior as siclausulapagoposterior, ");
		hql.append(" dc.siclausulasuspensiontemporalrenta as siclausulasuspensiontemporalrenta, ");
		hql.append("  cuo.cancelado as sicancelado ,");
		hql.append(" dc.montopagoposterior as montopagoposterior, dc.observacionpagoposterior as observacionpagoposterior,dc.descuentoreconocimientorenta as descuentoreconocimientorenta, dc.observacionreconocimientorenta as observacionreconocimientorenta, ");
	
		hql.append(" case");
		hql.append(" when cuo.idcuota  is null then dc.montopagar");
		hql.append(" else (dc.montopagar-cuo.montosoles) ");
		hql.append(" end as montopagar");
		hql.append(" from Cuota cuo  ");
		hql.append(" right join cuo.maeDetalleCondicion dc ");
		hql.append(" inner join dc.contrato c ");
		hql.append(" where c.idcontrato="+idcontrato+" and dc.sirentagenerada=1 and cuo.cancelado='0'");
	//	hql.append(" where c.idcontrato="+idcontrato+" and dc.sirentagenerada=1 and (cuo.cancelado='0' or cuo.idcuota is null )");
		hql.append(" order by dc.secuencia asc");
		
		
		Query query= sessionFactory.getCurrentSession().createQuery(hql.toString());
		
		
		return (ArrayList<RentaBean>) query.setResultTransformer(Transformers.aliasToBean(RentaBean.class)).list();
	
	}
	@SuppressWarnings("unchecked")
	public List<RentaBean> obtenerPeriodosPendientesContrato(int idcontrato) {
		StringBuilder hql = new StringBuilder();
		
		hql.append("select dc.mes as mes, dc.anio as anio,dc.secuencia as secuencia, dc.contraprestacion as contraprestacion,dc.renta as renta, dc.montopagar as montopagar, ");
		hql.append(" dc.siclausulareconocimientorenta as siclausulareconocimientorenta, dc.siclausulaperiodogracia as siclausulaperiodogracia,dc.siclausulareconocimientoinversion as siclausulareconocimientoinversion, dc.siclausulapagoposterior as siclausulapagoposterior, ");
		hql.append(" dc.siclausulasuspensiontemporalrenta as siclausulasuspensiontemporalrenta, ");
		hql.append("  cuo.cancelado as sicancelado ,");
		hql.append(" dc.montopagoposterior as montopagoposterior, dc.observacionpagoposterior as observacionpagoposterior,dc.descuentoreconocimientorenta as descuentoreconocimientorenta, dc.observacionreconocimientorenta as observacionreconocimientorenta, ");
	
		hql.append(" case");
		hql.append(" when cuo.idcuota  is null then dc.montopagar");
		hql.append(" else (dc.montopagar-cuo.montosoles) ");
		hql.append(" end as montopagar");
		hql.append(" from Cuota cuo  ");
		hql.append(" right join cuo.maeDetalleCondicion dc ");
		hql.append(" inner join dc.contrato c ");
		hql.append(" where c.idcontrato="+idcontrato+" and dc.sirentagenerada=1 and cuo.cancelado='0'");
	//	hql.append(" where c.idcontrato="+idcontrato+" and dc.sirentagenerada=1 and (cuo.cancelado='0' or cuo.idcuota is null )");
		hql.append(" order by dc.secuencia asc");
		
		
		Query query= sessionFactory.getCurrentSession().createQuery(hql.toString());
		
		
		return (ArrayList<RentaBean>) query.setResultTransformer(Transformers.aliasToBean(RentaBean.class)).list();
	
	}
	@SuppressWarnings("unchecked")
	@Override
    public List<CuotaBean> obtenerPeriodosPendientesContratoDeuda(int idcontrato){
    	StringBuilder hql = new StringBuilder();
    	hql.append(" select cuo.idcuota as idcuota ,u.clave,cuo.MESPAGADO as mes,CAST(cuo.ANIOCUOTAMES as int) as anio,cuo.NROPAGOS  as nropagos,cuo.cancelado as cancelado ,cuo.morasoles as mora, cuo.tipocambio as tipocambio , ");
		hql.append("  SUM(case when  dcuo.montosoles is null then 0 else (case when cp.FECHACANCELACION is null then 0 else dcuo.MONTOSOLES END) END )AS monto ");
		hql.append(" from cuota cuo");
		hql.append(" inner join DETALLECUOTA dcuo on dcuo.IDCUOTA=cuo.IDCUOTA ");
		hql.append(" inner join CONTRATO c ON c.IDCONTRATO=cuo.IDCONTRATO ");
		hql.append(" inner join UPA u ON u.IDUPA=c.IDUPA ");
		hql.append(" inner join COMPROBANTEPAGO cp on cp.IDCOMPROBANTE=dcuo.IDCOMPROBANTE ");
		hql.append(" where c.idcontrato="+idcontrato+" and dcuo.estado='1' ");
		hql.append(" GROUP BY cuo.idcuota,u.clave,cuo.MESPAGADO,cuo.ANIOCUOTAMES,cuo.MONTOSOLES,cuo.NROPAGOS,cuo.NROMES , cuo.cancelado , cuo.morasoles , cuo.tipocambio ");
		hql.append(" order by cuo.ANIOCUOTAMES,cuo.NROMES ");
		Query query= sessionFactory.getCurrentSession().createSQLQuery(hql.toString());
		
		return (ArrayList<CuotaBean>) query.setResultTransformer(Transformers.aliasToBean(CuotaBean.class)).list();
    }
	
	@SuppressWarnings("unchecked")
	@Override
    public List<CuotaBean> obtenerPeriodosPendientesDeuda(int idcontrato){
    	StringBuilder hql = new StringBuilder();
    	hql.append(" select dc.ID_DETALLE_CONDICION as iddetallecondicion,  dc.MES as mes , CAST(dc.ANIO AS int)  as anio , u.clave, dc.contraprestacion as renta ,dc.montopagar as nuevarenta , ");
//		hql.append(" CASE");
//		hql.append(" WHEN cuo.IDCUOTA  IS NULL THEN dc.MONTOPAGAR");
//		hql.append(" ELSE (case when (cuo.TIPOCAMBIO=0 or cuo.TIPOCAMBIO is null ) then (dc.MONTOPAGAR-cuo.MONTOSOLES) else  (dc.MONTOPAGAR-round(cuo.MONTOSOLES/cuo.TIPOCAMBIO,2)) end)");
//		hql.append(" END as monto,");
		///hql.append(" dc.MONTOPAGAR-SUM(case when  dcuo.montosoles is null then 0 else (case when cp.FECHACANCELACION is null then 0 else dcuo.MONTOSOLES END) END  )AS monto , ");
		//hql.append(" round(dc.MONTOPAGAR-SUM(case when  dcuo.montosoles is null then 0 else (case when cp.FECHACANCELACION is null then 0 else ( CASE WHEN (cuo.TIPOCAMBIO=0 or cuo.TIPOCAMBIO is null ) THEN  (dcuo.MONTOSOLES) else  (round(dcuo.MONTOSOLES/dcuo.TIPOCAMBIO,2))  END) END) END  ),2) AS monto , ");
    	hql.append(" round(dc.MONTOPAGAR-SUM(case when  dcuo.montosoles is null then 0 else (case when cp.FECHACANCELACION is null then 0 else ( CASE WHEN (cuo.TIPOCAMBIO=0 or cuo.TIPOCAMBIO is null ) THEN  ( case when dcuo.estado is null or dcuo.estado='1' then dcuo.MONTOSOLES else 0  end) else  (round(( case when dcuo.estado is null or dcuo.estado='1' then dcuo.MONTOSOLES else 0 end)/dcuo.TIPOCAMBIO,2))  END) END) END  ),2) AS monto , ");
		hql.append(" cuo.MONTOSOLES as rentapagada, cuo.morasoles as moraacumulada, ");
		hql.append(" cuo.NROPAGOS as nropagos,cuo.idcuota as idcuota ");
		hql.append(" from MAE_DETALLE_CONDICION dc");
		hql.append(" inner join CONTRATO c ON c.IDCONTRATO=dc.IDCONTRATO ");
		hql.append(" inner join UPA u ON u.IDUPA=c.IDUPA ");
		hql.append(" left join  CUOTA cuo ON cuo.ID_DETALLE_CONDICION=dc.ID_DETALLE_CONDICION ");
		hql.append(" left join DETALLECUOTA dcuo on dcuo.IDCUOTA=cuo.IDCUOTA");
		hql.append(" left join COMPROBANTEPAGO cp on cp.IDCOMPROBANTE=dcuo.IDCOMPROBANTE");
		hql.append(" where c.idcontrato="+idcontrato+" and dc.sirentagenerada=1 and (cuo.CANCELADO='0' or cuo.CANCELADO='2' or cuo.idcuota is null) and dc.estado='GENERADO' and (EOMONTH('1/'+LTRIM (dc.NUMEROMES)+ '/' +LTRIM (dc.ANIO))  < = ((CONVERT(varchar,EOMONTH(GETDATE()),103))) )");
		hql.append(" GROUP BY dc.ID_DETALLE_CONDICION ,  dc.MES  , CAST(dc.ANIO AS int) , u.clave, dc.contraprestacion,dc.montopagar  , ");
		//hql.append(" CASE WHEN cuo.IDCUOTA  IS NULL THEN dc.MONTOPAGAR ELSE (case when (cuo.TIPOCAMBIO=0 or cuo.TIPOCAMBIO is null ) then (dc.MONTOPAGAR-cuo.MONTOSOLES) else  (dc.MONTOPAGAR-round(cuo.MONTOSOLES/cuo.TIPOCAMBIO,2)) end) END,");
		hql.append(" cuo.MONTOSOLES , cuo.morasoles, cuo.NROPAGOS ,cuo.idcuota ,dc.secuencia ");
		hql.append(" order by dc.secuencia asc");
		Query query= sessionFactory.getCurrentSession().createSQLQuery(hql.toString());
		
		return (ArrayList<CuotaBean>) query.setResultTransformer(Transformers.aliasToBean(CuotaBean.class)).list();
    }
       
//	@SuppressWarnings("unchecked")
//	public List<CuotaBean> obtenerPeriodosPendientesDeuda(int idcontrato) {
//		StringBuilder hql = new StringBuilder();	
//		hql.append("select dc.mes as mes , CAST(dc.anio as int) as anio  , dc.contraprestacion as renta,dc.renta as nuevarenta, dc.montopagar as monto, ");
//		//hql.append(" dc.siclausulareconocimientorenta as siclausulareconocimientorenta, dc.siclausulaperiodogracia as siclausulaperiodogracia,dc.siclausulareconocimientoinversion as siclausulareconocimientoinversion, dc.siclausulapagoposterior as siclausulapagoposterior, ");
//		//hql.append(" dc.siclausulasuspensiontemporalrenta as siclausulasuspensiontemporalrenta, ");
//		//hql.append("  cuo.cancelado as sicancelado ,");
//		//hql.append(" dc.montopagoposterior as montopagoposterior, dc.observacionpagoposterior as observacionpagoposterior,dc.descuentoreconocimientorenta as descuentoreconocimientorenta, dc.observacionreconocimientorenta as observacionreconocimientorenta, ");
//	
//		hql.append(" CASE ");
//		hql.append(" when cuo.idcuota  is null then dc.montopagar");
//		hql.append(" else (dc.montopagar-cuo.montosoles) ");
//		hql.append(" end as monto");
//		hql.append(" from Cuota cuo  ");
//		hql.append(" right join cuo.maeDetalleCondicion dc ");
//		hql.append(" inner join dc.contrato c ");
//		hql.append(" where c.idcontrato="+idcontrato+" and dc.sirentagenerada=1 and cuo.cancelado='0'");
//	//	hql.append(" where c.idcontrato="+idcontrato+" and dc.sirentagenerada=1 and (cuo.cancelado='0' or cuo.idcuota is null )");
//		hql.append(" order by dc.secuencia asc");
//		Query query= sessionFactory.getCurrentSession().createQuery(hql.toString());
//		return (ArrayList<CuotaBean>) query.setResultTransformer(Transformers.aliasToBean(CuotaBean.class)).list();
//	
//	}
	@Override
	public List<CuotaArbitrioBean> obtenerPeriodosPendientesxMes(int idarbitrio) {
		List<CuotaArbitrioBean> lista= new ArrayList<CuotaArbitrioBean>();
		StringBuilder hql= new StringBuilder();
		hql.append("select cuo.idcuota_arbitrio as idcuota,cuo.mes as mes,cuo.periodo as periodo,cuo.trimestre as trimestre,cuo.monto as monto,cuo.cancelado as sipagado,cuo.estado as estado,cuo.usrcre as usrcre, cuo.feccre as feccre ,cuo.nropagos as nropagos ");
		hql.append("from Cuota_arbitrio cuo ");
		hql.append("inner join cuo.arbitrio a ");
		hql.append("where a.idarbitrio='"+idarbitrio+"' and cuo.estado='PENDIENTE'  order by cuo.periodo,cuo.nromes asc");
		
		Query query=sessionFactory.getCurrentSession().createQuery(hql.toString());
		lista=query.setResultTransformer(Transformers.aliasToBean(CuotaArbitrioBean.class)).list();
		return lista;
	}
	@Override
	public List<CuotaArbitrioBean> obtenerDetalleCuotaPeriodosPendientesxMes(int idarbitrio) {
		List<CuotaArbitrioBean> lista= new ArrayList<CuotaArbitrioBean>();
		StringBuilder hql= new StringBuilder();
		hql.append("select cuo.idcuota_arbitrio as cuota ,cuo.mes as mes , sum(d.montosoles) as monto ");
		hql.append("from Detallecuota_arbitrio d ");
		hql.append("inner join d.cuota_arbitrio cuo ");
		hql.append("inner join cuo.arbitrio a ");
		hql.append("where a.idarbitrio='"+idarbitrio+"' and d.estado='1' group by cuo.idcuota_arbitrio, cuo.mes " );
		Query query=sessionFactory.getCurrentSession().createQuery(hql.toString());
		lista=query.setResultTransformer(Transformers.aliasToBean(CuotaArbitrioBean.class)).list();
		return lista;
	}
	@Override
	public List<CuotaArbitrioBean> obtenerDetalleCuotaPeriodosPagadosxMes(int idarbitrio) {
		List<CuotaArbitrioBean> lista= new ArrayList<CuotaArbitrioBean>();
		StringBuilder hql= new StringBuilder();
		hql.append("select cuo.mes as mes , sum(d.montosoles) as monto ");
		hql.append("from Detallecuota_arbitrio d ");
		hql.append("inner join d.cuota_arbitrio cuo ");
		hql.append("inner join cuo.arbitrio a ");
		hql.append("where a.idarbitrio='"+idarbitrio+"' group by cuo.mes " );
		
		
		Query query=sessionFactory.getCurrentSession().createQuery(hql.toString());
		lista=query.setResultTransformer(Transformers.aliasToBean(CuotaArbitrioBean.class)).list();
		return lista;
	}
	@SuppressWarnings("unchecked")
	public List<PeriodoContratoBean> obtenerPeriodoxContrato(int idccontrato) {
			List<PeriodoContratoBean> lista = new ArrayList<PeriodoContratoBean>();
			
			StringBuilder hql = new StringBuilder();
			hql.append("select pc.tipoincremento as tipoincremento, pc.nombretipoincremento as nombretipoincremento , pc.periodo as periodo , pc.monto as monto , pc.numerocuotas as numerocuotas , pc.valorincremento as valorincremento,pc.fechainicioperiodo as fechainicioperiodo,pc.fechafinperiodo as fechafinperiodo ,");
			hql.append("c.idcontrato as idcontrato ");
		
			hql.append(" from PeriodoContrato pc  ");
			hql.append("inner join pc.contrato c ");
			hql.append("where c.idcontrato='"+idccontrato+"' order by pc.periodo asc ");
			
			Query query= sessionFactory.getCurrentSession().createQuery(hql.toString());
			lista = query.setResultTransformer(Transformers.aliasToBean(PeriodoContratoBean.class)).list();
			
		
			return lista;
	}
	
	public Sunat_Comprobante obtenerComprobante(Comprobantepago comp) {
		return (Sunat_Comprobante) getSessionFactory().getCurrentSession().createQuery("select scompd.Id_Comprobante as Id_Comprobante from Sunat_Comprobante scompd where scompd.id_referencia='"+comp.getIdcomprobante()+"'").uniqueResult();
	}
	@SuppressWarnings("unchecked")
	public List<Sunat_Comprobante> obtenerListaSunatComprobantePendientes(){
		return (List<Sunat_Comprobante>) getSessionFactory().getCurrentSession().createQuery(" select scompd from Sunat_Comprobante scompd  where scompd.respuesta_sunat is null and scompd.codigoSunat is null and scompd.origen_documento='SGI'").list();
	}
	
	@SuppressWarnings("unchecked")
	public List<String> obtenerCondicionDeContratoxUpa(String clave){
	    	StringBuilder hql = new StringBuilder();
			hql.append("select distinct c.condicion  from Contrato c ");
			hql.append("inner join  c.upa u ");
			hql.append("where u.clave='"+clave+"' ");

			Query query= sessionFactory.getCurrentSession().createQuery(hql.toString());
			

		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CondicionBean> obtenerListaContratosDeUpaxCondicion(String clave ,String condicion) {
		StringBuilder hql = new StringBuilder();	
		
		hql.append("select c.idcontrato as idcontrato, c.sinuevomantenimiento as sinuevomantenimiento,c.condicion as condicion,c.nrocontrato as nrocontrato,c.siocupante as siocupante,c.sinombreocupante as nombreocupante,c.sidniocupante as dniocupante ,c.tipomoneda as moneda,");
		hql.append("c.iniciocontrato as iniciocontrato,c.fincontrato as fincontrato,c.iniciocobro as iniciocobro,c.fincobro as fincobro,");
		hql.append("c.sicuotascanceladas as sicuotascanceladas,c.estado as estado,c.numerocuotas as numerocuotas,");
		hql.append("c.siresuelto as siresuelto,c.siadenda as siadenda,");
		hql.append("c.montocuotasoles as  cuota1,c.montocuotasoles2 as  cuota2,c.montocuotasoles3 as  cuota3,c.montocuotasoles4 as  cuota4,c.montocuotasoles5 as  cuota5,c.montocuotasoles6 as  cuota6,");
		hql.append("u.clave as claveUpa,u.clavenueva as claveupanueva,u.estado as estadoupa,i.nombrescompletos as nombresInquilino,i.ruc as ruc,tper.descripcion as tipopersona,i.dni as dni,");	
		hql.append("(inm.direccion+' '+inm.numeroprincipal+' '+u.nombrenuminterior) as direccion , u.siprocesojudicial as siprocesojudicial , CASE  WHEN c.sireferenciareconocimientodeuda IS NULL  then false  else c.sireferenciareconocimientodeuda END  as sireferenciareconocimientodeuda ");
		hql.append(",c.sireconocimientodeuda  as sireconocimientodeuda ");
		hql.append(" from Contrato c ");
		hql.append("inner join  c.upa u ");
		hql.append("inner join  u.inmueble inm ");
		hql.append("inner join  c.inquilino i ");
		hql.append("inner join  i.tipopersona tper ");
		hql.append("where u.clave='"+clave+"'" );
		hql.append(" and c.sianulado <> 1 and c.condicion='"+condicion +"' ");
		hql.append("order by c.iniciocobro desc");
		
		Query query=getSessionFactory().getCurrentSession().createQuery(hql.toString());
		
		
		return	query.setResultTransformer(Transformers.aliasToBean(CondicionBean.class)).list();
	}
	
	public void updateComprobanteSgiSunatFe(Comprobantepago comprobante){
		try{
			// sgi 
			getSessionFactory().getCurrentSession().createSQLQuery("update  Comprobantepago set dnirucpagante ='"+comprobante.getDnirucpagante()+"' WHERE idcomprobante="+ comprobante.getIdcomprobante()+"").executeUpdate();

			//sunatfe
			getSecondDBSessionFactory().getCurrentSession().createSQLQuery("update  Sunat_Comprobante set nroDoc_Receptor ='"+comprobante.getDnirucpagante()+"' WHERE id_referencia="+ comprobante.getIdcomprobante()+"").executeUpdate();

			}catch (Exception e){
			System.out.println("error en actualizar comprobante de pago:::"
					+ e.getMessage());
			e.printStackTrace();
		}
	}
	/** Obtencion de deuda por contrato - con nuevo mantenimiento*/
	@SuppressWarnings("unchecked")
	@Override
    public List<RentaBean> listarRentaPendientesxContrato(int idcontrato ,String condicion, Boolean condMaeDetalle ){
    	StringBuilder hql = new StringBuilder();
    	//hql.append(" select convert( date ,str(c.IDFECHAPAGO)+'-' + STR(mae.NUMEROMES) + '-' + mae.ANIO)  as fechavencimiento  , ");
    	hql.append(" select cast (convert( date ,str(c.IDFECHAPAGO)+'-' +STR(mae.NUMEROMES)+'-'+mae.ANIO) as datetime)  as fechavencimiento , ");
    	hql.append(" mae.mes as mes ,mae.anio as anio , mae.MONTOPAGAR  as renta ");		
		hql.append(" ,(round(mae.MONTOPAGAR -  round( sum( case when cp.FECHACANCELACION is null then 0 else ");
	    hql.append(" ( case when dcuo.estado='1' then ");	
	    hql.append(" ( case when c.TIPOMONEDA='D' then");
	    hql.append(" round(dcuo.MONTOSOLES/dcuo.TIPOCAMBIO,2) else dcuo.MONTOSOLES end) else 0 end)end),2) ,2))  as montopagar");
		hql.append(" ,DATEDIFF(day ,convert( date ,str(c.IDFECHAPAGO)+'-' +STR(mae.NUMEROMES)+'-'+mae.ANIO),GETDATE()) as nrodias_adeudo , c.tipomoneda  as tipomoneda ");	
		hql.append(" from mae_detalle_condicion mae");
		hql.append(" inner join contrato c on c.IDCONTRATO=mae.IDCONTRATO");
		hql.append(" left join cuota cuo on cuo.ID_DETALLE_CONDICION=mae.ID_DETALLE_CONDICION ");
		hql.append(" left join DETALLECUOTA dcuo on cuo.IDCUOTA=dcuo.IDCUOTA ");
		hql.append(" left join COMPROBANTEPAGO cp on dcuo.IDCOMPROBANTE=cp.IDCOMPROBANTE ");
		hql.append(" where c.idcontrato="+idcontrato+" and convert( date ,str(c.IDFECHAPAGO)+'-' +STR(mae.NUMEROMES)+'-'+mae.ANIO)<= GETDATE() and mae.ESTADO='GENERADO'  ");
		hql.append(" GROUP BY c.IDFECHAPAGO ,mae.NUMEROMES,mae.MES ,mae.ANIO  ,mae.MONTOPAGAR,mae.ESTADO,cuo.MONTOSOLES,cuo.MORASOLES,mae.secuencia , c.tipomoneda  ");
		hql.append(" order by mae.secuencia ");
		Query query= sessionFactory.getCurrentSession().createSQLQuery(hql.toString());
		return (ArrayList<RentaBean>) query.setResultTransformer(Transformers.aliasToBean(RentaBean.class)).list();
    }
  
	@SuppressWarnings("unchecked")
	@Override
	public List<RentaBean> listarRentasPagadasxContrato(int idcontrato,String condicion, Boolean condMaeDetalle) {
		StringBuilder hql = new StringBuilder();
		if(condMaeDetalle){
			hql.append(" select mae.mes as mes ,mae.anio as anio , mae.MONTOPAGAR  as renta  ,mae.contraprestacion as contraprestacion ");
			hql.append(", round( sum( case when cp.FECHACANCELACION is null then 0 else ( case when dcuo.estado='1' then  ( case when c.TIPOMONEDA='S'  then dcuo.montosoles");
			hql.append("   else ( CASE when dcuo.tipocambio is not null  and  dcuo.tipocambio>0 then (dcuo.montosoles/dcuo.tipocambio) else 0 end)end) else 0 end ) end ),2) as montopagado ");
			hql.append(" ,round( sum( case when cp.FECHACANCELACION is null then 0 else ( case when dcuo.estado='1' then  ( case when c.TIPOMONEDA='S'  then dcuo.montosoles ");
			hql.append(" else ( CASE when dcuo.tipocambio is not null  and  dcuo.tipocambio>0 then (dcuo.montosoles/dcuo.tipocambio) else 0 end)end) else 0 end ) end ),2) as montopagar  ");
			hql.append(" , '1' as sicancelado ,c.tipomoneda  as tipomoneda ,cuo.IDCUOTA as id ");
			hql.append(" from mae_detalle_condicion mae ");
			hql.append(" inner join contrato c on c.IDCONTRATO=mae.IDCONTRATO ");
			hql.append(" inner join cuota cuo on cuo.ID_DETALLE_CONDICION=mae.ID_DETALLE_CONDICION ");
			hql.append(" inner join DETALLECUOTA dcuo on cuo.IDCUOTA=dcuo.IDCUOTA ");
			hql.append(" inner join COMPROBANTEPAGO cp on dcuo.IDCOMPROBANTE=cp.IDCOMPROBANTE  ");
			hql.append(" inner join tipodocu td on td.idtipodocu = cp.idtipodocu  ");
			hql.append(" where c.idcontrato='"+idcontrato +"' and mae.ESTADO='GENERADO'  and dcuo.ESTADO='1' ");
			hql.append(" GROUP BY mae.NUMEROMES,mae.MES ,mae.ANIO  ,mae.MONTOPAGAR,mae.ESTADO ");
			hql.append(" ,cuo.MONTOSOLES,cuo.MORASOLES,mae.secuencia , cuo.idcuota ,mae.contraprestacion , c.tipomoneda ");
			hql.append(" order by mae.secuencia ");				
		}else{
				hql.append(" select cuo.MESPAGADO as mes ,  cast(str (cuo.ANIOCUOTAMES) as varchar) as anio , c.MONTOCUOTASOLES  as renta ,c.montocuotasoles as contraprestacion  ");
				hql.append("  , round( sum( case when cp.FECHACANCELACION is null then 0 else  ( case when dcuo.estado='1' then ( case when c.TIPOMONEDA='S' then  ");
				hql.append("  dcuo.MONTOSOLES else round(case when dcuo.TIPOCAMBIO is not null and dcuo.tipocambio>0 then dcuo.MONTOSOLES/dcuo.TIPOCAMBIO else 0  end ,2) end) else 0 end)end),2)   as montopagar  ");
				hql.append(" , round( sum( case when cp.FECHACANCELACION is null then 0 else  ( case when dcuo.estado='1' then  ( case when c.TIPOMONEDA='S' then  ");
				hql.append(" dcuo.MONTOSOLES else round(case when dcuo.TIPOCAMBIO is not null and dcuo.tipocambio>0 then dcuo.MONTOSOLES/dcuo.TIPOCAMBIO else 0  end ,2) end) else 0 end)end),2)   as montopagado  ");
				hql.append(" ,'1' as sicancelado ,c.tipomoneda  as tipomoneda ,cuo.IDCUOTA as id  ");
				hql.append(" from  contrato c ");
				hql.append(" inner join cuota cuo on cuo.IDCONTRATO=c.IDCONTRATO ");
				hql.append(" inner join DETALLECUOTA dcuo on cuo.IDCUOTA=dcuo.IDCUOTA  ");
				hql.append(" inner join COMPROBANTEPAGO cp on dcuo.IDCOMPROBANTE=cp.IDCOMPROBANTE  ");
				hql.append(" inner join tipodocu td on td.idtipodocu = cp.idtipodocu  ");
				hql.append(" where c.idcontrato='"+idcontrato +"' and dcuo.ESTADO='1'  ");
				hql.append(" GROUP BY cuo.MESPAGADO ,cuo.ANIOCUOTAMES,cuo.MONTOSOLES,cuo.MORASOLES, cuo.idcuota,c.MONTOCUOTASOLES ,cuo.NROMES , c.tipomoneda");
				hql.append(" order by cuo.ANIOCUOTAMES, cuo.NROMES ");
	
		}
		Query query= sessionFactory.getCurrentSession().createSQLQuery(hql.toString());
		return (ArrayList<RentaBean>) query.setResultTransformer(Transformers.aliasToBean(RentaBean.class)).list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<RentaBean> listarRentasPendientesxContrato(int idcontrato,String condicion, Boolean condMaeDetalle) {
		StringBuilder hql = new StringBuilder();
		StringBuilder hqlU = new StringBuilder();
		List<RentaBean> lista = new ArrayList<RentaBean>();
		if(condMaeDetalle){
			hql.append(" select  mae.mes as mes ,cast(str (mae.anio) as varchar) as anio ,mae.CONTRAPRESTACION as contraprestacion");
			hql.append(" , mae.MONTOPAGAR  as renta ,(round(mae.MONTOPAGAR -  round( sum( case when cp.FECHACANCELACION is null then 0 else");
			hql.append(" ( case when dcuo.estado='1' then    ( case when c.TIPOMONEDA='S' then dcuo.MONTOSOLES  ");
			hql.append(" ELSE ( CASE when dcuo.tipocambio is not null and dcuo.tipocambio>0 then (dcuo.montosoles/dcuo.tipocambio) else 0 end)end) else 0 end) end) ,2),2)) as montopagar ");
			hql.append(" , round( sum( case when cp.FECHACANCELACION is null then 0 else( case when dcuo.estado='1' then  ( case when c.TIPOMONEDA='S'  then dcuo.MONTOSOLES ");
			hql.append(" ELSE ( CASE when dcuo.tipocambio is not null and dcuo.tipocambio>0 then (dcuo.montosoles/dcuo.tipocambio) else 0 end) end) else 0 end) end) ,2)  as montopagado ");
			hql.append(" , c.tipomoneda as tipomoneda ");
			hql.append(" from mae_detalle_condicion mae");
			hql.append(" inner join contrato c on c.IDCONTRATO=mae.IDCONTRATO");
			hql.append(" left join cuota cuo on cuo.ID_DETALLE_CONDICION=mae.ID_DETALLE_CONDICION ");
			hql.append(" left join DETALLECUOTA dcuo on cuo.IDCUOTA=dcuo.IDCUOTA ");
			hql.append(" left join COMPROBANTEPAGO cp on dcuo.IDCOMPROBANTE=cp.IDCOMPROBANTE ");
			hql.append(" left join tipodocu td on td.idtipodocu = cp.idtipodocu  ");
			hql.append(" where c.idcontrato='"+idcontrato+"' and convert( date ,str(case when c.INICIOCONTRATO is not null then DAY(c.iniciocontrato) ");
			hql.append(" else day(c.iniciocobro) end )+'-' +STR(mae.NUMEROMES)+'-'+mae.ANIO)<= GETDATE() and mae.ESTADO='GENERADO' ");
			hql.append(" GROUP BY c.IDFECHAPAGO ,mae.NUMEROMES,mae.MES ,mae.ANIO  ,mae.MONTOPAGAR,mae.ESTADO,cuo.MONTOSOLES,");
			hql.append(" cuo.MORASOLES,mae.secuencia,mae.CONTRAPRESTACION , c.tipomoneda ");
			hql.append(" order by mae.secuencia ");
			
			hqlU.append(" select  mae.mes as mes ,cast(str (mae.anio) as varchar) as anio ,mae.CONTRAPRESTACION as contraprestacion");
			hqlU.append(" , mae.MONTOPAGAR  as renta ,(round(mae.MONTOPAGAR -  round( sum( case when cp.FECHACANCELACION is null then 0 else");
			hqlU.append(" ( case when dcuo.estado='1' then    ( case when c.TIPOMONEDA='S' then dcuo.MONTOSOLES  ");
			hqlU.append(" ELSE ( CASE when dcuo.tipocambio is not null and dcuo.tipocambio>0 then (dcuo.montosoles/dcuo.tipocambio) else 0 end)end) else 0 end) end) ,2),2)) as montopagar ");
			hqlU.append(" , round( sum( case when cp.FECHACANCELACION is null then 0 else( case when dcuo.estado='1' then  ( case when c.TIPOMONEDA='S'  then dcuo.MONTOSOLES ");
			hqlU.append(" ELSE ( CASE when dcuo.tipocambio is not null and dcuo.tipocambio>0 then (dcuo.montosoles/dcuo.tipocambio) else 0 end) end) else 0 end) end) ,2)  as montopagado ");
			hqlU.append(" , c.tipomoneda as tipomoneda ");
			hqlU.append(" from mae_detalle_condicion mae");
			hqlU.append(" inner join contrato c on c.IDCONTRATO=mae.IDCONTRATO");
			hqlU.append(" inner join cuota cuo on cuo.ID_DETALLE_CONDICION=mae.ID_DETALLE_CONDICION ");
			hqlU.append(" inner join DETALLECUOTA dcuo on cuo.IDCUOTA=dcuo.IDCUOTA ");
			hqlU.append(" inner join COMPROBANTEPAGO cp on dcuo.IDCOMPROBANTE=cp.IDCOMPROBANTE ");
			hqlU.append(" inner join tipodocu td on td.idtipodocu = cp.idtipodocu  ");
			hqlU.append(" where c.idcontrato='"+idcontrato+"' and convert( date ,str(case when c.INICIOCONTRATO is not null then DAY(c.iniciocontrato) ");
			hqlU.append(" else day(c.iniciocobro) end )+'-' +STR(mae.NUMEROMES)+'-'+mae.ANIO)> GETDATE() and mae.ESTADO='GENERADO' ");
			hqlU.append(" GROUP BY c.IDFECHAPAGO ,mae.NUMEROMES,mae.MES ,mae.ANIO  ,mae.MONTOPAGAR,mae.ESTADO,cuo.MONTOSOLES,");
			hqlU.append(" cuo.MORASOLES,mae.secuencia,mae.CONTRAPRESTACION , c.tipomoneda ");
			hqlU.append(" order by mae.secuencia ");
			Query query= sessionFactory.getCurrentSession().createSQLQuery(hql.toString());
			 lista = query.setResultTransformer(Transformers.aliasToBean(RentaBean.class)).list();
			if(condicion.equalsIgnoreCase(Constante.OPCION_REPORTE_CANCELADOS_PENDIENTES)){
				Query queryU = sessionFactory.getCurrentSession().createSQLQuery(hqlU.toString());
				lista.addAll(queryU.setResultTransformer(Transformers.aliasToBean(RentaBean.class)).list());
			}
			
			
			
			
		}//Pendientes sin mantenimiento 
		return lista;
//		Query query= sessionFactory.getCurrentSession().createSQLQuery(hql.toString());
//		return (ArrayList<RentaBean>) query.setResultTransformer(Transformers.aliasToBean(RentaBean.class)).list();
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<DetalleCuotaBean> listarDetalleCuotasxRenta(int idrenta) {
		StringBuilder hql = new StringBuilder();
		hql.append(" select cp.IDCOMPROBANTE as idcomprobante, cast(str (dcuo.ANIO) as varchar) as anio , dcuo.MES as mes ,cp.NROSERIECOMPROBANTE as nroseriecomprobante,");
		hql.append(" case when cp.IDTIPODOCU ='08' then (case  when tc.IDTIPOCONCEPTO='17' then dcuo.MONTOPENALIZACION else dcuo.MORA end ) else (dcuo.MONTOSOLES) end as montosoles ,");
		hql.append(" case when c.TIPOMONEDA='D' THEN (case when dcuo.TIPOCAMBIO is null or dcuo.tipocambio=0 then dcuo.montosoles else round(dcuo.MONTOSOLES/dcuo.TIPOCAMBIO ,2)end)else dcuo.montosoles end as montodolar , ");
		hql.append(" case when cp.IDTIPODOCU IS NULL OR  LEN(cp.IDTIPODOCU)=0 THEN ' ' ELSE (");
		hql.append(" case when cp.IDTIPODOCU ='00' THEN 'SV' ELSE (");
		hql.append(" case when cp.IDTIPODOCU ='01' THEN 'F' ELSE (");
		hql.append(" case when cp.IDTIPODOCU ='03' THEN 'BV' ELSE (");
		hql.append(" case when cp.IDTIPODOCU ='04' THEN 'RC' ELSE (");
		hql.append(" case when cp.IDTIPODOCU ='08' THEN 'ND' ELSE (");
		hql.append(" case when cp.IDTIPODOCU ='09' THEN 'NC' ELSE (cp.IDTIPODOCU) END) END ) END ) END ) END ) END )END AS nombretipodocu");
		hql.append(" ,cp.IDTIPODOCU AS  idtipodocu ,cpref.NROSERIECOMPROBANTE as nroseriecomprobanteref , c.tipomoneda as tipomoneda , cp.fechacancelacion as fecCancelacion ");
		hql.append(" from DETALLECUOTA dcuo");
		hql.append(" inner join CUOTA cuo on dcuo.IDCUOTA=cuo.IDCUOTA");
		hql.append(" inner join contrato c on c.idcontrato=cuo.idcontrato");
		hql.append(" inner join COMPROBANTEPAGO cp on cp.IDCOMPROBANTE=dcuo.IDCOMPROBANTE");
		hql.append(" inner join TIPODOCU td on td.IDTIPODOCU=cp.IDTIPODOCU");
		hql.append(" inner join TIPOCONCEPTO tc on tc.IDTIPOCONCEPTO=cp.IDTIPOCONCEPTO");
		hql.append(" left join COMPROBANTEPAGO cpref  on  cpref.IDCOMPROBANTE=cp.IDCOMPROBANTEREF");
		hql.append(" where cuo.IDCUOTA='"+idrenta +"' and cp.FECHACANCELACION is not null and cp.sianulado<>'1' ");
		hql.append(" order by cp.IDCOMPROBANTE,cp.nroseriecomprobante");
		Query query= sessionFactory.getCurrentSession().createSQLQuery(hql.toString());
		return (ArrayList<DetalleCuotaBean>) query.setResultTransformer(Transformers.aliasToBean(DetalleCuotaBean.class)).list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean siRentasPagadasxContrato(int idcontrato) {
        boolean res=false;
        List<Cuota> lista=(List<Cuota>)getSessionFactory().getCurrentSession().createQuery("from Cuota c where c.contrato.idcontrato="+idcontrato).list();
        for(Cuota cuo:lista){
        	if(cuo.getMontosoles()>0|| cuo.getMorasoles()>0|| cuo.getMontopenalizacion()>0){
        		res=true;
        	}
 
        }
		return res;
	}
   
	@Override
	public RentaBean ultimaRentaPagada(int idcontrato) {
		StringBuilder hql= new StringBuilder();
		hql.append("select TOP 1 cuo.montosoles as montopagado,cast(str (cuo.aniocuotames) as varchar )  as anio,cuo.mespagado as mes,cp.fechacancelacion as fechacancelacion from cuota cuo ");
		hql.append("inner join detallecuota dcuo on dcuo.idcuota=cuo.idcuota ");
		hql.append("inner join comprobantepago cp on cp.idcomprobante=dcuo.idcomprobante ");
		hql.append("where cuo.idcontrato='"+idcontrato +"' and cp.fechacancelacion is not null " );
		hql.append("order by cuo.aniocuotames desc , cuo.nromes desc ");		
		Query query= sessionFactory.getCurrentSession().createSQLQuery(hql.toString());

		return  (RentaBean) query.setResultTransformer(Transformers.aliasToBean(RentaBean.class)).uniqueResult();
	}
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}


	public SessionFactory getSecondDBSessionFactory() {
		return secondDBSessionFactory;
	}


	public void setSecondDBSessionFactory(SessionFactory secondDBSessionFactory) {
		this.secondDBSessionFactory = secondDBSessionFactory;
	}



	
}
