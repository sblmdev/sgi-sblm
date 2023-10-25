package pe.gob.sblm.sgi.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import pe.gob.sblm.sgi.bean.ArbitrioBean;
import pe.gob.sblm.sgi.bean.DetalleArbitrioBean;
import pe.gob.sblm.sgi.bean.UpaBean;
import pe.gob.sblm.sgi.entity.Arbitrio;
import pe.gob.sblm.sgi.entity.Detallearbitrio;
import pe.gob.sblm.sgi.entity.Upa;
import pe.gob.sblm.sgi.service.IAutovaluoArbitrioService;
import pe.gob.sblm.sgi.service.IUpaService;
import pe.gob.sblm.sgi.util.Almanaque;

@ManagedBean(name = "autovaluoarbitrioMB")
@ViewScoped
public class RecaudacionAutovaluoArbitrioManagedBean extends BaseController implements Serializable {
	private static final long serialVersionUID = 5524190003746598593L;
	
	@ManagedProperty(value = "#{autovaluoarbitrioService}")
	private transient IAutovaluoArbitrioService arbitrioService;

	private List<UpaBean> listaUPA=new ArrayList<UpaBean>();
	private List<UpaBean> listaUPAFiltered;
	
	private UpaBean upaBean=new UpaBean();
	private String direccionUPA;
	
	private List<ArbitrioBean> listaARBITRIO;
	private List<ArbitrioBean> listaARBITRIOFiltered;
	
	private List<DetalleArbitrioBean> listaDETALLEARBITRIO;
	private DetalleArbitrioBean detalleArbitrioBean;
	
	private ArbitrioBean arbitrioBean= new ArbitrioBean();
	private int anioseleccionado=2014;
	private String  frecuenciaseleccionado;
	
	
	@ManagedProperty(value = "#{usuarioMB}")
	UsuarioManagedBean usuarioMB;
	
	@ManagedProperty(value = "#{upaService}")
	private transient IUpaService upaService;

	
	
	@ManagedProperty(value = "#{usuarioMB}")
	UsuarioManagedBean userMB;
	
	@PostConstruct
	public void init(){
		
	}
	
	/**BUSQUEDAS*/
	public void buscarUPA() {
//		listaUPA=upaService.listarUpasBeanxDireccion(direccionUPA);
	}
	
	/**NEGOCIO*/
	
	public void loadInformationUPA() {
		listaARBITRIO=arbitrioService.listarArbitriosxAnio(upaBean.getIdupa());
	}
	

	public void loadDetalleArbitrio() {
		listaDETALLEARBITRIO=arbitrioService.listarDetalleArbitrio(arbitrioBean.getIdarbitrio());
	}
	
	
	public void  editarDetalleArbitrio() {
		
		System.out.println(detalleArbitrioBean.getNrorecibo());
		arbitrioService.editarDetalleArbitrio(detalleArbitrioBean);
		
	}
	
	
	public void grabarPeriodo() {
		Arbitrio arbitrio= new Arbitrio();
		//COMENTADO 19062019 creacion de nueva tabla arbitrio
//		arbitrio.setFrecuencia(frecuenciaseleccionado);
//		arbitrio.setAnio(anioseleccionado);
		arbitrio.setUsrcre(userMB.getNombrecompleto());
		arbitrio.setFeccre(new Date());
		
		
		Upa upa= new Upa();
		upa.setIdupa(upaBean.getIdupa());
		
		arbitrio.setUpa(upa);
		
		int nro = 0;
		String mes;
		if (frecuenciaseleccionado.equals("Mensual"))
			nro=12;
			
			else if (frecuenciaseleccionado.equals("Bimestral"))
					nro=6;
				else if (frecuenciaseleccionado.equals("Trimestral"))
						nro=4;
					else if (frecuenciaseleccionado.equals("Semestral"))
							nro=2;
						else if (frecuenciaseleccionado.equals("Anual"))
								nro=1;
		
		arbitrioService.grabarPeriodoArbitrio(arbitrio);
		
		for (int i = 1; i <= nro; i++) {
			 Detallearbitrio detallearbitrio= new Detallearbitrio();
			 detallearbitrio.setSecuencia(i);
			 detallearbitrio.setInsoluto(0.0);
			 detallearbitrio.setMora(0.0);
			 detallearbitrio.setNrorecibo("");
			 detallearbitrio.setObservacion("");
			 detallearbitrio.setFeccre(new Date());
			 detallearbitrio.setUsrcre(userMB.getNombrecompleto());

			 
			 if (nro==12) 
			 detallearbitrio.setMes(Almanaque.obtenerNombreMes(i-1).substring(0, 3));
			 else if(nro==6)
			 detallearbitrio.setMes(Almanaque.obtenerNombreMes(2*(i-1)).substring(0, 3)+","+Almanaque.obtenerNombreMes(2*(i-1)+1).substring(0, 3));
			 else if(nro==4)
			 detallearbitrio.setMes(Almanaque.obtenerNombreMes(3*(i-1)).substring(0, 3)+","+Almanaque.obtenerNombreMes(3*(i-1)+1).substring(0, 3)+","+Almanaque.obtenerNombreMes(3*(i-1)+2).substring(0, 3));
			 else if(nro==2)
			 detallearbitrio.setMes(Almanaque.obtenerNombreMes(6*(i-1)).substring(0, 3)+","+Almanaque.obtenerNombreMes(6*(i-1)+1).substring(0, 3)+","+Almanaque.obtenerNombreMes(6*(i-1)+2).substring(0, 3)+","+Almanaque.obtenerNombreMes(6*(i-1)+3).substring(0, 3)+","+Almanaque.obtenerNombreMes(6*(i-1)+4).substring(0, 3)+","+Almanaque.obtenerNombreMes(6*(i-1)+5).substring(0, 3));	 	 
			 else if(nro==1)
			 detallearbitrio.setMes(Almanaque.obtenerNombreMes((i-1)).substring(0, 3)+","+Almanaque.obtenerNombreMes(i).substring(0, 3)+","+Almanaque.obtenerNombreMes(i+1).substring(0, 3)+","+Almanaque.obtenerNombreMes(i+2).substring(0, 3)+","+Almanaque.obtenerNombreMes(i+3).substring(0, 3)+","+Almanaque.obtenerNombreMes(i+4).substring(0, 3)
					 		   +","+Almanaque.obtenerNombreMes(i+5).substring(0, 3)+","+Almanaque.obtenerNombreMes(i+6).substring(0, 3)+","+Almanaque.obtenerNombreMes(i+7).substring(0, 3)+","+Almanaque.obtenerNombreMes(i+8).substring(0, 3)+","+Almanaque.obtenerNombreMes(i+9).substring(0, 3)+","+Almanaque.obtenerNombreMes(i+10).substring(0, 3));	 	 
				 
			 
			 detallearbitrio.setArbitrio(arbitrio);
			 arbitrioService.grabarDetallePeriodoArbitrio(detallearbitrio);
			
		}
		
	}
	
	
	/**VALIDACIONES*/
	
