package com.sige.mat.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.sige.core.dao.cache.CacheManager;
import com.sige.mat.dao.AreaDAO;
import com.tesla.colegio.model.Area;

@RestController
@RequestMapping(value = "/api/area")
public class AreaRestController {
	
	@Autowired
	private AreaDAO areaDAO;

	@Autowired
	private CacheManager cacheManager;
	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(Area area) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(areaDAO.listFullByParams( area, new String[]{"area.nom asc"}) );
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(Area area) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( areaDAO.saveOrUpdate(area) );
			cacheManager.update(Area.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			areaDAO.delete(id);
			cacheManager.update(Area.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( areaDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
}
