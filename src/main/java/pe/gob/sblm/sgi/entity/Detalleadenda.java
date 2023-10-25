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
@Table(name="DETALLEADENDA"
      ,schema="dbo"
      ,catalog="sisinmueble"
		)
public class Detalleadenda implements java.io.Serializable{

	private Integer id_detalleadenda;
	private Adenda adenda;
	private String mes;
	private String anio;
	private Double contraprestacion;
	private Double renta;
	private Double montopagar;
	private Integer secuencia;
	private Integer numeromes;
	private String motivodetalle;
	private String usrcre;
	private Date feccre;
	private String obscre;
	private String host_ip_usrcre;
	private String usrmod;
	private Date fecmod;
	private String obsmod;
	private String host_ip_usrmod;
	private String estado;
	
	@Id
	@GeneratedValue
	@Column(name="ID_DETALLEADENDA",unique=true , nullable=false)
	public Integer getId_detalleadenda() {
		return id_detalleadenda;
	}
	public void setId_detalleadenda(Integer id_detalleadenda) {
		this.id_detalleadenda = id_detalleadenda;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="IDADENDA" , nullable=false)
	public Adenda getAdenda() {
		return adenda;
	}
	public void setAdenda(Adenda adenda) {
		this.adenda = adenda;
	}
	@Column(name="MES")
	public String getMes() {
		return mes;
	}
	public void setMes(String mes) {
		this.mes = mes;
	}
	@Column(name="ANIO")
	public String getAnio() {
		return anio;
	}
	public void setAnio(String anio) {
		this.anio = anio;
	}
	@Column(name="CONTRAPRESTACION")
	public Double getContraprestacion() {
		return contraprestacion;
	}
	public void setContraprestacion(Double contraprestacion) {
		this.contraprestacion = contraprestacion;
	}
	@Column(name="RENTA")
	public Double getRenta() {
		return renta;
	}
	public void setRenta(Double renta) {
		this.renta = renta;
	}
	@Column(name="MONTOPAGAR")
	public Double getMontopagar() {
		return montopagar;
	}
	public void setMontopagar(Double montopagar) {
		this.montopagar = montopagar;
	}
	@Column(name="SECUENCIA")
	public Integer getSecuencia() {
		return secuencia;
	}
	public void setSecuencia(Integer secuencia) {
		this.secuencia = secuencia;
	}
	@Column(name="NUMEROMES")
	public Integer getNumeromes() {
		return numeromes;
	}
	public void setNumeromes(Integer numeromes) {
		this.numeromes = numeromes;
	}
	@Column(name="MOTIVODETALLE")
	public String getMotivodetalle() {
		return motivodetalle;
	}
	public void setMotivodetalle(String motivodetalle) {
		this.motivodetalle = motivodetalle;
	}
	@Column(name="USRCRE")
	public String getUsrcre() {
		return usrcre;
	}
	public void setUsrcre(String usrcre) {
		this.usrcre = usrcre;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="FECCRE", length=23)
	public Date getFeccre() {
		return feccre;
	}
	public void setFeccre(Date feccre) {
		this.feccre = feccre;
	}
	@Column(name="OBSCRE")
	public String getObscre() {
		return obscre;
	}
	public void setObscre(String obscre) {
		this.obscre = obscre;
	}
	@Column(name="HOST_IP_USRCRE")
	public String getHost_ip_usrcre() {
		return host_ip_usrcre;
	}
	public void setHost_ip_usrcre(String host_ip_usrcre) {
		this.host_ip_usrcre = host_ip_usrcre;
	}
	@Column(name="USRMOD")
	public String getUsrmod() {
		return usrmod;
	}
	public void setUsrmod(String usrmod) {
		this.usrmod = usrmod;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="FECMOD", length=23)
	public Date getFecmod() {
		return fecmod;
	}
	public void setFecmod(Date fecmod) {
		this.fecmod = fecmod;
	}
	@Column(name="OBSMOD")
	public String getObsmod() {
		return obsmod;
	}
	public void setObsmod(String obsmod) {
		this.obsmod = obsmod;
	}
	@Column(name="HOST_IP_USRMOD")
	public String getHost_ip_usrmod() {
		return host_ip_usrmod;
	}
	public void setHost_ip_usrmod(String host_ip_usrmod) {
		this.host_ip_usrmod = host_ip_usrmod;
	}
	@Column(name="ESTADO")
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}


}
