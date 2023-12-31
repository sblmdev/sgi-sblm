package pe.gob.sblm.sgi.entity;
// Generated 13-ene-2016 11:37:19 by Hibernate Tools 4.3.1


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
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
 * Inmueblemaestro generated by hbm2java
 */
@Entity
@Table(name="INMUEBLEMAESTRO"
    ,schema="dbo"
    ,catalog="sisinmueble"
)
public class Inmueblemaestro  implements java.io.Serializable {


     private int idinmueble;
     private Materialpredominante materialpredominante;
     private Tipovia tipovia;
     private Origendominio origendominio;
     private Tipozona tipozona;
     private Titularidad titularidad;
     private Ubigeo ubigeo;
     private String numregistrosbn;
     private String clave;
     private String codigoinmueble;
     private String nombretipozona;
     private String nombretipovia;
     private String direccionprincipal;
     private String direccion;
     private String direccionreferencia;
     private Boolean sihabilitacionurbana;
     private Boolean simonumentohistorico;
     private String tipomonumentohistorico;
     private String resolucionmonumentohistorico;
     private Date fecharesolucionmonumentohistorico;
     private String codigopredio;
     private Date fechainscripcionregistral;
     private String asientoregistral;
     private String fojasregistral;
     private String tomoregistral;
     private String ficharegistral;
     private String partidaelectronicaregistral;
     private String observacionregistral;
     private Boolean sideclaratoriafabrica;
     private String asientofabrica;
     private String fojasfabrica;
     private String tomofabrica;
     private String fichafabrica;
     private String partidaelectronicafabrica;
     private String observacionfabrica;
     private Boolean siindependizacion;
     private Integer unidadesinmobiliariasindependizacion;
     private String asientoindependizacion;
     private String fojasindependizacion;
     private String tomoindependizacion;
     private String fichaindependizacion;
     private String partidaelectronicaindependizacion;
     private Date fechainscripcionindependizacion;
     private String observacionindependizacion;
     private Boolean sigravamen;
     private String afavordegravamen;
     private String asientogravamen;
     private String fojasgravamen;
     private String tomogravamen;
     private String fichagravamen;
     private String partidaelectronicagravamen;
     private Date fechainscripciongravamen;
     private String observaciongravamen;
     private Boolean simandascargas;
     private String afavordemandascargas;
     private String asientomandascargas;
     private String fojasmandascargas;
     private String tomomandascargas;
     private String fichamandascargas;
     private String partidaelectronicamandascargas;
     private Date fechainscripcionmandascargas;
     private String observacionmandascargas;
     private Boolean sisaneado;
     private String observacionsaneado;
     private Integer anioconstruccion;
     private Integer numpisos;
     private String areaterreno;
     private String areaconstruida;
     private Boolean sipartidaregistral;
     private Boolean siplanoubicacion;
     private Boolean sifotos;
     private Boolean simemoriadescrip;
     private Boolean sitasacion;
     private Date fechatasacion;
     private String usrcre;
     private String usrmod;
     private Date fechcre;
     private Date fechmod;
     private String latitudlocalizacion;
	 private String longitudlocalizacion;
	 private String descripcionlocalizacion;
     private Set<Inmueblesaneamiento> inmueblesaneamientos = new HashSet<Inmueblesaneamiento>(0);
     private Boolean estado;
     private Boolean sidocumento;
     private String condicion;
     private Set<Upa> upas = new HashSet<Upa>(0);
//     private Boolean sihabitable;

