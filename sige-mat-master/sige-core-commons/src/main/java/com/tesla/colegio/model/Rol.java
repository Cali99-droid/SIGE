package com.tesla.colegio.model;

 import com.tesla.frmk.model.EntidadBase;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/**
 * Clase entidad que representa a la tabla seg_rol
 * @author MV
 *
 */
@Component
public class Rol extends EntidadBase{

	
	public final static String TABLA = "seg_rol";
	private Integer id;
	private String nom;
	private List<UsuarioRol> usuariorols;

	public Rol(){
	 
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
	* Obtiene Rol 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Rol 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	* Obtiene lista de Rol Usuario  
	*/
	public List<UsuarioRol> getUsuarioRols() {
		return usuariorols;
	}

	/**
	* Seta Lista de Rol Usuario  
	* @param usuariorols
	*/	
	public void setUsuarioRol(List<UsuarioRol> usuariorols) {
		this.usuariorols = usuariorols;
	}
}