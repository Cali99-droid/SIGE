package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla col_tema
 * @author MV
 *
 */
public class Tema extends EntidadBase{

	public final static String TABLA = "col_tema";
	private Integer id;
	private Integer id_niv;
	private Integer id_cur;
	private Integer id_anio;
	private String nom;
	private Integer ord;
	private Nivel nivel;	
	private Curso curso;	
	private List<Subtema> subtemas;

	public Tema(){
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
	* Obtiene Nivel al que pertenece el tema 
	* @return id_niv
	*/
	public Integer getId_niv(){
		return id_niv;
	}	

	/**
	* Nivel al que pertenece el tema 
	* @param id_niv
	*/
	public void setId_niv(Integer id_niv) {
		this.id_niv = id_niv;
	}

	/**
	* Obtiene Curso al que pertenece el tema 
	* @return id_cur
	*/
	public Integer getId_cur(){
		return id_cur;
	}	

	/**
	* Curso al que pertenece el tema 
	* @param id_cur
	*/
	public void setId_cur(Integer id_cur) {
		this.id_cur = id_cur;
	}

	/**
	* Obtiene Nombre del Tema 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Nombre del Tema 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	* Obtiene Orden del Tema 
	* @return ord
	*/
	public Integer getOrd(){
		return ord;
	}	

	/**
	* Orden del Tema 
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
	* Obtiene lista de Subtema por Tema 
	*/
	public List<Subtema> getSubtemas() {
		return subtemas;
	}

	/**
	* Seta Lista de Subtema por Tema 
	* @param subtemas
	*/	
	public void setSubtema(List<Subtema> subtemas) {
		this.subtemas = subtemas;
	}

	public Integer getId_anio() {
		return id_anio;
	}

	public void setId_anio(Integer id_anio) {
		this.id_anio = id_anio;
	}
}