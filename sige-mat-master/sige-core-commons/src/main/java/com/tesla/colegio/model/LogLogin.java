package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla seg_log_login
 * @author MV
 *
 */
public class LogLogin extends EntidadBase{

	public final static String TABLA = "seg_log_login";
	private Integer id;
	private Integer id_usr;
	private Integer id_per;
	private String ip;
	private String exito;
	private Perfil perfil;	

	public LogLogin(){
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
	* Obtiene Usuario 
	* @return id_usr
	*/
	public Integer getId_usr(){
		return id_usr;
	}	

	/**
	* Usuario 
	* @param id_usr
	*/
	public void setId_usr(Integer id_usr) {
		this.id_usr = id_usr;
	}

	/**
	* Obtiene Perfil 
	* @return id_per
	*/
	public Integer getId_per(){
		return id_per;
	}	

	/**
	* Perfil 
	* @param id_per
	*/
	public void setId_per(Integer id_per) {
		this.id_per = id_per;
	}

	/**
	* Obtiene Ip que se loguea 
	* @return ip
	*/
	public String getIp(){
		return ip;
	}	

	/**
	* Ip que se loguea 
	* @param ip
	*/
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	* Obtiene Flag si fue exitoso o no 
	* @return exito
	*/
	public String getExito(){
		return exito;
	}	

	/**
	* Flag si fue exitoso o no 
	* @param exito
	*/
	public void setExito(String exito) {
		this.exito = exito;
	}

	public Perfil getPerfil(){
		return perfil;
	}	

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}
}