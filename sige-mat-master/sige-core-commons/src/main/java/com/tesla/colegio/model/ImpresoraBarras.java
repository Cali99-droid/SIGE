package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla asi_impresora_barras
 * @author MV
 *
 */
public class ImpresoraBarras extends EntidadBase{

	public final static String TABLA = "asi_impresora_barras";
	private Integer id;
	private String tipo;
	private java.math.BigDecimal ancho;
	private java.math.BigDecimal tam_letra;
	private java.math.BigDecimal mar_izq;
	private java.math.BigDecimal mar_der;
	private java.math.BigDecimal mar_sup;
	private java.math.BigDecimal mar_inf;
	private java.math.BigDecimal texto_relleno;
	private java.math.BigDecimal barra_relleno;

	public ImpresoraBarras(){
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
	* Obtiene ATipo de codificacin 
	* @return tipo
	*/
	public String getTipo(){
		return tipo;
	}	

	/**
	* ATipo de codificacin 
	* @param tipo
	*/
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	/**
	* Obtiene Ancho del cdigo de barras 
	* @return ancho
	*/
	public java.math.BigDecimal getAncho(){
		return ancho;
	}	

	/**
	* Ancho del cdigo de barras 
	* @param ancho
	*/
	public void setAncho(java.math.BigDecimal ancho) {
		this.ancho = ancho;
	}

	/**
	* Obtiene Tamao de la letra 
	* @return tam_letra
	*/
	public java.math.BigDecimal getTam_letra(){
		return tam_letra;
	}	

	/**
	* Tamao de la letra 
	* @param tam_letra
	*/
	public void setTam_letra(java.math.BigDecimal tam_letra) {
		this.tam_letra = tam_letra;
	}

	/**
	* Obtiene Margen izquierdo 
	* @return mar_izq
	*/
	public java.math.BigDecimal getMar_izq(){
		return mar_izq;
	}	

	/**
	* Margen izquierdo 
	* @param mar_izq
	*/
	public void setMar_izq(java.math.BigDecimal mar_izq) {
		this.mar_izq = mar_izq;
	}

	/**
	* Obtiene Margen derecho 
	* @return mar_der
	*/
	public java.math.BigDecimal getMar_der(){
		return mar_der;
	}	

	/**
	* Margen derecho 
	* @param mar_der
	*/
	public void setMar_der(java.math.BigDecimal mar_der) {
		this.mar_der = mar_der;
	}

	/**
	* Obtiene Margen superior 
	* @return mar_sup
	*/
	public java.math.BigDecimal getMar_sup(){
		return mar_sup;
	}	

	/**
	* Margen superior 
	* @param mar_sup
	*/
	public void setMar_sup(java.math.BigDecimal mar_sup) {
		this.mar_sup = mar_sup;
	}

	/**
	* Obtiene Margen inferior 
	* @return mar_inf
	*/
	public java.math.BigDecimal getMar_inf(){
		return mar_inf;
	}	

	/**
	* Margen inferior 
	* @param mar_inf
	*/
	public void setMar_inf(java.math.BigDecimal mar_inf) {
		this.mar_inf = mar_inf;
	}

	/**
	* Obtiene Margen inferior 
	* @return texto_relleno
	*/
	public java.math.BigDecimal getTexto_relleno(){
		return texto_relleno;
	}	

	/**
	* Margen inferior 
	* @param texto_relleno
	*/
	public void setTexto_relleno(java.math.BigDecimal texto_relleno) {
		this.texto_relleno = texto_relleno;
	}

	/**
	* Obtiene Margen inferior 
	* @return barra_relleno
	*/
	public java.math.BigDecimal getBarra_relleno(){
		return barra_relleno;
	}	

	/**
	* Margen inferior 
	* @param barra_relleno
	*/
	public void setBarra_relleno(java.math.BigDecimal barra_relleno) {
		this.barra_relleno = barra_relleno;
	}

}