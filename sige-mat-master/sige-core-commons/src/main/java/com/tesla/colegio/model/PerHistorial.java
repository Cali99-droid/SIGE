package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla col_per_historial
 * @author MV
 *
 */
public class PerHistorial extends EntidadBase{

	public final static String TABLA = "col_per_historial";
	private Integer id;
	private Integer id_per;
	private Integer id_anio;
	private Integer id_eci;
	private String corr_actual;
	private String corr_antiguo;
	private String cel_actual;
	private String cel_antiguo;
	private Persona persona;	
	private Anio anio;	
	private EstCivil estcivil;	

	public PerHistorial(){
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
	* Obtiene Estado Civil de la persona 
	* @return id_eci
	*/
	public Integer getId_eci(){
		return id_eci;
	}	

	/**
	* Estado Civil de la persona 
	* @param id_eci
	*/
	public void setId_eci(Integer id_eci) {
		this.id_eci = id_eci;
	}

	/**
	* Obtiene Correo Actual 
	* @return corr_actual
	*/
	public String getCorr_actual(){
		return corr_actual;
	}	

	/**
	* Correo Actual 
	* @param corr_actual
	*/
	public void setCorr_actual(String corr_actual) {
		this.corr_actual = corr_actual;
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
	* Obtiene Celular Actual 
	* @return cel_actual
	*/
	public String getCel_actual(){
		return cel_actual;
	}	

	/**
	* Celular Actual 
	* @param cel_actual
	*/
	public void setCel_actual(String cel_actual) {
		this.cel_actual = cel_actual;
	}

	/**
	* Obtiene Celular Antiguo 
	* @return cel_antiguo
	*/
	public String getCel_antiguo(){
		return cel_antiguo;
	}	

	/**
	* Celular Antiguo 
	* @param cel_antiguo
	*/
	public void setCel_antiguo(String cel_antiguo) {
		this.cel_antiguo = cel_antiguo;
	}

	public Persona getPersona(){
		return persona;
	}	

	public void setPersona(Persona persona) {
		this.persona = persona;
	}
	public Anio getAnio(){
		return anio;
	}	

	public void setAnio(Anio anio) {
		this.anio = anio;
	}
	public EstCivil getEstCivil(){
		return estcivil;
	}	

	public void setEstCivil(EstCivil estcivil) {
		this.estcivil = estcivil;
	}
}