package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla cat_gen
 * @author MV
 *
 */
public class Gen extends EntidadBase{
	
	private int id;
	private String nom;

	public Gen(){
	}

	/**
	* Obtiene $field.description 
	* @return id
	*/
	public int getId(){
		return id;
	}	

	/**
	* $field.description 
	* @param id
	*/
	public void setId(int id) {
		this.id = id;
	}

	/**
	* Obtiene G?nero 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* G?nero 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}


}

