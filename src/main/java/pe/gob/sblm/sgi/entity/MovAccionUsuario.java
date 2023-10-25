package pe.gob.sblm.sgi.entity;

// Generated 14-oct-2013 14:58:03 by Hibernate Tools 4.0.0

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Perfilmodulo generated by hbm2java
 */
@Entity
@Table(name = "MOV_ACCION_USUARIO", schema = "dbo", catalog = "sisinmueble")
public class MovAccionUsuario implements java.io.Serializable {

	private int idaccionusuario;
	private Usuario usuario;
	private MaeAccion maeAccion;


	public MovAccionUsuario() {
	}

	public MovAccionUsuario(int idaccionusuario) {
		this.idaccionusuario = idaccionusuario;
	}

	public MovAccionUsuario(int idaccionusuario, Usuario usuario, MaeAccion maeAccion) {
		this.idaccionusuario = idaccionusuario;
		this.usuario = usuario;
		this.maeAccion = maeAccion;
	}

	@Id @GeneratedValue
	@Column(name = "ID_ACCION_USUARIO", unique = true, nullable = false)
	public int getIdaccionusuario() {
		return idaccionusuario;
	}


	public void setIdaccionusuario(int idaccionusuario) {
		this.idaccionusuario = idaccionusuario;
	}
	
	
	

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IDUSUARIO")
	public Usuario getUsuario() {
		return usuario;
	}


	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_ACCION")
	public MaeAccion getMaeAccion() {
		return maeAccion;
	}


	public void setMaeAccion(MaeAccion maeAccion) {
		this.maeAccion = maeAccion;
	}
	

}