package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla cat_tip_inc
 * @author MV
 *
 */
public class TipInc extends EntidadBase{

	public final static String TABLA = "cat_tip_inc";
	private Integer id;
	private String nom;
	private List<TipFalta> tipfaltas;

	public TipInc(){
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
	* Obtiene Nombre 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Nombre 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	* Obtiene lista de Tipo de Falta Conductual 
	*/
	public List<TipFalta> getTipFaltas() {
		return tipfaltas;
	}

	/**
	* Seta Lista de Tipo de Falta Conductual 
	* @param tipfaltas
	*/	
	public void setTipFalta(List<TipFalta> tipfaltas) {
		this.tipfaltas = tipfaltas;
	}
}