package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla col_sesion_actividad
 * @author MV
 *
 */
public class SesionActividad extends EntidadBase{

	public final static String TABLA = "col_sesion_actividad";
	private Integer id;
	private Integer id_ses;
	private String nom;
	private UnidadSesion unidadsesion;	

	public SesionActividad(){
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
	* Obtiene Sesion 
	* @return id_ses
	*/
	public Integer getId_ses(){
		return id_ses;
	}	

	/**
	* Sesion 
	* @param id_ses
	*/
	public void setId_ses(Integer id_ses) {
		this.id_ses = id_ses;
	}

	/**
	* Obtiene Nombre de la actividad 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Nombre de la actividad 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	public UnidadSesion getUnidadSesion(){
		return unidadsesion;
	}	

	public void setUnidadSesion(UnidadSesion unidadsesion) {
		this.unidadsesion = unidadsesion;
	}
}