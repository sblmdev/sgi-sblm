package pe.gob.sblm.sgi.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="MAE_CONFIGURACION_PARAMETRO", schema = "dbo", catalog = "sisinmueble")
public class MaeConfiguracionParametro implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name="ID_CONFIGURACION_PARAMETRO")
	private int id_configuracion_parametro;
	
	@Column(name="N_TABLA")
	private String n_tabla;

	@Column(name="N_PARAMETRO")
	private String n_parametro;

    @Column(name="N_VALOR")
	private String n_valor;

    @Column(name="N_DESCRIPCION")
    private String n_descripcion;
    
    @Column(name="C_OPERACION")
    private String c_operacion;
    
    @Column(name="N_OPERACION")
    private String n_operacion;
    
    @Column(name="N_DIAS")
    private Integer n_dias;
    
    @Column(name="USR_CRE")
    private String usr_cre;
    
    @Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FEC_CRE", length = 23)
    private Date fec_cre;
    
    @Column(name="USR_MOD")
    private String usr_mod;
    
    @Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FEC_MOD", length = 23)
    private Date fec_mod;
    
    @Column(name="ACTIVO")
    private String activo;

	public MaeConfiguracionParametro() {

	}

	public int getId_configuracion_parametro() {
		return id_configuracion_parametro;
	}

	public void setId_configuracion_parametro(int id_configuracion_parametro) {
		this.id_configuracion_parametro = id_configuracion_parametro;
	}

	public String getN_tabla() {
		return n_tabla;
	}

	public void setN_tabla(String n_tabla) {
		this.n_tabla = n_tabla;
	}

	public String getN_parametro() {
		return n_parametro;
	}

	public void setN_parametro(String n_parametro) {
		this.n_parametro = n_parametro;
	}

	public String getN_valor() {
		return n_valor;
	}

	public void setN_valor(String n_valor) {
		this.n_valor = n_valor;
	}

	public String getN_descripcion() {
		return n_descripcion;
	}

	public void setN_descripcion(String n_descripcion) {
		this.n_descripcion = n_descripcion;
	}

	public String getC_operacion() {
		return c_operacion;
	}

	public void setC_operacion(String c_operacion) {
		this.c_operacion = c_operacion;
	}

	public String getN_operacion() {
		return n_operacion;
	}

	public void setN_operacion(String n_operacion) {
		this.n_operacion = n_operacion;
	}

	public Integer getN_dias() {
		return n_dias;
	}

	public void setN_dias(Integer n_dias) {
		this.n_dias = n_dias;
	}

	public String getUsr_cre() {
		return usr_cre;
	}

	public void setUsr_cre(String usr_cre) {
		this.usr_cre = usr_cre;
	}

	public Date getFec_cre() {
		return fec_cre;
	}

	public void setFec_cre(Date fec_cre) {
		this.fec_cre = fec_cre;
	}

	public String getUsr_mod() {
		return usr_mod;
	}

	public void setUsr_mod(String usr_mod) {
		this.usr_mod = usr_mod;
	}

	public Date getFec_mod() {
		return fec_mod;
	}

	public void setFec_mod(Date fec_mod) {
		this.fec_mod = fec_mod;
	}

	public String getActivo() {
		return activo;
	}

	public void setActivo(String activo) {
		this.activo = activo;
	}
	
	

}
