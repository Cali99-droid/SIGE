package com.tesla.colegio.model.bean;

import org.springframework.format.annotation.DateTimeFormat;


public class MatriculaPagos {

	private Integer id;
	private Integer id_mat;
	private String tip;
	private String descripcion;
	private Integer mens;
	private java.math.BigDecimal monto;
	private java.math.BigDecimal montoTotal;
	private String canc;
	private String nro_rec;
	private String nro_pe;
	private String banco;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date fec_pago;
	private java.math.BigDecimal desc_hermano;
	private java.math.BigDecimal desc_pronto_pago;
	private java.math.BigDecimal desc_pago_adelantado;
	private java.math.BigDecimal desc_personalizado;

	private java.math.BigDecimal montoReserva;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId_mat() {
		return id_mat;
	}

	public void setId_mat(Integer id_mat) {
		this.id_mat = id_mat;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	public Integer getMens() {
		return mens;
	}

	public void setMens(Integer mens) {
		this.mens = mens;
	}

	public java.math.BigDecimal getMonto() {
		return monto;
	}

	public void setMonto(java.math.BigDecimal monto) {
		this.monto = monto;
	}

	public java.math.BigDecimal getMontoTotal() {
		return montoTotal;
	}

	public void setMontoTotal(java.math.BigDecimal montoTotal) {
		this.montoTotal = montoTotal;
	}

	public String getCanc() {
		return canc;
	}

	public void setCanc(String canc) {
		this.canc = canc;
	}

	public String getNro_rec() {
		return nro_rec;
	}

	public void setNro_rec(String nro_rec) {
		this.nro_rec = nro_rec;
	}

	public String getNro_pe() {
		return nro_pe;
	}

	public void setNro_pe(String nro_pe) {
		this.nro_pe = nro_pe;
	}

	public String getBanco() {
		return banco;
	}

	public void setBanco(String banco) {
		this.banco = banco;
	}

	public java.util.Date getFec_pago() {
		return fec_pago;
	}

	public void setFec_pago(java.util.Date fec_pago) {
		this.fec_pago = fec_pago;
	}

	public java.math.BigDecimal getDesc_hermano() {
		return desc_hermano;
	}

	public void setDesc_hermano(java.math.BigDecimal desc_hermano) {
		this.desc_hermano = desc_hermano;
	}

	public java.math.BigDecimal getDesc_pronto_pago() {
		return desc_pronto_pago;
	}

	public void setDesc_pronto_pago(java.math.BigDecimal desc_pronto_pago) {
		this.desc_pronto_pago = desc_pronto_pago;
	}

	public java.math.BigDecimal getDesc_pago_adelantado() {
		return desc_pago_adelantado;
	}

	public void setDesc_pago_adelantado(java.math.BigDecimal desc_pago_adelantado) {
		this.desc_pago_adelantado = desc_pago_adelantado;
	}

	public java.math.BigDecimal getDesc_personalizado() {
		return desc_personalizado;
	}

	public void setDesc_personalizado(java.math.BigDecimal desc_personalizado) {
		this.desc_personalizado = desc_personalizado;
	}

	public java.math.BigDecimal getMontoReserva() {
		return montoReserva;
	}

	public void setMontoReserva(java.math.BigDecimal montoReserva) {
		this.montoReserva = montoReserva;
	}

	@Override
	public String toString() {
		return "MatriculaPagos [id=" + id + ", id_mat=" + id_mat + ", tip=" + tip + ", mens=" + mens + ", monto="
				+ monto + ", montoTotal=" + montoTotal + ", canc=" + canc + ", nro_rec=" + nro_rec + ", nro_pe="
				+ nro_pe + ", banco=" + banco + ", fec_pago=" + fec_pago + ", desc_hermano=" + desc_hermano
				+ ", desc_pronto_pago=" + desc_pronto_pago + ", desc_pago_adelantado=" + desc_pago_adelantado
				+ ", desc_personalizado=" + desc_personalizado + ", montoReserva=" + montoReserva + "]";
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	
}
