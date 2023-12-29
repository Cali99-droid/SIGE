package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla cat_pais
 * @author MV
 *
 */
public class Pais extends EntidadBase{

	public final static String TABLA = "cat_pais";
	private Integer id;
	private String nom;
	private String cod;
	private List<Departamento> departamentos;

	public Pais(){
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
	* Obtiene Pais 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Pais 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	* Obtiene Cdigo 
	* @return cod
	*/
	public String getCod(){
		return cod;
	}	

	/**
	* Cdigo 
	* @param cod
	*/
	public void setCod(String cod) {
		this.cod = cod;
	}

	/**
	* Obtiene lista de Departamento (Ancash, Apurimac, Loreto, etc) 
	*/
	public List<Departamento> getDepartamentos() {
		return departamentos;
	}

	/**
	* Seta Lista de Departamento (Ancash, Apurimac, Loreto, etc) 
	* @param departamentos
	*/	
	public void setDepartamento(List<Departamento> departamentos) {
		this.departamentos = departamentos;
	}
}