package pe.gob.sblm.sgi.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="SUNAT_COMPROBANTE"
    ,schema="dbo"
    ,catalog="sunatfe"
)
public class Sunat_Comprobante implements java.io.Serializable{
     
	private int Id_Comprobante;
	private String TipoDoc_Emisor;
	private String NroDoc_Emisor;
	private String RSocial_Emisor;
	private String NombreCorto_Emisor;
	private String Direccion_Emisor;
	private String Nombre_Oficina1_Emisor;
	private String Nombre_Oficina2_Emisor;
	private String DireccionSecundaria_Emisor;
	private String Urbanizacion_Emisor;
	private String Ubigeo_Emisor;
	private String Dpto_Emisor;
	private String Prov_Emisor;
	private String Dist_Emisor;
	private String Telefono_Emisor;
	private String CodPais_Emisor;
	private String tipoDoc_Receptor;
	private String nroDoc_Receptor;
	private String RSocial_Receptor;
	private String Direccion_Receptor;
	private String Urbanizacion_Receptor;
	private String Ubigeo_Receptor;
	private String Dpto_Receptor;
	private String Prov_Receptor;
	private String Dist_Receptor;
	private String Codigo_Upa_Receptor;
	private String Motivo_Pago_Receptor;
	private String CodPais_Receptor;
	private String Telefono_Receptor;
	private String Codigo_Documento;
	private String Serie_Documento;
	private String Numero_Documento;
	private Date fecha_emision;
	private Date Fecha_Vencimiento;
	private String Codigo_Moneda;
	private String Tipo_Venta;
	private Double Porcentaje_Detraccion;
	private String Codigo_Detraccion;
	private String Descripcion_Detraccion;
	private Double Importe_Detraccion;
	private String NroCuenta_Detraccion;
	private Double Importe_Gravado;
	private Double Importe_Exonerado;
	private Double Importe_Inafecto;
	private Double Importe_Gratuito;
	private Double Importe_SubTotal;
	private Double Importe_DsctoUnitario;
	private Double Importe_ValorVenta;
	private Double PorcentajeIGV;
	private Double Importe_IGV;
	private Double Importe_ISC;
	private Double Importe_Total;
	private Double Importe_DctoGlobal;
	private Double Importe_OtrosCargos;
	private Double Importe_OtrosTributos;
	private Double Importe_Perseccion;
	private Double Importe_Cobrado;
	private String Texto_Importe_Total;
	private String Codigo_Documento_Ref;
	private String Documento_Ref;
	private Date Fecha_Documento_Ref;
	private String Codigo_Motivo_Ref;
	private String Descripcion_Motivo_Ref;
	private String Email_Recepctor;
	private String Email_Emisor;
	private String Glosario;
	private String codigoSunat;
	private String CodigoHash;
	private String CodigoBarra;
	private String CodigoRubro;
	private Boolean Estado;
	private Date FechaHora_Generacion;
	private Date FechaHora_EnvioSunat;
	private String respuesta_sunat;
	private String Mensaje_Sunat;
	private String RutaXML_DLL;
	private String RutaXML_SNT;
	private String sRutaPDF_ITS;
	private String origen_documento;
	private int id_referencia;
	private String Observaciones;
	private String Usuario_Crea;
	private Date Fecha_Crea;
	private String Host_IP_Crea;
	private String Usuario_Modifica;
	private Date Fecha_Modifica;
	private String Host_IP_Modifica;
	private String Usuario_appSunat_Envio;
	private Date Fecha_appSunat_Envio;
	private String Host_IP_appSunat_Envio;
	private String Usuario_Anula_Documento;
	private Date Fecha_Anula_Documento;
	private String Host_IP_Anula_Documento;
	
	public Sunat_Comprobante() {
		// TODO Auto-generated constructor stub
	}
	 @Id
	 @GeneratedValue
	 @Column(name="ID_COMPROBANTE" ,unique=true ,nullable=false)
	public int getId_Comprobante() {
		return Id_Comprobante;
	}
    
	public void setId_Comprobante(int id_Comprobante) {
		Id_Comprobante = id_Comprobante;
	}
	@Column(name="TIPODOC_EMISOR", length=2)
	public String getTipoDoc_Emisor() {
		return TipoDoc_Emisor;
	}

