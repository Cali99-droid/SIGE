package com.tesla.colegio.model.bean;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla cat_grad
 * @author MV
 *
 */
public class GradoCapacidad extends EntidadBase{

	public final static String TABLA = "cat_grad";
	private Integer id;
	private Integer id_grad;	
	private String nom;	
	private String nro_vac;	
	private String vac_ofe;	
	private String post;	
	private String capacidad;
	private String matriculados;

	public GradoCapacidad(){
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	/**
	* Obtiene Llave principal 
	* @return id
	*/
	public Integer getIdgrad(){
		return id_grad;
	}	

	/**
	* Llave principal 
	* @param id
	*/
	public void setId_grad(Integer id_grad) {
		this.id_grad = id_grad;
	}

	/**
	* Obtiene Nivel educativo 
	* @return id_nvl
	*/
	public String getCapacidad(){
		return capacidad;
	}	

	/**
	* Nivel educativo 
	* @param id_nvl
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	
	/**
	* Obtiene lista de Examen Vacante 
	*/
	public String getNom(){
		return nom;
	}	
	
	/**
	* Nivel educativo 
	* @param id_nvl
	*/
	public void setNro_vac(String nro_vac) {
		this.nro_vac = nro_vac;
	}

	
	/**
	* Obtiene lista de Examen Vacante 
	*/
	public String getNro_vac(){
		return nro_vac;
	}
	
	/**
	* Nivel educativo 
	* @param id_nvl
	*/
	public void setVac_ofe(String vac_ofe) {
		this.vac_ofe = vac_ofe;
	}

	
	/**
	* Obtiene lista de Examen Vacante 
	*/
	public String getVac_ofe(){
		return vac_ofe;
	}
	
	public void setPost(String post) {
		this.post = post;
	}

	
	/**
	* Obtiene lista de Examen Vacante 
	*/
	public String getPost(){
		return post;
	}
	public void setCapacidad(String capacidad) {
		this.capacidad = capacidad;
	}

	
	/**
	* Obtiene lista de Examen Vacante 
	*/
	public String getMatriculados(){
		return matriculados;
	}	

	/**
	* Nivel educativo 
	* @param id_nvl
	*/
	public void setMatriculados(String matriculados) {
		this.matriculados = matriculados;
	}
	
	
}