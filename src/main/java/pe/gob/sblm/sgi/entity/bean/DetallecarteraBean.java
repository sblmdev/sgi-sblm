package pe.gob.sblm.sgi.entity.bean;
// Generated 19/03/2014 03:10:50 PM by Hibernate Tools 3.2.1.GA


import java.util.Date;

public class DetallecarteraBean  implements java.io.Serializable {


     private int iddetallecartera;
     private int idcontrato;
     private int idcartera;
     private String numerocartera;
     private String  claveupa;
     private String direccion;
     private String numerointerior;
     private String numerocontrato;
     private String distrito;
     private String condicion;
     private Date feccre;
     private String usrcre;

    public DetallecarteraBean() {
    }

	/**
	 * @return the iddetallecartera
	 */
	public int getIddetallecartera() {
		return iddetallecartera;
	}

	/**
	 * @param iddetallecartera the iddetallecartera to set
	 */
	public void setIddetallecartera(int iddetallecartera) {
		this.iddetallecartera = iddetallecartera;
	}

	
	/**
	 * @return the idcontrato
	 */
	public int getIdcontrato() {
		return idcontrato;
	}

	/**
	 * @param idcontrato the idcontrato to set
	 */
	public void setIdcontrato(int idcontrato) {
		this.idcontrato = idcontrato;
	}
	
	

	/**
	 * @return the idcartera
	 */
	public int getIdcartera() {
		return idcartera;
	}

	/**
	 * @param idcartera the idcartera to set
	 */
	public void setIdcartera(int idcartera) {
		this.idcartera = idcartera;
	}

	/**
	 * @return the numerocartera
	 */
	public String getNumerocartera() {
		return numerocartera;
	}

	/**
	 * @param numerocartera the numerocartera to set
	 */
	public void setNumerocartera(String numerocartera) {
		this.numerocartera = numerocartera;
	}

	/**
	 * @return the claveupa
	 */
	public String getClaveupa() {
		return claveupa;
	}

	/**
	 * @param claveupa the claveupa to set
	 */
	public void setClaveupa(String claveupa) {
		this.claveupa = claveupa;
	}

	/**
	 * @return the direccion
	 */
	public String getDireccion() {
		return direccion;
	}

	/**
	 * @param direccion the direccion to set
	 */
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	/**
	 * @return the numerointerior
	 */
	public String getNumerointerior() {
		return numerointerior;
	}

	/**
	 * @param numerointerior the numerointerior to set
	 */
	public void setNumerointerior(String numerointerior) {
		this.numerointerior = numerointerior;
	}

	/**
	 * @return the feccre
	 */
	public Date getFeccre() {
		return feccre;
	}

	/**
	 * @param feccre the feccre to set
	 */
	public void setFeccre(Date feccre) {
		this.feccre = feccre;
	}

	/**
	 * @return the numerocontrato
	 */
	public String getNumerocontrato() {
		return numerocontrato;
	}

	/**
	 * @param numerocontrato the numerocontrato to set
	 */
	public void setNumerocontrato(String numerocontrato) {
		this.numerocontrato = numerocontrato;
	}

	/**
	 * @return the distrito
	 */
	public String getDistrito() {
		return distrito;
	}

	/**
	 * @param distrito the distrito to set
	 */
	public void setDistrito(String distrito) {
		this.distrito = distrito;
	}

	/**
	 * @return the condicion
	 */
	public String getCondicion() {
		return condicion;
	}

	/**
	 * @param condicion the condicion to set
	 */
	public void setCondicion(String condicion) {
		this.condicion = condicion;
	}

	/**
	 * @return the usrcre
	 */
	public String getUsrcre() {
		return usrcre;
	}

	/**
	 * @param usrcre the usrcre to set
	 */
	public void setUsrcre(String usrcre) {
		this.usrcre = usrcre;
	}



}


