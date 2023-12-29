package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla cat_tipo_agresor
 * @author MV
 *
 */
public class TipoAgresor extends EntidadBase{

	public final static String TABLA = "cat_tipo_agresor";
	private Integer id;
	private String nom;
	private List<BullingAgresor> bullingagresors;

	public TipoAgresor(){
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
	* Obtiene Nombre 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Nombre 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	* Obtiene lista de Agresor de bulling 
	*/
	public List<BullingAgresor> getBullingAgresors() {
		return bullingagresors;
	}

	/**
	* Seta Lista de Agresor de bulling 
	* @param bullingagresors
	*/	
	public void setBullingAgresor(List<BullingAgresor> bullingagresors) {
		this.bullingagresors = bullingagresors;
	}
}