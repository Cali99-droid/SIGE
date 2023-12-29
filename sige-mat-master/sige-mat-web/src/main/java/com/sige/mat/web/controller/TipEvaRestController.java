package com.sige.mat.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.sige.mat.dao.TipEvaDAO;
import com.tesla.colegio.model.TipEva;


@RestController
@RequestMapping(value = "/api/tipEva")
public class TipEvaRestController {
	
	@Autowired
	private TipEvaDAO tip_evaDAO;

	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(TipEva tip_eva) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(tip_evaDAO.listFullByParams( tip_eva, new String[]{"tae.id"}) );
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(TipEva tip_eva) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( tip_evaDAO.saveOrUpdate(tip_eva) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			tip_evaDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( tip_evaDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
}
