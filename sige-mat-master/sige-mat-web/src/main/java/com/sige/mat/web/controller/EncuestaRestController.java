package com.sige.mat.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.sige.core.dao.cache.CacheManager;
import com.sige.mat.dao.AdjuntoDAO;
import com.sige.mat.dao.EncuestaAltDAO;
import com.sige.mat.dao.EncuestaAlumnoDAO;
import com.sige.mat.dao.EncuestaAlumnoDetDAO;
import com.sige.mat.dao.EncuestaDAO;
import com.sige.mat.dao.EncuestaPregDAO;
import com.sige.mat.dao.PregDependenciaDAO;
import com.tesla.colegio.model.Adjunto;
import com.tesla.colegio.model.Encuesta;
import com.tesla.colegio.model.EncuestaAlt;
import com.tesla.colegio.model.EncuestaAlumno;
import com.tesla.colegio.model.EncuestaAlumnoDet;
import com.tesla.colegio.model.EncuestaPreg;
import com.tesla.colegio.model.PregDependencia;


@RestController
@RequestMapping(value = "/api/encuesta")
public class EncuestaRestController {
	
	@Autowired
	private EncuestaDAO encuestaDAO;
	

	@Autowired
	private EncuestaPregDAO  encuestaPregDAO ;
	
	@Autowired
	private PregDependenciaDAO  pregDependenciaDAO ;
	
	@Autowired
	private EncuestaAltDAO  encuestaAltDAO ;
	
	@Autowired
	private EncuestaAlumnoDAO encuestaAlumnoDAO ;
	
	@Autowired
	private EncuestaAlumnoDetDAO encuestaAlumnDetDAO ;

	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(Adjunto adjunto, Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(encuestaDAO.listFullByParams(new Param("enc.id_anio",id_anio), new String[]{"enc.id"}) );
		
		return result;
	}
	

