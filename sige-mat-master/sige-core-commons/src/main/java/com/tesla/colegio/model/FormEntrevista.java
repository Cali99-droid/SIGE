package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Clase entidad que representa a la tabla con_form_entrevista
 * @author MV
 *
 */
public class FormEntrevista extends EntidadBase{

	public final static String TABLA = "con_form_entrevista";
	private Integer id;
	private Integer id_inc;
	private Integer id_cfi;
	private Integer id_mat;
	private Integer id_tra;
	private Integer id_fam;
	private Integer id_vic;
	private String des;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date fec;
	private String archivo;
	private Incidencia incidencia;	
	private FormatoIncidencia formatoincidencia;	
	private Matricula matricula;	
	private Trabajador trabajador;	
	private Familiar familiar;	

	public FormEntrevista(){
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
	* Obtiene Incidencia 
	* @return id_inc
	*/
	public Integer getId_inc(){
		return id_inc;
	}	

	/**
	* Incidencia 
	* @param id_inc
	*/
	public void setId_inc(Integer id_inc) {
		this.id_inc = id_inc;
	}

	/**
	* Obtiene Formato Incidencia 
	* @return id_cfi
	*/
	public Integer getId_cfi(){
		return id_cfi;
	}	

	/**
	* Formato Incidencia 
	* @param id_cfi
	*/
	public void setId_cfi(Integer id_cfi) {
		this.id_cfi = id_cfi;
	}

	/**
	* Obtiene Alumno 
	* @return id_mat
	*/
	public Integer getId_mat(){
		return id_mat;
	}	

	/**
	* Alumno 
	* @param id_mat
	*/
	public void setId_mat(Integer id_mat) {
		this.id_mat = id_mat;
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
	* Obtiene Familiar 
	* @return id_fam
	*/
	public Integer getId_fam(){
		return id_fam;
	}	

	/**
	* Familiar 
	* @param id_fam
	*/
	public void setId_fam(Integer id_fam) {
		this.id_fam = id_fam;
	}

	/**
	* Obtiene Descripcin de los hechos 
	* @return des
	*/
	public String getDes(){
		return des;
	}	

	/**
	* Descripcin de los hechos 
	* @param des
	*/
	public void setDes(String des) {
		this.des = des;
	}

	/**
	* Obtiene Fecha 
	* @return fec
	*/
	public java.util.Date getFec(){
		return fec;
	}	

	/**
	* Fecha 
	* @param fec
	*/
	public void setFec(java.util.Date fec) {
		this.fec = fec;
	}

	/**
	* Obtiene Documento Escaneado 
	* @return archivo
	*/
	public String getArchivo(){
		return archivo;
	}	

	/**
	* Documento Escaneado 
	* @param archivo
	*/
	public void setArchivo(String archivo) {
		this.archivo = archivo;
	}

	public Incidencia getIncidencia(){
		return incidencia;
	}	

	public void setIncidencia(Incidencia incidencia) {
		this.incidencia = incidencia;
	}
	public FormatoIncidencia getFormatoIncidencia(){
		return formatoincidencia;
	}	

	public void setFormatoIncidencia(FormatoIncidencia formatoincidencia) {
		this.formatoincidencia = formatoincidencia;
	}
	public Matricula getMatricula(){
		return matricula;
	}	

	public void setMatricula(Matricula matricula) {
		this.matricula = matricula;
	}
	public Trabajador getTrabajador(){
		return trabajador;
	}	

	public void setTrabajador(Trabajador trabajador) {
		this.trabajador = trabajador;
	}
	public Familiar getFamiliar(){
		return familiar;
	}	

	public void setFamiliar(Familiar familiar) {
		this.familiar = familiar;
	}

	public Integer getId_vic() {
		return id_vic;
	}

	public void setId_vic(Integer id_vic) {
		this.id_vic = id_vic;
	}
}