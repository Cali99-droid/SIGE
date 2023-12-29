package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * Clase entidad que representa a la tabla pag_pago_programacion
 * @author MV
 *
 */
public class PagoProgramacion extends EntidadBase{

	public final static String TABLA = "pag_pago_programacion";
	private Integer id;
	private Integer id_cpa;
	private Integer id_per;
	private java.math.BigDecimal monto;
	private String mes;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date fec;
	private ConceptoPago conceptopago;	
	private Periodo periodo;	
	private List<PagoDetalle> pagodetalles;

	public PagoProgramacion(){
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
	* @return id_cpa
	*/
	public Integer getId_cpa(){
		return id_cpa;
	}	

	/**
	* Concepto 
	* @param id_cpa
	*/
	public void setId_cpa(Integer id_cpa) {
		this.id_cpa = id_cpa;
	}

	/**
	* Obtiene Periodo Acadmico 
	* @return id_per
	*/
	public Integer getId_per(){
		return id_per;
	}	

	/**
	* Periodo Acadmico 
	* @param id_per
	*/
	public void setId_per(Integer id_per) {
		this.id_per = id_per;
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
	* Obtiene Mes 
	* @return mes
	*/
	public String getMes(){
		return mes;
	}	

	/**
	* Mes 
	* @param mes
	*/
	public void setMes(String mes) {
		this.mes = mes;
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

	public ConceptoPago getConceptoPago(){
		return conceptopago;
	}	

	public void setConceptoPago(ConceptoPago conceptopago) {
		this.conceptopago = conceptopago;
	}
	public Periodo getPeriodo(){
		return periodo;
	}	

	public void setPeriodo(Periodo periodo) {
		this.periodo = periodo;
	}
	/**
	* Obtiene lista de Pago detalle 
	*/
	public List<PagoDetalle> getPagoDetalles() {
		return pagodetalles;
	}

	/**
	* Seta Lista de Pago detalle 
	* @param pagodetalles
	*/	
	public void setPagoDetalle(List<PagoDetalle> pagodetalles) {
		this.pagodetalles = pagodetalles;
	}
}