package pe.gob.sblm.sgi.entity;

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

@Entity
@Table(name="TIPOTRANSACCION"
    ,schema="dbo"
    ,catalog="sisinmueble"
)
public class Tipotransaccion implements java.io.Serializable{
	
	private String idtipotransaccion;
    private String descripcion;
    private String usrcre;
    private String usrmod;
    private String obs;
    private Date feccre;
    private Date fecmod;
    private String obsmod;
    private Set<Comprobantepago> comprobantepagos = new HashSet<Comprobantepago>(0);
    
    public Tipotransaccion() {
		super();
	}
	public Tipotransaccion(String idtipotransaccion) {
        this.idtipotransaccion = idtipotransaccion;
    }
	
	public Tipotransaccion(String idtipotransaccion, String descripcion,
			String usrcre, String usrmod, String obs, Date feccre, Date fecmod,
			String obsmod, Set<Comprobantepago> comprobantepagos) {
		this.idtipotransaccion = idtipotransaccion;
		this.descripcion = descripcion;
		this.usrcre = usrcre;
		this.usrmod = usrmod;
		this.obs = obs;
		this.feccre = feccre;
		this.fecmod = fecmod;
		this.obsmod = obsmod;
		this.comprobantepagos = comprobantepagos;
	}
	
	@Id 
	    
	@Column(name="IDTIPOTRANSACCION", unique=true, nullable=false, length=2)
	public String getIdtipotransaccion() {
		return idtipotransaccion;
	}
	public void setIdtipotransaccion(String idtipotransaccion) {
		this.idtipotransaccion = idtipotransaccion;
	}
	@Column(name="DESCRIPCION", length=30)
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	@Column(name="USRCRE", length=100)
	public String getUsrcre() {
		return usrcre;
	}
	public void setUsrcre(String usrcre) {
		this.usrcre = usrcre;
	}
	@Column(name="USRMOD", length=50)
	public String getUsrmod() {
		return usrmod;
	}
	public void setUsrmod(String usrmod) {
		this.usrmod = usrmod;
	}
	@Column(name="OBS", length=1000)
	public String getObs() {
		return obs;
	}
	public void setObs(String obs) {
		this.obs = obs;
	}
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name="FECCRE", length=23)
	public Date getFeccre() {
		return feccre;
	}
	public void setFeccre(Date feccre) {
		this.feccre = feccre;
	}
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name="FECMOD", length=23)
	public Date getFecmod() {
		return fecmod;
	}
	public void setFecmod(Date fecmod) {
		this.fecmod = fecmod;
	}
	@Column(name="OBS_MOD", length=1000)
	public String getObsmod() {
		return obsmod;
	}
	public void setObsmod(String obsmod) {
		this.obsmod = obsmod;
	}
	 @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="tipotransaccion")
	public Set<Comprobantepago> getComprobantepagos() {
		return comprobantepagos;
	}
	public void setComprobantepagos(Set<Comprobantepago> comprobantepagos) {
		this.comprobantepagos = comprobantepagos;
	}
    
    
    
    

}
