package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla cat_central_riesgo
 * @author MV
 *
 */
public class CentralRiesgo extends EntidadBase{

	public final static String TABLA = "cat_central_riesgo";
	private Integer id;
	private String nom;
	private Integer id_pad;
	private String nom_pad;
	private Integer id_mad;
	private String nom_mad;
	private List<HistorialEco> historialecos;

	public CentralRiesgo(){
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
	* Obtiene Nombre de la central riesgo 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Nombre de la central riesgo 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	* Obtiene lista de Historial Economico Alumno 
	*/
	public List<HistorialEco> getHistorialEcos() {
		return historialecos;
	}

	/**
	* Seta Lista de Historial Economico Alumno 
	* @param historialecos
	*/	
	public void setHistorialEco(List<HistorialEco> historialecos) {
		this.historialecos = historialecos;
	}

	public Integer getId_pad() {
		return id_pad;
	}

	public void setId_pad(Integer id_pad) {
		this.id_pad = id_pad;
	}

	public String getNom_pad() {
		return nom_pad;
	}

	public void setNom_pad(String nom_pad) {
		this.nom_pad = nom_pad;
	}

	public Integer getId_mad() {
		return id_mad;
	}

	public void setId_mad(Integer id_mad) {
		this.id_mad = id_mad;
	}

	public String getNom_mad() {
		return nom_mad;
	}

	public void setNom_mad(String nom_mad) {
		this.nom_mad = nom_mad;
	}
	
}