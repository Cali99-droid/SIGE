package com.sige.spring.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sige.mat.dao.AcademicoPagoDAO;
import com.sige.mat.dao.AlumnoDescuentoDAO;
import com.sige.mat.dao.AnioDAO;
import com.sige.mat.dao.ConfMensualidadDAO;
import com.sige.mat.dao.DescHnoDAO;
import com.sige.mat.dao.HistorialMenDAO;
import com.sige.mat.dao.MatriculaDAO;
import com.sige.mat.dao.PeriodoAcaDAO;
import com.sige.mat.dao.PeriodoDAO;
import com.sige.mat.dao.ReservaCuotaDAO;
import com.sige.mat.dao.SituacionMatDAO;
import com.sige.mat.dao.SolicitudDAO;
import com.sige.mat.dao.TarifasEmergenciaDAO;
import com.sige.mat.dao.TrasladoDetalleDAO;
import com.sige.mat.dao.impl.TarifasEmergenciaDAOImpl;
import com.tesla.colegio.model.AcademicoPago;
import com.tesla.colegio.model.ConfMensualidad;
import com.tesla.colegio.model.DescHno;
import com.tesla.colegio.model.HistorialMen;
import com.tesla.colegio.model.Matricula;
import com.tesla.colegio.model.Periodo;
import com.tesla.colegio.model.PeriodoAca;
import com.tesla.colegio.model.TarifasEmergencia;
import com.tesla.colegio.model.TrasladoDetalle;
import com.tesla.colegio.util.Constante;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;

@Service
public class PagosService {

	@Autowired
	MatriculaDAO matriculaDAO;

	@Autowired
	DescHnoDAO deschnoDAO;

	@Autowired
	AnioDAO anioDAO;

	@Autowired
	SituacionMatDAO situacionMatDAO;

	@Autowired
	SolicitudDAO solicitudDAO;

	@Autowired
	ConfMensualidadDAO confMensualidadDAO;

	@Autowired
	AlumnoDescuentoDAO alumnoDescuentoDAO;

	@Autowired
	AcademicoPagoDAO academicoPagoDAO;
	
	@Autowired
	PeriodoDAO periodoDAO;
	
	@Autowired
	TarifasEmergenciaDAO tarifasEmergenciaDAO;
	
	@Autowired
	private TrasladoDetalleDAO trasladoDetalleDAO;
	
	@Autowired
	private HistorialMenDAO historialMenDAO;

	@Autowired
	private ReservaCuotaDAO reservaCuotaDAO;
	
	@Autowired
	private PagosService pagosService;
	
