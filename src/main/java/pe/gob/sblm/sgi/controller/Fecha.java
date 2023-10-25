package pe.gob.sblm.sgi.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Fecha {
//    public static void main(String[] args) {
//        try {
//            Calendar inicio = new GregorianCalendar();
//            Calendar fin = new GregorianCalendar();
//            inicio.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("01/07/2013"));
//            fin.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("30/06/2014"));
//            int difA = fin.get(Calendar.YEAR) - inicio.get(Calendar.YEAR);
//            int difM = difA * 12 + fin.get(Calendar.MONTH) - inicio.get(Calendar.MONTH);
//            System.out.println(difM);
//            System.out.println(difA);
//        } catch(ParseException ex) {
//
//        }
//    }
    
    public static void main(String[] args) {
        try {
            Calendar inicio = new GregorianCalendar();
            Calendar fin = new GregorianCalendar();
            Calendar inicioa = new GregorianCalendar();
            Calendar fina = new GregorianCalendar();
            inicio.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("01/07/2013"));
            fin.setTime(new SimpleDateFormat("dd/MM/yyyy").parse("30/06/2014"));
            int i =0;
            inicioa=inicio;
            fina=fin;
            while(inicioa.compareTo(fina)<0){
            	inicioa.add(inicioa.MONTH, 1);
            	System.out.println("i=["+i);
            	i++;
            }
            int difA = fin.get(Calendar.YEAR) - inicio.get(Calendar.YEAR);
            int difM = difA * 12 + fin.get(Calendar.MONTH) - inicio.get(Calendar.MONTH);
            System.out.println(difM);
            System.out.println(difA);
        } catch(ParseException ex) {

        }
    }
    
}
