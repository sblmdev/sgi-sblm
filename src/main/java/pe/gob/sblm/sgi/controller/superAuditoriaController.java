package pe.gob.sblm.sgi.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.SelectEvent;

import pe.gob.sblm.sgi.entity.Auditoria;
import pe.gob.sblm.sgi.entity.Modulo;
import pe.gob.sblm.sgi.entity.Pagina;
import pe.gob.sblm.sgi.entity.Perfilusuario;
import pe.gob.sblm.sgi.entity.Usuario;
import pe.gob.sblm.sgi.service.IAuditoriaService;
import pe.gob.sblm.sgi.util.Almanaque;
import pe.gob.sblm.sgi.util.CompDataModel;


@ManagedBean(name = "cauditoria")
@ViewScoped
public class superAuditoriaController implements Serializable {
	private static final long serialVersionUID = 5524190003746598593L;

	@ManagedProperty(value = "#{panelAuditoriaServiceImpl}")
	private transient IAuditoriaService panelAuditoriaServiceImpl;
 
	
	private String codigo;
	private String nombreUsuario;
	private String nombrePerfil;
	private String ip;
	private String horaEntrada;
	private String nombrePantalla;
	private String mensaje;
	private String modulo;
	private String url;
	private boolean estado;
	private List<Auditoria> listAuditoriaInit;
	private Auditoria selectRegistroAudi;
	private CompDataModel compDataModel;
	private List<Usuario> listUsuarios;
	private List<Perfilusuario>  listPerfiles;
	private List<Modulo>  listModulo;
	private List<Pagina>  listRecurso;
//	private List<Usuario> listUserTop;
	private Usuario listUsuarioTop;
	private String usrBusqueda;
	private String fechaCaducidad;
	private String perfilBusqueda;
	private String moduloBusqueda;
	private String recursoBusqueda;
	private String nroConectadosHoy;
	private String mesActual;
	private List<Auditoria> listaAuditoriaFiltro;
	private String txtnombreUsuario;
	private String txtcargoUsuario;
	private String rutaimgusrTop;
	private Date fechaInicio;
	private Date fechaFin;
	private String nomPantalla;
	private String nomPerfil;
	private String nomModulo;
	private String tipoBusqueda;
	
	
	


	public superAuditoriaController(){
		fechaInicio = new Date();
		fechaFin = new Date();
		Almanaque almanaque= new Almanaque();
		mesActual=almanaque.obtenerMes(new Date());
		nombreUsuario="";
		nomPantalla=""; 
		nomPerfil="";
		nomModulo="";
	}
	
	@PostConstruct
	public void initObjects(){
		listAuditoriaInit = new ArrayList<Auditoria>();
		listUsuarioTop = new Usuario();
		
		
		
		try {
			listAuditoriaInit = panelAuditoriaServiceImpl.listarAuditoria();
			
			for(int i=0;i<listAuditoriaInit.size();i++){
				try {
					String queryString=listAuditoriaInit.get(i).getUrl();
					int index = queryString.indexOf("pages/");
					queryString=queryString.substring(index+6, queryString.length());
					listAuditoriaInit.get(i).setUrl(queryString);
				} catch (Exception e) {
				}
			
			}
			
			
			compDataModel = new CompDataModel(listAuditoriaInit);
			
			
			setNroConectadosHoy(panelAuditoriaServiceImpl.nroConectadosDelDia());
			

			
			listPerfiles = panelAuditoriaServiceImpl.listPerfilbyNom();
			listModulo = panelAuditoriaServiceImpl.listModulobyNom();
			listRecurso = panelAuditoriaServiceImpl.listRecursobyNom();
			listUsuarioTop =panelAuditoriaServiceImpl.listUsuarioTop();
			//listUserTop=panelAuditoriaServiceImpl.listUserTop((String) listUsuarioTop.get(0));
			
			
			setTxtnombreUsuario(listUsuarioTop.getNombres()+" "+listUsuarioTop.getApellidopat());
			setRutaimgusrTop(listUsuarioTop.getRutaimgusr());
			setTxtcargoUsuario(listUsuarioTop.getCargo());
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		
		}

	}
	
