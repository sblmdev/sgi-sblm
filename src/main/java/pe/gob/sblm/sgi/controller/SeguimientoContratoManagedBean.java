package pe.gob.sblm.sgi.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;

import org.primefaces.event.CellEditEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.StreamedContent;

import pe.gob.sblm.api.commons.utility.FuncionesHelper;
import pe.gob.sblm.sgi.entity.Archivo;
import pe.gob.sblm.sgi.entity.Area;
import pe.gob.sblm.sgi.entity.Contrato;
import pe.gob.sblm.sgi.entity.Detallesegcontrato;
import pe.gob.sblm.sgi.entity.Requisito;
import pe.gob.sblm.sgi.entity.Seguimientocontrato;
import pe.gob.sblm.sgi.entity.Tiporequisito;
import pe.gob.sblm.sgi.entity.Usuario;
import pe.gob.sblm.sgi.service.ISeguimientoContratoService;
import pe.gob.sblm.sgi.util.FHvariado;

@ManagedBean(name = "segContratoMB")
@ViewScoped
public class SeguimientoContratoManagedBean implements Serializable {
	private static final long serialVersionUID = 1L;
	@ManagedProperty(value = "#{seguimientocontratoService}")
	private transient ISeguimientoContratoService seguimientocontratoService;
	
	@ManagedProperty(value = "#{fHvariado}")
	private transient  FHvariado fHvariado;
	
	private Seguimientocontrato seguimientocontrato;
	
	private List<Seguimientocontrato> listaseguimientocontratos;
	
	private List<Contrato> listaContratos= new ArrayList<Contrato>();
	private Contrato contratoSeleccionado;
	private List<Contrato> contratos;

	
	private List<Tiporequisito> tiporequisitos;
	private Tiporequisito tiporequisito;
	private List<Requisito> listaRequisitos;
	private List<Requisito> listaRequisitosDisplay;
	private List<String> listaRequisitosDisplaySeleccionado;
	private List<Detallesegcontrato> listadetallesegcontrato;
	private List<Seguimientocontrato> seguimientocontratos;
	List<Detallesegcontrato> detalleContratoSeleccionado1= new ArrayList<Detallesegcontrato>();
	private HashMap<String, Boolean> mapTabs;
	private HashMap<String, Boolean> mapFinalizadoPaso;
	private HashMap<String, String> mapNombresPaso;
	private HashMap<String, Date> mapFechaPaso;
	
	private HashMap<String, String> mapUrlUsuarioPaso;
	
	private String ultimoInputConsultado;
	private Boolean ultimoValorPermiso;
	private String contenidoMensajePersonalizado;
	
	private List<Usuario> listadousuariosSeleccionados;
	private int indicesalvador;
	private Usuario selectRegistroUsuario;
	private int idUsuarioSeleccionado;
	private String destinatarios;
//	private List<Perfilusuario> perfilesusuario;
	private List<Seguimientocontrato> segcontrato= new ArrayList<Seguimientocontrato>();
	
	//File
	private Map<String, InputStream> carousel;
	private List<Archivo> displayList;
	private List<Archivo> displayListTmp;
	private Archivo archivoDelete;
	private StreamedContent filedownload;
	
	@ManagedProperty(value = "#{usuarioMB}")
	UsuarioManagedBean userMB;
	/*******agregar sa********/
	  
	    private Seguimientocontrato seguimientocap;


		public Seguimientocontrato getSeguimientocap() {
			return seguimientocap;
		}

		public void setSeguimientocap(Seguimientocontrato seguimientocap) {
			this.seguimientocap = seguimientocap;
		}


	public void obtenerDatosPaso(int numpaso,  int numcolum, int idcontrato){
		System.out.println("numpaso:::"+numpaso);
		System.out.println("numcolum:::"+numcolum);
		System.out.println("idcontrato:::"+idcontrato);
		System.out.println("Consulta>>>>>>>>>");
		//System.out.println("contrato>>>>>>>>>"+seguimientocap.getContrato().getIdcontrato());
		seguimientocap=	seguimientocontratoService.obtenerSeguimientoContratoParametros(idcontrato, numcolum);
		System.out.println("getUsercre>>>>>>>>>"+seguimientocap.getUsercre());
		System.out.println("getNumeropaso>>>>>>>>>"+seguimientocap.getNumeropaso());
		System.out.println("getIdsegcontrato>>>>>>>>>"+seguimientocap.getIdsegcontrato());
	}

	JasperPrint jasperPrint;
	private List<Seguimientocontrato> filtroReporteSeguimiento;
	@ManagedProperty(value = "#{tipocambioMB}")
	TipoCambioManagedBean tipocambioMB;
	
	public TipoCambioManagedBean getTipocambioMB() {
		return tipocambioMB;
	}

	public void setTipocambioMB(TipoCambioManagedBean tipocambioMB) {
		this.tipocambioMB = tipocambioMB;
	}

