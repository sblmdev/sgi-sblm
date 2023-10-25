package pe.gob.sblm.sgi.serviceImpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedProperty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.gob.sblm.api.commons.constants.sgi.Constantes;
import pe.gob.sblm.api.commons.utility.FuncionesHelper;
import pe.gob.sblm.sgi.bean.ArbitrioBean;
import pe.gob.sblm.sgi.bean.CondicionBean;
import pe.gob.sblm.sgi.bean.CuotaArbitrioBean;
import pe.gob.sblm.sgi.bean.CuotaBean;
import pe.gob.sblm.sgi.bean.DetalleCuotaBean;
import pe.gob.sblm.sgi.bean.MaeDetCondicionDetalleBean;
import pe.gob.sblm.sgi.bean.PagoArbitrioBean;
import pe.gob.sblm.sgi.bean.PeriodoContratoBean;
import pe.gob.sblm.sgi.bean.PeriodoPagadoBean;
import pe.gob.sblm.sgi.bean.RentaBean;
import pe.gob.sblm.sgi.bean.UpaPagadorDeudorBean;
import pe.gob.sblm.sgi.dao.ICarteraDAO;
import pe.gob.sblm.sgi.dao.IContratoDAO;
import pe.gob.sblm.sgi.dao.MaeDetallecondicionDao;
import pe.gob.sblm.sgi.entity.Cuota;
import pe.gob.sblm.sgi.entity.Contrato;
import pe.gob.sblm.sgi.entity.Comprobantepago;
import pe.gob.sblm.sgi.entity.Cuota_arbitrio;
import pe.gob.sblm.sgi.entity.Detallecuota;
import pe.gob.sblm.sgi.entity.Sunat_Comprobante;
import pe.gob.sblm.sgi.service.IRecaudacionReporteService;
import pe.gob.sblm.sgi.util.Almanaque;
import pe.gob.sblm.sgi.util.Constante;

@Transactional(readOnly = true)
@Service(value="reporteRecaudacionService")
public class RecaudacionReporteService extends RecaudacionCobranzaService implements IRecaudacionReporteService {
	
	@Autowired
	private ICarteraDAO carteraDAO;
	
	@Autowired
	private IContratoDAO contratoDAO;
	
	
	@Autowired
	private MaeDetallecondicionDao maeDetallecondicionDao;
	
	@ManagedProperty(value = "#{reporteRecaudacionService}")
	private transient IRecaudacionReporteService recaudacionReporteService;
	@ManagedProperty(value = "#{reporteRecaudacionService}")
	private transient IRecaudacionReporteService reporteRecaudacionService;
	
	private List<RentaBean> listaRentas= new ArrayList<RentaBean>();


	
	public double obtenerSumaTotalCuotas(List<CuotaBean> lista) {
		double suma=0;
		for (CuotaBean c : lista) {
			suma=suma+c.getMonto();
		}
		return suma;
	}
	
	public List<CuotaBean> generarPendientesParaCobroSinContrato(int idcondicion,Double tipocambio) {
		List<CuotaBean> listaCuotasPendientesCondicion = new ArrayList<CuotaBean>();
		CondicionBean condicionBean= new CondicionBean();
		condicionBean=contratoDAO.obtenerCondicionBean(idcondicion);
//		limpiarCampos();
		listaCuotasPendientesCondicion.clear();
		CuotaBean cuota;
		Calendar beginCalendar = Calendar.getInstance();
		
		beginCalendar.setTime(condicionBean.getIniciocobro());
		
		
		int i=0;
		List<Cuota> lista= new ArrayList<Cuota>();
		lista=listarcuotasxcontrato(condicionBean.getIdcontrato());
		
		Calendar now = GregorianCalendar.getInstance();
		
		if (condicionBean.getFincobro()!=null ) {
			now.setTime(condicionBean.getFincobro());
		} else {
			now.add(Calendar.MONTH, 18);
		}
		
		if (condicionBean.getMoneda().equals("S")) {
			/*LISTA CON TODOS LOS PAGOS DESDE EL 1999*/
			
			while (beginCalendar.compareTo(now) < 0) {
				
				cuota= new CuotaBean();
			    cuota.setNrocuota(i);
			    cuota.setAnio(beginCalendar.get(Calendar.YEAR));
			    cuota.setMes(Almanaque.obtenerNombreMes(beginCalendar.get(Calendar.MONTH)));
			    cuota.setSipagado("0");
			    
			    cuota.setCondicion("Pendiente");
			    cuota.setMora(0.0);
			    cuota.setMontopenalizacion(0.0);
			    cuota.setMonto(condicionBean.getCuota1());
			    cuota.setNropagos(1);
			    cuota.setMc(cuota.getMonto());
			    listaCuotasPendientesCondicion.add(cuota);
			    beginCalendar.add(Calendar.MONTH, 1);
			    
			    i++;
			}
			
			
			for (int j = 0;j < listaCuotasPendientesCondicion.size(); j++) {
				
				for (int k = 0; k <lista.size() ; k++) {
					if (listaCuotasPendientesCondicion.get(j).getMes().equals(lista.get(k).getMespagado())&& listaCuotasPendientesCondicion.get(j).getAnio()==lista.get(k).getAniocuotames()  ) {
						if (lista.get(k).getCancelado().equals("1")) {
							listaCuotasPendientesCondicion.get(j).setSipagado("1");
						} else {
							listaCuotasPendientesCondicion.get(j).setIdcuota(lista.get(k).getIdcuota()); 
							listaCuotasPendientesCondicion.get(j).setMonto(FuncionesHelper.redondear(condicionBean.getCuota1()-lista.get(k).getMontosoles(), 2));
							listaCuotasPendientesCondicion.get(j).setSiacuenta(true);
							listaCuotasPendientesCondicion.get(j).setDeudaacuenta(FuncionesHelper.redondear(lista.get(k).getMontosoles(),2));
							listaCuotasPendientesCondicion.get(j).setNropagos(lista.get(k).getNropagos());
							listaCuotasPendientesCondicion.get(j).setMoraacumulada(lista.get(k).getMorasoles());
							listaCuotasPendientesCondicion.get(j).setCondicion(lista.get(k).getCancelado().equals("0")?"Pendiente":"Generado");
						
						}
					}
				}
			}
			
			
		} else {
			
			/*LISTA CON TODOS LOS PAGOS DESDE EL 1999*/
			while (beginCalendar.compareTo(now) < 0) {
				
				cuota= new CuotaBean();
			    
			    cuota.setNrocuota(i);
			    cuota.setAnio(beginCalendar.get(Calendar.YEAR));
			    cuota.setMes(Almanaque.obtenerNombreMes(beginCalendar.get(Calendar.MONTH)));
			    cuota.setSipagado("0");
			    cuota.setCondicion("Pendiente");
			    cuota.setMora(0.0);
			    cuota.setMontopenalizacion(0.0);
			    cuota.setNropagos(1);
			    cuota.setMonto(FuncionesHelper.redondear(condicionBean.getCuota1()*tipocambio, 2));

			    listaCuotasPendientesCondicion.add(cuota);
			    beginCalendar.add(Calendar.MONTH, 1);
			    i++;
			}
			
			Double val;
			for (int j = 0;j < listaCuotasPendientesCondicion.size(); j++) {
				
				for (int k = 0; k <lista.size() ; k++) {
					if (listaCuotasPendientesCondicion.get(j).getMes().equals(lista.get(k).getMespagado())&& listaCuotasPendientesCondicion.get(j).getAnio()==lista.get(k).getAniocuotames() ) {
						
						if (lista.get(k).getCancelado().equals("1")) {
							listaCuotasPendientesCondicion.get(j).setSipagado("1");
						} else {
							val=obtenerTotalValorPagadoDolares(lista.get(k).getIdcuota());
							if (val!=null) {
								val=FuncionesHelper.redondear(val, 2);
							}else {
								val=FuncionesHelper.redondear(0.0, 2);
							}
							
							listaCuotasPendientesCondicion.get(j).setIdcuota(lista.get(k).getIdcuota()); 
							listaCuotasPendientesCondicion.get(j).setMonto(FuncionesHelper.redondear((condicionBean.getCuota1()-val)*tipocambio, 2));
							listaCuotasPendientesCondicion.get(j).setSiacuenta(true);
							listaCuotasPendientesCondicion.get(j).setDeudaacuenta(FuncionesHelper.redondear(lista.get(k).getMontosoles(),2));
							listaCuotasPendientesCondicion.get(j).setNropagos(lista.get(k).getNropagos());
							listaCuotasPendientesCondicion.get(j).setMoraacumulada(lista.get(k).getMorasoles());
							listaCuotasPendientesCondicion.get(j).setCondicion(lista.get(k).getCancelado().equals("0")?"Pendiente":"Generado");
						}
					}
				}
			}

		}

		
		return seleccionarRentaPendientes(listaCuotasPendientesCondicion);
	}
	
	public void numeroSerie(int idcondicion){
		CondicionBean condicionBean= new CondicionBean();
		condicionBean=contratoDAO.obtenerCondicionBean(idcondicion);
		System.out.println("condicionBean="+condicionBean.toString());
		System.out.println("claveupa_metodo="+condicionBean.getIdupa());
		List<Contrato> listacontrato =new ArrayList<Contrato>();
		listacontrato=contratoDAO.listacontratosxclaveupa(condicionBean.getIdupa());
		System.out.println("listacontrato:::="+listacontrato.toString());
	}
	
	public List<CuotaBean> generarPendientesParaCobroContrato(int idcondicion,Double tipocambio) {
		List<CuotaBean> listaCuotasPendientesCondicion = new ArrayList<CuotaBean>();
		CondicionBean condicionBean= new CondicionBean();
		condicionBean=contratoDAO.obtenerCondicionBean(idcondicion);
		//numeroSerie(idcondicion);
		listaCuotasPendientesCondicion.clear();
		
		CuotaBean cuota;
		Calendar beginCalendar = Calendar.getInstance();
		beginCalendar.setTime(condicionBean.getIniciocobro());
		int i=1;
		List<Cuota> lista= new ArrayList<Cuota>();
		lista=listarcuotasxcontrato(condicionBean.getIdcontrato());
		
		
		Calendar fin = Calendar.getInstance();
		fin.setTime(condicionBean.getFincobro());
		
		if (condicionBean.getMoneda().equals("S")) 
		{

				/*LISTA CON TODOS LOS PAGOS DEL CONTRATO*/
				while (beginCalendar.compareTo(fin) <= 0) {
					
					cuota= new CuotaBean();
				    cuota.setNrocuota(i);
				    cuota.setAnio(beginCalendar.get(Calendar.YEAR));
				    cuota.setMes(Almanaque.obtenerNombreMes(beginCalendar.get(Calendar.MONTH)));
				    cuota.setSipagado("0");
				    cuota.setCondicion("Pendiente");
				    cuota.setMora(0.0);
				    cuota.setMontopenalizacion(0.0);
				    cuota.setNropagos(1);
			
					if (i<=12) {
						cuota.setMonto(condicionBean.getCuota1());
					}else if (i<=24) {
						cuota.setMonto(condicionBean.getCuota2());
					}else if (i<=36) {
						cuota.setMonto(condicionBean.getCuota3());
					}else if (i<=48) {
						cuota.setMonto(condicionBean.getCuota4());
					}else if (i<=60) {
						cuota.setMonto(condicionBean.getCuota5());
					}else {
						cuota.setMonto(condicionBean.getCuota6());
					}
					cuota.setMc(cuota.getMonto());
				    listaCuotasPendientesCondicion.add(cuota);
				    beginCalendar.add(Calendar.MONTH, 1);
				    i++;
				
				}
				
				
				for (int j = 0;j < listaCuotasPendientesCondicion.size(); j++) {
					
					for (int k = 0; k <lista.size() ; k++) {
						if (listaCuotasPendientesCondicion.get(j).getMes().equals(lista.get(k).getMespagado())&& listaCuotasPendientesCondicion.get(j).getAnio()==lista.get(k).getAniocuotames()  ) {
							if (lista.get(k).getCancelado().equals("1")) { 
								listaCuotasPendientesCondicion.get(j).setSipagado("1");
							} else {
								
								listaCuotasPendientesCondicion.get(j).setIdcuota(lista.get(k).getIdcuota());
								listaCuotasPendientesCondicion.get(j).setSiacuenta(true);
								listaCuotasPendientesCondicion.get(j).setNropagos(lista.get(k).getNropagos());
								listaCuotasPendientesCondicion.get(j).setMoraacumulada(lista.get(k).getMorasoles());
								listaCuotasPendientesCondicion.get(j).setDeudaacuenta(FuncionesHelper.redondear(lista.get(k).getMontosoles(),2));
								listaCuotasPendientesCondicion.get(j).setMonto(FuncionesHelper.redondear(listaCuotasPendientesCondicion.get(j).getMonto()-lista.get(k).getMontosoles(), 2));
								listaCuotasPendientesCondicion.get(j).setCondicion(lista.get(k).getCancelado().equals("0")||listaCuotasPendientesCondicion.get(j).getMonto()>0?"Pendiente":"Generado");
							}
						}
					}
				}
			
			
		} 
		else 
		{
			/*LISTA CON TODOS LOS PAGOS DEL CONTRATO - EN DOLARES*/
			while (beginCalendar.compareTo(fin) <= 0) {
				
				cuota= new CuotaBean();
			    cuota.setNrocuota(i);
			    cuota.setAnio(beginCalendar.get(Calendar.YEAR));
			    cuota.setMes(Almanaque.obtenerNombreMes(beginCalendar.get(Calendar.MONTH)));
			    cuota.setSipagado("0");
			    cuota.setCondicion("Pendiente");
			    cuota.setMora(0.0);
			    cuota.setMontopenalizacion(0.0);
			    cuota.setNropagos(1);
			    
				if (i<=12) {
					cuota.setMonto(FuncionesHelper.redondear(condicionBean.getCuota1()*tipocambio, 2));
					cuota.setMontodolar(condicionBean.getCuota1());
				}else if (i<=24) {
					cuota.setMonto(FuncionesHelper.redondear(condicionBean.getCuota2()*tipocambio, 2));
					cuota.setMontodolar(condicionBean.getCuota2());
				}else if (i<=36) {
					cuota.setMonto(FuncionesHelper.redondear(condicionBean.getCuota3()*tipocambio, 2));
					cuota.setMontodolar(condicionBean.getCuota3());
				}else if (i<=48) {
					cuota.setMonto(FuncionesHelper.redondear(condicionBean.getCuota4()*tipocambio, 2));
					cuota.setMontodolar(condicionBean.getCuota4());
				}else if (i<=60) {
					cuota.setMonto(FuncionesHelper.redondear(condicionBean.getCuota5()*tipocambio, 2));
					cuota.setMontodolar(condicionBean.getCuota5());
				}else {
					cuota.setMontodolar(condicionBean.getCuota6());
					cuota.setMonto(FuncionesHelper.redondear(condicionBean.getCuota6()*tipocambio, 2));
				}
				
				cuota.setMc(cuota.getMonto());
			    
			    listaCuotasPendientesCondicion.add(cuota);
			    beginCalendar.add(Calendar.MONTH, 1);
			    i++;
			}
			
			Double val;
			for (int j = 0;j < listaCuotasPendientesCondicion.size(); j++) {
				
				for (int k = 0; k <lista.size() ; k++) {
					if (listaCuotasPendientesCondicion.get(j).getMes().equals(lista.get(k).getMespagado())&& listaCuotasPendientesCondicion.get(j).getAnio()==lista.get(k).getAniocuotames()  ) {
						if (lista.get(k).getCancelado().equals("1")) {
							listaCuotasPendientesCondicion.get(j).setSipagado("1");
						} else {
							val=obtenerTotalValorPagadoDolares(lista.get(k).getIdcuota());
							if (val!=null) {
								val=FuncionesHelper.redondear(val, 2);
							}else {
								val=FuncionesHelper.redondear(0.0, 2);
							}
							
							listaCuotasPendientesCondicion.get(j).setIdcuota(lista.get(k).getIdcuota()); 
							
							listaCuotasPendientesCondicion.get(j).setMonto(FuncionesHelper.redondear((listaCuotasPendientesCondicion.get(j).getMontodolar()-val)*tipocambio, 2));
							listaCuotasPendientesCondicion.get(j).setSiacuenta(true);
							listaCuotasPendientesCondicion.get(j).setDeudaacuenta(FuncionesHelper.redondear(lista.get(k).getMontosoles(),2));
							listaCuotasPendientesCondicion.get(j).setNropagos(lista.get(k).getNropagos());
							listaCuotasPendientesCondicion.get(j).setMoraacumulada(lista.get(k).getMorasoles());
							listaCuotasPendientesCondicion.get(j).setCondicion(lista.get(k).getCancelado().equals("0")?"Pendiente":"Generado");
						}
					}
				}
			}
		}
		
		
		return seleccionarRentaPendientes(listaCuotasPendientesCondicion);
	}
	
	public List<CuotaBean> seleccionarRentaPendientes(List<CuotaBean> listaCuotasPendientesCondicion) {
		List<CuotaBean> lista= new ArrayList<CuotaBean>();
			System.out.println("seleccionarRentapendiente");
			for (int i = 0; i < listaCuotasPendientesCondicion.size(); i++) {
//				if (!listaCuotasPendientesCondicion.get(i).getSipagado()) {
				if (listaCuotasPendientesCondicion.get(i).getSipagado().equals("0")) {
					lista.add(listaCuotasPendientesCondicion.get(i));
				}
			}
			
			listaCuotasPendientesCondicion.clear();
			listaCuotasPendientesCondicion.addAll(lista);
			
		
		return listaCuotasPendientesCondicion;
	}
	
	public ArrayList<CuotaBean> seleccionarRentaCanceladas(ArrayList<CuotaBean> lista){
		List<CuotaBean> listaCuotaBean= new ArrayList<CuotaBean>();

		for (int i = 0; i < lista.size(); i++) {
//			if (lista.get(i).getSipagado()) {
			if (lista.get(i).getSipagado().equals("1")) {
				listaCuotaBean.add(lista.get(i));
			}
		}

		lista.clear();
		lista.addAll(listaCuotaBean);

		return lista;
	}

	public  List<CuotaBean> obtenerInformacionCobroCondicionTipo(int idcondicion,String condicion,String tipo,Double tipocambio) {
		  
		if (condicion.equals("Sin Contrato")) {
			return obtenerInformacionCobroSinContratoTipo(idcondicion, tipo, tipocambio);
		} else {
			return obtenerInformacionCobroContratoTipo(idcondicion, tipo, tipocambio);
		}
	}
	public  List<CuotaBean> obtenerInformacionCobroCondicionTipoxDeuda(int idcondicion,String condicion,String tipo,Double tipocambio) {
		  
		if (condicion.equals("Sin Contrato")) {
			return obtenerInformacionCobroSinContratoTipoxDeuda(idcondicion, tipo, tipocambio);
		} else {
			return obtenerInformacionCobroContratoTipoxDeuda(idcondicion, tipo, tipocambio);
		}
	}

