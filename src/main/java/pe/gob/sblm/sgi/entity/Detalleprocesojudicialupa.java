package pe.gob.sblm.sgi.entity;
// Generated 25/08/2014 09:36:23 AM by Hibernate Tools 3.6.0


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Contrato generated by hbm2java
 */
@Entity
@Table(name="DETALLEPROCESOJUDICIALUPA"
    ,schema="dbo"
    ,catalog="sisinmueble"
)
public class Detalleprocesojudicialupa  implements java.io.Serializable {


     private int iddetalleprocesojudicialupa;
     private Procesojudicialupa procesojudicialupa;
     private Boolean estado;
     private String situacion;
     private String observacion;
 	 private String usrcre;
 	 private String usrmod;
 	 private Date feccre;
 	 private Date fecmod;
     
    @Id 
    @GeneratedValue
    @Column(name="IDDETALLEPROCESOJUDIDCIALUPA", unique=true, nullable=false)
	public int getIddetalleprocesojudicialupa() {
		return iddetalleprocesojudicialupa;
	}

	public void setIddetalleprocesojudicialupa(int iddetalleprocesojudicialupa) {
		this.iddetalleprocesojudicialupa = iddetalleprocesojudicialupa;
	}
    

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="IDPROCESOJUDICIALUPA")
	public Procesojudicialupa getProcesojudicialupa() {
		return procesojudicialupa;
	}

	public void setProcesojudicialupa(Procesojudicialupa procesojudicialupa) {
		this.procesojudicialupa = procesojudicialupa;
	}
	
	
	
    @Column(name="SITUACION", length=100)
	public String getSituacion() {
		return situacion;
	}
    


	public void setSituacion(String situacion) {
		this.situacion = situacion;
	}
	
    @Column(name="OBSERVACION", length=300)
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	
    @Column(name="ESTADO")
	public Boolean getEstado() {
		return estado;
	}

	public void setEstado(Boolean estado) {
		this.estado = estado;
	}
    
	@Column(name = "USRCRE", length = 100)
	public String getUsrcre() {
		return this.usrcre;
	}

	public void setUsrcre(String usrcre) {
		this.usrcre = usrcre;
	}

	@Column(name = "USRMOD", length = 100)
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

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FECMOD", length = 23)
	public Date getFecmod() {
		return this.fecmod;
	}

	public void setFecmod(Date fecmod) {
		this.fecmod = fecmod;
	}

     
     
}

