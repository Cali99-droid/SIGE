package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla mat_sit_historial
 * @author MV
 *
 */
public class SitHistorial extends EntidadBase{

	public final static String TABLA = "mat_sit_historial";
	private Integer id;
	private Integer id_mat;
	private Integer id_sit;
	private Matricula matricula;	
	private ColSituacion colsituacion;	

	public SitHistorial(){
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
	* Obtiene Matricula 
	* @return id_mat
	*/
	public Integer getId_mat(){
		return id_mat;
	}	

	/**
	* Matricula 
	* @param id_mat
	*/
	public void setId_mat(Integer id_mat) {
		this.id_mat = id_mat;
	}

	/**
	* Obtiene Situacin 
	* @return id_sit
	*/
	public Integer getId_sit(){
		return id_sit;
	}	

	/**
	* Situacin 
	* @param id_sit
	*/
	public void setId_sit(Integer id_sit) {
		this.id_sit = id_sit;
	}

	public Matricula getMatricula(){
		return matricula;
	}	

	public void setMatricula(Matricula matricula) {
		this.matricula = matricula;
	}
	public ColSituacion getColSituacion(){
		return colsituacion;
	}	

	public void setColSituacion(ColSituacion colsituacion) {
		this.colsituacion = colsituacion;
	}
}