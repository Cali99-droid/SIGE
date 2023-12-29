package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla cat_grad
 * @author MV
 *
 */
public class Grad extends EntidadBase{

	public final static String TABLA = "cat_grad";
	private Integer id;
	private Integer id_nvl;
	private Integer id_gra_ant;
	private String nom;
	private String tipo;
	private String abrv;
	private String abrv_classroom;
	private Nivel nivel;	
	private Grad grad;	
	private List<Grad> grads;
	private List<Vacante> vacantes;
	private List<MatrVacante> matrvacantes;
	private List<CapacidadSetup> capacidadsetups;
	private List<Reserva> reservas;
	private List<Matricula> matriculas;
	private List<Aula> aulas;
	private List<CursoAnio> cursoanios;
	private List<Indicador> indicadors;
	private List<CursoSubtema> cursosubtemas;
	private List<CursoUnidad> cursounidads;

	public Grad(){
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
	* Obtiene Nivel educativo 
	* @return id_nvl
	*/
	public Integer getId_nvl(){
		return id_nvl;
	}	

	/**
	* Nivel educativo 
	* @param id_nvl
	*/
	public void setId_nvl(Integer id_nvl) {
		this.id_nvl = id_nvl;
	}

	/**
	* Obtiene Grado al que postula 
	* @return id_gra_ant
	*/
	public Integer getId_gra_ant(){
		return id_gra_ant;
	}	

	/**
	* Grado al que postula 
	* @param id_gra_ant
	*/
	public void setId_gra_ant(Integer id_gra_ant) {
		this.id_gra_ant = id_gra_ant;
	}

	/**
	* Obtiene Nombre del grado 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Nombre del grado 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	* Obtiene Tipo de grado 
	* @return tipo
	*/
	public String getTipo(){
		return tipo;
	}	

	/**
	* Tipo de grado 
	* @param tipo
	*/
	public void setTipo(String tipo) {
		this.tipo = tipo;
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
	/**
	* Obtiene lista de Grado Educativo 
	*/
	public List<Grad> getGrads() {
		return grads;
	}

	/**
	* Seta Lista de Grado Educativo 
	* @param grads
	*/	
	public void setGrad(List<Grad> grads) {
		this.grads = grads;
	}
	
	/**
	* Obtiene lista de Vacante 
	*/
	public List<Vacante> getVacantes() {
		return vacantes;
	}

	/**
	* Seta Lista de Vacante 
	* @param vacantes
	*/	
	public void setVacante(List<Vacante> vacantes) {
		this.vacantes = vacantes;
	}
	/**
	* Obtiene lista de Examen Vacante 
	*/
	public List<MatrVacante> getMatrVacantes() {
		return matrvacantes;
	}

	/**
	* Seta Lista de Examen Vacante 
	* @param matrvacantes
	*/	
	public void setMatrVacante(List<MatrVacante> matrvacantes) {
		this.matrvacantes = matrvacantes;
	}
	/**
	* Obtiene lista de Configuracion Capacidad 
	*/
	public List<CapacidadSetup> getCapacidadSetups() {
		return capacidadsetups;
	}

	/**
	* Seta Lista de Configuracion Capacidad 
	* @param capacidadsetups
	*/	
	public void setCapacidadSetup(List<CapacidadSetup> capacidadsetups) {
		this.capacidadsetups = capacidadsetups;
	}
	/**
	* Obtiene lista de Reserva de matrcula 
	*/
	public List<Reserva> getReservas() {
		return reservas;
	}

	/**
	* Seta Lista de Reserva de matrcula 
	* @param reservas
	*/	
	public void setReserva(List<Reserva> reservas) {
		this.reservas = reservas;
	}
	/**
	* Obtiene lista de Matricula del alumno 
	*/
	public List<Matricula> getMatriculas() {
		return matriculas;
	}

	/**
	* Seta Lista de Matricula del alumno 
	* @param matriculas
	*/	
	public void setMatricula(List<Matricula> matriculas) {
		this.matriculas = matriculas;
	}
	/**
	* Obtiene lista de Aula del colegio 
	*/
	public List<Aula> getAulas() {
		return aulas;
	}

	/**
	* Seta Lista de Aula del colegio 
	* @param aulas
	*/	
	public void setAula(List<Aula> aulas) {
		this.aulas = aulas;
	}
	/**
	* Obtiene lista de Curso 
	*/
	public List<CursoAnio> getCursoAnios() {
		return cursoanios;
	}

	/**
	* Seta Lista de Curso 
	* @param cursoanios
	*/	
	public void setCursoAnio(List<CursoAnio> cursoanios) {
		this.cursoanios = cursoanios;
	}
	/**
	* Obtiene lista de Indicador 
	*/
	public List<Indicador> getIndicadors() {
		return indicadors;
	}

	/**
	* Seta Lista de Indicador 
	* @param indicadors
	*/	
	public void setIndicador(List<Indicador> indicadors) {
		this.indicadors = indicadors;
	}
	/**
	* Obtiene lista de Curso Subtema 
	*/
	public List<CursoSubtema> getCursoSubtemas() {
		return cursosubtemas;
	}

	/**
	* Seta Lista de Curso Subtema 
	* @param cursosubtemas
	*/	
	public void setCursoSubtema(List<CursoSubtema> cursosubtemas) {
		this.cursosubtemas = cursosubtemas;
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

	public String getAbrv() {
		return abrv;
	}

	public void setAbrv(String abrv) {
		this.abrv = abrv;
	}

	public String getAbrv_classroom() {
		return abrv_classroom;
	}

	public void setAbrv_classroom(String abrv_classroom) {
		this.abrv_classroom = abrv_classroom;
	}
}