package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla mat_conf_cuota
 * @author MV
 *
 */
public class ConfCuota extends EntidadBase{

	public final static String TABLA = "mat_conf_cuota";
	private Integer id;
	private Integer id_per;
	private Integer id_cct;
	private Integer id_cme;
	private java.math.BigDecimal matricula;
	private java.math.BigDecimal cuota;
	private java.math.BigDecimal reserva;
	private String tip_cuota_ing;

	private Periodo periodo;	
	private ModalidadEstudio modalidadEstudio;	
	private Turno turno;

	public ConfCuota(){
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
	* Obtiene Matricula 
	* @return matricula
	*/
	public java.math.BigDecimal getMatricula(){
		return matricula;
	}	

	/**
	* Matricula 
	* @param matricula
	*/
	public void setMatricula(java.math.BigDecimal matricula) {
		this.matricula = matricula;
	}

	/**
	* Obtiene Cuota Ingreso 
	* @return cuota
	*/
	public java.math.BigDecimal getCuota(){
		return cuota;
	}	

	/**
	* Cuota Ingreso 
	* @param cuota
	*/
	public void setCuota(java.math.BigDecimal cuota) {
		this.cuota = cuota;
	}

	public Periodo getPeriodo(){
		return periodo;
	}	

	public void setPeriodo(Periodo periodo) {
		this.periodo = periodo;
	}
	public java.math.BigDecimal getReserva() {
		return reserva;
	}

	public void setReserva(java.math.BigDecimal reserva) {
		this.reserva = reserva;
	}

	public String getTip_cuota_ing() {
		return tip_cuota_ing;
	}

	public void setTip_cuota_ing(String tip_cuota_ing) {
		this.tip_cuota_ing = tip_cuota_ing;
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
}