package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Clase entidad que representa a la tabla rhh_contrato_trabajador
 * @author MV
 *
 */
public class ContratoTrabajador extends EntidadBase{

	public final static String TABLA = "rhh_contrato_trabajador";
	private Integer id;
	private Integer id_tra;
	private Integer id_anio_con;
	private Integer id_emp;
	private Integer id_gir;
	private Integer id_reg;
	private Integer id_mod;
	private Integer id_cat;
	private Integer id_pue;
	private Integer id_niv_tra;
	private Integer id_prue;
	private Integer id_lin_carr;
	private Integer id_den;
	private Integer id_rem_cat;
	private Integer id_frec_pag;
	private String num_con;
	private String con_indf;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date fec_ini;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date fec_fin;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private java.util.Date fec_fin_prue;
	private Trabajador trabajador;	
	private Anio anio;	
	private Empresa empresa;	
	private GiroNegocio gironegocio;	
	private RegimenLaboral regimenlaboral;	
	private ModalidadTrabajo modalidadtrabajo;	
	private CategoriaOcupacional categoriaocupacional;	
	private PuestoTrabajador puestotrabajador;	
	private Nivel nivel;	
	private PeriodoPrueba periodoprueba;	
	private LineaCarrera lineacarrera;	
	private Denominacion denominacion;	
	private Remuneracion remuneracion;	
	private TipFrecPago tipfrecpago;	

