package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla col_encuesta_alumno_det
 * @author MV
 *
 */
public class EncuestaAlumnoDet extends EntidadBase{

	public final static String TABLA = "col_encuesta_alumno_det";
	private Integer id;
	private Integer id_enc_alu;
	private Integer id_enc_pre;
	private Integer id_enc_alt;
	private String res;
	private EncuestaAlumno encuestaalumno;	
	private EncuestaPreg encuestapreg;	
	private EncuestaAlt encuestaalt;	

	public EncuestaAlumnoDet(){
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
	* Obtiene Encuesta Alumno 
	* @return id_enc_alu
	*/
	public Integer getId_enc_alu(){
		return id_enc_alu;
	}	

	/**
	* Encuesta Alumno 
	* @param id_enc_alu
	*/
	public void setId_enc_alu(Integer id_enc_alu) {
		this.id_enc_alu = id_enc_alu;
	}

	/**
	* Obtiene Pregunta 
	* @return id_enc_pre
	*/
	public Integer getId_enc_pre(){
		return id_enc_pre;
	}	

	/**
	* Pregunta 
	* @param id_enc_pre
	*/
	public void setId_enc_pre(Integer id_enc_pre) {
		this.id_enc_pre = id_enc_pre;
	}

	/**
	* Obtiene Alternativa 
	* @return id_enc_alt
	*/
	public Integer getId_enc_alt(){
		return id_enc_alt;
	}	

	/**
	* Alternativa 
	* @param id_enc_alt
	*/
	public void setId_enc_alt(Integer id_enc_alt) {
		this.id_enc_alt = id_enc_alt;
	}

	/**
	* Obtiene Respuesta 
	* @return res
	*/
	public String getRes(){
		return res;
	}	

	/**
	* Respuesta 
	* @param res
	*/
	public void setRes(String res) {
		this.res = res;
	}

	public EncuestaAlumno getEncuestaAlumno(){
		return encuestaalumno;
	}	

	public void setEncuestaAlumno(EncuestaAlumno encuestaalumno) {
		this.encuestaalumno = encuestaalumno;
	}
	public EncuestaPreg getEncuestaPreg(){
		return encuestapreg;
	}	

	public void setEncuestaPreg(EncuestaPreg encuestapreg) {
		this.encuestapreg = encuestapreg;
	}
	public EncuestaAlt getEncuestaAlt(){
		return encuestaalt;
	}	

	public void setEncuestaAlt(EncuestaAlt encuestaalt) {
		this.encuestaalt = encuestaalt;
	}
}