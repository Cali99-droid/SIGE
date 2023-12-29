package com.sige.mat.web.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;
import com.sige.mat.dao.CondicionDAO;
import com.tesla.colegio.model.Condicion;


@RestController
@RequestMapping(value = "/api/condicion")
public class CondicionRestController {
	
	@Autowired
	private CondicionDAO condicionDAO;

	/*@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(Condicion condicion) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(condicionDAO.listFullByParams( condicion, new String[]{"mc.id"}) );
		
		return result;
	}*/

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(Condicion condicion) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( condicionDAO.saveOrUpdate(condicion) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			condicionDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}/{tipo}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ,@PathVariable Integer tipo) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			Param param = new Param();
			param.put("mc.id", id);
			param.put("tip.id", tipo);
			List<Condicion> condicion=condicionDAO.listFullByParams(param, null);
			if(condicion.size()>0){
				result.setResult(condicion.get(0));
			} else{
				result.setResult(null);
			}
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping(value = "/obtenerBloqueo", method = RequestMethod.GET)
	public AjaxResponseBody editar(Integer id_mat, String tip) throws Exception {
		
		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			
			Row datos=condicionDAO.obtenerBloqueo(id_mat, tip);
			result.setResult(datos);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

		
	}

}
