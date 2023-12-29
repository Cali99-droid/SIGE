package com.sige.mat.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.sige.mat.dao.NotaUpdDAO;
import com.tesla.colegio.model.NotaUpd;


@RestController
@RequestMapping(value = "/api/notaUpd")
public class NotaUpdController {

	
	@Autowired
	private NotaUpdDAO nota_updDAO;

	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(NotaUpd nota_upd) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(nota_updDAO.listFullByParams( nota_upd, new String[]{"audn.id"}) );
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(NotaUpd nota_upd) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( nota_updDAO.saveOrUpdate(nota_upd) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			nota_updDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( nota_updDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
}
