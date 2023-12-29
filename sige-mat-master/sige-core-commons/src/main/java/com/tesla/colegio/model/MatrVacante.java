package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla eva_matr_vacante
 * @author MV
 *
 */
public class MatrVacante extends EntidadBase{

	public final static String TABLA = "eva_matr_vacante";
	private Integer id;
	private Integer id_alu;
	private Integer id_eva;
	private Integer id_gra;
	private Integer id_col;
	private Integer id_cli;
	private String num_rec;
	private String num_cont;
	private String res;
	private Alumno alumno;	
	private EvaluacionVac evaluacionvac;	
	private Grad grad;	
	private Colegio colegio;	
	private Familiar familiar;	
	private List<CriterioNota> criterionotas;
	private List<MatrVacanteResultado> matrvacanteresultados;
	private List<MarcacionNota> marcacionnotas;

	public MatrVacante(){
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
	* Obtiene Evaluacin Vacante 
	* @return id_eva
	*/
	public Integer getId_eva(){
		return id_eva;
	}	

	/**
	* Evaluacin Vacante 
	* @param id_eva
	*/
	public void setId_eva(Integer id_eva) {
		this.id_eva = id_eva;
	}

	/**
	* Obtiene Grado al que postula 
	* @return id_gra
	*/
	public Integer getId_gra(){
		return id_gra;
	}	

	/**
	* Grado al que postula 
	* @param id_gra
	*/
	public void setId_gra(Integer id_gra) {
		this.id_gra = id_gra;
	}

	/**
	* Obtiene Colegio de Procedencia 
	* @return id_col
	*/
	public Integer getId_col(){
		return id_col;
	}	

	/**
	* Colegio de Procedencia 
	* @param id_col
	*/
	public void setId_col(Integer id_col) {
		this.id_col = id_col;
	}

	/**
	* Obtiene Familiar que hace la insripcion 
	* @return id_cli
	*/
	public Integer getId_cli(){
		return id_cli;
	}	

	/**
	* Familiar que hace la insripcion 
	* @param id_cli
	*/
	public void setId_cli(Integer id_cli) {
		this.id_cli = id_cli;
	}

	/**
	* Obtiene Nmero de Recibo 
	* @return num_rec
	*/
	public String getNum_rec(){
		return num_rec;
	}	

	/**
	* Nmero de Recibo 
	* @param num_rec
	*/
	public void setNum_rec(String num_rec) {
		this.num_rec = num_rec;
	}

	/**
	* Obtiene Nmero de Contrato 
	* @return num_cont
	*/
	public String getNum_cont(){
		return num_cont;
	}	

	/**
	* Nmero de Contrato 
	* @param num_cont
	*/
	public void setNum_cont(String num_cont) {
		this.num_cont = num_cont;
	}

	/**
	* Obtiene Resultado Final 
	* @return res
	*/
	public String getRes(){
		return res;
	}	

	/**
	* Resultado Final 
	* @param res
	*/
	public void setRes(String res) {
		this.res = res;
	}

	public Alumno getAlumno(){
		return alumno;
	}	

	public void setAlumno(Alumno alumno) {
		this.alumno = alumno;
	}
	public EvaluacionVac getEvaluacionVac(){
		return evaluacionvac;
	}	

	public void setEvaluacionVac(EvaluacionVac evaluacionvac) {
		this.evaluacionvac = evaluacionvac;
	}
	public Grad getGrad(){
		return grad;
	}	

	public void setGrad(Grad grad) {
		this.grad = grad;
	}
	public Colegio getColegio(){
		return colegio;
	}	

	public void setColegio(Colegio colegio) {
		this.colegio = colegio;
	}
	public Familiar getFamiliar(){
		return familiar;
	}	

	public void setFamiliar(Familiar familiar) {
		this.familiar = familiar;
	}
	/**
	* Obtiene lista de Evaluacin a criterio 
	*/
	public List<CriterioNota> getCriterioNotas() {
		return criterionotas;
	}

	/**
	* Seta Lista de Evaluacin a criterio 
	* @param criterionotas
	*/	
	public void setCriterioNota(List<CriterioNota> criterionotas) {
		this.criterionotas = criterionotas;
	}
	/**
	* Obtiene lista de Resultado Evaluacion Vacante 
	*/
	public List<MatrVacanteResultado> getMatrVacanteResultados() {
		return matrvacanteresultados;
	}

	/**
	* Seta Lista de Resultado Evaluacion Vacante 
	* @param matrvacanteresultados
	*/	
	public void setMatrVacanteResultado(List<MatrVacanteResultado> matrvacanteresultados) {
		this.matrvacanteresultados = matrvacanteresultados;
	}
	/**
	* Obtiene lista de Nota del examen por marcacion 
	*/
	public List<MarcacionNota> getMarcacionNotas() {
		return marcacionnotas;
	}

	/**
	* Seta Lista de Nota del examen por marcacion 
	* @param marcacionnotas
	*/	
	public void setMarcacionNota(List<MarcacionNota> marcacionnotas) {
		this.marcacionnotas = marcacionnotas;
	}

}