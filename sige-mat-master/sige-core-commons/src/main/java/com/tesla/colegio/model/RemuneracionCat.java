package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla rhh_remuneracion_cat
 * @author MV
 *
 */
public class RemuneracionCat extends EntidadBase{

	public final static String TABLA = "rhh_remuneracion_cat";
	private Integer id;
	private Integer id_anio;
	private Integer id_lcarr;
	private Integer id_cden;
	private Integer id_cocu;
	private java.math.BigDecimal rem;
	private Anio anio;	
	private LineaCarrera lineacarrera;	
	private Denominacion denominacion;	
	private CategoriaOcupacional categoriaocupacional;	

	public RemuneracionCat(){
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
	* Obtiene Anio 
	* @return id_anio
	*/
	public Integer getId_anio(){
		return id_anio;
	}	

	/**
	* Anio 
	* @param id_anio
	*/
	public void setId_anio(Integer id_anio) {
		this.id_anio = id_anio;
	}

	/**
	* Obtiene Linea Carrera 
	* @return id_lcarr
	*/
	public Integer getId_lcarr(){
		return id_lcarr;
	}	

	/**
	* Linea Carrera 
	* @param id_lcarr
	*/
	public void setId_lcarr(Integer id_lcarr) {
		this.id_lcarr = id_lcarr;
	}

	/**
	* Obtiene Denominacion 
	* @return id_cden
	*/
	public Integer getId_cden(){
		return id_cden;
	}	

	/**
	* Denominacion 
	* @param id_cden
	*/
	public void setId_cden(Integer id_cden) {
		this.id_cden = id_cden;
	}

	/**
	* Obtiene Categoria Ocupacional 
	* @return id_cocu
	*/
	public Integer getId_cocu(){
		return id_cocu;
	}	

	/**
	* Categoria Ocupacional 
	* @param id_cocu
	*/
	public void setId_cocu(Integer id_cocu) {
		this.id_cocu = id_cocu;
	}

	/**
	* Obtiene Remuneracion 
	* @return rem
	*/
	public java.math.BigDecimal getRem(){
		return rem;
	}	

	/**
	* Remuneracion 
	* @param rem
	*/
	public void setRem(java.math.BigDecimal rem) {
		this.rem = rem;
	}

	public Anio getAnio(){
		return anio;
	}	

	public void setAnio(Anio anio) {
		this.anio = anio;
	}
	public LineaCarrera getLineaCarrera(){
		return lineacarrera;
	}	

	public void setLineaCarrera(LineaCarrera lineacarrera) {
		this.lineacarrera = lineacarrera;
	}
	public Denominacion getDenominacion(){
		return denominacion;
	}	

	public void setDenominacion(Denominacion denominacion) {
		this.denominacion = denominacion;
	}
	public CategoriaOcupacional getCategoriaOcupacional(){
		return categoriaocupacional;
	}	

	public void setCategoriaOcupacional(CategoriaOcupacional categoriaocupacional) {
		this.categoriaocupacional = categoriaocupacional;
	}
}