package com.sige.rest.request;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.tesla.colegio.model.CompetenciaDc; 

public class NotaConductalReq implements Serializable{
 
	private static final long serialVersionUID = -4470656952360885399L;
	private String nota_final_comp;
	private List<NotaConductalPeriodoReq> list_notas_comportamiento;
	
	public String getNota_final_comp() {
		return nota_final_comp;
	}
	public void setNota_final_comp(String nota_final_comp) {
		this.nota_final_comp = nota_final_comp;
	}
	public List<NotaConductalPeriodoReq> getList_notas_comportamiento() {
		return list_notas_comportamiento;
	}
	public void setList_notas_comportamiento(List<NotaConductalPeriodoReq> list_notas_comportamiento) {
		this.list_notas_comportamiento = list_notas_comportamiento;
	}
	@Override
	public String toString() {
		return "{\"nota_final_comp\":\"" + nota_final_comp + "\", \"list_notas_comportamiento\":"
				+ list_notas_comportamiento + "}";
	}

}
