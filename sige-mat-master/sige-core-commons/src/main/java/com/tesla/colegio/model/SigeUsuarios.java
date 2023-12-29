package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla cvi_sige_usuarios
 * @author MV
 *
 */
public class SigeUsuarios extends EntidadBase{

	public final static String TABLA = "cvi_sige_usuarios";
	private Integer id;
	private Integer id_alu;
	private String Nombres;
	private String Apellidos;
	private String Correo;
	private String Clave;
	private Alumno alumno;	

	public SigeUsuarios(){
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
	* Obtiene Alumno 
	* @return id_alu
	*/
	public Integer getId_alu(){
		return id_alu;
	}	

	/**
	* Alumno 
	* @param id_alu
	*/
	public void setId_alu(Integer id_alu) {
		this.id_alu = id_alu;
	}

	/**
	* Obtiene Nombres 
	* @return Nombres
	*/
	public String getNombres(){
		return Nombres;
	}	

	/**
	* Nombres 
	* @param Nombres
	*/
	public void setNombres(String Nombres) {
		this.Nombres = Nombres;
	}

	/**
	* Obtiene Apellidos 
	* @return Apellidos
	*/
	public String getApellidos(){
		return Apellidos;
	}	

	/**
	* Apellidos 
	* @param Apellidos
	*/
	public void setApellidos(String Apellidos) {
		this.Apellidos = Apellidos;
	}

	/**
	* Obtiene Correo 
	* @return Correo
	*/
	public String getCorreo(){
		return Correo;
	}	

	/**
	* Correo 
	* @param Correo
	*/
	public void setCorreo(String Correo) {
		this.Correo = Correo;
	}

	/**
	* Obtiene Clave 
	* @return Clave
	*/
	public String getClave(){
		return Clave;
	}	

	/**
	* Clave 
	* @param Clave
	*/
	public void setClave(String Clave) {
		this.Clave = Clave;
	}

	public Alumno getAlumno(){
		return alumno;
	}	

	public void setAlumno(Alumno alumno) {
		this.alumno = alumno;
	}
}