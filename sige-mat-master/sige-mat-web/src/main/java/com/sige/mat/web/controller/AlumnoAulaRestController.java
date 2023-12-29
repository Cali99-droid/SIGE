package com.sige.mat.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.sige.mat.dao.AlumnoAulaDAO;
import com.tesla.colegio.model.AlumnoAula;


@RestController
@RequestMapping(value = "/api/alumnoAula")
public class AlumnoAulaRestController {
	
	@Autowired
	private AlumnoAulaDAO alumno_aulaDAO;

	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(AlumnoAula alumno_aula) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(alumno_aulaDAO.listFullByParams( alumno_aula, new String[]{"cala.id"}) );
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(AlumnoAula alumno_aula) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( alumno_aulaDAO.saveOrUpdate(alumno_aula) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			alumno_aulaDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( alumno_aulaDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
}
