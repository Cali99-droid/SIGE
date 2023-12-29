package com.tesla.colegio.model.bean;

import com.tesla.colegio.model.Nota;
import com.tesla.frmk.model.EntidadBase;

import java.math.BigDecimal;
import java.util.List;

/**
 * Clase entidad que representa a la tabla cat_grad
 * @author MV
 *
 */
public class FamiliarDeudaBean{

	public String getAulas() {
		return aulas;
	}

	public void setAulas(String aulas) {
		this.aulas = aulas;
	}

	public String getHijos() {
		return hijos;
	}

	public void setHijos(String hijos) {
		this.hijos = hijos;
	}

	public Integer getMes_deuda() {
		return mes_deuda;
	}

	public void setMes_deuda(Integer mes_deuda) {
		this.mes_deuda = mes_deuda;
	}

	private Integer  mes_deuda;
	private String hijos;
	private String aulas;

	public FamiliarDeudaBean(){
	}
	
}