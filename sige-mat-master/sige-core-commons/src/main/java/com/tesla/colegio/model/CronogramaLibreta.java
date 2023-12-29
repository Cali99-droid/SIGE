package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Clase entidad que representa a la tabla not_cronograma_libreta
 * @author MV
 *
 */
public class CronogramaLibreta extends EntidadBase{

	public final static String TABLA = "not_cronograma_libreta";
	private Integer id;
	private Integer id_anio;
	private Integer id_niv;
	private Integer id_cpu;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date fec_ini;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date fec_fin;
	private Anio anio;	
	private Nivel nivel;	
	private PerUni perUni;	

	public CronogramaLibreta(){
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
	* Obtiene Nivel 
	* @return id_niv
	*/
	public Integer getId_niv(){
		return id_niv;
	}	

	/**
	* Nivel 
	* @param id_niv
	*/
	public void setId_niv(Integer id_niv) {
		this.id_niv = id_niv;
	}


	/**
	* Obtiene Desde 
	* @return fec_ini
	*/
	public java.util.Date getFec_ini(){
		return fec_ini;
	}	

	/**
	* Desde 
	* @param fec_ini
	*/
	public void setFec_ini(java.util.Date fec_ini) {
		this.fec_ini = fec_ini;
	}

	/**
	* Obtiene Hasta 
	* @return fec_fin
	*/
	public java.util.Date getFec_fin(){
		return fec_fin;
	}	

	/**
	* Hasta 
	* @param fec_fin
	*/
	public void setFec_fin(java.util.Date fec_fin) {
		this.fec_fin = fec_fin;
	}

	public Anio getAnio(){
		return anio;
	}	

	public void setAnio(Anio anio) {
		this.anio = anio;
	}
	public Nivel getNivel(){
		return nivel;
	}	

	public void setNivel(Nivel nivel) {
		this.nivel = nivel;
	}

	public Integer getId_cpu() {
		return id_cpu;
	}

	public void setId_cpu(Integer id_cpu) {
		this.id_cpu = id_cpu;
	}

	public PerUni getPerUni() {
		return perUni;
	}

	public void setPerUni(PerUni perUni) {
		this.perUni = perUni;
	}


}