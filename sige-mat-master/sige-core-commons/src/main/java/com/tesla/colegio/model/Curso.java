package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla cat_curso
 * @author MV
 *
 */
public class Curso extends EntidadBase{

	public final static String TABLA = "cat_curso";
	private Integer id;
	private String nom;
	private List<CursoAnio> cursoanios;
	private List<Tema> temas;
	private List<CursoSubtema> cursosubtemas;

	public Curso(){
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
	* Obtiene Curso 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Curso 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	* Obtiene lista de Curso 
	*/
	public List<CursoAnio> getCursoAnios() {
		return cursoanios;
	}

	/**
	* Seta Lista de Curso 
	* @param cursoanios
	*/	
	public void setCursoAnio(List<CursoAnio> cursoanios) {
		this.cursoanios = cursoanios;
	}
	/**
	* Obtiene lista de Tema a tratarse por curso 
	*/
	public List<Tema> getTemas() {
		return temas;
	}

	/**
	* Seta Lista de Tema a tratarse por curso 
	* @param temas
	*/	
	public void setTema(List<Tema> temas) {
		this.temas = temas;
	}
	/**
	* Obtiene lista de Curso Subtema 
	*/
	public List<CursoSubtema> getCursoSubtemas() {
		return cursosubtemas;
	}

	/**
	* Seta Lista de Curso Subtema 
	* @param cursosubtemas
	*/	
	public void setCursoSubtema(List<CursoSubtema> cursosubtemas) {
		this.cursosubtemas = cursosubtemas;
	}
}