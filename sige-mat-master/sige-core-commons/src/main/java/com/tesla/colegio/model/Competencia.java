package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla col_competencia
 * @author MV
 *
 */
public class Competencia extends EntidadBase{

	public final static String TABLA = "col_competencia";
	private Integer id;
	private Integer id_niv;
	private Integer id_cur;
	private String nom;
	private String peso;
	private Integer ord;
	private Nivel nivel;	
	private Curso curso;	
	private List<Capacidad> capacidads;

	public Competencia(){
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
	* Obtiene Nivel 
	* @return id_niv
	*/
	public Integer getId_niv(){
		return id_niv;
	}	

	/**
	* Nivel 
	* @param id_niv
	*/
	public void setId_niv(Integer id_niv) {
		this.id_niv = id_niv;
	}

	/**
	* Obtiene Curso 
	* @return id_cur
	*/
	public Integer getId_cur(){
		return id_cur;
	}	

	/**
	* Curso 
	* @param id_cur
	*/
	public void setId_cur(Integer id_cur) {
		this.id_cur = id_cur;
	}

	/**
	* Obtiene Competencia 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Competencia 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	* Obtiene Peso 
	* @return peso
	*/
	public String getPeso(){
		return peso;
	}	

	/**
	* Peso 
	* @param peso
	*/
	public void setPeso(String peso) {
		this.peso = peso;
	}

	/**
	* Obtiene Orden 
	* @return ord
	*/
	public Integer getOrd(){
		return ord;
	}	

	/**
	* Orden 
	* @param ord
	*/
	public void setOrd(Integer ord) {
		this.ord = ord;
	}

	public Nivel getNivel(){
		return nivel;
	}	

	public void setNivel(Nivel nivel) {
		this.nivel = nivel;
	}
	public Curso getCurso(){
		return curso;
	}	

	public void setCurso(Curso curso) {
		this.curso = curso;
	}
	/**
	* Obtiene lista de Capacidad 
	*/
	public List<Capacidad> getCapacidads() {
		return capacidads;
	}

	/**
	* Seta Lista de Capacidad 
	* @param capacidads
	*/	
	public void setCapacidad(List<Capacidad> capacidads) {
		this.capacidads = capacidads;
	}
}