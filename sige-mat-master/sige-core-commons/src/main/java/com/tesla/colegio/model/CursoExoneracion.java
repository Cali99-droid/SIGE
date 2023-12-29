package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Clase entidad que representa a la tabla not_curso_exoneracion
 * @author MV
 *
 */
public class CursoExoneracion extends EntidadBase{

	public final static String TABLA = "not_curso_exoneracion";
	private Integer id;
	private Integer id_cur;
	private Integer id_mat;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date fecha;
	private String motivo;
	private String resol;
	private Curso curso;	
	private Matricula matricula;	

	public CursoExoneracion(){
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
	* Obtiene Matrcula 
	* @return id_mat
	*/
	public Integer getId_mat(){
		return id_mat;
	}	

	/**
	* Matrcula 
	* @param id_mat
	*/
	public void setId_mat(Integer id_mat) {
		this.id_mat = id_mat;
	}

	/**
	* Obtiene Fecha de exoneracin 
	* @return fecha
	*/
	public java.util.Date getFecha(){
		return fecha;
	}	

	/**
	* Fecha de exoneracin 
	* @param fecha
	*/
	public void setFecha(java.util.Date fecha) {
		this.fecha = fecha;
	}

	/**
	* Obtiene Motivo 
	* @return motivo
	*/
	public String getMotivo(){
		return motivo;
	}	

	/**
	* Motivo 
	* @param motivo
	*/
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	/**
	* Obtiene Resolucin 
	* @return resol
	*/
	public String getResol(){
		return resol;
	}	

	/**
	* Resolucin 
	* @param resol
	*/
	public void setResol(String resol) {
		this.resol = resol;
	}

	public Curso getCurso(){
		return curso;
	}	

	public void setCurso(Curso curso) {
		this.curso = curso;
	}
	public Matricula getMatricula(){
		return matricula;
	}	

	public void setMatricula(Matricula matricula) {
		this.matricula = matricula;
	}
}