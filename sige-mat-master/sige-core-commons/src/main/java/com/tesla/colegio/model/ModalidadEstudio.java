package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla cat_modalidad_estudio
 * @author MV
 *
 */
public class ModalidadEstudio extends EntidadBase{

	public final static String TABLA = "cat_modalidad_estudio";
	private Integer id;
	private String nom;
	private String des;
	private List<ConfMensualidad> confmensualidads;
	private List<ConfCuota> confcuotas;
	private List<Aula> aulas;
	private List<AulaModalidadDet> aulamodalidaddets;

	public ModalidadEstudio(){
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
	* Obtiene Modalidad 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Modalidad 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
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
	* Obtiene lista de Configuracin de mensualidad 
	*/
	public List<ConfMensualidad> getConfMensualidads() {
		return confmensualidads;
	}

	/**
	* Seta Lista de Configuracin de mensualidad 
	* @param confmensualidads
	*/	
	public void setConfMensualidad(List<ConfMensualidad> confmensualidads) {
		this.confmensualidads = confmensualidads;
	}
	/**
	* Obtiene lista de Configuracin de ingreso 
	*/
	public List<ConfCuota> getConfCuotas() {
		return confcuotas;
	}

	/**
	* Seta Lista de Configuracin de ingreso 
	* @param confcuotas
	*/	
	public void setConfCuota(List<ConfCuota> confcuotas) {
		this.confcuotas = confcuotas;
	}
	/**
	* Obtiene lista de Aula del colegio 
	*/
	public List<Aula> getAulas() {
		return aulas;
	}

	/**
	* Seta Lista de Aula del colegio 
	* @param aulas
	*/	
	public void setAula(List<Aula> aulas) {
		this.aulas = aulas;
	}
	/**
	* Obtiene lista de Aula Modalidad Detalle 
	*/
	public List<AulaModalidadDet> getAulaModalidadDets() {
		return aulamodalidaddets;
	}

	/**
	* Seta Lista de Aula Modalidad Detalle 
	* @param aulamodalidaddets
	*/	
	public void setAulaModalidadDet(List<AulaModalidadDet> aulamodalidaddets) {
		this.aulamodalidaddets = aulamodalidaddets;
	}
}