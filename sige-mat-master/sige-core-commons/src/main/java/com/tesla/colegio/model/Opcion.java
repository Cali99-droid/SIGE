package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase entidad que representa a la tabla seg_opcion
 * @author MV
 *
 */
public class Opcion extends EntidadBase{

	public final static String TABLA = "seg_opcion";
	private Integer id;
	private Integer id_opc;
	private String nom;
	private String titulo;
	private String subtitulo;
	private String icon;
	private String url;
	private Opcion opcion;	
	private List<Opcion> opcions = new ArrayList<Opcion>();
	private List<RolOpcion> rolopcions;
	
	//aux
	private Integer niv;

	public Opcion(){
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
	* Obtiene Mdulo padre  
	* @return id_opc
	*/
	public Integer getId_opc(){
		return id_opc;
	}	

	/**
	* Mdulo padre  
	* @param id_opc
	*/
	public void setId_opc(Integer id_opc) {
		this.id_opc = id_opc;
	}

	/**
	* Obtiene Nombre de la opcin 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Nombre de la opcin 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	* Obtiene Titulo 
	* @return titulo
	*/
	public String getTitulo(){
		return titulo;
	}	

	/**
	* Titulo 
	* @param titulo
	*/
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	/**
	* Obtiene Nombre de la opcin 
	* @return icon
	*/
	public String getIcon(){
		return icon;
	}	

	/**
	* Nombre de la opcin 
	* @param icon
	*/
	public void setIcon(String icon) {
		this.icon = icon;
	}

	/**
	* Obtiene URL de la opcin 
	* @return url
	*/
	public String getUrl(){
		return url;
	}	

	/**
	* URL de la opcin 
	* @param url
	*/
	public void setUrl(String url) {
		this.url = url;
	}

	public Opcion getOpcion(){
		return opcion;
	}	

	public void setOpcion(Opcion opcion) {
		this.opcion = opcion;
	}
	/**
	* Obtiene lista de Opcin del sistema 
	*/
	public List<Opcion> getOpcions() {
		return opcions;
	}

	/**
	* Seta Lista de Opcin del sistema 
	* @param opcions
	*/	
	public void setOpcion(List<Opcion> opcions) {
		this.opcions = opcions;
	}
	/**
	* Obtiene lista de Tabla INTERMEDIA rol opcion 
	*/
	public List<RolOpcion> getRolOpcions() {
		return rolopcions;
	}

	/**
	* Seta Lista de Tabla INTERMEDIA rol opcion 
	* @param rolopcions
	*/	
	public void setRolOpcion(List<RolOpcion> rolopcions) {
		this.rolopcions = rolopcions;
	}

	
	
	public Integer getNiv() {
		return niv;
	}

	public void setNiv(Integer niv) {
		this.niv = niv;
	}

	
	public String getSubtitulo() {
		return subtitulo;
	}

	public void setSubtitulo(String subtitulo) {
		this.subtitulo = subtitulo;
	}

	@Override
	public String toString() {
		return "Opcion [id=" + id + ", id_opc=" + id_opc + ", nom=" + nom + ", titulo=" + titulo + ", icon=" + icon
				+ ", url=" + url + ", opcion=" + opcion + ", opcions=" + opcions + ", rolopcions=" + rolopcions + "]";
	}
	
	
}