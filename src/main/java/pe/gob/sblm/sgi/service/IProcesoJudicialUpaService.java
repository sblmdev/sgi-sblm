package pe.gob.sblm.sgi.service;
import pe.gob.sblm.sgi.bean.ProcesojudicialupaBean;
import pe.gob.sblm.sgi.entity.Procesojudicialupa;

import java.util.List;

public interface IProcesoJudicialUpaService {
   public List<ProcesojudicialupaBean> buscarExpediente(int idprocesojudicialupa);
   public Procesojudicialupa buscarDatosExpediente(int idprocesojudicialupa);
}
