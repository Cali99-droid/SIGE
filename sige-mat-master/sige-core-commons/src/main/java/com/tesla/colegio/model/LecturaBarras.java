package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Clase entidad que representa a la tabla asi_lectura_barras
 * @author MV
 *
 */
public class LecturaBarras extends EntidadBase{

	public final static String TABLA = "asi_lectura_barras";
	private Integer id;
	private String codigo;
	private Integer id_per;
	private String fecha_ori;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date fecha;
	private String asistencia;
	private String observacion;
	public LecturaBarras(){
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
	* Obtiene Codigo 
	* @return codigo
	*/
	public String getCodigo(){
		return codigo;
	}	

	/**
	* Codigo 
	* @param codigo
	*/
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	/**
	* Obtiene Periodo 
	* @return id_per
	*/
	public Integer getId_per(){
		return id_per;
	}	

	/**
	* Periodo 
	* @param id_per
	*/
	public void setId_per(Integer id_per) {
		this.id_per = id_per;
	}

	/**
	* Obtiene Fecha original de la lectora de barras 
	* @return fecha_ori
	*/
	public String getFecha_ori(){
		return fecha_ori;
	}	

	/**
	* Fecha original de la lectora de barras 
	* @param fecha_ori
	*/
	public void setFecha_ori(String fecha_ori) {
		this.fecha_ori = fecha_ori;
	}

	/**
	* Obtiene Codigo 
	* @return fecha
	*/
	public java.util.Date getFecha(){
		return fecha;
	}	

	/**
	* Codigo 
	* @param fecha
	*/
	public void setFecha(java.util.Date fecha) {
		this.fecha = fecha;
	}

	public String getAsistencia() {
		return asistencia;
	}

	public void setAsistencia(String asistencia) {
		this.asistencia = asistencia;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}



}