package com.sige.rest.request;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.tesla.colegio.model.CompetenciaDc; 

public class TardanzaPeriodoReq implements Serializable{
 
	private static final long serialVersionUID = -4470656952360885399L;
	private String periodo;
	private Integer cant_tar_injustificada;
	private Integer cant_tar_justificada;
	private Integer cant_falta_injustificada;
	private Integer cant_falta_justificada;
	public String getPeriodo() {
		return periodo;
	}
	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}
	public Integer getCant_tar_injustificada() {
		return cant_tar_injustificada;
	}
	public void setCant_tar_injustificada(Integer cant_tar_injustificada) {
		this.cant_tar_injustificada = cant_tar_injustificada;
	}
	public Integer getCant_tar_justificada() {
		return cant_tar_justificada;
	}
	public void setCant_tar_justificada(Integer cant_tar_justificada) {
		this.cant_tar_justificada = cant_tar_justificada;
	}
	public Integer getCant_falta_injustificada() {
		return cant_falta_injustificada;
	}
	public void setCant_falta_injustificada(Integer cant_falta_injustificada) {
		this.cant_falta_injustificada = cant_falta_injustificada;
	}
	public Integer getCant_falta_justificada() {
		return cant_falta_justificada;
	}
	public void setCant_falta_justificada(Integer cant_falta_justificada) {
		this.cant_falta_justificada = cant_falta_justificada;
	}
	@Override
	public String toString() {
		return "[{\"periodo\":\"" + periodo + "\", \"cant_tar_injustificada\":\"" + cant_tar_injustificada
				+ "\", \"cant_tar_justificada\":\"" + cant_tar_justificada + "\", \"cant_falta_injustificada\":\""
				+ cant_falta_injustificada + "\", \"cant_falta_justificada\":\"" + cant_falta_justificada + "\"}]";
	}
	
	
}