	public void setTipoDoc_Emisor(String tipoDoc_Emisor) {
		TipoDoc_Emisor = tipoDoc_Emisor;
	}
	@Column(name="NRODOC_EMISOR")
	public String getNroDoc_Emisor() {
		return NroDoc_Emisor;
	}
   
	public void setNroDoc_Emisor(String nroDoc_Emisor) {
		NroDoc_Emisor = nroDoc_Emisor;
	}
	@Column(name="RSOCIAL_EMISOR")
	public String getRSocial_Emisor() {
		return RSocial_Emisor;
	}

	public void setRSocial_Emisor(String rSocial_Emisor) {
		RSocial_Emisor = rSocial_Emisor;
	}
	@Column(name="NOMBRECORTO_EMISOR")
	public String getNombreCorto_Emisor() {
		return NombreCorto_Emisor;
	}

	public void setNombreCorto_Emisor(String nombreCorto_Emisor) {
		NombreCorto_Emisor = nombreCorto_Emisor;
	}
	@Column(name="DIRECCION_EMISOR")
	public String getDireccion_Emisor() {
		return Direccion_Emisor;
	}

	public void setDireccion_Emisor(String direccion_Emisor) {
		Direccion_Emisor = direccion_Emisor;
	}
	@Column(name="NOMBRE_OFICINA1_EMISOR")
	public String getNombre_Oficina1_Emisor() {
		return Nombre_Oficina1_Emisor;
	}

	public void setNombre_Oficina1_Emisor(String nombre_Oficina1_Emisor) {
		Nombre_Oficina1_Emisor = nombre_Oficina1_Emisor;
	}
	@Column(name="NOMBRE_OFICINA2_EMISOR")
	public String getNombre_Oficina2_Emisor() {
		return Nombre_Oficina2_Emisor;
	}

	public void setNombre_Oficina2_Emisor(String nombre_Oficina2_Emisor) {
		Nombre_Oficina2_Emisor = nombre_Oficina2_Emisor;
	}
	@Column(name="DIRECCIONSECUNDARIA_EMISOR")
	public String getDireccionSecundaria_Emisor() {
		return DireccionSecundaria_Emisor;
	}

	public void setDireccionSecundaria_Emisor(String direccionSecundaria_Emisor) {
		DireccionSecundaria_Emisor = direccionSecundaria_Emisor;
	}
	@Column(name="URBANIZACION_EMISOR")
	public String getUrbanizacion_Emisor() {
		return Urbanizacion_Emisor;
	}

	public void setUrbanizacion_Emisor(String urbanizacion_Emisor) {
		Urbanizacion_Emisor = urbanizacion_Emisor;
	}
	@Column(name="UBIGEO_EMISOR")
	public String getUbigeo_Emisor() {
		return Ubigeo_Emisor;
	}

	public void setUbigeo_Emisor(String ubigeo_Emisor) {
		Ubigeo_Emisor = ubigeo_Emisor;
	}
	@Column(name="DPTO_EMISOR")
	public String getDpto_Emisor() {
		return Dpto_Emisor;
	}

	public void setDpto_Emisor(String dpto_Emisor) {
		Dpto_Emisor = dpto_Emisor;
	}
	@Column(name="PROV_EMISOR")
	public String getProv_Emisor() {
		return Prov_Emisor;
	}

	public void setProv_Emisor(String prov_Emisor) {
		Prov_Emisor = prov_Emisor;
	}
	@Column(name="DIST_EMISOR")
	public String getDist_Emisor() {
		return Dist_Emisor;
	}

	public void setDist_Emisor(String dist_Emisor) {
		Dist_Emisor = dist_Emisor;
	}
	@Column(name="TELEFONO_EMISOR")
	public String getTelefono_Emisor() {
		return Telefono_Emisor;
	}

	public void setTelefono_Emisor(String telefono_Emisor) {
		Telefono_Emisor = telefono_Emisor;
	}
	@Column(name="CODPAIS_EMISOR")
	public String getCodPais_Emisor() {
		return CodPais_Emisor;
	}

