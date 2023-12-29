package com.sige.mat.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.sige.core.dao.cache.CacheManager;
import com.sige.mat.dao.SesionIndicadorDAO;
import com.tesla.colegio.model.SesionIndicador;

@RestController
@RequestMapping(value = "/api/sesionIndicador")
public class SesionIndicadorRestController {
	
	@Autowired
	private SesionIndicadorDAO sesion_indicadorDAO;

	@Autowired
	private CacheManager cacheManager;
	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(SesionIndicador sesion_indicador) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(sesion_indicadorDAO.listFullByParams( sesion_indicador, new String[]{"csi.id"}) );
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(SesionIndicador sesion_indicador) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( sesion_indicadorDAO.saveOrUpdate(sesion_indicador) );
			cacheManager.update(SesionIndicador.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			sesion_indicadorDAO.delete(id);
			cacheManager.update(SesionIndicador.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( sesion_indicadorDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
}
