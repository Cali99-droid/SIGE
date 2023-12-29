package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla cvi_periodo_aula_virtual
 * @author MV
 *
 */
public class PeriodoAulaVirtual extends EntidadBase{

	public final static String TABLA = "cvi_periodo_aula_virtual";
	private Integer id;
	private Integer id_anio;
	private Integer id_cpa;
	private Integer id_niv;
	private String abrev;
	private Integer nro_per;
	private Anio anio;	
	private PeriodoAca periodoaca;	
	private Nivel nivel;	
	private List<PeriodoCurso> periodocursos;

	public PeriodoAulaVirtual(){
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
	* Obtiene Anio 
	* @return id_anio
	*/
	public Integer getId_anio(){
		return id_anio;
	}	

	/**
	* Anio 
	* @param id_anio
	*/
	public void setId_anio(Integer id_anio) {
		this.id_anio = id_anio;
	}

	/**
	* Obtiene Periodo Academico 
	* @return id_cpa
	*/
	public Integer getId_cpa(){
		return id_cpa;
	}	

	/**
	* Periodo Academico 
	* @param id_cpa
	*/
	public void setId_cpa(Integer id_cpa) {
		this.id_cpa = id_cpa;
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
	* Obtiene Abreviatura 
	* @return abrev
	*/
	public String getAbrev(){
		return abrev;
	}	

	/**
	* Abreviatura 
	* @param abrev
	*/
	public void setAbrev(String abrev) {
		this.abrev = abrev;
	}

	/**
	* Obtiene Numero Periodo 
	* @return nro_per
	*/
	public Integer getNro_per(){
		return nro_per;
	}	

	/**
	* Numero Periodo 
	* @param nro_per
	*/
	public void setNro_per(Integer nro_per) {
		this.nro_per = nro_per;
	}

	public Anio getAnio(){
		return anio;
	}	

	public void setAnio(Anio anio) {
		this.anio = anio;
	}
	public PeriodoAca getPeriodoAca(){
		return periodoaca;
	}	

	public void setPeriodoAca(PeriodoAca periodoaca) {
		this.periodoaca = periodoaca;
	}
	public Nivel getNivel(){
		return nivel;
	}	

	public void setNivel(Nivel nivel) {
		this.nivel = nivel;
	}
	/**
	* Obtiene lista de Periodo Curso 
	*/
	public List<PeriodoCurso> getPeriodoCursos() {
		return periodocursos;
	}

	/**
	* Seta Lista de Periodo Curso 
	* @param periodocursos
	*/	
	public void setPeriodoCurso(List<PeriodoCurso> periodocursos) {
		this.periodocursos = periodocursos;
	}
}