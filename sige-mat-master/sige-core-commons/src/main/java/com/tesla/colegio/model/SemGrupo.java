package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.List;

/**
 * Clase entidad que representa a la tabla col_sem_grupo
 * @author MV
 *
 */
public class SemGrupo extends EntidadBase{

	public final static String TABLA = "col_sem_grupo";
	private Integer id;
	private Integer id_sem;
	private String nom;
	private Integer nro;
	private Integer cap;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date fec;
	private String hor_ing;
	private Seminario seminario;	
	private List<SemInscripcion> seminscripcions;

	public SemGrupo(){
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
	* Obtiene Seminario 
	* @return id_sem
	*/
	public Integer getId_sem(){
		return id_sem;
	}	

	/**
	* Seminario 
	* @param id_sem
	*/
	public void setId_sem(Integer id_sem) {
		this.id_sem = id_sem;
	}

	/**
	* Obtiene Nombre 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Nombre 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	* Obtiene Nmero 
	* @return nro
	*/
	public Integer getNro(){
		return nro;
	}	

	/**
	* Nmero 
	* @param nro
	*/
	public void setNro(Integer nro) {
		this.nro = nro;
	}

	/**
	* Obtiene Capacidad 
	* @return cap
	*/
	public Integer getCap(){
		return cap;
	}	

	/**
	* Capacidad 
	* @param cap
	*/
	public void setCap(Integer cap) {
		this.cap = cap;
	}

	/**
	* Obtiene Fecha del Seminario para el grupo 
	* @return fec
	*/
	public java.util.Date getFec(){
		return fec;
	}	

	/**
	* Fecha del Seminario para el grupo 
	* @param fec
	*/
	public void setFec(java.util.Date fec) {
		this.fec = fec;
	}

	/**
	* Obtiene Hora Ingreso 
	* @return hor_ing
	*/
	public String getHor_ing(){
		return hor_ing;
	}	

	/**
	* Hora Ingreso 
	* @param hor_ing
	*/
	public void setHor_ing(String hor_ing) {
		this.hor_ing = hor_ing;
	}

	public Seminario getSeminario(){
		return seminario;
	}	

	public void setSeminario(Seminario seminario) {
		this.seminario = seminario;
	}
	/**
	* Obtiene lista de Inscripcin 
	*/
	public List<SemInscripcion> getSemInscripcions() {
		return seminscripcions;
	}

	/**
	* Seta Lista de Inscripcin 
	* @param seminscripcions
	*/	
	public void setSemInscripcion(List<SemInscripcion> seminscripcions) {
		this.seminscripcions = seminscripcions;
	}
}