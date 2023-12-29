package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla cvi_grupo_config
 * @author MV
 *
 */
public class GrupoConfig extends EntidadBase{

	public final static String TABLA = "cvi_grupo_config";
	private Integer id;
	private String cap;
	private String des;
	private List<GrupoAulaVirtual> grupos;

	public GrupoConfig(){
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
	* Obtiene Capcaidad de grupo 
	* @return cap
	*/
	public String getCap(){
		return cap;
	}	

	/**
	* Capcaidad de grupo 
	* @param cap
	*/
	public void setCap(String cap) {
		this.cap = cap;
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
	* Obtiene lista de Grupo 
	*/
	public List<GrupoAulaVirtual> getGrupos() {
		return grupos;
	}

	/**
	* Seta Lista de Grupo 
	* @param grupos
	*/	
	public void setGrupo(List<GrupoAulaVirtual> grupos) {
		this.grupos = grupos;
	}
}