	public  List<CuotaBean> obtenerInformacionCobroCondicionTipoSinMantenimiento(int idcondicion,String condicion,String tipo,Double tipocambio) {
		  
		if (condicion.equals("Sin Contrato")) {
			return obtenerInformacionCobroTipoSinContrato(idcondicion, tipo, tipocambio);
		} else {
			return obtenerInformacionCobroContratoTipo(idcondicion, tipo, tipocambio);
		}
	}
	public List<CuotaBean> obtenerInformacionCobroContratoTipo(int idcondicion,String tipo,Double tipocambio) {
		listaRentas.clear();
		List<CuotaBean> listaPendientesContratoPreContratoCuotaBean= new ArrayList<CuotaBean>();
		CondicionBean condicionBean= new CondicionBean();
		condicionBean=contratoDAO.obtenerCondicionBean(idcondicion);
		
		listaPendientesContratoPreContratoCuotaBean.clear();


		CuotaBean cuota;
		List<Cuota> lista= new ArrayList<Cuota>();
		List<Detallecuota> listadetalle= new ArrayList<Detallecuota>();
		lista=listarcuotasxcontrato(condicionBean.getIdcontrato()); //lista de pagadas
		listadetalle=listardetallecuotasxcontrato(condicionBean.getIdcontrato()); //lista de pagadas
		Calendar iniciocobro = Calendar.getInstance();
		iniciocobro.setTime(condicionBean.getIniciocobro());

		Calendar fincobro= Calendar.getInstance();
		fincobro.set(Calendar.DATE, fincobro.getActualMaximum(Calendar.DAY_OF_MONTH)); /*Inicializando fecha*/

		if (tipo.equals("canceladas y pendientes") || tipo.equals("canceladas")  ) {
			fincobro.setTime(condicionBean.getFincobro());
		}else if (tipo.equals("ratio") || tipo.equals("pendientes")) {
			if (condicionBean.getFincobro().compareTo(fincobro.getTime())<=0){ /*Verifica si termino contrato antes de la fecha de hoy*/
				fincobro.setTime(condicionBean.getFincobro());
			}
		} 

		fincobro.set(Calendar.DATE, fincobro.getActualMaximum(Calendar.DAY_OF_MONTH));
		if (condicionBean.getSinuevomantenimiento() !=null && condicionBean.getSinuevomantenimiento()) {
		//listaRentas=recaudacionReporteService.obtenerRentasNuevoMantenimientoContrato(condicionBean.getIdcontrato());
		listaRentas=contratoDAO.obtenerRentasNuevoMantenimientoContrato(condicionBean.getIdcontrato());
		
	}
		int i=1,l=0;
		if (condicionBean.getMoneda().equals("S")) 
		{       
				/*LISTA CON TODOS LOS PAGOS DEL CONTRATO EN SOLES*/ /*CINDY*/
				while (iniciocobro.compareTo(fincobro) <= 0) 
				{

					cuota= new CuotaBean();
				    cuota.setNrocuota(i);
				    cuota.setAnio(iniciocobro.get(Calendar.YEAR));
				    cuota.setMes(Almanaque.obtenerNombreMes(iniciocobro.get(Calendar.MONTH)));
				    cuota.setSipagado("0");
				    cuota.setCondicion("Pendiente");
				    cuota.setSiacuenta(false);
				    cuota.setNropagos(1);

				    cuota.setRentapagada(0.0);
				    cuota.setMora(0.0);
				    cuota.setNuevamc(0.0);
				    cuota.setNuevarenta(0.0);
					if (condicionBean.getSinuevomantenimiento() !=null && condicionBean.getSinuevomantenimiento()) {
						cuota.setMonto(listaRentas.get(l).getMontopagar());
						
					}else{
					if (i<=12) 
					{
						cuota.setMonto(condicionBean.getCuota1());

					}
					else 
						if (i<=24) 
						{
							cuota.setMonto(condicionBean.getCuota2());
						}
						else 
							if (i<=36) 
							{
								cuota.setMonto(condicionBean.getCuota3());
							}
							else 
								if (i<=48) 
								{
									cuota.setMonto(condicionBean.getCuota4());
								}
								else 
									if (i<=60) 
									{
										cuota.setMonto(condicionBean.getCuota5());
									}
									else 
									{
										cuota.setMonto(condicionBean.getCuota6());
									}
					
					}
					cuota.setRenta(cuota.getMonto());
					cuota.setIgv(FuncionesHelper.redondear(cuota.getRenta()/1.18*0.18, 2));               
					listaPendientesContratoPreContratoCuotaBean.add(cuota);
				    iniciocobro.add(Calendar.MONTH, 1);
				    i++;l++;

				}
				
				/*SACA LA LISTA DE PENDIENTES soles-- COMPARA LISTA DE CONTRATOS CON LISTA DE CANCELADOS(TABLA CUOTA)*/
				for (int j = 0;j < listaPendientesContratoPreContratoCuotaBean.size(); j++) 
				{
					for (int k = 0; k <lista.size() ; k++) 
					{	
						if (listaPendientesContratoPreContratoCuotaBean.get(j).getMes().equals(lista.get(k).getMespagado())&& listaPendientesContratoPreContratoCuotaBean.get(j).getAnio()==lista.get(k).getAniocuotames()) 
						{
							if (lista.get(k).getCancelado().equals("1")) 
							{	
								listaPendientesContratoPreContratoCuotaBean.get(j).setSipagado("1");
								listaPendientesContratoPreContratoCuotaBean.get(j).setMonto(0.0);
								listaPendientesContratoPreContratoCuotaBean.get(j).setRentapagada(listaPendientesContratoPreContratoCuotaBean.get(j).getRenta());						
							} 
							else 
							{	
							  	
							if (lista.get(k).getCancelado().equals("2")&& tipo.equals("ratio")) {
								listaPendientesContratoPreContratoCuotaBean.get(j).setIdcuota(lista.get(k).getIdcuota());
								listaPendientesContratoPreContratoCuotaBean.get(j).setSiacuenta(true);
								listaPendientesContratoPreContratoCuotaBean.get(j).setNropagos(lista.get(k).getNropagos());
								listaPendientesContratoPreContratoCuotaBean.get(j).setMoraacumulada(lista.get(k).getMorasoles());
								Double montosoles=0.0;
								for( int p=0;p<listadetalle.size();p++){
									   
									if(lista.get(k).getIdcuota()==listadetalle.get(p).getCuota().getIdcuota()){
										
									  if(listadetalle.get(p).getCancelado().equals("0")){// monto en estado pendiente
										 
									  }else if(listadetalle.get(p).getCancelado().equals("1")){//monto en estado cancelado
										  //montosoles=montosoles+FuncionesHelper.redondear(listadetalle.get(p).getMontosoles(),2);
										 
									  }else{//montos en estado generado 
										  montosoles=montosoles+FuncionesHelper.redondear(listadetalle.get(p).getMontosoles(),2);
									  }
									  listaPendientesContratoPreContratoCuotaBean.get(j).setRentapagada(FuncionesHelper.redondear(montosoles,2));
									  listaPendientesContratoPreContratoCuotaBean.get(j).setMonto(FuncionesHelper.redondear(montosoles,2));
									}
								}
								
								//listaPendientesContratoPreContratoCuotaBean.get(j).setRentapagada(FuncionesHelper.redondear(lista.get(k).getMontosoles(),2)); 
								//listaPendientesContratoPreContratoCuotaBean.get(j).setMonto(FuncionesHelper.redondear(lista.get(k).getMontosoles(), 2));
								listaPendientesContratoPreContratoCuotaBean.get(j).setIgv(lista.get(k).getMontosoles()/1.18*0.18);
								listaPendientesContratoPreContratoCuotaBean.get(j).setCondicion(lista.get(k).getCancelado().equals("0")?"Pendiente":"Generado");
							  }else{
								 if((lista.get(k).getCancelado().equals("2")&& tipo.equals("pendientes"))||(lista.get(k).getCancelado().equals("2")&& tipo.equals("canceladas y pendientes"))){
										listaPendientesContratoPreContratoCuotaBean.get(j).setIdcuota(lista.get(k).getIdcuota());
										listaPendientesContratoPreContratoCuotaBean.get(j).setSiacuenta(true);
										listaPendientesContratoPreContratoCuotaBean.get(j).setNropagos(lista.get(k).getNropagos());
										listaPendientesContratoPreContratoCuotaBean.get(j).setMoraacumulada(lista.get(k).getMorasoles());
										Double montosoles=0.0;
										for( int p=0;p<listadetalle.size();p++){
											   
											if(lista.get(k).getIdcuota()==listadetalle.get(p).getCuota().getIdcuota()){
												
											  if(listadetalle.get(p).getCancelado().equals("0")){
												  
											  }else if(listadetalle.get(p).getCancelado().equals("1")){
												  montosoles=montosoles+FuncionesHelper.redondear(listadetalle.get(p).getMontosoles(),2);
												  
											  }else{
												  
											  }
											  listaPendientesContratoPreContratoCuotaBean.get(j).setRentapagada(FuncionesHelper.redondear(montosoles,2));
											}
										}
										//listaPendientesContratoPreContratoCuotaBean.get(j).setRentapagada(FuncionesHelper.redondear(0.0,2)); 
										listaPendientesContratoPreContratoCuotaBean.get(j).setMonto(FuncionesHelper.redondear(listaPendientesContratoPreContratoCuotaBean.get(j).getMonto()-listaPendientesContratoPreContratoCuotaBean.get(j).getRentapagada(), 2));
										listaPendientesContratoPreContratoCuotaBean.get(j).setIgv(listaPendientesContratoPreContratoCuotaBean.get(j).getMonto()/1.18*0.18);
										listaPendientesContratoPreContratoCuotaBean.get(j).setCondicion(lista.get(k).getCancelado().equals("0")?"Pendiente":"Generado");
								 }else{
								listaPendientesContratoPreContratoCuotaBean.get(j).setIdcuota(lista.get(k).getIdcuota());
								listaPendientesContratoPreContratoCuotaBean.get(j).setSiacuenta(true);
								listaPendientesContratoPreContratoCuotaBean.get(j).setNropagos(lista.get(k).getNropagos());
								listaPendientesContratoPreContratoCuotaBean.get(j).setMoraacumulada(lista.get(k).getMorasoles());
								Double montosoles=0.0;
								for( int p=0;p<listadetalle.size();p++){
									   
									if(lista.get(k).getIdcuota()==listadetalle.get(p).getCuota().getIdcuota()){
										
									  if(listadetalle.get(p).getCancelado().equals("0")){
										  montosoles=montosoles+FuncionesHelper.redondear(listadetalle.get(p).getMontosoles(),2);
									  }else if(listadetalle.get(p).getCancelado().equals("1")){
										  montosoles=montosoles+FuncionesHelper.redondear(listadetalle.get(p).getMontosoles(),2);
										  
									  }else{
										  
									  }
									  listaPendientesContratoPreContratoCuotaBean.get(j).setRentapagada(FuncionesHelper.redondear(montosoles,2));
									}
								}
								//listaPendientesContratoPreContratoCuotaBean.get(j).setRentapagada(FuncionesHelper.redondear(lista.get(k).getMontosoles(),2)); 
								//listaPendientesContratoPreContratoCuotaBean.get(j).setMonto(FuncionesHelper.redondear(listaPendientesContratoPreContratoCuotaBean.get(j).getMonto()-lista.get(k).getMontosoles(), 2));
								listaPendientesContratoPreContratoCuotaBean.get(j).setMonto(FuncionesHelper.redondear(listaPendientesContratoPreContratoCuotaBean.get(j).getMonto()-listaPendientesContratoPreContratoCuotaBean.get(j).getRentapagada(), 2));

								listaPendientesContratoPreContratoCuotaBean.get(j).setIgv(listaPendientesContratoPreContratoCuotaBean.get(j).getMonto()/1.18*0.18);
								listaPendientesContratoPreContratoCuotaBean.get(j).setCondicion(lista.get(k).getCancelado().equals("0")?"Pendiente":"Generado");							   
								 }
								 }
							  }
						}
					}
				}
		} 
		
		else 
		{
			/*LISTA CON TODOS LOS PAGOS DEL CONTRATO EN DOLARES*/ //CINDY
			while (iniciocobro.compareTo(fincobro) <= 0) {

				cuota= new CuotaBean();
			    cuota.setNrocuota(i);
			    cuota.setAnio(iniciocobro.get(Calendar.YEAR));
			    cuota.setMes(Almanaque.obtenerNombreMes(iniciocobro.get(Calendar.MONTH)));
			    cuota.setSipagado("0");
			    cuota.setCondicion("Pendiente");
			    cuota.setSiacuenta(false);
			    cuota.setNropagos(1);

			    cuota.setRentapagada(0.0);
			    cuota.setMora(0.0);
			    cuota.setNuevamc(0.0);
			    cuota.setNuevarenta(0.0);

			    if (i<=12) {
					
					cuota.setMonto(tipo.equals("X")?FuncionesHelper.redondear(condicionBean.getCuota1()*tipocambio, 2):FuncionesHelper.redondear(condicionBean.getCuota1(), 2));
				}else if (i<=24) {
					cuota.setMonto(tipo.equals("X")?FuncionesHelper.redondear(condicionBean.getCuota2()*tipocambio, 2):FuncionesHelper.redondear(condicionBean.getCuota2(), 2));
				}else if (i<=36) {
					cuota.setMonto(tipo.equals("X")?FuncionesHelper.redondear(condicionBean.getCuota3()*tipocambio, 2):FuncionesHelper.redondear(condicionBean.getCuota3(), 2));
				}else if (i<=48) {
					cuota.setMonto(tipo.equals("X")?FuncionesHelper.redondear(condicionBean.getCuota4()*tipocambio, 2):FuncionesHelper.redondear(condicionBean.getCuota4(), 2));
				}else if (i<=60) {
					cuota.setMonto(tipo.equals("X")?FuncionesHelper.redondear(condicionBean.getCuota5()*tipocambio, 2):FuncionesHelper.redondear(condicionBean.getCuota5(), 2));					
				}else {
					cuota.setMonto(tipo.equals("X")?FuncionesHelper.redondear(condicionBean.getCuota6()*tipocambio, 2):FuncionesHelper.redondear(condicionBean.getCuota6(), 2));					
				}

				cuota.setRenta(cuota.getMonto());
				cuota.setIgv(FuncionesHelper.redondear(cuota.getRenta()/1.18*0.18, 2));
			    listaPendientesContratoPreContratoCuotaBean.add(cuota);
			    iniciocobro.add(Calendar.MONTH, 1);
			    i++;
			}

			Double val;
			for (int j = 0;j < listaPendientesContratoPreContratoCuotaBean.size(); j++) 
			{

				for (int k = 0; k <lista.size() ; k++) {
					if (listaPendientesContratoPreContratoCuotaBean.get(j).getMes().equals(lista.get(k).getMespagado())&& listaPendientesContratoPreContratoCuotaBean.get(j).getAnio()==lista.get(k).getAniocuotames()  ) 
					{
						if (lista.get(k).getCancelado().equals("1")) 
						{
							listaPendientesContratoPreContratoCuotaBean.get(j).setSipagado("1");
							listaPendientesContratoPreContratoCuotaBean.get(j).setMonto(0.0);
							listaPendientesContratoPreContratoCuotaBean.get(j).setRentapagada(FuncionesHelper.redondear(listaPendientesContratoPreContratoCuotaBean.get(j).getRenta(),2));
						}
						else 
						{
							
							val=obtenerTotalValorPagadoDolares(lista.get(k).getIdcuota());
							
							listaPendientesContratoPreContratoCuotaBean.get(j).setIdcuota(lista.get(k).getIdcuota());
							listaPendientesContratoPreContratoCuotaBean.get(j).setSiacuenta(true);
							listaPendientesContratoPreContratoCuotaBean.get(j).setMoraacumulada(lista.get(k).getMorasoles());
							
							try {
								System.out.println("tipocambio = "+tipocambio);
								listaPendientesContratoPreContratoCuotaBean.get(j).setMonto(tipo.equals("X")?FuncionesHelper.redondear((listaPendientesContratoPreContratoCuotaBean.get(j).getMonto()/tipocambio-val)*tipocambio, 2):FuncionesHelper.redondear((listaPendientesContratoPreContratoCuotaBean.get(j).getMonto()-val), 2));
								
							} catch (Exception e) {
								throw new NullPointerException("Error al obtener tipo de cambio en IDCUOTA="+lista.get(k).getIdcuota());
							}
						
							listaPendientesContratoPreContratoCuotaBean.get(j).setRentapagada(val);
							
							
							listaPendientesContratoPreContratoCuotaBean.get(j).setNropagos(lista.get(k).getNropagos());
							listaPendientesContratoPreContratoCuotaBean.get(j).setIgv(listaPendientesContratoPreContratoCuotaBean.get(j).getMonto()/1.18*0.18);
							listaPendientesContratoPreContratoCuotaBean.get(j).setCondicion(lista.get(k).getCancelado().equals("0")?"Pendiente":"Generado");
						}
					}
				}
			}
		}
		
		if (tipo.equals("pendientes")) {
			return seleccionarRentaPendientes(listaPendientesContratoPreContratoCuotaBean);
		} else {
			return listaPendientesContratoPreContratoCuotaBean;
		}

	}
	public List<CuotaBean> obtenerInformacionCobroContratoTipoxDeuda(int idcondicion,String tipo,Double tipocambio) {
		listaRentas.clear();
		List<CuotaBean> listaPendientesContratoPreContratoCuotaBean= new ArrayList<CuotaBean>();
		CondicionBean condicionBean= new CondicionBean();
		condicionBean=contratoDAO.obtenerCondicionBean(idcondicion);
		
		listaPendientesContratoPreContratoCuotaBean.clear();


		CuotaBean cuota;
		List<Cuota> lista= new ArrayList<Cuota>();
		List<Detallecuota> listadetalle= new ArrayList<Detallecuota>();
		lista=listarcuotasxcontrato(condicionBean.getIdcontrato()); //lista de pagadas
		listadetalle=listardetallecuotasxcontrato(condicionBean.getIdcontrato()); //lista de pagadas
		List<CuotaBean> listapagados= new ArrayList<CuotaBean>();
		listapagados=obtenerPeriodosPendientesContratoDeuda(idcondicion);
		Calendar iniciocobro = Calendar.getInstance();
		iniciocobro.setTime(condicionBean.getIniciocobro());

		Calendar fincobro= Calendar.getInstance();
		fincobro.set(Calendar.DATE, fincobro.getActualMaximum(Calendar.DAY_OF_MONTH)); /*Inicializando fecha*/

		if (tipo.equals("canceladas y pendientes") || tipo.equals("canceladas")  ) {
			fincobro.setTime(condicionBean.getFincobro());
		}else if (tipo.equals("ratio") || tipo.equals("pendientes")) {
			if (condicionBean.getFincobro().compareTo(fincobro.getTime())<=0){ /*Verifica si termino contrato antes de la fecha de hoy*/
				fincobro.setTime(condicionBean.getFincobro());
			}
		} 

		fincobro.set(Calendar.DATE, fincobro.getActualMaximum(Calendar.DAY_OF_MONTH));
		if (condicionBean.getSinuevomantenimiento() !=null && condicionBean.getSinuevomantenimiento()) {
		//listaRentas=recaudacionReporteService.obtenerRentasNuevoMantenimientoContrato(condicionBean.getIdcontrato());
		listaRentas=contratoDAO.obtenerRentasNuevoMantenimientoContrato(condicionBean.getIdcontrato());
		
	}
		int i=1,l=0;
		if (condicionBean.getMoneda().equals("S")) 
		{       
				/*LISTA CON TODOS LOS PAGOS DEL CONTRATO EN SOLES*/ /*CINDY*/
				while (iniciocobro.compareTo(fincobro) <= 0) 
				{

					cuota= new CuotaBean();
				    cuota.setNrocuota(i);
				    cuota.setAnio(iniciocobro.get(Calendar.YEAR));
				    cuota.setMes(Almanaque.obtenerNombreMes(iniciocobro.get(Calendar.MONTH)));
				    cuota.setSipagado("0");
				    cuota.setCondicion("Pendiente");
				    cuota.setSiacuenta(false);
				    cuota.setNropagos(1);
                    cuota.setIdcontrato(condicionBean.getIdcontrato());
				    cuota.setRentapagada(0.0);
				    cuota.setMora(0.0);
				    cuota.setNuevamc(0.0);
				    //cuota.setNuevarenta(0.0);
					if (condicionBean.getSinuevomantenimiento() !=null && condicionBean.getSinuevomantenimiento()) {
						cuota.setMonto(listaRentas.get(l).getMontopagar());
						
					}else{
					if (i<=12) 
					{
						cuota.setMonto(condicionBean.getCuota1());

					}
					else 
						if (i<=24) 
						{
							cuota.setMonto(condicionBean.getCuota2());
						}
						else 
							if (i<=36) 
							{
								cuota.setMonto(condicionBean.getCuota3());
							}
							else 
								if (i<=48) 
								{
									cuota.setMonto(condicionBean.getCuota4());
								}
								else 
									if (i<=60) 
									{
										cuota.setMonto(condicionBean.getCuota5());
									}
									else 
									{
										cuota.setMonto(condicionBean.getCuota6());
									}
					
					}
					cuota.setNuevarenta(cuota.getMonto()!=null? (FuncionesHelper.redondearNum(cuota.getMonto(), 2)):0.0);
					cuota.setRenta(cuota.getMonto());
					cuota.setIgv(cuota.getRenta()!=null? (FuncionesHelper.redondear(cuota.getRenta()/1.18*0.18, 2)):0.0);               
					listaPendientesContratoPreContratoCuotaBean.add(cuota);
				    iniciocobro.add(Calendar.MONTH, 1);
				    i++;l++;

				}
//				for (int j = 0;j < listaPendientesContratoPreContratoCuotaBean.size(); j++) {
//					for (int k = 0; k <listapagados.size() ; k++){
//						if(listaPendientesContratoPreContratoCuotaBean.get(j).getMes().equals(listapagados.get(k).getMes()) && listaPendientesContratoPreContratoCuotaBean.get(j).getAnio()==listapagados.get(k).getAnio() ){
//							
//								if(FuncionesHelper.redondear(listaPendientesContratoPreContratoCuotaBean.get(j).getMonto(),2)-FuncionesHelper.redondear(listapagados.get(k).getMonto(),2)==0){
//									listaPendientesContratoPreContratoCuotaBean.get(j).setSipagado("1");
//									listaPendientesContratoPreContratoCuotaBean.get(j).setMonto(0.0);
//									listaPendientesContratoPreContratoCuotaBean.get(j).setRentapagada(FuncionesHelper.redondear(listaPendientesContratoPreContratoCuotaBean.get(j).getRenta(),2));	
//								}else{
//									if(listapagados.get(k).getCancelado().equals("1")){
//										listaPendientesContratoPreContratoCuotaBean.get(j).setSipagado("1");
//										listaPendientesContratoPreContratoCuotaBean.get(j).setMonto(0.0);
//										listaPendientesContratoPreContratoCuotaBean.get(j).setRentapagada(FuncionesHelper.redondear(listaPendientesContratoPreContratoCuotaBean.get(j).getRenta(),2));	
//									}else if(listapagados.get(k).getCancelado().equals("0")||listapagados.get(k).getCancelado().equals("2")){
//										listaPendientesContratoPreContratoCuotaBean.get(j).setSipagado("0");
//										listaPendientesContratoPreContratoCuotaBean.get(j).setIdcuota(listapagados.get(k).getIdcuota());
//										listaPendientesContratoPreContratoCuotaBean.get(j).setSiacuenta(true);
//										listaPendientesContratoPreContratoCuotaBean.get(j).setNropagos(listapagados.get(k).getNropagos());
//										listaPendientesContratoPreContratoCuotaBean.get(j).setMoraacumulada(listapagados.get(k).getMora());
//										listaPendientesContratoPreContratoCuotaBean.get(j).setRentapagada(FuncionesHelper.redondear(listaPendientesContratoPreContratoCuotaBean.get(j).getMonto()-listapagados.get(k).getMonto(),2));
//										listaPendientesContratoPreContratoCuotaBean.get(j).setMonto(FuncionesHelper.redondear(listaPendientesContratoPreContratoCuotaBean.get(j).getMonto()-listapagados.get(k).getMonto(),2));
//										}
//									}
//							
//						}
//							
//					}
//				}
					
				/*SACA LA LISTA DE PENDIENTES soles-- COMPARA LISTA DE CONTRATOS CON LISTA DE CANCELADOS(TABLA CUOTA)*/
				for (int j = 0;j < listaPendientesContratoPreContratoCuotaBean.size(); j++) 
				{
					for (int k = 0; k <listapagados.size() ; k++) 
					{	
						if (listaPendientesContratoPreContratoCuotaBean.get(j).getMes().equals(listapagados.get(k).getMes())&& listaPendientesContratoPreContratoCuotaBean.get(j).getAnio()==listapagados.get(k).getAnio()) 
						{
							if (listapagados.get(k).getCancelado().equals("1")) 
							{	
								listaPendientesContratoPreContratoCuotaBean.get(j).setSipagado("1");
								listaPendientesContratoPreContratoCuotaBean.get(j).setMonto(0.0);
								listaPendientesContratoPreContratoCuotaBean.get(j).setRentapagada(listaPendientesContratoPreContratoCuotaBean.get(j).getRenta());						
							} 
							else 
							{	
							  	
							if (listapagados.get(k).getCancelado().equals("2")&& tipo.equals("ratio")) {
								listaPendientesContratoPreContratoCuotaBean.get(j).setIdcuota(listapagados.get(k).getIdcuota());
								listaPendientesContratoPreContratoCuotaBean.get(j).setSiacuenta(true);
								listaPendientesContratoPreContratoCuotaBean.get(j).setNropagos(listapagados.get(k).getNropagos());
								listaPendientesContratoPreContratoCuotaBean.get(j).setMoraacumulada(listapagados.get(k).getMora());
								Double montosoles=0.0;
								for( int p=0;p<listadetalle.size();p++){
									   
									if(listapagados.get(k).getIdcuota()==listadetalle.get(p).getCuota().getIdcuota()){
										
									  if(listadetalle.get(p).getCancelado().equals("0")){// monto en estado pendiente
										  
									  }else if(listadetalle.get(p).getCancelado().equals("1")){//monto en estado cancelado
										  //montosoles=montosoles+FuncionesHelper.redondear(listadetalle.get(p).getMontosoles(),2);
										  
									  }else{//montos en estado generado 
										  montosoles=montosoles+FuncionesHelper.redondear(listadetalle.get(p).getMontosoles(),2);
									  }
									  listaPendientesContratoPreContratoCuotaBean.get(j).setRentapagada(FuncionesHelper.redondear(montosoles,2));
									  listaPendientesContratoPreContratoCuotaBean.get(j).setMonto(FuncionesHelper.redondear(montosoles,2));
									}
								}
								
								//listaPendientesContratoPreContratoCuotaBean.get(j).setRentapagada(FuncionesHelper.redondear(lista.get(k).getMontosoles(),2)); 
								//listaPendientesContratoPreContratoCuotaBean.get(j).setMonto(FuncionesHelper.redondear(lista.get(k).getMontosoles(), 2));
								listaPendientesContratoPreContratoCuotaBean.get(j).setIgv(listapagados.get(k).getMonto()/1.18*0.18);
								listaPendientesContratoPreContratoCuotaBean.get(j).setCondicion(listapagados.get(k).getCancelado().equals("0")?"Pendiente":"Generado");
							  }else{
								 if((listapagados.get(k).getCancelado().equals("2")&& tipo.equals("pendientes"))||(listapagados.get(k).getCancelado().equals("2")&& tipo.equals("canceladas y pendientes"))){
										listaPendientesContratoPreContratoCuotaBean.get(j).setIdcuota(listapagados.get(k).getIdcuota());
										listaPendientesContratoPreContratoCuotaBean.get(j).setSiacuenta(true);
										listaPendientesContratoPreContratoCuotaBean.get(j).setNropagos(listapagados.get(k).getNropagos());
										listaPendientesContratoPreContratoCuotaBean.get(j).setMoraacumulada(listapagados.get(k).getMora());
//										Double montosoles=0.0;
//										for( int p=0;p<listadetalle.size();p++){
//											   
//											if(listapagados.get(k).getIdcuota()==listadetalle.get(p).getCuota().getIdcuota()){
//												
//											  if(listadetalle.get(p).getCancelado().equals("0")){
//												  montosoles=montosoles+FuncionesHelper.redondear(listadetalle.get(p).getMontosoles(),2);
//											  }else if(listadetalle.get(p).getCancelado().equals("1")){
//												  montosoles=montosoles+FuncionesHelper.redondear(listadetalle.get(p).getMontosoles(),2);
//												  
//											  }else{
//												  
//											  }
//											  listaPendientesContratoPreContratoCuotaBean.get(j).setRentapagada(FuncionesHelper.redondear(montosoles,2));
//											}
//										}
										listaPendientesContratoPreContratoCuotaBean.get(j).setRentapagada(FuncionesHelper.redondear(listapagados.get(k).getMonto(),2));
										//listaPendientesContratoPreContratoCuotaBean.get(j).setRentapagada(FuncionesHelper.redondear(0.0,2)); 
										listaPendientesContratoPreContratoCuotaBean.get(j).setMonto(FuncionesHelper.redondear(listaPendientesContratoPreContratoCuotaBean.get(j).getMonto()-listaPendientesContratoPreContratoCuotaBean.get(j).getRentapagada(), 2));
										listaPendientesContratoPreContratoCuotaBean.get(j).setIgv(listaPendientesContratoPreContratoCuotaBean.get(j).getMonto()/1.18*0.18);
										listaPendientesContratoPreContratoCuotaBean.get(j).setCondicion(listapagados.get(k).getCancelado().equals("0")?"Pendiente":"Generado");
								 }else{
								listaPendientesContratoPreContratoCuotaBean.get(j).setIdcuota(listapagados.get(k).getIdcuota());
								listaPendientesContratoPreContratoCuotaBean.get(j).setSiacuenta(true);
								listaPendientesContratoPreContratoCuotaBean.get(j).setNropagos(listapagados.get(k).getNropagos());
								listaPendientesContratoPreContratoCuotaBean.get(j).setMoraacumulada(listapagados.get(k).getMora());
//								Double montosoles=0.0;
//								for( int p=0;p<listadetalle.size();p++){
//									   
//									if(listapagados.get(k).getIdcuota()==listadetalle.get(p).getCuota().getIdcuota()){
//										
//									  if(listadetalle.get(p).getCancelado().equals("0")){
//										  montosoles=montosoles+FuncionesHelper.redondear(listadetalle.get(p).getMontosoles(),2);
//									  }else if(listadetalle.get(p).getCancelado().equals("1")){
//										  montosoles=montosoles+FuncionesHelper.redondear(listadetalle.get(p).getMontosoles(),2);
//										  
//									  }else{
//										  
//									  }
//									  listaPendientesContratoPreContratoCuotaBean.get(j).setRentapagada(FuncionesHelper.redondear(montosoles,2));
//									}
//								}
								listaPendientesContratoPreContratoCuotaBean.get(j).setRentapagada(FuncionesHelper.redondear(listapagados.get(k).getMonto(),2));
								//listaPendientesContratoPreContratoCuotaBean.get(j).setRentapagada(FuncionesHelper.redondear(lista.get(k).getMontosoles(),2)); 
								//listaPendientesContratoPreContratoCuotaBean.get(j).setMonto(FuncionesHelper.redondear(listaPendientesContratoPreContratoCuotaBean.get(j).getMonto()-lista.get(k).getMontosoles(), 2));
								listaPendientesContratoPreContratoCuotaBean.get(j).setMonto(FuncionesHelper.redondear(listaPendientesContratoPreContratoCuotaBean.get(j).getMonto()-listaPendientesContratoPreContratoCuotaBean.get(j).getRentapagada(), 2));

								listaPendientesContratoPreContratoCuotaBean.get(j).setIgv(listaPendientesContratoPreContratoCuotaBean.get(j).getMonto()/1.18*0.18);
								listaPendientesContratoPreContratoCuotaBean.get(j).setCondicion(listapagados.get(k).getCancelado().equals("0")?"Pendiente":"Generado");							   
								 }
								 }
							  }
						}
					}
				}
		} 
		
		else 
		{
			/*LISTA CON TODOS LOS PAGOS DEL CONTRATO EN DOLARES*/ //CINDY
			while (iniciocobro.compareTo(fincobro) <= 0) {

				cuota= new CuotaBean();
			    cuota.setNrocuota(i);
			    cuota.setAnio(iniciocobro.get(Calendar.YEAR));
			    cuota.setMes(Almanaque.obtenerNombreMes(iniciocobro.get(Calendar.MONTH)));
			    cuota.setSipagado("0");
			    cuota.setCondicion("Pendiente");
			    cuota.setSiacuenta(false);
			    cuota.setNropagos(1);
			    cuota.setIdcontrato(condicionBean.getIdcontrato());
			    cuota.setRentapagada(0.0);
			    cuota.setMora(0.0);
			    cuota.setNuevamc(0.0);
			    //cuota.setNuevarenta(0.0);

			    if (i<=12) {
					
					cuota.setMonto(tipo.equals("X")?FuncionesHelper.redondear(condicionBean.getCuota1()*tipocambio, 2):FuncionesHelper.redondear(condicionBean.getCuota1(), 2));
				}else if (i<=24) {
					cuota.setMonto(tipo.equals("X")?FuncionesHelper.redondear(condicionBean.getCuota2()*tipocambio, 2):FuncionesHelper.redondear(condicionBean.getCuota2(), 2));
				}else if (i<=36) {
					cuota.setMonto(tipo.equals("X")?FuncionesHelper.redondear(condicionBean.getCuota3()*tipocambio, 2):FuncionesHelper.redondear(condicionBean.getCuota3(), 2));
				}else if (i<=48) {
					cuota.setMonto(tipo.equals("X")?FuncionesHelper.redondear(condicionBean.getCuota4()*tipocambio, 2):FuncionesHelper.redondear(condicionBean.getCuota4(), 2));
				}else if (i<=60) {
					cuota.setMonto(tipo.equals("X")?FuncionesHelper.redondear(condicionBean.getCuota5()*tipocambio, 2):FuncionesHelper.redondear(condicionBean.getCuota5(), 2));					
				}else {
					cuota.setMonto(tipo.equals("X")?FuncionesHelper.redondear(condicionBean.getCuota6()*tipocambio, 2):FuncionesHelper.redondear(condicionBean.getCuota6(), 2));					
				}
			    //cuota.setNuevarenta(FuncionesHelper.redondearNum(cuota.getMonto(), 2));
			    cuota.setNuevarenta(cuota.getMonto()!=null? (FuncionesHelper.redondearNum(cuota.getMonto(), 2)):0.0);
				cuota.setRenta(cuota.getMonto());
				cuota.setIgv(cuota.getRenta()!=null?(FuncionesHelper.redondear(cuota.getRenta()/1.18*0.18, 2)):0.0);
			    listaPendientesContratoPreContratoCuotaBean.add(cuota);
			    iniciocobro.add(Calendar.MONTH, 1);
			    i++;
			}

			Double val;
			for (int j = 0;j < listaPendientesContratoPreContratoCuotaBean.size(); j++) 
			{

				for (int k = 0; k <listapagados.size() ; k++) {
					if (listaPendientesContratoPreContratoCuotaBean.get(j).getMes().equals(listapagados.get(k).getMes())&& listaPendientesContratoPreContratoCuotaBean.get(j).getAnio()==listapagados.get(k).getAnio()  ) 
					{
						if (listapagados.get(k).getCancelado().equals("1")) 
						{
							listaPendientesContratoPreContratoCuotaBean.get(j).setSipagado("1");
							listaPendientesContratoPreContratoCuotaBean.get(j).setMonto(0.0);
							listaPendientesContratoPreContratoCuotaBean.get(j).setRentapagada(FuncionesHelper.redondear(listaPendientesContratoPreContratoCuotaBean.get(j).getRenta(),2));
						}
						else 
						{
							
							val=obtenerTotalValorPagadoDolares(listapagados.get(k).getIdcuota());
							
							listaPendientesContratoPreContratoCuotaBean.get(j).setIdcuota(listapagados.get(k).getIdcuota());
							listaPendientesContratoPreContratoCuotaBean.get(j).setSiacuenta(true);
							listaPendientesContratoPreContratoCuotaBean.get(j).setMoraacumulada(listapagados.get(k).getMora());
							
							try {
								listaPendientesContratoPreContratoCuotaBean.get(j).setMonto(tipo.equals("X")?FuncionesHelper.redondear((listaPendientesContratoPreContratoCuotaBean.get(j).getMonto()/tipocambio-val)*tipocambio, 2):FuncionesHelper.redondear((listaPendientesContratoPreContratoCuotaBean.get(j).getMonto()-val), 2));
								
							} catch (Exception e) {
								throw new NullPointerException("Error al obtener tipo de cambio en IDCUOTA="+listapagados.get(k).getIdcuota());
							}
						
							listaPendientesContratoPreContratoCuotaBean.get(j).setRentapagada(val);
							
							
							listaPendientesContratoPreContratoCuotaBean.get(j).setNropagos(listapagados.get(k).getNropagos());
							listaPendientesContratoPreContratoCuotaBean.get(j).setIgv(listaPendientesContratoPreContratoCuotaBean.get(j).getMonto()/1.18*0.18);
							listaPendientesContratoPreContratoCuotaBean.get(j).setCondicion(listapagados.get(k).getCancelado().equals("0")?"Pendiente":"Generado");
						}
					}
				}
			}
		}
		
		if (tipo.equals("pendientes")) {
			return seleccionarRentaPendientes(listaPendientesContratoPreContratoCuotaBean);
		} else {
			return listaPendientesContratoPreContratoCuotaBean;
		}

	}
	
