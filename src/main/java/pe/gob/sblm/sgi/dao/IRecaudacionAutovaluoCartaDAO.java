package pe.gob.sblm.sgi.dao;

import java.util.List;

import pe.gob.sblm.sgi.entity.Archivo;
import pe.gob.sblm.sgi.entity.Carta;

public interface IRecaudacionAutovaluoCartaDAO {

	void grabarCabeceraArbitrio(Carta carta);

	List<Carta> listarArbitrioConsulta();

	Carta getUltimaCabeceraGrabada();

	void actualizarCabeceraCarta(Carta carta);

}
