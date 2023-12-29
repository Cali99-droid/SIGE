package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla seg_usuario_rol
 * @author MV
 *
 */
public class UsuarioRol extends EntidadBase{

	public final static String TABLA = "seg_usuario_rol";
	private Integer id;
	private Integer id_usr;
	private Integer id_rol;
	private Usuario usuario;	
	private Rol rol;
	private Trabajador trabajador;
	private Persona persona;

	public UsuarioRol(){
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

	public Usuario getUsuario(){
		return usuario;
	}	

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public Rol getRol(){
		return rol;
	}	

	public void setRol(Rol rol) {
		this.rol = rol;
	}

	public Trabajador getTrabajador() {
		return trabajador;
	}

	public void setTrabajador(Trabajador trabajador) {
		this.trabajador = trabajador;
	}

	public Persona getPersona() {
		return persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}
}