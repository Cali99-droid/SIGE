package com.tesla.frmk.sql;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class Param extends HashMap<String, Object>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1567381564811151852L;
	private  Map<String, Object> rowValue= new HashMap<String, Object>();

	//public Row(HashMap<String, Object> map){
	//	this.rowValue=map;
	//}
	public Param(){
		super();
	}
	public Param(HashMap<? extends String, ? extends Object> t) {
		super(t);
	}
	
	public static Param toParam(String alias, Object object){
		Field[] fields = object.getClass().getDeclaredFields();
		Param param = new Param();
		try{
		for (int i=0; i<fields.length ;i++)
		{
			fields[i].setAccessible(true);
			String name=fields[i].getName();
			Object value = fields[i].get(object);
			if (value!=null)
				param.put(alias + "." + name, value );
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		return param;
	}
	
	public Param(String key, Object value){
		super.put(key, value);
		this.rowValue.put(key, value);
	}
	
	public Object get(String key){
		return this.rowValue.get(key);
	}
	
	public void set( Map<String,Object> map){
		this.rowValue = map;
	}
	
	@Override
	public Object put( String key, Object value){
		super.put(key, value);
		return this.rowValue.put(key, value);
	}
	
}
