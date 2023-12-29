package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla col_correlativo
 * @author MV
 *
 */
public class Correlativo extends EntidadBase{

	public final static String TABLA = "col_correlativo";
	private Integer id;
	private Integer id_suc;
	private Integer id_anio;
	private String tipo;
	private Integer numero;
	private Sucursal sucursal;	
	private Anio anio;	

	public Correlativo(){
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
	* Obtiene Local 
	* @return id_suc
	*/
	public Integer getId_suc(){
		return id_suc;
	}	

	/**
	* Local 
	* @param id_suc
	*/
	public void setId_suc(Integer id_suc) {
		this.id_suc = id_suc;
	}

	/**
	* Obtiene Anio 
	* @return id_anio
	*/
	public Integer getId_anio(){
		return id_anio;
	}	

	/**
	* Anio 
	* @param id_anio
	*/
	public void setId_anio(Integer id_anio) {
		this.id_anio = id_anio;
	}

	/**
	* Obtiene ADE_Adenda 
	* @return tipo
	*/
	public String getTipo(){
		return tipo;
	}	

	/**
	* ADE_Adenda 
	* @param tipo
	*/
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	/**
	* Obtiene Nro de correlativo 
	* @return numero
	*/
	public Integer getNumero(){
		return numero;
	}	

	/**
	* Nro de correlativo 
	* @param numero
	*/
	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public Sucursal getSucursal(){
		return sucursal;
	}	

	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
	}
	public Anio getAnio(){
		return anio;
	}	

	public void setAnio(Anio anio) {
		this.anio = anio;
	}
}