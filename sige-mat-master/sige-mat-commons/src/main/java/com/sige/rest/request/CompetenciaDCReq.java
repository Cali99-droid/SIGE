package com.sige.rest.request;

import java.io.Serializable;
import java.util.Date;
import java.util.List; 

public class CompetenciaDCReq implements Serializable{
 
	private static final long serialVersionUID = -4470656952360885399L;
	private Integer id;
	private Integer id_area;
	private String nom;
	private String nota_final;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getId_area() {
		return id_area;
	}
	public void setId_area(Integer id_area) {
		this.id_area = id_area;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getNota_final() {
		return nota_final;
	}
	public void setNota_final(String nota_final) {
		this.nota_final = nota_final;
	}
	@Override
	public String toString() {
		return " {\"id\":\"" + id + "\", \"id_area\":\"" + id_area + "\", \"nom\":\"" + nom + "\", \"nota_final\":\"" + nota_final
				+ "\"}";
	}
	
}
