package com.sige.mat.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.sige.mat.dao.EstadoMensajeriaDAO;
import com.tesla.colegio.model.EstadoMensajeria;


@RestController
@RequestMapping(value = "/api/estadoMensajeria")
public class EstadoMensajeriaRestController {
	
	@Autowired
	private EstadoMensajeriaDAO estado_mensajeriaDAO;

	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(EstadoMensajeria estado_mensajeria) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(estado_mensajeriaDAO.listFullByParams( estado_mensajeria, new String[]{"est.id"}) );
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(EstadoMensajeria estado_mensajeria) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( estado_mensajeriaDAO.saveOrUpdate(estado_mensajeria) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			estado_mensajeriaDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( estado_mensajeriaDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
}
