package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla col_sesion_indicador
 * @author MV
 *
 */
public class SesionIndicador extends EntidadBase{

	public final static String TABLA = "col_sesion_indicador";
	private Integer id;
	private Integer id_ses;
	private Integer id_ind;
	private UnidadSesion unidadsesion;	
	private Indicador indicador;	

	public SesionIndicador(){
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
	* Obtiene Sesion 
	* @return id_ses
	*/
	public Integer getId_ses(){
		return id_ses;
	}	

	/**
	* Sesion 
	* @param id_ses
	*/
	public void setId_ses(Integer id_ses) {
		this.id_ses = id_ses;
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

	public UnidadSesion getUnidadSesion(){
		return unidadsesion;
	}	

	public void setUnidadSesion(UnidadSesion unidadsesion) {
		this.unidadsesion = unidadsesion;
	}
	public Indicador getIndicador(){
		return indicador;
	}	

	public void setIndicador(Indicador indicador) {
		this.indicador = indicador;
	}
}