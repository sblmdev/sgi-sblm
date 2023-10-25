package pe.gob.sblm.sgi.service;

import java.util.List;

import pe.gob.sblm.sgi.entity.Archivo;
import pe.gob.sblm.sgi.entity.Carta;

public interface IAutovaluoCartaService {

	void grabarCabeceraCarta(Carta carta);

	List<Carta> listarArbitrioConsulta();

	Carta getUltimaCabeceraGrabada();

	void actualizarCabeceraCarta(Carta carta);

}
