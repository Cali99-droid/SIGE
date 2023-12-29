package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla cat_mat_documentos
 * @author MV
 *
 */
public class MatDocumentos extends EntidadBase{

	public final static String TABLA = "cat_mat_documentos";
	private Integer id;
	private String nom;

	public MatDocumentos(){
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
	* Obtiene Documento 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Documento 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

}