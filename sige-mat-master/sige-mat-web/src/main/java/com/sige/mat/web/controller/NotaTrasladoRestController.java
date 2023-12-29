package com.sige.mat.web.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.sige.mat.dao.NotaDAO;
import com.sige.spring.service.NotaTrasladoService;

import org.apache.log4j.Logger; 

@RestController
@RequestMapping(value = "/api/notaTraslado")
public class NotaTrasladoRestController {

	final static Logger logger = Logger.getLogger(NotaTrasladoRestController.class);

	@Autowired
	private NotaDAO notaDAO;
	
	@Autowired
	private NotaTrasladoService notaTrasladoService;

	@RequestMapping(value = "/cursosPeriodo")
	public AjaxResponseBody getLista(Integer id_mat, Integer nump) {

		AjaxResponseBody result = new AjaxResponseBody();

		result.setResult(notaDAO.promerdioNotasPorPeriodo(id_mat, nump));

		return result;
	}

	@RequestMapping(value = "/grabar", method = RequestMethod.POST)
	public AjaxResponseBody grabarNota(Integer id_cpu,Integer[] nota, Integer id_mat) {

		
		AjaxResponseBody result = new AjaxResponseBody();
		result.setResult(notaTrasladoService.grabarNotasPeriodo(id_cpu,id_mat, nota));
		return result;
	}
}