package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla fac_historial_men
 * @author MV
 *
 */
public class HistorialMen extends EntidadBase{

	public final static String TABLA = "fac_historial_men";
	private Integer id;
	private Integer id_fac;
	private java.math.BigDecimal monto_anterior;
	private java.math.BigDecimal monto_actual;
	private AcademicoPago academicopago;	

	public HistorialMen(){
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
	* Obtiene Acadmico Pago 
	* @return id_fac
	*/
	public Integer getId_fac(){
		return id_fac;
	}	

	/**
	* Acadmico Pago 
	* @param id_fac
	*/
	public void setId_fac(Integer id_fac) {
		this.id_fac = id_fac;
	}

	/**
	* Obtiene Monto Anterior 
	* @return monto_anterior
	*/
	public java.math.BigDecimal getMonto_anterior(){
		return monto_anterior;
	}	

	/**
	* Monto Anterior 
	* @param monto_anterior
	*/
	public void setMonto_anterior(java.math.BigDecimal monto_anterior) {
		this.monto_anterior = monto_anterior;
	}

	/**
	* Obtiene Monto Actual 
	* @return monto_actual
	*/
	public java.math.BigDecimal getMonto_actual(){
		return monto_actual;
	}	

	/**
	* Monto Actual 
	* @param monto_actual
	*/
	public void setMonto_actual(java.math.BigDecimal monto_actual) {
		this.monto_actual = monto_actual;
	}

	public AcademicoPago getAcademicoPago(){
		return academicopago;
	}	

	public void setAcademicoPago(AcademicoPago academicopago) {
		this.academicopago = academicopago;
	}
}