package pe.gob.sblm.sgi.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name="TIPOINTERESMORA"
    ,schema="dbo"
    ,catalog="sisinmueble"
)
public class Tipointeresmora {

	@Id
	@Column(name="ID_TIPOINTERES", unique = true, nullable = false)
    private String id_tipointeres;
	
	@Column(name="DESCRIPCION")
    private String descripcion ;
	
	@Column(name="USR_CRE")
    private String usr_cre;
	
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FEC_CRE", length = 23)
    private Date fec_cre;
	
	@Column(name="USR_MOD")
    private String usr_mod;
    
    @Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FEC_MOD", length = 23)
    private Date fec_mod;
    
    @Column(name="OBSERVACION")
    private String observacion;
    
    @Column(name="ESTADO")
    private String estado;

	public String getId_tipointeres() {
		return id_tipointeres;
	}

	public void setId_tipointeres(String id_tipointeres) {
		this.id_tipointeres = id_tipointeres;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getUsr_cre() {
		return usr_cre;
	}

	public void setUsr_cre(String usr_cre) {
		this.usr_cre = usr_cre;
	}

	public Date getFec_cre() {
		return fec_cre;
	}

	public void setFec_cre(Date fec_cre) {
		this.fec_cre = fec_cre;
	}

	public String getUsr_mod() {
		return usr_mod;
	}

	public void setUsr_mod(String usr_mod) {
		this.usr_mod = usr_mod;
	}

	public Date getFec_mod() {
		return fec_mod;
	}

	public void setFec_mod(Date fec_mod) {
		this.fec_mod = fec_mod;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
    
    

}
