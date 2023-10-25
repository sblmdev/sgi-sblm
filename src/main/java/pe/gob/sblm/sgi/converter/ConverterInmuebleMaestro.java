package pe.gob.sblm.sgi.converter;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import pe.gob.sblm.sgi.controller.InmuebleManagedBean;
import pe.gob.sblm.sgi.entity.Inmueblemaestro;

@FacesConverter(value="converterInmuebleMa")
public class ConverterInmuebleMaestro  implements Converter,Serializable{

	@Override
	public String getAsString(FacesContext context ,UIComponent component,Object value){
		return (value instanceof Inmueblemaestro) ? ((Inmueblemaestro) value).getClave() : null;
	}
	@Override
	public Object getAsObject(FacesContext context, UIComponent component,String value)
	{
	    if(value == null)
	        return null;

	    InmuebleManagedBean data = context.getApplication().evaluateExpressionGet(context, "#{inmuebleMB}", InmuebleManagedBean.class);

	    for(Inmueblemaestro inm : data.getListaInmuebleMaestro()){
	        if(inm.getClave().toString().equals(value.toString())){
	            return inm;			}
	    }
		return null;

	}
}
