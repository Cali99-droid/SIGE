package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla col_uni_sub
 * @author MV
 *
 */
public class UniSub extends EntidadBase{

	public final static String TABLA = "col_uni_sub";
	private Integer id;
	private Integer id_uni;
	private Integer id_cgsp;
	private CursoUnidad cursounidad;	
	private Tema tema;
	private Subtema subtema;
	//private GrupSubPadre grupsubpadre;	

	public UniSub(){
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
	* Obtiene Unidad Acadmica 
	* @return id_uni
	*/
	public Integer getId_uni(){
		return id_uni;
	}	

	/**
	* Unidad Acadmica 
	* @param id_uni
	*/
	public void setId_uni(Integer id_uni) {
		this.id_uni = id_uni;
	}

	/**
	* Obtiene Grupo Subtema Padre 
	* @return id_cgsp
	*/
	public Integer getId_cgsp(){
		return id_cgsp;
	}	

	/**
	* Grupo Subtema Padre 
	* @param id_cgsp
	*/
	public void setId_cgsp(Integer id_cgsp) {
		this.id_cgsp = id_cgsp;
	}

	public CursoUnidad getCursoUnidad(){
		return cursounidad;
	}	

	public void setCursoUnidad(CursoUnidad cursounidad) {
		this.cursounidad = cursounidad;
	}
	/*public GrupSubPadre getGrupSubPadre(){
		return grupsubpadre;
	}	

	public void setGrupSubPadre(GrupSubPadre grupsubpadre) {
		this.grupsubpadre = grupsubpadre;
	}*/

	public Tema getTema() {
		return tema;
	}

	public void setTema(Tema tema) {
		this.tema = tema;
	}

	public Subtema getSubtema() {
		return subtema;
	}

	public void setSubtema(Subtema subtema) {
		this.subtema = subtema;
	}
}