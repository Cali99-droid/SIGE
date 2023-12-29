package com.sige.mat.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.sige.mat.dao.TutorAulaDAO;
import com.tesla.colegio.model.TutorAula;


@RestController
@RequestMapping(value = "/api/tutorAula")
public class TutorAulaRestController {
	
	@Autowired
	private TutorAulaDAO tutor_aulaDAO;

	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(TutorAula tutor_aula, Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		Param param = new Param();
		param.put("per.id_anio", id_anio);
		result.setResult(tutor_aulaDAO.listFullByParams( param, new String[]{"niv.id, gra.id, aula.secc,tra.ape_pat, tra.ape_mat, tra.nom"}) );
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(TutorAula tutor_aula) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( tutor_aulaDAO.saveOrUpdate(tutor_aula) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			tutor_aulaDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( tutor_aulaDAO.listFullByParams(new Param("cta.id",id), null).get(0) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarProfesor", method = RequestMethod.GET)
	public AjaxResponseBody listarProfesor( ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( tutor_aulaDAO.listaProfesor());
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
}
