package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.List;

/**
 * Clase entidad que representa a la tabla col_seminario
 * @author MV
 *
 */
public class Seminario extends EntidadBase{

	public final static String TABLA = "col_seminario";
	private Integer id;
	private Integer id_anio;
	private String nom;
	private String corr_envio;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date fec;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date fec_ini_ins;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date fec_fin_ins;
	private String costo;
	private java.math.BigDecimal monto;
	private Anio anio;	
	private List<SemGrupo> semgrupos;
	private List<SemInscripcion> seminscripcions;

	public Seminario(){
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
	* Obtiene Nombre 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Nombre 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	* Obtiene Correo envio 
	* @return corr_envio
	*/
	public String getCorr_envio(){
		return corr_envio;
	}	

	/**
	* Correo envio 
	* @param corr_envio
	*/
	public void setCorr_envio(String corr_envio) {
		this.corr_envio = corr_envio;
	}

	/**
	* Obtiene Fecha del Seminario 
	* @return fec
	*/
	public java.util.Date getFec(){
		return fec;
	}	

	/**
	* Fecha del Seminario 
	* @param fec
	*/
	public void setFec(java.util.Date fec) {
		this.fec = fec;
	}

	/**
	* Obtiene Fecha Inicio Inscripcin 
	* @return fec_ini_ins
	*/
	public java.util.Date getFec_ini_ins(){
		return fec_ini_ins;
	}	

	/**
	* Fecha Inicio Inscripcin 
	* @param fec_ini_ins
	*/
	public void setFec_ini_ins(java.util.Date fec_ini_ins) {
		this.fec_ini_ins = fec_ini_ins;
	}

	/**
	* Obtiene Fecha Fin Inscripcin 
	* @return fec_fin_ins
	*/
	public java.util.Date getFec_fin_ins(){
		return fec_fin_ins;
	}	

	/**
	* Fecha Fin Inscripcin 
	* @param fec_fin_ins
	*/
	public void setFec_fin_ins(java.util.Date fec_fin_ins) {
		this.fec_fin_ins = fec_fin_ins;
	}

	/**
	* Obtiene Costo de la Olimpiada Si?, No? 
	* @return costo
	*/
	public String getCosto(){
		return costo;
	}	

	/**
	* Costo de la Olimpiada Si?, No? 
	* @param costo
	*/
	public void setCosto(String costo) {
		this.costo = costo;
	}

	/**
	* Obtiene Monto 
	* @return monto
	*/
	public java.math.BigDecimal getMonto(){
		return monto;
	}	

	/**
	* Monto 
	* @param monto
	*/
	public void setMonto(java.math.BigDecimal monto) {
		this.monto = monto;
	}

	public Anio getAnio(){
		return anio;
	}	

	public void setAnio(Anio anio) {
		this.anio = anio;
	}
	/**
	* Obtiene lista de Seminario Grupo 
	*/
	public List<SemGrupo> getSemGrupos() {
		return semgrupos;
	}

	/**
	* Seta Lista de Seminario Grupo 
	* @param semgrupos
	*/	
	public void setSemGrupo(List<SemGrupo> semgrupos) {
		this.semgrupos = semgrupos;
	}
	/**
	* Obtiene lista de Inscripcin 
	*/
	public List<SemInscripcion> getSemInscripcions() {
		return seminscripcions;
	}

	/**
	* Seta Lista de Inscripcin 
	* @param seminscripcions
	*/	
	public void setSemInscripcion(List<SemInscripcion> seminscripcions) {
		this.seminscripcions = seminscripcions;
	}
}