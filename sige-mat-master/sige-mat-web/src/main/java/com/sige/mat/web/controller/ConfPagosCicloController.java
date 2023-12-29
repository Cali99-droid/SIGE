package com.sige.mat.web.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;
import com.tesla.frmk.util.FechaUtil;
import com.sige.common.enums.EnumTipoPeriodo;
import com.sige.mat.dao.AnioDAO;
import com.sige.mat.dao.CicloDAO;
import com.sige.mat.dao.CicloTurnoDAO;
import com.sige.mat.dao.ConfPagosCicloCuotaDAO;
import com.sige.mat.dao.ConfPagosCicloDAO;
import com.sige.mat.dao.DescuentoConfDAO;
import com.sige.mat.dao.DescuentoCuotaDAO;
import com.sige.mat.dao.DescuentoDAO;
import com.sige.mat.dao.MatriculaDAO;
import com.sige.mat.dao.PeriodoDAO;
import com.sige.mat.dao.TurnoDAO;
import com.tesla.colegio.model.AcademicoPago;
import com.tesla.colegio.model.Anio;
import com.tesla.colegio.model.Ciclo;
import com.tesla.colegio.model.CicloTurno;
import com.tesla.colegio.model.ConfPagosCiclo;
import com.tesla.colegio.model.ConfPagosCicloCuota;
import com.tesla.colegio.model.DescuentoConf;
import com.tesla.colegio.model.DescuentoCuota;
import com.tesla.colegio.model.Matricula;
import com.tesla.colegio.model.Periodo;
import com.tesla.colegio.model.bean.MatriculaPagos;
import com.tesla.colegio.util.Constante;


@RestController
@RequestMapping(value = "/api/confPagosCiclo")
public class ConfPagosCicloController {
	
	@Autowired
	private ConfPagosCicloDAO confPagosCicloDAO;
	
	@Autowired
	private ConfPagosCicloCuotaDAO confPagosCicloCuotaDAO;
	
	@Autowired
	private DescuentoConfDAO descuenConfDAO;
	
	@Autowired
	private DescuentoCuotaDAO descuentoCuotaDAO;
	
	@Autowired
	private DescuentoDAO descuentoDAO;
	
	@Autowired
	private PeriodoDAO periodoDAO;
	
	@Autowired
	private AnioDAO anioDAO;
	
	@Autowired
	private MatriculaDAO matriculaDAO;

	@RequestMapping(value = "/listarDescuentos")
	public AjaxResponseBody getLista(DescuentoConf descuentoConf, Integer id_cct) {

		AjaxResponseBody result = new AjaxResponseBody();
		descuentoConf.setId_cct(id_cct); 
		result.setResult(descuenConfDAO.listarDescuentos(id_cct));
		
		return result;
	}
	
	@RequestMapping(value = "/listarDescuentosCatalogo")
	public AjaxResponseBody getListaDescuentoCatalogo() {

		AjaxResponseBody result = new AjaxResponseBody();
		result.setResult(descuentoDAO.list());
		
		return result;
	}
	
