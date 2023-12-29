package com.sige.mat.web.controller;
//package com.sige.spring.rest.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sige.mat.dao.UsuarioRolDAO;
import com.tesla.colegio.model.UsuarioRol;
import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;

@RestController
@RequestMapping(value = "/api/usuarioRol")
public class UsuarioRolRestController {
	
	@Autowired
	private UsuarioRolDAO usuario_rolDAO;

	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista() {

		AjaxResponseBody result = new AjaxResponseBody();
		
		Param param = new Param();
		param.put("usr.est", "A");
		param.put("tra.est", "A");
		 
		result.setResult(usuario_rolDAO.listFullByParams( param, new String[]{"tra.ape_pat asc, tra.ape_mat asc"}) );
		
		return result;

	}
	
	@RequestMapping(value = "/listarUsuariosxContrato/")
	public AjaxResponseBody listarUsuariosxContrato(String tip_con) {

		AjaxResponseBody result = new AjaxResponseBody();
				 
		result.setResult(usuario_rolDAO.listarUsuarioRolxContrato(tip_con));
		
		return result;

	}


	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(UsuarioRol usuario_rol) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( usuario_rolDAO.saveOrUpdate(usuario_rol) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			usuario_rolDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			List<UsuarioRol> usuarioRolList=usuario_rolDAO.listFullByParams(new Param("uro.id", id), null);
			
			result.setResult( usuarioRolList.get(0) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
}