	public List<CuotaBean> obtenerInformacionCobroSinContratoTipo(int idcondicion,String tipo,Double tipocambio) {
		List<CuotaBean> listaPendientesSinContratoCuotaBean= new ArrayList<CuotaBean>();
		CondicionBean condicionBean= new CondicionBean();
		
		condicionBean=contratoDAO.obtenerCondicionBean(idcondicion);
		CuotaBean cuota;
		
		int i=0;
		List<Cuota> lista= new ArrayList<Cuota>();
		List<Detallecuota> listadetalle= new ArrayList<Detallecuota>();
		lista=listarcuotasxcontrato(condicionBean.getIdcontrato());//lista de pagadas
		listadetalle=listardetallecuotasxcontrato(condicionBean.getIdcontrato()); //lista de pagadas 
		

		Calendar iniciocobro = Calendar.getInstance();
		iniciocobro.setTime(condicionBean.getIniciocobro());

		Calendar fincobro = Calendar.getInstance();
		fincobro.setTime(condicionBean.getFincobro()!=null ? condicionBean.getFincobro() :fincobro.getTime());
		fincobro.set(Calendar.DATE, fincobro.getActualMaximum(Calendar.DAY_OF_MONTH));

		/*RECORRE LA LISTA DE CONTRATOS QUE HAN PAGADO EN SOLES*/ /*cindy*/
		if (condicionBean.getMoneda().equals("S")) {
			/*LISTA CON TODOS LOS PAGOS DESDE EL 1999*/
				while (iniciocobro.getTime().compareTo(fincobro.getTime()) < 0) 
				{
					cuota= new CuotaBean();
					cuota.setNrocuota(i);
					cuota.setAnio(iniciocobro.get(Calendar.YEAR));
			    	cuota.setMes(Almanaque.obtenerNombreMes(iniciocobro.get(Calendar.MONTH)));
			    	cuota.setSipagado("0");
			    	cuota.setCondicion("Pendiente");
			    	cuota.setNropagos(1);
			    	cuota.setMonto(FuncionesHelper.redondearNum(condicionBean.getCuota1(), 2)); 
			    	
			    	
			    	
			    	cuota.setRenta(cuota.getMonto());
			    	
			    	
			    	cuota.setIgv(FuncionesHelper.redondearNum(cuota.getRenta()/1.18*0.18, 2));

			    	cuota.setRentapagada(0.0);
			    	cuota.setMora(0.0);
			    	cuota.setNuevamc(0.0);
			    	cuota.setNuevarenta(0.0);

			    	listaPendientesSinContratoCuotaBean.add(cuota);
			    	
			    	iniciocobro.add(Calendar.MONTH, 1);
			    	i++;
				}
			
			
				/*Cindy, SACA LA LISTA DE PENDIENTES EN SOLES */
				for (int j = 0;j < listaPendientesSinContratoCuotaBean.size(); j++) {

					for (int k = 0; k <lista.size() ; k++) 
					{   
						if (listaPendientesSinContratoCuotaBean.get(j).getMes().equals(lista.get(k).getMespagado())&& listaPendientesSinContratoCuotaBean.get(j).getAnio()==lista.get(k).getAniocuotames()  ) {
							if (lista.get(k).getCancelado().equals("1")) 
							{
								listaPendientesSinContratoCuotaBean.get(j).setMonto(0.0);
								listaPendientesSinContratoCuotaBean.get(j).setSipagado("1");
								listaPendientesSinContratoCuotaBean.get(j).setRentapagada(listaPendientesSinContratoCuotaBean.get(j).getRenta());
								//idcuota borrar
								listaPendientesSinContratoCuotaBean.get(j).setIdcuota(lista.get(k).getIdcuota());

							} 
							else 
							{   
								
								if (lista.get(k).getCancelado().equals("2")&& tipo.equals("ratio")) {
									
									listaPendientesSinContratoCuotaBean.get(j).setIdcuota(lista.get(k).getIdcuota());
									listaPendientesSinContratoCuotaBean.get(j).setSiacuenta(true);
									listaPendientesSinContratoCuotaBean.get(j).setNropagos(lista.get(k).getNropagos());
									listaPendientesSinContratoCuotaBean.get(j).setMoraacumulada(lista.get(k).getMorasoles());
									Double montosoles=0.0;
									for( int p=0;p<listadetalle.size();p++){
										   
										if(lista.get(k).getIdcuota()==listadetalle.get(p).getCuota().getIdcuota()){
											
										  if(listadetalle.get(p).getCancelado().equals("0")){// monto en estado pendiente
											  
										  }else if(listadetalle.get(p).getCancelado().equals("1")){//monto en estado cancelado
											  montosoles=montosoles+FuncionesHelper.redondearNum(listadetalle.get(p).getMontosoles(),2);
											  
										  }else{//montos en estado generado 
											  //montosoles=montosoles+FuncionesHelper.redondear(listadetalle.get(p).getMontosoles(),2);
										  }
										  listaPendientesSinContratoCuotaBean.get(j).setRentapagada(FuncionesHelper.redondearNum(montosoles,2));
										  //listaPendientesSinContratoCuotaBean.get(j).setMonto(FuncionesHelper.redondear(montosoles,2));
										  //nuevo monto para ratio 
										  listaPendientesSinContratoCuotaBean.get(j).setMonto(FuncionesHelper.redondearNum((condicionBean.getCuota1()-FuncionesHelper.redondear(montosoles,2)),2));
										}
									}
									//listaPendientesSinContratoCuotaBean.get(j).setRentapagada(FuncionesHelper.redondear(lista.get(k).getMontosoles(),2)); 
									//listaPendientesSinContratoCuotaBean.get(j).setMonto(FuncionesHelper.redondear(lista.get(k).getMontosoles(), 2));
									listaPendientesSinContratoCuotaBean.get(j).setIgv(lista.get(k).getMontosoles()/1.18*0.18);
									listaPendientesSinContratoCuotaBean.get(j).setCondicion(lista.get(k).getCancelado().equals("0")?"Pendiente":"Generado");
									//idcuota borrar
									listaPendientesSinContratoCuotaBean.get(j).setIdcuota(lista.get(k).getIdcuota());
								}else{
									if((lista.get(k).getCancelado().equals("2")&& tipo.equals("pendientes"))||(lista.get(k).getCancelado().equals("2")&& tipo.equals("canceladas y pendientes"))){
										listaPendientesSinContratoCuotaBean.get(j).setIdcuota(lista.get(k).getIdcuota());
										listaPendientesSinContratoCuotaBean.get(j).setSiacuenta(true);
										listaPendientesSinContratoCuotaBean.get(j).setNropagos(lista.get(k).getNropagos());
										listaPendientesSinContratoCuotaBean.get(j).setMoraacumulada(lista.get(k).getMorasoles());
										Double montosoles=0.0;
										for( int p=0;p<listadetalle.size();p++){
											   
											if(lista.get(k).getIdcuota()==listadetalle.get(p).getCuota().getIdcuota()){
												
											  if(listadetalle.get(p).getCancelado().equals("0")){
												  
											  }else if(listadetalle.get(p).getCancelado().equals("1")){
												  montosoles=montosoles+FuncionesHelper.redondearNum(listadetalle.get(p).getMontosoles(),2);
												  
											  }else{
												  
											  }
											  listaPendientesSinContratoCuotaBean.get(j).setRentapagada(FuncionesHelper.redondear(montosoles,2));
											}
										}
										//listaPendientesSinContratoCuotaBean.get(j).setRentapagada(FuncionesHelper.redondear(0.0,2)); 
										listaPendientesSinContratoCuotaBean.get(j).setMonto(FuncionesHelper.redondearNum(condicionBean.getCuota1()-listaPendientesSinContratoCuotaBean.get(j).getRentapagada(), 2));

										//listaPendientesSinContratoCuotaBean.get(j).setMonto(FuncionesHelper.redondear(lista.get(k).getMontosoles(), 2));
										listaPendientesSinContratoCuotaBean.get(j).setIgv(lista.get(k).getMontosoles()/1.18*0.18);
										listaPendientesSinContratoCuotaBean.get(j).setCondicion(lista.get(k).getCancelado().equals("0")?"Pendiente":"Generado");
										//idcuota borrar
										listaPendientesSinContratoCuotaBean.get(j).setIdcuota(lista.get(k).getIdcuota());
									}else{
									
									listaPendientesSinContratoCuotaBean.get(j).setIdcuota(lista.get(k).getIdcuota());
									Double montosoles=0.0;
									for( int p=0;p<listadetalle.size();p++){
										 
										if(lista.get(k).getIdcuota()==listadetalle.get(p).getCuota().getIdcuota()){
											
										  if(listadetalle.get(p).getCancelado().equals("0")){
											  montosoles=montosoles+FuncionesHelper.redondear(listadetalle.get(p).getMontosoles(),2);
										  }else if(listadetalle.get(p).getCancelado().equals("1")){
											  montosoles=montosoles+FuncionesHelper.redondear(listadetalle.get(p).getMontosoles(),2);
											  
										  }else{
											  
										  }
										  listaPendientesSinContratoCuotaBean.get(j).setRentapagada(FuncionesHelper.redondearNum(montosoles,2));
										}
									}
									listaPendientesSinContratoCuotaBean.get(j).setMonto(FuncionesHelper.redondearNum(condicionBean.getCuota1()-listaPendientesSinContratoCuotaBean.get(j).getRentapagada(), 2));
									//listaPendientesSinContratoCuotaBean.get(j).setMonto(FuncionesHelper.redondear(condicionBean.getCuota1()-lista.get(k).getMontosoles(), 2));  //Franco
									listaPendientesSinContratoCuotaBean.get(j).setSiacuenta(true);
									//listaPendientesSinContratoCuotaBean.get(j).setRentapagada(FuncionesHelper.redondear(lista.get(k).getMontosoles(),2));
									listaPendientesSinContratoCuotaBean.get(j).setNropagos(lista.get(k).getNropagos());
									listaPendientesSinContratoCuotaBean.get(j).setMoraacumulada(lista.get(k).getMorasoles());
									listaPendientesSinContratoCuotaBean.get(j).setIgv(listaPendientesSinContratoCuotaBean.get(j).getMonto()/1.18*0.18);
									listaPendientesSinContratoCuotaBean.get(j).setCondicion(lista.get(k).getCancelado().equals("0")?"Pendiente":"Generado");
									//idcuota borrar
									listaPendientesSinContratoCuotaBean.get(j).setIdcuota(lista.get(k).getIdcuota());
									 }
									}
							}
						}
						//System.out.println("listaa=="+listaPendientesSinContratoCuotaBean.get(k).toString());
					}
				}

			} 
		else 
		{
				/* LISTA DE PENDIENTES SIN CONTRATO EN DOLARES */ //CINDY
				/*LISTA CON TODOS LOS PAGOS DESDE EL 1999*/
				while (iniciocobro.getTime().compareTo(fincobro.getTime()) < 0) 
				{
					cuota= new CuotaBean();
					cuota.setNrocuota(i);
					cuota.setAnio(iniciocobro.get(Calendar.YEAR));
					cuota.setMes(Almanaque.obtenerNombreMes(iniciocobro.get(Calendar.MONTH)));
					cuota.setSipagado("0");
					cuota.setCondicion("Pendiente");
					cuota.setNropagos(1);
					
					cuota.setMonto(tipo.equals("X")?FuncionesHelper.redondearNum(condicionBean.getCuota1()*tipocambio, 2):FuncionesHelper.redondear(condicionBean.getCuota1(), 2));//CINDY   //getMontocuotasoles() bota en dólares
					
					cuota.setRenta(cuota.getMonto()); //cindy //Bota el monto en dólares
			    	cuota.setIgv(FuncionesHelper.redondear(cuota.getRenta()/1.18*0.18, 2));

			    	cuota.setRentapagada(0.0);
			    	cuota.setMora(0.0);
			    	cuota.setNuevamc(0.0);
			    	cuota.setNuevarenta(0.0);
			    	listaPendientesSinContratoCuotaBean.add(cuota);
			    	iniciocobro.add(Calendar.MONTH, 1);
			    	i++;
				}

				Double val;
				for (int j = 0;j < listaPendientesSinContratoCuotaBean.size(); j++) 
				{
					for (int k = 0; k <lista.size() ; k++) 
					{
						if (listaPendientesSinContratoCuotaBean.get(j).getMes().equals(lista.get(k).getMespagado())&& listaPendientesSinContratoCuotaBean.get(j).getAnio()==lista.get(k).getAniocuotames() ) 
						{

							if (lista.get(k).getCancelado().equals("1")) //cancelado
							{
								listaPendientesSinContratoCuotaBean.get(j).setMonto(0.0);
								listaPendientesSinContratoCuotaBean.get(j).setSipagado("1");
								listaPendientesSinContratoCuotaBean.get(j).setRentapagada(listaPendientesSinContratoCuotaBean.get(j).getRenta());
								//idcuota borrar
								listaPendientesSinContratoCuotaBean.get(j).setIdcuota(lista.get(k).getIdcuota());
							} 
							else 
							{//si tiene pendiente
								
								try {//si encuentra error lo atrapa y continua si n caerese
									
									if (lista.get(k).getCancelado().equals("2")&& tipo.equals("ratio")) {
										val=lista.get(k).getTipocambio()==null? 0.0: FuncionesHelper.redondearNum(lista.get(k).getMontosoles()/lista.get(k).getTipocambio(),2);//estamos mandando un null a formatear
										listaPendientesSinContratoCuotaBean.get(j).setIdcuota(lista.get(k).getIdcuota());
										listaPendientesSinContratoCuotaBean.get(j).setSiacuenta(true);
										listaPendientesSinContratoCuotaBean.get(j).setMoraacumulada(lista.get(k).getMorasoles());
										
										
										listaPendientesSinContratoCuotaBean.get(j).setMonto(tipo.equals("X")?FuncionesHelper.redondearNum((listaPendientesSinContratoCuotaBean.get(j).getMonto()/tipocambio-val)*tipocambio, 2):FuncionesHelper.redondear((val), 2));
										listaPendientesSinContratoCuotaBean.get(j).setRentapagada(val);
										
										listaPendientesSinContratoCuotaBean.get(j).setNropagos(lista.get(k).getNropagos());
										listaPendientesSinContratoCuotaBean.get(j).setIgv(listaPendientesSinContratoCuotaBean.get(j).getMonto()/1.18*0.18);
										listaPendientesSinContratoCuotaBean.get(j).setCondicion(lista.get(k).getCancelado().equals("0")?"Pendiente":"Generado");
										//idcuota borrar
										listaPendientesSinContratoCuotaBean.get(j).setIdcuota(lista.get(k).getIdcuota());
									}else{
										if(lista.get(k).getCancelado().equals("2")&& tipo.equals("canceladas y pendientes")){
											val=lista.get(k).getTipocambio()==null? 0.0: FuncionesHelper.redondearNum(lista.get(k).getMontosoles()/lista.get(k).getTipocambio(),2);//estamos mandando un null a formatear
											listaPendientesSinContratoCuotaBean.get(j).setIdcuota(lista.get(k).getIdcuota());
											listaPendientesSinContratoCuotaBean.get(j).setSiacuenta(true);
											listaPendientesSinContratoCuotaBean.get(j).setMoraacumulada(lista.get(k).getMorasoles());
											
											
											listaPendientesSinContratoCuotaBean.get(j).setMonto(tipo.equals("X")?FuncionesHelper.redondearNum((listaPendientesSinContratoCuotaBean.get(j).getMonto()/tipocambio-val)*tipocambio, 2):FuncionesHelper.redondear((val), 2));
											listaPendientesSinContratoCuotaBean.get(j).setRentapagada(FuncionesHelper.redondearNum(0.0,2));
											
											listaPendientesSinContratoCuotaBean.get(j).setNropagos(lista.get(k).getNropagos());
											listaPendientesSinContratoCuotaBean.get(j).setIgv(listaPendientesSinContratoCuotaBean.get(j).getMonto()/1.18*0.18);
											listaPendientesSinContratoCuotaBean.get(j).setCondicion(lista.get(k).getCancelado().equals("0")?"Pendiente":"Generado");
											//idcuota borrar
											listaPendientesSinContratoCuotaBean.get(j).setIdcuota(lista.get(k).getIdcuota());
										
										}else{
									//val=lista.get(k).getTipocambio()==null? 0.0: FuncionesHelper.redondear(lista.get(k).getMontosoles()/lista.get(k).getTipocambio(),2);//estamos mandando un null a formatear
									val=obtenerTotalValorPagadoDolares(lista.get(k).getIdcuota());
									listaPendientesSinContratoCuotaBean.get(j).setIdcuota(lista.get(k).getIdcuota());
									listaPendientesSinContratoCuotaBean.get(j).setSiacuenta(true);
									listaPendientesSinContratoCuotaBean.get(j).setMoraacumulada(lista.get(k).getMorasoles());
									
									
									listaPendientesSinContratoCuotaBean.get(j).setMonto(tipo.equals("X")?FuncionesHelper.redondearNum((listaPendientesSinContratoCuotaBean.get(j).getMonto()/tipocambio-val)*tipocambio, 2):FuncionesHelper.redondear((listaPendientesSinContratoCuotaBean.get(j).getMonto()-val), 2));
									listaPendientesSinContratoCuotaBean.get(j).setRentapagada(val);
									
									listaPendientesSinContratoCuotaBean.get(j).setNropagos(lista.get(k).getNropagos());
									listaPendientesSinContratoCuotaBean.get(j).setIgv(listaPendientesSinContratoCuotaBean.get(j).getMonto()/1.18*0.18);
									listaPendientesSinContratoCuotaBean.get(j).setCondicion(lista.get(k).getCancelado().equals("0")?"Pendiente":"Generado");
									//idcuota borrar
									listaPendientesSinContratoCuotaBean.get(j).setIdcuota(lista.get(k).getIdcuota());
										
										}}
								} catch (Exception e) {
									
									System.out.println("Error al obtener tipo de cambio en IDCUOTA="+lista.get(k).getIdcuota());
								}

							}
						}
					}
				}
		}
		if (tipo.equals("pendientes")) {
			return seleccionarRentaPendientes(listaPendientesSinContratoCuotaBean);
		}else {
			return listaPendientesSinContratoCuotaBean;
		}
		
		
	}
	public List<CuotaBean> obtenerInformacionTipoSinContrato(int idcondicion,String tipo,Double tipocambio){
		List<CuotaBean> listaPendientesSinContratoCuotaBean= new ArrayList<CuotaBean>();
		CondicionBean condicionBean= new CondicionBean();
		
		condicionBean=contratoDAO.obtenerCondicionBean(idcondicion);
		if(condicionBean.getMoneda().equalsIgnoreCase(Constantes.MONEDA_SOLES)){
			
		}else{
			
		}
		
		return null;
		
	}
	public List<CuotaBean> obtenerInformacionCobroTipoSinContrato(int idcondicion,String tipo,Double tipocambio) {
		List<CuotaBean> listaPendientesSinContratoCuotaBean= new ArrayList<CuotaBean>();
		CondicionBean condicionBean= new CondicionBean();
		
		condicionBean=contratoDAO.obtenerCondicionBean(idcondicion);
		CuotaBean cuota;
		
		int i=0;
		List<Cuota> lista= new ArrayList<Cuota>();
		List<Detallecuota> listadetalle= new ArrayList<Detallecuota>();
		//lista=listarcuotasxcontrato(condicionBean.getIdcontrato());//lista de pagadas
		lista=listarcuotascontrato(condicionBean.getIdcontrato());//lista de pagadas x contrato sgi
		//listadetalle=listardetallecuotasxcontrato(condicionBean.getIdcontrato()); //lista de pagadas 
		listadetalle=listardetallecuotascontrato(condicionBean.getIdcontrato());//lista detalle pagadas x comprobante

		Calendar iniciocobro = Calendar.getInstance();
		iniciocobro.setTime(condicionBean.getIniciocobro());

		Calendar fincobro = Calendar.getInstance();
		fincobro.setTime(condicionBean.getFincobro()!=null ? condicionBean.getFincobro() :fincobro.getTime());
		fincobro.set(Calendar.DATE, fincobro.getActualMaximum(Calendar.DAY_OF_MONTH));

		/*RECORRE LA LISTA DE CONTRATOS QUE HAN PAGADO EN SOLES*/ /*cindy*/
		if (condicionBean.getMoneda().equals("S")) {
			/*LISTA CON TODOS LOS PAGOS DESDE EL 1999*/
				while (iniciocobro.getTime().compareTo(fincobro.getTime()) < 0) 
				{
					cuota= new CuotaBean();
					cuota.setNrocuota(i);
					cuota.setAnio(iniciocobro.get(Calendar.YEAR));
			    	cuota.setMes(Almanaque.obtenerNombreMes(iniciocobro.get(Calendar.MONTH)));
			    	cuota.setSipagado("0");
			    	cuota.setCondicion("Pendiente");
			    	cuota.setNropagos(1);
			    	cuota.setMonto(FuncionesHelper.redondearNum(condicionBean.getCuota1(), 2)); 
			    	
			    	
			    	
			    	cuota.setRenta(cuota.getMonto());
			    	
			    	
			    	cuota.setIgv(FuncionesHelper.redondearNum(cuota.getRenta()/1.18*0.18, 2));

			    	cuota.setRentapagada(0.0);
			    	cuota.setMora(0.0);
			    	cuota.setNuevamc(0.0);
			    	cuota.setNuevarenta(0.0);

			    	listaPendientesSinContratoCuotaBean.add(cuota);
			    	
			    	iniciocobro.add(Calendar.MONTH, 1);
			    	i++;
				}
			
			
				/*Cindy, SACA LA LISTA DE PENDIENTES EN SOLES */
				for (int j = 0;j < listaPendientesSinContratoCuotaBean.size(); j++) {

					for (int k = 0; k <lista.size() ; k++) 
					{   
						if (listaPendientesSinContratoCuotaBean.get(j).getMes().equals(lista.get(k).getMespagado())&& listaPendientesSinContratoCuotaBean.get(j).getAnio()==lista.get(k).getAniocuotames()  ) {
							if (lista.get(k).getCancelado().equals("1")) 
							{
								listaPendientesSinContratoCuotaBean.get(j).setMonto(0.0);
								listaPendientesSinContratoCuotaBean.get(j).setSipagado("1");
								listaPendientesSinContratoCuotaBean.get(j).setRentapagada(listaPendientesSinContratoCuotaBean.get(j).getRenta());
								//idcuota borrar
								listaPendientesSinContratoCuotaBean.get(j).setIdcuota(lista.get(k).getIdcuota());

							} 
							else 
							{   
								
								if (lista.get(k).getCancelado().equals("2")&& tipo.equals("ratio")) {
									
									listaPendientesSinContratoCuotaBean.get(j).setIdcuota(lista.get(k).getIdcuota());
									listaPendientesSinContratoCuotaBean.get(j).setSiacuenta(true);
									listaPendientesSinContratoCuotaBean.get(j).setNropagos(lista.get(k).getNropagos());
									listaPendientesSinContratoCuotaBean.get(j).setMoraacumulada(lista.get(k).getMorasoles());
									Double montosoles=0.0;
									for( int p=0;p<listadetalle.size();p++){
										   
										if(lista.get(k).getIdcuota()==listadetalle.get(p).getCuota().getIdcuota()){
											
										  if(listadetalle.get(p).getCancelado().equals("0")){// monto en estado pendiente
											  
										  }else if(listadetalle.get(p).getCancelado().equals("1")){//monto en estado cancelado
											  montosoles=montosoles+FuncionesHelper.redondearNum(listadetalle.get(p).getMontosoles(),2);
											  
										  }else{//montos en estado generado 
											  //montosoles=montosoles+FuncionesHelper.redondear(listadetalle.get(p).getMontosoles(),2);
										  }
										  listaPendientesSinContratoCuotaBean.get(j).setRentapagada(FuncionesHelper.redondearNum(montosoles,2));
										  //listaPendientesSinContratoCuotaBean.get(j).setMonto(FuncionesHelper.redondear(montosoles,2));
										  //nuevo monto para ratio 
										  listaPendientesSinContratoCuotaBean.get(j).setMonto(FuncionesHelper.redondearNum((condicionBean.getCuota1()-FuncionesHelper.redondearNum(montosoles,2)),2));
										}
									}
									//listaPendientesSinContratoCuotaBean.get(j).setRentapagada(FuncionesHelper.redondear(lista.get(k).getMontosoles(),2)); 
									//listaPendientesSinContratoCuotaBean.get(j).setMonto(FuncionesHelper.redondear(lista.get(k).getMontosoles(), 2));
									listaPendientesSinContratoCuotaBean.get(j).setIgv(lista.get(k).getMontosoles()/1.18*0.18);
									listaPendientesSinContratoCuotaBean.get(j).setCondicion(lista.get(k).getCancelado().equals("0")?"Pendiente":"Generado");
									//idcuota borrar
									listaPendientesSinContratoCuotaBean.get(j).setIdcuota(lista.get(k).getIdcuota());
								}else{
									if((lista.get(k).getCancelado().equals("2")&& tipo.equals("pendientes"))||(lista.get(k).getCancelado().equals("2")&& tipo.equals("canceladas y pendientes"))){
										listaPendientesSinContratoCuotaBean.get(j).setIdcuota(lista.get(k).getIdcuota());
										listaPendientesSinContratoCuotaBean.get(j).setSiacuenta(true);
										listaPendientesSinContratoCuotaBean.get(j).setNropagos(lista.get(k).getNropagos());
										listaPendientesSinContratoCuotaBean.get(j).setMoraacumulada(lista.get(k).getMorasoles());
										Double montosoles=0.0;
										for( int p=0;p<listadetalle.size();p++){
											   
											if(lista.get(k).getIdcuota()==listadetalle.get(p).getCuota().getIdcuota()){
												
											  if(listadetalle.get(p).getCancelado().equals("0")){
												  
											  }else if(listadetalle.get(p).getCancelado().equals("1")){
												  montosoles=montosoles+FuncionesHelper.redondear(listadetalle.get(p).getMontosoles(),2);
												  
											  }else{
												  
											  }
											  listaPendientesSinContratoCuotaBean.get(j).setRentapagada(FuncionesHelper.redondearNum(montosoles,2));
											}
										}
										//listaPendientesSinContratoCuotaBean.get(j).setRentapagada(FuncionesHelper.redondear(0.0,2)); 
										listaPendientesSinContratoCuotaBean.get(j).setMonto(FuncionesHelper.redondearNum(condicionBean.getCuota1()-listaPendientesSinContratoCuotaBean.get(j).getRentapagada(), 2));

										//listaPendientesSinContratoCuotaBean.get(j).setMonto(FuncionesHelper.redondear(lista.get(k).getMontosoles(), 2));
										listaPendientesSinContratoCuotaBean.get(j).setIgv(lista.get(k).getMontosoles()/1.18*0.18);
										listaPendientesSinContratoCuotaBean.get(j).setCondicion(lista.get(k).getCancelado().equals("0")?"Pendiente":"Generado");
										//idcuota borrar
										listaPendientesSinContratoCuotaBean.get(j).setIdcuota(lista.get(k).getIdcuota());
									}else{
									
									listaPendientesSinContratoCuotaBean.get(j).setIdcuota(lista.get(k).getIdcuota());
									Double montosoles=0.0;
									for( int p=0;p<listadetalle.size();p++){
										  
										if(lista.get(k).getIdcuota()==listadetalle.get(p).getCuota().getIdcuota()){
											
										  if(listadetalle.get(p).getCancelado().equals("0")){
											  //montosoles=montosoles+FuncionesHelper.redondear(listadetalle.get(p).getMontosoles(),2);
										  }else if(listadetalle.get(p).getCancelado().equals("1")){
											  montosoles=montosoles+FuncionesHelper.redondearNum(listadetalle.get(p).getMontosoles(),2);
											  
										  }else{
											  
										  }
										  listaPendientesSinContratoCuotaBean.get(j).setRentapagada(FuncionesHelper.redondearNum(montosoles,2));
										}
									}
									listaPendientesSinContratoCuotaBean.get(j).setMonto(FuncionesHelper.redondearNum(condicionBean.getCuota1()-listaPendientesSinContratoCuotaBean.get(j).getRentapagada(), 2));
									//listaPendientesSinContratoCuotaBean.get(j).setMonto(FuncionesHelper.redondear(condicionBean.getCuota1()-lista.get(k).getMontosoles(), 2));  //Franco
									listaPendientesSinContratoCuotaBean.get(j).setSiacuenta(true);
									//listaPendientesSinContratoCuotaBean.get(j).setRentapagada(FuncionesHelper.redondear(lista.get(k).getMontosoles(),2));
									listaPendientesSinContratoCuotaBean.get(j).setNropagos(lista.get(k).getNropagos());
									listaPendientesSinContratoCuotaBean.get(j).setMoraacumulada(lista.get(k).getMorasoles());
									listaPendientesSinContratoCuotaBean.get(j).setIgv(listaPendientesSinContratoCuotaBean.get(j).getMonto()/1.18*0.18);
									listaPendientesSinContratoCuotaBean.get(j).setCondicion(lista.get(k).getCancelado().equals("0")?"Pendiente":"Generado");
									//idcuota borrar
									listaPendientesSinContratoCuotaBean.get(j).setIdcuota(lista.get(k).getIdcuota());
									 }
									}
							}
						}
						//System.out.println("listaa=="+listaPendientesSinContratoCuotaBean.get(k).toString());
					}
				}

			} 
		else 
		{
				/* LISTA DE PENDIENTES SIN CONTRATO EN DOLARES */ //CINDY
				/*LISTA CON TODOS LOS PAGOS DESDE EL 1999*/
				while (iniciocobro.getTime().compareTo(fincobro.getTime()) < 0) 
				{
					cuota= new CuotaBean();
					cuota.setNrocuota(i);
					cuota.setAnio(iniciocobro.get(Calendar.YEAR));
					cuota.setMes(Almanaque.obtenerNombreMes(iniciocobro.get(Calendar.MONTH)));
					cuota.setSipagado("0");
					cuota.setCondicion("Pendiente");
					cuota.setNropagos(1);
					
					cuota.setMonto(tipo.equals("X")?FuncionesHelper.redondearNum(condicionBean.getCuota1()*tipocambio, 2):FuncionesHelper.redondear(condicionBean.getCuota1(), 2));//CINDY   //getMontocuotasoles() bota en dólares
					
					cuota.setRenta(cuota.getMonto()); //cindy //Bota el monto en dólares
			    	cuota.setIgv(FuncionesHelper.redondear(cuota.getRenta()/1.18*0.18, 2));

			    	cuota.setRentapagada(0.0);
			    	cuota.setMora(0.0);
			    	cuota.setNuevamc(0.0);
			    	cuota.setNuevarenta(0.0);
			    	listaPendientesSinContratoCuotaBean.add(cuota);
			    	iniciocobro.add(Calendar.MONTH, 1);
			    	i++;
				}

				Double val;
				for (int j = 0;j < listaPendientesSinContratoCuotaBean.size(); j++) 
				{
					for (int k = 0; k <lista.size() ; k++) 
					{
						if (listaPendientesSinContratoCuotaBean.get(j).getMes().equals(lista.get(k).getMespagado())&& listaPendientesSinContratoCuotaBean.get(j).getAnio()==lista.get(k).getAniocuotames() ) 
						{

							if (lista.get(k).getCancelado().equals("1")) //cancelado
							{
								listaPendientesSinContratoCuotaBean.get(j).setMonto(0.0);
								listaPendientesSinContratoCuotaBean.get(j).setSipagado("1");
								listaPendientesSinContratoCuotaBean.get(j).setRentapagada(listaPendientesSinContratoCuotaBean.get(j).getRenta());
								//idcuota borrar
								listaPendientesSinContratoCuotaBean.get(j).setIdcuota(lista.get(k).getIdcuota());
							} 
							else 
							{//si tiene pendiente
								
								try {//si encuentra error lo atrapa y continua si n caerese
									
									if (lista.get(k).getCancelado().equals("2")&& tipo.equals("ratio")) {
										val=lista.get(k).getTipocambio()==null? 0.0: FuncionesHelper.redondearNum(lista.get(k).getMontosoles()/lista.get(k).getTipocambio(),2);//estamos mandando un null a formatear
										listaPendientesSinContratoCuotaBean.get(j).setIdcuota(lista.get(k).getIdcuota());
										listaPendientesSinContratoCuotaBean.get(j).setSiacuenta(true);
										listaPendientesSinContratoCuotaBean.get(j).setMoraacumulada(lista.get(k).getMorasoles());
										
										
										listaPendientesSinContratoCuotaBean.get(j).setMonto(tipo.equals("X")?FuncionesHelper.redondear((listaPendientesSinContratoCuotaBean.get(j).getMonto()/tipocambio-val)*tipocambio, 2):FuncionesHelper.redondear((val), 2));
										listaPendientesSinContratoCuotaBean.get(j).setRentapagada(val);
										
										listaPendientesSinContratoCuotaBean.get(j).setNropagos(lista.get(k).getNropagos());
										listaPendientesSinContratoCuotaBean.get(j).setIgv(listaPendientesSinContratoCuotaBean.get(j).getMonto()/1.18*0.18);
										listaPendientesSinContratoCuotaBean.get(j).setCondicion(lista.get(k).getCancelado().equals("0")?"Pendiente":"Generado");
										//idcuota borrar
										listaPendientesSinContratoCuotaBean.get(j).setIdcuota(lista.get(k).getIdcuota());
									}else{
										if(lista.get(k).getCancelado().equals("2")&& tipo.equals("canceladas y pendientes")){
											val=lista.get(k).getTipocambio()==null? 0.0: FuncionesHelper.redondearNum(lista.get(k).getMontosoles()/lista.get(k).getTipocambio(),2);//estamos mandando un null a formatear
											listaPendientesSinContratoCuotaBean.get(j).setIdcuota(lista.get(k).getIdcuota());
											listaPendientesSinContratoCuotaBean.get(j).setSiacuenta(true);
											listaPendientesSinContratoCuotaBean.get(j).setMoraacumulada(lista.get(k).getMorasoles());
											
											
											listaPendientesSinContratoCuotaBean.get(j).setMonto(tipo.equals("X")?FuncionesHelper.redondearNum((listaPendientesSinContratoCuotaBean.get(j).getMonto()/tipocambio-val)*tipocambio, 2):FuncionesHelper.redondear((val), 2));
											listaPendientesSinContratoCuotaBean.get(j).setRentapagada(FuncionesHelper.redondearNum(0.0,2));
											
											listaPendientesSinContratoCuotaBean.get(j).setNropagos(lista.get(k).getNropagos());
											listaPendientesSinContratoCuotaBean.get(j).setIgv(listaPendientesSinContratoCuotaBean.get(j).getMonto()/1.18*0.18);
											listaPendientesSinContratoCuotaBean.get(j).setCondicion(lista.get(k).getCancelado().equals("0")?"Pendiente":"Generado");
											//idcuota borrar
											listaPendientesSinContratoCuotaBean.get(j).setIdcuota(lista.get(k).getIdcuota());
										
										}else{
									val=lista.get(k).getTipocambio()==null? 0.0: FuncionesHelper.redondearNum(lista.get(k).getMontosoles()/lista.get(k).getTipocambio(),2);//estamos mandando un null a formatear
									listaPendientesSinContratoCuotaBean.get(j).setIdcuota(lista.get(k).getIdcuota());
									listaPendientesSinContratoCuotaBean.get(j).setSiacuenta(true);
									listaPendientesSinContratoCuotaBean.get(j).setMoraacumulada(lista.get(k).getMorasoles());
									
									
									listaPendientesSinContratoCuotaBean.get(j).setMonto(tipo.equals("X")?FuncionesHelper.redondearNum((listaPendientesSinContratoCuotaBean.get(j).getMonto()/tipocambio-val)*tipocambio, 2):FuncionesHelper.redondear((listaPendientesSinContratoCuotaBean.get(j).getMonto()-val), 2));
									listaPendientesSinContratoCuotaBean.get(j).setRentapagada(val);
									
									listaPendientesSinContratoCuotaBean.get(j).setNropagos(lista.get(k).getNropagos());
									listaPendientesSinContratoCuotaBean.get(j).setIgv(listaPendientesSinContratoCuotaBean.get(j).getMonto()/1.18*0.18);
									listaPendientesSinContratoCuotaBean.get(j).setCondicion(lista.get(k).getCancelado().equals("0")?"Pendiente":"Generado");
									//idcuota borrar
									listaPendientesSinContratoCuotaBean.get(j).setIdcuota(lista.get(k).getIdcuota());
										
										}}
								} catch (Exception e) {
									
									System.out.println("Error al obtener tipo de cambio en IDCUOTA="+lista.get(k).getIdcuota());
								}

							}
						}
					}
				}
		}
		if (tipo.equals("pendientes")) {
			return seleccionarRentaPendientes(listaPendientesSinContratoCuotaBean);
		}else {
			return listaPendientesSinContratoCuotaBean;
		}
		
	}
	public List<CuotaBean> obtenerInformacionCobroSinContratoTipoxDeuda(int idcondicion,String tipo,Double tipocambio) {
		List<CuotaBean> listaPendientesSinContratoCuotaBean= new ArrayList<CuotaBean>();
		CondicionBean condicionBean= new CondicionBean();
		
		condicionBean=contratoDAO.obtenerCondicionBean(idcondicion);
		CuotaBean cuota;
		
		int i=0;
		List<Cuota> lista= new ArrayList<Cuota>();
		List<Detallecuota> listadetalle= new ArrayList<Detallecuota>();
		lista=listarcuotasxcontrato(condicionBean.getIdcontrato());//lista de pagadas
		listadetalle=listardetallecuotasxcontrato(condicionBean.getIdcontrato()); //lista de pagadas 
		List<CuotaBean> listapagados= new ArrayList<CuotaBean>();
		listapagados=obtenerPeriodosPendientesContratoDeuda(idcondicion);	
		Calendar iniciocobro = Calendar.getInstance();
		iniciocobro.setTime(condicionBean.getIniciocobro());

		Calendar fincobro = Calendar.getInstance();
		fincobro.setTime(condicionBean.getFincobro()!=null ? condicionBean.getFincobro() :fincobro.getTime());
		fincobro.set(Calendar.DATE, fincobro.getActualMaximum(Calendar.DAY_OF_MONTH));

		/*RECORRE LA LISTA DE CONTRATOS QUE HAN PAGADO EN SOLES*/ /*cindy*/
		if (condicionBean.getMoneda().equals("S")) {
			/*LISTA CON TODOS LOS PAGOS DESDE EL 1999*/
				while (iniciocobro.getTime().compareTo(fincobro.getTime()) < 0) 
				{
					cuota= new CuotaBean();
					cuota.setNrocuota(i);
					cuota.setAnio(iniciocobro.get(Calendar.YEAR));
			    	cuota.setMes(Almanaque.obtenerNombreMes(iniciocobro.get(Calendar.MONTH)));
			    	cuota.setSipagado("0");
			    	cuota.setCondicion("Pendiente");
			    	cuota.setNropagos(1);
			    	cuota.setMonto(FuncionesHelper.redondearNum(condicionBean.getCuota1(), 2)); 
			    	cuota.setNuevarenta(FuncionesHelper.redondearNum(cuota.getMonto(), 2));
			    	
			    	
			    	
			    	cuota.setRenta(cuota.getMonto());
			    	cuota.setIdcontrato(condicionBean.getIdcontrato());
			    	
			    	cuota.setIgv(FuncionesHelper.redondearNum(cuota.getRenta()/1.18*0.18, 2));

			    	cuota.setRentapagada(0.0);
			    	cuota.setMora(0.0);
			    	cuota.setNuevamc(0.0);
			    	//cuota.setNuevarenta(0.0);

			    	listaPendientesSinContratoCuotaBean.add(cuota);
			    	
			    	iniciocobro.add(Calendar.MONTH, 1);
			    	i++;
				}
//				for (int j = 0;j < listaPendientesSinContratoCuotaBean.size(); j++) {
//					for (int k = 0; k <listapagados.size() ; k++){
//						if(listaPendientesSinContratoCuotaBean.get(j).getMes().equals(listapagados.get(k).getMes()) && listaPendientesSinContratoCuotaBean.get(j).getAnio()==listapagados.get(k).getAnio() ){
//							
//								if(FuncionesHelper.redondear(listaPendientesSinContratoCuotaBean.get(j).getMonto(),2)-FuncionesHelper.redondear(listapagados.get(k).getMonto(),2)==0){
//									listaPendientesSinContratoCuotaBean.get(j).setSipagado("1");
//									listaPendientesSinContratoCuotaBean.get(j).setMonto(0.0);
//									listaPendientesSinContratoCuotaBean.get(j).setRentapagada(FuncionesHelper.redondear(listaPendientesSinContratoCuotaBean.get(j).getRenta(),2));	
//								}else{
//									if(listapagados.get(k).getCancelado().equals("1")){
//										listaPendientesSinContratoCuotaBean.get(j).setSipagado("1");
//										listaPendientesSinContratoCuotaBean.get(j).setMonto(0.0);
//										listaPendientesSinContratoCuotaBean.get(j).setRentapagada(FuncionesHelper.redondear(listaPendientesSinContratoCuotaBean.get(j).getRenta(),2));	
//									}else if(listapagados.get(k).getCancelado().equals("0")||listapagados.get(k).getCancelado().equals("2")){
//										listaPendientesSinContratoCuotaBean.get(j).setSipagado("0");
//										listaPendientesSinContratoCuotaBean.get(j).setIdcuota(listapagados.get(k).getIdcuota());
//										listaPendientesSinContratoCuotaBean.get(j).setSiacuenta(true);
//										listaPendientesSinContratoCuotaBean.get(j).setNropagos(listapagados.get(k).getNropagos());
//										listaPendientesSinContratoCuotaBean.get(j).setMoraacumulada(listapagados.get(k).getMora());
//										listaPendientesSinContratoCuotaBean.get(j).setRentapagada(FuncionesHelper.redondear(listaPendientesSinContratoCuotaBean.get(j).getMonto()-listapagados.get(k).getMonto(),2));
//										listaPendientesSinContratoCuotaBean.get(j).setMonto(FuncionesHelper.redondear(listaPendientesSinContratoCuotaBean.get(j).getMonto()-listapagados.get(k).getMonto(),2));
//										}
//									}
//							
//						}
//							
//					}
//				}
				Double montosoles=0.0;
				/*Cindy, SACA LA LISTA DE PENDIENTES EN SOLES */
				for (int j = 0;j < listaPendientesSinContratoCuotaBean.size(); j++) {
					;
					for (int k = 0; k <listapagados.size() ; k++) 
					{   
						
						if (listaPendientesSinContratoCuotaBean.get(j).getMes().equals(listapagados.get(k).getMes())&& Integer.toString(listaPendientesSinContratoCuotaBean.get(j).getAnio()).equals(Integer.toString(listapagados.get(k).getAnio())) ) {

								
							if (listapagados.get(k).getCancelado().equals("1")) 
							{
								listaPendientesSinContratoCuotaBean.get(j).setMonto(0.0);
								listaPendientesSinContratoCuotaBean.get(j).setSipagado("1");
								listaPendientesSinContratoCuotaBean.get(j).setRentapagada(listaPendientesSinContratoCuotaBean.get(j).getRenta());
								//idcuota borrar
								listaPendientesSinContratoCuotaBean.get(j).setIdcuota(listapagados.get(k).getIdcuota());

							} 
							else 
							{   
								
								if (listapagados.get(k).getCancelado().equals("2")&& tipo.equals("ratio")) {
									
									listaPendientesSinContratoCuotaBean.get(j).setIdcuota(listapagados.get(k).getIdcuota());
									listaPendientesSinContratoCuotaBean.get(j).setSiacuenta(true);
									listaPendientesSinContratoCuotaBean.get(j).setNropagos(listapagados.get(k).getNropagos());
									listaPendientesSinContratoCuotaBean.get(j).setMoraacumulada(listapagados.get(k).getMora());
									montosoles=0.0;
									for( int p=0;p<listadetalle.size();p++){
										   
										if(listapagados.get(k).getIdcuota()==listadetalle.get(p).getCuota().getIdcuota()){
											
										  if(listadetalle.get(p).getCancelado().equals("0")){// monto en estado pendiente
											  montosoles=montosoles+FuncionesHelper.redondearNum(listadetalle.get(p).getMontosoles(),2);
										  }else if(listadetalle.get(p).getCancelado().equals("1")){//monto en estado cancelado
											  montosoles=montosoles+FuncionesHelper.redondearNum(listadetalle.get(p).getMontosoles(),2);
											  
										  }else{//montos en estado generado 
											  //montosoles=montosoles+FuncionesHelper.redondear(listadetalle.get(p).getMontosoles(),2);
										  }
										  listaPendientesSinContratoCuotaBean.get(j).setRentapagada(FuncionesHelper.redondearNum(montosoles,2));
										  //listaPendientesSinContratoCuotaBean.get(j).setMonto(FuncionesHelper.redondear(montosoles,2));
										  //nuevo monto para ratio 
										  listaPendientesSinContratoCuotaBean.get(j).setMonto(FuncionesHelper.redondearNum((condicionBean.getCuota1()-FuncionesHelper.redondear(montosoles,2)),2));
										}
									}
									//listaPendientesSinContratoCuotaBean.get(j).setRentapagada(FuncionesHelper.redondear(lista.get(k).getMontosoles(),2)); 
									//listaPendientesSinContratoCuotaBean.get(j).setMonto(FuncionesHelper.redondear(lista.get(k).getMontosoles(), 2));
									listaPendientesSinContratoCuotaBean.get(j).setIgv(listapagados.get(k).getMonto()/1.18*0.18);
									listaPendientesSinContratoCuotaBean.get(j).setCondicion(listapagados.get(k).getCancelado().equals("0")?"Pendiente":"Generado");
									//idcuota borrar
									listaPendientesSinContratoCuotaBean.get(j).setIdcuota(listapagados.get(k).getIdcuota());
								}else{
									if((listapagados.get(k).getCancelado().equals("2")&& tipo.equals("pendientes"))||(listapagados.get(k).getCancelado().equals("2")&& tipo.equals("canceladas y pendientes"))){
										listaPendientesSinContratoCuotaBean.get(j).setIdcuota(listapagados.get(k).getIdcuota());
										listaPendientesSinContratoCuotaBean.get(j).setSiacuenta(true);
										listaPendientesSinContratoCuotaBean.get(j).setNropagos(listapagados.get(k).getNropagos());
										listaPendientesSinContratoCuotaBean.get(j).setMoraacumulada(listapagados.get(k).getMora());
//										montosoles=0.0;
//										for( int p=0;p<listadetalle.size();p++){
//											   
//											if(listapagados.get(k).getIdcuota()==listadetalle.get(p).getCuota().getIdcuota()){
//												
//											  if(listadetalle.get(p).getCancelado().equals("0")){
//												  montosoles=montosoles+FuncionesHelper.redondear(listadetalle.get(p).getMontosoles(),2);
//
//											  }else if(listadetalle.get(p).getCancelado().equals("1")){
//												  montosoles=montosoles+FuncionesHelper.redondear(listadetalle.get(p).getMontosoles(),2);
//												  
//											  }else{
//												  
//											  }
//											  listaPendientesSinContratoCuotaBean.get(j).setRentapagada(FuncionesHelper.redondear(montosoles,2));
//											}
//										}
										listaPendientesSinContratoCuotaBean.get(j).setSipagado("0");
										listaPendientesSinContratoCuotaBean.get(j).setRentapagada(FuncionesHelper.redondearNum(listapagados.get(k).getMonto(),2));
										//listaPendientesSinContratoCuotaBean.get(j).setRentapagada(FuncionesHelper.redondear(0.0,2)); 
										listaPendientesSinContratoCuotaBean.get(j).setMonto(FuncionesHelper.redondearNum(condicionBean.getCuota1()-listaPendientesSinContratoCuotaBean.get(j).getRentapagada(), 2));

										//listaPendientesSinContratoCuotaBean.get(j).setMonto(FuncionesHelper.redondear(lista.get(k).getMontosoles(), 2));
										listaPendientesSinContratoCuotaBean.get(j).setIgv(listapagados.get(k).getMonto()/1.18*0.18);
										listaPendientesSinContratoCuotaBean.get(j).setCondicion(listapagados.get(k).getCancelado().equals("0")?"Pendiente":"Pendiente");
										//idcuota borrar
										listaPendientesSinContratoCuotaBean.get(j).setIdcuota(listapagados.get(k).getIdcuota());
									}else{
									
									listaPendientesSinContratoCuotaBean.get(j).setIdcuota(listapagados.get(k).getIdcuota());
//									montosoles=0.0;
//									for( int p=0;p<listadetalle.size();p++){
//										   
//										if(lista.get(k).getIdcuota()==listadetalle.get(p).getCuota().getIdcuota()){
//											
//										  if(listadetalle.get(p).getCancelado().equals("0")){
//											  montosoles=montosoles+FuncionesHelper.redondear(listadetalle.get(p).getMontosoles(),2);
//										  }else if(listadetalle.get(p).getCancelado().equals("1")){
//											  montosoles=montosoles+FuncionesHelper.redondear(listadetalle.get(p).getMontosoles(),2);
//											  
//										  }else{
//											  
//										  }
//										  listaPendientesSinContratoCuotaBean.get(j).setRentapagada(FuncionesHelper.redondear(montosoles,2));
//										}
//									}
									
									listaPendientesSinContratoCuotaBean.get(j).setRentapagada(FuncionesHelper.redondearNum(listapagados.get(k).getMonto(),2));
									listaPendientesSinContratoCuotaBean.get(j).setMonto(FuncionesHelper.redondearNum(condicionBean.getCuota1()-listaPendientesSinContratoCuotaBean.get(j).getRentapagada(), 2));
									//listaPendientesSinContratoCuotaBean.get(j).setMonto(FuncionesHelper.redondear(condicionBean.getCuota1()-lista.get(k).getMontosoles(), 2));  //Franco
									listaPendientesSinContratoCuotaBean.get(j).setSipagado(listaPendientesSinContratoCuotaBean.get(j).getMonto()==0.0? "1":"0");
									listaPendientesSinContratoCuotaBean.get(j).setSiacuenta(true);
									//listaPendientesSinContratoCuotaBean.get(j).setRentapagada(FuncionesHelper.redondear(lista.get(k).getMontosoles(),2));
									listaPendientesSinContratoCuotaBean.get(j).setNropagos(listapagados.get(k).getNropagos());
									listaPendientesSinContratoCuotaBean.get(j).setMoraacumulada(listapagados.get(k).getMora());
									listaPendientesSinContratoCuotaBean.get(j).setIgv(listaPendientesSinContratoCuotaBean.get(j).getMonto()/1.18*0.18);
									listaPendientesSinContratoCuotaBean.get(j).setCondicion(listapagados.get(k).getCancelado().equals("0")?"Pendiente":"Generado");
									//idcuota borrar
									listaPendientesSinContratoCuotaBean.get(j).setIdcuota(listapagados.get(k).getIdcuota());
									 }
									}
							}
						}
						//System.out.println("listaa=="+listaPendientesSinContratoCuotaBean.get(k).toString());
					}
				}

			} 
		else 
		{
				/* LISTA DE PENDIENTES SIN CONTRATO EN DOLARES */ //CINDY
				/*LISTA CON TODOS LOS PAGOS DESDE EL 1999*/
				while (iniciocobro.getTime().compareTo(fincobro.getTime()) < 0) 
				{
					cuota= new CuotaBean();
					cuota.setNrocuota(i);
					cuota.setAnio(iniciocobro.get(Calendar.YEAR));
					cuota.setMes(Almanaque.obtenerNombreMes(iniciocobro.get(Calendar.MONTH)));
					cuota.setSipagado("0");
					cuota.setCondicion("Pendiente");
					cuota.setNropagos(1);
					cuota.setIdcontrato(condicionBean.getIdcontrato());
					cuota.setMonto(tipo.equals("X")?FuncionesHelper.redondearNum(condicionBean.getCuota1()*tipocambio, 2):FuncionesHelper.redondearNum(condicionBean.getCuota1(), 2));//CINDY   //getMontocuotasoles() bota en dólares
					
					cuota.setRenta(cuota.getMonto()); //cindy //Bota el monto en dólares
			    	cuota.setIgv(FuncionesHelper.redondear(cuota.getRenta()/1.18*0.18, 2));

			    	cuota.setRentapagada(0.0);
			    	cuota.setMora(0.0);
			    	cuota.setNuevamc(0.0);
			    	//cuota.setNuevarenta(0.0);
			    	cuota.setNuevarenta(FuncionesHelper.redondear(cuota.getMonto(), 2));
			    	listaPendientesSinContratoCuotaBean.add(cuota);
			    	iniciocobro.add(Calendar.MONTH, 1);
			    	i++;
				}

				Double val;
				for (int j = 0;j < listaPendientesSinContratoCuotaBean.size(); j++) 
				{
					for (int k = 0; k <listapagados.size() ; k++) 
					{
						if (listaPendientesSinContratoCuotaBean.get(j).getMes().equals(listapagados.get(k).getMes())&& listaPendientesSinContratoCuotaBean.get(j).getAnio()==listapagados.get(k).getAnio() ) 
						{

							if (listapagados.get(k).getCancelado().equals("1")) //cancelado
							{
								listaPendientesSinContratoCuotaBean.get(j).setMonto(0.0);
								listaPendientesSinContratoCuotaBean.get(j).setSipagado("1");
								listaPendientesSinContratoCuotaBean.get(j).setRentapagada(listaPendientesSinContratoCuotaBean.get(j).getRenta());
								//idcuota borrar
								listaPendientesSinContratoCuotaBean.get(j).setIdcuota(listapagados.get(k).getIdcuota());
							} 
							else 
							{//si tiene pendiente
								
								try {//si encuentra error lo atrapa y continua si n caerese
									
									if (listapagados.get(k).getCancelado().equals("2")&& tipo.equals("ratio")) {
										val=listapagados.get(k).getTipocambio()==null? 0.0: FuncionesHelper.redondearNum(listapagados.get(k).getMonto()/listapagados.get(k).getTipocambio(),2);//estamos mandando un null a formatear
										listaPendientesSinContratoCuotaBean.get(j).setIdcuota(listapagados.get(k).getIdcuota());
										listaPendientesSinContratoCuotaBean.get(j).setSiacuenta(true);
										listaPendientesSinContratoCuotaBean.get(j).setMoraacumulada(listapagados.get(k).getMora());
										
										
										listaPendientesSinContratoCuotaBean.get(j).setMonto(tipo.equals("X")?FuncionesHelper.redondearNum((listaPendientesSinContratoCuotaBean.get(j).getMonto()/tipocambio-val)*tipocambio, 2):FuncionesHelper.redondear((val), 2));
										listaPendientesSinContratoCuotaBean.get(j).setRentapagada(val);
										
										listaPendientesSinContratoCuotaBean.get(j).setNropagos(listapagados.get(k).getNropagos());
										listaPendientesSinContratoCuotaBean.get(j).setIgv(listaPendientesSinContratoCuotaBean.get(j).getMonto()/1.18*0.18);
										listaPendientesSinContratoCuotaBean.get(j).setCondicion(listapagados.get(k).getCancelado().equals("0")?"Pendiente":"Generado");
										//idcuota borrar
										listaPendientesSinContratoCuotaBean.get(j).setIdcuota(listapagados.get(k).getIdcuota());
									}else{
										if(listapagados.get(k).getCancelado().equals("2")&& tipo.equals("canceladas y pendientes")){
											val=listapagados.get(k).getTipocambio()==null? 0.0: FuncionesHelper.redondearNum(lista.get(k).getMontosoles()/listapagados.get(k).getTipocambio(),2);//estamos mandando un null a formatear
											listaPendientesSinContratoCuotaBean.get(j).setIdcuota(listapagados.get(k).getIdcuota());
											listaPendientesSinContratoCuotaBean.get(j).setSiacuenta(true);
											listaPendientesSinContratoCuotaBean.get(j).setMoraacumulada(listapagados.get(k).getMora());
											
											
											listaPendientesSinContratoCuotaBean.get(j).setMonto(tipo.equals("X")?FuncionesHelper.redondearNum((listaPendientesSinContratoCuotaBean.get(j).getMonto()/tipocambio-val)*tipocambio, 2):FuncionesHelper.redondear((val), 2));
											listaPendientesSinContratoCuotaBean.get(j).setRentapagada(FuncionesHelper.redondearNum(0.0,2));
											
											listaPendientesSinContratoCuotaBean.get(j).setNropagos(listapagados.get(k).getNropagos());
											listaPendientesSinContratoCuotaBean.get(j).setIgv(listaPendientesSinContratoCuotaBean.get(j).getMonto()/1.18*0.18);
											listaPendientesSinContratoCuotaBean.get(j).setCondicion(listapagados.get(k).getCancelado().equals("0")?"Pendiente":"Generado");
											//idcuota borrar
											listaPendientesSinContratoCuotaBean.get(j).setIdcuota(listapagados.get(k).getIdcuota());
										
										}else{
									val=listapagados.get(k).getTipocambio()==null? 0.0: FuncionesHelper.redondearNum(listapagados.get(k).getMonto()/lista.get(k).getTipocambio(),2);//estamos mandando un null a formatear
									listaPendientesSinContratoCuotaBean.get(j).setIdcuota(listapagados.get(k).getIdcuota());
									listaPendientesSinContratoCuotaBean.get(j).setSiacuenta(true);
									listaPendientesSinContratoCuotaBean.get(j).setMoraacumulada(listapagados.get(k).getMora());
									
									
									listaPendientesSinContratoCuotaBean.get(j).setMonto(tipo.equals("X")?FuncionesHelper.redondearNum((listaPendientesSinContratoCuotaBean.get(j).getMonto()/tipocambio-val)*tipocambio, 2):FuncionesHelper.redondear((listaPendientesSinContratoCuotaBean.get(j).getMonto()-val), 2));
									listaPendientesSinContratoCuotaBean.get(j).setRentapagada(val);
									
									listaPendientesSinContratoCuotaBean.get(j).setNropagos(listapagados.get(k).getNropagos());
									listaPendientesSinContratoCuotaBean.get(j).setIgv(listaPendientesSinContratoCuotaBean.get(j).getMonto()/1.18*0.18);
									listaPendientesSinContratoCuotaBean.get(j).setCondicion(listapagados.get(k).getCancelado().equals("0")?"Pendiente":"Generado");
									//idcuota borrar
									listaPendientesSinContratoCuotaBean.get(j).setIdcuota(listapagados.get(k).getIdcuota());
										
										}}
								} catch (Exception e) {
									
									System.out.println("Error al obtener tipo de cambio en IDCUOTA="+lista.get(k).getIdcuota());
								}

							}
						}
					}
				}
		}
		if (tipo.equals("pendientes")) {
			return seleccionarRentaPendientes(listaPendientesSinContratoCuotaBean);
		}else {
			return listaPendientesSinContratoCuotaBean;
		}
		
	}


	
	
