package pe.gob.sblm.sgi.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.chart.BubbleChartModel;
import org.primefaces.model.chart.BubbleChartSeries;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;

import pe.gob.sblm.api.commons.utility.FuncionesHelper;
import pe.gob.sblm.sgi.bean.ItemBean;
import pe.gob.sblm.sgi.entity.Usuario;
import pe.gob.sblm.sgi.service.IContratoService;
import pe.gob.sblm.sgi.service.IFHvariadoService;
import pe.gob.sblm.sgi.service.IRecaudacionCarteraService;
import pe.gob.sblm.sgi.service.IRecaudacionCobranzaService;
import pe.gob.sblm.sgi.service.ITipoCambioService;
import pe.gob.sblm.sgi.util.Almanaque;

@ManagedBean(name = "cobranzagraficaMB")
@ViewScoped
public class CobranzaGraficaManagedBean extends BaseController implements Serializable {
	
	
	private CartesianChartModel model;
	private CartesianChartModel model1;
	private CartesianChartModel model2;
	private CartesianChartModel model3;
	private BubbleChartModel model4;
	private CartesianChartModel model5;
	private CartesianChartModel model6;	
	
	/**Grafica especifica de recaudacion de renta cobradores por mes**/
	private String mesRecauCobraxMes=String.valueOf(Almanaque.obtenerNombreMes(Almanaque.getNumeroMes(new Date())));
	private String anioRecauCobraxMes=String.valueOf(Almanaque.getNumeroAnio(new Date()));
	
	/**Grafica especifica de recaudacion de mora de cobradores por mes**/
	private String mesRecauMoraCobraxMes=String.valueOf(Almanaque.obtenerNombreMes(Almanaque.getNumeroMes(new Date())));
	private String anioRecauMoraCobraxMes=String.valueOf(Almanaque.getNumeroAnio(new Date()));
	
	/**Grafica especifica de recaudacion de todos los meses por anio**/
	private String anioRecauMesesxAnio=String.valueOf(Almanaque.getNumeroAnio(new Date()));
	
	
	/**Grafica especifica de recaudacion de cobrador por anio **/
	private String anioRecauCobraxAnio=String.valueOf(Almanaque.getNumeroAnio(new Date()));
	private List<ItemBean> listaCobrador= new ArrayList<ItemBean>();
	
	/**Grafica especifica de recaudacion de cobrador por mes y tamanio de cartera **/
	private String mesRecauCobraxMesVsTamanioCartera=String.valueOf(Almanaque.getNumeroMes(new Date()));
	private String anioRecauCobraxMesVsTamanioCartera=String.valueOf(Almanaque.getNumeroAnio(new Date()));
	
	
	private ItemBean selectedCobrador= new ItemBean();
	private int idselectedCobrador;
	
	
	
	@ManagedProperty(value = "#{usuarioMB}")
	UsuarioManagedBean userMB;
	
	@ManagedProperty(value = "#{contratoService}")
	private transient IContratoService contratoService;
	
	
	@ManagedProperty(value = "#{cobranzaRecaudacionService}")
	private transient IRecaudacionCobranzaService cobranzaRecaudacionService;
	
	@ManagedProperty(value = "#{carteraService}")
	private transient IRecaudacionCarteraService carteraService;
	
	@ManagedProperty(value = "#{tipocambioService}")
	private transient ITipoCambioService tipocambioService;
	
