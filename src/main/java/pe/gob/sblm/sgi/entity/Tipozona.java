package pe.gob.sblm.sgi.entity;
// Generated 11-ene-2016 22:15:03 by Hibernate Tools 4.3.1


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Tipozona generated by hbm2java
 */
@Entity
@Table(name="TIPOZONA"
    ,schema="dbo"
    ,catalog="sisinmueble"
)
public class Tipozona  implements java.io.Serializable {


     private int idtipozona;
     private String descripcion;
     private String abreviatura;
     private Boolean activo;

    public Tipozona() {
    }

	
    public Tipozona(int idtipozona) {
        this.idtipozona = idtipozona;
    }
    public Tipozona(int idtipozona, String descripcion, String abreviatura, Boolean activo) {
       this.idtipozona = idtipozona;
       this.descripcion = descripcion;
       this.abreviatura = abreviatura;
       this.activo = activo;
    }
   
     @Id 

    
    @Column(name="IDTIPOZONA", unique=true, nullable=false)
    public int getIdtipozona() {
        return this.idtipozona;
    }
    
    public void setIdtipozona(int idtipozona) {
        this.idtipozona = idtipozona;
    }

    
    @Column(name="DESCRIPCION", length=50)
    public String getDescripcion() {
        return this.descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    
    @Column(name="ABREVIATURA", length=20)
    public String getAbreviatura() {
        return this.abreviatura;
    }
    
    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    
    @Column(name="ACTIVO")
    public Boolean getActivo() {
        return this.activo;
    }
    
    public void setActivo(Boolean activo) {
        this.activo = activo;
    }


}


