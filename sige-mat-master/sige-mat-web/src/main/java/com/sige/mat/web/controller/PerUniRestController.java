package com.sige.mat.web.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

 
import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.sige.mat.dao.AnioDAO;
import com.sige.mat.dao.AreaAnioDAO;
import com.sige.mat.dao.PerUniDAO;
import com.sige.mat.dao.PerUniDetDAO;
import com.sige.mat.dao.PeriodoCalificacionDAO;
import com.sige.mat.dao.TipoCalificacionDAO;
import com.tesla.colegio.model.Anio;
import com.tesla.colegio.model.AreaAnio;
import com.tesla.colegio.model.DesempenioAula;
import com.tesla.colegio.model.PerUni;
import com.tesla.colegio.model.PerUniDet;
import com.tesla.colegio.model.PeriodoCalificacion;
import com.tesla.colegio.model.TipoCalificacion;
import com.tesla.colegio.model.Usuario;
import com.tesla.frmk.sql.Row;

@RestController
@RequestMapping(value = "/api/perUni")
public class PerUniRestController {
	
	@Autowired
	private PerUniDAO per_uniDAO;
	
	@Autowired
	private PerUniDetDAO per_uni_detDAO;;
	
	@Autowired
	private AnioDAO anioDAO;
	
	@Autowired
	private AreaAnioDAO areaAnioDAO;
	
	@Autowired
	private PeriodoCalificacionDAO periodoCalificacionDAO;
	
	@Autowired
	private TipoCalificacionDAO tipoCalificacionDAO;
	 
	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(PerUni per_uni, Integer id_anio) {
		
		AjaxResponseBody result = new AjaxResponseBody();
		per_uni.setId_anio(id_anio); 
		result.setResult(per_uniDAO.listFullByParams( per_uni, new String[]{"niv.nom asc, cpa.nom asc, cpu.nump asc"}) );
		
		return result;
	}
	
