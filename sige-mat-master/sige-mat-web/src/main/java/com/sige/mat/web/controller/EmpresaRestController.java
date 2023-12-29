package com.sige.mat.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.sige.mat.dao.EmpresaDAO;
import com.sige.mat.dao.GiroNegocioDAO;
import com.sige.mat.dao.ServicioDAO;
import com.sige.mat.dao.SucursalDAO;
import com.tesla.colegio.model.Empresa;
import com.tesla.colegio.model.GiroNegocio;
import com.tesla.colegio.model.Servicio;
import com.tesla.colegio.model.Sucursal;


@RestController
@RequestMapping(value = "/api/empresa")
public class EmpresaRestController {

	
	@Autowired
	private EmpresaDAO empresaDAO;
	
	@Autowired
	private GiroNegocioDAO giroNegocioDAO;
	
	@Autowired
	private SucursalDAO sucursalDAO;
	
	@Autowired
	private ServicioDAO servicioDAO;

	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(Empresa empresa) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(empresaDAO.listFullByParams( empresa, new String[]{"emp.id"}) );
		
		return result;
	}
	
	@RequestMapping(value = "/listarGiroNegocio/{id_emp}")
	public AjaxResponseBody listarGiroNegocio(@PathVariable Integer id_emp) {

		AjaxResponseBody result = new AjaxResponseBody();
		
		 
		result.setResult(giroNegocioDAO.listFullByParams( new Param("id_emp",id_emp), new String[]{"gir.id"}) );
		
		return result;
	}
	
	@RequestMapping(value = "/listarLocales/{id_emp}")
	public AjaxResponseBody listarLocales(@PathVariable Integer id_emp) {

		AjaxResponseBody result = new AjaxResponseBody();
		
		 
		result.setResult(sucursalDAO.listFullByParams( new Param("id_emp",id_emp), new String[]{"suc.id"}) );
		
		return result;
	}
	
	@RequestMapping(value = "/listarServicios/{id_suc}/{id_gir}")
	public AjaxResponseBody listarServicios(@PathVariable Integer id_suc,@PathVariable Integer id_gir) {

		AjaxResponseBody result = new AjaxResponseBody();
		
		 Param param = new Param();
		 param.put("id_suc", id_suc);
		 param.put("id_gir", id_gir);
		result.setResult(servicioDAO.listFullByParams( param, new String[]{"srv.id"}) );
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(Empresa empresa) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( empresaDAO.saveOrUpdate(empresa) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/grabarGiroNegocio", method = RequestMethod.POST)
	public AjaxResponseBody grabarGiroNegocio(GiroNegocio giroNegocio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( giroNegocioDAO.saveOrUpdate(giroNegocio) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/grabarSucursal", method = RequestMethod.POST)
	public AjaxResponseBody grabarSucursal(Sucursal sucursal) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( sucursalDAO.saveOrUpdate(sucursal) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/grabarServicio", method = RequestMethod.POST)
	public AjaxResponseBody grabarServicio(Servicio servicio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( servicioDAO.saveOrUpdate(servicio) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			empresaDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="eliminarGiroNegocio/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminarGiroNegocio(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			giroNegocioDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="eliminarSucursal/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminarSucursal(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			sucursalDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="eliminarServicio/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminarServicio(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			servicioDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			List<Empresa> datos_empresa=empresaDAO.listFullByParams(new Param("emp.id",id),null);
			Empresa empresa= null;
			if(datos_empresa.size()>0)
				empresa=datos_empresa.get(0);
			result.setResult(empresa);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="datosGiroNegocio/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalleGiroNegocio(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult(giroNegocioDAO.get(id));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/obtenerDatosGiroNegocio", method = RequestMethod.GET)
	public AjaxResponseBody obtenerDatosGiroNegocio(Integer id_giro ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( empresaDAO.datosGiroNegocio(id_giro) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/obtenerDatosLocal/{id_suc}", method = RequestMethod.GET)
	public AjaxResponseBody obtenerDatosLocal(@PathVariable Integer id_suc) {

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

	}
	
	@RequestMapping( value="/obtenerDatosServicio/{id}", method = RequestMethod.GET)
	public AjaxResponseBody obtenerDatosServicio(@PathVariable Integer id) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( servicioDAO.get(id));
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
