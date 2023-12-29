package com.sige.rest.request;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.tesla.colegio.model.CompetenciaDc; 

public class NotaConductalPeriodoReq implements Serializable{
 
	private static final long serialVersionUID = -4470656952360885399L;
	private String periodo;
	private String nota_comp;
	public String getPeriodo() {
		return periodo;
	}
	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}
	public String getNota_comp() {
		return nota_comp;
	}
	public void setNota_comp(String nota_comp) {
		this.nota_comp = nota_comp;
	}
	@Override
	public String toString() {
		return "[{\"periodo\":\"" + periodo + "\", \"nota_comp\":\"" + nota_comp + "\"}]";
	}	
}
