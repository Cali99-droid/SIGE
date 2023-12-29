package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla col_unidad_sesion
 * @author MV
 *
 */
public class UnidadSesion extends EntidadBase{

	public final static String TABLA = "col_unidad_sesion";
	private Integer id;
	private Integer id_uni;
	private Integer nro;
	private String nom;
	private CursoUnidad cursounidad;	
	private SesionTipo sesionTipo;
	private List<SesionTipo> sesiontipos;
	private List<SesionIndicador> sesionindicadors;
	private List<SesionActividad> sesionactividads;

	public UnidadSesion(){
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
	* Obtiene Unidad 
	* @return id_uni
	*/
	public Integer getId_uni(){
		return id_uni;
	}	

	/**
	* Unidad 
	* @param id_uni
	*/
	public void setId_uni(Integer id_uni) {
		this.id_uni = id_uni;
	}

	/**
	* Obtiene Nmero de Sesin 
	* @return nro
	*/
	public Integer getNro(){
		return nro;
	}	

	/**
	* Nmero de Sesin 
	* @param nro
	*/
	public void setNro(Integer nro) {
		this.nro = nro;
	}

	/**
	* Obtiene Titulo de la Sesin 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Titulo de la Sesin 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	public CursoUnidad getCursoUnidad(){
		return cursounidad;
	}	

	public void setCursoUnidad(CursoUnidad cursounidad) {
		this.cursounidad = cursounidad;
	}
	/**
	* Obtiene lista de Sesiones por unidad x tipo 
	*/
	public List<SesionTipo> getSesionTipos() {
		return sesiontipos;
	}

	/**
	* Seta Lista de Sesiones por unidad x tipo 
	* @param sesiontipos
	*/	
	public void setSesionTipo(List<SesionTipo> sesiontipos) {
		this.sesiontipos = sesiontipos;
	}
	/**
	* Obtiene lista de Indicadores que se trabajara en la sesion 
	*/
	public List<SesionIndicador> getSesionIndicadors() {
		return sesionindicadors;
	}

	/**
	* Seta Lista de Indicadores que se trabajara en la sesion 
	* @param sesionindicadors
	*/	
	public void setSesionIndicador(List<SesionIndicador> sesionindicadors) {
		this.sesionindicadors = sesionindicadors;
	}
	/**
	* Obtiene lista de Actividades por Sesion 
	*/
	public List<SesionActividad> getSesionActividads() {
		return sesionactividads;
	}

	/**
	* Seta Lista de Actividades por Sesion 
	* @param sesionactividads
	*/	
	public void setSesionActividad(List<SesionActividad> sesionactividads) {
		this.sesionactividads = sesionactividads;
	}

	@Override
	public String toString() {
		return "UnidadSesion [id=" + id + ", id_uni=" + id_uni + ", nro=" + nro + ", nom=" + nom + ", cursounidad="
				+ cursounidad + ", sesiontipos=" + sesiontipos + ", sesionindicadors=" + sesionindicadors
				+ ", sesionactividads=" + sesionactividads + "]";
	}

	public SesionTipo getSesionTipo() {
		return sesionTipo;
	}

	public void setSesionTipo(SesionTipo sesionTipo) {
		this.sesionTipo = sesionTipo;
	}
	
	
}