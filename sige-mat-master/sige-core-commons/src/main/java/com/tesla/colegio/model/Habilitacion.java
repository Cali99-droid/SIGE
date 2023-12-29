package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Clase entidad que representa a la tabla mat_habilitacion
 * @author MV
 *
 */
public class Habilitacion extends EntidadBase{

	public final static String TABLA = "mat_habilitacion";
	private Integer id;
	private Integer id_alu;
	private Integer id_anio;
	private String hab;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date fec_fin;
	private Alumno alumno;	
	private Anio anio;
	private Trabajador trabajador;

	public Habilitacion(){
	}

	/**
	* Obtiene Llave Principal 
	* @return id
	*/
	public Integer getId(){
		return id;
	}	

	/**
	* Llave Principal 
	* @param id
	*/
	public void setId(Integer id) {
		this.id = id;
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
	* Obtiene Habilitado Matricula? 
	* @return hab
	*/
	public String getHab(){
		return hab;
	}	

	/**
	* Habilitado Matricula? 
	* @param hab
	*/
	public void setHab(String hab) {
		this.hab = hab;
	}

	/**
	* Obtiene Fecha Fin para matrcula 
	* @return fec_fin
	*/
	public java.util.Date getFec_fin(){
		return fec_fin;
	}	

	/**
	* Fecha Fin para matrcula 
	* @param fec_fin
	*/
	public void setFec_fin(java.util.Date fec_fin) {
		this.fec_fin = fec_fin;
	}

	public Alumno getAlumno(){
		return alumno;
	}	

	public void setAlumno(Alumno alumno) {
		this.alumno = alumno;
	}
	public Anio getAnio(){
		return anio;
	}	

	public void setAnio(Anio anio) {
		this.anio = anio;
	}

	public Trabajador getTrabajador() {
		return trabajador;
	}

	public void setTrabajador(Trabajador trabajador) {
		this.trabajador = trabajador;
	}
}