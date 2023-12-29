package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla cat_area
 * @author MV
 *
 */
public class Area extends EntidadBase{

	public final static String TABLA = "cat_area";
	private Integer id;
	private String nom;
	private String des;
	private List<AreaAnio> areaanios;

	public Area(){
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
	* Obtiene Rgimen laboral 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Rgimen laboral 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	* Obtiene Descripcin del rgimen 
	* @return des
	*/
	public String getDes(){
		return des;
	}	

	/**
	* Descripcin del rgimen 
	* @param des
	*/
	public void setDes(String des) {
		this.des = des;
	}

	/**
	* Obtiene lista de reas Educativas 
	*/
	public List<AreaAnio> getAreaAnios() {
		return areaanios;
	}

	/**
	* Seta Lista de reas Educativas 
	* @param areaanios
	*/	
	public void setAreaAnio(List<AreaAnio> areaanios) {
		this.areaanios = areaanios;
	}
}