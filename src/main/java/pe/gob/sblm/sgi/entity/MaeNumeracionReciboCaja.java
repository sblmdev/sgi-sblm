package pe.gob.sblm.sgi.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name="MAE_NUMERACION_RECIBOCAJA"
    ,schema="dbo"
    ,catalog="sisinmueble"
)
public class MaeNumeracionReciboCaja  implements java.io.Serializable{
    private Integer id_numeracion_recibocaja;
    private MaeSerieReciboCaja maeserierecibocaja;
    private String numeracion;
    private Boolean l_usado;
	public MaeNumeracionReciboCaja() {
		// TODO Auto-generated constructor stub
	}
	
	public MaeNumeracionReciboCaja(Integer id_numeracion_recibocaja) {
		this.id_numeracion_recibocaja = id_numeracion_recibocaja;
	}
	@Id 
    @Column(name="ID_NUMERACION_RECIBOCAJA", unique=true, nullable=false)
	public Integer getId_numeracion_recibocaja() {
		return id_numeracion_recibocaja;
	}
	public void setId_numeracion_recibocaja(Integer id_numeracion_recibocaja) {
		this.id_numeracion_recibocaja = id_numeracion_recibocaja;
	}
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="ID_SERIE_RECIBOCAJA")
	public MaeSerieReciboCaja getMaeserierecibocaja() {
		return maeserierecibocaja;
	}
	public void setMaeserierecibocaja(MaeSerieReciboCaja maeserierecibocaja) {
		this.maeserierecibocaja = maeserierecibocaja;
	}
	@Column(name="NUMERACION", length=10)
	public String getNumeracion() {
		return numeracion;
	}
	public void setNumeracion(String numeracion) {
		this.numeracion = numeracion;
	}
	@Column(name="L_USADO")
	public Boolean getL_usado() {
		return l_usado;
	}
	public void setL_usado(Boolean l_usado) {
		this.l_usado = l_usado;
	}

	
}
