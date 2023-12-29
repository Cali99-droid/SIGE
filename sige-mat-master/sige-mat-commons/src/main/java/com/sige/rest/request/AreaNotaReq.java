package com.sige.rest.request;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.tesla.colegio.model.CompetenciaDc; 

public class AreaNotaReq implements Serializable{
 
	private static final long serialVersionUID = -4470656952360885399L;
	private Integer id;
	private String nom_area;
	private Integer prom_area;
	private Integer eva_recu;
	private String val_area;
	private List<CompetenciaNotaReq> competencia_notas;
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
	public Integer getProm_area() {
		return prom_area;
	}
	public void setProm_area(Integer prom_area) {
		this.prom_area = prom_area;
	}
	public Integer getEva_recu() {
		return eva_recu;
	}
	public void setEva_recu(Integer eva_recu) {
		this.eva_recu = eva_recu;
	}
	public String getVal_area() {
		return val_area;
	}
	public void setVal_area(String val_area) {
		this.val_area = val_area;
	}
	public List<CompetenciaNotaReq> getCompetencia_notas() {
		return competencia_notas;
	}
	public void setCompetencia_notas(List<CompetenciaNotaReq> competencia_notas) {
		this.competencia_notas = competencia_notas;
	}
	@Override
	public String toString() {
		return "{\"id\":\"" + id + "\", \"nom_area\":\"" + nom_area + "\", \"prom_area\":\"" + prom_area + "\", \"eva_recu\":\""
				+ eva_recu + "\", \"val_area\":\"" + val_area + "\", \"competencia_notas\":" + competencia_notas + "}";
	}
}
