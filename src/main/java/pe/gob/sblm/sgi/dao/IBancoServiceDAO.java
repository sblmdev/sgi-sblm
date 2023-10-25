package pe.gob.sblm.sgi.dao;

import java.util.List;

import pe.gob.sblm.sgi.entity.Banco;
import pe.gob.sblm.sgi.entity.Cuentabancaria;

public interface IBancoServiceDAO {

	void grabarbanco(Banco banco);

	List<Banco> obtenerTodosBancos();

	void eliminarBanco(Banco banco);

	int nroBancos();




}
