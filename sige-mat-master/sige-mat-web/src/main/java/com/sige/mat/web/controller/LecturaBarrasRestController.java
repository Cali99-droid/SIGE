package com.sige.mat.web.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sige.mat.dao.LecturaBarrasDAO;
import com.tesla.colegio.model.LecturaBarras;
import com.tesla.frmk.rest.util.AjaxResponseBody;


@RestController
@RequestMapping(value = "/api/asiLectura")
public class LecturaBarrasRestController {
	
	@Autowired
	private LecturaBarrasDAO lecturaBarrasDAO;
	

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(LecturaBarras lecturaBarras) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( lecturaBarrasDAO.saveOrUpdate(lecturaBarras) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( lecturaBarrasDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			lecturaBarrasDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
}
