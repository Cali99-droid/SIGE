package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla cat_alu_documentos
 * @author MV
 *
 */
public class AluDocumentos extends EntidadBase{

	public final static String TABLA = "cat_alu_documentos";
	private Integer id;
	private String nom;
	private List<MatriculaDoc> matriculaDocs;

	public AluDocumentos(){
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
	* Obtiene Documento 
	* @return nom
	*/
	public String getNom(){
		return nom;
	}	

	/**
	* Documento 
	* @param nom
	*/
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	* Obtiene lista de Matricula del alumno 
	*/
	public List<MatriculaDoc> getMatriculaDocs() {
		return matriculaDocs;
	}

	/**
	* Seta Lista de Matricula del alumno 
	* @param matriculaDocs
	*/	
	public void setMatriculaDoc(List<MatriculaDoc> matriculaDocs) {
		this.matriculaDocs = matriculaDocs;
	}
}