	@RequestMapping(value = "/listarDescuentosCuotas")
	public AjaxResponseBody getListaDescuentosCuota(DescuentoConf descuentoConf, Integer id_cct) {

		AjaxResponseBody result = new AjaxResponseBody();
		descuentoConf.setId_cct(id_cct); 
		result.setResult(descuenConfDAO.listarDescuentos(id_cct));
		
		return result;
	}
	

/*	@Transactional
	@RequestMapping( value = "/grabarCiclo" ,method = RequestMethod.POST)
	public AjaxResponseBody grabar(Ciclo ciclo, String id_ctu[], Integer id_tur[], String hor_ini[], String hor_fin[]) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			//Grabar Ciclo
			Integer id_cic=cicloDAO.saveOrUpdate(ciclo);
			
			//Obtenemos la evaluacion de la lista de aulas
			List<CicloTurno> ciclosTurno = new ArrayList<CicloTurno>();
			if (ciclo.getId()!=null){
				Param param = new Param();
				param.put("id_cic", id_cic);
				ciclosTurno = cicloTurnoDAO.listByParams(param, null);
			}
			
			//List<CicloTurno> ciclosTurnoInsert = new ArrayList<CicloTurno>();
			
			if(id_tur.length>0) {
				for (int i = 0; i < id_tur.length; i++) {
					if(ciclosTurno.size()>0) {
						Integer id_ctu_ex=ciclosTurno.get(i).getId();
						if(Integer.parseInt(id_ctu[i])==id_ctu_ex) {
							//Actualizar los datos
							CicloTurno cicloTurno= new CicloTurno();
							cicloTurno.setId(Integer.parseInt(id_ctu[i]));
							cicloTurno.setId_cic(id_cic);
							cicloTurno.setId_tur(id_tur[i]);
							cicloTurno.setHor_ini(hor_ini[i]);
							cicloTurno.setHor_fin(hor_fin[i]);
							cicloTurno.setEst("A");
							cicloTurnoDAO.saveOrUpdate(cicloTurno);
							//ciclosTurnoInsert.add(e)
							
						} else {
							//Si no es igual desactivo
							CicloTurno cicloTurno= new CicloTurno();
							cicloTurno.setId(Integer.parseInt(id_ctu[i]));
							cicloTurno.setEst("I");
							cicloTurnoDAO.saveOrUpdate(cicloTurno);
						}
					} else {
						//Inserto los datos
						CicloTurno cicloTurno= new CicloTurno();
						cicloTurno.setId_cic(id_cic);
						cicloTurno.setId_tur(id_tur[i]);
						cicloTurno.setHor_ini(hor_ini[i]);
						cicloTurno.setHor_fin(hor_fin[i]);
						cicloTurno.setEst("A");
						cicloTurnoDAO.saveOrUpdate(cicloTurno);
					}
					
				}
			}
			
			if(ciclosTurno.size()>0) {
				int i=0;
				for (CicloTurno cicloTurno : ciclosTurno) {
					if(cicloTurno.getId_tur()!=id_tur[i]) {
						//Si no es igual desactiva
						cicloTurnoDAO.desactivarTurno(cicloTurno.getId(),cicloTurno.getId_tur());
					}	
				}
			}
			
			/*Map<String, Object> map = new HashMap<String,Object>();
			map.put("id_cic", id_cic);
			map.put("listaTurnos", value)*/
/*			result.setResult(id_cic);
			return result;
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	*/
	@Transactional
	@RequestMapping( value = "/grabarDescuento", method = RequestMethod.POST)
	public AjaxResponseBody grabarDescuentoCuotas(DescuentoConf descuentoConf, String nro_cuota[]) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			//Primero grabamos la cabecera que es el descuento
			Integer id_des= descuenConfDAO.saveOrUpdate(descuentoConf);
			//Obtenemos la lista de las cuotas que existen
			/*
			List<DescuentoCuota> descuentoCuotas = new ArrayList<DescuentoCuota>();
			if (descuentoConf.getId()!=null){
				Param param = new Param();
				param.put("id_fdes", id_des);
				descuentoCuotas = descuentoCuotaDAO.listByParams(param, null);
			}
			
			//Obtenemos la configuracion del ciclo turno
			ConfPagosCiclo confPagosCiclo=confPagosCicloDAO.getByParams(new Param("id_cct",descuentoConf.getId_cct()));
			
			if(nro_cuota.length>0) {
				
					if(descuentoCuotas.size()>0) {
					
						
							//for (int i = 0; i < nro_cuota.length; i++) {
								//int i=0;
								for (DescuentoCuota descuentoCuota : descuentoCuotas) {
								Integer nro_cuota_ex=descuentoCuota.getNro_cuota();
								Integer id_cuota_ex=descuentoCuota.getId();
								for (int i = 0; i < nro_cuota.length; i++) {
								if(Integer.parseInt(nro_cuota[i])==nro_cuota_ex) {
									
									//Obtenemos el id de la cuota configuracion
									Param param = new Param();
									param.put("id_cfpav", confPagosCiclo.getId());
									param.put("nro_cuota", Integer.parseInt(nro_cuota[i]));
									ConfPagosCicloCuota confPagosCicloCuota= confPagosCicloCuotaDAO.getByParams(param);
									//Actualizar los datos
									DescuentoCuota descuentoCuota2= new DescuentoCuota();
									descuentoCuota2.setId(id_cuota_ex);
									descuentoCuota2.setId_fdes(id_des);
									descuentoCuota2.setId_fcuo(confPagosCicloCuota.getId());
									descuentoCuota2.setNro_cuota(Integer.parseInt(nro_cuota[i]));
									descuentoCuota2.setEst("A");
									descuentoCuotaDAO.saveOrUpdate(descuentoCuota2);
								} else {
									//Inserto
									/*DescuentoCuota descuentoCuota2= new DescuentoCuota();
									descuentoCuota2.setNro_cuota(Integer.parseInt(nro_cuota[i]));
									descuentoCuota2.setId_fdes(id_des);
									descuentoCuota2.setNro_cuota(Integer.parseInt(nro_cuota[i]));
									descuentoCuota2.setEst("A");
									descuentoCuotaDAO.saveOrUpdate(descuentoCuota2);*/
					/*				descuentoCuotaDAO.desactivarCuota(descuentoCuota.getId(),descuentoCuota.getNro_cuota());
								}
								}
							}
								
								for (int i = 0; i < nro_cuota.length; i++) {
									//verifico si no existe inserto
									Param param = new Param();
									param.put("id_fdes", id_des);
									param.put("nro_cuota", Integer.parseInt(nro_cuota[i]));
									DescuentoCuota cuota_existe=descuentoCuotaDAO.getByParams(param);
									if(cuota_existe==null) {
										//Obtenemos el id de la cuota configuracion
										Param param2 = new Param();
										param2.put("id_cfpav", confPagosCiclo.getId());
										param2.put("nro_cuota", Integer.parseInt(nro_cuota[i]));
										ConfPagosCicloCuota confPagosCicloCuota= confPagosCicloCuotaDAO.getByParams(param2);
										DescuentoCuota descuentoCuota2= new DescuentoCuota();
										descuentoCuota2.setNro_cuota(Integer.parseInt(nro_cuota[i]));
										descuentoCuota2.setId_fdes(id_des);
										descuentoCuota2.setId_fcuo(confPagosCicloCuota.getId());
										descuentoCuota2.setNro_cuota(Integer.parseInt(nro_cuota[i]));
										descuentoCuota2.setEst("A");
										descuentoCuotaDAO.saveOrUpdate(descuentoCuota2);
									}
								}
						//}
					} else {
						for (int i = 0; i < nro_cuota.length; i++) {
						//Inserto los datos
							//Obtenemos el id de la cuota configuracion
						Param param2 = new Param();
						param2.put("id_cfpav", confPagosCiclo.getId());
						param2.put("nro_cuota", Integer.parseInt(nro_cuota[i]));
						ConfPagosCicloCuota confPagosCicloCuota= confPagosCicloCuotaDAO.getByParams(param2);
						DescuentoCuota descuentoCuota= new DescuentoCuota();
						descuentoCuota.setNro_cuota(Integer.parseInt(nro_cuota[i]));
						descuentoCuota.setId_fdes(id_des);
						descuentoCuota.setId_fcuo(confPagosCicloCuota.getId());
						descuentoCuota.setNro_cuota(Integer.parseInt(nro_cuota[i]));
						descuentoCuota.setEst("A");
						descuentoCuotaDAO.saveOrUpdate(descuentoCuota);
						}
					}
					
				}
				
				/*if(descuentoCuotas.size()>0) {
					int i=0;
					for (DescuentoCuota descuentoCuota : descuentoCuotas) {
						if(descuentoCuota.getNro_cuota()!=Integer.parseInt(nro_cuota[i])) {
							//Si no es igual desactiva
							descuentoCuotaDAO.desactivarCuota(descuentoCuota.getId(),descuentoCuota.getNro_cuota());
						}	
					}
				}*/
			
			
			/*if(descuentoCuotas.size()>0) {
				int i=0;
				for (CicloTurno cicloTurno : ciclosTurno) {
					if(cicloTurno.getId_tur()!=id_tur[i]) {
						//Si no es igual desactiva
						cicloTurnoDAO.desactivarTurno(cicloTurno.getId(),cicloTurno.getId_tur());
					}	
				}
			}*/
			
