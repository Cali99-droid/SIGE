package com.sige.mat.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.sige.mat.dao.InscripcionCampusDAO;
import com.sige.mat.dao.UsuarioCampusDAO;
import com.sige.spring.service.CampusVirtualService;
import com.tesla.colegio.model.InscripcionCampus;


@RestController
@RequestMapping(value = "/api/inscripcionCampus")
public class InscripcionCampusRestController {
	
	@Autowired
	private InscripcionCampusDAO inscripcion_campusDAO;
	
	@Autowired
	private CampusVirtualService campusVirtualService;
	
	@Autowired
	private UsuarioCampusDAO usuarioCampusDAO;

	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(InscripcionCampus inscripcion_campus) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(inscripcion_campusDAO.listFullByParams( inscripcion_campus, new String[]{"cvic.id"}) );
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(InscripcionCampus inscripcion_campus, String id_alu[]) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			campusVirtualService.grabarInscripcion(id_alu, inscripcion_campus,inscripcion_campus.getId_anio());
			result.setResult(1);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			inscripcion_campusDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( inscripcion_campusDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/cambiarContrasenia", method = RequestMethod.POST)
	public AjaxResponseBody detalle(String usuario, String nuevo_pass) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			campusVirtualService.restablecerContrasenia(usuario, nuevo_pass);
			result.setResult(1);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/obtenerContraseniaAnterior", method = RequestMethod.GET)
	public AjaxResponseBody detalle(String usuario) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			
			result.setResult(usuarioCampusDAO.obtenerContraseniaUsuario(usuario));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	

	@RequestMapping( value="/listarInscritos", method = RequestMethod.GET)
	public AjaxResponseBody listarInscritos(Integer id_cga, Integer id_niv, Integer id_grad, Integer id_anio, String inscripcion) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( inscripcion_campusDAO.listarInscripcionesAulaVirtual(id_cga, id_niv, id_grad, id_anio, inscripcion));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/generarUsuarioGoogle/{id_alu}/{id_anio}", method = RequestMethod.POST)
	public AjaxResponseBody generarUsuarioGoogle(@PathVariable Integer id_alu, @PathVariable Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			campusVirtualService.generarUsuarioGogle(id_alu, id_anio);
			result.setResult(1);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/syncronizarUsuarios/{id_anio}", method = RequestMethod.POST)
	public AjaxResponseBody sincronizarUusarios(@PathVariable Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			campusVirtualService.sincronizarUsuarios(id_anio);
			result.setResult(1);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/matricularXbimestre/{id_anio}/{nro_pe}", method = RequestMethod.POST)
	public AjaxResponseBody sincronizarUusarios(@PathVariable Integer id_anio, @PathVariable Integer nro_pe) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			campusVirtualService.matriculacionFull(id_anio, nro_pe);
			result.setResult(1);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	/*@RequestMapping( value="/generarPass", method = RequestMethod.POST)
	public AjaxResponseBody detalle(String usuario, String nuevo_pass) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			campusVirtualService.restablecerContrasenia(usuario, nuevo_pass);
			result.setResult(1);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}*/	
}
