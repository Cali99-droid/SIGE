package com.sige.mat.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.sige.mat.dao.ResDiarioBoletaDAO;
import com.tesla.colegio.model.ResDiarioBoleta;


@RestController
@RequestMapping(value = "/api/resDiarioBoleta")
public class ResDiarioBoletaRestController {
	
	@Autowired
	private ResDiarioBoletaDAO res_diario_boletaDAO;

	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(ResDiarioBoleta res_diario_boleta) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(res_diario_boletaDAO.listFullByParams( res_diario_boleta, new String[]{"frda.id"}) );
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(ResDiarioBoleta res_diario_boleta) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( res_diario_boletaDAO.saveOrUpdate(res_diario_boleta) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			res_diario_boletaDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( res_diario_boletaDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
}
