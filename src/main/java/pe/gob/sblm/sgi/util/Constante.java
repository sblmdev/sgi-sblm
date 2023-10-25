package pe.gob.sblm.sgi.util;

import java.util.Map;
import java.util.HashMap;

public class Constante {
	/**TIPO DE DOCUMENTO**/
	public static final String TIPO_DOCUMENTO_SIN_VALOR_LEGAL="00";
	public static final String TIPO_DOCUMENTO_FACTURA="01";
	public static final String TIPO_DOCUMENTO_BOLETA_VENTA="03";
	public static final String TIPO_DOCUMENTO_RECIBO_CAJA="04";
	public static final String TIPO_DOCUMENTO_NOTA_DEBITO="08";
	public static final String TIPO_DOCUMENTO_NOTA_CREDITO="09";
	public static final String TIPO_DOCUMENTO_NOTA_CREDITO_="10";
	public static final String TIPO_DOCUMENTO_NOTA_CREDITO_MORA="11";
	public static final String TIPO_DOCUMENTO_DOCUMENTO_ADMINISTRATIVO="DA";
	public static final String TIPO_DOCUMENTO_PERIODO_GRACIA="PD";
	/**NOMBRE  DE DOCUMENTO **/
	public static final String TIPO_DOCUMENTO_00="Sin valor legal";
	public static final String TIPO_DOCUMENTO_01="Factura";
	public static final String TIPO_DOCUMENTO_03="Boleta de Venta";
	public static final String TIPO_DOCUMENTO_04="Recibo de caja";
	public static final String TIPO_DOCUMENTO_08="Nota de Débito";
	public static final String TIPO_DOCUMENTO_09="Nota de Crédito";
	public static final String TIPO_DOCUMENTO_DA="Documento Administrativo";
	public static final String TIPO_DOCUMENTO_PG="Periodo de Gracia";
	/**TIPO DE CONCEPTO*/
	public static final String TIPO_CONCEPTO_M_TAURINO="01";
	public static final String TIPO_CONCEPTO_SOLICITUD="02";
	public static final String TIPO_CONCEPTO_P_GARANTIA="03";
	public static final String TIPO_CONCEPTO_F_RIMAC="04";
	public static final String TIPO_CONCEPTO_O_INGRESOS="05";
	public static final String TIPO_CONCEPTO_RENTA="06";
	public static final String TIPO_CONCEPTO_MORA="07";
	public static final String TIPO_CONCEPTO_R_RENTA="08";
	public static final String TIPO_CONCEPTO_NINGUNO="09";
	public static final String TIPO_CONCEPTO_R_V_ACHO="10";
	public static final String TIPO_CONCEPTO_PPA="11";
	public static final String TIPO_CONCEPTO_CARTA_FIANZA="12";
	public static final String TIPO_CONCEPTO_REBAJA_MORA="13";
	public static final String TIPO_CONCEPTO_ANULACION_DOC="14";
	public static final String TIPO_CONCEPTO_RECHAZADO="15";
	public static final String TIPO_CONCEPTO_REC_A="16";
	public static final String TIPO_CONCEPTO_PENALIZACION="17";
	public static final String TIPO_CONCEPTO_RECONO_DEUDA="18";
    /** NOMBRE DE CONCEPTO*/
	public static final String TIPO_CONCEPTO_01="Museo Taurino	";
	public static final String TIPO_CONCEPTO_02="Solicitud";
	public static final String TIPO_CONCEPTO_03="Pago de Garantia";
	public static final String TIPO_CONCEPTO_04="Finca Rimac";
	public static final String TIPO_CONCEPTO_05="Otros ingresos";
	public static final String TIPO_CONCEPTO_06="Renta";
	public static final String TIPO_CONCEPTO_07="Mora";
	public static final String TIPO_CONCEPTO_08="Rebaja de Renta";
	public static final String TIPO_CONCEPTO_09="Ninguno";
	public static final String TIPO_CONCEPTO_10="R.V. Acho";
	public static final String TIPO_CONCEPTO_11="P.P.A. Esp. Lib.";
	public static final String TIPO_CONCEPTO_12="Carta Fianza Ejecutada";
	public static final String TIPO_CONCEPTO_13="Rebaja Mora";
	public static final String TIPO_CONCEPTO_14="Anulación Documento";
	public static final String TIPO_CONCEPTO_15="Rechazado";
	public static final String TIPO_CONCEPTO_16="Recuperación de Arbitrios";
	public static final String TIPO_CONCEPTO_17="Penalización";
	public static final String TIPO_CONCEPTO_18="Reconocimiento de Deuda";
	/** TIPO DE MOVIMIENTO **/
	public static final String TIPO_MOVIMIENTO_CANCELACION_MENSUAL       ="01";
	public static final String TIPO_MOVIMIENTO_PAGO_A_CUENTA             ="02";
	public static final String TIPO_MOVIMIENTO_PAGO_GARANTIA             ="03";
	public static final String TIPO_MOVIMIENTO_NINGUNO                   ="04";
	public static final String TIPO_MOVIMIENTO_EXTINCION_DE_DEUDA        ="05";
	public static final String TIPO_MOVIMIENTO_PAGO_RECONOCIMIENTO_DEUDA ="06";
	/** OPCIONES**/
	public static final String OPCION_RECUPERACION_ARBITRIOS="RA";
	/**ARBITRIO **/
	public static final int  NUMERO_PERIODO=4;
	/**Contrato**/
	public static final String CONDICION_CONTRATO="Contrato";
	public static final String CONDICION_SINCONTRATO="Sin Contrato";
	public static final String CONDICION_PRECONTRATO="Precontrato";
	public static final String CONDICION_RECONOCIMIENTO_DEUDA= "Reconocimiento Deuda";
	/** Condicion de Contrato */
	public static final String CONDICION_CONTRATO_VIGENTE="VIGENTE";
	public static final String CONDICION_CONTRATO_FINALIZADO="FINALIZADO";
	public static final String CONDICION_CONTRATO_ANULADO="ANULADO";
	/** Nuevos contrato tabla MaeDetalleCondicion*/
	public static final String ESTADO_GENERADO="GENERADO";
	public static final String ESTADO_ANULADO="ANULADO";
	/** Frecuencia de Arbitrio */
	public static final String FRECUENCIA_ARBITRIO="Trimestral";
	/** Estados de Arbitrio*/
	public static final String ESTADO_CANCELADO="CANCELADO";
	public static final String ESTADO_FINALIZAFO="FINALIZADO";
	public static final String ESTADO_PENDIENTE="PENDIENTE";
	public static final String ESTADO_VIGENTE="VIGENTE";
	/** Tipo de Moneda*/
	public static final String MONEDA_SOLES="S";
	/**Si no registra contrato para la upa*/
    public static final String NO_NOMBRE_INQUILINO="NO REGISTRA INQUILINO";
    /**Tipo de opcion de documento DNI Y RUC*/
    public static final String OPCION_TIPO_DOC_DNI="1";
    public static final String OPCION_TIPO_DOC_RUC="2";	
    public static final String TIPO_DOC_RECEPTOR_RUC="6";
    public static final String TIPO_DOC_RECEPTOR_DNI="1";
    public static final String TIPO_DOC_RECEPTOR_CARNET_EXTRANJERIA="4";
    public static final String NRO_DOC_RECEPTOR_INVALIDO="00000000";
    public static final String OPCION_REPORTE_CANCELADOS="1";
    public static final String OPCION_REPORTE_PENDIENTES="2";
    public static final String OPCION_REPORTE_CANCELADOS_PENDIENTES="3";
    
