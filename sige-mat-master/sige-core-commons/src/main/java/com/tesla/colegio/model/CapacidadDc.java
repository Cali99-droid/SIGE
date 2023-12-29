package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla aca_capacidad_dc
 * @author MV
 *
 */
public class CapacidadDc extends EntidadBase{

	public final static String TABLA = "aca_capacidad_dc";
	private Integer id;
	private Integer id_com;
	private String nom;
	private java.math.BigDecimal peso;
	private Integer orden;
	private CompetenciaDc competenciadc;	

	public CapacidadDc(){
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
	* Obtiene Peso 
	* @return peso
	*/
	public java.math.BigDecimal getPeso(){
		return peso;
	}	

	/**
	* Peso 
	* @param peso
	*/
	public void setPeso(java.math.BigDecimal peso) {
		this.peso = peso;
	}

	/**
	* Obtiene Orden 
	* @return orden
	*/
	public Integer getOrden(){
		return orden;
	}	

	/**
	* Orden 
	* @param orden
	*/
	public void setOrden(Integer orden) {
		this.orden = orden;
	}

	public CompetenciaDc getCompetenciaDc(){
		return competenciadc;
	}	

	public void setCompetenciaDc(CompetenciaDc competenciadc) {
		this.competenciadc = competenciadc;
	}
}