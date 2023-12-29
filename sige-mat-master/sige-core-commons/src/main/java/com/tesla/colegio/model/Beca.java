package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla col_beca
 * @author MV
 *
 */
public class Beca extends EntidadBase{

	public final static String TABLA = "col_beca";
	private Integer id;
	private String nom;
	private String val;
	private String abrv;
	private Integer id_tdes;
	private TipoDescuento tipodescuento;	
	private List<MotivoBeca> motivobecas;

	public Beca(){
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
	* Obtiene Beca 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Beca 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	* Obtiene Beca 
	* @return val
	*/
	public String getVal(){
		return val;
	}	

	/**
	* Beca 
	* @param val
	*/
	public void setVal(String val) {
		this.val = val;
	}

	/**
	* Obtiene Abreviatura 
	* @return abrv
	*/
	public String getAbrv(){
		return abrv;
	}	

	/**
	* Abreviatura 
	* @param abrv
	*/
	public void setAbrv(String abrv) {
		this.abrv = abrv;
	}

	/**
	* Obtiene Tipo de Descuento 
	* @return id_tdes
	*/
	public Integer getId_tdes(){
		return id_tdes;
	}	

	/**
	* Tipo de Descuento 
	* @param id_tdes
	*/
	public void setId_tdes(Integer id_tdes) {
		this.id_tdes = id_tdes;
	}

	public TipoDescuento getTipoDescuento(){
		return tipodescuento;
	}	

	public void setTipoDescuento(TipoDescuento tipodescuento) {
		this.tipodescuento = tipodescuento;
	}
	/**
	* Obtiene lista de Motivo Beca 
	*/
	public List<MotivoBeca> getMotivoBecas() {
		return motivobecas;
	}

	/**
	* Seta Lista de Motivo Beca 
	* @param motivobecas
	*/	
	public void setMotivoBeca(List<MotivoBeca> motivobecas) {
		this.motivobecas = motivobecas;
	}
}