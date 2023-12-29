package com.sige.mat.web.controller;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sige.mat.dao.AlumnoDAO;
import com.sige.mat.dao.CriterioNotaDAO;
import com.sige.mat.dao.EvaluacionDAO;
import com.sige.mat.dao.EvaluacionVacDAO;
import com.sige.mat.dao.LecturaBarrasDAO;
import com.sige.mat.dao.MatrVacanteDAO;
import com.sige.mat.dao.ParametroDAO;
import com.sige.spring.service.CondicionService;
import com.sige.spring.service.VacanteService;
import com.tesla.colegio.model.Alumno;
import com.tesla.colegio.model.EvaluacionVac;
import com.tesla.colegio.model.LecturaBarras;
import com.tesla.colegio.model.MatrVacante;
import com.tesla.colegio.model.Parametro;
import com.tesla.colegio.model.bean.CondicionBean;
import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.util.JsonUtil;


@RestController
@RequestMapping(value = "/api/matrVacante")
public class MatrVacanteRestController {

	@Autowired
	private VacanteService vacanteService;
	
	@Autowired
	private CondicionService condicionService;
	
	@Autowired
	private AlumnoDAO alumnoDAO;
	
	@Autowired
	private ParametroDAO parametroDAO;
	
	@Autowired
	private EvaluacionVacDAO evaluacionVacDAO;
	
	@Autowired
	private MatrVacanteDAO matrVacanteDAO;
	
	@Autowired
	private CriterioNotaDAO criterioNotaDAO;
	
