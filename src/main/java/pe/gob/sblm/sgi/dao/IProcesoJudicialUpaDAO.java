package pe.gob.sblm.sgi.dao;
import java.util.List;

import pe.gob.sblm.sgi.bean.ProcesojudicialupaBean;
import pe.gob.sblm.sgi.entity.Procesojudicialupa;
public interface IProcesoJudicialUpaDAO {
 public List<ProcesojudicialupaBean> buscarExpediente(int idprocesojudicialupa);
 public Procesojudicialupa buscarDatosExpediente(int idprocesojudicialupa);
}
