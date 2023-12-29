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

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;
import com.sige.common.enums.EnumSituacionFinal;
import com.sige.mat.dao.AcademicoPagoDAO;
import com.sige.mat.dao.ConfMensualidadDAO;
import com.sige.mat.dao.LecturaBarrasDAO;
import com.sige.mat.dao.MatriculaDAO;
import com.sige.mat.dao.SituacionMatDAO;
import com.sige.spring.service.PagosService;
import com.tesla.colegio.model.AcademicoPago;
import com.tesla.colegio.model.Alumno;
import com.tesla.colegio.model.Aula;
import com.tesla.colegio.model.ColSituacion;
import com.tesla.colegio.model.Curso;
import com.tesla.colegio.model.Grad;
import com.tesla.colegio.model.Matricula;
import com.tesla.colegio.model.Nivel;
import com.tesla.colegio.model.SituacionMat;


@RestController
@RequestMapping(value = "/api/situacionMat")
public class SituacionMatRestController {
	final static Logger logger = Logger.getLogger(SituacionMatRestController.class);
	@Autowired
	private SituacionMatDAO situacion_matDAO;
	
	@Autowired
	private AcademicoPagoDAO academicoPagoDAO;

	@Autowired
	private LecturaBarrasDAO lecturaBarrasDAO;
	
	@Autowired
	private ConfMensualidadDAO confMensualidadDAO;
	
	@Autowired
	private MatriculaDAO matriculaDAO;
	
	@Autowired
	private PagosService pagosService;

	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(SituacionMat situacion_mat) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(situacion_matDAO.listFullByParams( situacion_mat, new String[]{"csm.id"}) );
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(SituacionMat situacion_mat) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			//Grabamos el traslado
			 situacion_matDAO.saveOrUpdate(situacion_mat);
			 
			//Actualizar su pago
			 Date fec=situacion_mat.getFec();
		     LocalDate localDate = fec.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		     int anio  = localDate.getYear();
		     int mes = localDate.getMonthValue();
		     int dia   = localDate.getDayOfMonth();
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date fechaInicial;
			Integer mes_up;
			//Solamente si es traslado o retiro
			if(	situacion_mat.getId_sit()==EnumSituacionFinal.TRASLADADO.getValue() || 
				situacion_mat.getId_sit()==EnumSituacionFinal.RETIRADO.getValue()){
				
				//Obtener ultimo dia de asistencia
				Date ultimaAsistencia = lecturaBarrasDAO.getUltimaAsistencia(situacion_mat.getId_mat());
				
				//Obtener ultimo dia de pago
				
				
				if(dia>=8){
					 fechaInicial=dateFormat.parse(anio+"-"+mes+"-08");
					 mes_up=mes;
				} else{
					 fechaInicial=dateFormat.parse(anio+"-"+(mes-1)+"-08");
					 mes_up=mes-1;
				}	
				int dias_asis=(int) ((fec.getTime()-fechaInicial.getTime())/86400000);
				logger.debug(fechaInicial);
				logger.debug(dias_asis);
				
				Param param = new Param();
				param.put("id_mat", situacion_mat.getId_mat());
				param.put("tip","MEN");
				List<AcademicoPago> mensualidades= academicoPagoDAO.listByParams(param, new String[]{"mens"});
				BigDecimal cuota_men=situacion_matDAO.obtenerMonto(situacion_mat.getId_mat()).get(0).getBigDecimal("cuota");
				for (AcademicoPago academicoPago : mensualidades) {
					if(academicoPago.getMens()==mes_up){
						BigDecimal nueva_men=(cuota_men.divide(new BigDecimal("30"), 2,RoundingMode.CEILING)).multiply(new BigDecimal(dias_asis));
						situacion_matDAO.actualizaPago(academicoPago.getId_mat(), academicoPago.getMens(), nueva_men);
					} else if(academicoPago.getMens()>mes_up){
						situacion_matDAO.deshabilitaPago(academicoPago.getId_mat(), academicoPago.getMens());
					} else if(academicoPago.getMens()<mes_up){
						situacion_matDAO.actualizaPago(academicoPago.getId_mat(), academicoPago.getMens(), cuota_men);
					}
					
				}
			}
			
