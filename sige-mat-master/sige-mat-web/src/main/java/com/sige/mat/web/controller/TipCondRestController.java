package com.sige.mat.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.sige.core.dao.cache.CacheManager;
import com.sige.mat.dao.TipCondDAO;
import com.tesla.colegio.model.TipCond;

@RestController
@RequestMapping(value = "/api/tipCond")
public class TipCondRestController {
	
	@Autowired
	private TipCondDAO tip_condDAO;

	@Autowired
	private CacheManager cacheManager;
	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(TipCond tip_cond) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(tip_condDAO.listFullByParams( tip_cond, new String[]{"ctc.id"}) );
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(TipCond tip_cond) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( tip_condDAO.saveOrUpdate(tip_cond) );
			cacheManager.update(TipCond.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			tip_condDAO.delete(id);
			cacheManager.update(TipCond.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( tip_condDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
}
