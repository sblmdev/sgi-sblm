package pe.gob.sblm.sgi.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import pe.gob.sblm.sgi.bean.UpaBean;
import pe.gob.sblm.sgi.entity.Cartera;
import pe.gob.sblm.sgi.entity.Inquilino;
import pe.gob.sblm.sgi.entity.Perfil;
import pe.gob.sblm.sgi.entity.Usuario;
import pe.gob.sblm.sgi.service.IFHvariadoService;
@ManagedBean(name = "fHvariado")
@ViewScoped
public  class FHvariado implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{FHvariadoService}")
	private transient  IFHvariadoService FHvariadoService;

	private List<UpaBean> listaupa=new ArrayList<UpaBean>();
	private List<Inquilino> listainquilino=new ArrayList<Inquilino>();
	private List<Perfil> listaPerfiles= new ArrayList<Perfil>();
	private List<Usuario> todosUsuarios= new ArrayList<Usuario>();
	private List<Usuario> listaCobradores= new ArrayList<Usuario>();
	private Usuario cobrador;
	private List<Cartera> listaCarteras= new ArrayList<Cartera>();

	
	
	public  int retornarIdPerfilPorNombre(String nombrePerfil){
		
		 listaPerfiles= FHvariadoService.obtenerPerfiles();

		 for (Perfil perfil : listaPerfiles) {
			
			if (perfil.getNombreperfil().equals(nombrePerfil)) {
				return perfil.getIdperfil();
			}
			
		}
		
		return 0;
	}

	
	
	public IFHvariadoService getFHvariadoService() {
		return FHvariadoService;
	}

	public void setFHvariadoService(IFHvariadoService fHvariadoService) {
		FHvariadoService = fHvariadoService;
	}
	
	
	public List<Usuario> autoCompleteCobradores(String query){
		listaCobradores=FHvariadoService.obtenerCobradores();
		
		return listaCobradores;
		
		}

	
	public List<String> autoCompleteUpa(String query){
		
		List<String> result = new ArrayList<String>();
//		listaupa=FHvariadoService.listaUpa();
		for(UpaBean upa : (List<UpaBean>)listaupa){
			String claveupa=upa.getClave();
			if(claveupa.toLowerCase().contains(query.toLowerCase())){
				result.add(claveupa);
			}
		}
		
		return result;
	}
	
	public List<String> autoCompleteInquilino(String query){
		
		List<String> result = new ArrayList<String>();
//		listainquilino=FHvariadoService.listaInquilino();
		for(Inquilino inq : (List<Inquilino>)listainquilino){
			String nombrescompletos=inq.getNombrescompletos();
			if(nombrescompletos.toLowerCase().contains(query.toLowerCase())){
				result.add(nombrescompletos);
			}
		}
		
		return result;
	}
	
	public List<String> autoCompleteUsuario(String query){
		todosUsuarios=FHvariadoService.obtenerUsuarios();
		List<String> result = new ArrayList<String>();
		 
		
		for(Usuario usu : (List<Usuario>)todosUsuarios){
			
			String nomComplusu=usu.getNombrescompletos();
			
			if(nomComplusu.toLowerCase().contains(query.toLowerCase())){
				result.add(nomComplusu);
			}
		}
		
		return result;
		
		}
	
	
	public Usuario buscarDatosUsuarioOrigen(int id){
			
			for (Usuario usuario : todosUsuarios) {
				if(usuario.getIdusuario()==id) {
					return usuario;
				}
			}
			
			return null;
		}
	
	
	public String obtenerCorreoXid(int id){
		String correo = "";
		
		
		for (Usuario usr : todosUsuarios) {
			if (usr.getIdusuario()==id) {
				correo=usr.getEmailusr();
			}
		}

		return correo;
	}

	public List<Usuario> getTodosUsuarios() {
		return todosUsuarios;
	}

	public void setTodosUsuarios(List<Usuario> todosUsuarios) {
		this.todosUsuarios = todosUsuarios;
	}
	
	public List<Inquilino> getListainquilino() {
		return listainquilino;
	}
	public void setListainquilino(List<Inquilino> listainquilino) {
		this.listainquilino = listainquilino;
	}
	
	public List<UpaBean> getListaupa() {
		return listaupa;
	}

	public void setListaupa(List<UpaBean> listaupa) {
		this.listaupa = listaupa;
	}



	public Usuario getCobrador() {
		return cobrador;
	}
	public void setCobrador(Usuario cobrador) {
		this.cobrador = cobrador;
	}
}
