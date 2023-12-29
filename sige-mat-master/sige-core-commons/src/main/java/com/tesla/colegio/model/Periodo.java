package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * Clase entidad que representa a la tabla per_periodo
 * @author MV
 *
 */
public class Periodo extends EntidadBase{

	public final static String TABLA = "per_periodo";
	private Integer id;
	private Integer id_anio;
	private Integer id_srv;
	private Integer id_tpe;
	private Integer id_suc;
	private Integer id_niv;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date fec_ini;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date fec_fin;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date fec_cie_mat;
	private Anio anio;	
	private Servicio servicio;	
	private Sucursal sucursal;
	private Nivel nivel;
	private TipPeriodo tipperiodo;	
	private GiroNegocio giroNegocio;
	private List<EvaluacionVac> evaluacionvacs;
	private List<Aula> aulas;
	private List<Reserva> reservas;
	private List<Matricula> matriculas;
	private List<PagoProgramacion> pagoprogramacions;
	private String value;

	private String flag_sit;
	private String flag_usu_alu;
	
	public Periodo(){
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
	* Obtiene Anio 
	* @return id_anio
	*/
	public Integer getId_anio(){
		return id_anio;
	}	

	/**
	* Anio 
	* @param id_anio
	*/
	public void setId_anio(Integer id_anio) {
		this.id_anio = id_anio;
	}

	/**
	* Obtiene Servicio 
	* @return id_srv
	*/
	public Integer getId_srv(){
		return id_srv;
	}	

	/**
	* Servicio 
	* @param id_srv
	*/
	public void setId_srv(Integer id_srv) {
		this.id_srv = id_srv;
	}

	/**
	* Obtiene Tipo de periodo 
	* @return id_tpe
	*/
	public Integer getId_tpe(){
		return id_tpe;
	}	

	/**
	* Tipo de periodo 
	* @param id_tpe
	*/
	public void setId_tpe(Integer id_tpe) {
		this.id_tpe = id_tpe;
	}

	/**
	* Obtiene Fecha de inicio del periodo 
	* @return fec_ini
	*/
	public java.util.Date getFec_ini(){
		return fec_ini;
	}	

	/**
	* Fecha de inicio del periodo 
	* @param fec_ini
	*/
	public void setFec_ini(java.util.Date fec_ini) {
		this.fec_ini = fec_ini;
	}

	/**
	* Obtiene Fecha de fin del periodo 
	* @return fec_fin
	*/
	public java.util.Date getFec_fin(){
		return fec_fin;
	}	

	/**
	* Fecha de fin del periodo 
	* @param fec_fin
	*/
	public void setFec_fin(java.util.Date fec_fin) {
		this.fec_fin = fec_fin;
	}

	/**
	* Obtiene Fecha cierre de matrcula 
	* @return fec_cie_mat
	*/
	public java.util.Date getFec_cie_mat(){
		return fec_cie_mat;
	}	

	/**
	* Fecha cierre de matrcula 
	* @param fec_cie_mat
	*/
	public void setFec_cie_mat(java.util.Date fec_cie_mat) {
		this.fec_cie_mat = fec_cie_mat;
	}

	public Anio getAnio(){
		return anio;
	}	

	public void setAnio(Anio anio) {
		this.anio = anio;
	}
	public Servicio getServicio(){
		return servicio;
	}	

	public void setServicio(Servicio servicio) {
		this.servicio = servicio;
	}
	public TipPeriodo getTipPeriodo(){
		return tipperiodo;
	}	

	public void setTipPeriodo(TipPeriodo tipperiodo) {
		this.tipperiodo = tipperiodo;
	}
	/**
	* Obtiene lista de $child.description 
	*/
	public List<EvaluacionVac> getEvaluacionVacs() {
		return evaluacionvacs;
	}

	/**
	* Seta Lista de $child.description 
	* @param evaluacionvacs
	*/	
	public void setEvaluacionVac(List<EvaluacionVac> evaluacionvacs) {
		this.evaluacionvacs = evaluacionvacs;
	}
	/**
	* Obtiene lista de Aula del colegio 
	*/
	public List<Aula> getAulas() {
		return aulas;
	}

	/**
	* Seta Lista de Aula del colegio 
	* @param aulas
	*/	
	public void setAula(List<Aula> aulas) {
		this.aulas = aulas;
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
	/**
	* Obtiene lista de Programacin de Pagos por Matricula del alumno 
	*/
	public List<PagoProgramacion> getPagoProgramacions() {
		return pagoprogramacions;
	}

	/**
	* Seta Lista de Programacin de Pagos por Matricula del alumno 
	* @param pagoprogramacions
	*/	
	public void setPagoProgramacion(List<PagoProgramacion> pagoprogramacions) {
		this.pagoprogramacions = pagoprogramacions;
	}

	public Integer getId_suc() {
		return id_suc;
	}

	public void setId_suc(Integer id_suc) {
		this.id_suc = id_suc;
	}

	public Integer getId_niv() {
		return id_niv;
	}

	public void setId_niv(Integer id_niv) {
		this.id_niv = id_niv;
	}

	public Sucursal getSucursal() {
		return sucursal;
	}

	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
	}

	public String getFlag_sit() {
		return flag_sit;
	}

	public void setFlag_sit(String flag_sit) {
		this.flag_sit = flag_sit;
	}

	public String getFlag_usu_alu() {
		return flag_usu_alu;
	}

	public void setFlag_usu_alu(String flag_usu_alu) {
		this.flag_usu_alu = flag_usu_alu;
	}

	public Nivel getNivel() {
		return nivel;
	}

	public void setNivel(Nivel nivel) {
		this.nivel = nivel;
	}

	public GiroNegocio getGiroNegocio() {
		return giroNegocio;
	}

	public void setGiroNegocio(GiroNegocio giroNegocio) {
		this.giroNegocio = giroNegocio;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	
}