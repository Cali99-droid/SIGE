package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla seg_usuario_sucursal
 * @author MV
 *
 */
public class UsuarioSucursal extends EntidadBase{

	public final static String TABLA = "seg_usuario_sucursal";
	private Integer id;
	private Integer id_usr;
	private Integer id_suc;
	private Usuario usuario;	
	private Sucursal sucursal;	

	public UsuarioSucursal(){
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
	* Obtiene Local 
	* @return id_suc
	*/
	public Integer getId_suc(){
		return id_suc;
	}	

	/**
	* Local 
	* @param id_suc
	*/
	public void setId_suc(Integer id_suc) {
		this.id_suc = id_suc;
	}

	public Usuario getUsuario(){
		return usuario;
	}	

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public Sucursal getSucursal(){
		return sucursal;
	}	

	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
	}
}