package pe.gob.sblm.sgi.dao;

import pe.gob.sblm.sgi.bean.NumeracionComprobantePagoBean;


public interface MaeNumeracionComprobantePagoDAO {


	public NumeracionComprobantePagoBean obtenerNumeracionBoletaFactura(String idtipodocuseleccionado, String nombrecarteraContrato);
	public NumeracionComprobantePagoBean obtenerNumeracionComprobantePago(String idtipodocuseleccionado);
	public void usarNumeracionComprobantepago(int idNumeracionComprobantepago);
	public void usarNumeracionComprobantepagoRC(int idNumeracionComprobantepago);
	public NumeracionComprobantePagoBean obtenerNumeracionNotaDebitoCredito(String idtipodocuseleccionado, String nombrecarteraContrato,String idtipodocpadrenotadebito);
	public boolean siExisteNumeroOperacion(String numerooperacion);
}