	@ManagedProperty(value = "#{FHvariadoService}")
	private transient  IFHvariadoService FHvariadoService;
	
	
	@PostConstruct
	public void init(){
		
		listaCobrador=carteraService.listarCobradores();
		idselectedCobrador=99999;
		generarGrafica();
		generarGrafica2();
		generarGrafica3();
		generarGrafica5();
		
		
	}
	
	
	public void generarGrafica() {
		model = new CartesianChartModel();
		model1 = new CartesianChartModel();
		List<Usuario> listaCobradores= new ArrayList<Usuario>();
		listaCobradores= FHvariadoService.obtenerCobradores();
		
		for (Usuario usuario : listaCobradores) {
			 ChartSeries cobradortotalrecaudado = new ChartSeries();
			 ChartSeries cobradornroupasrecaudado = new ChartSeries();
			 
			 cobradortotalrecaudado.setLabel(usuario.getNombrescompletos());
			 	Calendar desde = Calendar.getInstance();
				Calendar hasta= Calendar.getInstance();
				
				desde.set(Calendar.YEAR,Integer.parseInt(anioRecauCobraxMes));
				desde.set(Calendar.MONTH, Integer.parseInt(Almanaque.mesanumero(mesRecauCobraxMes))-1);
				desde.set(Calendar.DATE, 1);
				desde.set(Calendar.HOUR,0);
				desde.set(Calendar.MINUTE,0);
				desde.set(Calendar.SECOND,0);
				desde.set(Calendar.MILLISECOND,0);
				
				hasta.set(Calendar.YEAR,Integer.parseInt(anioRecauCobraxMes));
				hasta.set(Calendar.MONTH, Integer.parseInt(Almanaque.mesanumero(mesRecauCobraxMes))-1);
				hasta.set(Calendar.DATE, desde.getActualMaximum(Calendar.DAY_OF_MONTH));
				hasta.set(Calendar.HOUR,0);
				hasta.set(Calendar.MINUTE,0);
				hasta.set(Calendar.SECOND,0);
				hasta.set(Calendar.MILLISECOND,0);
			 	
				cobradortotalrecaudado.set(mesRecauCobraxMes, cobranzaRecaudacionService.sumarComprobanteCobrador(usuario.getIdusuario(), FuncionesHelper.fechaToString(desde.getTime()), FuncionesHelper.fechaToString(hasta.getTime()),Boolean.FALSE,Boolean.FALSE));
				cobradornroupasrecaudado.set(mesRecauCobraxMes, cobranzaRecaudacionService.NroUpasVisitadasCobrador(usuario.getIdusuario(), FuncionesHelper.fechaToString(desde.getTime()), FuncionesHelper.fechaToString(hasta.getTime())));
				
				model.addSeries(cobradortotalrecaudado);
				model1.addSeries(cobradornroupasrecaudado);
		}
		
	}
	