    public Inmueblemaestro() {
    }

	
    public Inmueblemaestro(int idinmueble, String ficharegistral) {
        this.idinmueble = idinmueble;
        this.ficharegistral = ficharegistral;
    }
    public Inmueblemaestro(int idinmueble, Materialpredominante materialpredominante, Tipovia tipovia, Origendominio origendominio,Tipozona tipozona, Titularidad titularidad, Ubigeo ubigeo, String numregistrosbn, String clave, String codigoinmueble, String nombretipozona, String nombretipovia, String direccionprincipal, String direccion, String direccionreferencia, Boolean sihabilitacionurbana, Boolean simonumentohistorico, String tipomonumentohistorico, String resolucionmonumentohistorico, Date fecharesolucionmonumentohistorico, String codigopredio, Date fechainscripcionregistral, String asientoregistral, String fojasregistral, String tomoregistral, String ficharegistral, String partidaelectronicaregistral, String observacionregistral, Boolean sideclaratoriafabrica, String asientofabrica, String fojasfabrica, String tomofabrica, String fichafabrica, String observacionfabrica, Boolean siindependizacion, Integer unidadesinmobiliariasindependizacion, String asientoindependizacion, String fojasindependizacion, String tomoindependizacion, String fichaindependizacion, String partidaelectronicaindependizacion, Date fechainscripcionindependizacion, String observacionindependizacion, Boolean sigravamen, String afavordegravamen, String asientogravamen, String fojasgravamen, String tomogravamen, String fichagravamen, String partidaelectronicagravamen, Date fechainscripciongravamen, String observaciongravamen, Boolean simandascargas, String afavordemandascargas, String asientomandascargas, String fojasmandascargas, String tomomandascargas, String fichamandascargas, String partidaelectronicamandascargas, Date fechainscripcionmandascargas, String observacionmandascargas, Boolean sisaneado, Integer anioconstruccion, Integer numpisos, BigDecimal areaterreno, BigDecimal areaconstruida, Boolean sipartidaregistral, Boolean siplanoubicacion, Boolean simemoriadescrip, Boolean sifotos, Boolean sitasacion, Date fechatasacion, String usrcre, String usrmod, Date fechcre, Date fechmod,Boolean sidocumento, String condicion) {
       this.idinmueble = idinmueble;
       this.materialpredominante = materialpredominante;
       this.tipovia = tipovia;
       this.origendominio=origendominio;
       this.tipozona = tipozona;
       this.titularidad = titularidad;
       this.ubigeo = ubigeo;
       this.numregistrosbn = numregistrosbn;
       this.clave = clave;
       this.codigoinmueble = codigoinmueble;
       this.nombretipozona = nombretipozona;
       this.nombretipovia = nombretipovia;
       this.direccionprincipal = direccionprincipal;
       this.direccion = direccion;
       this.direccionreferencia = direccionreferencia;
       this.sihabilitacionurbana = sihabilitacionurbana;
       this.simonumentohistorico = simonumentohistorico;
       this.tipomonumentohistorico = tipomonumentohistorico;
       this.resolucionmonumentohistorico = resolucionmonumentohistorico;
       this.fecharesolucionmonumentohistorico = fecharesolucionmonumentohistorico;
       this.codigopredio = codigopredio;
       this.fechainscripcionregistral = fechainscripcionregistral;
       this.asientoregistral = asientoregistral;
       this.fojasregistral = fojasregistral;
       this.tomoregistral = tomoregistral;
       this.ficharegistral = ficharegistral;
       this.partidaelectronicaregistral = partidaelectronicaregistral;
       this.observacionregistral = observacionregistral;
       this.sideclaratoriafabrica = sideclaratoriafabrica;
       this.asientofabrica = asientofabrica;
       this.fojasfabrica = fojasfabrica;
       this.tomofabrica = tomofabrica;
       this.fichafabrica = fichafabrica;
       this.observacionfabrica = observacionfabrica;
       this.siindependizacion = siindependizacion;
       this.unidadesinmobiliariasindependizacion = unidadesinmobiliariasindependizacion;
       this.asientoindependizacion = asientoindependizacion;
       this.fojasindependizacion = fojasindependizacion;
       this.tomoindependizacion = tomoindependizacion;
       this.fichaindependizacion = fichaindependizacion;
       this.partidaelectronicaindependizacion = partidaelectronicaindependizacion;
       this.fechainscripcionindependizacion = fechainscripcionindependizacion;
       this.observacionindependizacion = observacionindependizacion;
       this.sigravamen = sigravamen;
       this.afavordegravamen = afavordegravamen;
       this.asientogravamen = asientogravamen;
       this.fojasgravamen = fojasgravamen;
       this.tomogravamen = tomogravamen;
       this.fichagravamen = fichagravamen;
       this.partidaelectronicagravamen = partidaelectronicagravamen;
       this.fechainscripciongravamen = fechainscripciongravamen;
       this.observaciongravamen = observaciongravamen;
       this.simandascargas = simandascargas;
       this.afavordemandascargas = afavordemandascargas;
       this.asientomandascargas = asientomandascargas;
       this.fojasmandascargas = fojasmandascargas;
       this.tomomandascargas = tomomandascargas;
       this.fichamandascargas = fichamandascargas;
       this.partidaelectronicamandascargas = partidaelectronicamandascargas;
       this.fechainscripcionmandascargas = fechainscripcionmandascargas;
       this.observacionmandascargas = observacionmandascargas;
       this.sisaneado = sisaneado;
       this.anioconstruccion = anioconstruccion;
       this.numpisos = numpisos;
       this.sipartidaregistral = sipartidaregistral;
       this.siplanoubicacion = siplanoubicacion;
       this.simemoriadescrip = simemoriadescrip;
       this.sifotos = sifotos;
       this.sitasacion = sitasacion;
       this.fechatasacion = fechatasacion;
       this.usrcre = usrcre;
       this.usrmod = usrmod;
       this.fechcre = fechcre;
       this.fechmod = fechmod;
       this.sidocumento=sidocumento;
       this.condicion=condicion;
    }
   
