package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla cat_tip_periodo
 * @author MV
 *
 */
public class TipPeriodo extends EntidadBase{

	public final static String TABLA = "cat_tip_periodo";
	private Integer id;
	private String nom;
	private String des;
	private List<Periodo> periodos;

	public TipPeriodo(){
	}

	/**
	* Obtiene Llave Principal 
	* @return id
	*/
	public Integer getId(){
		return id;
	}	

	/**
	* Llave Principal 
	* @param id
	*/
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	* Obtiene Tipo de periodo 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Tipo de periodo 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	* Obtiene Descripcion del Tipo de periodo 
	* @return des
	*/
	public String getDes(){
		return des;
	}	

	/**
	* Descripcion del Tipo de periodo 
	* @param des
	*/
	public void setDes(String des) {
		this.des = des;
	}

	/**
	* Obtiene lista de Periodo de estudio 
	*/
	public List<Periodo> getPeriodos() {
		return periodos;
	}

	/**
	* Seta Lista de Periodo de estudio 
	* @param periodos
	*/	
	public void setPeriodo(List<Periodo> periodos) {
		this.periodos = periodos;
	}
}