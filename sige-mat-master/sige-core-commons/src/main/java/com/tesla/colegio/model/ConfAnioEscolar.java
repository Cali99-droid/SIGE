package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla col_conf_anio_escolar
 * @author MV
 *
 */
public class ConfAnioEscolar extends EntidadBase{

	public final static String TABLA = "col_conf_anio_escolar";
	private Integer id;
	private Integer id_anio;
	private Integer nro_sem;
	private Anio anio;	

	public ConfAnioEscolar(){
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
	* Obtiene Ao academico 
	* @return id_anio
	*/
	public Integer getId_anio(){
		return id_anio;
	}	

	/**
	* Ao academico 
	* @param id_anio
	*/
	public void setId_anio(Integer id_anio) {
		this.id_anio = id_anio;
	}

	/**
	* Obtiene Nmero de Semanas del Ao Escolar 
	* @return nro_sem
	*/
	public Integer getNro_sem(){
		return nro_sem;
	}	

	/**
	* Nmero de Semanas del Ao Escolar 
	* @param nro_sem
	*/
	public void setNro_sem(Integer nro_sem) {
		this.nro_sem = nro_sem;
	}

	public Anio getAnio(){
		return anio;
	}	

	public void setAnio(Anio anio) {
		this.anio = anio;
	}
}