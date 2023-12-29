package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Clase entidad que representa a la tabla seg_usuario_token
 * @author MV
 *
 */
public class UsuarioToken extends EntidadBase{

	public final static String TABLA = "seg_usuario_token";
	private Integer id;
	private String token;
	private Integer id_usr;
	private Integer id_fam;
	private Integer id_per;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date fecha;
	private Usuario usuario;	
	private String fecha_expiracion;
	
	public UsuarioToken(){
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
	* Obtiene Token 
	* @return token
	*/
	public String getToken(){
		return token;
	}	

	/**
	* Token 
	* @param token
	*/
	public void setToken(String token) {
		this.token = token;
	}

	/**
	* Obtiene Usuario 
	* @return id_usr
	*/
	public Integer getId_usr(){
		return id_usr;
	}	

	/**
	* Usuario 
	* @param id_usr
	*/
	public void setId_usr(Integer id_usr) {
		this.id_usr = id_usr;
	}

	/**
	* Obtiene Fecha de vigencia 
	* @return fecha
	*/
	public java.util.Date getFecha(){
		return fecha;
	}	

	/**
	* Fecha de vigencia 
	* @param fecha
	*/
	public void setFecha(java.util.Date fecha) {
		this.fecha = fecha;
	}

	public Usuario getUsuario(){
		return usuario;
	}	

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Integer getId_fam() {
		return id_fam;
	}

	public void setId_fam(Integer id_fam) {
		this.id_fam = id_fam;
	}

	public Integer getId_per() {
		return id_per;
	}

	public void setId_per(Integer id_per) {
		this.id_per = id_per;
	}

	public String getFecha_expiracion() {
		return fecha_expiracion;
	}

	public void setFecha_expiracion(String fecha_expiracion) {
		this.fecha_expiracion = fecha_expiracion;
	}
	
}