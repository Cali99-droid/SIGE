package com.tesla.colegio.model;

import com.sige.rest.request.MovimientoDetalleReq;
import com.tesla.frmk.model.EntidadBase;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.List;

/**
 * Clase entidad que representa a la tabla fac_movimiento
 * @author MV
 *
 */
public class Movimiento extends EntidadBase{

	public final static String TABLA = "fac_movimiento";
	private Integer id;
	private String tipo;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date fec;
	private Integer id_suc;
	private Integer id_mat;
	private Integer id_fam;
	private Integer id_per;
	private Integer id_fpa;//forma de pago
	private java.math.BigDecimal monto;
	private java.math.BigDecimal descuento;
	private java.math.BigDecimal monto_total;
	private String nro_rec;
	private String obs;
	private String cod_res;
	private Sucursal sucursal;	
	private Matricula matricula;
	private Familiar familiar;
	private Persona persona_alu;
	private Persona persona_fam;
	private List<MovimientoDetalle> movimientodetalles;
	// uaxiliar para mostrar la grilla
	private List<MovimientoDetalleReq> movimientoDetalleReqs;
	
	private Usuario usuario;

	public Movimiento(){
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
	* Obtiene Ingreso, Salida, Ajuste 
	* @return tipo
	*/
	public String getTipo(){
		return tipo;
	}	

	/**
	* Ingreso, Salida, Ajuste 
	* @param tipo
	*/
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	/**
	* Obtiene Fecha de movimiento 
	* @return fec
	*/
	public java.util.Date getFec(){
		return fec;
	}	

	/**
	* Fecha de movimiento 
	* @param fec
	*/
	public void setFec(java.util.Date fec) {
		this.fec = fec;
	}

	/**
	* Obtiene Local 
	* @return id_suc
	*/
	public Integer getId_suc(){
		return id_suc;
	}	

	/**
	* Local 
	* @param id_suc
	*/
	public void setId_suc(Integer id_suc) {
		this.id_suc = id_suc;
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

	public Sucursal getSucursal(){
		return sucursal;
	}	

	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
	}
	public Matricula getMatricula(){
		return matricula;
	}	

	public void setMatricula(Matricula matricula) {
		this.matricula = matricula;
	}
	/**
	* Obtiene lista de Detalle 
	*/
	public List<MovimientoDetalle> getMovimientoDetalles() {
		return movimientodetalles;
	}

	/**
	* Seta Lista de Detalle 
	* @param movimientodetalles
	*/	
	public void setMovimientoDetalle(List<MovimientoDetalle> movimientodetalles) {
		this.movimientodetalles = movimientodetalles;
	}

	public List<MovimientoDetalleReq> getMovimientoDetalleReqs() {
		return movimientoDetalleReqs;
	}

	public void setMovimientoDetalleReqs(List<MovimientoDetalleReq> movimientoDetalleReqs) {
		this.movimientoDetalleReqs = movimientoDetalleReqs;
	}

	public Integer getId_fam() {
		return id_fam;
	}

	public void setId_fam(Integer id_fam) {
		this.id_fam = id_fam;
	}

	public Familiar getFamiliar() {
		return familiar;
	}

	public void setFamiliar(Familiar familiar) {
		this.familiar = familiar;
	}

	public Integer getId_fpa() {
		return id_fpa;
	}

	public void setId_fpa(Integer id_fpa) {
		this.id_fpa = id_fpa;
	}

	public String getCod_res() {
		return cod_res;
	}

	public void setCod_res(String cod_res) {
		this.cod_res = cod_res;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@Override
	public String toString() {
		return "Movimiento [id=" + id + ", tipo=" + tipo + ", fec=" + fec + ", id_suc=" + id_suc + ", id_mat=" + id_mat
				+ ", id_fam=" + id_fam + ", id_fpa=" + id_fpa + ", monto=" + monto + ", descuento=" + descuento
				+ ", monto_total=" + monto_total + ", nro_rec=" + nro_rec + ", obs=" + obs + ", cod_res=" + cod_res
				+ ", sucursal=" + sucursal + ", matricula=" + matricula + ", familiar=" + familiar
				+ ", movimientodetalles=" + movimientodetalles + ", movimientoDetalleReqs=" + movimientoDetalleReqs
				+ ", usuario=" + usuario + "]";
	}

	public Integer getId_per() {
		return id_per;
	}

	public void setId_per(Integer id_per) {
		this.id_per = id_per;
	}

	public Persona getPersona_alu() {
		return persona_alu;
	}

	public void setPersona_alu(Persona persona_alu) {
		this.persona_alu = persona_alu;
	}

	public Persona getPersona_fam() {
		return persona_fam;
	}

	public void setPersona_fam(Persona persona_fam) {
		this.persona_fam = persona_fam;
	}


	
	
	
}