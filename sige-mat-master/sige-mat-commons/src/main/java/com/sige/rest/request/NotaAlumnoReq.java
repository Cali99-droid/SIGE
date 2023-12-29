package com.sige.rest.request;

import java.io.Serializable;

@SuppressWarnings("serial")
public class NotaAlumnoReq implements Serializable{
	
	private Integer id_alu;
	Integer[] notas;
	
	
	public Integer getId_alu() {
		return id_alu;
	}
	public void setId_alu(Integer id_alu) {
		this.id_alu = id_alu;
	}
	public Integer[] getNotas() {
		return notas;
	}
	public void setNotas(Integer[] notas) {
		this.notas = notas;
	}


	
}
