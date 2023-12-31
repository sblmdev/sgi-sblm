package pe.gob.sblm.sgi.entity;

// Generated 14-oct-2013 14:58:03 by Hibernate Tools 4.0.0

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Tipocambio generated by hbm2java
 */
@Entity
@Table(name = "DETRACCION", schema = "dbo", catalog = "sisinmueble")
public class Detraccion implements java.io.Serializable {

	private int idtipocambio;
	private Double valor;
	private String usrcre;
	private String usrmod;
	private Date feccre;
	private String horcre;

	public Detraccion() {
	}

	public Detraccion(int idtipocambio) {
		this.idtipocambio = idtipocambio;
	}

	public Detraccion(int idtipocambio, Double tipocambio, Date fecha,
			String usrcre, String usrmod, Date feccre, String horcre) {
		this.idtipocambio = idtipocambio;
		this.usrcre = usrcre;
		this.usrmod = usrmod;
		this.feccre = feccre;
		this.horcre = horcre;
	}

	@Id @GeneratedValue
	@Column(name = "IDDETRACCION", unique = true, nullable = false)
	public int getIdtipocambio() {
		return this.idtipocambio;
	}

	public void setIdtipocambio(int idtipocambio) {
		this.idtipocambio = idtipocambio;
	}

	
    @Column(name="VALOR", precision=53, scale=0)
	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	@Column(name = "USRCRE", length = 50)
	public String getUsrcre() {
		return this.usrcre;
	}

	public void setUsrcre(String usrcre) {
		this.usrcre = usrcre;
	}

	@Column(name = "USRMOD", length = 50)
	public String getUsrmod() {
		return this.usrmod;
	}

	public void setUsrmod(String usrmod) {
		this.usrmod = usrmod;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FECCRE", length = 23)
	public Date getFeccre() {
		return this.feccre;
	}

	public void setFeccre(Date feccre) {
		this.feccre = feccre;
	}

	@Column(name = "HORCRE", length = 50)
	public String getHorcre() {
		return this.horcre;
	}

	public void setHorcre(String horcre) {
		this.horcre = horcre;
	}

}
