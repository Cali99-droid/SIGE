package com.sige.rest.request;

import java.io.Serializable;

@SuppressWarnings("serial")
public class NotaComAlumnoUpdateReq implements Serializable{
	
	private Integer nota;
	private Integer id;
	private Integer id_usr;
	public Integer getNota() {
		return nota;
	}
	public void setNota(Integer nota) {
		this.nota = nota;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getId_usr() {
		return id_usr;
	}
	public void setId_usr(Integer id_usr) {
		this.id_usr = id_usr;
	}
	
	
}
