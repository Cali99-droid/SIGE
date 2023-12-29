package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla alu_gru_fam_alumno
 * @author MV
 *
 */
public class GruFamAlumno extends EntidadBase{

	public final static String TABLA = "alu_gru_fam_alumno";
	private Integer id;
	private Integer id_gpf;
	private Integer id_alu;
	private GruFam gruFam;	
	private Alumno alumno;	
	private Persona persona;

	public GruFamAlumno(){
	}

	/**
	* Obtiene Llave Principal 
	* @return id
	*/
	public Integer getId(){
		return id;
	}	

	/**
	* Llave Principal 
	* @param id
	*/
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	* Obtiene Grupo familiar 
	* @return id_gpf
	*/
	public Integer getId_gpf(){
		return id_gpf;
	}	

	/**
	* Grupo familiar 
	* @param id_gpf
	*/
	public void setId_gpf(Integer id_gpf) {
		this.id_gpf = id_gpf;
	}

	/**
	* Obtiene Familiar 
	* @return id_alu
	*/
	public Integer getId_alu(){
		return id_alu;
	}	

	/**
	* Familiar 
	* @param id_alu
	*/
	public void setId_alu(Integer id_alu) {
		this.id_alu = id_alu;
	}

	public GruFam getGruFam(){
		return gruFam;
	}	

	public void setGruFam(GruFam gruFam) {
		this.gruFam = gruFam;
	}
	public Alumno getAlumno(){
		return alumno;
	}	

	public void setAlumno(Alumno alumno) {
		this.alumno = alumno;
	}

	public Persona getPersona() {
		return persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

}