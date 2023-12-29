package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * Clase entidad que representa a la tabla col_anio
 * @author MV
 *
 */
public class Anio extends EntidadBase{

	public final static String TABLA = "col_anio";
	private Integer id;
	private String nom;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date fec_ini;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date fec_fin;
	private List<Cronograma> cronogramas;
	private List<ConfFechas> conffechass;
	private List<Periodo> periodos;
	private List<GradoHorario> gradohorarios;
	private List<AreaAnio> areaanios;
	private List<CursoHorario> cursohorarios;
	private List<Indicador> indicadors;
	private List<CursoSubtema> cursosubtemas;
	private List<PerUni> perunis;

	public Anio(){
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
	* Obtiene Ao 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Ao 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	* Obtiene lista de Cronograma de matrcula 
	*/
	public List<Cronograma> getCronogramas() {
		return cronogramas;
	}

	/**
	* Seta Lista de Cronograma de matrcula 
	* @param cronogramas
	*/	
	public void setCronograma(List<Cronograma> cronogramas) {
		this.cronogramas = cronogramas;
	}
	/**
	* Obtiene lista de Cronograma de matrcula 
	*/
	public List<ConfFechas> getConfFechass() {
		return conffechass;
	}

	/**
	* Seta Lista de Cronograma de matrcula 
	* @param conffechass
	*/	
	public void setConfFechas(List<ConfFechas> conffechass) {
		this.conffechass = conffechass;
	}
	/**
	* Obtiene lista de Periodo de estudio 
	*/
	public List<Periodo> getPeriodos() {
		return periodos;
	}

	/**
	* Seta Lista de Periodo de estudio 
	* @param periodos
	*/	
	public void setPeriodo(List<Periodo> periodos) {
		this.periodos = periodos;
	}
	/**
	* Obtiene lista de Horario por grado 
	*/
	public List<GradoHorario> getGradoHorarios() {
		return gradohorarios;
	}

	/**
	* Seta Lista de Horario por grado 
	* @param gradohorarios
	*/	
	public void setGradoHorario(List<GradoHorario> gradohorarios) {
		this.gradohorarios = gradohorarios;
	}
	
	/**
	* Obtiene lista de reas Educativas 
	*/
	public List<AreaAnio> getAreaAnios() {
		return areaanios;
	}

	/**
	* Seta Lista de reas Educativas 
	* @param areaanios
	*/	
	public void setAreaAnio(List<AreaAnio> areaanios) {
		this.areaanios = areaanios;
	}
	/**
	* Obtiene lista de Curso Horario 
	*/
	public List<CursoHorario> getCursoHorarios() {
		return cursohorarios;
	}

	/**
	* Seta Lista de Curso Horario 
	* @param cursohorarios
	*/	
	public void setCursoHorario(List<CursoHorario> cursohorarios) {
		this.cursohorarios = cursohorarios;
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
	* Obtiene lista de Periodo Unidad 
	*/
	public List<PerUni> getPerUnis() {
		return perunis;
	}

	/**
	* Seta Lista de Periodo Unidad 
	* @param perunis
	*/	
	public void setPerUni(List<PerUni> perunis) {
		this.perunis = perunis;
	}

	public java.util.Date getFec_ini() {
		return fec_ini;
	}

	public void setFec_ini(java.util.Date fec_ini) {
		this.fec_ini = fec_ini;
	}

	public java.util.Date getFec_fin() {
		return fec_fin;
	}

	public void setFec_fin(java.util.Date fec_fin) {
		this.fec_fin = fec_fin;
	}
}