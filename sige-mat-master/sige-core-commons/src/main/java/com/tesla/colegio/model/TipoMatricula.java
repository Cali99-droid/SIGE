package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla cat_tipo_matricula
 * @author MV
 *
 */
public class TipoMatricula extends EntidadBase{

	public final static String TABLA = "cat_tipo_matricula";
	private Integer id;
	private String nom;

	public TipoMatricula(){
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
	* Obtiene Nacionalidad 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Nacionalidad 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

}