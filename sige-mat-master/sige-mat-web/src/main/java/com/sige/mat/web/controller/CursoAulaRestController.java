package com.sige.mat.web.controller;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;
import com.tesla.frmk.util.WordToPDFUtil;

import net.sf.jasperreports.engine.json.expression.filter.evaluation.NotFilterExpressionEvaluator;

import com.sige.core.dao.cache.CacheManager;
import com.sige.mat.dao.CursoAulaDAO;
import com.sige.mat.dao.EvaluacionDAO;
import com.sige.mat.dao.IndEvaDAO;
import com.sige.mat.dao.ProgAnualDAO;
import com.sige.spring.service.ProgramacionAnualService;
import com.sige.web.security.TokenSeguridad;
import com.tesla.colegio.model.AreaAnio;
import com.tesla.colegio.model.CursoAula;
import com.tesla.colegio.model.Evaluacion;
import com.tesla.colegio.model.IndEva;
import com.tesla.colegio.model.ProgAnual;


@RestController
@RequestMapping(value = "/api/cursoAula")
public class CursoAulaRestController {
	
	@Autowired
	private ProgAnualDAO progAnualDAO;

	@Autowired
	private CursoAulaDAO curso_aulaDAO;
	
	@Autowired
	private EvaluacionDAO evaluacionDAO;
	
	@Autowired
	private IndEvaDAO indEvaDAO;

	@Autowired
	private CacheManager cacheManager;

	@Autowired
	private ProgramacionAnualService programacionAnualService;
	
	@Autowired
	private TokenSeguridad tokenSeguridad;

	
	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(Integer id_niv, Integer id_gra, Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		
		result.setResult(curso_aulaDAO.listaAnual(id_niv, id_gra, id_anio));
		
		return result;
	}
	
	@RequestMapping( value="/listaCursosAulaxNivelAnio", method = RequestMethod.GET)
	public AjaxResponseBody listaCursosAulaxNivelAnio(Integer id_anio, Integer id_niv, Integer id_tpe, Integer id_gir, Integer id_grad, Integer id_au, Integer id_tra) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			List<Row> aulas_doce_correcto = new ArrayList<>();
			List<Row> aulas_docente=curso_aulaDAO.listaCursosAulaxNivelAnio(id_anio, id_niv, id_tpe, id_gir, id_grad, id_au, id_tra);
			for (Row row : aulas_docente) {
				if(row.getInteger("id_tra")!=null && row.getInteger("id_cua")==null && row.getInteger("id_caul")!=null) {
					Row aula = new Row();
					aula.put("aula", row.getString("aula"));
					aula.put("area", row.getString("area"));
					aula.put("curso", row.getString("curso"));
					aula.put("id_tra", row.getInteger("id_tra"));
					aula.put("id_caa", row.getInteger("id_caa"));
					aula.put("id_cua", row.getInteger("id_cua"));
					aula.put("id_caul", row.getInteger("id_caul"));
					aula.put("id_au", row.getInteger("id_au"));
					aula.put("id_cur", row.getInteger("id_cur"));
					aulas_doce_correcto.add(aula);
				} else if(row.getInteger("id_tra")==null && row.getInteger("id_caul")==null) {
					//Busco si no existe en el arreglo
					boolean encontrado = false;
					for (int i = 0; i < aulas_doce_correcto.size(); i++) {
						if(aulas_doce_correcto.get(i).getInteger("id_au").equals(row.getInteger("id_au")) && aulas_doce_correcto.get(i).getInteger("id_cur")!=null  && row.getInteger("id_caul")==null) {
							if(aulas_doce_correcto.get(i).getInteger("id_cur").equals(row.getInteger("id_cur"))) {
								encontrado =true;
								break;
							}
							
						} else if(aulas_doce_correcto.get(i).getInteger("id_au").equals(row.getInteger("id_au")) && aulas_doce_correcto.get(i).getInteger("id_cur")!=null  && row.getInteger("id_caul")!=null) {
							if(aulas_doce_correcto.get(i).getInteger("id_cur").equals(row.getInteger("id_cur")) &&  aulas_doce_correcto.get(i).getInteger("id_caul")==null) {
								//&& row.getInteger("id_cua_cual")!=null
								encontrado =true;
								aulas_doce_correcto.get(i).put("id_tra",row.getInteger("id_tra"));
								aulas_doce_correcto.get(i).put("id_caul",row.getInteger("id_caul"));
								break;
							}
								
						}
						
					}
					
					if(!encontrado) {
						Row aula = new Row();
						aula.put("aula", row.getString("aula"));
						aula.put("area", row.getString("area"));
						aula.put("curso", row.getString("curso"));
						aula.put("id_tra", row.getInteger("id_tra"));
						aula.put("id_caa", row.getInteger("id_caa"));
						aula.put("id_cua", row.getInteger("id_cua"));
						aula.put("id_caul", row.getInteger("id_caul"));
						aula.put("id_au", row.getInteger("id_au"));
						aula.put("id_cur", row.getInteger("id_cur"));
						aulas_doce_correcto.add(aula);
					}
					
				}else if(row.getInteger("id_tra")!=null && row.getInteger("id_caul")!=null) {
					//Busco si no existe en el arreglo
					boolean encontrado = false;
					for (int i = 0; i < aulas_doce_correcto.size(); i++) {
						if(aulas_doce_correcto.get(i).getInteger("id_au").equals(row.getInteger("id_au")) && aulas_doce_correcto.get(i).getInteger("id_cur")!=null  && row.getInteger("id_caul")==null) {
							if(aulas_doce_correcto.get(i).getInteger("id_cur").equals(row.getInteger("id_cur"))) {
								encontrado =true;
								break;
							}
							
						} else if(aulas_doce_correcto.get(i).getInteger("id_au").equals(row.getInteger("id_au")) && aulas_doce_correcto.get(i).getInteger("id_cur")!=null  && row.getInteger("id_caul")!=null) {
							if(aulas_doce_correcto.get(i).getInteger("id_cur").equals(row.getInteger("id_cur")) &&  aulas_doce_correcto.get(i).getInteger("id_caul")==null) {
								//&& row.getInteger("id_cua_cual")!=null
								encontrado =true;
								aulas_doce_correcto.get(i).put("id_tra",row.getInteger("id_tra"));
								aulas_doce_correcto.get(i).put("id_caul",row.getInteger("id_caul"));
								break;
							}
								
						}
						
					}
					
					if(!encontrado) {
						Row aula = new Row();
						aula.put("aula", row.getString("aula"));
						aula.put("area", row.getString("area"));
						aula.put("curso", row.getString("curso"));
						aula.put("id_tra", row.getInteger("id_tra"));
						aula.put("id_caa", row.getInteger("id_caa"));
						aula.put("id_cua", row.getInteger("id_cua"));
						aula.put("id_caul", row.getInteger("id_caul"));
						aula.put("id_au", row.getInteger("id_au"));
						aula.put("id_cur", row.getInteger("id_cur"));
						aulas_doce_correcto.add(aula);
					} 
				}	
			}
			
