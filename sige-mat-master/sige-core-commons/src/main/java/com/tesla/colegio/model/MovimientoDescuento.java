package com.tesla.colegio.model;

import java.math.BigDecimal;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla fac_movimiento_descuento
 * @author MV
 *
 */
public class MovimientoDescuento extends EntidadBase{

	public final static String TABLA = "fac_movimiento_descuento";
	private Integer id;
	private Integer id_fmd;
	private java.math.BigDecimal descuento;
	private String des;
	private MovimientoDetalle movimientodetalle;	

	
	
	public MovimientoDescuento( BigDecimal descuento, String des) {
		super();
		//this.id_fmd = id_fmd;
		this.descuento = descuento;
		this.des = des;
	}

	public MovimientoDescuento(){
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
	* Obtiene Descuento 
	* @return id_fmd
	*/
	public Integer getId_fmd(){
		return id_fmd;
	}	

	/**
	* Descuento 
	* @param id_fmd
	*/
	public void setId_fmd(Integer id_fmd) {
		this.id_fmd = id_fmd;
	}

	/**
	* Obtiene Descuento 
	* @return descuento
	*/
	public java.math.BigDecimal getDescuento(){
		return descuento;
	}	

	/**
	* Descuento 
	* @param descuento
	*/
	public void setDescuento(java.math.BigDecimal descuento) {
		this.descuento = descuento;
	}

	/**
	* Obtiene DEsripcion 
	* @return des
	*/
	public String getDes(){
		return des;
	}	

	/**
	* DEsripcion 
	* @param des
	*/
	public void setDes(String des) {
		this.des = des;
	}

	public MovimientoDetalle getMovimientoDetalle(){
		return movimientodetalle;
	}	

	public void setMovimientoDetalle(MovimientoDetalle movimientodetalle) {
		this.movimientodetalle = movimientodetalle;
	}
}