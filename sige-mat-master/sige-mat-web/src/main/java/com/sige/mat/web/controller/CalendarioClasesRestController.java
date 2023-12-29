package com.sige.mat.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.sige.core.dao.cache.CacheManager;
import com.sige.mat.dao.CalendarioClasesDAO;
import com.tesla.colegio.model.CalendarioClases;

@RestController
@RequestMapping(value = "/api/calendarioClases")
public class CalendarioClasesRestController {
	
	@Autowired
	private CalendarioClasesDAO calendario_clasesDAO;

	@Autowired
	private CacheManager cacheManager;
	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(CalendarioClases calendario_clases) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(calendario_clasesDAO.listFullByParams( calendario_clases, new String[]{"ccl.id"}) );
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(CalendarioClases calendario_clases) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( calendario_clasesDAO.saveOrUpdate(calendario_clases) );
			cacheManager.update(CalendarioClases.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			calendario_clasesDAO.delete(id);
			cacheManager.update(CalendarioClases.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( calendario_clasesDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
}
