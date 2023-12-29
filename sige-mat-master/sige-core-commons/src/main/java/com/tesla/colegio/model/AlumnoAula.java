package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla col_alumno_aula
 * @author MV
 *
 */
public class AlumnoAula extends EntidadBase{

	public final static String TABLA = "col_alumno_aula";
	private Integer id;
	private Integer id_alu;
	private Integer id_au;
	private Integer id_cpu;
	private Alumno alumno;	
	private Aula aula;	
	private PerUni peruni;	

	public AlumnoAula(){
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
	* Obtiene Familiar 
	* @return id_alu
	*/
	public Integer getId_alu(){
		return id_alu;
	}	

	/**
	* Familiar 
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
	* Obtiene Periodo 
	* @return id_cpu
	*/
	public Integer getId_cpu(){
		return id_cpu;
	}	

	/**
	* Periodo 
	* @param id_cpu
	*/
	public void setId_cpu(Integer id_cpu) {
		this.id_cpu = id_cpu;
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
}