			result.setResult(id_des);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@Transactional
	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabarDescuento(ConfPagosCiclo confPagosCiclo, String id_cuo[],Integer nro_cuo[], String costo_cuo[], String fec_venc[]) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			Integer id_cpc=confPagosCicloDAO.saveOrUpdate(confPagosCiclo);
			//Insertamos las cuotas
			for (int i = 0; i < nro_cuo.length; i++) {
				if(!id_cuo[i].equals("null")) {
					//Actualizo la información
					BigDecimal costo= new BigDecimal(costo_cuo[i]);
					ConfPagosCicloCuota confPagosCicloCuota = new ConfPagosCicloCuota();
					confPagosCicloCuota.setId(Integer.parseInt(id_cuo[i]));
					confPagosCicloCuota.setId_cfpav(id_cpc);
					confPagosCicloCuota.setNro_cuota(nro_cuo[i]);
					confPagosCicloCuota.setCosto(costo);
					confPagosCicloCuota.setFec_venc(FechaUtil.toDate(fec_venc[i]));
					confPagosCicloCuota.setEst("A");
					confPagosCicloCuotaDAO.saveOrUpdate(confPagosCicloCuota);
				} else {
					//Actualizo la información
					BigDecimal costo= new BigDecimal(costo_cuo[i]);
					ConfPagosCicloCuota confPagosCicloCuota = new ConfPagosCicloCuota();
					confPagosCicloCuota.setId_cfpav(id_cpc);
					//confPagosCicloCuota.setId(id_cuo[i]);
					confPagosCicloCuota.setNro_cuota(nro_cuo[i]);
					confPagosCicloCuota.setCosto(costo);
					confPagosCicloCuota.setFec_venc(FechaUtil.toDate(fec_venc[i]));
					confPagosCicloCuota.setEst("A");
					confPagosCicloCuotaDAO.saveOrUpdate(confPagosCicloCuota);
				}
				
			}
			result.setResult(id_cpc);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/eliminarDescuento/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			//Prmero eliminamos las cuotas
			descuentoCuotaDAO.delete(id);
			//Luego eliminamos el descuento
			descuenConfDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
		//	result.setResult( periodoDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="obtenerDatosDescuento/{id}", method = RequestMethod.GET)
	public AjaxResponseBody obtenerDatosDescuento(@PathVariable Integer id) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			DescuentoConf descuentoConf=descuenConfDAO.get(id);
			/*Param param = new Param();
			param.put("id_fdes", id);
			param.put("est", "A");
			List<DescuentoCuota> descuentoCuotas= descuentoCuotaDAO.listByParams(param, new String[]{"nro_cuota"});
			descuentoConf.setDescuentoCuotas(descuentoCuotas);*/
			result.setResult(descuentoConf);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/obtenerDatosCuotas", method = RequestMethod.GET)
	public AjaxResponseBody obtenerDatosDescuentoCuota(Integer id_fdes, Integer nro_cuota) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			if(id_fdes!=null) {
				Param param = new Param();
				param.put("id_cfpav", id_fdes);
				param.put("nro_cuota", nro_cuota);
				ConfPagosCicloCuota confPagosCicloCuota= confPagosCicloCuotaDAO.getByParams(param);
				result.setResult(confPagosCicloCuota);
			}
			
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/obtenerDatosCuotaxCiclo", method = RequestMethod.GET)
	public AjaxResponseBody obtenerDatosDescuentoxCiclo(Integer id_cct, Integer nro_cuo, Integer nro_cuo_total, Integer id_alu, Integer id_per) {

		AjaxResponseBody result = new AjaxResponseBody();
		
		try {
			ConfPagosCiclo confPagosCiclo= confPagosCicloDAO.getByParams(new Param("id_cct",id_cct));
			BigDecimal monto_ciclo=confPagosCiclo.getCosto();
			Integer nro_cuotas_posibles_pagar=confPagosCiclo.getNum_cuotas();
						
			Map<String,Object> map = new HashMap<String,Object>();
			BigDecimal monto_total_cuota = BigDecimal.ZERO;
			String fecha_venc=null;
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.add(Calendar.DAY_OF_YEAR, 2);
			Date fechaActual=new Date();
			String cadenaFecha=FechaUtil.toString(calendar.getTime());
			System.out.println(cadenaFecha); 
			
			//En el 2022, José solicita que se obtenga el monto de la cuota equivalentemente
			//Primero obtengo el costo del ciclo que ya tengo monto_ciclo
			//Divido para obtener el monto de la cuota
			BigDecimal monto_cuota_act=monto_ciclo.divide(new BigDecimal(nro_cuo_total), 2, RoundingMode.CEILING);
			
			//Logica para cuotas q se paga en menos
			//Hallamos lo q va a pagar en la primera cuota
			//Comentado esto para la cuota 1
			
			/*Param param1 = new Param();
			param1.put("id_cfpav", confPagosCiclo.getId());
			param1.put("nro_cuota", 1);
			ConfPagosCicloCuota confPagosCicloCuota1 = confPagosCicloCuotaDAO.getByParams(param1);
			BigDecimal cuota1=confPagosCicloCuota1.getCosto();*/
			BigDecimal cuota1=monto_cuota_act;
			BigDecimal resta=monto_ciclo.subtract(cuota1);
			Integer nro_cuo_pendientes_dif_1=nro_cuo_total-1;
			//hallamos las cuotas mensulaes de la resta
			BigDecimal cuota_mens_resta=BigDecimal.ZERO;
			if(nro_cuo_pendientes_dif_1>0)
			cuota_mens_resta=resta.divide(new BigDecimal(nro_cuo_pendientes_dif_1), 2, RoundingMode.CEILING);
			
			//Aca todo el cambio
			List<Row> descuentoConf = confPagosCicloDAO.datosDescuentoCuota(confPagosCiclo.getId_cct());
			//Obtengo su ultima matricula en el año anterior y q no sea trasladado
			Periodo periodo= periodoDAO.getByParams(new Param("id",id_per));
			Anio anio_mat=anioDAO.getByParams(new Param("id",periodo.getId_anio()));
			Integer anio_nom=Integer.parseInt(anio_mat.getNom());
			Integer anio_ant=anio_nom-1;
			Integer id_anio_ant=anioDAO.getByParams(new Param("nom",anio_ant)).getId();
			Matricula matricula_anterior=matriculaDAO.getMatriculaAnterior(id_alu, id_anio_ant);
			Row matricula_vigente_colegio=matriculaDAO.getMatricula(id_alu, anio_mat.getId());
			//Hallo el total de descuentos por el ciclo turno
			BigDecimal total_desc = BigDecimal.ZERO;
			if(descuentoConf!=null) {
				for (Row row : descuentoConf) {
					String acu=row.getString("acu");//aqui
					if(acu.equals("1")) {//Si el descuento es acumulable sigo la logica y considero los descuentos
						if(nro_cuo_total<=row.getInt("nro_cuota_max")) {
							if(row.get("nom").equals(Constante.DESCUENTO_EINSTINO)) {
								if(matricula_anterior!=null || matricula_vigente_colegio!=null) {
									String venc=row.getString("venc");
									Date fec_ven=row.getDate("fec_venc");
									BigDecimal descuento=(BigDecimal)row.get("monto");
									//if(nro_cuo_total==1) {
										if(venc.equals("1")) {
											if (fechaActual.before(fec_ven)){
												total_desc=total_desc.add(descuento);
												//monto_total_cuota=monto_ciclo.subtract(descuento);
												//fecha_venc=cadenaFecha;
											} /*else {
												//monto_total_cuota=monto_ciclo;
												//fecha_venc=cadenaFecha;
											}*/
										} else {
											total_desc=total_desc.add(descuento);
										}
								}
							} else {
								String venc=row.getString("venc");
								Date fec_ven=row.getDate("fec_venc");
								BigDecimal descuento=(BigDecimal)row.get("monto");
								//if(nro_cuo_total==1) {
									if(venc.equals("1")) {
										if (fechaActual.before(fec_ven)){
											total_desc=total_desc.add(descuento);
											//monto_total_cuota=monto_ciclo.subtract(descuento);
											//fecha_venc=cadenaFecha;
										} /*else {
											//monto_total_cuota=monto_ciclo;
											//fecha_venc=cadenaFecha;
										}*/
									} else {
										total_desc=total_desc.add(descuento);
									}
							}
							
						
							
						}
					} else {
						if(nro_cuo_total==row.getInt("nro_cuota_max")) {
							if(nro_cuo_total<=row.getInt("nro_cuota_max")) {
								if(row.get("nom").equals(Constante.DESCUENTO_EINSTINO)) {
									if(matricula_anterior!=null || matricula_vigente_colegio!=null) {
										String venc=row.getString("venc");
										Date fec_ven=row.getDate("fec_venc");
										BigDecimal descuento=(BigDecimal)row.get("monto");
										//if(nro_cuo_total==1) {
											if(venc.equals("1")) {
												if (fechaActual.before(fec_ven)){
													total_desc=total_desc.add(descuento);
													//monto_total_cuota=monto_ciclo.subtract(descuento);
													//fecha_venc=cadenaFecha;
												} /*else {
													//monto_total_cuota=monto_ciclo;
													//fecha_venc=cadenaFecha;
												}*/
											} else {
												total_desc=total_desc.add(descuento);
											}
									}
								} else {
									String venc=row.getString("venc");
									Date fec_ven=row.getDate("fec_venc");
									BigDecimal descuento=(BigDecimal)row.get("monto");
									//if(nro_cuo_total==1) {
										if(venc.equals("1")) {
											if (fechaActual.before(fec_ven)){
												total_desc=total_desc.add(descuento);
												//monto_total_cuota=monto_ciclo.subtract(descuento);
												//fecha_venc=cadenaFecha;
											} /*else {
												//monto_total_cuota=monto_ciclo;
												//fecha_venc=cadenaFecha;
											}*/
										} else {
											total_desc=total_desc.add(descuento);
										}
								}
								
							
								
							}
						}
					}
					}	
			}
			
			if(nro_cuo==1 && nro_cuo_total==1) {
				monto_total_cuota=monto_ciclo.subtract(total_desc);
				fecha_venc=cadenaFecha;
			} else if(nro_cuo==1 && nro_cuo_total<=nro_cuotas_posibles_pagar && nro_cuo_total!=1) {
				Param param = new Param();
				param.put("id_cfpav", confPagosCiclo.getId());
				param.put("nro_cuota", nro_cuo);
				ConfPagosCicloCuota confPagosCicloCuota = confPagosCicloCuotaDAO.getByParams(param);
				//monto_total_cuota=confPagosCicloCuota.getCosto().subtract(total_desc);
				//cambio 2022
				monto_total_cuota=cuota1.subtract(total_desc);
				fecha_venc=cadenaFecha;
			} else if(nro_cuo>1 && nro_cuo_total<nro_cuotas_posibles_pagar) {		
				Param param = new Param();
				param.put("id_cfpav", confPagosCiclo.getId());
				param.put("nro_cuota", nro_cuo);
				ConfPagosCicloCuota confPagosCicloCuota = confPagosCicloCuotaDAO.getByParams(param);
				monto_total_cuota=cuota_mens_resta;
				fecha_venc=FechaUtil.toString(confPagosCicloCuota.getFec_venc());
			} else if(nro_cuo>1 && nro_cuo_total==nro_cuotas_posibles_pagar) {		
				Param param = new Param();
				param.put("id_cfpav", confPagosCiclo.getId());
				param.put("nro_cuota", nro_cuo);
				ConfPagosCicloCuota confPagosCicloCuota = confPagosCicloCuotaDAO.getByParams(param);
				monto_total_cuota=cuota_mens_resta;
				//monto_total_cuota=confPagosCicloCuota.getCosto();
				fecha_venc=FechaUtil.toString(confPagosCicloCuota.getFec_venc());
			} 
			/*else if(nro_cuo==1) {
				Param param = new Param();
				param.put("id_cfpav", confPagosCiclo.getId());
				param.put("nro_cuota", nro_cuo);
				ConfPagosCicloCuota confPagosCicloCuota = confPagosCicloCuotaDAO.getByParams(param);
				monto_total_cuota=confPagosCicloCuota.getCosto().subtract(total_desc);
				fecha_venc=cadenaFecha;
			} else {
				Param param = new Param();
				param.put("id_cfpav", confPagosCiclo.getId());
				param.put("nro_cuota", nro_cuo);
				ConfPagosCicloCuota confPagosCicloCuota = confPagosCicloCuotaDAO.getByParams(param);
				monto_total_cuota=confPagosCicloCuota.getCosto();
				fecha_venc=FechaUtil.toString(confPagosCicloCuota.getFec_venc());
			}*/
			
			//hasta aqui
			
			/*if(nro_cuo_total==1) {
				//Busco si existe un descuento cuando se paga en 1 cuota
				Row descuentoConf = confPagosCicloDAO.datosDescuentoCuota(confPagosCiclo.getId_cct());
				if(descuentoConf!=null) {
					String venc=descuentoConf.getString("venc");
					Date fec_ven=descuentoConf.getDate("fec_venc");
					if(venc.equals("1")) {
						if (fechaActual.before(fec_ven)){
							BigDecimal descuento=(BigDecimal)descuentoConf.get("monto");
							monto_total_cuota=monto_ciclo.subtract(descuento);
							fecha_venc=cadenaFecha;
						} else {
							monto_total_cuota=monto_ciclo;
							fecha_venc=cadenaFecha;
						}
					} else {
						BigDecimal descuento=(BigDecimal)descuentoConf.get("monto");
						monto_total_cuota=monto_ciclo.subtract(descuento);
						fecha_venc=cadenaFecha;
					}
					
				} else {
					monto_total_cuota=monto_ciclo;
					fecha_venc=cadenaFecha;
				}
				
			} else {				
				//Busco si existe un descuento cuando se paga en 1 cuota
				//Row descuentoConf = confPagosCicloDAO.datosDescuentoCuota(confPagosCiclo.getId_cct());
				List<Row> descuentoConf = confPagosCicloDAO.datosDescuentoCuota(confPagosCiclo.getId_cct());
				
				////Obtengo el número  maximo de cuotas a la cual es aplicable el descuento
				Integer nro_cuo_max=descuentoConf.getInteger("nro_cuota_max");
				
				if(nro_cuo_total<=nro_cuo_max) {
					//Busco la conficguracion de la cuota
					Param param = new Param();
					param.put("id_cfpav", confPagosCiclo.getId());
					param.put("nro_cuota", nro_cuo);
					ConfPagosCicloCuota confPagosCicloCuota = confPagosCicloCuotaDAO.getByParams(param);
					Date fec_ven_cuo=confPagosCicloCuota.getFec_venc();
					BigDecimal monto_cuota=confPagosCicloCuota.getCosto();
					if(nro_cuo==1) {
						if(descuentoConf!=null) {
							String venc=descuentoConf.getString("venc");
							Date fec_ven=descuentoConf.getDate("fec_venc");
							if(venc.equals("1")) {
								if (fechaActual.before(fec_ven)){
									BigDecimal descuento=(BigDecimal)descuentoConf.get("monto");
									monto_total_cuota=monto_cuota.subtract(descuento);
									fecha_venc=cadenaFecha;
								} else {
									monto_total_cuota=monto_cuota;
									fecha_venc=cadenaFecha;
								}
							} else {
								BigDecimal descuento=(BigDecimal)descuentoConf.get("monto");
								monto_total_cuota=monto_cuota.subtract(descuento);
								fecha_venc=cadenaFecha;
							}
							
						} else {
							monto_total_cuota=monto_ciclo;
							fecha_venc=cadenaFecha;
						}
						
					} else {
						if(descuentoConf!=null) { //Si existe un descuento
							String venc=descuentoConf.getString("venc");
							Date fec_ven_des=descuentoConf.getDate("fec_venc");
							if(venc.equals("1")) {//si vence el descuento
								if (fechaActual.before(fec_ven_des)){
									BigDecimal descuento=(BigDecimal)descuentoConf.get("monto");
									monto_total_cuota=monto_cuota.subtract(descuento);
									fecha_venc=FechaUtil.toString(fec_ven_cuo);
								} else {
									monto_total_cuota=monto_cuota;
									fecha_venc=FechaUtil.toString(fec_ven_cuo);
								}
							} else {
								BigDecimal descuento=(BigDecimal)descuentoConf.get("monto");
								monto_total_cuota=monto_cuota.subtract(descuento);
								fecha_venc=FechaUtil.toString(fec_ven_cuo);
							}
							
						} else {
							monto_total_cuota=monto_cuota;
							FechaUtil.toString(fec_ven_cuo);
						}
					}
				}
				
	
				
			}*/
			
			map.put("monto_total",monto_total_cuota);
			map.put("fec_venc",fecha_venc);
			map.put("estado", "Pendiente");
			map.put("nro_cuota", nro_cuo);
		/*	Param param = new Param();
			param.put("id_fdes", id);
			param.put("est", "A");
			List<DescuentoCuota> descuentoCuotas= descuentoCuotaDAO.listByParams(param, new String[]{"nro_cuota"});
			descuentoConf.setDescuentoCuotas(descuentoCuotas);*/
			result.setResult(map);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="obtenerDatosConfPago/{id_cct}", method = RequestMethod.GET)
	public AjaxResponseBody obtenerDatosCiclo(@PathVariable Integer id_cct ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult(confPagosCicloDAO.get(id_cct));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/ResumendePagos", method = RequestMethod.GET)
	public AjaxResponseBody ResumendePagos(Integer id_cct, Integer nro_cuotas, Integer id_alu, Integer id_per) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			List<MatriculaPagos> listPagos =new ArrayList<MatriculaPagos>(); 
			Map<String,Object> map = new HashMap<String,Object>();
			BigDecimal monto_total_ciclo = BigDecimal.ZERO;
			//BigDecimal descuento_total_ciclo = BigDecimal.ZERO;
					//<MatriculaPagos><MatriculaPagos>();
			//Obtengo el costo total del ciclo
			ConfPagosCiclo confPagosCiclo= confPagosCicloDAO.getByParams(new Param("id_cct",id_cct));
			MatriculaPagos academicoPago = new MatriculaPagos();
			academicoPago.setTip(Constante.PAGO_MATRICULA);
			academicoPago.setMonto(confPagosCiclo.getCosto());
			academicoPago.setDescripcion("Costo Total del Ciclo");
			listPagos.add(academicoPago);
			//Obtengo la lista de descuentos aplicados segun los números de cuotas
			BigDecimal monto_ciclo=confPagosCiclo.getCosto();
			//Map<String,Object> map = new HashMap<String,Object>();
			
			String fecha_venc=null;
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.add(Calendar.DAY_OF_YEAR, 2);
			Date fechaActual=new Date();
			String cadenaFecha=FechaUtil.toString(calendar.getTime());
			System.out.println(cadenaFecha); 
			
			//Aca todo el cambio
			List<Row> descuentoConf = confPagosCicloDAO.datosDescuentoCuota(confPagosCiclo.getId_cct());
			//Obtengo su ultima matricula en el año anterior y q no sea trasladado
			Periodo periodo= periodoDAO.getByParams(new Param("id",id_per));
			Anio anio_mat=anioDAO.getByParams(new Param("id",periodo.getId_anio()));
			Integer anio_nom=Integer.parseInt(anio_mat.getNom());
			Integer anio_ant=anio_nom-1;
			Integer id_anio_ant=anioDAO.getByParams(new Param("nom",anio_ant)).getId();
			Matricula matricula_anterior=matriculaDAO.getMatriculaAnterior(id_alu, id_anio_ant);
			Row matricula_vigente_colegio=matriculaDAO.getMatricula(id_alu, anio_mat.getId());
			//Hallo el total de descuentos por el ciclo turno
			BigDecimal total_desc = BigDecimal.ZERO;
			if(descuentoConf!=null) {
				for (Row row : descuentoConf) {
					String acu=row.getString("acu");//aqui
					if(acu.equals("1")) {//Si el descuento es acumulable sigo la logica y considero los descuentos
						if(nro_cuotas<=row.getInt("nro_cuota_max")) {
							if(row.get("nom").equals(Constante.DESCUENTO_EINSTINO)) {
								if(matricula_anterior!=null || matricula_vigente_colegio!=null) {
									String venc=row.getString("venc");	
									Date fec_ven=row.getDate("fec_venc");
									BigDecimal descuento=(BigDecimal)row.get("monto");
									//if(nro_cuo_total==1) {
										if(venc.equals("1")) {
											if (fechaActual.before(fec_ven)){
													total_desc=total_desc.add(descuento);
											
												MatriculaPagos academicoPago2 = new MatriculaPagos();
												academicoPago2.setTip(Constante.DESCUENTO);
												academicoPago2.setDescripcion(row.getString("nom"));
												academicoPago2.setMonto(descuento);
												listPagos.add(academicoPago2);
												//monto_total_cuota=monto_ciclo.subtract(descuento);
												//fecha_venc=cadenaFecha;
											} /*else {
												//monto_total_cuota=monto_ciclo;
												//fecha_venc=cadenaFecha;
											}*/
										} else {
											total_desc=total_desc.add(descuento);
											MatriculaPagos academicoPago2 = new MatriculaPagos();
											academicoPago2.setTip(Constante.DESCUENTO);
											academicoPago2.setDescripcion(row.getString("nom"));
											academicoPago2.setMonto(descuento);
											listPagos.add(academicoPago2);
										}
								}
							} else {
								String venc=row.getString("venc");
								Date fec_ven=row.getDate("fec_venc");
								BigDecimal descuento=(BigDecimal)row.get("monto");
								//if(nro_cuo_total==1) {
									if(venc.equals("1")) {
										if (fechaActual.before(fec_ven)){
											total_desc=total_desc.add(descuento);
											MatriculaPagos academicoPago2 = new MatriculaPagos();
											academicoPago2.setTip(Constante.DESCUENTO);
											academicoPago2.setDescripcion(row.getString("nom"));
											academicoPago2.setMonto(descuento);
											listPagos.add(academicoPago2);
											//monto_total_cuota=monto_ciclo.subtract(descuento);
											//fecha_venc=cadenaFecha;
										} /*else {
											//monto_total_cuota=monto_ciclo;
											//fecha_venc=cadenaFecha;
										}*/
									} else {
										total_desc=total_desc.add(descuento);
										MatriculaPagos academicoPago2 = new MatriculaPagos();
										academicoPago2.setTip(Constante.DESCUENTO);
										academicoPago2.setDescripcion(row.getString("nom"));
										academicoPago2.setMonto(descuento);
										listPagos.add(academicoPago2);
									}
							}
						}
					} else {//No es acumulable , solo debe tomar para la cuota configurada
						if(nro_cuotas==row.getInt("nro_cuota_max")) {
							if(nro_cuotas<=row.getInt("nro_cuota_max")) {
								if(row.get("nom").equals(Constante.DESCUENTO_EINSTINO)) {
									if(matricula_anterior!=null || matricula_vigente_colegio!=null) {
										String venc=row.getString("venc");
										
										Date fec_ven=row.getDate("fec_venc");
										BigDecimal descuento=(BigDecimal)row.get("monto");
										//if(nro_cuo_total==1) {
											if(venc.equals("1")) {
												if (fechaActual.before(fec_ven)){
														total_desc=total_desc.add(descuento);
													MatriculaPagos academicoPago2 = new MatriculaPagos();
													academicoPago2.setTip(Constante.DESCUENTO);
													academicoPago2.setDescripcion(row.getString("nom"));
													academicoPago2.setMonto(descuento);
													listPagos.add(academicoPago2);
													//monto_total_cuota=monto_ciclo.subtract(descuento);
													//fecha_venc=cadenaFecha;
												} /*else {
													//monto_total_cuota=monto_ciclo;
													//fecha_venc=cadenaFecha;
												}*/
											} else {
												total_desc=total_desc.add(descuento);
												MatriculaPagos academicoPago2 = new MatriculaPagos();
												academicoPago2.setTip(Constante.DESCUENTO);
												academicoPago2.setDescripcion(row.getString("nom"));
												academicoPago2.setMonto(descuento);
												listPagos.add(academicoPago2);
											}
									}
								} else {
									String venc=row.getString("venc");
									Date fec_ven=row.getDate("fec_venc");
									BigDecimal descuento=(BigDecimal)row.get("monto");
									//if(nro_cuo_total==1) {
										if(venc.equals("1")) {
											if (fechaActual.before(fec_ven)){
												total_desc=total_desc.add(descuento);
												MatriculaPagos academicoPago2 = new MatriculaPagos();
												academicoPago2.setTip(Constante.DESCUENTO);
												academicoPago2.setDescripcion(row.getString("nom"));
												academicoPago2.setMonto(descuento);
												listPagos.add(academicoPago2);
												//monto_total_cuota=monto_ciclo.subtract(descuento);
												//fecha_venc=cadenaFecha;
											} /*else {
												//monto_total_cuota=monto_ciclo;
												//fecha_venc=cadenaFecha;
											}*/
										} else {
											total_desc=total_desc.add(descuento);
											MatriculaPagos academicoPago2 = new MatriculaPagos();
											academicoPago2.setTip(Constante.DESCUENTO);
											academicoPago2.setDescripcion(row.getString("nom"));
											academicoPago2.setMonto(descuento);
											listPagos.add(academicoPago2);
										}
								}
							}
						}
						
					}
					}	
			}
			
			
			//hasta aqui
			
			//Creo mi lista de Descuentos
			//List<DescuentoCuota> descuentoCuotas= null;
			/*for (int i = 1; i <= nro_cuotas; i++) {
				BigDecimal monto_total_cuota = BigDecimal.ZERO;
				if(i==1) {
					//Busco si existe un descuento cuando se paga en 1 cuota
					Row descuentoConf = confPagosCicloDAO.datosDescuentoCuota(confPagosCiclo.getId_cct(), i);
					if(descuentoConf!=null) {
						String venc=descuentoConf.getString("venc");
						Date fec_ven=descuentoConf.getDate("fec_venc");
						if(venc.equals("1")) {
							if (fechaActual.before(fec_ven)){
								BigDecimal descuento=(BigDecimal)descuentoConf.get("monto");
								MatriculaPagos academicoPago2 = new MatriculaPagos();
								academicoPago2.setTip(Constante.DESCUENTO);
								academicoPago2.setDescripcion(descuentoConf.getString("nom"));
								academicoPago2.setMonto(descuento);
								listPagos.add(academicoPago2);
								descuento_total_ciclo=descuento_total_ciclo.add(descuento);
								monto_total_cuota=monto_ciclo.subtract(descuento);
								fecha_venc=cadenaFecha;
							} else {
								monto_total_cuota=monto_ciclo;
								fecha_venc=cadenaFecha;
							}
						} else {
							BigDecimal descuento=(BigDecimal)descuentoConf.get("monto");
							MatriculaPagos academicoPago2 = new MatriculaPagos();
							academicoPago2.setTip(Constante.DESCUENTO);
							academicoPago2.setDescripcion(descuentoConf.getString("nom"));
							academicoPago2.setMonto(descuento);
							listPagos.add(academicoPago2);
							descuento_total_ciclo=descuento_total_ciclo.add(descuento);
							monto_total_cuota=monto_ciclo.subtract(descuento);
							fecha_venc=cadenaFecha;
						}
						
					} else {
						monto_total_cuota=monto_ciclo;
						fecha_venc=cadenaFecha;
					}
					
				} else {				
					//Busco si existe un descuento cuando se paga en 1 cuota
					Row descuentoConf = confPagosCicloDAO.datosDescuentoCuota(confPagosCiclo.getId_cct(), i);
					
					//Busco la conficguracion de la cuota
					Param param = new Param();
					param.put("id_cfpav", confPagosCiclo.getId());
					param.put("nro_cuota", i);
					ConfPagosCicloCuota confPagosCicloCuota = confPagosCicloCuotaDAO.getByParams(param);
					Date fec_ven_cuo=confPagosCicloCuota.getFec_venc();
					BigDecimal monto_cuota=confPagosCicloCuota.getCosto();
					if(i==1) {
						if(descuentoConf!=null) {
							String venc=descuentoConf.getString("venc");
							Date fec_ven=descuentoConf.getDate("fec_venc");
							if(venc.equals("1")) {
								if (fechaActual.before(fec_ven)){
									BigDecimal descuento=(BigDecimal)descuentoConf.get("monto");
									MatriculaPagos academicoPago2 = new MatriculaPagos();
									academicoPago2.setTip(Constante.DESCUENTO);
									academicoPago2.setDescripcion(descuentoConf.getString("nom"));
									academicoPago2.setMonto(descuento);
									listPagos.add(academicoPago2);
									descuento_total_ciclo=descuento_total_ciclo.add(descuento);
									monto_total_cuota=monto_cuota.subtract(descuento);
									fecha_venc=cadenaFecha;
								} else {
									monto_total_cuota=monto_cuota;
									fecha_venc=cadenaFecha;
								}
							} else {
								BigDecimal descuento=(BigDecimal)descuentoConf.get("monto");
								MatriculaPagos academicoPago2 = new MatriculaPagos();
								academicoPago2.setTip(Constante.DESCUENTO);
								academicoPago2.setDescripcion(descuentoConf.getString("nom"));
								academicoPago2.setMonto(descuento);
								listPagos.add(academicoPago2);
								descuento_total_ciclo=descuento_total_ciclo.add(descuento);
								monto_total_cuota=monto_cuota.subtract(descuento);
								fecha_venc=cadenaFecha;
							}
							
						} else {
							monto_total_cuota=monto_ciclo;
							fecha_venc=cadenaFecha;
						}
						
					} else {
						if(descuentoConf!=null) { //Si existe un descuento
							String venc=descuentoConf.getString("venc");
							Date fec_ven_des=descuentoConf.getDate("fec_venc");
							if(venc.equals("1")) {//si vence el descuento
								if (fechaActual.before(fec_ven_des)){
									BigDecimal descuento=(BigDecimal)descuentoConf.get("monto");
									MatriculaPagos academicoPago2 = new MatriculaPagos();
									academicoPago2.setTip(Constante.DESCUENTO);
									academicoPago2.setDescripcion(descuentoConf.getString("nom"));
									academicoPago2.setMonto(descuento);
									listPagos.add(academicoPago2);
									descuento_total_ciclo=descuento_total_ciclo.add(descuento);
									monto_total_cuota=monto_cuota.subtract(descuento);
									fecha_venc=FechaUtil.toString(fec_ven_cuo);
								} else {
									monto_total_cuota=monto_cuota;
									fecha_venc=FechaUtil.toString(fec_ven_cuo);
								}
							} else {
								BigDecimal descuento=(BigDecimal)descuentoConf.get("monto");
								MatriculaPagos academicoPago2 = new MatriculaPagos();
								academicoPago2.setTip(Constante.DESCUENTO);
								academicoPago2.setDescripcion(descuentoConf.getString("nom"));
								academicoPago2.setMonto(descuento);
								listPagos.add(academicoPago2);
								descuento_total_ciclo=descuento_total_ciclo.add(descuento);
								monto_total_cuota=monto_cuota.subtract(descuento);
								fecha_venc=FechaUtil.toString(fec_ven_cuo);
							}
							
						} else {
							monto_total_cuota=monto_cuota;
							FechaUtil.toString(fec_ven_cuo);
						}
					}
					
				}
			}*/
			monto_total_ciclo=monto_ciclo.subtract(total_desc);
			map.put("listPagos", listPagos);
			map.put("monto_total", monto_total_ciclo);
			result.setResult(map);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
}
