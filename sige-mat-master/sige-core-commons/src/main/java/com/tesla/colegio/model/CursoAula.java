package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla col_curso_aula
 * @author MV
 *
 */
public class CursoAula extends EntidadBase{

	public final static String TABLA = "col_curso_aula";
	private Integer id;
	private Integer id_caa;
	private Integer id_cua;
	private Integer id_au;
	private Integer id_tra;
	private String cod_classroom;
	private CursoAnio cursoanio;	
	private Aula aula;	
	private Trabajador trabajador;	
	private Curso curso;
	private Periodo periodo;
	private Grad grad;
	private Nivel niv;

	public CursoAula(){
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
	* Obtiene Curso Anio 
	* @return id_cua
	*/
	public Integer getId_cua(){
		return id_cua;
	}	

	/**
	* Curso Anio 
	* @param id_cua
	*/
	public void setId_cua(Integer id_cua) {
		this.id_cua = id_cua;
	}

	/**
	* Obtiene Aula 
	* @return id_au
	*/
	public Integer getId_au(){
		return id_au;
	}	

	/**
	* Aula 
	* @param id_au
	*/
	public void setId_au(Integer id_au) {
		this.id_au = id_au;
	}

	/**
	* Obtiene Docente 
	* @return id_tra
	*/
	public Integer getId_tra(){
		return id_tra;
	}	

	/**
	* Docente 
	* @param id_tra
	*/
	public void setId_tra(Integer id_tra) {
		this.id_tra = id_tra;
	}

	public CursoAnio getCursoAnio(){
		return cursoanio;
	}	

	public void setCursoAnio(CursoAnio cursoanio) {
		this.cursoanio = cursoanio;
	}
	public Aula getAula(){
		return aula;
	}	

	public void setAula(Aula aula) {
		this.aula = aula;
	}
	public Trabajador getTrabajador(){
		return trabajador;
	}	

	public void setTrabajador(Trabajador trabajador) {
		this.trabajador = trabajador;
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	public Periodo getPeriodo() {
		return periodo;
	}

	public void setPeriodo(Periodo periodo) {
		this.periodo = periodo;
	}

	public Grad getGrad() {
		return grad;
	}

	public void setGrad(Grad grad) {
		this.grad = grad;
	}

	public Nivel getNiv() {
		return niv;
	}

	public void setNiv(Nivel niv) {
		this.niv = niv;
	}

	public String getCod_classroom() {
		return cod_classroom;
	}

	public void setCod_classroom(String cod_classroom) {
		this.cod_classroom = cod_classroom;
	}

	public Integer getId_caa() {
		return id_caa;
	}

	public void setId_caa(Integer id_caa) {
		this.id_caa = id_caa;
	}

}