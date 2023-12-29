package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla cat_nivel
 * @author MV
 *
 */
public class Nivel extends EntidadBase{

	public final static String TABLA = "cat_nivel";
	private Integer id;
	private String cod_mod;
	private String nom;
	private List<Grad> grads;

	public Nivel(){
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
	* Obtiene Cdigo modular del nivel educativo 
	* @return cod_mod
	*/
	public String getCod_mod(){
		return cod_mod;
	}	

	/**
	* Cdigo modular del nivel educativo 
	* @param cod_mod
	*/
	public void setCod_mod(String cod_mod) {
		this.cod_mod = cod_mod;
	}

	/**
	* Obtiene Nombre del nivel educativo 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Nombre del nivel educativo 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	* Obtiene lista de Grado Educativo 
	*/
	public List<Grad> getGrads() {
		return grads;
	}

	/**
	* Seta Lista de Grado Educativo 
	* @param grads
	*/	
	public void setGrad(List<Grad> grads) {
		this.grads = grads;
	}
}