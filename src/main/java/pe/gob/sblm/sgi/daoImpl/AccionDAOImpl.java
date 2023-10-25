package pe.gob.sblm.sgi.daoImpl;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import pe.gob.sblm.sgi.bean.AccionUsuarioBean;
import pe.gob.sblm.sgi.dao.AccionDAO;
import pe.gob.sblm.sgi.entity.MaeAccion;
import pe.gob.sblm.sgi.entity.MovAccionUsuario;
import pe.gob.sblm.sgi.entity.Usuario;

@Repository(value = "accionDAO")
public class AccionDAOImpl implements AccionDAO,Serializable{

	private static final long serialVersionUID = 1L;

	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<AccionUsuarioBean> obtenerUsuarioAccion(String nombreAccion) {
		
		StringBuilder hql= new StringBuilder();
		hql.append("select u.idusuario as idusuario,a.descripcion as descripcionAccion,a.nombre as nombreAccion, u.nombrescompletos as nombresUsuario, u.emailusr as correoUsuario from MovAccionUsuario mau inner join mau.usuario u inner join mau.maeAccion a where  a.nombre='"+nombreAccion+"'and  u.estado=1 ");
		
		Query query= sessionFactory.getCurrentSession().createQuery(hql.toString());
		
		return query.setResultTransformer(Transformers.aliasToBean(AccionUsuarioBean.class)).list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<AccionUsuarioBean> obtenerUsuarioAccion(int idaccion) {
		StringBuilder hql= new StringBuilder();
		hql.append("select u.idusuario as idusuario,a.descripcion as descripcionAccion, u.nombrescompletos as nombresUsuario, u.emailusr as correoUsuario from MovAccionUsuario mau inner join mau.usuario u inner join mau.maeAccion a where  a.idaccion='"+idaccion+"' and u.estado=1 ");
		
		Query query= sessionFactory.getCurrentSession().createQuery(hql.toString());
		
		return query.setResultTransformer(Transformers.aliasToBean(AccionUsuarioBean.class)).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MaeAccion> obtenerAcciones() {
		
		StringBuilder hql= new StringBuilder();
		hql.append("select ma from MaeAccion ma");
		
		return (List<MaeAccion>) sessionFactory.getCurrentSession().createQuery(hql.toString()).list();
	}

	@Override
	public void grabarUsuarioAccion(int idusuarioSeleccionado,int idAccionSeleccionado) {
		MovAccionUsuario movAccionUsuario= new MovAccionUsuario();
		movAccionUsuario.setUsuario(new Usuario(idusuarioSeleccionado));
		movAccionUsuario.setMaeAccion(new MaeAccion(idAccionSeleccionado));
		
		sessionFactory.getCurrentSession().save(movAccionUsuario);
		
	}

	@Override
	public void eliminarUsuarioAccion(int idusuario, int idaccion) {
		StringBuilder hql= new StringBuilder();
		
		String deleteQuery="DELETE MOV_ACCION_USUARIO WHERE ID_ACCION="+idaccion+" AND IDUSUARIO="+idusuario;
		
		sessionFactory.getCurrentSession().createSQLQuery(deleteQuery).executeUpdate();
	}

}
