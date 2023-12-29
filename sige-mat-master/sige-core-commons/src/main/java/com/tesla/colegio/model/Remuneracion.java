package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla cat_remuneracion
 * @author MV
 *
 */
public class Remuneracion extends EntidadBase{

	public final static String TABLA = "cat_remuneracion";
	private Integer id;
	private String nom;
	private String des;

	public Remuneracion(){
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
	* Obtiene Descripcion 
	* @return des
	*/
	public String getDes(){
		return des;
	}	

	/**
	* Descripcion 
	* @param des
	*/
	public void setDes(String des) {
		this.des = des;
	}

}