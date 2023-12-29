package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla cvi_curso_aula_virtual
 * @author MV
 *
 */
public class CursoAulaVirtual extends EntidadBase{

	public final static String TABLA = "cvi_curso_aula_virtual";
	private Integer id;
	private Integer id_anio;
	private Integer id_gra;
	private String nom;
	private String abrev;
	private Anio anio;	
	private Grad grad;	
	private List<PeriodoCurso> periodocursos;

	public CursoAulaVirtual(){
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
	* Obtiene Anio 
	* @return id_anio
	*/
	public Integer getId_anio(){
		return id_anio;
	}	

	/**
	* Anio 
	* @param id_anio
	*/
	public void setId_anio(Integer id_anio) {
		this.id_anio = id_anio;
	}

	/**
	* Obtiene Grado 
	* @return id_gra
	*/
	public Integer getId_gra(){
		return id_gra;
	}	

	/**
	* Grado 
	* @param id_gra
	*/
	public void setId_gra(Integer id_gra) {
		this.id_gra = id_gra;
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
	* Obtiene Abreviatura 
	* @return abrev
	*/
	public String getAbrev(){
		return abrev;
	}	

	/**
	* Abreviatura 
	* @param abrev
	*/
	public void setAbrev(String abrev) {
		this.abrev = abrev;
	}

	public Anio getAnio(){
		return anio;
	}	

	public void setAnio(Anio anio) {
		this.anio = anio;
	}
	public Grad getGrad(){
		return grad;
	}	

	public void setGrad(Grad grad) {
		this.grad = grad;
	}
	/**
	* Obtiene lista de Periodo Curso 
	*/
	public List<PeriodoCurso> getPeriodoCursos() {
		return periodocursos;
	}

	/**
	* Seta Lista de Periodo Curso 
	* @param periodocursos
	*/	
	public void setPeriodoCurso(List<PeriodoCurso> periodocursos) {
		this.periodocursos = periodocursos;
	}
}