	public BigDecimal pagosPendientes(Integer id_alu, Integer id_anio) throws Exception {

		BigDecimal deuda = BigDecimal.ZERO;

		// Obtenemos datos de la matricula del alumno
		Param param = new Param();
		param.put("id_alu", id_alu);

		//List<Matricula> matriculas = matriculaDAO.listByParams(param, new String[] { "id desc" });
		List<Row> matriculas = matriculaDAO.obtenerMatriculaColegioxAlumnoAnio(id_alu, id_anio);

		if (matriculas.size() == 0)
			return deuda;

		Row ultimaMatricula = matriculas.get(0);

		String num_cont = ultimaMatricula.getString("num_cont");
		
		BigDecimal desc_hermano=null;
		
		 ConfMensualidad descHno=confMensualidadDAO.getByParams(new Param("id_per", ultimaMatricula.getInteger("id_per")));
		 if(descHno!=null)
		 desc_hermano=descHno.getDesc_hermano();
		// Integer anio_actual = anioDAO.getById_per(id_per);

		// Buscamos si el alumno tiene hermano con el mismo numero de
		// contrato

		if (num_cont == null || "".equals(num_cont)) {
			throw new Exception("El alumno no tiene nro de contrato en su ultima matricula");// ESTO
																								// ES
																								// IMPOSIBLE
		}

		List<Matricula> hermanoList = matriculaDAO.listFullByParams(new Param("num_cont", num_cont),new String[] { "mat.id" });
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		String format = formatter.format(new Date());
		int fecActual = Integer.parseInt(format);
		
		Integer cant_hermanos = 0;
		for (Matricula matriculalist : hermanoList) {
			// if( !(new Integer(5)).equals(matriculalist.getId_sit() ) &&
			// !matriculalist.getId_niv().equals(Constante.NIVEL_INICIAL)
			// )//si el hermano no esta trasladado
			//SituacionMat situacionMat = situacionMatDAO.getByParams(new Param("id_mat", matriculalist.getId()));
			TrasladoDetalle situacionMat=trasladoDetalleDAO.getByParams(new Param("id_mat", matriculalist.getId()));
			Integer id_sit = null;
			if (situacionMat != null)
				id_sit = situacionMat.getId_sit();
			if (id_sit != null) {
				if ((id_sit != null && id_sit != 5) && !matriculalist.getId_niv().equals(Constante.NIVEL_INICIAL))// si
																													// el
																													// hermano
																													// no
																													// esta
																													// trasladado*/
					cant_hermanos = cant_hermanos + 1;
			} else {
				// if(
				// !matriculalist.getId_niv().equals(Constante.NIVEL_INICIAL)
				// )//si el hermano no esta trasladado*/
				cant_hermanos = cant_hermanos + 1;
			}

		}
		
		for (Matricula hermano : hermanoList) {

			Param param_pago = new Param();
			param_pago.put("id_mat", hermano.getId());
			param_pago.put("tip", "MEN");
			param_pago.put("canc", "0");
			param_pago.put("est", "A");
			List<AcademicoPago> meses_pagos = academicoPagoDAO.listByParams(param_pago, new String[] { "mens" });
			
			// Obtener descuento personalizado si es que lo tiene
			Map<String, Object> alumnoDescuento = alumnoDescuentoDAO.getAlumnoPorMatricula(hermano.getId());
			BigDecimal descuentoPersonalizado = null;
			String id_descuento_personalizado = null;

			if (alumnoDescuento != null) {
				descuentoPersonalizado = new BigDecimal(alumnoDescuento.get("descuento").toString());
				id_descuento_personalizado = alumnoDescuento.get("id").toString();
			}
			
			ConfMensualidad confMensualidad = confMensualidadDAO.getByParams(new Param("id_per", hermano.getId_per()));
			BigDecimal desc_secretaria =null;
			if(confMensualidad!=null)
			 desc_secretaria = confMensualidad.getDescuento();

			/*for (AcademicoPago meses_pagar : meses_pagos) {
				deuda = deuda.add(meses_pagar.getMonto());
			}*/
			boolean hayVencimiento = true;
			int mesesPagoPuntuales = 0;
			for (AcademicoPago meses_pagar : meses_pagos) {
				//Calculamos si tiene descuento 0
				Param param1 = new Param();
				param1.put("mens", meses_pagar.getMens());
				param1.put("id_mat",hermano.getId());
				AcademicoPago descuento = academicoPagoDAO.getByParams(param1);
				//Ahora cambio para la fecha de vencimiento, busco el Periodo y luego el anio
				Periodo periodo = periodoDAO.getByParams(new Param("id",hermano.getId_per()));
				Integer anio_ult_mat=null;
				if(periodo!=null)
					anio_ult_mat=periodo.getId_anio();
				int fecVencimiento = getFecVencimiento(anio_ult_mat, meses_pagar.getMens(),hermano.getId_per());
				if(descuento.getDesc_hermano()!=null && descuento.getDesc_pago_adelantado()!=null && descuento.getDesc_personalizado()!=null && descuento.getDesc_pronto_pago()!=null){//si el descuento es diferente de nulo y cero entonces se procede a los cálculos de descuento, caso contrario no
					if(descuento.getDesc_hermano().compareTo(new BigDecimal(0))!=0 && descuento.getDesc_pago_adelantado().compareTo(new BigDecimal(0))!=0 && descuento.getDesc_personalizado().compareTo(new BigDecimal(0))!=0 && descuento.getDesc_pronto_pago().compareTo(new BigDecimal(0))!=0){
						if (descuentoPersonalizado != null && fecActual <= fecVencimiento) {
							meses_pagar.setDesc_personalizado(descuentoPersonalizado);
							meses_pagar.setDesc_hermano(new BigDecimal(0));
							meses_pagar.setDesc_pronto_pago(new BigDecimal(0));
						} else {
							meses_pagar.setDesc_personalizado(new BigDecimal(0));

							if (fecActual <= fecVencimiento) {
								hayVencimiento = false;
								mesesPagoPuntuales ++;
								if (cant_hermanos > 1 && hermano.getId_niv() != Constante.NIVEL_INICIAL) {
									meses_pagar.setDesc_hermano(desc_hermano);
								} else
									meses_pagar.setDesc_hermano(new BigDecimal(0));

								meses_pagar.setDesc_pronto_pago(desc_secretaria);

							} else {
								meses_pagar.setDesc_hermano(new BigDecimal(0));
								meses_pagar.setDesc_pronto_pago(new BigDecimal(0));

							}

						}
					}
				} else{
						if (descuentoPersonalizado != null ) { //&& fecActual <= fecVencimiento
							meses_pagar.setDesc_personalizado(descuentoPersonalizado);
							meses_pagar.setDesc_hermano(new BigDecimal(0));
							meses_pagar.setDesc_pronto_pago(new BigDecimal(0));
							meses_pagar.setDesc_personalizado(new BigDecimal(0));

							if (fecActual <= fecVencimiento) {
								hayVencimiento = false;
								mesesPagoPuntuales ++;
								if (cant_hermanos > 1 && hermano.getId_niv() != Constante.NIVEL_INICIAL) {
									meses_pagar.setDesc_hermano(desc_hermano);
								} else
									meses_pagar.setDesc_hermano(new BigDecimal(0));

								meses_pagar.setDesc_pronto_pago(desc_secretaria);

							} else {
								meses_pagar.setDesc_hermano(new BigDecimal(0));
								meses_pagar.setDesc_pronto_pago(new BigDecimal(0));

							}

						} else {
							if (fecActual <= fecVencimiento) {
								hayVencimiento = false;
								mesesPagoPuntuales ++;
								if (cant_hermanos > 1 && hermano.getId_niv() != Constante.NIVEL_INICIAL) {
									meses_pagar.setDesc_hermano(desc_hermano);
								} else
									meses_pagar.setDesc_hermano(new BigDecimal(0));

								meses_pagar.setDesc_pronto_pago(desc_secretaria);

							} else {
								meses_pagar.setDesc_hermano(new BigDecimal(0));
								meses_pagar.setDesc_pronto_pago(new BigDecimal(0));

							}
						}
				}
				//_meses_pagos.add(meses_pagar);
				 deuda=deuda.add(meses_pagar.getMonto());
			}

		}

		return deuda;

	}
	
