package pe.gob.sblm.sgi.controller;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import net.sf.jasperreports.engine.JasperPrint;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import pe.gob.sblm.api.commons.constants.sgi.Constantes;
import pe.gob.sblm.api.commons.utility.FuncionesHelper;
import pe.gob.sblm.sgi.bean.LetraBean;
import pe.gob.sblm.sgi.service.ITipoCambioService;
import pe.gob.sblm.sgi.util.ExportarArchivo;

@ViewScoped
@ManagedBean(name = "herramientaGeneradorLetraMB")
public class HerramientaGeneradorLetraManagedBean extends BaseController implements Serializable {

	private static final long serialVersionUID = 1L;
	private JasperPrint jasperPrint;
	
	@ManagedProperty(value = "#{tipocambioService}")
	private ITipoCambioService tipocambioService;
	
	@ManagedProperty(value = "#{usuarioMB}")
	public UsuarioManagedBean userMB;
	
	private StreamedContent report;
	private Double tipcambio;
	
	private boolean visiblefiador=true;
	private String nombresfiador;
	private String dnifiador;
	private String razonsocialfiador;
	private String rucfiador;
	private String domiciliofiador;
	private String rptelegalnombresfiador;
	private String rptelegaldnifiador;
	private String rptelegaltelffiador;
	private String telffiador;
	
	
	private boolean visiblegirador=true;
	private String nombresgirador;
	private String dnigirador;
	private String razonsocialgirador;
	private String rucgirador;
	private String domiciliogirador;
	private String rptelegalnombresgirador;
	private String rptelegaldnigirador;
	private String rptelegaltelfgirador;
	private String telfgirador;
	
	
	private String lugargiro="LIMA";
	private Date fechagiro;
	private Date vencimiento;
	private int nrocuotas;
	private int nrocuotaspagadas;
	private String nombresgerente="SR. JOSÉ JUAN RODRIGUEZ CARDENAS";
	private String rucgerente="20135604551";

	private boolean merced1=false,merced2=false,merced3=false,merced4=false,merced5=false,merced6=false;
	private Double montomerced1,montomerced2,montomerced3,montomerced4,montomerced5,montomerced6;
	private String montoletramerced1,montoletramerced2,montoletramerced3,montoletramerced4,montoletramerced5,montoletramerced6;
	
