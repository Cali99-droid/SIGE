package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla not_nota_indicador
 * @author MV
 *
 */
public class NotaIndicador extends EntidadBase{

	public final static String TABLA = "not_nota_indicador";
	private Integer id;
	private Integer id_not;
	private Integer id_nie;
	private Integer nota;
	private Nota notaObj;	
	private IndEva indeva;	

	public NotaIndicador(){
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
	* Obtiene Nota Evaluacion 
	* @return id_not
	*/
	public Integer getId_not(){
		return id_not;
	}	

	/**
	* Nota Evaluacion 
	* @param id_not
	*/
	public void setId_not(Integer id_not) {
		this.id_not = id_not;
	}

	/**
	* Obtiene Indicador Evaluacion 
	* @return id_nie
	*/
	public Integer getId_nie(){
		return id_nie;
	}	

	/**
	* Indicador Evaluacion 
	* @param id_nie
	*/
	public void setId_nie(Integer id_nie) {
		this.id_nie = id_nie;
	}

	/**
	* Obtiene Nota 
	* @return nota
	*/
	public Integer getNota(){
		return nota;
	}	

	/**
	* Nota 
	* @param nota
	*/
	public void setNota(Integer nota) {
		this.nota = nota;
	}

	public Nota getNotaObj(){
		return notaObj;
	}	

	public void setNotaObj(Nota notaObj) {
		this.notaObj = notaObj;
	}
	public IndEva getIndEva(){
		return indeva;
	}	

	public void setIndEva(IndEva indeva) {
		this.indeva = indeva;
	}
}