	@RequestMapping( value="/listarNumeroVacantes")
	public AjaxResponseBody listarNumeroVacantes(Integer id_per, Integer id_grad, Integer id_eva ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			Integer vacantes= vacanteService.getNroVacantesMatriculaVacante(id_per, id_grad, id_eva);
			Param map = new Param();
			map.put("vacantes", vacantes);
			map.put("anio_setup", "N");
			result.setResult(map);
			
			 /*
	         * Todo esto era mi logica antigua
	         * Calendar c1 = Calendar.getInstance();
			int anio = c1.get(Calendar.YEAR);
			int id_anio_eva=periodoDAO.getByParams(new Param("id",id_per)).getId_anio();
			int anio_eva=Integer.parseInt(anioDAO.getByParams(new Param("id",id_anio_eva)).getNom());
			int anio_setup=Integer.parseInt(parametroDAO.getByParams(new Param("nom","ANIO_SETUP")).getVal());
			int cant_matriculas_anteriores=matriculaDAO.getMatriculadosporA�o(id_anio_eva-1);
			int cant_matriculas_vig=matriculaDAO.getMatriculadosporA�o(id_anio_eva);
			Map<String, Object> map = new HashMap<String,Object>();
			
			if(anio==anio_setup  && cant_matriculas_anteriores==0 ){
				//Funcionalidad a�io setup
				List<Map<String, Object>> postulantes = matr_vacanteDAO.ListaVacantes(id_per,id_eva,id_grad);	
				List<Map<String, Object>> matriculados = matr_vacanteDAO.Matriculados(id_grad, id_eva);
				Integer vacantes=0;
		    	if(postulantes.size()>0 && matriculados.size()>0){
		    		vacantes=Integer.parseInt(postulantes.get(0).get("post").toString())-Integer.parseInt(matriculados.get(0).get("matr_vac").toString());
		    		map.put("vacantes", vacantes);
		    	//map.put("postulantes", postulantes.get(0).get("post"));
		    	//map.put("matriculados", matriculados.get(0).get("matr_vac"));
		    	}
		    	if(matriculados.size()==0){
		    		vacantes=Integer.parseInt(postulantes.get(0).get("post").toString())-0;
		    		map.put("vacantes", vacantes);
		    		//map.put("postulantes", postulantes.get(0).get("vacantes"));
		        	//map.put("matriculados",0);
		    	}
		    	map.put("anio_setup", "Y");
		    	map.put("postulantes", postulantes);
		    	//System.out.println(map);
		    	
			} else if(anio==anio_eva && cant_matriculas_vig>0){
				//Funcionalidad para matricularse en una evaluacion del mismo a�o pero ya hubo matriculas del periodo
				Periodo periodoVacante = periodoDAO.get(id_per);
				Param param = new Param();
				param.put("id_anio", periodoVacante.getId_anio());
				param.put("id_srv", periodoVacante.getId_srv());
				param.put("id_tpe", Constante.TIPO_PERIODO_ESCOLAR);
				
				Periodo periodoEscolar= periodoDAO.getByParams(param);
						
				Integer total_matriculados = matriculaDAO.getTotalMatriculadosGrado(periodoEscolar.getId(),id_grad);//Matriculados en el grado del periodo escolar vigente
				Integer reservaCapaidad[] = reservaDAO.getCapacidadGrado(periodoEscolar.getId(),id_grad);//
				Integer total_capacidad = reservaCapaidad[0];//Se tiene el total de capacidad de aulas por grado
				Integer total_reserva = reservaCapaidad[1];//Se tiene las reservas q no fueron matroiculadas y q estan dentro de la fecha limite por grado
				Integer matricula_vacante = matriculaVacDAO.matriculasVacante(id_eva, id_grad, periodoEscolar.getId());//Las matriculas vacante q aun no tienen reserva y q no esten no apto
				Integer vacantes=total_capacidad-total_matriculados-total_reserva-matricula_vacante;
				map.put("vacantes", vacantes);
				map.put("anio_setup", "N");
			} else if(anio_eva>=anio && cant_matriculas_anteriores>0){
				//Funcionalidad para matricularse al pr�ximo a�o y ya hubo matriculas para el a�o q se desea postular
				Periodo periodoVacante = periodoDAO.get(id_per);
				Param param = new Param();
				param.put("id_anio", periodoVacante.getId_anio());
				param.put("id_srv", periodoVacante.getId_srv());
				param.put("id_tpe", Constante.TIPO_PERIODO_ESCOLAR);
		
				Periodo periodoEscolar= periodoDAO.getByParams(param);
				Integer reservaCapaidad[] = reservaDAO.getCapacidadGrado(periodoEscolar.getId(),id_grad);//
				Integer total_capacidad = reservaCapaidad[0];//Se tiene el total de capacidad de aulas por grado
				Integer total_reserva = reservaCapaidad[1];//Se tiene las reservas q no fueron matroiculadas y q estan dentro de la fecha limite por grado
				Integer matriculados_anio_anterior= matriculaDAO.getTotalMatriculadosGradoAnterior((id_anio_eva-1), (id_grad-1),periodoVacante.getId_suc());
				Integer repitentes = matriculaDAO.getRepitentes((id_anio_eva-1), id_grad,periodoVacante.getId_suc());//Los Repitentes
				Integer matricula_vacante = matriculaVacDAO.matriculasVacante(id_eva, id_grad, periodoEscolar.getId());//Las matriculas vacante q aun no tienen reserva y q no esten no apto
				
				Integer vacantes = total_capacidad-matriculados_anio_anterior-total_reserva-repitentes-matricula_vacante;
				map.put("vacantes", vacantes);
				map.put("anio_setup", "N");
		    	
			}
			response.setContentType("application/json");
	        response.getWriter().write(JsonUtil.toJson(map));
	         */
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarMatriculaVacante", method = RequestMethod.GET)
	public AjaxResponseBody listarTodosAlumnos(Integer id_alu) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult(matrVacanteDAO.listarMatriculasVancante(id_alu));
		} catch (Exception e) {
			result.setException(e);
		}
		return result;
	}
	
	@RequestMapping( value="/listarCantidadxColegio", method = RequestMethod.GET)
	public AjaxResponseBody listarColegioProcedencia(Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult(matrVacanteDAO.listarColegioProcedencia(id_anio));
		} catch (Exception e) {
			result.setException(e);
		}
		return result;
	}
	
	@RequestMapping(value = "/MatrVacanteGrabar", method = RequestMethod.POST)
	public void grabar( MatrVacante matrVacante,
			 HttpServletResponse response) throws IOException {
		try{
		EvaluacionVac periodo= evaluacionVacDAO.getByParams(new Param("id",matrVacante.getId_eva()));
		Integer id_per=periodo.getId_per();

			/*List<Map<String, Object>> desaprobado=matrVacanteDAO.estaDesaprobadoPisc(matrVacante.getId_alu(), id_per);
			if(desaprobado.size()>0){
			String desa=(desaprobado.get(0).get("apto")).toString();
			if(desa.equals("N")){
				Map<String, Object> map = new HashMap<String,Object>();
				map.put("error", "El alumno ya no puede postular por una vacante en este periodo.");
				//System.out.println(map);
				response.setContentType("application/json");
		        response.getWriter().write(JsonUtil.toJson(map));
		        return;
			}
			}*/
		
			if (matrVacanteDAO.estaMatriculado(matrVacante.getId_alu(), matrVacante.getId(), matrVacante.getId_eva())){
	
				Map<String, Object> map = new HashMap<String,Object>();
				map.put("error", "Alumno ya se encuentra matriculado para esa evaluacion");
				//System.out.println(map);
				response.setContentType("application/json");
		        response.getWriter().write(JsonUtil.toJson(map));
		        return;
			} 
				
				int id_matr = matrVacanteDAO.saveOrUpdate(matrVacante);	
				Map<String, Object> map = new HashMap<String,Object>();
				map.put("id", id_matr);
				map.put("id_alu", matrVacante.getId_alu());
				//System.out.println(map);
				response.setContentType("application/json");
		        response.getWriter().write(JsonUtil.toJson(map));	
		}catch (Exception e) {
			e.printStackTrace();
			throw new IOException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/validarCondicion/{id_alu}")
	public AjaxResponseBody validarCondicion(HttpServletResponse response,@PathVariable Integer id_alu) throws IOException{
		AjaxResponseBody result = new AjaxResponseBody();	
		
		/*********************************************************************************/
		/** VALIDACION DE CONDICION DE ALUMNOS ANTIGUOS */
		/*********************************************************************************/
		//Map<String, Object> map = new HashMap<String,Object>();
		Alumno alumno= alumnoDAO.getByParams(new Param("id",id_alu));
		//Ya no se tiene levantado ese servidor
		/*String condicionAlumnoArr[] = validarCondicion(alumno.getNro_doc());
		if (condicionAlumnoArr != null) {
			String condicion = condicionAlumnoArr[0];
			String descripcionCondicion = condicionAlumnoArr[1];
			if (!"1".equals(condicion)){
				//ES UNA ADVERTENCIA
				/*map.put("error", "EL ALUMNNO NO PUEDE POSTULAR: " + descripcionCondicion);
				response.setContentType("application/json");
		        response.getWriter().write(JsonUtil.toJson(map));
		        result.setResult(map);*/
		/*        result.setCode("201");
				result.setMsg( "EL ALUMNNO NO PUEDE POSTULAR: " + descripcionCondicion);
		        return result;
			}
		}*/		
		List<CondicionBean> condiciones = condicionService.mensajeCondicionalumno(id_alu);
		int nroMatriculasCondiconales = 0;
		String matriculaCondicionada ="";

		if(condiciones!=null)
		for (CondicionBean condicionBean : condiciones) {
			
			if (condicionBean.getTipo().equals("M")){
				nroMatriculasCondiconales ++;
				if (matriculaCondicionada.length()==0)
					matriculaCondicionada = matriculaCondicionada  + condicionBean.getObs();
				else 
					matriculaCondicionada = matriculaCondicionada  + ", " + condicionBean.getObs();
				
			}
			
			if (condicionBean.getTipo().equals("B")){
				/*map.put("error","ATENCION: " + condicionBean.getObs());
				response.setContentType("application/json");
		        response.getWriter().write(JsonUtil.toJson(map));
		        result.setResult(map);*/
		        result.setCode("201");
				result.setMsg("ATENCION: " + condicionBean.getObs());
		        return result;
			}
			
			if (condicionBean.getTipo().equals("V")){
				/*map.put("error","ATENCION,\n El alumno pierde vacante:" + condicionBean.getObs());
				response.setContentType("application/json");
		        response.getWriter().write(JsonUtil.toJson(map));
		        result.setResult(map);*/
		        result.setCode("201");
				result.setMsg("ATENCION,\n El alumno pierde vacante:" + condicionBean.getObs());
		        return result;
			}
			
			
		}
		
		if (nroMatriculasCondiconales >1){
			/*map.put("error","ATENCION,\nEl alumno tiene dos matriculas condicionadas:" + matriculaCondicionada);
			response.setContentType("application/json");
	        response.getWriter().write(JsonUtil.toJson(map));
	        result.setResult(map);*/
	        result.setCode("201");
			result.setMsg("ATENCION,\nEl alumno tiene dos matriculas condicionadas:" + matriculaCondicionada);
	        return result;
		}
		
		return result;

	}
	
	private String[] validarCondicion(String dni) {

		List<Parametro> parametros = parametroDAO.list();
		String servicio = null;
		for (Parametro parametro : parametros) {
			if (parametro.getNom().equals("URL_SERVIDOR_EXTERNO"))
				servicio =parametro.getVal(); 
		}
		
		String surl = servicio + "?mod=condicionAlumno&alu_dni=" + dni;

		
		try {
			URL url = new URL(surl);

			InputStream is = url.openConnection().getInputStream();

			BufferedReader reader = new BufferedReader(new InputStreamReader(is));

			String line = null;
			String mensaje = null;
			while ((line = reader.readLine()) != null) {
				
				mensaje = line;
			}

			if (mensaje == null || "".equals(mensaje.trim()))
				return null;
			else
				return mensaje.split("\\|");

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return null;
		}

	}
	
	@RequestMapping(value="/validarCondicionDesaprobado/{id_alu}/{id_anio}")
	public AjaxResponseBody validarCondicionDesaprobado(HttpServletResponse response,@PathVariable Integer id_alu, @PathVariable Integer id_anio) throws IOException{
		AjaxResponseBody result = new AjaxResponseBody();		
		//Map<String, Object> map = new HashMap<String,Object>();
		try{
			Alumno alumno= alumnoDAO.getByParams(new Param("id",id_alu));
			String mensaje=validarSiDesaproboAntes(alumno.getNro_doc(), id_anio);
			if(mensaje!=null){
				result.setCode("201");
				result.setMsg("ATENCION: " + mensaje);
			} else{
				result.setResult(null);
			}			
			return result;
		}catch (Exception e) {
			e.printStackTrace();
			result.setCode("500");
			result.setMsg("Error en el sistema, consulte con el administrador del sistema");
			return result;
		}
	}
	
	public String validarSiDesaproboAntes(String dni, Integer id_anio){
		//EvaluacionVac periodo= evaluacionVacDAO.getByParams(new Param("id",matrVacante.getId_eva()));
		//Integer id_per=periodo.getId_per();
		String mensaje=null;
			List<Map<String, Object>> desaprobado=matrVacanteDAO.estaDesaprobadoPisc(dni, id_anio);
			if(desaprobado.size()>0){
			String desa=(desaprobado.get(0).get("apto")).toString();
			if(desa.equals("N")){
				//Map<String, Object> map = new HashMap<String,Object>();
				//map.put("error", "El alumno ya no puede postular por una vacante en este periodo.");
				mensaje = "El alumno ya no puede postular por una vacante en este periodo.";
				/*//System.out.println(map);
				response.setContentType("application/json");
		        response.getWriter().write(JsonUtil.toJson(map));
		        return;*/
			}
			}
			return mensaje;
	}
	
	@RequestMapping( value="/editarInscripcion", method = RequestMethod.GET)
	public AjaxResponseBody detalle(Integer id_ins) {

		AjaxResponseBody result = new AjaxResponseBody();		 
		try {
			result.setResult( matrVacanteDAO.obtenerDatosInscripcion(id_ins));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping(value="/GrabarCondFinal",method = RequestMethod.GET)
	public void grabarCondicionFinal(String res, Integer id_mat) throws IOException{
		matrVacanteDAO.condFinal(res, id_mat);
		
	}
	
	@RequestMapping( value="/listarAlumnosEvaPsi", method = RequestMethod.GET)
	public AjaxResponseBody listarAlumnosEvaPsi(Integer id_anio, Integer id_eva, Integer id_grad) {

		AjaxResponseBody result = new AjaxResponseBody();		 
		try {
			//result.setResult( matrVacanteDAO.obtenerDatosInscripcion(id_ins));
			if (criterioNotaDAO.existeExamen(id_anio,id_eva)) {
				result.setResult(criterioNotaDAO.Alumnos_PrimSec(id_eva,null, id_grad));
			} else {
				result.setResult(criterioNotaDAO.Alumnos_Inicial(id_eva,null, id_grad));
			}
		} catch (Exception e) {
			result.setException(e);
		}
		return result;
	}	
	
}
