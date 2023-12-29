package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla cat_periodo_aca
 * @author MV
 *
 */
public class PeriodoAca extends EntidadBase{

	public final static String TABLA = "cat_periodo_aca";
	private Integer id;
	private String nom;
	private List<PerAcaNivel> peracanivels;

	public PeriodoAca(){
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
	* Obtiene Nombre del periodo Academio 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Nombre del periodo Academio 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	* Obtiene lista de Periodo  Acadmico por Nivel 
	*/
	public List<PerAcaNivel> getPerAcaNivels() {
		return peracanivels;
	}

	/**
	* Seta Lista de Periodo  Acadmico por Nivel 
	* @param peracanivels
	*/	
	public void setPerAcaNivel(List<PerAcaNivel> peracanivels) {
		this.peracanivels = peracanivels;
	}
}