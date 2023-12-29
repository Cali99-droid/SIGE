package com.sige.mat.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.sige.mat.dao.ConfReciboDAO;
import com.sige.spring.service.FacturacionService;
import com.tesla.colegio.model.ConfRecibo;
import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.rest.util.Views;
import com.tesla.frmk.sql.Param;


@RestController
@RequestMapping(value = "/api/confRecibo")
public class ConfReciboRestController {

	@Autowired
	private ConfReciboDAO confReciboDAO;

	//@JsonView(Views.Public.class)
	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista() {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult( confReciboDAO.listFullByParams( new Param(), new String[]{"rco.id_suc","rco.serie"}) );
		
		return result;

	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(ConfRecibo conf_recibo) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( confReciboDAO.saveOrUpdate(conf_recibo) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			confReciboDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( confReciboDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
}
