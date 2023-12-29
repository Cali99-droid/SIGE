package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla col_encuesta_alumno
 * @author MV
 *
 */
public class EncuestaAlumno extends EntidadBase{

	public final static String TABLA = "col_encuesta_alumno";
	private Integer id;
	private Integer id_enc;
	private Integer id_mat;
	private String num;
	private Integer ptje;
	private String res;
	private Encuesta encuesta;	
	private Matricula matricula;	
	private List<EncuestaAlumnoDet> encuestaalumnodets;

	public EncuestaAlumno(){
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
	* Obtiene Encuesta 
	* @return id_enc
	*/
	public Integer getId_enc(){
		return id_enc;
	}	

	/**
	* Encuesta 
	* @param id_enc
	*/
	public void setId_enc(Integer id_enc) {
		this.id_enc = id_enc;
	}

	/**
	* Obtiene Matcula Alumno 
	* @return id_mat
	*/
	public Integer getId_mat(){
		return id_mat;
	}	

	/**
	* Matcula Alumno 
	* @param id_mat
	*/
	public void setId_mat(Integer id_mat) {
		this.id_mat = id_mat;
	}

	/**
	* Obtiene Numero 
	* @return num
	*/
	public String getNum(){
		return num;
	}	

	/**
	* Numero 
	* @param num
	*/
	public void setNum(String num) {
		this.num = num;
	}

	/**
	* Obtiene Puntaje 
	* @return ptje
	*/
	public Integer getPtje(){
		return ptje;
	}	

	/**
	* Puntaje 
	* @param ptje
	*/
	public void setPtje(Integer ptje) {
		this.ptje = ptje;
	}

	/**
	* Obtiene Resultado 
	* @return res
	*/
	public String getRes(){
		return res;
	}	

	/**
	* Resultado 
	* @param res
	*/
	public void setRes(String res) {
		this.res = res;
	}

	public Encuesta getEncuesta(){
		return encuesta;
	}	

	public void setEncuesta(Encuesta encuesta) {
		this.encuesta = encuesta;
	}
	public Matricula getMatricula(){
		return matricula;
	}	

	public void setMatricula(Matricula matricula) {
		this.matricula = matricula;
	}
	/**
	* Obtiene lista de Encuesta Alumno Detalle 
	*/
	public List<EncuestaAlumnoDet> getEncuestaAlumnoDets() {
		return encuestaalumnodets;
	}

	/**
	* Seta Lista de Encuesta Alumno Detalle 
	* @param encuestaalumnodets
	*/	
	public void setEncuestaAlumnoDet(List<EncuestaAlumnoDet> encuestaalumnodets) {
		this.encuestaalumnodets = encuestaalumnodets;
	}
}