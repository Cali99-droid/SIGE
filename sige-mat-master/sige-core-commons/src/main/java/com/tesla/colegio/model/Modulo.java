package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla mod_modulo
 * @author MV
 *
 */
public class Modulo extends EntidadBase{

	public final static String TABLA = "mod_modulo";
	private Integer id;
	private String cod;
	private String nom;
	private String des;
	private List<Parametro> parametros;

	public Modulo(){
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
	* Obtiene C&oacute;digo del m&oacute;dulo 
	* @return cod
	*/
	public String getCod(){
		return cod;
	}	

	/**
	* C&oacute;digo del m&oacute;dulo 
	* @param cod
	*/
	public void setCod(String cod) {
		this.cod = cod;
	}

	/**
	* Obtiene Nombre del m&oacute;dulo 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Nombre del m&oacute;dulo 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	* Obtiene Descripci&oacute;n del m&oacute;dulo 
	* @return des
	*/
	public String getDes(){
		return des;
	}	

	/**
	* Descripci&oacute;n del m&oacute;dulo 
	* @param des
	*/
	public void setDes(String des) {
		this.des = des;
	}

	/**
	* Obtiene lista de Par&aacute;metro 
	*/
	public List<Parametro> getParametros() {
		return parametros;
	}

	/**
	* Seta Lista de Par&aacute;metro 
	* @param parametros
	*/	
	public void setParametro(List<Parametro> parametros) {
		this.parametros = parametros;
	}
}