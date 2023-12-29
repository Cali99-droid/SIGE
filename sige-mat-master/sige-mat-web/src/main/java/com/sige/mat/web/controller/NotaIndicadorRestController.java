package com.sige.mat.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.sige.mat.dao.NotaIndicadorDAO;
import com.tesla.colegio.model.NotaIndicador;


@RestController
@RequestMapping(value = "/api/notaIndicador")
public class NotaIndicadorRestController {
	
	@Autowired
	private NotaIndicadorDAO nota_indicadorDAO;

	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(NotaIndicador nota_indicador) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(nota_indicadorDAO.listFullByParams( nota_indicador, new String[]{"nni.id"}) );
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(NotaIndicador nota_indicador) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( nota_indicadorDAO.saveOrUpdate(nota_indicador) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id_eva}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id_eva ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			nota_indicadorDAO.delete(id_eva);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( nota_indicadorDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
}