	public double obtenerRentaActualCondicion(Integer idcondicion) {
		
		CondicionBean condicionCobro= new CondicionBean();
		condicionCobro=contratoDAO.obtenerCondicionBean(idcondicion);

		/**condicional para poner el valor de la renta actual en todo contrato**/
		if (condicionCobro.getCondicion().equals("Contrato") || condicionCobro.getCondicion().equals("Precontrato")) {
			int i=0;
			
			Calendar iniciocobro=Calendar.getInstance();
			Calendar fincobro=Calendar.getInstance();
			
			iniciocobro.setTime(condicionCobro.getIniciocobro());
			fincobro.setTime(condicionCobro.getFincobro());

			if (condicionCobro.getFincobro().compareTo(new Date())>0) {
				fincobro.setTime(new Date());
			}else{
				fincobro.setTime(fincobro.getTime());//Conserva su valor inicial
			}

			while (iniciocobro.compareTo(fincobro) <= 0) {
				iniciocobro.add(Calendar.MONTH, 1);
			    i++;
			}
			
			if (i<=12) {
				return condicionCobro.getCuota1();
			}else if (i<=24) {
				return condicionCobro.getCuota2();
			}else if (i<=36) {
				return condicionCobro.getCuota3();
			}else if (i<=48) {
				return condicionCobro.getCuota4();
			}else if (i<=60) {
				return condicionCobro.getCuota5();
			}else {
				return condicionCobro.getCuota6();
			}
		}else {
			return condicionCobro.getCuota1();
		}
	}
	
	
	
