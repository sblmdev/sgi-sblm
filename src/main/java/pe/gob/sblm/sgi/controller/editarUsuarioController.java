package pe.gob.sblm.sgi.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import pe.gob.sblm.api.commons.constants.sgi.Constantes;
import pe.gob.sblm.api.commons.utility.FuncionesHelper;
import pe.gob.sblm.sgi.entity.Perfil;
import pe.gob.sblm.sgi.entity.Perfilusuario;
import pe.gob.sblm.sgi.entity.Usuario;
import pe.gob.sblm.sgi.service.IEditarUsuarioService;
import pe.gob.sblm.sgi.util.CompDataModelPerfilUsuario;
import pe.gob.sblm.sgi.util.CompDataModelUsuario;


@ManagedBean(name = "ceditarusuarios")
@ViewScoped
public class editarUsuarioController implements Serializable {
	private static final long serialVersionUID = 5524190003746598593L;
	

	@ManagedProperty(value = "#{panelEditarUsuarioServiceImpl}")
	private transient IEditarUsuarioService panelEditarUsuarioServiceImpl;
	
	private String masterKey="default.jpg";
	private List<String> listPerfilSeleccionado;
	private String areaRegistro;
	private String emailRegistro;
	private String usuarioRegistro;
	private String apellidoRegistro;
	private String nombreRegistro; 
	private String passRegistro;
	private String passRepitRegistro;
	private String cargoRegistro;
	private String fechaCreacionUsuario;
	private Date fechaNacimientoUsuario;
	private String estadoRegistro;
	private int lastIdUsuario;
	private CompDataModelUsuario compDataModelUsuario;
	private CompDataModelPerfilUsuario compDataModelPerfilUsuario;
	private Usuario selectRegistroUsuario;
	private List<Usuario> listUsuarioInit;
	private List<Usuario> listUsuarioEdit;
	private List<Perfilusuario> listUsuarioEditConPerfil;
	private String selectIdRegistroUsuario="0";
	private List<Perfil>  listPerfiles;
	private UploadedFile file;
	private Boolean flagNewFile;
	


	public editarUsuarioController(){
		listPerfilSeleccionado = new ArrayList<String>();
		Date fechaHoy= new Date();	
		fechaCreacionUsuario = new SimpleDateFormat("dd-MM-yyyy").format(fechaHoy);

	}
	
