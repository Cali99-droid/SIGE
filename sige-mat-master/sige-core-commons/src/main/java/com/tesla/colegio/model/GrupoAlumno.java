package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla cvi_grupo_alumno
 * @author MV
 *
 */
public class GrupoAlumno extends EntidadBase{

	public final static String TABLA = "cvi_grupo_alumno";
	private Integer id;
	private Integer id_cgr;
	private Integer id_alu;
	private GrupoAulaVirtual grupoaulavirtual;	
	private Alumno alumno;	

	public GrupoAlumno(){
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
	* Obtiene Grupo 
	* @return id_cgr
	*/
	public Integer getId_cgr(){
		return id_cgr;
	}	

	/**
	* Grupo 
	* @param id_cgr
	*/
	public void setId_cgr(Integer id_cgr) {
		this.id_cgr = id_cgr;
	}

	/**
	* Obtiene Alumno 
	* @return id_alu
	*/
	public Integer getId_alu(){
		return id_alu;
	}	

	/**
	* Alumno 
	* @param id_alu
	*/
	public void setId_alu(Integer id_alu) {
		this.id_alu = id_alu;
	}

	public GrupoAulaVirtual getGrupoAulaVirtual(){
		return grupoaulavirtual;
	}	

	public void setGrupoAulaVirtual(GrupoAulaVirtual grupoaulavirtual) {
		this.grupoaulavirtual = grupoaulavirtual;
	}
	public Alumno getAlumno(){
		return alumno;
	}	

	public void setAlumno(Alumno alumno) {
		this.alumno = alumno;
	}
}