package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla msj_mensajeria_familiar
 * @author MV
 *
 */
public class MensajeriaFamiliar extends EntidadBase{

	public final static String TABLA = "msj_mensajeria_familiar";
	private Integer id;
	private Integer id_des;
	private Integer id_per;
	private Integer id_alu;
	private String msj;
	private String flg_en;
	private String clave;
	private String usr_rmt;
	private Familiar familiar;	
	private Perfil perfil;	

	public MensajeriaFamiliar(){
	}

	/**
	* Obtiene $field.description 
	* @return id
	*/
	public Integer getId(){
		return id;
	}	

	/**
	* $field.description 
	* @param id
	*/
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	* Obtiene Destino 
	* @return id_des
	*/
	public Integer getId_des(){
		return id_des;
	}	

	/**
	* Destino 
	* @param id_des
	*/
	public void setId_des(Integer id_des) {
		this.id_des = id_des;
	}

	/**
	* Obtiene Origen 
	* @return id_per
	*/
	public Integer getId_per(){
		return id_per;
	}	

	/**
	* Origen 
	* @param id_per
	*/
	public void setId_per(Integer id_per) {
		this.id_per = id_per;
	}

	/**
	* Obtiene Mensaje 
	* @return msj
	*/
	public String getMsj(){
		return msj;
	}	

	/**
	* Mensaje 
	* @param msj
	*/
	public void setMsj(String msj) {
		this.msj = msj;
	}

	/**
	* Obtiene Entregado 
	* @return flg_en
	*/
	public String getFlg_en(){
		return flg_en;
	}	

	/**
	* Entregado 
	* @param flg_en
	*/
	public void setFlg_en(String flg_en) {
		this.flg_en = flg_en;
	}

	public Familiar getFamiliar(){
		return familiar;
	}	

	public void setFamiliar(Familiar familiar) {
		this.familiar = familiar;
	}
	public Perfil getPerfil(){
		return perfil;
	}	

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public Integer getId_alu() {
		return id_alu;
	}

	public void setId_alu(Integer id_alu) {
		this.id_alu = id_alu;
	}

	public String getUsr_rmt() {
		return usr_rmt;
	}

	public void setUsr_rmt(String usr_rmt) {
		this.usr_rmt = usr_rmt;
	}
	
	
}