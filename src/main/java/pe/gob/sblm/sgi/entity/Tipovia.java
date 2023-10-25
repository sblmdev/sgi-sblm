package pe.gob.sblm.sgi.entity;
// Generated 19/03/2014 03:10:50 PM by Hibernate Tools 3.2.1.GA


import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Tipovia generated by hbm2java
 */
@Entity
@Table(name="TIPOVIA"
    ,schema="dbo"
    ,catalog="sisinmueble"
)
public class Tipovia  implements java.io.Serializable {


     private int idtipovia;
     private String descripcion;
     private Boolean estado;
     private String abreviatura;
     private Set<Inmueble> inmuebles = new HashSet<Inmueble>(0);

    public Tipovia() {
    }

	
    public Tipovia(int idtipovia) {
        this.idtipovia = idtipovia;
    }
    public Tipovia(int idtipovia, String descripcion, Boolean estado, Set<Upa> upas, Set<Inmueble> inmuebles) {
       this.idtipovia = idtipovia;
       this.descripcion = descripcion;
       this.estado = estado;
       this.inmuebles = inmuebles;
    }
   
     @Id 
    
    @Column(name="IDTIPOVIA", unique=true, nullable=false)
    public int getIdtipovia() {
        return this.idtipovia;
    }
    
    public void setIdtipovia(int idtipovia) {
        this.idtipovia = idtipovia;
    }
    
    @Column(name="DESCRIPCION", length=100)
    public String getDescripcion() {
        return this.descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    @Column(name="ABREVIATURA", length=10)
    public String getAbreviatura() {
		return abreviatura;
	}


	public void setAbreviatura(String abreviatura) {
		this.abreviatura = abreviatura;
	}


	@Column(name="ESTADO")
    public Boolean getEstado() {
        return this.estado;
    }
    
    public void setEstado(Boolean estado) {
        this.estado = estado;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="tipovia")
    public Set<Inmueble> getInmuebles() {
        return this.inmuebles;
    }
    
    public void setInmuebles(Set<Inmueble> inmuebles) {
        this.inmuebles = inmuebles;
    }




}


