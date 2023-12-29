package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla oli_config_usuario
 * @author MV
 *
 */
public class ConfigUsuario extends EntidadBase{

	public final static String TABLA = "oli_config_usuario";
	private Integer id;
	private Integer id_oli;
	private Integer id_usr;
	private Integer id_rol;
	private Usuario usuario;	
	private Rol rol;	

	public ConfigUsuario(){
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
	* Obtiene Configuracin Olimpiada 
	* @return id_oli
	*/
	public Integer getId_oli(){
		return id_oli;
	}	

	/**
	* Configuracin Olimpiada 
	* @param id_oli
	*/
	public void setId_oli(Integer id_oli) {
		this.id_oli = id_oli;
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
	* Obtiene Rol 
	* @return id_rol
	*/
	public Integer getId_rol(){
		return id_rol;
	}	

	/**
	* Rol 
	* @param id_rol
	*/
	public void setId_rol(Integer id_rol) {
		this.id_rol = id_rol;
	}

	/*public Config getConfig(){
		return config;
	}	

	public void setConfig(Config config) {
		this.config = config;
	}
	public Usuario getUsuario(){
		return usuario;
	}	*/

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public Rol getRol(){
		return rol;
	}	

	public void setRol(Rol rol) {
		this.rol = rol;
	}
}