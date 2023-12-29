package com.tesla.colegio.model;

import java.util.ArrayList;
import java.util.List;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla fac_movimiento_detalle
 * @author MV
 *
 */
public class MovimientoDetalle extends EntidadBase{

	public final static String TABLA = "fac_movimiento_detalle";
	private Integer id;
	private Integer id_fmo;
	private Integer id_fco;
	private java.math.BigDecimal monto;
	private java.math.BigDecimal descuento;
	private java.math.BigDecimal monto_total;
	private String obs;
	private Movimiento movimiento;	
	private Concepto concepto;	
	private List<MovimientoDescuento> descuentos = new ArrayList<MovimientoDescuento>();

	public MovimientoDetalle(){
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
	* Obtiene Movimiento 
	* @return id_fmo
	*/
	public Integer getId_fmo(){
		return id_fmo;
	}	

	/**
	* Movimiento 
	* @param id_fmo
	*/
	public void setId_fmo(Integer id_fmo) {
		this.id_fmo = id_fmo;
	}

	/**
	* Obtiene Concepto 
	* @return id_fco
	*/
	public Integer getId_fco(){
		return id_fco;
	}	

	/**
	* Concepto 
	* @param id_fco
	*/
	public void setId_fco(Integer id_fco) {
		this.id_fco = id_fco;
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

	/**
	* Obtiene %Descuento por pago puntual 
	* @return descuento
	*/
	public java.math.BigDecimal getDescuento(){
		return descuento;
	}	

	/**
	* %Descuento por pago puntual 
	* @param descuento
	*/
	public void setDescuento(java.math.BigDecimal descuento) {
		this.descuento = descuento;
	}

	/**
	* Obtiene Monto total 
	* @return monto_total
	*/
	public java.math.BigDecimal getMonto_total(){
		return monto_total;
	}	

	/**
	* Monto total 
	* @param monto_total
	*/
	public void setMonto_total(java.math.BigDecimal monto_total) {
		this.monto_total = monto_total;
	}

	/**
	* Obtiene Nmero Recibo 
	* @return obs
	*/
	public String getObs(){
		return obs;
	}	

	/**
	* Nmero Recibo 
	* @param obs
	*/
	public void setObs(String obs) {
		this.obs = obs;
	}

	public Movimiento getMovimiento(){
		return movimiento;
	}	

	public void setMovimiento(Movimiento movimiento) {
		this.movimiento = movimiento;
	}
	public Concepto getConcepto(){
		return concepto;
	}	

	public void setConcepto(Concepto concepto) {
		this.concepto = concepto;
	}

	public List<MovimientoDescuento> getDescuentos() {
		return descuentos;
	}

	public void setDescuentos(List<MovimientoDescuento> descuentos) {
		this.descuentos = descuentos;
	}
	
	
}