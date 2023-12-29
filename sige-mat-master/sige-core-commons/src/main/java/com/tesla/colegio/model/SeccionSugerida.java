package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla mat_seccion_sugerida
 * @author MV
 *
 */
public class SeccionSugerida extends EntidadBase{

	public final static String TABLA = "mat_seccion_sugerida";
	private Integer id;
	private Integer id_mat;
	private Integer id_au_nue;
	private Integer id_anio;
	private Integer id_gra_nue;
	private Integer id_suc_nue;
	
	private Matricula matricula;	
	private Aula aula;	

	public SeccionSugerida(){
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
	* Obtiene Matricula 
	* @return id_mat
	*/
	public Integer getId_mat(){
		return id_mat;
	}	

	/**
	* Matricula 
	* @param id_mat
	*/
	public void setId_mat(Integer id_mat) {
		this.id_mat = id_mat;
	}

	/**
	* Obtiene Aula sugerida 
	* @return id_au_nue
	*/
	public Integer getId_au_nue(){
		return id_au_nue;
	}	

	/**
	* Aula sugerida 
	* @param id_au_nue
	*/
	public void setId_au_nue(Integer id_au_nue) {
		this.id_au_nue = id_au_nue;
	}

	public Matricula getMatricula(){
		return matricula;
	}	

	public void setMatricula(Matricula matricula) {
		this.matricula = matricula;
	}
	public Aula getAula(){
		return aula;
	}	

	public void setAula(Aula aula) {
		this.aula = aula;
	}

	public Integer getId_anio() {
		return id_anio;
	}

	public void setId_anio(Integer id_anio) {
		this.id_anio = id_anio;
	}

	public Integer getId_gra_nue() {
		return id_gra_nue;
	}

	public void setId_gra_nue(Integer id_gra_nue) {
		this.id_gra_nue = id_gra_nue;
	}

	public Integer getId_suc_nue() {
		return id_suc_nue;
	}

	public void setId_suc_nue(Integer id_suc_nue) {
		this.id_suc_nue = id_suc_nue;
	}
	
}