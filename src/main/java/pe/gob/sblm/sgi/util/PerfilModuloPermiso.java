package pe.gob.sblm.sgi.util;

import javax.persistence.Entity;
import javax.persistence.Table;

//@Entity
//@Table(name = "PERFIL", schema = "dbo", catalog = "beneficenciadb")
public class PerfilModuloPermiso {

	private int idmodulo;
	private int idpermiso;
	private boolean estado;
	private String nombremodulo;
	
	private String nombrepermiso;
	
	public PerfilModuloPermiso() {
	}

	public int getIdmodulo() {
		return idmodulo;
	}

	public void setIdmodulo(int idmodulo) {
		this.idmodulo = idmodulo;
	}

	public int getIdpermiso() {
		return idpermiso;
	}

	public void setIdpermiso(int idpermiso) {
		this.idpermiso = idpermiso;
	}

	public boolean isEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}

	public String getNombremodulo() {
		return nombremodulo;
	}

	public void setNombremodulo(String nombremodulo) {
		this.nombremodulo = nombremodulo;
	}



	public String getNombrepermiso() {
		return nombrepermiso;
	}

	public void setNombrepermiso(String nombrepermiso) {
		this.nombrepermiso = nombrepermiso;
	}





}
