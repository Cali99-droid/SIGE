package com.sige.mat.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;		
		
import com.tesla.colegio.model.ReglasNegocio;
import com.sige.mat.dao.ReglasNegocioDAO;


@RestController
@RequestMapping(value = "/api/reglasNegocio")
public class ReglasNegocioRestController {
	
	@Autowired
	private ReglasNegocioDAO reglas_negocioDAO;

	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(ReglasNegocio reglas_negocio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(reglas_negocioDAO.listFullByParams( reglas_negocio, new String[]{"col_reg.id"}) );
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(ReglasNegocio reglas_negocio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( reglas_negocioDAO.saveOrUpdate(reglas_negocio) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			reglas_negocioDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( reglas_negocioDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping(value = "/listarReglasNegocio")
	public AjaxResponseBody getListaModulos(Integer id_mod, Integer id_emp) {

		AjaxResponseBody result = new AjaxResponseBody();
		
		Param param = new Param();
		param.put("id_emp", id_emp);
		param.put("id_mod", id_mod);
		 
		result.setResult(reglas_negocioDAO.listByParams(param,null));
		
		return result;
	}
	
	@RequestMapping(value = "/validarActualizacionDatos")
	public AjaxResponseBody validarActualizacionDatos() {

		AjaxResponseBody result = new AjaxResponseBody();
		
		Param param = new Param();
		param.put("cod","ACT_DATOS_OBL");
		 
		result.setResult(reglas_negocioDAO.getByParams(param));
		
		return result;
	}
	
	@RequestMapping(value = "/validarCambioAula")
	public AjaxResponseBody validarCambioAula() {

		AjaxResponseBody result = new AjaxResponseBody();
		
		Param param = new Param();
		param.put("cod","MODIFICAR_AULA");
		 
		result.setResult(reglas_negocioDAO.getByParams(param));
		
		return result;
	}
	
	@RequestMapping(value = "/pagoObligatorio")
	public AjaxResponseBody pagoObligatorio() {

		AjaxResponseBody result = new AjaxResponseBody();
		
		Param param = new Param();
		param.put("cod","PAGO_OBL_MATR");
		 
		result.setResult(reglas_negocioDAO.getByParams(param));
		
		return result;
	}
}
