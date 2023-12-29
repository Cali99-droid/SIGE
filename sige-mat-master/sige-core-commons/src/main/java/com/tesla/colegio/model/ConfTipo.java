package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla eco_conf_tipo
 * @author MV
 *
 */
public class ConfTipo extends EntidadBase{

	public final static String TABLA = "eco_conf_tipo";
	private Integer id;
	private String tip;
	private String nom;
	private List<Configuracion> configuracions;

	public ConfTipo(){
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
	* Obtiene Tipo de respuesta 
	* @return tip
	*/
	public String getTip(){
		return tip;
	}	

	/**
	* Tipo de respuesta 
	* @param tip
	*/
	public void setTip(String tip) {
		this.tip = tip;
	}

	/**
	* Obtiene Valor 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Valor 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	* Obtiene lista de Configuracin de la Evaluacin Econmica 
	*/
	public List<Configuracion> getConfiguracions() {
		return configuracions;
	}

	/**
	* Seta Lista de Configuracin de la Evaluacin Econmica 
	* @param configuracions
	*/	
	public void setConfiguracion(List<Configuracion> configuracions) {
		this.configuracions = configuracions;
	}
}