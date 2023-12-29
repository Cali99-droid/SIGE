package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla eva_nota_criterio
 * @author MV
 *
 */
public class NotaCriterio extends EntidadBase{

	public final static String TABLA = "eva_nota_criterio";
	private Integer id;
	private Integer id_alu;
	private Integer id_exa;
	private Integer num;
	private String instr_tecn1;
	private String instr_tecn2;
	private String instr_tecn3;
	private String instr_tecn4;
	private String resultado;
	private String apto;
	private String concl_recom1;
	private String concl_recom2;
	private String concl_recom3;
	private String concl_recom4;
	private String concl_recom5;
	private String concl_recom6;
	private Alumno alumno;	
	private Examen examen;	

	public NotaCriterio(){
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

	/**
	* Obtiene Examen Vacante 
	* @return id_exa
	*/
	public Integer getId_exa(){
		return id_exa;
	}	

	/**
	* Examen Vacante 
	* @param id_exa
	*/
	public void setId_exa(Integer id_exa) {
		this.id_exa = id_exa;
	}

	/**
	* Obtiene Nmero de la evluacin 
	* @return num
	*/
	public Integer getNum(){
		return num;
	}	

	/**
	* Nmero de la evluacin 
	* @param num
	*/
	public void setNum(Integer num) {
		this.num = num;
	}

	/**
	* Obtiene Instrumento y Tecnica 1 utilizada 
	* @return instr_tecn1
	*/
	public String getInstr_tecn1(){
		return instr_tecn1;
	}	

	/**
	* Instrumento y Tecnica 1 utilizada 
	* @param instr_tecn1
	*/
	public void setInstr_tecn1(String instr_tecn1) {
		this.instr_tecn1 = instr_tecn1;
	}

	/**
	* Obtiene Instrumento y Tecnica 2 utilizada 
	* @return instr_tecn2
	*/
	public String getInstr_tecn2(){
		return instr_tecn2;
	}	

	/**
	* Instrumento y Tecnica 2 utilizada 
	* @param instr_tecn2
	*/
	public void setInstr_tecn2(String instr_tecn2) {
		this.instr_tecn2 = instr_tecn2;
	}

	/**
	* Obtiene Instrumento y Tecnica 3 utilizada 
	* @return instr_tecn3
	*/
	public String getInstr_tecn3(){
		return instr_tecn3;
	}	

	/**
	* Instrumento y Tecnica 3 utilizada 
	* @param instr_tecn3
	*/
	public void setInstr_tecn3(String instr_tecn3) {
		this.instr_tecn3 = instr_tecn3;
	}

	/**
	* Obtiene Instrumento y Tecnica 4 utilizada 
	* @return instr_tecn4
	*/
	public String getInstr_tecn4(){
		return instr_tecn4;
	}	

	/**
	* Instrumento y Tecnica 4 utilizada 
	* @param instr_tecn4
	*/
	public void setInstr_tecn4(String instr_tecn4) {
		this.instr_tecn4 = instr_tecn4;
	}

	/**
	* Obtiene Resultado 
	* @return resultado
	*/
	public String getResultado(){
		return resultado;
	}	

	/**
	* Resultado 
	* @param resultado
	*/
	public void setResultado(String resultado) {
		this.resultado = resultado;
	}

	/**
	* Obtiene Apto 
	* @return apto
	*/
	public String getApto(){
		return apto;
	}	

	/**
	* Apto 
	* @param apto
	*/
	public void setApto(String apto) {
		this.apto = apto;
	}

	/**
	* Obtiene Conclusin y Recomendacin 1 
	* @return concl_recom1
	*/
	public String getConcl_recom1(){
		return concl_recom1;
	}	

	/**
	* Conclusin y Recomendacin 1 
	* @param concl_recom1
	*/
	public void setConcl_recom1(String concl_recom1) {
		this.concl_recom1 = concl_recom1;
	}

	/**
	* Obtiene Conclusin y Recomendacin 2 
	* @return concl_recom2
	*/
	public String getConcl_recom2(){
		return concl_recom2;
	}	

	/**
	* Conclusin y Recomendacin 2 
	* @param concl_recom2
	*/
	public void setConcl_recom2(String concl_recom2) {
		this.concl_recom2 = concl_recom2;
	}

	/**
	* Obtiene Conclusin y Recomendacin 3 
	* @return concl_recom3
	*/
	public String getConcl_recom3(){
		return concl_recom3;
	}	

	/**
	* Conclusin y Recomendacin 3 
	* @param concl_recom3
	*/
	public void setConcl_recom3(String concl_recom3) {
		this.concl_recom3 = concl_recom3;
	}

	/**
	* Obtiene Conclusin y Recomendacin 4 
	* @return concl_recom4
	*/
	public String getConcl_recom4(){
		return concl_recom4;
	}	

	/**
	* Conclusin y Recomendacin 4 
	* @param concl_recom4
	*/
	public void setConcl_recom4(String concl_recom4) {
		this.concl_recom4 = concl_recom4;
	}

	/**
	* Obtiene Conclusin y Recomendacin 5 
	* @return concl_recom5
	*/
	public String getConcl_recom5(){
		return concl_recom5;
	}	

	/**
	* Conclusin y Recomendacin 5 
	* @param concl_recom5
	*/
	public void setConcl_recom5(String concl_recom5) {
		this.concl_recom5 = concl_recom5;
	}

	/**
	* Obtiene Conclusin y Recomendacin 6 
	* @return concl_recom6
	*/
	public String getConcl_recom6(){
		return concl_recom6;
	}	

	/**
	* Conclusin y Recomendacin 6 
	* @param concl_recom6
	*/
	public void setConcl_recom6(String concl_recom6) {
		this.concl_recom6 = concl_recom6;
	}

	public Alumno getAlumno(){
		return alumno;
	}	

	public void setAlumno(Alumno alumno) {
		this.alumno = alumno;
	}
	public Examen getExamen(){
		return examen;
	}	

	public void setExamen(Examen examen) {
		this.examen = examen;
	}
}