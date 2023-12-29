package com.sige.mat.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.sige.common.enums.EnumTipoPeriodo;
import com.sige.mat.dao.PeriodoDAO;
import com.sige.spring.service.SituacionFinalService;
import com.tesla.colegio.model.Periodo; 


@RestController
@RequestMapping(value = "/api/situacionFinal")
public class SituacionFinalRestController {
 
	@Autowired
	private SituacionFinalService situacionFinalService;

	@Autowired
	private PeriodoDAO periodoDAO;
	
	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(Periodo periodo) {

		AjaxResponseBody result = new AjaxResponseBody();
		periodo.setId_tpe(EnumTipoPeriodo.ESCOLAR.getValue());
		 
		result.setResult(periodoDAO.listFullByParams( periodo, new String[]{"pee.id"}) );
		
		return result;
	}
	
	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(Integer[] id) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		if(id==null || id.length==0){
			result.setCode("201");
			result.setMsg("Debe seleccionar al menos un nivel");
			return result;
		}
		 
		situacionFinalService.grabar(id);
		
		return result;

	}	
	
}
