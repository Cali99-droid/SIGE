package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla col_historial_correo
 * @author MV
 *
 */
public class HistorialCorreo extends EntidadBase{

	public final static String TABLA = "col_historial_correo";
	private Integer id;
	private Integer id_per;
	private String corr_antiguo;
	private String corr_nuevo;
	private Persona persona;	

	public HistorialCorreo(){
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
	* Obtiene Persona 
	* @return id_per
	*/
	public Integer getId_per(){
		return id_per;
	}	

	/**
	* Persona 
	* @param id_per
	*/
	public void setId_per(Integer id_per) {
		this.id_per = id_per;
	}

	/**
	* Obtiene Correo Antiguo 
	* @return corr_antiguo
	*/
	public String getCorr_antiguo(){
		return corr_antiguo;
	}	

	/**
	* Correo Antiguo 
	* @param corr_antiguo
	*/
	public void setCorr_antiguo(String corr_antiguo) {
		this.corr_antiguo = corr_antiguo;
	}

	/**
	* Obtiene Correo Nuevo 
	* @return corr_nuevo
	*/
	public String getCorr_nuevo(){
		return corr_nuevo;
	}	

	/**
	* Correo Nuevo 
	* @param corr_nuevo
	*/
	public void setCorr_nuevo(String corr_nuevo) {
		this.corr_nuevo = corr_nuevo;
	}

	public Persona getPersona(){
		return persona;
	}	

	public void setPersona(Persona persona) {
		this.persona = persona;
	}
}