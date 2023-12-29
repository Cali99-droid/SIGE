package com.sige.spring.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sige.common.enums.EnumConceptoPago;
import com.sige.common.enums.EnumTipoMovimiento;
import com.sige.invoice.bean.Impresion;
import com.sige.mat.dao.AcademicoPagoDAO;
import com.sige.mat.dao.AlumnoDAO;
import com.sige.mat.dao.AlumnoDescuentoDAO;
import com.sige.mat.dao.AnioDAO;
import com.sige.mat.dao.AulaDAO;
import com.sige.mat.dao.CicloDAO;
import com.sige.mat.dao.ConfCuotaDAO;
import com.sige.mat.dao.ConfFechasDAO;
import com.sige.mat.dao.ConfMensualidadDAO;
import com.sige.mat.dao.CorrelativoDAO;
import com.sige.mat.dao.CronogramaDAO;
import com.sige.mat.dao.DescHnoDAO;
import com.sige.mat.dao.FamiliarDAO;
import com.sige.mat.dao.GradDAO;
import com.sige.mat.dao.GruFamAlumnoDAO;
import com.sige.mat.dao.MatriculaDAO;
import com.sige.mat.dao.MovimientoDAO;
import com.sige.mat.dao.MovimientoDescuentoDAO;
import com.sige.mat.dao.MovimientoDetalleDAO;
import com.sige.mat.dao.NivelDAO;
import com.sige.mat.dao.PeriodoDAO;
import com.sige.mat.dao.PersonaDAO;
import com.sige.mat.dao.ReservaDAO;
import com.sige.mat.dao.SeccionSugeridaDAO;
import com.sige.mat.dao.SituacionMatDAO;
import com.sige.mat.dao.SolicitudDAO;
import com.sige.mat.dao.SucursalDAO;
import com.sige.rest.request.PagoMatriculaReq;
import com.tesla.colegio.model.AcademicoPago;
import com.tesla.colegio.model.Alumno;
import com.tesla.colegio.model.Anio;
import com.tesla.colegio.model.Aula;
import com.tesla.colegio.model.Ciclo;
import com.tesla.colegio.model.ConfCuota;
import com.tesla.colegio.model.ConfMensualidad;
import com.tesla.colegio.model.Correlativo;
import com.tesla.colegio.model.Grad;
import com.tesla.colegio.model.GruFamAlumno;
import com.tesla.colegio.model.Matricula;
import com.tesla.colegio.model.Movimiento;
import com.tesla.colegio.model.MovimientoDescuento;
import com.tesla.colegio.model.MovimientoDetalle;
import com.tesla.colegio.model.Nivel;
import com.tesla.colegio.model.Periodo;
import com.tesla.colegio.model.Persona;
import com.tesla.colegio.model.ReservaCuota;
import com.tesla.colegio.model.SeccionSugerida;
import com.tesla.colegio.model.Solicitud;
import com.tesla.colegio.model.Sucursal;
import com.tesla.colegio.model.bean.MatriculaPagos;
import com.tesla.colegio.util.Constante;
import com.tesla.frmk.common.exceptions.ServiceException;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;

@Service
public class MatriculaService {

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
	ConfCuotaDAO confCuotaDAO;

	@Autowired
	PeriodoDAO periodoDAO;

	@Autowired
	FacturacionService facturacionService;

	@Autowired
	SucursalDAO sucursalDAO;

	@Autowired
	FamiliarDAO familiarDAO;

	@Autowired
	AlumnoDAO alumnoDAO;

	@Autowired
	AulaDAO aulaDAO;

	@Autowired
	MovimientoDAO movimientoDAO;

	@Autowired
	ReservaDAO reservaDAO;

	@Autowired
	MovimientoDetalleDAO movimientoDetalleDAO;

	@Autowired
	MovimientoDescuentoDAO movimientoDescuentoDAO;
	
	@Autowired
	GruFamAlumnoDAO gruFamAlumnoDAO;
	
	@Autowired
	CorrelativoDAO correlativoDAO;
	
	@Autowired
	SeccionSugeridaDAO seccionSugeridaDAO;
	
	@Autowired
	PersonaDAO personaDAO;
	
	@Autowired
	CicloDAO cicloDAO;
	
	@Autowired
	NivelDAO nivelDAO;
	
	@Autowired
	GradDAO gradDAO;
	
	@Autowired
	CronogramaDAO cronogramaDAO;
	
	@Autowired
	ConfFechasDAO confFechasDAO;
	
	@Autowired
	VacanteService vacanteService;

	/**
	 * utlizado cuando se matriculaba y pagaba a la vez
	 * @param matricula
	 * @param id_suc
	 * @return
	 * @throws Exception
	 */
/*	@Transactional
	public Impresion matricularYPagar(Matricula matricula, Integer id_suc) throws Exception {

		Impresion impresion = null;

		// grabamos matricula
		matricula.setEst("A");

		if (matricula.getId() == null) {

			matricula.setId_au_asi(matricula.getId_au());
			Aula aula = aulaDAO.getFull(matricula.getId_au(), new String[] { Nivel.TABLA, Grad.TABLA,Periodo.TABLA });
			matricula.setId_per(aula.getId_per());
			
			//buscar si contrato ya existe
			Param param = new Param();
			param.put("num_cont", matricula.getNum_cont());
			
			
			List<Matricula> matriculasContrato = matriculaDAO.listByParams(param, new String[]{"id desc"});
			
			if (matriculasContrato.size()==0)
				matricula.setId_suc_con(id_suc);
			else
				matricula.setId_suc_con(matriculasContrato.get(0).getId_suc_con());
			
			int id = matriculaDAO.saveOrUpdate(matricula);
 
			// cuando se inserta las matriculas
			matricula.setId(id);
			
			Alumno alumno = alumnoDAO.get(matricula.getId_alu());

			// pago que le corresponde para la matricula
			String nro_rec = facturacionService.getNroRecibo(id_suc);
			facturacionService.updateNroRecibo(id_suc, nro_rec);

			BigDecimal mov_monto= BigDecimal.ZERO;
			BigDecimal mov_montoTotal = BigDecimal.ZERO;
			BigDecimal mov_descuento= BigDecimal.ZERO;
			
			ConfCuota confCuota = confCuotaDAO.getByParams(new Param("id_per", matricula.getId_per()));

			//revisar si tiene un pago por reserva
			//Aula aula = aulaDAO.getFull(matricula.getId_au(), new String[]{Periodo.TABLA});
			
			
			ReservaCuota reservaCuota = reservaDAO.getMontoReserva(aula.getPeriodo().getId_anio(), matricula.getId_alu());
			BigDecimal montoMatricula = BigDecimal.ZERO;// confCuota.getMatricula();

			Periodo periodo = periodoDAO.get(matricula.getId_per());
			Anio anio = anioDAO.get(periodo.getId_anio());
			
			Movimiento movimiento = new Movimiento();
			movimiento.setDescuento(new BigDecimal(0));
			movimiento.setEst("A");
			movimiento.setFec(matricula.getFecha());
			movimiento.setId_mat(matricula.getId());

			movimiento.setId_fam(matricula.getId_fam());
			movimiento.setId_suc(id_suc); 
			movimiento.setNro_rec(nro_rec);
			movimiento.setTipo(EnumTipoMovimiento.INGRESO.getValue());
			movimiento.setId_fpa(1);// EFECTIVO
 
			movimiento.setObs("MATRICULA " + anio.getNom());

			List<MatriculaPagos> listPagar = obtenerPagosProgramados(matricula.getId_alu(), matricula.getId_au(), aula.getPeriodo().getId_anio());
			for (MatriculaPagos matriculaPagos : listPagar) {
				 
				String tipo = matriculaPagos.getTip();
				
				if (tipo.equals("MAT") || tipo.equals("ING")){

					mov_monto =  mov_monto.add(matriculaPagos.getMonto());
					String descripcionItem = null;
					Integer id_fco = null;//CONCEPTO DE PAGO
					if (tipo.equals("MAT")) {
						montoMatricula = montoMatricula.add(matriculaPagos.getMonto());

						descripcionItem = EnumConceptoPago.MATRICULA.getDescripcion();
						id_fco = EnumConceptoPago.MATRICULA.getValue();
					}
					if (tipo.equals("ING")) {
						descripcionItem = EnumConceptoPago.CUOTA_DE_INGRESO.getDescripcion();
						id_fco = EnumConceptoPago.CUOTA_DE_INGRESO.getValue();
					}
					descripcionItem = descripcionItem + " " + anio.getNom() + ", ALUMNO:" + alumno.getApe_pat() + " "
							+ alumno.getApe_mat() + ", " + alumno.getNom() + " NIVEL:" + aula.getGrad().getNivel().getNom()
							+ " GRADO:" + aula.getGrad().getNom() + " " + aula.getSecc() + " CONTRATO:" + matricula.getNum_cont();
					 
					MovimientoDetalle movimientoDetalle = new MovimientoDetalle();
					movimientoDetalle.setDescuento(new BigDecimal(0));
					movimientoDetalle.setEst("A");
					movimientoDetalle.setId_fco(id_fco);
 					movimientoDetalle.setMonto(confCuota.getMatricula());
					movimientoDetalle.setMonto_total(confCuota.getMatricula());
					movimientoDetalle.setObs(descripcionItem);
					
					if (movimiento.getMovimientoDetalles()==null)
						movimiento.setMovimientoDetalle(new ArrayList<MovimientoDetalle>());
					movimiento.getMovimientoDetalles().add(movimientoDetalle);
					
				}else if (tipo.equals("RES")){
					montoMatricula = montoMatricula.add(matriculaPagos.getMonto());

					//esto es un descuento a la matricula
				
					String descripcion = "DESCUENTO POR RESERVA";
					descripcion += "\nALUMNO " + alumno.getApe_pat() + " " + alumno.getNom() + "-" + aula.getGrad().getNom() + "-"
							+  aula.getSecc()  + " " +  aula.getGrad().getNivel().getNom();
					
					MovimientoDetalle movimientoDetalleMAt =  null;
					for (MovimientoDetalle movimientoDetalle1 :movimiento.getMovimientoDetalles()) {
						if (movimientoDetalle1.getId_fco().intValue() == EnumConceptoPago.MATRICULA.getValue() )
							movimientoDetalleMAt = movimientoDetalle1;
					}

					 MovimientoDescuento movimientoDescuento = new MovimientoDescuento();
					 
					 movimientoDescuento.setDes(descripcion);
					 movimientoDescuento.setDescuento(matriculaPagos.getMonto().multiply(new BigDecimal(-1)));
					 movimientoDescuento.setEst("A");
					// mov_descuento = mov_descuento.add(augend)
					mov_descuento = mov_descuento.add(movimientoDescuento.getDescuento());

					 
					 movimientoDetalleMAt.getDescuentos().add(movimientoDescuento);
					 movimientoDetalleMAt.setDescuento(mov_descuento);
					 movimientoDetalleMAt.setMonto_total(movimientoDetalleMAt.getMonto().subtract(mov_descuento));
				}
				
					 
			}
			mov_montoTotal= mov_monto.subtract(mov_descuento);
			movimiento.setMonto(mov_monto);
			movimiento.setDescuento(mov_descuento);
			movimiento.setMonto_total(mov_montoTotal);
			
			
			
			AcademicoPago academicoPagoPrincipal = new AcademicoPago();
			academicoPagoPrincipal.setCanc("1");// Se paga al matricular
			academicoPagoPrincipal.setEst("A");
 			academicoPagoPrincipal.setId_mat(matricula.getId());
			academicoPagoPrincipal.setMonto(montoMatricula);
			academicoPagoPrincipal.setMontoTotal(montoMatricula);
			academicoPagoPrincipal.setTip(com.tesla.colegio.util.Constante.PAGO_MATRICULA);
			academicoPagoPrincipal.setNro_rec(nro_rec);
			academicoPagoPrincipal.setFec_pago(new Date());


			academicoPagoDAO.saveOrUpdate(academicoPagoPrincipal);

			// pago que le corresponde para la cuota de ingreso (Solo cuando es
			// nuevo )
			Matricula matriculaAnterior = matriculaDAO.getMatriculaAnterior(matricula.getId_alu(),periodo.getId_anio() - 1);

			if (matriculaAnterior == null || matriculaAnterior.equals(com.tesla.colegio.util.Constante.CLIENTE_NUEVO)) {
				AcademicoPago academicoPago = new AcademicoPago();
				academicoPago.setCanc("1");// Paga al matricular
				academicoPago.setEst("A");
				academicoPago.setId_mat(matricula.getId());
				academicoPago.setMonto(confCuota.getCuota());
				academicoPago.setTip(com.tesla.colegio.util.Constante.PAGO_CUOTA_INGRESO);
				academicoPago.setFec_pago(new Date());
				academicoPago.setNro_rec(nro_rec);
				academicoPago.setMontoTotal(confCuota.getCuota());

				academicoPagoDAO.saveOrUpdate(academicoPago);
			}

			Integer[] meses = new Integer[] { 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };

			 param = new Param();
			param.put("id_per", matricula.getId_per());

			ConfMensualidad confMensualidad = confMensualidadDAO.getByParams(param);
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

			String format = formatter.format(new Date());
			int fecActual = Integer.parseInt(format);
			
			for (int i = 0; i < meses.length; i++) {
				Param param1 = new Param();
				param1.put("id_mat", matricula.getId());
				param1.put("mens", meses[i]);
				AcademicoPago pago_mes = academicoPagoDAO.getByParams(param1);
				if(pago_mes==null){
					Calendar cal = Calendar.getInstance();
					int anio_actual = cal.get(Calendar.YEAR);
					int fecVencimiento = getFecVencimiento(anio_actual, meses[i], matricula.getId_per());
					Integer dia_mora=confMensualidadDAO.getByParams(new Param("id_per", matricula.getId_per())).getDia_mora();
					
					AcademicoPago academicoPago = new AcademicoPago();
					
					academicoPago.setEst("A");
					academicoPago.setId_mat(matricula.getId());
					if (fecActual < fecVencimiento) {
						//int dias_cla=dia_mora-(new Date().getDate());
						BigDecimal monto_mens=new BigDecimal(0);
						//if(dias_cla>0)
						//monto_mens=(confMensualidad.getMonto().divide(new BigDecimal(30),2)).multiply(new BigDecimal(dias_cla));
						//else
						monto_mens=confMensualidad.getMonto();	
						academicoPago.setMonto(monto_mens);
						academicoPago.setCanc("0");// Por defecto pago pendiente
					} else if(fecActual==fecVencimiento){
						academicoPago.setCanc("1");// Por defecto pagado
						academicoPago.setMonto(new BigDecimal(0));
						academicoPago.setMontoTotal(new BigDecimal(0));
					} else if(fecActual > fecVencimiento){
						int dia_act=(new Date().getDate());
						int dias_cla=dia_mora-dia_act;
						academicoPago.setMonto(new BigDecimal(0));
						academicoPago.setCanc("1");//Le pongo cancelado
						academicoPago.setMontoTotal(new BigDecimal(0));
						Integer mes_sig=meses[i]+1;
						AcademicoPago pago_mes_siguiente = new AcademicoPago();
						pago_mes_siguiente.setEst("A");
						pago_mes_siguiente.setId_mat(matricula.getId());
						BigDecimal monto_mens=(confMensualidad.getMonto().divide(new BigDecimal(30),2)).multiply(new BigDecimal(dias_cla));
						pago_mes_siguiente.setMonto(monto_mens);
						pago_mes_siguiente.setCanc("0");// Por defecto pago pendiente
						pago_mes_siguiente.setMens(mes_sig);
						pago_mes_siguiente.setDesc_hermano(new BigDecimal(0));
						pago_mes_siguiente.setDesc_pronto_pago(new BigDecimal(0));
						pago_mes_siguiente.setDesc_pago_adelantado(new BigDecimal(0));
						pago_mes_siguiente.setDesc_personalizado(new BigDecimal(0));
						pago_mes_siguiente.setTip(com.tesla.colegio.util.Constante.PAGO_MENSUAL);
						
						academicoPagoDAO.saveOrUpdate(pago_mes_siguiente);
					} else{
						academicoPago.setMonto(confMensualidad.getMonto());
						academicoPago.setCanc("1");
					}
					//academicoPago.setMonto(confMensualidad.getMonto());
					academicoPago.setMens(meses[i]);
					academicoPago.setTip(com.tesla.colegio.util.Constante.PAGO_MENSUAL);
					academicoPagoDAO.saveOrUpdate(academicoPago);
				}
			}

			// ES UN PAGO DE UN ALUMNO MATRICULADO

			Integer id_fmo = movimientoDAO.saveOrUpdate(movimiento);
			for (MovimientoDetalle movimientoDetalle : movimiento.getMovimientoDetalles()) {
				movimientoDetalle.setId_fmo(id_fmo);
				Integer id_fmd = movimientoDetalleDAO.saveOrUpdate(movimientoDetalle);

				if (movimientoDetalle.getDescuentos().size()>0){
			
					for (MovimientoDescuento descuento : movimientoDetalle.getDescuentos() ) {
						descuento.setId_fmd(id_fmd);
						movimientoDescuentoDAO.saveOrUpdate(descuento);
						
					}
				}
				
			}
			 impresion = facturacionService.getImpresion(nro_rec);

		}

		
		return impresion;

	}*/
	
