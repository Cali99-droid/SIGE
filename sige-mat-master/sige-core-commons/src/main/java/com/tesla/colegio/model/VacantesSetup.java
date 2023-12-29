package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla mat_vacantes_setup
 * @author MV
 *
 */
public class VacantesSetup extends EntidadBase{

	public final static String TABLA = "mat_vacantes_setup";
	private Integer id;
	private Integer id_au;
	private Integer vacantes;
	private Nivel nivel;	

	public VacantesSetup(){
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
	* Obtiene Nivel 
	* @return id_au
	*/
	public Integer getId_au(){
		return id_au;
	}	

	/**
	* Nivel 
	* @param id_au
	*/
	public void setId_au(Integer id_au) {
		this.id_au = id_au;
	}

	/**
	* Obtiene Nmero de vacantes 
	* @return vacantes
	*/
	public Integer getVacantes(){
		return vacantes;
	}	

	/**
	* Nmero de vacantes 
	* @param vacantes
	*/
	public void setVacantes(Integer vacantes) {
		this.vacantes = vacantes;
	}

	public Nivel getNivel(){
		return nivel;
	}	

	public void setNivel(Nivel nivel) {
		this.nivel = nivel;
	}
}