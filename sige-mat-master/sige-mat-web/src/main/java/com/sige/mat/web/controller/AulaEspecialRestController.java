package com.sige.mat.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sige.mat.dao.AulaEspecialDAO;
import com.tesla.colegio.model.AulaEspecial;
import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;


@RestController
@RequestMapping(value = "/api/aulaEspecial")
public class AulaEspecialRestController {
	
	@Autowired
	private AulaEspecialDAO aula_especialDAO;

	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		Param param = new Param();
		param.put("per.id_anio", id_anio);
		result.setResult(aula_especialDAO.listFullByParams( param, new String[]{"cae.id"}) );
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(AulaEspecial aula_especial) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			
			aula_especial.setEst("A");
			result.setResult( aula_especialDAO.saveOrUpdate(aula_especial) );
			
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			aula_especialDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( aula_especialDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
}
