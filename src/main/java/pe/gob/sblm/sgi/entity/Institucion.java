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
 * Institucion generated by hbm2java
 */
@Entity
@Table(name="INSTITUCION"
    ,schema="dbo"
    ,catalog="sisinmueble"
)
public class Institucion  implements java.io.Serializable {


     private int idinstitucion;
     private String nombre;
     private String tipo;
     private String direccion;
     private String telefono;
     private String ruc;
     private Date fechacreacion;
     private String usuariocreador;
     private Date fechamodificacion;
     private String usuariomodificador;
     private Set<Contrato> contratos = new HashSet<Contrato>(0);

    public Institucion() {
    }

	
    public Institucion(int idinstitucion) {
        this.idinstitucion = idinstitucion;
    }
    public Institucion(int idinstitucion, String nombre, String tipo, String direccion, String telefono, String ruc, Date fechacreacion, String usuariocreador, Date fechamodificacion, String usuariomodificador, Set<Contrato> contratos) {
       this.idinstitucion = idinstitucion;
       this.nombre = nombre;
       this.tipo = tipo;
       this.direccion = direccion;
       this.telefono = telefono;
       this.ruc = ruc;
       this.fechacreacion = fechacreacion;
       this.usuariocreador = usuariocreador;
       this.fechamodificacion = fechamodificacion;
       this.usuariomodificador = usuariomodificador;
       this.contratos = contratos;
    }
   
     @Id 
    
    @Column(name="idinstitucion", unique=true, nullable=false)
    public int getIdinstitucion() {
        return this.idinstitucion;
    }
    
    public void setIdinstitucion(int idinstitucion) {
        this.idinstitucion = idinstitucion;
    }
    
    @Column(name="nombre", length=100)
    public String getNombre() {
        return this.nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    @Column(name="tipo", length=50)
    public String getTipo() {
        return this.tipo;
    }
    
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    @Column(name="direccion", length=100)
    public String getDireccion() {
        return this.direccion;
    }
    
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    
    @Column(name="telefono", length=20)
    public String getTelefono() {
        return this.telefono;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    @Column(name="ruc", length=10)
    public String getRuc() {
        return this.ruc;
    }
    
    public void setRuc(String ruc) {
        this.ruc = ruc;
    }
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="FECHACREACION", length=23)
    public Date getFechacreacion() {
        return this.fechacreacion;
    }
    
    public void setFechacreacion(Date fechacreacion) {
        this.fechacreacion = fechacreacion;
    }
    
    @Column(name="USUARIOCREADOR", length=100)
    public String getUsuariocreador() {
        return this.usuariocreador;
    }
    
    public void setUsuariocreador(String usuariocreador) {
        this.usuariocreador = usuariocreador;
    }
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="FECHAMODIFICACION", length=23)
    public Date getFechamodificacion() {
        return this.fechamodificacion;
    }
    
    public void setFechamodificacion(Date fechamodificacion) {
        this.fechamodificacion = fechamodificacion;
    }
    
    @Column(name="USUARIOMODIFICADOR", length=100)
    public String getUsuariomodificador() {
        return this.usuariomodificador;
    }
    
    public void setUsuariomodificador(String usuariomodificador) {
        this.usuariomodificador = usuariomodificador;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="institucion")
    public Set<Contrato> getContratos() {
        return this.contratos;
    }
    
    public void setContratos(Set<Contrato> contratos) {
        this.contratos = contratos;
    }




}


