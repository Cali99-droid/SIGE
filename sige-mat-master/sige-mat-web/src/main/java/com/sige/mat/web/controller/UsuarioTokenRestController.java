package com.sige.mat.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.sige.mat.dao.UsuarioTokenDAO;
import com.tesla.colegio.model.UsuarioToken;


@RestController
@RequestMapping(value = "/api/usuarioToken")
public class UsuarioTokenRestController {
	
	@Autowired
	private UsuarioTokenDAO usuario_tokenDAO;

	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(UsuarioToken usuario_token) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(usuario_tokenDAO.listFullByParams( usuario_token, new String[]{"$table.alias.id"}) );
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(UsuarioToken usuario_token) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( usuario_tokenDAO.saveOrUpdate(usuario_token) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			usuario_tokenDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( usuario_tokenDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
}