	@Override
	public double obtenerDeudaCobroSinContratoCA(int idcondicion) {

		return 0;
	}
	@Override
	public double obtenerDeudaCobroContratoCA(int idcondicion) {
		return 0;
	}
	@Override
	public Map<String,String> obtenerDatosDeudaCobroUPA(int idupa,double tipocambio) {
		Map<String, String> datos = new HashMap<String, String>();
		
		List<CondicionBean> listaCondicionesDeUpaConDeuda=new ArrayList<CondicionBean>();
		listaCondicionesDeUpaConDeuda=carteraDAO.obtenerCondicionesDeUpaConDeuda(idupa);
 		
		double totalMontoDeuda=0;
		Integer totalMesesDeuda=0;
		
		for (CondicionBean condicionBean : listaCondicionesDeUpaConDeuda) {
			List<CuotaBean> lista=new ArrayList<CuotaBean>();
			
			if (condicionBean.getCondicion().equals("Sin Contrato")) {
				lista=obtenerInformacionCobroSinContratoTipo(condicionBean.getIdcontrato(),"pendientes" ,tipocambio);
			} else {
				lista=obtenerInformacionCobroContratoTipo(condicionBean.getIdcontrato(),"pendientes" ,tipocambio);
			}
			
			
			totalMontoDeuda=totalMontoDeuda+obtenerSumaTotalCuotas(lista);
			totalMesesDeuda=totalMesesDeuda+lista.size();
		}
		
		datos.put("MESESDEUDA", String.valueOf(totalMesesDeuda));
		datos.put("VALORDEUDA", String.valueOf(totalMontoDeuda));
		
		return datos;
	}
	@Override
	public String resumenPeriodosPendientesCondicion(int idcondicion,String condicion) {
			List<CuotaBean> lista=new ArrayList<>();
			lista=condicion.equals("Sin Contrato")?obtenerInformacionCobroSinContratoTipo(idcondicion,"pendientes", 0.0):obtenerInformacionCobroContratoTipo(idcondicion,"pendientes", 0.0);
		  	String resultado="";
	        Boolean inicializar=true;
	        
	        for (int i = 0; i < lista.size(); i++) {
	            
	            if (inicializar) {
	                resultado=resultado+lista.get(i).getMes().toUpperCase().substring(0,3)+"-";
	            }

	            if (i!=lista.size()-1) {
	                if (lista.get(i).getAnio()==lista.get(i+1).getAnio()) {

	                     if (Almanaque.obtenerNombreMes(Integer.parseInt(Almanaque.mesanumero(lista.get(i).getMes())))!=lista.get(i+1).getMes()) {
	                        resultado=resultado+lista.get(i).getMes().toUpperCase().substring(0,3)+"/"+lista.get(i).getAnio()+",";
	                        inicializar=true;
	                     
	                     }else{
	                        inicializar=false;
	                     }
	                }else{
	                    resultado=resultado+lista.get(i).getMes().toUpperCase().substring(0,3)+"/"+lista.get(i).getAnio()+",";
	                    inicializar=true;
	                }
	            }else{
	                resultado=resultado+lista.get(i).getMes().toUpperCase().substring(0,3)+"/"+lista.get(i).getAnio()+".";
	            }
	        }
	        
	        resultado=resultado.replaceAll("ENE-ENE", "ENE");
	        resultado=resultado.replaceAll("FEB-FEB", "FEB");
	        resultado=resultado.replaceAll("MAR-MAR", "MAR");
	        resultado=resultado.replaceAll("ABR-ABR", "ABR");
	        resultado=resultado.replaceAll("MAY-MAY", "MAY");
	        resultado=resultado.replaceAll("JUN-JUN", "JUN");
	        resultado=resultado.replaceAll("JUL-JUL", "JUL");
	        resultado=resultado.replaceAll("AGO-AGO", "AGO");
	        resultado=resultado.replaceAll("SEP-SEP", "SEP");
	        resultado=resultado.replaceAll("OCT-OCT", "OCT");
	        resultado=resultado.replaceAll("NOV-NOV", "NOV");
	        resultado=resultado.replaceAll("DIC-DIC", "DIC");
	        
	        return resultado;
	}