	public void validarAgregarArbitrio() {
		


	}
	
	public void validarAgregarPeriodo() {
		if (upaBean==null) {
			addErrorMessage("No ha seleccionado upa", "Busque o seleccione upa para agregar período");
		}else  {
			RequestContext contextRequest = RequestContext.getCurrentInstance();
			contextRequest.execute("dlgAgregarPeriodo.show();");
		}
	}
	
	public void validarGrabarPeriodo() {
		if (anioseleccionado==0) {
			addWarnMessage("No ha ingresado año", "Ingrese el año para continuar");
		}else if (frecuenciaseleccionado.equals("")) {
			addWarnMessage("No ha ingresado la frecuencia", "Ingrese la frecuencia para continuar");
		}else {
			RequestContext contextRequest = RequestContext.getCurrentInstance();
			contextRequest.execute("dlgConfirmarPeriodo.show();");
		}
	}
	
	

	public List<UpaBean> getListaUPA() {
		return listaUPA;
	}

	public void setListaUPA(List<UpaBean> listaUPA) {
		this.listaUPA = listaUPA;
	}

	public UsuarioManagedBean getUsuarioMB() {
		return usuarioMB;
	}

	public void setUsuarioMB(UsuarioManagedBean usuarioMB) {
		this.usuarioMB = usuarioMB;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public UpaBean getUpaBean() {
		return upaBean;
	}

	public void setUpaBean(UpaBean upaBean) {
		this.upaBean = upaBean;
	}

	public String getDireccionUPA() {
		return direccionUPA;
	}

	public void setDireccionUPA(String direccionUPA) {
		this.direccionUPA = direccionUPA;
	}

	public IUpaService getUpaService() {
		return upaService;
	}

	public void setUpaService(IUpaService upaService) {
		this.upaService = upaService;
	}

	public List<UpaBean> getListaUPAFiltered() {
		return listaUPAFiltered;
	}

	public void setListaUPAFiltered(List<UpaBean> listaUPAFiltered) {
		this.listaUPAFiltered = listaUPAFiltered;
	}

	public List<ArbitrioBean> getListaARBITRIO() {
		return listaARBITRIO;
	}

	public void setListaARBITRIO(List<ArbitrioBean> listaARBITRIO) {
		this.listaARBITRIO = listaARBITRIO;
	}



	public ArbitrioBean getArbitrioBean() {
		return arbitrioBean;
	}

	public void setArbitrioBean(ArbitrioBean arbitrioBean) {
		this.arbitrioBean = arbitrioBean;
	}

	public List<ArbitrioBean> getListaARBITRIOFiltered() {
		return listaARBITRIOFiltered;
	}

	public void setListaARBITRIOFiltered(List<ArbitrioBean> listaARBITRIOFiltered) {
		this.listaARBITRIOFiltered = listaARBITRIOFiltered;
	}

	public int getAnioseleccionado() {
		return anioseleccionado;
	}

	public void setAnioseleccionado(int anioseleccionado) {
		this.anioseleccionado = anioseleccionado;
	}

	public List<DetalleArbitrioBean> getListaDETALLEARBITRIO() {
		return listaDETALLEARBITRIO;
	}

	public void setListaDETALLEARBITRIO(
			List<DetalleArbitrioBean> listaDETALLEARBITRIO) {
		this.listaDETALLEARBITRIO = listaDETALLEARBITRIO;
	}

	public IAutovaluoArbitrioService getArbitrioService() {
		return arbitrioService;
	}

	public void setArbitrioService(
			IAutovaluoArbitrioService arbitrioService) {
		this.arbitrioService = arbitrioService;
	}

	public UsuarioManagedBean getUserMB() {
		return userMB;
	}

	public void setUserMB(UsuarioManagedBean userMB) {
		this.userMB = userMB;
	}

	public String getFrecuenciaseleccionado() {
		return frecuenciaseleccionado;
	}

	public void setFrecuenciaseleccionado(String frecuenciaseleccionado) {
		this.frecuenciaseleccionado = frecuenciaseleccionado;
	}

	public DetalleArbitrioBean getDetalleArbitrioBean() {
		return detalleArbitrioBean;
	}

	public void setDetalleArbitrioBean(DetalleArbitrioBean detalleArbitrioBean) {
		this.detalleArbitrioBean = detalleArbitrioBean;
	}


}
