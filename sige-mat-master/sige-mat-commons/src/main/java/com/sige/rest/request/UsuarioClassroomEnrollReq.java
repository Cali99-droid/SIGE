package com.sige.rest.request;

import java.io.Serializable;
import java.util.Date; 

public class UsuarioClassroomEnrollReq implements Serializable{
 
	private static final long serialVersionUID = -4470656952360885399L;
	private String idUsuario;
	private String[] errors;
	private String message;
	public String getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(String idUsuario) {
		this.idUsuario = idUsuario;
	}
	@Override
	public String toString() {
		return "{idUsuario:" + idUsuario + "}";
	}
	public String[] getErrors() {
		return errors;
	}
	public void setErrors(String[] errors) {
		this.errors = errors;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
	
}
