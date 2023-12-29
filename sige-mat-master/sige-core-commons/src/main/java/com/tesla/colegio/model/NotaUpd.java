package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla aud_nota_upd
 * @author MV
 *
 */
public class NotaUpd extends EntidadBase{

	public final static String TABLA = "aud_nota_upd";
	private Integer id;
	private Integer id_tra;
	private Integer id_nni;
	private Integer nota_ant;
	private Integer nota_act;
	private Trabajador trabajador;	
	private NotaIndicador notaindicador;	

	public NotaUpd(){
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
	* Obtiene Docente 
	* @return id_tra
	*/
	public Integer getId_tra(){
		return id_tra;
	}	

	/**
	* Docente 
	* @param id_tra
	*/
	public void setId_tra(Integer id_tra) {
		this.id_tra = id_tra;
	}

	/**
	* Obtiene Nota Indicador 
	* @return id_nni
	*/
	public Integer getId_nni(){
		return id_nni;
	}	

	/**
	* Nota Indicador 
	* @param id_nni
	*/
	public void setId_nni(Integer id_nni) {
		this.id_nni = id_nni;
	}

	/**
	* Obtiene Nota Anterior 
	* @return nota_ant
	*/
	public Integer getNota_ant(){
		return nota_ant;
	}	

	/**
	* Nota Anterior 
	* @param nota_ant
	*/
	public void setNota_ant(Integer nota_ant) {
		this.nota_ant = nota_ant;
	}

	/**
	* Obtiene Nota Actual 
	* @return nota_act
	*/
	public Integer getNota_act(){
		return nota_act;
	}	

	/**
	* Nota Actual 
	* @param nota_act
	*/
	public void setNota_act(Integer nota_act) {
		this.nota_act = nota_act;
	}

	public Trabajador getTrabajador(){
		return trabajador;
	}	

	public void setTrabajador(Trabajador trabajador) {
		this.trabajador = trabajador;
	}
	public NotaIndicador getNotaIndicador(){
		return notaindicador;
	}	

	public void setNotaIndicador(NotaIndicador notaindicador) {
		this.notaindicador = notaindicador;
	}
}