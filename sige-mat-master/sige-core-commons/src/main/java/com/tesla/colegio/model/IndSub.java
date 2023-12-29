package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla col_ind_sub
 * @author MV
 *
 */
public class IndSub extends EntidadBase{

	public final static String TABLA = "col_ind_sub";
	private Integer id;
	private Integer id_ind;
	private Integer id_sub;
	private Indicador indicador;	
	private CursoSubtema cursosubtema;	

	public IndSub(){
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
	* Obtiene Indicador 
	* @return id_ind
	*/
	public Integer getId_ind(){
		return id_ind;
	}	

	/**
	* Indicador 
	* @param id_ind
	*/
	public void setId_ind(Integer id_ind) {
		this.id_ind = id_ind;
	}

	/**
	* Obtiene Curso Subtema 
	* @return id_sub
	*/
	public Integer getId_sub(){
		return id_sub;
	}	

	/**
	* Curso Subtema 
	* @param id_sub
	*/
	public void setId_sub(Integer id_sub) {
		this.id_sub = id_sub;
	}

	public Indicador getIndicador(){
		return indicador;
	}	

	public void setIndicador(Indicador indicador) {
		this.indicador = indicador;
	}
	public CursoSubtema getCursoSubtema(){
		return cursosubtema;
	}	

	public void setCursoSubtema(CursoSubtema cursosubtema) {
		this.cursosubtema = cursosubtema;
	}
}