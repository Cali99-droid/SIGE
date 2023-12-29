package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.List;

/**
 * Clase entidad que representa a la tabla col_encuesta
 * @author MV
 *
 */
public class Encuesta extends EntidadBase{

	public final static String TABLA = "col_encuesta";
	private Integer id;
	private Integer id_anio;
	private Integer id_gir;
	private String nom;
	private String msj_ini;
	private String msj_fin;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date fec_ini;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date fec_fin;
	private Anio anio;	
	private GiroNegocio gironegocio;	
	private List<EncuestaPreg> encuestapregs;
	private List<EncuestaAlumno> encuestaalumnos;

	public Encuesta(){
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
	* Obtiene Ao academico 
	* @return id_anio
	*/
	public Integer getId_anio(){
		return id_anio;
	}	

	/**
	* Ao academico 
	* @param id_anio
	*/
	public void setId_anio(Integer id_anio) {
		this.id_anio = id_anio;
	}

	/**
	* Obtiene Giro de Negocio 
	* @return id_gir
	*/
	public Integer getId_gir(){
		return id_gir;
	}	

	/**
	* Giro de Negocio 
	* @param id_gir
	*/
	public void setId_gir(Integer id_gir) {
		this.id_gir = id_gir;
	}

	/**
	* Obtiene Nombre de la Encuesta 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Nombre de la Encuesta 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	* Obtiene Mensaje Inicio 
	* @return msj_ini
	*/
	public String getMsj_ini(){
		return msj_ini;
	}	

	/**
	* Mensaje Inicio 
	* @param msj_ini
	*/
	public void setMsj_ini(String msj_ini) {
		this.msj_ini = msj_ini;
	}

	/**
	* Obtiene Mensaje Final 
	* @return msj_fin
	*/
	public String getMsj_fin(){
		return msj_fin;
	}	

	/**
	* Mensaje Final 
	* @param msj_fin
	*/
	public void setMsj_fin(String msj_fin) {
		this.msj_fin = msj_fin;
	}

	/**
	* Obtiene Fecha Inicio 
	* @return fec_ini
	*/
	public java.util.Date getFec_ini(){
		return fec_ini;
	}	

	/**
	* Fecha Inicio 
	* @param fec_ini
	*/
	public void setFec_ini(java.util.Date fec_ini) {
		this.fec_ini = fec_ini;
	}

	/**
	* Obtiene Fecha Fin 
	* @return fec_fin
	*/
	public java.util.Date getFec_fin(){
		return fec_fin;
	}	

	/**
	* Fecha Fin 
	* @param fec_fin
	*/
	public void setFec_fin(java.util.Date fec_fin) {
		this.fec_fin = fec_fin;
	}

	public Anio getAnio(){
		return anio;
	}	

	public void setAnio(Anio anio) {
		this.anio = anio;
	}
	public GiroNegocio getGiroNegocio(){
		return gironegocio;
	}	

	public void setGiroNegocio(GiroNegocio gironegocio) {
		this.gironegocio = gironegocio;
	}
	/**
	* Obtiene lista de Encuesta Preguntas 
	*/
	public List<EncuestaPreg> getEncuestaPregs() {
		return encuestapregs;
	}

	/**
	* Seta Lista de Encuesta Preguntas 
	* @param encuestapregs
	*/	
	public void setEncuestaPreg(List<EncuestaPreg> encuestapregs) {
		this.encuestapregs = encuestapregs;
	}
	/**
	* Obtiene lista de Encuesta Alumno 
	*/
	public List<EncuestaAlumno> getEncuestaAlumnos() {
		return encuestaalumnos;
	}

	/**
	* Seta Lista de Encuesta Alumno 
	* @param encuestaalumnos
	*/	
	public void setEncuestaAlumno(List<EncuestaAlumno> encuestaalumnos) {
		this.encuestaalumnos = encuestaalumnos;
	}
}