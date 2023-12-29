package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla cat_col_situacion
 * @author MV
 *
 */
public class ColSituacion extends EntidadBase{

	public final static String TABLA = "cat_col_situacion";
	private Integer id;
	private String cod;
	private String nom;
	private String des;

	public ColSituacion(){
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
	* Obtiene Cdigo 
	* @return cod
	*/
	public String getCod(){
		return cod;
	}	

	/**
	* Cdigo 
	* @param cod
	*/
	public void setCod(String cod) {
		this.cod = cod;
	}

	/**
	* Obtiene Situacin 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Situacin 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	* Obtiene Descripcin 
	* @return des
	*/
	public String getDes(){
		return des;
	}	

	/**
	* Descripcin 
	* @param des
	*/
	public void setDes(String des) {
		this.des = des;
	}

}