	public void generarGrafica2() {
		model2 = new CartesianChartModel();
//		
//		for (String mes : Almanaque.listaMesesAlmanaque()) {
//			 ChartSeries anio = new ChartSeries();
//			 
//			 	anio.setLabel(mes);
//			 	Calendar desde = Calendar.getInstance();
//				Calendar hasta= Calendar.getInstance();
//				
//				
//				desde.set(Calendar.YEAR,Integer.parseInt(anioRecauMesesxAnio));
//				desde.set(Calendar.MONTH, Integer.parseInt(Almanaque.mesanumero(mes))-1);
//				desde.set(Calendar.DATE, 1);
//				desde.set(Calendar.HOUR,0);
//				desde.set(Calendar.MINUTE,0);
//				desde.set(Calendar.SECOND,0);
//				desde.set(Calendar.MILLISECOND,0);
//				
//				hasta.set(Calendar.YEAR,Integer.parseInt(anioRecauMesesxAnio));
//				hasta.set(Calendar.MONTH, Integer.parseInt(Almanaque.mesanumero(mes))-1);
//				hasta.set(Calendar.DATE, desde.getActualMaximum(Calendar.DAY_OF_MONTH));
//				hasta.set(Calendar.HOUR,0);
//				hasta.set(Calendar.MINUTE,0);
//				hasta.set(Calendar.SECOND,0);
//				hasta.set(Calendar.MILLISECOND,0);
//			 	
//				anio.set(mes, cobranzaRecaudacionService.sumarComprobanteCobrador(99999, FuncionesHelper.fechaToString(desde.getTime()), FuncionesHelper.fechaToString(hasta.getTime())));
//				model2.addSeries(anio);
//		}
		
	}

	
	public void generarGrafica3() {
		model3 = new CartesianChartModel();
		ChartSeries anio = new ChartSeries();
		anio.setLabel("Cobrador");
//		for (String mes : Almanaque.listaMesesAlmanaque()) {
//				
//			 
//			 	
//			 	Calendar desde = Calendar.getInstance();
//				Calendar hasta= Calendar.getInstance();
//				
//				
//				desde.set(Calendar.YEAR,Integer.parseInt(anioRecauCobraxAnio));
//				desde.set(Calendar.MONTH, Integer.parseInt(Almanaque.mesanumero(mes))-1);
//				desde.set(Calendar.DATE, 1);
//				desde.set(Calendar.HOUR,0);
//				desde.set(Calendar.MINUTE,0);
//				desde.set(Calendar.SECOND,0);
//				desde.set(Calendar.MILLISECOND,0);
//				
//				hasta.set(Calendar.YEAR,Integer.parseInt(anioRecauCobraxAnio));
//				hasta.set(Calendar.MONTH, Integer.parseInt(Almanaque.mesanumero(mes))-1);
//				hasta.set(Calendar.DATE, desde.getActualMaximum(Calendar.DAY_OF_MONTH));
//				hasta.set(Calendar.HOUR,0);
//				hasta.set(Calendar.MINUTE,0);
//				hasta.set(Calendar.SECOND,0);
//				hasta.set(Calendar.MILLISECOND,0);
//			 	
//				
//				anio.set(mes.substring(0, 3), cobranzaRecaudacionService.sumarComprobanteCobrador(idselectedCobrador, FuncionesHelper.fechaToString(desde.getTime()), FuncionesHelper.fechaToString(hasta.getTime())));
//				
//		}
//		
//		model3.addSeries(anio);
//		
		
	}
	
