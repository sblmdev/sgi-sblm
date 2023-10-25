package pe.gob.sblm.sgi.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import pe.gob.sblm.api.commons.utility.FuncionesHelper;
import pe.gob.sblm.sgi.entity.Archivo;
import pe.gob.sblm.sgi.entity.Carta;
import pe.gob.sblm.sgi.entity.Contrato;
import pe.gob.sblm.sgi.entity.Usuario;
import pe.gob.sblm.sgi.service.IAutovaluoArbitrioService;
import pe.gob.sblm.sgi.service.IAutovaluoCartaService;

@ManagedBean(name = "recaudacionautovaluocartaMB")
@ViewScoped
public class RecaudacionAutovaluoCartaManagedBean implements Serializable {
	private static final long serialVersionUID = 5524190003746598593L;

	@ManagedProperty(value = "#{recaudacionautovaluocartaService}")
	private transient IAutovaluoCartaService recaudacionautovaluocartaService;

	@ManagedProperty(value = "#{recaudacionautovaluoarbitrioService}")
	private transient IAutovaluoArbitrioService recaudacionautovaluoarbitrioService;

	private Contrato contratoSeleccionado;
	
	String jravcalle;
	private Carta carta;
	private List<Usuario> listaTodosUsuarios;
	private Usuario usuarioCreador;

	private List<Carta> listaCartaConsultaTOP;
	private List<Carta> listaCartaConsulta;
	private List<Carta> listafiltroCartaConsulta;

	private int idcartaGlobal = 0;

	/****** cargado de archivos ****/
	Map<String, InputStream> carousel;
	private List<Archivo> displayList;

	private List<Archivo> displayListTmp;
	private Archivo archivoDelete;
	private StreamedContent filedownload;

	List<String> fileList;
	private static final String OUTPUT_ZIP_FILE= "documentos.zip";
	private static final String SOURCE_FOLDER="";



	@ManagedProperty(value = "#{usuarioMB}")
	UsuarioManagedBean usuarioMB;

	@PostConstruct
	public void init() {

		carta = new Carta();
		listarCartaConsulta();

		listaTodosUsuarios = new ArrayList<Usuario>();
		listaTodosUsuarios = usuarioMB.getUsuariosdgi();
	}


