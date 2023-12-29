package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla col_subtema_capacidad
 * @author MV
 *
 */
public class SubtemaCapacidad extends EntidadBase{

	public final static String TABLA = "col_subtema_capacidad";
	private Integer id;
	private Integer id_ccs;
	private Integer id_cap;
	private CursoSubtema cursosubtema;	
	private Capacidad capacidad;	

	public SubtemaCapacidad(){
	}

	/**
	* Obtiene Llave principal 
	* @return id
	*/
	public Integer getId(){
		return id;
	}	

	/**
	* Llave principal 
	* @param id
	*/
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	* Obtiene Curso Subtema 
	* @return id_ccs
	*/
	public Integer getId_ccs(){
		return id_ccs;
	}	

	/**
	* Curso Subtema 
	* @param id_ccs
	*/
	public void setId_ccs(Integer id_ccs) {
		this.id_ccs = id_ccs;
	}

	/**
	* Obtiene Capacidad 
	* @return id_cap
	*/
	public Integer getId_cap(){
		return id_cap;
	}	

	/**
	* Capacidad 
	* @param id_cap
	*/
	public void setId_cap(Integer id_cap) {
		this.id_cap = id_cap;
	}

	public CursoSubtema getCursoSubtema(){
		return cursosubtema;
	}	

	public void setCursoSubtema(CursoSubtema cursosubtema) {
		this.cursosubtema = cursosubtema;
	}
	public Capacidad getCapacidad(){
		return capacidad;
	}	

	public void setCapacidad(Capacidad capacidad) {
		this.capacidad = capacidad;
	}
}