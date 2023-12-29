package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla eva_tip_eva
 * @author MV
 *
 */
public class TipEva extends EntidadBase{

	public final static String TABLA = "eva_tip_eva";
	private Integer id;
	private String nom;
	private String tabla;
	private List<EvaluacionVacExamen> evaluacionvacexamens;

	public TipEva(){
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
	* Obtiene Tipo de evaluacin 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Tipo de evaluacin 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	* Obtiene Entidad 
	* @return tabla
	*/
	public String getTabla(){
		return tabla;
	}	

	/**
	* Entidad 
	* @param tabla
	*/
	public void setTabla(String tabla) {
		this.tabla = tabla;
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
}