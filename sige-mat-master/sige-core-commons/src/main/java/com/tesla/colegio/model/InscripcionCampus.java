package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla cvi_inscripcion_campus
 * @author MV
 *
 */
public class InscripcionCampus extends EntidadBase{

	public final static String TABLA = "cvi_inscripcion_campus";
	private Integer id;
	private Integer id_alu;
	private Integer id_fam;
	private Integer id_anio;
	private String tc_acept;
	private String tc_not_acept_mot;
	private Integer id_error;
	private Alumno alumno;	
	private Familiar familiar;	
	private Anio anio;	
	private List<UsuarioCampus> usuariocampuss;

	public InscripcionCampus(){
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
	* Obtiene Apoderado 
	* @return id_fam
	*/
	public Integer getId_fam(){
		return id_fam;
	}	

	/**
	* Apoderado 
	* @param id_fam
	*/
	public void setId_fam(Integer id_fam) {
		this.id_fam = id_fam;
	}

	/**
	* Obtiene Anio 
	* @return id_anio
	*/
	public Integer getId_anio(){
		return id_anio;
	}	

	/**
	* Anio 
	* @param id_anio
	*/
	public void setId_anio(Integer id_anio) {
		this.id_anio = id_anio;
	}

	/**
	* Obtiene Terminos y Condiciones Aceptado? 
	* @return tc_acept
	*/
	public String getTc_acept(){
		return tc_acept;
	}	

	/**
	* Terminos y Condiciones Aceptado? 
	* @param tc_acept
	*/
	public void setTc_acept(String tc_acept) {
		this.tc_acept = tc_acept;
	}

	/**
	* Obtiene Motivo por el cual no acepta los TyC 
	* @return tc_not_acept_mot
	*/
	public String getTc_not_acept_mot(){
		return tc_not_acept_mot;
	}	

	/**
	* Motivo por el cual no acepta los TyC 
	* @param tc_not_acept_mot
	*/
	public void setTc_not_acept_mot(String tc_not_acept_mot) {
		this.tc_not_acept_mot = tc_not_acept_mot;
	}

	/**
	* Obtiene Error 
	* @return id_error
	*/
	public Integer getId_error(){
		return id_error;
	}	

	/**
	* Error 
	* @param id_error
	*/
	public void setId_error(Integer id_error) {
		this.id_error = id_error;
	}

	public Alumno getAlumno(){
		return alumno;
	}	

	public void setAlumno(Alumno alumno) {
		this.alumno = alumno;
	}
	public Familiar getFamiliar(){
		return familiar;
	}	

	public void setFamiliar(Familiar familiar) {
		this.familiar = familiar;
	}
	public Anio getAnio(){
		return anio;
	}	

	public void setAnio(Anio anio) {
		this.anio = anio;
	}
	/**
	* Obtiene lista de Usuario Campus Virtual 
	*/
	public List<UsuarioCampus> getUsuarioCampuss() {
		return usuariocampuss;
	}

	/**
	* Seta Lista de Usuario Campus Virtual 
	* @param usuariocampuss
	*/	
	public void setUsuarioCampus(List<UsuarioCampus> usuariocampuss) {
		this.usuariocampuss = usuariocampuss;
	}
}