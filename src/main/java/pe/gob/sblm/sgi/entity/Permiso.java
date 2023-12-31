package pe.gob.sblm.sgi.entity;

// Generated 14-oct-2013 14:58:03 by Hibernate Tools 4.0.0

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

/**
 * Permiso generated by hbm2java
 */
@Entity
@Table(name = "PERMISO", schema = "dbo", catalog = "sisinmueble")
public class Permiso implements java.io.Serializable {

	private int idpermiso;
	private String valcrud;
	private String descripcur;
	private Date feccre;
	private Date fecmod;
	private String usr;
	private Boolean tipo;
	private Set<Perfilmodulo> perfilmodulos = new HashSet<Perfilmodulo>(0);

	public Permiso() {
	}

	public Permiso(int idpermiso) {
		this.idpermiso = idpermiso;
	}

	public Permiso(int idpermiso, String valcrud, String descripcur,
			Date feccre, Date fecmod, String usr, Boolean tipo,
			Set<Perfilmodulo> perfilmodulos) {
		this.idpermiso = idpermiso;
		this.valcrud = valcrud;
		this.descripcur = descripcur;
		this.feccre = feccre;
		this.fecmod = fecmod;
		this.usr = usr;
		this.tipo = tipo;
		this.perfilmodulos = perfilmodulos;
	}

	@Id @GeneratedValue
	@Column(name = "IDPERMISO", unique = true, nullable = false)
	public int getIdpermiso() {
		return this.idpermiso;
	}

	public void setIdpermiso(int idpermiso) {
		this.idpermiso = idpermiso;
	}

	@Column(name = "VALCRUD", length = 20)
	public String getValcrud() {
		return this.valcrud;
	}

	public void setValcrud(String valcrud) {
		this.valcrud = valcrud;
	}

	@Column(name = "DESCRIPCUR", length = 50)
	public String getDescripcur() {
		return this.descripcur;
	}

	public void setDescripcur(String descripcur) {
		this.descripcur = descripcur;
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

	@Column(name = "USR", length = 50)
	public String getUsr() {
		return this.usr;
	}

	public void setUsr(String usr) {
		this.usr = usr;
	}

	@Column(name = "TIPO")
	public Boolean getTipo() {
		return this.tipo;
	}

	public void setTipo(Boolean tipo) {
		this.tipo = tipo;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "permiso")
	public Set<Perfilmodulo> getPerfilmodulos() {
		return this.perfilmodulos;
	}

	public void setPerfilmodulos(Set<Perfilmodulo> perfilmodulos) {
		this.perfilmodulos = perfilmodulos;
	}

}
