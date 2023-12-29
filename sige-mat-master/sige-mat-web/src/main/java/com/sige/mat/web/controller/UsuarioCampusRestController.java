package com.sige.mat.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.sige.mat.dao.UsuarioCampusDAO;
import com.tesla.colegio.model.UsuarioCampus;


@RestController
@RequestMapping(value = "/api/usuarioCampus")
public class UsuarioCampusRestController {
	
	@Autowired
	private UsuarioCampusDAO usuario_campusDAO;

	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(UsuarioCampus usuario_campus) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(usuario_campusDAO.listFullByParams( usuario_campus, new String[]{"cviu.id"}) );
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(UsuarioCampus usuario_campus) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( usuario_campusDAO.saveOrUpdate(usuario_campus) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			usuario_campusDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( usuario_campusDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
}
