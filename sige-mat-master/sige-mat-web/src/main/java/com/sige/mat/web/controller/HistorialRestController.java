package com.sige.mat.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.sige.mat.dao.HistorialDAO;
import com.tesla.colegio.model.Historial;


@RestController
@RequestMapping(value = "/api/historial")
public class HistorialRestController {
	
	@Autowired
	private HistorialDAO historialDAO;

	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(Historial historial) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(historialDAO.listFullByParams( historial, new String[]{"hist.id"}) );
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(Historial historial) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( historialDAO.saveOrUpdate(historial) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			historialDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( historialDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
}
