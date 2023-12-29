package com.sige.mat.web.controller;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tesla.frmk.sql.Row;
import com.tesla.frmk.util.FechaUtil;
import com.tesla.frmk.common.exceptions.ServiceException;
import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.sige.core.dao.cache.CacheManagerUtil;
import com.sige.mat.dao.EvaPadreDAO;
import com.sige.mat.dao.EvaluacionDAO;
import com.sige.mat.dao.IndEvaDAO;
import com.sige.mat.dao.NotaDAO;
import com.sige.mat.dao.PerUniDAO;
import com.sige.spring.service.EvaluacionService;
import com.tesla.colegio.model.CursoAula;
import com.tesla.colegio.model.EvaPadre;
import com.tesla.colegio.model.Evaluacion;
import com.tesla.colegio.model.IndEva;
import com.tesla.colegio.model.Nota;


@RestController
@RequestMapping(value = "/api/evaluacion") 
public class EvaluacionRestController {
	final static Logger logger = Logger.getLogger(EvaluacionRestController.class);
	@Autowired
	private EvaluacionDAO evaluacionDAO;

	@Autowired
	private IndEvaDAO indEvaDAO;

	@Autowired
	private NotaDAO notaDAO;

	@Autowired
	private EvaPadreDAO evaPadreDAO;
	
	@Autowired
	private PerUniDAO perUniDAO;

	@Autowired
	private EvaluacionService evaluacionService;

	@Autowired
	private CacheManagerUtil cacheManagerUtil;

	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(Integer id_tra, Integer id_anio, Integer id_au, Integer id_cpu) {

		AjaxResponseBody result = new AjaxResponseBody();
		Param param = new Param();
		param.put("ne.est", "A");
		param.put("nep.id_tra",id_tra);
		param.put("cpu.id", id_cpu);
		param.put("ca.id", id_au);
		result.setResult(evaluacionDAO.listFullByParams( param, new String[]{"gra.id asc, ca.secc asc, ne.fec_fin asc"}) );
		
		return result;
	}
	
