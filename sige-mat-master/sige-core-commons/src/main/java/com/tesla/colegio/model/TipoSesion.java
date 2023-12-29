package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla cat_tipo_sesion
 * @author MV
 *
 */
public class TipoSesion extends EntidadBase{

	public final static String TABLA = "cat_tipo_sesion";
	private Integer id;
	private String nom;
	private List<SesionTipo> sesiontipos;

	public TipoSesion(){
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
	* Obtiene Tipo 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Tipo 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	* Obtiene lista de Sesiones por unidad x tipo 
	*/
	public List<SesionTipo> getSesionTipos() {
		return sesiontipos;
	}

	/**
	* Seta Lista de Sesiones por unidad x tipo 
	* @param sesiontipos
	*/	
	public void setSesionTipo(List<SesionTipo> sesiontipos) {
		this.sesiontipos = sesiontipos;
	}
}