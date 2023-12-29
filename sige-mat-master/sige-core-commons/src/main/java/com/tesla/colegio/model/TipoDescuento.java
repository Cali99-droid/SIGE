package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla cat_tipo_descuento
 * @author MV
 *
 */
public class TipoDescuento extends EntidadBase{

	public final static String TABLA = "cat_tipo_descuento";
	private Integer id;
	private String nom;
	private String cod;
	private List<Beca> becas;

	public TipoDescuento(){
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
	* Obtiene Tipo Descuento 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Tipo Descuento 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	* Obtiene Codigo 
	* @return cod
	*/
	public String getCod(){
		return cod;
	}	

	/**
	* Codigo 
	* @param cod
	*/
	public void setCod(String cod) {
		this.cod = cod;
	}

	/**
	* Obtiene lista de Area 
	*/
	public List<Beca> getBecas() {
		return becas;
	}

	/**
	* Seta Lista de Area 
	* @param becas
	*/	
	public void setBeca(List<Beca> becas) {
		this.becas = becas;
	}
}