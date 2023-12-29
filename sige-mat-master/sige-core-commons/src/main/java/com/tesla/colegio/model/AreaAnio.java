package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla col_area_anio
 * @author MV
 *
 */
public class AreaAnio extends EntidadBase{

	public final static String TABLA = "col_area_anio";
	private Integer id;
	private Integer id_anio;
	private Integer id_niv;
	private Integer id_gra;
	private Integer id_area;
	private Integer id_tca;
	private Integer id_pro_per;
	private Integer id_pro_anu;
	private Integer id_adc;
	private Integer id_gir;
	private Integer ord;
	private Anio anio;	
	private Nivel nivel;	
	private Area area;	
	private List<CursoAnio> cursoanios;

	public AreaAnio(){
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
	* Obtiene Ao academico 
	* @return id_anio
	*/
	public Integer getId_anio(){
		return id_anio;
	}	

	/**
	* Ao academico 
	* @param id_anio
	*/
	public void setId_anio(Integer id_anio) {
		this.id_anio = id_anio;
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
	* Obtiene Orden del rea 
	* @return ord
	*/
	public Integer getOrd(){
		return ord;
	}	

	/**
	* Orden del rea 
	* @param ord
	*/
	public void setOrd(Integer ord) {
		this.ord = ord;
	}

	public Anio getAnio(){
		return anio;
	}	

	public void setAnio(Anio anio) {
		this.anio = anio;
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
	/**
	* Obtiene lista de Curso 
	*/
	public List<CursoAnio> getCursoAnios() {
		return cursoanios;
	}

	/**
	* Seta Lista de Curso 
	* @param cursoanios
	*/	
	public void setCursoAnio(List<CursoAnio> cursoanios) {
		this.cursoanios = cursoanios;
	}

	@Override
	public String toString() {
		return "AreaAnio [id=" + id + ", id_anio=" + id_anio + ", id_niv=" + id_niv + ", id_area=" + id_area + ", ord="
				+ ord + ", anio=" + anio + ", nivel=" + nivel + ", area=" + area + ", cursoanios=" + cursoanios + "]";
	}

	public Integer getId_gra() {
		return id_gra;
	}

	public void setId_gra(Integer id_gra) {
		this.id_gra = id_gra;
	}

	public Integer getId_tca() {
		return id_tca;
	}

	public void setId_tca(Integer id_tca) {
		this.id_tca = id_tca;
	}

	public Integer getId_pro_per() {
		return id_pro_per;
	}

	public void setId_pro_per(Integer id_pro_per) {
		this.id_pro_per = id_pro_per;
	}

	public Integer getId_pro_anu() {
		return id_pro_anu;
	}

	public void setId_pro_anu(Integer id_pro_anu) {
		this.id_pro_anu = id_pro_anu;
	}

	public Integer getId_adc() {
		return id_adc;
	}

	public void setId_adc(Integer id_adc) {
		this.id_adc = id_adc;
	}

	public Integer getId_gir() {
		return id_gir;
	}

	public void setId_gir(Integer id_gir) {
		this.id_gir = id_gir;
	}
	
	
}