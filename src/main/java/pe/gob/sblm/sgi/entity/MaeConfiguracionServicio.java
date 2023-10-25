package pe.gob.sblm.sgi.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="MAE_CONFIGURACION_SERVICIO", schema = "dbo", catalog = "sisinmueble")
public class MaeConfiguracionServicio implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name="N_ID_CONFIGURACION_SERVICIO")
	private int nIdConfiguracionServicio;

	@Column(name="C_PARAMETRO")
	private String cParametro;

    @Column(name="C_TABLA")
	private String cTabla;

	
	@Column(name="L_ACTIVO")
	private String lActivo;
	

	@Column(name="X_DESCRIPCION")
	private String xDescripcion;

	@Column(name="X_VALOR")
	private String xValor;	

	public MaeConfiguracionServicio() {
	}

	public int getNIdConfiguracionServicio() {
		return this.nIdConfiguracionServicio;
	}

	public void setNIdConfiguracionServicio(int nIdConfiguracionServicio) {
		this.nIdConfiguracionServicio = nIdConfiguracionServicio;
	}

    public String getCParametro() {
    	return this.cParametro;
	}

	public void setCParametro(String cParametro) {
		this.cParametro = cParametro;
	}

	public String getCTabla() {
		return this.cTabla;
	}

	public void setCTabla(String cTabla) {
		this.cTabla = cTabla;
	}


	public String getLActivo() {
		return this.lActivo;
	}

	public void setLActivo(String lActivo) {
		this.lActivo = lActivo;
	}


	public String getXDescripcion() {
		return this.xDescripcion;
	}

	public void setXDescripcion(String xDescripcion) {
		this.xDescripcion = xDescripcion;
	}


	public String getXValor() {
		return this.xValor;
	}

	public void setXValor(String xValor) {
		this.xValor = xValor;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MaeConfiguracionServicio [nIdConfiguracionServicio="
				+ nIdConfiguracionServicio + ", cParametro=" + cParametro
				+ ", cTabla=" + cTabla + ", lActivo=" + lActivo
				+ ", xDescripcion=" + xDescripcion + ", xValor=" + xValor + "]";
	}

}