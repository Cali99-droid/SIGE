package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla col_sesion_tema
 * @author MV
 *
 */
public class SesionTema extends EntidadBase{

	public final static String TABLA = "col_sesion_tema";
	private Integer id;
	private Integer id_ses;
	private Integer id_ccs;
	private String tipo;
	private UnidadSesion unidadsesion;	
	private CursoSubtema cursosubtema;	

	public SesionTema(){
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
	* Obtiene Sesion 
	* @return id_ses
	*/
	public Integer getId_ses(){
		return id_ses;
	}	

	/**
	* Sesion 
	* @param id_ses
	*/
	public void setId_ses(Integer id_ses) {
		this.id_ses = id_ses;
	}

	/**
	* Obtiene Curso Subtema 
	* @return id_ccs
	*/
	public Integer getId_ccs(){
		return id_ccs;
	}	

	/**
	* Curso Subtema 
	* @param id_ccs
	*/
	public void setId_ccs(Integer id_ccs) {
		this.id_ccs = id_ccs;
	}

	/**
	* Obtiene Terico o Prctico 
	* @return tipo
	*/
	public String getTipo(){
		return tipo;
	}	

	/**
	* Terico o Prctico 
	* @param tipo
	*/
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public UnidadSesion getUnidadSesion(){
		return unidadsesion;
	}	

	public void setUnidadSesion(UnidadSesion unidadsesion) {
		this.unidadsesion = unidadsesion;
	}
	public CursoSubtema getCursoSubtema(){
		return cursosubtema;
	}	

	public void setCursoSubtema(CursoSubtema cursosubtema) {
		this.cursosubtema = cursosubtema;
	}
}