	@Transactional
	public Map<String, Object> matricularYPagarV2(Matricula matricula, Integer id_suc, Integer id_anio, Integer id_perfil, Integer id_bco_pag) throws ServiceException {

		Impresion impresion = null;
		
		Map<String, Object> map = new HashMap<String, Object>();

		// grabamos matricula
		matricula.setEst("A");
		Integer gpfa = null;
		
		Anio anio = anioDAO.get(id_anio);
		
		//Verificar el cronograma 
		//ALUMNOS ANTIGUOS - CON CRONOGRAMA
		boolean antiguo_con_cronograma = cronogramaDAO.alumnosAntiguosTienenVigente(id_anio);

		//ALUMNOS ANTIGUOS - SIN CRONOGRAMA
		boolean antiguo_sin_cronograma = confFechasDAO.cronogramaVigente(id_anio, "AS");

				
		//ALUMNOS NUEVOS SIN CRONOGRAMA
		boolean nuevos_sin_cronograma = confFechasDAO.cronogramaVigente(id_anio, "NS");
		
		//ALUMNOS NUEVOS CON CRONOGRAMA 
		boolean nuevos_cronograma = confFechasDAO.cronogramaVigente(id_anio, "NC");
		
		if(antiguo_con_cronograma) {
			if (matricula.getId() == null) {
				//Busco su matricula anterior
				List<Row> matricula_ant = matriculaDAO.obtenerMatriculaColegioxAlumnoAnio(matricula.getId_alu(), id_anio-1);
				//Funcionaria para los antiguos
				if(matricula_ant.size()>0) {
					Periodo periodo_ant=periodoDAO.get(matricula_ant.get(0).getInteger("id_per"));
					//Busco el grado y periodo al q se va a matricular
					SeccionSugerida seccionSugerida = seccionSugeridaDAO.getByParams(new Param("id_mat",matricula_ant.get(0).getInteger("id_mat")));
					if(seccionSugerida!=null) {
						Aula aula = aulaDAO.get(seccionSugerida.getId_au_nue());
						//Obtengo el ciclo
						Ciclo ciclo = cicloDAO.get(aula.getId_cic());
						Periodo periodo = periodoDAO.get(ciclo.getId_per());
						matricula.setId_per(periodo.getId());
						matricula.setId_cic(ciclo.getId());
						matricula.setId_gra(aula.getId_grad());
						matricula.setId_niv(periodo.getId_niv());
						matricula.setId_au(aula.getId());
						matricula.setId_au_asi(aula.getId());
					} else {
						//Buscamos el periodo siguiente
						Param param = new Param();
						param.put("id_suc", periodo_ant.getId_suc());
						Integer id_niv=null;
						if(matricula_ant.get(0).getInteger("id_gra").equals(3))
							id_niv= 2;
						else if(matricula_ant.get(0).getInteger("id_gra").equals(9))
							id_niv=3;
						else 
							id_niv= matricula_ant.get(0).getInteger("id_niv");
						
						param.put("id_niv", id_niv);
						param.put("id_anio", id_anio);
						param.put("id_tpe",1);
						Periodo periodo_nuevo = periodoDAO.getByParams(param);
						Ciclo ciclo_nuevo=cicloDAO.getByParams(new Param("id_per",periodo_nuevo.getId()));
						matricula.setId_per(periodo_nuevo.getId());
						matricula.setId_cic(ciclo_nuevo.getId());
						matricula.setId_gra(matricula_ant.get(0).getInteger("id_gra")+1);
						matricula.setId_niv(id_niv);
						matricula.setId_au_asi(null);
						matricula.setId_au(null);
					}
				}
				
				//matricula.setId_au_asi(matricula.getId_au());
				//Aula aula = aulaDAO.getFull(matricula.getId_au(), new String[] { Nivel.TABLA, Grad.TABLA,Periodo.TABLA });
				//matricula.setId_per(aula.getId_per());
			//Otorgamos el numero de Contrato Correcto
				
				Map<String, Object> contratoActual = matriculaDAO.getContrato(matricula.getId_fam(), id_anio);
				String contrato=null;
				if (contratoActual == null) {
					
					//No comprendo xq se consideraria periodo??
					Map<String, Object> map2 = matriculaDAO.getTotalContratos(id_anio);
					Double total = (Double)map2.get("total");
					
					int correlativo = total.intValue() + 1;
					
					/*if (correlativo==1){
						Sucursal sucursal = sucursalDAO.get(id_suc);
						map.put("cod", sucursal.getCod());
					}*/
					
					//String contrato = anio.getNom() + "-" +  map.get("cod") + "-" + String.format("%05d", correlativo);
					contrato = anio.getNom() + "-" + String.format("%05d", correlativo);

				} else {
					contrato = contratoActual.get("num_cont").toString();
				}
				matricula.setNum_cont(contrato);
				
				//buscar si contrato ya existe
				Param param = new Param();
				param.put("num_cont", matricula.getNum_cont());
				
				
				List<Matricula> matriculasContrato = matriculaDAO.listByParams(param, new String[]{"id desc"});
				//Si existe matricula del mismo local 
				Integer correlativo_adenda=null;
				String num_adenda=null;
				Boolean ins_adenda =false;
				List<Row> matriculasLocalApod= matriculaDAO.matriculadosxLocalyApoderado(id_suc, matricula.getId_fam(), id_anio);
				
				
				/*if(matriculasContrato.size()==0){ //si no existe ninguna matricula no inserto adenda
					//matricula.setId_suc_con(id_suc);
				
				} else*/
				if(matricula.getNum_adenda()==null){
					/*if (matriculasContrato.size()>0 && matriculasLocalApod.size()==0){ // si ya existe matricula pero de otros locales inserto adenda
						//inserto adenda
						//Ontengo el correlativo de adenda
						//correlativo_adenda=correlativoDAO.obtenerCorrelativoAdenda(id_suc, id_anio);
						correlativo_adenda=correlativoDAO.obtenerCorrelativoAdenda(2, id_anio);
						num_adenda=anio.getNom() + "-001-"+String.format("%05d", correlativo_adenda);
						ins_adenda=true;
						/*if(id_suc==2){
							num_adenda=anio.getNom() + "-001-"+String.format("%05d", correlativo_adenda);
							ins_adenda=true;
						} else if(id_suc==3){
							num_adenda=anio.getNom() + "-002-"+String.format("%05d", correlativo_adenda);
							ins_adenda=true;
						} else if(id_suc==4){
							num_adenda=anio.getNom() + "-003-"+String.format("%05d", correlativo_adenda);
							ins_adenda=true;
						}*/
				/*	} else if(matriculasContrato.size()>0 && matriculasLocalApod.size()>0){
						//recorro las matriculas por apoderado
						//Integer cant_adendas=0;
						for (Row row : matriculasLocalApod) {
							if(row.getString("num_adenda")!=null){
								num_adenda=row.getString("num_adenda");
								break;
							}
						}
					}
					matricula.setNum_adenda(num_adenda);*/
				}
				
				matricula.setId_suc_con(2);
				/*if (matriculasContrato.size()==0){
					matricula.setId_suc_con(id_suc);
				} else{
					matricula.setId_suc_con(matriculasContrato.get(0).getId_suc_con());
				}	*/
				
				Integer tip_mat=null;
				if(id_perfil.equals(Constante.PERFIL_FAMILIAR))
					tip_mat=2;
				else if(id_perfil.equals(Constante.PERFIL_TRABAJADOR))
					tip_mat=1;
				matricula.setTip_mat(tip_mat);
				matricula.setTipo("C");
				matricula.setMat_val("0");
				
				int id = matriculaDAO.saveOrUpdate(matricula);
				//aumento el codigo de adenda si existe
				/*if(ins_adenda==true){
					Integer nuevo_num=correlativo_adenda+1;
					correlativoDAO.updateCorrelativo(nuevo_num, id_suc, id_anio);
					
				}*/
				//aumento el codigo de adenda si existe
				if(ins_adenda==true){
					Integer nuevo_num=correlativo_adenda+1;
					correlativoDAO.updateCorrelativo(nuevo_num, 2, id_anio);
					
				}
				// cuando se inserta las matriculas
				matricula.setId(id);
				
				Alumno alumno = alumnoDAO.get(matricula.getId_alu());
				GruFamAlumno gruFamAlumno = gruFamAlumnoDAO.getByParams(new Param("id_alu",alumno.getId()));
				gpfa = gruFamAlumno.getId_gpf();
				// pago que le corresponde para la matricula
				/*String nro_rec = facturacionService.getNroRecibo(id_suc);
				facturacionService.updateNroRecibo(id_suc, nro_rec);*/

				BigDecimal mov_monto= BigDecimal.ZERO;
				BigDecimal mov_montoTotal = BigDecimal.ZERO;
				BigDecimal mov_descuento= BigDecimal.ZERO;
				
				Matricula matricula_nueva = matriculaDAO.get(id);
				ConfCuota confCuota = confCuotaDAO.getByParams(new Param("id_per", matricula_nueva.getId_per()));
				Periodo periodo_nuevo= periodoDAO.get(matricula_nueva.getId_per());
				ReservaCuota reservaCuota = reservaDAO.getMontoReserva(periodo_nuevo.getId_anio(), matricula.getId_alu());
				BigDecimal montoMatricula = BigDecimal.ZERO;// confCuota.getMatricula();

				Periodo periodo = periodoDAO.get(matricula.getId_per());

				List<MatriculaPagos> listPagar = obtenerPagosProgramadosv2(matricula_nueva.getId(),matricula_nueva.getId_alu(), periodo_nuevo.getId(), periodo_nuevo.getId_anio(),null);
				for (MatriculaPagos matriculaPagos : listPagar) {
					 
					String tipo = matriculaPagos.getTip();
					
					if (tipo.equals("MAT") || tipo.equals("ING")){

						mov_monto =  mov_monto.add(matriculaPagos.getMonto());
						//String descripcionItem = null;
						Integer id_fco = null;//CONCEPTO DE PAGO
						if (tipo.equals("MAT")) {
							montoMatricula = montoMatricula.add(matriculaPagos.getMonto());

							//descripcionItem = EnumConceptoPago.MATRICULA.getDescripcion();
							id_fco = EnumConceptoPago.MATRICULA.getValue();
						}
						if (tipo.equals("ING")) {
							//descripcionItem = EnumConceptoPago.CUOTA_DE_INGRESO.getDescripcion();
							id_fco = EnumConceptoPago.CUOTA_DE_INGRESO.getValue();
						}
						/*descripcionItem = descripcionItem + " " + anio.getNom() + ", ALUMNO:" + alumno.getApe_pat() + " "
								+ alumno.getApe_mat() + ", " + alumno.getNom() + " NIVEL:" + aula.getGrad().getNivel().getNom()
								+ " GRADO:" + aula.getGrad().getNom() + " " + aula.getSecc() + " CONTRATO:" + matricula.getNum_cont();*/
						
					}else if (tipo.equals("RES")){
						montoMatricula = montoMatricula.add(matriculaPagos.getMonto());

						//esto es un descuento a la matricula
					
						String descripcion = "DESCUENTO POR RESERVA";
						/*descripcion += "\nALUMNO " + alumno.getApe_pat() + " " + alumno.getNom() + "-" + aula.getGrad().getNom() + "-"
								+  aula.getSecc()  + " " +  aula.getGrad().getNivel().getNom();*/
						
					}
					
						 
				}
				mov_montoTotal= mov_monto.subtract(mov_descuento);
				
				AcademicoPago academicoPagoPrincipal = new AcademicoPago();
				academicoPagoPrincipal.setCanc("0");// Se paga al matricular //antes era 1
				academicoPagoPrincipal.setEst("A");
	 			academicoPagoPrincipal.setId_mat(matricula.getId());
				academicoPagoPrincipal.setMonto(montoMatricula);
				academicoPagoPrincipal.setId_bco_pag(id_bco_pag);
				//if()
				//academicoPagoPrincipal.setMontoTotal(montoMatricula);
				academicoPagoPrincipal.setTip(com.tesla.colegio.util.Constante.PAGO_MATRICULA);
				//academicoPagoPrincipal.setNro_rec(nro_rec);
				//academicoPagoPrincipal.setFec_pago(new Date());


				academicoPagoDAO.saveOrUpdate(academicoPagoPrincipal);

				// pago que le corresponde para la cuota de ingreso (Solo cuando es
				// nuevo )
				Matricula matriculaAnterior = matriculaDAO.getMatriculaAnterior(matricula.getId_alu(),periodo.getId_anio() - 1);

				if (matriculaAnterior == null || matriculaAnterior.equals(com.tesla.colegio.util.Constante.CLIENTE_NUEVO)) {
					//verificar si la cuota de ingreso es anual o unico
					String tipo_cuota= confCuota.getTip_cuota_ing();
					if(tipo_cuota!=null){
						if(tipo_cuota.equals("A")){
							AcademicoPago academicoPago = new AcademicoPago();
							academicoPago.setCanc("0");// Paga al matricular	//antes era 1
							academicoPago.setEst("A");
							academicoPago.setId_mat(matricula.getId());
							academicoPago.setMonto(confCuota.getCuota());
							academicoPago.setTip(com.tesla.colegio.util.Constante.PAGO_CUOTA_INGRESO);
							academicoPagoDAO.saveOrUpdate(academicoPago);
						} else if(tipo_cuota.equals("U")){
							//Buscamos si anteriormente ha tenido matriculas y ha pagado Cuota de Ingreso
							List<Row> matriculas = matriculaDAO.listaMatriculasCuotaIngreso(matricula.getId_alu(),id_anio);
							if(matriculas.size()==0){
								AcademicoPago academicoPago = new AcademicoPago();
								academicoPago.setCanc("0");// Paga al matricular	//antes era 1
								academicoPago.setEst("A");
								academicoPago.setId_mat(matricula.getId());
								academicoPago.setMonto(confCuota.getCuota());
								academicoPago.setTip(com.tesla.colegio.util.Constante.PAGO_CUOTA_INGRESO);
								academicoPagoDAO.saveOrUpdate(academicoPago);
							}
						}
					}
					
				}

				Integer[] meses = new Integer[] { 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };

				 param = new Param();
				param.put("id_per", matricula.getId_per());

				ConfMensualidad confMensualidad = confMensualidadDAO.getByParams(param);
				SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

				String format = formatter.format(new Date());
				int fecActual = Integer.parseInt(format);
				try {
					for (int i = 0; i < meses.length; i++) {
						Param param1 = new Param();
						param1.put("id_mat", matricula.getId());
						param1.put("mens", meses[i]);
						AcademicoPago pago_mes = academicoPagoDAO.getByParams(param1);
						if(pago_mes==null){
							Calendar cal = Calendar.getInstance();
							//int anio_actual = cal.get(Calendar.YEAR);
							int anio_matricula= Integer.parseInt(anioDAO.getByParams(new Param("id",id_anio)).getNom());
							String tipo_fec_ven=confMensualidadDAO.getByParams(new Param("id_per", matricula.getId_per())).getTipo_fec_ven();
							int fecVencimiento =0 ;
							Integer dia_mora=0 ;
							String fec_ven=null;
							if(tipo_fec_ven.equals(Constante.TIPO_FEC_VEN_DIA)){
								 fecVencimiento = getFecVencimiento(anio_matricula, meses[i], matricula.getId_per());
								 dia_mora=confMensualidadDAO.getByParams(new Param("id_per", matricula.getId_per())).getDia_mora();
							} else if(tipo_fec_ven.equals(Constante.TIPO_FEC_VEN_FIN)){
								fec_ven=getFecVencimientoFinMes(Integer.parseInt(anio.getNom()), meses[i]);
								String fecha[]  = fec_ven.split("/");
								 fecVencimiento=Integer.parseInt(fecha[2]+fecha[1]+fecha[0]);
								 dia_mora=Integer.parseInt(fec_ven.substring(0, 2));
							}
							//int fecVencimiento = getFecVencimiento(anio_matricula, meses[i], matricula.getId_per());
							//Integer dia_mora=confMensualidadDAO.getByParams(new Param("id_per", matricula.getId_per())).getDia_mora();
							
							AcademicoPago academicoPago = new AcademicoPago();
							
							academicoPago.setEst("A");
							academicoPago.setId_mat(matricula.getId());
							academicoPago.setId_bco_pag(id_bco_pag);
							if (fecActual < fecVencimiento) {
								//int dias_cla=dia_mora-(new Date().getDate());
								BigDecimal monto_mens=new BigDecimal(0);
								//if(dias_cla>0)
								//monto_mens=(confMensualidad.getMonto().divide(new BigDecimal(30),2)).multiply(new BigDecimal(dias_cla));
								//else
								monto_mens=confMensualidad.getMonto();	
								academicoPago.setMonto(monto_mens);
								academicoPago.setCanc("0");// Por defecto pago pendiente
							} else if(fecActual==fecVencimiento){
								academicoPago.setCanc("1");// Por defecto pagado
								academicoPago.setMonto(new BigDecimal(0));
								academicoPago.setMontoTotal(new BigDecimal(0));
							} else if(fecActual > fecVencimiento){
								int dia_act=(new Date().getDate());
								int dias_cla=dia_mora-dia_act;
								academicoPago.setMonto(new BigDecimal(0));
								academicoPago.setCanc("1");//Le pongo cancelado
								academicoPago.setMontoTotal(new BigDecimal(0));
								Integer mes_sig=meses[i]+1;
								AcademicoPago pago_mes_siguiente = new AcademicoPago();
								pago_mes_siguiente.setEst("A");
								pago_mes_siguiente.setId_mat(matricula.getId());
								BigDecimal monto_mens=(confMensualidad.getMonto().divide(new BigDecimal(30),2)).multiply(new BigDecimal(dias_cla));
								pago_mes_siguiente.setMonto(monto_mens);
								pago_mes_siguiente.setCanc("0");// Por defecto pago pendiente
								pago_mes_siguiente.setMens(mes_sig);
								pago_mes_siguiente.setDesc_hermano(new BigDecimal(0));
								pago_mes_siguiente.setDesc_pronto_pago(new BigDecimal(0));
								pago_mes_siguiente.setDesc_pago_adelantado(new BigDecimal(0));
								pago_mes_siguiente.setDesc_personalizado(new BigDecimal(0));
								pago_mes_siguiente.setTip(com.tesla.colegio.util.Constante.PAGO_MENSUAL);
								
								academicoPagoDAO.saveOrUpdate(pago_mes_siguiente);
							} else{
								academicoPago.setMonto(confMensualidad.getMonto());
								academicoPago.setCanc("1");
							}
							//academicoPago.setMonto(confMensualidad.getMonto());
							academicoPago.setMens(meses[i]);
							academicoPago.setTip(com.tesla.colegio.util.Constante.PAGO_MENSUAL);
							SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); 
							academicoPago.setFec_venc(sdf.parse(fec_ven));
							academicoPagoDAO.saveOrUpdate(academicoPago);
						}
					}
				} catch (Exception e) {
					// TODO: handle exception
				}

		} 
		} else if(antiguo_sin_cronograma || nuevos_sin_cronograma) {
			if (matricula.getId() == null) {
				//Busco su matricula anterior
				List<Row> matricula_ant = matriculaDAO.obtenerMatriculaColegioxAlumnoAnio(matricula.getId_alu(), id_anio-1);
				//Funcionaria para los antiguos
				if(matricula_ant.size()>0) {
					Periodo periodo_ant=periodoDAO.get(matricula_ant.get(0).getInteger("id_per"));
					Param param = new Param();
					param.put("id_anio", id_anio);
					param.put("id_suc", id_suc);
					param.put("id_tpe", 1);

					param.put("id_niv", matricula.getId_niv());
					Periodo periodo_act = periodoDAO.getByParams(param);
					Ciclo ciclo_act = cicloDAO.getByParams(new Param("id_per",periodo_act.getId()));
					matricula.setId_per(periodo_act.getId());
					matricula.setId_cic(ciclo_act.getId());
					SeccionSugerida seccionSugerida = seccionSugeridaDAO.getByParams(new Param("id_mat",matricula_ant.get(0).getInteger("id_mat")));
					if(seccionSugerida!=null) {
						Aula aula = aulaDAO.get(seccionSugerida.getId_au_nue());
						Integer cap=aula.getCap();
						Param param5 = new Param();
						param5.put("id_au_asi", aula.getId());
						param5.put("est", "A");
						List<Matricula> matriculas=matriculaDAO.listByParams(param5, null);
						Integer res=vacanteService.getReservasxAula(aula.getId(), id_anio);
						Integer nro_vac=cap-matriculas.size()-res;
						if(nro_vac>0) {
							matricula.setId_au(aula.getId());
							matricula.setId_au_asi(aula.getId());
						} else {
							//Buscamos el aula q tiene vacante
							Param param3= new Param();
							param3.put("aula.id_cic",matricula.getId_cic());
							param3.put("aula.id_grad", matricula_ant.get(0).getInteger("id_gra")+1);
							param3.put("pee.id_suc", id_suc);
							List<Aula> aulas = aulaDAO.listFullByParams(param3, new String[]{"aula.secc"});
							for (Aula aula2 : aulas) {
								//Busco la capacidad del aula
								Integer cap2=aula2.getCap();
								//Busco los matriculados en ese aula
								Param param4 = new Param();
								param4.put("id_au_asi", aula2.getId());
								param4.put("est", "A");
								List<Matricula> matriculas2=matriculaDAO.listByParams(param4, null);
								Integer res2=vacanteService.getReservasxAula(aula2.getId(), id_anio);
								Integer nro_vac2=cap2-matriculas2.size()-res2;
								if(nro_vac2>0) {
									matricula.setId_au(aula2.getId());
									matricula.setId_au_asi(aula2.getId());
									break;
								}
							}
						}
					} else {
						//Buscamos el aula q tiene vacante
						Param param3= new Param();
						param3.put("aula.id_cic",matricula.getId_cic());
						param3.put("aula.id_grad", matricula_ant.get(0).getInteger("id_gra")+1);
						param3.put("pee.id_suc", id_suc);
						List<Aula> aulas = aulaDAO.listFullByParams(param3, new String[]{"aula.secc"});
						for (Aula aula2 : aulas) {
							//Busco la capacidad del aula
							Integer cap2=aula2.getCap();
							//Busco los matriculados en ese aula
							Param param4 = new Param();
							param4.put("id_au_asi", aula2.getId());
							param4.put("est", "A");
							List<Matricula> matriculas2=matriculaDAO.listByParams(param4, null);
							Integer res2=vacanteService.getReservasxAula(aula2.getId(), id_anio);
							Integer nro_vac2=cap2-matriculas2.size()-res2;
							if(nro_vac2>0) {
								matricula.setId_au(aula2.getId());
								matricula.setId_au_asi(aula2.getId());
								break;
							}
						}
					}
					
					if(matricula.getId_au_asi()==null) {
						throw new ServiceException("No existen vacantes disponibles para el grado.");
					}
					matricula.setId_gra(matricula_ant.get(0).getInteger("id_gra")+1);
					matricula.setId_niv(periodo_act.getId_niv());

				} else {
					throw new ServiceException("No es alumno antiguo.");
				}
				
			//Otorgamos el numero de Contrato Correcto
				
				Map<String, Object> contratoActual = matriculaDAO.getContrato(matricula.getId_fam(), id_anio);
				String contrato=null;
				if (contratoActual == null) {
					
					//No comprendo xq se consideraria periodo??
					Map<String, Object> map2 = matriculaDAO.getTotalContratos(id_anio);
					Double total = (Double)map2.get("total");
					
					int correlativo = total.intValue() + 1;
					contrato = anio.getNom() + "-" + String.format("%05d", correlativo);

				} else {
					contrato = contratoActual.get("num_cont").toString();
				}
				matricula.setNum_cont(contrato);
				
				//buscar si contrato ya existe
				Param param = new Param();
				param.put("num_cont", matricula.getNum_cont());
				
				
				List<Matricula> matriculasContrato = matriculaDAO.listByParams(param, new String[]{"id desc"});
				//Si existe matricula del mismo local 				
				
				matricula.setId_suc_con(2);
				/*if (matriculasContrato.size()==0){
					matricula.setId_suc_con(id_suc);
				} else{
					matricula.setId_suc_con(matriculasContrato.get(0).getId_suc_con());
				}	*/
				
				Integer tip_mat=null;
				if(id_perfil.equals(Constante.PERFIL_FAMILIAR))
					tip_mat=2;
				else if(id_perfil.equals(Constante.PERFIL_TRABAJADOR))
					tip_mat=1;
				matricula.setTip_mat(tip_mat);
				matricula.setTipo("C");
				matricula.setMat_val("0");
				
				int id = matriculaDAO.saveOrUpdate(matricula);
				//aumento el codigo de adenda si existe
				/*if(ins_adenda==true){
					Integer nuevo_num=correlativo_adenda+1;
					correlativoDAO.updateCorrelativo(nuevo_num, id_suc, id_anio);
					
				}*/
				// cuando se inserta las matriculas
				matricula.setId(id);
				
				Alumno alumno = alumnoDAO.get(matricula.getId_alu());
				GruFamAlumno gruFamAlumno = gruFamAlumnoDAO.getByParams(new Param("id_alu",alumno.getId()));
				gpfa = gruFamAlumno.getId_gpf();
				// pago que le corresponde para la matricula
				/*String nro_rec = facturacionService.getNroRecibo(id_suc);
				facturacionService.updateNroRecibo(id_suc, nro_rec);*/

				BigDecimal mov_monto= BigDecimal.ZERO;
				BigDecimal mov_montoTotal = BigDecimal.ZERO;
				BigDecimal mov_descuento= BigDecimal.ZERO;
				
				Matricula matricula_nueva = matriculaDAO.get(id);
				ConfCuota confCuota = confCuotaDAO.getByParams(new Param("id_per", matricula_nueva.getId_per()));
				Periodo periodo_nuevo= periodoDAO.get(matricula_nueva.getId_per());
				ReservaCuota reservaCuota = reservaDAO.getMontoReserva(periodo_nuevo.getId_anio(), matricula.getId_alu());
				BigDecimal montoMatricula = BigDecimal.ZERO;// confCuota.getMatricula();

				Periodo periodo = periodoDAO.get(matricula.getId_per());

				List<MatriculaPagos> listPagar = obtenerPagosProgramadosv2(matricula_nueva.getId(),matricula_nueva.getId_alu(), periodo_nuevo.getId(), periodo_nuevo.getId_anio(),null);
				for (MatriculaPagos matriculaPagos : listPagar) {
					 
					String tipo = matriculaPagos.getTip();
					
					if (tipo.equals("MAT") || tipo.equals("ING")){

						mov_monto =  mov_monto.add(matriculaPagos.getMonto());
						//String descripcionItem = null;
						Integer id_fco = null;//CONCEPTO DE PAGO
						if (tipo.equals("MAT")) {
							montoMatricula = montoMatricula.add(matriculaPagos.getMonto());

							//descripcionItem = EnumConceptoPago.MATRICULA.getDescripcion();
							id_fco = EnumConceptoPago.MATRICULA.getValue();
						}
						if (tipo.equals("ING")) {
							//descripcionItem = EnumConceptoPago.CUOTA_DE_INGRESO.getDescripcion();
							id_fco = EnumConceptoPago.CUOTA_DE_INGRESO.getValue();
						}
						/*descripcionItem = descripcionItem + " " + anio.getNom() + ", ALUMNO:" + alumno.getApe_pat() + " "
								+ alumno.getApe_mat() + ", " + alumno.getNom() + " NIVEL:" + aula.getGrad().getNivel().getNom()
								+ " GRADO:" + aula.getGrad().getNom() + " " + aula.getSecc() + " CONTRATO:" + matricula.getNum_cont();*/
						
					}else if (tipo.equals("RES")){
						montoMatricula = montoMatricula.add(matriculaPagos.getMonto());

						//esto es un descuento a la matricula
					
						String descripcion = "DESCUENTO POR RESERVA";
						/*descripcion += "\nALUMNO " + alumno.getApe_pat() + " " + alumno.getNom() + "-" + aula.getGrad().getNom() + "-"
								+  aula.getSecc()  + " " +  aula.getGrad().getNivel().getNom();*/
						
					}
					
						 
				}
				mov_montoTotal= mov_monto.subtract(mov_descuento);
				
				AcademicoPago academicoPagoPrincipal = new AcademicoPago();
				academicoPagoPrincipal.setCanc("0");// Se paga al matricular //antes era 1
				academicoPagoPrincipal.setEst("A");
	 			academicoPagoPrincipal.setId_mat(matricula.getId());
				academicoPagoPrincipal.setMonto(montoMatricula);
				academicoPagoPrincipal.setId_bco_pag(id_bco_pag);
				//if()
				//academicoPagoPrincipal.setMontoTotal(montoMatricula);
				academicoPagoPrincipal.setTip(com.tesla.colegio.util.Constante.PAGO_MATRICULA);
				//academicoPagoPrincipal.setNro_rec(nro_rec);
				//academicoPagoPrincipal.setFec_pago(new Date());


				academicoPagoDAO.saveOrUpdate(academicoPagoPrincipal);

				// pago que le corresponde para la cuota de ingreso (Solo cuando es
				// nuevo )
				Matricula matriculaAnterior = matriculaDAO.getMatriculaAnterior(matricula.getId_alu(),periodo.getId_anio() - 1);

				if (matriculaAnterior == null || matriculaAnterior.equals(Constante.CLIENTE_NUEVO)) {
					//verificar si la cuota de ingreso es anual o unico
					String tipo_cuota= confCuota.getTip_cuota_ing();
					if(tipo_cuota!=null){
						if(tipo_cuota.equals("A")){
							AcademicoPago academicoPago = new AcademicoPago();
							academicoPago.setCanc("0");// Paga al matricular	//antes era 1
							academicoPago.setEst("A");
							academicoPago.setId_mat(matricula.getId());
							academicoPago.setMonto(confCuota.getCuota());
							academicoPago.setTip(com.tesla.colegio.util.Constante.PAGO_CUOTA_INGRESO);
							academicoPagoDAO.saveOrUpdate(academicoPago);
						} else if(tipo_cuota.equals("U")){
							//Buscamos si anteriormente ha tenido matriculas y ha pagado Cuota de Ingreso
							List<Row> matriculas = matriculaDAO.listaMatriculasCuotaIngreso(matricula.getId_alu(),id_anio);
							if(matriculas.size()==0){
								AcademicoPago academicoPago = new AcademicoPago();
								academicoPago.setCanc("0");// Paga al matricular	//antes era 1
								academicoPago.setEst("A");
								academicoPago.setId_mat(matricula.getId());
								academicoPago.setMonto(confCuota.getCuota());
								academicoPago.setTip(com.tesla.colegio.util.Constante.PAGO_CUOTA_INGRESO);
								academicoPagoDAO.saveOrUpdate(academicoPago);
							}
						}
					}
					
				}

				Integer[] meses = new Integer[] { 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };

				 param = new Param();
				param.put("id_per", matricula.getId_per());

				ConfMensualidad confMensualidad = confMensualidadDAO.getByParams(param);
				SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

				String format = formatter.format(new Date());
				int fecActual = Integer.parseInt(format);
				try {
					for (int i = 0; i < meses.length; i++) {
						Param param1 = new Param();
						param1.put("id_mat", matricula.getId());
						param1.put("mens", meses[i]);
						AcademicoPago pago_mes = academicoPagoDAO.getByParams(param1);
						if(pago_mes==null){
							Calendar cal = Calendar.getInstance();
							//int anio_actual = cal.get(Calendar.YEAR);
							int anio_matricula= Integer.parseInt(anioDAO.getByParams(new Param("id",id_anio)).getNom());
							String tipo_fec_ven=confMensualidadDAO.getByParams(new Param("id_per", matricula.getId_per())).getTipo_fec_ven();
							int fecVencimiento =0 ;
							Integer dia_mora=0 ;
							String fec_ven=null;
							if(tipo_fec_ven.equals(Constante.TIPO_FEC_VEN_DIA)){
								 fecVencimiento = getFecVencimiento(anio_matricula, meses[i], matricula.getId_per());
								 dia_mora=confMensualidadDAO.getByParams(new Param("id_per", matricula.getId_per())).getDia_mora();
							} else if(tipo_fec_ven.equals(Constante.TIPO_FEC_VEN_FIN)){
								fec_ven=getFecVencimientoFinMes(Integer.parseInt(anio.getNom()), meses[i]);
								String fecha[]  = fec_ven.split("/");
								 fecVencimiento=Integer.parseInt(fecha[2]+fecha[1]+fecha[0]);
								 dia_mora=Integer.parseInt(fec_ven.substring(0, 2));
							}
							//int fecVencimiento = getFecVencimiento(anio_matricula, meses[i], matricula.getId_per());
							//Integer dia_mora=confMensualidadDAO.getByParams(new Param("id_per", matricula.getId_per())).getDia_mora();
							
							AcademicoPago academicoPago = new AcademicoPago();
							
							academicoPago.setEst("A");
							academicoPago.setId_mat(matricula.getId());
							academicoPago.setId_bco_pag(id_bco_pag);
							if (fecActual < fecVencimiento) {
								//int dias_cla=dia_mora-(new Date().getDate());
								BigDecimal monto_mens=new BigDecimal(0);
								//if(dias_cla>0)
								//monto_mens=(confMensualidad.getMonto().divide(new BigDecimal(30),2)).multiply(new BigDecimal(dias_cla));
								//else
								monto_mens=confMensualidad.getMonto();	
								academicoPago.setMonto(monto_mens);
								academicoPago.setCanc("0");// Por defecto pago pendiente
							} else if(fecActual==fecVencimiento){
								academicoPago.setCanc("1");// Por defecto pagado
								academicoPago.setMonto(new BigDecimal(0));
								academicoPago.setMontoTotal(new BigDecimal(0));
							} else if(fecActual > fecVencimiento){
								int dia_act=(new Date().getDate());
								int dias_cla=dia_mora-dia_act;
								academicoPago.setMonto(new BigDecimal(0));
								academicoPago.setCanc("1");//Le pongo cancelado
								academicoPago.setMontoTotal(new BigDecimal(0));
								Integer mes_sig=meses[i]+1;
								AcademicoPago pago_mes_siguiente = new AcademicoPago();
								pago_mes_siguiente.setEst("A");
								pago_mes_siguiente.setId_mat(matricula.getId());
								BigDecimal monto_mens=(confMensualidad.getMonto().divide(new BigDecimal(30),2)).multiply(new BigDecimal(dias_cla));
								pago_mes_siguiente.setMonto(monto_mens);
								pago_mes_siguiente.setCanc("0");// Por defecto pago pendiente
								pago_mes_siguiente.setMens(mes_sig);
								pago_mes_siguiente.setDesc_hermano(new BigDecimal(0));
								pago_mes_siguiente.setDesc_pronto_pago(new BigDecimal(0));
								pago_mes_siguiente.setDesc_pago_adelantado(new BigDecimal(0));
								pago_mes_siguiente.setDesc_personalizado(new BigDecimal(0));
								pago_mes_siguiente.setTip(com.tesla.colegio.util.Constante.PAGO_MENSUAL);
								
								academicoPagoDAO.saveOrUpdate(pago_mes_siguiente);
							} else{
								academicoPago.setMonto(confMensualidad.getMonto());
								academicoPago.setCanc("1");
							}
							//academicoPago.setMonto(confMensualidad.getMonto());
							academicoPago.setMens(meses[i]);
							academicoPago.setTip(com.tesla.colegio.util.Constante.PAGO_MENSUAL);
							SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); 
							academicoPago.setFec_venc(sdf.parse(fec_ven));
							academicoPagoDAO.saveOrUpdate(academicoPago);
						}
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.getMessage();
				}

		} 
		}

