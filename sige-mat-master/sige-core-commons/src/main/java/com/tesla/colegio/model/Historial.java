package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla msj_historial
 * @author MV
 *
 */
public class Historial extends EntidadBase{

	public final static String TABLA = "msj_historial";
	private Integer id;
	private Integer id_est;
	private Integer id_rec;
	private EstadoMensajeria estadomensajeria;	
	private Receptor receptor;	

	public Historial(){
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
	* Obtiene Estado 
	* @return id_est
	*/
	public Integer getId_est(){
		return id_est;
	}	

	/**
	* Estado 
	* @param id_est
	*/
	public void setId_est(Integer id_est) {
		this.id_est = id_est;
	}

	/**
	* Obtiene Receptor 
	* @return id_rec
	*/
	public Integer getId_rec(){
		return id_rec;
	}	

	/**
	* Receptor 
	* @param id_rec
	*/
	public void setId_rec(Integer id_rec) {
		this.id_rec = id_rec;
	}

	public EstadoMensajeria getEstadoMensajeria(){
		return estadomensajeria;
	}	

	public void setEstadoMensajeria(EstadoMensajeria estadomensajeria) {
		this.estadomensajeria = estadomensajeria;
	}
	public Receptor getReceptor(){
		return receptor;
	}	

	public void setReceptor(Receptor receptor) {
		this.receptor = receptor;
	}
}