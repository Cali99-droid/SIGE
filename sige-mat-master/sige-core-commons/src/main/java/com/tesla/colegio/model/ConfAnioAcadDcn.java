package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla col_conf_anio_acad_dcn
 * @author MV
 *
 */
public class ConfAnioAcadDcn extends EntidadBase{

	public final static String TABLA = "col_conf_anio_acad_dcn";
	private Integer id;
	private Integer id_anio;
	private Integer id_dcn;
	private Anio anio;	
	private DisenioCurricular diseniocurricular;	

	public ConfAnioAcadDcn(){
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
	* Obtiene Disenio Curricular 
	* @return id_dcn
	*/
	public Integer getId_dcn(){
		return id_dcn;
	}	

	/**
	* Disenio Curricular 
	* @param id_dcn
	*/
	public void setId_dcn(Integer id_dcn) {
		this.id_dcn = id_dcn;
	}

	public Anio getAnio(){
		return anio;
	}	

	public void setAnio(Anio anio) {
		this.anio = anio;
	}
	public DisenioCurricular getDisenioCurricular(){
		return diseniocurricular;
	}	

	public void setDisenioCurricular(DisenioCurricular diseniocurricular) {
		this.diseniocurricular = diseniocurricular;
	}
}