	public BigDecimal pagosPendientesParaInscripcion(Integer id_alu, Integer id_anio) throws Exception {

		BigDecimal deuda = BigDecimal.ZERO;

		// Obtenemos datos de la matricula del alumno
		Param param = new Param();
		param.put("id_alu", id_alu);

		//List<Matricula> matriculas = matriculaDAO.listByParams(param, new String[] { "id desc" });
		List<Row> matriculas = matriculaDAO.obtenerMatriculaColegioxAlumnoAnio(id_alu, id_anio);

		if (matriculas.size() == 0)
			return deuda;

		Row ultimaMatricula = matriculas.get(0);

		String num_cont = ultimaMatricula.getString("num_cont");
		
		BigDecimal desc_hermano=null;
		
		 ConfMensualidad descHno=confMensualidadDAO.getByParams(new Param("id_per", ultimaMatricula.getInteger("id_per")));
		 if(descHno!=null)
		 desc_hermano=descHno.getDesc_hermano();
		// Integer anio_actual = anioDAO.getById_per(id_per);

		// Buscamos si el alumno tiene hermano con el mismo numero de
		// contrato

		if (num_cont == null || "".equals(num_cont)) {
			throw new Exception("El alumno no tiene nro de contrato en su ultima matricula");// ESTO
																								// ES
																								// IMPOSIBLE
		}

		List<Matricula> hermanoList = matriculaDAO.listFullByParams(new Param("num_cont", num_cont),new String[] { "mat.id" });
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		String format = formatter.format(new Date());
		int fecActual = Integer.parseInt(format);
		
		Integer cant_hermanos = 0;
		for (Matricula matriculalist : hermanoList) {
			// if( !(new Integer(5)).equals(matriculalist.getId_sit() ) &&
			// !matriculalist.getId_niv().equals(Constante.NIVEL_INICIAL)
			// )//si el hermano no esta trasladado
			//SituacionMat situacionMat = situacionMatDAO.getByParams(new Param("id_mat", matriculalist.getId()));
			TrasladoDetalle situacionMat=trasladoDetalleDAO.getByParams(new Param("id_mat", matriculalist.getId()));
			Integer id_sit = null;
			if (situacionMat != null)
				id_sit = situacionMat.getId_sit();
			if (id_sit != null) {
				if ((id_sit != null && id_sit != 5) && !matriculalist.getId_niv().equals(Constante.NIVEL_INICIAL))// si
																													// el
																													// hermano
																													// no
																													// esta
																													// trasladado*/
					cant_hermanos = cant_hermanos + 1;
			} else {
				// if(
				// !matriculalist.getId_niv().equals(Constante.NIVEL_INICIAL)
				// )//si el hermano no esta trasladado*/
				cant_hermanos = cant_hermanos + 1;
			}

		}
		
		for (Matricula hermano : hermanoList) {

			Param param_pago = new Param();
			param_pago.put("id_mat", hermano.getId());
			param_pago.put("tip", "MEN");
			param_pago.put("canc", "0");
			param_pago.put("est", "A");
			List<AcademicoPago> meses_pagos = academicoPagoDAO.listByParams(param_pago, new String[] { "mens" });
			
			// Obtener descuento personalizado si es que lo tiene
			Map<String, Object> alumnoDescuento = alumnoDescuentoDAO.getAlumnoPorMatricula(hermano.getId());
			BigDecimal descuentoPersonalizado = null;
			String id_descuento_personalizado = null;

			if (alumnoDescuento != null) {
				descuentoPersonalizado = new BigDecimal(alumnoDescuento.get("descuento").toString());
				id_descuento_personalizado = alumnoDescuento.get("id").toString();
			}
			
			ConfMensualidad confMensualidad = confMensualidadDAO.getByParams(new Param("id_per", hermano.getId_per()));
			BigDecimal desc_secretaria =null;
			if(confMensualidad!=null)
			 desc_secretaria = confMensualidad.getDescuento();

			/*for (AcademicoPago meses_pagar : meses_pagos) {
				deuda = deuda.add(meses_pagar.getMonto());
			}*/
			boolean hayVencimiento = true;
			int mesesPagoPuntuales = 0;
			for (AcademicoPago meses_pagar : meses_pagos) {
				//Calculamos si tiene descuento 0
				Param param1 = new Param();
				param1.put("mens", meses_pagar.getMens());
				param1.put("id_mat",hermano.getId());
				AcademicoPago descuento = academicoPagoDAO.getByParams(param1);
				//Ahora cambio para la fecha de vencimiento, busco el Periodo y luego el anio
				Periodo periodo = periodoDAO.getByParams(new Param("id",hermano.getId_per()));
				Integer anio_ult_mat=null;
				if(periodo!=null)
					anio_ult_mat=periodo.getId_anio();
				//int fecVencimiento = getFecVencimiento(anio_ult_mat, meses_pagar.getMens(),hermano.getId_per());
				String tipo_fec_venc = confMensualidadDAO.getByParams(new Param("id_per",hermano.getId_per())).getTipo_fec_ven();
				int fecVencimiento=0;
				if(tipo_fec_venc!=null){
					if(tipo_fec_venc.equals(Constante.TIPO_FEC_VEN_FIN)){					
						fecVencimiento = pagosService.getFecVencimientoFin(hermano.getId(), meses_pagar.getMens(),"MEN");
					} else if(tipo_fec_venc.equals(Constante.TIPO_FEC_VEN_DIA)){
						fecVencimiento = getFecVencimiento(anio_ult_mat, meses_pagar.getMens(),hermano.getId_per());
					}
				} else{
					fecVencimiento = getFecVencimiento(anio_ult_mat, meses_pagar.getMens(),hermano.getId_per());
				}						
				
				if(descuento.getDesc_hermano()!=null && descuento.getDesc_pago_adelantado()!=null && descuento.getDesc_personalizado()!=null && descuento.getDesc_pronto_pago()!=null){//si el descuento es diferente de nulo y cero entonces se procede a los cálculos de descuento, caso contrario no
					if(descuento.getDesc_hermano().compareTo(new BigDecimal(0))!=0 && descuento.getDesc_pago_adelantado().compareTo(new BigDecimal(0))!=0 && descuento.getDesc_personalizado().compareTo(new BigDecimal(0))!=0 && descuento.getDesc_pronto_pago().compareTo(new BigDecimal(0))!=0){
						if (descuentoPersonalizado != null && fecActual <= fecVencimiento) {
							meses_pagar.setDesc_personalizado(descuentoPersonalizado);
							meses_pagar.setDesc_hermano(new BigDecimal(0));
							meses_pagar.setDesc_pronto_pago(new BigDecimal(0));
						} else {
							meses_pagar.setDesc_personalizado(new BigDecimal(0));

							if (fecActual <= fecVencimiento) {
								hayVencimiento = false;
								mesesPagoPuntuales ++;
								if (cant_hermanos > 1 && hermano.getId_niv() != Constante.NIVEL_INICIAL) {
									meses_pagar.setDesc_hermano(desc_hermano);
								} else
									meses_pagar.setDesc_hermano(new BigDecimal(0));

								meses_pagar.setDesc_pronto_pago(desc_secretaria);

							} else {
								meses_pagar.setDesc_hermano(new BigDecimal(0));
								meses_pagar.setDesc_pronto_pago(new BigDecimal(0));

							}

						}
					}
				} else{
						if (descuentoPersonalizado != null ) { //&& fecActual <= fecVencimiento
							meses_pagar.setDesc_personalizado(descuentoPersonalizado);
							meses_pagar.setDesc_hermano(new BigDecimal(0));
							meses_pagar.setDesc_pronto_pago(new BigDecimal(0));
							meses_pagar.setDesc_personalizado(new BigDecimal(0));

							if (fecActual <= fecVencimiento) {
								hayVencimiento = false;
								mesesPagoPuntuales ++;
								if (cant_hermanos > 1 && hermano.getId_niv() != Constante.NIVEL_INICIAL) {
									meses_pagar.setDesc_hermano(desc_hermano);
								} else
									meses_pagar.setDesc_hermano(new BigDecimal(0));

								meses_pagar.setDesc_pronto_pago(desc_secretaria);

							} else {
								meses_pagar.setDesc_hermano(new BigDecimal(0));
								meses_pagar.setDesc_pronto_pago(new BigDecimal(0));

							}

						} else {
							if (fecActual <= fecVencimiento) {
								hayVencimiento = false;
								mesesPagoPuntuales ++;
								if (cant_hermanos > 1 && hermano.getId_niv() != Constante.NIVEL_INICIAL) {
									meses_pagar.setDesc_hermano(desc_hermano);
								} else
									meses_pagar.setDesc_hermano(new BigDecimal(0));

								meses_pagar.setDesc_pronto_pago(desc_secretaria);

							} else {
								meses_pagar.setDesc_hermano(new BigDecimal(0));
								meses_pagar.setDesc_pronto_pago(new BigDecimal(0));

							}
						}
				}
				//_meses_pagos.add(meses_pagar);
				if (fecActual <= fecVencimiento) {
					
				} else {
					deuda=deuda.add(meses_pagar.getMonto());
				}
				 
			}

		}

		return deuda;

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

		String fecVenc = anioSiguiente + String.format("%02d", mesSiguiente) + dia_mora;// TODO
																					// PARAMETRIZAR
																					// FECHA

		return Integer.parseInt(fecVenc);

	}
	
