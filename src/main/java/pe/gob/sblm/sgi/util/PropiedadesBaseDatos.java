package pe.gob.sblm.sgi.util;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;


public class PropiedadesBaseDatos {
	private static final String BUNDLE_NAME = "configuracionParametrosBaseDatos"; 
	private static final ResourceBundle RESOURCE_BUNDLE_NAME = ResourceBundle.getBundle(BUNDLE_NAME, Locale.ENGLISH, Thread.currentThread().getContextClassLoader());
	
	public static String getString(String key) {
		
		try {
			return RESOURCE_BUNDLE_NAME.getString(key);
		} catch (MissingResourceException e) {
			e.printStackTrace();
		}
		return null;
	}

}
