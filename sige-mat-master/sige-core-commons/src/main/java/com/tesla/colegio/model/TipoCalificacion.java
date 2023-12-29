package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla cat_tipo_calificacion
 * @author MV
 *
 */
public class TipoCalificacion extends EntidadBase{

	public final static String TABLA = "cat_tipo_calificacion";
	private Integer id;
	private String nom;
	private String cod;

	public TipoCalificacion(){
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
	* Obtiene Tipo Calificacion 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Tipo Calificacion 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	* Obtiene Codigo 
	* @return cod
	*/
	public String getCod(){
		return cod;
	}	

	/**
	* Codigo 
	* @param cod
	*/
	public void setCod(String cod) {
		this.cod = cod;
	}

}