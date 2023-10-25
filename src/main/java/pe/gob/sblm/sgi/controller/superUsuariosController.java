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
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import pe.gob.sblm.api.commons.utility.FuncionesHelper;
import pe.gob.sblm.sgi.entity.Area;
import pe.gob.sblm.sgi.entity.Perfil;
import pe.gob.sblm.sgi.entity.Perfilusuario;
import pe.gob.sblm.sgi.entity.Usuario;
import pe.gob.sblm.sgi.service.ISuperUsuarioService;
import pe.gob.sblm.sgi.util.CompDataModelPerfilUsuario;
import pe.gob.sblm.sgi.util.CompDataModelUsuario;


@ManagedBean(name = "csuperusuarios")
@ViewScoped
public class superUsuariosController implements Serializable {
	private static final long serialVersionUID = 5524190003746598593L;
	

	@ManagedProperty(value = "#{panelSuperUsuarioServiceImpl}")
	private transient ISuperUsuarioService panelSuperUsuarioServiceImpl;
	
	private String rutaImagenUsuario="default.jpg";
	private String destination;
	private List<String> listPerfilSeleccionado;
	private String areaRegistro;
	private String emailRegistro;
	private String usuarioRegistro;
	private String apellidoRegistro;
	private String nombreRegistro; 
//	private String passRegistro;
//	private String passRepitRegistro;
	private String cargoRegistro;
	private String fechaCreacionUsuario;
	private Date fechaNacimientoUsuario;
	private String estadoRegistro;
	private int lastIdUsuario;
	private CompDataModelUsuario compDataModelUsuario;
	private CompDataModelPerfilUsuario compDataModelPerfilUsuario;
	private Usuario selectRegistroUsuario;
	private List<Usuario> listUsuarioInit;
	private List<Perfilusuario> listaPerfilUsuarioSeleccionado;
	private String selectIdRegistroUsuario="0";
	private List<Perfil>  listPerfiles;
	private UploadedFile file;
	private Boolean flagNewFile;
	private List<Area> listaarea;
	
	private String idarea;
	Random rnd = new Random();
	

	public superUsuariosController(){
		
		listPerfilSeleccionado = new ArrayList<String>();
		String curDir = System.getProperty("user.dir");
		
		destination = curDir+"\\webapps\\sistemaSBLM\\resources\\images\\usuarios\\";

		Date fechaHoy= new Date();	
		fechaCreacionUsuario = new SimpleDateFormat("dd-MM-yyyy").format(fechaHoy);
		System.out.println(FuncionesHelper.getUsuario().toString());
		System.out.println(FuncionesHelper.getTerminal().toString());
		System.out.println(FuncionesHelper.getURL().toString());
		
	}
	
