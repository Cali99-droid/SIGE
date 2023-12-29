package com.sige.mat.web.controller;
//package com.sige.spring.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.sige.core.dao.cache.CacheManager;
import com.sige.mat.dao.PerfilDAO;
import com.tesla.colegio.model.Perfil;
import com.tesla.colegio.model.SesionTema;
import com.tesla.colegio.model.Trabajador;
import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.rest.util.Views;
import com.tesla.frmk.sql.Param;

@RestController
@RequestMapping(value = "/api/perfil")
public class PerfilRestController {
	
	@Autowired
	private PerfilDAO perfilDAO;
	
	@Autowired
	private CacheManager cacheManager;

	//@JsonView(Views.Public.class)
	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista() {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(perfilDAO.listFullByParams( new Param(), new String[]{"per.id"}) );
		
		return result;

	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(Perfil perfil) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( perfilDAO.saveOrUpdate(perfil) );
			cacheManager.update(Perfil.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			perfilDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( perfilDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
}
