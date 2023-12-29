package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla col_motivo_beca
 * @author MV
 *
 */
public class MotivoBeca extends EntidadBase{

	public final static String TABLA = "col_motivo_beca";
	private Integer id;
	private String nom;
	private Integer id_bec;
	private Beca beca;	

	public MotivoBeca(){
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
	* Obtiene Motivo Beca 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Motivo Beca 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	* Obtiene Beca 
	* @return id_bec
	*/
	public Integer getId_bec(){
		return id_bec;
	}	

	/**
	* Beca 
	* @param id_bec
	*/
	public void setId_bec(Integer id_bec) {
		this.id_bec = id_bec;
	}

	public Beca getBeca(){
		return beca;
	}	

	public void setBeca(Beca beca) {
		this.beca = beca;
	}
}