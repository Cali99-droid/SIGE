package com.sige.mat.web.controller;

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sige.invoice.bean.ComunicacionBajaBean;
import com.sige.invoice.bean.ComunicacionBajaItemBean;
import com.sige.invoice.bean.DocumentoReferencia;
import com.sige.invoice.bean.Impresion;
import com.sige.invoice.bean.ImpresionCabecera;
import com.sige.invoice.bean.ImpresionCliente;
import com.sige.invoice.bean.ImpresionItem;
import com.sige.invoice.bean.SunatResultJson;
import com.sige.mat.dao.AcademicoPagoDAO;
import com.sige.mat.dao.AlumnoDAO;
import com.sige.mat.dao.ComunicacionBajaDAO;
import com.sige.mat.dao.ConfReciboDAO;
import com.sige.mat.dao.EmpresaDAO;
import com.sige.mat.dao.MatriculaDAO;
import com.sige.mat.dao.MovimientoDAO;
import com.sige.mat.dao.MovimientoDetalleDAO;
import com.sige.mat.dao.NotaCreditoDAO;
import com.sige.mat.dao.PeriodoDAO;
import com.sige.mat.dao.PersonaDAO;
import com.sige.mat.dao.ReservaDAO;
import com.sige.mat.dao.SucursalDAO;
import com.sige.spring.service.FacturacionService;
import com.sige.spring.service.ImpresionService;
import com.sige.web.security.TokenSeguridad;
import com.tesla.colegio.model.AcademicoPago;
import com.tesla.colegio.model.Alumno;
import com.tesla.colegio.model.ComunicacionBaja;
import com.tesla.colegio.model.Matricula;
import com.tesla.colegio.model.Movimiento;
import com.tesla.colegio.model.MovimientoDetalle;
import com.tesla.colegio.model.NotaCredito;
import com.tesla.colegio.model.Periodo;
import com.tesla.colegio.model.Persona;
import com.tesla.colegio.model.Reserva;
import com.tesla.colegio.model.Sucursal;
import com.tesla.colegio.model.UsuarioSeg;
import com.tesla.colegio.util.Constante;
import com.tesla.frmk.common.exceptions.ControllerException;
import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;
import com.tesla.frmk.util.FechaUtil;


@RestController
@RequestMapping(value = "/api/notaCredito")
public class NotaCreditoRestController {
	final static Logger logger = Logger.getLogger(NotaCreditoRestController.class);

	@Autowired
	private FacturacionService facturacionService;

 	@Autowired
	private MovimientoDAO movimientoDAO;
	
 	@Autowired
	private NotaCreditoDAO notaCreditoDAO;
 	
 	@Autowired
	private EmpresaDAO empresaDAO;
 	
 	@Autowired
	private TokenSeguridad tokenSeguridad;
 	
 	@Autowired
	private SucursalDAO sucursalDAO;
	
 	@Autowired
	private ImpresionService impresionService;
 	
 	@Autowired
	private MatriculaDAO matriculaDAO;
 	
 	@Autowired
	private AlumnoDAO alumnoDAO;
 	
 	@Autowired
	private PersonaDAO personaDAO;
 	
 	@Autowired
	private MovimientoDetalleDAO movimientoDetalleDAO;
 	
 	@Autowired
	private PeriodoDAO periodoDAO;
 	
	@Autowired
	private ConfReciboDAO confReciboDAO;
	
	@Autowired
	private AcademicoPagoDAO academicoPagoDAO;
	
	@Autowired
	private ReservaDAO reservaDAO;
	/**
	 * Graba fac_comunicacion_baja
	 * @param comunicacionBaja
	 * @return
	 */
	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(com.tesla.colegio.model.NotaCredito notaCreditoForm) {

		AjaxResponseBody result = new AjaxResponseBody();
		
		try {
			
			notaCreditoForm.setFec_emi(new Date());
			notaCreditoForm.setEst("A");//GRABADO, TIENE 3 ESTADOS A: GRABADO, S: ACEPTADO SUNAT, E: ERROR
			
			result.setResult(notaCreditoDAO.saveOrUpdate(notaCreditoForm));
			
			

		} catch (Exception e) {
			result.setException(e);
			
		}
		return result;

	}
	
