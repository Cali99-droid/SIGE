package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla col_sesion_tipo
 * @author MV
 *
 */
public class SesionTipo extends EntidadBase{

	public final static String TABLA = "col_sesion_tipo";
	private Integer id;
	private Integer id_uns;
	private Integer id_cts;
	private TipoSesion tiposesion;	
	private List<SesionDesempenio> sesiondesempenios;

	public SesionTipo(){
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
	* Obtiene Sesin 
	* @return id_uns
	*/
	public Integer getId_uns(){
		return id_uns;
	}	

	/**
	* Sesin 
	* @param id_uns
	*/
	public void setId_uns(Integer id_uns) {
		this.id_uns = id_uns;
	}

	/**
	* Obtiene Tipo de Sesin 
	* @return id_cts
	*/
	public Integer getId_cts(){
		return id_cts;
	}	

	/**
	* Tipo de Sesin 
	* @param id_cts
	*/
	public void setId_cts(Integer id_cts) {
		this.id_cts = id_cts;
	}

	public TipoSesion getTipoSesion(){
		return tiposesion;
	}	

	public void setTipoSesion(TipoSesion tiposesion) {
		this.tiposesion = tiposesion;
	}
	/**
	* Obtiene lista de Desempeo Indicador 
	*/
	public List<SesionDesempenio> getSesionDesempenios() {
		return sesiondesempenios;
	}

	/**
	* Seta Lista de Desempeo Indicador 
	* @param sesiondesempenios
	*/	
	public void setSesionDesempenio(List<SesionDesempenio> sesiondesempenios) {
		this.sesiondesempenios = sesiondesempenios;
	}
}