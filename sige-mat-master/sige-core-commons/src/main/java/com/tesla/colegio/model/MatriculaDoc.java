package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla mat_matricula_doc
 * @author MV
 *
 */
public class MatriculaDoc extends EntidadBase{

	public final static String TABLA = "mat_matricula_doc";
	private Integer id;
	private Integer id_ado;
	private Integer id_alu;
	private AluDocumentos aluDocumentos;	
	private Alumno alumno;	

	public MatriculaDoc(){
	}

	/**
	* Obtiene Llave Principal 
	* @return id
	*/
	public Integer getId(){
		return id;
	}	

	/**
	* Llave Principal 
	* @param id
	*/
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	* Obtiene Documento 
	* @return id_ado
	*/
	public Integer getId_ado(){
		return id_ado;
	}	

	/**
	* Documento 
	* @param id_ado
	*/
	public void setId_ado(Integer id_ado) {
		this.id_ado = id_ado;
	}

	/**
	* Obtiene Alumno 
	* @return id_alu
	*/
	public Integer getId_alu(){
		return id_alu;
	}	

	/**
	* Alumno 
	* @param id_alu
	*/
	public void setId_alu(Integer id_alu) {
		this.id_alu = id_alu;
	}

	public AluDocumentos getAluDocumentos(){
		return aluDocumentos;
	}	

	public void setAluDocumentos(AluDocumentos aluDocumentos) {
		this.aluDocumentos = aluDocumentos;
	}
	public Alumno getAlumno(){
		return alumno;
	}	

	public void setAlumno(Alumno alumno) {
		this.alumno = alumno;
	}
}