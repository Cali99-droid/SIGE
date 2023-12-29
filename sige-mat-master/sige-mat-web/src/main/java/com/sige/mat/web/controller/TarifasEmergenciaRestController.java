package com.sige.mat.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.sige.mat.dao.TarifasEmergenciaDAO;
import com.sige.spring.service.PagosService;
import com.tesla.colegio.model.TarifasEmergencia;


@RestController
@RequestMapping(value = "/api/tarifasEmergencia")
public class TarifasEmergenciaRestController {
	
	@Autowired
	private TarifasEmergenciaDAO tarifas_emergenciaDAO;
	
	@Autowired
	private PagosService pagosService;

	@RequestMapping(value = "/listar/{id_anio}")
	public AjaxResponseBody getLista(@PathVariable Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		Param param = new Param();
		param.put("per.id_anio", id_anio);
		 
		result.setResult(tarifas_emergenciaDAO.listFullByParams( param, new String[]{"mte.id"}) );
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(TarifasEmergencia tarifas_emergencia) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( tarifas_emergenciaDAO.saveOrUpdate(tarifas_emergencia) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			tarifas_emergenciaDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( tarifas_emergenciaDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/procesarTarifa", method = RequestMethod.POST)
	public AjaxResponseBody procesarTarifa(Integer id_per, Integer mes, Integer id_tar) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			pagosService.procesarTarifaEmergencia(id_per, mes, id_tar);
			result.setResult("1");
		} catch (Exception e) {
			e.printStackTrace();
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/procesarTarifaxAlumo", method = RequestMethod.POST)
	public AjaxResponseBody procesarTarifaxAlumno(Integer id_mat) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			pagosService.procesarTarifaEmergenciaxAlumno(id_mat);
			result.setResult("1");
		} catch (Exception e) {
			e.printStackTrace();
			result.setException(e);
		}
		
		return result;

	}	
}
