package com.sige.mat.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;
import com.sige.mat.dao.ConfSemanasDAO;
import com.sige.mat.dao.CursoUnidadDAO;
import com.tesla.colegio.model.ConfSemanas;


@RestController
@RequestMapping(value = "/api/confSemanas")
public class ConfSemanasRestController {
	
	@Autowired
	private ConfSemanasDAO conf_semanasDAO;

	@Autowired
	private CursoUnidadDAO cursoUnidadDAO;

//	getByAnioNivelUnidad
	
	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(ConfSemanas conf_semanas, Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		result.setResult(conf_semanasDAO.listFullByParams(new Param("cnf_anio.id_anio",id_anio), new String[]{"cnf_sem.id"}) );
		
		return result;
	}

	/**
	 * Filtro paa mostrar la semana por a�o, nivel y unidad
	 * @param id_anio
	 * @param id_niv
	 * @param num
	 * @return
	 */
	@RequestMapping(value = "/listarxUnidad")
	public AjaxResponseBody getListarxUnidad(Integer id_anio,Integer id_niv,Integer num) {

		AjaxResponseBody result = new AjaxResponseBody();
		
		Row unidad = cursoUnidadDAO.getByAnioNivelUnidad(id_anio, id_niv, num);
		
		result.setResult(conf_semanasDAO.listarxUnidad(id_anio, unidad.getInteger("sem_ini"), unidad.getInteger("sem_fin")) );
		
		return result;
	}
	
	
	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(ConfSemanas conf_semanas) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( conf_semanasDAO.saveOrUpdate(conf_semanas) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			conf_semanasDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( conf_semanasDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
}
