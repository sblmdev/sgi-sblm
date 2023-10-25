package pe.gob.sblm.sgi.converter;
import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import pe.gob.sblm.sgi.controller.UpaManagedBean;
import pe.gob.sblm.sgi.entity.Upa;

/**
 * Resumen.
 * Objecto : ConverterUpa
 * Descripcion : Convertidor para manejar el componenete selectCheckboxMenu para que nos deje manejar un objeto en vez de solo una string.
 * Fecha de creacion : 22-05-2021
 * @author M@rco Al@rcon
 * ----------------------------------------------------------------
 * Modificaciones.
 * Fecha		Nombre		Descripcion
 * ----------------------------------------------------------------
 *
 */
@FacesConverter(value="converterUpa")
public class ConverterUpa implements Converter,Serializable{

	@Override
	public String getAsString(FacesContext context ,UIComponent component,Object value){
		return (value instanceof Upa) ? ((Upa) value).getClave() : null;
	}
	@Override
	public Object getAsObject(FacesContext context, UIComponent component,String value)
	{
	    if(value == null)
	        return null;

	    UpaManagedBean data = context.getApplication().evaluateExpressionGet(context, "#{upaMB}", UpaManagedBean.class);

	    for(Upa upa : data.getListaUpasActivas()){
	        if(upa.getClave().toString().equals(value.toString())){
	            return upa;			}
	    }
		return null;

	}
}
