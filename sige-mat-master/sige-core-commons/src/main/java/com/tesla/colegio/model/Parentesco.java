package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla cat_parentesco
 * @author MV
 *
 */
public class Parentesco extends EntidadBase{

	public final static String TABLA = "cat_parentesco";
	private Integer id;
	private String par;
	private List<Familiar> familiars;

	public Parentesco(){
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
	* Obtiene Parentesco 
	* @return par
	*/
	public String getPar(){
		return par;
	}	

	/**
	* Parentesco 
	* @param par
	*/
	public void setPar(String par) {
		this.par = par;
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