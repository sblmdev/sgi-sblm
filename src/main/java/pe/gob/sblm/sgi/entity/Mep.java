package pe.gob.sblm.sgi.entity;
// Generated 19/03/2014 03:10:50 PM by Hibernate Tools 3.2.1.GA


import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Mep generated by hbm2java
 */
@Entity
@Table(name="MEP"
    ,schema="dbo"
    ,catalog="sisinmueble"
)
public class Mep  implements java.io.Serializable {


     private int idmep;
     private String descripcion;
     private String usrcre;
     private String usrmod;
     private Date feccre;
     private Date fecmod;

    public Mep() {
    }

	
    public Mep(int idmep) {
        this.idmep = idmep;
    }
    public Mep(int idmep, String descripcion, String usrcre, String usrmod, Date feccre, Date fecmod, Set<Upa> upas) {
       this.idmep = idmep;
       this.descripcion = descripcion;
       this.usrcre = usrcre;
       this.usrmod = usrmod;
       this.feccre = feccre;
       this.fecmod = fecmod;
    }
   
     @Id 
    
    @Column(name="idmep", unique=true, nullable=false)
    public int getIdmep() {
        return this.idmep;
    }
    
    public void setIdmep(int idmep) {
        this.idmep = idmep;
    }
    
    @Column(name="descripcion", length=50)
    public String getDescripcion() {
        return this.descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    @Column(name="usrcre", length=50)
    public String getUsrcre() {
        return this.usrcre;
    }
    
    public void setUsrcre(String usrcre) {
        this.usrcre = usrcre;
    }
    
    @Column(name="usrmod", length=50)
    public String getUsrmod() {
        return this.usrmod;
    }
    
    public void setUsrmod(String usrmod) {
        this.usrmod = usrmod;
    }
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="feccre", length=23)
    public Date getFeccre() {
        return this.feccre;
    }
    
    public void setFeccre(Date feccre) {
        this.feccre = feccre;
    }
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="fecmod", length=23)
    public Date getFecmod() {
        return this.fecmod;
    }
    
    public void setFecmod(Date fecmod) {
        this.fecmod = fecmod;
    }

}