	@Override
	public UpaPagadorDeudorBean obtenerInformacionUltimoPagoRentaxCondicion(int idcondicion, String Desde,String Hasta) {
		return carteraDAO.obtenerInformacionUltimoPagoRentaxCondicion(idcondicion,  Desde, Hasta);
	}
	@Override
	public String obtenerPrimeraRentaPendienteCondicion(Integer idcondicion) {
		List<CuotaBean> lista= new ArrayList<CuotaBean>();
		CondicionBean condicionCobro= new CondicionBean();
		condicionCobro=contratoDAO.obtenerCondicionBean(idcondicion);
		
		/**condicional para poner el valor de la renta actual en todo contrato**/
		if (condicionCobro.getCondicion().equals("Contrato") || condicionCobro.getCondicion().equals("Precontrato")) {
			lista=generarPendientesParaCobroContrato(condicionCobro.getIdcontrato(), 0.0);
		}else {
			lista=generarPendientesParaCobroSinContrato(condicionCobro.getIdcontrato(), 0.0);
		}
		
		if (lista.size()!=0) {
			return lista.get(0).getMes().substring(0,3)+"-"+lista.get(0).getAnio();
		} else {
			return "Al día";
		}
	}
	
	@Override
	public Integer obtenerNumeroRentasCondicion(int idcondicion,String condicion, Date desde,Date hasta) {
			
		int nro=0;
		Calendar iniciocobro=Calendar.getInstance();
		Calendar fincobro=Calendar.getInstance();
		
		iniciocobro.setTime(desde);
		fincobro.setTime(hasta);

		if (condicion.equals("Sin Contrato")) {
			
			if (fincobro.getTime()!=null ) {
				fincobro.setTime(fincobro.getTime());
			} else {
				fincobro.setTime(new Date());
			}
		
		}else {
			
			if (fincobro.getTime().compareTo(new Date())>0) {
				fincobro.setTime(new Date());
			}else{
				fincobro.setTime(fincobro.getTime());//Conserva su valor inicial
			}
		}
		
		while (iniciocobro.compareTo(fincobro) <= 0) {
			iniciocobro.add(Calendar.MONTH, 1);
			nro++;
		}
		
		return nro;
		
	}
	
	public UpaPagadorDeudorBean obtenerInformacionUltimoPagoRentaxUpa(int idupa) {
		return carteraDAO.obtenerInformacionUltimoPagoRentaxUpa(idupa);
	}

	
	public List<UpaPagadorDeudorBean> determinarCarteraActiva(String nombrecartera, String Desde, String Hasta) {

		List<UpaPagadorDeudorBean> listaCA= new ArrayList<UpaPagadorDeudorBean>();
		List<UpaPagadorDeudorBean> temporal = new ArrayList<UpaPagadorDeudorBean>();
		temporal=carteraDAO.obtenerDeterminadosComprobantes(Desde,Hasta,nombrecartera);
		
		int flag=0;
		
		for (int i = 0; i < temporal.size(); i++) {
				if (flag==0) {
					listaCA.add(temporal.get(i));
					flag++;
				}
				if (i!=temporal.size()-1) {
					if(!temporal.get(i).getIdupa().equals(temporal.get(i+1).getIdupa())) {
						flag=0;
					}
				}
			}
		
		return listaCA;
	}
	
	
	public List<UpaPagadorDeudorBean> determinarCarteraPesada(String nombrecartera, String Desde, String Hasta) {
		
		List<UpaPagadorDeudorBean> listaCA= new ArrayList<UpaPagadorDeudorBean>();
		List<UpaPagadorDeudorBean> listaCP= new ArrayList<UpaPagadorDeudorBean>();
		
		listaCA=determinarCarteraActiva(nombrecartera, Desde, Hasta);
		
		
		List<Integer> listaIdUpa= new ArrayList<Integer>(FuncionesHelper.eliminarDuplicados(carteraDAO.listarUpasConCondicion()));
		List<Integer> listaIdUpaCA= new ArrayList<Integer>();
		
		for (UpaPagadorDeudorBean upaPagadorDeudorBean : listaCA) {
			listaIdUpaCA.add(upaPagadorDeudorBean.getIdupa());
		}
		
		listaIdUpa.removeAll(listaIdUpaCA);/**operacion diferencia*/
		
		for (Integer idupa : listaIdUpa) {
			UpaPagadorDeudorBean upaPagadorDeudorBean= new UpaPagadorDeudorBean();
			upaPagadorDeudorBean=carteraDAO.obtenerInformacionUltimoPagoRentaxUpa(idupa);
			
			if (upaPagadorDeudorBean==null) {
				CondicionBean condicionBean= new CondicionBean();
				condicionBean=carteraDAO.obtenerCondicionesDeUpaConDeuda(idupa).get(0);
				upaPagadorDeudorBean=new UpaPagadorDeudorBean();
				upaPagadorDeudorBean.setClaveUpa(condicionBean.getClaveUpa());
				upaPagadorDeudorBean.setCondicion(condicionBean.getCondicion());
				upaPagadorDeudorBean.setDistrito(condicionBean.getDistrito());
				upaPagadorDeudorBean.setDireccion(condicionBean.getDireccion());
				upaPagadorDeudorBean.setNombreInquilino(condicionBean.getNombresInquilino());
				upaPagadorDeudorBean.setMoneda(condicionBean.getMoneda());
				upaPagadorDeudorBean.setRenta(obtenerRentaActualCondicion(condicionBean.getIdcontrato()));
			}
			
			listaCP.add(upaPagadorDeudorBean);
			
		}
		
		return listaCP;
	}
	
