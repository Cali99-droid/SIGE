package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla cat_condicion_per
 * @author MV
 *
 */
public class CondicionPer extends EntidadBase{

	public final static String TABLA = "cat_condicion_per";
	private Integer id;
	private String nom;
	private List<Persona> personas;

	public CondicionPer(){
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
	* Obtiene Condicion 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Condicion 
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