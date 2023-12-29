package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla con_ficha_seguimiento_det
 * @author MV
 *
 */
public class FichaSeguimientoDet extends EntidadBase{

	public final static String TABLA = "con_ficha_seguimiento_det";
	private Integer id;
	private Integer id_cfs;
	private Integer id_cpa;
	private String res;
	private String acc;
	private FichaSeguimiento fichaseguimiento;	

	public FichaSeguimientoDet(){
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
	* Obtiene Ficha Seguimiento 
	* @return id_cfs
	*/
	public Integer getId_cfs(){
		return id_cfs;
	}	

	/**
	* Ficha Seguimiento 
	* @param id_cfs
	*/
	public void setId_cfs(Integer id_cfs) {
		this.id_cfs = id_cfs;
	}

	/**
	* Obtiene Acciones a Realizar 
	* @return id_cpa
	*/
	public Integer getId_cpa(){
		return id_cpa;
	}	

	/**
	* Acciones a Realizar 
	* @param id_cpa
	*/
	public void setId_cpa(Integer id_cpa) {
		this.id_cpa = id_cpa;
	}

	/**
	* Obtiene Resultado 
	* @return res
	*/
	public String getRes(){
		return res;
	}	

	/**
	* Resultado 
	* @param res
	*/
	public void setRes(String res) {
		this.res = res;
	}

	/**
	* Obtiene Nuevas Acciones a Realizar 
	* @return acc
	*/
	public String getAcc(){
		return acc;
	}	

	/**
	* Nuevas Acciones a Realizar 
	* @param acc
	*/
	public void setAcc(String acc) {
		this.acc = acc;
	}

	public FichaSeguimiento getFichaSeguimiento(){
		return fichaseguimiento;
	}	

	public void setFichaSeguimiento(FichaSeguimiento fichaseguimiento) {
		this.fichaseguimiento = fichaseguimiento;
	}
}