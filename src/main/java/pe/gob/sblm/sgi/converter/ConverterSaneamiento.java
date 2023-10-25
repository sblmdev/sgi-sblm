package pe.gob.sblm.sgi.converter;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import pe.gob.sblm.sgi.controller.InmuebleManagedBean;
import pe.gob.sblm.sgi.entity.Saneamiento;

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
@FacesConverter(value="converterSaneamiento")
public class ConverterSaneamiento implements Converter, Serializable{
	
	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value)
	{
	    return (value instanceof Saneamiento) ? ((Saneamiento) value).getDescripcion() : null;
	}
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component,String value)
	{
	    if(value == null)
	        return null;

	    InmuebleManagedBean data = context.getApplication().evaluateExpressionGet(context, "#{inmuebleMB}", InmuebleManagedBean.class);

	    for(Saneamiento saneamiento : data.getListaCondicionSaneamiento())
	    {
	        if(saneamiento.getDescripcion().equals(value)){
	            return saneamiento;			}
	    }

	    throw new ConverterException(new FacesMessage(String.format("Cannot convert %s to CompLovDtgrid", value)));
	}
	
}
