package pe.gob.sblm.sgi.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import pe.gob.sblm.sgi.entity.Contrato;
import pe.gob.sblm.sgi.service.IContratoService;

@ManagedBean(name = "letraMB")
@ViewScoped
public class LetraManagedBean extends BaseController implements Serializable {
	
	private static final long serialVersionUID = -5464824396065999857L;
	private List<Contrato> contratos= new ArrayList<Contrato>();
	
	
	@ManagedProperty(value = "#{contratoService}")
	private transient IContratoService contratoService;
	
	@PostConstruct
	public void init(){
//		contratos=contratoService.getListaContrato("Contrato");
	}
	
	
	public void test(){
	Double val;
//		System.out.println(FuncionesHelper.conversor(1983.90));
//		System.out.println(FuncionesHelper.conversor(2483.90));
//		System.out.println(FuncionesHelper.conversor(3920.90));
//		System.out.println(FuncionesHelper.conversor(2.90));
//		System.out.println(FuncionesHelper.conversor(120.0));
		
		for (Contrato contrato : contratos) {
			
		val=0.0;
		val=contratoService.sumacobrado(contrato.getIdcontrato());
		System.out.println(val);
			contratoService.actualizarMonto(contrato.getIdcontrato(),val);
		}
	}


	public List<Contrato> getContratos() {
		return contratos;
	}


	public void setContratos(List<Contrato> contratos) {
		this.contratos = contratos;
	}


	public IContratoService getContratoService() {
		return contratoService;
	}


	public void setContratoService(IContratoService contratoService) {
		this.contratoService = contratoService;
	}
	
	

}
