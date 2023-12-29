package com.sige.mat.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sige.mat.dao.AreaCoordinadorDAO;
import com.tesla.colegio.model.AreaCoordinador;
import com.tesla.frmk.common.exceptions.ControllerException;
import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Row;

@RestController
@RequestMapping(value = "/api/cursoCoordinador")
public class CursoCoordinadorRestController {

	@Autowired
	private AreaCoordinadorDAO area_coordinadorDAO;

	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(AreaCoordinador area_coordinador) {

		AjaxResponseBody result = new AjaxResponseBody();

		result.setResult(area_coordinadorDAO.listFullByParams(area_coordinador, new String[] { "cac.id" }));

		return result;
	}

	@RequestMapping(method = RequestMethod.POST)
	public AjaxResponseBody grabar(AreaCoordinador area_coordinador) {

		AjaxResponseBody result = new AjaxResponseBody();

		try {
			
			if (area_coordinador.getId_tra()==null)
				throw new ControllerException("El coordinador es obligatorio");
			
			result.setResult(area_coordinadorDAO.saveOrUpdate(area_coordinador));
		} catch (Exception e) {
			result.setException(e);
		}

		return result;

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id) {

		AjaxResponseBody result = new AjaxResponseBody();

		try {
			area_coordinadorDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}

		return result;

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id) {

		AjaxResponseBody result = new AjaxResponseBody();

		try {
			result.setResult(area_coordinadorDAO.get(id));
		} catch (Exception e) {
			result.setException(e);
		}

		return result;

	}


	
	@RequestMapping( value="/listaCursoxNivel/{id_anio}", method = RequestMethod.GET)
	public AjaxResponseBody listarCursoxNivel(@PathVariable Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			 List<Row> cursosCoordinadores = area_coordinadorDAO.listCursosCoordinadores(id_anio) ;
			
			result.setResult(cursosCoordinadores );
			
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}

	@RequestMapping( value="/coordinadoresDisponibles", method = RequestMethod.GET)
	public AjaxResponseBody listarCoordinadores(Integer id_anio,Integer id_niv,Integer id_cur ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( area_coordinadorDAO.coordinadoresDisponibles(id_anio, id_niv, id_cur));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarNiveles/{id_tra}", method = RequestMethod.GET)
	public AjaxResponseBody listarNiveles(@PathVariable Integer id_tra) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( area_coordinadorDAO.listaNiveles(id_tra) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	


	@RequestMapping( value="/listarGrados", method = RequestMethod.GET)
	public AjaxResponseBody listarGrados(Integer id_anio, Integer id_tra, Integer id_niv, Integer id_gra, Integer id_cur) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( area_coordinadorDAO.listaGrados(id_anio, id_tra, id_niv) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarCursos", method = RequestMethod.GET)
	public AjaxResponseBody listarCursos(Integer id_anio, Integer id_tra, Integer id_niv, Integer id_gra) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			if(id_gra!=null){
				List<Row> cursos = area_coordinadorDAO.listaCursos(id_anio, id_tra, id_niv, id_gra, 0);
				if(cursos.size()>0)
					result.setResult(cursos);
				else
					result.setResult(null);
			}
			
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}

	


}
