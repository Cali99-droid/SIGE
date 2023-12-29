package com.sige.mat.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.sige.mat.dao.MensajeDAO;
import com.tesla.colegio.model.Mensaje;


@RestController
@RequestMapping(value = "/api/mensaje")
public class MensajeRestController {
	
	@Autowired
	private MensajeDAO mensajeDAO;

	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(Mensaje mensaje) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(mensajeDAO.listFullByParams( mensaje, new String[]{"msj.id"}) );
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(Mensaje mensaje) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( mensajeDAO.saveOrUpdate(mensaje) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			mensajeDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( mensajeDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
}
