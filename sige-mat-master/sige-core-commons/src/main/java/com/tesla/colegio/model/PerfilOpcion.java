package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla seg_perfil_opcion
 * @author MV
 *
 */
public class PerfilOpcion extends EntidadBase{

	public final static String TABLA = "seg_perfil_opcion";
	private Integer id;
	private Integer id_per;
	private Integer id_opc;
	private Perfil perfil;	
	private Opcion opcion;	

	public PerfilOpcion(){
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

	public Perfil getPerfil(){
		return perfil;
	}	

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}
	public Opcion getOpcion(){
		return opcion;
	}	

	public void setOpcion(Opcion opcion) {
		this.opcion = opcion;
	}
}