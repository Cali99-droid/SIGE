package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Clase entidad que representa a la tabla msj_contador
 * @author MV
 *
 */
public class Contador extends EntidadBase{

	public final static String TABLA = "msj_contador";
	private Integer id;
	private Integer nro;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date fec;
	private String usr;
	private String psw;

	public Contador(){
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
	* Obtiene Numero 
	* @return nro
	*/
	public Integer getNro(){
		return nro;
	}	

	/**
	* Numero 
	* @param nro
	*/
	public void setNro(Integer nro) {
		this.nro = nro;
	}

	/**
	* Obtiene Fecha 
	* @return fec
	*/
	public java.util.Date getFec(){
		return fec;
	}	

	/**
	* Fecha 
	* @param fec
	*/
	public void setFec(java.util.Date fec) {
		this.fec = fec;
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

}