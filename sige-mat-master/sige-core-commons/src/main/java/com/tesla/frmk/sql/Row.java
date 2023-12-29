package com.tesla.frmk.sql;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Row extends HashMap<String, Object>{

	private static final long serialVersionUID = -8128230713930570061L;
	private  Map<String, Object> rowValue= new HashMap<String, Object>();

	//public Row(HashMap<String, Object> map){
	//	this.rowValue=map;
	//}
	public Row(){
		super();
	}
	public Row(HashMap<? extends String, ? extends Object> t) {
		super(t);
	}
	
	
	public Row(String key, Object value){
		this.rowValue.put(key, value);
	}
	
	public Object get(String key){
		return this.rowValue.get(key);
	}
	
	public Object put(String key,Object value){
		super.put(key, value);
		return this.rowValue.put(key, value);
	}
	public int getInt(String key){
		Object value = this.rowValue.get(key);
		return  (value!=null)?Integer.parseInt(value.toString()):null;
	}
	
	public Integer getInteger(String key){
		Object value = this.rowValue.get(key);
		return  (value!=null)?Integer.parseInt(value.toString()):null;
	}
	public Long  getLong(String key){//todavia nadie lo usa... REVISAR Y CORREGIR
		Object value = this.rowValue.get(key);
		return  (value!=null)?Long.parseLong(String.valueOf(value)):null;
	}
	public BigDecimal getBigDecimal(String key){
		Object value = this.rowValue.get(key);
		return  (value!=null)?(BigDecimal)value:null;
	}
	public Double getDouble(String key){
		Object value = this.rowValue.get(key);
		return  (value!=null)?(Double)value:null;
	}
	public String getString(String key){
		if (this.rowValue.get(key)==null)
			return null;
		return  this.rowValue.get(key).toString();
	}
	
	public Date getDate(String key){
		if (this.rowValue.get(key)==null)
			return null;
		return  (Date)this.rowValue.get(key);
	}
	
	public void set( Map<String,Object> map){
		this.rowValue = map;
	}
	
	
}
