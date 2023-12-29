package com.sige.mat.web.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sige.mat.dao.IndSubDAO;
import com.sige.mat.dao.IndicadorDAO;
import com.sige.mat.dao.SubtemaCapacidadDAO;
import com.tesla.colegio.model.IndSub;
import com.tesla.colegio.model.Indicador;
import com.tesla.colegio.model.SubtemaCapacidad;
import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;


@RestController
@RequestMapping(value = "/api/indSub")
public class IndSubRestController {
	
	@Autowired
	private IndicadorDAO indicadorDAO;

	@Autowired
	private SubtemaCapacidadDAO subtemaCapacidadDAO;

	@Autowired
	private IndSubDAO ind_subDAO;

	
	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(IndSub ind_sub) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(ind_subDAO.listFullByParams( ind_sub, new String[]{"cis.id"}) );
		
		return result;
	}
	
	@RequestMapping(value = "/listarIndicadores")
	public AjaxResponseBody listarIndicadores(Integer id_anio, Integer id_gra, Integer id_cap,Integer id_sub) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(ind_subDAO.listaIndicadores(id_anio, id_gra, id_cap, id_sub));
		
		return result;
	}
	

/*	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(Indicador indicador, Integer id_sub, Integer id_csc) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			
			//subtema capacidad
			Param param = new Param();
			param.put("id_cap",indicador.getId_cap());
			param.put("id_ccs",id_sub);
			SubtemaCapacidad sc = subtemaCapacidadDAO.getByParams(param);
			Integer id_csc_nuevo = null;
			if(sc==null){
				SubtemaCapacidad subtema_capacidad = new SubtemaCapacidad();
				subtema_capacidad.setId_cap(indicador.getId_cap());
				subtema_capacidad.setId_ccs(id_sub);
				subtema_capacidad.setId(id_csc);
				subtema_capacidad.setEst("A");
				 id_csc_nuevo = subtemaCapacidadDAO.saveOrUpdate(subtema_capacidad);
			}else
				 id_csc_nuevo = sc.getId();
			
			Integer id_ind = indicadorDAO.saveOrUpdate(indicador);
			
			if(indicador.getId()==null){// solo se inserta si subtema capacidad es null
				IndSub indSub = new IndSub();
				indSub.setId_ind(id_ind);
				indSub.setId_sub(id_sub);
				indSub.setEst("A");
				ind_subDAO.saveOrUpdate(indSub);

			}
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("id_ind", id_ind );
			map.put("id_csc", id_csc_nuevo );
				
			result.setResult( map );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	*/
	
	/**
	 * 
	 * @param id ID DEL INDICADOR
	 * @return
	 */
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			Indicador indicador = indicadorDAO.get(id);
			
			//buscar col_subtema_capacidad
			
			ind_subDAO.deleteByIdIndicador(id);
			indicadorDAO.delete(id);
			
			
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( ind_subDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	

	@RequestMapping( value="/PorSubtemaCapacidad", method = RequestMethod.GET)
	public AjaxResponseBody porSubtemaCapacidad(Integer id, Integer id_anio, Integer id_gra, Integer id_cap ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			
			Param param = new Param();
			param.put("id_anio", id_anio);
			param.put("id_gra", id_gra);
			param.put("id_cap", id_cap);
			List<Indicador> indicadores = indicadorDAO.listByParams(param, null);
			
			Indicador indicador = null;
			if (indicadores.size()>0)
				indicador  = indicadores.get(0);
			
			result.setResult( indicador );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	
}
