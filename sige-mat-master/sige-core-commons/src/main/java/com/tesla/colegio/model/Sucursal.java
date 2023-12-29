package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla ges_sucursal
 * @author MV
 *
 */
public class Sucursal extends EntidadBase{

	public final static String TABLA = "ges_sucursal";
	private Integer id;
	private Integer id_ggn;
	private Integer id_emp;
	private Integer id_dist;
	private String nom;
	private String cod;
	private String abrv;
	private String dir;
	private String tel;
	private String correo;
	private String pag_web;
	private Integer tot_au;
	private Integer tot_ofi;
	private List<Aula> aulas;
	private Provincia provincia;
	private Departamento departamento;
	private Pais pais;

	public Sucursal(){
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
	* Obtiene Nombre del local 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Nombre del local 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	* Obtiene Direccin 
	* @return dir
	*/
	public String getDir(){
		return dir;
	}	

	/**
	* Direccin 
	* @param dir
	*/
	public void setDir(String dir) {
		this.dir = dir;
	}

	/**
	* Obtiene Telfono del local 
	* @return tel
	*/
	public String getTel(){
		return tel;
	}	

	/**
	* Telfono del local 
	* @param tel
	*/
	public void setTel(String tel) {
		this.tel = tel;
	}

	/**
	* Obtiene Correo del local 
	* @return correo
	*/
	public String getCorreo(){
		return correo;
	}	

	/**
	* Correo del local 
	* @param correo
	*/
	public void setCorreo(String correo) {
		this.correo = correo;
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

	public String getCod() {
		return cod;
	}

	public void setCod(String cod) {
		this.cod = cod;
	}

	public String getAbrv() {
		return abrv;
	}

	public void setAbrv(String abrv) {
		this.abrv = abrv;
	}

	public Integer getTot_au() {
		return tot_au;
	}

	public void setTot_au(Integer tot_au) {
		this.tot_au = tot_au;
	}

	public Integer getTot_ofi() {
		return tot_ofi;
	}

	public void setTot_ofi(Integer tot_ofi) {
		this.tot_ofi = tot_ofi;
	}

	public Provincia getProvincia() {
		return provincia;
	}

	public void setProvincia(Provincia provincia) {
		this.provincia = provincia;
	}

	public Departamento getDepartamento() {
		return departamento;
	}

	public void setDepartamento(Departamento departamento) {
		this.departamento = departamento;
	}

	public Pais getPais() {
		return pais;
	}

	public void setPais(Pais pais) {
		this.pais = pais;
	}

	public Integer getId_ggn() {
		return id_ggn;
	}

	public void setId_ggn(Integer id_ggn) {
		this.id_ggn = id_ggn;
	}

	public String getPag_web() {
		return pag_web;
	}

	public void setPag_web(String pag_web) {
		this.pag_web = pag_web;
	}

	public Integer getId_dist() {
		return id_dist;
	}

	public void setId_dist(Integer id_dist) {
		this.id_dist = id_dist;
	}

	public Integer getId_emp() {
		return id_emp;
	}

	public void setId_emp(Integer id_emp) {
		this.id_emp = id_emp;
	}
	
	
}