     @Id 
     @GeneratedValue
    @Column(name="IDINMUEBLE", unique=true, nullable=false)
    public int getIdinmueble() {
        return this.idinmueble;
    }
    
    public void setIdinmueble(int idinmueble) {
        this.idinmueble = idinmueble;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="IDMATERIALPREDOMINANTE")
    public Materialpredominante getMaterialpredominante() {
        return this.materialpredominante;
    }
    
    public void setMaterialpredominante(Materialpredominante materialpredominante) {
        this.materialpredominante = materialpredominante;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="IDTIPOVIA")
    public Tipovia getTipovia() {
        return this.tipovia;
    }
    
    public void setTipovia(Tipovia tipovia) {
        this.tipovia = tipovia;
    }

@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="IDORIGENDOMINIO")    
    
    public Origendominio getOrigendominio() {
		return this.origendominio;
	}


	public void setOrigendominio(Origendominio origendominio) {
		this.origendominio = origendominio;
	}
    
    
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="IDTIPOZONA")
    public Tipozona getTipozona() {
        return this.tipozona;
    }
    
    public void setTipozona(Tipozona tipozona) {
        this.tipozona = tipozona;
    }

@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="IDTITULARIDAD")
    public Titularidad getTitularidad() {
        return this.titularidad;
    }
    
    public void setTitularidad(Titularidad titularidad) {
        this.titularidad = titularidad;
    }

