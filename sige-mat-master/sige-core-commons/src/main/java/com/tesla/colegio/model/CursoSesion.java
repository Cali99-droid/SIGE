package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla col_curso_sesion
 * @author MV
 *
 */
public class CursoSesion extends EntidadBase{

	public final static String TABLA = "col_curso_sesion";
	private Integer id;
	private Integer id_niv;
	private Integer id_gra;
	private Integer id_caa;
	private Integer id_cur;
	private Integer nro_ses;
	private Nivel nivel;	
	private Grad grad;	
	private AreaAnio areaanio;	
	private Curso curso;	

	public CursoSesion(){
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
	* Obtiene Grado Acadmico 
	* @return id_gra
	*/
	public Integer getId_gra(){
		return id_gra;
	}	

	/**
	* Grado Acadmico 
	* @param id_gra
	*/
	public void setId_gra(Integer id_gra) {
		this.id_gra = id_gra;
	}

	/**
	* Obtiene Area que pertenece el curso 
	* @return id_caa
	*/
	public Integer getId_caa(){
		return id_caa;
	}	

	/**
	* Area que pertenece el curso 
	* @param id_caa
	*/
	public void setId_caa(Integer id_caa) {
		this.id_caa = id_caa;
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
	* Obtiene Numero de Sesiones 
	* @return nro_ses
	*/
	public Integer getNro_ses(){
		return nro_ses;
	}	

	/**
	* Numero de Sesiones 
	* @param nro_ses
	*/
	public void setNro_ses(Integer nro_ses) {
		this.nro_ses = nro_ses;
	}

	public Nivel getNivel(){
		return nivel;
	}	

	public void setNivel(Nivel nivel) {
		this.nivel = nivel;
	}
	public Grad getGrad(){
		return grad;
	}	

	public void setGrad(Grad grad) {
		this.grad = grad;
	}
	public AreaAnio getAreaAnio(){
		return areaanio;
	}	

	public void setAreaAnio(AreaAnio areaanio) {
		this.areaanio = areaanio;
	}
	public Curso getCurso(){
		return curso;
	}	

	public void setCurso(Curso curso) {
		this.curso = curso;
	}
}