	public int getFecVencimientoFin(int id_mat, int mes, String tip) {
		
		AcademicoPago academicoPago = academicoPagoDAO.getByParams(new Param("id_mat",id_mat));
		Date ultimo_dia_pago=null;
		if(academicoPago!=null){
			Param param = new Param();
			param.put("id_mat", id_mat);			
			param.put("tip", tip);
			param.put("mens", mes);
			ultimo_dia_pago=academicoPagoDAO.getByParams(param).getFec_venc();
		}
		
		String fecha_vencimiento = ultimo_dia_pago.toString().replace("-", "");
				 
		/*int anioSiguiente;
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
																					// FECHA*/
		return Integer.parseInt(fecha_vencimiento);

	}
	
	@Transactional
	public void procesarTarifaEmergencia(Integer id_per, Integer mes, Integer id_tar) throws Exception {
	
		List<Row> matriculas =matriculaDAO.listarAlumnosxAnio(4);
		TarifasEmergencia tarifasEmergencia=tarifasEmergenciaDAO.getByParams(new Param("id",id_tar));
		for (Row row : matriculas) {
			Integer id_mat = row.getInteger("id_mat");
			Integer id_alu = row.getInteger("id_alu");

			Param param = new Param();
			param.put("id_mat",id_mat);
			param.put("mens", mes);

			AcademicoPago academicoPago= academicoPagoDAO.getByParams(param);


			if(academicoPago!=null){
				//ALUMNO EXISTENTE
				
				String exonerado=tarifasEmergencia.getExonerado();
				if(exonerado.equals("1")){
					
						//voy a hardcodear, jose necesita su pago
					
					if(mes==3){
						
						//SE CALCULA EL SALDO
						BigDecimal montoReserva = reservaCuotaDAO.obtenerMonto(id_alu);//reserva pagada
						BigDecimal montoMatricula = montoMes(id_mat, "MAT", null, 1);// matricula pagada
						BigDecimal montoMEs = montoMes(id_mat, "MEN", mes, 1);//monto que pagó en marzo
					
						BigDecimal saldo_favor = montoReserva.add(montoMatricula).add(montoMEs);
						//SE ACTUALIZA COMO PAGADO
						academicoPagoDAO.updateSAldoFavor(academicoPago.getId(), saldo_favor,1);
										
					}else{
						//CASO ABRIL PA ADELANTE
						BigDecimal saldo_anterior = saldoAnterior(id_mat, "MEN", mes, 1);// MI SALDO DE abril
						if(mes>4){
							BigDecimal montoMEs = montoMes(id_mat, "MEN", mes, 1);//monto que pagó en abril
							saldo_anterior = saldo_anterior.add(montoMEs);
						}
						academicoPagoDAO.updateSAldoFavor(academicoPago.getId(),saldo_anterior,1);

					}
					
					
				}else{
					//SE CALCULA LO QUE VA A PAGAR Y EL SALDO
					BigDecimal saldo_anterior = saldoAnterior(id_mat, "MEN", mes, 1);//SALDO ANTERIOR ( MES -1)
					BigDecimal montoMEs = montoMes(id_mat, "MEN", mes, 1);//monto que pagó en el mes actual
					saldo_anterior = saldo_anterior.add(montoMEs);
					//LO QUE DEBE PAGAR
					BigDecimal pagoMEs = new BigDecimal(200);
					if(mes==5){// SOLO SE SUMA PARA MAYO
						pagoMEs = pagoMEs.add(new BigDecimal(200));// LE AREGO MATRICULA
					}

					
					if(saldo_anterior.compareTo(pagoMEs)>0){
						BigDecimal saldo = saldo_anterior.subtract(pagoMEs);
						//SE ACTUALIZA COMO PAGADO
						academicoPagoDAO.updateSAldoFavor(academicoPago.getId(),saldo,1);//pendiente por pagar


					}else{

						BigDecimal pago = pagoMEs.subtract(saldo_anterior);
						//SE ACTUALIZA COMO PAGADO
					
						academicoPagoDAO.updateMontoPagoYtotal(pago, pago, BigDecimal.ZERO, academicoPago.getId() ,tarifasEmergencia.getFec_ven() );
 
					}
					
				
				}

			}else{
				//ALUMNO NUEVO
			}

		}

	}
	
