package pe.gob.sblm.sgi.dao;

import java.util.List;

import pe.gob.sblm.sgi.entity.Banco;
import pe.gob.sblm.sgi.entity.Cuentabancaria;

public interface ICuentaBancariaDAO {

	void grabarCuentaBancaria(Cuentabancaria banco);

	List<Cuentabancaria> obtenerTodasCuentasBancarias();

	void eliminarCuentabancaria(Cuentabancaria banco);

	int nroCuentasBancarias();

	List<Banco> listBancos();

}
