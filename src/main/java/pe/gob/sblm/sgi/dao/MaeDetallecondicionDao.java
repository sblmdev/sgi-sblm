package pe.gob.sblm.sgi.dao;

import java.util.List;

import pe.gob.sblm.sgi.bean.RentaBean;
import pe.gob.sblm.sgi.entity.MaeDetalleCondicion;


public interface MaeDetallecondicionDao {


	public void registrarDetalleCondicion(MaeDetalleCondicion maeDetalleCondicion);
	public List<RentaBean> obtenerDetalleCondicion(int idcontrato);
	public List<RentaBean> obtenerDetalleMaeCondicion(int idcontrato);
	

}
