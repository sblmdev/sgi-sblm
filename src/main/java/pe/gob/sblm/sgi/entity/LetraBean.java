package pe.gob.sblm.sgi.entity;

import java.util.Date;


public class LetraBean implements java.io.Serializable {

	private static final long serialVersionUID = -5042124107624268204L;
	private int idletra;
	private int nroletra;
	private Date fechaVencimiento;
	private Double montonumeros;
	private String montoletras;
	
	public int getIdletra() {
		return idletra;
	}
	public void setIdletra(int idletra) {
		this.idletra = idletra;
	}
	public int getNroletra() {
		return nroletra;
	}
	public void setNroletra(int nroletra) {
		this.nroletra = nroletra;
	}
	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}
	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Double getMontonumeros() {
		return montonumeros;
	}
	public void setMontonumeros(Double montonumeros) {
		this.montonumeros = montonumeros;
	}
	public String getMontoletras() {
		return montoletras;
	}
	public void setMontoletras(String montoletras) {
		this.montoletras = montoletras;
	}
}
