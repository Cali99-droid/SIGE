package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla cat_cond_matricula
 * @author MV
 *
 */
public class CondMatricula extends EntidadBase{

	public final static String TABLA = "cat_cond_matricula";
	private Integer id;
	private String nom;
	private String des;
	private List<Reserva> reservas;
	private List<Matricula> matriculas;

	public CondMatricula(){
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
	* Obtiene Condicion 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Condicion 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	* Obtiene Descripcin 
	* @return des
	*/
	public String getDes(){
		return des;
	}	

	/**
	* Descripcin 
	* @param des
	*/
	public void setDes(String des) {
		this.des = des;
	}

	/**
	* Obtiene lista de Reserva de matrcula 
	*/
	public List<Reserva> getReservas() {
		return reservas;
	}

	/**
	* Seta Lista de Reserva de matrcula 
	* @param reservas
	*/	
	public void setReserva(List<Reserva> reservas) {
		this.reservas = reservas;
	}
	/**
	* Obtiene lista de Matricula del alumno 
	*/
	public List<Matricula> getMatriculas() {
		return matriculas;
	}

	/**
	* Seta Lista de Matricula del alumno 
	* @param matriculas
	*/	
	public void setMatricula(List<Matricula> matriculas) {
		this.matriculas = matriculas;
	}
}