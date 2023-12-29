package com.sige.mat.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.sige.mat.dao.ReceptorDAO;
import com.tesla.colegio.model.Receptor;


@RestController
@RequestMapping(value = "/api/receptor")
public class ReceptorRestController {
	
	@Autowired
	private ReceptorDAO receptorDAO;

	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(Receptor receptor) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(receptorDAO.listFullByParams( receptor, new String[]{"rec.id"}) );
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(Receptor receptor) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( receptorDAO.saveOrUpdate(receptor) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			receptorDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( receptorDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
}