	public void setCodPais_Emisor(String codPais_Emisor) {
		CodPais_Emisor = codPais_Emisor;
	}
	@Column(name="TIPODOC_RECEPTOR")
	public String getTipoDoc_Receptor() {
		return tipoDoc_Receptor;
	}

	public void setTipoDoc_Receptor(String tipoDocReceptor) {
		tipoDoc_Receptor = tipoDocReceptor;
	}
	@Column(name="NRODOC_RECEPTOR")
	public String getNroDoc_Receptor() {
		return nroDoc_Receptor;
	}

	public void setNroDoc_Receptor(String nroDocReceptor) {
		nroDoc_Receptor = nroDocReceptor;
	}
	@Column(name="RSOCIAL_RECEPTOR")
	public String getRSocial_Receptor() {
		return RSocial_Receptor;
	}

	public void setRSocial_Receptor(String rSocial_Receptor) {
		RSocial_Receptor = rSocial_Receptor;
	}
	@Column(name="DIRECCION_RECEPTOR")
	public String getDireccion_Receptor() {
		return Direccion_Receptor;
	}

	public void setDireccion_Receptor(String direccion_Receptor) {
		Direccion_Receptor = direccion_Receptor;
	}
	@Column(name="URBANIZACION_RECEPTOR")
	public String getUrbanizacion_Receptor() {
		return Urbanizacion_Receptor;
	}

	public void setUrbanizacion_Receptor(String urbanizacion_Receptor) {
		Urbanizacion_Receptor = urbanizacion_Receptor;
	}
	@Column(name="UBIGEO_RECEPTOR")
	public String getUbigeo_Receptor() {
		return Ubigeo_Receptor;
	}

	public void setUbigeo_Receptor(String ubigeo_Receptor) {
		Ubigeo_Receptor = ubigeo_Receptor;
	}
	@Column(name="DPTO_RECEPTOR")
	public String getDpto_Receptor() {
		return Dpto_Receptor;
	}

	public void setDpto_Receptor(String dpto_Receptor) {
		Dpto_Receptor = dpto_Receptor;
	}
	@Column(name="PROV_RECEPTOR")
	public String getProv_Receptor() {
		return Prov_Receptor;
	}

	public void setProv_Receptor(String prov_Receptor) {
		Prov_Receptor = prov_Receptor;
	}
	@Column(name="DIST_RECEPTOR")
	public String getDist_Receptor() {
		return Dist_Receptor;
	}

	public void setDist_Receptor(String dist_Receptor) {
		Dist_Receptor = dist_Receptor;
	}
	@Column(name="CODIGO_UPA_RECEPTOR")
	public String getCodigo_Upa_Receptor() {
		return Codigo_Upa_Receptor;
	}

	public void setCodigo_Upa_Receptor(String codigo_Upa_Receptor) {
		Codigo_Upa_Receptor = codigo_Upa_Receptor;
	}
	@Column(name="MOTIVO_PAGO_RECEPTOR")
	public String getMotivo_Pago_Receptor() {
		return Motivo_Pago_Receptor;
	}

	public void setMotivo_Pago_Receptor(String motivo_Pago_Receptor) {
		Motivo_Pago_Receptor = motivo_Pago_Receptor;
	}
	@Column(name="CODPAIS_RECEPTOR")
	public String getCodPais_Receptor() {
		return CodPais_Receptor;
	}

	public void setCodPais_Receptor(String codPais_Receptor) {
		CodPais_Receptor = codPais_Receptor;
	}
	@Column(name="TELEFONO_RECEPTOR")
	public String getTelefono_Receptor() {
		return Telefono_Receptor;
	}

	public void setTelefono_Receptor(String telefono_Receptor) {
		Telefono_Receptor = telefono_Receptor;
	}
	@Column(name="CODIGO_DOCUMENTO")
	public String getCodigo_Documento() {
		return Codigo_Documento;
	}

	public void setCodigo_Documento(String codigo_Documento) {
		Codigo_Documento = codigo_Documento;
	}
	@Column(name="SERIE_DOCUMENTO")
	public String getSerie_Documento() {
		return Serie_Documento;
	}

