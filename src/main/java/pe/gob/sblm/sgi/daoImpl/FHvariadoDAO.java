package pe.gob.sblm.sgi.daoImpl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import pe.gob.sblm.sgi.bean.ItemBean;
import pe.gob.sblm.sgi.dao.IFHvariadoDAO;
import pe.gob.sblm.sgi.entity.Area;
import pe.gob.sblm.sgi.entity.Perfil;
import pe.gob.sblm.sgi.entity.Usuario;

@Repository(value = "FHvariadoDAO")
public class FHvariadoDAO implements IFHvariadoDAO,Serializable{


	private static final long serialVersionUID = 1L;

	@Autowired
	private SessionFactory sessionFactory;

	
	
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}


	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}


	
	public List<Perfil> obtenerPerfiles() {
		Session session = sessionFactory.openSession();
		return session.createQuery("from Perfil").list();
	}
	

	
	public List<Usuario> obtenerUsuarios() {
		Session session = sessionFactory.openSession();
		List<List> list = null;
		
		
		list = session.createQuery("select new List(U.idusuario,U.rutaimgusr,U.cargo,A.desare,U.nombrescompletos,U.estado,U.escobrador,U.codCob,U.emailusr) from Usuario U inner join U.area A").list();
		List<Usuario> lista = new ArrayList<Usuario>();
		
		
		for(int i=0;i<list.size();i++){
			
			Area area= new Area();
			
			area.setDesare((String) list.get(i).get(3));
			
			
			
			Usuario usuario= new Usuario();
			usuario.setRutaimgusr((String) list.get(i).get(1));
			usuario.setCargo((String) list.get(i).get(2));
			usuario.setIdusuario((Integer) list.get(i).get(0));
			usuario.setNombrescompletos((String)list.get(i).get(4));
			usuario.setEstado((Boolean)list.get(i).get(5));
			usuario.setEscobrador((Boolean)list.get(i).get(6));
			usuario.setArea(area);
			usuario.setCodCob((Integer)list.get(i).get(7));
			usuario.setEmailusr((String)list.get(i).get(8));
			
			lista.add(usuario);
		}
		
		
		return lista;
	}


	@SuppressWarnings("unchecked")
	
	public List<Usuario> obtenerCobradores() {
		return getSessionFactory().getCurrentSession().createQuery("select U from Usuario U inner join U.area A where U.escobrador='true'").list();
	}


	@SuppressWarnings("unchecked")
	
	public List<ItemBean> obtenerCobradores(String texto) {
		
		System.out.println("######"+texto+"#########");
		
		StringBuilder hql = new StringBuilder();
		hql.append("select usr.idusuario as id,usr.nombrescompletos as descripcion ");
		hql.append("from Usuario as usr ");
		hql.append("where usr.nombrescompletos like '%");
		hql.append(texto);
		hql.append("%' ");
		hql.append(" and usr.escobrador='true' order by usr.nombrescompletos");
		
		Query query = getSessionFactory().getCurrentSession().createQuery(hql.toString());
		return query.setResultTransformer(Transformers.aliasToBean(ItemBean.class)).list();
	}


	@SuppressWarnings("unchecked")
	
	public List<ItemBean> obtenerCobradoresPorNombreExacto(String texto) {
		
		StringBuilder hql = new StringBuilder();
		hql.append("select usr.idusuario as id,usr.nombrescompletos as descripcion ");
		hql.append("from Usuario as usr ");
		hql.append("where usr.nombrescompletos= :texto ");		
		hql.append(" and usr.escobrador='true' order by usr.nombrescompletos");
		
		Query query = getSessionFactory().getCurrentSession().createQuery(hql.toString());
		query.setParameter("texto", texto);
		return query.setResultTransformer(Transformers.aliasToBean(ItemBean.class)).list();
	}




	
	}
