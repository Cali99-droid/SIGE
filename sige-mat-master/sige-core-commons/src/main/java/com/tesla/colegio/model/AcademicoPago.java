package com.tesla.colegio.model;


import com.tesla.colegio.util.Constante;
import com.tesla.frmk.model.EntidadBase;

import java.math.BigDecimal;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * Clase entidad que representa a la tabla fac_academico_pago
 * @author MV
 *
 */
public class AcademicoPago extends EntidadBase{

	public final static String TABLA = "fac_academico_pago";
	private Integer id;
	private Integer id_mat;
	private Integer id_bco_pag;
	private Integer id_bec;
	private String tip;
	private Integer mens;
	private Integer nro_cuota;
	private String tip_pag;
	private java.math.BigDecimal monto;
	private java.math.BigDecimal montoTotal;
	private String canc;
	private String nro_rec;
	private String nro_pe;
	private String banco;
	private String concepto;
	private String anio;
	private String nombre_mes;
	private String obs;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date fec_pago;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date fec_venc;
	private java.math.BigDecimal desc_beca;
	private java.math.BigDecimal desc_hermano;
	private java.math.BigDecimal desc_pronto_pago;
	private java.math.BigDecimal desc_pago_adelantado = new BigDecimal(0);
	private java.math.BigDecimal desc_personalizado;

	private Matricula matricula = new Matricula();	//no estaba inicializado y generaba problema al  mostrar el json
	private java.math.BigDecimal montoReserva;
	
	public BigDecimal getSaldo_favor() {
		return saldo_favor;
	}

	public void setSaldo_favor(BigDecimal saldo_favor) {
		this.saldo_favor = saldo_favor;
	}

	private BigDecimal saldo_favor;
	
