package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla eva_criterio_alternativa
 * @author MV
 *
 */
public class CriterioAlternativa extends EntidadBase{

	public final static String TABLA = "eva_criterio_alternativa";
	private Integer id;
	private Integer id_pre;
	private String alt;
	private Integer punt;
	private CriterioPregunta criteriopregunta;	

	public CriterioAlternativa(){
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
	* Obtiene Pregunta 
	* @return id_pre
	*/
	public Integer getId_pre(){
		return id_pre;
	}	

	/**
	* Pregunta 
	* @param id_pre
	*/
	public void setId_pre(Integer id_pre) {
		this.id_pre = id_pre;
	}

	/**
	* Obtiene Alternativa 
	* @return alt
	*/
	public String getAlt(){
		return alt;
	}	

	/**
	* Alternativa 
	* @param alt
	*/
	public void setAlt(String alt) {
		this.alt = alt;
	}

	/**
	* Obtiene Puntaje 
	* @return punt
	*/
	public Integer getPunt(){
		return punt;
	}	

	/**
	* Puntaje 
	* @param punt
	*/
	public void setPunt(Integer punt) {
		this.punt = punt;
	}

	public CriterioPregunta getCriterioPregunta(){
		return criteriopregunta;
	}	

	public void setCriterioPregunta(CriterioPregunta criteriopregunta) {
		this.criteriopregunta = criteriopregunta;
	}
}