	public ICarteraDAO getCarteraDAO() {
		return carteraDAO;
	}

	public void setCarteraDAO(ICarteraDAO carteraDAO) {
		this.carteraDAO = carteraDAO;
	}




	@Override
	public List<CuotaBean> listarRentaMensualPagadoCondicionxIntervalo(int idcondicion, Date desde, Date hasta, String moneda) {
		return carteraDAO.listarRentaMensualPagadoCondicionxIntervalo(idcondicion,desde,hasta, moneda);
	}
	
	@Override
	public List<CuotaBean> listarRentaMensualAPagarCondicionxIntervalo(int idcondicion, Date desde, Date Hasta) {
		
		List<CuotaBean> listaCuotasPendientesCondicion = new ArrayList<CuotaBean>();
		CondicionBean condicionBean= new CondicionBean();
		condicionBean=contratoDAO.obtenerCondicionBean(idcondicion);
		listaCuotasPendientesCondicion.clear();
		
		Calendar iniciocobro = Calendar.getInstance();
		
		iniciocobro.setTime(condicionBean.getIniciocobro());
		
		
		if (condicionBean.getCondicion().equals("Sin Contrato")) {
			int i=0;
		Calendar fin = GregorianCalendar.getInstance();
		fin.setTime(Hasta);
		
			if (condicionBean.getFincobro()!=null ) {
				if (condicionBean.getFincobro().compareTo(Hasta)<0) {

					fin.setTime(condicionBean.getFincobro());
				}
			}
			
			iniciocobro.set(Calendar.DATE, fin.getActualMinimum(Calendar.DAY_OF_MONTH));
			fin.set(Calendar.DATE, fin.getActualMaximum(Calendar.DAY_OF_MONTH));
			
			/*LISTA CON TODOS LOS PAGOS DESDE EL 1999*/
			
			while (iniciocobro.compareTo(fin) < 0) {
//				System.out.println("inicio: "+iniciocobro.getTime()+"fin"+fin.getTime());
				CuotaBean cuota= new CuotaBean();
			    cuota.setNrocuota(i);
			    cuota.setAnio(iniciocobro.get(Calendar.YEAR));
			    cuota.setMes(Almanaque.obtenerNombreMes(iniciocobro.get(Calendar.MONTH)));
			    cuota.setMonto(condicionBean.getCuota1());
			    cuota.setClaveupa(condicionBean.getClaveUpa());
			    cuota.setCondicion(condicionBean.getCondicion());
			    listaCuotasPendientesCondicion.add(cuota);
			    iniciocobro.add(Calendar.MONTH, 1);
			    
			    i++;
			}
		}else {
			
			iniciocobro.setTime(condicionBean.getIniciocobro());
			int j=1;
			
			Calendar fin = Calendar.getInstance();
			fin.setTime(Hasta);
			if (condicionBean.getFincobro().compareTo(Hasta)<0) {
				fin.setTime(condicionBean.getFincobro());	
			}
			
			
			fin.set(Calendar.DATE, fin.getActualMaximum(Calendar.DAY_OF_MONTH));
					/*LISTA CON TODOS LOS PAGOS DEL CONTRATO*/
					while (iniciocobro.compareTo(fin) <= 0) {
						
						CuotaBean cuota= new CuotaBean();
					    cuota.setNrocuota(j);
					    cuota.setAnio(iniciocobro.get(Calendar.YEAR));
					    cuota.setMes(Almanaque.obtenerNombreMes(iniciocobro.get(Calendar.MONTH)));
					    cuota.setClaveupa(condicionBean.getClaveUpa());
					    cuota.setCondicion(condicionBean.getCondicion());
				
						if (j<=12) {
							cuota.setMonto(condicionBean.getCuota1());
						}else if (j<=24) {
							cuota.setMonto(condicionBean.getCuota2());
						}else if (j<=36) {
							cuota.setMonto(condicionBean.getCuota3());
						}else if (j<=48) {
							cuota.setMonto(condicionBean.getCuota4());
						}else if (j<=60) {
							cuota.setMonto(condicionBean.getCuota5());
						}else {
							cuota.setMonto(condicionBean.getCuota6());
						}
						cuota.setMc(cuota.getMonto());
					    listaCuotasPendientesCondicion.add(cuota);
					    iniciocobro.add(Calendar.MONTH, 1);
					    j++;
					}
			}
		
		
		return (ArrayList<CuotaBean>) listaCuotasPendientesCondicion;
	}

	@Override
	public List<CuotaBean> generarRentasPendientesParaCobroNuevoContrato(int idcontrato, Double valorTipoCambio) {
		CondicionBean condicionBean= new CondicionBean();
		condicionBean=contratoDAO.obtenerCondicionBean(idcontrato);
		List<CuotaBean> listaCuotaBean= new ArrayList<CuotaBean>();
		listaCuotaBean=carteraDAO.generarPendientesParaCobroNuevoContrato(idcontrato,valorTipoCambio);
		
		int i=0;
		
		for (CuotaBean cuotaBean : listaCuotaBean) {
			
			cuotaBean.setClaveupa(cuotaBean.getClave());
			cuotaBean.setNrocuota(i+1);
			cuotaBean.setAnio(cuotaBean.getAnio());
			cuotaBean.setMes(cuotaBean.getMes());
			cuotaBean.setSipagado("0");
			
			cuotaBean.setCondicion("Pendiente");
			//cuotaBean.setNropagos(cuotaBean.getNropagos()!=null?cuotaBean.getNropagos():0);
			//cuotaBean.setSiacuenta(cuotaBean.getNropagos()>0?Boolean.TRUE:Boolean.FALSE);
			if(cuotaBean.getNropagos()==null){
				cuotaBean.setNropagos(0);
				cuotaBean.setSiacuenta(cuotaBean.getNropagos()>0?Boolean.TRUE:Boolean.FALSE);
			}else{
				cuotaBean.setSiacuenta(cuotaBean.getNropagos()>=0?Boolean.TRUE:Boolean.FALSE);
			}
			
			
			cuotaBean.setIdcuota(cuotaBean.getIdcuota());
			if (condicionBean.getMoneda().equals("D") && valorTipoCambio>0 ) {
				cuotaBean.setMontodolar(cuotaBean.getMonto());
				cuotaBean.setMonto(FuncionesHelper.redondearNum(cuotaBean.getMonto()*valorTipoCambio, 2));
			}else{
				cuotaBean.setMonto(FuncionesHelper.redondearNum(cuotaBean.getMonto(),2));
				
			}
			
			//System.out.println(cuotaBean.getClaveupa());
			if(cuotaBean.getRentapagada()!=null){
			cuotaBean.setRentapagada(FuncionesHelper.redondearNum(cuotaBean.getRentapagada(),2));
			
     		cuotaBean.setDeudaacuenta(FuncionesHelper.redondearNum(cuotaBean.getRentapagada(),2));
     		
     		
			}
			cuotaBean.setMoraacumulada(cuotaBean.getMoraacumulada());
			cuotaBean.setMora(0.0);
			cuotaBean.setMontopenalizacion(0.0);
			i++;
			
			
		}
		
		
		return listaCuotaBean;
	}

	public List<CuotaBean> generarCuotasPendientes(List<CuotaBean> listacondicion,Double valorTipoCambio){
	    List<CuotaBean> listacuotabean= new ArrayList<CuotaBean>();
		CuotaBean cuotaBean = new CuotaBean();
		int i=0;
		for (CuotaBean cuota : listacondicion) {
			
			cuotaBean.setClaveupa(cuota.getClaveupa());
			cuotaBean.setNrocuota(i+1);
			cuotaBean.setAnio(cuota.getAnio());
			cuotaBean.setMes(cuota.getMes());
			cuotaBean.setSipagado("0");			
			cuotaBean.setCondicion("Pendiente");
			cuotaBean.setNropagos(cuota.getNropagos()!=null?cuota.getNropagos():0);
			cuotaBean.setSiacuenta(cuotaBean.getNropagos()>0?Boolean.TRUE:Boolean.FALSE);
			cuotaBean.setIdcontrato(cuota.getIdcontrato());
			//cuotaBean.setIddetallecondicion(cuota.getId_maedetallecondicion());
			cuotaBean.setId_maedetallecondicion(cuota.getId_maedetallecondicion());
			cuotaBean.setIdcuota(cuota.getIdcuota());
			cuotaBean.setMoneda(cuota.getTipomoneda());
			if (cuota.getTipomoneda().equals("D") && valorTipoCambio>0 ) {
				cuotaBean.setMontodolar(cuota.getMonto());
				cuotaBean.setMonto(FuncionesHelper.redondearNum(cuota.getMonto()*valorTipoCambio, 2));
			}else{
				cuotaBean.setMonto(FuncionesHelper.redondearNum(cuota.getMonto(),2));
			}
			
			//System.out.println(cuotaBean.getClaveupa());
			if(cuota.getRentapagada()!=null){
			cuotaBean.setRentapagada(FuncionesHelper.redondearNum(cuota.getRentapagada(),2));
     		cuotaBean.setDeudaacuenta(FuncionesHelper.redondearNum(cuota.getRentapagada(),2));
			}
			cuotaBean.setMoraacumulada(cuota.getMoraacumulada());
			cuotaBean.setMora(0.0);
			cuotaBean.setMontopenalizacion(0.0);
			i++;
			
			listacuotabean.add(cuotaBean);
		}
		return listacondicion;
	}
	@Override
	public List<CuotaBean> generarRentasPendientesParaCobroReconocimientoDeuda(int idcontrato, Double valorTipoCambio) {
		CondicionBean condicionBean= new CondicionBean();
		condicionBean=contratoDAO.obtenerCondicionBean(idcontrato);
		List<CuotaBean> listaCuotaBean,listacuotadetalle;
		List<CuotaBean> listadetallecuota= new ArrayList<CuotaBean>();
		List<MaeDetCondicionDetalleBean> listamaecondicion;
		listaCuotaBean=carteraDAO.generarPendientesParaCobroReconocimientoDeuda(idcontrato,valorTipoCambio);
		
		
		int i=0;
		
		for (CuotaBean cuotaBean : listaCuotaBean) {
			
			cuotaBean.setClaveupa(cuotaBean.getClave());
			cuotaBean.setNrocuota(i+1);
			cuotaBean.setAnio(cuotaBean.getAnio());
			cuotaBean.setMes(cuotaBean.getMes());
			cuotaBean.setSipagado("0");
			
			cuotaBean.setCondicion("Pendiente");
			//cuotaBean.setNropagos(cuotaBean.getNropagos()!=null?cuotaBean.getNropagos():0);
			//cuotaBean.setSiacuenta(cuotaBean.getNropagos()>=0?Boolean.TRUE:Boolean.FALSE);
			if(cuotaBean.getNropagos()==null){
				cuotaBean.setNropagos(0);
				cuotaBean.setSiacuenta(cuotaBean.getNropagos()>0?Boolean.TRUE:Boolean.FALSE);
			}else{
				cuotaBean.setSiacuenta(cuotaBean.getNropagos()>=0?Boolean.TRUE:Boolean.FALSE);
			}
			cuotaBean.setIdcuota(cuotaBean.getIdcuota());
			if (condicionBean.getMoneda().equals("D") && valorTipoCambio>0 ) {
				cuotaBean.setMontodolar(cuotaBean.getMonto());
				cuotaBean.setMonto(FuncionesHelper.redondearNum(cuotaBean.getMonto()*valorTipoCambio, 2));
			}else{
				cuotaBean.setMonto(FuncionesHelper.redondearNum(cuotaBean.getMonto(),2));
			}
			
			//System.out.println(cuotaBean.getClaveupa());
			if(cuotaBean.getRentapagada()!=null){
			cuotaBean.setRentapagada(FuncionesHelper.redondearNum(cuotaBean.getRentapagada(),2));
     		cuotaBean.setDeudaacuenta(FuncionesHelper.redondearNum(cuotaBean.getRentapagada(),2));
			}
			cuotaBean.setMoraacumulada(cuotaBean.getMoraacumulada());
			cuotaBean.setMora(0.0);
			cuotaBean.setMontopenalizacion(0.0);
			listamaecondicion= new ArrayList<MaeDetCondicionDetalleBean>();
			listacuotadetalle= new ArrayList<CuotaBean>();
			
			//listamaecondicion=carteraDAO.generarPendientesDetalleCuotaReconocimientoDeuda(cuotaBean.getIddetallecondicion());
			//listacuotadetalle=cartaraDao.
			listacuotadetalle=carteraDAO.generarPendientesCuotasDetalleReconocimientoDeuda(cuotaBean.getIddetallecondicion(), valorTipoCambio);
               //
			List<CuotaBean> listadetallecuotabean= new ArrayList<CuotaBean>();
			
			int j=0;
			for (CuotaBean cuota : listacuotadetalle) {
				CuotaBean cuoBean = new CuotaBean();
				cuoBean.setClaveupa(cuota.getClaveupa());
				cuoBean.setNrocuota(j+1);
				cuoBean.setAnio(cuota.getAnio());
				cuoBean.setMes(cuota.getMes());
				cuoBean.setSipagado("0");			
				cuoBean.setCondicion("Pendiente");
				cuoBean.setNropagos(cuota.getNropagos()!=null?cuota.getNropagos():0);
				cuoBean.setSiacuenta(cuoBean.getNropagos()>0?Boolean.TRUE:Boolean.FALSE);
				cuoBean.setIdcontrato(cuota.getIdcontrato());
				//cuotaBean.setIddetallecondicion(cuota.getId_maedetallecondicion());
				cuoBean.setId_maedetallecondicion(cuota.getId_maedetallecondicion());
				cuoBean.setId_maedcondiciondetalle(cuota.getId_maedcondiciondetalle());
				cuoBean.setIdcuota(cuota.getIdcuota());
				cuoBean.setMoneda(cuota.getTipomoneda());
				cuoBean.setConcepto(cuota.getConcepto());
				if (cuota.getTipomoneda().equals("D") && valorTipoCambio>0 ) {
					cuoBean.setMontodolar(cuota.getMonto());
					cuoBean.setMonto(FuncionesHelper.redondearNum(cuota.getMonto()*valorTipoCambio, 2));
				}else{
					cuoBean.setMonto(FuncionesHelper.redondearNum(cuota.getMonto(),2));
				}
				
				//System.out.println(cuotaBean.getClaveupa());
				if(cuota.getRentapagada()!=null){
					cuoBean.setRentapagada(FuncionesHelper.redondearNum(cuota.getRentapagada(),2));
					cuoBean.setDeudaacuenta(FuncionesHelper.redondearNum(cuota.getRentapagada(),2));
				}
				cuoBean.setMoraacumulada(cuota.getMoraacumulada());
				cuoBean.setMora(0.0);
				cuoBean.setMontopenalizacion(0.0);
				j++;
				
				listadetallecuotabean.add(cuoBean);
			}
//			if (condicionBean.getMoneda().equals("D") && valorTipoCambio>0 ) {
//				for(MaeDetCondicionDetalleBean maedet:  listamaecondicion){
//					maedet.setMontodolar(maedet.getMonto());
//					maedet.setMonto(FuncionesHelper.redondear(maedet.getMonto()*valorTipoCambio, 2));
//				}
//				
//			}
			//cuotaBean.setMaecondiciondetalle(listamaecondicion);
			cuotaBean.setListacuota(listadetallecuotabean);
			i++;
			
			
		}
		
		
		return listaCuotaBean;
	}
	
