package pe.gob.sblm.sgi.daoImpl;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import pe.gob.sblm.sgi.dao.MaeIpcDao;
import pe.gob.sblm.sgi.entity.MaeIpc;

@Repository(value = "ipcDAO")
public class MaeIpcDaoImpl implements MaeIpcDao,Serializable{

	private static final long serialVersionUID = 1L;

	@Autowired
	private SessionFactory sessionFactory;




	@SuppressWarnings("unchecked")
	@Override
	public List<MaeIpc> listarIpc() {
		StringBuilder hql= new StringBuilder();
		hql.append("select mi from MaeIpc mi order by mi.anio asc");
		
		Query query=sessionFactory.getCurrentSession().createQuery(hql.toString());
		query.setMaxResults(30);
		
		return query.list();
	}

	
}
