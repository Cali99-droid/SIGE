package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla cat_tipo_promedio_aca
 * @author MV
 *
 */
public class TipoPromedioAca extends EntidadBase{

	public final static String TABLA = "cat_tipo_promedio_aca";
	private Integer id;
	private String nom;
	private String cod;

	public TipoPromedioAca(){
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
	* Obtiene Tipo Promedio 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Tipo Promedio 
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