@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="UBIGEO")
    public Ubigeo getUbigeo() {
        return this.ubigeo;
    }
    
    public void setUbigeo(Ubigeo ubigeo) {
        this.ubigeo = ubigeo;
    }

    
    @Column(name="NUMREGISTROSBN", length=10)
    public String getNumregistrosbn() {
        return this.numregistrosbn;
    }
    
    public void setNumregistrosbn(String numregistrosbn) {
        this.numregistrosbn = numregistrosbn;
    }

    
    @Column(name="CLAVE", length=15)
    public String getClave() {
        return this.clave;
    }
    
    public void setClave(String clave) {
        this.clave = clave;
    }

    
    @Column(name="CODIGOINMUEBLE", length=4)
    public String getCodigoinmueble() {
        return this.codigoinmueble;
    }
    
    public void setCodigoinmueble(String codigoinmueble) {
        this.codigoinmueble = codigoinmueble;
    }

    
    @Column(name="NOMBRETIPOZONA", length=50)
    public String getNombretipozona() {
        return this.nombretipozona;
    }
    
    public void setNombretipozona(String nombretipozona) {
        this.nombretipozona = nombretipozona;
    }

    
    @Column(name="NOMBRETIPOVIA", length=100)
    public String getNombretipovia() {
        return this.nombretipovia;
    }
    
    public void setNombretipovia(String nombretipovia) {
        this.nombretipovia = nombretipovia;
    }

    
    @Column(name="DIRECCIONPRINCIPAL", length=150)
    public String getDireccionprincipal() {
        return this.direccionprincipal;
    }
    
    public void setDireccionprincipal(String direccionprincipal) {
        this.direccionprincipal = direccionprincipal;
    }

    
    @Column(name="DIRECCION", length=200)
    public String getDireccion() {
        return this.direccion;
    }
    
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    
    @Column(name="DIRECCIONREFERENCIA", length=100)
    public String getDireccionreferencia() {
        return this.direccionreferencia;
    }
    
    public void setDireccionreferencia(String direccionreferencia) {
        this.direccionreferencia = direccionreferencia;
    }

    
    @Column(name="SIHABILITACIONURBANA")
    public Boolean getSihabilitacionurbana() {
        return this.sihabilitacionurbana;
    }
    
    public void setSihabilitacionurbana(Boolean sihabilitacionurbana) {
        this.sihabilitacionurbana = sihabilitacionurbana;
    }

    
    @Column(name="SIMONUMENTOHISTORICO")
    public Boolean getSimonumentohistorico() {
        return this.simonumentohistorico;
    }
    
    public void setSimonumentohistorico(Boolean simonumentohistorico) {
        this.simonumentohistorico = simonumentohistorico;
    }

    
    @Column(name="TIPOMONUMENTOHISTORICO", length=50)
    public String getTipomonumentohistorico() {
        return this.tipomonumentohistorico;
    }
    
    public void setTipomonumentohistorico(String tipomonumentohistorico) {
        this.tipomonumentohistorico = tipomonumentohistorico;
    }

    
    @Column(name="RESOLUCIONMONUMENTOHISTORICO", length=100)
    public String getResolucionmonumentohistorico() {
        return this.resolucionmonumentohistorico;
    }
    
    public void setResolucionmonumentohistorico(String resolucionmonumentohistorico) {
        this.resolucionmonumentohistorico = resolucionmonumentohistorico;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="FECHARESOLUCIONMONUMENTOHISTORICO", length=23)
    public Date getFecharesolucionmonumentohistorico() {
        return this.fecharesolucionmonumentohistorico;
    }
    
    public void setFecharesolucionmonumentohistorico(Date fecharesolucionmonumentohistorico) {
        this.fecharesolucionmonumentohistorico = fecharesolucionmonumentohistorico;
    }

    
    @Column(name="CODIGOPREDIO", length=20)
    public String getCodigopredio() {
        return this.codigopredio;
    }
    
    public void setCodigopredio(String codigopredio) {
        this.codigopredio = codigopredio;
    }

    @Temporal(TemporalType.DATE)
    @Column(name="FECHAINSCRIPCIONREGISTRAL", length=10)
    public Date getFechainscripcionregistral() {
        return this.fechainscripcionregistral;
    }
    
    public void setFechainscripcionregistral(Date fechainscripcionregistral) {
        this.fechainscripcionregistral = fechainscripcionregistral;
    }

    
    @Column(name="ASIENTOREGISTRAL", length=10)
    public String getAsientoregistral() {
        return this.asientoregistral;
    }
    
    public void setAsientoregistral(String asientoregistral) {
        this.asientoregistral = asientoregistral;
    }

    
    @Column(name="FOJASREGISTRAL", length=10)
    public String getFojasregistral() {
        return this.fojasregistral;
    }
    
    public void setFojasregistral(String fojasregistral) {
        this.fojasregistral = fojasregistral;
    }

    
    @Column(name="TOMOREGISTRAL", length=10)
    public String getTomoregistral() {
        return this.tomoregistral;
    }
    
    public void setTomoregistral(String tomoregistral) {
        this.tomoregistral = tomoregistral;
    }

    
    @Column(name="FICHAREGISTRAL", nullable=false, length=20)
    public String getFicharegistral() {
        return this.ficharegistral;
    }
    
    public void setFicharegistral(String ficharegistral) {
        this.ficharegistral = ficharegistral;
    }

    
    @Column(name="PARTIDAELECTRONICAREGISTRAL", length=20)
    public String getPartidaelectronicaregistral() {
        return this.partidaelectronicaregistral;
    }
    
    public void setPartidaelectronicaregistral(String partidaelectronicaregistral) {
        this.partidaelectronicaregistral = partidaelectronicaregistral;
    }

    
    @Column(name="OBSERVACIONREGISTRAL", length=1000)
    public String getObservacionregistral() {
        return this.observacionregistral;
    }
    
    public void setObservacionregistral(String observacionregistral) {
        this.observacionregistral = observacionregistral;
    }
    
    @Column(name="OBSERVACIONSANEAMIENTO", length=1000)
    public String getObservacionsaneado() {
		return observacionsaneado;
	}


	public void setObservacionsaneado(String observacionsaneado) {
		this.observacionsaneado = observacionsaneado;
	}

    
    @Column(name="SIDECLARATORIAFABRICA")
    public Boolean getSideclaratoriafabrica() {
        return this.sideclaratoriafabrica;
    }

	public void setSideclaratoriafabrica(Boolean sideclaratoriafabrica) {
        this.sideclaratoriafabrica = sideclaratoriafabrica;
    }

    
    @Column(name="ASIENTOFABRICA", length=10)
    public String getAsientofabrica() {
        return this.asientofabrica;
    }
    
    public void setAsientofabrica(String asientofabrica) {
        this.asientofabrica = asientofabrica;
    }

    
    @Column(name="FOJASFABRICA", length=10)
    public String getFojasfabrica() {
        return this.fojasfabrica;
    }
    
    public void setFojasfabrica(String fojasfabrica) {
        this.fojasfabrica = fojasfabrica;
    }

    
    @Column(name="TOMOFABRICA", length=10)
    public String getTomofabrica() {
        return this.tomofabrica;
    }
    
    public void setTomofabrica(String tomofabrica) {
        this.tomofabrica = tomofabrica;
    }

    
    @Column(name="FICHAFABRICA", length=10)
    public String getFichafabrica() {
        return this.fichafabrica;
    }
    
    public void setFichafabrica(String fichafabrica) {
        this.fichafabrica = fichafabrica;
    }

    
    @Column(name="OBSERVACIONFABRICA", length=1000)
    public String getObservacionfabrica() {
        return this.observacionfabrica;
    }
    
    public void setObservacionfabrica(String observacionfabrica) {
        this.observacionfabrica = observacionfabrica;
    }

    
    @Column(name="SIINDEPENDIZACION")
    public Boolean getSiindependizacion() {
        return this.siindependizacion;
    }
    
    public void setSiindependizacion(Boolean siindependizacion) {
        this.siindependizacion = siindependizacion;
    }

    
    @Column(name="UNIDADESINMOBILIARIASINDEPENDIZACION")
    public Integer getUnidadesinmobiliariasindependizacion() {
        return this.unidadesinmobiliariasindependizacion;
    }
    
    public void setUnidadesinmobiliariasindependizacion(Integer unidadesinmobiliariasindependizacion) {
        this.unidadesinmobiliariasindependizacion = unidadesinmobiliariasindependizacion;
    }

    
    @Column(name="ASIENTOINDEPENDIZACION")
    public String getAsientoindependizacion() {
        return this.asientoindependizacion;
    }
    
    public void setAsientoindependizacion(String asientoindependizacion) {
        this.asientoindependizacion = asientoindependizacion;
    }

    
    @Column(name="FOJASINDEPENDIZACION", length=10)
    public String getFojasindependizacion() {
        return this.fojasindependizacion;
    }
    
    public void setFojasindependizacion(String fojasindependizacion) {
        this.fojasindependizacion = fojasindependizacion;
    }

    
    @Column(name="TOMOINDEPENDIZACION", length=10)
    public String getTomoindependizacion() {
        return this.tomoindependizacion;
    }
    
    public void setTomoindependizacion(String tomoindependizacion) {
        this.tomoindependizacion = tomoindependizacion;
    }

    
    @Column(name="FICHAINDEPENDIZACION", length=20)
    public String getFichaindependizacion() {
        return this.fichaindependizacion;
    }
    
    public void setFichaindependizacion(String fichaindependizacion) {
        this.fichaindependizacion = fichaindependizacion;
    }

    
    @Column(name="PARTIDAELECTRONICAINDEPENDIZACION", length=20)
    public String getPartidaelectronicaindependizacion() {
        return this.partidaelectronicaindependizacion;
    }
    
    public void setPartidaelectronicaindependizacion(String partidaelectronicaindependizacion) {
        this.partidaelectronicaindependizacion = partidaelectronicaindependizacion;
    }

    @Temporal(TemporalType.DATE)
    @Column(name="FECHAINSCRIPCIONINDEPENDIZACION", length=10)
    public Date getFechainscripcionindependizacion() {
        return this.fechainscripcionindependizacion;
    }
    
    public void setFechainscripcionindependizacion(Date fechainscripcionindependizacion) {
        this.fechainscripcionindependizacion = fechainscripcionindependizacion;
    }

    
    @Column(name="OBSERVACIONINDEPENDIZACION", length=1000)
    public String getObservacionindependizacion() {
        return this.observacionindependizacion;
    }
    
    public void setObservacionindependizacion(String observacionindependizacion) {
        this.observacionindependizacion = observacionindependizacion;
    }

    
    @Column(name="SIGRAVAMEN")
    public Boolean getSigravamen() {
        return this.sigravamen;
    }
    
    public void setSigravamen(Boolean sigravamen) {
        this.sigravamen = sigravamen;
    }

    
    @Column(name="AFAVORDEGRAVAMEN", length=50)
    public String getAfavordegravamen() {
        return this.afavordegravamen;
    }
    
    public void setAfavordegravamen(String afavordegravamen) {
        this.afavordegravamen = afavordegravamen;
    }

    
    @Column(name="ASIENTOGRAVAMEN", length=10)
    public String getAsientogravamen() {
        return this.asientogravamen;
    }
    
    public void setAsientogravamen(String asientogravamen) {
        this.asientogravamen = asientogravamen;
    }

    
    @Column(name="FOJASGRAVAMEN", length=10)
    public String getFojasgravamen() {
        return this.fojasgravamen;
    }
    
    public void setFojasgravamen(String fojasgravamen) {
        this.fojasgravamen = fojasgravamen;
    }

    
    @Column(name="TOMOGRAVAMEN", length=10)
    public String getTomogravamen() {
        return this.tomogravamen;
    }
    
    public void setTomogravamen(String tomogravamen) {
        this.tomogravamen = tomogravamen;
    }

    
    @Column(name="FICHAGRAVAMEN", length=10)
    public String getFichagravamen() {
        return this.fichagravamen;
    }
    
    public void setFichagravamen(String fichagravamen) {
        this.fichagravamen = fichagravamen;
    }

    
    @Column(name="PARTIDAELECTRONICAGRAVAMEN", length=20)
    public String getPartidaelectronicagravamen() {
        return this.partidaelectronicagravamen;
    }
    
    public void setPartidaelectronicagravamen(String partidaelectronicagravamen) {
        this.partidaelectronicagravamen = partidaelectronicagravamen;
    }

    @Temporal(TemporalType.DATE)
    @Column(name="FECHAINSCRIPCIONGRAVAMEN", length=10)
    public Date getFechainscripciongravamen() {
        return this.fechainscripciongravamen;
    }
    
    public void setFechainscripciongravamen(Date fechainscripciongravamen) {
        this.fechainscripciongravamen = fechainscripciongravamen;
    }

    
    @Column(name="OBSERVACIONGRAVAMEN", length=1000)
    public String getObservaciongravamen() {
        return this.observaciongravamen;
    }
    
    public void setObservaciongravamen(String observaciongravamen) {
        this.observaciongravamen = observaciongravamen;
    }

    
    @Column(name="SIMANDASCARGAS")
    public Boolean getSimandascargas() {
        return this.simandascargas;
    }
    
    public void setSimandascargas(Boolean simandascargas) {
        this.simandascargas = simandascargas;
    }

    
    @Column(name="AFAVORDEMANDASCARGAS", length=50)
    public String getAfavordemandascargas() {
        return this.afavordemandascargas;
    }
    
    public void setAfavordemandascargas(String afavordemandascargas) {
        this.afavordemandascargas = afavordemandascargas;
    }

    
    @Column(name="ASIENTOMANDASCARGAS", length=10)
    public String getAsientomandascargas() {
        return this.asientomandascargas;
    }
    
    public void setAsientomandascargas(String asientomandascargas) {
        this.asientomandascargas = asientomandascargas;
    }

    
    @Column(name="FOJASMANDASCARGAS", length=10)
    public String getFojasmandascargas() {
        return this.fojasmandascargas;
    }
    
    public void setFojasmandascargas(String fojasmandascargas) {
        this.fojasmandascargas = fojasmandascargas;
    }

    
    @Column(name="TOMOMANDASCARGAS", length=10)
    public String getTomomandascargas() {
        return this.tomomandascargas;
    }
    
    public void setTomomandascargas(String tomomandascargas) {
        this.tomomandascargas = tomomandascargas;
    }

    
    @Column(name="FICHAMANDASCARGAS", length=10)
    public String getFichamandascargas() {
        return this.fichamandascargas;
    }
    
    public void setFichamandascargas(String fichamandascargas) {
        this.fichamandascargas = fichamandascargas;
    }

    
    @Column(name="PARTIDAELECTRONICAMANDASCARGAS", length=20)
    public String getPartidaelectronicamandascargas() {
        return this.partidaelectronicamandascargas;
    }
    
    public void setPartidaelectronicamandascargas(String partidaelectronicamandascargas) {
        this.partidaelectronicamandascargas = partidaelectronicamandascargas;
    }

    @Temporal(TemporalType.DATE)
    @Column(name="FECHAINSCRIPCIONMANDASCARGAS", length=10)
    public Date getFechainscripcionmandascargas() {
        return this.fechainscripcionmandascargas;
    }
    
    public void setFechainscripcionmandascargas(Date fechainscripcionmandascargas) {
        this.fechainscripcionmandascargas = fechainscripcionmandascargas;
    }

    
    @Column(name="OBSERVACIONMANDASCARGAS", length=1000)
    public String getObservacionmandascargas() {
        return this.observacionmandascargas;
    }
    
    public void setObservacionmandascargas(String observacionmandascargas) {
        this.observacionmandascargas = observacionmandascargas;
    }

    
    @Column(name="SISANEADO")
    public Boolean getSisaneado() {
        return this.sisaneado;
    }
    
    public void setSisaneado(Boolean sisaneado) {
        this.sisaneado = sisaneado;
    }

    
    @Column(name="ANIOCONSTRUCCION")
    public Integer getAnioconstruccion() {
        return this.anioconstruccion;
    }
    
    public void setAnioconstruccion(Integer anioconstruccion) {
        this.anioconstruccion = anioconstruccion;
    }

    
    @Column(name="NUMPISOS")
    public Integer getNumpisos() {
        return this.numpisos;
    }
    
    public void setNumpisos(Integer numpisos) {
        this.numpisos = numpisos;
    }

    
    @Column(name="AREATERRENO", precision=53, scale=0)
    public String getAreaterreno() {
		return areaterreno;
	}


	public void setAreaterreno(String areaterreno) {
		this.areaterreno = areaterreno;
	}
	
    @Column(name="AREACONSTRUIDA", precision=53, scale=0)
	public String getAreaconstruida() {
		return areaconstruida;
	}


	public void setAreaconstruida(String areaconstruida) {
		this.areaconstruida = areaconstruida;
	}

    
    @Column(name="SIPARTIDAREGISTRAL")
    public Boolean getSipartidaregistral() {
        return this.sipartidaregistral;
    }


	public void setSipartidaregistral(Boolean sipartidaregistral) {
        this.sipartidaregistral = sipartidaregistral;
    }

    
    @Column(name="SIPLANOUBICACION")
    public Boolean getSiplanoubicacion() {
        return this.siplanoubicacion;
    }
    
    public void setSiplanoubicacion(Boolean siplanoubicacion) {
        this.siplanoubicacion = siplanoubicacion;
    }

    
    @Column(name="SIMEMORIADESCRIP")
    public Boolean getSimemoriadescrip() {
        return this.simemoriadescrip;
    }
    
    public void setSimemoriadescrip(Boolean simemoriadescrip) {
        this.simemoriadescrip = simemoriadescrip;
    }

    
    @Column(name="SIFOTOS")
    public Boolean getSifotos() {
        return this.sifotos;
    }
    
    public void setSifotos(Boolean sifotos) {
        this.sifotos = sifotos;
    }

    
    @Column(name="SITASACION")
    public Boolean getSitasacion() {
        return this.sitasacion;
    }
    
    public void setSitasacion(Boolean sitasacion) {
        this.sitasacion = sitasacion;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="FECHATASACION", length=23)
    public Date getFechatasacion() {
        return this.fechatasacion;
    }
    
    public void setFechatasacion(Date fechatasacion) {
        this.fechatasacion = fechatasacion;
    }
    
    @Column(name="LATITUDLOCALIZACION", length=20)
	public String getLatitudlocalizacion() {
		return latitudlocalizacion;
	}


	public void setLatitudlocalizacion(String latitudlocalizacion) {
		this.latitudlocalizacion = latitudlocalizacion;
	}

    @Column(name="LONGITUDLOCALIZACION", length=20)
	public String getLongitudlocalizacion() {
		return longitudlocalizacion;
	}


	public void setLongitudlocalizacion(String longitudlocalizacion) {
		this.longitudlocalizacion = longitudlocalizacion;
	}


    @Column(name="DESCRIPCIONLOCALIZACION", length=50)
	public String getDescripcionlocalizacion() {
		return descripcionlocalizacion;
	}

	public void setDescripcionlocalizacion(String descripcionlocalizacion) {
		this.descripcionlocalizacion = descripcionlocalizacion;
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
    @Column(name="FECHCRE", length=23)
    public Date getFechcre() {
        return this.fechcre;
    }
    
    public void setFechcre(Date fechcre) {
        this.fechcre = fechcre;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="FECHMOD", length=23)
    public Date getFechmod() {
        return this.fechmod;
    }
    
    public void setFechmod(Date fechmod) {
        this.fechmod = fechmod;
    }

    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="inmueble")
	public Set<Inmueblesaneamiento> getInmueblesaneamientos() {
		return inmueblesaneamientos;
	}
	public void setInmueblesaneamientos(
			Set<Inmueblesaneamiento> inmueblesaneamientos) {
		this.inmueblesaneamientos = inmueblesaneamientos;
	}
	
    @Column(name="PARTIDAELECTRONICAFABRICA", length=20)
	public String getPartidaelectronicafabrica() {
		return partidaelectronicafabrica;
	}

	public void setPartidaelectronicafabrica(String partidaelectronicafabrica) {
		this.partidaelectronicafabrica = partidaelectronicafabrica;
	}

	 @Column(name="ESTADO")
	public Boolean getEstado() {
		return estado;
	}


	public void setEstado(Boolean estado) {
		this.estado = estado;
	}

	 @Column(name="SIDOCUMENTO")
	public Boolean getSidocumento() {
		return sidocumento;
	}


	public void setSidocumento(Boolean sidocumento) {
		this.sidocumento = sidocumento;
	}

	 @Column(name="CONDICION")
	public String getCondicion() {
		return condicion;
	}


	public void setCondicion(String condicion) {
		this.condicion = condicion;
	}

	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="inmueblemaestro")
	public Set<Upa> getUpas() {
		return upas;
	}


	public void setUpas(Set<Upa> upas) {
		this.upas = upas;
	}


	@Override
	public String toString() {
		return "Inmueblemaestro [idinmueble=" + idinmueble
				+ ", materialpredominante=" + materialpredominante
				+ ", tipovia=" + tipovia + ", origendominio=" + origendominio
				+ ", tipozona=" + tipozona + ", titularidad=" + titularidad
				+ ", ubigeo=" + ubigeo + ", numregistrosbn=" + numregistrosbn
				+ ", clave=" + clave + ", codigoinmueble=" + codigoinmueble
				+ ", nombretipozona=" + nombretipozona + ", nombretipovia="
				+ nombretipovia + ", direccionprincipal=" + direccionprincipal
				+ ", direccion=" + direccion + ", direccionreferencia="
				+ direccionreferencia + ", sihabilitacionurbana="
				+ sihabilitacionurbana + ", simonumentohistorico="
				+ simonumentohistorico + ", tipomonumentohistorico="
				+ tipomonumentohistorico + ", resolucionmonumentohistorico="
				+ resolucionmonumentohistorico
				+ ", fecharesolucionmonumentohistorico="
				+ fecharesolucionmonumentohistorico + ", codigopredio="
				+ codigopredio + ", fechainscripcionregistral="
				+ fechainscripcionregistral + ", asientoregistral="
				+ asientoregistral + ", fojasregistral=" + fojasregistral
				+ ", tomoregistral=" + tomoregistral + ", ficharegistral="
				+ ficharegistral + ", partidaelectronicaregistral="
				+ partidaelectronicaregistral + ", observacionregistral="
				+ observacionregistral + ", sideclaratoriafabrica="
				+ sideclaratoriafabrica + ", asientofabrica=" + asientofabrica
				+ ", fojasfabrica=" + fojasfabrica + ", tomofabrica="
				+ tomofabrica + ", fichafabrica=" + fichafabrica
				+ ", partidaelectronicafabrica=" + partidaelectronicafabrica
				+ ", observacionfabrica=" + observacionfabrica
				+ ", siindependizacion=" + siindependizacion
				+ ", unidadesinmobiliariasindependizacion="
				+ unidadesinmobiliariasindependizacion
				+ ", asientoindependizacion=" + asientoindependizacion
				+ ", fojasindependizacion=" + fojasindependizacion
				+ ", tomoindependizacion=" + tomoindependizacion
				+ ", fichaindependizacion=" + fichaindependizacion
				+ ", partidaelectronicaindependizacion="
				+ partidaelectronicaindependizacion
				+ ", fechainscripcionindependizacion="
				+ fechainscripcionindependizacion
				+ ", observacionindependizacion=" + observacionindependizacion
				+ ", sigravamen=" + sigravamen + ", afavordegravamen="
				+ afavordegravamen + ", asientogravamen=" + asientogravamen
				+ ", fojasgravamen=" + fojasgravamen + ", tomogravamen="
				+ tomogravamen + ", fichagravamen=" + fichagravamen
				+ ", partidaelectronicagravamen=" + partidaelectronicagravamen
				+ ", fechainscripciongravamen=" + fechainscripciongravamen
				+ ", observaciongravamen=" + observaciongravamen
				+ ", simandascargas=" + simandascargas
				+ ", afavordemandascargas=" + afavordemandascargas
				+ ", asientomandascargas=" + asientomandascargas
				+ ", fojasmandascargas=" + fojasmandascargas
				+ ", tomomandascargas=" + tomomandascargas
				+ ", fichamandascargas=" + fichamandascargas
				+ ", partidaelectronicamandascargas="
				+ partidaelectronicamandascargas
				+ ", fechainscripcionmandascargas="
				+ fechainscripcionmandascargas + ", observacionmandascargas="
				+ observacionmandascargas + ", sisaneado=" + sisaneado
				+ ", observacionsaneado=" + observacionsaneado
				+ ", anioconstruccion=" + anioconstruccion + ", numpisos="
				+ numpisos + ", areaterreno=" + areaterreno
				+ ", areaconstruida=" + areaconstruida
				+ ", sipartidaregistral=" + sipartidaregistral
				+ ", siplanoubicacion=" + siplanoubicacion + ", sifotos="
				+ sifotos + ", simemoriadescrip=" + simemoriadescrip
				+ ", sitasacion=" + sitasacion + ", fechatasacion="
				+ fechatasacion + ", usrcre=" + usrcre + ", usrmod=" + usrmod
				+ ", fechcre=" + fechcre + ", fechmod=" + fechmod
				+ ", latitudlocalizacion=" + latitudlocalizacion
				+ ", longitudlocalizacion=" + longitudlocalizacion
				+ ", descripcionlocalizacion=" + descripcionlocalizacion
				+ ", inmueblesaneamientos=" + inmueblesaneamientos
				+ ", estado=" + estado + ", sidocumento=" + sidocumento
				+ ", condicion=" + condicion + ", upas=" + upas + "]";
	}

//    @Column(name="SIHABITABLE")
//	public Boolean getSihabitable() {
//		return sihabitable;
//	}
//
//
//	public void setSihabitable(Boolean sihabitable) {
//		this.sihabitable = sihabitable;
//	}
	
	


}


