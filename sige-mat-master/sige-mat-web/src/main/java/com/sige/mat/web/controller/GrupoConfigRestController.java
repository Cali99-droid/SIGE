package com.sige.mat.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.sige.mat.dao.GrupoConfigDAO;
import com.tesla.colegio.model.GrupoConfig;


@RestController
@RequestMapping(value = "/api/grupoConfig")
public class GrupoConfigRestController {
	
	@Autowired
	private GrupoConfigDAO grupo_configDAO;

	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(GrupoConfig grupo_config) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(grupo_configDAO.listFullByParams( grupo_config, new String[]{"cgc.id"}) );
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(GrupoConfig grupo_config) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( grupo_configDAO.saveOrUpdate(grupo_config) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			grupo_configDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( grupo_configDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
}
