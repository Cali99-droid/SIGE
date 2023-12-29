package com.sige.mat.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;
import com.sige.mat.dao.AnioDAO;
import com.sige.mat.dao.AreaCoordinadorDAO;
import com.sige.mat.dao.CursoSubtemaDAO;
import com.tesla.colegio.model.Anio;
import com.tesla.colegio.model.CursoSubtema;
import com.tesla.colegio.model.Usuario;


@RestController
@RequestMapping(value = "/api/cursoSubtema")
public class CursoSubtemaRestController {
	
	@Autowired
	private CursoSubtemaDAO curso_subtemaDAO;
	
	@Autowired
	private AreaCoordinadorDAO areaCoordinadorDAO;
	
	@Autowired
	AnioDAO anioDAO;
	
	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(CursoSubtema curso_subtema, Integer id_anio, Integer id_tra, Integer id_gra, Integer id_cur) {

		AjaxResponseBody result = new AjaxResponseBody();
		
		if(id_gra!=null && id_cur!=null){
			List<Row> listaCursos = areaCoordinadorDAO.listaCursos(id_anio, id_tra, 0, id_gra, id_cur);
			
			List<CursoSubtema> cursoSubtemaList = curso_subtemaDAO.listFullByParams( curso_subtema, new String[]{"ccs.id"});
			
			List<CursoSubtema> lista = new ArrayList<CursoSubtema>();
			
			for (CursoSubtema cursoSubtema : cursoSubtemaList) {
				
				//seleccionar los cursos que pertenecen al area
				
				for (Row row : listaCursos) {
					if (row.getInteger("id").equals(cursoSubtema.getId_cur()) && row.getInteger("aux1").equals(cursoSubtema.getId_niv()))
						lista.add(cursoSubtema);
				}
			}

			result.setResult( lista);
		} else
			result.setResult(new ArrayList<Row>());
  		
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(CursoSubtema curso_subtema) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( curso_subtemaDAO.saveOrUpdate(curso_subtema) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			curso_subtemaDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( curso_subtemaDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/clonar/{id_anio}", method = RequestMethod.POST)
	public AjaxResponseBody clonarConfiguracion( @PathVariable Integer id_anio, HttpSession session) {

		AjaxResponseBody result = new AjaxResponseBody();


		Anio anio=anioDAO.getByParams(new Param("id", id_anio));
		Integer anio_ant=Integer.parseInt(anio.getNom())-1;
		Integer id_anio_ant=anioDAO.getByParams(new Param("nom",anio_ant)).getId();
		try {
			result.setResult(curso_subtemaDAO.clonarConfiguraciones(id_anio,id_anio_ant));
			
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
}