	/**
	 * Enviar a sunat
	 * @param nota de credito
	 * @return
	 */
	@RequestMapping(value = "/enviarSunat")
	@ResponseBody
	public AjaxResponseBody enviarSunat(NotaCredito notaCreditoForm,Integer id_mat, Integer id_res, HttpServletResponse response)throws ControllerException {

		AjaxResponseBody result = new AjaxResponseBody();
		
		try {
			
			List<Impresion> impresiones = facturacionService.obtenerNotaCredito(notaCreditoForm);
			//Busco si ya existe insertado el movimiento
			//Movimiento mov_existe = movimientoDAO.getByParams(new Param("nro_rec",impresiones.get(0).getCabecera().getNro()));
			//if(notaCreditoForm.getId_fmo()!=null);
			//Movimiento mov_existe = movimientoDAO.getByParams(new Param("nro_rec",impresiones.get(0).getCabecera().getNro()));
			//Obtenemos la nota de Credito
		//	if(notaCreditoForm.getId()!=null) {
				NotaCredito nota_credito=notaCreditoDAO.get(notaCreditoForm.getId());
				if(nota_credito!=null) {
					Integer id_fmo_nc=nota_credito.getId_fmo_nc();
					if(id_fmo_nc==null || id_fmo_nc.equals(0)) {
							//Movimiento afectado
							Movimiento mov_afec = movimientoDAO.get(notaCreditoForm.getId_fmo());
							if(id_mat==null) {
								if(mov_afec.getId_mat()!=null && !mov_afec.getId_mat().equals(0)) {
									id_mat=mov_afec.getId_mat();
								} else {
									if(id_mat==null) {
										//Busco el Academico Pafo
										AcademicoPago academicoPago = academicoPagoDAO.getByParams(new Param("nro_rec",mov_afec.getNro_rec()));
										if(academicoPago!=null) {
											id_mat=academicoPago.getId_mat();
										}
									}
								}
							}
							if(id_res!=null) {
								Reserva reserva = reservaDAO.get(id_res);
								//Matricula matricula = matriculaDAO.get(id_mat);
								Periodo periodo = periodoDAO.getByParams(new Param("id",reserva.getId_per()));
								Alumno alumno = alumnoDAO.get(reserva.getId_alu());
								Persona persona = personaDAO.get(alumno.getId_per());
								//Grabar la NC en movimientos
								Movimiento movimiento_nc = new Movimiento();
								movimiento_nc.setTipo("S");
								movimiento_nc.setFec(new Date());
								movimiento_nc.setId_suc(periodo.getId_suc());
							//	movimiento_nc.setId_mat(matricula.getId());
								movimiento_nc.setId_fam(mov_afec.getId_fam());
								movimiento_nc.setId_fpa(1);
								movimiento_nc.setMonto(notaCreditoForm.getMonto());
								movimiento_nc.setDescuento(new BigDecimal(0));
								movimiento_nc.setMonto_total(notaCreditoForm.getMonto());
								movimiento_nc.setNro_rec(impresiones.get(0).getCabecera().getNro());
								movimiento_nc.setObs("DEVOLUCIÓN DE DINERO POR "+notaCreditoForm.getMotivo());
								movimiento_nc.setEst("A");
								Integer id_mov=movimientoDAO.saveOrUpdate(movimiento_nc);
								//Insertamos el detalle
								MovimientoDetalle movimiento_det = new MovimientoDetalle();
								movimiento_det.setId_fmo(id_mov);
								movimiento_det.setId_fco(28);
								movimiento_det.setMonto(notaCreditoForm.getMonto());
								movimiento_det.setDescuento(new BigDecimal(0));
								movimiento_det.setMonto_total(notaCreditoForm.getMonto());
								movimiento_det.setObs("DEVOLUCIÓN DE DINERO POR "+notaCreditoForm.getMotivo()+" - ALUMNO "+persona.getApe_pat()+" "+persona.getApe_mat()+" "+persona.getNom());
								movimiento_det.setEst("A");
								movimientoDetalleDAO.saveOrUpdate(movimiento_det);
								
								//Actualizar el movimiento al la NC
								notaCreditoDAO.actualizarMovimientoNC(notaCreditoForm.getId(), id_mov);
								
									//notaCreditoDAO.actualizarCodRes(notaCreditoForm.getId(), resultSunat.getRespuesta_sunat(), Integer.parseInt(resultSunat.getId_eiv()), resultSunat.getArchivo(), impresiones.get(0).getCabecera().getNro());
									notaCreditoDAO.actualizarCorrelativoNC(impresiones.get(0).getCabecera().getNro());
									
								
							} else {
								
								Matricula matricula = matriculaDAO.get(id_mat);
								Periodo periodo = periodoDAO.getByParams(new Param("id",matricula.getId_per()));
								Alumno alumno = alumnoDAO.get(matricula.getId_alu());
								Persona persona = personaDAO.get(alumno.getId_per());
								//Grabar la NC en movimientos
								Movimiento movimiento_nc = new Movimiento();
								movimiento_nc.setTipo("S");
								movimiento_nc.setFec(new Date());
								movimiento_nc.setId_suc(periodo.getId_suc());
								movimiento_nc.setId_mat(matricula.getId());
								movimiento_nc.setId_fam(mov_afec.getId_fam());
								movimiento_nc.setId_fpa(1);
								movimiento_nc.setMonto(notaCreditoForm.getMonto());
								movimiento_nc.setDescuento(new BigDecimal(0));
								movimiento_nc.setMonto_total(notaCreditoForm.getMonto());
								movimiento_nc.setNro_rec(impresiones.get(0).getCabecera().getNro());
								movimiento_nc.setObs("DEVOLUCIÓN DE DINERO POR "+notaCreditoForm.getMotivo());
								movimiento_nc.setEst("A");
								Integer id_mov=movimientoDAO.saveOrUpdate(movimiento_nc);
								//Insertamos el detalle
								MovimientoDetalle movimiento_det = new MovimientoDetalle();
								movimiento_det.setId_fmo(id_mov);
								movimiento_det.setId_fco(28);
								movimiento_det.setMonto(notaCreditoForm.getMonto());
								movimiento_det.setDescuento(new BigDecimal(0));
								movimiento_det.setMonto_total(notaCreditoForm.getMonto());
								movimiento_det.setObs("DEVOLUCIÓN DE DINERO POR "+notaCreditoForm.getMotivo()+" - ALUMNO "+persona.getApe_pat()+" "+persona.getApe_mat()+" "+persona.getNom());
								movimiento_det.setEst("A");
								movimientoDetalleDAO.saveOrUpdate(movimiento_det);
								
								//Actualizar el movimiento al la NC
								notaCreditoDAO.actualizarMovimientoNC(notaCreditoForm.getId(), id_mov);
								
									//notaCreditoDAO.actualizarCodRes(notaCreditoForm.getId(), resultSunat.getRespuesta_sunat(), Integer.parseInt(resultSunat.getId_eiv()), resultSunat.getArchivo(), impresiones.get(0).getCabecera().getNro());
									notaCreditoDAO.actualizarCorrelativoNC(impresiones.get(0).getCabecera().getNro());
									
								
							}
							
					}
				} else {
					//Integer id_fmo_nc=nota_credito.getId_fmo_nc();
					//if(id_fmo_nc==null) {
							//Movimiento afectado
							Movimiento mov_afec = movimientoDAO.getByParams(new Param("nro_rec",impresiones.get(0).getDocumentoReferencia().getNro_rec()));
							if(id_mat==null) {
								if(mov_afec.getId_mat()!=null && !mov_afec.getId_mat().equals(0)) {
									id_mat=mov_afec.getId_mat();
								} else {
									if(id_mat==null) {
										//Busco el Academico Pafo
										AcademicoPago academicoPago = academicoPagoDAO.getByParams(new Param("nro_rec",mov_afec.getNro_rec()));
										if(academicoPago!=null) {
											id_mat=academicoPago.getId_mat();
										}
									}
								}
							}
							if(id_res!=null) {
								Reserva reserva = reservaDAO.get(id_res);
								//Matricula matricula = matriculaDAO.get(id_mat);
								Periodo periodo = periodoDAO.getByParams(new Param("id",reserva.getId_per()));
								Alumno alumno = alumnoDAO.get(reserva.getId_alu());
								Persona persona = personaDAO.get(alumno.getId_per());
								//Grabar la NC en movimientos
								Movimiento movimiento_nc = new Movimiento();
								movimiento_nc.setTipo("S");
								movimiento_nc.setFec(new Date());
								movimiento_nc.setId_suc(periodo.getId_suc());
							//	movimiento_nc.setId_mat(matricula.getId());
								movimiento_nc.setId_fam(mov_afec.getId_fam());
								movimiento_nc.setId_fpa(1);
								movimiento_nc.setMonto(notaCreditoForm.getMonto());
								movimiento_nc.setDescuento(new BigDecimal(0));
								movimiento_nc.setMonto_total(notaCreditoForm.getMonto());
								movimiento_nc.setNro_rec(impresiones.get(0).getCabecera().getNro());
								movimiento_nc.setObs("DEVOLUCIÓN DE DINERO POR "+notaCreditoForm.getMotivo());
								movimiento_nc.setEst("A");
								Integer id_mov=movimientoDAO.saveOrUpdate(movimiento_nc);
								//Insertamos el detalle
								MovimientoDetalle movimiento_det = new MovimientoDetalle();
								movimiento_det.setId_fmo(id_mov);
								movimiento_det.setId_fco(28);
								movimiento_det.setMonto(notaCreditoForm.getMonto());
								movimiento_det.setDescuento(new BigDecimal(0));
								movimiento_det.setMonto_total(notaCreditoForm.getMonto());
								movimiento_det.setObs("DEVOLUCIÓN DE DINERO POR "+notaCreditoForm.getMotivo()+" - ALUMNO "+persona.getApe_pat()+" "+persona.getApe_mat()+" "+persona.getNom());
								movimiento_det.setEst("A");
								movimientoDetalleDAO.saveOrUpdate(movimiento_det);
								
								//Actualizar el movimiento al la NC
								notaCreditoDAO.actualizarMovimientoNC(notaCreditoForm.getId(), id_mov);
								
									//notaCreditoDAO.actualizarCodRes(notaCreditoForm.getId(), resultSunat.getRespuesta_sunat(), Integer.parseInt(resultSunat.getId_eiv()), resultSunat.getArchivo(), impresiones.get(0).getCabecera().getNro());
									notaCreditoDAO.actualizarCorrelativoNC(impresiones.get(0).getCabecera().getNro());
									
								
							} else {
								
								Matricula matricula = matriculaDAO.get(id_mat);
								Periodo periodo = periodoDAO.getByParams(new Param("id",matricula.getId_per()));
								Alumno alumno = alumnoDAO.get(matricula.getId_alu());
								Persona persona = personaDAO.get(alumno.getId_per());
								//Grabar la NC en movimientos
								Movimiento movimiento_nc = new Movimiento();
								movimiento_nc.setTipo("S");
								movimiento_nc.setFec(new Date());
								movimiento_nc.setId_suc(periodo.getId_suc());
								movimiento_nc.setId_mat(matricula.getId());
								movimiento_nc.setId_fam(mov_afec.getId_fam());
								movimiento_nc.setId_fpa(1);
								movimiento_nc.setMonto(notaCreditoForm.getMonto());
								movimiento_nc.setDescuento(new BigDecimal(0));
								movimiento_nc.setMonto_total(notaCreditoForm.getMonto());
								movimiento_nc.setNro_rec(impresiones.get(0).getCabecera().getNro());
								movimiento_nc.setObs("DEVOLUCIÓN DE DINERO POR "+notaCreditoForm.getMotivo());
								movimiento_nc.setEst("A");
								Integer id_mov=movimientoDAO.saveOrUpdate(movimiento_nc);
								//Insertamos el detalle
								MovimientoDetalle movimiento_det = new MovimientoDetalle();
								movimiento_det.setId_fmo(id_mov);
								movimiento_det.setId_fco(28);
								movimiento_det.setMonto(notaCreditoForm.getMonto());
								movimiento_det.setDescuento(new BigDecimal(0));
								movimiento_det.setMonto_total(notaCreditoForm.getMonto());
								movimiento_det.setObs("DEVOLUCIÓN DE DINERO POR "+notaCreditoForm.getMotivo()+" - ALUMNO "+persona.getApe_pat()+" "+persona.getApe_mat()+" "+persona.getNom());
								movimiento_det.setEst("A");
								movimientoDetalleDAO.saveOrUpdate(movimiento_det);
								
								//Actualizar el movimiento al la NC
								notaCreditoDAO.actualizarMovimientoNC(notaCreditoForm.getId(), id_mov);
								
									//notaCreditoDAO.actualizarCodRes(notaCreditoForm.getId(), resultSunat.getRespuesta_sunat(), Integer.parseInt(resultSunat.getId_eiv()), resultSunat.getArchivo(), impresiones.get(0).getCabecera().getNro());
									notaCreditoDAO.actualizarCorrelativoNC(impresiones.get(0).getCabecera().getNro());
									
								
							}
							
					//}
				}

			/*} else {
					//Movimiento afectado
					Movimiento mov_afec = movimientoDAO.get(notaCreditoForm.getId_fmo());
					if(id_mat==null) {
						if(mov_afec.getId_mat()!=null && !mov_afec.getId_mat().equals(0)) {
							id_mat=mov_afec.getId_mat();
						} else {
							if(id_mat==null) {
								//Busco el Academico Pafo
								AcademicoPago academicoPago = academicoPagoDAO.getByParams(new Param("nro_rec",mov_afec.getNro_rec()));
								if(academicoPago!=null) {
									id_mat=academicoPago.getId_mat();
								}
							}
						}
					}
					if(id_res!=null) {
						Reserva reserva = reservaDAO.get(id_res);
						//Matricula matricula = matriculaDAO.get(id_mat);
						Periodo periodo = periodoDAO.getByParams(new Param("id",reserva.getId_per()));
						Alumno alumno = alumnoDAO.get(reserva.getId_alu());
						Persona persona = personaDAO.get(alumno.getId_per());
						//Grabar la NC en movimientos
						Movimiento movimiento_nc = new Movimiento();
						movimiento_nc.setTipo("S");
						movimiento_nc.setFec(new Date());
						movimiento_nc.setId_suc(periodo.getId_suc());
					//	movimiento_nc.setId_mat(matricula.getId());
						movimiento_nc.setId_fam(mov_afec.getId_fam());
						movimiento_nc.setId_fpa(1);
						movimiento_nc.setMonto(notaCreditoForm.getMonto());
						movimiento_nc.setDescuento(new BigDecimal(0));
						movimiento_nc.setMonto_total(notaCreditoForm.getMonto());
						movimiento_nc.setNro_rec(impresiones.get(0).getCabecera().getNro());
						movimiento_nc.setObs("DEVOLUCIÓN DE DINERO POR "+notaCreditoForm.getMotivo());
						movimiento_nc.setEst("A");
						Integer id_mov=movimientoDAO.saveOrUpdate(movimiento_nc);
						//Insertamos el detalle
						MovimientoDetalle movimiento_det = new MovimientoDetalle();
						movimiento_det.setId_fmo(id_mov);
						movimiento_det.setId_fco(28);
						movimiento_det.setMonto(notaCreditoForm.getMonto());
						movimiento_det.setDescuento(new BigDecimal(0));
						movimiento_det.setMonto_total(notaCreditoForm.getMonto());
						movimiento_det.setObs("DEVOLUCIÓN DE DINERO POR "+notaCreditoForm.getMotivo()+" - ALUMNO "+persona.getApe_pat()+" "+persona.getApe_mat()+" "+persona.getNom());
						movimiento_det.setEst("A");
						movimientoDetalleDAO.saveOrUpdate(movimiento_det);
						
						//Actualizar el movimiento al la NC
						notaCreditoDAO.actualizarMovimientoNC(notaCreditoForm.getId(), id_mov);
						
							//notaCreditoDAO.actualizarCodRes(notaCreditoForm.getId(), resultSunat.getRespuesta_sunat(), Integer.parseInt(resultSunat.getId_eiv()), resultSunat.getArchivo(), impresiones.get(0).getCabecera().getNro());
							notaCreditoDAO.actualizarCorrelativoNC(impresiones.get(0).getCabecera().getNro());
							
						
					} else {
						
						Matricula matricula = matriculaDAO.get(id_mat);
						Periodo periodo = periodoDAO.getByParams(new Param("id",matricula.getId_per()));
						Alumno alumno = alumnoDAO.get(matricula.getId_alu());
						Persona persona = personaDAO.get(alumno.getId_per());
						//Grabar la NC en movimientos
						Movimiento movimiento_nc = new Movimiento();
						movimiento_nc.setTipo("S");
						movimiento_nc.setFec(new Date());
						movimiento_nc.setId_suc(periodo.getId_suc());
						movimiento_nc.setId_mat(matricula.getId());
						movimiento_nc.setId_fam(mov_afec.getId_fam());
						movimiento_nc.setId_fpa(1);
						movimiento_nc.setMonto(notaCreditoForm.getMonto());
						movimiento_nc.setDescuento(new BigDecimal(0));
						movimiento_nc.setMonto_total(notaCreditoForm.getMonto());
						movimiento_nc.setNro_rec(impresiones.get(0).getCabecera().getNro());
						movimiento_nc.setObs("DEVOLUCIÓN DE DINERO POR "+notaCreditoForm.getMotivo());
						movimiento_nc.setEst("A");
						Integer id_mov=movimientoDAO.saveOrUpdate(movimiento_nc);
						//Insertamos el detalle
						MovimientoDetalle movimiento_det = new MovimientoDetalle();
						movimiento_det.setId_fmo(id_mov);
						movimiento_det.setId_fco(28);
						movimiento_det.setMonto(notaCreditoForm.getMonto());
						movimiento_det.setDescuento(new BigDecimal(0));
						movimiento_det.setMonto_total(notaCreditoForm.getMonto());
						movimiento_det.setObs("DEVOLUCIÓN DE DINERO POR "+notaCreditoForm.getMotivo()+" - ALUMNO "+persona.getApe_pat()+" "+persona.getApe_mat()+" "+persona.getNom());
						movimiento_det.setEst("A");
						movimientoDetalleDAO.saveOrUpdate(movimiento_det);
						
						//Actualizar el movimiento al la NC
						notaCreditoDAO.actualizarMovimientoNC(notaCreditoForm.getId(), id_mov);
						
							//notaCreditoDAO.actualizarCodRes(notaCreditoForm.getId(), resultSunat.getRespuesta_sunat(), Integer.parseInt(resultSunat.getId_eiv()), resultSunat.getArchivo(), impresiones.get(0).getCabecera().getNro());
							notaCreditoDAO.actualizarCorrelativoNC(impresiones.get(0).getCabecera().getNro());
							
						
					}
			}*/
			
		

			
			logger.debug("enviando:" + impresiones.toString());
			System.out.println(impresiones.toString());
			SunatResultJson resultSunat = facturacionService.enviarNotaCreditoRestSunat(impresiones);
			String code = resultSunat.getCode();
			//Impresion impresion = new Impresion();
			if (code.equals("0")) {
				
				//PASO CORRECTAMENTE					impresionCabecera.setUsuario(usuarioSeg.getNombres());

			
				//for (Impresion impresion : impresiones) {
					
					//System.out.println(notaCreditoForm);
					 
					notaCreditoDAO.actualizarCodRes(notaCreditoForm.getId(), resultSunat.getRespuesta_sunat(), Integer.parseInt(resultSunat.getId_eiv()), resultSunat.getArchivo(), impresiones.get(0).getCabecera().getNro());
					
					//movimientoDAO.actualizarNCCodRes(notaCreditoForm, impresion.getCabecera().getNro(), resultSunat.getArchivo(), resultSunat.getId_eiv(), resultSunat.getRespuesta_sunat(),FechaUtil.toDate(impresion.getCabecera().getDia(), "dd/MM/yyyy") );
					
					
						result.setResult(notaCreditoForm.getId());
						/*File initialFile = new File(pdf);
					    InputStream is = FileUtils.openInputStream(initialFile);
						response.setContentType("application/pdf");
						response.addHeader("Content-Disposition", "attachment; filename=boleta" + impresiones.get(0).getCabecera().getNro() + ".pdf");

						IOUtils.copy(is, response.getOutputStream());*/
			//}
				///comunicacionBajaDAO.actualizarCodRes(comunicacionBajaDTO.getId(),resultSunat.getTicket(),Integer.parseInt(resultSunat.getId_eiv()), resultSunat.getRespuesta_sunat());

			}else{
				//Asi falle vamos a actualziar el recibo
				/*if(mov_existe==null) {
				//notaCreditoDAO.actualizarCodRes(notaCreditoForm.getId(), resultSunat.getRespuesta_sunat(), Integer.parseInt(resultSunat.getId_eiv()), resultSunat.getArchivo(), impresiones.get(0).getCabecera().getNro());
				notaCreditoDAO.actualizarCorrelativoNC(impresiones.get(0).getCabecera().getNro());
				}*/
				result.setResult(notaCreditoForm.getId());
				//ERROR EN EL ENVIO A SUNAT, por ahora ocultado
				//result.setCode(code);
				//result.setMsg(resultSunat.getRespuesta_sunat());
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
		
	@RequestMapping(value = "/imprimirNotaCredito")
	@ResponseBody
	public AjaxResponseBody imprimirNotaCredito(NotaCredito notaCreditoForm,HttpServletResponse response)throws ControllerException {

		AjaxResponseBody result = new AjaxResponseBody();
		
		try {
			List<Impresion> impresiones = facturacionService.obtenerNotaCredito(notaCreditoForm);
	
						String pdf =impresionService.generatePDF(impresiones.get(0)); 
						//result.setResult(impresiones.get(0));
						File initialFile = new File(pdf);
					    InputStream is = FileUtils.openInputStream(initialFile);
					    	
						
						response.setContentType("application/pdf");
						response.addHeader("Content-Disposition", "attachment; filename=boleta" + impresiones.get(0).getCabecera().getNro() + ".pdf");

						IOUtils.copy(is, response.getOutputStream());
						
		} catch (Throwable e) {
			logger.error("error envio a sunat",e);
			result.setCode("500");
			result.setMsg(e.getMessage());
			
		}
		return result;

	}
		
	
	@RequestMapping(value = "/consulta")
	public AjaxResponseBody getLista(String cliente,String alumno, Integer id_suc) {

		AjaxResponseBody result = new AjaxResponseBody();
		try {
			result.setResult(movimientoDAO.consultarBoletasAptosNotaCredito( cliente, alumno, id_suc) );
			
		} catch (Exception e) {
			result.setException(e);
		}
		return result;

	}
	
	@RequestMapping(value = "/listarNotasCredito")
	public AjaxResponseBody listarNotasCredito(String fec_ini, String fec_fin, Integer id_suc) {

		AjaxResponseBody result = new AjaxResponseBody();
		try {
			result.setResult(notaCreditoDAO.listarNotasCreditos(FechaUtil.toDate(fec_ini), FechaUtil.toDate(fec_fin),id_suc));
			
		} catch (Exception e) {
			result.setException(e);
		}
		return result;

	}
}
