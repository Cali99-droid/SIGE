package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla cat_tipo_derivacion_dep
 * @author MV
 *
 */
public class TipoDerivacionDep extends EntidadBase{

	public final static String TABLA = "cat_tipo_derivacion_dep";
	private Integer id;
	private String nom;

	public TipoDerivacionDep(){
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

}