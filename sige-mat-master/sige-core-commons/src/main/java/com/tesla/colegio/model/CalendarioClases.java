package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Clase entidad que representa a la tabla aca_calendario_clases
 * @author MV
 *
 */
public class CalendarioClases extends EntidadBase{

	public final static String TABLA = "aca_calendario_clases";
	private Integer id;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date dia;
	private String motivo;

	public CalendarioClases(){
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
	* Obtiene Da 
	* @return dia
	*/
	public java.util.Date getDia(){
		return dia;
	}	

	/**
	* Da 
	* @param dia
	*/
	public void setDia(java.util.Date dia) {
		this.dia = dia;
	}

	/**
	* Obtiene Descripcin del rgimen 
	* @return motivo
	*/
	public String getMotivo(){
		return motivo;
	}	

	/**
	* Descripcin del rgimen 
	* @param motivo
	*/
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

}