package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla cvi_grupo_aula_virtual
 * @author MV
 *
 */
public class GrupoAulaVirtual extends EntidadBase{

	public final static String TABLA = "cvi_grupo_aula_virtual";
	private Integer id;
	private Integer id_gra;
	private Integer id_cgc;
	private Integer id_anio;
	private String des;
	private Integer nro;
	private String lleno;
	private String id_grupoclass;
	private Grad grad;	
	private GrupoConfig grupoconfig;	
	private List<GrupoAlumno> grupoalumnos;

	public GrupoAulaVirtual(){
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
	* Obtiene Grado 
	* @return id_gra
	*/
	public Integer getId_gra(){
		return id_gra;
	}	

	/**
	* Grado 
	* @param id_gra
	*/
	public void setId_gra(Integer id_gra) {
		this.id_gra = id_gra;
	}

	/**
	* Obtiene Grupo Configuracion 
	* @return id_cgc
	*/
	public Integer getId_cgc(){
		return id_cgc;
	}	

	/**
	* Grupo Configuracion 
	* @param id_cgc
	*/
	public void setId_cgc(Integer id_cgc) {
		this.id_cgc = id_cgc;
	}

	/**
	* Obtiene Descripcion 
	* @return des
	*/
	public String getDes(){
		return des;
	}	

	/**
	* Descripcion 
	* @param des
	*/
	public void setDes(String des) {
		this.des = des;
	}

	/**
	* Obtiene Numero 
	* @return nro
	*/
	public Integer getNro(){
		return nro;
	}	

	/**
	* Numero 
	* @param nro
	*/
	public void setNro(Integer nro) {
		this.nro = nro;
	}

	/**
	* Obtiene LLeno? Si,No 
	* @return lleno
	*/
	public String getLleno(){
		return lleno;
	}	

	/**
	* LLeno? Si,No 
	* @param lleno
	*/
	public void setLleno(String lleno) {
		this.lleno = lleno;
	}

	public Grad getGrad(){
		return grad;
	}	

	public void setGrad(Grad grad) {
		this.grad = grad;
	}
	public GrupoConfig getGrupoConfig(){
		return grupoconfig;
	}	

	public void setGrupoConfig(GrupoConfig grupoconfig) {
		this.grupoconfig = grupoconfig;
	}
	/**
	* Obtiene lista de Grupo Alumno 
	*/
	public List<GrupoAlumno> getGrupoAlumnos() {
		return grupoalumnos;
	}

	/**
	* Seta Lista de Grupo Alumno 
	* @param grupoalumnos
	*/	
	public void setGrupoAlumno(List<GrupoAlumno> grupoalumnos) {
		this.grupoalumnos = grupoalumnos;
	}

	public Integer getId_anio() {
		return id_anio;
	}

	public void setId_anio(Integer id_anio) {
		this.id_anio = id_anio;
	}

	public String getId_grupoclass() {
		return id_grupoclass;
	}

	public void setId_grupoclass(String id_grupoclass) {
		this.id_grupoclass = id_grupoclass;
	}
}