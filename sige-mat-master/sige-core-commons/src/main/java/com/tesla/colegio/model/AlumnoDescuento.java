package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla fac_alumno_descuento
 * @author MV
 *
 */
public class AlumnoDescuento extends EntidadBase{

	public final static String TABLA = "fac_alumno_descuento";
	private Integer id;
	private Integer id_mat;
	private Integer id_fdes;
	private java.math.BigDecimal mensualidad;
	private java.math.BigDecimal mensualidad_bco;
	private java.math.BigDecimal descuento;
	private Matricula matricula;	
	private String motivo;
	
	public AlumnoDescuento(){
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
	* Obtiene Matrcula 
	* @return id_mat
	*/
	public Integer getId_mat(){
		return id_mat;
	}	

	/**
	* Matrcula 
	* @param id_mat
	*/
	public void setId_mat(Integer id_mat) {
		this.id_mat = id_mat;
	}

	/**
	* Obtiene Descuento 
	* @return descuento
	*/
	public java.math.BigDecimal getDescuento(){
		return descuento;
	}	

	/**
	* Descuento 
	* @param descuento
	*/
	public void setDescuento(java.math.BigDecimal descuento) {
		this.descuento = descuento;
	}

	public Matricula getMatricula(){
		return matricula;
	}	

	public void setMatricula(Matricula matricula) {
		this.matricula = matricula;
	}

	public java.math.BigDecimal getMensualidad() {
		return mensualidad;
	}

	public void setMensualidad(java.math.BigDecimal mensualidad) {
		this.mensualidad = mensualidad;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public java.math.BigDecimal getMensualidad_bco() {
		return mensualidad_bco;
	}

	public void setMensualidad_bco(java.math.BigDecimal mensualidad_bco) {
		this.mensualidad_bco = mensualidad_bco;
	}

	public Integer getId_fdes() {
		return id_fdes;
	}

	public void setId_fdes(Integer id_fdes) {
		this.id_fdes = id_fdes;
	}
	
	
}