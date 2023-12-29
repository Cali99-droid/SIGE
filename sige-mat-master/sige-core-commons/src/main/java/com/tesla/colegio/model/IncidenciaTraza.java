package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla con_incidencia_traza
 * @author MV
 *
 */
public class IncidenciaTraza extends EntidadBase{

	public final static String TABLA = "con_incidencia_traza";
	private Integer id;
	private Integer id_inc;
	private Integer id_cec;
	private Incidencia incidencia;	
	private EstadoConductual estadoconductual;	

	public IncidenciaTraza(){
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
	* Obtiene Incidencia 
	* @return id_inc
	*/
	public Integer getId_inc(){
		return id_inc;
	}	

	/**
	* Incidencia 
	* @param id_inc
	*/
	public void setId_inc(Integer id_inc) {
		this.id_inc = id_inc;
	}

	/**
	* Obtiene Estado Conductual 
	* @return id_cec
	*/
	public Integer getId_cec(){
		return id_cec;
	}	

	/**
	* Estado Conductual 
	* @param id_cec
	*/
	public void setId_cec(Integer id_cec) {
		this.id_cec = id_cec;
	}

	public Incidencia getIncidencia(){
		return incidencia;
	}	

	public void setIncidencia(Incidencia incidencia) {
		this.incidencia = incidencia;
	}
	public EstadoConductual getEstadoConductual(){
		return estadoconductual;
	}	

	public void setEstadoConductual(EstadoConductual estadoconductual) {
		this.estadoconductual = estadoconductual;
	}
}