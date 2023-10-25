package pe.gob.sblm.sgi.controller;

import java.io.Serializable;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.FacesEvent;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.context.RequestContext;

/** 
* Resumen.
* Objeto : BaseController
* Descripcion : Controlador generico.
* Fecha de Creacion : 12/06/2012.
* Autor : Jessica Vallejos. 
* ------------------------------------------------------------------------
* Modificaciones
* Fecha            Nombre           Descripcion
* ------------------------------------------------------------------------
* 12/03/2015	Jessica Vallejos 	Adicion del metodo getComponentById()
*/

public abstract class BaseController implements Serializable{
	
	
	public static final long serialVersionUID = 2605978164714023739L;

    /**
     * Devuelve el contexto.
     * @return Contexto, tipo ExternalContext.
     */
    public ExternalContext getContext(){
		return FacesContext.getCurrentInstance().getExternalContext();
	}
    
    /**
     * Devuelve el valor de un parámetro.
     * @param event - Evento que se realiza desde la página, tipo ActionEvent.
     * @param id - Identificador del parámetro, tipo String.
     * @return Valor del parámetro, tipo Object.
     * @throws Exception
     */
    public Object getUIParameter(ActionEvent event,String id)throws Exception{
		UIParameter param=(UIParameter)event.getComponent().findComponent(id);
		return param.getValue();
	}
	
    /**
     * Obtiene un componente de la página.
     * @param event - Evento que se realiza desde la página, tipo FacesEvent.
     * @param id - Identificador del componente, tipo String.
     * @return Componente, tipo UIComponent.
     */
    public UIComponent getUIComponent(FacesEvent event, String id){
    	return event.getComponent().findComponent(id);
    }
    
    
    /**
     * Agrega un mensaje de informacion.
     * @param msg - título del mensaje, tipo String.
     * @param detail - Detalle del mensaje, tipo String.
     */
	public void addInfoMessage(String msg, String detail) {
		addMessage(FacesMessage.SEVERITY_INFO, msg, detail);
	}

	/**
     * Agrega un mensaje de precaucion.
     * @param msg - título del mensaje, tipo String.
     * @param detail - Detalle del mensaje, tipo String.
     */
	public void addWarnMessage(String msg, String detail) {
		addMessage(FacesMessage.SEVERITY_WARN, msg, detail);
	}

	/**
     * Agrega un mensaje de error.
     * @param msg - título del mensaje, tipo String.
     * @param detail - Detalle del mensaje, tipo String.
     */
	public void addErrorMessage(String msg, String detail) {
		addMessage(FacesMessage.SEVERITY_ERROR, msg, detail);
	}

	/**
     * Agrega un mensaje de error fatal.
     * @param msg - título del mensaje, tipo String.
     * @param detail - Detalle del mensaje, tipo String.
     */
	public void addFatalMessage(String msg, String detail) {
		addMessage(FacesMessage.SEVERITY_FATAL, msg, detail);
	}

	/**
	 * Agrega un mensaje.
	 * @param severity - tipo de mensaje, tipo FacesMessage.Severity
	 * @param msg - título del mensaje, tipo String.
     * @param detail - Detalle del mensaje, tipo String.
	 */
	private void addMessage(FacesMessage.Severity severity, String message, String detail) {
		FacesMessage facesMessage = new FacesMessage(severity, message, detail);
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
	}
	
	/**
	 * Agrega un parámetro para ser invocado desde la página al retorno del metodo invocado.
	 * @param name - Nombre del parámetro, tipo String.
	 * @param value - Valor del parámetro, tipo Object.
	 */
	public void addCallbackParam(String name,Object value){
		 RequestContext context = RequestContext.getCurrentInstance();
		 context.addCallbackParam(name, value);
	}
	
	/**
	 * Obtiene la instancia activa de un controlador.
	 * @param controllerName - Nombre del controlador, tipo String.
	 * @return Controlador, tipo Object.
	 */
	public Object getController(String controllerName) {
		FacesContext fc = FacesContext.getCurrentInstance();
		Application app = fc.getApplication();
		ValueExpression ve = app.getExpressionFactory().createValueExpression(
				fc.getELContext(), String.format("#{%s}", controllerName),
				Object.class);
		return ve.getValue(fc.getELContext());
	}
	
	/**
	 * Obtiene el controlador que guarda los datos de sesion.
	 * @return Controlador de sesion, tipo CVariableSession.
	 */
	
	/**
	 * Obtiene la direccion IP del cliente.
	 * @return Direccion IP del cliente, tipo String.
	 */
	public String getRemoteAddress(){
		ExternalContext context = getContext();
		HttpServletRequest httpServletRequest = (HttpServletRequest)context.getRequest();
		return httpServletRequest.getRemoteAddr();
	}
	
	/**
	 * Obtiene un componente del contexto
	 * @param id - Identificador del componente a obtener, tipo String.
	 * @return Componente, tipo Object.
	 */
	public Object getComponentById(String id){
		FacesContext fc = FacesContext.getCurrentInstance();
		UIComponent component = fc.getViewRoot().findComponent(id);
		return component;
	}

	
}
