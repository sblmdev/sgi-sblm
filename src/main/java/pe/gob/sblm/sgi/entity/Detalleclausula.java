package pe.gob.sblm.sgi.entity;

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

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="DETALLECLAUSULA"
		,schema="dbo"
		,catalog="sisinmueble")
public class Detalleclausula  {

	
	@Id
	@GeneratedValue
	@Column(name="ID_DETALLE", unique=true , nullable =false)
	private String id_detalle;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="ID_TC")
	private Tipoclausula tipoclausula;
	
	@Column(name="NOMBRE")
	private String nombre;
	
	@Column(name="DESCRIPCION")
	private String descripcion;
	
	@Column(name="USRCRE" , length=50)
	private String usrcre;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="FECCRE", length=23)
	private Date feccre;
	
	@Column(name="USRMOD" , length=50)
	private String usrmod;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="FECMOD" ,length=23)
	private Date fecmod;
	
	@Column(name="ESTADO" )
	private Boolean estado;
	

	public Detalleclausula() {
		
	}

	public Detalleclausula(String id_detalle) {
		this.id_detalle = id_detalle;
	}
	public Detalleclausula(String id_detalle, Tipoclausula tipoclausula) {
		this.id_detalle = id_detalle;
		this.tipoclausula=tipoclausula;
	}
	public String getId_detalle() {
		return id_detalle;
	}

	public void setId_detalle(String id_detalle) {
		this.id_detalle = id_detalle;
	}

	public Tipoclausula getTipoclausula() {
		return tipoclausula;
	}

	public void setTipoclausula(Tipoclausula tipoclausula) {
		this.tipoclausula = tipoclausula;
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

	@Override
	public String toString() {
		return "Detalleclausula [id_detalle=" + id_detalle + ", tipoclausula="
				+ tipoclausula + ", nombre=" + nombre + ", descripcion="
				+ descripcion + ", usrcre=" + usrcre + ", feccre=" + feccre
				+ ", usrmod=" + usrmod + ", fecmod=" + fecmod + ", estado="
				+ estado + "]";
	}

   
	
	

}
