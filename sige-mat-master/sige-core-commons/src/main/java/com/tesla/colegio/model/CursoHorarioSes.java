package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Clase entidad que representa a la tabla col_curso_horario_ses
 * @author MV
 *
 */
public class CursoHorarioSes extends EntidadBase{

	public final static String TABLA = "col_curso_horario_ses";
	private Integer id;
	private Integer id_cch;
	private Integer id_uns;
	private Integer id_ccs;
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	private java.util.Date fec;
	private CursoHorario cursohorario;	
	private UnidadSesion unidadsesion;	
	private ConfSemanas confsemanas;	

	public CursoHorarioSes(){
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
	* Obtiene Curso Horario 
	* @return id_cch
	*/
	public Integer getId_cch(){
		return id_cch;
	}	

	/**
	* Curso Horario 
	* @param id_cch
	*/
	public void setId_cch(Integer id_cch) {
		this.id_cch = id_cch;
	}

	/**
	* Obtiene Unidad Sesion 
	* @return id_uns
	*/
	public Integer getId_uns(){
		return id_uns;
	}	

	/**
	* Unidad Sesion 
	* @param id_uns
	*/
	public void setId_uns(Integer id_uns) {
		this.id_uns = id_uns;
	}

	/**
	* Obtiene Configuracion Semana 
	* @return id_ccs
	*/
	public Integer getId_ccs(){
		return id_ccs;
	}	

	/**
	* Configuracion Semana 
	* @param id_ccs
	*/
	public void setId_ccs(Integer id_ccs) {
		this.id_ccs = id_ccs;
	}

	/**
	* Obtiene Fecha de la Sesion 
	* @return fec
	*/
	public java.util.Date getFec(){
		return fec;
	}	

	/**
	* Fecha de la Sesion 
	* @param fec
	*/
	public void setFec(java.util.Date fec) {
		this.fec = fec;
	}

	public CursoHorario getCursoHorario(){
		return cursohorario;
	}	

	public void setCursoHorario(CursoHorario cursohorario) {
		this.cursohorario = cursohorario;
	}
	public UnidadSesion getUnidadSesion(){
		return unidadsesion;
	}	

	public void setUnidadSesion(UnidadSesion unidadsesion) {
		this.unidadsesion = unidadsesion;
	}
	public ConfSemanas getConfSemanas(){
		return confsemanas;
	}	

	public void setConfSemanas(ConfSemanas confsemanas) {
		this.confsemanas = confsemanas;
	}
}