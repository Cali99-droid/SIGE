package com.sige.spring.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sun.corba.se.impl.orbutil.closure.Constant;
import com.tesla.colegio.util.Constante;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;

@Service
public class PagosVisa {
	
	public String getAuthotization() {
		return Base64.getEncoder().encodeToString((Constante.USER+":"+Constante.PASS).getBytes());
	}
	
	public String generarToken() throws IOException {
		
		String auth = getAuthotization();
				
		URL url = new URL(Constante.URL_API_AUTHORIZATION_DESA);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("Authorization", "Basic "+auth);
		//System.out.println("con: " + con.getResponseMessage());
		//System.out.println("con: " + auth);
		
		InputStreamReader in = new InputStreamReader(con.getInputStream());
		BufferedReader br = new BufferedReader(in);
		String output;
		StringBuffer buffer = new StringBuffer();
		while ((output = br.readLine()) != null) {
			buffer.append(output);
		}
		in.close();
		con.disconnect();
		
		return buffer.toString();
	}
	
	/*public String generarSession() throws IOException {
		
		String auth = getAuthotization();
				
		URL url = new URL(Constante.URL_API_AUTHORIZATION_DESA);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("Authorization", "Basic "+auth);
		//System.out.println("con: " + con.getResponseMessage());
		//System.out.println("con: " + auth);
		
		InputStreamReader in = new InputStreamReader(con.getInputStream());
		BufferedReader br = new BufferedReader(in);
		String output;
		StringBuffer buffer = new StringBuffer();
		while ((output = br.readLine()) != null) {
			buffer.append(output);
		}
		in.close();
		con.disconnect();
		
		return buffer.toString();
	}*/
	
	public Map<String, Object> generarSession(String url, String requestBody, String token) throws IOException {
		URL httpUrl = new URL(url);
		HttpURLConnection con = (HttpURLConnection) httpUrl.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type","application/json"); 
		con.setRequestProperty("Authorization", token);
		con.setDoOutput(true);
		
		try {
			byte[] outputInBytes = requestBody.getBytes("UTF-8");
			OutputStream os = con.getOutputStream();
			os.write(outputInBytes);    
			os.close();
			
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer buffer = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				buffer.append(inputLine);
			}
			in.close();
			
			Map<String, Object> map = new HashMap<>();
			map.put("status", con.getResponseCode());
			map.put("data", buffer.toString());
			map.put("error", false);
			//System.out.println("Sin Error");
			return map;
		} catch(Exception e) {
			InputStream errorstream = con.getErrorStream();
			String response = "";
			String line;
			BufferedReader br = new BufferedReader(new InputStreamReader(errorstream));
			while ((line = br.readLine()) != null) {
			    response += line;
			}
			Map<String, Object> map = new HashMap<>();
			map.put("status", con.getResponseCode());
			map.put("data", response);
			map.put("error", true);
			//System.out.println("Error: " + response);
			return map;
		}
		
	}
	
	public Map<String, Object> send(String url, String requestBody, String token) throws IOException {
		URL httpUrl = new URL(url);
		HttpURLConnection con = (HttpURLConnection) httpUrl.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type","application/json"); 
		con.setRequestProperty("Authorization", token);
		con.setDoOutput(true);
		
		try {
			byte[] outputInBytes = requestBody.getBytes("UTF-8");
			OutputStream os = con.getOutputStream();
			os.write(outputInBytes);    
			os.close();
			
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer buffer = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				buffer.append(inputLine);
			}
			in.close();
			
			Map<String, Object> map = new HashMap<>();
			map.put("status", con.getResponseCode());
			map.put("data", buffer.toString());
			map.put("error", false);
			//System.out.println("Sin Error");
			return map;
		} catch(Exception e) {
			InputStream errorstream = con.getErrorStream();
			String response = "";
			String line;
			BufferedReader br = new BufferedReader(new InputStreamReader(errorstream));
			while ((line = br.readLine()) != null) {
			    response += line;
			}
			Map<String, Object> map = new HashMap<>();
			map.put("status", con.getResponseCode());
			map.put("data", response);
			map.put("error", true);
			//System.out.println("Error: " + response);
			return map;
		}
		
	}
}
