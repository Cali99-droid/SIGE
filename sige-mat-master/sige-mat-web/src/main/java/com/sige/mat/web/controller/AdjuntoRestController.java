package com.sige.mat.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.sige.mat.dao.AdjuntoDAO;
import com.tesla.colegio.model.Adjunto;


@RestController
@RequestMapping(value = "/api/file")
public class AdjuntoRestController {
	
	@Autowired
	private AdjuntoDAO adjuntoDAO;

	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(Adjunto adjunto) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(adjuntoDAO.listFullByParams( adjunto, new String[]{"adj.id"}) );
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(Adjunto adjunto) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( adjuntoDAO.saveOrUpdate(adjunto) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			adjuntoDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( adjuntoDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
}
