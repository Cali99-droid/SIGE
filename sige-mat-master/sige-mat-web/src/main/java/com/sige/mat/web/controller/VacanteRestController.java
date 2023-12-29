package com.sige.mat.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.sige.mat.dao.VacanteDAO;
import com.tesla.colegio.model.Vacante;


@RestController
@RequestMapping(value = "/api/vacante")
public class VacanteRestController {
	
	@Autowired
	private VacanteDAO vacanteDAO;

	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(Vacante vacante) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(vacanteDAO.listFullByParams( vacante, new String[]{"vac.id"}) );
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(Vacante vacante) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( vacanteDAO.saveOrUpdate(vacante) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			vacanteDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( vacanteDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/listarEvaluaciones", method = RequestMethod.GET)
	public AjaxResponseBody listarEvaluaciones( Integer id_per) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( vacanteDAO.EvaluacionVacLista(id_per));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarGrados", method = RequestMethod.GET)
	public AjaxResponseBody listarGrados( Integer id_eva, Integer id_niv) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( vacanteDAO.listarGrados(id_eva, id_niv));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	
}
