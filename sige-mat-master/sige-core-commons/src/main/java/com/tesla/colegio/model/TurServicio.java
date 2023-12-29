package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla col_tur_servicio
 * @author MV
 *
 */
public class TurServicio extends EntidadBase{

	public final static String TABLA = "col_tur_servicio";
	private Integer id;
	private Integer id_tur;
	private Integer id_srv;
	private Turno turno;	
	private Servicio servicio;	

	public TurServicio(){
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
	* Obtiene Turno 
	* @return id_tur
	*/
	public Integer getId_tur(){
		return id_tur;
	}	

	/**
	* Turno 
	* @param id_tur
	*/
	public void setId_tur(Integer id_tur) {
		this.id_tur = id_tur;
	}

	/**
	* Obtiene Servicio 
	* @return id_srv
	*/
	public Integer getId_srv(){
		return id_srv;
	}	

	/**
	* Servicio 
	* @param id_srv
	*/
	public void setId_srv(Integer id_srv) {
		this.id_srv = id_srv;
	}

	public Turno getTurno(){
		return turno;
	}	

	public void setTurno(Turno turno) {
		this.turno = turno;
	}
	public Servicio getServicio(){
		return servicio;
	}	

	public void setServicio(Servicio servicio) {
		this.servicio = servicio;
	}
}