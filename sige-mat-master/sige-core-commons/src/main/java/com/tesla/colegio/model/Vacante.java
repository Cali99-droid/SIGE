
package com.tesla.colegio.model;

import java.util.List;

import com.tesla.colegio.model.bean.GradoCapacidad;
import com.tesla.frmk.model.EntidadBase;

/**
 * Clase entidad que representa a la tabla eva_vacante
 * 
 * @author MV
 *
 */
public class Vacante extends EntidadBase {

	public final static String TABLA = "eva_vacante";
	private Integer id;
	private Integer id_per;
	private Integer id_eva;
	private Integer id_grad;
	private Integer nro_vac;
	private Integer vac_ofe;
	private Integer post;
	private Periodo periodo;
	private EvaluacionVac evaluacionvac;
	private Grad grad;

	public List<GradoCapacidad> getGrados() {
		return grados;
	}

	public void setGrados(List<GradoCapacidad> grados) {
		this.grados = grados;
	}

	private List<GradoCapacidad> grados;

	public Vacante() {
	}

	/**
	 * Obtiene Llave Principal
	 * 
	 * @return id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Llave Principal
	 * 
	 * @param id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Obtiene Periodo Acadmico
	 * 
	 * @return id_per
	 */
	public Integer getId_per() {
		return id_per;
	}

	/**
	 * Periodo Acadmico
	 * 
	 * @param id_per
	 */
	public void setId_per(Integer id_per) {
		this.id_per = id_per;
	}

	/**
	 * Obtiene Evaluacin Vacante
	 * 
	 * @return id_eva
	 */
	public Integer getId_eva() {
		return id_eva;
	}

	/**
	 * Evaluacin Vacante
	 * 
	 * @param id_eva
	 */
	public void setId_eva(Integer id_eva) {
		this.id_eva = id_eva;
	}

	/**
	 * Obtiene Grado
	 * 
	 * @return id_grad
	 */
	public Integer getId_grad() {
		return id_grad;
	}

	/**
	 * Grado
	 * 
	 * @param id_grad
	 */
	public void setId_grad(Integer id_grad) {
		this.id_grad = id_grad;
	}

	/**
	 * Obtiene Es automatico, solo de lectura
	 * 
	 * @return nro_vac
	 */
	public Integer getNro_vac() {
		return nro_vac;
	}

	/**
	 * Es automatico, solo de lectura
	 * 
	 * @param nro_vac
	 */
	public void setNro_vac(Integer nro_vac) {
		this.nro_vac = nro_vac;
	}

	/**
	 * Obtiene Vacantes ofertadas
	 * 
	 * @return vac_ofe
	 */
	public Integer getVac_ofe() {
		return vac_ofe;
	}

	/**
	 * Vacantes ofertadas
	 * 
	 * @param vac_ofe
	 */
	public void setVac_ofe(Integer vac_ofe) {
		this.vac_ofe = vac_ofe;
	}

	/**
	 * Obtiene Postulantes
	 * 
	 * @return post
	 */
	public Integer getPost() {
		return post;
	}

	/**
	 * Postulantes
	 * 
	 * @param post
	 */
	/**
	 * Postulantes
	 * 
	 * @param post
	 */
	public void setPost(Integer post) {
		this.post = post;
	}

	public Periodo getPeriodo() {
		return periodo;
	}

	public void setPeriodo(Periodo periodo) {
		this.periodo = periodo;
	}

	public EvaluacionVac getEvaluacionVac() {
		return evaluacionvac;
	}

	public void setEvaluacionVac(EvaluacionVac evaluacionvac) {
		this.evaluacionvac = evaluacionvac;
	}

	public Grad getGrad() {
		return grad;
	}

	public void setGrad(Grad grad) {
		this.grad = grad;
	}
}