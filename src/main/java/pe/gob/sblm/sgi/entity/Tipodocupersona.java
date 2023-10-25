package pe.gob.sblm.sgi.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="TIPODOCUPERSONA"
    ,schema="dbo"
    ,catalog="sisinmueble"
)
public class Tipodocupersona implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name="IDTIPODOCUPERSONA")
	private String idtipodocupersona;
	  
	@Column(name="DESCRIPCION")
	private String descripcion;
	  
	@Column(name ="ABREVIATURA")
	private String abreviatura;
	
	@Column(name="LONGITUD")
	private Integer longitud;
	
	@Column(name="USRCRE")
	private String usrcre;
	
	@Column(name="FECCRE")
	private Date feccre;
	  
	@Column(name="ESTADO")
	private Boolean estado;

	public String getIdtipodocupersona() {
		return idtipodocupersona;
	}

	public void setIdtipodocupersona(String idtipodocupersona) {
		this.idtipodocupersona = idtipodocupersona;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getAbreviatura() {
		return abreviatura;
	}

	public void setAbreviatura(String abreviatura) {
		this.abreviatura = abreviatura;
	}

	public Integer getLongitud() {
		return longitud;
	}

	public void setLongitud(Integer longitud) {
		this.longitud = longitud;
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

	public Boolean getEstado() {
		return estado;
	}

	public void setEstado(Boolean estado) {
		this.estado = estado;
	}


    
}
