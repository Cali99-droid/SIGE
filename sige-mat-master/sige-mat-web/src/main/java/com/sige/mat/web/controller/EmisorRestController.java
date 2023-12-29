package com.sige.mat.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.sige.mat.dao.EmisorDAO;
import com.tesla.colegio.model.Emisor;


@RestController
@RequestMapping(value = "/api/emisor")
public class EmisorRestController {
	
	@Autowired
	private EmisorDAO emisorDAO;

	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(Emisor emisor) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(emisorDAO.listFullByParams( emisor, new String[]{"emi.id"}) );
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(Emisor emisor) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( emisorDAO.saveOrUpdate(emisor) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			emisorDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( emisorDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
}
