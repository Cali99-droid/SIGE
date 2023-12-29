package com.sige.rest.request;

import java.io.Serializable;


public class GrupoAulaVirtualReq implements Serializable{

	private String idClassroom;
	private String nombre;
	private String seccion;
	private String descripcion;
	private String idModerador;
	private String estado;
	private String code;
	private String[] errors;
	private String message;
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getSeccion() {
		return seccion;
	}
	public void setSeccion(String seccion) {
		this.seccion = seccion;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getIdModerador() {
		return idModerador;
	}
	public void setIdModerador(String idModerador) {
		this.idModerador = idModerador;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
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
	public String getIdClassroom() {
		return idClassroom;
	}
	public void setIdClassroom(String idClassroom) {
		this.idClassroom = idClassroom;
	}
}
