package pe.gob.sblm.sgi.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.gob.sblm.sgi.dao.MaeIpcDao;
import pe.gob.sblm.sgi.entity.MaeIpc;
import pe.gob.sblm.sgi.service.IpcService;

@Service(value="ipcService")
public class IpcServiceImpl implements IpcService {
	@Autowired
	private MaeIpcDao ipcDao;

	@Transactional(readOnly=true)
	public List<MaeIpc> listarIpc() {
		return ipcDao.listarIpc();
	}

	
}
