package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla asi_duracion_hora
 * @author MV
 *
 */
public class DuracionHora extends EntidadBase{

	public final static String TABLA = "asi_duracion_hora";
	private Integer id;
	private String nom;
	private String des;
	private Integer val;

	public DuracionHora(){
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
	* Obtiene Nombre de la hora 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Nombre de la hora 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	* Obtiene Descripcin del puesto de trabajador 
	* @return des
	*/
	public String getDes(){
		return des;
	}	

	/**
	* Descripcin del puesto de trabajador 
	* @param des
	*/
	public void setDes(String des) {
		this.des = des;
	}

	/**
	* Obtiene 45min, 60min 
	* @return val
	*/
	public Integer getVal(){
		return val;
	}	

	/**
	* 45min, 60min 
	* @param val
	*/
	public void setVal(Integer val) {
		this.val = val;
	}

}