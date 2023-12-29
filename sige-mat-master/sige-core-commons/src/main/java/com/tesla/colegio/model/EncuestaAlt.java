package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla col_encuesta_alt
 * @author MV
 *
 */
public class EncuestaAlt extends EntidadBase{

	public final static String TABLA = "col_encuesta_alt";
	private Integer id;
	private Integer id_enc_pre;
	private String alt;
	private Integer ptje;
	private Integer ord;
	private EncuestaPreg encuestapreg;	
	private List<EncuestaAlumnoDet> encuestaalumnodets;

	public EncuestaAlt(){
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
	* @return alt
	*/
	public String getAlt(){
		return alt;
	}	

	/**
	* Alternativa 
	* @param alt
	*/
	public void setAlt(String alt) {
		this.alt = alt;
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
	* Obtiene Orden 
	* @return ord
	*/
	public Integer getOrd(){
		return ord;
	}	

	/**
	* Orden 
	* @param ord
	*/
	public void setOrd(Integer ord) {
		this.ord = ord;
	}

	public EncuestaPreg getEncuestaPreg(){
		return encuestapreg;
	}	

	public void setEncuestaPreg(EncuestaPreg encuestapreg) {
		this.encuestapreg = encuestapreg;
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