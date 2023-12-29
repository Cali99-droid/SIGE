package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla mat_conf_mensualidad
 * @author MV
 *
 */
public class ConfMensualidad extends EntidadBase{

	public final static String TABLA = "mat_conf_mensualidad";
	private Integer id;
	private Integer id_per;
	private Integer id_cct;
	private Integer id_cme;
	private Integer mes;
	private java.math.BigDecimal monto;
	private java.math.BigDecimal descuento;
	private java.math.BigDecimal desc_banco;
	private java.math.BigDecimal desc_hermano;
	private Integer dia_mora;
	private Periodo periodo;	
	private ModalidadEstudio modalidadEstudio;
	private Turno turno;
	private String tipo_fec_ven;
	private String fec_fin_dic;

	public ConfMensualidad(){
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
	* Obtiene Max. dias de tolerancia 
	* @return dia_mora
	*/
	public Integer getDia_mora(){
		return dia_mora;
	}	

	/**
	* Max. dias de tolerancia 
	* @param dia_mora
	*/
	public void setDia_mora(Integer dia_mora) {
		this.dia_mora = dia_mora;
	}

	public Periodo getPeriodo(){
		return periodo;
	}	

	public void setPeriodo(Periodo periodo) {
		this.periodo = periodo;
	}
	public java.math.BigDecimal getDesc_banco() {
		return desc_banco;
	}

	public void setDesc_banco(java.math.BigDecimal desc_banco) {
		this.desc_banco = desc_banco;
	}

	/*metodo adicional*/
	public String getMesDes() {
		switch (getMes()){
		case 0:
			return "Todos";
		case 1:
			return "Enero";
		case 2:
			return "Febrero";
		case 3:
			return "Marzo";
		case 4:
			return "Abril";
		case 5:
			return "Mayo";
		case 6:
			return "Junio";
		case 7:
			return "Julio";
		case 8:
			return "Agosto";
		case 9:
			return "Setiembre";
		case 10:
			return "Octubre";
		case 11:
			return "Noviembre";
		case 12:
			return "Diciembre";
		}
		return null;
	}

	public java.math.BigDecimal getDesc_hermano() {
		return desc_hermano;
	}

	public void setDesc_hermano(java.math.BigDecimal desc_hermano) {
		this.desc_hermano = desc_hermano;
	}

	public String getTipo_fec_ven() {
		return tipo_fec_ven;
	}

	public void setTipo_fec_ven(String tipo_fec_ven) {
		this.tipo_fec_ven = tipo_fec_ven;
	}

	public Integer getId_cme() {
		return id_cme;
	}

	public void setId_cme(Integer id_cme) {
		this.id_cme = id_cme;
	}

	public ModalidadEstudio getModalidadEstudio() {
		return modalidadEstudio;
	}

	public void setModalidadEstudio(ModalidadEstudio modalidadEstudio) {
		this.modalidadEstudio = modalidadEstudio;
	}

	public Integer getId_cct() {
		return id_cct;
	}

	public void setId_cct(Integer id_cct) {
		this.id_cct = id_cct;
	}

	public Turno getTurno() {
		return turno;
	}

	public void setTurno(Turno turno) {
		this.turno = turno;
	}

	public String getFec_fin_dic() {
		return fec_fin_dic;
	}

	public void setFec_fin_dic(String fec_fin_dic) {
		this.fec_fin_dic = fec_fin_dic;
	}

	
	
}