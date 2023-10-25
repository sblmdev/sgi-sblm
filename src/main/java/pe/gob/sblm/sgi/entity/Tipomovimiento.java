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
 * Tipomovimiento generated by hbm2java
 */
@Entity
@Table(name="TIPOMOVIMIENTO"
    ,schema="dbo"
    ,catalog="sisinmueble"
)
public class Tipomovimiento  implements java.io.Serializable {


     private String idtipomovimiento;
     private String tipMovi;
     private String usrcre;
     private String usrmod;
     private Date feccre;
     private Date fecmod;
     private Set<Comprobantepago> comprobantepagos = new HashSet<Comprobantepago>(0);

     
	
    public Tipomovimiento() {
		super();
	}
	public Tipomovimiento(String idtipomovimiento) {
        this.idtipomovimiento = idtipomovimiento;
    }
    public Tipomovimiento(String idtipomovimiento, String tipMovi, String usrcre, String usrmod, Date feccre, Date fecmod, Set<Comprobantepago> comprobantepagos) {
       this.idtipomovimiento = idtipomovimiento;
       this.tipMovi = tipMovi;
       this.usrcre = usrcre;
       this.usrmod = usrmod;
       this.feccre = feccre;
       this.fecmod = fecmod;
       this.comprobantepagos = comprobantepagos;
    }
   
     @Id 
    
    @Column(name="IDTIPOMOVIMIENTO", unique=true, nullable=false, length=2)
    public String getIdtipomovimiento() {
        return this.idtipomovimiento;
    }
    
    public void setIdtipomovimiento(String idtipomovimiento) {
        this.idtipomovimiento = idtipomovimiento;
    }
    
    @Column(name="TIP_MOVI", length=20)
    public String getTipMovi() {
        return this.tipMovi;
    }
    
    public void setTipMovi(String tipMovi) {
        this.tipMovi = tipMovi;
    }
    
    @Column(name="USRCRE", length=50)
    public String getUsrcre() {
        return this.usrcre;
    }
    
    public void setUsrcre(String usrcre) {
        this.usrcre = usrcre;
    }
    
    @Column(name="USRMOD", length=50)
    public String getUsrmod() {
        return this.usrmod;
    }
    
    public void setUsrmod(String usrmod) {
        this.usrmod = usrmod;
    }
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="FECCRE", length=23)
    public Date getFeccre() {
        return this.feccre;
    }
    
    public void setFeccre(Date feccre) {
        this.feccre = feccre;
    }
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="FECMOD", length=23)
    public Date getFecmod() {
        return this.fecmod;
    }
    
    public void setFecmod(Date fecmod) {
        this.fecmod = fecmod;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="tipomovimiento")
    public Set<Comprobantepago> getComprobantepagos() {
        return this.comprobantepagos;
    }
    
    public void setComprobantepagos(Set<Comprobantepago> comprobantepagos) {
        this.comprobantepagos = comprobantepagos;
    }




}

