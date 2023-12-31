package pe.gob.sblm.sgi.entity;

// Generated 25-oct-2013 9:24:04 by Hibernate Tools 4.0.0

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Arbitrio generated by hbm2java
 */
@Entity
@Table(name = "ARBITRIO", schema = "dbo", catalog = "sisinmueble")
public class Arbitrio implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private int idarbitrio;
	private Upa upa;
	private Boolean sifinalizado;
	private String estado;
	private String direccion;
	private int periodo;
	private double trim1;
	private double trim2;
	private double trim3;
	private double trim4;
	private double total;
	private Date feccre;
	private Date fecmod;
	private String usrcre;
	private String usrmod;
	private String observacion;
	private String motivomod;
	private String host_ip_usrcre;
	private String host_ip_usrmod;
	


	
	private Set<Archivo> archivodocumentos = new HashSet<Archivo>(
			0);
	private Set<Detallearbitrio> detallearbitrios = new HashSet<Detallearbitrio>(
			0);
	 private Set<Cuota_arbitrio> cuotasArbitrio = new HashSet<Cuota_arbitrio>(0);
	public Arbitrio() {
	}

	public Arbitrio(int idarbitrio) {
		this.idarbitrio = idarbitrio;
	}

	public Arbitrio(int idarbitrio, String frecuenciapago,
			String clave, Date fechacreacion,
			Set<Archivo> archivodocumentos,Set<Detallearbitrio> detallearbitrios) {
		this.idarbitrio = idarbitrio;
		this.archivodocumentos = archivodocumentos;
		this.detallearbitrios = detallearbitrios;
	}

	@Id
	@GeneratedValue
	@Column(name = "IDARBITRIO", unique = true, nullable = false)
	public int getIdarbitrio() {
		return this.idarbitrio;
	}

	public void setIdarbitrio(int idarbitrio) {
		this.idarbitrio = idarbitrio;
	}



	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FECCRE", length = 23)
	public Date getFeccre() {
		return feccre;
	}

	public void setFeccre(Date feccre) {
		this.feccre = feccre;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "arbitrio")
	public Set<Archivo> getArchivodocumentos() {
		return this.archivodocumentos;
	}

	public void setArchivodocumentos(Set<Archivo> archivodocumentos) {
		this.archivodocumentos = archivodocumentos;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "arbitrio")
	public Set<Detallearbitrio> getDetallearbitrios() {
		return this.detallearbitrios;
	}

	public void setDetallearbitrios(Set<Detallearbitrio> detallearbitrios) {
		this.detallearbitrios = detallearbitrios;
	}
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FECMOD", length = 23)
	public Date getFecmod() {
		return fecmod;
	}

	public void setFecmod(Date fecmod) {
		this.fecmod = fecmod;
	}
	
	@Column(name = "USRCRE", length = 100)
	public String getUsrcre() {
		return usrcre;
	}

	public void setUsrcre(String usrcre) {
		this.usrcre = usrcre;
	}
	
	@Column(name = "USRMOD", length = 100)
	public String getUsrmod() {
		return usrmod;
	}

	public void setUsrmod(String usrmod) {
		this.usrmod = usrmod;
	}
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="IDUPA")
    public Upa getUpa() {
        return this.upa;
    }
    
    public void setUpa(Upa upa) {
        this.upa = upa;
    }
    
	@Column(name = "SIFINALIZADO")
	public Boolean getSifinalizado() {
		return sifinalizado;
	}

	public void setSifinalizado(Boolean sifinalizado) {
		this.sifinalizado = sifinalizado;
	}
	@Column(name="ESTADO", length=400)
	public String getEstado() {
		return estado;
	}



	public void setEstado(String estado) {
		this.estado = estado;
	}

	
	@Column(name = "DIRECCION")
	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	@Column(name = "PERIODO")
	public int getPeriodo() {
		return periodo;
	}

	public void setPeriodo(int periodo) {
		this.periodo = periodo;
	}
	@Column(name="TRIM1",precision=53,scale=0)
	public double getTrim1() {
		return trim1;
	}

	public void setTrim1(double trim1) {
		this.trim1 = trim1;
	}
	@Column(name="TRIM2",precision=53,scale=0)
	public double getTrim2() {
		return trim2;
	}

	public void setTrim2(double trim2) {
		this.trim2 = trim2;
	}
	@Column(name="TRIM3",precision=53,scale=0)
	public double getTrim3() {
		return trim3;
	}

	public void setTrim3(double trim3) {
		this.trim3 = trim3;
	}
	@Column(name="TRIM4",precision=53,scale=0)
	public double getTrim4() {
		return trim4;
	}

	public void setTrim4(double trim4) {
		this.trim4 = trim4;
	}
	@Column(name="TOTAL",precision=53,scale=0)
	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}
	@Column(name="OBSERVACION", length=200)
	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	
	@Column(name="MOTIVOMOD", length=1000)
	public String getMotivomod() {
		return motivomod;
	}

	public void setMotivomod(String motivomod) {
		this.motivomod = motivomod;
	}

	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="arbitrio")
	public Set<Cuota_arbitrio> getCuotasArbitrio() {
		return cuotasArbitrio;
	}

	public void setCuotasArbitrio(Set<Cuota_arbitrio> cuotasArbitrio) {
		this.cuotasArbitrio = cuotasArbitrio;
	}
	@Column(name="HOST_IP_USRCRE", length=100)
	public String getHost_ip_usrcre() {
		return host_ip_usrcre;
	}

	public void setHost_ip_usrcre(String host_ip_usrcre) {
		this.host_ip_usrcre = host_ip_usrcre;
	}
	@Column(name="HOST_IP_USRMOD", length=100)
	public String getHost_ip_usrmod() {
		return host_ip_usrmod;
	}

	public void setHost_ip_usrmod(String host_ip_usrmod) {
		this.host_ip_usrmod = host_ip_usrmod;
	}

	
	

}
