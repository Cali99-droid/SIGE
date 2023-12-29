package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla col_unidad_capacidad
 * @author MV
 *
 */
public class UnidadCapacidad extends EntidadBase{

	public final static String TABLA = "col_unidad_capacidad";
	private Integer id;
	private Integer id_uni;
	private Integer id_cap;
	private CursoUnidad cursounidad;	
	private Capacidad capacidad;	

	public UnidadCapacidad(){
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
	* Obtiene Unidad 
	* @return id_uni
	*/
	public Integer getId_uni(){
		return id_uni;
	}	

	/**
	* Unidad 
	* @param id_uni
	*/
	public void setId_uni(Integer id_uni) {
		this.id_uni = id_uni;
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

	public CursoUnidad getCursoUnidad(){
		return cursounidad;
	}	

	public void setCursoUnidad(CursoUnidad cursounidad) {
		this.cursounidad = cursounidad;
	}
	public Capacidad getCapacidad(){
		return capacidad;
	}	

	public void setCapacidad(Capacidad capacidad) {
		this.capacidad = capacidad;
	}
}