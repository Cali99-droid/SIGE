package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla col_unidad_tema
 * @author MV
 *
 */
public class UnidadTema extends EntidadBase{

	public final static String TABLA = "col_unidad_tema";
	private Integer id;
	private Integer id_uni;
	private Integer id_ccs;
	private CursoUnidad cursounidad;	
	private CursoSubtema cursosubtema;	

	public UnidadTema(){
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
	* Obtiene Unidad 
	* @return id_uni
	*/
	public Integer getId_uni(){
		return id_uni;
	}	

	/**
	* Unidad 
	* @param id_uni
	*/
	public void setId_uni(Integer id_uni) {
		this.id_uni = id_uni;
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

	public CursoUnidad getCursoUnidad(){
		return cursounidad;
	}	

	public void setCursoUnidad(CursoUnidad cursounidad) {
		this.cursounidad = cursounidad;
	}
	public CursoSubtema getCursoSubtema(){
		return cursosubtema;
	}	

	public void setCursoSubtema(CursoSubtema cursosubtema) {
		this.cursosubtema = cursosubtema;
	}
}