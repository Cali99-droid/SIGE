package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla cat_tipo_derivacion
 * @author MV
 *
 */
public class TipoDerivacion extends EntidadBase{

	public final static String TABLA = "cat_tipo_derivacion";
	private Integer id;
	private String nom;

	public TipoDerivacion(){
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
	* Obtiene Derivacin 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Derivacin 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

}