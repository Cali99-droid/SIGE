package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla col_historial_eco
 * @author MV
 *
 */
public class HistorialEco extends EntidadBase{

	public final static String TABLA = "col_historial_eco";
	private Integer id;
	private Integer id_mat;
	private Integer id_ccr_pad;
	private Integer id_ccr_mad;
	private String ult_men;
	private Integer nro_mens;
	private java.math.BigDecimal ing_fam;
	private java.math.BigDecimal puntaje;
	private Matricula matricula;	
	private CentralRiesgo centralriesgo;	

	public HistorialEco(){
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
	* Obtiene Matricula 
	* @return id_mat
	*/
	public Integer getId_mat(){
		return id_mat;
	}	

	/**
	* Matricula 
	* @param id_mat
	*/
	public void setId_mat(Integer id_mat) {
		this.id_mat = id_mat;
	}

	/**
	* Obtiene Central Riesgo Padre 
	* @return id_ccr_pad
	*/
	public Integer getId_ccr_pad(){
		return id_ccr_pad;
	}	

	/**
	* Central Riesgo Padre 
	* @param id_ccr_pad
	*/
	public void setId_ccr_pad(Integer id_ccr_pad) {
		this.id_ccr_pad = id_ccr_pad;
	}

	/**
	* Obtiene Central Riesgo Madre 
	* @return id_ccr_mad
	*/
	public Integer getId_ccr_mad(){
		return id_ccr_mad;
	}	

	/**
	* Central Riesgo Madre 
	* @param id_ccr_mad
	*/
	public void setId_ccr_mad(Integer id_ccr_mad) {
		this.id_ccr_mad = id_ccr_mad;
	}

	/**
	* Obtiene Ultima Mensualidad Cancelada 
	* @return ult_men
	*/
	public String getUlt_men(){
		return ult_men;
	}	

	/**
	* Ultima Mensualidad Cancelada 
	* @param ult_men
	*/
	public void setUlt_men(String ult_men) {
		this.ult_men = ult_men;
	}

	/**
	* Obtiene Nmero de Mensualidades que adeuda 
	* @return nro_mens
	*/
	public Integer getNro_mens(){
		return nro_mens;
	}	

	/**
	* Nmero de Mensualidades que adeuda 
	* @param nro_mens
	*/
	public void setNro_mens(Integer nro_mens) {
		this.nro_mens = nro_mens;
	}

	/**
	* Obtiene Ingresos Familiares 
	* @return ing_fam
	*/
	public java.math.BigDecimal getIng_fam(){
		return ing_fam;
	}	

	/**
	* Ingresos Familiares 
	* @param ing_fam
	*/
	public void setIng_fam(java.math.BigDecimal ing_fam) {
		this.ing_fam = ing_fam;
	}

	/**
	* Obtiene Puntaje 
	* @return puntaje
	*/
	public java.math.BigDecimal getPuntaje(){
		return puntaje;
	}	

	/**
	* Puntaje 
	* @param puntaje
	*/
	public void setPuntaje(java.math.BigDecimal puntaje) {
		this.puntaje = puntaje;
	}

	public Matricula getMatricula(){
		return matricula;
	}	

	public void setMatricula(Matricula matricula) {
		this.matricula = matricula;
	}
	public CentralRiesgo getCentralRiesgo(){
		return centralriesgo;
	}	

	public void setCentralRiesgo(CentralRiesgo centralriesgo) {
		this.centralriesgo = centralriesgo;
	}
	
}