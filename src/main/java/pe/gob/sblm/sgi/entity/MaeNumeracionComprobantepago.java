package pe.gob.sblm.sgi.entity;
// Generated 07-oct-2017 18:45:22 by Hibernate Tools 4.3.1


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * MaeNumeracionComprobantepago generated by hbm2java
 */
@Entity
@Table(name="MAE_NUMERACION_COMPROBANTEPAGO"
    ,schema="dbo"
    ,catalog="sisinmueble"
)
public class MaeNumeracionComprobantepago  implements java.io.Serializable {


     private int idNumeracionComprobantepago;
     private MaeSerieComprobantepago maeSerieComprobantepago;
     private String numeracion;
     private Boolean LUsado;

    public MaeNumeracionComprobantepago() {
    }

	
    public MaeNumeracionComprobantepago(int idNumeracionComprobantepago) {
        this.idNumeracionComprobantepago = idNumeracionComprobantepago;
    }
    public MaeNumeracionComprobantepago(int idNumeracionComprobantepago, MaeSerieComprobantepago maeSerieComprobantepago, String numeracion, Boolean LUsado) {
       this.idNumeracionComprobantepago = idNumeracionComprobantepago;
       this.maeSerieComprobantepago = maeSerieComprobantepago;
       this.numeracion = numeracion;
       this.LUsado = LUsado;
    }
   
     @Id 
    
    @Column(name="ID_NUMERACION_COMPROBANTEPAGO", unique=true, nullable=false)
    public int getIdNumeracionComprobantepago() {
        return this.idNumeracionComprobantepago;
    }
    
    public void setIdNumeracionComprobantepago(int idNumeracionComprobantepago) {
        this.idNumeracionComprobantepago = idNumeracionComprobantepago;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="ID_SERIE_COMPROBANTEPAGO")
    public MaeSerieComprobantepago getMaeSerieComprobantepago() {
        return this.maeSerieComprobantepago;
    }
    
    public void setMaeSerieComprobantepago(MaeSerieComprobantepago maeSerieComprobantepago) {
        this.maeSerieComprobantepago = maeSerieComprobantepago;
    }

    
    @Column(name="NUMERACION", length=10)
    public String getNumeracion() {
        return this.numeracion;
    }
    
    public void setNumeracion(String numeracion) {
        this.numeracion = numeracion;
    }

    
    @Column(name="L_USADO")
    public Boolean getLUsado() {
        return this.LUsado;
    }
    
    public void setLUsado(Boolean LUsado) {
        this.LUsado = LUsado;
    }




}


