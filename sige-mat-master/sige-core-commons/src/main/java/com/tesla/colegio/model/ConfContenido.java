package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla ges_conf_contenido
 * @author MV
 *
 */
public class ConfContenido extends EntidadBase{

	public final static String TABLA = "ges_conf_contenido";
	private Integer id;
	private Integer id_emp;
	private String insignia;
	private String logotipo;
	private String lema;
	private Empresa empresa;	

	public ConfContenido(){
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
	* Obtiene insignia 
	* @return insignia
	*/
	public String getInsignia(){
		return insignia;
	}	

	/**
	* insignia 
	* @param insignia
	*/
	public void setInsignia(String insignia) {
		this.insignia = insignia;
	}

	/**
	* Obtiene logotipo 
	* @return logotipo
	*/
	public String getLogotipo(){
		return logotipo;
	}	

	/**
	* logotipo 
	* @param logotipo
	*/
	public void setLogotipo(String logotipo) {
		this.logotipo = logotipo;
	}

	/**
	* Obtiene lema 
	* @return lema
	*/
	public String getLema(){
		return lema;
	}	

	/**
	* lema 
	* @param lema
	*/
	public void setLema(String lema) {
		this.lema = lema;
	}

	public Empresa getEmpresa(){
		return empresa;
	}	

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
}