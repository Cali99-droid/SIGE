package com.sige.mat.web.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.sige.core.dao.cache.CacheManager;
import com.sige.mat.dao.AreaAnioDAO;
import com.sige.mat.dao.CursoAnioDAO;
import com.sige.mat.dao.DcnAreaDAO;
import com.sige.mat.dao.GradDAO;
import com.tesla.colegio.model.Area;
import com.tesla.colegio.model.AreaAnio;
import com.tesla.colegio.model.DcnArea;
import com.tesla.colegio.model.Grad;
import com.tesla.colegio.model.TurnoAula;

@RestController
@RequestMapping(value = "/api/areaAnio")
public class AreaAnioRestController {
	
	@Autowired
	private AreaAnioDAO area_anioDAO;
	
	@Autowired
	private CursoAnioDAO cursoAnioDAO;
	
	@Autowired
	private GradDAO gradDAO;
	
	@Autowired
	private DcnAreaDAO dcnAreaDAO;

	@Autowired
	private CacheManager cacheManager;
	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(AreaAnio area_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(area_anioDAO.listFullByParams( area_anio, new String[]{"niv.id","caa.ord"}) );
		
		return result;
	}
	
	@RequestMapping(value = "/listarAreasDCN")
	public AjaxResponseBody listarAreasDCN(Integer id_anio, Integer id_gra, Integer id_gir) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(area_anioDAO.listarAreasDCN(id_gra, id_anio, id_gir) );
		
