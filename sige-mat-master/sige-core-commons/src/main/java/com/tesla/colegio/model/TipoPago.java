package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla asi_tipo_pago
 * @author MV
 *
 */
public class TipoPago extends EntidadBase{

	public final static String TABLA = "asi_tipo_pago";
	private Integer id;
	private String nom;
	private String des;

	public TipoPago(){
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
	* Obtiene hora,semanal, quincenal, mensual 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* hora,semanal, quincenal, mensual 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	* Obtiene Descripcin del tipo de facturacin trabajador 
	* @return des
	*/
	public String getDes(){
		return des;
	}	

	/**
	* Descripcin del tipo de facturacin trabajador 
	* @param des
	*/
	public void setDes(String des) {
		this.des = des;
	}

}