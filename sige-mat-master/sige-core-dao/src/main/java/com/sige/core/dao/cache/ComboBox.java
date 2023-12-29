package com.sige.core.dao.cache;

import java.io.Serializable;

public class ComboBox implements Serializable {
	
	private static final long serialVersionUID = 6694702365310438790L;
	Integer id;
	String value;
	String descripcion;	
	String aux1;
	String aux2;
	String aux3;
	String aux4;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getAux1() {
		return aux1;
	}
	public void setAux1(String aux1) {
		this.aux1 = aux1;
	}

	public String getAux2() {
		return aux2;
	}
	public void setAux2(String aux2) {
		this.aux2 = aux2;
	}
	public String getAux3() {
		return aux3;
	}
	public void setAux3(String aux3) {
		this.aux3 = aux3;
	}
	public String getAux4() {
		return aux4;
	}
	public void setAux4(String aux4) {
		this.aux4 = aux4;
	}

}
