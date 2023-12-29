package com.sige.mat.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.sige.mat.dao.DcnAreaDAO;
import com.sige.mat.dao.DcnCompTransDAO;
import com.sige.mat.dao.DcnNivelDAO;
import com.sige.mat.dao.DisenioCurricularDAO;
import com.sige.mat.dao.EmpresaDAO;
import com.sige.mat.dao.GiroNegocioDAO;
import com.sige.mat.dao.ServicioDAO;
import com.sige.mat.dao.SucursalDAO;
import com.tesla.colegio.model.DcnArea;
import com.tesla.colegio.model.DcnCompTrans;
import com.tesla.colegio.model.DcnNivel;
import com.tesla.colegio.model.DisenioCurricular;
import com.tesla.colegio.model.Empresa;
import com.tesla.colegio.model.GiroNegocio;
import com.tesla.colegio.model.Servicio;
import com.tesla.colegio.model.Sucursal;


@RestController
@RequestMapping(value = "/api/disenioCurricular")
public class DisenioCurricularRestController {

	@Autowired
	private DisenioCurricularDAO disenioCurricularDAO;
		
	@Autowired
	private DcnNivelDAO dcnNivelDAO;
	
	@Autowired
	private DcnAreaDAO dcnAreaDAO;
	
	@Autowired
	private DcnCompTransDAO dcnCompTransDAO;

	@RequestMapping(value = "/listar/{id_anio}")
	public AjaxResponseBody getLista(DisenioCurricular disenioCurricular, @PathVariable Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		
		disenioCurricular.setId_anio(id_anio);
		result.setResult(disenioCurricularDAO.listFullByParams( disenioCurricular, new String[]{"cdc.id"}) );
		
		return result;
	}
	
	@RequestMapping(value = "/listarNiveles/{id_dc}")
	public AjaxResponseBody listarNiveles(@PathVariable Integer id_dc) {

		AjaxResponseBody result = new AjaxResponseBody();
		
		 
		result.setResult(dcnNivelDAO.listFullByParams( new Param("id_dcn",id_dc), new String[]{"dcniv.id"}) );
		
		return result;
	}
	
	@RequestMapping(value = "/listarNivelesCombo/{id_dc}")
	public AjaxResponseBody listarNivelesCombo(@PathVariable Integer id_dc) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(dcnNivelDAO.listarNivelesCombo(id_dc));
		
		return result;
	}
	
	@RequestMapping(value = "/listarNivelesComboxGiro/{id_anio}/{id_gir}")
	public AjaxResponseBody listarNivelesCombo(@PathVariable Integer id_anio, @PathVariable Integer id_gir) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(dcnNivelDAO.listarNivelesComboxGiro(id_anio, id_gir));
		
		return result;
	}
	
	@RequestMapping(value = "/listarDisenioCurricular")
	public AjaxResponseBody listarDisenioCurricular(DisenioCurricular disenioCurricular) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(disenioCurricularDAO.listarDisenioCurricular());
		
		return result;
	}
	
	@RequestMapping(value = "/listarAreas/{id_dcniv}")
	public AjaxResponseBody listarAreas(@PathVariable Integer id_dcniv) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(dcnAreaDAO.listFullByParams( new Param("id_dcniv",id_dcniv), new String[]{"dcare.ord"}) );
		
		return result;
	}
	
	@RequestMapping(value = "/listarAreasCombo/{id_dcniv}")
	public AjaxResponseBody listarAreasCombo(@PathVariable Integer id_dcniv) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(dcnAreaDAO.listarAreasCombo(id_dcniv));
		
		return result;
	}
	
	@RequestMapping(value = "/listarCompetenciasTransversales/{id_dcniv}")
	public AjaxResponseBody listarServicios(@PathVariable Integer id_dcniv) {

		AjaxResponseBody result = new AjaxResponseBody();
		
		 Param param = new Param();
		 param.put("id_dcniv", id_dcniv);
		result.setResult(dcnCompTransDAO.listFullByParams( param, new String[]{"ctra.id"}) );
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(DisenioCurricular disenioCurricular) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( disenioCurricularDAO.saveOrUpdate(disenioCurricular) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/grabarNivel", method = RequestMethod.POST)
	public AjaxResponseBody grabarNivel(DcnNivel dcnNivel) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( dcnNivelDAO.saveOrUpdate(dcnNivel) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/grabarArea", method = RequestMethod.POST)
	public AjaxResponseBody grabarSucursal(DcnArea dcnArea) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( dcnAreaDAO.saveOrUpdate(dcnArea) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/grabarCompetenciaTransversal", method = RequestMethod.POST)
	public AjaxResponseBody grabarServicio(DcnCompTrans dcnCompTrans) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( dcnCompTransDAO.saveOrUpdate(dcnCompTrans));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			disenioCurricularDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="eliminarNivel/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminarNivel(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			dcnNivelDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="eliminarArea/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminarSucursal(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			dcnAreaDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="eliminarCompetenciaTransversal/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminarServicio(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			dcnCompTransDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			List<DisenioCurricular> disenio_curricular=disenioCurricularDAO.listFullByParams(new Param("cdc.id",id),null);
			DisenioCurricular disenio= null;
			if(disenio_curricular.size()>0)
				disenio=disenio_curricular.get(0);
			result.setResult(disenio);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="obtenerDatosNivel/{id}", method = RequestMethod.GET)
	public AjaxResponseBody obtenerDatosNivel(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult(dcnNivelDAO.listFullByParams(new Param("dcniv.id",id), new String[]{"dcniv.id"}).get(0));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	/*@RequestMapping( value="/obtenerDatosGiroNegocio", method = RequestMethod.GET)
	public AjaxResponseBody obtenerDatosGiroNegocio(Integer id_giro ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( empresaDAO.datosGiroNegocio(id_giro) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	*/
	
	@RequestMapping( value="/obtenerDatosArea/{id}", method = RequestMethod.GET)
	public AjaxResponseBody obtenerDatosLocal(@PathVariable Integer id) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			List<DcnArea> datos_area=dcnAreaDAO.listFullByParams(new Param("dcare.id",id),null);
			DcnArea area= null;
			if(datos_area.size()>0)
				area=datos_area.get(0);
			result.setResult(area);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/obtenerDatosCompetencia/{id}", method = RequestMethod.GET)
	public AjaxResponseBody obtenerDatosCompetencia(@PathVariable Integer id) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			List<DcnCompTrans> datos_competencia=dcnCompTransDAO.listFullByParams(new Param("ctra.id",id),null);
			DcnCompTrans competencia= null;
			if(datos_competencia.size()>0)
				competencia=datos_competencia.get(0);
			result.setResult(competencia);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	/*@RequestMapping( value="/listarNivelesSucursal/{id_suc}", method = RequestMethod.GET)
	public AjaxResponseBody listarNivelesXSucursal(@PathVariable Integer id_suc) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			List<Sucursal> datos_sucursal=sucursalDAO.listFullByParams(new Param("suc.id",id_suc),null);
			Sucursal sucursal= null;
			if(datos_sucursal.size()>0)
				sucursal=datos_sucursal.get(0);
			result.setResult(sucursal);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}*/
}
