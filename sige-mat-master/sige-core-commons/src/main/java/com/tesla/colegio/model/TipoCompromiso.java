package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla con_tipo_compromiso
 * @author MV
 *
 */
public class TipoCompromiso extends EntidadBase{

	public final static String TABLA = "con_tipo_compromiso";
	private Integer id;
	private String nom;
	//private List<CompromisoAl> compromisoalus;

	public TipoCompromiso(){
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
	* Obtiene Tipo Compromiso 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Tipo Compromiso 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

}