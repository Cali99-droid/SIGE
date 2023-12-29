package com.sige.mat.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.sige.mat.dao.ServicioDAO;
import com.tesla.colegio.model.Servicio;


@RestController
@RequestMapping(value = "/api/servicio")
public class ServicioRestController {

	
	@Autowired
	private ServicioDAO servicioDAO;

	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(Servicio servicio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(servicioDAO.listFullByParams( servicio, new String[]{"srv.id"}) );
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(Servicio servicio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( servicioDAO.saveOrUpdate(servicio) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			servicioDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( servicioDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/listarServicios", method = RequestMethod.GET)
	public AjaxResponseBody listarServicios() {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( servicioDAO.listaServicios() );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
}
