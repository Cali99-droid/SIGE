package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla col_turno
 * @author MV
 *
 */
public class Turno extends EntidadBase{

	public final static String TABLA = "col_turno";
	private Integer id;
	private String nom;
	private String cod;
	private List<TurServicio> turservicios;
	private List<Aula> aulas;

	public Turno(){
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
	* Obtiene Nombre del turno 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Nombre del turno 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	* Obtiene Codigo 
	* @return cod
	*/
	public String getCod(){
		return cod;
	}	

	/**
	* Codigo 
	* @param cod
	*/
	public void setCod(String cod) {
		this.cod = cod;
	}

	/**
	* Obtiene lista de Turno Servicio 
	*/
	public List<TurServicio> getTurServicios() {
		return turservicios;
	}

	/**
	* Seta Lista de Turno Servicio 
	* @param turservicios
	*/	
	public void setTurServicio(List<TurServicio> turservicios) {
		this.turservicios = turservicios;
	}
	/**
	* Obtiene lista de Aula del colegio 
	*/
	public List<Aula> getAulas() {
		return aulas;
	}

	/**
	* Seta Lista de Aula del colegio 
	* @param aulas
	*/	
	public void setAula(List<Aula> aulas) {
		this.aulas = aulas;
	}
}