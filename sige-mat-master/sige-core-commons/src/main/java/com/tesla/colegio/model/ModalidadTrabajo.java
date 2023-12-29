package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla cat_modalidad_trabajo
 * @author MV
 *
 */
public class ModalidadTrabajo extends EntidadBase{

	public final static String TABLA = "cat_modalidad_trabajo";
	private Integer id;
	private String nom;
	private String des;

	public ModalidadTrabajo(){
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