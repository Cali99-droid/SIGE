package com.sige.mat.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.sige.core.dao.cache.CacheManager;
import com.sige.mat.dao.OpcionDAO;
import com.tesla.colegio.model.Opcion;
import com.tesla.colegio.model.bean.MenuItem;

@RestController
@RequestMapping(value = "/api/opcion")
public class OpcionRestController {
	
	@Autowired
	private OpcionDAO opcionDAO;

	@Autowired
	private CacheManager cacheManager;
	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista( ) {

		AjaxResponseBody result = new AjaxResponseBody();
		Param param = new Param();
		result.setResult(opcionDAO.listFullByParams( param, new String[]{"opc.id"}) );
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(Opcion opcion) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( opcionDAO.saveOrUpdate(opcion) );
			cacheManager.update(Opcion.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			opcionDAO.delete(id);
			cacheManager.update(Opcion.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( opcionDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
}
