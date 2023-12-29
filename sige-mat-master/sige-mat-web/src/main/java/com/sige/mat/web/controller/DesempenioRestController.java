package com.sige.mat.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.sige.mat.dao.DesempenioDAO;
import com.tesla.colegio.model.Desempenio;


@RestController
@RequestMapping(value = "/api/desempenio")
public class DesempenioRestController {
	
	@Autowired
	private DesempenioDAO desempenioDAO;

	/*@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(Desempenio desempenio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(desempenioDAO.listFullByParams( desempenio, new String[]{"cde.id"}) );
		
		return result;
	}*/

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(Desempenio desempenio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			
			result.setResult( desempenioDAO.saveOrUpdate(desempenio) );
			
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			desempenioDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( desempenioDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarDesempenios", method = RequestMethod.GET)
	public AjaxResponseBody listarDesempenios(Integer id_cap, Integer id_cgsp, Integer id_ses) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( desempenioDAO.listaDesempenios(id_cap,id_cgsp, id_ses));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
}
