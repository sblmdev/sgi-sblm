package pe.gob.sblm.sgi.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
@ManagedBean
public class Almanaque implements Serializable {

	private static final long serialVersionUID = -310383516571744171L;
	private final static String[] mes; 	
	
	static {  
        mes = new String[12];  
        mes[0] = "Enero";  
        mes[1] = "Febrero";  
        mes[2] = "Marzo";  
        mes[3] = "Abril";  
        mes[4] = "Mayo";  
        mes[5] = "Junio";  
        mes[6] = "Julio";  
        mes[7] = "Agosto";  
        mes[8] = "Septiembre";  
        mes[9] = "Octubre";  
        mes[10] = "Noviembre";  
        mes[11] = "Diciembre";  
 }
	
	public  static String mesanumero(String mes){
		
		if (mes.equals("Enero")) {
			return "01";
		} else {
			if (mes.equals("Febrero")) {
				return "02";
			} else {
				if (mes.equals("Marzo")) {
					return "03";
				} else {
					if (mes.equals("Abril")) {
						return "04";
					} else {
						if (mes.equals("Mayo")) {
							return "05";
						} else {
							if (mes.equals("Junio")) {
								return "06";
							} else {
								if (mes.equals("Julio")) {
									return "07";
								} else {
								if (mes.equals("Agosto")) {
									return "08";
								} else {
									if (mes.equals("Septiembre")) {
										return "09";
									} else {
										if (mes.equals("Octubre")) {
											return "10";
										} else {
											if (mes.equals("Noviembre")) {
												return "11";
											} else {
												return "12";
											}
										}
									}
								}	
								}
							}
						}
					}
				}
			}
		}
		
	}
	
	
	public static List<String> listaAniosAlmanaque(){
		List<String> listAnio =new ArrayList<String>();
		for(int i=0;i<12;i++){
			 listAnio.add(String.valueOf(2002+i));
		}
		return listAnio;
	}
	
	public String obtenerMes(Date fecha){
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(fecha);
		
		return mes[calendar.get(Calendar.MONTH)];
	}
	
	public static Integer getNumeroMes(Date fecha){
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(fecha);
		
		return calendar.get(Calendar.MONTH);
	}
	
	public static Integer getNumeroDia(Date fecha){
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(fecha);
		
		return calendar.get(Calendar.DATE);
	}
	
	public static Integer getNumeroAnio(Date fecha){
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(fecha);
		
		return calendar.get(Calendar.YEAR);
	}
	
	public static String obtenerNombreMes(int nroMes){
		
		return mes[nroMes];
		
		
	}
	
	public static String[] getMes() {
		return mes;
	}


	
}