	@PostConstruct
	public void initObjects(){
		setFlagNewFile(true);
		listUsuarioInit = new ArrayList<Usuario>();
		listPerfiles = panelSuperUsuarioServiceImpl.listPerfilbyNom();
		listaarea= new ArrayList<Area>();
		listaarea=panelSuperUsuarioServiceImpl.getAllArea();
		
		try {
			listUsuarioInit = panelSuperUsuarioServiceImpl.listarUsuarios();
			compDataModelUsuario = new CompDataModelUsuario(listUsuarioInit);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void registro(){
		
	}
	

	public void seleccionIdRegistroUsuario(){
		
		setSelectIdRegistroUsuario(((Integer.toString(selectRegistroUsuario.getIdusuario()))));
		System.out.println("Id "+getSelectIdRegistroUsuario());
	}
	
	// Actualizar o Grabar nuevo
	public void registroUsuario(javax.faces.event.ActionEvent Event) throws ParseException{
		
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
		
		int contador=0;
		for(int i=0;i<listUsuarioInit.size();i++){
			try {
				if(listUsuarioInit.get(i).getNombres().equals(getNombreRegistro())&&listUsuarioInit.get(i).getApellidopat().equals(pat)&&listUsuarioInit.get(i).getApellidomat().equals(mat)){
					
					contador++;
				}
				
			} catch (Exception e) {
				System.out.println("Excepcion manejada exitosamente");
			}
			
		}
		
		int usuarioencontrado=0;
		for(int i=0;i<listUsuarioInit.size();i++){
			try {
				if(listUsuarioInit.get(i).getNombreusr().equals(getUsuarioRegistro())){
					System.out.println("USUARIO HA SIDO ENCONTRADO");
					usuarioencontrado++;
				}
				
			} catch (Exception e) {
			}
			
		}
		

		if(selectIdRegistroUsuario.equals("0") ){
			
			
			if(contador==0 && usuarioencontrado==0){
				
				String contrasenia=(pat+rnd.nextInt(10000)).toLowerCase();
				Usuario usr=new Usuario();
				usr.setApellidopat(pat);
				usr.setApellidomat(mat);
				usr.setNombreusr(getUsuarioRegistro());
				usr.setNombres(getNombreRegistro());
				usr.setCargo(getCargoRegistro());
				usr.setEmailusr(getEmailRegistro());
				usr.setContrasenausr(contrasenia);
				usr.setFechanacimiento((getFechaNacimientoUsuario()));
				usr.setNombrescompletos(getNombreRegistro()+" "+pat+" "+mat);
				
				Area area = new Area();
				area.setIdare(idarea);
				
				usr.setArea(area);
				 
				if(getEstadoRegistro().equals("1")){
					usr.setEstado(true);
				}else{
					usr.setEstado(false);
				}
					
				usr.setFeccre(new Date());
				usr.setRutaimgusr(getRutaImagenUsuario());

				
				try {
					System.out.println("Entrando a nuevo usuario");
					panelSuperUsuarioServiceImpl.nuevoUsuario(usr);

				} catch (Exception e) {
					e.printStackTrace();
					}
				
				lastIdUsuario=(Integer) panelSuperUsuarioServiceImpl.getLastIdUsuario();
			
				for(int i=0;i<listPerfilSeleccionado.size();i++)
				{
					System.out.println(listPerfilSeleccionado.get(i));				 
				}
				
							for (int i=0;i<listPerfilSeleccionado.size();i++) {
					
								
								Usuario user= new Usuario();
								user.setIdusuario(getLastIdUsuario());
								
								Perfil perfil= new Perfil();
								perfil.setIdperfil(Integer.parseInt(listPerfilSeleccionado.get(i)));
								
								
								
								Perfilusuario perfilusuario=new Perfilusuario();
					
								perfilusuario.setPerfil(perfil);
								perfilusuario.setUsuario(user);
								perfilusuario.setFeccre(new Date());
								perfilusuario.setFecmod(new Date());
								
								if(i==0){
								perfilusuario.setActivo(true);
								}else{
								perfilusuario.setActivo(false);
								}
								
								perfilusuario.setUsrcre("super");
								
								panelSuperUsuarioServiceImpl.save(perfilusuario);
								
							}
							 FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Grabado Exitoso",  "Se grabo con exito Usuario");  
						     FacesContext.getCurrentInstance().addMessage(null, message);  
				
			}
			
			else{
				
				if(usuarioencontrado>0){
					 FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Grabado sin Exito",  "Nombre de usuario existente en el sistema ");  
				     FacesContext.getCurrentInstance().addMessage(null, message); 
					
				}else{
					 FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Grabado sin Exito",  "Nombres de usuario existente en el sistema ");  
				     FacesContext.getCurrentInstance().addMessage(null, message);  
				}

			}
			
		
		
		}
			else {

				System.out.println("Existente");
				

				
					selectRegistroUsuario.setApellidopat(pat);
					selectRegistroUsuario.setApellidomat(mat);
					selectRegistroUsuario.setNombreusr(getUsuarioRegistro());
					selectRegistroUsuario.setNombres(getNombreRegistro());
					selectRegistroUsuario.setCargo(getCargoRegistro());
					selectRegistroUsuario.setEmailusr(getEmailRegistro());
				
					
					Area area= new Area();
					area.setIdare(getIdarea());

					selectRegistroUsuario.setArea(area);
					
					
					
					if(getEstadoRegistro().equals("1")){
						selectRegistroUsuario.setEstado(true);
					}else{
						selectRegistroUsuario.setEstado(false);
					}
		
					selectRegistroUsuario.setFeccre(new Date());
					selectRegistroUsuario.setRutaimgusr(getRutaImagenUsuario());
			
					try {
						panelSuperUsuarioServiceImpl.actualizarUsuario(selectRegistroUsuario,formatearFechaString((getFechaNacimientoUsuario())));
						panelSuperUsuarioServiceImpl.eliminarAntiguoPerfilUsuario(selectRegistroUsuario.getIdusuario());
						
						for (int i=0;i<listPerfilSeleccionado.size();i++) {
				
							
							Usuario user= new Usuario();
							user.setIdusuario(selectRegistroUsuario.getIdusuario());
							
							Perfil perfil= new Perfil();
							perfil.setIdperfil(Integer.parseInt(listPerfilSeleccionado.get(i)));
							
							
							
							Perfilusuario perfilusuario=new Perfilusuario();
				
							perfilusuario.setPerfil(perfil);
							perfilusuario.setUsuario(user);
							perfilusuario.setFeccre(new Date());
							perfilusuario.setFecmod(new Date());
								if(i==0){
									perfilusuario.setActivo(true);
								}else{
									perfilusuario.setActivo(false);
								}
							perfilusuario.setUsrcre("super");
							
							panelSuperUsuarioServiceImpl.save(perfilusuario);
						}
						
						
					} catch (Exception e) {
						e.printStackTrace();
						}
					
					 FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Edición Exitosa",  "Se edito con exito Usuario");  
				     FacesContext.getCurrentInstance().addMessage(null, message);  
		}
				
		
		/*Actualizar grilla*/
		actualizarGrilla();
		limpiarFormulario();
	}

	//Solo pinta el formulario de usuario a editar
	public void editarRegistroUsuario(){
		Usuario usuario= new Usuario();
		usuario=panelSuperUsuarioServiceImpl.getUsuarioaEditar(selectIdRegistroUsuario);
			listaPerfilUsuarioSeleccionado = panelSuperUsuarioServiceImpl.getPerfilesUsuario(getSelectIdRegistroUsuario());
			listPerfilSeleccionado.clear();
			
			setApellidoRegistro(usuario.getApellidopat() +" "+ usuario.getApellidomat());
			setNombreRegistro(usuario.getNombres());
			setCargoRegistro(usuario.getCargo());
			setEmailRegistro(usuario.getEmailusr());
			setUsuarioRegistro(usuario.getNombreusr());
			setRutaImagenUsuario(usuario.getRutaimgusr());
			setFechaNacimientoUsuario(usuario.getFechanacimiento());
//			setIdarea(usuario.getArea().getIdare());
			
				if(usuario.getEstado()){
					setEstadoRegistro("1");
				}else{
					setEstadoRegistro("0");
				}
			
			if(listaPerfilUsuarioSeleccionado.size()==0){
				System.out.println("El usuario no esta asociado a ningun perfil");
			}else {
				System.out.println("El usuario esta asociado a uno o mas perfiles");
					for(int i=0;i<listaPerfilUsuarioSeleccionado.size();i++){
						listPerfilSeleccionado.add(String.valueOf(listaPerfilUsuarioSeleccionado.get(i).getPerfil().getIdperfil()));
					}
				}
		actualizarGrilla();
	}

	
	public String formatearFechaString(Date fecha) throws ParseException{
		
		String fechaString = new SimpleDateFormat("dd-MM-yyyy").format(fecha);
		
	
		return fechaString;
	}
	
	public Date formatearDate(String fecha) throws ParseException{
		
		Date date = new SimpleDateFormat("yyyy-MM-dd").parse(fecha);		
	
		return date;
	}
	
	
	public void eliminarRegistroUsuario(){
		
		try {
			panelSuperUsuarioServiceImpl.eliminarUsuario(getSelectIdRegistroUsuario());
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,"Exito","Se elimino el usuario correctamente!!!"); 
			FacesContext.getCurrentInstance().addMessage(null, msg);


		} catch (Exception e) {
				e.printStackTrace();
}
		
		
		actualizarGrilla();
		limpiarFormulario();
		
	}
	
	public void actualizarGrilla(){
		listUsuarioInit = new ArrayList<Usuario>();

		
		try {
			listUsuarioInit = panelSuperUsuarioServiceImpl.listarUsuarios();

			
			compDataModelUsuario = new CompDataModelUsuario(listUsuarioInit);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void limpiarFormulario(){
		
		setApellidoRegistro(null);
		setNombreRegistro(null);
		setCargoRegistro(null);
		setEmailRegistro(null);
		setUsuarioRegistro(null);
//		setPassRegistro(null);
//		setPassRepitRegistro(null);
		setIdarea("0");
		setFechaNacimientoUsuario(null);
		setEstadoRegistro("1");
		listPerfilSeleccionado.clear();
		selectIdRegistroUsuario="0";
		selectRegistroUsuario=null;
		rutaImagenUsuario="default.jpg";
		

	}
	
	
	
	public void uploadImagenUsuario(FileUploadEvent event) {
		
		System.out.println("intro upload method");
		
		try {
			copyFile(event.getFile().getFileName(), event.getFile()
					.getInputstream());
			
			if(getFlagNewFile()){
				setRutaImagenUsuario(event.getFile().getFileName());
				
				 FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Exito",  "Se cargo imagen correctamente");  
			     FacesContext.getCurrentInstance().addMessage(null, message); 
			     
			}else{
 
				setRutaImagenUsuario("default.jpg");
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al subir imagen",  "Corrija error");  
			     FacesContext.getCurrentInstance().addMessage(null, message);
			}
			
			
			System.out.println(getRutaImagenUsuario());
			
			
			
		} catch (IOException e) {
			e.printStackTrace();
			
		}
		
		
	}
	
	
	public void copyFile(String fileName, InputStream in) {
		try {

			OutputStream out = new FileOutputStream(new File(destination
					+ fileName));

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
    	System.out.println("intro method");
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

//	public String getPassRegistro() {
//		return passRegistro;
//	}
//
//	public void setPassRegistro(String passRegistro) {
//		this.passRegistro = passRegistro;
//	}
//
//	public String getPassRepitRegistro() {
//		return passRepitRegistro;
//	}
//
//	public void setPassRepitRegistro(String passRepitRegistro) {
//		this.passRepitRegistro = passRepitRegistro;
//	}

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

	public ISuperUsuarioService getPanelSuperUsuarioServiceImpl() {
		return panelSuperUsuarioServiceImpl;
	}


	public void setPanelSuperUsuarioServiceImpl(
			ISuperUsuarioService panelSuperUsuarioServiceImpl) {
		this.panelSuperUsuarioServiceImpl = panelSuperUsuarioServiceImpl;
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

	public String getRutaImagenUsuario() {
		return rutaImagenUsuario;
	}

	public void setRutaImagenUsuario(String rutaImagenUsuario) {
		this.rutaImagenUsuario = rutaImagenUsuario;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public List<Perfilusuario> getListaPerfilUsuarioSeleccionado() {
		return listaPerfilUsuarioSeleccionado;
	}

	public void setListaPerfilUsuarioSeleccionado(
			List<Perfilusuario> listaPerfilUsuarioSeleccionado) {
		this.listaPerfilUsuarioSeleccionado = listaPerfilUsuarioSeleccionado;
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

	public Boolean getFlagNewFile() {
		return flagNewFile;
	}

	public void setFlagNewFile(Boolean flagNewFile) {
		this.flagNewFile = flagNewFile;
	}

	public List<Area> getListaarea() {
		return listaarea;
	}

	public void setListaarea(List<Area> listaarea) {
		this.listaarea = listaarea;
	}

	public String getIdarea() {
		return idarea;
	}

	public void setIdarea(String idarea) {
		this.idarea = idarea;
	}

}
