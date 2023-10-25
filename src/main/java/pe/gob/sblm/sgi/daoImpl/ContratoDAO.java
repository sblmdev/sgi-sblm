package pe.gob.sblm.sgi.daoImpl;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import pe.gob.sblm.api.commons.constants.sgi.Constantes;
import pe.gob.sblm.api.commons.utility.FuncionesHelper;
import pe.gob.sblm.sgi.bean.ComprobanteBean;
import pe.gob.sblm.sgi.bean.CondicionBean;
import pe.gob.sblm.sgi.bean.CuotaArbitrioBean;
import pe.gob.sblm.sgi.bean.PeriodoPagadoBean;
import pe.gob.sblm.sgi.bean.RentaBean;
import pe.gob.sblm.sgi.dao.IContratoDAO;
import pe.gob.sblm.sgi.entity.Adenda;
import pe.gob.sblm.sgi.entity.Area;
import pe.gob.sblm.sgi.entity.Auditoria;
import pe.gob.sblm.sgi.entity.Cliente;
import pe.gob.sblm.sgi.entity.Condicionincumplimientoclausula;
import pe.gob.sblm.sgi.entity.Contrato;
import pe.gob.sblm.sgi.entity.Cuentabancaria;
import pe.gob.sblm.sgi.entity.Cuota;
import pe.gob.sblm.sgi.entity.Detalleclausula;
import pe.gob.sblm.sgi.entity.Detallecuota;
import pe.gob.sblm.sgi.entity.Estadoauditoria;
import pe.gob.sblm.sgi.entity.Eventoauditoria;
import pe.gob.sblm.sgi.entity.Fechapago;
import pe.gob.sblm.sgi.entity.Inquilino;
import pe.gob.sblm.sgi.entity.Institucion;
import pe.gob.sblm.sgi.entity.MaeDetCondicionDetalle;
import pe.gob.sblm.sgi.entity.MaeDetalleCondicion;
import pe.gob.sblm.sgi.entity.Modulo;
import pe.gob.sblm.sgi.entity.Representante;
import pe.gob.sblm.sgi.entity.Tipoclausula;
import pe.gob.sblm.sgi.entity.Upa;
import pe.gob.sblm.sgi.entity.Uso;
import pe.gob.sblm.sgi.entity.Usuario;
import pe.gob.sblm.sgi.util.Constante;

