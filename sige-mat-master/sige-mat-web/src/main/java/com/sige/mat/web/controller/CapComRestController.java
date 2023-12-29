package com.sige.mat.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.sige.mat.dao.CapComDAO;
import com.tesla.colegio.model.CapCom;


@RestController
@RequestMapping(value = "/api/capCom")
public class CapComRestController {
	
	@Autowired
	private CapComDAO cap_comDAO;

	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(CapCom cap_com) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(cap_comDAO.listFullByParams( cap_com, new String[]{"ncc.id"}) );
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(CapCom cap_com) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( cap_comDAO.saveOrUpdate(cap_com) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			//cap_comDAO.delete(id); ESPERA DOS PARAMETROS ,,MV
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( cap_comDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
}