	public void setSerie_Documento(String serie_Documento) {
		Serie_Documento = serie_Documento;
	}
	@Column(name="NUMERO_DOCUMENTO")
	public String getNumero_Documento() {
		return Numero_Documento;
	}

	public void setNumero_Documento(String numero_Documento) {
		Numero_Documento = numero_Documento;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="Fecha_Emision", length=23)
	public Date getFecha_emision() {
		return fecha_emision;
	}

	public void setFecha_emision(Date fecha_Emision) {
		fecha_emision = fecha_Emision;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="FECHA_VENCIMIENTO", length=23)
	public Date getFecha_Vencimiento() {
		return Fecha_Vencimiento;
	}

	public void setFecha_Vencimiento(Date fecha_Vencimiento) {
		Fecha_Vencimiento = fecha_Vencimiento;
	}
	@Column(name="CODIGO_MONEDA")
	public String getCodigo_Moneda() {
		return Codigo_Moneda;
	}

	public void setCodigo_Moneda(String codigo_Moneda) {
		Codigo_Moneda = codigo_Moneda;
	}
	@Column(name="TIPO_VENTA")
	public String getTipo_Venta() {
		return Tipo_Venta;
	}

	public void setTipo_Venta(String tipo_Venta) {
		Tipo_Venta = tipo_Venta;
	}
	@Column(name="PORCENTAJE_DETRACCION")
	public Double getPorcentaje_Detraccion() {
		return Porcentaje_Detraccion;
	}

	public void setPorcentaje_Detraccion(Double porcentaje_Detraccion) {
		Porcentaje_Detraccion = porcentaje_Detraccion;
	}
	@Column(name="CODIGO_DETRACCION")
	public String getCodigo_Detraccion() {
		return Codigo_Detraccion;
	}

	public void setCodigo_Detraccion(String codigo_Detraccion) {
		Codigo_Detraccion = codigo_Detraccion;
	}
	@Column(name="DESCRIPCION_DETRACCION")
	public String getDescripcion_Detraccion() {
		return Descripcion_Detraccion;
	}

	public void setDescripcion_Detraccion(String descripcion_Detraccion) {
		Descripcion_Detraccion = descripcion_Detraccion;
	}
	@Column(name="IMPORTE_DETRACCION")
	public Double getImporte_Detraccion() {
		return Importe_Detraccion;
	}

	public void setImporte_Detraccion(Double importe_Detraccion) {
		Importe_Detraccion = importe_Detraccion;
	}
	@Column(name="NROCUENTA_DETRACCION")
	public String getNroCuenta_Detraccion() {
		return NroCuenta_Detraccion;
	}

	public void setNroCuenta_Detraccion(String nroCuenta_Detraccion) {
		NroCuenta_Detraccion = nroCuenta_Detraccion;
	}
	@Column(name="IMPORTE_GRAVADO")
	public Double getImporte_Gravado() {
		return Importe_Gravado;
	}

	public void setImporte_Gravado(Double importe_Gravado) {
		Importe_Gravado = importe_Gravado;
	}
	@Column(name="IMPORTE_EXONERADO")
	public Double getImporte_Exonerado() {
		return Importe_Exonerado;
	}

	public void setImporte_Exonerado(Double importe_Exonerado) {
		Importe_Exonerado = importe_Exonerado;
	}
	@Column(name="IMPORTE_INAFECTO")
	public Double getImporte_Inafecto() {
		return Importe_Inafecto;
	}

	public void setImporte_Inafecto(Double importe_Inafecto) {
		Importe_Inafecto = importe_Inafecto;
	}
	@Column(name="IMPORTE_GRATUITO")
	public Double getImporte_Gratuito() {
		return Importe_Gratuito;
	}

	public void setImporte_Gratuito(Double importe_Gratuito) {
		Importe_Gratuito = importe_Gratuito;
	}
	@Column(name="IMPORTE_SUBTOTAL")
	public Double getImporte_SubTotal() {
		return Importe_SubTotal;
	}

	public void setImporte_SubTotal(Double importe_SubTotal) {
		Importe_SubTotal = importe_SubTotal;
	}
	@Column(name="IMPORTE_DSCTOUNITARIO")
	public Double getImporte_DsctoUnitario() {
		return Importe_DsctoUnitario;
	}

