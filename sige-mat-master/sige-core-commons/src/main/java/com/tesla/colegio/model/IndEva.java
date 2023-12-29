package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla not_ind_eva
 * @author MV
 *
 */
public class IndEva extends EntidadBase{

	public final static String TABLA = "not_ind_eva";
	private Integer id;
	private Integer id_ne;
	private Integer id_ind;
	private Evaluacion evaluacion;	
	private IndSub indsub;	

	public IndEva(){
	}

	public IndEva(Integer id, Integer id_ne){
		this.id = id;
		this.id_ne = id_ne;
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
	* Obtiene Evaluacion 
	* @return id_ne
	*/
	public Integer getId_ne(){
		return id_ne;
	}	

	/**
	* Evaluacion 
	* @param id_ne
	*/
	public void setId_ne(Integer id_ne) {
		this.id_ne = id_ne;
	}

	/**
	* Obtiene Indicador Subtema 
	* @return id_cis
	*/
	public Integer getId_ind(){
		return id_ind;
	}	

	/**
	* Indicador Subtema 
	* @param id_cis
	*/
	public void setId_ind(Integer id_ind) {
		this.id_ind = id_ind;
	}

	public Evaluacion getEvaluacion(){
		return evaluacion;
	}	

	public void setEvaluacion(Evaluacion evaluacion) {
		this.evaluacion = evaluacion;
	}
	public IndSub getIndSub(){
		return indsub;
	}	

	public void setIndSub(IndSub indsub) {
		this.indsub = indsub;
	}
}