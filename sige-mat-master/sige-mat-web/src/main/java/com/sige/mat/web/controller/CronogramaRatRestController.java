package com.sige.mat.web.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.sige.core.dao.cache.CacheManager;
import com.sige.mat.dao.AnioDAO;
import com.sige.mat.dao.CronogramaRatificacionDAO;
import com.tesla.colegio.model.Anio;
import com.tesla.colegio.model.CronogramaRatificacion;

@RestController
@RequestMapping(value = "/api/cronogramaRatificacion")
public class CronogramaRatRestController {
	
	@Autowired
	private CronogramaRatificacionDAO cronogramaRatificacionDAO;

	@Autowired
	private CacheManager cacheManager;
	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(CronogramaRatificacion cronogramaRatificacion) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(cronogramaRatificacionDAO.listFullByParams(cronogramaRatificacion, new String[]{"anio.id"}) );
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(CronogramaRatificacion cronogramaRatificacion) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			
				result.setResult(cronogramaRatificacionDAO.saveOrUpdate(cronogramaRatificacion));	
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			cronogramaRatificacionDAO.delete(id);
			cacheManager.update(Anio.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( cronogramaRatificacionDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	/*@RequestMapping( value="/listarAnios", method = RequestMethod.GET)
	public AjaxResponseBody listarAnios() {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( anioDAO.listaAnios());
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}*/
}
