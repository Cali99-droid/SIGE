package com.sige.mat.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.sige.mat.dao.IndicadorDAO;
import com.tesla.colegio.model.Indicador;


@RestController
@RequestMapping(value = "/api/indicador")
public class IndicadorRestController {
	
	@Autowired
	private IndicadorDAO indicadorDAO;

	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(Indicador indicador) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(indicadorDAO.listFullByParams( indicador, new String[]{"ci.id"}) );
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(Indicador indicador) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( indicadorDAO.saveOrUpdate(indicador) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			indicadorDAO.deleteIndicador(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( indicadorDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
}