	public List<Seguimientocontrato> getfiltroReporteSeguimiento() {
		return filtroReporteSeguimiento;
	}

	public void setfiltroReporteSeguimiento(
			List<Seguimientocontrato> filtroReporteSeguimiento) {
		this.filtroReporteSeguimiento = filtroReporteSeguimiento;
	}

	public void initReporteSeguimiento() throws JRException {
		JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(
				filtroReporteSeguimiento);
		String reportPath = "C:\\reportesjasper\\reporteSeguimientoContrato.jasper";
		Map mapa = new HashMap();
		

		// PARAMETRO USUARIO CREADOR
		mapa.put("usuariocreador", userMB.getNombrecompleto());
		// PARAMETRO TIPO CAMBIO
		
		mapa.put("tipocambio", tipocambioMB.getTipocambioService()
				.obtenerUltimoTipocambio().getTipocambio());

		jasperPrint = JasperFillManager.fillReport(reportPath, mapa,
				beanCollectionDataSource);
	}

	public void  reporteSeguimientoExcel(ActionEvent actionEvent) throws JRException, IOException {
		initReporteSeguimiento();
		HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext
				.getCurrentInstance().getExternalContext().getResponse();

		Date ahora = new Date();
		SimpleDateFormat formateador = new SimpleDateFormat("dd_MM_yyyy");

		httpServletResponse.addHeader("Content-disposition",
				"attachment; filename=ReporteSeguimiento_" + formateador.format(ahora)
						+ ".xlsx");

		ServletOutputStream servletOutputStream = httpServletResponse
				.getOutputStream();
		JRXlsxExporter docxExporter = new JRXlsxExporter();
		docxExporter
				.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		docxExporter.setParameter(JRExporterParameter.OUTPUT_STREAM,
				servletOutputStream);
		docxExporter.exportReport();
		FacesContext.getCurrentInstance().responseComplete();
	}

