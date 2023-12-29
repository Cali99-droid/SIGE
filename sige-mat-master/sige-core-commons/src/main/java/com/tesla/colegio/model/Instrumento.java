package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla eva_instrumento
 * @author MV
 *
 */
public class Instrumento extends EntidadBase{

	public final static String TABLA = "eva_instrumento";
	private Integer id;
	private String nom;
	private List<InsExaCri> insexacris;

	public Instrumento(){
	}

	/**
	* Obtiene Llave Principal 
	* @return id
	*/
	public Integer getId(){
		return id;
	}	

	/**
	* Llave Principal 
	* @param id
	*/
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	* Obtiene Nombre 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Nombre 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	* Obtiene lista de Examen Criterio Instrumentos 
	*/
	public List<InsExaCri> getInsExaCris() {
		return insexacris;
	}

	/**
	* Seta Lista de Examen Criterio Instrumentos 
	* @param insexacris
	*/	
	public void setInsExaCri(List<InsExaCri> insexacris) {
		this.insexacris = insexacris;
	}
}