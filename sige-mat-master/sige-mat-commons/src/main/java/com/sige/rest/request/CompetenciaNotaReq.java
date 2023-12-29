package com.sige.rest.request;

import java.io.Serializable;
import java.util.Date;
import java.util.List; 

public class CompetenciaNotaReq implements Serializable{
 
	private static final long serialVersionUID = -4470656952360885399L;
	private Integer id;
	private String nombre;
	private String nota;
	private String periodo;
	private Integer id_dcare;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getNota() {
		return nota;
	}
	public void setNota(String nota) {
		this.nota = nota;
	}
	public String getPeriodo() {
		return periodo;
	}
	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}
	public Integer getId_dcare() {
		return id_dcare;
	}
	public void setId_dcare(Integer id_dcare) {
		this.id_dcare = id_dcare;
	}
	@Override
	public String toString() {
		return "{\"id\":\"" + id + "\", \"nombre\":\"" + nombre + "\", \"nota\":\"" + nota + "\", \"periodo\":\"" + periodo
				+ "\", \"id_dcare\":\"" + id_dcare + "\"}";
	}
		
}
