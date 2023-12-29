package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Clase entidad que representa a la tabla not_cap_com
 * @author MV
 *
 */
public class CapCom extends EntidadBase{

	public final static String TABLA = "not_cap_com";
	private Integer id;
	private Integer id_nc;
	private Integer id_cap;
	private Integer nota;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date fec;
	private Comportamiento comportamiento;	
	private Capacidad capacidad;	

	public CapCom(){
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
	* Obtiene Nota Comportamiento 
	* @return id_nc
	*/
	public Integer getId_nc(){
		return id_nc;
	}	

	/**
	* Nota Comportamiento 
	* @param id_nc
	*/
	public void setId_nc(Integer id_nc) {
		this.id_nc = id_nc;
	}

	/**
	* Obtiene Capacidad 
	* @return id_cap
	*/
	public Integer getId_cap(){
		return id_cap;
	}	

	/**
	* Capacidad 
	* @param id_cap
	*/
	public void setId_cap(Integer id_cap) {
		this.id_cap = id_cap;
	}

	/**
	* Obtiene Nota 
	* @return nota
	*/
	public Integer getNota(){
		return nota;
	}	

	/**
	* Nota 
	* @param nota
	*/
	public void setNota(Integer nota) {
		this.nota = nota;
	}

	/**
	* Obtiene Fecha 
	* @return fec
	*/
	public java.util.Date getFec(){
		return fec;
	}	

	/**
	* Fecha 
	* @param fec
	*/
	public void setFec(java.util.Date fec) {
		this.fec = fec;
	}

	public Comportamiento getComportamiento(){
		return comportamiento;
	}	

	public void setComportamiento(Comportamiento comportamiento) {
		this.comportamiento = comportamiento;
	}
	public Capacidad getCapacidad(){
		return capacidad;
	}	

	public void setCapacidad(Capacidad capacidad) {
		this.capacidad = capacidad;
	}
}