package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla fac_acapag_beca
 * @author MV
 *
 */
public class AcapagBeca extends EntidadBase{

	public final static String TABLA = "fac_acapag_beca";
	private Integer id;
	private Integer id_fac;
	private Integer id_bec;
	private Integer id_mbec;
	private java.math.BigDecimal monto_total;
	private java.math.BigDecimal monto_afectado;
	private AcademicoPago academicopago;	
	private Beca beca;	
	private MotivoBeca motivobeca;	

	public AcapagBeca(){
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
	* Obtiene Beca 
	* @return id_bec
	*/
	public Integer getId_bec(){
		return id_bec;
	}	

	/**
	* Beca 
	* @param id_bec
	*/
	public void setId_bec(Integer id_bec) {
		this.id_bec = id_bec;
	}

	/**
	* Obtiene Motivo Beca 
	* @return id_mbec
	*/
	public Integer getId_mbec(){
		return id_mbec;
	}	

	/**
	* Motivo Beca 
	* @param id_mbec
	*/
	public void setId_mbec(Integer id_mbec) {
		this.id_mbec = id_mbec;
	}

	/**
	* Obtiene Monto Total 
	* @return monto_total
	*/
	public java.math.BigDecimal getMonto_total(){
		return monto_total;
	}	

	/**
	* Monto Total 
	* @param monto_total
	*/
	public void setMonto_total(java.math.BigDecimal monto_total) {
		this.monto_total = monto_total;
	}

	/**
	* Obtiene Monto Afectado 
	* @return monto_afectado
	*/
	public java.math.BigDecimal getMonto_afectado(){
		return monto_afectado;
	}	

	/**
	* Monto Afectado 
	* @param monto_afectado
	*/
	public void setMonto_afectado(java.math.BigDecimal monto_afectado) {
		this.monto_afectado = monto_afectado;
	}

	public AcademicoPago getAcademicoPago(){
		return academicopago;
	}	

	public void setAcademicoPago(AcademicoPago academicopago) {
		this.academicopago = academicopago;
	}
	public Beca getBeca(){
		return beca;
	}	

	public void setBeca(Beca beca) {
		this.beca = beca;
	}
	public MotivoBeca getMotivoBeca(){
		return motivobeca;
	}	

	public void setMotivoBeca(MotivoBeca motivobeca) {
		this.motivobeca = motivobeca;
	}
}