	public void setImporte_DsctoUnitario(Double importe_DsctoUnitario) {
		Importe_DsctoUnitario = importe_DsctoUnitario;
	}
	@Column(name="IMPORTE_VALORVENTA")
	public Double getImporte_ValorVenta() {
		return Importe_ValorVenta;
	}

	public void setImporte_ValorVenta(Double importe_ValorVenta) {
		Importe_ValorVenta = importe_ValorVenta;
	}
	@Column(name="PORCENTAJEIGV")
	public Double getPorcentajeIGV() {
		return PorcentajeIGV;
	}

	public void setPorcentajeIGV(Double porcentajeIGV) {
		PorcentajeIGV = porcentajeIGV;
	}
	@Column(name="IMPORTE_IGV")
	public Double getImporte_IGV() {
		return Importe_IGV;
	}

	public void setImporte_IGV(Double importe_IGV) {
		Importe_IGV = importe_IGV;
	}
	@Column(name="IMPORTE_ISC")
	public Double getImporte_ISC() {
		return Importe_ISC;
	}

	public void setImporte_ISC(Double importe_ISC) {
		Importe_ISC = importe_ISC;
	}
	@Column(name="IMPORTE_TOTAL")
	public Double getImporte_Total() {
		return Importe_Total;
	}

	public void setImporte_Total(Double importe_Total) {
		Importe_Total = importe_Total;
	}
	@Column(name="IMPORTE_DCTOGLOBAL")
	public Double getImporte_DctoGlobal() {
		return Importe_DctoGlobal;
	}

	public void setImporte_DctoGlobal(Double importe_DctoGlobal) {
		Importe_DctoGlobal = importe_DctoGlobal;
	}
	@Column(name="IMPORTE_OTROSCARGOS")
	public Double getImporte_OtrosCargos() {
		return Importe_OtrosCargos;
	}

	public void setImporte_OtrosCargos(Double importe_OtrosCargos) {
		Importe_OtrosCargos = importe_OtrosCargos;
	}
	@Column(name="IMPORTE_OTROSTRIBUTOS")
	public Double getImporte_OtrosTributos() {
		return Importe_OtrosTributos;
	}

	public void setImporte_OtrosTributos(Double importe_OtrosTributos) {
		Importe_OtrosTributos = importe_OtrosTributos;
	}
	@Column(name="IMPORTE_PERSECCION")
	public Double getImporte_Perseccion() {
		return Importe_Perseccion;
	}

	public void setImporte_Perseccion(Double importe_Perseccion) {
		Importe_Perseccion = importe_Perseccion;
	}
	@Column(name="IMPORTE_COBRADO")
	public Double getImporte_Cobrado() {
		return Importe_Cobrado;
	}

	public void setImporte_Cobrado(Double importe_Cobrado) {
		Importe_Cobrado = importe_Cobrado;
	}
	@Column(name="TEXTO_IMPORTE_TOTAL")
	public String getTexto_Importe_Total() {
		return Texto_Importe_Total;
	}

	public void setTexto_Importe_Total(String texto_Importe_Total) {
		Texto_Importe_Total = texto_Importe_Total;
	}
	@Column(name="CODIGO_DOCUMENTO_REF")
	public String getCodigo_Documento_Ref() {
		return Codigo_Documento_Ref;
	}

	public void setCodigo_Documento_Ref(String codigo_Documento_Ref) {
		Codigo_Documento_Ref = codigo_Documento_Ref;
	}
	@Column(name="DOCUMENTO_REF")
	public String getDocumento_Ref() {
		return Documento_Ref;
	}

	public void setDocumento_Ref(String documento_Ref) {
		Documento_Ref = documento_Ref;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="FECHA_DOCUMENTO_REF",length=23)
	public Date getFecha_Documento_Ref() {
		return Fecha_Documento_Ref;
	}

	public void setFecha_Documento_Ref(Date fecha_Documento_Ref) {
		Fecha_Documento_Ref = fecha_Documento_Ref;
	}

	@Column(name="CODIGO_MOTIVO_REF")
	public String getCodigo_Motivo_Ref() {
		return Codigo_Motivo_Ref;
	}

