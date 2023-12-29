package com.sige.mat.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.sige.core.dao.cache.CacheManager;
import com.sige.mat.dao.UnidadTemaDAO;
import com.tesla.colegio.model.UnidadTema;

@RestController
@RequestMapping(value = "/api/unidadTema")
public class UnidadTemaRestController {
	
	@Autowired
	private UnidadTemaDAO unidad_temaDAO;

	@Autowired
	private CacheManager cacheManager;
	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(UnidadTema unidad_tema) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(unidad_temaDAO.listFullByParams( unidad_tema, new String[]{"cut.id"}) );
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(UnidadTema unidad_tema) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( unidad_temaDAO.saveOrUpdate(unidad_tema) );
			cacheManager.update(UnidadTema.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			unidad_temaDAO.delete(id);
			cacheManager.update(UnidadTema.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( unidad_temaDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
}
