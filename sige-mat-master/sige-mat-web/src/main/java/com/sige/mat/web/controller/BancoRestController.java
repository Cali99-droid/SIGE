package com.sige.mat.web.controller;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse; 

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sige.common.enums.EnumBanco;
import com.sige.core.dao.cache.CacheManager;
import com.sige.invoice.bean.DocumentoReferencia;
import com.sige.invoice.bean.Impresion;
import com.sige.invoice.bean.ImpresionCabecera;
import com.sige.invoice.bean.ImpresionCliente;
import com.sige.invoice.bean.ImpresionDcto;
import com.sige.invoice.bean.ImpresionItem;
import com.sige.mat.dao.AcademicoPagoDAO;
import com.sige.mat.dao.AlumnoDescuentoDAO;
import com.sige.mat.dao.AnioDAO;
import com.sige.mat.dao.AulaDAO;
import com.sige.mat.dao.BancoDAO;
import com.sige.mat.dao.BecaDAO;
import com.sige.mat.dao.ConfMensualidadDAO;
import com.sige.mat.dao.DescHnoDAO;
import com.sige.mat.dao.DescuentoConfDAO;
import com.sige.mat.dao.DescuentoDAO;
import com.sige.mat.dao.EmpresaDAO;
import com.sige.mat.dao.FamiliarDAO;
import com.sige.mat.dao.GruFamAlumnoDAO;
import com.sige.mat.dao.GruFamDAO;
import com.sige.mat.dao.MatriculaDAO;
import com.sige.mat.dao.MovimientoDAO;
import com.sige.mat.dao.NotaCreditoDAO;
import com.sige.mat.dao.PeriodoDAO;
import com.sige.mat.dao.PersonaDAO;
import com.sige.mat.dao.SituacionMatDAO;
import com.sige.mat.dao.SucursalDAO;
import com.sige.mat.dao.TarifasEmergenciaDAO;
import com.sige.spring.service.FacturacionService;
import com.sige.spring.service.PagosService;
import com.tesla.colegio.model.AcademicoPago;
import com.tesla.colegio.model.Alumno;
import com.tesla.colegio.model.AlumnoDescuento;
import com.tesla.colegio.model.Anio;
import com.tesla.colegio.model.Banco;
import com.tesla.colegio.model.Beca;
import com.tesla.colegio.model.DescHno;
import com.tesla.colegio.model.Descuento;
import com.tesla.colegio.model.DescuentoConf;
import com.tesla.colegio.model.Familiar;
import com.tesla.colegio.model.GruFam;
import com.tesla.colegio.model.GruFamAlumno;
import com.tesla.colegio.model.Matricula;
import com.tesla.colegio.model.Movimiento;
import com.tesla.colegio.model.NotaCredito;
import com.tesla.colegio.model.Periodo;
import com.tesla.colegio.model.Persona;
import com.tesla.colegio.model.SituacionMat;
import com.tesla.colegio.model.Sucursal;
import com.tesla.colegio.model.TarifasEmergencia;
import com.tesla.colegio.model.Usuario;
import com.tesla.colegio.util.Constante;
import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;
import com.tesla.frmk.util.ExcelXlsUtil;
import com.tesla.frmk.util.FechaUtil;
import com.tesla.frmk.util.StringUtil;

@RestController
@RequestMapping(value = "/api/banco")
public class BancoRestController {




	@Autowired
	private BancoDAO bancoDAO;
	
	@Autowired
	private AnioDAO anioDAO;
	
	@Autowired
	private AulaDAO aulaDAO;
	
	@Autowired
	private MatriculaDAO matriculaDAO;
	
	@Autowired
	private DescHnoDAO descHnoDAO;
	
	@Autowired
	private AlumnoDescuentoDAO alumnoDescuentoDAO;
	
	@Autowired
	private AcademicoPagoDAO academicoPagoDAO;

	@Autowired
	private FacturacionService facturacionService;
	
	@Autowired
	private SituacionMatDAO situacionMatDAO;
	
	@Autowired
	private EmpresaDAO empresaDAO;
	
	@Autowired
	private ConfMensualidadDAO confMensualidadDAO;
	
	@Autowired
	private PersonaDAO personaDAO;
	
	@Autowired
	private TarifasEmergenciaDAO tarifasEmergenciaDAO;
	
	@Autowired
	private PagosService pagosService;

	@Autowired
	private CacheManager cacheManager;
	
	@Autowired
	private PeriodoDAO periodoDAO;
	
	@Autowired
	private SucursalDAO sucursalDAO;
	
	@Autowired
	private BecaDAO becaDAO;
	
	@Autowired
	private FamiliarDAO familiarDAO;
	
	@Autowired
	private DescuentoConfDAO descuentoConfDAO;
	
	@Autowired
	private DescuentoDAO descuentoDAO;
	
	@Autowired
	private MovimientoDAO movimientoDAO;
	
	@Autowired
	private GruFamDAO gruFamDAO;
	
	@Autowired
	private GruFamAlumnoDAO gruFamAlumnoDAO;
	
