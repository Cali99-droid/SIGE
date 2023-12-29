package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla mod_parametro
 * @author MV
 *
 */
public class Parametro extends EntidadBase{

	public final static String TABLA = "mod_parametro";
	private Integer id;
	private Integer id_mod;
	private String tipo_html;
	private String nom;
	private String des;
	private String val;
	private Modulo modulo;	

	public Parametro(){
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
	* Obtiene Nombre del perfil 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Nombre del perfil 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	* Obtiene Descripci&oacute;n del par&aacute;metro 
	* @return des
	*/
	public String getDes(){
		return des;
	}	

	/**
	* Descripci&oacute;n del par&aacute;metro 
	* @param des
	*/
	public void setDes(String des) {
		this.des = des;
	}

	/**
	* Obtiene Valor del par&aacute;metro 
	* @return val
	*/
	public String getVal(){
		return val;
	}	

	/**
	* Valor del par&aacute;metro 
	* @param val
	*/
	public void setVal(String val) {
		this.val = val;
	}

	public Modulo getModulo(){
		return modulo;
	}	

	public void setModulo(Modulo modulo) {
		this.modulo = modulo;
	}

	public String getTipo_html() {
		return tipo_html;
	}

	public void setTipo_html(String tipo_html) {
		this.tipo_html = tipo_html;
	}
}