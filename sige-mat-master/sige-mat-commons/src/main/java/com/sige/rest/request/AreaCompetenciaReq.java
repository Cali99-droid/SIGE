package com.sige.rest.request;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.tesla.colegio.model.CompetenciaDc; 

public class AreaCompetenciaReq implements Serializable{
 
	private static final long serialVersionUID = -4470656952360885399L;
	private Integer id;
	private String nom_area;
	private List<CompetenciaDCReq> competencias;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNom_area() {
		return nom_area;
	}
	public void setNom_area(String nom_area) {
		this.nom_area = nom_area;
	}
	public List<CompetenciaDCReq> getCompetencias() {
		return competencias;
	}
	public void setCompetencias(List<CompetenciaDCReq> competencias) {
		this.competencias = competencias;
	}
	@Override
	public String toString() {
		return "{\"id\":\"" + id + "\", \"nom_area\":\"" + nom_area + "\", \"competencias\":" + competencias + "}";
	}
	
}
