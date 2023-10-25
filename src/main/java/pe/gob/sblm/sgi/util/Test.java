package pe.gob.sblm.sgi.util;

import java.io.*;
import java.net.*;
import java.security.SecureRandom;
import java.util.*;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

	class Test {
	    public static void main(String[] args) throws Exception {
	        //URL url = new URL("http://example.net/new-message.php");
	        URL url = new URL("https://ruc.com.pe/api/v1/consultas");
	        SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
	        sslContext.init(null, null, new SecureRandom());
	        URLConnection con = url.openConnection();
	        Map<String,Object> params = new LinkedHashMap<>();
//	        params.put("name", "Freddie the Fish");
//	        params.put("email", "fishie@seamail.example.com");
//	        params.put("reply_to_thread", 10394);
//	        params.put("message", "Shark attacks in Botany Bay have gotten out of control. We need more defensive dolphins to protect the schools here, but Mayor Porpoise is too busy stuffing his snout with lobsters. He's so shellfish.");
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
	        //HttpURLConnection conn = (HttpURLConnection)url.openConnection();
	        javax.net.ssl.HttpsURLConnection conn = (javax.net.ssl.HttpsURLConnection)con;
	        conn.setRequestMethod("POST");
	        conn.setSSLSocketFactory(sslContext.getSocketFactory());
	        //conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	        conn.setRequestProperty("Content-Type", "application/json");
	        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));	        
	       // conn.setRequestProperty("https.protocols", "TLSv1.2");
	        conn.setDoOutput(true);
	        conn.getOutputStream().write(postDataBytes);

	        Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

//	        for (int c; (c = in.read()) >= 0;)
//	            System.out.print((char)c);
	        StringBuilder sb = new StringBuilder();
	        for (int c; (c = in.read()) >= 0;)
	            sb.append((char)c);
	        String response = sb.toString();
	    }
	}

