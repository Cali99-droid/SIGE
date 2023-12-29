package com.sige.mat.web.controller;
//package com.sige.spring.rest.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.sige.mat.dao.GradoHorarioDAO;
import com.tesla.colegio.model.GradoHorario;
import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.rest.util.Views;
import com.tesla.frmk.sql.Param;

@RestController
@RequestMapping(value = "/api/gradoHorario")
public class GradoHorarioRestController {
	
	@Autowired
	private GradoHorarioDAO grado_horarioDAO;
	
	@Autowired
	private HttpSession httpSession;

	//@JsonView(Views.Public.class)
	@RequestMapping(value = "/listar/{id_anio}")
	public AjaxResponseBody getLista(@PathVariable Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		
		 
		result.setResult(grado_horarioDAO.horarioList(id_anio));
		
		return result;

	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(GradoHorario grado_horario) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			if ("".equals(grado_horario.getHora_ini_aux()))grado_horario.setHora_ini_aux(null);
			if ("".equals(grado_horario.getHora_fin_aux()))grado_horario.setHora_fin_aux(null);
			
			result.setResult( grado_horarioDAO.saveOrUpdate(grado_horario) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			grado_horarioDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( grado_horarioDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
}
