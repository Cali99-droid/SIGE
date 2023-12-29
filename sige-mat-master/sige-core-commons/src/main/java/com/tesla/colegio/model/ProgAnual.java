package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla con_prog_anual
 * @author MV
 *
 */
public class ProgAnual extends EntidadBase{

	public final static String TABLA = "con_prog_anual";
	private Integer id;
	private Integer id_tra;
	private Integer id_niv;
	private Integer id_gra;
	private Integer id_cur;
	private String flg_descarga;
	private Nivel nivel;	
	private Grad grad;	
	private Curso curso;	

	public ProgAnual(){
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
	* Obtiene Nivel al que pertenece el subtema 
	* @return id_niv
	*/
	public Integer getId_niv(){
		return id_niv;
	}	

	/**
	* Nivel al que pertenece el subtema 
	* @param id_niv
	*/
	public void setId_niv(Integer id_niv) {
		this.id_niv = id_niv;
	}

	/**
	* Obtiene Grado Acadmico al que pertenece el subtema 
	* @return id_gra
	*/
	public Integer getId_gra(){
		return id_gra;
	}	

	/**
	* Grado Acadmico al que pertenece el subtema 
	* @param id_gra
	*/
	public void setId_gra(Integer id_gra) {
		this.id_gra = id_gra;
	}

	/**
	* Obtiene Curso al que pertenece el subtema 
	* @return id_cur
	*/
	public Integer getId_cur(){
		return id_cur;
	}	

	/**
	* Curso al que pertenece el subtema 
	* @param id_cur
	*/
	public void setId_cur(Integer id_cur) {
		this.id_cur = id_cur;
	}

	/**
	* Obtiene Flag Programacion Anual 
	* @return flg_descarga
	*/
	public String getFlg_descarga(){
		return flg_descarga;
	}	

	/**
	* Flag Programacion Anual 
	* @param flg_descarga
	*/
	public void setFlg_descarga(String flg_descarga) {
		this.flg_descarga = flg_descarga;
	}

	public Nivel getNivel(){
		return nivel;
	}	

	public void setNivel(Nivel nivel) {
		this.nivel = nivel;
	}
	public Grad getGrad(){
		return grad;
	}	

	public void setGrad(Grad grad) {
		this.grad = grad;
	}
	public Curso getCurso(){
		return curso;
	}	

	public void setCurso(Curso curso) {
		this.curso = curso;
	}
}