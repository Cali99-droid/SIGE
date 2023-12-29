package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla fac_concepto
 * @author MV
 *
 */
public class Concepto extends EntidadBase{

	public final static String TABLA = "fac_concepto";
	private Integer id;
	private String nom;
	private String des;
	private String tip;
	private String flag_edit;
	
	private java.math.BigDecimal monto;
	private List<Movimiento> movimientos;

	public Concepto(){
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
	* Obtiene Concepto 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Concepto 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	* Obtiene Descripcin 
	* @return des
	*/
	public String getDes(){
		return des;
	}	

	/**
	* Descripcin 
	* @param des
	*/
	public void setDes(String des) {
		this.des = des;
	}

	/**
	* Obtiene Monto 
	* @return monto
	*/
	public java.math.BigDecimal getMonto(){
		return monto;
	}	

	/**
	* Monto 
	* @param monto
	*/
	public void setMonto(java.math.BigDecimal monto) {
		this.monto = monto;
	}
	
	

	public String getTip() {
		return tip;
	}

	public String getTipDes() {
		if ("I".equals(tip)) 
			return "Ingreso";
		else
			return "Salida";
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	/**
	* Obtiene lista de Entradas y salidas 
	*/
	public List<Movimiento> getMovimientos() {
		return movimientos;
	}

	/**
	* Seta Lista de Entradas y salidas 
	* @param movimientos
	*/	
	public void setMovimiento(List<Movimiento> movimientos) {
		this.movimientos = movimientos;
	}

	public String getFlag_edit() {
		return flag_edit;
	}

	public void setFlag_edit(String flag_edit) {
		this.flag_edit = flag_edit;
	}
	
}