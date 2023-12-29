package com.tesla.colegio.model;

import org.springframework.format.annotation.DateTimeFormat;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla pag_pago
 * @author MV
 *
 */
public class Pago extends EntidadBase{

	public final static String TABLA = "pag_pago";
	private Integer id;
	private String id_pre;
	private Integer id_ppr;
	private Integer id_pbco;
	private java.math.BigDecimal monto;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date fec;
	private PagoRealizado pagorealizado;	
	private PagoProgramacion pagoprogramacion;	

	public Pago(){
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
	* Obtiene Pago Realizado 
	* @return id_pre
	*/
	public String getId_pre(){
		return id_pre;
	}	

	/**
	* Pago Realizado 
	* @param id_pre
	*/
	public void setId_pre(String id_pre) {
		this.id_pre = id_pre;
	}

	/**
	* Obtiene Programacin del pago 
	* @return id_ppr
	*/
	public Integer getId_ppr(){
		return id_ppr;
	}	

	/**
	* Programacin del pago 
	* @param id_ppr
	*/
	public void setId_ppr(Integer id_ppr) {
		this.id_ppr = id_ppr;
	}

	/**
	* Obtiene Pago Banco 
	* @return id_pbco
	*/
	public Integer getId_pbco(){
		return id_pbco;
	}	

	/**
	* Pago Banco 
	* @param id_pbco
	*/
	public void setId_pbco(Integer id_pbco) {
		this.id_pbco = id_pbco;
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
	* Obtiene Fecha  
	* @return fec
	*/
	public java.util.Date getFec(){
		return fec;
	}	

	/**
	* Fecha  
	* @param fec
	*/
	public void setFec(java.util.Date fec) {
		this.fec = fec;
	}

	public PagoRealizado getPagoRealizado(){
		return pagorealizado;
	}	

	public void setPagoRealizado(PagoRealizado pagorealizado) {
		this.pagorealizado = pagorealizado;
	}
	public PagoProgramacion getPagoProgramacion(){
		return pagoprogramacion;
	}	

	public void setPagoProgramacion(PagoProgramacion pagoprogramacion) {
		this.pagoprogramacion = pagoprogramacion;
	}
}