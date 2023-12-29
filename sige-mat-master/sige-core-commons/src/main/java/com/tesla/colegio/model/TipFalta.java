package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla cat_tip_falta
 * @author MV
 *
 */
public class TipFalta extends EntidadBase{

	public final static String TABLA = "cat_tip_falta";
	private Integer id;
	private Integer id_cti;
	private String nom;
	private TipInc tipinc;	
	private List<Falta> faltas;

	public TipFalta(){
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
	* Obtiene Tipo de Incidencia 
	* @return id_cti
	*/
	public Integer getId_cti(){
		return id_cti;
	}	

	/**
	* Tipo de Incidencia 
	* @param id_cti
	*/
	public void setId_cti(Integer id_cti) {
		this.id_cti = id_cti;
	}

	/**
	* Obtiene Nombre del tipo de reporte 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Nombre del tipo de reporte 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	public TipInc getTipInc(){
		return tipinc;
	}	

	public void setTipInc(TipInc tipinc) {
		this.tipinc = tipinc;
	}
	/**
	* Obtiene lista de Faltas 
	*/
	public List<Falta> getFaltas() {
		return faltas;
	}

	/**
	* Seta Lista de Faltas 
	* @param faltas
	*/	
	public void setFalta(List<Falta> faltas) {
		this.faltas = faltas;
	}
}