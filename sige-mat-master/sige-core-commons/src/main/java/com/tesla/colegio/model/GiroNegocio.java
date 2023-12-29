package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla ges_giro_negocio
 * @author MV
 *
 */
public class GiroNegocio extends EntidadBase{

	public final static String TABLA = "ges_giro_negocio";
	private Integer id;
	private Integer id_emp;
	private String nom;
	private String des;
	private Empresa empresa;	
	private List<GiroSucursal> giroSucursals;

	public GiroNegocio(){
	}

	/**
	* Obtiene Llave principal 
	* @return id
	*/
	public Integer getId(){
		return id;
	}	

	/**
	* Llave principal 
	* @param id
	*/
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	* Obtiene Empresa 
	* @return id_emp
	*/
	public Integer getId_emp(){
		return id_emp;
	}	

	/**
	* Empresa 
	* @param id_emp
	*/
	public void setId_emp(Integer id_emp) {
		this.id_emp = id_emp;
	}

	/**
	* Obtiene Nombre  
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Nombre  
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	* Obtiene Descripcin del area 
	* @return des
	*/
	public String getDes(){
		return des;
	}	

	/**
	* Descripcin del area 
	* @param des
	*/
	public void setDes(String des) {
		this.des = des;
	}

	public Empresa getEmpresa(){
		return empresa;
	}	

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
	/**
	* Obtiene lista de Giro de negocio/Sucursal 
	*/
	public List<GiroSucursal> getGiroSucursals() {
		return giroSucursals;
	}

	/**
	* Seta Lista de Giro de negocio/Sucursal 
	* @param giroSucursals
	*/	
	public void setGiroSucursal(List<GiroSucursal> giroSucursals) {
		this.giroSucursals = giroSucursals;
	}
}