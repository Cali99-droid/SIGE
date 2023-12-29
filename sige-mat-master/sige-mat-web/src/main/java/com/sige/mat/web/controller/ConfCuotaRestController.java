package com.sige.mat.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.sige.mat.dao.ConfCuotaDAO;
import com.tesla.colegio.model.ConfCuota;


@RestController
@RequestMapping(value = "/api/confCuota")
public class ConfCuotaRestController {
	
	@Autowired
	private ConfCuotaDAO conf_cuotaDAO;

	@RequestMapping(value = "/listar/{id_anio}")
	public AjaxResponseBody getLista(@PathVariable Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		Param param = new Param();
		param.put("pee.id_anio", id_anio);
		result.setResult(conf_cuotaDAO.listFullByParams( param, new String[]{"men.id"}) );
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(ConfCuota conf_cuota) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( conf_cuotaDAO.saveOrUpdate(conf_cuota) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			conf_cuotaDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET) //aquiio
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( conf_cuotaDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/datosCuota/{id}", method = RequestMethod.GET)
	public AjaxResponseBody datosCuota(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( conf_cuotaDAO.datosCuotaxId(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
}