	@RequestMapping(value = "/listarEncuestaxGiro")
	public AjaxResponseBody listarEncuestaxGiro(Integer id_gir, Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(encuestaDAO.listarEncuestasxGiro(id_gir, id_anio) );
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(Encuesta encuesta) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( encuestaDAO.saveOrUpdate(encuesta) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value = "/grabarPreguntas", method = RequestMethod.POST)
	public AjaxResponseBody grabarPreguntas(Integer id_pre, Integer id_enc, Integer id_ctp, String pre, Integer ord, String dep, Integer id_pre_dep) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			EncuestaPreg encuestaPreg = new EncuestaPreg();
			encuestaPreg.setId(id_pre);
			encuestaPreg.setId_enc(id_enc);
			encuestaPreg.setId_ctp(id_ctp);
			encuestaPreg.setPre(pre);
			encuestaPreg.setOrd(ord);
			encuestaPreg.setDep(dep);
			encuestaPreg.setEst("A");
			Integer id_pre_conf=encuestaPregDAO.saveOrUpdate(encuestaPreg);
			//Busco si ha existido antes una dependencia
			Param param = new Param();
			param.put("id_enc_pre", id_pre_conf);
			PregDependencia pregDependenciaexiste = pregDependenciaDAO.getByParams(param);
			
			if(dep.equals("1")) {
				//
				PregDependencia pregDependencia = new PregDependencia();
				pregDependencia.setId_enc_pre(id_pre_conf);
				pregDependencia.setId_pre_dep(id_pre_dep);
				if(pregDependenciaexiste!=null) {
					pregDependencia.setId(pregDependenciaexiste.getId());
				}
			} else if(dep.equals("0")) {
				if(pregDependenciaexiste!=null) {
					pregDependenciaDAO.delete(pregDependenciaexiste.getId());
				}
				
			}
			result.setResult(id_pre_conf);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping(value = "/listarPreguntasAlt")
	public AjaxResponseBody listarPregAlt(Integer id_enc) {

		AjaxResponseBody result = new AjaxResponseBody();
		
		Param param = new Param();
		param.put("id_enc", id_enc);
		
		List<EncuestaPreg> lista_preguntas = encuestaPregDAO.listByParams(param, new String[]{"ord"});
		//List<EncuestaPreg> lista_preg_alt = new ArrayList<EncuestaPreg>();
		for (EncuestaPreg encuestaPreg : lista_preguntas) {
			//Lista de Alternativas
			Param param2 = new Param();
			param2.put("id_enc_pre", encuestaPreg.getId());
			List<EncuestaAlt> lista_alternativas=encuestaAltDAO.listByParams(param2, new String[]{"ord"});
			encuestaPreg.setEncuestaAlt(lista_alternativas);
			if(encuestaPreg.getDep().equals("1")) {
				Param param3 = new Param();
				param3.put("id_enc_pre", encuestaPreg.getId());
				PregDependencia pregDependencia = pregDependenciaDAO.getByParams(param3);
				encuestaPreg.setPregDependencia(pregDependencia);
			}
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("preguntas", lista_preguntas);
		//map.put("capacidades", capacidades);
		//map.put("alumnos", listAlumnos);
		
		result.setResult(lista_preguntas);
		 
		//result.setResult(encuestaDAO.listFullByParams(new Param("enc.id_anio",id_anio), new String[]{"enc.id"}) );
		
		return result;
	}
	
	@RequestMapping(value = "/listaPreguntasDependencia")
	public AjaxResponseBody listaPreguntasDependencia(Integer id_pre) {

		AjaxResponseBody result = new AjaxResponseBody();
		
		Param param = new Param();
		param.put("id_pre_dep", id_pre);
		
		List<PregDependencia> lista_preguntas_dependen = pregDependenciaDAO.listByParams(param, new String[]{"id_enc_pre"});
		//List<EncuestaPreg> lista_preg_alt = new ArrayList<EncuestaPreg>();
	
		
		result.setResult(lista_preguntas_dependen);
		 
		//result.setResult(encuestaDAO.listFullByParams(new Param("enc.id_anio",id_anio), new String[]{"enc.id"}) );
		
		return result;
	}
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			encuestaDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( encuestaDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@Transactional
	@RequestMapping( value="/grabarEncuestaAlumno", method = RequestMethod.POST)
	public AjaxResponseBody grabarEncuestaAlumno(EncuestaAlumno encuestaAlumno, Integer id_pre[], Integer id_alt[]) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			encuestaAlumno.setEst("A");
			Integer id_enc_alu=encuestaAlumnoDAO.saveOrUpdate(encuestaAlumno);
			//Integer listPre[]= new int [preg.length];
			//Integer listAlt[]= new int [alt.length];
			

			for(int i=0; i< id_pre.length; i++){
				//Insertamos la pregunta y respuesta
				if(id_alt[i]!=null) {
					EncuestaAlumnoDet encuestaAlumnoDet = new EncuestaAlumnoDet();
					encuestaAlumnoDet.setId_enc_pre(id_pre[i]);
					encuestaAlumnoDet.setId_enc_alt(id_alt[i]);
					encuestaAlumnoDet.setId_enc_alu(id_enc_alu);
					encuestaAlumnoDet.setEst("A");
					encuestaAlumnDetDAO.saveOrUpdate(encuestaAlumnoDet);
				}
				
				
			}
			result.setResult(id_enc_alu );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping(value = "/listarRespuestasxEncuesta")//aquiiiii
	public AjaxResponseBody listarRespuestasxEncuesta(Integer id_enc, Integer id_niv, Integer id_gra, Integer id_anio, Integer id_gir, String res, String resp_letras) {

		AjaxResponseBody result = new AjaxResponseBody();
	
		
		result.setResult(encuestaDAO.listRespuestasEncuesta(id_niv, id_gra, id_enc, id_anio, id_gir, res, resp_letras));
		 
		//result.setResult(encuestaDAO.listFullByParams(new Param("enc.id_anio",id_anio), new String[]{"enc.id"}) );
		
		return result;
	}
}
