package pe.gob.sblm.sgi.dao;

import java.util.List;

import pe.gob.sblm.sgi.entity.Tipodocupersona;

public interface TipoDocuPersonaDAO {

	List<Tipodocupersona> listarTipoDocumentoPersona();
	Tipodocupersona obtenerTipoDocPersona(String idtipo);
	Integer longitudTipoDocumentoPersona(String idtipo);
}
