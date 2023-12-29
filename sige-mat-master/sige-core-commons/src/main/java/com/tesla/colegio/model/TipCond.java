package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla cat_tip_cond
 * @author MV
 *
 */
public class TipCond extends EntidadBase{

	public final static String TABLA = "cat_tip_cond";
	private Integer id;
	private String nom;
	private List<CondAlumno> condalumnos;

	public TipCond(){
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
	* Obtiene Tipo de Condicion 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Tipo de Condicion 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	* Obtiene lista de Condicin del alumno 
	*/
	public List<CondAlumno> getCondAlumnos() {
		return condalumnos;
	}

	/**
	* Seta Lista de Condicin del alumno 
	* @param condalumnos
	*/	
	public void setCondAlumno(List<CondAlumno> condalumnos) {
		this.condalumnos = condalumnos;
	}
}