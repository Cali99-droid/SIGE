package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla col_encuesta_preg
 * @author MV
 *
 */
public class EncuestaPreg extends EntidadBase{

	public final static String TABLA = "col_encuesta_preg";
	private Integer id;
	private Integer id_enc;
	private Integer id_ctp;
	private String pre;
	private Integer ord;
	private String dep;
	private Encuesta encuesta;	
	private TipoPre tipoenc;	
	private List<EncuestaAlt> encuestaalts;
	private PregDependencia pregdependencias;
	private List<EncuestaAlumnoDet> encuestaalumnodets;

	public EncuestaPreg(){
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
	* Obtiene Tipo Encuesta 
	* @return id_cte
	*/
	public Integer getId_ctp(){
		return id_ctp;
	}	

	/**
	* Tipo Encuesta 
	* @param id_cte
	*/
	public void setId_ctp(Integer id_ctp) {
		this.id_ctp = id_ctp;
	}

	/**
	* Obtiene Pregunta 
	* @return pre
	*/
	public String getPre(){
		return pre;
	}	

	/**
	* Pregunta 
	* @param pre
	*/
	public void setPre(String pre) {
		this.pre = pre;
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

	/**
	* Obtiene Dependencia 
	* @return dep
	*/
	public String getDep(){
		return dep;
	}	

	/**
	* Dependencia 
	* @param dep
	*/
	public void setDep(String dep) {
		this.dep = dep;
	}

	public Encuesta getEncuesta(){
		return encuesta;
	}	

	public void setEncuesta(Encuesta encuesta) {
		this.encuesta = encuesta;
	}
	public TipoPre getTipoEnc(){
		return tipoenc;
	}	

	public void setTipoEnc(TipoPre tipoenc) {
		this.tipoenc = tipoenc;
	}
	/**
	* Obtiene lista de Encuesta Alternativas 
	*/
	public List<EncuestaAlt> getEncuestaAlts() {
		return encuestaalts;
	}

	/**
	* Seta Lista de Encuesta Alternativas 
	* @param encuestaalts
	*/	
	public void setEncuestaAlt(List<EncuestaAlt> encuestaalts) {
		this.encuestaalts = encuestaalts;
	}
	/**
	* Obtiene lista de Preguntas Dependencia 
	*/
	public PregDependencia getPregDependencias() {
		return pregdependencias;
	}

	/**
	* Seta Lista de Preguntas Dependencia 
	* @param pregdependencias
	*/	
	public void setPregDependencia(PregDependencia pregdependencias) {
		this.pregdependencias = pregdependencias;
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