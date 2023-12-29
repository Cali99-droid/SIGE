package com.sige.rest.request;

import java.io.Serializable;

public class HorarioReq implements Serializable{
 
	private static final long serialVersionUID = -5977577851069661370L;
	
	Integer dia;
	Integer id_cca; /*id de col_curso_anio*/
	Integer id;
	String hora_dia;
	String hora_fin;
	
	public Integer getDia() {
		return dia;
	}
	public void setDia(Integer dia) {
		this.dia = dia;
	}
	public String getHora_dia() {
		return hora_dia;
	}
	public void setHora_dia(String hora_dia) {
		this.hora_dia = hora_dia;
	}
	public String getHora_fin() {
		return hora_fin;
	}
	public void setHora_fin(String hora_fin) {
		this.hora_fin = hora_fin;
	}
	public Integer getId_cca() {
		return id_cca;
	}
	public void setId_cca(Integer id_cca) {
		this.id_cca = id_cca;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	
	
}