	@Transactional
	public void procesarTarifaEmergenciaxAlumno(Integer id_mat) throws Exception {
	
		Row matricula =matriculaDAO.obtenerMatriculaAlumno(id_mat);
		//Integer id_per=matricula.getInteger("id_per");
		for (int mes=3; mes<=12; mes++) {

			TarifasEmergencia tarifasEmergencia=tarifasEmergenciaDAO.getByParams(new Param("mes",mes));

			//Integer id_mat = matricula.getInteger("id_mat");
			Integer id_alu = matricula.getInteger("id_alu");

			Param param = new Param();
			param.put("id_mat",id_mat);
			param.put("mens", mes);

			AcademicoPago academicoPago= academicoPagoDAO.getByParams(param);


			if(academicoPago!=null){
				//ALUMNO EXISTENTE
				
				String exonerado=tarifasEmergencia.getExonerado();
				if(exonerado.equals("1")){
					
						//voy a hardcodear, jose necesita su pago
					
					if(mes==3){
						
						//SE CALCULA EL SALDO
						BigDecimal montoReserva = reservaCuotaDAO.obtenerMonto(id_alu);//reserva pagada
						BigDecimal montoMatricula = montoMes(id_mat, "MAT", null, 1);// matricula pagada
						BigDecimal montoMEs = montoMes(id_mat, "MEN", mes, 1);//monto que pagó en marzo
					
						BigDecimal saldo_favor = montoReserva.add(montoMatricula).add(montoMEs);
						//SE ACTUALIZA COMO PAGADO
						academicoPagoDAO.updateSAldoFavor(academicoPago.getId(), saldo_favor,1);
										
					}else{
						//CASO ABRIL PA ADELANTE
						BigDecimal saldo_anterior = saldoAnterior(id_mat, "MEN", mes, 1);// MI SALDO DE abril
						if(mes>4){
							BigDecimal montoMEs = montoMes(id_mat, "MEN", mes, 1);//monto que pagó en abril
							saldo_anterior = saldo_anterior.add(montoMEs);
						}
						academicoPagoDAO.updateSAldoFavor(academicoPago.getId(),saldo_anterior,1);

					}
					
					
				}else{
					//SE CALCULA LO QUE VA A PAGAR Y EL SALDO
					BigDecimal saldo_anterior = saldoAnterior(id_mat, "MEN", mes, 1);//SALDO ANTERIOR ( MES -1)
					BigDecimal montoMEs = montoMes(id_mat, "MEN", mes, 1);//monto que pagó en el mes actual
					saldo_anterior = saldo_anterior.add(montoMEs);
					//LO QUE DEBE PAGAR
					BigDecimal pagoMEs = new BigDecimal(200);
					if(mes==5){// SOLO SE SUMA PARA MAYO
						pagoMEs = pagoMEs.add(new BigDecimal(200));// LE AREGO MATRICULA
					}

					
					if(saldo_anterior.compareTo(pagoMEs)>0){
						BigDecimal saldo = saldo_anterior.subtract(pagoMEs);
						//SE ACTUALIZA COMO PAGADO
						academicoPagoDAO.updateSAldoFavor(academicoPago.getId(),saldo,1);//pendiente por pagar


					}else{

						BigDecimal pago = pagoMEs.subtract(saldo_anterior);
						//SE ACTUALIZA COMO PAGADO
					
						academicoPagoDAO.updateMontoPagoYtotal(pago, pago, BigDecimal.ZERO, academicoPago.getId() ,tarifasEmergencia.getFec_ven() );
 
					}
					
				
				}

			}else{
				//ALUMNO NUEVO
			}

		}

	}
	
		
	@Transactional
	public void procesarTarifaEmergencia_lina(Integer id_per, Integer mes, Integer id_tar) throws IOException {
		List<Row> matriculas =matriculaDAO.listarAlumnosxPeriodo(id_per);
		
		TarifasEmergencia tarifasEmergencia=tarifasEmergenciaDAO.getByParams(new Param("id",id_tar));

		for (Row row : matriculas) {
			//Buscamos el pago del mes a procesar para ese alumno
			Param param = new Param();
			param.put("id_mat", row.getInteger("id_mat"));
			param.put("mens", mes);
			AcademicoPago academicoPago= academicoPagoDAO.getByParams(param);
			Boolean monto_devuelto=false;
			//Tarifa de Emergencia	
			if(academicoPago!=null){
				//Primero pregunto si esta exonerado
				String exonerado=tarifasEmergencia.getExonerado();
				if(academicoPago.getCanc().equals("1")){//Si ya pago
					BigDecimal monto_pagado =academicoPago.getMontoTotal();
					if(exonerado.equals("1")){
						//Actualizo la tabla academico pago a cancelado
						//academicoPagoDAO.updateMensualidadCanc(academicoPago.getId());
						//Pasamos el monto pagado a otro mes
						List<AcademicoPago> academicoPago2= academicoPagoDAO.obtenerPagosxMatricula(row.getInteger("id_mat"), mes);
						for (AcademicoPago academicoPago3 : academicoPago2) {
							if(academicoPago3.getCanc().equals("0") && !monto_devuelto){
								BigDecimal monto_nuevo=academicoPago3.getMonto().subtract(monto_pagado);
								//BigDecimal monto_restante=monto_pagado.subtract(tarifasEmergencia.getMonto());
								if(monto_nuevo.compareTo(BigDecimal.ZERO)>0){
									//actualizamos el monto de fac_academico_pago
									academicoPagoDAO.updateDesctoPagoAdelantado(monto_pagado, academicoPago3.getId());
									//Insertamos en el historial
									/*HistorialMen historial_men=new HistorialMen();
									historial_men.setId_fac(academicoPago3.getId());
									historial_men.setMonto_actual(monto_nuevo);
									historial_men.setMonto_anterior(academicoPago3.getMonto());
									historial_men.setEst("A");
									historialMenDAO.saveOrUpdate(historial_men);*/
									monto_devuelto=true;
									break;
								}
							}
						}
					} else if(exonerado.equals("0")){
						BigDecimal monto_restante=monto_pagado.subtract(tarifasEmergencia.getMonto());
						if(monto_restante.compareTo(BigDecimal.ZERO)>0){
							//Al mes siguiente le reduzco el monto total	Suponiendo q pago 260
							//Buscamos el pago del mes siguiente, pero q no este pagado
							//Buscamos los meses siguiente q aun no ha pagado
							/*Param param2 = new Param();
							param2.put("id_mat", row.getInteger("id_mat"));
							param2.put("mens not in", "("+mes+",1,2)");*/
							List<AcademicoPago> academicoPago2= academicoPagoDAO.obtenerPagosxMatricula(row.getInteger("id_mat"), mes);
							for (AcademicoPago academicoPago3 : academicoPago2) {
								if(academicoPago3.getCanc().equals("0") && !monto_devuelto){
									BigDecimal monto_nuevo=academicoPago3.getMonto().subtract(monto_restante);
									if(monto_nuevo.compareTo(BigDecimal.ZERO)>0){
										//actualizamos el monto de fac_academico_pago
										academicoPagoDAO.updateDesctoPagoAdelantado(monto_restante, academicoPago3.getId());
										//Insertamos en el historial
										/*HistorialMen historial_men=new HistorialMen();
										historial_men.setId_fac(academicoPago3.getId());
										historial_men.setMonto_actual(monto_nuevo);
										historial_men.setMonto_anterior(academicoPago3.getMonto());
										historial_men.setEst("A");
										historialMenDAO.saveOrUpdate(historial_men);*/
										monto_devuelto=true;
										break;
									}
								}
							}
					}
				
					/*if(!monto_devuelto){
						//Inserto en nota de credito, por ahora no
					}*/					
				}
				} else if(academicoPago.getCanc().equals("0")){//No pago
					if(exonerado.equals("1")){
						//actualizmos a cancelado
						academicoPagoDAO.updateMensualidadCanc(academicoPago.getId());
						BigDecimal descuento_pago_adelantado = new BigDecimal(0);
						descuento_pago_adelantado=academicoPago.getDesc_pago_adelantado();
						if(descuento_pago_adelantado!=null){ //Si tenia descuento pronto pago lo insertamos
							List<AcademicoPago> academicoPago2= academicoPagoDAO.obtenerPagosxMatricula(row.getInteger("id_mat"), mes);
							for (AcademicoPago academicoPago3 : academicoPago2) {
								if(academicoPago3.getCanc().equals("0") && !monto_devuelto){
										academicoPagoDAO.updateDesctoPagoAdelantado(descuento_pago_adelantado, academicoPago3.getId());
										monto_devuelto=true;
										break;
								}
						}
						}
					} else if(exonerado.equals("0")){
						//Actualizo solo el monto, porque los descuentos estan calculados al momento
						if(academicoPago.getMonto().compareTo(tarifasEmergencia.getMonto())>=0){
							academicoPagoDAO.updateMontoPagoDesc(tarifasEmergencia.getMonto(), tarifasEmergencia.getDes_hermano(),tarifasEmergencia.getFec_ven(),academicoPago.getId());
							//Preguntamos si tenia un descuento por pago adelantado
							if (academicoPago.getDesc_pago_adelantado()!=null){
								if(tarifasEmergencia.getMonto().compareTo(academicoPago.getDesc_pago_adelantado())<0){
									BigDecimal sobrante= academicoPago.getDesc_pago_adelantado().subtract(tarifasEmergencia.getMonto());
									academicoPagoDAO.updateDesctoPagoAdelantado(academicoPago.getDesc_pago_adelantado().subtract(sobrante),academicoPago.getId());
									List<AcademicoPago> academicoPago2= academicoPagoDAO.obtenerPagosxMatricula(row.getInteger("id_mat"), mes);
									for (AcademicoPago academicoPago3 : academicoPago2) {
										if(academicoPago3.getCanc().equals("0") && !monto_devuelto){
												academicoPagoDAO.updateDesctoPagoAdelantado(sobrante, academicoPago3.getId());
												monto_devuelto=true;
												break;
										}
									}
								}
							}
							//Insertamos en el historial
							HistorialMen historial_men=new HistorialMen();
							historial_men.setId_fac(academicoPago.getId());
							historial_men.setMonto_actual(tarifasEmergencia.getMonto());
							historial_men.setMonto_anterior(academicoPago.getMonto());
							historial_men.setEst("A");
							historialMenDAO.saveOrUpdate(historial_men);
						} else if(academicoPago.getMonto().compareTo(tarifasEmergencia.getMonto())<0){
							academicoPagoDAO.updateMontoPagoDesc(academicoPago.getMonto(), tarifasEmergencia.getDes_hermano(),tarifasEmergencia.getFec_ven(),academicoPago.getId());
							//Preguntamos si tenia un descuento por pago adelantado
							if (academicoPago.getDesc_pago_adelantado()!=null){
								if(tarifasEmergencia.getMonto().compareTo(academicoPago.getDesc_pago_adelantado())<0){
									BigDecimal sobrante= academicoPago.getDesc_pago_adelantado().subtract(tarifasEmergencia.getMonto());
									academicoPagoDAO.updateDesctoPagoAdelantado(academicoPago.getDesc_pago_adelantado().subtract(sobrante),academicoPago.getId());
									List<AcademicoPago> academicoPago2= academicoPagoDAO.obtenerPagosxMatricula(row.getInteger("id_mat"), mes);
									for (AcademicoPago academicoPago3 : academicoPago2) {
										if(academicoPago3.getCanc().equals("0") && !monto_devuelto){
												academicoPagoDAO.updateDesctoPagoAdelantado(sobrante, academicoPago3.getId());
												monto_devuelto=true;
												break;
										}
									}
								}
							}
							//Insertamos en el historial
							HistorialMen historial_men=new HistorialMen();
							historial_men.setId_fac(academicoPago.getId());
							historial_men.setMonto_actual(academicoPago.getMonto());
							historial_men.setMonto_anterior(academicoPago.getMonto());
							historial_men.setEst("A");
							historialMenDAO.saveOrUpdate(historial_men);
						}
					}					
				}
			} else{
				//Insertamos la mensualidad en fac_academico
				AcademicoPago academicoPago2= new AcademicoPago();
				academicoPago2.setId_mat(row.getInteger("id_mat"));
				academicoPago2.setMens(mes);
				academicoPago2.setTip("MEN");
				academicoPago2.setMonto(tarifasEmergencia.getMonto());
				academicoPago2.setCanc("0");
				academicoPago2.setFec_venc(tarifasEmergencia.getFec_ven());
				academicoPago2.setEst("A");
				academicoPagoDAO.saveOrUpdate(academicoPago2);
			}
		}
		
		//Actualizamos la tarifa a procesado
		tarifasEmergenciaDAO.updateTarifaEmergencia(id_tar);
	}
	
	private BigDecimal montoMes(Integer id_mat,String tipo, Integer mes,Integer canc){
		Row academico = academicoPagoDAO.getByMes(id_mat, tipo, mes, canc);
		if(academico==null || academico.getBigDecimal("monto_total")==null)
			return BigDecimal.ZERO;
		else
			return academico.getBigDecimal("monto_total");
		
	}
	
	private BigDecimal saldoAnterior(Integer id_mat,String tipo, Integer mes,Integer canc){
		Row academico = academicoPagoDAO.getByMes(id_mat, tipo, mes-1, canc);
		if(academico==null ||  academico.get("saldo_favor")==null )
			return BigDecimal.ZERO;
		else
			return academico.getBigDecimal("saldo_favor");
		
	}
}
