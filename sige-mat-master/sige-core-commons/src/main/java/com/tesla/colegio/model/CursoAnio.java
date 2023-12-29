package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla col_curso_anio
 * @author MV
 *
 */
public class CursoAnio extends EntidadBase{

	public final static String TABLA = "col_curso_anio";
	private Integer id;
	private Integer id_per;
	private Integer id_cic;
	private Integer id_gra;
	private Integer id_caa;
	private Integer id_cur;
	private Integer peso;
	private Integer orden;
	private String flg_prom;
	private String cod_classroom;
	private Periodo periodo;	
	private Grad grad;	
	private AreaAnio areaanio;	
	private Curso curso;
	private Sucursal sucursal;
	private Area area;
	private Nivel nivel;
	private List<CursoAula> cursoaulas;
	private List<Competencia> competencias;
	private List<CursoUnidad> cursounidads;

	public CursoAnio(){
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
	* Obtiene Periodo Escolar 
	* @return id_per
	*/
	public Integer getId_per(){
		return id_per;
	}	

	/**
	* Periodo Escolar 
	* @param id_per
	*/
	public void setId_per(Integer id_per) {
		this.id_per = id_per;
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
	* Obtiene Peso de evaluacin 
	* @return peso
	*/
	public Integer getPeso(){
		return peso;
	}	

	/**
	* Peso de evaluacin 
	* @param peso
	*/
	public void setPeso(Integer peso) {
		this.peso = peso;
	}

	/**
	* Obtiene Orden 
	* @return orden
	*/
	public Integer getOrden(){
		return orden;
	}	

	/**
	* Orden 
	* @param orden
	*/
	public void setOrden(Integer orden) {
		this.orden = orden;
	}

	/**
	* Obtiene Si el curso de promediara por area 
	* @return flg_prom
	*/
	public String getFlg_prom(){
		return flg_prom;
	}	

	/**
	* Si el curso de promediara por area 
	* @param flg_prom
	*/
	public void setFlg_prom(String flg_prom) {
		this.flg_prom = flg_prom;
	}

	public Periodo getPeriodo(){
		return periodo;
	}	

	public void setPeriodo(Periodo periodo) {
		this.periodo = periodo;
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
	/**
	* Obtiene lista de Curso Aula 
	*/
	public List<CursoAula> getCursoAulas() {
		return cursoaulas;
	}

	/**
	* Seta Lista de Curso Aula 
	* @param cursoaulas
	*/	
	public void setCursoAula(List<CursoAula> cursoaulas) {
		this.cursoaulas = cursoaulas;
	}
	/**
	* Obtiene lista de Competencia 
	*/
	public List<Competencia> getCompetencias() {
		return competencias;
	}

	/**
	* Seta Lista de Competencia 
	* @param competencias
	*/	
	public void setCompetencia(List<Competencia> competencias) {
		this.competencias = competencias;
	}
	/**
	* Obtiene lista de Unidad Didctica 
	*/
	public List<CursoUnidad> getCursoUnidads() {
		return cursounidads;
	}

	/**
	* Seta Lista de Unidad Didctica 
	* @param cursounidads
	*/	
	public void setCursoUnidad(List<CursoUnidad> cursounidads) {
		this.cursounidads = cursounidads;
	}

	public Sucursal getSucursal() {
		return sucursal;
	}

	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
	}

	public Nivel getNivel() {
		return nivel;
	}

	public void setNivel(Nivel nivel) {
		this.nivel = nivel;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public Integer getId_cic() {
		return id_cic;
	}

	public void setId_cic(Integer id_cic) {
		this.id_cic = id_cic;
	}

	public String getCod_classroom() {
		return cod_classroom;
	}

	public void setCod_classroom(String cod_classroom) {
		this.cod_classroom = cod_classroom;
	}

}