	public void generarGrafica4() {
		model4 = new BubbleChartModel();
		List<Usuario> listaCobradores= new ArrayList<Usuario>();
		listaCobradores= FHvariadoService.obtenerCobradores();
		
		
		for (Usuario usuario : listaCobradores) {
			 
			 	Calendar desde = Calendar.getInstance();
				Calendar hasta= Calendar.getInstance();
				
				desde.set(Calendar.YEAR,Integer.parseInt(anioRecauCobraxMesVsTamanioCartera));
				desde.set(Calendar.MONTH, Integer.parseInt(Almanaque.mesanumero(mesRecauCobraxMesVsTamanioCartera))-1);
				desde.set(Calendar.DATE, 1);
				desde.set(Calendar.HOUR,0);
				desde.set(Calendar.MINUTE,0);
				desde.set(Calendar.SECOND,0);
				desde.set(Calendar.MILLISECOND,0);
				
				hasta.set(Calendar.YEAR,Integer.parseInt(anioRecauCobraxMesVsTamanioCartera));
				hasta.set(Calendar.MONTH, Integer.parseInt(Almanaque.mesanumero(mesRecauCobraxMesVsTamanioCartera))-1);
				hasta.set(Calendar.DATE, desde.getActualMaximum(Calendar.DAY_OF_MONTH));
				hasta.set(Calendar.HOUR,0);
				hasta.set(Calendar.MINUTE,0);
				hasta.set(Calendar.SECOND,0);
				hasta.set(Calendar.MILLISECOND,0);
			 	
			 
			 model4.add(new BubbleChartSeries(usuario.getNombrescompletos(),(int)cobranzaRecaudacionService.sumarComprobanteCobrador(usuario.getIdusuario(), FuncionesHelper.fechaToString(desde.getTime()), FuncionesHelper.fechaToString(hasta.getTime()),Boolean.FALSE,Boolean.FALSE),120,45));
		
		}
		
	}
	
	
	public void generarGrafica5() {
		model5 = new CartesianChartModel();
		model6 = new CartesianChartModel();
		List<Usuario> listaCobradores= new ArrayList<Usuario>();
		listaCobradores= FHvariadoService.obtenerCobradores();
		
		
		for (Usuario usuario : listaCobradores) {
			 ChartSeries cobradortotalrecaudado = new ChartSeries();
			 ChartSeries cobradornroupasrecaudado = new ChartSeries();
			 
			 cobradortotalrecaudado.setLabel(usuario.getNombrescompletos());
			 	Calendar desde = Calendar.getInstance();
				Calendar hasta= Calendar.getInstance();
				
				desde.set(Calendar.YEAR,Integer.parseInt(anioRecauMoraCobraxMes));
				desde.set(Calendar.MONTH, Integer.parseInt(Almanaque.mesanumero(mesRecauMoraCobraxMes))-1);
				desde.set(Calendar.DATE, 1);
				desde.set(Calendar.HOUR,0);
				desde.set(Calendar.MINUTE,0);
				desde.set(Calendar.SECOND,0);
				desde.set(Calendar.MILLISECOND,0);
				
				hasta.set(Calendar.YEAR,Integer.parseInt(anioRecauMoraCobraxMes));
				hasta.set(Calendar.MONTH, Integer.parseInt(Almanaque.mesanumero(mesRecauMoraCobraxMes))-1);
				hasta.set(Calendar.DATE, desde.getActualMaximum(Calendar.DAY_OF_MONTH));
				hasta.set(Calendar.HOUR,0);
				hasta.set(Calendar.MINUTE,0);
				hasta.set(Calendar.SECOND,0);
				hasta.set(Calendar.MILLISECOND,0);
			 	
				cobradortotalrecaudado.set(mesRecauMoraCobraxMes, cobranzaRecaudacionService.sumarMoraComprobanteCobrador(usuario.getIdusuario(), FuncionesHelper.fechaToString(desde.getTime()), FuncionesHelper.fechaToString(hasta.getTime())));
				cobradornroupasrecaudado.set(mesRecauMoraCobraxMes, cobranzaRecaudacionService.NroUpasMoraCobradaCobrador(usuario.getIdusuario(), FuncionesHelper.fechaToString(desde.getTime()), FuncionesHelper.fechaToString(hasta.getTime())));
				
				model5.addSeries(cobradortotalrecaudado);
				model6.addSeries(cobradornroupasrecaudado);
		}
		
	}
	


	public UsuarioManagedBean getUserMB() {
		return userMB;
	}


	public void setUserMB(UsuarioManagedBean userMB) {
		this.userMB = userMB;
	}


	public IContratoService getContratoService() {
		return contratoService;
	}


	public void setContratoService(IContratoService contratoService) {
		this.contratoService = contratoService;
	}





	public IRecaudacionCobranzaService getCobranzaRecaudacionService() {
		return cobranzaRecaudacionService;
	}


	public void setCobranzaRecaudacionService(
			IRecaudacionCobranzaService cobranzaRecaudacionService) {
		this.cobranzaRecaudacionService = cobranzaRecaudacionService;
	}


	public ITipoCambioService getTipocambioService() {
		return tipocambioService;
	}


	public void setTipocambioService(ITipoCambioService tipocambioService) {
		this.tipocambioService = tipocambioService;
	}


	public IFHvariadoService getFHvariadoService() {
		return FHvariadoService;
	}


	public void setFHvariadoService(IFHvariadoService fHvariadoService) {
		FHvariadoService = fHvariadoService;
	}


	public void setModel(CartesianChartModel model) {
		this.model = model;
	}

	public CartesianChartModel getModel() {
		return model; 
		}


	public String getMesRecauCobraxMes() {
		return mesRecauCobraxMes;
	}


	public void setMesRecauCobraxMes(String mesRecauCobraxMes) {
		this.mesRecauCobraxMes = mesRecauCobraxMes;
	}


