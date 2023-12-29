package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla col_capacidad
 * @author MV
 *
 */
public class Capacidad extends EntidadBase{

	public final static String TABLA = "col_capacidad";
	private Integer id;
	private Integer id_com;
	private String nom;
	private Competencia competencia;	
	private List<Indicador> indicadors;
	private List<SubtemaCapacidad> subtemacapacidads;

	public Capacidad(){
	}

	/**
	* Obtiene Llave principal 
	* @return id
	*/
	public Integer getId(){
		return id;
	}	

	/**
	* Llave principal 
	* @param id
	*/
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	* Obtiene Competencia 
	* @return id_com
	*/
	public Integer getId_com(){
		return id_com;
	}	

	/**
	* Competencia 
	* @param id_com
	*/
	public void setId_com(Integer id_com) {
		this.id_com = id_com;
	}

	/**
	* Obtiene Capacidad 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Capacidad 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	public Competencia getCompetencia(){
		return competencia;
	}	

	public void setCompetencia(Competencia competencia) {
		this.competencia = competencia;
	}
	/**
	* Obtiene lista de Indicador 
	*/
	public List<Indicador> getIndicadors() {
		return indicadors;
	}

	/**
	* Seta Lista de Indicador 
	* @param indicadors
	*/	
	public void setIndicador(List<Indicador> indicadors) {
		this.indicadors = indicadors;
	}
	/**
	* Obtiene lista de La relacion de tema capacidad nos da la programacion anual 
	*/
	public List<SubtemaCapacidad> getSubtemaCapacidads() {
		return subtemacapacidads;
	}

	/**
	* Seta Lista de La relacion de tema capacidad nos da la programacion anual 
	* @param subtemacapacidads
	*/	
	public void setSubtemaCapacidad(List<SubtemaCapacidad> subtemacapacidads) {
		this.subtemacapacidads = subtemacapacidads;
	}
}