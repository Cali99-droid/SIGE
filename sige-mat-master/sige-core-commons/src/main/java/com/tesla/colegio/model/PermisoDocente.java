package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla col_permiso_docente
 * @author MV
 *
 */
public class PermisoDocente extends EntidadBase{

	public final static String TABLA = "col_permiso_docente";
	private Integer id;
	private Integer id_prof;
	private Integer id_cpu;
	private Integer id_au;
	private Integer dias;
	private Trabajador trabajador;	
	private PerUni peruni;	
	private Aula aula;	
	private PeriodoAca periodoaca;
	private Grad grad;
	private Nivel nivel;
	private GiroNegocio giro;

	public PermisoDocente(){
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
	* Obtiene Profesor 
	* @return id_prof
	*/
	public Integer getId_prof(){
		return id_prof;
	}	

	/**
	* Profesor 
	* @param id_prof
	*/
	public void setId_prof(Integer id_prof) {
		this.id_prof = id_prof;
	}

	/**
	* Obtiene Periodo Unidad 
	* @return id_cpu
	*/
	public Integer getId_cpu(){
		return id_cpu;
	}	

	/**
	* Periodo Unidad 
	* @param id_cpu
	*/
	public void setId_cpu(Integer id_cpu) {
		this.id_cpu = id_cpu;
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
	* Obtiene Dias 
	* @return dias
	*/
	public Integer getDias(){
		return dias;
	}	

	/**
	* Dias 
	* @param dias
	*/
	public void setDias(Integer dias) {
		this.dias = dias;
	}

	public Trabajador getTrabajador(){
		return trabajador;
	}	

	public void setTrabajador(Trabajador trabajador) {
		this.trabajador = trabajador;
	}
	public PerUni getPerUni(){
		return peruni;
	}	

	public void setPerUni(PerUni peruni) {
		this.peruni = peruni;
	}
	public Aula getAula(){
		return aula;
	}	

	public void setAula(Aula aula) {
		this.aula = aula;
	}

	public PeriodoAca getPeriodoaca() {
		return periodoaca;
	}

	public void setPeriodoaca(PeriodoAca periodoaca) {
		this.periodoaca = periodoaca;
	}

	public Grad getGrad() {
		return grad;
	}

	public void setGrad(Grad grad) {
		this.grad = grad;
	}

	public Nivel getNivel() {
		return nivel;
	}

	public void setNivel(Nivel nivel) {
		this.nivel = nivel;
	}

	public GiroNegocio getGiro() {
		return giro;
	}

	public void setGiro(GiroNegocio giro) {
		this.giro = giro;
	}
	
}