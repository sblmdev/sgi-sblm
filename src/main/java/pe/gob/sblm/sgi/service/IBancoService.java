package pe.gob.sblm.sgi.service;

import java.util.List;

import pe.gob.sblm.sgi.entity.Banco;

public interface IBancoService {

	List<Banco> obtenerTodosBancos();

	void grabarBanco(Banco banco);

	int nroBancos();

	void eliminarBanco(Banco banco);

}
