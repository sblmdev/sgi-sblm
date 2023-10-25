package pe.gob.sblm.sgi.entity;

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
@Table(name="PERIODOCONTRATO"
    ,schema="dbo"
    ,catalog="sisinmueble"
)
public class PeriodoContrato implements java.io.Serializable {

	
	private Integer id_periodo_contrato;
	private Contrato contrato;
	private Integer tipoincremento;
	private String nombretipoincremento;
	private Integer periodo;
	private Double monto;
	private Integer numerocuotas;
	private Double valorincremento;
	private Date fechainicioperiodo;
	private Date fechafinperiodo;
	private String usuariocreador;
	private String usuariomodificador;
	private Date fechacreacion;
	private Date fechamodificacion;
	private String obs_creacion;
	private String obs_modificacion;
	private Boolean estado;
	private String condicion;
		
	public PeriodoContrato() {
		
	}
	
	 @Id 
	 @GeneratedValue
	 @Column(name="ID_PERIODO_CONTRATO", unique=true, nullable=false)
	public Integer getId_periodo_contrato() {
		return id_periodo_contrato;
	}
	public void setId_periodo_contrato(Integer id_periodo_contrato) {
		this.id_periodo_contrato = id_periodo_contrato;
	}
    
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="IDCONTRATO", nullable=false)
	public Contrato getContrato() {
		return contrato;
	}
	public void setContrato(Contrato contrato) {
		this.contrato = contrato;
	}
	@Column(name="TIPOINCREMENTO")
	public Integer getTipoincremento() {
		return tipoincremento;
	}
	public void setTipoincremento(Integer tipoincremento) {
		this.tipoincremento = tipoincremento;
	}
	@Column(name="PERIODO")
	public Integer getPeriodo() {
		return periodo;
	}
	public void setPeriodo(Integer periodo) {
		this.periodo = periodo;
	}
	@Column(name="MONTO")
	public Double getMonto() {
		return monto;
	}
	public void setMonto(Double monto) {
		this.monto = monto;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="FECHAINICIOPERIODO",length=23)
	public Date getFechainicioperiodo() {
		return fechainicioperiodo;
	}
	public void setFechainicioperiodo(Date fechainicioperiodo) {
		this.fechainicioperiodo = fechainicioperiodo;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="FECHAFINPERIODO" ,length=23)
	public Date getFechafinperiodo() {
		return fechafinperiodo;
	}
	public void setFechafinperiodo(Date fechafinperiodo) {
		this.fechafinperiodo = fechafinperiodo;
	}
	@Column(name="USUARIOCREADOR")
	public String getUsuariocreador() {
		return usuariocreador;
	}
	public void setUsuariocreador(String usuariocreador) {
		this.usuariocreador = usuariocreador;
	}
	@Column(name="USUARIOMODIFICADOR")
	public String getUsuariomodificador() {
		return usuariomodificador;
	}
	public void setUsuariomodificador(String usuariomodificador) {
		this.usuariomodificador = usuariomodificador;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="FECHACREACION",length=23)
	public Date getFechacreacion() {
		return fechacreacion;
	}
	public void setFechacreacion(Date fechacreacion) {
		this.fechacreacion = fechacreacion;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="FECHAMODIFICACION",length=23)
	public Date getFechamodificacion() {
		return fechamodificacion;
	}
	public void setFechamodificacion(Date fechamodificacion) {
		this.fechamodificacion = fechamodificacion;
	}
	@Column(name="ESTADO")
	public Boolean getEstado() {
		return estado;
	}
	public void setEstado(Boolean estado) {
		this.estado = estado;
	}
	@Column(name="NOMBRETIPOINCREMENTO")
	public String getNombretipoincremento() {
		return nombretipoincremento;
	}
	public void setNombretipoincremento(String nombretipoincremento) {
		this.nombretipoincremento = nombretipoincremento;
	}
	@Column(name="OBS_CREACION")
	public String getObs_creacion() {
		return obs_creacion;
	}
	public void setObs_creacion(String obs_creacion) {
		this.obs_creacion = obs_creacion;
	}
	@Column(name="OBS_MODIFICACION")
	public String getObs_modificacion() {
		return obs_modificacion;
	}
	public void setObs_modificacion(String obs_modificacion) {
		this.obs_modificacion = obs_modificacion;
	}
	@Column(name="CONDICION")
	public String getCondicion() {
		return condicion;
	}
	public void setCondicion(String condicion) {
		this.condicion = condicion;
	}
	@Column(name="NUMEROCUOTAS")
	public Integer getNumerocuotas() {
		return numerocuotas;
	}
	public void setNumerocuotas(Integer numerocuotas) {
		this.numerocuotas = numerocuotas;
	}
	@Column(name="VALORINCREMENTO")
	public Double getValorincremento() {
		return valorincremento;
	}
	public void setValorincremento(Double valorincremento) {
		this.valorincremento = valorincremento;
	}
	
	
    
	
	
	
	
	

}