	public void setCodigo_Motivo_Ref(String codigo_Motivo_Ref) {
		Codigo_Motivo_Ref = codigo_Motivo_Ref;
	}
	@Column(name="DESCRIPCION_MOTIVO_REF")
	public String getDescripcion_Motivo_Ref() {
		return Descripcion_Motivo_Ref;
	}

	public void setDescripcion_Motivo_Ref(String descripcion_Motivo_Ref) {
		Descripcion_Motivo_Ref = descripcion_Motivo_Ref;
	}
	@Column(name="EMAIL_RECEPCTOR")
	public String getEmail_Recepctor() {
		return Email_Recepctor;
	}

	public void setEmail_Recepctor(String email_Recepctor) {
		Email_Recepctor = email_Recepctor;
	}
	@Column(name="EMAIL_EMISOR")
	public String getEmail_Emisor() {
		return Email_Emisor;
	}

	public void setEmail_Emisor(String email_Emisor) {
		Email_Emisor = email_Emisor;
	}
	@Column(name="GLOSARIO")
	public String getGlosario() {
		return Glosario;
	}

	public void setGlosario(String glosario) {
		Glosario = glosario;
	}
	@Column(name="CodigoSunat")
	public String getCodigoSunat() {
		return codigoSunat;
	}

	public void setCodigoSunat(String codigo_Sunat) {
		codigoSunat = codigo_Sunat;
	}
	@Column(name="CODIGOHASH")
	public String getCodigoHash() {
		return CodigoHash;
	}

	public void setCodigoHash(String codigoHash) {
		CodigoHash = codigoHash;
	}
	@Column(name="CODIGOBARRA")
	public String getCodigoBarra() {
		return CodigoBarra;
	}

	public void setCodigoBarra(String codigoBarra) {
		CodigoBarra = codigoBarra;
	}
	@Column(name="CODIGORUBRO")
	public String getCodigoRubro() {
		return CodigoRubro;
	}
  
	public void setCodigoRubro(String codigoRubro) {
		CodigoRubro = codigoRubro;
	}
	@Column(name="ESTADO")
	public Boolean getEstado() {
		return Estado;
	}

	public void setEstado(Boolean estado) {
		Estado = estado;
	}
	@Column(name="FECHAHORA_GENERACION")
	public Date getFechaHora_Generacion() {
		return FechaHora_Generacion;
	}

	public void setFechaHora_Generacion(Date fechaHora_Generacion) {
		FechaHora_Generacion = fechaHora_Generacion;
	}
	@Column(name="FECHAHORA_ENVIOSUNAT")
	public Date getFechaHora_EnvioSunat() {
		return FechaHora_EnvioSunat;
	}

	public void setFechaHora_EnvioSunat(Date fechaHora_EnvioSunat) {
		FechaHora_EnvioSunat = fechaHora_EnvioSunat;
	}
	@Column(name="Respuesta_Sunat")
	public String getRespuesta_sunat() {
		return respuesta_sunat;
	}

	public void setRespuesta_sunat(String respuesta_Sunat) {
		respuesta_sunat = respuesta_Sunat;
	}
	@Column(name="MENSAJE_SUNAT")
	public String getMensaje_Sunat() {
		return Mensaje_Sunat;
	}

	public void setMensaje_Sunat(String mensaje_Sunat) {
		Mensaje_Sunat = mensaje_Sunat;
	}
	@Column(name="RUTAXML_DLL")
	public String getRutaXML_DLL() {
		return RutaXML_DLL;
	}

	public void setRutaXML_DLL(String rutaXML_DLL) {
		RutaXML_DLL = rutaXML_DLL;
	}
	@Column(name="RUTAXML_SNT")
	public String getRutaXML_SNT() {
		return RutaXML_SNT;
	}

	public void setRutaXML_SNT(String rutaXML_SNT) {
		RutaXML_SNT = rutaXML_SNT;
	}
	@Column(name="SRUTAPDF_ITS")
	public String getsRutaPDF_ITS() {
		return sRutaPDF_ITS;
	}

