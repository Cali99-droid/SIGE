package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Clase entidad que representa a la tabla aca_festivo
 * @author MV
 *
 */
public class Festivo extends EntidadBase{

	public final static String TABLA = "aca_festivo";
	private Integer id;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date dia;
	private String motivo;

	public Festivo(){
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
	* Obtiene Da Festivo 
	* @return dia
	*/
	public java.util.Date getDia(){
		return dia;
	}	

	/**
	* Da Festivo 
	* @param dia
	*/
	public void setDia(java.util.Date dia) {
		this.dia = dia;
	}

	/**
	* Obtiene Motivo 
	* @return motivo
	*/
	public String getMotivo(){
		return motivo;
	}	

	/**
	* Motivo 
	* @param motivo
	*/
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

}