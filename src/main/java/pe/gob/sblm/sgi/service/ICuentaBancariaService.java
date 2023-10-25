package pe.gob.sblm.sgi.service;

import java.util.List;

import pe.gob.sblm.sgi.entity.Banco;

public interface ICuentaBancariaService {

	List<pe.gob.sblm.sgi.entity.Cuentabancaria> obtenerTodasCuentasBancarias();

	void grabarCuentaBancaria(pe.gob.sblm.sgi.entity.Cuentabancaria cuentabancaria);

	int nroCuentasBancarias();

	void eliminarCuentabancaria(pe.gob.sblm.sgi.entity.Cuentabancaria cuentabancaria);

	List<Banco> listBancos();

}
