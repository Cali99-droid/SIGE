package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

import java.math.BigDecimal;
import java.util.List;

/**
 * Clase entidad que representa a la tabla col_curso_subtema
 * @author MV
 *
 */
public class CursoSubtema extends EntidadBase{

	public final static String TABLA = "col_curso_subtema";
	private Integer id;
	private Integer id_anio;
	private Integer id_niv;
	private Integer id_gra;
	private Integer id_cur;
	private Integer id_sub;
	private Integer id_tem;
	private BigDecimal dur;
	private Anio anio;	
	private Nivel nivel;	
	private Grad grad;	
	private Curso curso;	
	private Subtema subtema;
	private Area area;
	private Tema tema;
	private List<UnidadTema> unidadtemas;
	private List<SesionTema> sesiontemas;

	public CursoSubtema(){
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
	* Obtiene Nivel al que pertenece el subtema 
	* @return id_niv
	*/
	public Integer getId_niv(){
		return id_niv;
	}	

	/**
	* Nivel al que pertenece el subtema 
	* @param id_niv
	*/
	public void setId_niv(Integer id_niv) {
		this.id_niv = id_niv;
	}

	/**
	* Obtiene Grado Acadmico al que pertenece el subtema 
	* @return id_gra
	*/
	public Integer getId_gra(){
		return id_gra;
	}	

	/**
	* Grado Acadmico al que pertenece el subtema 
	* @param id_gra
	*/
	public void setId_gra(Integer id_gra) {
		this.id_gra = id_gra;
	}

	/**
	* Obtiene Curso al que pertenece el subtema 
	* @return id_cur
	*/
	public Integer getId_cur(){
		return id_cur;
	}	

	/**
	* Curso al que pertenece el subtema 
	* @param id_cur
	*/
	public void setId_cur(Integer id_cur) {
		this.id_cur = id_cur;
	}

	/**
	* Obtiene Subtema 
	* @return id_sub
	*/
	public Integer getId_sub(){
		return id_sub;
	}	

	/**
	* Subtema 
	* @param id_sub
	*/
	public void setId_sub(Integer id_sub) {
		this.id_sub = id_sub;
	}

	/**
	* Obtiene Duracion en semanas 
	* @return dur
	*/
	public BigDecimal getDur(){
		return dur;
	}	

	/**
	* Duracion en semanas 
	* @param dur
	*/
	public void setDur(BigDecimal dur) {
		this.dur = dur;
	}

	public Anio getAnio(){
		return anio;
	}	

	public void setAnio(Anio anio) {
		this.anio = anio;
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
	public Curso getCurso(){
		return curso;
	}	

	public void setCurso(Curso curso) {
		this.curso = curso;
	}
	public Subtema getSubtema(){
		return subtema;
	}	

	public void setSubtema(Subtema subtema) {
		this.subtema = subtema;
	}
	/**
	* Obtiene lista de La unidad tiene campo temtico exclusivos 
	*/
	public List<UnidadTema> getUnidadTemas() {
		return unidadtemas;
	}

	/**
	* Seta Lista de La unidad tiene campo temtico exclusivos 
	* @param unidadtemas
	*/	
	public void setUnidadTema(List<UnidadTema> unidadtemas) {
		this.unidadtemas = unidadtemas;
	}
	/**
	* Obtiene lista de Campo Tematico por Sesion 
	*/
	public List<SesionTema> getSesionTemas() {
		return sesiontemas;
	}

	/**
	* Seta Lista de Campo Tematico por Sesion 
	* @param sesiontemas
	*/	
	public void setSesionTema(List<SesionTema> sesiontemas) {
		this.sesiontemas = sesiontemas;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public Tema getTema() {
		return tema;
	}

	public void setTema(Tema tema) {
		this.tema = tema;
	}

	public Integer getId_tem() {
		return id_tem;
	}

	public void setId_tem(Integer id_tem) {
		this.id_tem = id_tem;
	}
}