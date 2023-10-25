package pe.gob.sblm.sgi.entity;

// Generated 20/12/2013 11:02:53 AM by Hibernate Tools 4.0.0

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
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
 * Seguimientocontrato generated by hbm2java
 */
@Entity
@Table(name = "SEGUIMIENTOCONTRATO", schema = "dbo", catalog = "sisinmueble")
public class Seguimientocontrato implements java.io.Serializable {

	private int idsegcontrato;
	private Pasoperfil pasoperfil;
	private Contrato contrato;
	private Boolean siactivopaso;
	private String usercre;
	private String usermod;
	private Date feccre;
	private Date fecmod;
	private Integer numeropaso;
	private Integer idusuarioanotificar;
	private String mensajenotificacion;
private String usrruta;
	private String destinatarios;
	private String nombreperfil;
	private Set<Detallesegcontrato> detallesegcontratos = new HashSet<Detallesegcontrato>(
			0);

	public Seguimientocontrato() {
	}

	public Seguimientocontrato(int idsegcontrato) {
		this.idsegcontrato = idsegcontrato;
	}
	public Seguimientocontrato(int idsegcontrato,
			Contrato contrato, Boolean siactivopaso, String usercre,
			String usermod, Date feccre, Date fecmod, Integer numeropaso
			) {
		this.idsegcontrato = idsegcontrato;
		this.contrato = contrato;
		this.siactivopaso = siactivopaso;
		this.usercre = usercre;
		this.usermod = usermod;
		this.feccre = feccre;
		this.fecmod = fecmod;
		this.numeropaso = numeropaso;
		
	}
	public Seguimientocontrato(int idsegcontrato, Pasoperfil pasoperfil,
			Contrato contrato, Boolean siactivopaso, String usercre,
			String usermod, Date feccre, Date fecmod, Integer numeropaso,
			Integer idusuarioanotificar, String mensajenotificacion,
			String nombreperfil, Set<Detallesegcontrato> detallesegcontratos) {
		this.idsegcontrato = idsegcontrato;
		this.pasoperfil = pasoperfil;
		this.contrato = contrato;
		this.siactivopaso = siactivopaso;
		this.usercre = usercre;
		this.usermod = usermod;
		this.feccre = feccre;
		this.fecmod = fecmod;
		this.numeropaso = numeropaso;
		this.idusuarioanotificar = idusuarioanotificar;
		this.mensajenotificacion = mensajenotificacion;
		this.nombreperfil = nombreperfil;
		this.detallesegcontratos = detallesegcontratos;
	}

	@Id
	@GeneratedValue
	@Column(name = "IDSEGCONTRATO", unique = true, nullable = false)
	public int getIdsegcontrato() {
		return this.idsegcontrato;
	}

	public void setIdsegcontrato(int idsegcontrato) {
		this.idsegcontrato = idsegcontrato;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IDPASOPERFIL")
	public Pasoperfil getPasoperfil() {
		return this.pasoperfil;
	}

	public void setPasoperfil(Pasoperfil pasoperfil) {
		this.pasoperfil = pasoperfil;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IDCONTRATO")
	public Contrato getContrato() {
		return this.contrato;
	}

	public void setContrato(Contrato contrato) {
		this.contrato = contrato;
	}

	@Column(name = "SIACTIVOPASO")
	public Boolean getSiactivopaso() {
		return this.siactivopaso;
	}

	public void setSiactivopaso(Boolean siactivopaso) {
		this.siactivopaso = siactivopaso;
	}

	@Column(name = "USERCRE", length = 50)
	public String getUsercre() {
		return this.usercre;
	}

	public void setUsercre(String usercre) {
		this.usercre = usercre;
	}

	@Column(name = "DESTINATARIOS", length = 200)
	public String getDestinatarios() {
		return destinatarios;
	}

	public void setDestinatarios(String destinatarios) {
		this.destinatarios = destinatarios;
	}
	@Column(name = "USERMOD", length = 50)
	public String getUsermod() {
		return this.usermod;
	}

	public void setUsermod(String usermod) {
		this.usermod = usermod;
	}
	
	
	@Column(name = "USRRUTA", length = 200)
	public String getUsrruta() {
		return usrruta;
	}

	public void setUsrruta(String usrruta) {
		this.usrruta = usrruta;
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

	@Column(name = "NUMEROPASO")
	public Integer getNumeropaso() {
		return this.numeropaso;
	}

	public void setNumeropaso(Integer numeropaso) {
		this.numeropaso = numeropaso;
	}

	@Column(name = "IDUSUARIOANOTIFICAR")
	public Integer getIdusuarioanotificar() {
		return this.idusuarioanotificar;
	}

	public void setIdusuarioanotificar(Integer idusuarioanotificar) {
		this.idusuarioanotificar = idusuarioanotificar;
	}

	@Column(name = "MENSAJENOTIFICACION", length = 100)
	public String getMensajenotificacion() {
		return this.mensajenotificacion;
	}

	public void setMensajenotificacion(String mensajenotificacion) {
		this.mensajenotificacion = mensajenotificacion;
	}

	@Column(name = "NOMBREPERFIL", length = 100)
	public String getNombreperfil() {
		return this.nombreperfil;
	}

	public void setNombreperfil(String nombreperfil) {
		this.nombreperfil = nombreperfil;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "seguimientocontrato")
	public Set<Detallesegcontrato> getDetallesegcontratos() {
		return this.detallesegcontratos;
	}

	public void setDetallesegcontratos(
			Set<Detallesegcontrato> detallesegcontratos) {
		this.detallesegcontratos = detallesegcontratos;
	}

}