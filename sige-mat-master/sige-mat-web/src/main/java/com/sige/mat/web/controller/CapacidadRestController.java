package com.sige.mat.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sige.core.dao.cache.CacheManager;
import com.sige.mat.dao.CapacidadDAO;
import com.tesla.colegio.model.Capacidad;
import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;

@RestController
@RequestMapping(value = "/api/capacidad")
public class CapacidadRestController {
	
	@Autowired
	private CapacidadDAO capacidadDAO;

	@Autowired
	private CacheManager cacheManager;
	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(Capacidad capacidad, Integer id_com) {

		AjaxResponseBody result = new AjaxResponseBody();
		capacidad.setEst("A"); 
		Param param = new Param();
		param.put("cap.est", "A");
		param.put("cap.id_com", id_com);
		result.setResult(capacidadDAO.listFullByParams( param, new String[]{"cap.id"}) );
		
		return result;
	}
	
	@RequestMapping(value = "/listar/{id_com}")
	public AjaxResponseBody getLista2(Capacidad capacidad, @PathVariable Integer id_com) {

		AjaxResponseBody result = new AjaxResponseBody();
		capacidad.setEst("A"); 
		Param param = new Param();
		param.put("cap.est", "A");
		param.put("cap.id_com", id_com);
		result.setResult(capacidadDAO.listFullByParams( param, new String[]{"cap.id"}) );
		
		return result;
	}


	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(Capacidad capacidad) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( capacidadDAO.saveOrUpdate(capacidad) );
			cacheManager.update(Capacidad.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			capacidadDAO.delete(id);
			cacheManager.update(Capacidad.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( capacidadDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/listarCapacidadxSubtema", method = RequestMethod.GET)
	public AjaxResponseBody listarSubtemasxUnidad(Integer id_ccs) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( capacidadDAO.listaCapacidadxSubtema(id_ccs));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}

}
