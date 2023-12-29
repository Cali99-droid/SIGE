package com.sige.mat.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sige.mat.dao.AnioDAO;
import com.sige.mat.dao.MatriculaDAO;
import com.sige.mat.dao.SeccionSugeridaDAO;
import com.tesla.colegio.model.Anio;
import com.tesla.colegio.model.Matricula;
import com.tesla.colegio.model.SeccionSugerida;
import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;

@RestController
@RequestMapping(value = "/api/seccionSugerida")
public class SeccionSugeridaRestController {

	@Autowired
	private SeccionSugeridaDAO seccionSugeridDAO;

	@Autowired
	private AnioDAO anioDAO;
	
	@Autowired
	private MatriculaDAO matriculaDAO;

	
	/**
	 * Listar la seccion sugerida para el a�o siguiente
	 * @param id_au
	 * @return
	 */
	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(Integer id_au,Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		
		Anio anio = anioDAO.get(id_anio);
		Param param = new Param();
		param.put("nom", Integer.parseInt( anio.getNom()) + 1);
		
		Anio anioSigueinte = anioDAO.getByParams(param);
		
		if (anioSigueinte==null){
			result.setCode("201");
			result.setMsg("No existe a�o posterior configurado al " + anio.getNom());
			return result;
		}
		 
		List<Row> alumnos =seccionSugeridDAO.listaralumnosYseccionSugerida(id_au,id_anio, anioSigueinte.getId());
		int con_sugerencia = 0;
		for (Row row : alumnos) {
			if (row.get("sug_secc")!=null)
				con_sugerencia++;
		}
		
		Map<String,Object> resultado = new HashMap<String, Object>();
		resultado.put("alumnos", alumnos);
		resultado.put("con_sugerencia", con_sugerencia);
		
		result.setResult(resultado );
		
		return result;
	}
	
	@RequestMapping(value = "/generar", method = RequestMethod.POST)
	public AjaxResponseBody generarAulaSugerida(Integer id_au,Integer id_anio) throws Exception {

		AjaxResponseBody result = new AjaxResponseBody();
		

		Anio anio = anioDAO.get(id_anio);
		Param param = new Param();
		param.put("nom", Integer.parseInt( anio.getNom()) + 1);
		
		Anio anioSigueinte = anioDAO.getByParams(param);
		
		seccionSugeridDAO.actualizarAlumnosSeccionSugerida(id_au, id_anio, anioSigueinte.getId());
		
		return result;
	}

	@RequestMapping(value = "/grabar", method = RequestMethod.POST)
	public AjaxResponseBody grabar(Integer id , Integer id_mat, Integer id_au, Integer id_anio, Integer id_au_sug) throws Exception {

		AjaxResponseBody result = new AjaxResponseBody();
		
		Anio anio = anioDAO.get(id_anio);
		Param param = new Param();
		param.put("nom", Integer.parseInt( anio.getNom()) + 1);
		
		Anio anioSigueinte = anioDAO.getByParams(param);
		
		SeccionSugerida seccionSugerida = new SeccionSugerida();
		seccionSugerida.setId (id);
		seccionSugerida.setId_mat(id_mat);
		seccionSugerida.setId_anio(anioSigueinte.getId());
		
		if (id_au_sug==null || id_au_sug.intValue()==-1)
			seccionSugerida.setId_au_nue(null);
		else
			seccionSugerida.setId_au_nue(id_au_sug);
		
		seccionSugerida.setEst("A");
		
		Integer msg_id = seccionSugeridDAO.saveOrUpdate(seccionSugerida);
		result.setResult(msg_id);
		return result;
	}
	
	@RequestMapping(value = "/grabarLocal", method = RequestMethod.POST)
	public AjaxResponseBody grabarLocal(Integer id , Integer id_mat, Integer id_anio, Integer id_suc_sug) throws Exception {

		AjaxResponseBody result = new AjaxResponseBody();
		
		Matricula matricula = matriculaDAO.get(id_mat);
		Integer id_sit = matricula.getId_sit();
		Integer id_gra_ant=matricula.getId_gra();
		Integer id_gra_nue = null;
		if(id_sit==null) {
			/*result.setCode("201");
			result.setMsg("El alumno no presenta una Situación Final Académica, no se puede proceder ");
			return result;*/
			id_gra_nue=id_gra_ant+1;
		} else {
			if(id_sit.equals(1) || id_sit.equals(0)) { //Aprobado
				id_gra_nue=id_gra_ant+1;
			} else if(id_sit.equals(3)) {
				id_gra_nue=id_gra_ant;
			}
		}
		Anio anio = anioDAO.get(id_anio);
		Param param = new Param();
		param.put("nom", Integer.parseInt( anio.getNom()) + 1);
		
		Anio anioSigueinte = anioDAO.getByParams(param);
		
		SeccionSugerida seccionSugerida = new SeccionSugerida();
		seccionSugerida.setId (id);
		seccionSugerida.setId_mat(id_mat);
		seccionSugerida.setId_anio(anioSigueinte.getId());
		
		if (id_suc_sug==null || id_suc_sug.intValue()==-1)
			seccionSugerida.setId_suc_nue(null);
		else
			seccionSugerida.setId_suc_nue(id_suc_sug);
		
		seccionSugerida.setEst("A");
		seccionSugerida.setId_gra_nue(id_gra_nue);
		
		Integer msg_id = seccionSugeridDAO.saveOrUpdate(seccionSugerida);
		result.setResult(msg_id);
		return result;
	}


}
