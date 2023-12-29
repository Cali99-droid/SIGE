package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla col_area_coordinador
 * @author MV
 *
 */
public class AreaCoordinador extends EntidadBase{

	public final static String TABLA = "col_area_coordinador";
	private Integer id;
	private Integer id_anio;
	private Integer id_niv;
	private Integer id_area;
	private Integer id_tra;
	private Integer id_cur;
	private Integer id_gir;
	private Nivel nivel;	
	private Area area;	
	private Trabajador trabajador;	

	public AreaCoordinador(){
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
	* Obtiene Area 
	* @return id_area
	*/
	public Integer getId_area(){
		return id_area;
	}	

	/**
	* Area 
	* @param id_area
	*/
	public void setId_area(Integer id_area) {
		this.id_area = id_area;
	}

	/**
	* Obtiene Trabajador 
	* @return id_tra
	*/
	public Integer getId_tra(){
		return id_tra;
	}	

	/**
	* Trabajador 
	* @param id_tra
	*/
	public void setId_tra(Integer id_tra) {
		this.id_tra = id_tra;
	}

	public Nivel getNivel(){
		return nivel;
	}	

	public void setNivel(Nivel nivel) {
		this.nivel = nivel;
	}
	public Area getArea(){
		return area;
	}	

	public void setArea(Area area) {
		this.area = area;
	}
	public Trabajador getTrabajador(){
		return trabajador;
	}	

	public void setTrabajador(Trabajador trabajador) {
		this.trabajador = trabajador;
	}

	public Integer getId_cur() {
		return id_cur;
	}

	public void setId_cur(Integer id_cur) {
		this.id_cur = id_cur;
	}

	
	
	public Integer getId_anio() {
		return id_anio;
	}

	public void setId_anio(Integer id_anio) {
		this.id_anio = id_anio;
	}

	@Override
	public String toString() {
		return "AreaCoordinador [id=" + id + ", id_niv=" + id_niv + ", id_area=" + id_area + ", id_tra=" + id_tra
				+ ", id_cur=" + id_cur + ", nivel=" + nivel + ", area=" + area + ", trabajador=" + trabajador + "]";
	}

	public Integer getId_gir() {
		return id_gir;
	}

	public void setId_gir(Integer id_gir) {
		this.id_gir = id_gir;
	}
	
	
}