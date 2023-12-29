package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla cat_per_aca_nivel
 * @author MV
 *
 */
public class PerAcaNivel extends EntidadBase{

	public final static String TABLA = "cat_per_aca_nivel";
	private Integer id;
	private Integer id_anio;
	private Integer id_niv;
	private Integer id_gir;
	private Integer id_cpa;
	private Nivel nivel;	
	private PeriodoAca periodoaca;	
	private GiroNegocio giroNegocio;
	private List<PerUni> perunis;

	public PerAcaNivel(){
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
	* Obtiene Nivel 
	* @return id_niv
	*/
	public Integer getId_niv(){
		return id_niv;
	}	

	/**
	* Nivel 
	* @param id_niv
	*/
	public void setId_niv(Integer id_niv) {
		this.id_niv = id_niv;
	}

	/**
	* Obtiene Tipo de Periodo Academico 
	* @return id_cpa
	*/
	public Integer getId_cpa(){
		return id_cpa;
	}	

	/**
	* Tipo de Periodo Academico 
	* @param id_cpa
	*/
	public void setId_cpa(Integer id_cpa) {
		this.id_cpa = id_cpa;
	}

	public Nivel getNivel(){
		return nivel;
	}	

	public void setNivel(Nivel nivel) {
		this.nivel = nivel;
	}
	public PeriodoAca getPeriodoAca(){
		return periodoaca;
	}	

	public void setPeriodoAca(PeriodoAca periodoaca) {
		this.periodoaca = periodoaca;
	}
	/**
	* Obtiene lista de Periodo Unidad 
	*/
	public List<PerUni> getPerUnis() {
		return perunis;
	}

	/**
	* Seta Lista de Periodo Unidad 
	* @param perunis
	*/	
	public void setPerUni(List<PerUni> perunis) {
		this.perunis = perunis;
	}

	public Integer getId_anio() {
		return id_anio;
	}

	public void setId_anio(Integer id_anio) {
		this.id_anio = id_anio;
	}

	public Integer getId_gir() {
		return id_gir;
	}

	public void setId_gir(Integer id_gir) {
		this.id_gir = id_gir;
	}

	public GiroNegocio getGiroNegocio() {
		return giroNegocio;
	}

	public void setGiroNegocio(GiroNegocio giroNegocio) {
		this.giroNegocio = giroNegocio;
	}
}