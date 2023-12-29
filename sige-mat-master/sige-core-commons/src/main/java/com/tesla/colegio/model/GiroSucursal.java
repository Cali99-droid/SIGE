package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla ges_giro_sucursal
 * @author MV
 *
 */
public class GiroSucursal extends EntidadBase{

	public final static String TABLA = "ges_giro_sucursal";
	private Integer id;
	private Integer id_gir;
	private Integer id_suc;
	private GiroNegocio giroNegocio;	
	private Sucursal sucursal;	

	public GiroSucursal(){
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
	* Obtiene Giro de negocio 
	* @return id_gir
	*/
	public Integer getId_gir(){
		return id_gir;
	}	

	/**
	* Giro de negocio 
	* @param id_gir
	*/
	public void setId_gir(Integer id_gir) {
		this.id_gir = id_gir;
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

	public GiroNegocio getGiroNegocio(){
		return giroNegocio;
	}	

	public void setGiroNegocio(GiroNegocio giroNegocio) {
		this.giroNegocio = giroNegocio;
	}
	public Sucursal getSucursal(){
		return sucursal;
	}	

	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
	}
}