	public void setsRutaPDF_ITS(String sRutaPDF_ITS) {
		this.sRutaPDF_ITS = sRutaPDF_ITS;
	}
    @Column(name="Origen_documento")
	public String getOrigen_documento() {
		return origen_documento;
	}

	public void setOrigen_documento(String origen_document) {
		origen_documento = origen_document;
	}
    @Column(name="Id_Referencia")
	public int getId_referencia() {
		return id_referencia;
	}
	public void setId_referencia(int id_referencia) {
		this.id_referencia = id_referencia;
	}
	@Column(name="OBSERVACIONES")
	public String getObservaciones() {
		return Observaciones;
	}

	public void setObservaciones(String observaciones) {
		Observaciones = observaciones;
	}
	@Column(name="USUARIO_CREA")
	public String getUsuario_Crea() {
		return Usuario_Crea;
	}

	public void setUsuario_Crea(String usuario_Crea) {
		Usuario_Crea = usuario_Crea;
	}
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name="FECHA_CREA", length=23)
	public Date getFecha_Crea() {
		return Fecha_Crea;
	}

	public void setFecha_Crea(Date fecha_Crea) {
		Fecha_Crea = fecha_Crea;
	}
    @Column(name="HOST_IP_CREA")
	public String getHost_IP_Crea() {
		return Host_IP_Crea;
	}

	public void setHost_IP_Crea(String host_IP_Crea) {
		Host_IP_Crea = host_IP_Crea;
	}
    @Column(name="USUARIO_MODIFICA")
	public String getUsuario_Modifica() {
		return Usuario_Modifica;
	}

	public void setUsuario_Modifica(String usuario_Modifica) {
		Usuario_Modifica = usuario_Modifica;
	}
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name="FECHA_MODIFICA" ,length=23)
	public Date getFecha_Modifica() {
		return Fecha_Modifica;
	}

	public void setFecha_Modifica(Date fecha_Modifica) {
		Fecha_Modifica = fecha_Modifica;
	}
    @Column(name="HOST_IP_MODIFICA")
	public String getHost_IP_Modifica() {
		return Host_IP_Modifica;
	}

	public void setHost_IP_Modifica(String host_IP_Modifica) {
		Host_IP_Modifica = host_IP_Modifica;
	}
    @Column(name="USUARIO_APPSUNAT_ENVIO")
	public String getUsuario_appSunat_Envio() {
		return Usuario_appSunat_Envio;
	}

	public void setUsuario_appSunat_Envio(String usuario_appSunat_Envio) {
		Usuario_appSunat_Envio = usuario_appSunat_Envio;
	}
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name="FECHA_APPSUNAT_ENVIO" , length=23)
	public Date getFecha_appSunat_Envio() {
		return Fecha_appSunat_Envio;
	}

	public void setFecha_appSunat_Envio(Date fecha_appSunat_Envio) {
		Fecha_appSunat_Envio = fecha_appSunat_Envio;
	}
    @Column(name="HOST_IP_APPSUNAT_ENVIO")
	public String getHost_IP_appSunat_Envio() {
		return Host_IP_appSunat_Envio;
	}

	public void setHost_IP_appSunat_Envio(String host_IP_appSunat_Envio) {
		Host_IP_appSunat_Envio = host_IP_appSunat_Envio;
	}
    @Column(name="USUARIO_ANULA_DOCUMENTO")
	public String getUsuario_Anula_Documento() {
		return Usuario_Anula_Documento;
	}

	public void setUsuario_Anula_Documento(String usuario_Anula_Documento) {
		Usuario_Anula_Documento = usuario_Anula_Documento;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="FECHA_ANULA_DOCUMENTO",length=23)
	public Date getFecha_Anula_Documento() {
		return Fecha_Anula_Documento;
	}

	public void setFecha_Anula_Documento(Date fecha_Anula_Documento) {
		Fecha_Anula_Documento = fecha_Anula_Documento;
	}
    @Column(name="HOST_IP_ANULA_DOCUMENTO")
	public String getHost_IP_Anula_Documento() {
		return Host_IP_Anula_Documento;
	}

	public void setHost_IP_Anula_Documento(String host_IP_Anula_Documento) {
		Host_IP_Anula_Documento = host_IP_Anula_Documento;
	}
    
}
