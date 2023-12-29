package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Clase entidad que representa a la tabla con_plan_actividades
 * @author MV
 *
 */
public class PlanActividades extends EntidadBase{

	public final static String TABLA = "con_plan_actividades";
	private Integer id;
	private Integer id_cpt;
	private String des;
	private Integer id_enc;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date fec;
	private PlanTutoria plantutoria;	

	public PlanActividades(){
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
	* Obtiene Plan de Tutoria 
	* @return id_cpt
	*/
	public Integer getId_cpt(){
		return id_cpt;
	}	

	/**
	* Plan de Tutoria 
	* @param id_cpt
	*/
	public void setId_cpt(Integer id_cpt) {
		this.id_cpt = id_cpt;
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

	public PlanTutoria getPlanTutoria(){
		return plantutoria;
	}	

	public void setPlanTutoria(PlanTutoria plantutoria) {
		this.plantutoria = plantutoria;
	}

	public Integer getId_enc() {
		return id_enc;
	}

	public void setId_enc(Integer id_enc) {
		this.id_enc = id_enc;
	}
}