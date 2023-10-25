package pe.gob.sblm.sgi.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.gob.sblm.sgi.dao.IRecaudacionAutovaluoCartaDAO;
import pe.gob.sblm.sgi.entity.Carta;
import pe.gob.sblm.sgi.service.IAutovaluoCartaService;

@Transactional(readOnly = true)
@Service(value="recaudacionautovaluocartaService")
public class AutovaluoCartaService implements IAutovaluoCartaService{
	@Autowired
	private IRecaudacionAutovaluoCartaDAO recaudacionautovaluocartaDAO;


	
	public void grabarCabeceraCarta(Carta carta) {
		
		recaudacionautovaluocartaDAO.grabarCabeceraArbitrio(carta);
		
	}


	
	public List<Carta> listarArbitrioConsulta() {
		// TODO Auto-generated method stub
		return recaudacionautovaluocartaDAO.listarArbitrioConsulta();
	}


	
	public Carta getUltimaCabeceraGrabada() {
		// TODO Auto-generated method stub
		return recaudacionautovaluocartaDAO.getUltimaCabeceraGrabada();
	}



	
	public void actualizarCabeceraCarta(Carta carta) {
		recaudacionautovaluocartaDAO.actualizarCabeceraCarta(carta);
		
	}


	
}
