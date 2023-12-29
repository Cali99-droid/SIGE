package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla cat_idioma
 * @author MV
 *
 */
public class Idioma extends EntidadBase{

	public final static String TABLA = "cat_idioma";
	private Integer id;
	private String nom;
	private List<Alumno> alumnos;

	public Idioma(){
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
	* Obtiene Idioma 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Idioma 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	* Obtiene lista de Alumno 
	*/
	public List<Alumno> getAlumnos() {
		return alumnos;
	}

	/**
	* Seta Lista de Alumno 
	* @param alumnos
	*/	
	public void setAlumno(List<Alumno> alumnos) {
		this.alumnos = alumnos;
	}
}