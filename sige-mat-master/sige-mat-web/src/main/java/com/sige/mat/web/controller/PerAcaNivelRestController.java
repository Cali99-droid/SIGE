package com.sige.mat.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.sige.core.dao.cache.CacheManager;
import com.sige.mat.dao.PerAcaNivelDAO;
import com.tesla.colegio.model.PerAcaNivel;

@RestController
@RequestMapping(value = "/api/perAcaNivel")
public class PerAcaNivelRestController {
	
	@Autowired
	private PerAcaNivelDAO per_aca_nivelDAO;

	@Autowired
	private CacheManager cacheManager;
	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(PerAcaNivel per_aca_nivel) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(per_aca_nivelDAO.listFullByParams( per_aca_nivel, new String[]{"cpan.id"}) );
		
		return result;
	}
	
	@RequestMapping(value = "/listarPeriodosxNivelyAnio") //aqui
	public AjaxResponseBody listarPeriodosxNivelyAnio(Integer id_anio, Integer id_niv, Integer id_gir) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(per_aca_nivelDAO.listarPeriodosAcademicosxNivelAnio(id_niv, id_anio, id_gir));
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(PerAcaNivel per_aca_nivel) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( per_aca_nivelDAO.saveOrUpdate(per_aca_nivel) );
			cacheManager.update(PerAcaNivel.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			per_aca_nivelDAO.delete(id);
			cacheManager.update(PerAcaNivel.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( per_aca_nivelDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
}
