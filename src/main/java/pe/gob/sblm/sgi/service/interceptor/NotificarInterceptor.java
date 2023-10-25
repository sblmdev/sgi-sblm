package pe.gob.sblm.sgi.service.interceptor;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pe.gob.sblm.sgi.entity.Contrato;
import pe.gob.sblm.sgi.service.NotificarService;

@Aspect
@Component
public class NotificarInterceptor  {
	
	@Autowired
	NotificarService notificarService;
	
	
	private static final Logger Logger = LoggerFactory.getLogger(NotificarInterceptor.class);

	
	@After("execution(* pe.gob.sblm.sgi.serviceImpl.ContratoService.registrarContrato(..))")
	public void proceed(JoinPoint joinPoint) throws Exception   {
		
		try {
			
	    	 Object[] signatureArgs = joinPoint.getArgs();
	    	 
	    	 Contrato contrato= new Contrato();
	    	 contrato=(Contrato) signatureArgs[0];
	    	 
	    	 if (contrato.getIdcontrato()!=0) {
	    		 
	    		if (contrato.getFechamodificacion()!=null) {
	    			 notificarService.notificarActualizacionCondicion(contrato.getUpa().getClave(), contrato.getCondicion());
				} else {
					 notificarService.notificarNuevoRegistroCondicion(contrato.getUpa().getClave(), contrato.getCondicion());
				}
	    	 
	    	 } 
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
    		throw new Exception(e.getMessage(), e);
		}
		

	 }
}
