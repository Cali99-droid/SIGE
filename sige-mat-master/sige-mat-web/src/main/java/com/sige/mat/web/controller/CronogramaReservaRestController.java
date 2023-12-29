package com.sige.mat.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.sige.mat.dao.CronogramaReservaDAO;
import com.tesla.colegio.model.CronogramaReserva;


@RestController
@RequestMapping(value = "/api/cronogramaReserva")
public class CronogramaReservaRestController {
	
	@Autowired
	private CronogramaReservaDAO cronograma_reservaDAO;

	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(CronogramaReserva cronograma_reserva, Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		cronograma_reserva.setId_anio(id_anio);
		result.setResult(cronograma_reservaDAO.listFullByParams( cronograma_reserva, new String[]{"mcr.id"}) );
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(CronogramaReserva cronograma_reserva) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( cronograma_reservaDAO.saveOrUpdate(cronograma_reserva) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			cronograma_reservaDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( cronograma_reservaDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
}
