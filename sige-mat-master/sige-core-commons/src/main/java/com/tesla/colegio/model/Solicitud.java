package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla mat_solicitud
 * @author MV
 *
 */
public class Solicitud extends EntidadBase{

	@Override
	public String toString() {
		return "Solicitud [id=" + id + ", id_mat=" + id_mat + ", id_alu=" + id_alu + ", id_anio=" + id_anio
				+ ", id_fam=" + id_fam + ", id_suc_or=" + id_suc_or + ", id_suc_des=" + id_suc_des + ", tipo=" + tipo
				+ ", nro_exp=" + nro_exp + ", motivo=" + motivo + ", matricula=" + matricula + ", familiar=" + familiar
				+ ", sucursal=" + sucursal + "]";
	}

	public final static String TABLA = "mat_solicitud";
	private Integer id;
	private Integer id_mat;
	private Integer id_alu;
	private Integer id_anio;
	private Integer id_fam;
	private Integer id_suc_or;
	private Integer id_suc_des;
	private String tipo;//matricula o reserva
	private String nro_exp;
	private String motivo;
	private Matricula matricula;	
	private Familiar familiar;	
	private Sucursal sucursal;	


	public Solicitud(){
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
	* Obtiene Apoderado 
	* @return id_fam
	*/
	public Integer getId_fam(){
		return id_fam;
	}	

	/**
	* Apoderado 
	* @param id_fam
	*/
	public void setId_fam(Integer id_fam) {
		this.id_fam = id_fam;
	}

	/**
	* Obtiene Local Origen 
	* @return id_suc_or
	*/
	public Integer getId_suc_or(){
		return id_suc_or;
	}	

	/**
	* Local Origen 
	* @param id_suc_or
	*/
	public void setId_suc_or(Integer id_suc_or) {
		this.id_suc_or = id_suc_or;
	}

	/**
	* Obtiene Local Destino 
	* @return id_suc_des
	*/
	public Integer getId_suc_des(){
		return id_suc_des;
	}	

	/**
	* Local Destino 
	* @param id_suc_des
	*/
	public void setId_suc_des(Integer id_suc_des) {
		this.id_suc_des = id_suc_des;
	}

	/**
	* Obtiene Nmero de Expediente 
	* @return nro_exp
	*/
	public String getNro_exp(){
		return nro_exp;
	}	

	/**
	* Nmero de Expediente 
	* @param nro_exp
	*/
	public void setNro_exp(String nro_exp) {
		this.nro_exp = nro_exp;
	}

	/**
	* Obtiene Motivo 
	* @return motivo
	*/
	public String getMotivo(){
		return motivo;
	}	

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	/**
	* Motivo 
	* @param motivo
	*/
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public Matricula getMatricula(){
		return matricula;
	}	

	public void setMatricula(Matricula matricula) {
		this.matricula = matricula;
	}
	public Familiar getFamiliar(){
		return familiar;
	}	

	public void setFamiliar(Familiar familiar) {
		this.familiar = familiar;
	}
	public Sucursal getSucursal(){
		return sucursal;
	}	

	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
	}

	public Integer getId_alu() {
		return id_alu;
	}

	public void setId_alu(Integer id_alu) {
		this.id_alu = id_alu;
	}

	public Integer getId_anio() {
		return id_anio;
	}

	public void setId_anio(Integer id_anio) {
		this.id_anio = id_anio;
	}
	
	
	
}