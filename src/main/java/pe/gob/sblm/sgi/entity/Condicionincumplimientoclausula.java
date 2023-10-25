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

import pe.gob.sblm.sgi.entity.Detalleclausula;

@Entity
@Table(name="CONDICIONINCUMPLIMIENTOCLAUSULA"
    ,schema="dbo"
    ,catalog="sisinmueble"
)
public class Condicionincumplimientoclausula {

	 @Id
	 @GeneratedValue
	 @Column(name="ID_CONDICION_INCUMPLIMIENTO")
	 private int id_condicion_incumplimiento;
	
	 @ManyToOne(fetch=FetchType.LAZY)
	 @JoinColumn(name="ID_CONTRATO")
	 private Contrato contrato ;
	 
	 @ManyToOne(fetch= FetchType.EAGER)
	 @JoinColumn(name="ID_DETALLE")
	 private Detalleclausula detalleclausula;
	 
	 @Column(name="VALOR_PORCENTAJE")
	 private Double valor_porcentaje;
	 
	 @Column(name="EJECUCION_PERIODICA")
	 private String ejecucion_periodica;
	 
	 @Column(name="USRCRE", length=50)
	 private String usrcre;
	 
	 @Temporal(TemporalType.TIMESTAMP)
	 @Column(name="FECCRE", length=23)
	 private Date feccre;
	 
	 @Column(name="USRMOD", length=50)
	 private String usrmod;

	 
	 @Temporal(TemporalType.TIMESTAMP)
	 @Column(name="FECMOD", length=23)
	 private Date fecmod;

	 @Column(name="ESTADO")
	 private Boolean estado;

	public int getId_condicion_incumplimiento() {
		return id_condicion_incumplimiento;
	}

	public void setId_condicion_incumplimiento(int id_condicion_incumplimiento) {
		this.id_condicion_incumplimiento = id_condicion_incumplimiento;
	}

	public Contrato getContrato() {
		return contrato;
	}

	public void setContrato(Contrato contrato) {
		this.contrato = contrato;
	}

	public Detalleclausula getDetalleclausula() {
		return detalleclausula;
	}

	public void setDetalleclausula(Detalleclausula detalleclausula) {
		this.detalleclausula = detalleclausula;
	}

	public Double getValor_porcentaje() {
		return valor_porcentaje;
	}

	public void setValor_porcentaje(Double valor_porcentaje) {
		this.valor_porcentaje = valor_porcentaje;
	}

	public String getUsrcre() {
		return usrcre;
	}

	public void setUsrcre(String usrcre) {
		this.usrcre = usrcre;
	}

	public Date getFeccre() {
		return feccre;
	}

	public void setFeccre(Date feccre) {
		this.feccre = feccre;
	}

	public String getUsrmod() {
		return usrmod;
	}

	public void setUsrmod(String usrmod) {
		this.usrmod = usrmod;
	}

	public Date getFecmod() {
		return fecmod;
	}

	public void setFecmod(Date fecmod) {
		this.fecmod = fecmod;
	}

	public Boolean getEstado() {
		return estado;
	}

	public void setEstado(Boolean estado) {
		this.estado = estado;
	}

	public String getEjecucion_periodica() {
		return ejecucion_periodica;
	}

	public void setEjecucion_periodica(String ejecucion_periodica) {
		this.ejecucion_periodica = ejecucion_periodica;
	}

	@Override
	public String toString() {
		return "Condicionincumplimientoclausula [id_condicion_incumplimiento="
				+ id_condicion_incumplimiento + ", contrato=" + contrato
				+ ", detalleclausula=" + detalleclausula
				+ ", valor_porcentaje=" + valor_porcentaje
				+ ", ejecucion_periodica=" + ejecucion_periodica + ", usrcre="
				+ usrcre + ", feccre=" + feccre + ", usrmod=" + usrmod
				+ ", fecmod=" + fecmod + ", estado=" + estado + "]";
	}
	 
	 

}
