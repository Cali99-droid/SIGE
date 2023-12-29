package com.tesla.colegio.model.bean;

import java.util.List;

public class FormGradoCapacidad {
	public List<GradoCapacidad> getGradoCapacidades() {
		return gradoCapacidades;
	}

	public void setGradoCapacidades(List<GradoCapacidad> gradoCapacidades) {
		this.gradoCapacidades = gradoCapacidades;
	}

	List<GradoCapacidad> gradoCapacidades;
}

//een tu controller, en lugar de llamar a GradoCapacidad, llaa FormGradoCapacidad.. ?