	public void grabarCabeceraCarta() {

		// GRABAR ACTUALIZAR
		if (carta.getIdcarta() == 0) {
			carta.setIdusuariocreador((Integer) FuncionesHelper.getUsuario());
			carta.setFechacreacion(new Date());
			carta.setContrato(contratoSeleccionado);
			
			carta.setNroarchivos(carousel.size());
			recaudacionautovaluocartaService.grabarCabeceraCarta(carta);

			System.out.println("Nro de Archivos :" + carta.getNroarchivos());

			messageFacesPrederminado("Éxito",
					"Se agregó una nueva carta para el inquilino:"
							+ contratoSeleccionado.getInquilino().getNombrescompletos(),
					FacesMessage.SEVERITY_INFO);

			listarCartaConsulta();

			carta = recaudacionautovaluocartaService.getUltimaCabeceraGrabada();

			carta = null;
			contratoSeleccionado=null;
		}else {
			carta.setIdusuariocreador((Integer) FuncionesHelper.getUsuario());
			carta.setFechacreacion(new Date());

			
			carta.setNroarchivos(carousel.size());
			
			try {
				recaudacionautovaluocartaService.actualizarCabeceraCarta(carta);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			
			messageFacesPrederminado("Éxito", "Se actualizó el Carta para el inquilino:"+contratoSeleccionado.getInquilino().getNombrescompletos(), FacesMessage.SEVERITY_INFO);
			contratoSeleccionado=null;
			carta=null;
			listarCartaConsulta();
			
		}
	}

	public void obtenerCarta(int idcarta) {
		if (idcarta != 0) {

			for (int i = 0; i < listaCartaConsulta.size(); i++) {
				if (idcarta == listaCartaConsulta.get(i).getIdcarta()) {

					carta = listaCartaConsulta.get(i);
					contratoSeleccionado=listaCartaConsulta.get(i).getContrato();
					
					buscandoUsuarioCreador(listaCartaConsulta.get(i)
							.getIdusuariocreador());
				}
			}
			setIdcartaGlobal(carta.getIdcarta());
		}

	}


	public void zipiarArchivoCarta(int idcarta) {
		fileList = new ArrayList<String>();

		if (idcarta != 0) {

			List<Archivo> lista = new ArrayList<Archivo>();

			for (int i = 0; i < lista.size(); i++) {
//				generateFileList(new File(FuncionesHelper.directorioPrincipalLibreria+ lista.get(i).getRuta()));
			}

			zipIt(OUTPUT_ZIP_FILE);

		}
		descargarArchivo(OUTPUT_ZIP_FILE);

	}

	public void listarCartaConsulta() {
		listaCartaConsulta = new ArrayList<Carta>();
		listaCartaConsulta = recaudacionautovaluocartaService.listarArbitrioConsulta();
		


		listaCartaConsultaTOP = new ArrayList<Carta>();
		if (listaCartaConsulta.size() > 4) {
			listaCartaConsultaTOP = listaCartaConsulta.subList(0, 4);
		} else {
			listaCartaConsultaTOP = listaCartaConsulta;
		}

	}



	public void eliminarArchivodeLista() {
		displayList.remove(getArchivoDelete());
	}

	

	public void buscandoUsuarioCreador(int idUsuarioCreador) {
		usuarioCreador = new Usuario();
		for (int i = 0; i < listaTodosUsuarios.size(); i++) {
			if (listaTodosUsuarios.get(i).getIdusuario() == idUsuarioCreador) {

				usuarioCreador.setApellidopat(listaTodosUsuarios.get(i)
						.getApellidopat());
				usuarioCreador.setNombres(listaTodosUsuarios.get(i)
						.getNombres());
				usuarioCreador.setRutaimgusr(listaTodosUsuarios.get(i)
						.getRutaimgusr());
			}
		}

	}

	public void messageFacesPrederminado(String titulo, String mensaje,
			Severity severityFatal) {
		FacesContext contextFaces = FacesContext.getCurrentInstance();
		contextFaces.addMessage(null, new FacesMessage(severityFatal, titulo,
				mensaje));
	}

	public IAutovaluoCartaService getRecaudacionautovaluocartaService() {
		return recaudacionautovaluocartaService;
	}

	public void setRecaudacionautovaluocartaService(
			IAutovaluoCartaService recaudacionautovaluocartaService) {
		this.recaudacionautovaluocartaService = recaudacionautovaluocartaService;
	}

	public Carta getCarta() {
		return carta;
	}

	public void setCarta(Carta carta) {
		this.carta = carta;
	}


	public List<Usuario> getListaTodosUsuarios() {
		return listaTodosUsuarios;
	}

	public void setListaTodosUsuarios(List<Usuario> listaTodosUsuarios) {
		this.listaTodosUsuarios = listaTodosUsuarios;
	}

	public IAutovaluoArbitrioService getRecaudacionautovaluoarbitrioService() {
		return recaudacionautovaluoarbitrioService;
	}

	public void setRecaudacionautovaluoarbitrioService(
			IAutovaluoArbitrioService recaudacionautovaluoarbitrioService) {
		this.recaudacionautovaluoarbitrioService = recaudacionautovaluoarbitrioService;
	}

	public Usuario getUsuarioCreador() {
		return usuarioCreador;
	}

	public void setUsuarioCreador(Usuario usuarioCreador) {
		this.usuarioCreador = usuarioCreador;
	}

	public UsuarioManagedBean getUsuarioMB() {
		return usuarioMB;
	}

	public void setUsuarioMB(UsuarioManagedBean usuarioMB) {
		this.usuarioMB = usuarioMB;
	}

	public List<Carta> getListaCartaConsulta() {
		return listaCartaConsulta;
	}

	public void setListaCartaConsulta(List<Carta> listaCartaConsulta) {
		this.listaCartaConsulta = listaCartaConsulta;
	}

	public List<Carta> getListafiltroCartaConsulta() {
		return listafiltroCartaConsulta;
	}

	public void setListafiltroCartaConsulta(List<Carta> listafiltroCartaConsulta) {
		this.listafiltroCartaConsulta = listafiltroCartaConsulta;
	}

	public int getIdcartaGlobal() {
		return idcartaGlobal;
	}

	public void setIdcartaGlobal(int idcartaGlobal) {
		this.idcartaGlobal = idcartaGlobal;
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



	public List<Carta> getListaCartaConsultaTOP() {
		return listaCartaConsultaTOP;
	}

	public void setListaCartaConsultaTOP(List<Carta> listaCartaConsultaTOP) {
		this.listaCartaConsultaTOP = listaCartaConsultaTOP;
	}
	
	

	public Contrato getContratoSeleccionado() {
		return contratoSeleccionado;
	}


	public void setContratoSeleccionado(Contrato contratoSeleccionado) {
		this.contratoSeleccionado = contratoSeleccionado;
	}


	/**
	 * Zip it
	 * 
	 * @param zipFile
	 *            output ZIP file location
	 */
	public void zipIt(String zipFile) {

		byte[] buffer = new byte[1024];

		try {

			FileOutputStream fos = new FileOutputStream(zipFile);
			ZipOutputStream zos = new ZipOutputStream(fos);

			System.out.println("Output to Zip : " + zipFile);

			for (String file : this.fileList) {

				System.out.println("File Added : " + file);
				ZipEntry ze = new ZipEntry(file);
				zos.putNextEntry(ze);

				FileInputStream in = new FileInputStream(SOURCE_FOLDER
						+ File.separator + file);

				int len;
				while ((len = in.read(buffer)) > 0) {
					zos.write(buffer, 0, len);
				}

				in.close();
			}

			zos.closeEntry();
			// remember close it
			zos.close();

			System.out.println("Done");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Traverse a directory and get all files, and add the file into fileList
	 * 
	 * @param node
	 *            file or directory
	 */
	public void generateFileList(File node) {

		// add file only
		if (node.isFile()) {
			fileList.add(generateZipEntry(node.getAbsoluteFile().toString()));
		}

		if (node.isDirectory()) {
			String[] subNote = node.list();
			for (String filename : subNote) {
				generateFileList(new File(node, filename));
			}
		}

	}

	/**
	 * Format the file path for zip
	 * 
	 * @param file
	 *            file path
	 * @return Formatted file path
	 */
	private String generateZipEntry(String file) {
		return file.substring(SOURCE_FOLDER.length(), file.length());
	}

	public void descargarArchivo(String archivoCapturado) {

		System.out.println(":::::::::...rutax::" + archivoCapturado);
		String curDir = System.getProperty("user.dir");
		String direccion = curDir
				+ "\\webapps\\sistemaSBLM\\resources\\documents\\"
				+ archivoCapturado;

		// String direccion= "C:\\archivosxxx\\"+archivoCapturado.getRuta();

		direccion = archivoCapturado;

		String extension = "";
		int i = direccion.lastIndexOf('.');
		if (i > 0) {
			extension = direccion.substring(i + 1);
		}

		System.out.println(":::::::::...extension::" + extension);
		System.out.println(":::::::::...direccion::" + direccion);
		InputStream stream;
		try {
			stream = new FileInputStream(direccion);
			filedownload = new DefaultStreamedContent(stream, "application/"
					+ extension + "", archivoCapturado);
		} catch (FileNotFoundException e) {

			System.out.println("ERROR:" + e.getMessage());
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN,
					"Advertencia", new String(
							"No existe el archivo en el servidor"));

			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}

	public StreamedContent getFiledownload() {
		return filedownload;
	}

	public void setFiledownload(StreamedContent filedownload) {
		this.filedownload = filedownload;
	}

	public String getJravcalle() {
		return jravcalle;
	}

	public void setJravcalle(String jravcalle) {
		this.jravcalle = jravcalle;
	}
	

}