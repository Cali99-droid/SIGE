package com.sige.rest.request;

import java.io.Serializable;

public class PeriodosReq implements Serializable{
	
	private static final long serialVersionUID = -7090031095757893226L;
	
	private Integer id;
	private String nom_periodo;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNom_periodo() {
		return nom_periodo;
	}
	public void setNom_periodo(String nom_periodo) {
		this.nom_periodo = nom_periodo;
	}
	@Override
	public String toString() {
		return "{\"id\":\"" + id + "\", \"nom_periodo\":\"" + nom_periodo + "\"}";
	}
	
	
}