	public void consultarUltimoModuloVisitado(){
		
	 //getUltimoModulo(String.valueOf(panelAuditoriaServiceImpl.ultimoModuloVisitado().toString()));
		
	}
	
	public List<String> autoCompleteUsuario(String query){
		
		List<String> result = new ArrayList<String>();
		listUsuarios = panelAuditoriaServiceImpl.listUsuariobyNom();
		 
		
		for(Usuario usu : (List<Usuario>)listUsuarios){
			
			String nomComplusu=usu.getNombres()+" "+usu.getApellidopat()+" "+usu.getApellidomat();
			
			if(nomComplusu.toLowerCase().contains(query.toLowerCase())){
				result.add(nomComplusu);
			}
		}
		
		return result;
		
		}

	
	public void listAuditoria(){
		listaAuditoriaFiltro = new ArrayList<Auditoria>();
		
		if(tipoBusqueda.equals("1")){
			
			try {
				listaAuditoriaFiltro = panelAuditoriaServiceImpl.listAuditoriaFiltro(fechaInicio,fechaFin,nombreUsuario,recursoBusqueda,nomPerfil,moduloBusqueda);
				for(int i=0;i<listAuditoriaInit.size();i++){
					try {
						String queryString=listAuditoriaInit.get(i).getUrl();
//						int index = queryString.indexOf("pages/");
//						System.out.println(queryString);
//						queryString=queryString.substring(index+6, queryString.length());
						listaAuditoriaFiltro.get(i).setUrl(queryString);
					} catch (Exception e) {
					}
					
				
				}
				compDataModel = new CompDataModel(listaAuditoriaFiltro);
			
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}else{
			if(tipoBusqueda.equals("2")){


				
				try {
					listaAuditoriaFiltro = panelAuditoriaServiceImpl.listAuditoriaFiltroPerfil(fechaInicio,fechaFin,nombrePerfil,recursoBusqueda,nomPerfil,moduloBusqueda);
					for(int i=0;i<listAuditoriaInit.size();i++){
						try {
							String queryString=listAuditoriaInit.get(i).getUrl();
							int index = queryString.indexOf("pages/");
							
							queryString=queryString.substring(index+6, queryString.length());
							listaAuditoriaFiltro.get(i).setUrl(queryString);
						} catch (Exception e) {
						}
						
					
					}
					compDataModel = new CompDataModel(listaAuditoriaFiltro);
				
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}

	}
	

	public void handleSelect(SelectEvent event) {
				
	} 

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}


	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getHoraEntrada() {
		return horaEntrada;
	}

	public void setHoraEntrada(String horaEntrada) {
		this.horaEntrada = horaEntrada;
	}

	public String getNombrePantalla() {
		return nombrePantalla;
	}

	public void setNombrePantalla(String nombrePantalla) {
		this.nombrePantalla = nombrePantalla;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public String getModulo() {
		return modulo;
	}

	public void setModulo(String modulo) {
		this.modulo = modulo;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}

	public List<Auditoria> getListAuditoriaInit() {
		return listAuditoriaInit;
	}

	public void setListAuditoriaInit(List<Auditoria> listAuditoriaInit) {
		this.listAuditoriaInit = listAuditoriaInit;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public IAuditoriaService getPanelAuditoriaServiceImpl() {
		return panelAuditoriaServiceImpl;
	}


	public void setPanelAuditoriaServiceImpl(
			IAuditoriaService panelAuditoriaServiceImpl) {
		this.panelAuditoriaServiceImpl = panelAuditoriaServiceImpl;
	}


	public Auditoria getSelectRegistroAudi() {
		return selectRegistroAudi;
	}


	public void setSelectRegistroAudi(Auditoria selectRegistroAudi) {
		this.selectRegistroAudi = selectRegistroAudi;
	}


	public CompDataModel getCompDataModel() {
		return compDataModel;
	}


	public void setCompDataModel(CompDataModel compDataModel) {
		this.compDataModel = compDataModel;
	}


	public List<Usuario> getListUsuarios() {
		return listUsuarios;
	}


	public void setListUsuarios(List<Usuario> listUsuarios) {
		this.listUsuarios = listUsuarios;
	}


	public String getUsrBusqueda() {
		return usrBusqueda;
	}


	public void setUsrBusqueda(String usrBusqueda) {
		this.usrBusqueda = usrBusqueda;
	}

	public String getFechaCaducidad() {
		return fechaCaducidad;
	}

	public void setFechaCaducidad(String fechaCaducidad) {
		this.fechaCaducidad = fechaCaducidad;
	}

	public List<Perfilusuario> getListPerfiles() {
		return listPerfiles;
	}

	public void setListPerfiles(List<Perfilusuario> listPerfiles) {
		this.listPerfiles = listPerfiles;
	}


	public String getPerfilBusqueda() {
		return perfilBusqueda;
	}

	public void setPerfilBusqueda(String perfilBusqueda) {
		this.perfilBusqueda = perfilBusqueda;
	}

	public String getModuloBusqueda() {
		return moduloBusqueda;
	}

	public void setModuloBusqueda(String moduloBusqueda) {
		this.moduloBusqueda = moduloBusqueda;
	}

	public List<Auditoria> getListaAuditoriaFiltro() {
		return listaAuditoriaFiltro;
	}

	public void setListaAuditoriaFiltro(List<Auditoria> listaAuditoriaFiltro) {
		this.listaAuditoriaFiltro = listaAuditoriaFiltro;
	}

	public String getTxtnombreUsuario() {
		return txtnombreUsuario;
	}

	public void setTxtnombreUsuario(String txtnombreUsuario) {
		this.txtnombreUsuario = txtnombreUsuario;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public String getNomPantalla() {
		return nomPantalla;
	}

	public void setNomPantalla(String nomPantalla) {
		this.nomPantalla = nomPantalla;
	}

	public String getNomPerfil() {
		return nomPerfil;
	}

	public void setNomPerfil(String nomPerfil) {
		this.nomPerfil = nomPerfil;
	}

	public String getNomModulo() {
		return nomModulo;
	}

	public void setNomModulo(String nomModulo) {
		this.nomModulo = nomModulo;
	}

	public List<Modulo> getListModulo() {
		return listModulo;
	}

	public void setListModulo(List<Modulo> listModulo) {
		this.listModulo = listModulo;
	}

	public List<Pagina> getListRecurso() {
		return listRecurso;
	}

	public void setListRecurso(List<Pagina> listRecurso) {
		this.listRecurso = listRecurso;
	}

	public String getRecursoBusqueda() {
		return recursoBusqueda;
	}

	public void setRecursoBusqueda(String recursoBusqueda) {
		this.recursoBusqueda = recursoBusqueda;
	}

	public String getNroConectadosHoy() {
		return nroConectadosHoy;
	}

	public void setNroConectadosHoy(String nroConectadosHoy) {
		this.nroConectadosHoy = nroConectadosHoy;
	}

	

	public String getMesActual() {
		return mesActual;
	}

	public void setMesActual(String mesActual) {
		this.mesActual = mesActual;
	}


	public String getRutaimgusrTop() {
		return rutaimgusrTop;
	}

	public void setRutaimgusrTop(String rutaimgusrTop) {
		this.rutaimgusrTop = rutaimgusrTop;
	}


	public Usuario getListUsuarioTop() {
		return listUsuarioTop;
	}

	public void setListUsuarioTop(Usuario listUsuarioTop) {
		this.listUsuarioTop = listUsuarioTop;
	}

//	public List<Usuario> getListUserTop() {
//		return listUserTop;
//	}
//
//	public void setListUserTop(List<Usuario> listUserTop) {
//		this.listUserTop = listUserTop;
//	}

	public String getNombrePerfil() {
		return nombrePerfil;
	}

	public void setNombrePerfil(String nombrePerfil) {
		this.nombrePerfil = nombrePerfil;
	}

	public String getTipoBusqueda() {
		return tipoBusqueda;
	}

	public void setTipoBusqueda(String tipoBusqueda) {
		this.tipoBusqueda = tipoBusqueda;
	}

	public String getTxtcargoUsuario() {
		return txtcargoUsuario;
	}

	public void setTxtcargoUsuario(String txtcargoUsuario) {
		this.txtcargoUsuario = txtcargoUsuario;
	}

 
}
