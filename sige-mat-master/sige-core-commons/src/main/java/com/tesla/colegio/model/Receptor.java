package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla msj_receptor
 * @author MV
 *
 */
public class Receptor extends EntidadBase{

	public final static String TABLA = "msj_receptor";
	private Integer id;
	private Integer id_usr;
	private Integer id_per;
	private Integer id_msj;
	private Integer id_est;
	private Perfil perfil;	
	private Mensaje mensaje;	
	private EstadoMensajeria estadomensajeria;	
	private List<Historial> historials;

	public Receptor(){
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
	* Obtiene Uusario(Trabajador, Familiar) 
	* @return id_usr
	*/
	public Integer getId_usr(){
		return id_usr;
	}	

	/**
	* Uusario(Trabajador, Familiar) 
	* @param id_usr
	*/
	public void setId_usr(Integer id_usr) {
		this.id_usr = id_usr;
	}

	/**
	* Obtiene Perfil 
	* @return id_per
	*/
	public Integer getId_per(){
		return id_per;
	}	

	/**
	* Perfil 
	* @param id_per
	*/
	public void setId_per(Integer id_per) {
		this.id_per = id_per;
	}

	/**
	* Obtiene Mensaje 
	* @return id_msj
	*/
	public Integer getId_msj(){
		return id_msj;
	}	

	/**
	* Mensaje 
	* @param id_msj
	*/
	public void setId_msj(Integer id_msj) {
		this.id_msj = id_msj;
	}

	/**
	* Obtiene Estado 
	* @return id_est
	*/
	public Integer getId_est(){
		return id_est;
	}	

	/**
	* Estado 
	* @param id_est
	*/
	public void setId_est(Integer id_est) {
		this.id_est = id_est;
	}

	public Perfil getPerfil(){
		return perfil;
	}	

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}
	public Mensaje getMensaje(){
		return mensaje;
	}	

	public void setMensaje(Mensaje mensaje) {
		this.mensaje = mensaje;
	}
	public EstadoMensajeria getEstadoMensajeria(){
		return estadomensajeria;
	}	

	public void setEstadoMensajeria(EstadoMensajeria estadomensajeria) {
		this.estadomensajeria = estadomensajeria;
	}
	/**
	* Obtiene lista de Historial 
	*/
	public List<Historial> getHistorials() {
		return historials;
	}

	/**
	* Seta Lista de Historial 
	* @param historials
	*/	
	public void setHistorial(List<Historial> historials) {
		this.historials = historials;
	}
}