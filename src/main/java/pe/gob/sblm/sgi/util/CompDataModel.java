package pe.gob.sblm.sgi.util;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import pe.gob.sblm.sgi.entity.Auditoria;



public class CompDataModel extends ListDataModel<Auditoria> implements SelectableDataModel<Auditoria>{

	public CompDataModel() {
		
	}
	
	public CompDataModel(List<Auditoria> data) {
		super(data);
	}
	
	
	public Auditoria getRowData(String rowKey) {
		List<Auditoria> listCamp = (List<Auditoria>) getWrappedData();
		
		for (Auditoria ctmaeCampania : listCamp) {
			if ((ctmaeCampania.getIdauditoria()+"").equals(rowKey)) {
				return ctmaeCampania;
			}
		}
		return null;
	}

	
	public Object getRowKey(Auditoria ctmaeCampania) {
		return ctmaeCampania.getIdauditoria();
	}
}