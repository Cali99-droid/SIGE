package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla cvi_error
 * @author MV
 *
 */
public class Error extends EntidadBase{

	public final static String TABLA = "cvi_error";
	private Integer id;
	private String sql_code;
	private String sqlerrm;
	private String error;
	private Integer id_cvi;
	
	public Error(){
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
	* Obtiene Sql Code 
	* @return sql_code
	*/
	public String getSql_code(){
		return sql_code;
	}	

	/**
	* Sql Code 
	* @param sql_code
	*/
	public void setSql_code(String sql_code) {
		this.sql_code = sql_code;
	}

	/**
	* Obtiene Sqlerrm 
	* @return sqlerrm
	*/
	public String getSqlerrm(){
		return sqlerrm;
	}	

	/**
	* Sqlerrm 
	* @param sqlerrm
	*/
	public void setSqlerrm(String sqlerrm) {
		this.sqlerrm = sqlerrm;
	}

	/**
	* Obtiene Error 
	* @return error
	*/
	public String getError(){
		return error;
	}	

	/**
	* Error 
	* @param error
	*/
	public void setError(String error) {
		this.error = error;
	}

	public Integer getId_cvi() {
		return id_cvi;
	}

	public void setId_cvi(Integer id_cvi) {
		this.id_cvi = id_cvi;
	}

}