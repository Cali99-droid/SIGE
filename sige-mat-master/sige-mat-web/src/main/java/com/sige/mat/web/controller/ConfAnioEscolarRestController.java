package com.sige.mat.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.sige.mat.dao.ConfAnioEscolarDAO;
import com.tesla.colegio.model.ConfAnioEscolar;

//import com.sige.common.cache.CacheManager;

@RestController
@RequestMapping(value = "/api/confAnioEscolar")
public class ConfAnioEscolarRestController {
	
	@Autowired
	private ConfAnioEscolarDAO conf_anio_escolarDAO;

	
	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(ConfAnioEscolar conf_anio_escolar) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(conf_anio_escolarDAO.listFullByParams( conf_anio_escolar, new String[]{"cnf_anio.id"}) );
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(ConfAnioEscolar conf_anio_escolar) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( conf_anio_escolarDAO.saveOrUpdate(conf_anio_escolar) );
			//cacheManager.update(ConfAnioEscolar.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			conf_anio_escolarDAO.delete(id);
			//cacheManager.update(ConfAnioEscolar.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( conf_anio_escolarDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/obtenerConfSemanas/{id_anio}", method = RequestMethod.GET)
	public AjaxResponseBody listarNiveles(@PathVariable Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( conf_anio_escolarDAO.getConfSemanas(id_anio).get(0));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
}
