package pe.gob.sblm.sgi.util;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import pe.gob.sblm.sgi.entity.Usuario;



public class CompDataModelUsuario extends ListDataModel<Usuario> implements SelectableDataModel<Usuario>{

	public CompDataModelUsuario() {
		
	}
	
	public CompDataModelUsuario(List<Usuario> data) {
		super(data);
	}
	
	
	public Usuario getRowData(String rowKey) {
		List<Usuario> listCamp = (List<Usuario>) getWrappedData();
		
		for (Usuario ctmaeCampania : listCamp) {
			if ((ctmaeCampania.getIdusuario()+"").equals(rowKey)) {
				return ctmaeCampania;
			}
		}
		return null;
	}

	
	public Object getRowKey(Usuario ctmaeCampania) {
		return ctmaeCampania.getIdusuario();
	}
}