package com.sige.mat.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.sige.mat.dao.PeriodoCursoDAO;
import com.tesla.colegio.model.PeriodoCurso;


@RestController
@RequestMapping(value = "/api/periodoCurso")
public class PeriodoCursoRestController {
	
	@Autowired
	private PeriodoCursoDAO periodo_cursoDAO;

	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(PeriodoCurso periodo_curso) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(periodo_cursoDAO.listFullByParams( periodo_curso, new String[]{"cpc.id"}) );
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(PeriodoCurso periodo_curso) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( periodo_cursoDAO.saveOrUpdate(periodo_curso) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			periodo_cursoDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( periodo_cursoDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
}
