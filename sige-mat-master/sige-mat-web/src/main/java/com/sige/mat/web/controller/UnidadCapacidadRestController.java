package com.sige.mat.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.sige.core.dao.cache.CacheManager;
import com.sige.mat.dao.UnidadCapacidadDAO;
import com.tesla.colegio.model.UnidadCapacidad;

@RestController
@RequestMapping(value = "/api/unidadCapacidad")
public class UnidadCapacidadRestController {
	
	@Autowired
	private UnidadCapacidadDAO unidad_capacidadDAO;

	@Autowired
	private CacheManager cacheManager;
	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(UnidadCapacidad unidad_capacidad) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(unidad_capacidadDAO.listFullByParams( unidad_capacidad, new String[]{"cuc.id"}) );
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(UnidadCapacidad unidad_capacidad) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( unidad_capacidadDAO.saveOrUpdate(unidad_capacidad) );
			cacheManager.update(UnidadCapacidad.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			unidad_capacidadDAO.delete(id);
			cacheManager.update(UnidadCapacidad.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( unidad_capacidadDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
}
