package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Clase entidad que representa a la tabla col_ciclo_turno
 * @author MV
 *
 */
public class CicloTurno extends EntidadBase{

	public final static String TABLA = "col_ciclo_turno";
	private Integer id;
	private Integer id_cic;
	private Integer id_tur;
	private String hor_ini;
	private String hor_fin;
	private Ciclo ciclo;	
	private Turno turno;	

	public CicloTurno(){
	}

	/**
	* Obtiene Llave Principal 
	* @return id
	*/
	public Integer getId(){
		return id;
	}	

	/**
	* Llave Principal 
	* @param id
	*/
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	* Obtiene Ciclo 
	* @return id_cic
	*/
	public Integer getId_cic(){
		return id_cic;
	}	

	/**
	* Ciclo 
	* @param id_cic
	*/
	public void setId_cic(Integer id_cic) {
		this.id_cic = id_cic;
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

	

	public Ciclo getCiclo(){
		return ciclo;
	}	

	public void setCiclo(Ciclo ciclo) {
		this.ciclo = ciclo;
	}
	public Turno getTurno(){
		return turno;
	}	

	public void setTurno(Turno turno) {
		this.turno = turno;
	}

	public String getHor_ini() {
		return hor_ini;
	}

	public void setHor_ini(String hor_ini) {
		this.hor_ini = hor_ini;
	}

	public String getHor_fin() {
		return hor_fin;
	}

	public void setHor_fin(String hor_fin) {
		this.hor_fin = hor_fin;
	}
}