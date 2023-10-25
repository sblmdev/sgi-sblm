package pe.gob.sblm.sgi.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="MAE_SERIE_RECIBOCAJA"
    ,schema="dbo"
    ,catalog="sisinmueble")
public class MaeSerieReciboCaja implements java.io.Serializable{
	
	private Integer id_serie_recibocaja;
	private Tipodocu tipodocu;
	private String serie;
	
	public MaeSerieReciboCaja() {
		// TODO Auto-generated constructor stub
	}
	
	public MaeSerieReciboCaja(Integer id_serie_recibocaja) {
		this.id_serie_recibocaja = id_serie_recibocaja;
	}

	@Id 
    @Column(name="ID_SERIE_RECIBOCAJA", unique=true, nullable=false)
	public Integer getId_serie_recibocaja() {
		return id_serie_recibocaja;
	}

	public void setId_serie_recibocaja(Integer id_serie_recibocaja) {
		this.id_serie_recibocaja = id_serie_recibocaja;
	}

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="IDTIPODOCU")
	public Tipodocu getTipodocu() {
		return tipodocu;
	}

	public void setTipodocu(Tipodocu tipodocu) {
		this.tipodocu = tipodocu;
	}
    
	@Column(name="SERIE", length=4)
	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	
	
	
	
}
