package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla fac_banco
 * @author MV
 *
 */
public class Banco extends EntidadBase{

	public final static String TABLA = "fac_banco";
	private Integer id;
	private String nom;
	private String cod;
	private String nro_cta;
	private String moneda;
	private String tip_cta;

	public Banco(){
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
	* Obtiene Nro de cuenta 
	* @return nro_cta
	*/
	public String getNro_cta(){
		return nro_cta;
	}	

	/**
	* Nro de cuenta 
	* @param nro_cta
	*/
	public void setNro_cta(String nro_cta) {
		this.nro_cta = nro_cta;
	}

	/**
	* Obtiene Moneda 
	* @return moneda
	*/
	public String getMoneda(){
		return moneda;
	}	

	/**
	* Moneda 
	* @param moneda
	*/
	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	/**
	* Obtiene Tipo de cuenta (REC) 
	* @return tip_cta
	*/
	public String getTip_cta(){
		return tip_cta;
	}	

	/**
	* Tipo de cuenta (REC) 
	* @param tip_cta
	*/
	public void setTip_cta(String tip_cta) {
		this.tip_cta = tip_cta;
	}

}