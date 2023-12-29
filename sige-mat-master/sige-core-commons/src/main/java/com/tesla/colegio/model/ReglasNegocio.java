package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla ges_reglas_negocio
 * @author MV
 *
 */
public class ReglasNegocio extends EntidadBase{

	public final static String TABLA = "ges_reglas_negocio";
	private Integer id;
	private Integer id_emp;
	private Integer id_mod;
	private String nom;
	private String cod;
	private String tipo_html;
	private String val;
	private Empresa empresa;	
	private Modulo modulo;	

	public ReglasNegocio(){
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
	* Obtiene Empresa 
	* @return id_emp
	*/
	public Integer getId_emp(){
		return id_emp;
	}	

	/**
	* Empresa 
	* @param id_emp
	*/
	public void setId_emp(Integer id_emp) {
		this.id_emp = id_emp;
	}

	/**
	* Obtiene Modulo 
	* @return id_mod
	*/
	public Integer getId_mod(){
		return id_mod;
	}	

	/**
	* Modulo 
	* @param id_mod
	*/
	public void setId_mod(Integer id_mod) {
		this.id_mod = id_mod;
	}

	/**
	* Obtiene Nombre 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Nombre 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	* Obtiene Codigo 
	* @return cod
	*/
	public String getCod(){
		return cod;
	}	

	/**
	* Codigo 
	* @param cod
	*/
	public void setCod(String cod) {
		this.cod = cod;
	}

	/**
	* Obtiene Tipo Html 
	* @return tipo_html
	*/
	public String getTipo_html(){
		return tipo_html;
	}	

	/**
	* Tipo Html 
	* @param tipo_html
	*/
	public void setTipo_html(String tipo_html) {
		this.tipo_html = tipo_html;
	}

	/**
	* Obtiene Valor 
	* @return val
	*/
	public String getVal(){
		return val;
	}	

	/**
	* Valor 
	* @param val
	*/
	public void setVal(String val) {
		this.val = val;
	}

	public Empresa getEmpresa(){
		return empresa;
	}	

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
	public Modulo getModulo(){
		return modulo;
	}	

	public void setModulo(Modulo modulo) {
		this.modulo = modulo;
	}
}