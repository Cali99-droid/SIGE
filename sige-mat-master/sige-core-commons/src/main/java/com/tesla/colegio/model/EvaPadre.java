package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla not_eva_padre
 * @author MV
 *
 */
public class EvaPadre extends EntidadBase{

	public final static String TABLA = "not_eva_padre";
	private Integer id;
	private Integer id_tra;
	private Trabajador trabajador;	
	private List<Evaluacion> evaluacions;

	public EvaPadre(){
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
	* Obtiene Docente 
	* @return id_tra
	*/
	public Integer getId_tra(){
		return id_tra;
	}	

	/**
	* Docente 
	* @param id_tra
	*/
	public void setId_tra(Integer id_tra) {
		this.id_tra = id_tra;
	}

	public Trabajador getTrabajador(){
		return trabajador;
	}	

	public void setTrabajador(Trabajador trabajador) {
		this.trabajador = trabajador;
	}
	/**
	* Obtiene lista de Evaluaciones Acadmicas 
	*/
	public List<Evaluacion> getEvaluacions() {
		return evaluacions;
	}

	/**
	* Seta Lista de Evaluaciones Acadmicas 
	* @param evaluacions
	*/	
	public void setEvaluacion(List<Evaluacion> evaluacions) {
		this.evaluacions = evaluacions;
	}
}