@Repository(value = "contratoDAO")
public class ContratoDAO implements IContratoDAO, Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private SessionFactory sessionFactory;
    
	
	public void registrarContrato(Contrato contrato) {
		getSessionFactory().getCurrentSession().saveOrUpdate(contrato);
	}
	
	public void eliminarxCondicionContrato(int idcontrato) {
		Session session = sessionFactory.openSession();
		
		Contrato as= new Contrato();
		as.setIdcontrato(idcontrato);
		session.delete(as);
		session.flush();		
		session.close();
	}

	public Contrato obtenerContratoPorID(int id) {
		return (Contrato) getSessionFactory().getCurrentSession().createQuery("select c from Contrato c where c.idcontrato='"+id+"'").uniqueResult();
	}
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@SuppressWarnings("unchecked")
	
	public List<Uso> getListaUsos() {
		return getSessionFactory().getCurrentSession().createQuery("from Uso ").list();
	}
	@SuppressWarnings("unchecked")
	
	
	public List<Contrato> ListarContratosPorID(int idcontrato) {
		return getSessionFactory().getCurrentSession().createQuery("from Contrato c where c.idcontrato='"+idcontrato+"' ").list();
	}

	@SuppressWarnings("unchecked")
	
	public List<Inquilino> getListaInquilino() {
		return getSessionFactory().getCurrentSession().createQuery("from Inquilino").list();
	}

	@SuppressWarnings("unchecked")
	
	public List<Upa> buscarUpasXInmueble(int idinmueble) {
		return getSessionFactory().getCurrentSession().createQuery("select U from Upa U inner join U.inmueble I where I.idinmueble='"+idinmueble+"'").list();
	}

	@SuppressWarnings("unchecked")
	
	public List<CondicionBean> getListaContrato(String condicion) {
		return getSessionFactory().getCurrentSession().createQuery("select con from Contrato con left join fetch con.upa u left join fetch con.inquilino inq left join fetch u.inmueble inm  left join fetch inm.ubigeo ub where con.condicion='"+condicion+"' order by con.nrocontrato asc ").list();
	}

	@SuppressWarnings("unchecked")
	
	public List<Institucion> getListaInstitucion() {
		return getSessionFactory().getCurrentSession().createQuery("from Institucion ").list();
	}


	@SuppressWarnings("unchecked")
	
	public List<Representante> getListaRepresentante() {
		return getSessionFactory().getCurrentSession().createQuery("from Representante ").list();
	}
	
	public void resolverContrato(int idcontratoGlobal,String observacion, String fincobro, String fincontratoresuelto) {
		String updateQuery="UPDATE CONTRATO SET ESTADO='FINALIZADO',SIFINALIZADO='TRUE', FINCOBRO='"+fincobro+"', SIRESUELTO='TRUE', OBSERVACIONRESUELTO='"+observacion+"',FINCONTRATORESUELTO='"+fincontratoresuelto+"', FECMODFINALIZADO='"+FuncionesHelper.fechaHORAtoString(new Date())+"' , FECMODRESUELTO='"+FuncionesHelper.fechaHORAtoString(new Date())+"'  WHERE IDCONTRATO='"+idcontratoGlobal+"'";
		getSessionFactory().getCurrentSession().createSQLQuery(updateQuery).executeUpdate();
	}
	public void anularContrato(CondicionBean condicion ) {
		String updateQuery="UPDATE CONTRATO  SET ESTADO='"+condicion.getEstado()+"', SIFINALIZADO=1, USR_ANULA='"+condicion.getUsrmod()+"',MOTIVO_ANULA='"+condicion.getMotivo_anula()+"',HOST_IP_ANULA='"+condicion.getIp_host_anula()+"',SIDOCUMENTO='"+condicion.getSidocumento()+"' ,FEC_ANULA='"+FuncionesHelper.fechaHORAtoString(new Date())+"'  WHERE IDCONTRATO='"+condicion.getIdcontrato()+"'";
		getSessionFactory().getCurrentSession().createSQLQuery(updateQuery).executeUpdate();
	}
	
	public void finalizarContrato(int idcontratoGlobal,String observacion, String fincobro, String fincontratoresuelto,String nombreusuario) {
		String updateQuery="UPDATE CONTRATO SET ESTADO='FINALIZADO',SIFINALIZADO='TRUE', FINCOBRO='"+fincobro+"', OBSERVACIONFINALIZADO='"+observacion+"',USUARIOMODIFICADOR='"+ nombreusuario+"', FECHAMODIFICACION='"+FuncionesHelper.fechaHORAtoString(new Date())+"', FECMODFINALIZADO='"+FuncionesHelper.fechaHORAtoString(new Date())+"'   WHERE IDCONTRATO='"+idcontratoGlobal+"'";		
		getSessionFactory().getCurrentSession().createSQLQuery(updateQuery).executeUpdate();
	}
	
	public void incluirAdendaContrato(Adenda a,int idcontratoGlobal,String observacionadenda, String fincobro, String fincontratoadenda) {
		
		String updateQueryAdenda="UPDATE ADENDA SET SIFINALIZADO='TRUE',ESTADO='FINALIZADO' ,OBSERVACIONMOD='Se agrega una nueva adenda Nro:"+a.getNroadenda()+" el "+FuncionesHelper.fechaHORAtoString(new Date())+"',FECMOD='"+FuncionesHelper.fechaHORAtoString(new Date())+"', USRMOD='"+a.getUsrcre()+"' WHERE IDCONTRATO='"+idcontratoGlobal+"' and SIFINALIZADO = 0 ";
		getSessionFactory().getCurrentSession().createSQLQuery(updateQueryAdenda).executeUpdate();
		
		String updateQuery="UPDATE CONTRATO SET FINCOBRO='"+fincobro+"', SIADENDA='TRUE', OBSERVACIONADENDA='"+observacionadenda+"',FINCONTRATOADENDA='"+fincontratoadenda+"', FECMODADENDA='"+FuncionesHelper.fechaHORAtoString(new Date())+"',USUARIOMODIFICADOR='"+a.getUsrcre() +"',FECHAMODIFICACION='"+FuncionesHelper.fechaHORAtoString(new Date())+"' WHERE IDCONTRATO='"+idcontratoGlobal+"'";		
		getSessionFactory().getCurrentSession().createSQLQuery(updateQuery).executeUpdate();
		/** Insertar nueva adenda*/
		incluirAdenda(a);

	}
   public void incluirAdendaContrato(Adenda a, CondicionBean c) {
		
		String updateQueryAdenda="UPDATE ADENDA SET SIFINALIZADO='TRUE',ESTADO='FINALIZADO' ,OBSERVACIONMOD='Se agrega una nueva adenda Nro:"+a.getNroadenda()+" el "+FuncionesHelper.fechaHORAtoString(new Date())+"',FECMOD='"+FuncionesHelper.fechaHORAtoString(new Date())+"', USRMOD='"+a.getUsrcre()+"' WHERE IDCONTRATO='"+c.getIdcontrato()+"' and SIFINALIZADO = 0 ";
		getSessionFactory().getCurrentSession().createSQLQuery(updateQueryAdenda).executeUpdate();
		
		//String updateQuery="UPDATE CONTRATO SET FINCOBRO='"+FuncionesHelper.fechaToString(c.getFincobro())+"', SIADENDA='TRUE', OBSERVACIONADENDA='"+c.getObservacionadenda()+"',FINCONTRATO='"+FuncionesHelper.fechaToString(c.getFincontratoadenda())+"',FINCONTRATOADENDA='"+FuncionesHelper.fechaToString(c.getFincontratoadenda())+"', FECMODADENDA='"+FuncionesHelper.fechaHORAtoString(new Date())+"',USUARIOMODIFICADOR='"+a.getUsrcre() +"',FECHAMODIFICACION='"+FuncionesHelper.fechaHORAtoString(new Date())+"',SIDOCUMENTO='TRUE'";
		String updateQuery="";
		if(a.getTipocondicionadenda().equals(Constante.TIPO_CONDICION_ADENDA_A)){
			
			updateQuery="UPDATE CONTRATO SET FINCOBRO='"+FuncionesHelper.fechaToString(c.getFincobro())+"', SIADENDA='TRUE', OBSERVACIONADENDA='"+c.getObservacionadenda()+"',FINCONTRATO='"+FuncionesHelper.fechaToString(c.getFincontratoadenda())+"',FINCONTRATOADENDA='"+FuncionesHelper.fechaToString(c.getFincontratoadenda())+"', FECMODADENDA='"+FuncionesHelper.fechaHORAtoString(new Date())+"',USUARIOMODIFICADOR='"+a.getUsrcre() +"',FECHAMODIFICACION='"+FuncionesHelper.fechaHORAtoString(new Date())+"',SIDOCUMENTO='TRUE' , ESTADO='"+c.getEstado()+"', SIFINALIZADO='FALSE' ";
		}			
		else{
			updateQuery="UPDATE CONTRATO SET SIADENDA='TRUE', OBSERVACIONADENDA='"+c.getObservacionadenda()+"', FECMODADENDA='"+FuncionesHelper.fechaHORAtoString(new Date())+"',USUARIOMODIFICADOR='"+a.getUsrcre() +"',FECHAMODIFICACION='"+FuncionesHelper.fechaHORAtoString(new Date())+"',SIDOCUMENTO='TRUE' , ESTADO='"+c.getEstado()+"', SIFINALIZADO='FALSE' ";
		}
		
		updateQuery=updateQuery+ " WHERE IDCONTRATO='"+c.getIdcontrato()+"'";		
		getSessionFactory().getCurrentSession().createSQLQuery(updateQuery).executeUpdate();
		/** Insertar nueva adenda*/
		incluirAdenda(a);

	}
	/**Insertar nueva adenda */
	public void incluirAdenda(Adenda adenda){
		//getSessionFactory().getCurrentSession().save(adenda);
		getSessionFactory().getCurrentSession().saveOrUpdate(adenda);
	}
	
	
	
	public void cancelarSinContrato(int idcontratoGlobal, String observacion,
			Date fecUltimoPagoSinContrato,String usuario) {
		String updateQuery="UPDATE CONTRATO SET ESTADO='FINALIZADO',SIFINALIZADO='TRUE',OBSERVACIONFINALIZADO='"+observacion+"', FECHAMODIFICACION='"+FuncionesHelper.fechaHORAtoString(new Date())+"',USUARIOMODIFICADOR='"+usuario+"',FECMODFINALIZADO='"+FuncionesHelper.fechaHORAtoString(new Date())+"',FINCOBRO='"+FuncionesHelper.fechaToString(fecUltimoPagoSinContrato)+"', SICUOTASCANCELADAS='true'   WHERE IDCONTRATO='"+idcontratoGlobal+"'";
		getSessionFactory().getCurrentSession().createSQLQuery(updateQuery).executeUpdate();
	}


	@SuppressWarnings("unchecked")
	
	public List<Cliente> getListaCliente() {
		return getSessionFactory().getCurrentSession().createQuery("from Cliente ").list();
	}


	@SuppressWarnings("unchecked")
	
	public List<Usuario> obtenerUsuarios() {
		Session session = sessionFactory.openSession();
		List<List> list = null;
		
		
		list = session.createQuery("select new List(U.idusuario,U.rutaimgusr,U.cargo,A.desare,U.nombrescompletos,U.emailusr) from Usuario U inner join U.area A").list();
		List<Usuario> lista = new ArrayList<Usuario>();
		
		
		for(int i=0;i<list.size();i++){
			
			Area area= new Area();
			
			area.setDesare((String) list.get(i).get(3));
			
			Usuario usuario= new Usuario();
			usuario.setRutaimgusr((String) list.get(i).get(1));
			usuario.setCargo((String) list.get(i).get(2));
			usuario.setIdusuario((Integer) list.get(i).get(0));
			usuario.setNombrescompletos((String)list.get(i).get(4));
			usuario.setEmailusr((String)list.get(i).get(5));
			
			usuario.setArea(area);
			
			
			lista.add(usuario);
		}
		
		
		return lista;
	}
	
	public void enviarNotificacionPersonalizable(
			String contenidoMensajePersonalizado, int idusuariodestino) {
		try {
			settingLog(1, 30, idusuariodestino,contenidoMensajePersonalizado);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

public  void settingLog(int idestadoauditoria, int ideventoauditoria, int idusuariodestino, String mensajePersonalizado){
		
		String url=FuncionesHelper.getURL().toString();
	
		try {
			int index = url.indexOf("pages/");
			url=url.substring(index+6, url.length());
			url=url.substring(0, url.length()-4);
		} catch (Exception e) {e.getMessage();	}

				Session session = getSessionFactory().openSession();
				Auditoria Adt= new Auditoria();
				Usuario usr= new Usuario();
				usr.setIdusuario((Integer)(FuncionesHelper.getUsuario()));
				
				Usuario usrdes= new Usuario();
				usrdes.setIdusuario(idusuariodestino);
				
				Modulo mod= new Modulo();
				
				Query modulo=session.createSQLQuery("select  m.IDMODULO from PAGINAMODULO as pm inner join MODULO m on pm.IDMODULO= m.IDMODULO  inner join PAGINA as p on p.IDPAGINA=pm.IDPAGINA where P.NOMBREPAGINA='"+url+"'");
				
				if (modulo.list().size()==1) {
					int var=(Integer) modulo.list().get(0);
					mod.setIdmodulo(var);
				} else {
					int var=(Integer) modulo.list().get(0);
					mod.setIdmodulo(var);
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
			getSessionFactory().getCurrentSession().save(Adt);
		
		} catch (Exception e) {
		e.printStackTrace();
		}
	
	}

	
	@SuppressWarnings("unchecked")
	
	
	public List<Cuentabancaria> getCtasBancarias() {
		Session session = getSessionFactory().openSession();
		return session.createQuery("from Cuentabancaria ").list();
	}
	
	
	public void registrarCoutasContrato(Cuota couta) {
		getSessionFactory().getCurrentSession().save(couta);
	}
	
	@SuppressWarnings("unchecked")
	
	
	public List<Contrato> obtenerContratosUpa(int idupa) {
		return	getSessionFactory().getCurrentSession().createQuery("select con from Contrato con left join fetch con.upa u left join fetch con.inquilino inq left join fetch u.inmueble i left join fetch i.ubigeo left join fetch inq.tipopersona where u.idupa='"+idupa+"' order by con.fechacreacion asc").list();
	}
	
	
	public void actualizarEstadoUltimoContratoDeUpa(int idupa) {
//		Object idcontrato=getSessionFactory().getCurrentSession().createQuery("select c.idcontrato from Contrato c inner join c.upa u where u.idupa='"+idupa+"' and c.estado='VIGENTE'").uniqueResult();
//		
//		if (idcontrato!=null) {
//			String updateQuery="UPDATE CONTRATO SET ESTADO='FINALIZADO',SIFINALIZADO='1' WHERE IDUPA='"+idupa+"' AND IDCONTRATO='"+idcontrato.toString()+"'";
//			getSessionFactory().getCurrentSession().createSQLQuery(updateQuery).executeUpdate();
//		} 
//		
	}
	
	@SuppressWarnings("unchecked")
	
	
	public List<Cuota> obtenerCuotasDeContrato(int idcontratoGlobal) {
		return	getSessionFactory().getCurrentSession().createQuery("select cuo from Cuota cuo inner join cuo.contrato c where c.idcontrato='"+idcontratoGlobal+"' order by cuo.aniocuotames asc,cuo.nromes asc").list();
	}
	
	@SuppressWarnings("unchecked")
	
	
	public List<Detallecuota> obtenerDetalleDeCuota(int idcuota) {
		return	getSessionFactory().getCurrentSession().createQuery("select dc from Detallecuota dc left join fetch  dc.cuota cuo left join fetch dc.comprobantepago cp where cuo.idcuota='"+idcuota+"'").list();
	}
	
	
	
	public boolean verificarExistenciaCodigoContrato(String nrocontrato) {
		String val=(String) getSessionFactory().getCurrentSession().createQuery("select c.nrocontrato from Contrato c where c.nrocontrato='"+nrocontrato+"'").uniqueResult();
		
		if (val!=null) {
			return true;
		} else {
			return false;
		}
	}
	
	
	
	public CondicionBean obtenerContratoxNrocontrato(String nrocontrato) {
		StringBuilder hql= new StringBuilder();
		
		hql.append("select c.idcontrato as idcontrato,c.numerocuotas as numerocuotas,");
		hql.append("u.clave as claveUpa,i.nombrescompletos as nombresInquilino,u.uso as uso,i.dni as dni,i.ruc as ruc,tp.descripcion as tipopersona, ubi.depa as departamento,ubi.prov as provincia, ubi.dist as distrito,");	
		hql.append("c.condicion as condicion,c.tipomoneda as moneda,c.iniciocobro as iniciocobro, c.fincobro as fincobro,c.sinombreocupante as nombreocupante,c.nrocontrato as nrocontrato ,c.estado as estado,");
		hql.append("c.iniciocontrato as iniciocontrato, c.fincontrato as fincontrato,");
		hql.append("c.iniciocontrato as iniciocontrato, c.fincontrato as fincontrato,");
		hql.append("c.siocupante as siocupante, c.sinombreocupante as nombreocupante,");
		hql.append("c.montocuotasoles as cuota1,c.montocuotasoles2 as cuota2,c.montocuotasoles3 as cuota3,c.montocuotasoles4 as cuota4,c.montocuotasoles5 as cuota5,c.montocuotasoles6 as cuota6,");
		hql.append("inm.direccion as direccion, inm.numeroprincipal as numeroprincipal, u.nombrenuminterior as numerointerior , ");
		hql.append("c.fechacreacion as feccre ,c.usuariocreador as usrcre ,c.fechamodificacion as fecmod,c.usuariomodificador as usrmod ");
		hql.append("from Contrato c ");
		hql.append("inner join c.upa u ");
		hql.append("inner join c.inquilino i ");
		hql.append("inner join i.tipopersona tp ");
		hql.append("inner join u.inmueble inm ");
		hql.append("inner join inm.ubigeo ubi ");
		hql.append("where c.nrocontrato='"+nrocontrato+"'");
		hql.append(" order by inm.direccion asc,inm.numeroprincipal asc,u.nombrenuminterior asc");
		Query query= sessionFactory.getCurrentSession().createQuery(hql.toString());
		return (CondicionBean) query.setResultTransformer(Transformers.aliasToBean(CondicionBean.class)).uniqueResult();
		}
	
	@SuppressWarnings("unchecked")
	
	
	public List<Contrato> obtenerContratosInquilino(int idinquilinoseleccionado) {
		return	getSessionFactory().getCurrentSession().createQuery("select con from Contrato con left join fetch con.upa u left join fetch con.inquilino inq left join fetch u.inmueble i left join fetch i.ubigeo left join fetch inq.tipopersona  where inq.idinquilino='"+idinquilinoseleccionado+"' order by con.fechacreacion desc").list();
	}
	
	
	
	public Double sumacobrado(int idcontrato) {
		return	(Double) getSessionFactory().getCurrentSession().createSQLQuery("select SUM(TOTALSOLES) from COMPROBANTEPAGO as CP INNER JOIN DETALLECARTERA as DC ON CP.IDDETALLECARTERA=DC.IDDETALLECARTERA INNER JOIN CONTRATO as C ON DC.IDCONTRATO=C.IDCONTRATO where C.IDCONTRATO='"+idcontrato+"'").uniqueResult();
	}
	
	public int numSecuenciaMaeDetalleCondicion(int idcontrato){
		return (int) getSessionFactory().getCurrentSession().createSQLQuery("select MAX(SECUENCIA)+1 from MAE_DETALLE_CONDICION  where IDCONTRATO= "+idcontrato).uniqueResult();
	}
	
	public void actualizarMonto(int idcontrato, Double sumacobrado) {
		String updateQuery="UPDATE CONTRATO SET MONTODEUDA="+sumacobrado+" WHERE  IDCONTRATO='"+idcontrato+"'";
		getSessionFactory().getCurrentSession().createSQLQuery(updateQuery).executeUpdate();
	}
	
	
	
	public void actualizarMonto(int idcontrato) {
		String updateQuery="UPDATE CONTRATO SET ESTADO='FINALIZADO' WHERE  IDCONTRATO='"+idcontrato+"'";
		getSessionFactory().getCurrentSession().createSQLQuery(updateQuery).executeUpdate();
		
	}
	
	public List<CondicionBean> buscarContrato(Contrato contrato) { 

		StringBuilder hql = new StringBuilder();

		hql.append("select c.idcontrato as idcontrato,c.numerocuotas as numerocuotas,c.sicuotascanceladas as sicuotascanceladas,");
		hql.append("u.idupa as idupa, u.clave as claveUpa,i.idinquilino as idinquilino,i.nombrescompletos as nombresInquilino,u.uso as uso,i.dni as dni,i.ruc as ruc,tp.descripcion as tipopersona, ubi.depa as departamento,ubi.prov as provincia, ubi.dist as distrito,");	
		hql.append("c.condicion as condicion,c.tipo as tipo,c.tipocontrato as tipocontrato,c.tipomoneda as moneda,c.iniciocobro as iniciocobro, c.fincobro as fincobro,c.sinombreocupante as nombreocupante,c.nrocontrato as nrocontrato ,c.estado as estado,");
		hql.append("c.iniciocontrato as iniciocontrato, c.fincontrato as fincontrato,");
		hql.append("c.siocupante as siocupante, c.sinombreocupante as nombreocupante,c.sidniocupante as dniocupante,");
		
		
		hql.append("c.observacion as observacion,c.usoespecifico as usoespecifico,");
		hql.append("c.siresuelto as siresuelto, c.fecmodresuelto as fecmodresuelto, c.observacionresuelto as observacionresuelto, c.fincontratoresuelto as fincontratoresuelto,");
		hql.append("c.siadenda as siadenda, c.observacionadenda as observacionadenda,c.fincontratoadenda as fincontratoadenda,");
		hql.append("c.sisuscrito as sisuscrito, c.sifechasuscrito as fechasuscrito ,");
		hql.append("c.siresolucion as siresolucion, c.siresolucion as siresolucion, c.fecharesolucion as fecharesolucion, c.fecmodresolucion as fecharesolucion,c.numeroresolucion as numeroresolucion,");
		hql.append("c.siactaentrega as siactaentrega, c.numeroactaentrega as numeroactaentrega,c.sifechaactaentrega as fechaactaentrega,");
		hql.append("c.siactadevolucion as siactadevolucion, c.numeroactadevolucion as numeroactadevolucion,c.fechaactadevolucion as fechaactadevolucion,");

		hql.append("c.aniocontrato as aniocontrato ,c.observacionfinalizado as observacionfinalizado ,c.sifinalizado as sifinalizado,");
		
		hql.append("c.siclausulareconocimientorenta as siclausulareconocimientorenta ,c.siclausulaperiodogracia as siclausulaperiodogracia ,c.siclausulareconocimientoinversion as siclausulareconocimientoinversion,c.siclausulapagoposterior as siclausulapagoposterior, ");
		hql.append("c.siclausulasuspensiontemporalrenta as siclausulasuspensiontemporalrenta,");
		hql.append("c.montocuotasoles as contraprestacion ,c.contraprestacionadicional as contraprestacionadicional ,c.sicontraprestacionadicional as sicontraprestacionadicional,");
		hql.append("c.tiporeajusteanual as tiporeajusteanual ,c.reajusteanual as reajusteanual ,c.sinuevomantenimiento as sinuevomantenimiento,");
		
		hql.append("c.fechacreacion as feccre ,c.usuariocreador as usrcre ,c.fechamodificacion as fecmod,c.usuariomodificador as usrmod,");
		
		hql.append("c.tipocontrato as tipocontrato,c.tipo as tipo,");
		
		hql.append("c.montocuotasoles as cuota1,c.montocuotasoles2 as cuota2,c.montocuotasoles3 as cuota3,c.montocuotasoles4 as cuota4,c.montocuotasoles5 as cuota5,c.montocuotasoles6 as cuota6,");
		hql.append("inm.direccion as direccion, inm.numeroprincipal as numeroprincipal, u.nombrenuminterior as numerointerior  ");
		hql.append("from Contrato c ");
		hql.append("inner join c.upa u ");
		hql.append("inner join c.inquilino i ");
		hql.append("inner join i.tipopersona tp ");
		hql.append("inner join u.inmueble inm ");
		hql.append("inner join inm.ubigeo ubi ");
		
		hql.append("where u.clave like '"+contrato.getUpa().getClave()+"%' and c.condicion='"+contrato.getCondicion()+"' and c.estado='"+contrato.getEstado()+"' and c.idcontrato='"+contrato.getIdcontrato()+"' ");	
			//contrato anulado
		hql.append(" and c.sianulado <> 1");
		hql.append(" order by c.iniciocobro desc");
			
			
			
			Query query= sessionFactory.getCurrentSession().createQuery(hql.toString());
			return (List<CondicionBean>) query.setResultTransformer(Transformers.aliasToBean(CondicionBean.class)).list();
	}
	
	@SuppressWarnings("unchecked")
	
	public List<CondicionBean> buscarConSinContratoxClave(String valorbusqueda,String tipo) { 
		
		StringBuilder hql = new StringBuilder();

		hql.append("select c.idcontrato as idcontrato,c.numerocuotas as numerocuotas,c.sicuotascanceladas as sicuotascanceladas,");
		hql.append("u.idupa as idupa, u.clave as claveUpa,i.idinquilino as idinquilino,i.nombrescompletos as nombresInquilino,u.uso as uso,i.dni as dni,i.ruc as ruc,tp.descripcion as tipopersona, ubi.depa as departamento,ubi.prov as provincia, ubi.dist as distrito,");	
		hql.append("c.condicion as condicion,c.tipo as tipo,c.tipocontrato as tipocontrato,c.tipomoneda as moneda,c.iniciocobro as iniciocobro, c.fincobro as fincobro,c.sinombreocupante as nombreocupante,c.nrocontrato as nrocontrato ,c.estado as estado,");
		hql.append("c.iniciocontrato as iniciocontrato, c.fincontrato as fincontrato,");
		hql.append("c.siocupante as siocupante, c.sinombreocupante as nombreocupante,c.sidniocupante as dniocupante,");
		
		
		hql.append("c.observacion as observacion,c.usoespecifico as usoespecifico,");
		hql.append("c.siresuelto as siresuelto, c.fecmodresuelto as fecmodresuelto, c.observacionresuelto as observacionresuelto, c.fincontratoresuelto as fincontratoresuelto,");
		hql.append("c.siadenda as siadenda, c.observacionadenda as observacionadenda,c.fincontratoadenda as fincontratoadenda,");
		hql.append("c.sisuscrito as sisuscrito, c.sifechasuscrito as fechasuscrito ,");
		hql.append("c.siresolucion as siresolucion, c.siresolucion as siresolucion, c.fecharesolucion as fecharesolucion, c.fecmodresolucion as fecharesolucion,c.numeroresolucion as numeroresolucion,");
		hql.append("c.siactaentrega as siactaentrega, c.numeroactaentrega as numeroactaentrega,c.sifechaactaentrega as fechaactaentrega,");
		hql.append("c.siactadevolucion as siactadevolucion, c.numeroactadevolucion as numeroactadevolucion,c.fechaactadevolucion as fechaactadevolucion,");

		hql.append("c.aniocontrato as aniocontrato ,c.observacionfinalizado as observacionfinalizado ,c.sifinalizado as sifinalizado,");
		
		hql.append("c.siclausulareconocimientorenta as siclausulareconocimientorenta ,c.siclausulaperiodogracia as siclausulaperiodogracia ,c.siclausulareconocimientoinversion as siclausulareconocimientoinversion,c.siclausulapagoposterior as siclausulapagoposterior, ");
		hql.append("c.siclausulasuspensiontemporalrenta as siclausulasuspensiontemporalrenta,");
		hql.append("c.montocuotasoles as contraprestacion ,c.contraprestacionadicional as contraprestacionadicional ,c.sicontraprestacionadicional as sicontraprestacionadicional,");
		hql.append("c.tiporeajusteanual as tiporeajusteanual ,c.reajusteanual as reajusteanual ,c.sinuevomantenimiento as sinuevomantenimiento,");
		
		hql.append("c.fechacreacion as feccre ,c.usuariocreador as usrcre ,c.fechamodificacion as fecmod,c.usuariomodificador as usrmod,");
		
		hql.append("c.tipocontrato as tipocontrato,c.tipo as tipo, c.sidocumento as sidocumento ,");
		
		hql.append("c.montocuotasoles as cuota1,c.montocuotasoles2 as cuota2,c.montocuotasoles3 as cuota3,c.montocuotasoles4 as cuota4,c.montocuotasoles5 as cuota5,c.montocuotasoles6 as cuota6,");
		hql.append("inm.direccion as direccion, inm.numeroprincipal as numeroprincipal, u.nombrenuminterior as numerointerior ,  ");
		hql.append("c.sireconocimientodeuda as sireconocimientodeuda , ");
		hql.append("c.nroacta as nroacta , c.simontoinicialdeuda as simontoinicialdeuda, c.montoinicialdeuda as montoinicialdeuda , c.montodeuda as montodeuda ,c.fec_inicio_rec as fechadesde_rec ,");
		hql.append("c.fec_fin_rec as fechahasta_rec ,c.nromeses_rec as nromes_rec , c.montodeuda as  totalsoles_rec ");
		hql.append(", c.siincrementoporcentajevariable  as siincrementoporcentajevariable , c.siincrementomontovariable as siincrementomontovariable, c.sipenalidad as sipenalidad  ");
		hql.append(", c.siinteresmoratorio as siinteresmoratorio, fp.id_fechapago as idfechapago , c.sicondicionclausula as sicondicionclausula " );
		hql.append("from Contrato c "); 
		hql.append("inner join c.upa u ");
		hql.append("inner join c.inquilino i ");
		hql.append("inner join i.tipopersona tp ");
		hql.append("inner join u.inmueble inm ");
		hql.append("inner join inm.ubigeo ubi ");
		hql.append("left join c.fechapago fp ");
		
		if (!tipo.equals("")) {
		hql.append("where u.clave like '"+valorbusqueda+"%' and c.condicion='"+tipo+"'");
		}else {
		hql.append("where u.clave like '"+valorbusqueda+"%'");	
		}
		//contrato anulado
		hql.append(" and c.sianulado <> 1");
		hql.append(" order by c.iniciocobro desc");
		
		
		
		Query query= sessionFactory.getCurrentSession().createQuery(hql.toString());
		return (List<CondicionBean>) query.setResultTransformer(Transformers.aliasToBean(CondicionBean.class)).list();
		
	}
	
	@SuppressWarnings("unchecked")
	
	
	public List<CondicionBean> buscarConSinContratoxDireccion(String valorbusqueda,String tipo) {
		
		StringBuilder hql = new StringBuilder();

		hql.append("select c.idcontrato as idcontrato,c.numerocuotas as numerocuotas,c.sicuotascanceladas as sicuotascanceladas,");
		hql.append("u.idupa as idupa, u.clave as claveUpa,i.idinquilino as idinquilino,i.nombrescompletos as nombresInquilino,u.uso as uso,i.dni as dni,i.ruc as ruc,tp.descripcion as tipopersona, ubi.depa as departamento,ubi.prov as provincia, ubi.dist as distrito,");	
		hql.append("c.condicion as condicion,c.tipo as tipo,c.tipocontrato as tipocontrato,c.tipomoneda as moneda,c.iniciocobro as iniciocobro, c.fincobro as fincobro,c.sinombreocupante as nombreocupante,c.nrocontrato as nrocontrato ,c.estado as estado,");
		hql.append("c.iniciocontrato as iniciocontrato, c.fincontrato as fincontrato,");
		hql.append("c.siocupante as siocupante, c.sinombreocupante as nombreocupante,c.sidniocupante as dniocupante,");
		
		
		hql.append("c.observacion as observacion,c.usoespecifico as usoespecifico,");
		hql.append("c.siresuelto as siresuelto, c.fecmodresuelto as fecmodresuelto, c.observacionresuelto as observacionresuelto, c.fincontratoresuelto as fincontratoresuelto,");
		hql.append("c.siadenda as siadenda, c.observacionadenda as observacionadenda,c.fincontratoadenda as fincontratoadenda,");
		hql.append("c.sisuscrito as sisuscrito, c.sifechasuscrito as fechasuscrito ,");
		hql.append("c.siresolucion as siresolucion, c.siresolucion as siresolucion, c.fecharesolucion as fecharesolucion, c.fecmodresolucion as fecharesolucion,c.numeroresolucion as numeroresolucion,");
		hql.append("c.siactaentrega as siactaentrega, c.numeroactaentrega as numeroactaentrega,c.sifechaactaentrega as fechaactaentrega,");
		hql.append("c.siactadevolucion as siactadevolucion, c.numeroactadevolucion as numeroactadevolucion,c.fechaactadevolucion as fechaactadevolucion,");
		hql.append("c.aniocontrato as aniocontrato ,c.observacionfinalizado as observacionfinalizado ,c.sifinalizado as sifinalizado,");
		hql.append("c.siclausulareconocimientorenta as siclausulareconocimientorenta ,c.siclausulaperiodogracia as siclausulaperiodogracia ,c.siclausulareconocimientoinversion as siclausulareconocimientoinversion,c.siclausulapagoposterior as siclausulapagoposterior, ");
		hql.append("c.siclausulasuspensiontemporalrenta as siclausulasuspensiontemporalrenta,");

		hql.append("c.montocuotasoles as contraprestacion ,c.contraprestacionadicional as contraprestacionadicional ,c.sicontraprestacionadicional as sicontraprestacionadicional,");
		hql.append("c.tiporeajusteanual as tiporeajusteanual ,c.reajusteanual as reajusteanual ,c.sinuevomantenimiento as sinuevomantenimiento,");
		
		hql.append("c.fechacreacion as feccre ,c.usuariocreador as usrcre ,c.fechamodificacion as fecmod,c.usuariomodificador as usrmod,");
		
		hql.append("c.tipocontrato as tipocontrato,c.tipo as tipo,c.sidocumento as sidocumento ,");
		
		hql.append("c.montocuotasoles as cuota1,c.montocuotasoles2 as cuota2,c.montocuotasoles3 as cuota3,c.montocuotasoles4 as cuota4,c.montocuotasoles5 as cuota5,c.montocuotasoles6 as cuota6,");
		hql.append("inm.direccion as direccion, inm.numeroprincipal as numeroprincipal, u.nombrenuminterior as numerointerior,  ");
		hql.append("c.sireconocimientodeuda as sireconocimientodeuda , ");
		hql.append("c.nroacta as nroacta , c.simontoinicialdeuda as simontoinicialdeuda, c.montoinicialdeuda as montoinicialdeuda , c.montodeuda as montodeuda  ");
		hql.append("from Contrato c ");
		hql.append("inner join c.upa u ");
		hql.append("inner join c.inquilino i ");
		hql.append("inner join i.tipopersona tp ");
		hql.append("inner join u.inmueble inm ");
		hql.append("inner join inm.ubigeo ubi ");
		
		if (!tipo.equals("")) {
			hql.append("where u.direccion + ' '+ u.numprincipal like '%"+valorbusqueda+"%' and c.condicion='"+tipo+"' ");
		}else {
			hql.append("where u.direccion + ' '+ u.numprincipal  like '%"+valorbusqueda+"%' ");	
		}
		//contrato anulado
		hql.append(" and c.sianulado <> 1");
		hql.append(" order by c.iniciocobro desc");
		
		Query query= sessionFactory.getCurrentSession().createQuery(hql.toString());
		return (List<CondicionBean>) query.setResultTransformer(Transformers.aliasToBean(CondicionBean.class)).list();
}

	@SuppressWarnings("unchecked")
	
	
	public List<CondicionBean> buscarConSinContratoxNombreInquilino(String valorbusqueda, String tipo) {
		
		StringBuilder hql = new StringBuilder();

		hql.append("select c.idcontrato as idcontrato,c.numerocuotas as numerocuotas,c.sicuotascanceladas as sicuotascanceladas,");
		hql.append("u.idupa as idupa, u.clave as claveUpa,i.idinquilino as idinquilino,i.nombrescompletos as nombresInquilino,u.uso as uso,i.dni as dni,i.ruc as ruc,tp.descripcion as tipopersona, ubi.depa as departamento,ubi.prov as provincia, ubi.dist as distrito,");	
		hql.append("c.condicion as condicion,c.tipo as tipo,c.tipomoneda as moneda,c.iniciocobro as iniciocobro, c.fincobro as fincobro,c.sinombreocupante as nombreocupante,c.nrocontrato as nrocontrato ,c.estado as estado,");
		hql.append("c.iniciocontrato as iniciocontrato, c.fincontrato as fincontrato,");
		hql.append("c.siocupante as siocupante, c.sinombreocupante as nombreocupante,c.sidniocupante as dniocupante,");
		
		
		hql.append("c.observacion as observacion,c.usoespecifico as usoespecifico,");
		hql.append("c.siresuelto as siresuelto, c.fecmodresuelto as fecmodresuelto, c.observacionresuelto as observacionresuelto, c.fincontratoresuelto as fincontratoresuelto,");
		hql.append("c.siadenda as siadenda, c.observacionadenda as observacionadenda,c.fincontratoadenda as fincontratoadenda,");
		hql.append("c.sisuscrito as sisuscrito, c.sifechasuscrito as fechasuscrito ,");
		hql.append("c.siresolucion as siresolucion, c.siresolucion as siresolucion, c.fecharesolucion as fecharesolucion, c.fecmodresolucion as fecharesolucion,c.numeroresolucion as numeroresolucion,");
		hql.append("c.siactaentrega as siactaentrega, c.numeroactaentrega as numeroactaentrega,c.sifechaactaentrega as fechaactaentrega,");
		hql.append("c.siactadevolucion as siactadevolucion, c.numeroactadevolucion as numeroactadevolucion,c.fechaactadevolucion as fechaactadevolucion,");
		hql.append("c.aniocontrato as aniocontrato ,c.observacionfinalizado as observacionfinalizado ,c.sifinalizado as sifinalizado,");
		hql.append("c.siclausulareconocimientorenta as siclausulareconocimientorenta ,c.siclausulaperiodogracia as siclausulaperiodogracia ,c.siclausulareconocimientoinversion as siclausulareconocimientoinversion,c.siclausulapagoposterior as siclausulapagoposterior, ");
		hql.append("c.siclausulasuspensiontemporalrenta as siclausulasuspensiontemporalrenta,");

		hql.append("c.montocuotasoles as contraprestacion ,c.contraprestacionadicional as contraprestacionadicional ,c.sicontraprestacionadicional as sicontraprestacionadicional,");
		hql.append("c.tiporeajusteanual as tiporeajusteanual ,c.reajusteanual as reajusteanual ,c.sinuevomantenimiento as sinuevomantenimiento,");
		
		hql.append("c.tipocontrato as tipocontrato,c.tipo as tipo,");
		
		hql.append("c.fechacreacion as feccre ,c.usuariocreador as usrcre ,c.fechamodificacion as fecmod,c.usuariomodificador as usrmod,");
		hql.append("c.montocuotasoles as cuota1,c.montocuotasoles2 as cuota2,c.montocuotasoles3 as cuota3,c.montocuotasoles4 as cuota4,c.montocuotasoles5 as cuota5,c.montocuotasoles6 as cuota6,");
		hql.append("inm.direccion as direccion, inm.numeroprincipal as numeroprincipal, u.nombrenuminterior as numerointerior,  ");
		hql.append("c.sireconocimientodeuda as sireconocimientodeuda , ");
		hql.append("c.nroacta as nroacta , c.simontoinicialdeuda as simontoinicialdeuda, c.montoinicialdeuda as montoinicialdeuda , c.montodeuda as montodeuda  ");
		hql.append("from Contrato c ");
		hql.append("inner join c.upa u ");
		hql.append("inner join c.inquilino i ");
		hql.append("inner join i.tipopersona tp ");
		hql.append("inner join u.inmueble inm ");
		hql.append("inner join inm.ubigeo ubi ");
		
		if (!tipo.equals("")) {
		hql.append("where i.nombrescompletos like '%"+valorbusqueda+"%' and c.condicion='"+tipo+"' ");
		}else {
		hql.append("where i.nombrescompletos like '%"+valorbusqueda+"%' ");	
		}
		//contrato anulado
		hql.append(" and c.sianulado <> 1");
		hql.append(" order by c.iniciocobro desc");
		
		
		Query query= sessionFactory.getCurrentSession().createQuery(hql.toString());
		return (List<CondicionBean>) query.setResultTransformer(Transformers.aliasToBean(CondicionBean.class)).list();

	}
	@SuppressWarnings("unchecked")
	
	
	public List<CondicionBean> buscarAdenda(String valorbusqueda,String tipo,int tipobusqueda) {
		
		StringBuilder hql = new StringBuilder();

		hql.append("select a.idadenda as idadenda,c.idcontrato as idcontrato, a.sifinalizado as sifinalizado,a.estado as estado,a.nroadenda as nroadenda,a.observacionadenda as observacionadenda,");
		hql.append("a.fincontratoadenda as fincontratoadenda,a.fincobrocontrato as fincobro, a.observacionmod as observacionmod, a.usrcre as usrcre, a.feccre as feccre, a.usrmod as usrmod,a.fecmod as fecmod ");
		hql.append("from Adenda a ");
		hql.append("inner join a.contrato c ");
		hql.append("inner join c.upa u ");
		hql.append("inner join c.inquilino i ");
		hql.append("inner join i.tipopersona tp ");
		hql.append("inner join u.inmueble inm ");
		if(tipobusqueda==1){
			if (!tipo.equals("")) {
				hql.append("where u.clave like '"+valorbusqueda+"%' and c.condicion='"+tipo+"'");
				}else {
				hql.append("where u.clave like '"+valorbusqueda+"%'");	
				}
		}else if (tipobusqueda==2){
			if (!tipo.equals("")) {
				hql.append("where inm.direccion like '%"+valorbusqueda+"%' and c.condicion='"+tipo+"' ");
				}else {
				hql.append("where inm.direccion like '%"+valorbusqueda+"%' ");	
				}
		}else if(tipobusqueda==3){
			if (!tipo.equals("")) {
				hql.append("where i.nombrescompletos like '%"+valorbusqueda+"%' and c.condicion='"+tipo+"' ");
				}else {
				hql.append("where i.nombrescompletos like '%"+valorbusqueda+"%' ");	
				}
		}else{
			hql.append("where c.idcontrato = '"+valorbusqueda+"' ");
			  
		}
		if(tipobusqueda!=4){
			hql.append("and a.sifinalizado = 0 ");
		}
		
		hql.append(" order by a.nroadenda desc");
		
		
		Query query= sessionFactory.getCurrentSession().createQuery(hql.toString());
		return (List<CondicionBean>) query.setResultTransformer(Transformers.aliasToBean(CondicionBean.class)).list();

	}

	
	
	public void cambiarCondicionContrato(String condicion, int idcontrato, String numerocontrato) {
		getSessionFactory().getCurrentSession().createSQLQuery("UPDATE CONTRATO SET CONDICION='"+condicion+"', NROCONTRATO='"+numerocontrato+"' WHERE IDCONTRATO='"+idcontrato+"'").executeUpdate();
	}
	

	
	/**
	 * Lista las contratos totalmente cancelados.
	 * @return Lista de mapas con los registros de los contratos, tipo List.
	 */
	@SuppressWarnings("unchecked")
	
	public List<Contrato> contratosTotalmenteCancelados() {
		return sessionFactory.getCurrentSession().createQuery("select c from Contrato c where c.condicion='Contrato' and c.numerocuotas=(select count(*) from Cuota cuo where cuo.contrato.idcontrato=c.idcontrato and cuo.cancelado='true') ").list();
	}



	@SuppressWarnings("unchecked")
	
	public List<Contrato> sincontratoFinalizados() {
		return sessionFactory.getCurrentSession().createQuery("select c from Contrato c where c.condicion='Sin Contrato' and c.fincobro is not null").list();
	}

	
	public void actualizarContrato(Contrato contrato) {
		StringBuilder hql = new StringBuilder();
		hql.append("update Contrato  set ");
		hql.append("idupa ='"+ contrato.getUpa().getIdupa()+"',");
		hql.append("idinquilino ='"+contrato.getInquilino().getIdinquilino()+"',");
		hql.append("siocupante ='"+contrato.getSiocupante()+"',");
		hql.append("sinombreocupante ='"+contrato.getSinombreocupante()+"',");
		hql.append("sidniocupante='"+contrato.getSidniocupante()+"',");
		hql.append("usoespecifico='"+contrato.getUsoespecifico()+"',");
		hql.append("iniciocobro='"+ FuncionesHelper.fechaToString(contrato.getIniciocobro())+"',");
		hql.append("montocuotasoles='"+contrato.getMontocuotasoles()+"',");
		hql.append("tipomoneda='"+contrato.getTipomoneda()+"',");
		hql.append("sicompromisopago='"+contrato.getSicompromisopago()+"',");
		hql.append("usuariomodificador='"+contrato.getUsuariomodificador()+"',");
		hql.append("fechamodificacion='"+FuncionesHelper.fechaHORAtoString(contrato.getFechamodificacion())+"' ");
		hql.append("where idcontrato ='"+contrato.getIdcontrato()+"'");
		
		sessionFactory.getCurrentSession().createSQLQuery(hql.toString()).executeUpdate();

	}

	@SuppressWarnings("unchecked")
	
	public List<PeriodoPagadoBean> obtenerPeriodosPagados(int idcontratoGlobal) {
		List<PeriodoPagadoBean> lista = new ArrayList<PeriodoPagadoBean>();
		
		StringBuilder hql = new StringBuilder();
		hql.append("select cuo.idcuota as idcuota,cuo.cuotanumero as mes,cuo.aniocuotames as anio, cuo.montosoles as monto,cuo.morasoles as mora,cuo.cancelado as sipagado,cuo.nropagos as nropagos , cuo.estado as estado, cuo.observacion as observacion ,cuo.montopenalizacion as montopenalizacion ");
		hql.append(" from Cuota cuo  ");
		hql.append("inner join cuo.contrato c ");
		hql.append("where c.idcontrato='"+idcontratoGlobal+"' and ( cuo.montosoles>=0 and cuo.nropagos>0 ) order by cuo.aniocuotames ,cuo.nromes asc");
		
		Query query= sessionFactory.getCurrentSession().createQuery(hql.toString());
		lista = query.setResultTransformer(Transformers.aliasToBean(PeriodoPagadoBean.class)).list();
		
	
		return lista;
	}
	@SuppressWarnings("unchecked")
	
	public List<PeriodoPagadoBean> obtenerPeriodosPagadosObservacion(int idcuotaGlobal) {
		List<PeriodoPagadoBean> lista = new ArrayList<PeriodoPagadoBean>();
		
		StringBuilder hql = new StringBuilder();
		hql.append("select cuo.idcuota as idcuota,cuo.cuotanumero as mes,cuo.aniocuotames as anio, cuo.montosoles as monto,cuo.morasoles as mora,cuo.cancelado as sipagado,cuo.nropagos as nropagos , cuo.estado as estado, cuo.observacion as observacion ");
		hql.append(" from Cuota cuo  ");
//		hql.append("inner join cuo.contrato c ");
		hql.append("where cuo.idcuota='"+idcuotaGlobal+"' order by cuo.aniocuotames , cuo.nromes asc");
		
		Query query= sessionFactory.getCurrentSession().createQuery(hql.toString());
		lista = query.setResultTransformer(Transformers.aliasToBean(PeriodoPagadoBean.class)).list();
		
	
		return lista;
	}
	@SuppressWarnings("unchecked")
	
	public List<Contrato> listacontratosxclaveupa(int claveupa) {
		System.out.println("claveupa="+claveupa);
		List<Contrato> lista = new ArrayList<Contrato>();
		
		StringBuilder hql = new StringBuilder();
		hql.append("select idcontrato as idcontrato from CONTRATO where IDUPA = (select IDUPA from UPA where CLAVE='");
		hql.append(+claveupa+"' )");		
		Query query= sessionFactory.getCurrentSession().createQuery(hql.toString());
		lista = query.setResultTransformer(Transformers.aliasToBean(Contrato.class)).list();
		
	
		return lista;
	}


	@SuppressWarnings("unchecked")
	
	public List<ComprobanteBean> buscarComprobantesPeriodo(int idcuota) {
		StringBuilder hql = new StringBuilder();	
		List<ComprobanteBean> lista= new ArrayList<ComprobanteBean>();
		hql.append("select cp.nroserie as nroserie,cp.nrocomprobante as nrocomprobante,cp.nroseriecomprobante as nroseriecomprobante,");
		hql.append("ndref.nroseriecomprobante as nronotadebitoref,ncref.nroseriecomprobante as nronotacreditoref,ncref2.nroseriecomprobante as nronotacreditoref2,");;
		hql.append("td.idtipodocu as idtipdocu,dcp.mora as mora,dcp.montosoles as total,dcp.montopenalizacion as penalizacion , ");	
		hql.append("cp.fechaemision as fecEmision,cp.fechacancelacion as fecCancelacion,");
		hql.append("usr.nombrescompletos as nombrecobrador,cp.usuariocreador as usrcre,cp.fechacreacion as feccre ");
		hql.append(" from Detallecuota dcp ");
		hql.append("inner join dcp.cuota cuo ");
		hql.append("inner join dcp.comprobantepago cp ");
		hql.append("left join cp.notadebito ndref ");
		hql.append("left join cp.notacredito ncref ");
		hql.append("left join cp.notacredito2 ncref2 ");
		hql.append("inner join cp.detallecartera dc ");
		hql.append("inner join cp.usuario usr ");
		hql.append("inner join  cp.tipodocu td ");

		hql.append("where cuo.idcuota='"+idcuota+"' and ( dcp.estado is null or dcp.estado='1' )order by cp.fechacancelacion asc");
		
		Query query= sessionFactory.getCurrentSession().createQuery(hql.toString());
		
		lista=query.setResultTransformer(Transformers.aliasToBean(ComprobanteBean.class)).list();
		return lista;
	}

	@SuppressWarnings("unchecked")
	
	public List<CondicionBean> obtenerCarteraActiva(String nombrecartera) {
		List<CondicionBean> lista = new ArrayList<CondicionBean>();
		StringBuilder hql = new StringBuilder();

		hql.append("select dc.iddetallecartera as  iddetallecartera, c.idcontrato as idcontrato,");
		hql.append("u.clave as claveUpa,i.nombrescompletos as nombresInquilino,u.uso as uso,");	
		hql.append("c.condicion as condicion,C.tipomoneda as moneda,c.iniciocobro as iniciocobro, c.fincobro as fincobro,");
		hql.append("c.montocuotasoles as cuota1,c.montocuotasoles2 as cuota2,c.montocuotasoles3 as cuota3,c.montocuotasoles4 as cuota4,c.montocuotasoles5 as cuota5,c.montocuotasoles6 as cuota6,");
		hql.append("(inm.direccion+' '+inm.numeroprincipal+' '+u.nombrenuminterior) as direccion ");
		hql.append("FROM  DETALLECARTERA AS DC ");
		hql.append("INNER JOIN CARTERA AS CAR ON CAR.IDCARTERA=DC.IDCARTERA ");
		hql.append("INNER JOIN CONTRATO AS C ON DC.IDCONTRATO=C.IDCONTRATO ");
		hql.append("INNER JOIN UPA AS U ON C.IDUPA=U.IDUPA ");
		hql.append("INNER JOIN INQUILINO AS I ON C.IDINQUILINO=I.IDINQUILINO ");
		hql.append("INNER JOIN INMUEBLE AS INM ON INM.IDINMUEBLE=U.IDINMUEBLE ");
		hql.append("WHERE C.IDCONTRATO=(select TOP 1.IDCONTRATO  from CONTRATO where IDupa=U.IDUPA order by INICIOCOBRO desc)");
		if (!nombrecartera.equals("")) {
			hql.append(" AND CAR.NUMERO='"+nombrecartera+"'");
		}
		
		
		hql.append(" order by inm.direccion asc,inm.numeroprincipal asc,u.nombrenuminterior asc");
		Query query= sessionFactory.getCurrentSession().createSQLQuery(hql.toString());
		
		lista = query.setResultTransformer(Transformers.aliasToBean(CondicionBean.class)).list();
		return lista;
	}

	
	public CondicionBean obtenerInformacionCondicion(int idcontrato) {
		StringBuilder hql = new StringBuilder();

		hql.append("select c.idcontrato as idcontrato,c.numerocuotas as numerocuotas,");
		hql.append("u.clave as claveUpa,i.nombrescompletos as nombresInquilino,u.uso as uso,i.dni as dni,i.ruc as ruc,i.carnetextranjeria as carnetextranjeria ,tp.descripcion as tipopersona, ubi.depa as departamento,ubi.prov as provincia, ubi.dist as distrito,");	
		hql.append("c.condicion as condicion,c.tipomoneda as moneda,c.iniciocobro as iniciocobro, c.fincobro as fincobro,c.sinombreocupante as nombreocupante,c.nrocontrato as nrocontrato ,c.estado as estado,");
		hql.append("c.iniciocontrato as iniciocontrato, c.fincontrato as fincontrato,");
		hql.append("c.iniciocontrato as iniciocontrato, c.fincontrato as fincontrato,");
		hql.append("c.siocupante as siocupante, c.sinombreocupante as nombreocupante,");
		hql.append("c.sinuevomantenimiento as sinuevomantenimiento,");
		hql.append("c.montocuotasoles as cuota1,c.montocuotasoles2 as cuota2,c.montocuotasoles3 as cuota3,c.montocuotasoles4 as cuota4,c.montocuotasoles5 as cuota5,c.montocuotasoles6 as cuota6,");
		hql.append("u.direccion as direccion, u.numprincipal as numeroprincipal, u.nombrenuminterior as numerointerior ,u.siprocesojudicial as siprocesojudicial,  c.siresuelto as siresuelto, c.observacionresuelto as observacionresuelto,c.observacionfinalizado as observacionfinalizado , ");
		hql.append("c.sireconocimientodeuda as sireconocimientodeuda , c.siescriturapublica as siescriturapublica ");
		hql.append("from Contrato c ");
		hql.append("inner join c.upa u ");
		hql.append("inner join c.inquilino i ");
		hql.append("inner join i.tipopersona tp ");
		hql.append("inner join u.inmueble inm ");
		hql.append("inner join inm.ubigeo ubi ");
		hql.append("where c.idcontrato='"+idcontrato+"'");
		hql.append(" order by inm.direccion asc,inm.numeroprincipal asc,u.nombrenuminterior asc");
		Query query= sessionFactory.getCurrentSession().createQuery(hql.toString());
		return (CondicionBean) query.setResultTransformer(Transformers.aliasToBean(CondicionBean.class)).uniqueResult();
	}
	public CondicionBean obtenerInformacionCondicionxArbitrio(int idupa) {
		StringBuilder hql = new StringBuilder();

		hql.append("select c.idcontrato as idcontrato,c.numerocuotas as numerocuotas,");
		hql.append("u.clave as claveUpa,i.nombrescompletos as nombresInquilino,u.uso as uso,i.dni as dni,i.ruc as ruc,tp.descripcion as tipopersona, ubi.depa as departamento,ubi.prov as provincia, ubi.dist as distrito,");	
		hql.append("c.condicion as condicion,c.tipomoneda as moneda,c.iniciocobro as iniciocobro, c.fincobro as fincobro,c.sinombreocupante as nombreocupante,c.nrocontrato as nrocontrato ,c.estado as estado,");
		hql.append("c.iniciocontrato as iniciocontrato, c.fincontrato as fincontrato,");
		hql.append("c.iniciocontrato as iniciocontrato, c.fincontrato as fincontrato,");
		hql.append("c.siocupante as siocupante, c.sinombreocupante as nombreocupante,");
		hql.append("c.sinuevomantenimiento as sinuevomantenimiento,");
		hql.append("c.montocuotasoles as cuota1,c.montocuotasoles2 as cuota2,c.montocuotasoles3 as cuota3,c.montocuotasoles4 as cuota4,c.montocuotasoles5 as cuota5,c.montocuotasoles6 as cuota6,");
		hql.append("inm.direccion as direccion, inm.numeroprincipal as numeroprincipal, u.nombrenuminterior as numerointerior ,c.siresuelto as siresuelto, c.observacionresuelto as observacionresuelto ");
		hql.append("from Contrato c ");
		hql.append("inner join c.upa u ");
		hql.append("inner join c.inquilino i ");
		hql.append("inner join i.tipopersona tp ");
		hql.append("inner join u.inmueble inm ");
		hql.append("inner join inm.ubigeo ubi ");
		hql.append("where u.idupa="+idupa+" ");
		hql.append(" order by inm.direccion asc,inm.numeroprincipal asc,u.nombrenuminterior asc");
		Query query= sessionFactory.getCurrentSession().createQuery(hql.toString());
		return (CondicionBean) query.setResultTransformer(Transformers.aliasToBean(CondicionBean.class)).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	
	public List<CondicionBean> obtenerCondicion(String condicion, String estado,String sicancelado) {
		List<CondicionBean> lista = new ArrayList<CondicionBean>();
		StringBuilder hql = new StringBuilder();

		hql.append("select c.idcontrato as idcontrato,");
		hql.append("u.idupa as idupa ,u.clave as claveUpa,i.nombrescompletos as nombresInquilino,u.uso as uso,");	
		hql.append("c.condicion as condicion,c.tipomoneda as moneda,c.iniciocontrato as iniciocontrato, c.fincontrato as fincontrato, c.nrocontrato as nrocontrato,");
		hql.append("c.montocuotasoles as cuota1,c.montocuotasoles2 as cuota2,c.montocuotasoles3 as cuota3,c.montocuotasoles4 as cuota4,c.montocuotasoles5 as cuota5,c.montocuotasoles6 as cuota6,");
		hql.append("(u.direccion + ' ' + u.numprincipal + ' ' + CASE WHEN u.nombrenuminterior <>'' then ('INT.'+u.nombrenuminterior) else '' end   ) as direccion, ubi.dist as distrito, c.usoespecifico as  usoespecifico ,c.siescriturapublica as siescriturapublica ,u.sicopropiedad as sicopropiedad  ");
		hql.append(" from Contrato c ");
		hql.append("inner join  c.upa u ");
		hql.append("inner join  c.inquilino i ");
		hql.append("inner join  u.inmueble inm ");
		hql.append("inner join  inm.ubigeo ubi ");
		if(condicion.equals("Contrato")){
		if (estado.equals("FINALIZADO") && sicancelado.equals("todos")) {
			hql.append("where  c.condicion='"+condicion+"' and c.estado='"+estado+"' and c.sianulado=0  ");
		} else if (estado.equals("FINALIZADO") && sicancelado.equals("si")){
			hql.append("where  c.condicion='"+condicion+"' and c.estado='"+estado+"'  and c.sicuotascanceladas='"+true+"'");
		}else if(estado.equals("FINALIZADO") && sicancelado.equals("no")){
			hql.append("where  c.condicion='"+condicion+"' and c.estado='"+estado+"'  and c.sicuotascanceladas='"+false+"'");
		}else{			
			if(estado.equals("todos") && sicancelado.equals("todos")){
				hql.append("where  c.condicion='"+condicion+"' ");
			}else{
			hql.append("where  c.condicion='"+condicion+"' and c.estado='"+estado+"' ");}
		}
		}else if(condicion.equals("Precontrato")){
			if(estado.equals("VIGENTE") && sicancelado.equals("todos")){
				hql.append(" where  c.condicion='"+condicion+"' and  c.sifinalizado='0'  and  c.estado='VIGENTE' and c.sianulado=0 ");
				}
				if(estado.equals("todos") && sicancelado.equals("todos")){
					hql.append(" where c.condicion='"+condicion+"'and c.sianulado=0 ");
				}
		}	else{
		
			if(condicion.equals("Global")){
				
				if(estado.equals("VIGENTE") && sicancelado.equals("todos")){
				hql.append(" where  c.sifinalizado='0'  and  c.estado='VIGENTE' and c.sianulado=0 ");
				}
				if(estado.equals("todos") && sicancelado.equals("todos")){
					hql.append(" where  c.sianulado=0 ");
				}
			}
		}
			
		hql.append(" order by u.clave,c.iniciocontrato ,c.fincontrato asc");
		System.out.println("QUERY="+hql.toString());
		Query query= sessionFactory.getCurrentSession().createQuery(hql.toString());
		
		lista = query.setResultTransformer(Transformers.aliasToBean(CondicionBean.class)).list();
		return lista;
	}
	@SuppressWarnings("unchecked")
	
	public List<CondicionBean> obtenerCondicionDeuda(String condicion, String estado,String sicancelado,String tipomoneda) {
		List<CondicionBean> lista = new ArrayList<CondicionBean>();
		StringBuilder hql = new StringBuilder();

		hql.append("select c.idcontrato as idcontrato,c.sinuevomantenimiento as sinuevomantenimiento ,c.sidetraccion as sidetraccion ,");
		hql.append("u.clave as claveUpa,i.nombrescompletos as nombresInquilino,u.uso as uso,c.estado as estado ,");	
		hql.append("c.condicion as condicion,c.tipomoneda as moneda,c.iniciocontrato as iniciocontrato, c.fincontrato as fincontrato, c.nrocontrato as nrocontrato,c.fincobro as fincobro ,");
		hql.append("c.montocuotasoles as cuota1,c.montocuotasoles2 as cuota2,c.montocuotasoles3 as cuota3,c.montocuotasoles4 as cuota4,c.montocuotasoles5 as cuota5,c.montocuotasoles6 as cuota6,");
		hql.append("(u.direccion + ' ' + u.numprincipal + ' ' + CASE WHEN u.nombrenuminterior <>'' then ('INT.'+u.nombrenuminterior) else '' end   ) as direccion, ubi.dist as distrito, c.usoespecifico as  usoespecifico ,c.siescriturapublica as siescriturapublica ");
		hql.append(" from Contrato c ");
		hql.append("inner join  c.upa u ");
		hql.append("inner join  c.inquilino i ");
		hql.append("inner join  u.inmueble inm ");
		hql.append("inner join  inm.ubigeo ubi ");
		
		if(condicion.equals("Contrato")){
		if (estado.equals("FINALIZADO") && sicancelado.equals("todos")) {
			hql.append("where  c.condicion='"+condicion+"' and c.estado='"+estado+"'   ");
		} else if (estado.equals("FINALIZADO") && sicancelado.equals("si")){
			hql.append("where  c.condicion='"+condicion+"' and c.estado='"+estado+"'  and c.sicuotascanceladas='"+true+"'");
		}else if(estado.equals("FINALIZADO") && sicancelado.equals("no")){
			hql.append("where  c.condicion='"+condicion+"' and c.estado='"+estado+"'  and c.sicuotascanceladas='"+false+"'");
		}else{			
			if(estado.equals("todos") && sicancelado.equals("todos")){
				hql.append("where  c.condicion='"+condicion+"' ");
			}else{
			hql.append("where  c.condicion='"+condicion+"' and c.estado='"+estado+"' ");}
		}
		}else{
			if(condicion.equals("Global")){
					
				if(estado.equals("VIGENTE") && sicancelado.equals("todos")){
				hql.append(" where  c.sifinalizado='0'  and  c.estado='"+estado+"' and c.sianulado=0  and c.tipomoneda='"+tipomoneda+"'  ");
//				hql.append(" where  c.sifinalizado='0'  and  c.estado='"+estado+"' and c.sianulado=0  and c.tipomoneda='"+tipomoneda+"'  and u.clave='01-0018-013' ");
				}
				if(estado.equals("FINALIZADO") && sicancelado.equals("todos")){
					hql.append(" where  c.sifinalizado='1'  and  c.estado='"+estado+"' and c.sianulado=0 and c.tipomoneda='"+tipomoneda+"' ");
				}
				if(estado.equals("GLOBAL") && sicancelado.equals("todos")){
					hql.append(" where  sianulado=0 and c.tipomoneda='"+tipomoneda+"'  and i.idinquilino in (select inq.idinquilino from Contrato co inner join co.inquilino inq where co.estado='VIGENTE' )");
				}

			}
		}
			
		hql.append(" order by u.clave,c.iniciocontrato ,c.fincontrato desc");
		System.out.println("QUERY="+hql.toString());
		Query query= sessionFactory.getCurrentSession().createQuery(hql.toString());
		
		lista = query.setResultTransformer(Transformers.aliasToBean(CondicionBean.class)).list();
		return lista;
	}
	@SuppressWarnings("unchecked")
	
	public List<CondicionBean> obtenerCondicionDeudaxUpa(String condicion, String estado,String sicancelado,String tipomoneda,String claveupa) {
		List<CondicionBean> lista = new ArrayList<CondicionBean>();
		StringBuilder hql = new StringBuilder();

		hql.append("select c.idcontrato as idcontrato,c.sinuevomantenimiento as sinuevomantenimiento ,c.sidetraccion as sidetraccion ,");
		hql.append("u.clave as claveUpa,i.nombrescompletos as nombresInquilino,u.uso as uso,c.estado as estado ,");	
		hql.append("c.condicion as condicion,c.tipomoneda as moneda,c.iniciocontrato as iniciocontrato, c.fincontrato as fincontrato, c.nrocontrato as nrocontrato,c.fincobro as fincobro ,");
		hql.append("c.montocuotasoles as cuota1,c.montocuotasoles2 as cuota2,c.montocuotasoles3 as cuota3,c.montocuotasoles4 as cuota4,c.montocuotasoles5 as cuota5,c.montocuotasoles6 as cuota6,");
		hql.append("(u.direccion + ' ' + u.numprincipal + ' ' + CASE WHEN u.nombrenuminterior <>'' then ('INT.'+u.nombrenuminterior) else '' end   ) as direccion, ubi.dist as distrito, c.usoespecifico as  usoespecifico ,c.siescriturapublica as siescriturapublica ");
		hql.append(" from Contrato c ");
		hql.append("inner join  c.upa u ");
		hql.append("inner join  c.inquilino i ");
		hql.append("inner join  u.inmueble inm ");
		hql.append("inner join  inm.ubigeo ubi ");
		
		if(condicion.equals("Contrato")){
		if (estado.equals("FINALIZADO") && sicancelado.equals("todos")) {
			hql.append("where  c.condicion='"+condicion+"' and c.estado='"+estado+"'   ");
		} else if (estado.equals("FINALIZADO") && sicancelado.equals("si")){
			hql.append("where  c.condicion='"+condicion+"' and c.estado='"+estado+"'  and c.sicuotascanceladas='"+true+"'");
		}else if(estado.equals("FINALIZADO") && sicancelado.equals("no")){
			hql.append("where  c.condicion='"+condicion+"' and c.estado='"+estado+"'  and c.sicuotascanceladas='"+false+"'");
		}else{			
			if(estado.equals("todos") && sicancelado.equals("todos")){
				hql.append("where  c.condicion='"+condicion+"' ");
			}else{
			hql.append("where  c.condicion='"+condicion+"' and c.estado='"+estado+"' ");}
		}
		}else{
			if(condicion.equals("Global")){
				
				if(estado.equals("VIGENTE") && sicancelado.equals("todos")){
				hql.append(" where  c.sifinalizado='0'  and  c.estado='"+estado+"' and c.sianulado=0  and c.tipomoneda='"+tipomoneda+"' ");
				}
				if(estado.equals("FINALIZADO") && sicancelado.equals("todos")){
					hql.append(" where  c.sifinalizado='1'  and  c.estado='"+estado+"' and c.sianulado=0 and c.tipomoneda='"+tipomoneda+"' ");
				}
				if(estado.equals("GLOBAL") && sicancelado.equals("todos")){
					//hql.append(" where  sianulado=0 and c.tipomoneda='"+tipomoneda+"' and  and i.idinquilino in (select inq.idinquilino from Contrato co inner join co.inquilino inq where co.estado='VIGENTE' ) ");
					hql.append(" where  c.sianulado=0 and u.clave='"+claveupa+"' ");

				}

			}
		}
			
		hql.append(" order by u.clave,c.iniciocobro,c.fincobro ,c.iniciocontrato ,c.fincontrato desc");
		System.out.println("QUERY="+hql.toString());
		Query query= sessionFactory.getCurrentSession().createQuery(hql.toString());
		
		lista = query.setResultTransformer(Transformers.aliasToBean(CondicionBean.class)).list();
		return lista;
	}
	@SuppressWarnings("unchecked")
	
	public List<CondicionBean> obtenerCondicionContratoDetraccion(String condicion, String estado,String sicancelado) {
		List<CondicionBean> lista = new ArrayList<CondicionBean>();
		StringBuilder hql = new StringBuilder();

		hql.append(" select c.idcontrato as idcontrato,");
		hql.append("u.clave as claveUpa,i.nombrescompletos as nombresInquilino,u.uso as uso,");	
		hql.append("c.condicion as condicion,c.tipomoneda as moneda,c.iniciocobro as iniciocobro, c.fincobro as fincobro,");
		hql.append("c.montocuotasoles as cuota1,c.montocuotasoles2 as cuota2,c.montocuotasoles3 as cuota3,c.montocuotasoles4 as cuota4,c.montocuotasoles5 as cuota5,c.montocuotasoles6 as cuota6, '1' as sidetraccion ,");
		hql.append("(inm.direccion+' '+inm.numeroprincipal+' '+u.nombrenuminterior) as direccion, ");
		hql.append(" car.numero as numerocartera");
		hql.append(" from Detallecartera dc");
		hql.append(" inner join dc.contrato c ");
		hql.append(" inner join dc.cartera car ");
		hql.append(" inner join c.upa u ");
		hql.append(" inner join c.inquilino i ");
		hql.append(" inner join u.inmueble inm ");
		hql.append(" inner join inm.ubigeo ubi ");
		//hql.append(" where c.estado='VIGENTE' ");
		hql.append(" where car.idcartera in (select ca.idcartera from MaeSerieComprobantepago mae inner join mae.cartera ca  where mae.serie like 'FN%' )  order by u.clave,c.iniciocontrato ,c.fincontrato asc");
		hql.append(" order by u.clave,c.iniciocontrato ,c.fincontrato asc");
		Query query= sessionFactory.getCurrentSession().createQuery(hql.toString());
		
		lista = query.setResultTransformer(Transformers.aliasToBean(CondicionBean.class)).list();
		return lista;
	}
	@SuppressWarnings("unchecked")	
	public List<CondicionBean> obtenerListaContratoxCuota(String condicion, String estado,String sicancelado) {
		List<CondicionBean> lista = new ArrayList<CondicionBean>();
		StringBuilder hql = new StringBuilder();

		hql.append("select c.idcontrato as idcontrato , cuo.feccre as feccre , cuo.montosoles as montosoles");
		hql.append(" from Cuota cuo ");
		hql.append("inner join  cuo.contrato c ");
		hql.append(" where cuo.feccre=(select max(feccre) from Cuota where idcontrato = c.idcontrato and cancelado='1' ) ");
		
		Query query= sessionFactory.getCurrentSession().createQuery(hql.toString());
		
		lista = query.setResultTransformer(Transformers.aliasToBean(CondicionBean.class)).list();
		return lista;
	}
	public List<CondicionBean> obtenerCondicionxFecha(String condicion, String estado,String sicancelado ,String desde , String hasta) {
		List<CondicionBean> lista = new ArrayList<CondicionBean>();
		StringBuilder hql = new StringBuilder();

		hql.append("select c.idcontrato as idcontrato,");
		hql.append("u.clave as claveUpa,i.nombrescompletos as nombresInquilino,u.uso as uso,");	
		hql.append("c.condicion as condicion,c.tipomoneda as moneda,c.iniciocontrato as iniciocontrato, c.fincontrato as fincontrato, c.nrocontrato as nrocontrato,");
		hql.append("c.montocuotasoles as cuota1,c.montocuotasoles2 as cuota2,c.montocuotasoles3 as cuota3,c.montocuotasoles4 as cuota4,c.montocuotasoles5 as cuota5,c.montocuotasoles6 as cuota6,");
		hql.append("(inm.direccion+' '+inm.numeroprincipal+' '+u.nombrenuminterior) as direccion , ubi.dist as distrito");
		hql.append(" from Contrato c ");
		hql.append("inner join  c.upa u ");
		hql.append("inner join  c.inquilino i ");
		hql.append("inner join  u.inmueble inm ");
		hql.append("inner join  inm.ubigeo ubi ");
		
		if (estado.equals("FINALIZADO") && sicancelado.equals("todos")) {
			hql.append("where  c.condicion='"+condicion+"' and c.estado='"+estado+"'   ");
		} else if (estado.equals("FINALIZADO") && sicancelado.equals("si")){
			hql.append("where  c.condicion='"+condicion+"' and c.estado='"+estado+"'  and c.sicuotascanceladas='"+true+"'");
		}else if(estado.equals("FINALIZADO") && sicancelado.equals("no")){
			hql.append("where  c.condicion='"+condicion+"' and c.estado='"+estado+"'  and c.sicuotascanceladas='"+false+"'");
		}else{
			if(estado.equals("todos") && sicancelado.equals("todos")){
				hql.append("where  c.condicion='"+condicion+"' ");
			}else{
			hql.append("where  c.condicion='"+condicion+"' and c.estado='"+estado+"' ");}
		}
		hql.append(" and (c.iniciocontrato>='"+desde+"' or c.iniciocobro>='"+desde+"')" );
		hql.append(" and (c.iniciocontrato<='"+hasta+"' or c.iniciocobro<='"+hasta+"')" );
		hql.append(" order by c.iniciocontrato,c.fincontrato asc");
		
		Query query= sessionFactory.getCurrentSession().createQuery(hql.toString());
		
		lista = query.setResultTransformer(Transformers.aliasToBean(CondicionBean.class)).list();
		return lista;
	}
	
	public Boolean verificarExisteCondicionVigente(int idupa) {
		StringBuilder hql = new StringBuilder();

		hql.append("select count(c.idcontrato) from Contrato  c  inner join c.upa u where c.estado='VIGENTE' and u.idupa='"+idupa+"' and c.condicion<>'"+Constantes.CONDICION_RECONOCIMIENTO_DEUDA+"'");

		Long rows= (Long) sessionFactory.getCurrentSession().createQuery(hql.toString()).uniqueResult();
		
		if(rows != null && rows > 0) return true;
		else return false;	

	}
	public Boolean verificarExisteCondicionVigente(int idupa , String condicion) {
		StringBuilder hql = new StringBuilder();

		hql.append("select count(c.idcontrato) from Contrato  c  inner join c.upa u where c.estado='VIGENTE' and u.idupa='"+idupa+"' and c.condicion='"+condicion+"' ");

		Long rows= (Long) sessionFactory.getCurrentSession().createQuery(hql.toString()).uniqueResult();
		
		if(rows != null && rows > 0) return true;
		else return false;	

	}
	@SuppressWarnings("unchecked")
	
	public List<CondicionBean> obtenerListaContratoBeanDeUpa(String claveupaTabCondicion) {
		StringBuilder hql = new StringBuilder();

		hql.append("select c.idcontrato as idcontrato,c.numerocuotas as numerocuotas,c.sicuotascanceladas as sicuotascanceladas,");
		hql.append("u.clave as claveUpa,i.nombrescompletos as nombresInquilino,u.uso as uso,i.dni as dni,i.ruc as ruc,i.carnetextranjeria as carnetextranjeria ,tp.descripcion as tipopersona, ubi.depa as departamento,ubi.prov as provincia, ubi.dist as distrito,u.siprocesojudicial as siprocesojudicial,");	
		hql.append("c.condicion as condicion,c.tipomoneda as moneda,c.iniciocobro as iniciocobro, c.fincobro as fincobro,c.sinombreocupante as nombreocupante,c.nrocontrato as nrocontrato ,c.estado as estado,");
		hql.append("c.iniciocontrato as iniciocontrato, c.fincontrato as fincontrato,");
		hql.append("c.sinuevomantenimiento as sinuevomantenimiento,");
		hql.append("c.usuariocreador as usrcre,c.fechacreacion as feccre, ");
		hql.append("c.usuariomodificador as usrmod,c.fechamodificacion as fecmod, ");
		hql.append("c.siresuelto as siresuelto,c.fecmodresuelto as fecmodresuelto, c.siadenda as siadenda ,c.siocupante as siocupante, c.sinombreocupante as nombreocupante,");
		hql.append("c.montocuotasoles as cuota1,c.montocuotasoles2 as cuota2,c.montocuotasoles3 as cuota3,c.montocuotasoles4 as cuota4,c.montocuotasoles5 as cuota5,c.montocuotasoles6 as cuota6,");
		hql.append("c.valorcartafianza as valorcartafianza,c.fechavencimientocartafianza as fechavencimientocartafianza,c.sidocumento as sidocumento,");
		hql.append("inm.direccion as direccion, inm.numeroprincipal as numeroprincipal, u.nombrenuminterior as numerointerior ,c.siresuelto as siresuelto, ");
		hql.append("c.sireconocimientodeuda as sireconocimientodeuda, ");
		hql.append("c.nroacta as nroacta , c.simontoinicialdeuda as simontoinicialdeuda, c.montoinicialdeuda as montoinicialdeuda , c.montodeuda as montodeuda ,c.siescriturapublica as siescriturapublica ");
		hql.append("from Contrato c ");
		hql.append("inner join c.upa u ");
		hql.append("inner join c.inquilino i ");
		hql.append("inner join i.tipopersona tp ");
		hql.append("inner join u.inmueble inm ");
		hql.append("inner join inm.ubigeo ubi ");
		//hql.append("where u.clave='"+claveupaTabCondicion+"' and c.sianulado <> 1 ");
		hql.append("where (u.clave='"+claveupaTabCondicion+"' or u.clavenueva='"+claveupaTabCondicion+"' ) and c.sianulado <> 1 ");
		hql.append(" order by c.iniciocobro desc");
		Query query= sessionFactory.getCurrentSession().createQuery(hql.toString());
		return (List<CondicionBean>) query.setResultTransformer(Transformers.aliasToBean(CondicionBean.class)).list();
	}
	public List<CondicionBean> obtenerListaContratoBean(String claveupaTabCondicion) {
		StringBuilder hql = new StringBuilder();

		hql.append("select c.idcontrato as idcontrato,c.numerocuotas as numerocuotas,c.sicuotascanceladas as sicuotascanceladas,");
		hql.append("u.clave as claveUpa,i.nombrescompletos as nombresInquilino,u.uso as uso,i.dni as dni,i.ruc as ruc,tp.descripcion as tipopersona, ubi.depa as departamento,ubi.prov as provincia, ubi.dist as distrito,u.siprocesojudicial as siprocesojudicial,");	
		hql.append("c.condicion as condicion,c.tipomoneda as moneda,c.iniciocobro as iniciocobro, c.fincobro as fincobro,c.sinombreocupante as nombreocupante,c.nrocontrato as nrocontrato ,c.estado as estado,");
		hql.append("c.iniciocontrato as iniciocontrato, c.fincontrato as fincontrato,");
		hql.append("c.sinuevomantenimiento as sinuevomantenimiento,");
		
		hql.append("c.siresuelto as siresuelto,c.siadenda as siadenda ,c.siocupante as siocupante, c.sinombreocupante as nombreocupante,");
		hql.append("c.montocuotasoles as cuota1,c.montocuotasoles2 as cuota2,c.montocuotasoles3 as cuota3,c.montocuotasoles4 as cuota4,c.montocuotasoles5 as cuota5,c.montocuotasoles6 as cuota6,");
		hql.append("c.valorcartafianza as valorcartafianza,c.fechavencimientocartafianza as fechavencimientocartafianza,");
		hql.append("inm.direccion as direccion, inm.numeroprincipal as numeroprincipal, u.nombrenuminterior as numerointerior ,c.siresuelto as siresuelto ");
		hql.append("from Contrato c ");
		hql.append("inner join c.upa u ");
		hql.append("inner join c.inquilino i ");
		hql.append("inner join i.tipopersona tp ");
		hql.append("inner join u.inmueble inm ");
		hql.append("inner join inm.ubigeo ubi ");
//		hql.append("where u.clave='"+claveupaTabCondicion+"'");
		hql.append(" order by c.iniciocobro desc");
		Query query= sessionFactory.getCurrentSession().createQuery(hql.toString());
		return (List<CondicionBean>) query.setResultTransformer(Transformers.aliasToBean(CondicionBean.class)).list();
	}
	public List<CondicionBean> obtenerListaContratoUpa() {
		StringBuilder hql = new StringBuilder();

		hql.append("select c.idcontrato as idcontrato,c.numerocuotas as numerocuotas,c.sicuotascanceladas as sicuotascanceladas,");
		hql.append("u.clave as claveUpa,i.nombrescompletos as nombresInquilino,u.uso as uso,i.dni as dni,i.ruc as ruc,tp.descripcion as tipopersona, ubi.depa as departamento,ubi.prov as provincia, ubi.dist as distrito,u.siprocesojudicial as siprocesojudicial,");	
		hql.append("c.condicion as condicion,c.tipomoneda as moneda,c.iniciocobro as iniciocobro, c.fincobro as fincobro,c.sinombreocupante as nombreocupante,c.nrocontrato as nrocontrato ,c.estado as estado,");
		hql.append("c.iniciocontrato as iniciocontrato, c.fincontrato as fincontrato,");
		hql.append("c.sinuevomantenimiento as sinuevomantenimiento,");
		
		hql.append("c.siresuelto as siresuelto,c.siadenda as siadenda ,c.siocupante as siocupante, c.sinombreocupante as nombreocupante,");
		hql.append("c.montocuotasoles as cuota1,c.montocuotasoles2 as cuota2,c.montocuotasoles3 as cuota3,c.montocuotasoles4 as cuota4,c.montocuotasoles5 as cuota5,c.montocuotasoles6 as cuota6,");
		hql.append("c.valorcartafianza as valorcartafianza,c.fechavencimientocartafianza as fechavencimientocartafianza,");
		hql.append("inm.direccion as direccion, inm.numeroprincipal as numeroprincipal, u.nombrenuminterior as numerointerior ,c.siresuelto as siresuelto ");
		hql.append("from Contrato c ");
		hql.append("inner join c.upa u ");
		hql.append("inner join c.inquilino i ");
		hql.append("inner join i.tipopersona tp ");
		hql.append("inner join u.inmueble inm ");
		hql.append("inner join inm.ubigeo ubi ");
		hql.append("where c.estado='VIGENTE' ");
		hql.append(" order by c.iniciocobro desc");
		Query query= sessionFactory.getCurrentSession().createQuery(hql.toString());
		return (List<CondicionBean>) query.setResultTransformer(Transformers.aliasToBean(CondicionBean.class)).list();
	}
	public CondicionBean obtenerCondicionBean(int idcondicion) {
		StringBuilder hql = new StringBuilder();	
		
		hql.append("select c.idcontrato as idcontrato,c.condicion as condicion,c.nrocontrato as nrocontrato,c.siocupante as siocupante,c.sinombreocupante as nombreocupante,c.sidniocupante as dniocupante ,c.tipomoneda as moneda,");
		hql.append("c.iniciocontrato as iniciocontrato,c.fincontrato as fincontrato,c.iniciocobro as iniciocobro,c.fincobro as fincobro,");
		hql.append("c.sicuotascanceladas as sicuotascanceladas,c.estado as estado,c.numerocuotas as numerocuotas,");
		hql.append("c.siresuelto as siresuelto,c.siadenda as siadenda,");
		hql.append("c.sinuevomantenimiento as sinuevomantenimiento,");
		hql.append("c.montocuotasoles as  cuota1,c.montocuotasoles2 as  cuota2,c.montocuotasoles3 as  cuota3,c.montocuotasoles4 as  cuota4,c.montocuotasoles5 as  cuota5,c.montocuotasoles6 as  cuota6, ");
		hql.append("c.valorcartafianza as valorcartafianza,c.fechavencimientocartafianza as fechavencimientocartafianza,");
		hql.append("u.clave as claveUpa,i.nombrescompletos as nombresInquilino,i.ruc as ruc,i.dni as dni,");
		hql.append("(inm.direccion+' '+inm.numeroprincipal+' '+u.nombrenuminterior) as direccion,ubi.dist as distrito");
		hql.append(" from Contrato c ");
		hql.append("inner join  c.upa u ");
		hql.append("inner join  u.inmueble inm ");
		hql.append("inner join  c.inquilino i ");
		hql.append("inner join  inm.ubigeo ubi ");
		hql.append("where c.idcontrato='"+idcondicion+"'" );
		
		Query query=getSessionFactory().getCurrentSession().createQuery(hql.toString());
		

		return	(CondicionBean) query.setResultTransformer(Transformers.aliasToBean(CondicionBean.class)).uniqueResult();
	}
	
	public CondicionBean obtenerUltimaCondicion(Integer idupa) {
		StringBuilder hql = new StringBuilder();
		hql.append("select c.idcontrato as idcontrato,c.condicion as condicion,c.nrocontrato as nrocontrato,c.siocupante as siocupante,c.sinombreocupante as nombreocupante,c.sidniocupante as dniocupante ,c.tipomoneda as moneda,");
		hql.append("c.iniciocontrato as iniciocontrato,c.fincontrato as fincontrato,c.iniciocobro as iniciocobro,c.fincobro as fincobro,");
		hql.append("c.sicuotascanceladas as sicuotascanceladas,c.estado as estado,c.numerocuotas as numerocuotas,");
		hql.append("c.siresuelto as siresuelto,c.siadenda as siadenda,");
		hql.append("c.sinuevomantenimiento as sinuevomantenimiento,");
		hql.append("c.montocuotasoles as  cuota1,c.montocuotasoles2 as  cuota2,c.montocuotasoles3 as  cuota3,c.montocuotasoles4 as  cuota4,c.montocuotasoles5 as  cuota5,c.montocuotasoles6 as  cuota6,");
		hql.append("c.valorcartafianza as valorcartafianza,c.fechavencimientocartafianza as fechavencimientocartafianza,");
		hql.append("u.clave as claveUpa,i.nombrescompletos as nombresInquilino,i.ruc as ruc,tper.descripcion as tipopersona,i.dni as dni,");	
		hql.append("(inm.direccion+' '+inm.numeroprincipal+' '+u.nombrenuminterior) as direccion,ubi.dist as distrito");
		hql.append(" from Contrato c ");
		hql.append("inner join  c.upa u ");
		hql.append("inner join  u.inmueble inm ");
		hql.append("inner join  c.inquilino i ");
		hql.append("inner join  i.tipopersona tper ");
		hql.append("inner join  inm.ubigeo ubi ");
		hql.append("where  u.idupa='"+idupa+"' ");
		hql.append("order by c.iniciocobro desc");
		
		

		Query query= sessionFactory.getCurrentSession().createQuery(hql.toString());
		query.setMaxResults(1);
		return	(CondicionBean) query.setResultTransformer(Transformers.aliasToBean(CondicionBean.class)).uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<CondicionBean> obtenerCondicionesVigentes(String nombrecartera) {
		StringBuilder hql = new StringBuilder();

		hql.append(" select c.idcontrato as idcontrato,");
		hql.append("u.clave as claveUpa,i.nombrescompletos as nombresInquilino,u.uso as uso,");	
		hql.append("c.condicion as condicion,c.tipomoneda as moneda,c.iniciocobro as iniciocobro, c.fincobro as fincobro,");
		hql.append("c.montocuotasoles as cuota1,c.montocuotasoles2 as cuota2,c.montocuotasoles3 as cuota3,c.montocuotasoles4 as cuota4,c.montocuotasoles5 as cuota5,c.montocuotasoles6 as cuota6,");
		hql.append("(inm.direccion+' '+inm.numeroprincipal+' '+u.nombrenuminterior) as direccion, ");
		hql.append(" car.numero as numerocartera");
		hql.append(" from Detallecartera dc");
		hql.append(" inner join dc.contrato c ");
		hql.append(" inner join dc.cartera car ");
		hql.append(" inner join c.upa u ");
		hql.append(" inner join c.inquilino i ");
		hql.append(" inner join u.inmueble inm ");
		hql.append(" inner join inm.ubigeo ubi ");
		hql.append(" where c.estado='VIGENTE' ");
		
		if (!nombrecartera.equals("")) {
			hql.append(" and car.numero='"+nombrecartera+ "'");
		}
		
		hql.append(" order by ubi.dist asc ,inm.direccion asc ,inm.numeroprincipal ,u.nombrenuminterior asc");
		
		
		Query query= sessionFactory.getCurrentSession().createQuery(hql.toString());
		return	query.setResultTransformer(Transformers.aliasToBean(CondicionBean.class)).list();
		
//		List<CondicionBean> lista = new ArrayList<CondicionBean>();
//		StringBuilder hql = new StringBuilder();
	//
//		hql.append("select dc.iddetallecartera as  iddetallecartera, c.idcontrato as idcontrato,");
//		hql.append("u.clave as claveUpa,i.nombrescompletos as nombresInquilino,u.uso as uso,");	
//		hql.append("c.condicion as condicion,c.tipomoneda as moneda,c.iniciocobro as iniciocobro, c.fincobro as fincobro,");
//		hql.append("c.montocuotasoles as cuota1,c.montocuotasoles2 as cuota2,c.montocuotasoles3 as cuota3,c.montocuotasoles4 as cuota4,c.montocuotasoles5 as cuota5,c.montocuotasoles6 as cuota6,");
//		hql.append("(inm.direccion+' '+inm.numeroprincipal+' '+u.nombrenuminterior) as direccion ");
//		hql.append("FROM  DETALLECARTERA AS DC ");
//		hql.append("INNER JOIN CARTERA AS CAR ON CAR.IDCARTERA=DC.IDCARTERA ");
//		hql.append("INNER JOIN CONTRATO AS C ON DC.IDCONTRATO=C.IDCONTRATO ");
//		hql.append("INNER JOIN UPA AS U ON C.IDUPA=U.IDUPA ");
//		hql.append("INNER JOIN INQUILINO AS I ON C.IDINQUILINO=I.IDINQUILINO ");
//		hql.append("INNER JOIN INMUEBLE AS INM ON INM.IDINMUEBLE=U.IDINMUEBLE ");
//		hql.append("INNER JOIN UBIGEO AS UBI ON INM.UBIGEO=UBI.UBIGEO ");
//		hql.append("WHERE C.IDCONTRATO=(select TOP 1.IDCONTRATO  from CONTRATO where IDupa=U.IDUPA order by INICIOCOBRO desc)");
//		if (!nombrecartera.equals("")) {
//			hql.append(" AND CAR.NUMERO='"+nombrecartera+"'");
//		}
	//	
	//	
////		hql.append(" order by inm.direccion asc,inm.numeroprincipal asc,u.nombrenuminterior asc");
//		hql.append(" order by ubi.dist asc ,inm.direccion asc ,inm.numeroprincipal ,u.nombrenuminterior asc");
//		Query query= sessionFactory.getCurrentSession().createSQLQuery(hql.toString());
	//	
//		lista = query.setResultTransformer(Transformers.aliasToBean(CondicionBean.class)).list();
//		return lista;
	}


	@Override
	public CondicionBean verificarDisponibilidadContrato(int idupa) {
		
			
			StringBuilder hql = new StringBuilder();

			hql.append("  select i.nombrescompletos as nombresInquilino,i.idinquilino as idinquilino, i.ruc as ruc, tp.descripcion as tipopersona, i.ruc as ruc");
			hql.append("  ,c.condicion as condicion,c.fincontrato as fincontrato");
			hql.append("  ,u.idupa as idupa,u.clave as claveUpa,u.uso as uso");
			hql.append("  ,inm.direccion as direccion, inm.numeroprincipal as numeroprincipal, u.nombrenuminterior as numerointerior  ");
			
			hql.append("  from Contrato  c");
			hql.append("  inner join c.inquilino i ");
			hql.append("  inner join i.tipopersona tp ");
			hql.append("  inner join c.upa u ");
			hql.append("  inner join u.inmueble inm ");
			hql.append("  where c.estado='"+Constantes.CONDICION_ESTADO_VIGENTE+"' and u.idupa='"+idupa+"' and c.tipo='"+Constantes.CONTRATO_TIPO_RENOVACION+"'");
			
			Query query= sessionFactory.getCurrentSession().createQuery(hql.toString());
		
			return (CondicionBean) query.setResultTransformer(Transformers.aliasToBean(CondicionBean.class)).uniqueResult();
			
	}


	@Override
	public void actualizarDevolucionInmuebleContrato(int idcontrato,String fechaactadevolucion, String numeroactadevolucion) {
		String updateQuery="UPDATE CONTRATO SET SIACTADEVOLUCION='TRUE',FECHAACTADEVOLUCION='"+fechaactadevolucion+"', NUMEROACTADEVOLUCION='"+numeroactadevolucion+"' WHERE IDCONTRATO='"+idcontrato+"'";
		getSessionFactory().getCurrentSession().createSQLQuery(updateQuery).executeUpdate();
	}


	@Override
	public void actualizarPenalidadContrato(int idcontrato, String observacionpenalidad) {
		String updateQuery="UPDATE CONTRATO SET SIPENALIDAD='FALSE', OBSERVACIONPENALIDAD='"+observacionpenalidad+"' WHERE IDCONTRATO='"+idcontrato+"'";
		getSessionFactory().getCurrentSession().createSQLQuery(updateQuery).executeUpdate();
		
	}


	@Override
	public List<RentaBean> obtenerRentasNuevoMantenimientoContrato(
			int idcontrato) {
		StringBuilder hql = new StringBuilder();

		hql.append("select dc.mes as mes, dc.anio as anio,dc.secuencia as secuencia, dc.contraprestacion as contraprestacion,dc.renta as renta, dc.montopagar as montopagar, ");
		hql.append(" dc.siclausulareconocimientorenta as siclausulareconocimientorenta, dc.siclausulaperiodogracia as siclausulaperiodogracia,dc.siclausulareconocimientoinversion as siclausulareconocimientoinversion, dc.siclausulapagoposterior as siclausulapagoposterior, ");
		hql.append(" dc.siclausulasuspensiontemporalrenta as siclausulasuspensiontemporalrenta, ");
		hql.append(" dc.montopagoposterior as montopagoposterior, dc.observacionpagoposterior as observacionpagoposterior,dc.descuentoreconocimientorenta as descuentoreconocimientorenta, dc.observacionreconocimientorenta as observacionreconocimientorenta ");
		hql.append(" from MaeDetalleCondicion ");
		hql.append(" dc inner join dc.contrato c");
		hql.append(" where c.idcontrato= "+idcontrato+" and dc.estado='GENERADO' ");
		hql.append(" order by dc.secuencia asc");
		
		
		Query query= sessionFactory.getCurrentSession().createQuery(hql.toString());
		
		return (List<RentaBean>) query.setResultTransformer(Transformers.aliasToBean(RentaBean.class)).list();
	}
    
	
	@SuppressWarnings("unchecked")
	
	public List<MaeDetalleCondicion> obtenerListaMaeCondicion(int idcontrato) {
		return sessionFactory.getCurrentSession().createQuery("select mae from MaeDetalleCondicion mae inner join mae.contrato c where  c.idcontrato="+idcontrato+"order by mae.secuencia asc  ").list();
	}
   
	@SuppressWarnings("unchecked")
	
	public List<MaeDetCondicionDetalle> obtenerListaMaeCondicionDetalle(int idcontrato) {
		return sessionFactory.getCurrentSession().createQuery("select maed from MaeDetCondicionDetalle maed inner join maed.maedetallecondicion mae  inner join mae.contrato c where  c.idcontrato="+idcontrato+"order by maed.secuencia asc  ").list();
	}

	@Override
	public void asignarCondicion(Contrato c ,Integer idcontrato, String nombreusuario) {
		String updateQuery="UPDATE CONTRATO SET SIREFERENCIARECONOCIMIENTODEUDA='TRUE',IDRECONOCIMIENTODEUDA="+c.getIdcontrato()+" , USR_CRE_ACTA_RECON_DEUDA='"+nombreusuario+"', FEC_CRE_ACTA_RECON_DEUDA=GETDATE()  WHERE IDCONTRATO='"+idcontrato+"' AND ( SIREFERENCIARECONOCIMIENTODEUDA='FALSE' OR SIREFERENCIARECONOCIMIENTODEUDA IS NULL ) ";
		getSessionFactory().getCurrentSession().createSQLQuery(updateQuery).executeUpdate();
		
	}
	
	public void registrarMaeDetalleCondicion(MaeDetalleCondicion maedetalle){
		getSessionFactory().getCurrentSession().saveOrUpdate(maedetalle);
	}
	
	@SuppressWarnings("unchecked")
	
	public MaeDetalleCondicion getMaeDetalleCondicion(int idDetalleCondicion) {
		return (MaeDetalleCondicion)sessionFactory.getCurrentSession().
				createQuery("select mae from MaeDetalleCondicion mae  where  mae.idDetalleCondicion="+idDetalleCondicion+""
						+ " and mae.estado='GENERADO' order by mae.secuencia asc  ").uniqueResult();
	}
	@SuppressWarnings("unchecked")
	public List<Fechapago> listarFechaPago(){
		return getSessionFactory().getCurrentSession().createQuery("from Fechapago f where f.estado='1' ").list();
	}
	

	public Fechapago getFechaPago(int id){
		return (Fechapago) getSessionFactory().getCurrentSession().createQuery("select f from Fechapago f where f.id_fechapago='"+id+"'").uniqueResult();
	}
	
	@Transactional(readOnly = true)
	public Detalleclausula getDetalleClausula(String id) {
		return (Detalleclausula) getSessionFactory().getCurrentSession().createQuery("from Detalleclausula dt  where dt.id_detalle='"+id+"'").uniqueResult() ;
	}

	@Override
	public Tipoclausula getTipoClausula(String id) {
		return (Tipoclausula) getSessionFactory().getCurrentSession().createQuery("select tc from Tipoclausula tc where tc.id_tc='"+id+"'").uniqueResult() ;
	}

	@SuppressWarnings("unchecked")
	public List<Tipoclausula> listarTipoClausula() {
		return getSessionFactory().getCurrentSession().createQuery("from Tipoclausula tc where tc.estado='1' ").list();
	}

	@SuppressWarnings("unchecked")
	public List<Condicionincumplimientoclausula> listarCondicionIncumplimiento(
			Integer idcontrato) {
		return getSessionFactory().getCurrentSession().createQuery("from Condicionincumplimientoclausula cci where cci.estado='1' and cci.contrato.idcontrato = "+idcontrato).list();
	}

	@SuppressWarnings("unchecked")
	public List<Detalleclausula> listarDetalleTipoClausula(String id) {
		// TODO Auto-generated method stub
		return getSessionFactory().getCurrentSession().createQuery("from Detalleclausula dtc where dtc.estado='1' and dtc.tipoclausula.id_tc='"+id+"'").list();

	}
	
	public void registrarClausulaContrato(Condicionincumplimientoclausula clausula) {
		getSessionFactory().getCurrentSession().saveOrUpdate(clausula);
	}

	@Override
	public boolean existeUltimaCondicionVigenteContrato(Integer idcontrato) {
		StringBuilder hql = new StringBuilder();
				hql.append(" select  c.idcontrato from Contrato c ");
				hql.append(" inner join c.upa u");
				hql.append(" where u.idupa = (select uu.idupa from Contrato cc inner join cc.upa uu where cc.idcontrato='"+idcontrato+"') ");
				hql.append(" and c.iniciocobro = dateadd( DAY ,1,(select  co.fincobro from Contrato co where co.idcontrato='"+idcontrato+"' )) ");
		Integer rows= (Integer) sessionFactory.getCurrentSession().createQuery(hql.toString()).uniqueResult();
		
		if(rows != null && rows > 0) return true;
		else return false;	
		
		
		
	}
	@SuppressWarnings("unchecked")
	public List<CondicionBean> obtenerListaContratos(List<String> condicion,List<String> estado,String desdeinicio,String hastainicio,String desdefin, String hastafin){
		List<CondicionBean> lista = new ArrayList<CondicionBean>();
		StringBuilder hql = new StringBuilder();
		String op1="",op2="",op3="",op4="",op5="",op6="";
		boolean siSinContrato=false;
		boolean sifinalizado=false;
		if(condicion.size()>0){
			for (int i = 0; i < condicion.size(); i++) {
				if(i<1){
					op1=op1+" and ( c.condicion= '"+condicion.get(i)+"' ";
				}else{
					op1=op1+" or  c.condicion= '"+condicion.get(i)+"' ";
				}
				if(condicion.get(i).equalsIgnoreCase(Constante.CONDICION_SINCONTRATO)){
					siSinContrato=true;
				}
			}
			op1=op1+") ";
			
		}
		if(estado.size()>0){
			for (int i = 0; i < estado.size(); i++) {
				if(i<1){
					op2=op2+" and ( c.estado= '"+estado.get(i)+"' ";
				}else{
					op2=op2+" or  c.estado= '"+estado.get(i)+"' ";
				}
				if(estado.get(i).equalsIgnoreCase(Constante.CONDICION_CONTRATO_FINALIZADO)){
					sifinalizado=true;
				}
			}
			op2=op2+") ";
			
		}
		if(desdeinicio!=null ){
			if(siSinContrato){
				op3=" and (c.iniciocontrato>='"+desdeinicio+"' or c.iniciocobro>='"+desdeinicio+"') ";
			}else{
				op3=" and (c.iniciocontrato>='"+desdeinicio+"') ";
			}			
		}
		if(hastainicio!=null){
			if(siSinContrato)
				op4=" and (c.iniciocontrato<='"+hastainicio+"' or c.iniciocobro<='"+hastainicio+"') ";
			else
				op4=" and (c.iniciocontrato<='"+hastainicio+"')";
		}
		if(desdefin!=null){
			if(sifinalizado){
				if(siSinContrato){
					op5=" and (c.fincontrato>='"+desdefin+"' or c.fincobro>='"+desdefin+"') ";
				}
			}else{
				op5=" and (c.fincontrato>='"+desdefin+"')";
			}

		}
		if(hastafin!=null){
		  if(sifinalizado){
			if(siSinContrato)
				op6=" and (c.fincontrato<='"+hastafin+"' or c.fincontrato<='"+hastafin+"') ";
		  }	else
				op6=" and (c.fincontrato<='"+hastafin+"') ";
		}
		

		
////////////////////////////
		hql.append("select c.idcontrato as idcontrato,c.numerocuotas as numerocuotas,c.sicuotascanceladas as sicuotascanceladas,");
		hql.append("u.clave as claveUpa,i.nombrescompletos as nombresInquilino,c.usoespecifico as usoespecifico ,c.tipo as tipo, u.uso as uso,i.dni as dni,i.ruc as ruc,i.carnetextranjeria as carnetextranjeria ,tp.descripcion as tipopersona, ubi.depa as departamento,ubi.prov as provincia, ubi.dist as distrito,");	
		hql.append("c.condicion as condicion,c.tipomoneda as moneda,c.iniciocobro as iniciocobro, c.fincobro as fincobro,c.sinombreocupante as nombreocupante,c.nrocontrato as nrocontrato ,c.estado as estado,");
		hql.append("c.iniciocontrato as iniciocontrato, c.fincontrato as fincontrato,");
		hql.append("c.sinuevomantenimiento as sinuevomantenimiento,");
		hql.append("c.usuariocreador as usrcre,c.fechacreacion as feccre, ");
		hql.append("c.usuariomodificador as usrmod,c.fechamodificacion as fecmod, ");
		hql.append("c.siresuelto as siresuelto,c.fecmodresuelto as fecmodresuelto, c.siadenda as siadenda ,c.siocupante as siocupante, c.sinombreocupante as nombreocupante,");
		hql.append("c.montocuotasoles as cuota1,c.montocuotasoles2 as cuota2,c.montocuotasoles3 as cuota3,c.montocuotasoles4 as cuota4,c.montocuotasoles5 as cuota5,c.montocuotasoles6 as cuota6,");
		hql.append("c.valorcartafianza as valorcartafianza,c.fechavencimientocartafianza as fechavencimientocartafianza,c.sidocumento as sidocumento,");
		hql.append("c.siresuelto as siresuelto,c.sireconocimientodeuda as sireconocimientodeuda, ");
		hql.append("c.nroacta as nroacta , c.simontoinicialdeuda as simontoinicialdeuda, c.montoinicialdeuda as montoinicialdeuda , c.montodeuda as montodeuda ,c.siescriturapublica as siescriturapublica,u.siprocesojudicial as siprocesojudicial, ");
		hql.append("(u.direccion + ' ' + u.numprincipal + ' ' + CASE WHEN u.nombrenuminterior <>'' then ('INT.'+u.nombrenuminterior) else '' end   ) as direccion, ubi.dist as distrito, c.usoespecifico as  usoespecifico ,c.siescriturapublica as siescriturapublica ,u.sicopropiedad as sicopropiedad  ");
		hql.append("from Contrato c ");
		hql.append("inner join c.upa u ");
		hql.append("inner join c.inquilino i ");
		hql.append("inner join i.tipopersona tp ");
		hql.append("inner join u.inmueble inm ");
		hql.append("inner join inm.ubigeo ubi ");
		hql.append("where c.sianulado <> 1 "+op1+op2+op3+op4+op5+op6);
		hql.append(" order by u.clave,c.iniciocontrato,c.fincontrato asc");
		Query query= sessionFactory.getCurrentSession().createQuery(hql.toString());
		return (List<CondicionBean>) query.setResultTransformer(Transformers.aliasToBean(CondicionBean.class)).list();
		
	}
	
}
