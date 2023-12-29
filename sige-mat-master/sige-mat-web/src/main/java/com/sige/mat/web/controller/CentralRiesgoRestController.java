package com.sige.mat.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.sige.core.dao.cache.CacheManager;
import com.sige.mat.dao.CentralRiesgoDAO;
import com.tesla.colegio.model.CentralRiesgo;

@RestController
@RequestMapping(value = "/api/centralRiesgo")
public class CentralRiesgoRestController {
	
	@Autowired
	private CentralRiesgoDAO central_riesgoDAO;

	@Autowired
	private CacheManager cacheManager;
	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(CentralRiesgo central_riesgo) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(central_riesgoDAO.listFullByParams( central_riesgo, new String[]{"ccr.id"}) );
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(CentralRiesgo central_riesgo) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( central_riesgoDAO.saveOrUpdate(central_riesgo) );
			cacheManager.update(CentralRiesgo.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			central_riesgoDAO.delete(id);
			cacheManager.update(CentralRiesgo.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( central_riesgoDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
}
