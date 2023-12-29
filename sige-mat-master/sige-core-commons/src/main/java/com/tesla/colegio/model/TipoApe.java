package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla cat_tipo_ape
 * @author MV
 *
 */
public class TipoApe extends EntidadBase{
	
	private int id;
	private String nom;

	public TipoApe(){
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
	* Obtiene Tipo Paterno 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Tipo Paterno 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}


}

