package com.sige.mat.web.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.sige.mat.dao.IndEvaDAO;
import com.tesla.colegio.model.IndEva;


@RestController
@RequestMapping(value = "/api/indEva")
public class IndEvaRestController {
	final static Logger logger = Logger.getLogger(IndEvaRestController.class);
	@Autowired
	private IndEvaDAO ind_evaDAO;

	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(IndEva ind_eva) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(ind_evaDAO.listFullByParams( ind_eva, new String[]{"nie.id"}) );
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(IndEva ind_eva) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			int id_indeva= ind_evaDAO.saveOrUpdate(ind_eva);
			result.setResult(id_indeva);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			ind_evaDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="eliminarInd/{id_ne}/{id_cis}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id_ne, @PathVariable Integer id_cis ) {

		AjaxResponseBody result = new AjaxResponseBody();
		logger.debug("hola:" + id_ne);
		try {
			ind_evaDAO.deleteInd(id_ne, id_cis);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( ind_evaDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
}
