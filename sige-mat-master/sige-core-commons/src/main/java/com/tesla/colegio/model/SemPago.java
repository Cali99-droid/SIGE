package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla col_sem_pago
 * @author MV
 *
 */
public class SemPago extends EntidadBase{

	public final static String TABLA = "col_sem_pago";
	private Integer id;
	private Integer id_semins;
	private java.math.BigDecimal monto;
	private SemInscripcion seminscripcion;	

	public SemPago(){
	}

	/**
	* Obtiene $field.description 
	* @return id
	*/
	public Integer getId(){
		return id;
	}	

	/**
	* $field.description 
	* @param id
	*/
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	* Obtiene Inscripcion 
	* @return id_semins
	*/
	public Integer getId_semins(){
		return id_semins;
	}	

	/**
	* Inscripcion 
	* @param id_semins
	*/
	public void setId_semins(Integer id_semins) {
		this.id_semins = id_semins;
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

	public SemInscripcion getSemInscripcion(){
		return seminscripcion;
	}	

	public void setSemInscripcion(SemInscripcion seminscripcion) {
		this.seminscripcion = seminscripcion;
	}
}