package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla col_tutor_aula
 * @author MV
 *
 */
public class TutorAula extends EntidadBase{

	public final static String TABLA = "col_tutor_aula";
	private Integer id;
	private Integer id_au;
	private Integer id_tra;
	private Aula aula;	
	private Trabajador trabajador;	
	private Nivel nivel;
	private Grad grad;

	public TutorAula(){
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
	* Obtiene Aula 
	* @return id_au
	*/
	public Integer getId_au(){
		return id_au;
	}	

	/**
	* Aula 
	* @param id_au
	*/
	public void setId_au(Integer id_au) {
		this.id_au = id_au;
	}

	/**
	* Obtiene Trabajador 
	* @return id_tra
	*/
	public Integer getId_tra(){
		return id_tra;
	}	

	/**
	* Trabajador 
	* @param id_tra
	*/
	public void setId_tra(Integer id_tra) {
		this.id_tra = id_tra;
	}

	public Aula getAula(){
		return aula;
	}	

	public void setAula(Aula aula) {
		this.aula = aula;
	}
	public Trabajador getTrabajador(){
		return trabajador;
	}	

	public void setTrabajador(Trabajador trabajador) {
		this.trabajador = trabajador;
	}

	public Nivel getNivel() {
		return nivel;
	}

	public void setNivel(Nivel nivel) {
		this.nivel = nivel;
	}

	public Grad getGrad() {
		return grad;
	}

	public void setGrad(Grad grad) {
		this.grad = grad;
	}
}