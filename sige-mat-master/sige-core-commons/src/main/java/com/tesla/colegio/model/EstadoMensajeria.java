package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla msj_estado_mensajeria
 * 
 * @author MV
 *
 */
public class EstadoMensajeria extends EntidadBase {

	public final static String TABLA = "msj_estado_mensajeria";
	private Integer id;
	private String des;
	private String cod;
	private List<Receptor> receptors;
	private List<Historial> historials;

	public EstadoMensajeria() {
	}

	/**
	 * Obtiene $field.description
	 * 
	 * @return id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * $field.description
	 * 
	 * @param id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Obtiene Descripcion
	 * 
	 * @return des
	 */
	public String getDes() {
		return des;
	}

	/**
	 * Descripcion
	 * 
	 * @param des
	 */
	public void setDes(String des) {
		this.des = des;
	}

	/**
	 * Obtiene Cdigo
	 * 
	 * @return cod
	 */
	public String getCod() {
		return cod;
	}

	/**
	 * Cdigo
	 * 
	 * @param cod
	 */
	public void setCod(String cod) {
		this.cod = cod;
	}

	/**
	 * Obtiene lista de Receptor
	 */
	public List<Receptor> getReceptors() {
		return receptors;
	}

	/**
	 * Seta Lista de Receptor
	 * 
	 * @param receptors
	 */
	public void setReceptor(List<Receptor> receptors) {
		this.receptors = receptors;
	}

	/**
	 * Obtiene lista de Historial
	 */
	public List<Historial> getHistorials() {
		return historials;
	}

	/**
	 * Seta Lista de Historial
	 * 
	 * @param historials
	 */
	public void setHistorial(List<Historial> historials) {
		this.historials = historials;
	}
}