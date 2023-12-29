package com.sige.mat.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;
import com.sige.mat.dao.CriterioAlternativaDAO;
import com.sige.mat.dao.CriterioNotaDAO;
import com.sige.mat.dao.CriterioPreAltDAO;
import com.sige.mat.dao.CriterioPreguntaDAO;
import com.sige.mat.dao.EvaluacionDAO;
import com.sige.mat.dao.EvaluacionVacDAO;
import com.sige.mat.dao.MatrVacanteDAO;
import com.tesla.colegio.model.CriterioAlternativa;
import com.tesla.colegio.model.CriterioNota;
import com.tesla.colegio.model.CriterioPreAlt;
import com.tesla.colegio.model.CriterioPregunta;
import com.tesla.colegio.model.EvaluacionVac;
import com.tesla.colegio.model.MatrVacante;


@RestController
@RequestMapping(value = "/api/criterioNota")
public class CriterioNotaRestController {

	
	@Autowired
	private CriterioNotaDAO criterio_notaDAO;
	
	@Autowired
	private CriterioPreguntaDAO criterioPreguntaDAO;
	
	@Autowired
	private CriterioAlternativaDAO criterioAlternativaDAO;
	
	@Autowired
	private CriterioPreAltDAO criterioPreAltDAO;
	
	@Autowired
	private MatrVacanteDAO	matrVacanteDAO;
	
	@Autowired
	private EvaluacionVacDAO evaluacionVacDAO;
	


	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(CriterioNota criterio_nota) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(criterio_notaDAO.listFullByParams( criterio_nota, new String[]{"eva_exa.id"}) );
		
		return result;
	}

	@Transactional
	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(CriterioNota criterio_nota, Integer id_alu, Integer id_mat,  String[] preg, String[] id_alt) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			if (criterio_notaDAO.tieneEva(id_mat)){
				result.setCode("500");
				result.setMsg("El alumno ya tiene ingresado su evaluacion psicol�gica" );
				return result;
			}
			MatrVacante matrVacante = matrVacanteDAO.getByParams(new Param("id",criterio_nota.getId_mat_vac()));
			//obtener el id_excri
			List<Row> confCriterio = criterio_notaDAO.conf_criterio(matrVacante.getId_eva());
			Integer id_ex_cri=null;
			if(confCriterio!=null)
				id_ex_cri=confCriterio.get(0).getInteger("id");
			criterio_nota.setId_ex_cri(id_ex_cri);
			int id_crit_not=criterio_notaDAO.saveOrUpdate(criterio_nota);
			int listPre[]= new int [preg.length];
			int listAlt[]= new int [id_alt.length];

			for(int i=0; i< preg.length; i++){
				listPre[i] = Integer.parseInt(preg[i]);
				listAlt[i] = Integer.parseInt(id_alt[i]);
				CriterioPreAlt criterioPreAlt = new CriterioPreAlt();
				criterioPreAlt.setId_cri_not(id_crit_not);
				criterioPreAlt.setId_pre(listPre[i]);
				criterioPreAlt.setId_alt(listAlt[i]);
				criterioPreAlt.setEst("A");
				criterioPreAltDAO.saveOrUpdate(criterioPreAlt);
			}
			result.setResult(id_crit_not);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			criterio_notaDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( criterio_notaDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/listarPreguntasRespuestas", method = RequestMethod.GET)
	public AjaxResponseBody listarPreguntasRespuestas(Integer id_mat) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			MatrVacante matr_vacante = matrVacanteDAO.getByParams(new Param("id",id_mat));
			EvaluacionVac evaluacion = evaluacionVacDAO.getByParams(new Param("id", matr_vacante.getId_eva()));
			Integer id_per= evaluacion.getId_per();
			CriterioNota nota_criterioList = criterio_notaDAO.getByParams(new Param("id_mat_vac",id_mat));
			if(nota_criterioList!=null){
				int id_cri_not=nota_criterioList.getId();
				List<CriterioPregunta> preguntas_list = criterioPreguntaDAO.listByParams(new Param("id_per", id_per),new String[]{"ord asc"} );
				for (CriterioPregunta criterioPregunta : preguntas_list) {
					int id_pre=criterioPregunta.getId();
					List<CriterioAlternativa> criterio_alternativaList = criterioAlternativaDAO.listByParams(new Param("id_pre",id_pre), null);
					criterioPregunta.setCriterioAlternativa(criterio_alternativaList);//
					
					Param param = new Param();
					param.put("id_pre", id_pre);
					param.put("id_cri_not", id_cri_not);
					CriterioPreAlt respuesta = criterioPreAltDAO.getByParams(param);
					criterioPregunta.setId_alt(respuesta.getId_alt());		
					
				}
				result.setResult(preguntas_list);
			} else{
				List<CriterioPregunta> preguntas_list = criterioPreguntaDAO.listByParams(new Param("id_per",id_per), new String[]{"ord asc"});

				for (CriterioPregunta criterioPregunta : preguntas_list) {
					int id_pre=criterioPregunta.getId();
					List<CriterioAlternativa> criterio_alternativaList = criterioAlternativaDAO.listByParams(new Param("id_pre",id_pre), null);
					criterioPregunta.setCriterioAlternativa(criterio_alternativaList);//
					
				}
				result.setResult(preguntas_list);
			}
			return result;
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/listarInstrumentosEvaluacion", method = RequestMethod.GET)
	public AjaxResponseBody listarInstrumentos(Integer id_matr_vac) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			MatrVacante matricula_vac = matrVacanteDAO.getByParams(new Param("id",id_matr_vac));
			result.setResult(criterio_notaDAO.instrumentosEvaluacion(matricula_vac.getId_eva()));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/obtenerNumero", method = RequestMethod.GET)
	public AjaxResponseBody obtenerNumeroEvaluacion(Integer id_matr_vac) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			List<Row> numero =criterio_notaDAO.numero();
			if(numero!=null)
				result.setResult(numero.get(0));
			else
				result.setResult(null);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
}
