package com.sige.mat.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.fasterxml.jackson.annotation.JsonView;
import com.sige.mat.dao.HabilitacionDAO;
import com.tesla.colegio.model.Habilitacion;


@RestController
@RequestMapping(value = "/api/habilitacion")
public class HabilitacionRestController {
	
	@Autowired
	private HabilitacionDAO habilitacionDAO;

	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(Habilitacion habilitacion, Integer id_anio ) {

		AjaxResponseBody result = new AjaxResponseBody();
		habilitacion.setId_anio(id_anio); 
		result.setResult(habilitacionDAO.listFullByParams( habilitacion, new String[]{"mh.id"}) );
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(Habilitacion habilitacion) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( habilitacionDAO.saveOrUpdate(habilitacion) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			habilitacionDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( habilitacionDAO.listFullByParams(new Param("mh.id",id),null).get(0) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	//alumnos matriculados
	@RequestMapping(value = "/buscarAlumnos")
	@JsonView
	public Object autocompleteAjax(@RequestParam String term, @RequestParam Integer id_anio,@RequestParam Integer id_suc) {
		return habilitacionDAO.listaAlumnos(term,  id_anio, id_suc);

	}
}
