package com.tesla.colegio.model.bean;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;

public class PromedioBean {
	private Integer nro;
	private String curso;
	private String periodo1 = null;
	private String periodo2= null;
	private String periodo3= null;
	private String periodo4= null;
	private String promedio= "";
	
	
	public String getPeriodo1() {
		return periodo1;
	}
	public void setPeriodo1(String periodo1) {
		this.periodo1 = periodo1;
	}
	public String getPeriodo2() {
		return periodo2;
	}
	public void setPeriodo2(String periodo2) {
		this.periodo2 = periodo2;
	}
	public String getPeriodo3() {
		return periodo3;
	}
	public void setPeriodo3(String periodo3) {
		this.periodo3 = periodo3;
	}
	public String getPeriodo4() {
		return periodo4;
	}
	public void setPeriodo4(String periodo4) {
		this.periodo4 = periodo4;
	}
	public Integer getNro() {
		return nro;
	}
	public void setNro(Integer nro) {
		this.nro = nro;
	}
	public String getCurso() {
		return curso;
	}
	public void setCurso(String curso) {
		this.curso = curso;
	}
	public String getPromedio() {
		return promedio;
	}
	public void setPromedio(String promedio) {
		this.promedio = promedio;
	}
	@Override
	public String toString() {
		return "PromedioBean [nro=" + nro + ", curso=" + curso + ", periodo1=" + periodo1 + ", periodo2=" + periodo2
				+ ", periodo3=" + periodo3 + ", periodo4=" + periodo4 + ", promedio=" + promedio + "]";
	}
	


	
}