		map.put("id_alu", matricula.getId_alu());
		map.put("id_mat", matricula.getId());
		map.put("id_gpf", gpfa);
		map.put("id_apod", matricula.getId_fam());
		//return impresion;
		return map;

	}

	@Transactional
	public Map<String, Object> matricularYPagar(Matricula matricula, Integer id_suc, Integer id_anio, Integer id_perfil) throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();

		// grabamos matricula
		matricula.setEst("A");
		Integer gpfa = null;
		
		Anio anio = anioDAO.get(id_anio);

		if (matricula.getId() == null) {

			matricula.setId_au_asi(matricula.getId_au());
			Aula aula = aulaDAO.getFull(matricula.getId_au(), new String[] { Nivel.TABLA, Grad.TABLA,Periodo.TABLA });
			matricula.setId_per(aula.getId_per());
		//Otorgamos el numero de Contrato Correcto
			
			String contratoReal=null;
			Map<String, Object> contratoActual = matriculaDAO.getContrato(matricula.getId_fam(), id_anio);

			if (contratoActual == null) {
				Anio anio2 = anioDAO.get(id_anio);
				
				Map<String, Object> map2 = matriculaDAO.getTotalContratosxLocal(id_anio, id_suc);
				Double total = (Double)map2.get("total");
				
				int correlativo = total.intValue() + 1;
				
				if (correlativo==1){
					Sucursal sucursal = sucursalDAO.get(id_suc);
					map2.put("cod", sucursal.getCod());
				}
				
				String contrato = anio.getNom() + "-" +  map2.get("cod") + "-" + String.format("%05d", correlativo);

				contratoReal=contrato;
			} else {
				contratoReal=contratoActual.get("num_cont").toString();
			}
			
			matricula.setNum_cont(contratoReal);
			
			//buscar si contrato ya existe
			Param param = new Param();
			param.put("num_cont", matricula.getNum_cont());
			
			
			List<Matricula> matriculasContrato = matriculaDAO.listByParams(param, new String[]{"id desc"});
			//Si existe matricula del mismo local 
			Integer correlativo_adenda=null;
			String num_adenda=null;
			Boolean ins_adenda =false;
			List<Row> matriculasLocalApod= matriculaDAO.matriculadosxLocalyApoderado(id_suc, matricula.getId_fam(), id_anio);
			
			
			/*if(matriculasContrato.size()==0){ //si no existe ninguna matricula no inserto adenda
				//matricula.setId_suc_con(id_suc);
			
			} else*/
			if(matricula.getNum_adenda()==null){
				if (matriculasContrato.size()>0 && matriculasLocalApod.size()==0){ // si ya existe matricula pero de otros locales inserto adenda
					//inserto adenda
					//Ontengo el correlativo de adenda
					correlativo_adenda=correlativoDAO.obtenerCorrelativoAdenda(id_suc, id_anio); 
					if(id_suc==2){
						num_adenda=anio.getNom() + "-001-"+String.format("%05d", correlativo_adenda);
						ins_adenda=true;
					} else if(id_suc==3){
						num_adenda=anio.getNom() + "-002-"+String.format("%05d", correlativo_adenda);
						ins_adenda=true;
					} else if(id_suc==4){
						num_adenda=anio.getNom() + "-003-"+String.format("%05d", correlativo_adenda);
						ins_adenda=true;
					}
				} else if(matriculasContrato.size()>0 && matriculasLocalApod.size()>0){
					//recorro las matriculas por apoderado
					//Integer cant_adendas=0;
					for (Row row : matriculasLocalApod) {
						if(row.getString("num_adenda")!=null){
							num_adenda=row.getString("num_adenda");
							break;
						}
					}
				}
				matricula.setNum_adenda(num_adenda);
			}
			
			
			if (matriculasContrato.size()==0){
				matricula.setId_suc_con(id_suc);
			} else{
				matricula.setId_suc_con(matriculasContrato.get(0).getId_suc_con());
			}	
			
			Integer tip_mat=null;
			if(id_perfil.equals(Constante.PERFIL_FAMILIAR))
				tip_mat=2;
			else if(id_perfil.equals(Constante.PERFIL_TRABAJADOR))
				tip_mat=1;
			matricula.setTip_mat(tip_mat);
			matricula.setMat_val("0");
			
			int id = matriculaDAO.saveOrUpdate(matricula);
			//aumento el codigo de adenda si existe
			if(ins_adenda==true){
				Integer nuevo_num=correlativo_adenda+1;
				correlativoDAO.updateCorrelativo(nuevo_num, id_suc, id_anio);
				
			}
			// cuando se inserta las matriculas
			matricula.setId(id);
			
			Alumno alumno = alumnoDAO.get(matricula.getId_alu());
			GruFamAlumno gruFamAlumno = gruFamAlumnoDAO.getByParams(new Param("id_alu",alumno.getId()));
			gpfa = gruFamAlumno.getId_gpf();
			// pago que le corresponde para la matricula
			/*String nro_rec = facturacionService.getNroRecibo(id_suc);
			facturacionService.updateNroRecibo(id_suc, nro_rec);*/

			BigDecimal mov_monto= BigDecimal.ZERO;
			BigDecimal mov_montoTotal = BigDecimal.ZERO;
			BigDecimal mov_descuento= BigDecimal.ZERO;
			
			ConfCuota confCuota = confCuotaDAO.getByParams(new Param("id_per", matricula.getId_per()));

			//revisar si tiene un pago por reserva
			//Aula aula = aulaDAO.getFull(matricula.getId_au(), new String[]{Periodo.TABLA});
			
			
			ReservaCuota reservaCuota = reservaDAO.getMontoReserva(aula.getPeriodo().getId_anio(), matricula.getId_alu());
			BigDecimal montoMatricula = BigDecimal.ZERO;// confCuota.getMatricula();

			Periodo periodo = periodoDAO.get(matricula.getId_per());
			//Anio anio = anioDAO.get(periodo.getId_anio());
			
			//verificar si tendra adenda
			
			
			/*Movimiento movimiento = new Movimiento();
			movimiento.setDescuento(new BigDecimal(0));
			movimiento.setEst("A");
			movimiento.setFec(matricula.getFecha());
			movimiento.setId_mat(matricula.getId());

			movimiento.setId_fam(matricula.getId_fam());
			movimiento.setId_suc(id_suc); 
			movimiento.setNro_rec(nro_rec);
			movimiento.setTipo(EnumTipoMovimiento.INGRESO.getValue());
			movimiento.setId_fpa(1);// EFECTIVO
 
			movimiento.setObs("MATRICULA " + anio.getNom());*/
			
			//Boolean reserva = true;

			List<MatriculaPagos> listPagar = obtenerPagosProgramados(matricula.getId(),matricula.getId_alu(), matricula.getId_au(), aula.getPeriodo().getId_anio(),null);
			for (MatriculaPagos matriculaPagos : listPagar) {
				 
				String tipo = matriculaPagos.getTip();
				
				if (tipo.equals("MAT") || tipo.equals("ING")){

					mov_monto =  mov_monto.add(matriculaPagos.getMonto());
					String descripcionItem = null;
					Integer id_fco = null;//CONCEPTO DE PAGO
					if (tipo.equals("MAT")) {
						montoMatricula = montoMatricula.add(matriculaPagos.getMonto());

						descripcionItem = EnumConceptoPago.MATRICULA.getDescripcion();
						id_fco = EnumConceptoPago.MATRICULA.getValue();
					}
					if (tipo.equals("ING")) {
						descripcionItem = EnumConceptoPago.CUOTA_DE_INGRESO.getDescripcion();
						id_fco = EnumConceptoPago.CUOTA_DE_INGRESO.getValue();
					}
					descripcionItem = descripcionItem + " " + anio.getNom() + ", ALUMNO:" + alumno.getApe_pat() + " "
							+ alumno.getApe_mat() + ", " + alumno.getNom() + " NIVEL:" + aula.getGrad().getNivel().getNom()
							+ " GRADO:" + aula.getGrad().getNom() + " " + aula.getSecc() + " CONTRATO:" + matricula.getNum_cont();
					 
				/*	MovimientoDetalle movimientoDetalle = new MovimientoDetalle();
					movimientoDetalle.setDescuento(new BigDecimal(0));
					movimientoDetalle.setEst("A");
					movimientoDetalle.setId_fco(id_fco);
 					movimientoDetalle.setMonto(confCuota.getMatricula());
					movimientoDetalle.setMonto_total(confCuota.getMatricula());
					movimientoDetalle.setObs(descripcionItem);
					
					if (movimiento.getMovimientoDetalles()==null)
						movimiento.setMovimientoDetalle(new ArrayList<MovimientoDetalle>());
					movimiento.getMovimientoDetalles().add(movimientoDetalle);*/
					
				}else if (tipo.equals("RES")){
					montoMatricula = montoMatricula.add(matriculaPagos.getMonto());

					//esto es un descuento a la matricula
				
					String descripcion = "DESCUENTO POR RESERVA";
					descripcion += "\nALUMNO " + alumno.getApe_pat() + " " + alumno.getNom() + "-" + aula.getGrad().getNom() + "-"
							+  aula.getSecc()  + " " +  aula.getGrad().getNivel().getNom();
					
					/*MovimientoDetalle movimientoDetalleMAt =  null;
					for (MovimientoDetalle movimientoDetalle1 :movimiento.getMovimientoDetalles()) {
						if (movimientoDetalle1.getId_fco().intValue() == EnumConceptoPago.MATRICULA.getValue() )
							movimientoDetalleMAt = movimientoDetalle1;
					}

					 MovimientoDescuento movimientoDescuento = new MovimientoDescuento();
					 
					 movimientoDescuento.setDes(descripcion);
					 movimientoDescuento.setDescuento(matriculaPagos.getMonto().multiply(new BigDecimal(-1)));
					 movimientoDescuento.setEst("A");
					// mov_descuento = mov_descuento.add(augend)
					mov_descuento = mov_descuento.add(movimientoDescuento.getDescuento());

					 
					 movimientoDetalleMAt.getDescuentos().add(movimientoDescuento);
					 movimientoDetalleMAt.setDescuento(mov_descuento);
					 movimientoDetalleMAt.setMonto_total(movimientoDetalleMAt.getMonto().subtract(mov_descuento));*/
				}
				
					 
			}
			mov_montoTotal= mov_monto.subtract(mov_descuento);
			/*movimiento.setMonto(mov_monto);
			movimiento.setDescuento(mov_descuento);
			movimiento.setMonto_total(mov_montoTotal);*/
			
			
			
			AcademicoPago academicoPagoPrincipal = new AcademicoPago();
			academicoPagoPrincipal.setCanc("0");// Se paga al matricular //antes era 1
			academicoPagoPrincipal.setEst("A");
 			academicoPagoPrincipal.setId_mat(matricula.getId());
			academicoPagoPrincipal.setMonto(montoMatricula);
			//if()
			//academicoPagoPrincipal.setMontoTotal(montoMatricula);
			academicoPagoPrincipal.setTip(com.tesla.colegio.util.Constante.PAGO_MATRICULA);
			//academicoPagoPrincipal.setNro_rec(nro_rec);
			//academicoPagoPrincipal.setFec_pago(new Date());


			academicoPagoDAO.saveOrUpdate(academicoPagoPrincipal);

			// pago que le corresponde para la cuota de ingreso (Solo cuando es
			// nuevo )
			Matricula matriculaAnterior = matriculaDAO.getMatriculaAnterior(matricula.getId_alu(),periodo.getId_anio() - 1);

			if (matriculaAnterior == null || matriculaAnterior.equals(com.tesla.colegio.util.Constante.CLIENTE_NUEVO)) {
				//verificar si la cuota de ingreso es anual o unico
				String tipo_cuota= confCuota.getTip_cuota_ing();
				if(tipo_cuota!=null){
					if(tipo_cuota.equals("A")){
						AcademicoPago academicoPago = new AcademicoPago();
						academicoPago.setCanc("0");// Paga al matricular	//antes era 1
						academicoPago.setEst("A");
						academicoPago.setId_mat(matricula.getId());
						academicoPago.setMonto(confCuota.getCuota());
						academicoPago.setTip(com.tesla.colegio.util.Constante.PAGO_CUOTA_INGRESO);
						academicoPagoDAO.saveOrUpdate(academicoPago);
					} else if(tipo_cuota.equals("U")){
						//Buscamos si anteriormente ha tenido matriculas y ha pagado Cuota de Ingreso
						List<Row> matriculas = matriculaDAO.listaMatriculasCuotaIngreso(matricula.getId_alu(),id_anio);
						if(matriculas.size()==0){
							AcademicoPago academicoPago = new AcademicoPago();
							academicoPago.setCanc("0");// Paga al matricular	//antes era 1
							academicoPago.setEst("A");
							academicoPago.setId_mat(matricula.getId());
							academicoPago.setMonto(confCuota.getCuota());
							academicoPago.setTip(com.tesla.colegio.util.Constante.PAGO_CUOTA_INGRESO);
							academicoPagoDAO.saveOrUpdate(academicoPago);
						}
					}
				}
				
			}

			Integer[] meses = new Integer[] { 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };

			 param = new Param();
			param.put("id_per", matricula.getId_per());

			ConfMensualidad confMensualidad = confMensualidadDAO.getByParams(param);
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

			String format = formatter.format(new Date());
			int fecActual = Integer.parseInt(format);
			
			for (int i = 0; i < meses.length; i++) {
				Param param1 = new Param();
				param1.put("id_mat", matricula.getId());
				param1.put("mens", meses[i]);
				AcademicoPago pago_mes = academicoPagoDAO.getByParams(param1);
				if(pago_mes==null){
					Calendar cal = Calendar.getInstance();
					//int anio_actual = cal.get(Calendar.YEAR);
					int anio_matricula= Integer.parseInt(anioDAO.getByParams(new Param("id",id_anio)).getNom());
					String tipo_fec_ven=confMensualidadDAO.getByParams(new Param("id_per", matricula.getId_per())).getTipo_fec_ven();
					int fecVencimiento =0 ;
					Integer dia_mora=0 ;
					String fec_ven=null;
					if(tipo_fec_ven.equals(Constante.TIPO_FEC_VEN_DIA)){
						 fecVencimiento = getFecVencimiento(anio_matricula, meses[i], matricula.getId_per());
						 dia_mora=confMensualidadDAO.getByParams(new Param("id_per", matricula.getId_per())).getDia_mora();
					} else if(tipo_fec_ven.equals(Constante.TIPO_FEC_VEN_FIN)){
						fec_ven=getFecVencimientoFinMes(Integer.parseInt(anio.getNom()), meses[i]);
						String fecha[]  = fec_ven.split("/");
						 fecVencimiento=Integer.parseInt(fecha[2]+fecha[1]+fecha[0]);
						 dia_mora=Integer.parseInt(fec_ven.substring(0, 2));
					}
					//int fecVencimiento = getFecVencimiento(anio_matricula, meses[i], matricula.getId_per());
					//Integer dia_mora=confMensualidadDAO.getByParams(new Param("id_per", matricula.getId_per())).getDia_mora();
					
					AcademicoPago academicoPago = new AcademicoPago();
					
					academicoPago.setEst("A");
					academicoPago.setId_mat(matricula.getId());
					if (fecActual < fecVencimiento) {
						//int dias_cla=dia_mora-(new Date().getDate());
						BigDecimal monto_mens=new BigDecimal(0);
						//if(dias_cla>0)
						//monto_mens=(confMensualidad.getMonto().divide(new BigDecimal(30),2)).multiply(new BigDecimal(dias_cla));
						//else
						monto_mens=confMensualidad.getMonto();	
						academicoPago.setMonto(monto_mens);
						academicoPago.setCanc("0");// Por defecto pago pendiente
					} else if(fecActual==fecVencimiento){
						academicoPago.setCanc("1");// Por defecto pagado
						academicoPago.setMonto(new BigDecimal(0));
						academicoPago.setMontoTotal(new BigDecimal(0));
					} else if(fecActual > fecVencimiento){
						int dia_act=(new Date().getDate());
						int dias_cla=dia_mora-dia_act;
						academicoPago.setMonto(new BigDecimal(0));
						academicoPago.setCanc("1");//Le pongo cancelado
						academicoPago.setMontoTotal(new BigDecimal(0));
						Integer mes_sig=meses[i]+1;
						AcademicoPago pago_mes_siguiente = new AcademicoPago();
						pago_mes_siguiente.setEst("A");
						pago_mes_siguiente.setId_mat(matricula.getId());
						BigDecimal monto_mens=(confMensualidad.getMonto().divide(new BigDecimal(30),2)).multiply(new BigDecimal(dias_cla));
						pago_mes_siguiente.setMonto(monto_mens);
						pago_mes_siguiente.setCanc("0");// Por defecto pago pendiente
						pago_mes_siguiente.setMens(mes_sig);
						pago_mes_siguiente.setDesc_hermano(new BigDecimal(0));
						pago_mes_siguiente.setDesc_pronto_pago(new BigDecimal(0));
						pago_mes_siguiente.setDesc_pago_adelantado(new BigDecimal(0));
						pago_mes_siguiente.setDesc_personalizado(new BigDecimal(0));
						pago_mes_siguiente.setTip(com.tesla.colegio.util.Constante.PAGO_MENSUAL);
						
						academicoPagoDAO.saveOrUpdate(pago_mes_siguiente);
					} else{
						academicoPago.setMonto(confMensualidad.getMonto());
						academicoPago.setCanc("1");
					}
					//academicoPago.setMonto(confMensualidad.getMonto());
					academicoPago.setMens(meses[i]);
					academicoPago.setTip(com.tesla.colegio.util.Constante.PAGO_MENSUAL);
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); 
					academicoPago.setFec_venc(sdf.parse(fec_ven));
					academicoPagoDAO.saveOrUpdate(academicoPago);
				}
			}

			// ES UN PAGO DE UN ALUMNO MATRICULADO

			/*Integer id_fmo = movimientoDAO.saveOrUpdate(movimiento);
			for (MovimientoDetalle movimientoDetalle : movimiento.getMovimientoDetalles()) {
				movimientoDetalle.setId_fmo(id_fmo);
				Integer id_fmd = movimientoDetalleDAO.saveOrUpdate(movimientoDetalle);

				if (movimientoDetalle.getDescuentos().size()>0){
			
					for (MovimientoDescuento descuento : movimientoDetalle.getDescuentos() ) {
						descuento.setId_fmd(id_fmd);
						movimientoDescuentoDAO.saveOrUpdate(descuento);
						
					}
				}
				
			}
			 impresion = facturacionService.getImpresion(nro_rec);*/

		}

		map.put("id_alu", matricula.getId_alu());
		map.put("id_mat", matricula.getId());
		map.put("id_gpf", gpfa);
		map.put("id_apod", matricula.getId_fam());
		//return impresion;
		return map;

	}
	
	@Transactional
	public void actualizarSeccion( Integer id_mat , Integer id_au) throws Exception {
			 
			Matricula matricula = matriculaDAO.getFull(id_mat,new String []{Periodo.TABLA});
			
			Param param = new Param();
			param.put("id_alu", matricula.getId_alu());
			param.put("id_anio", matricula.getPeriodo().getId_anio());
			param.put("est", "A");
			Solicitud solicitud = solicitudDAO.getByParams(param);
			
			//if (solicitud==null){//ACTUALIZA SECCI�N SIN HACER NADA MAS Jose desea actualizar a cualquiera 2021
				matriculaDAO.actualizarSeccion(id_mat , id_au);
			//}
	}

	
	public List<MatriculaPagos> obtenerPagosProgramados(Integer id_mat, Integer id_alu, Integer id_au, Integer id_anio, String tip) {
		// obtener periodo de estudio

		List<MatriculaPagos> list = new ArrayList<MatriculaPagos>();
		//Obtenemos la Matricula
		/*		Param param = new Param();
				param.put("pee.id_anio", id_anio);
				param.put("mat.id_alu", id_alu);*/
		Matricula matricula = matriculaDAO.getByParams(new Param("id",id_mat));
				
		/*Aula aula = aulaDAO.get(matricula.get(0).getId_au_asi());*/
		/*Param param = new Param();
		param.put("id_mat", id_mat);
		param.put("tip", 'MAT');
		AcademicoPago academicoPago = academicoPagoDAO.getByParams(param)*/
		
		Aula aula = aulaDAO.get(id_au);
		
		/** PAGO POR MATRICULA **/
		ConfCuota confCuota = confCuotaDAO.getByParams(new Param("id_per", aula.getId_per()));
		MatriculaPagos academicoPago = new MatriculaPagos();
		academicoPago.setCanc("0");// Por defecto pago pendiente
		academicoPago.setMonto(confCuota.getMatricula());
		academicoPago.setTip(com.tesla.colegio.util.Constante.PAGO_MATRICULA);
		//if(matricula.getMat_val().equals("0"))
			list.add(academicoPago);

		ReservaCuota reservaCuota = reservaDAO.getMontoReserva(id_anio, id_alu);
		if(tip!=null){
			if(tip.equals("CL")){
				if (reservaCuota != null && matricula.getId_cli()!=1) {
					/** PAGO POR RESERVA **/
					BigDecimal montoReserva = reservaCuota.getMonto();
					academicoPago = new MatriculaPagos();
					academicoPago.setCanc("1");// PAGO CANCELADO
					academicoPago.setMonto(montoReserva.multiply(new BigDecimal(-1)));
					academicoPago.setTip(com.tesla.colegio.util.Constante.PAGO_RESERVA);
					academicoPago.setNro_rec(reservaCuota.getNro_recibo());
					list.add(academicoPago);
					
					academicoPago.setMontoReserva(montoReserva);
				}
			} else{
				if (reservaCuota != null) {
					/** PAGO POR RESERVA **/
					BigDecimal montoReserva = reservaCuota.getMonto();
					academicoPago = new MatriculaPagos();
					academicoPago.setCanc("1");// PAGO CANCELADO
					academicoPago.setMonto(montoReserva.multiply(new BigDecimal(-1)));
					academicoPago.setTip(com.tesla.colegio.util.Constante.PAGO_RESERVA);
					academicoPago.setNro_rec(reservaCuota.getNro_recibo());
					list.add(academicoPago);
					
					academicoPago.setMontoReserva(montoReserva);
				}
			}
		} else{
			if (reservaCuota != null) {
				/** PAGO POR RESERVA **/
				BigDecimal montoReserva = reservaCuota.getMonto();
				academicoPago = new MatriculaPagos();
				academicoPago.setCanc("1");// PAGO CANCELADO
				academicoPago.setMonto(montoReserva.multiply(new BigDecimal(-1)));
				academicoPago.setTip(com.tesla.colegio.util.Constante.PAGO_RESERVA);
				academicoPago.setNro_rec(reservaCuota.getNro_recibo());
				list.add(academicoPago);
				
				academicoPago.setMontoReserva(montoReserva);
			}
		}
		
		
		
		/** PAGO POR CUOTA DE INGRESO **/
		Matricula mat_anterior = obtenerMatriculaAnterior(id_alu, id_anio);

		
 		if (mat_anterior==null) {//alumno nuevo
 			//Verificamos si la cuota de Ingreso es Unico o Anual
 			String tipo_cuota=confCuota.getTip_cuota_ing();
 			if(tipo_cuota!=null){
 				if(tipo_cuota.equals("A")){
 	 				academicoPago = new MatriculaPagos();
 	 				academicoPago.setCanc("0");// Por defecto pago pendiente
 	 				academicoPago.setMonto(confCuota.getCuota());
 	 				academicoPago.setTip(com.tesla.colegio.util.Constante.PAGO_CUOTA_INGRESO);
 	 				list.add(academicoPago);
 	 			} else if(tipo_cuota.equals("U")){
 	 				//Verificamos si ha antes ha pagado cuota de ingreso
 	 				List<Row> matriculas= matriculaDAO.listaMatriculasCuotaIngreso(id_alu,id_anio);
 	 				if(matriculas.size()==0){
 	 					academicoPago = new MatriculaPagos();
 	 	 				academicoPago.setCanc("0");// Por defecto pago pendiente
 	 	 				academicoPago.setMonto(confCuota.getCuota());
 	 	 				academicoPago.setTip(com.tesla.colegio.util.Constante.PAGO_CUOTA_INGRESO);
 	 	 				list.add(academicoPago);
 	 				} /*else{
 	 					academicoPago = new MatriculaPagos();
 	 	 				academicoPago.setCanc("0");// Por defecto pago pendiente
 	 	 				academicoPago.setMonto(confCuota.getCuota());
 	 	 				academicoPago.setTip(com.tesla.colegio.util.Constante.PAGO_CUOTA_INGRESO);
 	 	 				list.add(academicoPago);
 	 				}*/
 	 			}
 			}
		}

		return list;
	}	
	
	public List<MatriculaPagos> obtenerPagosProgramadosv2(Integer id_mat, Integer id_alu, Integer id_per, Integer id_anio, String tip) {
		// obtener periodo de estudio

		List<MatriculaPagos> list = new ArrayList<MatriculaPagos>();
		//Obtenemos la Matricula
		/*		Param param = new Param();
				param.put("pee.id_anio", id_anio);
				param.put("mat.id_alu", id_alu);*/
		Matricula matricula = matriculaDAO.getByParams(new Param("id",id_mat));
				
		/** PAGO POR MATRICULA **/
		ConfCuota confCuota = confCuotaDAO.getByParams(new Param("id_per", id_per));
		MatriculaPagos academicoPago = new MatriculaPagos();
		academicoPago.setCanc("0");// Por defecto pago pendiente
		academicoPago.setMonto(confCuota.getMatricula());
		academicoPago.setTip(com.tesla.colegio.util.Constante.PAGO_MATRICULA);
		//if(matricula.getMat_val().equals("0"))
			list.add(academicoPago);

		ReservaCuota reservaCuota = reservaDAO.getMontoReserva(id_anio, id_alu);
		if(tip!=null){
			if(tip.equals("CL")){
				if (reservaCuota != null && matricula.getId_cli()!=1) {
					/** PAGO POR RESERVA **/
					BigDecimal montoReserva = reservaCuota.getMonto();
					academicoPago = new MatriculaPagos();
					academicoPago.setCanc("1");// PAGO CANCELADO
					academicoPago.setMonto(montoReserva.multiply(new BigDecimal(-1)));
					academicoPago.setTip(com.tesla.colegio.util.Constante.PAGO_RESERVA);
					academicoPago.setNro_rec(reservaCuota.getNro_recibo());
					list.add(academicoPago);
					
					academicoPago.setMontoReserva(montoReserva);
				}
			} else{
				if (reservaCuota != null) {
					/** PAGO POR RESERVA **/
					BigDecimal montoReserva = reservaCuota.getMonto();
					academicoPago = new MatriculaPagos();
					academicoPago.setCanc("1");// PAGO CANCELADO
					academicoPago.setMonto(montoReserva.multiply(new BigDecimal(-1)));
					academicoPago.setTip(com.tesla.colegio.util.Constante.PAGO_RESERVA);
					academicoPago.setNro_rec(reservaCuota.getNro_recibo());
					list.add(academicoPago);
					
					academicoPago.setMontoReserva(montoReserva);
				}
			}
		} else{
			if (reservaCuota != null) {
				/** PAGO POR RESERVA **/
				BigDecimal montoReserva = reservaCuota.getMonto();
				academicoPago = new MatriculaPagos();
				academicoPago.setCanc("1");// PAGO CANCELADO
				academicoPago.setMonto(montoReserva.multiply(new BigDecimal(-1)));
				academicoPago.setTip(com.tesla.colegio.util.Constante.PAGO_RESERVA);
				academicoPago.setNro_rec(reservaCuota.getNro_recibo());
				list.add(academicoPago);
				
				academicoPago.setMontoReserva(montoReserva);
			}
		}
		
		
		
		/** PAGO POR CUOTA DE INGRESO **/
		Matricula mat_anterior = obtenerMatriculaAnterior(id_alu, id_anio);

		
 		if (mat_anterior==null) {//alumno nuevo
 			//Verificamos si la cuota de Ingreso es Unico o Anual
 			String tipo_cuota=confCuota.getTip_cuota_ing();
 			if(tipo_cuota!=null){
 				if(tipo_cuota.equals("A")){
 	 				academicoPago = new MatriculaPagos();
 	 				academicoPago.setCanc("0");// Por defecto pago pendiente
 	 				academicoPago.setMonto(confCuota.getCuota());
 	 				academicoPago.setTip(com.tesla.colegio.util.Constante.PAGO_CUOTA_INGRESO);
 	 				list.add(academicoPago);
 	 			} else if(tipo_cuota.equals("U")){
 	 				//Verificamos si ha antes ha pagado cuota de ingreso
 	 				List<Row> matriculas= matriculaDAO.listaMatriculasCuotaIngreso(id_alu,id_anio);
 	 				if(matriculas.size()==0){
 	 					academicoPago = new MatriculaPagos();
 	 	 				academicoPago.setCanc("0");// Por defecto pago pendiente
 	 	 				academicoPago.setMonto(confCuota.getCuota());
 	 	 				academicoPago.setTip(com.tesla.colegio.util.Constante.PAGO_CUOTA_INGRESO);
 	 	 				list.add(academicoPago);
 	 				} /*else{
 	 					academicoPago = new MatriculaPagos();
 	 	 				academicoPago.setCanc("0");// Por defecto pago pendiente
 	 	 				academicoPago.setMonto(confCuota.getCuota());
 	 	 				academicoPago.setTip(com.tesla.colegio.util.Constante.PAGO_CUOTA_INGRESO);
 	 	 				list.add(academicoPago);
 	 				}*/
 	 			}
 			}
		}

		return list;
	}	

	public Matricula obtenerMatriculaAnterior(Integer id_alu, Integer id_anio) {
		
		Anio anio = anioDAO.get(id_anio);
		int anio_anterior = Integer.parseInt(anio.getNom()) - 1;

		Param param = new Param();
		param.put("nom", anio_anterior);
		Anio anioAnterior = anioDAO.getByParams(param);

		Matricula matricula = matriculaDAO.getMatriculaAnterior(id_alu, anioAnterior.getId());

		return matricula;
		/*
		 * if(matricula == null) return
		 * com.tesla.colegio.util.Constante.CLIENTE_NUEVO; else return
		 * com.tesla.colegio.util.Constante.CLIENTE_ALUMNO;//TODO FALTA
		 * REENTRANTE
		 */
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
	
	private String getFecVencimientoFinMes(int anio, int mes) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); 
		 Calendar calendar = new GregorianCalendar(anio,mes-1,1);
		 Date date = calendar.getTime();
		 Calendar cal_ini = Calendar.getInstance();
		 cal_ini.setTime(date);
		 cal_ini.set(Calendar.DAY_OF_MONTH, 1);
		 cal_ini.set(Calendar.DATE, cal_ini.getActualMaximum(Calendar.DATE));
		 String fecVenc= sdf.format(cal_ini.getTime());

		return fecVenc;

	}
	
	@Transactional
	public Impresion pagarMatricula(PagoMatriculaReq pagoMatriculaReq, Integer id_suc) throws Exception {
		Impresion impresion = null;
		
		Map<String, Object> map = new HashMap<String, Object>();

		Integer gpfa = null;

		if (pagoMatriculaReq.getId_mat() != null) {
			// pago que le corresponde para la matricula
			String nro_rec = facturacionService.getNroRecibo(id_suc);
			//Obtenemos la matricula
			Matricula matricula = matriculaDAO.getByParams(new Param("id",pagoMatriculaReq.getId_mat()));
			Nivel nivel = nivelDAO.get(matricula.getId_niv());
			Grad grado = gradDAO.get(matricula.getId_gra());
								
			//Obtener datos del alumno
			Alumno alumno = alumnoDAO.getByParams(new Param("id",matricula.getId_alu()));
			//Datos de la persona
			Persona persona = personaDAO.get(alumno.getId_per());
			
			//Obtener datos del Aula
			Aula aula = aulaDAO.getFull(matricula.getId_au_asi(), new String[]{Periodo.TABLA,Grad.TABLA, Nivel.TABLA});
			
			ConfCuota confCuota = confCuotaDAO.getByParams(new Param("id_per", matricula.getId_per()));
			
			Periodo periodo = periodoDAO.get(matricula.getId_per());
			Anio anio = anioDAO.get(periodo.getId_anio());
			//Insertamos el movimiento
			Movimiento movimiento = new Movimiento();
			movimiento.setDescuento(new BigDecimal(0));
			movimiento.setEst("A");
			movimiento.setFec(new Date());
			movimiento.setId_mat(pagoMatriculaReq.getId_mat());

			movimiento.setId_fam(matricula.getId_fam());
			movimiento.setId_suc(id_suc); 
			movimiento.setNro_rec(nro_rec);
			movimiento.setTipo(EnumTipoMovimiento.INGRESO.getValue());
			movimiento.setId_fpa(1);// EFECTIVO
 
			movimiento.setObs("MATRICULA " + anio.getNom());
						
			facturacionService.updateNroRecibo(id_suc, nro_rec);

			BigDecimal mov_monto= BigDecimal.ZERO;
			BigDecimal mov_montoTotal = BigDecimal.ZERO;
			BigDecimal mov_descuento= BigDecimal.ZERO;
			BigDecimal montoMatricula = BigDecimal.ZERO;
			

			//List<MatriculaPagos> listPagar = obtenerPagosProgramados(matricula.getId(),matricula.getId_alu(), matricula.getId_au_asi(), periodo.getId_anio(),null);
			List<MatriculaPagos> listPagar = obtenerPagosProgramadosv2(matricula.getId(),matricula.getId_alu(), matricula.getId_per(), periodo.getId_anio(),null);
			for (MatriculaPagos matriculaPagos : listPagar) {
				 
				String tipo = matriculaPagos.getTip();
				
				if (tipo.equals("MAT") || tipo.equals("ING")){

					mov_monto =  mov_monto.add(matriculaPagos.getMonto());
					String descripcionItem = null;
					Integer id_fco = null;//CONCEPTO DE PAGO
					if (tipo.equals("MAT")) {
						montoMatricula = montoMatricula.add(matriculaPagos.getMonto());

						descripcionItem = EnumConceptoPago.MATRICULA.getDescripcion();
						id_fco = EnumConceptoPago.MATRICULA.getValue();
					}
					if (tipo.equals("ING")) {
						descripcionItem = EnumConceptoPago.CUOTA_DE_INGRESO.getDescripcion();
						id_fco = EnumConceptoPago.CUOTA_DE_INGRESO.getValue();
					}
					/*descripcionItem = descripcionItem + " " + anio.getNom() + ", ALUMNO:" + persona.getApe_pat() + " "
							+ persona.getApe_mat() + ", " + persona.getNom() + " NIVEL:" + aula.getGrad().getNivel().getNom()
							+ " GRADO:" + aula.getGrad().getNom() + " " + aula.getSecc() + " CONTRATO:" + matricula.getNum_cont();*/
					descripcionItem = descripcionItem + " " + anio.getNom() + ", ALUMNO:" + persona.getApe_pat() + " "
							+ persona.getApe_mat() + ", " + persona.getNom() + " NIVEL:" + nivel.getNom()
							+ " GRADO:" + grado.getNom() +  " CONTRATO:" + matricula.getNum_cont();
					 
					MovimientoDetalle movimientoDetalle = new MovimientoDetalle();
					movimientoDetalle.setDescuento(new BigDecimal(0));
					movimientoDetalle.setEst("A");
					movimientoDetalle.setId_fco(id_fco);
 					movimientoDetalle.setMonto(confCuota.getMatricula());
					movimientoDetalle.setMonto_total(confCuota.getMatricula());
					movimientoDetalle.setObs(descripcionItem);
					
					if (movimiento.getMovimientoDetalles()==null)
						movimiento.setMovimientoDetalle(new ArrayList<MovimientoDetalle>());
					movimiento.getMovimientoDetalles().add(movimientoDetalle);
					
				}else if (tipo.equals("RES")){
					montoMatricula = montoMatricula.add(matriculaPagos.getMonto());

					//esto es un descuento a la matricula
				
					String descripcion = "DESCUENTO POR RESERVA";
					descripcion += "\nALUMNO " + alumno.getApe_pat() + " " + alumno.getNom() + "-" + aula.getGrad().getNom() + "-"
							+  aula.getSecc()  + " " +  aula.getGrad().getNivel().getNom();
					
					MovimientoDetalle movimientoDetalleMAt =  null;
					for (MovimientoDetalle movimientoDetalle1 :movimiento.getMovimientoDetalles()) {
						if (movimientoDetalle1.getId_fco().intValue() == EnumConceptoPago.MATRICULA.getValue() )
							movimientoDetalleMAt = movimientoDetalle1;
					}

					 MovimientoDescuento movimientoDescuento = new MovimientoDescuento();
					 
					 movimientoDescuento.setDes(descripcion);
					 movimientoDescuento.setDescuento(matriculaPagos.getMonto().multiply(new BigDecimal(-1)));
					 movimientoDescuento.setEst("A");
					// mov_descuento = mov_descuento.add(augend)
					mov_descuento = mov_descuento.add(movimientoDescuento.getDescuento());

					 
					 movimientoDetalleMAt.getDescuentos().add(movimientoDescuento);
					 movimientoDetalleMAt.setDescuento(mov_descuento);
					 movimientoDetalleMAt.setMonto_total(movimientoDetalleMAt.getMonto().subtract(mov_descuento));
				}
				
					 
			}
			mov_montoTotal= mov_monto.subtract(mov_descuento);
			movimiento.setMonto(mov_monto);
			movimiento.setDescuento(mov_descuento);
			movimiento.setMonto_total(mov_montoTotal);
			
			//Buscamos el pago de matricula 
			Row pago_matricula = matriculaDAO.getPagoMatricula(pagoMatriculaReq.getId_mat());
			if(pago_matricula.size()>0){
				AcademicoPago academicoPago = new AcademicoPago();
				academicoPago.setCanc("1");
				academicoPago.setNro_rec(nro_rec);
				academicoPago.setFec_pago(new Date());
				academicoPago.setMontoTotal(pago_matricula.getBigDecimal("monto"));
				academicoPago.setId(pago_matricula.getInteger("id"));
				matriculaDAO.updatePagoMatriculaIngreso(academicoPago);
				//Actualizamos el estado de la matricula a validado
				matriculaDAO.updateEstadoMatricula(pagoMatriculaReq.getId_mat());
			}
			
			//Buscams la cuota de Ingreso
			Row pago_cuota_ingreso= matriculaDAO.getPagoCuotaIngreso(pagoMatriculaReq.getId_mat());
			if(pago_cuota_ingreso!=null){
				AcademicoPago academicoPago = new AcademicoPago();
				academicoPago.setCanc("1");
				academicoPago.setNro_rec(nro_rec);
				academicoPago.setFec_pago(new Date());
				academicoPago.setMontoTotal(pago_cuota_ingreso.getBigDecimal("monto"));
				academicoPago.setId(pago_cuota_ingreso.getInteger("id"));
				matriculaDAO.updatePagoMatriculaIngreso(academicoPago);
			}

			
			// ES UN PAGO DE UN ALUMNO MATRICULADO

			Integer id_fmo = movimientoDAO.saveOrUpdate(movimiento);
			for (MovimientoDetalle movimientoDetalle : movimiento.getMovimientoDetalles()) {
				movimientoDetalle.setId_fmo(id_fmo);
				Integer id_fmd = movimientoDetalleDAO.saveOrUpdate(movimientoDetalle);

				if (movimientoDetalle.getDescuentos().size()>0){
			
					for (MovimientoDescuento descuento : movimientoDetalle.getDescuentos() ) {
						descuento.setId_fmd(id_fmd);
						movimientoDescuentoDAO.saveOrUpdate(descuento);
						
					}
				}
				
			}
			 impresion = facturacionService.getImpresion(nro_rec,periodo.getId_anio(),matricula.getId_alu());

		}

		return impresion;
	}
}