	public AcademicoPago(){
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
	* Obtiene Matrcula 
	* @return id_mat
	*/
	public Integer getId_mat(){
		return id_mat;
	}	

	/**
	* Matrcula 
	* @param id_mat
	*/
	public void setId_mat(Integer id_mat) {
		this.id_mat = id_mat;
	}

	/**
	* Obtiene Tipo de Pago 
	* @return tip
	*/
	public String getTip(){
		return tip;
	}	

	/**
	* Tipo de Pago 
	* @param tip
	*/
	public void setTip(String tip) {
		this.tip = tip;
	}

	/**
	* Obtiene Mes 
	* @return mens
	*/
	public Integer getMens(){
		return mens;
	}	

	/**
	* Mes 
	* @param mens
	*/
	public void setMens(Integer mens) {
		this.mens = mens;
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
	* Obtiene Cancelado 
	* @return canc
	*/
	public String getCanc(){
		return canc;
	}	

	/**
	* Cancelado 
	* @param canc
	*/
	public void setCanc(String canc) {
		this.canc = canc;
	}

	/**
	* Obtiene Nmero Recibo 
	* @return nro_rec
	*/
	public String getNro_rec(){
		return nro_rec;
	}	

	/**
	* Nmero Recibo 
	* @param nro_rec
	*/
	public void setNro_rec(String nro_rec) {
		this.nro_rec = nro_rec;
	}

	/**
	* Obtiene Nmero de operacin 
	* @return nro_pe
	*/
	public String getNro_pe(){
		return nro_pe;
	}	

	/**
	* Nmero de operacin 
	* @param nro_pe
	*/
	public void setNro_pe(String nro_pe) {
		this.nro_pe = nro_pe;
	}

	/**
	* Obtiene Banco 
	* @return banco
	*/
	public String getBanco(){
		return banco;
	}	

	/**
	* Banco 
	* @param banco
	*/
	public void setBanco(String banco) {
		this.banco = banco;
	}

	/**
	* Obtiene Fecha Pago 
	* @return fec_pago
	*/
	public java.util.Date getFec_pago(){
		return fec_pago;
	}	

	/**
	* Fecha Pago 
	* @param fec_pago
	*/
	public void setFec_pago(java.util.Date fec_pago) {
		this.fec_pago = fec_pago;
	}

	/**
	* Obtiene Fecha Pago 
	* @return desc_hermano
	*/
	public java.math.BigDecimal getDesc_hermano(){
		return desc_hermano;
	}	

	/**
	* Fecha Pago 
	* @param desc_hermano
	*/
	public void setDesc_hermano(java.math.BigDecimal desc_hermano) {
		this.desc_hermano = desc_hermano;
	}

	/**
	* Obtiene Fecha Pago 
	* @return desc_pronto_pago
	*/
	public java.math.BigDecimal getDesc_pronto_pago(){
		return desc_pronto_pago;
	}	

	/**
	* Fecha Pago 
	* @param desc_pronto_pago
	*/
	public void setDesc_pronto_pago(java.math.BigDecimal desc_pronto_pago) {
		this.desc_pronto_pago = desc_pronto_pago;
	}

	/**
	* Obtiene Descuento pago adelantando 
	* @return desc_pago_adelantado
	*/
	public java.math.BigDecimal getDesc_pago_adelantado(){
		return desc_pago_adelantado;
	}	

	/**
	* Descuento pago adelantando 
	* @param desc_pago_adelantado
	*/
	public void setDesc_pago_adelantado(java.math.BigDecimal desc_pago_adelantado) {
		this.desc_pago_adelantado = desc_pago_adelantado;
	}

	public Matricula getMatricula(){
		return matricula;
	}	

	public void setMatricula(Matricula matricula) {
		this.matricula = matricula;
	}
	
	public String getTipoDes(){
		if(getTip().equals(Constante.PAGO_MENSUAL))
			return "Mensualidad";
		if(getTip().equals(Constante.PAGO_MATRICULA))
			return "Matricula";
		if(getTip().equals(Constante.PAGO_CUOTA_INGRESO))
			return "Cuota de ingreso";
		if(getTip().equals(Constante.PAGO_OTROS))
			return "Otros";
		return "";
	}

	public java.math.BigDecimal getMontoReserva() {
		return montoReserva;
	}

	public void setMontoReserva(java.math.BigDecimal montoReserva) {
		this.montoReserva = montoReserva;
	}

	
	public java.math.BigDecimal getMontoTotal() {
		return montoTotal;
	}

	public void setMontoTotal(java.math.BigDecimal montoTotal) {
		this.montoTotal = montoTotal;
	}

	public String getMes(){
		return Constante.MES[getMens()-1];
	}

	@Override
	public String toString() {
		return "AcademicoPago [id=" + id + ", id_mat=" + id_mat + ", tip=" + tip + ", mens=" + mens + ", monto=" + monto
				+ ", montoTotal=" + montoTotal + ", canc=" + canc + ", nro_rec=" + nro_rec + ", nro_pe=" + nro_pe
				+ ", banco=" + banco + ", fec_pago=" + fec_pago + ", desc_hermano=" + desc_hermano
				+ ", desc_pronto_pago=" + desc_pronto_pago + ", desc_pago_adelantado=" + desc_pago_adelantado
				+ ", desc_personalizado=" + desc_personalizado + ", matricula=" + matricula + ", montoReserva="
				+ montoReserva + "]";
	}

	public java.math.BigDecimal getDesc_personalizado() {
		return desc_personalizado;
	}

	public void setDesc_personalizado(java.math.BigDecimal desc_personalizado) {
		this.desc_personalizado = desc_personalizado;
	}

	public java.util.Date getFec_venc() {
		return fec_venc;
	}

	public void setFec_venc(java.util.Date fec_venc) {
		this.fec_venc = fec_venc;
	}

	public Integer getNro_cuota() {
		return nro_cuota;
	}

	public void setNro_cuota(Integer nro_cuota) {
		this.nro_cuota = nro_cuota;
	}

	public Integer getId_bco_pag() {
		return id_bco_pag;
	}

	public void setId_bco_pag(Integer id_bco_pag) {
		this.id_bco_pag = id_bco_pag;
	}

	public String getConcepto() {
		return concepto;
	}

	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}

	public String getAnio() {
		return anio;
	}

	public void setAnio(String anio) {
		this.anio = anio;
	}

	public String getNombre_mes() {
		return nombre_mes;
	}

	public void setNombre_mes(String nombre_mes) {
		this.nombre_mes = nombre_mes;
	}

	public java.math.BigDecimal getDesc_beca() {
		return desc_beca;
	}

	public void setDesc_beca(java.math.BigDecimal desc_beca) {
		this.desc_beca = desc_beca;
	}

	public Integer getId_bec() {
		return id_bec;
	}

	public void setId_bec(Integer id_bec) {
		this.id_bec = id_bec;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

	public String getTip_pag() {
		return tip_pag;
	}

	public void setTip_pag(String tip_pag) {
		this.tip_pag = tip_pag;
	}

	/*public String getConcepto() {
		return concepto;
	}

	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}

	public String getAnio() {
		return anio;
	}

	public void setAnio(String anio) {
		this.anio = anio;
	}

	public String getNombre_mes() {
		return nombre_mes;
	}

	public void setNombre_mes(String nombre_mes) {
		this.nombre_mes = nombre_mes;
	}*/

	

	
	
}