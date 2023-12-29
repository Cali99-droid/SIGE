package com.sige.mat.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.sige.mat.dao.HistorialEcoDAO;
import com.tesla.colegio.model.HistorialEco;


@RestController
@RequestMapping(value = "/api/historialEco")
public class HistorialEcoRestController {
	
	@Autowired
	private HistorialEcoDAO historial_ecoDAO;

	@RequestMapping(value = "/listar/{id_mat}")
	public AjaxResponseBody getLista(HistorialEco historial_eco, @PathVariable Integer id_mat) {

		AjaxResponseBody result = new AjaxResponseBody();
		historial_eco.setId_mat(id_mat);
		result.setResult(historial_ecoDAO.listFullByParams( historial_eco, new String[]{"csh.id"}) );
		
		return result;
	}

	@RequestMapping( value="/grabarHistorial/{id_mat}", method = RequestMethod.POST)
	public AjaxResponseBody grabar(HistorialEco historial_eco, @PathVariable Integer id_mat ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			historial_eco.setId_mat(id_mat);
			result.setResult( historial_ecoDAO.saveOrUpdate(historial_eco) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			historial_ecoDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( historial_ecoDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
}
