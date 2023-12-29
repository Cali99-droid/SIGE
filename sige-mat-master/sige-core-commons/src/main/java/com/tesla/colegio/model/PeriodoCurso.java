package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla cvi_periodo_curso
 * @author MV
 *
 */
public class PeriodoCurso extends EntidadBase{

	public final static String TABLA = "cvi_periodo_curso";
	private Integer id;
	private Integer id_cpv;
	private Integer id_cau;
	private Integer activo;
	private PeriodoAulaVirtual periodoaulavirtual;	
	private CursoAulaVirtual cursoaulavirtual;	

	public PeriodoCurso(){
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
	* Obtiene Periodo Aula Virtual 
	* @return id_cpv
	*/
	public Integer getId_cpv(){
		return id_cpv;
	}	

	/**
	* Periodo Aula Virtual 
	* @param id_cpv
	*/
	public void setId_cpv(Integer id_cpv) {
		this.id_cpv = id_cpv;
	}

	/**
	* Obtiene Curso Aula Virtual 
	* @return id_cau
	*/
	public Integer getId_cau(){
		return id_cau;
	}	

	/**
	* Curso Aula Virtual 
	* @param id_cau
	*/
	public void setId_cau(Integer id_cau) {
		this.id_cau = id_cau;
	}

	/**
	* Obtiene Activo? Si No 
	* @return activo
	*/
	public Integer getActivo(){
		return activo;
	}	

	/**
	* Activo? Si No 
	* @param activo
	*/
	public void setActivo(Integer activo) {
		this.activo = activo;
	}

	public PeriodoAulaVirtual getPeriodoAulaVirtual(){
		return periodoaulavirtual;
	}	

	public void setPeriodoAulaVirtual(PeriodoAulaVirtual periodoaulavirtual) {
		this.periodoaulavirtual = periodoaulavirtual;
	}
	public CursoAulaVirtual getCursoAulaVirtual(){
		return cursoaulavirtual;
	}	

	public void setCursoAulaVirtual(CursoAulaVirtual cursoaulavirtual) {
		this.cursoaulavirtual = cursoaulavirtual;
	}
}