		return result;
	}
	
	@RequestMapping(value = "/listarAreasDCNGrado")
	public AjaxResponseBody listarAreasDCNGrado(Integer id_anio, Integer id_gra, Integer id_gir) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(area_anioDAO.listarAreasDCNxGrado(id_gra, id_anio, id_gir));
		
		return result;
	}
	
	@RequestMapping(value = "/listarAreasDCNGradoCombo")
	public AjaxResponseBody listarAreasDCNGradoCombo(Integer id_anio, Integer id_gra, Integer id_tra, Integer id_gir) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(area_anioDAO.listarAreasDCNxGradoCombo(id_gra, id_anio, id_tra, id_gir));
		
		return result;
	}
	
	@RequestMapping(value = "/listarAreasxCoordinadorDCNGradoCombo")
	public AjaxResponseBody listarAreasDCNxCoordinadorxGradoCombo(Integer id_anio, Integer id_gra, Integer id_tra, Integer id_gir) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(area_anioDAO.listarAreasDCNxCoordinadorxGradoCombo(id_gra, id_anio, id_tra, id_gir));
		
		return result;
	}
	
	@RequestMapping(value = "/listarAreasxCoordinadorAreaDCNGradoCombo")
	public AjaxResponseBody listarAreasDCNxCoordinadorAreaxGradoCombo(Integer id_anio, Integer id_gra, Integer id_tra, Integer id_gir) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(area_anioDAO.listarAreasDCNxCoordinadorAreaxGradoCombo(id_gra, id_anio, id_tra, id_gir));
		
		return result;
	}
	
	@RequestMapping(value = "/listarCursosDCNGrado")
	public AjaxResponseBody listarCursosDCNGrado(Integer id_caa) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(cursoAnioDAO.listarCursosDCN(id_caa));
		
		return result;
	}
	
	@RequestMapping(value = "/listarCursosDCNXArea")
	public AjaxResponseBody listarCursosDCNXArea(Integer id_caa) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(cursoAnioDAO.listarCursosporArea(id_caa));
		
		return result;
	}
	
	@RequestMapping(value = "/listarCursosDCNXAreaxGradoCombo")
	public AjaxResponseBody listarCursosDCNXAreaxGradoCombo(Integer id_caa, Integer id_au, Integer id_tra) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(cursoAnioDAO.listarCursosporAreaxGradoCombo(id_caa, id_au,id_tra));
		
		return result;
	}
	
	@RequestMapping(value = "/listarTiposCalificacion")
	public AjaxResponseBody listarTiposCalificacion() {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(area_anioDAO.listarTiposCalificacion());
		
		return result;
	}
	
	@RequestMapping(value = "/listarTiposPromedio")
	public AjaxResponseBody listarTiposPromedio() {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(area_anioDAO.listarTiposPromedio());
		
		return result;
	}
	
	/*@RequestMapping(value = "/listarGradosxNivel/{id_niv}")
	public AjaxResponseBody listarGradosxNivel(@PathVariable Integer id_niv) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(area_anioDAO.listFullByParams( area_anio, new String[]{"niv.id","caa.ord"}) );
		
		return result;
	}*/

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(AreaAnio area_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( area_anioDAO.saveOrUpdate(area_anio) );
			//cacheManager.update(AreaAnio.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/grabarAreasDC", method = RequestMethod.POST)
	public AjaxResponseBody grabarAreasAnioDC(String id_caa[], Integer id_adc[], AreaAnio areaAnio, Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		Grad grad = gradDAO.get(areaAnio.getId_gra());
		Integer id_gra=grad.getId();
		Integer id_niv=grad.getId_nvl();
		Integer id_tca=areaAnio.getId_tca();
		Integer id_pro_per=areaAnio.getId_pro_per();
		Integer id_pro_anu=areaAnio.getId_pro_anu();
		Integer id_gir=areaAnio.getId_gir();
		try {

			//Obtenemos la lista de Areas para el grado
			List<AreaAnio> area_anio = new ArrayList<AreaAnio>();
			if (id_adc.length>0){
				Param param = new Param();
				param.put("id_gra", areaAnio.getId_gra());
				param.put("id_niv", grad.getId_nvl());
				param.put("id_anio", areaAnio.getId_anio());
				param.put("id_gir", id_gir);
				area_anio = area_anioDAO.listByParams(param, null);
			}
			if(area_anio.size()>0) {
				for (int i = 0; i < id_adc.length; i++) {
					if(!id_caa[i].equals("null")) {
						for (AreaAnio areaAnio3 : area_anio) {
							Integer id_caa_ex=areaAnio3.getId();
							DcnArea dcnArea = dcnAreaDAO.get(id_adc[i]);
							if(Integer.parseInt(id_caa[i])!=id_caa_ex) {
								//Actualizar los datos
								//Si no es igual desactivo
								/*AreaAnio areaAnio2= new AreaAnio();
								areaAnio2.setId(Integer.parseInt(id_caa[i]));
								areaAnio2.setId_niv(id_niv);
								areaAnio2.setId_gra(id_gra);
								areaAnio2.setId_anio(id_anio);
								areaAnio2.setId_adc(id_adc[i]);
								areaAnio2.setId_area(dcnArea.getId_are());
								areaAnio2.setId_tca(id_tca);
								areaAnio2.setId_pro_per(id_pro_per);
								areaAnio2.setId_pro_anu(id_pro_anu);
								areaAnio2.setId_gir(id_gir);
								areaAnio2.setEst("I");
								area_anioDAO.saveOrUpdate(areaAnio2);*/
							} else {
								//Inserto los datos
								AreaAnio areaAnio2= new AreaAnio();
								if(id_caa[i]!=null) {
									areaAnio2.setId(Integer.parseInt(id_caa[i]));
								}	
								areaAnio2.setId_niv(id_niv);
								areaAnio2.setId_gra(id_gra);
								areaAnio2.setId_anio(id_anio);
								//Busco el adc
								if(areaAnio3!=null) {
									Integer id_adc_ex=areaAnio3.getId_adc();
									Integer id_adc_nue=id_adc[i];
									if(id_adc_ex.equals(id_adc_nue)) {
										areaAnio2.setId_adc(id_adc[i]);
										areaAnio2.setId_area(dcnArea.getId_are());
										areaAnio2.setId_tca(id_tca);
										areaAnio2.setId_pro_per(id_pro_per);
										areaAnio2.setId_pro_anu(id_pro_anu);
										areaAnio2.setId_gir(id_gir);
										areaAnio2.setEst("A");
										area_anioDAO.saveOrUpdate(areaAnio2);
									} else {
										
									}
								} 
								
								
							}
						}
					} else {
						DcnArea dcnArea = dcnAreaDAO.get(id_adc[i]);
						//Busco si existe el area
						Param param = new Param();
						param.put("id_niv", id_niv);
						param.put("id_gra", id_gra);
						param.put("id_anio", id_anio);
						param.put("id_adc", id_adc[i]);
						param.put("id_gir", id_gir);
						param.put("id_area", dcnArea.getId_are());
						AreaAnio area_anio_ex = area_anioDAO.getByParams(param);
						if(area_anio_ex!=null) {
							AreaAnio areaAnio2= new AreaAnio();
							areaAnio2.setId(area_anio_ex.getId());
							areaAnio2.setId_niv(id_niv);
							areaAnio2.setId_gra(id_gra);
							areaAnio2.setId_anio(id_anio);
							areaAnio2.setId_adc(id_adc[i]);
							areaAnio2.setId_area(dcnArea.getId_are());
							areaAnio2.setId_tca(id_tca);
							areaAnio2.setId_pro_per(id_pro_per);
							areaAnio2.setId_pro_anu(id_pro_anu);
							areaAnio2.setId_gir(id_gir);
							areaAnio2.setEst("A");
							area_anioDAO.saveOrUpdate(areaAnio2);
						} else {
							AreaAnio areaAnio2= new AreaAnio();
							areaAnio2.setId_niv(id_niv);
							areaAnio2.setId_gra(id_gra);
							areaAnio2.setId_anio(id_anio);
							areaAnio2.setId_adc(id_adc[i]);
							areaAnio2.setId_area(dcnArea.getId_are());
							areaAnio2.setId_tca(id_tca);
							areaAnio2.setId_pro_per(id_pro_per);
							areaAnio2.setId_pro_anu(id_pro_anu);
							areaAnio2.setId_gir(id_gir);
							areaAnio2.setEst("A");
							area_anioDAO.saveOrUpdate(areaAnio2);
						}					
						
					}
				}
			} else {
				for (int i = 0; i < id_adc.length; i++) {
					//Inserto los datos
					DcnArea dcnArea = dcnAreaDAO.get(id_adc[i]);
					AreaAnio areaAnio2= new AreaAnio();
					areaAnio2.setId_niv(id_niv);
					areaAnio2.setId_gra(id_gra);
					areaAnio2.setId_anio(id_anio);
					areaAnio2.setId_adc(id_adc[i]);
					areaAnio2.setId_area(dcnArea.getId_are());
					areaAnio2.setId_tca(id_tca);
					areaAnio2.setId_pro_per(id_pro_per);
					areaAnio2.setId_pro_anu(id_pro_anu);
					areaAnio2.setId_gir(id_gir);
					areaAnio2.setEst("A");
					area_anioDAO.saveOrUpdate(areaAnio2);
				}
			}

			if (id_caa != null){//si hay instrumentos
				int listaIds[] = new int[ id_caa.length ];
			final List<String> areasForm = Arrays.asList(id_caa);	
			
			if(area_anio.size()>0) {
				int i=0;
				for (AreaAnio areaAnio2 : area_anio) {
					if(!areasForm.contains(areaAnio2.getId().toString())){
						area_anioDAO.desactivarAreaAnio(areaAnio2.getId());
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
			area_anioDAO.delete(id);
			//cacheManager.update(AreaAnio.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( area_anioDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/clonarAnio", method = RequestMethod.POST)
	public AjaxResponseBody clonarAnio(Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try { 
			Integer cantidad =area_anioDAO.clonarAnio(id_anio);
			
			if (cantidad.intValue() == 0 )
				throw new Exception("No se pudo copiar los datos del a�o anterior");
			
			result.setResult( cantidad );
			//cacheManager.update(AreaAnio.TABLA);
			
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
}
