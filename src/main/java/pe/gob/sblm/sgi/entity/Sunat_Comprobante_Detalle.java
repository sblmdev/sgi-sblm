package pe.gob.sblm.sgi.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="SUNAT_COMPROBANTE_DETALLE"
    ,schema="dbo"
    ,catalog="sunatfe"
)
public class Sunat_Comprobante_Detalle implements java.io.Serializable {
 private int id_compr_det;
 private Sunat_Comprobante sunat_comprobante;
 private int nroitem;
 private String codigo_articulo;
 private String codigo_unidad;
 private String descripcion_articulo;
 private String descripcion_adicional;
 private BigDecimal cantidad;
 private BigDecimal precio_unitario_sinigv;
 private BigDecimal importe_subtotal;
 private BigDecimal importe_descuento;
 private BigDecimal importe_valorventa;
 private BigDecimal importe_igv;
 private BigDecimal importe_isc;
 private BigDecimal precio_unitario_conigv;
 private BigDecimal importe_total;
 private String tipo_afeccionigv;
 private Boolean esgravado;
 private Boolean esexonerado;
 private Boolean esinafecto;
 private Boolean esgratuito;
 
 public Sunat_Comprobante_Detalle(){
	 
 }
 @Id
 @GeneratedValue
 @Column(name="ID_COMPR_DET" ,unique=true ,nullable=false)
public int getId_compr_det() {
	return id_compr_det;
}
public void setId_compr_det(int id_compr_det) {
	this.id_compr_det = id_compr_det;
}
@ManyToOne(fetch=FetchType.LAZY)
@JoinColumn(name="ID_COMPROBANTE")
public Sunat_Comprobante getSunat_comprobante() {
	return sunat_comprobante;
}
public void setSunat_comprobante(Sunat_Comprobante sunat_comprobante) {
	this.sunat_comprobante = sunat_comprobante;
}
@Column(name="NROITEM")
public int getNroitem() {
	return nroitem;
}
public void setNroitem(int nroitem) {
	this.nroitem = nroitem;
}
@Column(name="DESCRIPCION_ARTICULO")
public String getDescripcion_articulo() {
	return descripcion_articulo;
}
public void setDescripcion_articulo(String descripcion_articulo) {
	this.descripcion_articulo = descripcion_articulo;
}
@Column(name="CODIGO_ARTICULO")
public String getCodigo_articulo() {
	return codigo_articulo;
}
public void setCodigo_articulo(String codigo_articulo) {
	this.codigo_articulo = codigo_articulo;
}
@Column(name="CODIGO_UNIDAD")
public String getCodigo_unidad() {
	return codigo_unidad;
}
public void setCodigo_unidad(String codigo_unidad) {
	this.codigo_unidad = codigo_unidad;
}
@Column(name="DESCRIPCION_ADICIONAL")
public String getDescripcion_adicional() {
	return descripcion_adicional;
}
public void setDescripcion_adicional(String descripcion_adicional) {
	this.descripcion_adicional = descripcion_adicional;
}
@Column(name="CANTIDAD")
public BigDecimal getCantidad() {
	return cantidad;
}
public void setCantidad(BigDecimal cantidad) {
	this.cantidad = cantidad;
}
@Column(name="PRECIO_UNITARIO_SINIGV")
public BigDecimal getPrecio_unitario_sinigv() {
	return precio_unitario_sinigv;
}
public void setPrecio_unitario_sinigv(BigDecimal precio_unitario_sinigv) {
	this.precio_unitario_sinigv = precio_unitario_sinigv;
}
@Column(name="IMPORTE_SUBTOTAL")
public BigDecimal getImporte_subtotal() {
	return importe_subtotal;
}
public void setImporte_subtotal(BigDecimal importe_subtotal) {
	this.importe_subtotal = importe_subtotal;
}
@Column(name="IMPORTE_DESCUENTO")
public BigDecimal getImporte_descuento() {
	return importe_descuento;
}
public void setImporte_descuento(BigDecimal importe_descuento) {
	this.importe_descuento = importe_descuento;
}
@Column(name="IMPORTE_VALORVENTA")
public BigDecimal getImporte_valorventa() {
	return importe_valorventa;
}
public void setImporte_valorventa(BigDecimal importe_valorventa) {
	this.importe_valorventa = importe_valorventa;
}
@Column(name="IMPORTE_IGV")
public BigDecimal getImporte_igv() {
	return importe_igv;
}
public void setImporte_igv(BigDecimal importe_igv) {
	this.importe_igv = importe_igv;
}
@Column(name="IMPORTE_ISC")
public BigDecimal getImporte_isc() {
	return importe_isc;
}
public void setImporte_isc(BigDecimal importe_isc) {
	this.importe_isc = importe_isc;
}
@Column(name="PRECIO_UNITARIO_CONIGV")
public BigDecimal getPrecio_unitario_conigv() {
	return precio_unitario_conigv;
}
public void setPrecio_unitario_conigv(BigDecimal precio_unitario_conigv) {
	this.precio_unitario_conigv = precio_unitario_conigv;
}
@Column(name="IMPORTE_TOTAL")
public BigDecimal getImporte_total() {
	return importe_total;
}
public void setImporte_total(BigDecimal importe_total) {
	this.importe_total = importe_total;
}
@Column(name="TIPO_AFECCIONIGV")
public String getTipo_afeccionigv() {
	return tipo_afeccionigv;
}
public void setTipo_afeccionigv(String tipo_afeccionigv) {
	this.tipo_afeccionigv = tipo_afeccionigv;
}
@Column(name="ESGRAVADO")
public Boolean getEsgravado() {
	return esgravado;
}
public void setEsgravado(Boolean esgravado) {
	this.esgravado = esgravado;
}
@Column(name="ESEXONERADO")
public Boolean getEsexonerado() {
	return esexonerado;
}
public void setEsexonerado(Boolean esexonerado) {
	this.esexonerado = esexonerado;
}
@Column(name="ESINAFECTO")
public Boolean getEsinafecto() {
	return esinafecto;
}
public void setEsinafecto(Boolean esinafecto) {
	this.esinafecto = esinafecto;
}
@Column(name="ESGRATUITO")
public Boolean getEsgratuito() {
	return esgratuito;
}
public void setEsgratuito(Boolean esgratuito) {
	this.esgratuito = esgratuito;
}

 
}
