package com.tesla.colegio.model;
 

/**
 * Clase entidad que representa a la tabla seg_usuario
 * @author MV
 *
 */
public class UsuarioSeg  {

	public final static String TABLA = "seg_usuario";
	private Integer id;
	private Integer id_per;
	private Integer id_tra;
	private String login;
	private String password;
	private Integer id_suc;
	private String nombres;
	private Integer[] roles;
 
	private String ini;  
	
	public UsuarioSeg(){
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
	* Obtiene Perfil 
	* @return id_per
	*/
	public Integer getId_per(){
		return id_per;
	}	

	/**
	* Perfil 
	* @param id_per
	*/
	public void setId_per(Integer id_per) {
		this.id_per = id_per;
	}

	/**
	* Obtiene Trabajador 
	* @return id_tra
	*/
	public Integer getId_tra(){
		return id_tra;
	}	

	/**
	* Trabajador 
	* @param id_tra
	*/
	public void setId_tra(Integer id_tra) {
		this.id_tra = id_tra;
	}

	/**
	* Obtiene Login 
	* @return login
	*/
	public String getLogin(){
		return login;
	}	

	/**
	* Login 
	* @param login
	*/
	public void setLogin(String login) {
		this.login = login;
	}

	/**
	* Obtiene Password 
	* @return password
	*/
	public String getPassword(){
		return password;
	}	

	/**
	* Password 
	* @param password
	*/
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	* Obtiene Sucursal 
	* @return id_suc
	*/
	public Integer getId_suc(){
		return id_suc;
	}	

	/**
	* Sucursal 
	* @param id_suc
	*/
	public void setId_suc(Integer id_suc) {
		this.id_suc = id_suc;
	}

	     
	public String getIni() {
		return ini;
	}

	public void setIni(String ini) {
		this.ini = ini;
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public Integer[] getRoles() {
		return roles;
	}

	public void setRoles(Integer[] roles) {
		this.roles = roles;
	}
 

	
	
}