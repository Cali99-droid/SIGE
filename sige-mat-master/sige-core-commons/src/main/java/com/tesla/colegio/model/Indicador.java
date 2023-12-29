package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla col_indicador
 * @author MV
 *
 */
public class Indicador extends EntidadBase{

	public final static String TABLA = "col_indicador";
	private Integer id;
	private String nom;
	private Integer id_csd;
	private SesionDesempenio sesiondesempenio;	
	private List<SesionIndicador> sesionindicadors;

	public Indicador(){
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
	* Obtiene Indicador 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Indicador 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	* Obtiene Desempeo 
	* @return id_csd
	*/
	public Integer getId_csd(){
		return id_csd;
	}	

	/**
	* Desempeo 
	* @param id_csd
	*/
	public void setId_csd(Integer id_csd) {
		this.id_csd = id_csd;
	}

	public SesionDesempenio getSesionDesempenio(){
		return sesiondesempenio;
	}	

	public void setSesionDesempenio(SesionDesempenio sesiondesempenio) {
		this.sesiondesempenio = sesiondesempenio;
	}
	/**
	* Obtiene lista de Indicadores que se trabajara en la sesion 
	*/
	public List<SesionIndicador> getSesionIndicadors() {
		return sesionindicadors;
	}

	/**
	* Seta Lista de Indicadores que se trabajara en la sesion 
	* @param sesionindicadors
	*/	
	public void setSesionIndicador(List<SesionIndicador> sesionindicadors) {
		this.sesionindicadors = sesionindicadors;
	}
}