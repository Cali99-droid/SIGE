package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla mat_desc_punt
 * @author MV
 *
 */
public class DescPunt extends EntidadBase{

	public final static String TABLA = "mat_desc_punt";
	private Integer id;
	private Integer id_per;
	private java.math.BigDecimal monto;
	private Periodo periodo;	

	public DescPunt(){
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
	* Obtiene Periodo Acadmico 
	* @return id_per
	*/
	public Integer getId_per(){
		return id_per;
	}	

	/**
	* Periodo Acadmico 
	* @param id_per
	*/
	public void setId_per(Integer id_per) {
		this.id_per = id_per;
	}

	/**
	* Obtiene Monto 
	* @return monto
	*/
	public java.math.BigDecimal getMonto(){
		return monto;
	}	

	/**
	* Monto 
	* @param monto
	*/
	public void setMonto(java.math.BigDecimal monto) {
		this.monto = monto;
	}

	public Periodo getPeriodo(){
		return periodo;
	}	

	public void setPeriodo(Periodo periodo) {
		this.periodo = periodo;
	}
}