package com.sige.mat.web.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.sige.mat.dao.EvaluacionVacDAO;
import com.sige.mat.dao.EvaluacionVacExamenDAO;
import com.sige.mat.dao.ExaConfCriterioDAO;
import com.sige.mat.dao.ExaConfEscritoDAO;
import com.sige.mat.dao.ExaConfMarcacionDAO;
import com.sige.mat.dao.InsExaCriDAO;
import com.sige.mat.dao.MatrVacanteDAO;
import com.tesla.colegio.model.EvaluacionVac;
import com.tesla.colegio.model.EvaluacionVacExamen;
import com.tesla.colegio.model.ExaConfCriterio;
import com.tesla.colegio.model.ExaConfEscrito;
import com.tesla.colegio.model.ExaConfMarcacion;
import com.tesla.colegio.model.InsExaCri;
import com.tesla.colegio.util.Constante;


@RestController
@RequestMapping(value = "/api/evaluacionVac")
public class EvaluacionVacRestController {
	
	@Autowired
	private EvaluacionVacDAO evaluacion_vacDAO;
	
	@Autowired
	private EvaluacionVacExamenDAO evaluacion_vac_exaDAO;

	@Autowired
	private ExaConfEscritoDAO exescritoDAO;
	
	@Autowired
	private ExaConfMarcacionDAO exmarcacionDAO;
		
	@Autowired
	private ExaConfCriterioDAO excriterioDAO;
	
	@Autowired
	private InsExaCriDAO ins_excriterioDAO;
	
