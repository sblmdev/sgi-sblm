package pe.gob.sblm.sgi.util;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import pe.gob.sblm.sgi.entity.Perfilusuario;



public class CompDataModelPerfilUsuario extends ListDataModel<Perfilusuario> implements SelectableDataModel<Perfilusuario>{

	public CompDataModelPerfilUsuario() {
		
	}
	
	public CompDataModelPerfilUsuario(List<Perfilusuario> data) {
		super(data);
	}
	
	
	public Perfilusuario getRowData(String rowKey) {
		List<Perfilusuario> listCamp = (List<Perfilusuario>) getWrappedData();
		
		for (Perfilusuario ctmaeCampania : listCamp) {
			if ((ctmaeCampania.getIdperfilusuario()+"").equals(rowKey)) {
				return ctmaeCampania;
			}
		}
		return null;
	}

	
	public Object getRowKey(Perfilusuario ctmaeCampania) {
		return ctmaeCampania.getIdperfilusuario();
	}
}