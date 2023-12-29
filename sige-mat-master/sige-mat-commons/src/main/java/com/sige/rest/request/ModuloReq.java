package com.sige.rest.request;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ModuloReq implements Serializable{

	private Integer id_mod;
	
	private ParametroReq[] parametroReq;

	public Integer getId_mod() {
		return id_mod;
	}

	public void setId_mod(Integer id_mod) {
		this.id_mod = id_mod;
	}

	public ParametroReq[] getParametroReq() {
		return parametroReq;
	}

	public void setParametroReq(ParametroReq[] parametroReq) {
		this.parametroReq = parametroReq;
	}
	
	

	
}
