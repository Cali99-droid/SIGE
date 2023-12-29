package com.sige.mat.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;		
		
import com.tesla.colegio.model.TurnoAula;
import com.sige.mat.dao.TurnoAulaDAO;


@RestController
@RequestMapping(value = "/api/turnoAula")
public class TurnoAulaRestController {
	
	@Autowired
	private TurnoAulaDAO turno_aulaDAO;

	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(TurnoAula turno_aula) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(turno_aulaDAO.listFullByParams( turno_aula, new String[]{"atur.id"}) );
		
		return result;
	}
	
	@RequestMapping(value = "/listarTurnosxAula")
	public AjaxResponseBody listarTurnoxAula(Integer id_au, Integer id_cic) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		if (id_cic!=null)
			result.setResult(turno_aulaDAO.listarTurnosxAula(id_au, id_cic));
		else
			result.setResult(null);
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(TurnoAula turno_aula) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( turno_aulaDAO.saveOrUpdate(turno_aula) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			turno_aulaDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( turno_aulaDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
}
