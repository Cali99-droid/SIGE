package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla cat_estado_conductual
 * @author MV
 *
 */
public class EstadoConductual extends EntidadBase{

	public final static String TABLA = "cat_estado_conductual";
	private Integer id;
	private String nom;
	private List<Incidencia> incidencias;
	private List<IncidenciaTraza> incidenciatrazas;

	public EstadoConductual(){
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
	* Obtiene Estado Conducutal 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Estado Conducutal 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	* Obtiene lista de Incidencias por Comportamiento 
	*/
	public List<Incidencia> getIncidencias() {
		return incidencias;
	}

	/**
	* Seta Lista de Incidencias por Comportamiento 
	* @param incidencias
	*/	
	public void setIncidencia(List<Incidencia> incidencias) {
		this.incidencias = incidencias;
	}
	/**
	* Obtiene lista de Incidencia Conductual Traza 
	*/
	public List<IncidenciaTraza> getIncidenciaTrazas() {
		return incidenciatrazas;
	}

	/**
	* Seta Lista de Incidencia Conductual Traza 
	* @param incidenciatrazas
	*/	
	public void setIncidenciaTraza(List<IncidenciaTraza> incidenciatrazas) {
		this.incidenciatrazas = incidenciatrazas;
	}
}