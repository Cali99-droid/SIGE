package com.tesla.frmk.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.LoggerFactory; 
 

public class JsonUtil {
    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(JsonUtil.class);
	public static void main(String args[]) {
		

	}
	
	
	public static String toJson(Map<String,Object> map) {
		StringBuilder json = new StringBuilder("{");

		
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			Object value = entry.getValue();
			String key = entry.getKey();
			
			if (value!=null) {
				json.append("\"" ).append(key).append("\":\"").append(value).append("\",");
			}
			
		}
		if(json.length()>1)
			json.deleteCharAt(json.length()-1);
		json.append("}");

		return json.toString();
	}
	
	public static String toJson(Object object) {
		
		StringBuilder json = new StringBuilder("{");
		
		Map<String, String> properties;
		try {
			properties = BeanUtils.describe(object);
		} catch (Exception e) {
			e.printStackTrace();
			return null;

		}
		
		for (Map.Entry<String, String> entry : properties.entrySet()) {
			String value = entry.getValue();
			String key = entry.getKey();
			
			if (value!=null) {
				json.append("\"" ).append(key).append("\":\"").append(value).append("\",");
			}
			
		}
		if(json.length()>1)
		json.deleteCharAt(json.length()-1);
		json.append("}");

		return json.toString();
	}
	
	public static <T> String toJsonListMap(List<T> lista) {

		StringBuilder jsonArray = new StringBuilder("[");

		for (Object object : lista) {
			StringBuilder json = new StringBuilder("{");
/*
			if(object instanceof HashMap<?, ?>){
				Map<String, Object> map = (HashMap<String, Object>)object;
				for (String val : map.keySet()) {
					
				}
			}else{aa un rat
				*/
			
				Map<String, Object> properties = (HashMap<String, Object>)object;
				try {
					if(object instanceof HashMap<?, ?>)
						properties = (HashMap<String, Object>)object;
					//else 
					//	properties = BeanUtils.describe(object);
				} catch (Exception e) {
					e.printStackTrace();
					return null;

				}
				
				for (Map.Entry<String, Object> entry : properties.entrySet()) {
					Object value = entry.getValue();
					String key = entry.getKey();
					
					if (value!=null) {
						json.append("\"" ).append(key).append("\":\"").append(value).append("\",");
					}
					
	//			}
				
			
				
			}
				json.deleteCharAt(json.length()-1);
				json.append("},");
				jsonArray.append(json);
		}
		if(jsonArray.length()>1)
		jsonArray.deleteCharAt(jsonArray.length()-1);
		jsonArray.append("]");
		

		return jsonArray.toString();
	}
	
	public static <T> String toJsonList(List<T> lista) {

		StringBuilder jsonArray = new StringBuilder("[");

		for (Object object : lista) {
			StringBuilder json = new StringBuilder("{");
				
			Map<String, String> properties;
			try {
				properties = BeanUtils.describe(object);
			} catch (Exception e) {
				e.printStackTrace();
				return null;

			}
			
			for (Map.Entry<String, String> entry : properties.entrySet()) {
				String value = entry.getValue();
				String key = entry.getKey();
				
				if (value!=null) {
					json.append("\"" ).append(key).append("\":\"").append(value).append("\",");
				}
				
			}
			
			json.deleteCharAt(json.length()-1);
			json.append("},");
			jsonArray.append(json);
		}
		if(jsonArray.length()>1)
		jsonArray.deleteCharAt(jsonArray.length()-1);
		jsonArray.append("]");
		

		return jsonArray.toString();
	}
}
