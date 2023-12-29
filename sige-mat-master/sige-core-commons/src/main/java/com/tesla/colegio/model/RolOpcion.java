package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla seg_rol_opcion
 * @author MV
 *
 */
public class RolOpcion extends EntidadBase{

	public final static String TABLA = "seg_rol_opcion";
	private Integer id;
	private Integer id_rol;
	private Integer id_opc;
	private Rol rol;	
	private Opcion opcion;	

	public RolOpcion(){
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

	/**
	* Obtiene Opcin 
	* @return id_opc
	*/
	public Integer getId_opc(){
		return id_opc;
	}	

	/**
	* Opcin 
	* @param id_opc
	*/
	public void setId_opc(Integer id_opc) {
		this.id_opc = id_opc;
	}

	public Rol getRol(){
		return rol;
	}	

	public void setRol(Rol rol) {
		this.rol = rol;
	}
	public Opcion getOpcion(){
		return opcion;
	}	

	public void setOpcion(Opcion opcion) {
		this.opcion = opcion;
	}
}