	@Override
	public List<CuotaBean> generarRentasPendientesNuevoContrato(int idcontrato, Double valorTipoCambio) {
		CondicionBean condicionBean= new CondicionBean();
		condicionBean=contratoDAO.obtenerCondicionBean(idcontrato);
		List<CuotaBean> listaCuotaBean= new ArrayList<CuotaBean>();
		listaCuotaBean=carteraDAO.generarPendientesParaCobroNuevoContrato(idcontrato,valorTipoCambio);
		
		int i=0;
		
		for (CuotaBean cuotaBean : listaCuotaBean) {
			
			cuotaBean.setClaveupa(cuotaBean.getClave());
			cuotaBean.setNrocuota(i+1);
			cuotaBean.setAnio(cuotaBean.getAnio());
			cuotaBean.setMes(cuotaBean.getMes());
			cuotaBean.setSipagado("0");
			
			cuotaBean.setCondicion("Pendiente");
			cuotaBean.setNropagos(cuotaBean.getNropagos()!=null?cuotaBean.getNropagos():0);
			cuotaBean.setSiacuenta(cuotaBean.getNropagos()>0?Boolean.TRUE:Boolean.FALSE);
			cuotaBean.setIdcontrato(idcontrato);
			cuotaBean.setId_maedetallecondicion(cuotaBean.getIddetallecondicion());
			
			cuotaBean.setIdcuota(cuotaBean.getIdcuota());
			if (condicionBean.getMoneda().equals("D") && valorTipoCambio>0 ) {
				cuotaBean.setMontodolar(cuotaBean.getMonto());
				cuotaBean.setMonto(FuncionesHelper.redondear(cuotaBean.getMonto()*valorTipoCambio, 2));
			}else{
				cuotaBean.setMonto(FuncionesHelper.redondear(cuotaBean.getMonto(),2));
			}
			
			//System.out.println(cuotaBean.getClaveupa());
			if(cuotaBean.getRentapagada()!=null){
			cuotaBean.setRentapagada(FuncionesHelper.redondear(cuotaBean.getRentapagada(),2));
     		cuotaBean.setDeudaacuenta(FuncionesHelper.redondear(cuotaBean.getRentapagada(),2));
			}
			cuotaBean.setMoraacumulada(cuotaBean.getMoraacumulada());
			cuotaBean.setMora(0.0);
			cuotaBean.setMontopenalizacion(0.0);
			i++;
			
			
		}
		
		
		return listaCuotaBean;
	}
	public List<RentaBean> obtenerRentasNuevoMantenimientoContrato(int idcontrato) {

		return maeDetallecondicionDao.obtenerDetalleCondicion(idcontrato);
	}
	public List<RentaBean> obtenerCuotasMantenimientoReconocimietoDeuda(int idcontrato) {

		return maeDetallecondicionDao.obtenerDetalleCondicion(idcontrato);
	}
	public List<RentaBean> obtenerPeriodosPagados(int idcontrato) {
		
		return carteraDAO.obtenerPeriodosPagados(idcontrato);
	}
	public List<RentaBean> obtenerRentasPagadosxContrato(int idcontrato , String condicion ,Boolean condMaeDetalle){
		List<RentaBean> listaRentaBean  = new ArrayList<RentaBean>();
		List<RentaBean> listaRenta  = new ArrayList<RentaBean>();
         listaRentaBean = carteraDAO.listarRentasPagadasxContrato(idcontrato, condicion, condMaeDetalle);
		 List<DetalleCuotaBean>  listadetallecuota= new ArrayList<DetalleCuotaBean>();
		 if(!condMaeDetalle){
			 listaRenta = listaRentasCondicion(idcontrato,Constante.CONDICION_CONSULTA_REPORTE_PAGADOS);//obtiene las rentas del contrato
			 listaRentaBean=setearListaRentaBean(listaRentaBean,listaRenta);
		 }
		 Iterator<RentaBean> iter= listaRentaBean.iterator();		 
		 while (iter.hasNext()) {
				RentaBean renta = iter.next();
				listadetallecuota=null;
				listadetallecuota=obtenerDetalleRentasPagadas(renta.getId());
				if(listadetallecuota.size()>0){
					renta.setDetalleCuotaBean(listadetallecuota );
				}else{
					iter.remove();
				}
		 }
		
		return listaRentaBean;
	}
	public List<RentaBean> obtenerRentasPendientesxContrato(int idcontrato , String condicion , Boolean condMaeDetalle){
		List<RentaBean> listaRentaBean  = new ArrayList<RentaBean>();
		List<RentaBean> listaRenta  = new ArrayList<RentaBean>();
		if(condMaeDetalle){
			listaRentaBean=carteraDAO.listarRentasPendientesxContrato(idcontrato, condicion, condMaeDetalle);
			listaRentaBean=setearListaRenta(listaRentaBean);
		}else{
			//sin mantenimiento 
			// obtener lista de pagados 
			listaRentaBean=carteraDAO.listarRentasPagadasxContrato(idcontrato, condicion, condMaeDetalle);
			listaRenta = listaRentasCondicion(idcontrato,Constante.CONDICION_CONSULTA_REPORTE_PENDIENTE);//obtiene las rentas del contrato
			listaRentaBean=setearlistaRenta(listaRentaBean, listaRenta,true);
			listaRentaBean.removeAll(Collections.singleton(null));
			listaRentaBean=setearListaRenta(listaRentaBean);
			
		}
		return listaRentaBean;
	}
	public List<RentaBean> obtenerRentasPagadosPendientesxContrato(int idcontrato , String condicion , Boolean condMaeDetalle){
		List<RentaBean> listaRentaBean  = new ArrayList<RentaBean>();
		List<RentaBean> listaRenta  = new ArrayList<RentaBean>();
		if(condMaeDetalle){
			listaRentaBean=carteraDAO.listarRentasPendientesxContrato(idcontrato,Constante.OPCION_REPORTE_CANCELADOS_PENDIENTES, condMaeDetalle);
			//listaRentaBean=setearListaRenta(listaRentaBean);
		}else{
			//sin mantenimiento 
			// obtener lista de pagados 
			listaRentaBean=carteraDAO.listarRentasPagadasxContrato(idcontrato, condicion, condMaeDetalle);
			listaRenta = listaRentasCondicion(idcontrato,Constante.CONDICION_CONSULTA_REPORTE_PENDIENTE);//obtiene las rentas del contrato
			listaRentaBean=setearlistaRenta(listaRentaBean, listaRenta,false);
			listaRentaBean.removeAll(Collections.singleton(null));
			//listaRentaBean=setearListaRenta(listaRentaBean);
			
		}
		return listaRentaBean;
	}
	public List<RentaBean> setearListaRenta(List<RentaBean> listaRenta){
		Iterator<RentaBean> iter= listaRenta.iterator();
		int i=0;
		 while (iter.hasNext()) {
				RentaBean renta = iter.next();
				if(renta.equals(null)){
					iter.remove();
				}else{
					if(renta.getMontopagar()==0.0){
						iter.remove();
					}
				}
				i++;
		}
		 return listaRenta;
	}
	public Date getFechaVencimientoContrato(String dia , String mes , String anio){
		String fecha= dia+"/"+Almanaque.mesanumero(mes)+"/"+anio;		
		return FuncionesHelper.convertirCadenaFecha(fecha);
	}
	public List<RentaBean> setearlistaRenta(List<RentaBean> listaRenta ,List<RentaBean> lista ,Boolean condicion){
		 Iterator<RentaBean> iter= lista.iterator();
		 
		 while (iter.hasNext()) {
				RentaBean renta = iter.next();
				for(RentaBean re : listaRenta){
					if(re.getFechavencimiento()!=null){
						renta.setFechavencimiento(re.getFechavencimiento());
						renta.setNrodias_adeudo(re.getNrodias_adeudo());
						renta.setNrodias_adeudo_penalidad(re.getNrodias_adeudo_penalidad()==null? 0 :re.getNrodias_adeudo_penalidad() );
					}
					if(re.getAnio().trim().equalsIgnoreCase(renta.getAnio()) && re.getMes().trim().equalsIgnoreCase(renta.getMes())){
							renta.setMontopagado(re.getMontopagar());
							renta.setMontopagar(FuncionesHelper.redondearNum(  renta.getRenta()-re.getMontopagar(),2));
							if(condicion){
								if(renta.getMontopagar()==0.0){
									iter.remove();
								}
							}
							
					}
					}
		}
		 
		 return lista;
		 
	}
    public List<RentaBean> setearListaRentaBean(List<RentaBean> listaRenta ,List<RentaBean> lista ){
    	Iterator<RentaBean> iter = listaRenta.iterator();
    	while(iter.hasNext()){
    		RentaBean re= iter.next();
    		for(RentaBean renta:lista){
    			if(re.getAnio().trim().equalsIgnoreCase(renta.getAnio()) && re.getMes().trim().equalsIgnoreCase(renta.getMes())){
    				re.setRenta(renta.getRenta());
    			}
    		}
    	}
    	
    	return listaRenta;
    }
	public List<RentaBean> listaRentasCondicion(Integer idcontrato , String condicion){
		System.out.println("metodo rentas condicon=");
		List<RentaBean> listaRentas= new ArrayList<RentaBean>();
		RentaBean renta;
		Contrato contrato =contratoDAO.obtenerContratoPorID(idcontrato);
		Calendar iniciocobro = Calendar.getInstance();
		Calendar fincobro = Calendar.getInstance();
		if(contrato.getEstado().equalsIgnoreCase(Constante.CONDICION_CONTRATO_FINALIZADO)){
			iniciocobro.setTime(contrato.getIniciocobro());
			if(!contratoDAO.existeUltimaCondicionVigenteContrato(idcontrato) && condicion.equalsIgnoreCase(Constante.CONDICION_CONSULTA_REPORTE_LIQUIDACION_DEUDA)) {
				fincobro = new GregorianCalendar();
				fincobro.set(Calendar.DATE, fincobro.getActualMaximum(Calendar.DAY_OF_MONTH));
					
			}else{
				fincobro.setTime(contrato.getFincobro()!=null ? contrato.getFincobro() :fincobro.getTime());
				fincobro.set(Calendar.DATE, fincobro.getActualMaximum(Calendar.DAY_OF_MONTH));
				
			}
			
		}else{
			iniciocobro.setTime(contrato.getIniciocobro());			
			fincobro.setTime(contrato.getFincobro()!=null ? contrato.getFincobro() :fincobro.getTime());
			fincobro.set(Calendar.DATE, fincobro.getActualMaximum(Calendar.DAY_OF_MONTH));
			
		}
		
		
		int i=1;
		if(contrato.getCondicion().equalsIgnoreCase(Constante.CONDICION_SINCONTRATO)){
		while (iniciocobro.getTime().compareTo(fincobro.getTime()) < 0) 
		{
			renta= new RentaBean();
			renta.setNrocuota(Integer.toString( i));
			renta.setAnio((Integer.toString(iniciocobro.get(Calendar.YEAR))).trim());
			renta.setMes(Almanaque.obtenerNombreMes(iniciocobro.get(Calendar.MONTH)).trim());
			renta.setSipagado("0");
			renta.setRenta(FuncionesHelper.redondearNum(contrato.getMontocuotasoles(), 2)); 
			renta.setMontopagado(0.0);
			renta.setMontopagar(renta.getRenta());
			renta.setSecuencia(i);
            renta.setTipomoneda(contrato.getTipomoneda());
            renta.setContraprestacion(FuncionesHelper.redondearNum(contrato.getMontocuotasoles(), 2));
            if(contrato.getFechapago() !=null){
		    	renta.setFechavencimiento(getFechaVencimientoContrato(Integer.toString(contrato.getFechapago().getId_fechapago()), renta.getMes(), renta.getAnio()));
		    	renta.setNrodias_adeudo((int)FuncionesHelper.diasEntreDosFechas(renta.getFechavencimiento(), new Date()));
		    	if(renta.getFechavencimiento().after(contrato.getFincobro()) ){
		    		renta.setNrodias_adeudo_penalidad((int)FuncionesHelper.diasEntreDosFechas(contrato.getFincontrato(), new Date()));	
		    	}else{
		    		renta.setNrodias_adeudo_penalidad(0);
		    	}
		    }
			listaRentas.add(renta);
	    	
	    	iniciocobro.add(Calendar.MONTH, 1);
	    	i++;
		}
		}else{
			while (iniciocobro.compareTo(fincobro) <= 0) 
			{

				renta= new RentaBean();
				renta.setNrocuota(Integer.toString( i));
				renta.setAnio(Integer.toString(iniciocobro.get(Calendar.YEAR)).trim());
				renta.setMes(Almanaque.obtenerNombreMes(iniciocobro.get(Calendar.MONTH)).trim());
			    renta.setMontopagado(0.0);
			    renta.setTipomoneda(contrato.getTipomoneda());
			    renta.setContraprestacion(FuncionesHelper.redondearNum(contrato.getMontocuotasoles(), 2));
			    if(contrato.getFechapago() !=null){
			    	renta.setFechavencimiento(getFechaVencimientoContrato(Integer.toString(contrato.getFechapago().getId_fechapago()), renta.getMes(), renta.getAnio()));
			    	renta.setNrodias_adeudo((int)FuncionesHelper.diasEntreDosFechas(renta.getFechavencimiento(), new Date()));
			    	if(renta.getFechavencimiento().after(contrato.getFincobro()) ){
			    		renta.setNrodias_adeudo_penalidad((int)FuncionesHelper.diasEntreDosFechas(contrato.getFincontrato(), new Date()));	
			    	}else{
			    		renta.setNrodias_adeudo_penalidad(0);
			    	}
			    }
			    

				if (i<=12) 
				{ 
					renta.setRenta(contrato.getMontocuotasoles() );

				}
				else 
					if (i<=24) 
					{
						renta.setRenta(contrato.getMontocuotasoles2()!=null? contrato.getMontocuotasoles2(): contrato.getMontocuotasoles());
					}
					else 
						if (i<=36) 
						{
							renta.setRenta(contrato.getMontocuotasoles3()!=null? contrato.getMontocuotasoles3(): (contrato.getMontocuotasoles2()!=null? contrato.getMontocuotasoles2(): contrato.getMontocuotasoles()));
						}
						else 
							if (i<=48) 
							{
								renta.setRenta(contrato.getMontocuotasoles4()!= null? contrato.getMontocuotasoles4() :(contrato.getMontocuotasoles3()!=null? contrato.getMontocuotasoles3(): (contrato.getMontocuotasoles2()!=null? contrato.getMontocuotasoles2(): contrato.getMontocuotasoles())));
							}
							else 
								if (i<=60) 
								{
									renta.setRenta(contrato.getMontocuotasoles5()!=null? contrato.getMontocuotasoles5() :(contrato.getMontocuotasoles4()!= null? contrato.getMontocuotasoles4() :(contrato.getMontocuotasoles3()!=null? contrato.getMontocuotasoles3(): (contrato.getMontocuotasoles2()!=null? contrato.getMontocuotasoles2(): contrato.getMontocuotasoles()))));
								}
								else 
								{
									renta.setRenta(contrato.getMontocuotasoles6()!=null? contrato.getMontocuotasoles6() : (contrato.getMontocuotasoles5()!=null? contrato.getMontocuotasoles5() :(contrato.getMontocuotasoles4()!= null? contrato.getMontocuotasoles4() :(contrato.getMontocuotasoles3()!=null? contrato.getMontocuotasoles3(): (contrato.getMontocuotasoles2()!=null? contrato.getMontocuotasoles2(): contrato.getMontocuotasoles())))));
								}
				
				renta.setMontopagar(renta.getRenta());
			    listaRentas.add(renta);
			    iniciocobro.add(Calendar.MONTH, 1);
			    i++;

			}
		}
		return listaRentas;
		
	}
	
    public List<DetalleCuotaBean> obtenerDetalleRentasPagadas(int id){
    	return carteraDAO.listarDetalleCuotasxRenta(id);
    }
	@Override
	public List<RentaBean> obtenerPeriodosPendientes(int idcontrato) {
		
		return carteraDAO.obtenerPeriodosPendientes(idcontrato);
	}
	@Override
	public List<RentaBean> obtenerPeriodosPendientesContrato(int idcontrato) {
		
		return carteraDAO.obtenerPeriodosPendientesContrato(idcontrato);
	}
	@Override
	public List<CuotaBean> obtenerPeriodosPendientesDeuda(int idcontrato) {
		
		return carteraDAO.obtenerPeriodosPendientesDeuda(idcontrato);
	}
	
	@Override
	public List<CuotaBean> obtenerPeriodosPendientesContratoDeuda(int idcontrato) {
		
		return carteraDAO.obtenerPeriodosPendientesContratoDeuda(idcontrato);
	}
	
	
	
	@Override
	public List<CuotaArbitrioBean> obtenerInformacionPagosPendientes(
			int idcondicion, List<PagoArbitrioBean> listaPagos,String condicion, String tipo,
			Double tipocambio) {
		
		List<CuotaArbitrioBean> listapendientes=new ArrayList<CuotaArbitrioBean>();
		List<Cuota_arbitrio> listapagados=new ArrayList<Cuota_arbitrio>();
		listapagados=listarcuotasxarbitrio(idcondicion);
		listapendientes.clear();
		CuotaArbitrioBean cuota;
		//lista de pagadas
        
		for(int i=0;i<listaPagos.size();i++){
			cuota=new CuotaArbitrioBean();
			cuota.setNrocuota(i+1);
			cuota.setAnio(Integer.parseInt(listaPagos.get(i).getAnio()));
			cuota.setMes(listaPagos.get(i).getMes());
//			cuota.setPeriodo(listaPagos.get(i).getPeriodo());
			cuota.setSipagado("0");
			cuota.setCondicion("Pendiente");
			cuota.setNropagos(1);
			cuota.setRentapagada(0.0);
			cuota.setMora(0.0);
			cuota.setNuevamc(0.0);
			cuota.setNuevarenta(0.0);
			cuota.setMonto(listaPagos.get(i).getMontopagar());
			listapendientes.add(cuota);
		}
		if(listapendientes!=null){
		 for(int j=0;j<listapendientes.size();j++){
			 for(int k=0;k<listapagados.size();k++){
				 if(listapendientes.get(j).getMes().equals(listapagados.get(k).getMes())&& listapendientes.get(j).getAnio()==listapagados.get(k).getPeriodo()){
					 if(listapagados.get(k).getCancelado().equals("1")){
						 listapendientes.get(j).setSipagado("1");
						 listapendientes.get(j).setMonto(0.0);
						 listapendientes.get(j).setRentapagada(listapendientes.get(j).getRenta());
					 }else{
						 listapendientes.get(j).setIdcuota(listapagados.get(k).getIdcuota_arbitrio());
						 listapendientes.get(j).setSiacuenta(true);
//						 listapendientes.get(j).setNropagos(listapagados.get(k).getNropagos());
//						 listapendientes.get(j).setMoraacumulada(listapagados.get(k).getMorasoles());
						 listapendientes.get(j).setDeudaacuenta(FuncionesHelper.redondearNum(listapagados.get(k).getMonto(), 2));
						 listapendientes.get(j).setMonto(FuncionesHelper.redondearNum(listapendientes.get(j).getMonto()-listapagados.get(k).getMonto(), 2));
						 listapendientes.get(j).setCondicion(listapagados.get(k).getCancelado().equals("0")? "Pendiente":"Generado");
					 }
				 }
			 }
			 
		 }
		 if(tipo.equals("pendiente")){
			 return seleccionarArbitrioPendientes(listapendientes);
		 }
		}
		return listapendientes;
	}
	public List<CuotaArbitrioBean> seleccionarArbitrioPendientes(List<CuotaArbitrioBean> listaCuotasPendientesxCondicion){
		List<CuotaArbitrioBean> lista=new ArrayList<CuotaArbitrioBean>();
		for(int i=0;i<listaCuotasPendientesxCondicion.size();i++){
			if(listaCuotasPendientesxCondicion.get(i).getSipagado().equals("0")){
				lista.add(listaCuotasPendientesxCondicion.get(i));
			}
		}
		listaCuotasPendientesxCondicion.clear();
		listaCuotasPendientesxCondicion.addAll(lista);
		return listaCuotasPendientesxCondicion;
	}

	@Override
	public List<CuotaArbitrioBean> obtenerInformacionPagosPendientes(
			ArbitrioBean arbitrioBean, Double tipocambio,List<PeriodoPagadoBean> listacuotaspagadas) {
		List<CuotaArbitrioBean> listapendientes =new ArrayList<CuotaArbitrioBean>();
		int periodo=Constante.NUMERO_PERIODO;
		int cont=0;
		List<Cuota_arbitrio> listapagados=new ArrayList<Cuota_arbitrio>();
		listapagados=listarcuotasxarbitrio(arbitrioBean.getIdarbitrio());
		CuotaArbitrioBean cuota;
		
		for(int i=0;i<periodo;i++){
			cuota=new CuotaArbitrioBean();
			cuota.setNrocuota(i+1);
			cuota.setAnio(arbitrioBean.getAnio());
			cuota.setMes(Almanaque.obtenerNombreMes(cont).substring(0, 3)+"/"+Almanaque.obtenerNombreMes(cont+1).substring(0, 3)+"/"+Almanaque.obtenerNombreMes(cont+2).substring(0, 3));
//			cuota.setPeriodo(arbitrioBean.getAnio()+"-"+String.format("%02d",i+1));
			cuota.setSipagado("0");
			cuota.setCondicion("Pendiente");
			cuota.setNropagos(1);
			cuota.setRentapagada(0.0);
			cuota.setMora(0.0);
			cuota.setNuevamc(0.0);
			cuota.setNuevarenta(0.0);
			cuota.setMonto(FuncionesHelper.redondearNum(arbitrioBean.getMonto() / 4.0, 2));
			if (i == 0) {
				if (FuncionesHelper.redondearNum(arbitrioBean.getMonto() / 4.0, 2) * 4 != arbitrioBean.getMonto()) {
					double montopagar = 0.0;
					montopagar = arbitrioBean.getMonto()- FuncionesHelper.redondearNum(arbitrioBean.getMonto() / 4.0, 2) * 3;
					cuota.setMonto(FuncionesHelper.redondearNum(montopagar, 2));
				}
			}
			
			listapendientes.add(cuota);
			cont=cont+3;
		}
		if(listapagados!=null){
		 for(int j=0;j<listapendientes.size();j++){
			 for(int k=0;k<listapagados.size();k++){
				 if(listapendientes.get(j).getMes().equals(listapagados.get(k).getMes())&& listapendientes.get(j).getAnio()==listapagados.get(k).getPeriodo()){
					 if(listapagados.get(k).getCancelado().equals("1")){
						 listapendientes.get(j).setSipagado("1");
						 listapendientes.get(j).setMonto(0.0);
						 listapendientes.get(j).setRentapagada(listapendientes.get(j).getRenta());
					 }else{
						 listapendientes.get(j).setIdcuota(listapagados.get(k).getIdcuota_arbitrio());
						 listapendientes.get(j).setSiacuenta(true);
//						 listapendientes.get(j).setNropagos(listapagados.get(k).getNropagos());
//						 listapendientes.get(j).setMoraacumulada(listapagados.get(k).getMorasoles());
						 listapendientes.get(j).setDeudaacuenta(FuncionesHelper.redondearNum(listapagados.get(k).getMonto(), 2));
						 listapendientes.get(j).setMonto(FuncionesHelper.redondearNum(listapendientes.get(j).getMonto()-listapagados.get(k).getMonto(), 2));
						 listapendientes.get(j).setCondicion(listapagados.get(k).getCancelado().equals("0")? "Pendiente":"Generado");
					 }
				 }
			 }
			 
		 }
	}
		 
     return seleccionarArbitrioPendientes(listapendientes);
		 

	}

	@Override
	public List<CuotaArbitrioBean> obtenerPeriodosPendientesxMes(int idarbitrio) {
		
		return carteraDAO.obtenerPeriodosPendientesxMes(idarbitrio);
	}
	@Override
	public List<CuotaArbitrioBean> obtenerDetalleCuotaPeriodosPendientesxMes(int idarbitrio) {
		
		return carteraDAO.obtenerDetalleCuotaPeriodosPendientesxMes(idarbitrio);
	}
	@Override
	public List<CuotaArbitrioBean> obtenerDetalleCuotaPeriodosPagadosxMes(int idarbitrio) {
		
		return carteraDAO.obtenerDetalleCuotaPeriodosPagadosxMes(idarbitrio);
	}
	@Override
	public List<PeriodoContratoBean> obtenerPeriodoxContrato(int idcontrato) {
		
		return carteraDAO.obtenerPeriodoxContrato(idcontrato);
	}
	
	@Transactional(readOnly = false)
	public List<Sunat_Comprobante> obtenerListaSunatComprobantePendientes() {
		return carteraDAO.obtenerListaSunatComprobantePendientes();
	}
	
	@Override
	public List<RentaBean> listarRentaPendientesxContrato(int idcontrato, String condicion, Boolean condMaeDetalle) {
		List<RentaBean> listaRentaBean  = new ArrayList<RentaBean>();
		List<RentaBean> listaRenta  = new ArrayList<RentaBean>();
		if(condMaeDetalle){
			listaRentaBean=carteraDAO.listarRentaPendientesxContrato(idcontrato, condicion, condMaeDetalle);
			//listaRentaBean=setearListaRenta(listaRentaBean);
		}else{
			//sin mantenimiento 
			// obtener lista de pagados 
			listaRentaBean=carteraDAO.listarRentasPagadasxContrato(idcontrato, condicion, condMaeDetalle);
			listaRenta = listaRentasCondicion(idcontrato,Constante.CONDICION_CONSULTA_REPORTE_LIQUIDACION_DEUDA);//obtiene las rentas del contrato
			listaRentaBean=setearlistaRenta(listaRentaBean, listaRenta,false);
			listaRentaBean.removeAll(Collections.singleton(null));
			listaRentaBean=setearListaRenta(listaRentaBean);
			
		}
		
		
		
		return listaRentaBean;
	}
	
	public IRecaudacionReporteService getRecaudacionReporteService() {
		return recaudacionReporteService;
	}

	public void setRecaudacionReporteService(
			IRecaudacionReporteService recaudacionReporteService) {
		this.recaudacionReporteService = recaudacionReporteService;
	}

	public List<RentaBean> getListaRentas() {
		return listaRentas;
	}

	public void setListaRentas(List<RentaBean> listaRentas) {
		this.listaRentas = listaRentas;
	}

	public IRecaudacionReporteService getReporteRecaudacionService() {
		return reporteRecaudacionService;
	}

	public void setReporteRecaudacionService(
			IRecaudacionReporteService reporteRecaudacionService) {
		this.reporteRecaudacionService = reporteRecaudacionService;
	}

	@Override
	public boolean siRentasPagadasxContrato(int idcontrato) {
		// TODO Auto-generated method stub
		return carteraDAO.siRentasPagadasxContrato(idcontrato);
	}
	@Override
	public RentaBean ultimaRentaPagada(int idcontrato) {
		// TODO Auto-generated method stub
		return carteraDAO.ultimaRentaPagada(idcontrato);
	}




}
