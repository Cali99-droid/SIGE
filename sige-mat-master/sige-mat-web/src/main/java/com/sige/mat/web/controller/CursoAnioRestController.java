package com.sige.mat.web.controller;


import com.tesla.frmk.sql.Param;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.sige.core.dao.cache.CacheManager;
import com.sige.mat.dao.CursoAnioDAO;
import com.sige.mat.dao.CursoSesionDAO;
import com.sige.mat.dao.PeriodoDAO;
import com.tesla.colegio.model.AreaAnio;
import com.tesla.colegio.model.CursoAnio;
import com.tesla.colegio.model.CursoSesion;
import com.tesla.colegio.model.DcnArea;
import com.tesla.colegio.model.Grad;

@RestController
@RequestMapping(value = "/api/cursoAnio")
public class CursoAnioRestController {
	
	@Autowired
	private CursoAnioDAO curso_anioDAO;
	
	@Autowired
	private CursoSesionDAO cursoSesionDAO;
	
	@Autowired
	private PeriodoDAO periodoDAO;

	@Autowired
	private CacheManager cacheManager;
	
	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(Integer id_niv, Integer id_gra, Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		
		result.setResult(curso_anioDAO.listaAnual(id_niv, id_gra, id_anio));
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	@Transactional
	public AjaxResponseBody grabar(CursoAnio curso_anio, CursoSesion curso_sesion, Integer id_cur_ses) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			
			//Obtener el nivel
			int id_niv=periodoDAO.getByParams(new Param("id",curso_anio.getId_per())).getId_niv();
			//Seteamos los valores
			curso_sesion.setId_niv(id_niv);
			curso_sesion.setId(id_cur_ses);
			//Inserto la sesi�n para el curso
			cursoSesionDAO.saveOrUpdate(curso_sesion);
			result.setResult( curso_anioDAO.saveOrUpdate(curso_anio) );
			cacheManager.update(CursoAnio.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/grabarCursosDC", method = RequestMethod.POST)
	public AjaxResponseBody grabarCursoAnioDC(String id_cua[], Integer id_cur[], CursoAnio cursoAnio) {

		AjaxResponseBody result = new AjaxResponseBody();
		try {

			//Obtenemos la lista de Cursos para el Area Anio
			List<CursoAnio> curso_anio = new ArrayList<CursoAnio>();
			//if (id_cur.length>0){
			if (cursoAnio!=null){
				Param param = new Param();
				param.put("id_caa", cursoAnio.getId_caa());
				curso_anio = curso_anioDAO.listByParams(param, null);
			}
			if(curso_anio.size()>0) {
				if (id_cur!=null){
					for (int i = 0; i < id_cur.length; i++) {
						if(!id_cua[i].equals("null")) {
							for (CursoAnio cursoAnio3 : curso_anio) {
								Integer id_cua_ex=cursoAnio3.getId();
								if(Integer.parseInt(id_cua[i])!=id_cua_ex) {
									//Actualizar los datos
								} else {
									//Inserto los datos
									CursoAnio cursoAnio2= new CursoAnio();
									if(id_cua[i]!=null) {
										cursoAnio2.setId(Integer.parseInt(id_cua[i]));
									}	
									cursoAnio2.setId_caa(cursoAnio.getId_caa());
									cursoAnio2.setId_cur(id_cur[i]);
									cursoAnio2.setEst("A");
									curso_anioDAO.saveOrUpdate(cursoAnio2);
								}
							}
						} else {
							CursoAnio cursoAnio2= new CursoAnio();
							cursoAnio2.setId_caa(cursoAnio.getId_caa());
							cursoAnio2.setId_cur(id_cur[i]);
							cursoAnio2.setEst("A");
							curso_anioDAO.saveOrUpdate(cursoAnio2);
						}
					}
				}

			} else {
				if (id_cur!=null){
					for (int i = 0; i < id_cur.length; i++) {
						//Inserto los datos
						CursoAnio cursoAnio2= new CursoAnio();
						cursoAnio2.setId_caa(cursoAnio.getId_caa());
						cursoAnio2.setId_cur(id_cur[i]);
						cursoAnio2.setEst("A");
						curso_anioDAO.saveOrUpdate(cursoAnio2);
					}
				}	
			}

			if (id_cua != null){//si hay instrumentos
				int listaIds[] = new int[ id_cua.length ];
			final List<String> cursosForm = Arrays.asList(id_cua);	
			
			if(curso_anio.size()>0) {
				int i=0;
				for (CursoAnio cursoAnio2 : curso_anio) {
					if(!cursosForm.contains(cursoAnio2.getId().toString())){
						curso_anioDAO.desactivarCursoAnio(cursoAnio2.getId());
					}
					i++;
				}
			}
			
			}	
			
			result.setResult(1);
			//cacheManager.update(AreaAnio.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			curso_anioDAO.delete(id);
			//cacheManager.update(CursoAnio.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( curso_anioDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/obtenerNroSes", method = RequestMethod.GET)
	public AjaxResponseBody detalle(Integer id_niv, Integer id_gra, Integer id_caa, Integer id_cur ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			if(id_cur!=null){
				Param param = new Param();
				param.put("id_niv", id_niv);
				param.put("id_gra", id_gra);
				param.put("id_caa", id_caa);
				param.put("id_cur", id_cur);
				result.setResult( cursoSesionDAO.getByParams(param));
			}			
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarAreas", method = RequestMethod.GET)
	public AjaxResponseBody listarArea( Integer id_niv, Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( curso_anioDAO.listaAreas(id_niv, id_anio) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarCursosAula", method = RequestMethod.GET)
	public AjaxResponseBody listarCursosAula( Integer id_anio, Integer id_au) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( curso_anioDAO.listaCursosAula(id_anio, id_au));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}

	@RequestMapping( value="/clonarAnio", method = RequestMethod.POST)
	public AjaxResponseBody clonarAnio(Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try { 
			result.setResult( curso_anioDAO.clonarAnio(id_anio) );
			cacheManager.update(CursoAnio.TABLA);
			
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	

}
