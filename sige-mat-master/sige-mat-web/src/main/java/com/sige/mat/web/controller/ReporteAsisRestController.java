package com.sige.mat.web.controller;


import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.sige.mat.dao.LecturaBarrasDAO;
import com.tesla.colegio.model.LecturaBarras;
import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.rest.util.Views;
import com.tesla.frmk.util.FechaUtil;


@RestController
@RequestMapping(value = "/api/reporteAsistencia")
public class ReporteAsisRestController {


	@Autowired
	private LecturaBarrasDAO lecturaBarrasDAO;
	
		
	//@JsonView(Views.Public.class)

	
	@RequestMapping(value = "/reporte",  method = RequestMethod.GET)
	public AjaxResponseBody reporteAsistencia(Integer id_suc, Integer id_niv, Integer id_gra,  Integer id_au, String fec , String asistencia, Integer id_anio, Integer id_tra, Integer id_cic) {
		AjaxResponseBody result = new AjaxResponseBody();
		try{
			
			Date fec_inicial= FechaUtil.toDate(fec);

			result.setResult( lecturaBarrasDAO.listAsistencia(id_suc,id_anio, id_niv,id_gra,id_au, fec_inicial, asistencia, id_tra, id_cic));
			return result;
		}  catch (Exception e) {
			e.printStackTrace();
			return result;
		}

	}
	
	@RequestMapping(value = "/reporteEstadisticaAsistencia",  method = RequestMethod.GET)
	public AjaxResponseBody reporteEstadisticaAsistencia(Integer id_suc, Integer id_anio, Integer id_niv,Integer id_gir, String fec_ini, String fec_fin) {
		AjaxResponseBody result = new AjaxResponseBody();
		try{
			
			Date fec_inicial= FechaUtil.toDate(fec_ini);
			Date fec_final= FechaUtil.toDate(fec_fin);

			result.setResult( lecturaBarrasDAO.listEstadisticaAsistencia(id_suc, id_anio, id_niv,id_gir,fec_inicial, fec_final));
			return result;
		}  catch (Exception e) {
			e.printStackTrace();
			return result;
		}

	}
	
	@RequestMapping(value = "/asistenciaAlumno",  method = RequestMethod.GET)
	public AjaxResponseBody asistenciaAlumno(Integer id_alu, String fec_ini, String fec_fin,String asistencia) {
		AjaxResponseBody result = new AjaxResponseBody();
		try{
			
			Date fec_inicial= FechaUtil.toDate(fec_ini);
			Date fec_final= FechaUtil.toDate(fec_fin);

			result.setResult( lecturaBarrasDAO.listAsistenciaxAlumno(fec_inicial, fec_final, asistencia, id_alu));
			return result;
		}  catch (Exception e) {
			e.printStackTrace();
			return result;
		}

	}
	
	@RequestMapping(value = "/ranqueoAsistencia",  method = RequestMethod.GET)
	public AjaxResponseBody ranqueoAsistencia(Integer id_suc, Integer id_niv, Integer id_au, String fec_ini, String fec_fin,String asistencia, Integer id_anio) {
		AjaxResponseBody result = new AjaxResponseBody();
		try{
			
			Date fec_inicial= FechaUtil.toDate(fec_ini);
			Date fec_final= FechaUtil.toDate(fec_fin);

			result.setResult( lecturaBarrasDAO.ranqueoAsistencia(fec_inicial, fec_final, asistencia, id_anio, id_suc, id_niv, id_au));
			return result;
		}  catch (Exception e) {
			e.printStackTrace();
			return result;
		}

	}
	
	@RequestMapping( value="/detalle/{id_asi}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id_asi ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( lecturaBarrasDAO.get(id_asi));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(LecturaBarras lecturaBarras, String fecha_cor) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			
			//lecturaBarras.setFecha( new java.sql.Timestamp(formatter_date.parse(fecha_ori).getTime()));
			//aqui falta para q puedan actualizar solo del dia en caso de tutores o secretaria, pero coordinador de cualquier dia
			/*if(new Date().equals(fecha_ori) && perfil='tutores'){
				
			}*/
			//if(lecturaBarras.getId()==null){
				//result.setResult(lecturaBarrasDAO.save(lecturaBarras));	
			//} else{
			//result.setResult( lecturaBarrasDAO.update(lecturaBarras) ); //}
			if(lecturaBarras.getId()==null){
				String formato = "h:m:s a M/dd/yy";
				SimpleDateFormat formatter = new SimpleDateFormat(formato);
				String formato2 = "dd/MM/yy";
				SimpleDateFormat formatter2 = new SimpleDateFormat(formato2);
				Date fecha = formatter2.parse(fecha_cor);
				SimpleDateFormat sdf = new SimpleDateFormat(formato2);
				sdf.applyPattern(formato); 
				String fecha_ori = sdf.format(fecha);

				
				//String fecha_ori=lecturaBarras.getFecha_ori();
				lecturaBarras.setFecha_ori(fecha_ori);
				lecturaBarras.setFecha( new java.sql.Timestamp(formatter2.parse(fecha_cor).getTime()));
				//lecturaBarras.setFecha_ori(formatter.parse(fecha_cor).toString());
				//lecturaBarras.setFecha( new java.sql.Timestamp(formatter.parse(fecha_cor).getTime()));
				//lecturaBarras.setFecha( new java.sql.Timestamp(formatter.parse(fecha_cor).getTime()));
				result.setResult(lecturaBarrasDAO.save(lecturaBarras));	
			} else{
				result.setResult( lecturaBarrasDAO.update(lecturaBarras) );
			}	
			
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	
}