	@PostConstruct
	public void init(){
		tipcambio=tipocambioService.obtenerTipoCambio(FuncionesHelper.fechaToString(new Date()));
	}

	
	public void generarLetra() {
		List<LetraBean> lista= new ArrayList<LetraBean>();
		Map<String, Object> parameters = new HashMap<String, Object>();
		String  reportPath;
		parameters.put("domiciliofiador",domiciliofiador);
		parameters.put("domiciliogirador", domiciliogirador);
		parameters.put("nombresgerente",nombresgerente);
		parameters.put("rucgerente","RUC Nº "+rucgerente);
		parameters.put("lugargiro", lugargiro);
		parameters.put("fechagiro",FuncionesHelper.fechaToString(fechagiro ));
		parameters.put("numeroletras", nrocuotas-nrocuotaspagadas);
		
		
		
		Calendar fechaVencimiento = Calendar.getInstance();
		fechaVencimiento.setTime(vencimiento);
		
		
		for (int i = 0; i < nrocuotas-nrocuotaspagadas; i++) {
			LetraBean bean= new LetraBean();
			bean.setNroletra(i+1);
			bean.setFechaVencimiento(fechaVencimiento.getTime());
			
			if (nrocuotas-nrocuotaspagadas<=12) {
				bean.setMontonumeros(montomerced1);
				bean.setMontoletras(montoletramerced1);
			}else if (nrocuotas-nrocuotaspagadas<=24) {
				bean.setMontonumeros(montomerced2);
				bean.setMontoletras(montoletramerced2);
			}else if (nrocuotas-nrocuotaspagadas<=36) {
				bean.setMontonumeros(montomerced3);
				bean.setMontoletras(montoletramerced3);
			}else if (nrocuotas-nrocuotaspagadas<=48) {
				bean.setMontonumeros(montomerced4);
				bean.setMontoletras(montoletramerced4);
			}else if (nrocuotas-nrocuotaspagadas<=60) {
				bean.setMontonumeros(montomerced5);	
				bean.setMontoletras(montoletramerced5);
			}else {
				bean.setMontonumeros(montomerced6);
				bean.setMontoletras(montoletramerced6);
			}
			
			lista.add(bean);
			fechaVencimiento.add(1, Calendar.MONTH);
		
		}
		
		System.out.println(lista.size());
		   
		if (!visiblegirador && visiblefiador) {
			   
			   reportPath=  "letraJN";
			   
			   /*Girador*/
			   parameters.put("razonsocialgirador",razonsocialgirador);
			   parameters.put("rucgirador", rucgirador);
			   parameters.put("telfgirador ",telfgirador );
			   parameters.put("rptelegalnombresgirador",rptelegalnombresgirador);
			   parameters.put("rptelegaldnigirador", rptelegaldnigirador);
			   parameters.put("rptelegaltelfgirador",rptelegaltelfgirador);
			   
			   /*Fiador*/
			   parameters.put("nombresfiador",nombresfiador);
			   parameters.put("dnifiador",dnifiador);
			   parameters.put("telffiador",telffiador);
			   
		} else if(!visiblegirador && !visiblefiador){	
			
			   parameters.put("razonsocialgirador",razonsocialgirador);
			   parameters.put("rucgirador", rucgirador);
			   parameters.put("telfgirador ",telfgirador );
			   
			   parameters.put("rptelegalnombresgirador",rptelegalnombresgirador);
			   parameters.put("rptelegaldnigirador", rptelegaldnigirador);
			   parameters.put("rptelegaltelfgirador",rptelegaltelfgirador);
			   
			   parameters.put("nombresfiador",nombresfiador);
			   parameters.put("dnifiador",dnifiador);
			   parameters.put("telffiador",telffiador);
			
			   reportPath=  "letraJJ";
			
		} else if(visiblegirador && !visiblefiador) {
			   reportPath=  "letraNJ";
		}
		else {
			  reportPath=  "letraNN";	
		}
			

	   report = generarPDF(reportPath, parameters, lista);  
		
	}
	
	
	@SuppressWarnings("static-access")
	private StreamedContent generarPDF(String nombreReporte, Map<String, Object> parameters, 
			List<?> lista){
		ExportarArchivo printer = new ExportarArchivo();
		StringBuilder jasperFile = new StringBuilder();
		StringBuilder reporte = new StringBuilder();
		jasperFile.append("C://SGI//reportes//Gerencial//"+nombreReporte+".jrxml");
		try{
			byte[] array = printer.exportPdf(jasperFile.toString(), parameters, lista);
			InputStream stream = new ByteArrayInputStream(array);
			reporte.append(nombreReporte);
			reporte.append(".");
			reporte.append(Constantes.EXTENSION_FORMATO_PDF);
			return new DefaultStreamedContent(stream,  Constantes.APPLICATION_PDF, reporte.toString());
		}catch(Exception e){
			return null;
		}
	}
	
	
	