    /** tipo de documento por escritura publica*/ 
    public static final String  TIPO_DOC_ESCRITURA_PUBLICA="ESCRITURA PUBLICA";
     
    /** tipo de nota de credito */
    public static final String TIPO_NOTA_CREDITO_1="TOTAL";   // GENERAR NOTA DE CREDITO  TOTAL
    public static final String TIPO_NOTA_CREDITO_2="PARCIAL"; // GENERAR NOTA DE CREDITO  PARCIAL
    public static final String TIPO_NOTA_CREDITO_3="GENERAR"; // GENERAR UNA SEGUNDA NOTA DE CREDITO
    /** Reporte*/
    public static final String CONDICION_CONTRATO_VIGENTE_FINALIZADO="GLOBAL";
    /** Tipo de Moneda*/
    public static final String TIPO_MONEDA_SOLES="S";
    public static final String TIPO_MONEDA_DOLARES="D";
    public static final String TIPO_MONEDA_SOLES_FORMATO="S/.";
    public static final String TIPO_MONEDA_DOLARES_FORMATO="$";
    /** Tipo de Moneda*/
    public static final String TIPO_MONEDA_S="SOLES";
    public static final String TIPO_MONEDA_D="DOLARES";
    /** Tipo de consulta */
    public static final String TIPO_CONSULTA_USUARIO="USUARIO";
    public static final String TIPO_CONSULTA_DETRACCION="DETRACCION";
    public static final String TIPO_CONSULTA_AUTODETRACCION="AUTODETRACCION";
    public static final String TIPO_CONSULTA_COBRADOR="COBRADOR";
    /** ESTADO UPA */
    public static final String ESTADO_A="0";// ACTIVO
    public static final String ESTADO_B="1";// BLOQUEADO
    public static final String ESTADO_C="2";// ANULADO
  
    public static final String ESTADO_REG="REGISTRADO";
    public static final String ESTADO_ACE="ACEPTADO";
    
