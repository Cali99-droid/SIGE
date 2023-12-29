package com.sige.mat.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.sige.core.dao.cache.CacheManager;
import com.sige.mat.dao.RemuneracionCatDAO;
import com.sige.mat.dao.SubtemaDAO;
import com.tesla.colegio.model.RemuneracionCat;
import com.tesla.colegio.model.Subtema;


@RestController
@RequestMapping(value = "/api/remuneracionCat")
public class RemuneracionCatRestController {
	
	@Autowired
	private RemuneracionCatDAO remuneracionCatDAO;
	
	@Autowired
	private CacheManager cacheManager;
	
	@RequestMapping(value = "/listar/{id_anio}")
	public AjaxResponseBody getLista(RemuneracionCat remuneracionCat, @PathVariable Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		remuneracionCat.setId_anio(id_anio);
		result.setResult(remuneracionCatDAO.listFullByParams( remuneracionCat, new String[]{"rcat.id"}) );
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(RemuneracionCat remuneracionCat, Integer id_anio_rem) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			remuneracionCat.setId_anio(id_anio_rem);
			result.setResult( remuneracionCatDAO.saveOrUpdate(remuneracionCat) );
			cacheManager.update(Subtema.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			remuneracionCatDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( remuneracionCatDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
		
	
}
