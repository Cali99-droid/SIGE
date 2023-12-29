package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla col_nivel_coordinador
 * @author MV
 *
 */
public class NivelCoordinador extends EntidadBase{

	public final static String TABLA = "col_nivel_coordinador";
	private Integer id;
	private Integer id_niv;
	private Integer id_anio;
	private Integer id_gir;
	private Integer id_tra;
	private Nivel nivel;	
	private Anio anio;	
	private GiroNegocio gironegocio;	
	private Trabajador trabajador;	

	public NivelCoordinador(){
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
	* Obtiene Giro de Negocio 
	* @return id_gir
	*/
	public Integer getId_gir(){
		return id_gir;
	}	

	/**
	* Giro de Negocio 
	* @param id_gir
	*/
	public void setId_gir(Integer id_gir) {
		this.id_gir = id_gir;
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
	public Anio getAnio(){
		return anio;
	}	

	public void setAnio(Anio anio) {
		this.anio = anio;
	}
	public GiroNegocio getGiroNegocio(){
		return gironegocio;
	}	

	public void setGiroNegocio(GiroNegocio gironegocio) {
		this.gironegocio = gironegocio;
	}
	public Trabajador getTrabajador(){
		return trabajador;
	}	

	public void setTrabajador(Trabajador trabajador) {
		this.trabajador = trabajador;
	}
}