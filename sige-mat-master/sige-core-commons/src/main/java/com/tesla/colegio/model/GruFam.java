package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

import java.math.BigDecimal;
import java.util.List;

/**
 * Clase entidad que representa a la tabla alu_gru_fam
 * @author MV
 *
 */
public class GruFam extends EntidadBase{

	public final static String TABLA = "alu_gru_fam";
	private Integer id;
	private String cod;
	private String nom;
	private String des;
	private Integer id_dist;
	private Integer id_seg;
	private Integer id_csal;
	private Integer id_usr;
	private String cod_aseg;
	private String direccion;
	private String referencia;
	private Distrito distrito;	
	private BigDecimal longitud;
	private BigDecimal latitud;
	private Integer zoom;
	private List<GruFamFamiliar> gruFamFamiliars;
	private List<GruFamAlumno> gruFamAlumnos;

	//auxiliar
	private Integer id_fam;
	
	public GruFam(){
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
	* Obtiene C&oacute;digo 
	* @return cod
	*/
	public String getCod(){
		return cod;
	}	

	/**
	* C&oacute;digo 
	* @param cod
	*/
	public void setCod(String cod) {
		this.cod = cod;
	}

	/**
	* Obtiene Descripci&oacute;n 
	* @return des
	*/
	public String getDes(){
		return des;
	}	

	/**
	* Descripci&oacute;n 
	* @param des
	*/
	public void setDes(String des) {
		this.des = des;
	}

	/**
	* Obtiene Distrito donde vive la familia 
	* @return id_dist
	*/
	public Integer getId_dist(){
		return id_dist;
	}	

	/**
	* Distrito donde vive la familia 
	* @param id_dist
	*/
	public void setId_dist(Integer id_dist) {
		this.id_dist = id_dist;
	}

	/**
	* Obtiene Direccion del alumno 
	* @return direccion
	*/
	public String getDireccion(){
		return direccion;
	}	

	/**
	* Direccion del alumno 
	* @param direccion
	*/
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public Distrito getDistrito(){
		return distrito;
	}	

	public void setDistrito(Distrito distrito) {
		this.distrito = distrito;
	}
	/**
	* Obtiene lista de Grupo Familiar/familia 
	*/
	public List<GruFamFamiliar> getGruFamFamiliars() {
		return gruFamFamiliars;
	}

	/**
	* Seta Lista de Grupo Familiar/familia 
	* @param gruFamFamiliars
	*/	
	public void setGruFamFamiliar(List<GruFamFamiliar> gruFamFamiliars) {
		this.gruFamFamiliars = gruFamFamiliars;
	}
	/**
	* Obtiene lista de Grupo Familiar/alumno 
	*/
	public List<GruFamAlumno> getGruFamAlumnos() {
		return gruFamAlumnos;
	}

	/**
	* Seta Lista de Grupo Familiar/alumno 
	* @param gruFamAlumnos
	*/	
	public void setGruFamAlumno(List<GruFamAlumno> gruFamAlumnos) {
		this.gruFamAlumnos = gruFamAlumnos;
	}
	
	public BigDecimal getLongitud() {
		return longitud;
	}

	public void setLongitud(BigDecimal longitud) {
		this.longitud = longitud;
	}

	public BigDecimal getLatitud() {
		return latitud;
	}

	public void setLatitud(BigDecimal latitud) {
		this.latitud = latitud;
	}

	public Integer getZoom() {
		return zoom;
	}

	public void setZoom(Integer zoom) {
		this.zoom = zoom;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public Integer getId_fam() {
		return id_fam;
	}

	public void setId_fam(Integer id_fam) {
		this.id_fam = id_fam;
	}

	public Integer getId_seg() {
		return id_seg;
	}

	public void setId_seg(Integer id_seg) {
		this.id_seg = id_seg;
	}

	public Integer getId_csal() {
		return id_csal;
	}

	public void setId_csal(Integer id_csal) {
		this.id_csal = id_csal;
	}

	public String getCod_aseg() {
		return cod_aseg;
	}

	public void setCod_aseg(String cod_aseg) {
		this.cod_aseg = cod_aseg;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public Integer getId_usr() {
		return id_usr;
	}

	public void setId_usr(Integer id_usr) {
		this.id_usr = id_usr;
	}
}