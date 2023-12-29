package com.sige.mat.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.sige.core.dao.cache.CacheManager;
import com.sige.mat.dao.PeriodoAcaDAO;
import com.tesla.colegio.model.PeriodoAca;

@RestController
@RequestMapping(value = "/api/periodoAca")
public class PeriodoAcaRestController {
	
	@Autowired
	private PeriodoAcaDAO periodo_acaDAO;

	@Autowired
	private CacheManager cacheManager;
	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(PeriodoAca periodo_aca) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(periodo_acaDAO.listFullByParams( periodo_aca, new String[]{"cpa.id"}) );
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(PeriodoAca periodo_aca) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( periodo_acaDAO.saveOrUpdate(periodo_aca) );
			cacheManager.update(PeriodoAca.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			periodo_acaDAO.delete(id);
			cacheManager.update(PeriodoAca.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( periodo_acaDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/listarPeriodoxTrabajador", method = RequestMethod.GET)
	public AjaxResponseBody listarHistorial(Integer id_anio,Integer id_usr, Integer id_rol) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( periodo_acaDAO.listarPeriodo(id_anio, id_usr, id_rol));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarPeriodosVacante", method = RequestMethod.GET)
	public AjaxResponseBody listarPeriodosVacante(Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( periodo_acaDAO.listarPeriodoVacante(id_anio));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarPeriodosxLocalNivel", method = RequestMethod.GET)
	public AjaxResponseBody listarPeriodosVacante(Integer id_anio, Integer id_suc, Integer id_niv) {

		AjaxResponseBody result = new AjaxResponseBody();

		try {
			result.setResult( periodo_acaDAO.listarPeriodoxLocalNivel(id_anio, id_suc, id_niv));
		} catch (Exception e) {
			result.setException(e);
		}

		return result;

	}
}
