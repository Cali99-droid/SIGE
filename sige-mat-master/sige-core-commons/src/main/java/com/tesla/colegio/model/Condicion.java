package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla mat_condicion
 * @author MV
 *
 */
public class Condicion extends EntidadBase{

	public final static String TABLA = "mat_condicion";
	private Integer id;
	private Integer id_cond;
	private Integer id_mat;
	private String tit;
	private String des;
	private String mat_blo;
	private String obs_blo;
	private String tip_blo;
	private CondAlumno condalumno;	
	private Matricula matricula;
	private Alumno alumno;

	public Condicion(){
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
	* Obtiene Condicin 
	* @return id_cond
	*/
	public Integer getId_cond(){
		return id_cond;
	}	

	/**
	* Condicin 
	* @param id_cond
	*/
	public void setId_cond(Integer id_cond) {
		this.id_cond = id_cond;
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
	* Obtiene Ttulo 
	* @return tit
	*/
	public String getTit(){
		return tit;
	}	

	/**
	* Ttulo 
	* @param tit
	*/
	public void setTit(String tit) {
		this.tit = tit;
	}

	/**
	* Obtiene Descripcin 
	* @return des
	*/
	public String getDes(){
		return des;
	}	

	/**
	* Descripcin 
	* @param des
	*/
	public void setDes(String des) {
		this.des = des;
	}

	public CondAlumno getCondAlumno(){
		return condalumno;
	}	

	public void setCondAlumno(CondAlumno condalumno) {
		this.condalumno = condalumno;
	}
	public Matricula getMatricula(){
		return matricula;
	}	

	public void setMatricula(Matricula matricula) {
		this.matricula = matricula;
	}

	public Alumno getAlumno() {
		return alumno;
	}

	public void setAlumno(Alumno alumno) {
		this.alumno = alumno;
	}

	public String getMat_blo() {
		return mat_blo;
	}

	public void setMat_blo(String mat_blo) {
		this.mat_blo = mat_blo;
	}

	public String getObs_blo() {
		return obs_blo;
	}

	public void setObs_blo(String obs_blo) {
		this.obs_blo = obs_blo;
	}

	public String getTip_blo() {
		return tip_blo;
	}

	public void setTip_blo(String tip_blo) {
		this.tip_blo = tip_blo;
	}


}