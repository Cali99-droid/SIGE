package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla col_aula_modalidad_det
 * @author MV
 *
 */
public class AulaModalidadDet extends EntidadBase{

	public final static String TABLA = "col_aula_modalidad_det";
	private Integer id;
	private Integer id_au;
	private Integer id_cme;
	private Integer mes;
	private Aula aula;	
	private ModalidadEstudio modalidadestudio;	

	public AulaModalidadDet(){
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
	* Obtiene Aula 
	* @return id_au
	*/
	public Integer getId_au(){
		return id_au;
	}	

	/**
	* Aula 
	* @param id_au
	*/
	public void setId_au(Integer id_au) {
		this.id_au = id_au;
	}

	/**
	* Obtiene Modalidad Estudio 
	* @return id_cme
	*/
	public Integer getId_cme(){
		return id_cme;
	}	

	/**
	* Modalidad Estudio 
	* @param id_cme
	*/
	public void setId_cme(Integer id_cme) {
		this.id_cme = id_cme;
	}

	/**
	* Obtiene Mes 
	* @return mes
	*/
	public Integer getMes(){
		return mes;
	}	

	/**
	* Mes 
	* @param mes
	*/
	public void setMes(Integer mes) {
		this.mes = mes;
	}

	public Aula getAula(){
		return aula;
	}	

	public void setAula(Aula aula) {
		this.aula = aula;
	}
	public ModalidadEstudio getModalidadEstudio(){
		return modalidadestudio;
	}	

	public void setModalidadEstudio(ModalidadEstudio modalidadestudio) {
		this.modalidadestudio = modalidadestudio;
	}
}