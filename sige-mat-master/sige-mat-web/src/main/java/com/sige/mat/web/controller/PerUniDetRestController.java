package com.sige.mat.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.sige.mat.dao.PerUniDetDAO;
import com.tesla.colegio.model.PerUniDet;


@RestController
@RequestMapping(value = "/api/perUniDet")
public class PerUniDetRestController {
	
	@Autowired
	private PerUniDetDAO per_uni_detDAO;

	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(PerUniDet per_uni_det, Integer id_cpu) {

		AjaxResponseBody result = new AjaxResponseBody();
		per_uni_det.setId_cpu(id_cpu);
		result.setResult(per_uni_detDAO.listFullByParams( per_uni_det, new String[]{"cpud.id"}) );
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(PerUniDet per_uni_det) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( per_uni_detDAO.saveOrUpdate(per_uni_det) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			per_uni_detDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( per_uni_detDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
}
