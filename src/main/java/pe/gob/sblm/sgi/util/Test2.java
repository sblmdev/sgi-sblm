package pe.gob.sblm.sgi.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.json.JSONException;
import org.json.JSONObject;

//import org.json.JSONException;
//import org.json.JSONObject;

public class Test2 {

    private final static String Token = "ef5e49a4-f461-4928-8b01-064e522a4a86-49610454-2d87-4152-bf66-2f7cd19f86c1";

    public static void main(String[] args) {
        String ruc = "20100070970";
        try {
            JSONObject empresa = GetInfo(ruc);

            System.out.println("RUC: " + empresa.getString("Ruc"));
            System.out.println("NOMBRE: " + empresa.getString("Nombre"));
            System.out.println("DIRECCION: " + empresa.getString("Direccion"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static JSONObject GetInfo(String ruc) throws IOException, JSONException {

        URL url = new URL("http://ruc.com.pe/api/v1/consultas");
        //HttpURLConnection conn= (HttpURLConnection) url.openConnection();
       // sslContext.init(null, null, new SecureRandom());
        URLConnection con = url.openConnection();
        Map<String,Object> params = new LinkedHashMap<>();
//        params.put("name", "Freddie the Fish");
//        params.put("email", "fishie@seamail.example.com");
//        params.put("reply_to_thread", 10394);
//        params.put("message", "Shark attacks in Botany Bay have gotten out of control. We need more defensive dolphins to protect the schools here, but Mayor Porpoise is too busy stuffing his snout with lobsters. He's so shellfish.");
        params.put("token","ef5e49a4-f461-4928-8b01-064e522a4a86-49610454-2d87-4152-bf66-2f7cd19f86c1");
        params.put("ruc","10178520739");
        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String,Object> param : params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }
        byte[] postDataBytes = postData.toString().getBytes("UTF-8");
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
       // HttpsURLConnection conn = (HttpsURLConnection)con;
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        //conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));	        
	       // conn.setRequestProperty("https.protocols", "TLSv1.2");
	        conn.setDoOutput(true);
	        conn.getOutputStream().write(postDataBytes);
	        Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
		    StringBuilder sbs = new StringBuilder();
	        for (int c; (c = in.read()) >= 0;)
	            sbs.append((char)c);
	        String response = sbs.toString();
	    InputStream is = conn.getInputStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }
            String jsonText = sb.toString();
            return new JSONObject(jsonText);
        } finally {
            is.close();
        }
    }
}