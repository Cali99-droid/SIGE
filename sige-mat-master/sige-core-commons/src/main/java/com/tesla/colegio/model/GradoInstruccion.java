package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla cat_grado_instruccion
 * @author MV
 *
 */
public class GradoInstruccion extends EntidadBase{

	public final static String TABLA = "cat_grado_instruccion";
	private Integer id;
	private String nom;
	private List<Familiar> familiars;

	public GradoInstruccion(){
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
	* Obtiene Tipo de documento 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Tipo de documento 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	* Obtiene lista de Familiar del alumno 
	*/
	public List<Familiar> getFamiliars() {
		return familiars;
	}

	/**
	* Seta Lista de Familiar del alumno 
	* @param familiars
	*/	
	public void setFamiliar(List<Familiar> familiars) {
		this.familiars = familiars;
	}
}