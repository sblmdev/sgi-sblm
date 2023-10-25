package pe.gob.sblm.sgi.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Cartera generated by hbm2java
 */
@Entity
@Table(name="FECHAPAGO"
    ,schema="dbo"
    ,catalog="sisinmueble"
)
public class Fechapago {
	
	
	@Id 
    @GeneratedValue
    @Column(name="ID_FECHAPAGO", unique=true, nullable=false)
	private int id_fechapago ;
	
	@Column(name="D_FECHA_PAGO")
	private String d_fecha_pago;
	
	@Column(name="ESTADO")
	private Boolean estado ;

   
	
	
	
	public Fechapago(int id_fechapago) {
	
		this.id_fechapago = id_fechapago;
	}



	public int getId_fechapago() {
		return id_fechapago;
	}



	public void setId_fechapago(int id_fechapago) {
		this.id_fechapago = id_fechapago;
	}



	public String getD_fecha_pago() {
		return d_fecha_pago;
	}



	public void setD_fecha_pago(String d_fecha_pago) {
		this.d_fecha_pago = d_fecha_pago;
	}



	public Boolean getEstado() {
		return estado;
	}



	public void setEstado(Boolean estado) {
		this.estado = estado;
	}



	public Fechapago() {
		// TODO Auto-generated constructor stub
	}

}
