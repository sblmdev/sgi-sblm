package pe.gob.sblm.sgi.entity;

// Generated 03/01/2014 03:09:25 PM by Hibernate Tools 4.0.0

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Exportardato generated by hbm2java
 */
@Entity
@Table(name = "EXPORTARDATO", schema = "dbo", catalog = "sisinmueble")
public class Exportardato implements java.io.Serializable {

	private int idexportardato;
	private Detallecuota detallecuota;
	private String codInmb;
	private String numContr;
	private String inquilino;
	private String numRuc;
	private String aa;
	private String mm;
	private String tipDocu;
	private String nroDocu;
	private Date fchPago;
	private String tipMovi;
	private String tipPago;
	private BigDecimal renta;
	private BigDecimal mora;
	private Short codCob;
	private Integer idigv;

	public Exportardato() {
	}

	public Exportardato(int idexportardato) {
		this.idexportardato = idexportardato;
	}

	public Exportardato(int idexportardato, Detallecuota detallecuota,
			String codInmb, String numContr, String inquilino, String numRuc,
			String aa, String mm, String tipDocu, String nroDocu, Date fchPago,
			String tipMovi, String tipPago, BigDecimal renta, BigDecimal mora,
			Short codCob, Integer idigv) {
		this.idexportardato = idexportardato;
		this.detallecuota = detallecuota;
		this.codInmb = codInmb;
		this.numContr = numContr;
		this.inquilino = inquilino;
		this.numRuc = numRuc;
		this.aa = aa;
		this.mm = mm;
		this.tipDocu = tipDocu;
		this.nroDocu = nroDocu;
		this.fchPago = fchPago;
		this.tipMovi = tipMovi;
		this.tipPago = tipPago;
		this.renta = renta;
		this.mora = mora;
		this.codCob = codCob;
		this.idigv = idigv;
	}

	@Id
	@Column(name = "IDEXPORTARDATO", unique = true, nullable = false)
	public int getIdexportardato() {
		return this.idexportardato;
	}

	public void setIdexportardato(int idexportardato) {
		this.idexportardato = idexportardato;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IDDETALLECUOTA")
	public Detallecuota getDetallecuota() {
		return this.detallecuota;
	}

	public void setDetallecuota(Detallecuota detallecuota) {
		this.detallecuota = detallecuota;
	}

	@Column(name = "COD_INMB", length = 11)
	public String getCodInmb() {
		return this.codInmb;
	}

	public void setCodInmb(String codInmb) {
		this.codInmb = codInmb;
	}

	@Column(name = "NUM_CONTR", length = 8)
	public String getNumContr() {
		return this.numContr;
	}

	public void setNumContr(String numContr) {
		this.numContr = numContr;
	}

	@Column(name = "INQUILINO", length = 45)
	public String getInquilino() {
		return this.inquilino;
	}

	public void setInquilino(String inquilino) {
		this.inquilino = inquilino;
	}

	@Column(name = "NUM_RUC", length = 11)
	public String getNumRuc() {
		return this.numRuc;
	}

	public void setNumRuc(String numRuc) {
		this.numRuc = numRuc;
	}

	@Column(name = "AA", length = 4)
	public String getAa() {
		return this.aa;
	}

	public void setAa(String aa) {
		this.aa = aa;
	}

	@Column(name = "MM", length = 2)
	public String getMm() {
		return this.mm;
	}

	public void setMm(String mm) {
		this.mm = mm;
	}

	@Column(name = "TIP_DOCU", length = 2)
	public String getTipDocu() {
		return this.tipDocu;
	}

	public void setTipDocu(String tipDocu) {
		this.tipDocu = tipDocu;
	}

	@Column(name = "NRO_DOCU", length = 10)
	public String getNroDocu() {
		return this.nroDocu;
	}

	public void setNroDocu(String nroDocu) {
		this.nroDocu = nroDocu;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FCH_PAGO", length = 23)
	public Date getFchPago() {
		return this.fchPago;
	}

	public void setFchPago(Date fchPago) {
		this.fchPago = fchPago;
	}

	@Column(name = "TIP_MOVI", length = 2)
	public String getTipMovi() {
		return this.tipMovi;
	}

	public void setTipMovi(String tipMovi) {
		this.tipMovi = tipMovi;
	}

	@Column(name = "TIP_PAGO", length = 2)
	public String getTipPago() {
		return this.tipPago;
	}

	public void setTipPago(String tipPago) {
		this.tipPago = tipPago;
	}

	@Column(name = "RENTA", precision = 14)
	public BigDecimal getRenta() {
		return this.renta;
	}

	public void setRenta(BigDecimal renta) {
		this.renta = renta;
	}

	@Column(name = "MORA", precision = 10)
	public BigDecimal getMora() {
		return this.mora;
	}

	public void setMora(BigDecimal mora) {
		this.mora = mora;
	}

	@Column(name = "COD_COB", precision = 3, scale = 0)
	public Short getCodCob() {
		return this.codCob;
	}

	public void setCodCob(Short codCob) {
		this.codCob = codCob;
	}

	@Column(name = "IDIGV")
	public Integer getIdigv() {
		return this.idigv;
	}

	public void setIdigv(Integer idigv) {
		this.idigv = idigv;
	}

}