	@PostConstruct
	public void initObjects(){
		
		setFlagNewFile(true);
		
		try {

			cargarDataEditarPaginaMaestra();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public void call(){
		
	}
	
	public void cargarDataEditarPaginaMaestra(){
		
		listUsuarioEdit = new ArrayList<Usuario>();
		listUsuarioEdit = panelEditarUsuarioServiceImpl.getUsuarioEditSinPerfil(FuncionesHelper.getUsuario().toString());
		
		System.out.println(listUsuarioEdit.get(0).getApellidopat() +" "+ listUsuarioEdit.get(0).getApellidomat());
		
			setApellidoRegistro(listUsuarioEdit.get(0).getApellidopat() +" "+ listUsuarioEdit.get(0).getApellidomat());
			setNombreRegistro(listUsuarioEdit.get(0).getNombres());
			setCargoRegistro(listUsuarioEdit.get(0).getCargo());
			setEmailRegistro(listUsuarioEdit.get(0).getEmailusr());
			setUsuarioRegistro(listUsuarioEdit.get(0).getNombreusr());
			setPassRegistro(listUsuarioEdit.get(0).getContrasenausr());
			setPassRepitRegistro(listUsuarioEdit.get(0).getContrasenausr());
			setMasterKey(listUsuarioEdit.get(0).getRutaimgusr());
			setFechaNacimientoUsuario(listUsuarioEdit.get(0).getFechanacimiento());
			
	}
	
	
	public void editarUsuarioPaginaMaestra(){

		String[] arrayNombreSimple = getApellidoRegistro().split(" ");
		String pat=arrayNombreSimple[0];
		String mat="";
		for (int i = 1; i < arrayNombreSimple.length; i++) {
			if(i==1){
				mat=mat+arrayNombreSimple[i];
			}else{
				mat=mat+" "+arrayNombreSimple[i];
			}
			}
		
		try {
			panelEditarUsuarioServiceImpl.actualizarUsuarioMaestro(getUsuarioRegistro(),getNombreRegistro(),pat, mat, getCargoRegistro(),getEmailRegistro(),getPassRegistro(),formatearFechaString((getFechaNacimientoUsuario())),getMasterKey());
		} catch (ParseException e) {
		
			e.printStackTrace();
		}
		
		
	}
	
	public String formatearFechaString(Date fecha) throws ParseException{
		
		String fechaString = new SimpleDateFormat("dd-MM-yyyy").format(fecha);
		
	
		return fechaString;
	}
	
	public Date formatearDate(String fecha) throws ParseException{
		
		Date date = new SimpleDateFormat("yyyy-MM-dd").parse(fecha);		
	
		return date;
	}
	
	
	
	
	public void limpiarFormulario(){
		setApellidoRegistro(null);
		setNombreRegistro(null);
		setCargoRegistro(null);
		setEmailRegistro(null);
		setUsuarioRegistro(null);
		setPassRegistro(null);
		setPassRepitRegistro(null);
		setFechaNacimientoUsuario(null);
		setEstadoRegistro("1");
		listPerfilSeleccionado.clear();
		selectIdRegistroUsuario="0";
		selectRegistroUsuario=null;
		masterKey="default.jpg";

	}
	
	
	
	public void uploadImagenUsuario(FileUploadEvent event) {
		
		
		setMasterKey(FuncionesHelper.getFileMasterKey()+"."+Constantes.EXTENSION_FORMATO_JPG);
		
		try {
			copyFile(getMasterKey(), event.getFile().getInputstream());
			
			if(getFlagNewFile()){
				 FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Exito",  "Se cargo imagen correctamente");  
			     FacesContext.getCurrentInstance().addMessage(null, message); 
			}else{
				setMasterKey("default.jpg");
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al subir imagen",  "Corrija error");  
			    FacesContext.getCurrentInstance().addMessage(null, message);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			
		}
		
		
	}
	
	
	public void copyFile(String fileName, InputStream in) {
		try {
			String curDir = System.getProperty("user.dir");
			fileName = curDir+"\\webapps\\sgi-sblm\\"+FuncionesHelper.IMAGENESUSUARIOSPATH;
			OutputStream out = new FileOutputStream(new File(fileName));
			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = in.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}

			in.close();
			out.flush();
			out.close();

			System.out.println("El nuevo fichero fue creado con exito!");
		} catch (IOException e) {
			System.out.println(e.getMessage());
			flagNewFile=false;
			
		}
	}
    public String mensajeGuardar(){  
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Exito",  "Se grabo con exito Usuario");  
          
        FacesContext.getCurrentInstance().addMessage(null, message);  
        return null;
    }
	
	public List<Usuario> getListUsuarioInit() {
		return listUsuarioInit;
	}


	public void setListUsuarioInit(List<Usuario> listUsuarioInit) {
		this.listUsuarioInit = listUsuarioInit;
	}


	public String getAreaRegistro() {
		return areaRegistro;
	}

	public void setAreaRegistro(String areaRegistro) {
		this.areaRegistro = areaRegistro;
	}

	public String getEmailRegistro() {
		return emailRegistro;
	}

	public void setEmailRegistro(String emailRegistro) {
		this.emailRegistro = emailRegistro;
	}

	public String getUsuarioRegistro() {
		return usuarioRegistro;
	}

	public void setUsuarioRegistro(String usuarioRegistro) {
		this.usuarioRegistro = usuarioRegistro;
	}

	public String getApellidoRegistro() {
		return apellidoRegistro;
	}

	public void setApellidoRegistro(String apellidoRegistro) {
		this.apellidoRegistro = apellidoRegistro;
	}

	public String getNombreRegistro() {
		return nombreRegistro;
	}

	public void setNombreRegistro(String nombreRegistro) {
		this.nombreRegistro = nombreRegistro;
	}

	public String getPassRegistro() {
		return passRegistro;
	}

	public void setPassRegistro(String passRegistro) {
		this.passRegistro = passRegistro;
	}

	public String getPassRepitRegistro() {
		return passRepitRegistro;
	}

	public void setPassRepitRegistro(String passRepitRegistro) {
		this.passRepitRegistro = passRepitRegistro;
	}

	public String getCargoRegistro() {
		return cargoRegistro;
	}

	public void setCargoRegistro(String cargoRegistro) {
		this.cargoRegistro = cargoRegistro;
	}

	

	public String getEstadoRegistro() {
		return estadoRegistro;
	}

	public void setEstadoRegistro(String estadoRegistro) {
		this.estadoRegistro = estadoRegistro;
	}




	public CompDataModelUsuario getCompDataModelUsuario() {
		return compDataModelUsuario;
	}


	public void setCompDataModelUsuario(CompDataModelUsuario compDataModelUsuario) {
		this.compDataModelUsuario = compDataModelUsuario;
	}


	public Usuario getSelectRegistroUsuario()
	{

		return selectRegistroUsuario;
	}

	public void setSelectRegistroUsuario(Usuario selectRegistroUsuario) {
		this.selectRegistroUsuario = selectRegistroUsuario;
	}

	public List<Usuario> getListUsuarioEdit() {
		return listUsuarioEdit;
	}

	public void setListUsuarioEdit(List<Usuario> listUsuarioEdit) {
		this.listUsuarioEdit = listUsuarioEdit;
	}

	public String getSelectIdRegistroUsuario() {
		return selectIdRegistroUsuario;
	}

	public void setSelectIdRegistroUsuario(String selectIdRegistroUsuario) {
		this.selectIdRegistroUsuario = selectIdRegistroUsuario;
	}

	

	public List<String> getListPerfilSeleccionado() {
		return listPerfilSeleccionado;
	}

	public void setListPerfilSeleccionado(List<String> listPerfilSeleccionado) {
		this.listPerfilSeleccionado = listPerfilSeleccionado;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<Perfil> getListPerfiles() {
		return listPerfiles;
	}

	public void setListPerfiles(List<Perfil> listPerfiles) {
		this.listPerfiles = listPerfiles;
	}

	public int getLastIdUsuario() {
		return lastIdUsuario;
	}

	public void setLastIdUsuario(int lastIdUsuario) {
		this.lastIdUsuario = lastIdUsuario;
	}

	public CompDataModelPerfilUsuario getCompDataModelPerfilUsuario() {
		return compDataModelPerfilUsuario;
	}

	public void setCompDataModelPerfilUsuario(
			CompDataModelPerfilUsuario compDataModelPerfilUsuario) {
		this.compDataModelPerfilUsuario = compDataModelPerfilUsuario;
	}



	public String getMasterKey() {
		return masterKey;
	}

	public void setMasterKey(String masterKey) {
		this.masterKey = masterKey;
	}

	public List<Perfilusuario> getListUsuarioEditConPerfil() {
		return listUsuarioEditConPerfil;
	}

	public void setListUsuarioEditConPerfil(
			List<Perfilusuario> listUsuarioEditConPerfil) {
		this.listUsuarioEditConPerfil = listUsuarioEditConPerfil;
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	public String getFechaCreacionUsuario() {
		return fechaCreacionUsuario;
	}

	public void setFechaCreacionUsuario(String fechaCreacionUsuario) {
		this.fechaCreacionUsuario = fechaCreacionUsuario;
	}

	public Date getFechaNacimientoUsuario() {
		return fechaNacimientoUsuario;
	}

	public void setFechaNacimientoUsuario(Date fechaNacimientoUsuario) {
		this.fechaNacimientoUsuario = fechaNacimientoUsuario;
	}

	public IEditarUsuarioService getPanelEditarUsuarioServiceImpl() {
		return panelEditarUsuarioServiceImpl;
	}

	public void setPanelEditarUsuarioServiceImpl(
			IEditarUsuarioService panelEditarUsuarioServiceImpl) {
		this.panelEditarUsuarioServiceImpl = panelEditarUsuarioServiceImpl;
	}

	public Boolean getFlagNewFile() {
		return flagNewFile;
	}

	public void setFlagNewFile(Boolean flagNewFile) {
		this.flagNewFile = flagNewFile;
	}

	
}
