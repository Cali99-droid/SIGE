package com.sige.mat.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.sige.mat.dao.LogLoginDAO;
import com.tesla.colegio.model.LogLogin;


@RestController
@RequestMapping(value = "/api/logLogin")
public class LogLoginRestController {
	
	@Autowired
	private LogLoginDAO log_loginDAO;

	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(LogLogin log_login) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(log_loginDAO.listFullByParams( log_login, new String[]{"llo.id"}) );
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(LogLogin log_login) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( log_loginDAO.saveOrUpdate(log_login) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			log_loginDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( log_loginDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
}
