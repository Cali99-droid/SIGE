package com.sige.mat.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.sige.mat.dao.ProgAnualDAO;
import com.tesla.colegio.model.ProgAnual;


@RestController
@RequestMapping(value = "/api/progAnual")
public class ProgAnualRestController {
	
	@Autowired
	private ProgAnualDAO prog_anualDAO;

	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(ProgAnual prog_anual) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(prog_anualDAO.listFullByParams( prog_anual, new String[]{"cpg.id"}) );
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(ProgAnual prog_anual) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( prog_anualDAO.saveOrUpdate(prog_anual) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			prog_anualDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( prog_anualDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
}