	@Autowired
	private NotaCreditoDAO notaCreditoDAO;
	
	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista() {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(bancoDAO.listFullByParams( new Param(), new String[]{"fba.id"}) );
		
		return result;

	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(Banco banco) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( bancoDAO.saveOrUpdate(banco) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	
	@RequestMapping( value="/actualizarBancoAlu", method = RequestMethod.POST)
	public AjaxResponseBody actualizarBanco(Integer id_mat, Integer id_bco) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			bancoDAO.actualizarBanco(id_mat, id_bco);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			bancoDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( bancoDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping(value = "/genArchivoPagos/{id_anio}/{mes}")
	@ResponseBody
	public void descargarTXT(HttpServletRequest request,HttpServletResponse response,@PathVariable Integer id_anio,@PathVariable Integer mes)  throws Exception {
	  
		response.setContentType("text/plain");

		String archivoDownload =  String.format("%02d", mes) + "-" + Constante.MES[mes-1] + ".txt";

		Integer anio = Integer.parseInt(anioDAO.get(id_anio).getNom());
		
		response.setHeader("Content-Disposition","attachment;filename=" + archivoDownload);

/**
		 * Se debe descomentar, cuando se habilite la parametrizacion
		Param param_des = new Param();
		param_des.put("id_per", id_per);
		DescHno descHno =  descHnoDAO.getByParams(param_des);
		
				BigDecimal desc_hermano= new BigDecimal(10) ; 
				if (descHno !=null)
					desc_hermano = descHno.getMonto();
*/
		BigDecimal desc_hermano = new BigDecimal(10);
		 
		
		try {

			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

			String format = formatter.format(new Date());
			int fecActual = Integer.parseInt(format); 
			//Cuando quiero q genere solo del a�o en curso le paso el id_anio sino no
			//List<Map<String, Object>> pagosList = bancoDAO.pagosMes(id_anio, mes);
			List<Map<String, Object>> pagosList = bancoDAO.pagosMes(id_anio,mes);
			
			StringBuilder sb = new StringBuilder("");
			
			boolean existioPrimerVencimiento = false;
			
			
			for (int i = 0; i < pagosList.size(); i++) {

			 	boolean hizo_cambio_local = (pagosList.get(i).get("solicitud")!=null);
			 	int dia_mora = Integer.parseInt(pagosList.get(i).get("dia_mora").toString());
			
			 	int primerVencimiento =  getFecVencimiento(anio, 3,dia_mora );
				if(fecActual>primerVencimiento)
					existioPrimerVencimiento = true;

				
				Object mensualidadBcoDescObj = pagosList.get(i).get("mensualidad_bco");
				
				Integer cant_hermanos=  Integer.parseInt(pagosList.get(i).get("hermanos").toString());
				Integer id_niv =   Integer.parseInt(pagosList.get(i).get("id_niv").toString());
				
				BigDecimal pago_mes = new BigDecimal(pagosList.get(i).get("monto").toString());
				
				if(hizo_cambio_local)
					pago_mes = new BigDecimal(pagosList.get(i).get("monto_conf").toString());
				
				BigDecimal desc_banco = new BigDecimal(pagosList.get(i).get("desc_banco").toString());
					
				BigDecimal pago_final= new BigDecimal(0);
				
				//CALCULAR EL DESCUENTO SI PAGA PUNTUAL
				int fecVencimiento =  getFecVencimiento(anio, mes,dia_mora);
				
				
				
				if (mensualidadBcoDescObj!=null && fecActual<=fecVencimiento ){
					pago_final= new BigDecimal(mensualidadBcoDescObj.toString());
				}else{
					pago_final=pago_mes;

					//se aplica descuentos si estan en fecha
					if(fecActual<=fecVencimiento){
						//Aplicamos el descuentopagosList.get(i).
						if(cant_hermanos>1 && id_niv != Constante.NIVEL_INICIAL)
							pago_final=pago_mes.subtract(desc_hermano);
						else 
							pago_final=pago_mes;
						//Actualizamos el monto de pago con el descuento si lo hubo


						pago_final = pago_final.subtract(desc_banco);


					}

					if(mes==12 && !existioPrimerVencimiento) //falta validar la fecha
						pago_final = pago_final.divide(new BigDecimal(2));

					
				}


					if (Integer.parseInt(pago_final.toString().replace(".","").toString())!=0 ){
						sb.append("CO\t549937730\t");
						sb.append(pagosList.get(i).get("nro_doc"));
						sb.append("\tPEN\t");
						sb.append(pago_final.toString().replace(".",""));
						sb.append("\tREC\t0035\tC\t");
						sb.append(pagosList.get(i).get("nro_doc"));
						sb.append("\t");
						sb.append(StringUtil.replaceTilde(pagosList.get(i).get("ape_pat").toString()));
						sb.append(" ");
						sb.append(StringUtil.replaceTilde(pagosList.get(i).get("ape_mat").toString()));
						sb.append(" ");
						sb.append(StringUtil.replaceTilde(pagosList.get(i).get("nom").toString()));
						sb.append("\tMENSUALIDAD ");
						sb.append(pagosList.get(i).get("mes"));
						sb.append("\t");
						sb.append(pagosList.get(i).get("id"));
						sb.append("\t07" + String.valueOf(fecVencimiento).substring(4, 6) + String.valueOf(fecVencimiento).substring(0, 4));//TODO PARAMETRIZAR
						sb.append("\n");
					}
				

			}
			
			//InputStream is=IOUtils.toInputStream(sb.toString(),"utf-8");
			InputStream is=IOUtils.toInputStream(sb.toString(),"Cp1252");
			IOUtils.copy(is, response.getOutputStream());

		} catch (IOException e) {

			e.printStackTrace();

		}

	}


	@RequestMapping(value = "/genArchivoAnual/{id_banco}/{id_anio}", produces = "text/plain;charset=windows-1252")
	@ResponseBody
	public void genArchivoPagos(HttpServletRequest request,HttpServletResponse response,@PathVariable Integer id_banco,@PathVariable Integer id_anio )  throws Exception {
	  
		response.setContentType("text/plain");

		String fechaActual = FechaUtil.toStringMYQL(new Date());

		String archivoDownload =  fechaActual + ".txt";

		Integer anio = Integer.parseInt(anioDAO.get(id_anio).getNom());
		
		response.setHeader("Content-Disposition","attachment;filename=" + archivoDownload);
 
		
		
		//CABECERA DEL ARCHIVO
		String cabecera = obtenerCabecera(id_banco);
		Integer total=0;
		
		try {

			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

			String format = formatter.format(new Date());
			int fecActual = Integer.parseInt(format); 
			//Si le pasamos de un a�o especifico, generar� solo de eso, sino de todos
			//List<Map<String, Object>> pagosList = bancoDAO.pagosMes(id_anio);
			List<Map<String, Object>> pagosList = bancoDAO.pagosMes(null,null,null,null);
			
			StringBuilder sb = new StringBuilder("");
			
			sb.append(cabecera);
			
			boolean existioPrimerVencimiento = false;
		
			BigDecimal pagoTotal = BigDecimal.ZERO;
			for (int i = 0; i < pagosList.size(); i++) {

				Object id = pagosList.get(i).get("id");
				System.out.println("id->" + id);

				System.out.println("pagosList.get(0)->" + pagosList.get(i));

				//int dia_mora = Integer.parseInt(pagosList.get(i).get("dia_mora").toString());
				//Busco si existe una tarifa de emergencia
				//Row tarifa= tarifasEmergenciaDAO.listarTarifasPeriodo(Integer.parseInt(pagosList.get(i).get("id_per").toString()), Integer.parseInt(pagosList.get(i).get("id_mes").toString()));
				Row tarifa= tarifasEmergenciaDAO.listarTarifasMEs(Integer.parseInt(pagosList.get(i).get("id_mes").toString()),Integer.parseInt(pagosList.get(i).get("id_anio_ult_mat").toString()));
				System.out.println("tarifa->" + tarifa);
				//System.out.println(pagosList.get(i).get("));
				BigDecimal desc_hermano = new BigDecimal(0);
				if(tarifa!=null){
					desc_hermano=tarifa.getBigDecimal("des_hermano");
					System.out.println("desc_hermano->" + desc_hermano);
				} else{
					desc_hermano = new BigDecimal(10);
				}
				Integer dia_mora= pagosList.get(i).get("dia_mora")!=null ? Integer.parseInt(pagosList.get(i).get("dia_mora").toString()) : null;
			 	boolean hizo_cambio_local = (pagosList.get(i).get("solicitud")!=null);
				
			 	Object mensualidadBcoDescObj = pagosList.get(i).get("mensualidad_bco");
				////System.out.print(""+pagosList.get(i).get("hermanos"));
				//	String cant_hermanos= pagosList.get(i).get("hermanos").toString();
				Integer cant_hermanos=  Integer.parseInt(pagosList.get(i).get("hermanos").toString());
				Integer id_niv =   Integer.parseInt(pagosList.get(i).get("id_niv").toString());
				Integer mes =   Integer.parseInt(pagosList.get(i).get("id_mes").toString());
				
				//logger.debug("ape_pat:" + pagosList.get(i).get("ape_pat") + "|mes:" + mes);
				
				BigDecimal pago_mes = new BigDecimal(pagosList.get(i).get("monto").toString());
				
				//Obtener el tipo de fecha de vencimiento
				String tipo_fec_venc = confMensualidadDAO.getByParams(new Param("id_per",pagosList.get(i).get("id_per"))).getTipo_fec_ven();
				
				Integer id_anio_ult_mat=Integer.parseInt(pagosList.get(i).get("id_anio_ult_mat").toString());
				Anio anio_ult_mat = anioDAO.getByParams(new Param("id",id_anio_ult_mat));

				int primerVencimiento=0;
				if(tipo_fec_venc!=null){
					if(tipo_fec_venc.equals(Constante.TIPO_FEC_VEN_FIN)){
						System.out.println("tipo_fec_venc->" + tipo_fec_venc);
						if(mes.equals("0"))
						primerVencimiento = pagosService.getFecVencimientoFin(Integer.parseInt(pagosList.get(i).get("id_mat").toString()), 3,"MAT");
						else
							primerVencimiento = pagosService.getFecVencimientoFin(Integer.parseInt(pagosList.get(i).get("id_mat").toString()), 3,"MEN");
							
					} else if(tipo_fec_venc.equals(Constante.TIPO_FEC_VEN_DIA)){
						primerVencimiento = getFecVencimiento(Integer.parseInt(anio_ult_mat.getNom()),3,dia_mora);
					}
				} else{
					if(dia_mora!=null){
						 primerVencimiento =  getFecVencimiento(Integer.parseInt(anio_ult_mat.getNom()), 3,dia_mora);
					} 
				}
				
				if(fecActual>primerVencimiento)
					existioPrimerVencimiento = true;

				
				if(hizo_cambio_local)
					pago_mes = new BigDecimal(pagosList.get(i).get("monto_conf").toString());
				
				BigDecimal desc_banco= new BigDecimal(0);
				if(tarifa!=null){
					//System.out.println(2);
					desc_banco = tarifa.getBigDecimal("des_banco");
					if(desc_banco==null)
						desc_banco=new BigDecimal(0);
					System.out.println("desc_banco-->" + desc_banco);
					// pago_mes=tarifa.getBigDecimal("monto");
					if(pagosList.get(i).get("monto_total")!=null)
						pago_mes = new BigDecimal(pagosList.get(i).get("monto_total").toString());

					else
						pago_mes=tarifa.getBigDecimal("monto");
				} else{
					//System.out.println(3);
					if(pagosList.get(i).get("desc_banco")!=null)
						desc_banco = new BigDecimal(pagosList.get(i).get("desc_banco").toString());
				
				}
					
				BigDecimal pago_final= new BigDecimal(0);
				
				System.out.println("1");
				//Calculamos si tiene descuentos 0 definidos
				Param param1 = new Param();
				param1.put("mens", pagosList.get(i).get("id_mes").toString());
				param1.put("id_mat", pagosList.get(i).get("id_mat").toString());
				AcademicoPago descuento = academicoPagoDAO.getByParams(param1);
				
				System.out.println("2");

				
				//CALCULAR EL DESCUENTO SI PAGA PUNTUAL
				int fecVencimiento=0;
				if(tipo_fec_venc!=null){
					if(tipo_fec_venc.equals(Constante.TIPO_FEC_VEN_FIN)){
						System.out.println("3.1");
						System.out.println("3.1-->" + pagosList.get(i).get("id_mat"));
						System.out.println("3.1---->" + mes);
						if(mes.equals("0"))
							primerVencimiento = pagosService.getFecVencimientoFin(Integer.parseInt(pagosList.get(i).get("id_mat").toString()), 3,"MAT");
						else
							primerVencimiento = pagosService.getFecVencimientoFin(Integer.parseInt(pagosList.get(i).get("id_mat").toString()), 3,"MEN");
								
					} else if(tipo_fec_venc.equals(Constante.TIPO_FEC_VEN_DIA)){
						

						fecVencimiento = getFecVencimiento(Integer.parseInt(anio_ult_mat.getNom()),mes,dia_mora);
					}
				} else{
					if(dia_mora!=null){
						System.out.println("3.2");

						fecVencimiento =  getFecVencimiento(Integer.parseInt(anio_ult_mat.getNom()),mes,dia_mora);
					} 
				}
				
				System.out.println("3");

				//int fecVencimiento =  getFecVencimiento(anio, mes, dia_mora);
				BigDecimal des_pag_adelantado= new BigDecimal(0);
				System.out.println("desc_pago_ade->" + descuento.getDesc_pago_adelantado());
				if(descuento.getDesc_pago_adelantado()!=null && tarifa==null )//MV 10/05 - AGREGU�  && tarifa==null
					des_pag_adelantado=descuento.getDesc_pago_adelantado();
				
				//if(descuento.getDesc_hermano()!=null && descuento.getDesc_pago_adelantado()!=null && descuento.getDesc_personalizado()!=null && descuento.getDesc_pronto_pago()!=null){//si el descuento es diferente de nulo y cero entonces se procede a los c�lculos de descuento, caso contrario no
				
				if((descuento.getDesc_hermano()!=null ) && //&& descuento.getDesc_hermano().intValue()==0
					 (descuento.getDesc_pago_adelantado()!=null ) && 
					 (descuento.getDesc_personalizado()!=null )  && 
					 (descuento.getDesc_pronto_pago()!=null )){//si el descuento es diferente de nulo y cero entonces se procede a los c�lculos de descuento, caso contrario no
				
					
					
					if(descuento.getDesc_hermano().compareTo(new BigDecimal(0))!=0 && descuento.getDesc_pago_adelantado().compareTo(new BigDecimal(0))!=0 && descuento.getDesc_personalizado().compareTo(new BigDecimal(0))!=0 && descuento.getDesc_pronto_pago().compareTo(new BigDecimal(0))!=0){
						if (mensualidadBcoDescObj!=null && fecActual<=fecVencimiento ){
							pago_final= new BigDecimal(mensualidadBcoDescObj.toString()).subtract(des_pag_adelantado);
						}else{
							pago_final=pago_mes.subtract(des_pag_adelantado);;

							//se aplica descuentos si estan en fecha
							if(fecActual<=fecVencimiento){
								//Aplicamos el descuentopagosList.get(i).
								if(cant_hermanos>1 && id_niv != Constante.NIVEL_INICIAL)
									pago_final=(pago_mes.subtract(desc_hermano)).subtract(des_pag_adelantado);
								else 
									pago_final=pago_mes.subtract(des_pag_adelantado);;
								//Actualizamos el monto de pago con el descuento si lo hubo


								pago_final = pago_final.subtract(desc_banco).subtract(des_pag_adelantado);;


							}

							//Para el a�o 2020 dejo de funcionar
							/*if(mes==12 && !existioPrimerVencimiento) //falta validar la fecha
								pago_final = pago_final.divide(new BigDecimal(2));*/
						}
					} else{
						if(tarifa!=null){
							if(mensualidadBcoDescObj!=null){
								BigDecimal monto_banco=new BigDecimal(mensualidadBcoDescObj.toString());
								if(tarifa.getBigDecimal("monto").compareTo(monto_banco)<=0){ //Si mi tarifa es menor al monto, pongo la tarifa del monto
									pago_final= pago_mes.subtract(des_pag_adelantado);
									//se aplica descuentos si estan en fecha
									if(fecActual<=fecVencimiento){
										//Aplicamos el descuentopagosList.get(i).
										if(cant_hermanos>1 && id_niv != Constante.NIVEL_INICIAL)
											pago_final=pago_mes.subtract(desc_hermano).subtract(des_pag_adelantado);
										else 
											pago_final=pago_mes;
										//Actualizamos el monto de pago con el descuento si lo hubo


										pago_final = pago_final.subtract(desc_banco).subtract(des_pag_adelantado);;


									}
								} else if(tarifa.getBigDecimal("monto").compareTo(monto_banco)>0){//Si mi tarifa es mayor, se mantiene el del banco
									if (mensualidadBcoDescObj!=null && fecActual<=fecVencimiento ){
										pago_final= new BigDecimal(mensualidadBcoDescObj.toString()).subtract(des_pag_adelantado);
									} else{
										pago_final=pago_mes.subtract(des_pag_adelantado);
									}
								}
							} else{
								pago_final=pago_mes.subtract(des_pag_adelantado);
							}
							
						} else{
							if (mensualidadBcoDescObj!=null && fecActual<=fecVencimiento ){
								pago_final= new BigDecimal(mensualidadBcoDescObj.toString()).subtract(des_pag_adelantado);
							} else{
								pago_final=pago_mes.subtract(des_pag_adelantado);
							}
						}
					}
				} else{
					if(tarifa!=null){
						if(mensualidadBcoDescObj!=null){
							BigDecimal monto_banco=new BigDecimal(mensualidadBcoDescObj.toString());
							if(tarifa.getBigDecimal("monto").compareTo(monto_banco)<=0){ //Si mi tarifa es menor al monto, pongo la tarifa del monto
								pago_final=pago_mes.subtract(des_pag_adelantado);
								//se aplica descuentos si estan en fecha
								if(fecActual<=fecVencimiento){
									//Aplicamos el descuentopagosList.get(i).
									if(cant_hermanos>1 && id_niv != Constante.NIVEL_INICIAL)
										pago_final=pago_mes.subtract(desc_hermano).subtract(des_pag_adelantado);
									else 
										pago_final=pago_mes;
									//Actualizamos el monto de pago con el descuento si lo hubo


									pago_final = pago_final.subtract(desc_banco).subtract(des_pag_adelantado);;


								}
							} else if(tarifa.getBigDecimal("monto").compareTo(monto_banco)>0){//Si mi tarifa es mayor, se mantiene el del banco
								if (mensualidadBcoDescObj!=null && fecActual<=fecVencimiento ){
									pago_final= new BigDecimal(mensualidadBcoDescObj.toString()).subtract(des_pag_adelantado);
								} else{
									pago_final=pago_mes.subtract(des_pag_adelantado);
								}
							}
						} else{
							pago_final=pago_mes;
							
							if(fecActual<=fecVencimiento){
								//Aplicamos el descuentopagosList.get(i).
								if(cant_hermanos>1 && id_niv != Constante.NIVEL_INICIAL)
									pago_final=(pago_mes.subtract(desc_hermano));
								

							}
							
						}
					} else{
						if (mensualidadBcoDescObj!=null && fecActual<=fecVencimiento ){
							pago_final= new BigDecimal(mensualidadBcoDescObj.toString()).subtract(des_pag_adelantado);;
						}else{
							pago_final=pago_mes.subtract(des_pag_adelantado);;

							//se aplica descuentos si estan en fecha
							if(fecActual<=fecVencimiento){
								//Aplicamos el descuentopagosList.get(i).
								if(cant_hermanos>1 && id_niv != Constante.NIVEL_INICIAL)
									pago_final=pago_mes.subtract(desc_hermano).subtract(des_pag_adelantado);
								else 
									pago_final=pago_mes;
								//Actualizamos el monto de pago con el descuento si lo hubo


								pago_final = pago_final.subtract(desc_banco).subtract(des_pag_adelantado);;


							}
					}
					

						/*if(mes==12 && !existioPrimerVencimiento) //falta validar la fecha
							pago_final = pago_final.divide(new BigDecimal(2));*/ //ya no usado para el decuento anual

						
					}
				}
				


				if (id_banco.intValue() == EnumBanco.FINANCIERO.getValue()){

					if (Integer.parseInt(pago_final.toString().replace(".","").toString())!=0 ){
						sb.append("CO\t549937730\t");
						sb.append(pagosList.get(i).get("nro_doc"));
						sb.append("\tPEN\t");
						sb.append(pago_final.toString().replace(".",""));
						sb.append("\tREC\t0035\tC\t");
						sb.append(pagosList.get(i).get("nro_doc"));
						sb.append("\t");
						sb.append(StringUtil.replaceTilde(pagosList.get(i).get("ape_pat").toString()));
						sb.append(" ");
						sb.append(StringUtil.replaceTilde(pagosList.get(i).get("ape_mat").toString()));
						sb.append(" ");
						sb.append(StringUtil.replaceTilde(pagosList.get(i).get("nom").toString()));
						sb.append("\tMENSUALIDAD ");
						sb.append(pagosList.get(i).get("mes"));
						sb.append("\t");
						sb.append(pagosList.get(i).get("id"));
						sb.append("\t07" + String.valueOf(fecVencimiento).substring(4, 6) + String.valueOf(fecVencimiento).substring(0, 4));//TODO PARAMETRIZAR
						sb.append("\n");
					}

				}else if(id_banco.intValue() == EnumBanco.CONTINENTAL.getValue()){
					//if (Integer.parseInt(pago_final.toString().replace(".","").toString())!=0 ){
						total++;
						String cliente = StringUtil.replaceTilde(pagosList.get(i).get("ape_pat").toString()) + " " + StringUtil.replaceTilde(pagosList.get(i).get("ape_mat").toString()) + " " + StringUtil.replaceTilde(pagosList.get(i).get("nom").toString());
						String concepto = "MENSUALIDAD " + pagosList.get(i).get("mes");
						sb.append("\n02");//Indicador de detalle 02 (*)
						sb.append(StringUtil.rellenaVacio(cliente,30)); //nombre del cliente
						sb.append(StringUtil.rellenaCaracterIzq(pagosList.get(i).get("nro_doc").toString(),11,"0"));
						sb.append(StringUtil.rellenaVacio(concepto, 24));
						sb.append("COD-" + StringUtil.rellenaCaracterIzq(pagosList.get(i).get("id").toString(), 9, "0"));
						sb.append(fecVencimiento);
						sb.append("20301231");
						sb.append(StringUtil.rellenaCaracterIzq(String.valueOf(mes),2,"0"));
						sb.append(StringUtil.rellenaCaracterIzq(pago_final.toString().replace(".",""),15,"0")); //Se colocara 13 enteros y 2 decimales sin punto ni comas
						sb.append(StringUtil.rellenaCaracterIzq(pago_final.toString().replace(".",""),15,"0")); //Se colocara 13 enteros y 2 decimales sin punto ni comas
						sb.append(StringUtil.repiteCaracter(32, "0"));
						sb.append(StringUtil.repiteCaracter(16*8, "0"));
						sb.append(StringUtil.repiteCaracter(72, " "));
						
						pagoTotal = pagoTotal.add(pago_final);
					//}
				}
				

			}
			
			//inicio - pie de archivo
			if(id_banco.intValue() == EnumBanco.CONTINENTAL.getValue()){
				sb.append("\n03");//Indicador de detalle�02 (*)
				//sb.append(StringUtil.rellenaCaracterIzq(Integer.toString(pagosList.size()),9,"0"));
				sb.append(StringUtil.rellenaCaracterIzq(Integer.toString(total),9,"0"));
				sb.append(StringUtil.rellenaCaracterIzq(pagoTotal.toString().replace(".",""),18,"0")); //18
				sb.append(StringUtil.rellenaCaracterIzq(pagoTotal.toString().replace(".",""),18,"0")); //18
				sb.append(StringUtil.repiteCaracter(18, "0"));
				sb.append(StringUtil.repiteCaracter(295, " "));

			}
			
			
			
			

			/*
			InputStream is=IOUtils.toInputStream(sb.toString());
			response.setContentType("text/plain");
			response.setCharacterEncoding("windows-1252");
			response.setHeader("Content-Type", "text/xml; charset=Windows-1252");

			IOUtils.copy(is, response.getOutputStream());
			*/

			response.setContentType("text/plain");
			response.setCharacterEncoding("windows-1252");

			PrintWriter out = response.getWriter();
			  out.append(sb.toString());
			  out.close();
			  
		} catch (IOException e) {

			e.printStackTrace();

		}

	}

	
		
	
	/**
	 * 
	 * @param mes
	 * @return
	 */
	private int getFecVencimiento(int anio, int mes, int dia) throws Exception{
		
		int anioSiguiente;
		int mesSiguiente;
		
		if (mes==12){
			anioSiguiente = anio + 1;
			mesSiguiente = 1;
		}else{
			anioSiguiente = anio;
			mesSiguiente = mes+1;
		}
		
		String fecVenc = anioSiguiente + String.format("%02d", mesSiguiente) + String.format("%02d", dia);//TODO PARAMETRIZAR FECHA
		
		
		return Integer.parseInt(fecVenc);
		
	}
	
	@RequestMapping(value = "/pagosBancoMes")
	public List<Map<String,Object>> getPagosPorMes(Integer id_anio, Integer mes )  throws IOException {
		//consultar  y devolver
		/*
		dni alumno
		Monto a pagar ( restale descuento de hermano)
		apellido paterno apellido materno, 
		nombre del mes
		 */
		id_anio=2;
		mes=3;
		//Lista de pagos por mes y anio
		List<Map<String, Object>> pagosList = bancoDAO.pagosMes(id_anio, mes);
		for (int i = 0; i < pagosList.size(); i++) {
			Integer id_mat = Integer.parseInt(pagosList.get(i).get("id_mat").toString());
			Integer id_per=Integer.parseInt(pagosList.get(i).get("id_per").toString());
			String num_cont=pagosList.get(i).get("num_cont").toString();
			//Obtenemos el descuento por hermano de acuerdo al periodo que pertenece
			Param param_des = new Param();
			param_des.put("id_per", id_per);
			DescHno descHno =  descHnoDAO.getByParams(param_des);
			
			BigDecimal desc_hermano= new BigDecimal(10) ; 
			if (descHno !=null)
				desc_hermano = descHno.getMonto();
			//Buscamos si el alumno tiene hermano con el mismo numero de contrato
			List<Matricula> hermanoList = matriculaDAO.listFullByParams(new Param("num_cont",num_cont),new String[]{"mat.id"});
			Integer cant_hermanos=0;
			for (Matricula matriculalist : hermanoList) {
				if( !(new Integer(5)).equals(matriculalist.getId_sit() ) && !matriculalist.getId_niv().equals(Constante.NIVEL_INICIAL) )//si el hermano no esta trasladado
					cant_hermanos=cant_hermanos+1;
			}
			//Obtenemos el monto de pago por el mes y matricula
			
			Param param = new Param();
			param.put("id_mat", id_mat);
			param.put("tip", "MEN");
			param.put("mens", mes);
			AcademicoPago pago_mes = academicoPagoDAO.getByParams(param);
			
			BigDecimal pago_final= new BigDecimal(0);
			//Aplicamos el descuento
			if(cant_hermanos>1)
				pago_final=pago_mes.getMonto().subtract(desc_hermano);
			else 
				pago_final=pago_mes.getMonto();
			//Actualizamos el monto de pago con el descuento si lo hubo
			pagosList.get(i).put("monto", pago_final);
		}
		 return pagosList;
	 }

	/**
	 * Procesar el archivo excel del banco FINANCIERO TODO FALTA PARAMETRIAR POR BANCO
	 * @param uploadfile
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/xls/upload")
	public AjaxResponseBody uploadFile(@RequestParam("file") MultipartFile uploadfile,Integer id_banco,Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();

		if (uploadfile.isEmpty()) {
			result.setCode("ARCHIVO");
			result.setMsg("archivo es vacio");
			return result;
		}

		try {

			InputStream is = uploadfile.getInputStream();

			List<Map<String,Object>> listaCargada = null;
			if (id_banco.intValue() == EnumBanco.FINANCIERO.getValue())
				listaCargada= procesaExcelBancoFinanciero(is,true);
			if (id_banco.intValue() == EnumBanco.CONTINENTAL.getValue())
				listaCargada= procesaTXTBancoContinental(is,true);
			
			result.setResult(listaCargada);
			return result;
			
		} catch (Exception e) {
			result.setCode("ARCHIVO");
			result.setMsg("archivo con errores:" + e.getMessage());
			e.printStackTrace();
			return result;
		}

	}
	/**
	 * Procesar el archivo excel del banco FINANCIERO TODO FALTA PARAMETRIAR POR BANCO
	 * @param uploadfile
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/xls/vistaPrevia")
	public AjaxResponseBody uploadFileParaVistaPrevia(@RequestParam("file") MultipartFile uploadfile,Integer id_banco) {

		AjaxResponseBody result = new AjaxResponseBody();

		if (uploadfile.isEmpty()) {
			result.setCode("ARCHIVO");
			result.setMsg("archivo es vacio");
			return result;
		}

		try {

			if (id_banco.intValue() == EnumBanco.FINANCIERO.getValue()){
				InputStream is = uploadfile.getInputStream();
				List<Map<String,Object>> listaCargada= procesaExcelBancoFinanciero(is,false);
				result.setResult(listaCargada);
			}
			
			if (id_banco.intValue() == EnumBanco.CONTINENTAL.getValue()){
				InputStream is = uploadfile.getInputStream();
				List<Map<String,Object>>  listaCargada= procesaTXTBancoContinental(is,false);
				result.setResult(listaCargada);
			}
			
			return result;
			
		} catch (Exception e) {
			result.setCode("ARCHIVO");
			result.setMsg("archivo con errores:" + e.getMessage());
			e.printStackTrace();
			return result;
		}

	}
	
/*	//Usado para actualizar insertar movimientos q no estaban , pero si en fac_academico_pago
	@RequestMapping(method = RequestMethod.GET, value = "/procesarMovimientosFaltantes")
	public AjaxResponseBody procesarFaltantes() {
		AjaxResponseBody result = new AjaxResponseBody();
		try{
			/*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date fecha = sdf.format("2019-12-03"); 
			//System.out.println(fecha); */
/*			String dateInString = "2019-12-03";
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			    Date date = formatter.parse(dateInString);
			 facturacionService.cargarMovimientosBanco(date);
			 result.setMsg("echo");
			 return result;
		} catch (Exception e) {
			result.setCode("ARCHIVO");
			result.setMsg("archivo con errores:" + e.getMessage());
			e.printStackTrace();
			return result;
		}
	}*/
	
	@Transactional
	public List<Map<String,Object>> procesaExcelBancoFinanciero(InputStream inputStream, boolean debeGrabar) throws Exception{

		int filaInicial = 3;//FILA INICIAL DONDE EMPIEZA LA LECTURA
		
		int columnaIdPagoBanco= 0;
		int columnaMontoVentanilla= 5;
		int columnaFechaProceso= 7;
		int columnaIdPagoColegio= 10;

		List<Map<String,Object>> listBanco = new ArrayList<Map<String,Object>>();
		int linea = 0;

		//Map<Integer,String> nroSerieLocal = new HashMap<Integer,String>();
		String  nroSerieLocal = null;
		if (!debeGrabar){
			//calcular el correlativo manualmente
			//List<Sucursal> sucurales = sucursalDAO.list();
			//for (Sucursal sucursal : sucurales) {
			//	nroSerieLocal.put(sucursal.getId(), facturacionService.getNroRecibo(sucursal.getId()));
			//}
			nroSerieLocal =  facturacionService.getNroReciboBanco(1);
		}
		
		Date fechaProcesoExcel = null;		
		
		try {
		
			//FileInputStream inputStream = new FileInputStream(new File(archivo));
			Workbook workbook = WorkbookFactory.create(inputStream);

			Sheet sheet = workbook.getSheetAt(0);
			String idPagoBanco = sheet.getRow(filaInicial).getCell(columnaIdPagoBanco).getStringCellValue();
			String montoVentanilla;
			String fechaProceso;
			String idPagoColegio;
				
			while(idPagoBanco!=null && !"".equals(idPagoBanco) ){
				linea++;
				
				idPagoBanco = sheet.getRow(filaInicial).getCell(columnaIdPagoBanco).getStringCellValue();
				montoVentanilla= sheet.getRow(filaInicial).getCell(columnaMontoVentanilla).getStringCellValue();
				fechaProceso= sheet.getRow(filaInicial).getCell(columnaFechaProceso).getStringCellValue();
				idPagoColegio= sheet.getRow(filaInicial).getCell(columnaIdPagoColegio).getStringCellValue();
		
				Map<String,Object> map = new HashMap<String,Object>();

				map.put("idPagoBanco", idPagoBanco);
				map.put("montoVentanilla", montoVentanilla);
				map.put("fechaProceso", fechaProceso);

				BigDecimal montoVentanillaExcel = new BigDecimal(montoVentanilla);
				 fechaProcesoExcel = FechaUtil.toDate(fechaProceso, "dd/MM/yyyy");
				
				
				AcademicoPago academicoPago = academicoPagoDAO.get(Integer.parseInt(idPagoColegio));
				Map<String,Object> matricula = matriculaDAO.getMatriculaDatosPrincipales(academicoPago.getId_mat());

				map.put("alumno", matricula.get("alumno"));
				map.put("local", matricula.get("local"));
				map.put("nivel", matricula.get("nivel"));
				map.put("montoMes", academicoPago.getMonto());
				
				//INICIO - descuentos
				//Obtenemos el descuento por hermano de acuerdo al periodo que pertenece
				Param param_des = new Param();
				param_des.put("id_per", matricula.get("id_per"));

				/* TODO, FALTA PARAMETRIZAR DESCUENTOS POR HREMANO
				DescHno descHno =  descHnoDAO.getByParams(param_des);

				BigDecimal desc_hermano= new BigDecimal(10) ; //TODO, lina por favor vuelve a abrir el mantenimiento de descuento de hermanos
				if (descHno !=null)
					desc_hermano = descHno.getMonto();
				*/
				//Buscamos si el alumno tiene hermano con el mismo numero de contrato
				List<Matricula> hermanoList = matriculaDAO.listFullByParams(new Param("num_cont",matricula.get("num_cont")),new String[]{"mat.id"});
				Integer cant_hermanos=0;
				for (Matricula matriculalist : hermanoList) {
					SituacionMat situacionMat=situacionMatDAO.getByParams(new Param("id_mat",matriculalist.getId()));
					Integer id_sit=null;
					if(situacionMat!=null)
						id_sit=situacionMat.getId_sit();
					if(id_sit!=null){
						if( (id_sit!=null && id_sit!=5) && !matriculalist.getId_niv().equals(Constante.NIVEL_INICIAL) )//si el hermano no esta trasladado*/
							cant_hermanos=cant_hermanos+1;
					} else{
						//if( !matriculalist.getId_niv().equals(Constante.NIVEL_INICIAL) )//si el hermano no esta trasladado*/
							cant_hermanos=cant_hermanos+1;	
					}
					
					
				}
				
				//Obtener descuento personalizado si es que lo tiene
				Map<String, Object> alumnoDescuento = alumnoDescuentoDAO.getAlumnoPorMatricula(academicoPago.getId_mat());
				BigDecimal descuentoPersonalizado = null;
				BigDecimal descuentoHermano = null;
				String id_descuento_personalizado = null;
				
				if (alumnoDescuento!=null){
					descuentoPersonalizado = new BigDecimal(alumnoDescuento.get("descuento").toString());
				}

				
				/*
				Calendar cal = Calendar.getInstance();
				int anio_actual = cal.get(Calendar.YEAR);
				int fecVencimiento =  getFecVencimiento(anio_actual, academicoPago.getMens(),dia_mora);//TODO PRIMER VENCIMIENTO.. PARAMETRIZAR 

				SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
				String format = formatter.format(fechaProcesoExcel);
				int fecActual = Integer.parseInt(format); 
			
				
				if (descuentoPersonalizado!=null && fecActual<=fecVencimiento ){
					//meses_pagar.setDesc_personalizado(descuentoPersonalizado);
					descuentoHermano = new BigDecimal(0);
				}else{
					descuentoPersonalizado = new BigDecimal(0);
					
					if (fecActual<=fecVencimiento){
					 	
						if(cant_hermanos>1 && Integer.parseInt(matricula.get("id_niv").toString())!=Constante.NIVEL_INICIAL ){
							descuentoHermano = desc_hermano;
						}
						else
							descuentoHermano = new BigDecimal(0);
 

					}else{
						descuentoHermano = new BigDecimal(0);
						
					}
					

				}
				*/
				
				//FIN - descuentos

				
				if (academicoPago==null ){
					map.put("procesado", "ERROR: Alumno no encontrado");
				}else{
					if ("1".equals(academicoPago.getCanc())){
						//NO SE HACE NADA, POR QUE YA EST� CANCELADO
						map.put("procesado", "NO");
						map.put("nro_rec", academicoPago.getNro_rec());
						map.put("montoVentanilla", academicoPago.getMontoTotal());
						map.put("fechaProceso",FechaUtil.toString(academicoPago.getFec_pago()));
						
					}else{
						String nuevo_nro_recivo = "";
						if (debeGrabar)
							nuevo_nro_recivo = facturacionService.getNroReciboBanco(1);//1== codigo de banco
						else{
							//incrementarlo manualmente
							//if(linea==1)
								nuevo_nro_recivo = nroSerieLocal;
							//else
								nuevo_nro_recivo = nuevo_nro_recivo.split("-")[0] + "-" + String.format("%06d", Integer.parseInt(nuevo_nro_recivo.split("-")[1]) + 1);
							nroSerieLocal = nuevo_nro_recivo;
						}
						
						academicoPago.setBanco("FINANCIERO");//debe parametrizarse por archivo
						academicoPago.setCanc("1");
						BigDecimal descuentoProntoPago = academicoPago.getMonto().subtract(montoVentanillaExcel);
						
						if(descuentoPersonalizado.compareTo(new BigDecimal(0))>0){
							
							academicoPago.setDesc_personalizado(descuentoPersonalizado);
							descuentoProntoPago = new BigDecimal(0);

						}else{
							if(descuentoHermano.compareTo(new BigDecimal(0))>0)
								academicoPago.setDesc_hermano(descuentoHermano);
							
							  descuentoProntoPago = descuentoProntoPago.subtract(descuentoHermano);
							  academicoPago.setDesc_pronto_pago(descuentoProntoPago);
						}
						
						
						academicoPago.setMontoTotal(montoVentanillaExcel);
						academicoPago.setFec_pago(fechaProcesoExcel);
						academicoPago.setNro_rec(nuevo_nro_recivo);
						academicoPago.setNro_pe(idPagoBanco);
						academicoPago.setUsr_ins(1);
						map.put("procesado", "SI");
						map.put("nro_rec", nuevo_nro_recivo);

						if (debeGrabar){
							academicoPagoDAO.saveOrUpdate(academicoPago);
							facturacionService.updateNroReciboBanco(1, nuevo_nro_recivo);
							
						}

					}
				}
				
				listBanco.add(map);
				
				
				
				filaInicial++;

				if (sheet.getRow(filaInicial)==null || sheet.getRow(filaInicial).getCell(columnaIdPagoBanco)==null)
					idPagoBanco = null;
				else
					idPagoBanco = sheet.getRow(filaInicial).getCell(columnaIdPagoBanco).getStringCellValue();

			}
			
			if (debeGrabar)
				facturacionService.cargarMovimientosBanco(fechaProcesoExcel);//TODO FALTA USUARIO
			

			inputStream.close();

			workbook.close();


		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Registro nro:" + linea + ", error: " +e.getMessage());
		}

		return listBanco ;
	}

	
	
/**
 * Graba los pagos del banco continental al sige, en caso, el registro ya ha sido procesado anteriormente, simplemente se ignora
 * 
 * @param inputStream
 * @param debeGrabar
 * @return
 * @throws Exception
 */
	@SuppressWarnings("unused")
	@Transactional
	public List<Map<String,Object>>  procesaTXTBancoContinental(InputStream inputStream, boolean debeGrabar ) throws Exception{


        BufferedReader br = null;
         
        Reader r = new InputStreamReader(inputStream);
        String line;

        StringBuilder sb = new StringBuilder();
        char[] chars = new char[4*1024];
        int len;
        while((len = r.read(chars))>=0) {
            sb.append(chars, 0, len);
        }
       
        String  nroSerieLocal = null;
		if (!debeGrabar){
			nroSerieLocal =  facturacionService.getNroReciboBanco(2);
			nroSerieLocal = nroSerieLocal.split("-")[0] + "-" + String.format("%06d", Integer.parseInt(nroSerieLocal.split("-")[1]) - 1);
		}
		
        String[] lines = sb.toString().split(System.getProperty("line.separator"));
       
        //leyendo linea 1

        String line1 = lines[0];
        //if (line1.length()<152)
       	if (line1.trim().length()<45)
       		throw new Exception("Archivo no tiene tama�o de cabecera correcto");
        
        String tipoRegistro 	= line1.substring(0,2); 
        String ruc 				= line1.substring(2,13);
        String clase 			= line1.substring(13,16);
        String moneda 			= line1.substring(16,19);
        String fecha 			= line1.substring(19,27);
        String cuentaRecaudadora= line1.substring(27,45);
        
        if (!tipoRegistro.equals("01")){
        	throw new Exception("Archivo no tiene el indicador de cabecera correcto");
        }
        
        if (!ruc.equals("20531084587")){
        	throw new Exception("Archivo no tiene el RUC correcto");
        }
        
        List<Map<String,Object>> listBanco = new ArrayList<Map<String,Object>>();
        
        //String fechaPago		= linea.substring(135,143);
        
        Date fechaProcesoExcel 		= FechaUtil.toDate(fecha, "yyyyMMdd");
        
        //leer lineas detalle
        List<String> listLineas = new ArrayList<String>();
        
        for (int i = 1; i < lines.length; i++) {
        	
			String linea = lines[i].replaceAll("\\r\\n|\\r|\\n", "");//ELIMINA LOS 'ENTER'
			tipoRegistro = linea.substring(0,2);
			if  (tipoRegistro.equals("02")){
		
				if (linea.trim().length()!=151)
					throw new Exception("La linea " + (++i) + " no tiene el tama�o correcto:" + linea + ":");
				
				String fechaPago		= linea.substring(135,143);
				
				listLineas.add(linea) ;
				
			}
			
			if  (tipoRegistro.equals("03")){
				//TOTALES				
				break;
			}
		}
        
        Collections.sort(listLineas, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                // return p1.age+"".compareTo(p2.age+""); //sort by age
                return s1.substring(71,80).compareTo(s2.substring(71,80)); // if you want to short by name
                //return s1.substring(32,80).compareTo(s2.substring(135,143)); // if you want to short by name
            }
        });
        
        
        
        
        //procesar lineas detalle
        
        for (String linea : listLineas) {
			
		
        //for (int i = 1; i < lines.length; i++) {
        	
			//String linea = lines[i].replaceAll("\\r\\n|\\r|\\n", "");//ELIMINA LOS 'ENTER'
			tipoRegistro = linea.substring(0,2);
			

				String nombreCliente	= linea.substring(2,32);
				String referencias		= linea.substring(32,80);
				String importeOrigen	= linea.substring(80,95);
				String strImporteDepositado= linea.substring(95,110);
				String importeMora		= linea.substring(110,125);
				String oficina			= linea.substring(125,129);
				String nroMovimiento	= linea.substring(129,135);
				String fechaPago		= linea.substring(135,143);
				Date fechaPagoDate 		= FechaUtil.toDate(fechaPago, "yyyyMMdd");
				String tipoValor		= linea.substring(143,145);
				String canal			= linea.substring(145,147);
				String vacio			= linea.substring(147);

				//fechaProcesoExcel 		= FechaUtil.toDate(fechaPago, "yyyyMMdd");
				String descripcion		= referencias.substring(11,35);
				String strIdPago		= referencias.substring(39);
				Integer id_pago 		= Integer.parseInt(strIdPago); 
				
				AcademicoPago pago = academicoPagoDAO.get(id_pago);
				
				//Ver si esta pagado el mes anterior
				
				
				/*if(!fechaPago.equals(fecha)){
					//NO PROCESAR
					continue;
				}*/
				
			 	int dia_mora = 10;
			 	boolean existioPrimerVencimiento = false;
	/*		 	
			 	int primerVencimiento =  getFecVencimiento(id_anio, 3,dia_mora );
				if(Integer.parseInt(fechaPago)>primerVencimiento)
					existioPrimerVencimiento = true;
*/

				Map<String,Object> map = new HashMap<String,Object>();

				//transformar importe a BigDecimal
				BigDecimal montoVentanilla = new BigDecimal(strImporteDepositado);
				montoVentanilla = montoVentanilla.divide(new BigDecimal(100),2);
				
				map.put("idPagoBanco", id_pago);
				map.put("montoVentanilla", montoVentanilla);
				//map.put("fechaProceso", fechaPago);
				map.put("fechaProceso", fechaPago);
				
				
				////System.out.println(id_pago);
				AcademicoPago academicoPago = academicoPagoDAO.get(id_pago);
				
				Map<String,Object> matricula = matriculaDAO.getMatriculaDatosPrincipales(academicoPago.getId_mat());

				map.put("alumno", matricula.get("alumno"));
				map.put("local", matricula.get("local"));
				map.put("nivel", matricula.get("nivel"));
				map.put("montoMes", academicoPago.getMonto());
				
				
				if (academicoPago==null ){
					map.put("procesado", "ERROR: Alumno no encontrado");
				}else{
					if ("1".equals(academicoPago.getCanc())){
						//NO SE HACE NADA, POR QUE YA EST� CANCELADO
						map.put("procesado", "NO");
						map.put("nro_rec", academicoPago.getNro_rec());
						map.put("montoVentanilla", academicoPago.getMontoTotal());
						map.put("fechaProceso",FechaUtil.toString(academicoPago.getFec_pago()));
						map.put("mes",academicoPago.getMes());
						
					}else{
						String nuevo_nro_recivo = "";
						if (debeGrabar)
							nuevo_nro_recivo = facturacionService.getNroReciboBanco(2);//1== codigo de banco
						else{
							//incrementarlo manualmente
							//if(linea==1)
								nuevo_nro_recivo = nroSerieLocal;
							//else
								nuevo_nro_recivo = nuevo_nro_recivo.split("-")[0] + "-" + String.format("%06d", Integer.parseInt(nuevo_nro_recivo.split("-")[1]) + 1);
							nroSerieLocal = nuevo_nro_recivo;
						}
						
						
						//Obtener descuento personalizado si es que lo tiene
						Map<String, Object> alumnoDescuento = alumnoDescuentoDAO.getAlumnoPorMatricula(academicoPago.getId_mat());
						BigDecimal descuentoPersonalizado = null;
						BigDecimal descuentoHermano = null;
						String id_descuento_personalizado = null;
						
						if (alumnoDescuento!=null){
							descuentoPersonalizado = new BigDecimal(alumnoDescuento.get("descuento").toString());
						}
						
						academicoPago.setBanco("CONTINENTAL");//debe parametrizarse por archivo
						academicoPago.setCanc("1");
						

						/*
						int fecVencimiento =  getFecVencimiento(id_anio, Integer.parseInt(academicoPago.getMes()),dia_mora );
						
						if(Integer.parseInt(fechaPago)>primerVencimiento)
							
						if (descuentoPersonalizado!=null && Integer.parseInt(fechaPago)<=fecVencimiento ){
 							descuentoHermano = new BigDecimal(0);
						}else{
							descuentoPersonalizado = new BigDecimal(0);
							
							if (Integer.parseInt(fechaPago)<=fecVencimiento){
							 	
								if(cant_hermanos>1 && Integer.parseInt(matricula.get("id_niv").toString())!=Constante.NIVEL_INICIAL ){
									descuentoHermano = desc_hermano;
								}
								else
									descuentoHermano = new BigDecimal(0);
		 

							}else{
								descuentoHermano = new BigDecimal(0);
								
							}
							

						}			
						*/
						
						BigDecimal descuentoTotal = academicoPago.getMonto().subtract(montoVentanilla);
						academicoPago.setDesc_pronto_pago(descuentoTotal);
						academicoPago.setMontoTotal(montoVentanilla);
						academicoPago.setFec_pago(fechaPagoDate);
						academicoPago.setNro_rec(nuevo_nro_recivo);
						academicoPago.setNro_pe(canal + vacio);
						map.put("procesado", "SI");
						map.put("nro_rec", nuevo_nro_recivo);
						map.put("mes",academicoPago.getMes());
						
						if (debeGrabar){
							academicoPagoDAO.saveOrUpdate(academicoPago);
							facturacionService.updateNroReciboBanco(2, nuevo_nro_recivo);
						}

					}
				}
				
				
				listBanco.add(map);
				
			}
			
		        
		if (debeGrabar)
			facturacionService.cargarMovimientosBanco(fechaProcesoExcel );

        
        
        return listBanco;
	}
	
	@RequestMapping(value = "/excel/{id_suc}")
	@ResponseBody
	public void getBancoPagados(HttpServletRequest request,HttpServletResponse response,@PathVariable Integer id_suc, 
			@RequestParam String fec_ini, 
			@RequestParam(value = "fec_fin", required=false) String fec_fin,
			@RequestParam(value = "usuario", required=false) String usuario,
			@RequestParam(value = "sucursal", required=false) String sucursal, @RequestParam String nro_serie) {

				
		try {
			ExcelXlsUtil xls = new ExcelXlsUtil();

			List<Map<String,Object>> pagos=academicoPagoDAO.listaPagosBanco(id_suc, FechaUtil.toDate(fec_ini),FechaUtil.toDate(fec_fin), nro_serie);

			response.setContentType("application/vnd.ms-excel");
			
			String  rutacARPETA =  cacheManager.getComboBox("mod_parametro", new String[]{"RUTA_PLANTILLA"} ).get(0).getAux1();

			String archivo = xls.generaExcelReporteBanco(rutacARPETA, "plantilla_reporte_banco.xls", FechaUtil.toDate(fec_ini), usuario, sucursal, pagos);
			response.setHeader("Content-Disposition","attachment;filename=Reporte_Banco" + FechaUtil.toString(FechaUtil.toDate(fec_ini), "dd-MM-yyyy") + ".xls");

			File initialFile = new File(archivo);
		    InputStream is = FileUtils.openInputStream(initialFile);
		    	
			IOUtils.copy(is, response.getOutputStream());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		


	}
	
	


	@RequestMapping(value = "/imprimir/mensualidad/{nro_rec}/{id_alu}", method = RequestMethod.POST)
	@Transactional
	public AjaxResponseBody imprimirmensualidad( @PathVariable String nro_rec, @PathVariable Integer id_alu){
		AjaxResponseBody result = new AjaxResponseBody();
		
		try{
			
			//Movimiento movimiento = movimientoDAO.getFullByNroRec(nro_rec,new String[]{Familiar.TABLA, Usuario.TABLA, Sucursal.TABLA});
			
			//logger.debug("nuevo recibo:" + nro_rec);
			//Obtenemos los datos de la empresa cuyo giro es colegio
			Row empresa = empresaDAO.datosGiroNegocio(Constante.GIRO_COLEGIO);
			
			//AcademicoPago academicoPago = academicoPagoDAO.getFullByNroRecibo(nro_rec, new String[]{Familiar.TABLA,Matricula.TABLA});
			//Buscamos si es Nota de Crédito
			//Movimiento movimiento=movimientoDAO.getByParams(new Param("nro_rec", nro_rec));
			Movimiento movimiento = movimientoDAO.getFullByNroRec(nro_rec,new String[]{Familiar.TABLA,Alumno.TABLA, Usuario.TABLA, Sucursal.TABLA, "col_persona_a", "col_persona_f"});
			NotaCredito nota_credito = notaCreditoDAO.getByParams(new Param("id_fmo_nc",movimiento.getId()));
			AcademicoPago academicoPago = new AcademicoPago();
			DocumentoReferencia documentoReferencia = new DocumentoReferencia();
			if(nota_credito!=null) {
				Movimiento mov_afec = movimientoDAO.get(nota_credito.getId_fmo());
				academicoPago = academicoPagoDAO.listFullByParams(new Param("fac_acad.nro_rec",mov_afec.getNro_rec()), null).get(0);
				
				documentoReferencia.setNro_rec(mov_afec.getNro_rec());
			} else {
				academicoPago = academicoPagoDAO.listFullByParams(new Param("fac_acad.nro_rec",nro_rec), null).get(0);
			}
			
			//obtener Periodo
			Periodo periodo= periodoDAO.getByParams(new Param("id",academicoPago.getMatricula().getId_per()));
			Sucursal sucursal= sucursalDAO.getByParams(new Param("id",periodo.getId_suc()));
			
			//Grupo Familiar
			GruFamAlumno gru_faFamAlumno= gruFamAlumnoDAO.getByParams(new Param("id_alu",id_alu));
			GruFam gruFam= gruFamDAO.get(gru_faFamAlumno.getId_gpf());
			//Row sucursal = aulaDAO.getSucursal(academicoPago.getMatricula().getId_per().g);
			//INICIO - FACTURA ELECTRONICA
			
			
			Impresion impresion = new Impresion();
			ImpresionCabecera impresionCabecera = new ImpresionCabecera();
			if(nota_credito!=null) {
				impresionCabecera.setNombreBoleta("NOTA DE CRÉDITO");
			} else {
				impresionCabecera.setNombreBoleta("BOLETA ELECTRÓNICA");
			}
			
			impresionCabecera.setNro(nro_rec);
			impresionCabecera.setDia(FechaUtil.toString(academicoPago.getFec_pago(), "dd/MM/yyyy"));
			impresionCabecera.setComercio(empresa.getString("giro_negocio"));
			impresionCabecera.setHora(FechaUtil.toString(movimiento.getFec_ins(), "hh:mm a"));
			impresionCabecera.setUsuario((movimiento.getUsuario().getTrabajador().getNom() + " " + movimiento.getUsuario().getTrabajador().getApe_pat()).toUpperCase());
			impresionCabecera.setTelefono(movimiento.getSucursal().getTel());
			impresionCabecera.setLocal(movimiento.getSucursal().getDir());
			//impresionCabecera.setHora("12:00 M");
			//impresionCabecera.setUsuario("SHUAMAN");
			//impresionCabecera.setTelefono(sucursal.getTel());
			//impresionCabecera.setLocal(sucursal.getDir());
			impresion.setCabecera(impresionCabecera);
			impresion.setDocumentoReferencia(documentoReferencia);
			
			
			ImpresionCliente impresionCliente = new ImpresionCliente(); 
			//Row datosCliente = academicoPagoDAO.obtenerDatosCliente(idPago);
			Matricula matricula = matriculaDAO.get(academicoPago.getId_mat());
			List<Persona> datosCliente = new ArrayList<>();
			if(matricula!=null) {
				/*if(matricula.getId_fam()!=null && !matricula.getId_fam().equals(0)) {
					Familiar familiar = familiarDAO.get(matricula.getId_fam());
					datosCliente = personaDAO.listFullByParams(new Param("per.id",familiar.getId_per()), null);
				} else {
					datosCliente = personaDAO.listFullByParams(new Param("per.id",matricula.getId_per_res()), null);
				}*/
				
				if(matricula.getTipo()!=null) {
					if(matricula.getTipo().equals("C")) {
						if(matricula.getId_fam_res_pag()!=null && !matricula.getId_fam_res_pag().equals(0)) {
							Familiar familiar = familiarDAO.get(matricula.getId_fam_res_pag());
							datosCliente = personaDAO.listFullByParams(new Param("per.id",familiar.getId_per()), null);
							
						} else {
							datosCliente = personaDAO.listFullByParams(new Param("per.id",matricula.getId_per_res()), null);
						}
					} else {
						if(matricula.getId_fam()!=null && !matricula.getId_fam().equals(0)) {
							Familiar familiar = familiarDAO.get(matricula.getId_fam());
							datosCliente = personaDAO.listFullByParams(new Param("per.id",familiar.getId_per()), null);
							
						} else {
							datosCliente = personaDAO.listFullByParams(new Param("per.id",matricula.getId_per_res()), null);
						}
					}
				} else {
					if(matricula.getId_fam()!=null && !matricula.getId_fam().equals(0)) {
						Familiar familiar = familiarDAO.get(matricula.getId_fam());
						datosCliente = personaDAO.listFullByParams(new Param("per.id",familiar.getId_per()), null);
						
					} else {
						datosCliente = personaDAO.listFullByParams(new Param("per.id",matricula.getId_per_res()), null);
					}
				}
			}
			
			Persona cliente = new Persona();
			if(datosCliente.size()>0) {
				cliente=datosCliente.get(0);
			}
			/*if(matricula.getId_fam()!=null && matricula.getTipo()==null) {
				Familiar familiar = familiarDAO.get(matricula.getId_fam());
				Persona datosCliente1 = personaDAO.listFullByParams(new Param("per.id",familiar.getId_per()), null).get(0);
				impresionCliente.setDireccion( datosCliente1.getDir()==null?"": datosCliente1.getDir());
				impresionCliente.setNombre(datosCliente1.getNom() + " " + datosCliente1.getApe_pat() + " " + datosCliente1.getApe_mat());
				impresionCliente.setTip_doc(datosCliente1.getTipoDocumento().getNom());
				impresionCliente.setNro_doc(datosCliente1.getNro_doc());
			}
			
			else {
				impresionCliente.setDireccion( cliente.getDir()==null?"": cliente.getDir());
				impresionCliente.setNombre(cliente.getNom() + " " + cliente.getApe_pat() + " " + cliente.getApe_pat());
				impresionCliente.setTip_doc(cliente.getTipoDocumento().getNom());
				impresionCliente.setNro_doc(cliente.getNro_doc());
			}*/
			impresionCliente.setDireccion( gruFam.getDireccion()==null?"": gruFam.getDireccion());
			if(cliente!=null) {
				impresionCliente.setNombre(cliente.getNom() + " " + cliente.getApe_pat() + " " + cliente.getApe_mat());
				impresionCliente.setTip_doc(cliente.getTipoDocumento().getNom());
				impresionCliente.setNro_doc(cliente.getNro_doc());
			} else {
				impresionCliente.setNombre(movimiento.getPersona_fam().getApe_pat() + " " + movimiento.getPersona_fam().getApe_mat() + ", " + movimiento.getPersona_fam().getNom());
				impresionCliente.setTip_doc(movimiento.getPersona_fam().getTipoDocumento().getNom());
				impresionCliente.setNro_doc(movimiento.getPersona_fam().getNro_doc());
			}
				
			
			impresion.setCliente(impresionCliente);	
				
			//FIN - FACTURA ELECTRONICA
			
			//List<MovimientoDetalle> items = movimientoDetalleDAO.listByParams(new Param("id_fmo", movimiento.getId()), new String[]{"id"});
			
			//PAGOS DE MENSUALIDAD --> DEBE SER REEMPLAZADO POR PAGO 
			
			Param param = new Param();
			param.put("nro_rec", nro_rec);
			//List<AcademicoPago> mensalidades = academicoPagoDAO.listByParams(param, new String[]{"mens"});
			
				//INICIO - FACTURA ELECTRONICA
				Row datosAlumno = matriculaDAO.getDatosAlumno(academicoPago.getId_mat());
				BigDecimal descuentoHermano = (academicoPago.getDesc_hermano()==null)?new BigDecimal(0):academicoPago.getDesc_hermano(); //TODO.. FALTA GRABAR DESCUENTO DE BANCO CUANDO EL PAGO ES BANCARIO
				BigDecimal descuentoProntoPago= (academicoPago.getDesc_pronto_pago()==null)?new BigDecimal(0):academicoPago.getDesc_pronto_pago(); 
				BigDecimal descuentoPersonalizado= (academicoPago.getDesc_personalizado()==null)?new BigDecimal(0):academicoPago.getDesc_personalizado();  
				BigDecimal descuentoBeca= (academicoPago.getDesc_beca()==null)?new BigDecimal(0):academicoPago.getDesc_beca();  
				
				BigDecimal descuentoItem = new BigDecimal(0);
				ImpresionItem impresionItem = new ImpresionItem();
				impresionItem.setNro(1);
				String descripcion="";
				
				if(matricula.getTipo()!=null) {
					if(matricula.getTipo().equals("A") || matricula.getTipo().equals("V")) {
						
						if(academicoPago.getNro_cuota().equals(1)) {
							List<AlumnoDescuento> descuentos= alumnoDescuentoDAO.listByParams(new Param("id_mat",academicoPago.getId_mat()),new String[] { "id" });
							for (AlumnoDescuento alumnoDescuento : descuentos) {
								//Obtener datos del descuento
								DescuentoConf descuentoConf = descuentoConfDAO.getByParams(new Param("id",alumnoDescuento.getId_fdes()));
								//Obtengo el nombre del descuento
								Descuento descuento= descuentoDAO.get(descuentoConf.getId_des());
								descripcion = "Descuento por "+descuento.getNom();
								impresionItem.getDescuentos()
										.add(new ImpresionDcto(descripcion.toUpperCase(), new BigDecimal(descuentoConf.getMonto())));
								descuentoItem = descuentoItem.add(new BigDecimal(descuentoConf.getMonto()));
							}
						}
							
					} else {
						descuentoItem = descuentoHermano.add(descuentoProntoPago).add(descuentoPersonalizado);
						if (descuentoHermano.compareTo(new BigDecimal(0))>0){
							descripcion = "Descuento por hermano";
							//descripcion += "\nMensualidad de " + Constante.MES[academicoPago.getMens()-1];
							//descripcion += "\n" + datosAlumno.getString("alumno")  +"-" + datosAlumno.getString("grado") + "-" + datosAlumno.get("secc") + " " + datosAlumno.getString("nivel").substring(0,3);
							impresionItem.getDescuentos().add(new ImpresionDcto(descripcion.toUpperCase(), descuentoHermano ));
						}
						if (descuentoProntoPago.compareTo(new BigDecimal(0))>0){
							descripcion = "Descuento pronto pago ";
							//descripcion += "\nMensualidad de " + Constante.MES[academicoPago.getMens()-1] ;
							//descripcion += "\n" + datosAlumno.getString("alumno")  +"-" + datosAlumno.getString("grado") + "-" + datosAlumno.get("secc") + " " + datosAlumno.getString("nivel").substring(0,3);
							impresionItem.getDescuentos().add(new ImpresionDcto(descripcion.toUpperCase(),descuentoProntoPago ));
						}
						if (descuentoPersonalizado.compareTo(new BigDecimal(0))>0){
								descripcion = "Descuento personalizado ";
								//descripcion += "\nMensualidad de " + Constante.MES[academicoPago.getMens()-1];
								//descripcion += "\n" + datosAlumno.getString("alumno")  +"-" + datosAlumno.getString("grado") + "-" + datosAlumno.get("secc") + " " + datosAlumno.getString("nivel").substring(0,3);
								impresionItem.getDescuentos().add(new ImpresionDcto(descripcion.toUpperCase(),descuentoPersonalizado));
							
						}
					}
				} else {
					descuentoItem = descuentoHermano.add(descuentoProntoPago).add(descuentoPersonalizado);
					if (descuentoHermano.compareTo(new BigDecimal(0))>0){
						descripcion = "Descuento por hermano";
						descripcion += "\nMensualidad de " + Constante.MES[academicoPago.getMens()-1];
						descripcion += "\n" + datosAlumno.getString("alumno")  +"-" + datosAlumno.getString("grado") + "-" + datosAlumno.get("secc") + " " + datosAlumno.getString("nivel").substring(0,3);
						impresionItem.getDescuentos().add(new ImpresionDcto(descripcion.toUpperCase(), descuentoHermano ));
					}
					if (descuentoProntoPago.compareTo(new BigDecimal(0))>0){
						descripcion = "Descuento pronto pago ";
						descripcion += "\nMensualidad de " + Constante.MES[academicoPago.getMens()-1] ;
						descripcion += "\n" + datosAlumno.getString("alumno")  +"-" + datosAlumno.getString("grado") + "-" + datosAlumno.get("secc") + " " + datosAlumno.getString("nivel").substring(0,3);
						impresionItem.getDescuentos().add(new ImpresionDcto(descripcion.toUpperCase(),descuentoProntoPago ));
					}
					if (descuentoPersonalizado.compareTo(new BigDecimal(0))>0){
							descripcion = "Descuento personalizado ";
							descripcion += "\nMensualidad de " + Constante.MES[academicoPago.getMens()-1];
							descripcion += "\n" + datosAlumno.getString("alumno")  +"-" + datosAlumno.getString("grado") + "-" + datosAlumno.get("secc") + " " + datosAlumno.getString("nivel").substring(0,3);
							impresionItem.getDescuentos().add(new ImpresionDcto(descripcion.toUpperCase(),descuentoPersonalizado));
						
					}
				}
				
				
				
				if (descuentoBeca.compareTo(new BigDecimal(0))>0){
					//Hallamos la beca
					Beca beca=becaDAO.get(academicoPago.getId_bec());
					descripcion = "Descuento por "+beca.getNom();
					impresionItem.getDescuentos().add(new ImpresionDcto(descripcion.toUpperCase(),descuentoBeca));
					descuentoItem = descuentoItem.add(descuentoBeca);
				}
				
				//String descripcion="";
				if(matricula.getTipo()!=null) {
					if(matricula.getTipo().equals("A") || matricula.getTipo().equals("V")) {
						if(nota_credito!=null) {
							descripcion = "Devolución - Cuota Nro. "+academicoPago.getNro_cuota()+" - "+datosAlumno.getString("ciclo") + "\n"
									+ datosAlumno.getString("alumno") + "-" + datosAlumno.getString("grado") + "-"
									+ datosAlumno.get("secc") + " " + datosAlumno.getString("nivel").substring(0, 3);
						} else {
							descripcion = "Cuota Nro. "+academicoPago.getNro_cuota()+" - "+datosAlumno.getString("ciclo") + "\n"
									+ datosAlumno.getString("alumno") + "-" + datosAlumno.getString("grado") + "-"
									+ datosAlumno.get("secc") + " " + datosAlumno.getString("nivel").substring(0, 3);
						}
						
					} else {
						if(academicoPago.getMens().equals(0)) {
							if(nota_credito!=null) {
								descripcion = "DEVOLUCIÓN POR MATRÍCULA \n" + datosAlumno.getString("alumno")+"-" + datosAlumno.getString("grado") + "-"  + datosAlumno.getString("nivel").substring(0,3);
							} else {
								descripcion = "MATRICULA \n" + datosAlumno.getString("alumno")+"-" + datosAlumno.getString("grado") + "-"  + datosAlumno.getString("nivel").substring(0,3);
							}
							
						} else {
							if(nota_credito!=null) {
								descripcion = " Devolución - Mensualidad de " + Constante.MES[academicoPago.getMens()-1]  + "\n" + datosAlumno.getString("alumno")+"-" + datosAlumno.getString("grado") + "-" + datosAlumno.get("secc") + " " + datosAlumno.getString("nivel").substring(0,3);
							} else {
								descripcion = "Mensualidad de " + Constante.MES[academicoPago.getMens()-1]  + "\n" + datosAlumno.getString("alumno")+"-" + datosAlumno.getString("grado") + "-" + datosAlumno.get("secc") + " " + datosAlumno.getString("nivel").substring(0,3);
							}
							
						}
						
					}
					
				} else {
					descripcion = "Mensualidad de " + Constante.MES[academicoPago.getMens()-1]  + "\n" + datosAlumno.getString("alumno")+"-" + datosAlumno.getString("grado") + "-" + datosAlumno.get("secc") + " " + datosAlumno.getString("nivel").substring(0,3);
				}
				
				
				impresionItem.setDescripcion(descripcion.toUpperCase() );
				impresionItem.setDescuento(descuentoItem);
				impresionItem.setPrecioUnit(academicoPago.getMonto());
				impresionItem.setCantidad(1);
				impresionItem.setPrecio(academicoPago.getMonto());
				impresionItem.setTipoOperacion("OPE_INA");
				impresion.getItems().add(impresionItem);
				//FIN - FACTURA ELECTRONICA
				


			

			
			result.setResult(impresion);
			
		} catch (Exception e) {
			result.setException(e);
		}
		
		
		return result;

	}

	

	/**
	 * Obtiene los datos de la cabecera del archivo segun el banco
	 * @param id_banco
	 * @return
	 */
	private String obtenerCabecera(int id_banco){
		String lineas = "";
		String linea1 = "";
		if (id_banco == EnumBanco.CONTINENTAL.getValue()){
			linea1 = "01";//indciador cabecera
			linea1 += "20531084587"; //ruc
			linea1 += "000"; //nro de la clase
			linea1 += "PEN"; //MONEDA
			linea1 += FechaUtil.toString(new Date(), "YYYYMMdd"); //FECHA DE FACTURACION
			linea1 += "000";//VERSION
			linea1 += StringUtil.espacioBlanco(7);//7 ESPACIOS EN BLANCO
			linea1 += "T";//T ( ACTUALIZACION TOTAL)
			linea1 += StringUtil.espacioBlanco(322);//322 ESPACIOS EN BLANCO
		} 
		
		lineas += linea1;
		return lineas;
	}
	
}
