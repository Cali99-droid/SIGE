package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla col_subtema
 * @author MV
 *
 */
public class Subtema extends EntidadBase{

	public final static String TABLA = "col_subtema";
	private Integer id;
	private Integer id_tem;
	private String nom;
	private Integer ord;
	private String obs;
	private Tema tema;	
	private List<CursoSubtema> cursosubtemas;

	public Subtema(){
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
	* Obtiene Tema al que pertenece 
	* @return id_tem
	*/
	public Integer getId_tem(){
		return id_tem;
	}	

	/**
	* Tema al que pertenece 
	* @param id_tem
	*/
	public void setId_tem(Integer id_tem) {
		this.id_tem = id_tem;
	}

	/**
	* Obtiene Subtema 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Subtema 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
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

	/**
	* Obtiene Observacion 
	* @return obs
	*/
	public String getObs(){
		return obs;
	}	

	/**
	* Observacion 
	* @param obs
	*/
	public void setObs(String obs) {
		this.obs = obs;
	}

	public Tema getTema(){
		return tema;
	}	

	public void setTema(Tema tema) {
		this.tema = tema;
	}
	/**
	* Obtiene lista de Curso Subtema 
	*/
	public List<CursoSubtema> getCursoSubtemas() {
		return cursosubtemas;
	}

	/**
	* Seta Lista de Curso Subtema 
	* @param cursosubtemas
	*/	
	public void setCursoSubtema(List<CursoSubtema> cursosubtemas) {
		this.cursosubtemas = cursosubtemas;
	}
}