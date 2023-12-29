package com.sige.mat.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.sige.mat.dao.InstrumentoDAO;
import com.tesla.colegio.model.Instrumento;


@RestController
@RequestMapping(value = "/api/instrumento")
public class InstrumentoRestController {
	
	@Autowired
	private InstrumentoDAO instrumentoDAO;

	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(Instrumento instrumento) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(instrumentoDAO.listFullByParams( instrumento, new String[]{"ins.id"}) );
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(Instrumento instrumento) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( instrumentoDAO.saveOrUpdate(instrumento) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			instrumentoDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( instrumentoDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping(value = "/listarInstrumentos")
	public AjaxResponseBody listarIns(Integer id_exa) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(instrumentoDAO.listIns(id_exa) );
		
		return result;
	}
}