	@Autowired
	private HttpSession httpSession;

	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(EvaluacionVac evaluacion_vac, Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(evaluacion_vacDAO.listFullByParams( evaluacion_vac, new String[]{"eva_vac.fec_fin desc, suc.nom desc, srv.nom asc"}) );
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(EvaluacionVac evaluacion_vac, EvaluacionVacExamen evaluacionVacExamen, ExaConfEscrito exaConfEscrito ,ExaConfCriterio exaConfCriterio , ExaConfMarcacion exaConfMarcacion,
				InsExaCri insExaCri, Integer id_eva_vacante, Integer id_examen, Integer id_escrito, Integer id_marcacion,Integer id_criterio,  String[] id_ins  ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			//result.setResult( evaluacion_vacDAO.saveOrUpdate(evaluacion_vac) );
			//tabla padre eva_evaluacion_vac
			evaluacion_vac.setId(id_eva_vacante);
			int id_eva=evaluacion_vacDAO.saveOrUpdate(evaluacion_vac);
			
			//tabla hijo examen eva_evaluacion_vac_examen
			evaluacionVacExamen.setId(id_examen);// se hace esto, por q ya existe un id en el formulario == evaluacion_vac 
			evaluacionVacExamen.setId_eva(id_eva);
			int id_eva_exa=evaluacion_vac_exaDAO.saveOrUpdate(evaluacionVacExamen);
			
			
			if (evaluacionVacExamen.getId_tae() ==com.tesla.colegio.util.Constante.TIPO_EVALUACION_ESCRITO) {
				exaConfEscrito.setId(id_escrito);
				exaConfEscrito.setId_eva_ex(id_eva_exa);
				exescritoDAO.saveOrUpdate(exaConfEscrito);
			}

			if (evaluacionVacExamen.getId_tae() ==com.tesla.colegio.util.Constante.TIPO_EVALUACION_MARCACION) {
				exaConfMarcacion.setId(id_marcacion);
				exaConfMarcacion.setId_eva_ex(id_eva_exa);
				exmarcacionDAO.saveOrUpdate(exaConfMarcacion);
			}
			
			if (evaluacionVacExamen.getId_tae() ==com.tesla.colegio.util.Constante.TIPO_EVALUACION_CRITERIO) {
				exaConfCriterio.setId_eva_ex(id_eva_exa);
				//if (id_criterio!=null)//significa que vamos a actualizar criterio
				exaConfCriterio.setId(id_criterio);

			//	exaConfCriterio.setFec_ini(fec_ini_psi);
			//	exaConfCriterio.setFec_fin(fec_fin_psi);
				int id_ex_cri=excriterioDAO.saveOrUpdate(exaConfCriterio);
				if (id_ins != null){//si hay instrumentos
				int listaIns[] = new int[ id_ins.length ];
				
				//quitar los que no tienen check
				List<InsExaCri> instrumentos = ins_excriterioDAO.listByParams(new Param("id_excri", id_ex_cri),null);
				final List<String> ainstrumentosForm = Arrays.asList(id_ins);

				//desactivar los q no estan en el form
				for (InsExaCri insExaCri2 : instrumentos) {
					if(!ainstrumentosForm.contains(insExaCri2.getId_ins().toString())){
						insExaCri2.setEst("I");
						ins_excriterioDAO.saveOrUpdate(insExaCri2);
					}
				}
				//activos
				Param p = new Param();
				p.put("id_excri", id_ex_cri);
				p.put("est", "A");
				List<InsExaCri> instrumentosActivos = ins_excriterioDAO.listByParams(p,null);
				final List<String>ins_grabados = new ArrayList<String>();
				for (InsExaCri insExaCri2 : instrumentosActivos) {
					ins_grabados.add(insExaCri2.getId_ins().toString());
				}
				
				//insertar los instrumentos del formulario
				for(int i=0; i< id_ins.length; i++){
					//listaIns[i] = Integer.parseInt(ins[i]);
					if(ins_grabados.size()==0 || !ins_grabados.contains(id_ins[i])){
						InsExaCri insExaCri2 = new InsExaCri();
						insExaCri2.setEst("A");
						insExaCri2.setId_excri(id_ex_cri);
						insExaCri2.setId_ins(Integer.parseInt(id_ins[i]));
						ins_excriterioDAO.saveOrUpdate(insExaCri2);
					}
					
				}
				}
			}
				Param param= new Param();
				param.put("id_eva", id_eva);
				param.put("id_examen", id_eva_exa);
			result.setResult(param);
			
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/eliminarExamen", method = RequestMethod.GET)
	public AjaxResponseBody eliminarExamen(Integer id_eva, Integer id_exa) {
	
	AjaxResponseBody result = new AjaxResponseBody();
	//EvaluacionVac evaluacion_vac = evaluacion_vacDAO.get(id_eva);
	//String id_examen = request.getParameter("id_examen");
	EvaluacionVacExamen evaluacion_vac_examen = null;
	
	//listar los examenes
	//List<Map<String, Object>> instrumentoList = evaluacion_vacDAO.listExcriIns(id_exa);

	List<EvaluacionVacExamen>  evaEvaluacionVacExamenList = evaluacion_vac_exaDAO.listFullByParams(new Param("id_eva",id_eva), null);
	if(id_exa!=null)
	for (EvaluacionVacExamen evaluacionVacExamen : evaEvaluacionVacExamenList) {
		if(id_exa.equals(evaluacionVacExamen.getId()))
			evaluacion_vac_examen = evaluacionVacExamen;
	}
	if(evaluacion_vac_examen==null && evaEvaluacionVacExamenList.size()>0) {
		evaluacion_vac_examen = evaEvaluacionVacExamenList.get(0);
	}
	//Tipo de evaluacion exa_conf_marcacion o exa_conf_criterio
	if(evaluacion_vac_examen.getId_tae()==Constante.TIPO_EVALUACION_ESCRITO) {
		exescritoDAO.delete(id_exa);
	}
	if(evaluacion_vac_examen.getId_tae()==Constante.TIPO_EVALUACION_MARCACION) {
		exmarcacionDAO.delete(id_exa);
	}
	if(evaluacion_vac_examen.getId_tae()==Constante.TIPO_EVALUACION_CRITERIO) {
		Integer id_ex_cri=excriterioDAO.getByParams(new Param("id_eva_ex",id_exa)).getId();
		ins_excriterioDAO.delete(id_ex_cri);
		excriterioDAO.delete(id_exa);
	}
	evaluacion_vac_exaDAO.delete(id_exa);
	return result;
	//List<EvaluacionVacExamen>  evaEvaluacionVacExamenList2 = evaluacion_vac_exaDAO.listFullByParams(new Param("id_eva",id_eva), null);
}
	@Transactional
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			List<EvaluacionVacExamen>  evaEvaluacionVacExamenList = evaluacion_vac_exaDAO.listFullByParams(new Param("id_eva",id), null);
			//Eliminamos los hijos
			if(evaEvaluacionVacExamenList.size()>0){
				for (EvaluacionVacExamen evaluacionVacExamen : evaEvaluacionVacExamenList) {
					if(evaluacionVacExamen.getId_tae()==Constante.TIPO_EVALUACION_CRITERIO){
						Integer id_ex_cri=excriterioDAO.getByParams(new Param("id_eva_ex",evaluacionVacExamen.getId())).getId();
						ins_excriterioDAO.delete(id_ex_cri);
						excriterioDAO.delete(evaluacionVacExamen.getId());
					}
					if(evaluacionVacExamen.getId_tae()==Constante.TIPO_EVALUACION_MARCACION){
						exmarcacionDAO.delete(evaluacionVacExamen.getId());
					}
					if(evaluacionVacExamen.getId_tae()==Constante.TIPO_EVALUACION_ESCRITO){
						exescritoDAO.delete(evaluacionVacExamen.getId());
					}
					evaluacion_vac_exaDAO.delete(evaluacionVacExamen.getId());
				}
			}
			evaluacion_vacDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( evaluacion_vacDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping(value = "/listarEvaluaciones")
	public AjaxResponseBody listarEvaluaciones(Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(evaluacion_vacDAO.listarEvaluaciones(id_anio));
		
		return result;
	}
	
	@RequestMapping(value = "/listarEvaluacionesVigentes")
	public AjaxResponseBody listarEvaluacionesVigentes(Integer id_niv, Integer id_suc, Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(evaluacion_vacDAO.listarEvaluacionesVigentes(id_niv, id_suc, id_anio));
		
		return result;
	}
	
	@RequestMapping(value = "/listarEvaluacionesLocalNivel")
	public AjaxResponseBody listarEvaluacionesxLocalNivel(Integer id_anio, 	Integer id_niv, Integer id_suc) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(evaluacion_vacDAO.EvaluacionVacList(id_niv, id_suc, id_anio));
		
		return result;
	}
	
	@RequestMapping(value = "/reporteGeneral")
	public AjaxResponseBody reporteGeneral(Integer id_eva,Integer id_grad, String ex_esc, String ex_psi, String vac) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(evaluacion_vacDAO.reporte_General(id_eva, id_grad, ex_esc, ex_psi, vac));
		
		return result;
	}
}
