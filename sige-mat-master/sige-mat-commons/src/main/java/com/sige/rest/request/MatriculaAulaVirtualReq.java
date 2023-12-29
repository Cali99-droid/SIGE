package com.sige.rest.request;

import java.io.Serializable;
import java.util.Date; 

public class MatriculaAulaVirtualReq implements Serializable{
 
	private static final long serialVersionUID = -4470656952360885399L;
	private String correo;
	private String codigoCurso;
	public String getCorreo() {
		return correo;
	}
	public void setCorreo(String correo) {
		this.correo = correo;
	}
	public String getCodigoCurso() {
		return codigoCurso;
	}
	public void setCodigoCurso(String codigoCurso) {
		this.codigoCurso = codigoCurso;
	}

	
}