			result.setResult(aulas_doce_correcto);
		} catch (Exception e) {
			result.setException(e);
		}
		
		/*else if(row.getInteger("id_tra")!=null && row.getInteger("id_cua")!=null && row.getInteger("id_cua").equals(row.getInteger("id_cua_cual"))) {
		Row aula = new Row();
		aula.put("aula", row.getString("aula"));
		aula.put("area", row.getString("area"));
		aula.put("curso", row.getString("curso"));
		aula.put("id_tra", row.getInteger("id_tra"));
		aula.put("id_caa", row.getInteger("id_caa"));
		aula.put("id_cua", row.getInteger("id_cua"));
		aula.put("id_caul", row.getInteger("id_caul"));
		aula.put("id_au", row.getInteger("id_au"));
		aula.put("id_cur", row.getInteger("id_cur"));
		aulas_doce_correcto.add(aula);
	}*/
		return result;

	}

	@Transactional
	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(CursoAula curso_aula) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			//Insertamos el curso Aula
			Integer id_cca=curso_aulaDAO.saveOrUpdate(curso_aula);
			
			//Buscamos si ya tiene evaluaciones creadas para el nuevo aula 
			List<Row> evaluaciones = evaluacionDAO.obtenerListaEvaluaciones(curso_aula.getId_cua());
			if(evaluaciones.size()>0 && curso_aula.getId()==null){
				//Insertamos en la tabla 
				for (Row row : evaluaciones) {
					Evaluacion evaluacion = new Evaluacion();
					evaluacion.setId_nep(row.getInteger("id_nep"));
					evaluacion.setId_cca(id_cca);
					evaluacion.setId_nte(row.getInteger("id_nte"));
					evaluacion.setId_ses(row.getInteger("id_ses"));
					evaluacion.setIns(row.getString("ins"));
					evaluacion.setEvi(row.getString("evi"));
					evaluacion.setNump(row.getInt("nump"));
					evaluacion.setFec_ini(null);
					evaluacion.setFec_fin(null);
					evaluacion.setEst("A");
					int id_ne=evaluacionDAO.saveOrUpdate(evaluacion);
					List<IndEva> indicadores = indEvaDAO.listByParams(new Param("id_ne",row.getInteger("id")),null);
					for (IndEva indEva : indicadores) {
						IndEva indEva2 = new IndEva();
						indEva2.setId_ne(id_ne);
						indEva2.setId_ind(indEva.getId_ind());
						indEva2.setEst("A");
						indEvaDAO.saveOrUpdate(indEva2);
					}
				}
			}
			result.setResult(1);
			//cacheManager.update(CursoAula.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@Transactional
	@RequestMapping( value="/grabarCursosAula", method = RequestMethod.POST)
	public AjaxResponseBody grabarCursosAula(Integer id_caa, Integer id_cua,Integer id_au, Integer id_tra, Integer id_caul) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			CursoAula curso_aula = new CursoAula();
			curso_aula.setId(id_caul);
			curso_aula.setId_caa(id_caa);
			curso_aula.setId_cua(id_cua);
			curso_aula.setId_au(id_au);
			curso_aula.setId_tra(id_tra);
			curso_aula.setEst("A");
			//Insertamos el curso Aula
			//Integer id_caul=curso_aulaDAO.saveOrUpdate(curso_aula);
		
			result.setResult(curso_aulaDAO.saveOrUpdate(curso_aula));
			//cacheManager.update(CursoAula.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			curso_aulaDAO.delete(id);
			//cacheManager.update(CursoAula.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			Param param=new Param();
			param.put("cca.id", id);
			List<CursoAula> cursoAulaList= curso_aulaDAO.listFullByParams(param,null);
			result.setResult( cursoAulaList.get(0));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarTrabajador", method = RequestMethod.GET)
	public AjaxResponseBody listarTrabajador() {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( curso_aulaDAO.listarTrabajador());
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarCursos", method = RequestMethod.GET)
	public AjaxResponseBody listarCursos(Integer id_niv, Integer id_gra, Integer id_anio, Integer id_suc) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( curso_aulaDAO.listarCursos(id_niv, id_gra, id_anio, id_suc));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/clonarAnio", method = RequestMethod.POST)
	public AjaxResponseBody clonarAnio(Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try { 
			result.setResult( curso_aulaDAO.clonarAnio(id_anio) );
 			
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	
	@RequestMapping( value="/listarCursosProfesor", method = RequestMethod.GET)
	public AjaxResponseBody listarCursosProfesor(Integer id_anio, Integer id_tra) {

		AjaxResponseBody result = new AjaxResponseBody();

		try {

			result.setResult( curso_aulaDAO.listarCursosProfesor(id_anio, id_tra));

		} catch (Exception e) {
			
			result.setException(e);
		}
		
		return result;

	}

	@RequestMapping( value="/validarUnidadesCompletas/{id_tra}/{id_anio}/{id_niv}/{id_gra}/{id_cur}", method = RequestMethod.GET)
	public AjaxResponseBody validarUnidadesCompletas( @PathVariable Integer id_tra,@PathVariable Integer id_anio,@PathVariable Integer id_niv,@PathVariable Integer id_gra,@PathVariable Integer id_cur) {

		AjaxResponseBody result = new AjaxResponseBody();

		try {
			boolean resultado = programacionAnualService.validarUnidadesCompeltas(id_niv, id_gra, id_cur, id_anio);
			if(resultado){
				result.setResult(1);
			} else{
				result.setResult(resultado);
			}

		} catch (Exception e) {
			
			result.setException(e);
		}
		
	 	return result;

	}

	
	@RequestMapping( value="/programacionAnual/{id_tra}/{id_anio}/{id_niv}/{id_gra}/{id_cur}", method = RequestMethod.GET)
	public void listarCursosProfesor(HttpServletResponse response, @PathVariable Integer id_tra,@PathVariable Integer id_anio,@PathVariable Integer id_niv,@PathVariable Integer id_gra,@PathVariable Integer id_cur) {

		AjaxResponseBody result = new AjaxResponseBody();

		try {
			//Boolean resultado = programacionAnualService.validarUnidadesCompeltas(id_niv, id_gra, id_cur, id_anio);
			//if(resultado==true){
				generarPdf( response, id_tra,id_anio, id_niv, id_gra, id_cur);
			//	result.setResult(1);
			//} else{
			//	result.setResult(resultado);
			//}

		} catch (Exception e) {
			
			result.setException(e);
		}
		
	//	return result;

	}
	
	/**
	 * Datos de la programacion anual por curso/profesor
	 * @param id_cca
	 * @return
	 */

	@ResponseBody
	public void generarPdf(HttpServletResponse response, Integer id_tra, Integer id_anio, Integer id_niv, Integer id_gra, Integer id_cur) {

		AjaxResponseBody result = new AjaxResponseBody();

		try {
			
			//CursoAula cursoAula = curso_aulaDAO.getFull(id_cca,new String[]{CursoAnio.TABLA});

				Row cabecera = programacionAnualService.obtenerCabecera(id_tra, id_cur, id_gra, id_anio, id_niv);
				List<Row> calendarizacion = programacionAnualService.obtenerCalendarizacion(id_anio, id_niv);
				List<Row> competencias= programacionAnualService.obtenerCompetenciasCapacidades(id_anio, id_niv, id_cur, id_gra);
				List<Row> periodos = programacionAnualService.listPeriodosSubtemas(id_anio, id_niv, id_cur, id_gra);
				//Row unidadesCapacidades = programacionAnualService.obtenerUnidadesCapacidades(id_anio, id_niv, id_cur, id_gra);//competencias y capacidades
				
				List<Row> unidadesCapacidades = programacionAnualService.obtenerCapacidadesxUnidad(id_niv, id_gra, id_cur, 0);
				
				Row vecesDesarrolloCapacidad = new Row();
				
				//setear las capacidades en X o '' por unidad
				for (Row row : unidadesCapacidades) {
					
					Integer[] capacidades_arr = (Integer[])row.get("capacidades_arr");
					List<String> capacidadesCheck = new ArrayList<>();
					for (Row rowCom : competencias) {
						@SuppressWarnings("unchecked")
						List<Row> rowCapacidades = (List<Row>)rowCom.get("capacidades");
						int checks = 0;
						for (Row row2 : rowCapacidades) {
							
							String check = "";
							
							for (Integer id_cap : capacidades_arr){
								if (id_cap.intValue() ==  row2.getInt("id")){
									check ="X";
									checks ++;
									String keytotal= rowCom.get("id") + "." +id_cap;

									Object total = vecesDesarrolloCapacidad.get(keytotal);
									if (total==null)
										vecesDesarrolloCapacidad.put(keytotal, 1);
									else
										vecesDesarrolloCapacidad.put(keytotal, (Integer)total +1);
								}
							}
							capacidadesCheck.add(check);
						}
						//vecesDesarrolloCapacidad.add(checks)	;

					}

					row.put("capacidadesCheck", capacidadesCheck);
						
				}
				
				//total de veces que desarrolla la capacidad
				
				
				Row programacionAnual = new Row();
				programacionAnual.put("cabecera", cabecera);
				programacionAnual.put("competencias", competencias);
				programacionAnual.put("unidades", periodos);
				programacionAnual.put("vecesDesarrolloCapacidad", vecesDesarrolloCapacidad);
				
				Row capacidades = new Row();
				int nro_capacidades = 0;
				for (Row rowC : competencias) {
					nro_capacidades += ((List)rowC.get("capacidades")).size();
				}
				capacidades.put("competencias", competencias);
				capacidades.put("nro_capacidades", nro_capacidades);
				
				programacionAnual.put("capacidades", capacidades);//
				programacionAnual.put("unidadesCapacidades", unidadesCapacidades);//
				
				String tipoPeriodo = "";
				if (calendarizacion!=null && calendarizacion.size()>0)
					tipoPeriodo = calendarizacion.get(0).getString("periodo");
				else
					calendarizacion = new ArrayList<Row>();
				
				programacionAnual.put("tipoPeriodo", tipoPeriodo.toUpperCase());
				programacionAnual.put("calendarizacion", calendarizacion);
				
				
				WordToPDFUtil util = new WordToPDFUtil();
				String  rutacARPETA =  cacheManager.getComboBox("mod_parametro", new String[]{"RUTA_PLANTILLA"} ).get(0).getAux1();

				String pdf = util.createPdf(rutacARPETA, cabecera.getInteger("id_tra"), programacionAnual );
				 
				byte[] fileContent = Files.readAllBytes(new File(pdf).toPath());

	 
				/**
				 * SI GENERO EL PDF CORRECTAMENTE, SE DEBE GRABAR EN EL CONTROL DE DESCARGAS
				 */
				ProgAnual prog_anual = new ProgAnual();
				prog_anual.setId_cur(id_cur);
				prog_anual.setId_gra(id_gra);
				prog_anual.setId_niv(id_niv);
				prog_anual.setId_tra(id_tra);
				prog_anual.setFlg_descarga("1");
				prog_anual.setEst("A");
				progAnualDAO.saveOrUpdate(prog_anual);
				
				response.setHeader("Content-Disposition","inline; filename=\"" + "xxxx" +"\"");
				response.setContentType("application/pdf; name=\"" + cabecera.getString("trabajador") + "\"");
				response.getOutputStream().write(fileContent);

				

		} catch (Exception e) {
			
			result.setException(e);
		}
		
	}
	
}
