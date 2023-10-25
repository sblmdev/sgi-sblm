package pe.gob.sblm.sgi.entity;

import java.io.Serializable;
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


@Entity
@Table(name="INTERESMORA"
    ,schema="dbo"
    ,catalog="sisinmueble"
)
public class InteresMora implements Serializable {


	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@Column(name="ID_INTERES", unique = true, nullable = false)
    private Integer id_interes;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="ID_TIPOINTERES")
    private Tipointeresmora tipointeresmora;
	
	@Column(name="TAMN")
    private Double tamn;
	
	@Column(name="TAMEX")
    private Double tamex;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="FEC_REG" , length =23)
	private Date fec_reg;
	
	@Column(name="USR_CRE")
    private String usr_cre;
	
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FEC_CRE", length = 23)
    private Date fec_cre;
	
	@Column(name="OBS_CRE")
    private String obs_cre;
    
	@Column(name="USR_MOD")
    private String usr_mod;
    
    @Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FEC_MOD", length = 23)
    private Date fec_mod;
    
    @Column(name="OBS_MOD")
    private String obs_mod;
    
    @Column(name="ESTADO")
    private String estado;
    

	public Integer getId_interes() {
		return id_interes;
	}

	public void setId_interes(Integer id_interes) {
		this.id_interes = id_interes;
	}

	public Tipointeresmora getTipointeresmora() {
		return tipointeresmora;
	}

	public void setTipointeresmora(Tipointeresmora tipointeresmora) {
		this.tipointeresmora = tipointeresmora;
	}

	public Double getTamn() {
		return tamn;
	}

	public void setTamn(Double tamn) {
		this.tamn = tamn;
	}

	public Double getTamex() {
		return tamex;
	}

	public void setTamex(Double tamex) {
		this.tamex = tamex;
	}
    
	public Date getFec_reg() {
		return fec_reg;
	}

	public void setFec_reg(Date fec_reg) {
		this.fec_reg = fec_reg;
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

	public String getObs_cre() {
		return obs_cre;
	}

	public void setObs_cre(String obs_cre) {
		this.obs_cre = obs_cre;
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

	public String getObs_mod() {
		return obs_mod;
	}

	public void setObs_mod(String obs_mod) {
		this.obs_mod = obs_mod;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	@Override
	public String toString() {
		return "InteresMora [id_interes=" + id_interes + "+ tamn=" + tamn + ", tamex=" + tamex
				+ ", fec_reg=" + fec_reg + ", usr_cre=" + usr_cre
				+ ", fec_cre=" + fec_cre + ", obs_cre=" + obs_cre
				+ ", usr_mod=" + usr_mod + ", fec_mod=" + fec_mod
				+ ", obs_mod=" + obs_mod + ", estado=" + estado + "]";
	}

	
	
	

}
