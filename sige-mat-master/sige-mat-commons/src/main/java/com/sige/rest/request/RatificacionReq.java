package com.sige.rest.request;

import java.io.Serializable;


public class RatificacionReq implements Serializable{

	private static final long serialVersionUID = -1182003383085892015L;
	private Integer id_mats_alu[];
	private String resp_alu[];
	private Integer id_anio;
	public Integer[] getId_mats_alu() {
		return id_mats_alu;
	}
	public void setId_mats_alu(Integer[] id_mats_alu) {
		this.id_mats_alu = id_mats_alu;
	}
	public String[] getResp_alu() {
		return resp_alu;
	}
	public void setResp_alu(String[] resp_alu) {
		this.resp_alu = resp_alu;
	}
	public Integer getId_anio() {
		return id_anio;
	}
	public void setId_anio(Integer id_anio) {
		this.id_anio = id_anio;
	}

}
