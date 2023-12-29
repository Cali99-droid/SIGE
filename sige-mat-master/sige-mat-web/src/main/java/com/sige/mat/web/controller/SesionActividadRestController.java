package com.sige.mat.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.sige.core.dao.cache.CacheManager;
import com.sige.mat.dao.SesionActividadDAO;
import com.tesla.colegio.model.SesionActividad;

@RestController
@RequestMapping(value = "/api/sesionActividad")
public class SesionActividadRestController {
	
	@Autowired
	private SesionActividadDAO sesion_actividadDAO;

	@Autowired
	private CacheManager cacheManager;
	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(SesionActividad sesion_actividad) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(sesion_actividadDAO.listFullByParams( sesion_actividad, new String[]{"csa.id"}) );
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(SesionActividad sesion_actividad) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( sesion_actividadDAO.saveOrUpdate(sesion_actividad) );
			cacheManager.update(SesionActividad.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			sesion_actividadDAO.delete(id);
			cacheManager.update(SesionActividad.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( sesion_actividadDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
}