    /** ESTADO DE DOCUMENTOS DE PAGO*/
    
    public static final String ESTADO_ACT="1";
    public static final String ESTADO_ANU="2";
    public static final String ESTADO_REC="3";
    /** ESTADO DOCUMENTOS **/
    
    public static final String ESTADO_CONDICION_ACT="ACTIVO";//CONDICION ACTIVO
    public static final String ESTADO_CONDICION_ANU="ANULADO";//CONDICION ANULADO
    public static final String ESTADO_CONDICION_REC="RECHAZADO";//CONDICION RECHAZADO
    
    /** OPCION DE ACCESO*/
    public static final String OPCION_RECAUDACION_CONSULTAS_01="01";
    public static final String OPCION_RECAUDACION_CONSULTAS_02="02";
    public static final String OPCION_RECAUDACION_CONSULTAS_03="03";
    public static final String OPCION_RECAUDACION_CONSULTAS_04="04";
    /** CONCEPTO DE RECONOCIMIENTO **/
    public static final String CONCEPTO_RECONOCIMIENTO_CANCELA ="01";
    public static final String CONCEPTO_RECONOCIMIENTO_A_CUENTA="02";
    /** LIMITE MAXIMO DE DIAS PARA ANULACION*/
    public static final int ANULACION_MAX_DIAS=5;
    /** TIPO DE INNCREMENTO VARIABLE **/
    public static final int TIPO_INCREMENTO_VARIABLE_PORCENTAJE = 1;
    public static final int TIPO_INCREMENTO_VARIABLE_MONTO = 2;
    public static final String TIPO_INCREMENTO_VARIABLE_1 ="PORCENTAJE VARIABLE";
    public static final String TIPO_INCREMENTO_VARIABLE_2 ="MONTO VARIABLE";
    
    /** TIPO DE REAJUSTE DE RENTA  */
    public static final String TIPO_REAJUSTE_RENTA_A="0";
    public static final String TIPO_REAJUSTE_RENTA_B="1";
    public static final String TIPO_REAJUSTE_RENTA_C="2";
    public static final String TIPO_REAJUSTE_RENTA_D="3";
    
    /** tIPO DE CONDICION DE ADENDA */
    public static final String TIPO_CONDICION_ADENDA_A="1";
    public static final String TIPO_CONDICION_ADENDA_B="2";
    
    /** TIPO DE INTERES EN MORA*/
    public static final String TIPO_TASA_INTERES_ACTIVA ="1";
    public static final String TIPO_TASA_INTERES_LEGAL  ="2";
    
    /** FORMA DE EJECUCION PERIODICA EN CLAUSULA*/
    public static final String FORMA_PERIODICA_01  = "Diaria";
    public static final String FORMA_PERIODICA_02  = "Mensual";
    
    /** TIPO DE CLAUSULA*/
    public static final String TIPO_CLAUSULA_INTERES_MORA = "01";
    public static final String TIPO_CLAUSULA_PENALIDAD    = "02";
    /** DETALLE CLAUSULA*/
    public static final String INTERES_ACTIVA_DIARIA_SBS  			= "01";
    public static final String INTERES_LEGAL_DIARIA_SBS  			= "02";
    public static final String PENALIDAD_DEMORA_PAGO_RENTA       	= "03";
    public static final String PENALIDAD_DEMORA_ENTREGA_INMUEBLE	= "04";
    public static final String PENALIDAD_RESOLVER_CONTRATO       	= "05";
    /** GENERAR CONSULTA PARA REPORTE*/
    public static final String CONDICION_CONSULTA_REPORTE_PENDIENTE         = "CRP";
    public static final String CONDICION_CONSULTA_REPORTE_LIQUIDACION_DEUDA = "CRLD";
    public static final String CONDICION_CONSULTA_REPORTE_PAGADOS        = "CRPA";
    
