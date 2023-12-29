package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla seg_perfil
 * @author MV
 *
 */
public class Perfil extends EntidadBase{

	public final static String TABLA = "seg_perfil";
	private Integer id;
	private String nom;
	private String des;
	private Integer dias_adi_login;
	private List<Usuario> usuarios;

	public Perfil(){
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
	* Obtiene Nombre del perfil 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Nombre del perfil 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	* Obtiene Descripcin del perfil 
	* @return des
	*/
	public String getDes(){
		return des;
	}	

	/**
	* Descripcin del perfil 
	* @param des
	*/
	public void setDes(String des) {
		this.des = des;
	}

	/**
	* Obtiene lista de Usuario del sistema 
	*/
	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	/**
	* Seta Lista de Usuario del sistema 
	* @param usuarios
	*/	
	public void setUsuario(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public Integer getDias_adi_login() {
		return dias_adi_login;
	}

	public void setDias_adi_login(Integer dias_adi_login) {
		this.dias_adi_login = dias_adi_login;
	}
}