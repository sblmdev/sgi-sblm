package pe.gob.sblm.sgi.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.gob.sblm.api.commons.constants.sgi.Constantes;
import pe.gob.sblm.api.commons.utility.FuncionesHelper;
import pe.gob.sblm.sgi.entity.InteresMora;
import pe.gob.sblm.sgi.entity.Tipointeresmora;
import pe.gob.sblm.sgi.service.IInteresMoraService;
import pe.gob.sblm.sgi.util.Constante;

@ManagedBean(name="interesmoraMB")
@ViewScoped
public class TipoInteresManagedBean extends BaseController implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger Logger = LoggerFactory.getLogger(TipoCambioManagedBean.class);
	
	private Date fechaTipoInteres;
	private String ultimoDia;
	private InteresMora tInteresActual= new InteresMora(),interesMora,interesMoraSelected= new InteresMora(),tInteresMora;
    private Tipointeresmora tipointeresmora;

	private List<InteresMora> listaTasaInteres= new ArrayList<InteresMora>();
	private List<Tipointeresmora> listaTipoTasaInteres= new ArrayList<Tipointeresmora>();
	private Boolean actualizado;
	private String tipoInteresSelected=Constante.TIPO_TASA_INTERES_ACTIVA;;
	
	@ManagedProperty(value="#{interesMoraService}")
	private transient IInteresMoraService interesMoraService;
	
	@ManagedProperty(value = "#{usuarioMB}")
	UsuarioManagedBean userMB;
	
	@PostConstruct
	public void init(){
		try{			
			tipointeresmora= new Tipointeresmora();
			tipointeresmora= interesMoraService.obtenerTipoInteresMora(tipoInteresSelected);
			tInteresActual=interesMoraService.obtenerUltimoInteresMora(tipoInteresSelected);			
			listaTasaInteres= interesMoraService.listarInteresMora(tipoInteresSelected);
			listaTipoTasaInteres= interesMoraService.listarTipoInteresMora();
			interesMora= new InteresMora();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void selectedTipoInteresMora(){
		try{
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
    public void actualizarTipoDeTasa(){
    	try{
    		limpiarCampos();
//    		tInteresActual=interesMoraService.obtenerUltimoInteresMora(tipoInteresSelected);			
//    		listaTasaInteres= interesMoraService.listarInteresMora(tipoInteresSelected);
    		System.out.println("tipoInteresSelected="+tipoInteresSelected);
    		init();
    		System.out.println("tipoInteresSelected2="+tipoInteresSelected);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
		
    }
	public void grabarInteresMora(){
		try {
			    Date ahora = new Date();
			    interesMora.toString();
			    if(!actualizado){
			    	interesMora.setFec_cre(ahora);
			    	interesMora.setUsr_cre(FuncionesHelper.converterCadenaMm(userMB.getNombreusr(), Boolean.TRUE));
			    	interesMoraService.grabarInteresMora(interesMora);
					FacesMessage msg = new FacesMessage("Se Registro correctamente la tasa de interes.");
					FacesContext.getCurrentInstance().addMessage(null, msg);			    	
					init();	    	
			    }else{			    	
			    	interesMora.setFec_mod(ahora);
			    	interesMora.setUsr_mod(FuncionesHelper.converterCadenaMm(userMB.getNombreusr(),Boolean.TRUE));
			    	interesMoraService.grabarInteresMora(interesMora);
			    	FacesMessage msg = new FacesMessage("Se Actualizo correctamente la tasa de interes.");
					FacesContext.getCurrentInstance().addMessage(null, msg);
					init();
			    }

		}catch(Exception e){
			
			addErrorMessage("", Constantes.MSG_GENERO_ERROR_OPERACION);
			Logger.error(e.getMessage(), e);
		}
		
	}

	public void limpiarCampos(){
		tInteresActual= new InteresMora();
		listaTasaInteres.clear();
	}
	public void consultaTasaInteres(){
		try {
			if (interesMoraSelected.getFec_reg() != null) {
				tInteresActual = interesMoraService.obtenerInteresMora(
						tipoInteresSelected, 
						null,
						FuncionesHelper.fechaHORAtoString(interesMoraSelected
								.getFec_reg()));
				if (tInteresActual == null) {
					addWarnMessage("", "No hay registro. ");
				}
			} else {
				addWarnMessage("", "Ingrese una fecha valida. ");
			}

		} catch (Exception e) {
			e.printStackTrace();
			addErrorMessage("", Constantes.MSG_GENERO_ERROR_OPERACION);
			Logger.error(e.getMessage(), e);
		}
	}
	public void habilitarVentanaNuevaTasaInteres(){
		try {
			Tipointeresmora tipointeres= new Tipointeresmora();
			tipointeres.setId_tipointeres(tipoInteresSelected);
			actualizado=Boolean.FALSE;
			interesMora = new InteresMora();			
			interesMora.setTipointeresmora(tipointeres);
			interesMora.setFec_reg(new Date());
			RequestContext contextRequest = RequestContext.getCurrentInstance();
			contextRequest.execute("dlgNuevaTasaInteres.show();");
		} catch (Exception e) {
			e.printStackTrace();
			addErrorMessage("", Constantes.MSG_GENERO_ERROR_OPERACION);
			Logger.error(e.getMessage(), e);
		}
	}
	public void habilitarVentanaActualizarTasaInteres(Integer idinteres){
		try {
			interesMora = new InteresMora();
			actualizado=Boolean.TRUE;
			interesMora = interesMoraService.obtenerInteresMora(tipoInteresSelected, idinteres, null);
			RequestContext contextRequest = RequestContext.getCurrentInstance();
			contextRequest.execute("dlgNuevaTasaInteres.show();");
		} catch (Exception e) {
			e.printStackTrace();
			addErrorMessage("", Constantes.MSG_GENERO_ERROR_OPERACION);
			Logger.error(e.getMessage(), e);
		}
	}
    public boolean validarFechaDelDia(InteresMora intmora){
    	boolean resp=false;
    	intmora.toString();
    	InteresMora interesmoras=interesMoraService.obtenerInteresMora(tipoInteresSelected, null, FuncionesHelper.fechaToString(intmora.getFec_reg()));    	
    	if(!actualizado){
    		if(interesmoras!=null){
        		resp=true;	
        	}
    	}else{
    		 if(interesmoras==null){
    			 resp=false;
    		   }else{
    			   if(!interesmoras.getId_interes().equals(intmora.getId_interes())){
    				   resp=true;
    			   }
    		}
    	}   	
    	return resp;
    }
    public void validarDatosInteres(){
    	interesMora.toString();
    	if(FuncionesHelper.validarNumeroReal(interesMora.getTamn())||interesMora.getTamn()<=0){
    		addWarnMessage("", "Debe ingresar el valor de la tasa de moneda nacional. ");
    	}else if(FuncionesHelper.validarNumeroReal(interesMora.getTamex())||interesMora.getTamex()<=0){
    		addWarnMessage("", "Debe ingresar el valor de la tasa de moneda extranjera. ");
    	}else if(FuncionesHelper.validarFecha(interesMora.getFec_reg())){
    		addWarnMessage("", "Debe ingresar la fecha. ");
    	}else if(validarFechaDelDia(interesMora)){
    		addWarnMessage("", "La tasa para este dia ya se encuentra registrada. ");
    	}else{
    		RequestContext contextRequest = RequestContext.getCurrentInstance();
			contextRequest.execute("dlgConfirmarRegistroTasaInteres.show();");
    	}
    	System.out.println("fin");
    }
	public void onRowSelect(SelectEvent event) {
		actualizado = true;
//		fechaTipoCambio=tipoCam.getFecha();
//		FacesMessage msg = new FacesMessage("tipo cambio :" + tipoCam.getTipocambio());
//		FacesContext.getCurrentInstance().addMessage(null, msg);
	}
	public void obtenerDatosSeleccionado(Integer id){
		try{
		interesMora= new InteresMora();
		interesMora = interesMoraService.obtenerInteresMora(tipointeresmora.getId_tipointeres(), id, null);
		if(interesMora!=null){
			actualizado=true;
			RequestContext contextRequest = RequestContext.getCurrentInstance();
			contextRequest.execute("dlgNuevaTasaInteres.show();");
		}else{
			addWarnMessage("", "No se encontro los datos de la tasa seleccionada. ");
		}
		}catch (Exception e) {
			e.printStackTrace();
			addErrorMessage("", Constantes.MSG_GENERO_ERROR_OPERACION);
			Logger.error(e.getMessage(), e);
		}
	}

    
	public Date getFechaTipoInteres() {
		return fechaTipoInteres;
	}


	public void setFechaTipoInteres(Date fechaTipoInteres) {
		this.fechaTipoInteres = fechaTipoInteres;
	}


	public String getUltimoDia() {
		return ultimoDia;
	}


	public void setUltimoDia(String ultimoDia) {
		this.ultimoDia = ultimoDia;
	}


	public InteresMora getInteresMora() {
		return interesMora;
	}


	public void setInteresMora(InteresMora interesMora) {
		this.interesMora = interesMora;
	}

	public List<InteresMora> getListaTasaInteres() {
		return listaTasaInteres;
	}

	public void setListaTasaInteres(List<InteresMora> listaTasaInteres) {
		this.listaTasaInteres = listaTasaInteres;
	}



	public InteresMora getInteresMoraSelected() {
		return interesMoraSelected;
	}


	public void setInteresMoraSelected(InteresMora interesMoraSelected) {
		this.interesMoraSelected = interesMoraSelected;
	}

	public InteresMora gettInteresActual() {
		return tInteresActual;
	}

	public void settInteresActual(InteresMora tInteresActual) {
		this.tInteresActual = tInteresActual;
	}
	public String getTipoInteresSelected() {
		return tipoInteresSelected;
	}

	public void setTipoInteresSelected(String tipoInteresSelected) {
		this.tipoInteresSelected = tipoInteresSelected;
	}

	public Boolean getActualizado() {
		return actualizado;
	}

	public void setActualizado(Boolean actualizado) {
		this.actualizado = actualizado;
	}

	public UsuarioManagedBean getUserMB() {
		return userMB;
	}

	public void setUserMB(UsuarioManagedBean userMB) {
		this.userMB = userMB;
	}

	public IInteresMoraService getInteresMoraService() {
		return interesMoraService;
	}

	public void setInteresMoraService(IInteresMoraService interesMoraService) {
		this.interesMoraService = interesMoraService;
	}
	public InteresMora gettInteresMora() {
		return tInteresMora;
	}

	public void settInteresMora(InteresMora tInteresMora) {
		this.tInteresMora = tInteresMora;
	}

	public Tipointeresmora getTipointeresmora() {
		return tipointeresmora;
	}

	public void setTipointeresmora(Tipointeresmora tipointeresmora) {
		this.tipointeresmora = tipointeresmora;
	}

	public List<Tipointeresmora> getListaTipoTasaInteres() {
		return listaTipoTasaInteres;
	}

	public void setListaTipoTasaInteres(List<Tipointeresmora> listaTipoTasaInteres) {
		this.listaTipoTasaInteres = listaTipoTasaInteres;
	}
	

}
