package pe.gob.sblm.sgi.converter;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import pe.gob.sblm.sgi.controller.CobranzaManagedBean;
import pe.gob.sblm.sgi.entity.Comprobantepago;

/**
 * Resumen.
 * Objecto : ConverterSaneamiento
 * Descripcion : Convertidor para manejar el componenete selectCheckboxMenu para que nos deje manejar un objeto en vez de solo una string.
 * Fecha de creacion : Dic 3, 2015 2:34:44 PM
 * @author Cindy Vallejos
 * ----------------------------------------------------------------
 * Modificaciones.
 * Fecha		Nombre		Descripcion
 * ----------------------------------------------------------------
 *
 */
@FacesConverter(value="converterComprobante")
public class ConverterComprobante implements Converter, Serializable{
	
	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value)
	{
	    return (value instanceof Comprobantepago) ? ((Comprobantepago) value).getNroseriecomprobante() : null;
	}
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component,String value)
	{
	    if(value == null)
	        return null;

	    CobranzaManagedBean data = context.getApplication().evaluateExpressionGet(context, "#{carteraMB}", CobranzaManagedBean.class);

	    for(Comprobantepago comprobantepago : data.getListacomprobantes()){
	        if(comprobantepago.getNroseriecomprobante().toString().equals(value.toString())){
	            return comprobantepago;			}
	    }
		return null;

	}
	
}
