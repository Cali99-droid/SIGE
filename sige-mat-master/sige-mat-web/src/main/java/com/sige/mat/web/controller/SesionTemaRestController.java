package com.sige.mat.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.sige.core.dao.cache.CacheManager;
import com.sige.mat.dao.SesionTemaDAO;
import com.tesla.colegio.model.SesionTema;

@RestController
@RequestMapping(value = "/api/sesionTema")
public class SesionTemaRestController {
	
	@Autowired
	private SesionTemaDAO sesion_temaDAO;

	@Autowired
	private CacheManager cacheManager;
	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(SesionTema sesion_tema) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(sesion_temaDAO.listFullByParams( sesion_tema, new String[]{"cst.id"}) );
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(SesionTema sesion_tema) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( sesion_temaDAO.saveOrUpdate(sesion_tema) );
			cacheManager.update(SesionTema.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			sesion_temaDAO.delete(id);
			cacheManager.update(SesionTema.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( sesion_temaDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
}