	@RequestMapping(value = "/listarPeriodosxAnio")
	public AjaxResponseBody listarPeriodosxAnio(Integer id_anio, Integer id_tra) {
		
		AjaxResponseBody result = new AjaxResponseBody();
		
		result.setResult(per_uniDAO.listarUnidadesPeriodoxAnio(id_anio, id_tra) );
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(PerUni per_uni, Integer[] id_uni_per_det, Integer[] nro_sem, HttpSession session) {
		
		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			int id_cpu=per_uniDAO.saveOrUpdate(per_uni);
			int uni_ini=per_uni.getNumu_ini();
			int uni_fin= per_uni.getNumu_fin();
			if(per_uni.getId()!=null){
				for (int i = 0; i < id_uni_per_det.length; i++) {
					Integer id_det = id_uni_per_det[i];
					PerUniDet per_uni_det= new PerUniDet();
					per_uni_det.setId(id_det);
					per_uni_det.setNro_sem(nro_sem[i]);
					per_uni_det.setId_cpu(id_cpu);

					per_uni_detDAO.saveOrUpdate(per_uni_det);
					
				}
			} else{
				for (int i = uni_ini; i <= uni_fin; i++) {
					PerUniDet per_uni_det= new PerUniDet();
					per_uni_det.setId_cpu(id_cpu);
					per_uni_det.setOrd(i);
					per_uni_det.setEst("A");
					per_uni_detDAO.saveOrUpdate(per_uni_det);
					
				}
			}			
			result.setResult(id_cpu);
			//cacheManager.update(PerUni.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping(value = "/grabarPeriodoCalificacion", method = RequestMethod.POST)
	public AjaxResponseBody grabarPeriodoCalificacion(PeriodoCalificacion periodoCalificacion, String[] letra) {
		
		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			Integer id_gra=periodoCalificacion.getId_gra();
			Integer id_cpu=periodoCalificacion.getId_cpu();
			Integer id_anio=periodoCalificacion.getId_anio();
			Integer id_tca=periodoCalificacion.getId_tca();
			TipoCalificacion tipoCalificacion = tipoCalificacionDAO.get(id_tca);
			if(tipoCalificacion.getCod().equals("CUALI")) {
				Param param = new Param();
				param.put("id_tca", periodoCalificacion.getId_tca());
				param.put("id_gra", periodoCalificacion.getId_gra());
				param.put("id_cpu", periodoCalificacion.getId_cpu());
				param.put("id_anio", periodoCalificacion.getId_anio());
				List<PeriodoCalificacion> periodo_calificacion = periodoCalificacionDAO.listByParams(param, null);
				
				final List<String> letrasForm = Arrays.asList(letra);	
				if (periodo_calificacion.size()>0){//si hay letras ya grabadas
					String listaIds[] = new String[periodo_calificacion.size() ];
				int i=0;	
				for (PeriodoCalificacion row : periodo_calificacion) {
					listaIds[i]=row.getLetra();
					i++;
				}

				final List<String> letrasExiste = Arrays.asList(listaIds);	
				for (String letraForm : letrasForm) {
						String id_letra=letraForm;
						for (String string : letrasExiste) {
							if(!id_letra.contains(string)) {
								periodoCalificacionDAO.desactivarPeriodoCalificacion(id_gra, id_cpu, id_anio, id_tca, string);
							} else if(id_letra.contains(string)) {
								periodoCalificacionDAO.activarPeriodoCalificacion(id_gra, id_cpu, id_anio, id_tca, string);
							} 
						}	
				}
				}
			
				for (int y = 0; y < letra.length; y++) {
						Param param2 = new Param();
						param2.put("id_tca", periodoCalificacion.getId_tca());
						param2.put("id_gra", periodoCalificacion.getId_gra());
						param2.put("id_cpu", periodoCalificacion.getId_cpu());
						param2.put("id_anio", periodoCalificacion.getId_anio());
						param2.put("letra", letra[y]);
						PeriodoCalificacion periodoCali = periodoCalificacionDAO.getByParams(param2);
						if(periodoCali!=null) {
							if(periodoCali.getEst().equals("I")) {
								PeriodoCalificacion periodoCalificacion2 = new PeriodoCalificacion();
								periodoCalificacion2.setId(periodoCali.getId());
								periodoCalificacion2.setId_gra(id_gra);
								periodoCalificacion2.setId_cpu(id_cpu);
								periodoCalificacion2.setId_anio(id_anio);
								periodoCalificacion2.setId_tca(id_tca);
								periodoCalificacion2.setLetra(letra[y]);
								periodoCalificacion2.setEst("A");
								periodoCalificacionDAO.saveOrUpdate(periodoCalificacion2);
							}
						} else {
							PeriodoCalificacion periodoCalificacion2 = new PeriodoCalificacion();
							periodoCalificacion2.setId_gra(id_gra);
							periodoCalificacion2.setId_cpu(id_cpu);
							periodoCalificacion2.setId_anio(id_anio);
							periodoCalificacion2.setId_tca(id_tca);
							periodoCalificacion2.setLetra(letra[y]);
							periodoCalificacion2.setEst("A");
							periodoCalificacionDAO.saveOrUpdate(periodoCalificacion2);
							
						}				 				
				}
			} else if(tipoCalificacion.getCod().equals("CUANTI")){
				Param param = new Param();
				param.put("id_tca", 1);
				param.put("id_gra", periodoCalificacion.getId_gra());
				param.put("id_cpu", periodoCalificacion.getId_cpu());
				param.put("id_anio", periodoCalificacion.getId_anio());
				List<PeriodoCalificacion> periodo_calificacion_cualitativa = periodoCalificacionDAO.listByParams(param, null);
				if(periodo_calificacion_cualitativa.size()>0) {
					//Elimino
					periodoCalificacionDAO.deletePeriodoCalificacion(id_gra, id_cpu, id_anio, 1);
				}
				
				PeriodoCalificacion periodoCalificacion2 = new PeriodoCalificacion();
				//Si ya existia periodo calificacion cualitativa
				Param param2 = new Param();
				param2.put("id_tca", 2);
				param2.put("id_gra", periodoCalificacion.getId_gra());
				param2.put("id_cpu", periodoCalificacion.getId_cpu());
				param2.put("id_anio", periodoCalificacion.getId_anio());
				List<PeriodoCalificacion> periodo_calificacion_cuantitativa = periodoCalificacionDAO.listByParams(param2, null);
				if(periodo_calificacion_cuantitativa.size()>0) {
					periodoCalificacion2.setId(periodo_calificacion_cuantitativa.get(0).getId());
				}
				periodoCalificacion2.setId_gra(id_gra);
				periodoCalificacion2.setId_cpu(id_cpu);
				periodoCalificacion2.setId_anio(id_anio);
				periodoCalificacion2.setId_tca(id_tca);
				periodoCalificacion2.setNota_ini(periodoCalificacion.getNota_ini());
				periodoCalificacion2.setNota_fin(periodoCalificacion.getNota_fin());
				periodoCalificacion2.setEst("A");
				periodoCalificacionDAO.saveOrUpdate(periodoCalificacion2);
			}
			
			result.setResult(id_cpu);
			//cacheManager.update(PerUni.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/detallePeriodoCalificacion", method = RequestMethod.GET)
	public AjaxResponseBody detallePeriodoCalificacion(Integer id_gra, Integer id_cpu, Integer id_anio, Integer id_gir) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			Param param = new Param();
			param.put("id_gra", id_gra);
			param.put("id_anio", id_anio);
			param.put("id_gir", id_gir);
			List<AreaAnio> areas_anio=areaAnioDAO.listByParams(param, null);
			Integer id_tca=null;
			Integer id_pro_per=null;
			Integer id_pro_anu=null;
			if(areas_anio.size()>0) {
				 id_tca=areas_anio.get(0).getId_tca();
				 id_pro_per=areas_anio.get(0).getId_pro_per();
				 id_pro_anu=areas_anio.get(0).getId_pro_anu();
			}
			
			List<Row> periodo_calificacion= periodoCalificacionDAO.listarPeriodosCalificacionxGrado(id_gra, id_cpu, id_anio);
			Map<String, Object> calificaciones = new HashMap<>();
			
			if(periodo_calificacion.size()>0) {
				calificaciones.put("id_tca", periodo_calificacion.get(0).getInteger("id_tca"));
				calificaciones.put("id_pro_per", id_pro_per);
				calificaciones.put("id_pro_anu", id_pro_anu);
				calificaciones.put("nota_ini", periodo_calificacion.get(0).getInteger("nota_ini"));
				calificaciones.put("nota_fin", periodo_calificacion.get(0).getInteger("nota_fin"));
				List<Row> notas = new ArrayList<>();
				if(periodo_calificacion.size()>1) {
					for (Row per_cal : periodo_calificacion) {
						Row row = new Row();
						row.put("letras", per_cal.get("letra"));
						notas.add(row);
					}
					
				} 
				calificaciones.put("notas", notas);
			} else {
				calificaciones.put("id_tca", id_tca);
				calificaciones.put("id_pro_per", id_pro_per);
				calificaciones.put("id_pro_anu", id_pro_anu);
			}
			result.setResult(calificaciones);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@Transactional
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			//Primero eliminamos a los hijos
			per_uni_detDAO.delete(id);
			//Luego eliminamos al padre
			per_uniDAO.delete(id);
			//cacheManager.update(PerUni.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			List<PerUni> periodoList= per_uniDAO.listFullByParams(new Param("cpu.id",id),null);
			result.setResult(periodoList.get(0));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/verificarPeriodoVig", method = RequestMethod.GET)
	public AjaxResponseBody verificarPeriodoVig( Integer id_niv, Integer id_anio, Integer nump) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			List<Map<String, Object>> per_vig=per_uniDAO.verificarPeriodoVig(id_niv, id_anio, nump);
			if(per_vig.size()>0) {
				//result.setResult(per_vig.get(0));
				result.setResult(1);
			}else {
				//result.setResult(per_vig);	
				result.setResult(0);	
			}	
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
			result.setResult( per_uniDAO.clonarConfiguraciones(id_anio,id_anio_ant));
			
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
}
