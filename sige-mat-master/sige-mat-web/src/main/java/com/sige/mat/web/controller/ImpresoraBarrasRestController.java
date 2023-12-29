package com.sige.mat.web.controller;
//package com.sige.spring.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sige.mat.dao.ImpresoraBarrasDAO;
import com.tesla.colegio.model.ImpresoraBarras;
import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;

@RestController
@RequestMapping(value = "/api/impresoraBarras")
public class ImpresoraBarrasRestController {
	
	@Autowired
	private ImpresoraBarrasDAO impresora_barrasDAO;

	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista() {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(impresora_barrasDAO.listFullByParams( new Param(), new String[]{"aib.id"}) );
		
		return result;

	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(ImpresoraBarras impresora_barras) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( impresora_barrasDAO.saveOrUpdate(impresora_barras) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			impresora_barrasDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( impresora_barrasDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
}
