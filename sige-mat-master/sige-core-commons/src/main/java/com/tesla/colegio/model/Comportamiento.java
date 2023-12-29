package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

import java.math.BigDecimal;
import java.util.List;

/**
 * Clase entidad que representa a la tabla not_comportamiento
 * @author MV
 *
 */
public class Comportamiento extends EntidadBase{

	public final static String TABLA = "not_comportamiento";
	private Integer id;
	private Integer id_tra;
	private Integer id_alu;
	private Integer id_au;
	private Integer id_cpu;
	private BigDecimal prom;
	private Trabajador trabajador;	
	private Alumno alumno;	
	private Aula aula;	
	private PerUni peruni;	
	private List<CapCom> capcoms;

	public Comportamiento(){
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
	* Obtiene Trabajador 
	* @return id_tra
	*/
	public Integer getId_tra(){
		return id_tra;
	}	

	/**
	* Trabajador 
	* @param id_tra
	*/
	public void setId_tra(Integer id_tra) {
		this.id_tra = id_tra;
	}

	/**
	* Obtiene Alumno 
	* @return id_alu
	*/
	public Integer getId_alu(){
		return id_alu;
	}	

	/**
	* Alumno 
	* @param id_alu
	*/
	public void setId_alu(Integer id_alu) {
		this.id_alu = id_alu;
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
	* Obtiene Periodo Academico 
	* @return id_cpu
	*/
	public Integer getId_cpu(){
		return id_cpu;
	}	

	/**
	* Periodo Academico 
	* @param id_cpu
	*/
	public void setId_cpu(Integer id_cpu) {
		this.id_cpu = id_cpu;
	}

	

	public Trabajador getTrabajador(){
		return trabajador;
	}	

	public void setTrabajador(Trabajador trabajador) {
		this.trabajador = trabajador;
	}
	public Alumno getAlumno(){
		return alumno;
	}	

	public void setAlumno(Alumno alumno) {
		this.alumno = alumno;
	}
	public Aula getAula(){
		return aula;
	}	

	public void setAula(Aula aula) {
		this.aula = aula;
	}
	public PerUni getPerUni(){
		return peruni;
	}	

	public void setPerUni(PerUni peruni) {
		this.peruni = peruni;
	}
	/**
	* Obtiene lista de Nota Capacidad Comportamiento 
	*/
	public List<CapCom> getCapComs() {
		return capcoms;
	}

	/**
	* Seta Lista de Nota Capacidad Comportamiento 
	* @param capcoms
	*/	
	public void setCapCom(List<CapCom> capcoms) {
		this.capcoms = capcoms;
	}

	public BigDecimal getProm() {
		return prom;
	}

	public void setProm(BigDecimal prom) {
		this.prom = prom;
	}
}