package com.sige.mat.web.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sige.core.security.response.UsuarioRs;
import com.sige.mat.dao.AnioDAO;
import com.tesla.colegio.model.Anio;
  

@RestController
@RequestMapping(value = "/api/usuario")
public class UsuarioController {

	@Autowired
	private AnioDAO anioDAO;
	
	@GetMapping
	public List<Anio> consultar() throws Exception {
 
		return anioDAO.list();
	}
}
