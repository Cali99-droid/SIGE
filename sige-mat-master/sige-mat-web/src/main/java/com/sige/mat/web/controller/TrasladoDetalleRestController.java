package com.sige.mat.web.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;

import net.sf.jasperreports.engine.type.OnErrorTypeEnum;

import com.sige.invoice.bean.Impresion;
import com.sige.mat.dao.AcademicoPagoDAO;
import com.sige.mat.dao.ConfMensualidadDAO;
import com.sige.mat.dao.LecturaBarrasDAO;
import com.sige.mat.dao.MatriculaDAO;
import com.sige.mat.dao.SituacionMatDAO;
import com.sige.mat.dao.TrasladoDetalleDAO;
import com.sige.spring.service.FacturacionService;
import com.tesla.colegio.model.AcademicoPago;
import com.tesla.colegio.model.Matricula;
import com.tesla.colegio.model.SitHistorial;
import com.tesla.colegio.model.TrasladoDetalle;
import com.tesla.colegio.model.Usuario;


@RestController
@RequestMapping(value = "/api/trasladoDetalle")
public class TrasladoDetalleRestController {
	
	@Autowired
	private TrasladoDetalleDAO traslado_detalleDAO;
	
	@Autowired
	private AcademicoPagoDAO academicoPagoDAO;

	@Autowired
	private LecturaBarrasDAO lecturaBarrasDAO;
	
	@Autowired
	private SituacionMatDAO situacion_matDAO;
	
	@Autowired
	private FacturacionService facturacionService;
	
	@Autowired
	private ConfMensualidadDAO confMensualidadDAO;
	
	@Autowired
	private MatriculaDAO matriculaDAO;

	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		
		result.setResult(traslado_detalleDAO.listFullByParams(new Param("per.id_anio",id_anio), new String[]{"mtd.fec_ins"}));
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(TrasladoDetalle trasladoDetalle, SitHistorial sitHistorial, Integer [] id_fco,Integer id_suc, HttpSession session) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			
			List<AcademicoPago> academicoPagos=mesesDeuda(trasladoDetalle.getId_mat());
			
 			 sitHistorial.setEst("A");
			 trasladoDetalle.setEst("A");
			Impresion impresion = facturacionService.grabarTraslado(academicoPagos, trasladoDetalle, sitHistorial, id_fco, id_suc);
			result.setResult(impresion );

		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			traslado_detalleDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( traslado_detalleDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping(value = "/obtenerMontoPago/{id_mat}", method = RequestMethod.GET)
	public AjaxResponseBody obtenerMontoPago(@PathVariable Integer id_mat) throws Exception{
		AjaxResponseBody result = new AjaxResponseBody();

		
		List<AcademicoPago> meses_atrasados=mesesDeuda(id_mat);
				
		Map<String, Object> map = new HashMap<String, Object>();		
		map.put("meses_deuda", meses_atrasados);
		//map.put("deuda", deuda);
		
		result.setResult(map);
		
		return result;

	}
	
	public List<AcademicoPago> mesesDeuda(Integer id_mat){
		//Obtener ultimo dia de asistencia
				Date ultimaAsistencia = lecturaBarrasDAO.getUltimaAsistencia(id_mat);
				List<AcademicoPago> meses_atrasados=new ArrayList<>();
				if(ultimaAsistencia!=null){
					//Obtener fecha actual
					Date fec=new Date();
					
					 LocalDate localDate = ultimaAsistencia.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				     int anio_actual  = localDate.getYear();
				     int mes_asi = localDate.getMonthValue();
					
					//Obtengo las mensualidades que adeuda
					Param param_pago = new Param();
					param_pago.put("id_mat", id_mat);
					param_pago.put("tip", "MEN");	
					param_pago.put("canc", "0");
					param_pago.put("est", "A");
					List<AcademicoPago> meses_pagos=academicoPagoDAO.listByParams(param_pago, new String[]{"mens"});
					Matricula matricula = new Matricula();
					matricula.setId(id_mat);
					int anio_ult_mat = academicoPagoDAO.obtenerAnioPeriodo(id_mat).getInteger("anio");
					Integer id_per=matriculaDAO.getByParams(new Param("id", id_mat)).getId_per();
					Calendar cal = Calendar.getInstance();
					//int anio_actual = cal.get(Calendar.YEAR);
					SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

					String format = formatter.format(new Date());
					int fecActual = Integer.parseInt(format);
					
					//Obtengo la cuota mensual
					BigDecimal cuota_men=situacion_matDAO.obtenerMonto(id_mat).get(0).getBigDecimal("cuota");
					
				
					
					for (AcademicoPago meses_pagar : meses_pagos) {
						int fecVencimiento =  getFecVencimiento(anio_ult_mat, meses_pagar.getMens(), id_per);
														
							if (fecActual>fecVencimiento && meses_pagar.getMens()<=mes_asi){
								if(meses_pagar.getMens()==mes_asi){
									//int dias_asi=localDate.getDayOfMonth();
									Integer dia_mora=confMensualidadDAO.getByParams(new Param("id_per", id_per)).getDia_mora();
									int dias_asi=ultimaAsistencia.getDate()-dia_mora;
									if(dias_asi>0){
									BigDecimal nueva_men=(cuota_men.divide(new BigDecimal("30"), 1,RoundingMode.CEILING)).multiply(new BigDecimal(dias_asi));
									meses_pagar.setMonto(nueva_men);
									meses_atrasados.add(meses_pagar);
									}
								} else{
									meses_atrasados.add(meses_pagar);
								}
								
							} else if(fecActual<fecVencimiento && meses_pagar.getMens()==mes_asi){
								Integer dia_mora=confMensualidadDAO.getByParams(new Param("id_per",id_per)).getDia_mora();
								int dias_asi=ultimaAsistencia.getDate()-dia_mora;
								if(dias_asi>0){
									BigDecimal nueva_men=(cuota_men.divide(new BigDecimal("30"), 1,RoundingMode.CEILING)).multiply(new BigDecimal(dias_asi));
									meses_pagar.setMonto(nueva_men);
									meses_atrasados.add(meses_pagar);
								}
							}
					}
				}
				
				
		return meses_atrasados;		
	}
	
	private int getFecVencimiento(int anio, int mes, int id_per) {

		int anioSiguiente;
		int mesSiguiente;

		if (mes == 12) {
			anioSiguiente = anio + 1;
			mesSiguiente = 1;
		} else {
			anioSiguiente = anio;
			mesSiguiente = mes + 1;
		}
		
		Integer dia_mora=confMensualidadDAO.getByParams(new Param("id_per", id_per)).getDia_mora();

		String fecVenc = anioSiguiente + String.format("%02d", mesSiguiente) + dia_mora;

		return Integer.parseInt(fecVenc);

	}

	
	
	@RequestMapping(value = "/validarAnioEstudios/{id_mat}/{id_alu}", method = RequestMethod.GET)
	public AjaxResponseBody validarUltimoAnioEstudios(@PathVariable Integer id_mat, @PathVariable Integer id_alu) throws Exception{
		AjaxResponseBody result = new AjaxResponseBody();

		
		if(traslado_detalleDAO.validarUltimoAnioEstudios(id_mat, id_alu))
			result.setResult("1");
		else 
			result.setResult("0");
		
		return result;

	}
}
