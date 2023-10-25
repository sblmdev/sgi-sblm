package pe.gob.sblm.sgi.entity;

import java.math.BigDecimal;
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
@Table(name="MAE_DET_CONDICION_DETALLE"
		,schema="dbo"
		,catalog="sisinmueble"
)

public class MaeDetCondicionDetalle implements java.io.Serializable {

	private int idcondiciondetalle;
	private MaeDetalleCondicion maedetallecondicion;
	private String idconcepto;
	private String concepto;
	private String mes;
	private String anio;
	private Double monto;
	private String tipomoneda;
	private Integer numeromes;
	private Integer secuencia;
	private int idcontrato;
	private Integer iddetallecondicion;
	private String usrcre;
	private Date feccre;
	private String obs;
	private String host_ip_cre;
	private String usrmod;
	private Date fecmod;
	private String obs_mod;
	private String host_ip_mod;
	private Boolean sianulado; // 0 =activo  1//anulado  	
	private String usr_anula;
	private Date fec_anula;
	private String motivo_anula;
	private String host_ip_anula;
	private String estado; // REGISTRADO // ANULADO
	
	public MaeDetCondicionDetalle() {
		
	}
    	
	public MaeDetCondicionDetalle(int idcondiciondetalle,
			MaeDetalleCondicion maedetallecondicion, String idconcepto,
			String concepto, String mes, String anio,
			Double monto, String tipomoneda, Integer numeromes,
			Integer secuencia, Integer idcontrato, Integer iddetallecondicion,
			String usrcre, Date feccre, String obs, String host_ip_cre,
			String usrmod, Date fecmod, String obs_mod, String host_ip_mod,
			Boolean sianulado, String usr_anula, Date fec_anula,
			String motivo_anula, String host_ip_anula, String estado) {
		this.idcondiciondetalle = idcondiciondetalle;
		this.maedetallecondicion = maedetallecondicion;
		this.idconcepto = idconcepto;
		this.concepto = concepto;
		this.mes = mes;
		this.anio = anio;
		this.monto = monto;
		this.tipomoneda = tipomoneda;
		this.numeromes = numeromes;
		this.secuencia = secuencia;
		this.idcontrato = idcontrato;
		this.iddetallecondicion = iddetallecondicion;
		this.usrcre = usrcre;
		this.feccre = feccre;
		this.obs = obs;
		this.host_ip_cre = host_ip_cre;
		this.usrmod = usrmod;
		this.fecmod = fecmod;
		this.obs_mod = obs_mod;
		this.host_ip_mod = host_ip_mod;
		this.sianulado = sianulado;
		this.usr_anula = usr_anula;
		this.fec_anula = fec_anula;
		this.motivo_anula = motivo_anula;
		this.host_ip_anula = host_ip_anula;
		this.estado = estado;
	}



	@Id
    @GeneratedValue
    @Column(name="ID_CONDICION_DETALLE", unique=true, nullable=false)
	public int getIdcondiciondetalle() {
		return idcondiciondetalle;
	}

	public void setIdcondiciondetalle(int idcondiciondetalle) {
		this.idcondiciondetalle = idcondiciondetalle;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ID_CONDICION",nullable=false)
	public MaeDetalleCondicion getMaedetallecondicion() {
		return maedetallecondicion;
	}

	public void setMaedetallecondicion(MaeDetalleCondicion maedetallecondicion) {
		this.maedetallecondicion = maedetallecondicion;
	}
	
		
    @Column(name="MES", length=12)
	public String getMes() {
		return mes;
	}




	public void setMes(String mes) {
		this.mes = mes;
	}
    @Column(name="ANIO",length=4)
	public String getAnio() {
		return anio;
	}

	public void setAnio(String anio) {
		this.anio = anio;
	}
    @Column(name="MONTO")
	public Double getMonto() {
		return monto;
	}

	public void setMonto(Double monto) {
		this.monto = monto;
	}
    @Column(name="TIPOMONEDA",length=1)
	public String getTipomoneda() {
		return tipomoneda;
	}

	public void setTipomoneda(String tipomoneda) {
		this.tipomoneda = tipomoneda;
	}
    @Column(name="NUMEROMES")
	public Integer getNumeromes() {
		return numeromes;
	}

	public void setNumeromes(Integer numeromes) {
		this.numeromes = numeromes;
	}
    @Column(name="SECUENCIA")
	public Integer getSecuencia() {
		return secuencia;
	}

	public void setSecuencia(Integer secuencia) {
		this.secuencia = secuencia;
	}
    @Column(name="USRCRE", length=100)
	public String getUsrcre() {
		return usrcre;
	}

	public void setUsrcre(String usrcre) {
		this.usrcre = usrcre;
	}
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="FECCRE" , length=23)
	public Date getFeccre() {
		return feccre;
	}

