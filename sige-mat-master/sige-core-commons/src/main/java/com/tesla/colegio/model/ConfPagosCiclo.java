package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla fac_conf_pagos_ciclo
 * @author MV
 *
 */
public class ConfPagosCiclo extends EntidadBase{

	public final static String TABLA = "fac_conf_pagos_ciclo";
	private Integer id;
	private Integer id_cct;
	private java.math.BigDecimal costo;
	private Integer num_cuotas;
	private Ciclo ciclo;	

	public ConfPagosCiclo(){
	}

	/**
	* Obtiene Llave Principal 
	* @return id
	*/
	public Integer getId(){
		return id;
	}	

	/**
	* Llave Principal 
	* @param id
	*/
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	* Obtiene Ciclo 
	* @return id_cct
	*/
	public Integer getId_cct(){
		return id_cct;
	}	

	/**
	* Ciclo 
	* @param id_cct
	*/
	public void setId_cct(Integer id_cct) {
		this.id_cct = id_cct;
	}


	/**
	* Obtiene Nmero de Cuotas 
	* @return num_cuotas
	*/
	public Integer getNum_cuotas(){
		return num_cuotas;
	}	

	/**
	* Nmero de Cuotas 
	* @param num_cuotas
	*/
	public void setNum_cuotas(Integer num_cuotas) {
		this.num_cuotas = num_cuotas;
	}

	public Ciclo getCiclo(){
		return ciclo;
	}	

	public void setCiclo(Ciclo ciclo) {
		this.ciclo = ciclo;
	}

	public java.math.BigDecimal getCosto() {
		return costo;
	}

	public void setCosto(java.math.BigDecimal costo) {
		this.costo = costo;
	}
}