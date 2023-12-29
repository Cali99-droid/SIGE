package com.sige.mat.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.sige.mat.dao.EvaPadreDAO;
import com.tesla.colegio.model.EvaPadre;


@RestController
@RequestMapping(value = "/api/evaPadre")
public class EvaPadreRestController {
	
	@Autowired
	private EvaPadreDAO eva_padreDAO;

	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(EvaPadre eva_padre) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(eva_padreDAO.listFullByParams( eva_padre, new String[]{"nep.id"}) );
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(EvaPadre eva_padre) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( eva_padreDAO.saveOrUpdate(eva_padre) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			eva_padreDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( eva_padreDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
}
