package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla col_desempenio
 * @author MV
 *
 */
public class Desempenio extends EntidadBase{

	public final static String TABLA = "col_desempenio";
	private Integer id;
	private String nom;
	private Integer id_cgc;
	//private GrupCapacidad grupcapacidad;	
	private List<Indicador> indicadors;

	public Desempenio(){
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
	* Obtiene Desempeo 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Desempeo 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	* Obtiene Capacidad 
	* @return id_cgc
	*/
	public Integer getId_cgc(){
		return id_cgc;
	}	

	/**
	* Capacidad 
	* @param id_cgc
	*/
	public void setId_cgc(Integer id_cgc) {
		this.id_cgc = id_cgc;
	}

	/*public GrupCapacidad getGrupCapacidad(){
		return grupcapacidad;
	}	

	public void setGrupCapacidad(GrupCapacidad grupcapacidad) {
		this.grupcapacidad = grupcapacidad;
	}*/
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
}