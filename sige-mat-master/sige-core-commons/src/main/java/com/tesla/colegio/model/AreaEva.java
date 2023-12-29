package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla eva_area_eva
 * @author MV
 *
 */
public class AreaEva extends EntidadBase{

	public final static String TABLA = "eva_area_eva";
	private Integer id;
	private String nom;
	private List<EvaluacionVacExamen> evaluacionvacexamens;

	public AreaEva(){
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
	* Obtiene Nombre del rea 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Nombre del rea 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	* Obtiene lista de Examen por Marcacin 
	*/
	public List<EvaluacionVacExamen> getEvaluacionVacExamens() {
		return evaluacionvacexamens;
	}

	/**
	* Seta Lista de Examen por Marcacin 
	* @param evaluacionvacexamens
	*/	
	public void setEvaluacionVacExamen(List<EvaluacionVacExamen> evaluacionvacexamens) {
		this.evaluacionvacexamens = evaluacionvacexamens;
	}
	/**
	* Obtiene lista de Nota Examen Vacante 
	*/


}