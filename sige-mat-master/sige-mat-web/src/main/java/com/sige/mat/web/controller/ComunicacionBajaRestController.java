package com.sige.mat.web.controller;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sige.invoice.bean.ComunicacionBajaBean;
import com.sige.invoice.bean.ComunicacionBajaItemBean;
import com.sige.invoice.bean.Impresion;
import com.sige.invoice.bean.SunatResultJson;
import com.sige.mat.dao.ComunicacionBajaDAO;
import com.sige.mat.dao.MovimientoDAO;
import com.sige.spring.service.FacturacionService;
import com.tesla.colegio.model.ComunicacionBaja;
import com.tesla.colegio.model.Movimiento;
import com.tesla.colegio.model.bean.NotaCredito;
import com.tesla.frmk.common.exceptions.ControllerException;
import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.util.FechaUtil;

@RestController
@RequestMapping(value = "/api/comunicacionBaja")
public class ComunicacionBajaRestController {
	final static Logger logger = Logger.getLogger(ComunicacionBajaRestController.class);

	@Autowired
	private FacturacionService facturacionService;

	@Autowired
	private ComunicacionBajaDAO comunicacionBajaDAO;
	
	@Autowired
	private MovimientoDAO movimientoDAO;
	
	@RequestMapping(value = "/consulta")
	public AjaxResponseBody getLista(String cliente,String alumno, Integer id_suc) {

		AjaxResponseBody result = new AjaxResponseBody();
		try {
			result.setResult(comunicacionBajaDAO.consultarReporteCaja(cliente, alumno, id_suc) );
			
		} catch (Exception e) {
			result.setException(e);
		}
		return result;

	}

	/**
	 * Graba fac_comunicacion_baja
	 * @param comunicacionBaja
	 * @return
	 */
	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(ComunicacionBaja comunicacionBaja) {

		AjaxResponseBody result = new AjaxResponseBody();
		try {
			
			comunicacionBaja.setFec_emi(new Date());
			comunicacionBaja.setEst("A");
			
			result.setResult(comunicacionBajaDAO.saveOrUpdate(comunicacionBaja));
			
		} catch (Exception e) {
			result.setException(e);
			
		}
		return result;

	}
	
	/**
	 * Enviar a sunat
	 * @param comunicacionBaja
	 * @return
	 */
	/*
	@RequestMapping(value = "/enviarSunat")
	public AjaxResponseBody enviarSunat(ComunicacionBaja comunicacionBajaDTO)throws ControllerException {

		Movimiento movimiento = movimientoDAO.get(comunicacionBajaDTO.getId_fmo());
		AjaxResponseBody result = new AjaxResponseBody();
		
		ComunicacionBaja comunicacionBaja = comunicacionBajaDAO.get(comunicacionBajaDTO.getId());
		
		ComunicacionBajaBean comunicacionBajaBean = new ComunicacionBajaBean();
		comunicacionBajaBean.setFechaCreacion(FechaUtil.toString(comunicacionBaja.getFec_emi(), "dd/MM/yyyy"));
		comunicacionBajaBean.setRazonSocial("ASOCIACION EDUCATIVA LUZ Y CIENCIA");
		comunicacionBajaBean.setRuc("20531084587");
		
		ComunicacionBajaItemBean[] items = new ComunicacionBajaItemBean[1];
		ComunicacionBajaItemBean item = new ComunicacionBajaItemBean();
		String[] arrRecibo = movimiento.getNro_rec().split("-");
		item.setLinea(1);
		item.setMotivo(comunicacionBajaDTO.getMotivo());
		item.setSerieDocumento(arrRecibo[0]);
		item.setNumDocumento(arrRecibo[1]);
		
		String tipoDocumento = null;
		if(movimiento.getTipo().equals("I"))
			tipoDocumento = "03";//BOLETA
		else if(movimiento.getTipo().equals("N"))
			tipoDocumento = "07";//NOTA DE CREDITO
		
		if(tipoDocumento==null)
			throw new ControllerException("El documento no es boleta ni nota de credito.");
		
		item.setTipoDocumento(tipoDocumento);
		
		items[0] = item;
		
		comunicacionBajaBean.setItems(items);
		
		try {
			
			logger.debug("enviando:" + comunicacionBajaBean.toString());
			SunatResultJson resultSunat = facturacionService.enviarComunicacionBajaSunat(comunicacionBajaBean);
			String code = resultSunat.getCode();
			
			if (code.equals("0")) {
				
				//PASO CORRECTAMENTE
				comunicacionBajaDAO.actualizarCodRes(comunicacionBajaDTO.getId(),resultSunat.getTicket(),Integer.parseInt(resultSunat.getId_eiv()), resultSunat.getRespuesta_sunat());

			}else{
				//ERROR EN EL ENVIO A SUNAT
				result.setCode(code);
				result.setMsg(resultSunat.getRespuesta_sunat());
			}
			logger.debug(resultSunat);
			
			result.setResult(resultSunat);
		} catch (Throwable e) {
			logger.error("error envio a sunat",e);
			result.setCode("500");
			result.setMsg(e.getMessage());
			
		}
		return result;

	}
	*/

	
	/**
	 * Enviar a sunat
	 * @param nota de credito
	 * @return
	 */
	@RequestMapping(value = "/enviarSunat")
	public AjaxResponseBody enviarSunat(ComunicacionBaja comunicacionBaja)throws ControllerException {

		AjaxResponseBody result = new AjaxResponseBody();
		
		try {
			
			List<Impresion> impresiones = facturacionService.obtenerComunicacionBaja(comunicacionBaja);
			
			logger.debug("enviando:" + impresiones.toString());
			SunatResultJson resultSunat = facturacionService.enviarComunicacionBajaRestSunat(impresiones);
			
			//System.out.println(resultSunat);
			String code = resultSunat.getCode();
			
			if (code.equals("0")) {
				
				//PASO CORRECTAMENTE
			
				//for (Impresion impresion : impresiones) {
					
					comunicacionBajaDAO.actualizarCodRes(comunicacionBaja.getId(), resultSunat.getRespuesta_sunat(), Integer.parseInt(resultSunat.getId_eiv()), resultSunat.getArchivo(), comunicacionBaja.getId_fmo());
					 
					///notaCreditoDAO.actualizarCodRes(notaCreditoForm.getId(), resultSunat.getRespuesta_sunat(), Integer.parseInt(resultSunat.getId_eiv()), resultSunat.getArchivo(), impresiones.get(0).getCabecera().getNro());
					
					//movimientoDAO.actualizarNCCodRes(notaCreditoForm, impresion.getCabecera().getNro(), resultSunat.getArchivo(), resultSunat.getId_eiv(), resultSunat.getRespuesta_sunat(),FechaUtil.toDate(impresion.getCabecera().getDia(), "dd/MM/yyyy") );	
				//}
				
				
				//comunicacionBajaDAO.actualizarCodRes(comunicacionBajaDTO.getId(),resultSunat.getTicket(),Integer.parseInt(resultSunat.getId_eiv()), resultSunat.getRespuesta_sunat());

			}else{
				//ERROR EN EL ENVIO A SUNAT
				result.setCode(code);
				result.setMsg(resultSunat.getRespuesta_sunat());
			}
			logger.debug(resultSunat);
			
			result.setResult(resultSunat);
		} catch (Throwable e) {
			logger.error("error envio a sunat",e);
			result.setCode("500");
			result.setMsg(e.getMessage());
			
		}
		return result;

	}

}
