package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla cat_distrito
 * @author MV
 *
 */
public class Distrito extends EntidadBase{

	public final static String TABLA = "cat_distrito";
	private Integer id;
	private String nom;
	private Integer id_pro;
	private Provincia provincia;	
	private List<Familiar> familiars;

	public Distrito(){
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
	* Obtiene Provincia 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Provincia 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	* Obtiene Provincia 
	* @return id_pro
	*/
	public Integer getId_pro(){
		return id_pro;
	}	

	/**
	* Provincia 
	* @param id_pro
	*/
	public void setId_pro(Integer id_pro) {
		this.id_pro = id_pro;
	}

	public Provincia getProvincia(){
		return provincia;
	}	

	public void setProvincia(Provincia provincia) {
		this.provincia = provincia;
	}
	/**
	* Obtiene lista de Familiar del alumno 
	*/
	public List<Familiar> getFamiliars() {
		return familiars;
	}

	/**
	* Seta Lista de Familiar del alumno 
	* @param familiars
	*/	
	public void setFamiliar(List<Familiar> familiars) {
		this.familiars = familiars;
	}
}