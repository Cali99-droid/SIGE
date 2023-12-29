package com.sige.mat.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.sige.mat.dao.CondAlumnoDAO;
import com.tesla.colegio.model.CondAlumno;


@RestController
@RequestMapping(value = "/api/condAlumno")
public class CondAlumnoRestController {
	
	@Autowired
	private CondAlumnoDAO cond_alumnoDAO;

	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(CondAlumno cond_alumno) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(cond_alumnoDAO.listFullByParams( cond_alumno, new String[]{"cond.id"}) );
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(CondAlumno cond_alumno) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( cond_alumnoDAO.saveOrUpdate(cond_alumno) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			cond_alumnoDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( cond_alumnoDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/listarCondicionTipo", method = RequestMethod.GET)
	public AjaxResponseBody listarCondEco() {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( cond_alumnoDAO.listaCondicionTipo());
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarCondicionTipoCond", method = RequestMethod.GET)
	public AjaxResponseBody listaCondicionTipoCond()  {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( cond_alumnoDAO.listaCondicionTipoCond());
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
}
