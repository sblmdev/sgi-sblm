package pe.gob.sblm.sgi.converter;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import pe.gob.sblm.api.commons.constants.sgi.Constantes;
import pe.gob.sblm.sgi.bean.ItemBean;
import pe.gob.sblm.sgi.controller.CobranzaManagedBean;

/**
 * Resumen.
 * Objecto : ConverterOrgJurisd
 * Descripci贸n : Convertidor para manejar el autocompletar de registro de notificaci贸n.
 * Fecha de creaci贸n : Apr 3, 2013 2:34:44 PM
 * @author C茅sar Altamirano
 * ----------------------------------------------------------------
 * Modificaciones.
 * Fecha		Nombre		Descripci贸n
 * ----------------------------------------------------------------
 *
 */
@FacesConverter(value="converterCobradorUsuario")
public class ConverterCobradorUsuario implements Converter, Serializable{
	
	private static final long serialVersionUID = 4910347070240746641L;

	/**
	 * Convierte el POJO "ItemBean" en su equivalente String.
	 * @param facesContext El contexto de la aplicaci贸n.
	 * @param component El componente JSF.
	 * @param El POJO para convertir a String.
	 * @return La descripci贸n a mostrar en la interfaz de usuario.
	 */
	
	public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
		ItemBean item = (ItemBean)object;
		String valor;
		if (object == null){
			valor=Constantes.EMPTY;
        }else{  
            valor =item.getDescripcion(); 
        }
		return valor;
	}
	
	/**
	 * Convierte el valor String a su equivalente POJO "ItemBean"
	 * @param facesContext El contexto de la aplicaci髇.
	 * @param component El componente JSF.
	 * @param value El valor String para convertir en POJO.
	 * @return El POJO "ItemBean"
	 */
	
	public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
		ItemBean item = new ItemBean();
		if (value.trim().equals("")) {  
			return null;  
        } else {  
            try {
            	CobranzaManagedBean rn = getCobranzaManagedBean();
            	if(rn == null) return null;
            	
            	item = rn.buscarUsuarioCobrador(value);
            	
            	return item;
            
            } catch(Exception e) {
                return null;
            }  
        }
	}
	
	/**
	 * Recupera el Managed Bean que utiliza 茅ste convertidor, para interactuar con sus variables.
	 * @return El Managed Bean CRegistroNotificacion
	 */
	private CobranzaManagedBean getCobranzaManagedBean(){
		FacesContext ctx = FacesContext.getCurrentInstance();
		return (CobranzaManagedBean)ctx.getViewRoot().getViewMap().get("carteraMB");
	}
}
