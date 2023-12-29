package com.sige.mat.web.controller;

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
import com.sige.core.dao.cache.CacheManager;
import com.sige.mat.dao.AnioDAO;
import com.sige.mat.dao.CursoDAO;
import com.sige.mat.dao.SubtemaDAO;
import com.sige.mat.dao.TemaDAO;
import com.tesla.colegio.model.Anio;
import com.tesla.colegio.model.Curso;
import com.tesla.colegio.model.Tema;
import com.tesla.colegio.model.Usuario;

@RestController
@RequestMapping(value = "/api/curso")
public class CursoRestController {
	
	@Autowired
	private CursoDAO cursoDAO;
	
	@Autowired
	private TemaDAO temaDAO;
	
	@Autowired
	private SubtemaDAO subtemaDAO;
	
	@Autowired
	private AnioDAO anioDAO;

	@Autowired
	private CacheManager cacheManager;

	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(Curso curso) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(cursoDAO.listFullByParams( curso, new String[]{"cur.nom asc"}) );
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(Curso curso) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( cursoDAO.saveOrUpdate(curso) );
			cacheManager.update(Curso.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			cursoDAO.delete(id);
			cacheManager.update(Curso.TABLA);
		} catch (Exception e) {
			/*result.setException(e);
			e.getMessage();*/
			result.setCode("500");
			result.setMsg(e.getMessage());
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( cursoDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping(value = "/porMatricula/{id_mat}")
	public AjaxResponseBody getCursosPorMatricula(@PathVariable Integer id_mat ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(cursoDAO.listarCursos(id_mat));
		
		return result;
	}
	
	@RequestMapping(value = "/listarCursosxNivel")
	public AjaxResponseBody getCursosNivel(Integer id_niv, Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(cursoDAO.listarCursosxNivel(id_niv, id_anio));
		
		return result;
	}
	
	@RequestMapping(value = "/clonar/{id_cur}/{id_anio}")
	public AjaxResponseBody clonarTemaSubtemaxCurso(@PathVariable Integer id_cur, @PathVariable Integer id_anio, HttpSession session) {

		AjaxResponseBody result = new AjaxResponseBody();
		Anio anio=anioDAO.getByParams(new Param("id", id_anio));
		Integer anio_ant=Integer.parseInt(anio.getNom())-1;
		Integer id_anio_ant=anioDAO.getByParams(new Param("nom",anio_ant)).getId();

		Param param = new Param();
		param.put("id_cur", id_cur);
		param.put("id_anio", id_anio_ant);
		List<Tema> temas = temaDAO.listFullByParams(param, null);
		
		for (Tema tema : temas) {
			//Inserto el Tema
			Tema tema_nuevo = new Tema();
			tema_nuevo.setId_anio(id_anio);
			tema_nuevo.setId_cur(tema.getId_cur());
			tema_nuevo.setId_niv(tema.getId_niv());
			tema_nuevo.setNom(tema.getNom());
			tema_nuevo.setOrd(tema.getOrd());
			tema_nuevo.setEst("A");
			int id_tem_nue=temaDAO.saveOrUpdate(tema_nuevo);
			//Inserto los subtemas
			temaDAO.insertarSubtemaxTema(id_tem_nue, tema.getId() );
		}
		
		return result;
	}
	
}
