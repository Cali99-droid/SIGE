package com.sige.mat.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.sige.mat.dao.ConfMensualidadDAO;
import com.tesla.colegio.model.ConfMensualidad;


@RestController
@RequestMapping(value = "/api/confMensualidad")
public class ConfMensualidadRestController {
	
	@Autowired
	private ConfMensualidadDAO conf_mensualidadDAO;

	@RequestMapping(value = "/listar/{id_anio}")
	public AjaxResponseBody getLista(@PathVariable Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		Param param = new Param();
		param.put("pee.id_anio", id_anio);
		 
		result.setResult(conf_mensualidadDAO.listFullByParams( param, new String[]{"men.id"}) );
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(ConfMensualidad conf_mensualidad) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( conf_mensualidadDAO.saveOrUpdate(conf_mensualidad) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			conf_mensualidadDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( conf_mensualidadDAO.listFullByParams(new Param("men.id",id), null).get(0) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
}
