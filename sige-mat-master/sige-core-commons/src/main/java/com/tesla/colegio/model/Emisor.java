package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla msj_emisor
 * @author MV
 *
 */
public class Emisor extends EntidadBase{

	public final static String TABLA = "msj_emisor";
	private Integer id;
	private Integer id_usr;
	private Integer id_per;
	private Perfil perfil;	
	private List<Mensaje> mensajes;

	public Emisor(){
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
	* Obtiene Uusario(Trabajador, Familiar) 
	* @return id_usr
	*/
	public Integer getId_usr(){
		return id_usr;
	}	

	/**
	* Uusario(Trabajador, Familiar) 
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

	public Perfil getPerfil(){
		return perfil;
	}	

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}
	/**
	* Obtiene lista de Mensaje 
	*/
	public List<Mensaje> getMensajes() {
		return mensajes;
	}

	/**
	* Seta Lista de Mensaje 
	* @param mensajes
	*/	
	public void setMensaje(List<Mensaje> mensajes) {
		this.mensajes = mensajes;
	}
}