package com.sige.rest.request;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class RolOpcionRequest implements Serializable{

	private Integer id;
	private List<Integer> opciones;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	 
	
	
	
	public List<Integer> getOpciones() {
		return opciones;
	}
	public void setOpciones(List<Integer> opciones) {
		this.opciones = opciones;
	}
	@Override
	public String toString() {
		return "RolOpcionRequest [id=" + id + ", opciones=" + opciones + "]";
	}
	
	
	
}
