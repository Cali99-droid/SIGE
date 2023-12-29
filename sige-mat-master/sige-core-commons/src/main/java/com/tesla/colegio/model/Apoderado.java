package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla alu_apoderado
 * @author MV
 *
 */
public class Apoderado extends EntidadBase{
	
	private int id;
	private int id_gru_fam;
	private String anio;

	public Apoderado(){
	}

	/**
	* Obtiene Llave Principal 
	* @return id
	*/
	public int getId(){
		return id;
	}	

	/**
	* Llave Principal 
	* @param id
	*/
	public void setId(int id) {
		this.id = id;
	}

	/**
	* Obtiene Grupo familiar familiar 
	* @return id_gru_fam
	*/
	public int getId_gru_fam(){
		return id_gru_fam;
	}	

	/**
	* Grupo familiar familiar 
	* @param id_gru_fam
	*/
	public void setId_gru_fam(int id_gru_fam) {
		this.id_gru_fam = id_gru_fam;
	}

	/**
	* Obtiene Anio de matricula 
	* @return anio
	*/
	public String getAnio(){
		return anio;
	}	

	/**
	* Anio de matricula 
	* @param anio
	*/
	public void setAnio(String anio) {
		this.anio = anio;
	}


}

