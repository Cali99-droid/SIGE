package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla cat_centro_salud
 * @author MV
 *
 */
public class CentroSalud extends EntidadBase{

	public final static String TABLA = "cat_centro_salud";
	private Integer id;
	private String nom;
	private List<GruFam> grufams;

	public CentroSalud(){
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
	* Obtiene Centro de Salud 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Centro de Salud 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	* Obtiene lista de Grupo Familiar 
	*/
	public List<GruFam> getGruFams() {
		return grufams;
	}

	/**
	* Seta Lista de Grupo Familiar 
	* @param grufams
	*/	
	public void setGruFam(List<GruFam> grufams) {
		this.grufams = grufams;
	}
}