	public void reporteSeguimientoPDF(ActionEvent actionEvent) throws JRException, IOException {

		initReporteSeguimiento();
		FacesContext context = FacesContext.getCurrentInstance();
		try {

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
			exporter.exportReport();

			byte[] bytes = baos.toByteArray();

			if (bytes != null && bytes.length > 0) {
				HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext
						.getCurrentInstance().getExternalContext()
						.getResponse();
				httpServletResponse.setContentType("application/pdf");

				Date ahora = new Date();
				SimpleDateFormat formateador = new SimpleDateFormat(
						"dd_MM_yyyy");

				httpServletResponse.addHeader(
						"Content-disposition",
						"attachment; filename=ReporteSeguimiento_"
								+ formateador.format(ahora) + ".pdf");

				ServletOutputStream servletOutputStream = httpServletResponse
						.getOutputStream();
				JasperExportManager.exportReportToPdfStream(jasperPrint,
						servletOutputStream);

				context.getApplication().getStateManager().saveView(context);

				context.responseComplete();
			} else {
				System.out.println("vacio");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}


	private List<Seguimientocontrato> filtroseguimientocontratos;
	
	public List<Seguimientocontrato> getFiltroseguimientocontratos() {
		return filtroseguimientocontratos;
	}

	public void setFiltroseguimientocontratos(
			List<Seguimientocontrato> filtroseguimientocontratos) {
		this.filtroseguimientocontratos = filtroseguimientocontratos;
	}

	/***************/
	@PostConstruct
	public void initObjects() {
		
		System.out.println("Post Constructor");
		
		initNew();
		inicializandoListas();
		inicializarCarousel();
			/*******agregar sa********/
				inicializarDatos();

				ActualizarMensajes();
			 /****************************/
		
	}

	public void initNew() {
		listadetallesegcontrato = new ArrayList<Detallesegcontrato>();
		listaseguimientocontratos= new ArrayList<Seguimientocontrato>();
		seguimientocontrato = new Seguimientocontrato();
		contratoSeleccionado= new Contrato();
		tiporequisito= new Tiporequisito();
		listaContratos= new ArrayList<Contrato>();
		listaRequisitos= new ArrayList<Requisito>();
		listaRequisitosDisplay= new ArrayList<Requisito>();
		listaContratos= new ArrayList<Contrato>();
		mapTabs = new HashMap<String, Boolean>();
		mapFinalizadoPaso= new HashMap<String, Boolean>();
		mapNombresPaso= new HashMap<String, String>();
		mapFechaPaso= new HashMap<String, Date>();
		mapUrlUsuarioPaso= new HashMap<String, String>();
		
//		perfilesusuario= new ArrayList<Perfilusuario>();
		
		mapTabs.put("1", true);
		
		listadousuariosSeleccionados= new ArrayList<Usuario>();

	}
	public void inicializandoListas(){
		
		listaContratos = getSeguimientocontratoService().listarContratos();
		
		listaRequisitos=getSeguimientocontratoService().obteneRequisitos();
		listaContratos=seguimientocontratoService.listarContratos();
		listaseguimientocontratos=seguimientocontratoService.listarSeguimientoContratos();
		listadetallesegcontrato= seguimientocontratoService.obtenerTodo();
		

		

	}
	
public void verficarAcceso(TabChangeEvent event){
	int idperfil = 0;
	
	
		setUltimoValorPermiso(false);
		setUltimoInputConsultado(event.getTab().getId());

		switch (Integer.parseInt(event.getTab().getId().substring(3))) {

		case 1:
			
			if (idperfil==129) {
				setUltimoValorPermiso(true);
			}break;
			
        case 2: 
        	
    		if (idperfil==131) {
    			setUltimoValorPermiso(true);
			}break;
			
        case 3:
        	
        	if (idperfil==129) {
        		setUltimoValorPermiso(true);
			} break;
			
        case 4:
        	
        	if (idperfil==129) {
        		setUltimoValorPermiso(true);
			}break;
			
        case 5:
        	
        	if (idperfil==129) {
        		setUltimoValorPermiso(true);
			}break;
			
        case 6: 
        	
        	if (idperfil==132) {
        		setUltimoValorPermiso(true);
			}break;	
			
        case 7:
        	
        	if (idperfil==51) {
        		setUltimoValorPermiso(true);
			}break;
			
        case 8:
        	
        	if (idperfil==129) {
        		setUltimoValorPermiso(true);
			}break;
			
        case 9:
        	
        	if (idperfil==129) {
        		setUltimoValorPermiso(true);
			}break;
			
        case 10:
        	
        	if (idperfil==129) {
        		setUltimoValorPermiso(true);
			}break;
			
        case 11:
        	
        	if (idperfil==133) {
        		setUltimoValorPermiso(true);
			}break;
			
        case 12:  
        	
        	if (idperfil==122) {
        		setUltimoValorPermiso(true);
			}break;
			
        case 13:  
       	 	 
        	if (idperfil==132) {
        		setUltimoValorPermiso(true);
			}break;
			
        case 14: 
        	
        	if (idperfil==51) {
        		setUltimoValorPermiso(true);
			}break;
			
        case 15:  
        	
        	if (idperfil==129) {
        		setUltimoValorPermiso(true);
			}break;
			
        case 16:  
        	
        	if (idperfil==51) {
        		setUltimoValorPermiso(true);
			}break;
			
        case 17:  
        	
        	if (idperfil==51) {
        		setUltimoValorPermiso(true);
			}break;  
			
        default:
           break;}
	}
	

	public void diplayRequisitos() {
		listaRequisitosDisplay.clear();
		
		for (Requisito requisito: listaRequisitos) {
			if (requisito.getTiporequisito().getIdtiporequisito()==tiporequisito.getIdtiporequisito()) {
				listaRequisitosDisplay.add(requisito);
			}
		}
		
	}
	
	public void listaTab(){
		mapTabs.clear();
		
		for (Seguimientocontrato seguimientocontrato : listaseguimientocontratos) {
			if (seguimientocontrato.getContrato().getIdcontrato()==contratoSeleccionado.getIdcontrato()) {
					mapTabs.put(String.valueOf(seguimientocontrato.getNumeropaso()), false);
			} else {

			}
		}
		
	}
	
	public void registroSeguimientoContrato1() {
			
			if (pasoactualcontrato()==0) {//es 0 Nuevo
				System.out.println("nuevo");
				
				seguimientocontrato.setContrato(contratoSeleccionado);
				seguimientocontrato.setFeccre(new Date());
				seguimientocontrato.setSiactivopaso(siseleccionotodo());
				seguimientocontrato.setNumeropaso(1);
				seguimientocontrato.setUsercre((fHvariado.buscarDatosUsuarioOrigen(Integer.parseInt(FuncionesHelper.getUsuario().toString()))).getNombrescompletos());
				seguimientocontrato.setUsrruta((fHvariado.buscarDatosUsuarioOrigen(Integer.parseInt(FuncionesHelper.getUsuario().toString()))).getRutaimgusr());
				seguimientocontrato.setFeccre(new Date());
				getSeguimientocontratoService().registrarSeguimientoContrato(seguimientocontrato);
				
				for (Requisito requisitodisplay : listaRequisitosDisplay) {
					Boolean siencontrado=false;
					for (String item : listaRequisitosDisplaySeleccionado) {
						if (requisitodisplay.getIdrequisito()==Integer.parseInt(item)) {
							
							Detallesegcontrato detallesegcontrato= new Detallesegcontrato();
							detallesegcontrato.setSeguimientocontrato(seguimientocontrato);
							detallesegcontrato.setActivado(true);
							detallesegcontrato.setRequisito(requisitodisplay);
							getSeguimientocontratoService().registrarDetalleSeguimientoContrato(detallesegcontrato);
							siencontrado=true;
																						}
						}
					
					if (!siencontrado) {
						Detallesegcontrato detallesegcontrato= new Detallesegcontrato();
						detallesegcontrato.setSeguimientocontrato(seguimientocontrato);
						detallesegcontrato.setActivado(false);
						detallesegcontrato.setRequisito(requisitodisplay);
						getSeguimientocontratoService().registrarDetalleSeguimientoContrato(detallesegcontrato);
					}
				}
				
				
				mapNombresPaso.put("1", (fHvariado.buscarDatosUsuarioOrigen(Integer.parseInt(FuncionesHelper.getUsuario().toString()))).getNombrescompletos());
				mapFechaPaso.put("1", new Date());
				mapUrlUsuarioPaso.put("1", (fHvariado.buscarDatosUsuarioOrigen(Integer.parseInt(FuncionesHelper.getUsuario().toString()))).getRutaimgusr());
				
			} else {

				getSeguimientocontratoService().eliminardetalleSeguimientoContrato(seguimientocontrato.getIdsegcontrato());
				
				
				for (Requisito requisitodisplay : listaRequisitosDisplay) {
					Boolean siencontrado=false;
					for (String item : listaRequisitosDisplaySeleccionado) {
						if (requisitodisplay.getIdrequisito()==Integer.parseInt(item)) {
							
							Detallesegcontrato detallesegcontrato= new Detallesegcontrato();
							detallesegcontrato.setSeguimientocontrato(seguimientocontrato);
							detallesegcontrato.setActivado(true);
							detallesegcontrato.setRequisito(requisitodisplay);
							getSeguimientocontratoService().registrarDetalleSeguimientoContrato(detallesegcontrato);
							siencontrado=true;
																						}
						}
					
					if (!siencontrado) {
						Detallesegcontrato detallesegcontrato= new Detallesegcontrato();
						detallesegcontrato.setSeguimientocontrato(seguimientocontrato);
						detallesegcontrato.setActivado(false);
						detallesegcontrato.setRequisito(requisitodisplay);
						getSeguimientocontratoService().registrarDetalleSeguimientoContrato(detallesegcontrato);
					}
						
				}
			}
				//Creamos siguiente
				if (siseleccionotodo()) {
					
				Seguimientocontrato nuevoSeguimientocontrato= new Seguimientocontrato();
				nuevoSeguimientocontrato.setContrato(contratoSeleccionado);
				nuevoSeguimientocontrato.setFeccre(new Date());
				nuevoSeguimientocontrato.setSiactivopaso(false);
				nuevoSeguimientocontrato.setNumeropaso(2);
				getSeguimientocontratoService().registrarSeguimientoContrato(nuevoSeguimientocontrato);
				mapTabs.put("2", true);
				mapFinalizadoPaso.put("2", false);
				
					
				}
	}
	
	
	private int pasoactualcontrato(){
		int pasoactual=seguimientocontratoService.pasoactualcontrato(contratoSeleccionado.getIdcontrato());
		return 	pasoactual;
	}
	
	public void obtenerSeguimientoContrato1(){
		detalleContratoSeleccionado1= new ArrayList<Detallesegcontrato>();
		listaRequisitosDisplaySeleccionado= new ArrayList<String>();
		
		
		if (contratoSeleccionado!=null) {
			
			detalleContratoSeleccionado1=getSeguimientocontratoService().obtenerDetalleSeguimientoContrato(contratoSeleccionado.getIdcontrato());
			seguimientocontrato=detalleContratoSeleccionado1.get(0).getSeguimientocontrato();//Consulta por contrato Correjir
			
			for (Detallesegcontrato detallesegcontrato : detalleContratoSeleccionado1) { 
				if (detallesegcontrato.getActivado()==true) {
					
					listaRequisitosDisplaySeleccionado.add(String.valueOf(detallesegcontrato.getRequisito().getIdrequisito()));
				}
		}


		mapNombresPaso.put("1", seguimientocontrato.getUsercre());
		mapFechaPaso.put("1", seguimientocontrato.getFeccre());
		mapUrlUsuarioPaso.put("1", seguimientocontrato.getUsrruta());
		}
		
		tiporequisito.setIdtiporequisito(detalleContratoSeleccionado1.get(0).getRequisito().getTiporequisito().getIdtiporequisito());
		diplayRequisitos() ;

	}
	
	
	
	private void obtenerSeguimientoContratoOtro(int pasoactualcontrato) {
	
		
		for (int i = 2; i <= pasoactualcontrato; i++) {
			mapFinalizadoPaso.put(String.valueOf(i), getSeguimientocontratoService().obtenerValorSiFinalizado(i,contratoSeleccionado.getIdcontrato()));
			if (i==3) {
				
				
			}
		}
		

		
		for (int i = 2; i <= pasoactualcontrato; i++) {
			for (Seguimientocontrato segcont : segcontrato) {
				if (i==segcont.getNumeropaso()) {
					if (i==3) {
						destinatarios=segcont.getDestinatarios();
					}

					mapNombresPaso.put(String.valueOf(i), segcont.getUsercre());
					mapFechaPaso.put(String.valueOf(i), segcont.getFeccre());
					mapUrlUsuarioPaso.put(String.valueOf(i), segcont.getUsrruta());
					
				
				}
				
			}
			
		}
		
		
		
		if (pasoactualcontrato>4) {
			
			System.out.println("Entro metodo");
			displayList= new ArrayList<Archivo>();
			
			displayListTmp= new ArrayList<Archivo>();
			
			for (Archivo A : displayList) {
				displayListTmp.add(A);
			}
			
			if(displayList.size()==0){
			}
			
		}
	}
	
	public void registroSeguimientoContratoPasoOtro(){
		
		System.out.println("ANTES DE GUARDAR:"+mapFinalizadoPaso.get("3"));
		
		int x=pasoactualcontrato();
		String nombresusr=(fHvariado.buscarDatosUsuarioOrigen(Integer.parseInt(FuncionesHelper.getUsuario().toString()))).getNombrescompletos();
		String rutaimagenusr=(fHvariado.buscarDatosUsuarioOrigen(Integer.parseInt(FuncionesHelper.getUsuario().toString()))).getRutaimgusr();
		
		if (x==5 || x==9) {
			
		}
		
		if ((Boolean) mapFinalizadoPaso.get(String.valueOf(x))) {
			getSeguimientocontratoService().actualizarPaso(x,nombresusr,rutaimagenusr);
			
			mapNombresPaso.put(String.valueOf(x),nombresusr );
			mapFechaPaso.put(String.valueOf(x), new Date());
			mapUrlUsuarioPaso.put(String.valueOf(x), rutaimagenusr);
			
			
			if (x!=17) {
				Seguimientocontrato nuevoSeguimientocontrato= new Seguimientocontrato();
				nuevoSeguimientocontrato.setContrato(contratoSeleccionado);
				nuevoSeguimientocontrato.setFeccre(new Date());
				nuevoSeguimientocontrato.setSiactivopaso(false);
				nuevoSeguimientocontrato.setNumeropaso(x+1);
				getSeguimientocontratoService().registrarSeguimientoContrato(nuevoSeguimientocontrato);
				
				mapTabs.put(String.valueOf(x+1), true);
			}
		}
		System.out.println("DESPUES DE GUARDAR:"+mapFinalizadoPaso.get("3"));
	}
	
	public void clearHashMap(int pasoactualcontrato){
		mapTabs.clear();

			for (int i = 1; i <= pasoactualcontrato; i++) {
				if (i==1) {
					mapTabs.put("1", false);
				}else {
					mapTabs.put(String.valueOf(i), true);
				}
			}
	}


	
	public void detectarUbicacionActual(){
		
		
		
		segcontrato=getSeguimientocontratoService().obtenerSeguimientoContrato(contratoSeleccionado.getIdcontrato());
		
		
		int pasoactualcontrato=pasoactualcontrato();
		
		
		 switch (pasoactualcontrato) { 
         case 1:
        	 clearHashMap(pasoactualcontrato);
        	 obtenerSeguimientoContrato1();
              break;
         case 2: obtenerSegunNumerodePaso(pasoactualcontrato);
        	  break;
         case 3: obtenerSegunNumerodePaso(pasoactualcontrato);
        	 break;
         case 4: obtenerSegunNumerodePaso(pasoactualcontrato);
        	 break;
         case 5: obtenerSegunNumerodePaso(pasoactualcontrato);
                  break;
         case 6: obtenerSegunNumerodePaso(pasoactualcontrato);
                  break;
         case 7: obtenerSegunNumerodePaso(pasoactualcontrato);
                  break;
         case 8: obtenerSegunNumerodePaso(pasoactualcontrato);  
                  break;
         case 9:  obtenerSegunNumerodePaso(pasoactualcontrato);  
                  break;
         case 10: obtenerSegunNumerodePaso(pasoactualcontrato); 
                  break;
         case 11:  obtenerSegunNumerodePaso(pasoactualcontrato);
                  break;
         case 12:  obtenerSegunNumerodePaso(pasoactualcontrato);
                  break;
         case 13:  obtenerSegunNumerodePaso(pasoactualcontrato);
        	 	  break;
         case 14:  obtenerSegunNumerodePaso(pasoactualcontrato);
             	  break;
         case 15:  obtenerSegunNumerodePaso(pasoactualcontrato);
         		  break;
         case 16:  obtenerSegunNumerodePaso(pasoactualcontrato);
         		  break;
         case 17:  obtenerSegunNumerodePaso(pasoactualcontrato);
             	  break;   	
         default:
                	 
        	 mapTabs.clear();
        	 tiporequisito.setIdtiporequisito(0);
        	 listaRequisitosDisplay.clear();
        	 mapTabs.put("1", false);
        	 mapFechaPaso.clear();
        	 mapFinalizadoPaso.clear();
        	 mapUrlUsuarioPaso.clear();
        	 mapNombresPaso.clear();
            break;
     }
	}
	
	public void obtenerSegunNumerodePaso(int pasoactualcontrato){
		 clearHashMap(pasoactualcontrato);
	   	 obtenerSeguimientoContrato1();
	   	 obtenerSeguimientoContratoOtro(pasoactualcontrato);	
	}

	public Boolean sifinalizadopaso1(){
		
		siseleccionotodo();
		
		return siseleccionotodo();
	}
	
	public Boolean siseleccionotodo(){
	  	
		if (listaRequisitosDisplaySeleccionado.size()==listaRequisitosDisplay.size()) {
				return true;
		} 
		
		return false;
	}
	
	public void enviarNotificacionPersonalizable(){
		destinatarios="";
		
		for (int i = 0; i < listadousuariosSeleccionados.size(); i++) {
			if ((i+1)==listadousuariosSeleccionados.size()) {
				destinatarios= destinatarios+listadousuariosSeleccionados.get(i).getEmailusr()+".";
			}else {
				destinatarios=destinatarios+listadousuariosSeleccionados.get(i).getEmailusr()+", ";	
			}
			
		}
		
		System.out.println("DESTINATARIOS:"+destinatarios);
		

		
		seguimientocontratoService.actualizarPaso3YNotificar(contratoSeleccionado.getIdcontrato(),3,getContenidoMensajePersonalizado(),listadousuariosSeleccionados,destinatarios);	
		

		
	}
	

	
	public void messageFacesPrederminado(String titulo, String mensaje, Severity severityFatal){
		FacesContext contextFaces = FacesContext.getCurrentInstance(); 
		contextFaces.addMessage(null, new FacesMessage(severityFatal,titulo,mensaje));
	}
	
	public void eliminarArchivodeLista() {
        displayList.remove(getArchivoDelete());
    }
	
	public void inicializarCarousel(){
		
		displayList= new ArrayList<Archivo>();
		carousel = new HashMap<String, InputStream>();
		displayListTmp= new ArrayList<Archivo>();
	}
	
		public void inicializarDatos() {
		
		seguimientocontrato = new Seguimientocontrato();
		seguimientocontrato.setContrato(new Contrato());
		
		tiporequisito = new Tiporequisito();
		
		contratos = getSeguimientocontratoService().listarContratos();
		tiporequisitos = getSeguimientocontratoService().listarTiporequisitos();
		/*******agregar sa********/
		seguimientocontratos=getSeguimientocontratoService().listarSeguimientoContratos();
		for (Seguimientocontrato seg : seguimientocontratos) {
			System.out.println("seguimiento contrato INQUI:::"+seg.getContrato().getInquilino().getNombrescompletos());
		}
		
		filtroReporteSeguimiento=getSeguimientocontratoService().listarSeguimientoContratos();
		/****************/
	}
	
	public  void escribirArchivo(String fileName, InputStream in){
		
		try {
//			System.out.println("entroxdireccion:::"+FuncionesHelper.directorioPrincipalLibreria+fileName);
			OutputStream out = new FileOutputStream(new File("filename"));
			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = in.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}

			in.close();
			out.flush();
			out.close();

			
		} catch (IOException e) {
			System.out.println("errorxx:::"+e.getMessage());
		}
	}

	/****************/
	


	public void agregarUsuarioLista(){
		boolean usuarioVacio = false;

		if (!usuarioVacio) {
		}
			Usuario Usu = new Usuario();
			Usu.setEmailusr("Escriba Nombre Usuario");
			Usu.setRutaimgusr("default.jpg");
			Usu.setContrasenausr("deleteUsuario.png");
			listadousuariosSeleccionados.add(Usu);
			setIndicesalvador(listadousuariosSeleccionados.size()-1);
		}
	

	
	public void onCellEdit(CellEditEvent event) { 
		
		
		if(Integer.parseInt(event.getColumn().getWidth())==99){
	        Object newValue = event.getNewValue();
	        
	        
	        FacesContext contextFaces = FacesContext.getCurrentInstance();  

	       
	       //buscamos id usuario
	       int id = 0;
	       for(int k=0;k<fHvariado.getTodosUsuarios().size();k++){	
				if((fHvariado.getTodosUsuarios().get(k).getNombrescompletos()).equals(newValue)){
					id=k;
				}

			}

			boolean flag = true;
		

			for (int i = 0; i < listadousuariosSeleccionados.size() - 1; i++) {
				if ((listadousuariosSeleccionados.get(i).getEmailusr())
						.equals(newValue)) { 
					
					contextFaces.addMessage(null, new FacesMessage(
							FacesMessage.SEVERITY_WARN, "Advertencia",
							"usuario ya se encuentra en la lista"));
					listadousuariosSeleccionados.remove(getIndicesalvador());
					flag = false;

				}
			}

			if (flag) {
				System.out.println("nuevo");
				listadousuariosSeleccionados.get(getIndicesalvador()).setRutaimgusr(fHvariado.getTodosUsuarios().get(id).getRutaimgusr());
				listadousuariosSeleccionados.get(getIndicesalvador()).setCargo(fHvariado.getTodosUsuarios().get(id).getCargo());
				listadousuariosSeleccionados.get(getIndicesalvador()).setIdusuario(fHvariado.getTodosUsuarios().get(id).getIdusuario());
				listadousuariosSeleccionados.get(getIndicesalvador()).setUsrmod("Ingrese Perfil");
				
				Area A= new Area();
				A.setDesare(fHvariado.getTodosUsuarios().get(id).getArea().getDesare());
				listadousuariosSeleccionados.get(getIndicesalvador()).setArea(A);
				setIdUsuarioSeleccionado(fHvariado.getTodosUsuarios().get(id).getIdusuario());
				
//				buscarPerfilUsuario();
			}
			
		}
//			else{
//	        Object newValue = event.getNewValue();
//			
//			listadousuariosSeleccionados.get(getIndicesalvador()).setUsrmod(newValue.toString());
//			
//		}

		
    }
	
//	public void buscarPerfilUsuario(){
//		perfilesusuario.clear();
//		perfilesusuario=getSeguimientocontratoService().getPerfilesUsuario(getIdUsuarioSeleccionado());
//	}	
	
	public void eliminarUsuarioDeLista(ActionEvent event) {
		
		for(int i=0;i<listadousuariosSeleccionados.size();i++){
			
			if(listadousuariosSeleccionados.get(i)==getSelectRegistroUsuario()){
				listadousuariosSeleccionados.remove(i);
			}
		}

	}

	public ISeguimientoContratoService getSeguimientocontratoService() {
		return seguimientocontratoService;
	}
	public void setSeguimientocontratoService(ISeguimientoContratoService seguimientocontratoService) {
		this.seguimientocontratoService = seguimientocontratoService;
	}

	public void ActualizarMensajes() {
		seguimientocontratos = getSeguimientocontratoService()
				.listarSeguimientoContratos();
	}
	public Seguimientocontrato getSeguimientocontrato() {
		return seguimientocontrato;
	}
	public void setSeguimientocontrato(Seguimientocontrato seguimientocontrato) {
		this.seguimientocontrato = seguimientocontrato;
	}
	public List<Contrato> getListaContratos() {
		return listaContratos;
	}
	public void setListaContratos(List<Contrato> listaContratos) {
		this.listaContratos = listaContratos;
	}
	public Tiporequisito getTiporequisito() {
		return tiporequisito;
	}
	public void setTiporequisito(Tiporequisito tiporequisito) {
		this.tiporequisito = tiporequisito;
	}
	public List<Requisito> getListaRequisitos() {
		return listaRequisitos;
	}
	public void setListaRequisitos(List<Requisito> listaRequisitos) {
		this.listaRequisitos = listaRequisitos;
	}
	public List<Requisito> getListaRequisitosDisplay() {
		return listaRequisitosDisplay;
	}
	public void setListaRequisitosDisplay(List<Requisito> listaRequisitosDisplay) {
		this.listaRequisitosDisplay = listaRequisitosDisplay;
	}
	public List<String> getListaRequisitosDisplaySeleccionado() {
		return listaRequisitosDisplaySeleccionado;
	}
	public void setListaRequisitosDisplaySeleccionado(
			List<String> listaRequisitosDisplaySeleccionado) {
		this.listaRequisitosDisplaySeleccionado = listaRequisitosDisplaySeleccionado;
	}
	public Contrato getContratoSeleccionado() {
		return contratoSeleccionado;
	}
	public void setContratoSeleccionado(Contrato contratoSeleccionado) {
		this.contratoSeleccionado = contratoSeleccionado;
	}
	public HashMap<String, Boolean> getMapTabs() {
		return mapTabs;
	}
	public void setMapTabs(HashMap<String, Boolean> mapTabs) {
		this.mapTabs = mapTabs;
	}
	public HashMap<String, Boolean> getMapFinalizadoPaso() {
		return mapFinalizadoPaso;
	}
	public void setMapFinalizadoPaso(HashMap<String, Boolean> mapFinalizadoPaso) {
		this.mapFinalizadoPaso = mapFinalizadoPaso;
	}
	public HashMap<String, String> getMapNombresPaso() {
		return mapNombresPaso;
	}
	public void setMapNombresPaso(HashMap<String, String> mapNombresPaso) {
		this.mapNombresPaso = mapNombresPaso;
	}
	public HashMap<String, Date> getMapFechaPaso() {
		return mapFechaPaso;
	}
	public void setMapFechaPaso(HashMap<String, Date> mapFechaPaso) {
		this.mapFechaPaso = mapFechaPaso;
	}
	public Boolean getUltimoValorPermiso() {
		return ultimoValorPermiso;
	}
	public void setUltimoValorPermiso(Boolean ultimoValorPermiso) {
		this.ultimoValorPermiso = ultimoValorPermiso;
	}
	public String getUltimoInputConsultado() {
		return ultimoInputConsultado;
	}
	public void setUltimoInputConsultado(String ultimoInputConsultado) {
		this.ultimoInputConsultado = ultimoInputConsultado;
	}
	public String getContenidoMensajePersonalizado() {
		return contenidoMensajePersonalizado;
	}
	public void setContenidoMensajePersonalizado(String contenidoMensajePersonalizado) {
		this.contenidoMensajePersonalizado = contenidoMensajePersonalizado;
	}
	public List<Usuario> getListadousuariosSeleccionados() {
		return listadousuariosSeleccionados;
	}
	public void setListadousuariosSeleccionados(
			List<Usuario> listadousuariosSeleccionados) {
		this.listadousuariosSeleccionados = listadousuariosSeleccionados;
	}
	public int getIndicesalvador() {
		return indicesalvador;
	}
	public void setIndicesalvador(int indicesalvador) {
		this.indicesalvador = indicesalvador;
	}
	public Usuario getSelectRegistroUsuario() {
		return selectRegistroUsuario;
	}
	public void setSelectRegistroUsuario(Usuario selectRegistroUsuario) {
		this.selectRegistroUsuario = selectRegistroUsuario;
	}
	public int getIdUsuarioSeleccionado() {
		return idUsuarioSeleccionado;
	}
	public void setIdUsuarioSeleccionado(int idUsuarioSeleccionado) {
		this.idUsuarioSeleccionado = idUsuarioSeleccionado;
	}
//	public List<Perfilusuario> getPerfilesusuario() {
//		return perfilesusuario;
//	}
//	public void setPerfilesusuario(List<Perfilusuario> perfilesusuario) {
//		this.perfilesusuario = perfilesusuario;
//	}
	public FHvariado getfHvariado() {
		return fHvariado;
	}
	public void setfHvariado(FHvariado fHvariado) {
		this.fHvariado = fHvariado;
	}
	public Map<String, InputStream> getCarousel() {
		return carousel;
	}
	public void setCarousel(Map<String, InputStream> carousel) {
		this.carousel = carousel;
	}
	public List<Archivo> getDisplayList() {
		return displayList;
	}
	public void setDisplayList(List<Archivo> displayList) {
		this.displayList = displayList;
	}
	public List<Archivo> getDisplayListTmp() {
		return displayListTmp;
	}
	public void setDisplayListTmp(List<Archivo> displayListTmp) {
		this.displayListTmp = displayListTmp;
	}
	public Archivo getArchivoDelete() {
		return archivoDelete;
	}
	public void setArchivoDelete(Archivo archivoDelete) {
		this.archivoDelete = archivoDelete;
	}
	public StreamedContent getFiledownload() {
		return filedownload;
	}
	public void setFiledownload(StreamedContent filedownload) {
		this.filedownload = filedownload;
	}

	public UsuarioManagedBean getUserMB() {
		return userMB;
	}

	public void setUserMB(UsuarioManagedBean userMB) {
		this.userMB = userMB;
	}

	public HashMap<String, String> getMapUrlUsuarioPaso() {
		return mapUrlUsuarioPaso;
	}

	public void setMapUrlUsuarioPaso(HashMap<String, String> mapUrlUsuarioPaso) {
		this.mapUrlUsuarioPaso = mapUrlUsuarioPaso;
	}

	public String getDestinatarios() {
		return destinatarios;
	}

	public void setDestinatarios(String destinatarios) {
		this.destinatarios = destinatarios;
	}

	public List<Contrato> getContratos() {
		return contratos;
	}

	public void setContratos(List<Contrato> contratos) {
		this.contratos = contratos;
	}

	public List<Tiporequisito> getTiporequisitos() {
		return tiporequisitos;
	}

	public void setTiporequisitos(List<Tiporequisito> tiporequisitos) {
		this.tiporequisitos = tiporequisitos;
	}

	public List<Detallesegcontrato> getListadetallesegcontrato() {
		return listadetallesegcontrato;
	}

	public void setListadetallesegcontrato(
			List<Detallesegcontrato> listadetallesegcontrato) {
		this.listadetallesegcontrato = listadetallesegcontrato;
	}

	public List<Seguimientocontrato> getSeguimientocontratos() {
		return seguimientocontratos;
	}

	public void setSeguimientocontratos(
			List<Seguimientocontrato> seguimientocontratos) {
		this.seguimientocontratos = seguimientocontratos;
	}



}
