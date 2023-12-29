package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla cvi_usuario_campus
 * @author MV
 *
 */
public class UsuarioCampus extends EntidadBase{

	public final static String TABLA = "cvi_usuario_campus";
	private Integer id;
	private Integer id_cvic;
	private String usr;
	private String psw;
	private Integer id_error;
	private InscripcionCampus inscripcioncampus;	

	public UsuarioCampus(){
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
	* Obtiene Inscripcion 
	* @return id_cvic
	*/
	public Integer getId_cvic(){
		return id_cvic;
	}	

	/**
	* Inscripcion 
	* @param id_cvic
	*/
	public void setId_cvic(Integer id_cvic) {
		this.id_cvic = id_cvic;
	}

	/**
	* Obtiene Usuario 
	* @return usr
	*/
	public String getUsr(){
		return usr;
	}	

	/**
	* Usuario 
	* @param usr
	*/
	public void setUsr(String usr) {
		this.usr = usr;
	}

	/**
	* Obtiene Password 
	* @return psw
	*/
	public String getPsw(){
		return psw;
	}	

	/**
	* Password 
	* @param psw
	*/
	public void setPsw(String psw) {
		this.psw = psw;
	}

	/**
	* Obtiene Error 
	* @return id_error
	*/
	public Integer getId_error(){
		return id_error;
	}	

	/**
	* Error 
	* @param id_error
	*/
	public void setId_error(Integer id_error) {
		this.id_error = id_error;
	}

	public InscripcionCampus getInscripcionCampus(){
		return inscripcioncampus;
	}	

	public void setInscripcionCampus(InscripcionCampus inscripcioncampus) {
		this.inscripcioncampus = inscripcioncampus;
	}
}