	/** tipo de conceptos SGI*/
	public final static Map<String,String> MAP_TIPO_CONCEPTO_SGI= new HashMap<String,String>(){
		private static final long serialVersionUID=1L;
		{
			put("01","Museo Taurino");    //Museo taurino
			put("02","Solicitud");        //Solicitud
			put("03","Pago Garantia");    //Pago de garantia
			put("04","Finca Rimac");      //Finca Rimac
			put("05","Otros ingresos");   //Otros Ingresos
			put("06","Renta");            //Renta
			put("07","Mora");             //Mora
			put("08","Rebaja de Renta");  //Rebaja de Renta
			put("09","Ninguno");          //Ninguno
			put("10","R.V. Acho");        //R.V. Acho
			put("11","P.P.A. Esp. Lib."); //P.P.A. Esp. Lib.
			put("12","C.F.Ejecutada");    //Carta Fianza Ejecutada
			put("13","Rebaja Mora");      //Rebaja Mora
			put("14","Anulación Doc.");   //Anulación Documento
			put("15","Rechazado");        //Rechazado
			put("16","Rec.Arbitrio");     //Recuperación de Arbitrios
			put("17","Penalización");     //Penalización
			put("18","Recon. Deuda");     //Reconocimiento de deuda 
		}
	};
	/** tipo de movimiento SGI*/
	public final static Map<String,String> MAP_TIPO_MOVIMIENTO_SGI= new HashMap<String,String>(){
		private static final long serialVersionUID=1L;
		{
			put("01","Cancelación Mensual");    			//Cancelación Mensual
			put("02","Pago a Cuenta");      	     		//Pago a Cuenta
			put("03","Pago Garantia");    					//Pago de Garantia
			put("04","Ninguno");      						//Ninguno
			put("05","Extincion de Deuda");  				//Extinción de Deuda
			put("06","Pago por Reconocimiento de Deuda");   //Pago por Reconocimiento de Deda

		}
	};
	public final static Map<String,String> MAP_CONCEPTO_RECONOCIMIENTO=new HashMap<String,String>(){
		private static final long SerialVersionUID=1L;
		{
			put("01","Cancela Renta");    			        //Cancelación 
			put("02","A Cuenta Renta");      	       		//Pago a Cuenta
		}
	};
	/** tipo de documento SGI*/
	public final static Map<String,String> MAP_TIPO_DOCUMENTO_SGI= new HashMap<String,String>(){
		private static final long serialVersionUID=1L;
		{
			put("00","SVL"); //Sin valor legal     
			put("01","F");	 //Factura             
			put("03","BV");	 //Boleta de Venta     
			put("04","RC");  //Recibo de Caja      
			put("08","ND");	 //Nota de Debito      
			put("09","NC");	 //Nota de Credito     
			put("DA","DA");  //Doc. administrativo 
			put("PD","PG");	 //Periodo de Gracia   
			
		}
	};
	/** tipo de pago SGI*/
	public final static Map<String,String> MAP_TIPO_PAGO_SGI= new HashMap<String,String>(){
		private static final long serialVersionUID=1L;
		{			   
			put("01","Pago en Efectivo ");	 	  //Pago en Efectivo             
			put("02","Pago en Cheque");	 		  //Pago en Cheque    
			put("03","Pago en Dólares"); 		  //Pago en Dólares      
			put("04","Ninguno");	 			  //Ninguno     
			put("05","Uso de Carta Fianza");	  //Uso de Carta Fianza     
			put("06","Uso de Garantia Liquida");  //Uso de Garantia Liquida 
			put("07","Extinción de Deuda");	 	  //Extincion de deuda  
			put("08","Uso de Resol. Coactiva");   //Uso de Resol. Coactiva
			
		}
	};
	/** concepto de comprobante*/
	public final static Map<String,String> MAP_COMP_CONCEPTO_SGI= new HashMap<String,String>(){
		private static final long serialVersionUID=1L;
		{
			put("0",""); //Sin valor     
			put("1","PAGO POR ALQUILER Y/O USO DE INMUEBLE");	             
			put("2","PAGO POR ALQUILER Y/O USO - ESPACIO LIBRE DEL PPA");	     
			put("3","PAGO POR RENTA VARIABLE");
			put("4","CONCEPTO DE GARANTIA");           
 
			
		}
	};
	/**Descripcion de comprobante */
	public final static Map<String,String> MAP_COM_DESCRIPCION_SGI=new HashMap<String,String>(){
	
		private static final long serialVersionUID = 1L;

		{   put("0","Cancelación Renta ");
			put("1","Cancelación Mora ");	             
			put("2","Cancelación Penalización ");
			put("3","Rebaja de Renta ");
			put("4","Rebaja de Mora ");
		}
	};
	  /** Usuarios especial*/
    public static final Map<String,String> USUARIO_ESPECIAL_SGI= new HashMap<String,String>(){
    	private static final long serialVersionUID=1L;
    	{
    		put("01","admin");
    		put("01","npomar");
    		put("01","hpeltroche");
    		put("02","jarenas");
    		put("03","jbenites");
    	}
    };
	  /** Usuarios especial*/
    public static final Map<String,String> USUARIO_OPCION_RECAUDACION_CONSULTAS_SGI= new HashMap<String,String>(){
    	private static final long serialVersionUID=1L;
    	{
    		put("admin","01");
    		put("npomar","01");
    		put("hpeltroche","01");
    		put("jarenas","02");
    		put("jbenites","03");
    		put("narce","01");
    	}
    };
     /** Tipo de condicion Adenda*/
    public static final Map<String,String> NOMBRE_TIPO_CONDICION_ADENDA_CONTRATO_SGI=  new HashMap<String,String>(){
    	 {
    		 put("1","Ampliar Plazo");
    		 put("2","Otra Condición");
    	 }
    };   
	
}