	public void agregar(){
		merced1=false;merced2=false;merced3=false;merced4=false;merced5=false;merced6=false;
		if (nrocuotas==0) {
			FacesContext context = FacesContext.getCurrentInstance();
			
			context.addMessage(null, new FacesMessage("No se ha ingresado el numero de cuotas", "Ingresar un número antes de agregar los montos de cuota"));
		
		} else if (nrocuotas>72) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage("El número de cuotas supera el máximo permitido", "Ingresar el número menor para agregar los montos de cuota"));
		} else {
			
			if (nrocuotas<=12) {
				merced1=true;
			}else if (nrocuotas<=24) {
				merced1=true;merced2=true;
			}else if (nrocuotas<=36) {
				merced1=true;merced2=true;merced3=true;
			}else if (nrocuotas<=48) {
				merced1=true;merced2=true;merced3=true;merced4=true;
			}else if (nrocuotas<=60) {
				merced1=true;merced2=true;merced3=true;merced4=true;merced5=true;
			}else {
				merced1=true;merced2=true;merced3=true;merced4=true;merced5=true;merced6=true;
			}
		}
		
		//Setting 
		setMontomerced1(0.0);
		setMontomerced2(0.0);
		setMontomerced3(0.0);
		setMontomerced4(0.0);
		setMontomerced5(0.0);
		setMontomerced6(0.0);
	}

	public JasperPrint getJasperPrint() {
		return jasperPrint;
	}

	public void setJasperPrint(JasperPrint jasperPrint) {
		this.jasperPrint = jasperPrint;
	}


	public UsuarioManagedBean getUserMB() {
		return userMB;
	}

	public void setUserMB(UsuarioManagedBean userMB) {
		this.userMB = userMB;
	}


	public ITipoCambioService getTipocambioService() {
		return tipocambioService;
	}

	public void setTipocambioService(ITipoCambioService tipocambioService) {
		this.tipocambioService = tipocambioService;
	}

	public StreamedContent getReport() {
		return report;
	}

	public void setReport(StreamedContent report) {
		this.report = report;
	}

	public Double getTipcambio() {
		return tipcambio;
	}

	public void setTipcambio(Double tipcambio) {
		this.tipcambio = tipcambio;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}



	public boolean isVisiblefiador() {
		return visiblefiador;
	}

	public void setVisiblefiador(boolean visiblefiador) {
		this.visiblefiador = visiblefiador;
	}

	public boolean isVisiblegirador() {
		return visiblegirador;
	}

	public void setVisiblegirador(boolean visiblegirador) {
		this.visiblegirador = visiblegirador;
	}

	public String getNombresfiador() {
		return nombresfiador;
	}

	public void setNombresfiador(String nombresfiador) {
		this.nombresfiador = nombresfiador;
	}

	public String getDnifiador() {
		return dnifiador;
	}

	public void setDnifiador(String dnifiador) {
		this.dnifiador = dnifiador;
	}

	public String getRazonsocialfiador() {
		return razonsocialfiador;
	}

	public void setRazonsocialfiador(String razonsocialfiador) {
		this.razonsocialfiador = razonsocialfiador;
	}

	public String getDomiciliofiador() {
		return domiciliofiador;
	}

	public void setDomiciliofiador(String domiciliofiador) {
		this.domiciliofiador = domiciliofiador;
	}

	public String getRptelegalnombresfiador() {
		return rptelegalnombresfiador;
	}

	public void setRptelegalnombresfiador(String rptelegalnombresfiador) {
		this.rptelegalnombresfiador = rptelegalnombresfiador;
	}

	public String getRptelegaldnifiador() {
		return rptelegaldnifiador;
	}

	public void setRptelegaldnifiador(String rptelegaldnifiador) {
		this.rptelegaldnifiador = rptelegaldnifiador;
	}

	public String getRptelegaltelffiador() {
		return rptelegaltelffiador;
	}

	public void setRptelegaltelffiador(String rptelegaltelffiador) {
		this.rptelegaltelffiador = rptelegaltelffiador;
	}

	public String getNombresgirador() {
		return nombresgirador;
	}

	public void setNombresgirador(String nombresgirador) {
		this.nombresgirador = nombresgirador;
	}

	public String getDnigirador() {
		return dnigirador;
	}

	public void setDnigirador(String dnigirador) {
		this.dnigirador = dnigirador;
	}

	public String getRazonsocialgirador() {
		return razonsocialgirador;
	}

	public void setRazonsocialgirador(String razonsocialgirador) {
		this.razonsocialgirador = razonsocialgirador;
	}



	public String getDomiciliogirador() {
		return domiciliogirador;
	}

	public void setDomiciliogirador(String domiciliogirador) {
		this.domiciliogirador = domiciliogirador;
	}

	public String getRptelegalnombresgirador() {
		return rptelegalnombresgirador;
	}

	public void setRptelegalnombresgirador(String rptelegalnombresgirador) {
		this.rptelegalnombresgirador = rptelegalnombresgirador;
	}

	public String getRptelegaldnigirador() {
		return rptelegaldnigirador;
	}

	public void setRptelegaldnigirador(String rptelegaldnigirador) {
		this.rptelegaldnigirador = rptelegaldnigirador;
	}

	public String getRptelegaltelfgirador() {
		return rptelegaltelfgirador;
	}

	public void setRptelegaltelfgirador(String rptelegaltelfgirador) {
		this.rptelegaltelfgirador = rptelegaltelfgirador;
	}

	public String getLugargiro() {
		return lugargiro;
	}

	public void setLugargiro(String lugargiro) {
		this.lugargiro = lugargiro;
	}

	public Date getFechagiro() {
		return fechagiro;
	}

	public void setFechagiro(Date fechagiro) {
		this.fechagiro = fechagiro;
	}

	public Date getVencimiento() {
		return vencimiento;
	}

	public void setVencimiento(Date vencimiento) {
		this.vencimiento = vencimiento;
	}

	public int getNrocuotas() {
		return nrocuotas;
	}

	public void setNrocuotas(int nrocuotas) {
		this.nrocuotas = nrocuotas;
	}

	public int getNrocuotaspagadas() {
		return nrocuotaspagadas;
	}

	public void setNrocuotaspagadas(int nrocuotaspagadas) {
		this.nrocuotaspagadas = nrocuotaspagadas;
	}



	public String getNombresgerente() {
		return nombresgerente;
	}

	public void setNombresgerente(String nombresgerente) {
		this.nombresgerente = nombresgerente;
	}

	public boolean isMerced1() {
		return merced1;
	}

	public void setMerced1(boolean merced1) {
		this.merced1 = merced1;
	}

	public boolean isMerced2() {
		return merced2;
	}

	public void setMerced2(boolean merced2) {
		this.merced2 = merced2;
	}

	public boolean isMerced3() {
		return merced3;
	}

	public void setMerced3(boolean merced3) {
		this.merced3 = merced3;
	}

	public boolean isMerced4() {
		return merced4;
	}

	public void setMerced4(boolean merced4) {
		this.merced4 = merced4;
	}

	public boolean isMerced5() {
		return merced5;
	}

	public void setMerced5(boolean merced5) {
		this.merced5 = merced5;
	}

	public boolean isMerced6() {
		return merced6;
	}

	public void setMerced6(boolean merced6) {
		this.merced6 = merced6;
	}

	public Double getMontomerced1() {
		return montomerced1;
	}

	public void setMontomerced1(Double montomerced1) {
		this.montomerced1 = montomerced1;
	}

	public Double getMontomerced2() {
		return montomerced2;
	}

	public void setMontomerced2(Double montomerced2) {
		this.montomerced2 = montomerced2;
	}

	public Double getMontomerced3() {
		return montomerced3;
	}

	public void setMontomerced3(Double montomerced3) {
		this.montomerced3 = montomerced3;
	}

	public Double getMontomerced4() {
		return montomerced4;
	}

	public void setMontomerced4(Double montomerced4) {
		this.montomerced4 = montomerced4;
	}

	public Double getMontomerced5() {
		return montomerced5;
	}

	public void setMontomerced5(Double montomerced5) {
		this.montomerced5 = montomerced5;
	}

	public Double getMontomerced6() {
		return montomerced6;
	}

	public void setMontomerced6(Double montomerced6) {
		this.montomerced6 = montomerced6;
	}

	public String getRucfiador() {
		return rucfiador;
	}

	public void setRucfiador(String rucfiador) {
		this.rucfiador = rucfiador;
	}

	public String getRucgirador() {
		return rucgirador;
	}

	public void setRucgirador(String rucgirador) {
		this.rucgirador = rucgirador;
	}

	public String getRucgerente() {
		return rucgerente;
	}

	public void setRucgerente(String rucgerente) {
		this.rucgerente = rucgerente;
	}

	public String getTelffiador() {
		return telffiador;
	}

	public void setTelffiador(String telffiador) {
		this.telffiador = telffiador;
	}

	public String getTelfgirador() {
		return telfgirador;
	}

	public void setTelfgirador(String telfgirador) {
		this.telfgirador = telfgirador;
	}


	public String getMontoletramerced1() {
		return montoletramerced1;
	}


	public void setMontoletramerced1(String montoletramerced1) {
		this.montoletramerced1 = montoletramerced1;
	}


	public String getMontoletramerced2() {
		return montoletramerced2;
	}


	public void setMontoletramerced2(String montoletramerced2) {
		this.montoletramerced2 = montoletramerced2;
	}


	public String getMontoletramerced3() {
		return montoletramerced3;
	}


	public void setMontoletramerced3(String montoletramerced3) {
		this.montoletramerced3 = montoletramerced3;
	}


	public String getMontoletramerced4() {
		return montoletramerced4;
	}


	public void setMontoletramerced4(String montoletramerced4) {
		this.montoletramerced4 = montoletramerced4;
	}


	public String getMontoletramerced5() {
		return montoletramerced5;
	}


	public void setMontoletramerced5(String montoletramerced5) {
		this.montoletramerced5 = montoletramerced5;
	}


	public String getMontoletramerced6() {
		return montoletramerced6;
	}


	public void setMontoletramerced6(String montoletramerced6) {
		this.montoletramerced6 = montoletramerced6;
	}

}
