package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla col_sesion_desempenio
 * @author MV
 *
 */
public class SesionDesempenio extends EntidadBase{

	public final static String TABLA = "col_sesion_desempenio";
	private Integer id;
	private Integer id_cde;
	private Integer id_ses;
	private Desempenio desempenio;	
	private SesionTipo sesiontipo;	
	private List<Indicador> indicadors;

	public SesionDesempenio(){
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
	* Obtiene Unidad 
	* @return id_cde
	*/
	public Integer getId_cde(){
		return id_cde;
	}	

	/**
	* Unidad 
	* @param id_cde
	*/
	public void setId_cde(Integer id_cde) {
		this.id_cde = id_cde;
	}

	/**
	* Obtiene Session Tipo 
	* @return id_ses
	*/
	public Integer getId_ses(){
		return id_ses;
	}	

	/**
	* Session Tipo 
	* @param id_ses
	*/
	public void setId_ses(Integer id_ses) {
		this.id_ses = id_ses;
	}

	public Desempenio getDesempenio(){
		return desempenio;
	}	

	public void setDesempenio(Desempenio desempenio) {
		this.desempenio = desempenio;
	}
	public SesionTipo getSesionTipo(){
		return sesiontipo;
	}	

	public void setSesionTipo(SesionTipo sesiontipo) {
		this.sesiontipo = sesiontipo;
	}
	/**
	* Obtiene lista de Indicador 
	*/
	public List<Indicador> getIndicadors() {
		return indicadors;
	}

	/**
	* Seta Lista de Indicador 
	* @param indicadors
	*/	
	public void setIndicador(List<Indicador> indicadors) {
		this.indicadors = indicadors;
	}
}