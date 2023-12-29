package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla msj_adjunto
 * @author MV
 *
 */
public class Adjunto extends EntidadBase{

	public final static String TABLA = "msj_adjunto";
	private Integer id;
	private String archivo;
	private List<Mensaje> mensajes;

	public Adjunto(){
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
	* Obtiene Archivo Adjuntado 
	* @return archivo
	*/
	public String getArchivo(){
		return archivo;
	}	

	/**
	* Archivo Adjuntado 
	* @param archivo
	*/
	public void setArchivo(String archivo) {
		this.archivo = archivo;
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