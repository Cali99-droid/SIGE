package com.sige.mat.web.controller;
//package com.sige.spring.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.sige.mat.dao.AlumnoDescuentoDAO;
import com.tesla.colegio.model.AlumnoDescuento;
import com.tesla.frmk.rest.util.AjaxResponseBody;

@RestController
@RequestMapping(value = "/api/alumnoDescuento")
public class AlumnoDescuentoRestController {
	
	@Autowired
	private AlumnoDescuentoDAO alumno_descuentoDAO;

	//@JsonView(Views.Public.class)
	@RequestMapping(value = "/listar/{id_anio}")
	public AjaxResponseBody getLista(@PathVariable Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(alumno_descuentoDAO.listAlumnos(id_anio));
		
		return result;

	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(AlumnoDescuento alumno_descuento) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( alumno_descuentoDAO.saveOrUpdate(alumno_descuento) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			alumno_descuentoDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( alumno_descuentoDAO.getAlumno(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping(value = "/descuentos/{id_mat}")
	public AjaxResponseBody autocompleteAjax(@PathVariable Integer id_mat) {
		 
		AjaxResponseBody result = new AjaxResponseBody();

		result.setResult( alumno_descuentoDAO.descuentosPorMatricula(id_mat) );
		return result ;
		
	}
		
}
