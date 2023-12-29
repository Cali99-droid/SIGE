package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Clase entidad que representa a la tabla col_conf_semanas
 * @author MV
 *
 */
public class ConfSemanas extends EntidadBase{

	public final static String TABLA = "col_conf_semanas";
	private Integer id;
	private Integer id_cnf_anio;
	private Integer nro_sem;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date fec_ini;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date fec_fin;
	private ConfAnioEscolar confanioescolar;	

	public ConfSemanas(){
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
	* Obtiene Configuracin ao escolar 
	* @return id_cnf_anio
	*/
	public Integer getId_cnf_anio(){
		return id_cnf_anio;
	}	

	/**
	* Configuracin ao escolar 
	* @param id_cnf_anio
	*/
	public void setId_cnf_anio(Integer id_cnf_anio) {
		this.id_cnf_anio = id_cnf_anio;
	}

	/**
	* Obtiene Nmero de Semana 
	* @return nro_sem
	*/
	public Integer getNro_sem(){
		return nro_sem;
	}	

	/**
	* Nmero de Semana 
	* @param nro_sem
	*/
	public void setNro_sem(Integer nro_sem) {
		this.nro_sem = nro_sem;
	}

	/**
	* Obtiene Fecha de Inicio 
	* @return fec_ini
	*/
	public java.util.Date getFec_ini(){
		return fec_ini;
	}	

	/**
	* Fecha de Inicio 
	* @param fec_ini
	*/
	public void setFec_ini(java.util.Date fec_ini) {
		this.fec_ini = fec_ini;
	}

	/**
	* Obtiene Fecha Final 
	* @return fec_fin
	*/
	public java.util.Date getFec_fin(){
		return fec_fin;
	}	

	/**
	* Fecha Final 
	* @param fec_fin
	*/
	public void setFec_fin(java.util.Date fec_fin) {
		this.fec_fin = fec_fin;
	}

	public ConfAnioEscolar getConfAnioEscolar(){
		return confanioescolar;
	}	

	public void setConfAnioEscolar(ConfAnioEscolar confanioescolar) {
		this.confanioescolar = confanioescolar;
	}
}