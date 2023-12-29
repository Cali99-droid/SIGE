package com.sige.rest.request;

import java.io.Serializable;

public class NotaAlumnoUpdateReq implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7986176126394050886L;
	private Integer nota;
	private Integer id;
	private Integer id_usr;
	private Integer id_ind;
	private Integer id_ne;
	private Integer id_alu;
	
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
	public Integer getId_ind() {
		return id_ind;
	}
	public void setId_ind(Integer id_ind) {
		this.id_ind = id_ind;
	}
	public Integer getId_ne() {
		return id_ne;
	}
	public void setId_ne(Integer id_ne) {
		this.id_ne = id_ne;
	}
	public Integer getId_alu() {
		return id_alu;
	}
	public void setId_alu(Integer id_alu) {
		this.id_alu = id_alu;
	}
	
	
}
