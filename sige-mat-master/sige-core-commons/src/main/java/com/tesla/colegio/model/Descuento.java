package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla cat_descuento
 * @author MV
 *
 */
public class Descuento extends EntidadBase{

	public final static String TABLA = "cat_descuento";
	private Integer id;
	private String nom;
	private String des;

	public Descuento(){
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
	* Obtiene Descuento 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Descuento 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	* Obtiene Descripcin del descuento 
	* @return des
	*/
	public String getDes(){
		return des;
	}	

	/**
	* Descripcin del descuento 
	* @param des
	*/
	public void setDes(String des) {
		this.des = des;
	}

}