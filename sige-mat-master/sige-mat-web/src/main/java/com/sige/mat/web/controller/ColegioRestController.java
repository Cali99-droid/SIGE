package com.sige.mat.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.sige.core.dao.cache.CacheManager;
import com.sige.mat.dao.ColegioDAO;
import com.tesla.colegio.model.Colegio;

@RestController
@RequestMapping(value = "/api/colegio")
public class ColegioRestController {
	
	@Autowired
	private ColegioDAO colegioDAO;

	@Autowired
	private CacheManager cacheManager;
	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(Colegio colegio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(colegioDAO.listFullByParams( colegio, new String[]{"col.id"}) );
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(Colegio colegio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( colegioDAO.saveOrUpdate(colegio) );
			cacheManager.update(Colegio.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			colegioDAO.delete(id);
			cacheManager.update(Colegio.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( colegioDAO.listFullByParams(new Param("col.id",id), null).get(0));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarcolegios", method = RequestMethod.GET)
	public AjaxResponseBody colegios() {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			
				result.setResult(colegioDAO.listColegios());
			
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarcolegiosConcurso", method = RequestMethod.GET)
	public AjaxResponseBody colegiosConcurso(String nivel) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			
				result.setResult(colegioDAO.listColegiosConcurso(nivel));
			
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarcolegiosDistrito", method = RequestMethod.GET)
	public AjaxResponseBody listarcolegiosDistrito(String nivel) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			
				result.setResult(colegioDAO.listColegiosDistrito(nivel));
			
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
}
