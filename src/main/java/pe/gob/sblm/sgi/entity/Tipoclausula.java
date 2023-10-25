package pe.gob.sblm.sgi.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="TIPOCLAUSULA"
		,schema="dbo"
		,catalog="sisinmueble"
)
public class Tipoclausula implements java.io.Serializable {
    
	@Id
	@GeneratedValue
	@Column(name="ID_TC", unique=true , nullable=false)
	private String id_tc;

	@Column(name="NOMBRE")
	private String nombre;
	
	@Column(name="DESCRIPCION")
	private String descripcion;
	
	@Column(name="USRCRE" , length=50)
	private String usrcre;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="FECCRE",length=23)
	private Date feccre;
	
	@Column(name="USRMOD" , length=50)
	private String usrmod;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="FECMOD",length=23)
	private Date fecmod;
	
	@Column(name="ESTADO")
	private Boolean estado;
	

	@OneToMany(fetch=FetchType.LAZY, mappedBy="tipoclausula")
	private Set<Detalleclausula> detalleclausula = new HashSet<Detalleclausula>(0);

	public String getId_tc() {
		return id_tc;
	}

	public void setId_tc(String id_tc) {
		this.id_tc = id_tc;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
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

	public Set<Detalleclausula> getDetalleclausula() {
		return detalleclausula;
	}
	
	public void setDetalleclausula(Set<Detalleclausula> detalleclausula) {
		this.detalleclausula = detalleclausula;
	}
	
	

}
