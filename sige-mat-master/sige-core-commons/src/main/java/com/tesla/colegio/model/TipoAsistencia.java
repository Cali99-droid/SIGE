package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla asi_tipo_asistencia
 * @author MV
 *
 */
public class TipoAsistencia extends EntidadBase{

	public final static String TABLA = "asi_tipo_asistencia";
	private Integer id;
	private String nom;
	private String des;

	public TipoAsistencia(){
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
	* Obtiene Tipo de asistencia 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Tipo de asistencia 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	* Obtiene Descripcin 
	* @return des
	*/
	public String getDes(){
		return des;
	}	

	/**
	* Descripcin 
	* @param des
	*/
	public void setDes(String des) {
		this.des = des;
	}

}