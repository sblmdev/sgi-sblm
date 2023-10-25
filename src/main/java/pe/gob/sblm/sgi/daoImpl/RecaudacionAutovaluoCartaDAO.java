package pe.gob.sblm.sgi.daoImpl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import pe.gob.sblm.api.commons.utility.FuncionesHelper;
import pe.gob.sblm.sgi.dao.IRecaudacionAutovaluoCartaDAO;
import pe.gob.sblm.sgi.entity.Auditoria;
import pe.gob.sblm.sgi.entity.Carta;
import pe.gob.sblm.sgi.entity.Estadoauditoria;
import pe.gob.sblm.sgi.entity.Eventoauditoria;
import pe.gob.sblm.sgi.entity.Modulo;
import pe.gob.sblm.sgi.entity.Usuario;

@Repository(value = "recaudacionautovaluocartaDAO")
public class RecaudacionAutovaluoCartaDAO implements IRecaudacionAutovaluoCartaDAO,Serializable{

	@Autowired
	private SessionFactory sessionFactory;
	

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	
	public void grabarCabeceraArbitrio(Carta carta) {
		getSessionFactory().getCurrentSession().merge(carta);
		
		settingLog(4, 26);
	}

	
	public List<Carta> listarArbitrioConsulta() {
		Session session = getSessionFactory().openSession();

		return session.createQuery("from Carta order by fechacreacion desc ").list();
	}

	
	public Carta getUltimaCabeceraGrabada() {
		Session session = getSessionFactory().openSession();
		int tamanio;
		
		Query q = session.createQuery("FROM Carta");
		tamanio= q.list().size();
		
		Carta A = new Carta();
		
		q.setFirstResult(tamanio-1);
		q.setMaxResults(tamanio);
		
		A=(Carta) q.list().get(0);
		
		return A;
	}


	
	public void actualizarCabeceraCarta(Carta carta) {
		String updateQuery=	"UPDATE CARTA  SET"+
				",[JRAVCALLE]='" +carta.getJravcalle()+"'"+
				",[INTDTOSTAND]='" +carta.getIntdtostand()+"'"+
				",[NRO] ='" +carta.getNro()+"'"+
				",[CLAVE]='" +carta.getClave()+"'"+
				",[IDUSUARIOCREADOR]='" +carta.getIdusuariocreador()+"'"+
				" WHERE IDCARTA='"+carta.getIdcarta()+"'";

			getSessionFactory().getCurrentSession().createSQLQuery(updateQuery).executeUpdate();
			
			settingLog(4, 28);	}

	
	public  void settingLog(int idestadoauditoria, int ideventoauditoria){
		String url=FuncionesHelper.getURL().toString();
		
		try {
			int index = url.indexOf("pages/");
			url=url.substring(index+6, url.length());
			url=url.substring(0, url.length()-4);
		} catch (Exception e) {
		}
	

	
		Auditoria Adt= new Auditoria();
		Usuario usr= new Usuario();
		usr.setIdusuario((Integer)(FuncionesHelper.getUsuario()));
		
//		Area area = new Area();
//		area.setIdare("10800000");
//		usr.setArea(area);
		
		Modulo mod= new Modulo();
		Session session = getSessionFactory().openSession();
		
		
		Query modulo=session.createSQLQuery("select  m.IDMODULO from PAGINAMODULO as pm inner join MODULO m on pm.IDMODULO= m.IDMODULO  inner join PAGINA as p on p.IDPAGINA=pm.IDPAGINA where P.NOMBREPAGINA='"+url+"'");
		
		if (modulo.list().size()==1) {
			int var=(Integer) modulo.list().get(0);
			mod.setIdmodulo(var);
		} else {
			//mod.setIdmodulo(idmodulo)
		}
		
		Estadoauditoria esa= new Estadoauditoria();
		esa.setIdestadoauditoria(idestadoauditoria);
		Eventoauditoria eva= new Eventoauditoria();
		eva.setIdeventoauditoria(ideventoauditoria);
		Adt.setUsuario(usr);
		Adt.setModulo(mod);
		Adt.setEstadoauditoria(esa);
		Adt.setEventoauditoria(eva);
		Adt.setFecentrada( new Date());
		Adt.setNompantalla(url);
		Adt.setUrl(FuncionesHelper.getURL().toString());
		if(FuncionesHelper.getTerminal().toString().equals("0:0:0:0:0:0:0:1")){
			Adt.setIp("192.");
		}else{
			Adt.setIp(FuncionesHelper.getTerminal().toString());
		}
		Adt.setEstado(true);
		Adt.setCodauditoria(0);
		try {
		getSessionFactory().getCurrentSession().save(Adt);
		
		} catch (Exception e) {
		e.printStackTrace();}
		
		}

	
}
