package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla cat_cliente
 * @author MV
 *
 */
public class Cliente extends EntidadBase{

	public final static String TABLA = "cat_cliente";
	private Integer id;
	private String nom;
	private List<Matricula> matriculas;

	public Cliente(){
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
	* Obtiene Cliente 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Cliente 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	* Obtiene lista de Matricula del alumno 
	*/
	public List<Matricula> getMatriculas() {
		return matriculas;
	}

	/**
	* Seta Lista de Matricula del alumno 
	* @param matriculas
	*/	
	public void setMatricula(List<Matricula> matriculas) {
		this.matriculas = matriculas;
	}
}