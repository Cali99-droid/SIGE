package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla cat_regimen_laboral
 * @author MV
 *
 */
public class RegimenLaboral extends EntidadBase{

	public final static String TABLA = "cat_regimen_laboral";
	private Integer id;
	private String nom;
	private String des;

	public RegimenLaboral(){
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
	* Obtiene Rgimen laboral 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Rgimen laboral 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	* Obtiene Descripcin del rgimen 
	* @return des
	*/
	public String getDes(){
		return des;
	}	

	/**
	* Descripcin del rgimen 
	* @param des
	*/
	public void setDes(String des) {
		this.des = des;
	}

}