	public void setFeccre(Date feccre) {
		this.feccre = feccre;
	}
    @Column(name="OBS",length=1000)
	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}
	@Column(name="USRMOD", length=100)
	public String getUsrmod() {
		return usrmod;
	}

	public void setUsrmod(String usrmod) {
		this.usrmod = usrmod;
	}
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="FECMOD" ,length=23)
	public Date getFecmod() {
		return fecmod;
	}

	public void setFecmod(Date fecmod) {
		this.fecmod = fecmod;
	}
	@Column(name="OBS_MOD",length=1000)
	public String getObs_mod() {
		return obs_mod;
	}

	public void setObs_mod(String obs_mod) {
		this.obs_mod = obs_mod;
	}
    @Column(name="ESTADO",length=10)
	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
	@Column(name="IDCONCEPTO",length=2)
	public String getIdconcepto() {
		return idconcepto;
	}

	public void setIdconcepto(String idconcepto) {
		this.idconcepto = idconcepto;
	}
	@Column(name="CONCEPTO",length=200)
	public String getConcepto() {
		return concepto;
	}

	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}
	@Column(name="IDCONTRATO")
	public int getIdcontrato() {
		return idcontrato;
	}

	public void setIdcontrato(int idcontrato) {
		this.idcontrato = idcontrato;
	}
	@Column(name="IDDETALLECONDICION")
	public Integer getIddetallecondicion() {
		return iddetallecondicion;
	}

	public void setIddetallecondicion(Integer iddetallecondicion) {
		this.iddetallecondicion = iddetallecondicion;
	}

    @Column(name="HOST_IP_CRE" , length=100)
	public String getHost_ip_cre() {
		return host_ip_cre;
	}


	public void setHost_ip_cre(String host_ip_cre) {
		this.host_ip_cre = host_ip_cre;
	}

    @Column(name="HOST_IP_MOD" , length=100)
	public String getHost_ip_mod() {
		return host_ip_mod;
	}


	public void setHost_ip_mod(String host_ip_mod) {
		this.host_ip_mod = host_ip_mod;
	}

    @Column(name="SIANULADO")
	public Boolean getSianulado() {
		return sianulado;
	}


	public void setSianulado(Boolean sianulado) {
		this.sianulado = sianulado;
	}

    @Column(name="USR_ANULA" , length=100)
	public String getUsr_anula() {
		return usr_anula;
	}


	public void setUsr_anula(String usr_anula) {
		this.usr_anula = usr_anula;
	}

    @Column(name="FEC_ANULA")
	public Date getFec_anula() {
		return fec_anula;
	}


	public void setFec_anula(Date fec_anula) {
		this.fec_anula = fec_anula;
	}

    @Column(name="MOTIVO_ANULA" , length=1000)
	public String getMotivo_anula() {
		return motivo_anula;
	}


	public void setMotivo_anula(String motivo_anula) {
		this.motivo_anula = motivo_anula;
	}

    @Column(name="HOST_IP_ANULA" , length=100)
	public String getHost_ip_anula() {
		return host_ip_anula;
	}


	public void setHost_ip_anula(String host_ip_anula) {
		this.host_ip_anula = host_ip_anula;
	}
	
	
	
}
