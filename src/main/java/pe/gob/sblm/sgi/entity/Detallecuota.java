package pe.gob.sblm.sgi.entity;
// Generated 25/08/2014 09:36:23 AM by Hibernate Tools 3.6.0


import java.util.Date;
import java.util.HashSet;
import java.util.Set;
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

/**
 * Detallecuota generated by hbm2java
 */
@Entity
@Table(name="DETALLECUOTA"
    ,schema="dbo"
    ,catalog="sisinmueble"
)
public class Detallecuota  implements java.io.Serializable {


     private int iddetallecuota;
     private Cuota cuota;
     private Comprobantepago comprobantepago;
     private Double montosoles;
     private Double montodolar;
     private String usrcre;
     private Date feccre;
     private Double mora;
     private Double tipocambio;
     private String mes;
     private Integer anio;
     private Integer numeromes;
     private String cancelado;
     private String observacion;
     private Set<Exportardato> exportardatos = new HashSet<Exportardato>(0);
     private MaeDetCondicionDetalle maedcondiciondetalle;
     private Double montopenalizacion;
     private String usuario_anula;
     private Date fecha_anula;
     private String host_ip_anula;
     private String descripcion_anula;
     private String estado;
     private String condicion;

    public Detallecuota() {
    }

	
   
     @Id 
     @GeneratedValue
    @Column(name="IDDETALLECUOTA", unique=true, nullable=false)
    public int getIddetallecuota() {
        return this.iddetallecuota;
    }
    
    public void setIddetallecuota(int iddetallecuota) {
        this.iddetallecuota = iddetallecuota;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="IDCUOTA")
    public Cuota getCuota() {
        return this.cuota;
    }
    
    public void setCuota(Cuota cuota) {
        this.cuota = cuota;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="IDCOMPROBANTE")
    public Comprobantepago getComprobantepago() {
        return this.comprobantepago;
    }
    
    public void setComprobantepago(Comprobantepago comprobantepago) {
        this.comprobantepago = comprobantepago;
    }

    
    @Column(name="MONTOSOLES", precision=53, scale=0)
    public Double getMontosoles() {
        return this.montosoles;
    }
    
    public void setMontosoles(Double montosoles) {
        this.montosoles = montosoles;
    }
    
    @Column(name="MONTODOLAR", precision=53, scale=0)
    public Double getMontodolar() {
        return this.montodolar;
    }
    
    public void setMontodolar(Double montodolar) {
        this.montodolar = montodolar;
    }

    
    @Column(name="USRCRE", length=50)
    public String getUsrcre() {
        return this.usrcre;
    }
    
    public void setUsrcre(String usrcre) {
        this.usrcre = usrcre;
    }



    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="FECCRE", length=23)
    public Date getFeccre() {
        return this.feccre;
    }
    
    public void setFeccre(Date feccre) {
        this.feccre = feccre;
    }

    
    @Column(name="MORA", precision=53, scale=0)
    public Double getMora() {
        return this.mora;
    }
    
    public void setMora(Double mora) {
        this.mora = mora;
    }

    @Column(name="TIPOCAMBIO", precision=53, scale=0)
    public Double getTipocambio() {
        return this.tipocambio;
    }
    
    public void setTipocambio(Double tipocambio) {
        this.tipocambio = tipocambio;
    }

    
    @Column(name="MES", length=15)
    public String getMes() {
        return this.mes;
    }
    
    public void setMes(String mes) {
        this.mes = mes;
    }

    
    @Column(name="ANIO")
    public Integer getAnio() {
        return this.anio;
    }
    
    public void setAnio(Integer anio) {
        this.anio = anio;
    }
    
    
    @Column(name="NUMEROMES")
    public Integer getNumeromes() {
		return numeromes;
	}



	public void setNumeromes(Integer numeromes) {
		this.numeromes = numeromes;
	}


	@Column(name="CANCELADO")
	public String getCancelado() {
		return cancelado;
	}



	public void setCancelado(String cancelado) {
		this.cancelado = cancelado;
	}

   
	@Column(name="OBSERVACION")
	public String getObservacion() {
		return observacion;
	}



	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}



	@OneToMany(fetch=FetchType.LAZY, mappedBy="detallecuota")
    public Set<Exportardato> getExportardatos() {
        return this.exportardatos;
    }
    
    public void setExportardatos(Set<Exportardato> exportardatos) {
        this.exportardatos = exportardatos;
    }

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="ID_MAED_CONDICION_DETALLE")
	public MaeDetCondicionDetalle getMaedcondiciondetalle() {
		return maedcondiciondetalle;
	}

	public void setMaedcondiciondetalle(MaeDetCondicionDetalle maedcondiciondetalle) {
		this.maedcondiciondetalle = maedcondiciondetalle;
	}

    @Column(name="USUARIO_ANULA")
	public String getUsuario_anula() {
		return usuario_anula;
	}

	public void setUsuario_anula(String usuario_anula) {
		this.usuario_anula = usuario_anula;
	}
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name="FECHA_ANULA",length=23)
	public Date getFecha_anula() {
		return fecha_anula;
	}

	public void setFecha_anula(Date fecha_anula) {
		this.fecha_anula = fecha_anula;
	}
    @Column (name="HOST_IP_ANULA")
	public String getHost_ip_anula() {
		return host_ip_anula;
	}

	public void setHost_ip_anula(String host_ip_anula) {
		this.host_ip_anula = host_ip_anula;
	}
    @Column(name="DESCRIPCION_ANULA")
	public String getDescripcion_anula() {
		return descripcion_anula;
	}

	public void setDescripcion_anula(String descripcion_anula) {
		this.descripcion_anula = descripcion_anula;
	}
    @Column(name="ESTADO")
	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
    @Column(name="CONDICION")
	public String getCondicion() {
		return condicion;
	}

	public void setCondicion(String condicion) {
		this.condicion = condicion;
	}
    
	@Column(name="MONTOPENALIZACION")
	public Double getMontopenalizacion() {
		return montopenalizacion;
	}

	public void setMontopenalizacion(Double montopenalizacion) {
		this.montopenalizacion = montopenalizacion;
	}
	
    
      
}

