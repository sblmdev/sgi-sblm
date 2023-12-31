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
 * Paginamodulo generated by hbm2java
 */
@Entity
@Table(name = "PAGINAPERFIL", schema = "dbo", catalog = "sisinmueble")
public class Paginaperfil implements java.io.Serializable {

	private int idpaginaperfil;
	private Pagina pagina;
	private Perfil perfil;

	@Id @GeneratedValue
	@Column(name = "IDPAGINAPERFIL", unique = true, nullable = false)
	public int getIdpaginaperfil() {
		return idpaginaperfil;
	}

	public void setIdpaginaperfil(int idpaginaperfil) {
		this.idpaginaperfil = idpaginaperfil;
	}
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IDPAGINA")
	public Pagina getPagina() {
		if(pagina==null){
			pagina=new Pagina();
			}
		return this.pagina;
	}

	public void setPagina(Pagina pagina) {
		this.pagina = pagina;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IDPERFIL")
	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	
}
