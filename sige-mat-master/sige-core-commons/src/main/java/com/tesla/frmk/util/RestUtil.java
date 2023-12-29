package com.tesla.frmk.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.JsonObject;
import com.itextpdf.text.pdf.codec.Base64.InputStream;

public class RestUtil {

	/**
	 * 
	 * @param uri URL COMPLETO
	 * @param method POST O GET
	 * @param json CUERPO STRING JSON
	 * @return
	 * @throws Exception
	 */
	public Object requestPOST(String  uri,String method,String json) throws Exception {

		//Object  json= null;
		// String json = "{\"key\":1}";
		try {

			URL url = new URL(uri);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(method);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			/*
			String requestProperty = restDto.getRequest_property();
			if (requestProperty != null)
				conn.setRequestProperty("Accept", requestProperty);
*/
			// parametros form
			if (json!= null) {
				OutputStream os = conn.getOutputStream();
				//BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));
				//writer.write(params);
				 os.write(json.getBytes("UTF-8"));//MANDAME ELEJEMPLO PLEASE
				// JSONObject jsonObject = new JSONObject(result);// eso esra en el ejemplo? era asi, loq pasa q en el ejemplo le pasa String en json al final lo convierte a object
		         os.close();
			}

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP Error code : " + conn.getResponseCode());
			}
			
			InputStreamReader in = new InputStreamReader(conn.getInputStream());
			BufferedReader br = new BufferedReader(in);
			String output;
			String rpta = "";
			while ((output = br.readLine()) != null) {

				if (output != null)
					rpta += output;
			}

			 

			conn.disconnect();

			return rpta;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return json;
	}

	/*private JsonObject uploadToServer(String  uri,String method,String json) throws IOException, JSONExcepti {
	            String query = "https://example.com";
	            String json = "{\"key\":1}";

	            URL url = new URL(query);
	            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	            conn.setConnectTimeout(5000);
	            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
	            conn.setDoOutput(true);
	            conn.setDoInput(true);
	            conn.setRequestMethod("POST");

	            OutputStream os = conn.getOutputStream();
	            os.write(json.getBytes("UTF-8"));
	            os.close();

	            // read the response
	            InputStream in = new BufferedInputStream(conn.getInputStream());
	            String result = org.apache.commons.io.IOUtils.toString(in, "UTF-8");
	            JSONObject jsonObject = new JSONObject(result);


	            in.close();
	            conn.disconnect();

	            return jsonObject;
	    }
*/
	
}