	public String getAnioRecauCobraxMes() {
		return anioRecauCobraxMes;
	}


	public void setAnioRecauCobraxMes(String anioRecauCobraxMes) {
		this.anioRecauCobraxMes = anioRecauCobraxMes;
	}


	public String getAnioRecauMesesxAnio() {
		return anioRecauMesesxAnio;
	}


	public void setAnioRecauMesesxAnio(String anioRecauMesesxAnio) {
		this.anioRecauMesesxAnio = anioRecauMesesxAnio;
	}


	public String getAnioRecauCobraxAnio() {
		return anioRecauCobraxAnio;
	}


	public void setAnioRecauCobraxAnio(String anioRecauCobraxAnio) {
		this.anioRecauCobraxAnio = anioRecauCobraxAnio;
	}


	public List<ItemBean> getListaCobrador() {
		return listaCobrador;
	}


	public void setListaCobrador(List<ItemBean> listaCobrador) {
		this.listaCobrador = listaCobrador;
	}


	public ItemBean getSelectedCobrador() {
		return selectedCobrador;
	}


	public void setSelectedCobrador(ItemBean selectedCobrador) {
		this.selectedCobrador = selectedCobrador;
	}


	public int getIdselectedCobrador() {
		return idselectedCobrador;
	}


	public void setIdselectedCobrador(int idselectedCobrador) {
		this.idselectedCobrador = idselectedCobrador;
	}


	public CartesianChartModel getModel2() {
		return model2;
	}


	public void setModel2(CartesianChartModel model2) {
		this.model2 = model2;
	}


	public CartesianChartModel getModel3() {
		return model3;
	}


	public void setModel3(CartesianChartModel model3) {
		this.model3 = model3;
	}




	public BubbleChartModel getModel4() {
		return model4;
	}


	public void setModel4(BubbleChartModel model4) {
		this.model4 = model4;
	}


	public String getMesRecauCobraxMesVsTamanioCartera() {
		return mesRecauCobraxMesVsTamanioCartera;
	}


	public void setMesRecauCobraxMesVsTamanioCartera(
			String mesRecauCobraxMesVsTamanioCartera) {
		this.mesRecauCobraxMesVsTamanioCartera = mesRecauCobraxMesVsTamanioCartera;
	}


	public String getAnioRecauCobraxMesVsTamanioCartera() {
		return anioRecauCobraxMesVsTamanioCartera;
	}


	public void setAnioRecauCobraxMesVsTamanioCartera(
			String anioRecauCobraxMesVsTamanioCartera) {
		this.anioRecauCobraxMesVsTamanioCartera = anioRecauCobraxMesVsTamanioCartera;
	}


	public CartesianChartModel getModel1() {
		return model1;
	}


	public void setModel1(CartesianChartModel model1) {
		this.model1 = model1;
	}


	public String getMesRecauMoraCobraxMes() {
		return mesRecauMoraCobraxMes;
	}


	public void setMesRecauMoraCobraxMes(String mesRecauMoraCobraxMes) {
		this.mesRecauMoraCobraxMes = mesRecauMoraCobraxMes;
	}


	public String getAnioRecauMoraCobraxMes() {
		return anioRecauMoraCobraxMes;
	}


	public void setAnioRecauMoraCobraxMes(String anioRecauMoraCobraxMes) {
		this.anioRecauMoraCobraxMes = anioRecauMoraCobraxMes;
	}


	public CartesianChartModel getModel5() {
		return model5;
	}


	public void setModel5(CartesianChartModel model5) {
		this.model5 = model5;
	}


	public CartesianChartModel getModel6() {
		return model6;
	}


	public void setModel6(CartesianChartModel model6) {
		this.model6 = model6;
	}


	public IRecaudacionCarteraService getCarteraService() {
		return carteraService;
	}


	public void setCarteraService(IRecaudacionCarteraService carteraService) {
		this.carteraService = carteraService;
	}
	
	

}

