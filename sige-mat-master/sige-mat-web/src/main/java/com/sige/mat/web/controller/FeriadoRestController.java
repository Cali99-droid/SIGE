package com.sige.mat.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.sige.core.dao.cache.CacheManager;
import com.sige.mat.dao.FeriadoDAO;
import com.tesla.colegio.model.Feriado;

@RestController
@RequestMapping(value = "/api/feriado")
public class FeriadoRestController {
	
	@Autowired
	private FeriadoDAO feriadoDAO;

	@Autowired
	private CacheManager cacheManager;
	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(Feriado feriado) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(feriadoDAO.listFullByParams( feriado, new String[]{"afe.dia asc"}) );
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(Feriado feriado) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( feriadoDAO.saveOrUpdate(feriado) );
			cacheManager.update(Feriado.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			feriadoDAO.delete(id);
			cacheManager.update(Feriado.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( feriadoDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
}
