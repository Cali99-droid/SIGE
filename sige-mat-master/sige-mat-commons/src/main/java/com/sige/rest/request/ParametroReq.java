package com.sige.rest.request;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ParametroReq implements Serializable{

	private Integer id_par;
	private String val;
	public Integer getId_par() {
		return id_par;
	}
	public void setId_par(Integer id_par) {
		this.id_par = id_par;
	}
	public String getVal() {
		return val;
	}
	public void setVal(String val) {
		this.val = val;
	}
	
}
