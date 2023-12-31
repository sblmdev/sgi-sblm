package pe.gob.sblm.sgi.entity;
// Generated 19/03/2014 03:10:50 PM by Hibernate Tools 3.2.1.GA


import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Origendominio generated by hbm2java
 */
@Entity
@Table(name="ORIGENDOMINIO"
    ,schema="dbo"
    ,catalog="sisinmueble"
)
public class Origendominio  implements java.io.Serializable {


     private int idorigendominio;
     private String descripcion;
     private Boolean estado;
	

    public Origendominio() {
    }

	
    public Origendominio(int idorigendominio) {
        this.idorigendominio = idorigendominio;
    }
    public Origendominio(int idorigendominio, String descripcion, Boolean estado, Set<Upa> upas, Set<Inmueble> inmuebles) {
       this.idorigendominio = idorigendominio;
       this.descripcion = descripcion;
       this.estado = estado;
    }
   
     @Id 
    
    @Column(name="IDORIGENDOMINIO", unique=true, nullable=false)
    public int getIdorigendominio() {
        return this.idorigendominio;
    }
    
    public void setIdorigendominio(int idorigendominio) {
        this.idorigendominio = idorigendominio;
    }
    
    @Column(name="DESCRIPCION", length=100)
    public String getDescripcion() {
        return this.descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    
	

	@Column(name="ESTADO")
    public Boolean getEstado() {
        return this.estado;
    }
    
    public void setEstado(Boolean estado) {
        this.estado = estado;
    }



}

