package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Clase entidad que representa a la tabla mat_tarifas_emergencia
 * @author MV
 *
 */
public class TarifasEmergencia extends EntidadBase{

	public final static String TABLA = "mat_tarifas_emergencia";
	private Integer id;
	private Integer id_per;
	private String Exonerado;
	private java.math.BigDecimal monto;
	private java.math.BigDecimal descuento;
	private java.math.BigDecimal des_hermano;
	private java.math.BigDecimal des_banco;
	private Integer mes;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date fec_ven;
	private String procesado;
	private Periodo periodo;	

	public TarifasEmergencia(){
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
	* Obtiene Exonerado 
	* @return Exonerado
	*/
	public String getExonerado(){
		return Exonerado;
	}	

	/**
	* Exonerado 
	* @param Exonerado
	*/
	public void setExonerado(String Exonerado) {
		this.Exonerado = Exonerado;
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
	* Obtiene %Descuento por hermano 
	* @return des_hermano
	*/
	public java.math.BigDecimal getDes_hermano(){
		return des_hermano;
	}	

	/**
	* %Descuento por hermano 
	* @param des_hermano
	*/
	public void setDes_hermano(java.math.BigDecimal des_hermano) {
		this.des_hermano = des_hermano;
	}

	/**
	* Obtiene %Descuento Banco 
	* @return des_banco
	*/
	public java.math.BigDecimal getDes_banco(){
		return des_banco;
	}	

	/**
	* %Descuento Banco 
	* @param des_banco
	*/
	public void setDes_banco(java.math.BigDecimal des_banco) {
		this.des_banco = des_banco;
	}

	/**
	* Obtiene Mes 
	* @return mes
	*/
	public Integer getMes(){
		return mes;
	}	

	/**
	* Mes 
	* @param mes
	*/
	public void setMes(Integer mes) {
		this.mes = mes;
	}

	/**
	* Obtiene Fecha Vencimiento del Pago 
	* @return fec_ven
	*/
	public java.util.Date getFec_ven(){
		return fec_ven;
	}	

	/**
	* Fecha Vencimiento del Pago 
	* @param fec_ven
	*/
	public void setFec_ven(java.util.Date fec_ven) {
		this.fec_ven = fec_ven;
	}

	/**
	* Obtiene Procesado 
	* @return procesado
	*/
	public String getProcesado(){
		return procesado;
	}	

	/**
	* Procesado 
	* @param procesado
	*/
	public void setProcesado(String procesado) {
		this.procesado = procesado;
	}

	public Periodo getPeriodo(){
		return periodo;
	}	

	public void setPeriodo(Periodo periodo) {
		this.periodo = periodo;
	}
}