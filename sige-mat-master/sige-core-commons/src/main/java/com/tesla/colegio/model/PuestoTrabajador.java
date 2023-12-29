package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla ges_puesto_trabajador
 * @author MV
 *
 */
public class PuestoTrabajador extends EntidadBase{

	public final static String TABLA = "ges_puesto_trabajador";
	private Integer id;
	private String nom;
	private String des;

	public PuestoTrabajador(){
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
	* Obtiene Puesto 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Puesto 
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

}