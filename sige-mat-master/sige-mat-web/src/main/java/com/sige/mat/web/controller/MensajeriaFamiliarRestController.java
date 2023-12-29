 package com.sige.mat.web.controller;


import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.sige.mat.dao.MensajeriaFamiliarDAO;
import com.sige.rest.request.MsgReq;
import com.sige.spring.service.MensajeriaService;
import com.tesla.colegio.model.MensajeriaFamiliar;


@RestController
@RequestMapping(value = "/api/mensajeriaFamiliar")
public class MensajeriaFamiliarRestController {
	final static Logger logger = Logger.getLogger(MensajeriaFamiliarRestController.class);

	@Autowired
	private MensajeriaFamiliarDAO mensajeria_familiarDAO;

	@Autowired
	private MensajeriaService mensajeriaService;
	
	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(MensajeriaFamiliar mensajeria_familiar) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(mensajeria_familiarDAO.listFullByParams( mensajeria_familiar, new String[]{"msjf.id"}) );
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(MensajeriaFamiliar mensajeria_familiar) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( mensajeria_familiarDAO.saveOrUpdate(mensajeria_familiar) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			mensajeria_familiarDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( mensajeria_familiarDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/pendientes",method = RequestMethod.GET)
	public AjaxResponseBody pendientes() {

		AjaxResponseBody result = new AjaxResponseBody();
		 logger.info("pendientes:" + new Date());
		 
		try {
			result.setResult( mensajeriaService.getMensajesPendientesFamiliar() );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;
	}
	

	@RequestMapping( value="/leidos",method = RequestMethod.POST)
	public AjaxResponseBody leidos(@RequestBody MsgReq objeto) {

		String ids = objeto.getIds();
		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			mensajeriaService.actalizarLeidos(ids );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;
	}
	
}
