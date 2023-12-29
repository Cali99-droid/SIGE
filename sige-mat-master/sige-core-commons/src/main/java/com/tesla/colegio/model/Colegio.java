package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla col_colegio
 * @author MV
 *
 */
public class Colegio extends EntidadBase{

	public final static String TABLA = "col_colegio";
	private Integer id;
	private Integer id_dist;
	private String cod_mod;
	private String nom_niv;
	private String nom;
	private String estatal;
	private String dir;
	private String tel;
	private Distrito distrito;	
	private Provincia provincia;
	private Departamento departamento;
	private List<MatrVacante> matrvacantes;

	public Colegio(){
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
	* Obtiene Distrito de donde proviene el colegio 
	* @return id_dist
	*/
	public Integer getId_dist(){
		return id_dist;
	}	

	/**
	* Distrito de donde proviene el colegio 
	* @param id_dist
	*/
	public void setId_dist(Integer id_dist) {
		this.id_dist = id_dist;
	}

	/**
	* Obtiene Cdigo modular 
	* @return cod_mod
	*/
	public String getCod_mod(){
		return cod_mod;
	}	

	/**
	* Cdigo modular 
	* @param cod_mod
	*/
	public void setCod_mod(String cod_mod) {
		this.cod_mod = cod_mod;
	}

	/**
	* Obtiene Nivel educativo 
	* @return nom_niv
	*/
	public String getNom_niv(){
		return nom_niv;
	}	

	/**
	* Nivel educativo 
	* @param nom_niv
	*/
	public void setNom_niv(String nom_niv) {
		this.nom_niv = nom_niv;
	}

	/**
	* Obtiene Nombre del colegio 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Nombre del colegio 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	* Obtiene Si es estatal o no 
	* @return estatal
	*/
	public String getEstatal(){
		return estatal;
	}	

	/**
	* Si es estatal o no 
	* @param estatal
	*/
	public void setEstatal(String estatal) {
		this.estatal = estatal;
	}

	/**
	* Obtiene Direccin del colegio 
	* @return dir
	*/
	public String getDir(){
		return dir;
	}	

	/**
	* Direccin del colegio 
	* @param dir
	*/
	public void setDir(String dir) {
		this.dir = dir;
	}

	/**
	* Obtiene Telfono del colegio 
	* @return tel
	*/
	public String getTel(){
		return tel;
	}	

	/**
	* Telfono del colegio 
	* @param tel
	*/
	public void setTel(String tel) {
		this.tel = tel;
	}

	public Distrito getDistrito(){
		return distrito;
	}	

	public void setDistrito(Distrito distrito) {
		this.distrito = distrito;
	}
	/**
	* Obtiene lista de Examen Vacante 
	*/
	public List<MatrVacante> getMatrVacantes() {
		return matrvacantes;
	}

	/**
	* Seta Lista de Examen Vacante 
	* @param matrvacantes
	*/	
	public void setMatrVacante(List<MatrVacante> matrvacantes) {
		this.matrvacantes = matrvacantes;
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
}