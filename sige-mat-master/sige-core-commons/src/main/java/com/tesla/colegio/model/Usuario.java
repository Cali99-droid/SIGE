package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla seg_usuario
 * @author MV
 *
 */
public class Usuario extends EntidadBase{

	public final static String TABLA = "seg_usuario";
	private Integer id;
	private Integer id_per;
	private Integer id_tra;
	private String login;
	private String password;
	private Integer id_suc;
	private String tipo;
	private Perfil perfil;	
	private Trabajador trabajador;	
	private Sucursal sucursal;	
	private Persona persona;	
	private String ini;
	private List<UsuarioRol> usuariorols;
	private List<UsuarioSucursal> usuariosucursals;
	private List<Trabajador> trabajadors;
	private Integer[] roles;
	private List<UsuarioNivel> usuarioNiveles;
	private Integer[] id_niveles;
	
	public Usuario(){
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

	public Perfil getPerfil(){
		return perfil;
	}	

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}
	public Trabajador getTrabajador(){
		return trabajador;
	}	

	public void setTrabajador(Trabajador trabajador) {
		this.trabajador = trabajador;
	}
	public Sucursal getSucursal(){
		return sucursal;
	}	

	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
	}

	/**
	* Obtiene lista de Usuario Sucursal 
	*/
	public List<UsuarioSucursal> getUsuarioSucursals() {
		return usuariosucursals;
	}

	/**
	* Seta Lista de Usuario Sucursal 
	* @param usuariosucursals
	*/	
	public void setUsuarioSucursal(List<UsuarioSucursal> usuariosucursals) {
		this.usuariosucursals = usuariosucursals;
	}
	/**
	* Obtiene lista de Trabajador de la empresa 
	*/
	public List<Trabajador> getTrabajadors() {
		return trabajadors;
	}

	/**
	* Seta Lista de Trabajador de la empresa 
	* @param trabajadors
	*/	
	public void setTrabajador(List<Trabajador> trabajadors) {
		this.trabajadors = trabajadors;
	}

	public List<UsuarioRol> getUsuariorols() {

		return usuariorols;
	}

	public void setUsuariorols(List<UsuarioRol> usuariorols) {
		this.roles = new Integer[usuariorols.size()];
		int i=0;
		for (UsuarioRol usuarioRol : usuariorols) {
			this.roles[i++] = usuarioRol.getId_rol();
		}
		this.usuariorols = usuariorols;
	}
	
	public Integer[] getRoles() {
		return roles;
	}

	public String getIni() {
		return ini;
	}

	public void setIni(String ini) {
		this.ini = ini;
	}

	public List<UsuarioNivel> getUsuarioNiveles() {
		return usuarioNiveles;
	}

	public void setUsuarioNiveles(List<UsuarioNivel> usuarioNiveles) {
		this.usuarioNiveles = usuarioNiveles;
	}

	public Integer[] getId_niveles() {
		return id_niveles;
	}

	public void setId_niveles(Integer[] id_niveles) {
		this.id_niveles = id_niveles;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Persona getPersona() {
		return persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}


	
	
}