	public ContratoTrabajador(){
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

	/**
	* Obtiene Anio Contrato 
	* @return id_anio_con
	*/
	public Integer getId_anio_con(){
		return id_anio_con;
	}	

	/**
	* Anio Contrato 
	* @param id_anio_con
	*/
	public void setId_anio_con(Integer id_anio_con) {
		this.id_anio_con = id_anio_con;
	}

	/**
	* Obtiene Empresa 
	* @return id_emp
	*/
	public Integer getId_emp(){
		return id_emp;
	}	

	/**
	* Empresa 
	* @param id_emp
	*/
	public void setId_emp(Integer id_emp) {
		this.id_emp = id_emp;
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
	* Obtiene Regimen Laboral 
	* @return id_reg
	*/
	public Integer getId_reg(){
		return id_reg;
	}	

	/**
	* Regimen Laboral 
	* @param id_reg
	*/
	public void setId_reg(Integer id_reg) {
		this.id_reg = id_reg;
	}

	/**
	* Obtiene Modalidad Trabajo 
	* @return id_mod
	*/
	public Integer getId_mod(){
		return id_mod;
	}	

	/**
	* Modalidad Trabajo 
	* @param id_mod
	*/
	public void setId_mod(Integer id_mod) {
		this.id_mod = id_mod;
	}

	/**
	* Obtiene Categoria Ocupacional 
	* @return id_cat
	*/
	public Integer getId_cat(){
		return id_cat;
	}	

	/**
	* Categoria Ocupacional 
	* @param id_cat
	*/
	public void setId_cat(Integer id_cat) {
		this.id_cat = id_cat;
	}

	/**
	* Obtiene Puesto Trabajador 
	* @return id_pue
	*/
	public Integer getId_pue(){
		return id_pue;
	}	

	/**
	* Puesto Trabajador 
	* @param id_pue
	*/
	public void setId_pue(Integer id_pue) {
		this.id_pue = id_pue;
	}

	/**
	* Obtiene Nivel 
	* @return id_niv_tra
	*/
	public Integer getId_niv_tra(){
		return id_niv_tra;
	}	

	/**
	* Nivel 
	* @param id_niv_tra
	*/
	public void setId_niv_tra(Integer id_niv_tra) {
		this.id_niv_tra = id_niv_tra;
	}

	/**
	* Obtiene Periodo Prueba 
	* @return id_prue
	*/
	public Integer getId_prue(){
		return id_prue;
	}	

	/**
	* Periodo Prueba 
	* @param id_prue
	*/
	public void setId_prue(Integer id_prue) {
		this.id_prue = id_prue;
	}

	/**
	* Obtiene Linea Carrera 
	* @return id_lin_carr
	*/
	public Integer getId_lin_carr(){
		return id_lin_carr;
	}	

	/**
	* Linea Carrera 
	* @param id_lin_carr
	*/
	public void setId_lin_carr(Integer id_lin_carr) {
		this.id_lin_carr = id_lin_carr;
	}

	/**
	* Obtiene Denominacion 
	* @return id_den
	*/
	public Integer getId_den(){
		return id_den;
	}	

	/**
	* Denominacion 
	* @param id_den
	*/
	public void setId_den(Integer id_den) {
		this.id_den = id_den;
	}

	/**
	* Obtiene Remuneracion 
	* @return id_rem
	*/
	public Integer getId_rem_cat(){
		return id_rem_cat;
	}	

	/**
	* Remuneracion 
	* @param id_rem
	*/
	public void setId_rem_cat(Integer id_rem_cat) {
		this.id_rem_cat = id_rem_cat;
	}

	/**
	* Obtiene Frecuencia Pago 
	* @return id_frec_pag
	*/
	public Integer getId_frec_pag(){
		return id_frec_pag;
	}	

	/**
	* Frecuencia Pago 
	* @param id_frec_pag
	*/
	public void setId_frec_pag(Integer id_frec_pag) {
		this.id_frec_pag = id_frec_pag;
	}

	/**
	* Obtiene Numero de Contrato 
	* @return num_con
	*/
	public String getNum_con(){
		return num_con;
	}	

	/**
	* Numero de Contrato 
	* @param num_con
	*/
	public void setNum_con(String num_con) {
		this.num_con = num_con;
	}

	/**
	* Obtiene Fecha Inicio 
	* @return fec_ini
	*/
	public java.util.Date getFec_ini(){
		return fec_ini;
	}	

	/**
	* Fecha Inicio 
	* @param fec_ini
	*/
	public void setFec_ini(java.util.Date fec_ini) {
		this.fec_ini = fec_ini;
	}

	/**
	* Obtiene Fecha Fin 
	* @return fec_fin
	*/
	public java.util.Date getFec_fin(){
		return fec_fin;
	}	

	/**
	* Fecha Fin 
	* @param fec_fin
	*/
	public void setFec_fin(java.util.Date fec_fin) {
		this.fec_fin = fec_fin;
	}

	/**
	* Obtiene Fecha Fin Prueba 
	* @return fec_fin_prue
	*/
	public java.util.Date getFec_fin_prue(){
		return fec_fin_prue;
	}	

	/**
	* Fecha Fin Prueba 
	* @param fec_fin_prue
	*/
	public void setFec_fin_prue(java.util.Date fec_fin_prue) {
		this.fec_fin_prue = fec_fin_prue;
	}

	public Trabajador getTrabajador(){
		return trabajador;
	}	

	public void setTrabajador(Trabajador trabajador) {
		this.trabajador = trabajador;
	}
	public Anio getAnio(){
		return anio;
	}	

	public void setAnio(Anio anio) {
		this.anio = anio;
	}
	public Empresa getEmpresa(){
		return empresa;
	}	

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
	public GiroNegocio getGiroNegocio(){
		return gironegocio;
	}	

	public void setGiroNegocio(GiroNegocio gironegocio) {
		this.gironegocio = gironegocio;
	}
	public RegimenLaboral getRegimenLaboral(){
		return regimenlaboral;
	}	

	public void setRegimenLaboral(RegimenLaboral regimenlaboral) {
		this.regimenlaboral = regimenlaboral;
	}
	public ModalidadTrabajo getModalidadTrabajo(){
		return modalidadtrabajo;
	}	

	public void setModalidadTrabajo(ModalidadTrabajo modalidadtrabajo) {
		this.modalidadtrabajo = modalidadtrabajo;
	}
	public CategoriaOcupacional getCategoriaOcupacional(){
		return categoriaocupacional;
	}	

	public void setCategoriaOcupacional(CategoriaOcupacional categoriaocupacional) {
		this.categoriaocupacional = categoriaocupacional;
	}
	public PuestoTrabajador getPuestoTrabajador(){
		return puestotrabajador;
	}	

	public void setPuestoTrabajador(PuestoTrabajador puestotrabajador) {
		this.puestotrabajador = puestotrabajador;
	}
	public Nivel getNivel(){
		return nivel;
	}	

	public void setNivel(Nivel nivel) {
		this.nivel = nivel;
	}
	public PeriodoPrueba getPeriodoPrueba(){
		return periodoprueba;
	}	

	public void setPeriodoPrueba(PeriodoPrueba periodoprueba) {
		this.periodoprueba = periodoprueba;
	}
	public LineaCarrera getLineaCarrera(){
		return lineacarrera;
	}	

	public void setLineaCarrera(LineaCarrera lineacarrera) {
		this.lineacarrera = lineacarrera;
	}
	public Denominacion getDenominacion(){
		return denominacion;
	}	

	public void setDenominacion(Denominacion denominacion) {
		this.denominacion = denominacion;
	}
	public Remuneracion getRemuneracion(){
		return remuneracion;
	}	

	public void setRemuneracion(Remuneracion remuneracion) {
		this.remuneracion = remuneracion;
	}
	public TipFrecPago getTipFrecPago(){
		return tipfrecpago;
	}	

	public void setTipFrecPago(TipFrecPago tipfrecpago) {
		this.tipfrecpago = tipfrecpago;
	}

	public String getCon_indf() {
		return con_indf;
	}

	public void setCon_indf(String con_indf) {
		this.con_indf = con_indf;
	}
}