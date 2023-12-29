package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla cat_nacionalidad
 * @author MV
 *
 */
public class Nacionalidad extends EntidadBase{

	public final static String TABLA = "cat_nacionalidad";
	private Integer id;
	private String nom;
	private List<Persona> personas;

	public Nacionalidad(){
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

	/**
	* Obtiene lista de Persona 
	*/
	public List<Persona> getPersonas() {
		return personas;
	}

	/**
	* Seta Lista de Persona 
	* @param personas
	*/	
	public void setPersona(List<Persona> personas) {
		this.personas = personas;
	}
}