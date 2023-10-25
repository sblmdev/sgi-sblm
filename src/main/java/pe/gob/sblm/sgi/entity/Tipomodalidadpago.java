package pe.gob.sblm.sgi.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="TIPOMODALIDADPAGO"
    ,schema="dbo"
    ,catalog="sisinmueble"
)

public class Tipomodalidadpago implements java.io.Serializable {
    
	private String idtipomodalidadpago;
	private String descripciontipo;
	
	public Tipomodalidadpago() {
		
	}
    
	
	public Tipomodalidadpago(String idtipomodalidadpago) {
		this.idtipomodalidadpago = idtipomodalidadpago;
	}


	public Tipomodalidadpago(String idtipomodalidadpago, String descripciontipo) {
		this.idtipomodalidadpago = idtipomodalidadpago;
		this.descripciontipo = descripciontipo;
	}


	@Id 
    @Column(name="IDTIPOMODALIDADPAGO", unique=true, nullable=false)
	public String getIdtipomodalidadpago() {
		return idtipomodalidadpago;
	}

	public void setIdtipomodalidadpago(String idtipomodalidadpago) {
		this.idtipomodalidadpago = idtipomodalidadpago;
	}
	
	@Column(name="DESCRIPCIONTIPO", length=150)
	public String getDescripciontipo() {
		return descripciontipo;
	}

	public void setDescripciontipo(String descripciontipo) {
		this.descripciontipo = descripciontipo;
	}
    
	
	
	
	
}
