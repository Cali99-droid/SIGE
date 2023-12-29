package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla cat_tipo_enc
 * @author MV
 *
 */
public class TipoPre extends EntidadBase{

	public final static String TABLA = "cat_tipo_enc";
	private Integer id;
	private String nom;
	private String cod;
	private List<EncuestaPreg> encuestapregs;

	public TipoPre(){
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
	* Obtiene lista de Encuesta Preguntas 
	*/
	public List<EncuestaPreg> getEncuestaPregs() {
		return encuestapregs;
	}

	/**
	* Seta Lista de Encuesta Preguntas 
	* @param encuestapregs
	*/	
	public void setEncuestaPreg(List<EncuestaPreg> encuestapregs) {
		this.encuestapregs = encuestapregs;
	}
}