	@RequestMapping( value="/listarAulasxDocente", method = RequestMethod.GET)
	public AjaxResponseBody listarAulasxDocente( Integer id_tra, Integer id_cur, Integer id_gra, Integer id_anio, Integer id_niv, Integer id_nep) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			logger.debug(id_gra);
			result.setResult( evaluacionDAO.listarAulas(id_tra, id_cur, id_gra, id_anio, id_niv, id_nep));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}

	@RequestMapping( value="/grabarEvaluaciones",method = RequestMethod.POST)
	@Transactional
	public AjaxResponseBody grabarEvaluaciones(Integer id_ind[], Integer id_nep, Integer id_tra, Integer id_cur, Integer id_gra,
			Integer id_anio, Integer id_niv, Integer _id_cpu, Integer id_ses,String ins, String evi, Integer id_nte ) throws ServiceException {
		AjaxResponseBody result = new AjaxResponseBody();

		//parametrizacion de maximo numero de indicadores por nivel
		Row parametroNivel = cacheManagerUtil.getParametro(id_niv,"MAX_NRO_INDICADORES");
		int max = 0; 
		if(parametroNivel==null){
			//tomar la parametrizacion por institucion
			Row parametroInstitucion= cacheManagerUtil.getParametro("MAX_NRO_INDICADORES");
			
			if (parametroInstitucion!=null)
				max = parametroInstitucion.getInt("val");
		}else
			max = parametroNivel.getInt("val");
		
		if (max==0){
			result.setCode("422");
			result.setMsg("Es necesario configurar el nro maximo de indicadores, por favor ingrese al modulo de configuracion.");
			return result;
		}
			
		if (id_ind.length>max){
			result.setCode("422");
			result.setMsg("Nro de indicadores excede el maximo permitido [" + max + "]");
			return result;
		}
		
		evaluacionService.grabarEvaluaciones(id_ind, id_nep, id_tra, id_cur, id_gra, id_anio, id_niv, _id_cpu, id_ses,ins,evi, id_nte);
		
		return result;
		
	}

			
	@RequestMapping( value="/grabarEvaluacionesOLD",method = RequestMethod.POST)
	//INSERTA EVALUACIONES ( GRABAR POR PRIMERA VEZ)
	@Transactional
	public AjaxResponseBody grabarEvaluaciones(Evaluacion evaluacion, Integer id_eva[], Integer[] id_au, String fec_ini[], String fec_fin[],Integer[] id_ind,
			 Integer id_tra, Integer id_cur, Integer id_gra, Integer id_anio, Integer id_niv, Integer id_nep, Integer id_cpu) {
		//aca debe pasar no llego
		AjaxResponseBody result = new AjaxResponseBody();
		try{
			
			//validamos si tiene notas
			if (id_nep!=null){
				Param param = new Param();
				param.put("id_nep", id_nep);
				List<Evaluacion> evaluacionesActualues = evaluacionDAO.listByParams(param, null);
				for (Evaluacion evaluacion2 : evaluacionesActualues) {
					int id = evaluacion2.getId();
					logger.debug(id);
					
					Param param1= new Param();
					param1.put("id_ne", id);
					
					List<Nota> notas = notaDAO.listByParams(param1, null);
					
					if(notas.size()>0){
						result.setCode("500");
						result.setMsg("No se puede modificar la evaluacion si tiene notas, por favor elimine todas las notas de la evaluacion para proceder a modificar");
						return result;
					}
					
					//verificar si tiene notas ingresadas
				}
			}
			
			//validar fecha
			/*for(int f=0;f<=fec_fin.length-1;f++){
				Evaluacion eval = null;
				if(id_eva.length>0 && id_eva[f]!=null)
					eval = evaluacionDAO.get(id_eva[f]);
				if(eval==null || (eval!=null && !FechaUtil.toString(eval.getFec_fin()).equals(fec_fin[f]) )){
					int cant=Integer.parseInt(evaluacionDAO.validarFechaFin( FechaUtil.toDate(fec_fin[f])).get(0).getString("cantidad").toString());
					if(cant>=3){
						result.setCode("500");
						result.setMsg("Ya existe programada 3 evaluaciones para esta fecha "+fec_fin[f].toString()+", porfavor elija otro!!");
						return result;
					}
				}
			}*/
			
			//validar fecha
			if(fec_fin.length>0){
				Date fec_ini_per=perUniDAO.getByParams(new Param("id",id_cpu)).getFec_ini();
				Date fec_fin_per=perUniDAO.getByParams(new Param("id",id_cpu)).getFec_fin();
				for(int f=0;f<=fec_fin.length-1;f++){
					if(FechaUtil.toDate(fec_fin[f]).after(fec_ini_per) && FechaUtil.toDate(fec_fin[f]).before(fec_fin_per) || FechaUtil.toDate(fec_fin[f]).equals(fec_ini_per) ||  FechaUtil.toDate(fec_fin[f]).equals(fec_fin_per)){	
					} else {
						result.setCode("500");
						result.setMsg("No se puede programar uan fecha fuera del periodo acad�mico vigente, porfavor programe entre "+fec_ini_per+" y "+fec_fin_per);
						return result;
					}
				}	
			}
			
			//Obtenemos la evaluacion de la lista de aulas
			List<Evaluacion> evaluacionesGrabadasPreviamente = new ArrayList<Evaluacion>();
			if (id_nep!=null){
				Param param = new Param();
				param.put("id_nep", id_nep);
				evaluacionesGrabadasPreviamente = evaluacionDAO.listByParams(param, null);
			}
			
			List<Integer> listaPorDesactivar = new ArrayList<Integer>();
			
			//GRABAMOS EL PADRE (insert o update)
			EvaPadre evaPadre = new EvaPadre();
			evaPadre.setId(id_nep);	
			evaPadre.setId_tra(id_tra);
			evaPadre.setEst("A");
			int eva_pad=evaPadreDAO.saveOrUpdate(evaPadre);
			
			//grabamos evaluaciones
			List<Evaluacion> listEvaluacion = new ArrayList<>();
			for (int i=0; i<=id_au.length-1;i++){
					evaluacion.setId_cca(id_au[i]);
					evaluacion.setId_nep(eva_pad);
					evaluacion.setNump(id_cpu);
					//String fec_ini=
					if(fec_ini!=null){
						evaluacion.setFec_ini(FechaUtil.toDate(fec_ini[i]));	
					}
					for (Evaluacion row : evaluacionesGrabadasPreviamente) {
						boolean encontro = false;
						if(id_eva!=null){
							
							//existen id eva guardados en bd
							for (Integer id_eva_valor : id_eva) {
								if(id_eva_valor!=null && id_eva_valor.equals(row.getId())){
									encontro = true;
								}
							}
							
							if(!encontro){
								//se tendria q deshabilitar.
								listaPorDesactivar.add(row.getId());
							}
						}
						
						
					}
				
					Integer id=null;
					if(fec_fin.length>0)
					evaluacion.setFec_fin(FechaUtil.toDate(fec_fin[i]));
					evaluacion.setEst("A");
						if(id_eva.length>0)
							evaluacion.setId(id_eva[i]);
						id=evaluacionDAO.saveOrUpdate(evaluacion);
					
					Evaluacion evaluacionNuevo = new Evaluacion();
					evaluacionNuevo.setId(id);
					evaluacionNuevo.setId_cca(evaluacion.getId_cca());
					evaluacionNuevo.setId_nep(eva_pad);
					listEvaluacion.add(evaluacionNuevo);
				}
				if(listaPorDesactivar.size()>0)
				evaluacionDAO.deshabilitar(listaPorDesactivar);
			
			//grabamos indicadores, por cada evaluacion
				for (int j=0; j<=listEvaluacion.size()-1;j++){
					List<IndEva> listIndEva = new ArrayList<IndEva>();
					for (int i = 0; i <= id_ind.length-1; i++) {
						Param param = new Param();
						param.put("id_ne", listEvaluacion.get(j).getId());
						param.put("id_ind", id_ind[i]);
						IndEva indEva = indEvaDAO.getByParams(param);
						
						Integer id = null; 
						if(indEva==null){
							//insertar por primera vez
							indEva = new IndEva();
							indEva.setId_ne(listEvaluacion.get(j).getId());
							indEva.setId_ind(id_ind[i]);
							indEva.setEst("A");
							id = indEvaDAO.saveOrUpdate(indEva);
						}else if (indEva.getEst().equals("I")){
							//volver a habililiar
							indEva.setEst("A");
							id = indEvaDAO.saveOrUpdate(indEva);
						}else
							id = indEva.getId(); //esta previamente grabado y estado A
						
						indEva.setId(id);
						listIndEva.add(indEva);
					}
					//deshabilitar el resto
					indEvaDAO.deshabilitar(listIndEva, listEvaluacion.get(j).getId());
					
					listEvaluacion.get(j).setIndEva(listIndEva);
					
				}
				
				result.setResult(listEvaluacion);//bueno cada evaluacion tendr� el mismo padre
			
		}catch(Exception e){
			result.setException(e);
		}
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(Evaluacion evaluacion , Integer[] id_) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			//if(evaluacion.getId_nte().equals(new Integer(1))) {//examen
				Date fec_fin=evaluacion.getFec_fin();
				int cant=Integer.parseInt(evaluacionDAO.validarFechaFin(fec_fin).get(0).getString("cantidad").toString());
				if(cant>=3){
					result.setCode("500");
					result.setMsg("Ya existe programada 3 evaluaciones para esta fecha "+fec_fin+", porfavor elija otro!!");
				} else{
					int id=evaluacionDAO.saveOrUpdate(evaluacion);
					result.setResult( id);
				}
			//}
			 
			
		

			
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/actualizarEvaluaciones",method = RequestMethod.POST)
	/**
	 * 
	 * @param evaluacioneso si
	 * @param id_cca Arreglo id de aaulas de los checkbox seleccionados
	 * @param id_eva Arreglo id de evaluaciones de los checkbox seleccionados
	 * @return
	 */
	public AjaxResponseBody grabar(Evaluacion evaluacion,Integer[] id_au,Integer[] id_eva ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
				for(int i=0; i<id_eva.length; i++){
					evaluacion.setId(id_eva[i]);
					evaluacion.setId_cca(id_au[i]);
					int id=evaluacionDAO.updateEvas(evaluacion);
					result.setResult( id);
				}

		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			Evaluacion evaluacion = evaluacionDAO.get(id);
			Integer id_nep = evaluacion.getId_nep();//id del padre de evaluacion
			indEvaDAO.delete(id);
			evaluacionDAO.delete(id);
			
			//Eliminar padre si ya no tienen hijos
			Param param = new Param();
			param.put("id_nep", id_nep);
			List<Evaluacion> evaluaciones = evaluacionDAO.listByParams(param, null);
			if(evaluaciones.size()==0)
				evaPadreDAO.delete(id_nep);
			
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			//result.setResult( evaluacionDAO.get(id) );
			result.setResult( evaluacionDAO.getFull(id,new String[]{CursoAula.TABLA}) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarCursos", method = RequestMethod.GET)
	public AjaxResponseBody listarUnidades( Integer id_tra,Integer id_anio, Integer id_niv, Integer id_gra) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( evaluacionDAO.listarCursos(id_tra,id_anio, id_niv, id_gra));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarAulas", method = RequestMethod.GET)
	public AjaxResponseBody listarAsss( Integer id_tra, Integer id_cur, Integer id_gra, Integer id_anio, Integer id_niv, Integer id_nep) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			logger.debug(id_gra);
			result.setResult( evaluacionDAO.listarAulas(id_tra, id_cur, id_gra, id_anio, id_niv, id_nep));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarAulasProfesor", method = RequestMethod.GET)
	public AjaxResponseBody listarAulaProfesor( Integer id_tra, Integer id_gra, Integer id_anio, Integer id_niv, Integer id_nep) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			logger.debug(id_gra);
			result.setResult( evaluacionDAO.listarAulasProfesor(id_tra, id_gra, id_anio, id_niv));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarSubtemas", method = RequestMethod.GET)
	public AjaxResponseBody listarSubtemas( Integer id_uni) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( evaluacionDAO.listarSubtemas(id_uni));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarNiveles", method = RequestMethod.GET)
	public AjaxResponseBody listarNiveles( Integer id_tra, Integer id_anio, Integer id_gir) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( evaluacionDAO.listaNiveles(id_tra, id_anio, id_gir));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarGrados", method = RequestMethod.GET)
	public AjaxResponseBody listarGrados( Integer id_tra, Integer id_anio, Integer id_niv) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( evaluacionDAO.listaGrados(id_tra, id_anio, id_niv));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
}
