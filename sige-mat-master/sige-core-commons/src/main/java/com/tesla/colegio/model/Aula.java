package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla col_aula
 * @author MV
 *
 */
public class Aula extends EntidadBase{

	public final static String TABLA = "col_aula";
	private Integer id;
	private Integer id_per;
	private Integer id_cic;
	private Integer id_grad;
	private Integer id_secc_ant;
	private Integer id_tur;
	private Integer id_cme;
	private String secc;
	private Integer cap;
	private String des_classroom;
	private String id_classroom;
	private Periodo periodo;	
	private Grad grad;	
	private Turno turno;	
	private Ciclo ciclo;
	private ModalidadEstudio modalidadEstudio;
	private List<Curso> cursos;
	private List<Reserva> reservas;
	private List<Matricula> matriculas;

	public Aula(){
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
	* Obtiene Periodo Acadmico 
	* @return id_per
	*/
	public Integer getId_per(){
		return id_per;
	}	

	/**
	* Periodo Acadmico 
	* @param id_per
	*/
	public void setId_per(Integer id_per) {
		this.id_per = id_per;
	}

	/**
	* Obtiene Grado educativo al que pertenece 
	* @return id_grad
	*/
	public Integer getId_grad(){
		return id_grad;
	}	

	/**
	* Grado educativo al que pertenece 
	* @param id_grad
	*/
	public void setId_grad(Integer id_grad) {
		this.id_grad = id_grad;
	}

	/**
	* Obtiene Seccin anterior que debio estudiar 
	* @return id_secc_ant
	*/
	public Integer getId_secc_ant(){
		return id_secc_ant;
	}	

	/**
	* Seccin anterior que debio estudiar 
	* @param id_secc_ant
	*/
	public void setId_secc_ant(Integer id_secc_ant) {
		this.id_secc_ant = id_secc_ant;
	}

	/**
	* Obtiene Turno 
	* @return id_tur
	*/
	public Integer getId_tur(){
		return id_tur;
	}	

	/**
	* Turno 
	* @param id_tur
	*/
	public void setId_tur(Integer id_tur) {
		this.id_tur = id_tur;
	}

	/**
	* Obtiene Seccin 
	* @return secc
	*/
	public String getSecc(){
		return secc;
	}	

	/**
	* Seccin 
	* @param secc
	*/
	public void setSecc(String secc) {
		this.secc = secc;
	}

	/**
	* Obtiene Capacidad mxima de alumnos 
	* @return cap
	*/
	public Integer getCap(){
		return cap;
	}	

	/**
	* Capacidad mxima de alumnos 
	* @param cap
	*/
	public void setCap(Integer cap) {
		this.cap = cap;
	}

	public Periodo getPeriodo(){
		return periodo;
	}	

	public void setPeriodo(Periodo periodo) {
		this.periodo = periodo;
	}
	public Grad getGrad(){
		return grad;
	}	

	public void setGrad(Grad grad) {
		this.grad = grad;
	}
	public Turno getTurno(){
		return turno;
	}	

	public void setTurno(Turno turno) {
		this.turno = turno;
	}
	/**
	* Obtiene lista de Curso 
	*/
	public List<Curso> getCursos() {
		return cursos;
	}

	/**
	* Seta Lista de Curso 
	* @param cursos
	*/	
	public void setCurso(List<Curso> cursos) {
		this.cursos = cursos;
	}
	/**
	* Obtiene lista de Reserva de matrcula 
	*/
	public List<Reserva> getReservas() {
		return reservas;
	}

	/**
	* Seta Lista de Reserva de matrcula 
	* @param reservas
	*/	
	public void setReserva(List<Reserva> reservas) {
		this.reservas = reservas;
	}
	/**
	* Obtiene lista de Matricula del alumno 
	*/
	public List<Matricula> getMatriculas() {
		return matriculas;
	}

	/**
	* Seta Lista de Matricula del alumno 
	* @param matriculas
	*/	
	public void setMatricula(List<Matricula> matriculas) {
		this.matriculas = matriculas;
	}

	public Integer getId_cic() {
		return id_cic;
	}

	public void setId_cic(Integer id_cic) {
		this.id_cic = id_cic;
	}

	public Ciclo getCiclo() {
		return ciclo;
	}

	public void setCiclo(Ciclo ciclo) {
		this.ciclo = ciclo;
	}

	public String getDes_classroom() {
		return des_classroom;
	}

	public void setDes_classroom(String des_classroom) {
		this.des_classroom = des_classroom;
	}

	public String getId_classroom() {
		return id_classroom;
	}

	public void setId_classroom(String id_classroom) {
		this.id_classroom = id_classroom;
	}

	public Integer getId_cme() {
		return id_cme;
	}

	public void setId_cme(Integer id_cme) {
		this.id_cme = id_cme;
	}

	public ModalidadEstudio getModalidadEstudio() {
		return modalidadEstudio;
	}

	public void setModalidadEstudio(ModalidadEstudio modalidadEstudio) {
		this.modalidadEstudio = modalidadEstudio;
	}
}