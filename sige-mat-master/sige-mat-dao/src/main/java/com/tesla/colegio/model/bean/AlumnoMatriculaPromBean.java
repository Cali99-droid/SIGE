package com.tesla.colegio.model.bean;

import java.math.BigDecimal;

public class AlumnoMatriculaPromBean {
	Integer id_mat;
	BigDecimal promedio;
	Integer puesto;
	String nombres;
	
	public AlumnoMatriculaPromBean(Integer id_mat,String nombres, BigDecimal promedio){
		this.id_mat = id_mat;
		this.promedio = promedio;
		this.nombres = nombres;
	}
	public Integer getId_mat() {
		return id_mat;
	}
	public void setId_mat(Integer id_mat) {
		this.id_mat = id_mat;
	}
	public BigDecimal getPromedio() {
		return promedio;
	}
	public void setPromedio(BigDecimal promedio) {
		this.promedio = promedio;
	}
	public Integer getPuesto() {
		return puesto;
	}
	public void setPuesto(Integer puesto) {
		this.puesto = puesto;
	}

	public String getNombres() {
		return nombres;
	}
	public void setNombres(String nombres) {
		this.nombres = nombres;
	}
	
	@Override
	public String toString(){
		return "alumno:" + nombres + ", promedio:" + promedio + ", puesto:" + puesto;
	}
}
