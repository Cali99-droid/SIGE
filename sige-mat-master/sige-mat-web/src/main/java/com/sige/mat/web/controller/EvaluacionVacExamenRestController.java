package com.sige.mat.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.sige.mat.dao.EvaluacionVacExamenDAO;
import com.tesla.colegio.model.EvaluacionVacExamen;


@RestController
@RequestMapping(value = "/api/evaluacionVacExamen")
public class EvaluacionVacExamenRestController {
	
	@Autowired
	private EvaluacionVacExamenDAO evaluacion_vac_examenDAO;

	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(EvaluacionVacExamen evaluacion_vac_examen, Integer id_eva) {

		AjaxResponseBody result = new AjaxResponseBody();
		evaluacion_vac_examen.setId_eva(id_eva);
		result.setResult(evaluacion_vac_examenDAO.listFullByParams( evaluacion_vac_examen, new String[]{"evaex.id"}) );
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(EvaluacionVacExamen evaluacion_vac_examen) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( evaluacion_vac_examenDAO.saveOrUpdate(evaluacion_vac_examen) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			evaluacion_vac_examenDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( evaluacion_vac_examenDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/obtenerDatos/{id_exa}/{tip_exa}", method = RequestMethod.GET)
	public AjaxResponseBody obtenerDatos(@PathVariable Integer id_exa, @PathVariable  Integer tip_exa ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( evaluacion_vac_examenDAO.obtenerDatos(id_exa, tip_exa).get(0) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
}