			result.setResult(1);
		} catch (Exception e) {
			result.setException(e);

		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			situacion_matDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( situacion_matDAO.getFull(id, new String[]{ColSituacion.TABLA, SituacionMat.TABLA, Matricula.TABLA,Nivel.TABLA,Grad.TABLA,Aula.TABLA, Alumno.TABLA})  );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping(value = "/obtenerMontoPago/{id_mat}", method = RequestMethod.GET)
	public AjaxResponseBody obtenerMontoPago(@PathVariable Integer id_mat) throws Exception{
		AjaxResponseBody result = new AjaxResponseBody();

		//Obtener ultimo dia de asistencia
		Date ultimaAsistencia = lecturaBarrasDAO.getUltimaAsistencia(id_mat);
		
		//Obtener fecha actual
		Date fec=new Date();
		LocalDate localDate = null;
		int mes_asi=0;
		if (ultimaAsistencia!=null){
		 localDate = ultimaAsistencia.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	     int anio_asi  = localDate.getYear();
	     mes_asi = localDate.getMonthValue();
		} else {
			Date fec_actual=new Date();
			mes_asi= fec_actual.getMonth()+1;
		}
		//Obtengo las mensualidades que adeuda
		Param param_pago = new Param();
		param_pago.put("id_mat", id_mat);
		param_pago.put("tip", "MEN");	
		param_pago.put("canc", "0");
		param_pago.put("est", "A");
		List<AcademicoPago> meses_pagos=academicoPagoDAO.listByParams(param_pago, new String[]{"mens"});
		Matricula matricula = new Matricula();
		matricula.setId(id_mat);
		Matricula matricula_alu=matriculaDAO.getByParams(new Param("id",id_mat));
		Integer anio_mat = academicoPagoDAO.obtenerAnioPeriodo(id_mat).getInteger("anio");
		Calendar cal = Calendar.getInstance();
		Integer anio_actual = cal.get(Calendar.YEAR);
		

		//Calendar cal = Calendar.getInstance();
		//int anio_actual = cal.get(Calendar.YEAR);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

		String format = formatter.format(new Date());
		Integer fecActual = Integer.parseInt(format);
		
		//Obtengo la cuota mensual
		BigDecimal cuota_men=situacion_matDAO.obtenerMonto(id_mat).get(0).getBigDecimal("cuota");
		
		List<AcademicoPago> meses_atrasados=new ArrayList<>();
		//if(ultimaAsistencia!=null){ no funciona para el virtual
			for (AcademicoPago meses_pagar : meses_pagos) {
				Integer fecVencimiento =  getFecVencimiento(anio_mat, meses_pagar.getMens(),matricula_alu.getId_per());

				if (fecActual>fecVencimiento && meses_pagar.getMens()<=mes_asi && anio_actual.equals(anio_mat)){
					if(meses_pagar.getMens()==mes_asi){
						//int dias_asi=localDate.getDayOfMonth();
						Integer dia_mora=confMensualidadDAO.getByParams(new Param("id_per", matricula_alu.getId_per())).getDia_mora();
						Integer dias_asi =0;
						//int dias_asi=ultimaAsistencia.getDate()-dia_mora;
						if(ultimaAsistencia!=null) {
							dias_asi=ultimaAsistencia.getDate()-dia_mora;
						}
						 
						if(dias_asi>0){
						BigDecimal nueva_men=(cuota_men.divide(new BigDecimal("30"), 1,RoundingMode.CEILING)).multiply(new BigDecimal(dias_asi));
						meses_pagar.setMonto(nueva_men);
						meses_atrasados.add(meses_pagar);
						} else {
							meses_pagar.setMonto(cuota_men);
							meses_atrasados.add(meses_pagar);
						}
					} else{
						meses_atrasados.add(meses_pagar);
					}
					
				} else if(fecActual<fecVencimiento && meses_pagar.getMens()==mes_asi  && anio_actual.equals(anio_mat)){
					if(!meses_pagar.getMes().equals(0)) {
						fecVencimiento = pagosService.getFecVencimientoFin(meses_pagar.getId_mat(), meses_pagar.getMens(),"MEN");
					} else {
						fecVencimiento = pagosService.getFecVencimientoFin(meses_pagar.getId_mat(), meses_pagar.getMens(),"MEN");
					}
				//	Integer dia_mora=confMensualidadDAO.getByParams(new Param("id_per", matricula_alu.getId_per())).getDia_mora();
					Integer dia_mora=Integer.parseInt(fecVencimiento.toString().substring(6,8));
					if(ultimaAsistencia!=null) {
						int dias_asi=ultimaAsistencia.getDate()-dia_mora;
						if(dias_asi>0){
							BigDecimal nueva_men=(cuota_men.divide(new BigDecimal("30"), 1,RoundingMode.CEILING)).multiply(new BigDecimal(dias_asi));
							meses_pagar.setMonto(nueva_men);
							meses_atrasados.add(meses_pagar);
						}
					} else {
						int dias_asi=dia_mora-Integer.parseInt(fecActual.toString().substring(6,8));
						if(dias_asi>0){
							BigDecimal nueva_men=(cuota_men.divide(new BigDecimal("30"), 1,RoundingMode.CEILING)).multiply(new BigDecimal(dias_asi));
							meses_pagar.setMonto(nueva_men);
							meses_atrasados.add(meses_pagar);
						}
					}
					
				} else if (fecActual>fecVencimiento && anio_mat<anio_actual){
					if(meses_pagar.getMens()==mes_asi){
						//int dias_asi=localDate.getDayOfMonth();
						Integer dia_mora=confMensualidadDAO.getByParams(new Param("id_per", matricula_alu.getId_per())).getDia_mora();
						Integer dias_asi =0;
						//int dias_asi=ultimaAsistencia.getDate()-dia_mora;
						if(ultimaAsistencia!=null) {
							dias_asi=ultimaAsistencia.getDate()-dia_mora;
						}
						 
						if(dias_asi>0){
						BigDecimal nueva_men=(cuota_men.divide(new BigDecimal("30"), 1,RoundingMode.CEILING)).multiply(new BigDecimal(dias_asi));
						meses_pagar.setMonto(nueva_men);
						meses_atrasados.add(meses_pagar);
						}
					} else{
						meses_atrasados.add(meses_pagar);
					}
					
				}
				
				/*else if(fecActual<fecVencimiento && meses_pagar.getMens()>mes_asi){
					
				} else {
					meses_atrasados.add(meses_pagar);
				}*/
			}	
		//}		
				
		Map<String, Object> map = new HashMap<String, Object>();		
		map.put("meses_deuda", meses_atrasados);
		//map.put("deuda", deuda);
		
		result.setResult(map);
		
		return result;

	}
	
	
	
	/*private int getFecVencimiento(int anio, int mes) {
		
		int anioSiguiente;
		int mesSiguiente;
		
		if (mes==12){
			anioSiguiente = anio + 1;
			mesSiguiente = 1;
		}else{
			anioSiguiente = anio;
			mesSiguiente = mes+1;
		}
		
		String fecVenc = anioSiguiente + String.format("%02d", mesSiguiente) + "07";//TODO PARAMETRIZAR FECHA
		
		
		return Integer.parseInt(fecVenc);
		
	}*/
	
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
		String fecVenc = anioSiguiente + String.format("%02d", mesSiguiente) + dia_mora;// TODO
																					// PARAMETRIZAR
																					// FECHA

		return Integer.parseInt(fecVenc);

	}
}
