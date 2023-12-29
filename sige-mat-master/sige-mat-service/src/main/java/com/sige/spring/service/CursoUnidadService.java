package com.sige.spring.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sige.mat.dao.CursoAnioDAO;
import com.sige.mat.dao.CursoUnidadDAO;
import com.sige.mat.dao.TrabajadorDAO;
import com.sige.mat.dao.UniSubDAO;
import com.tesla.colegio.model.CursoUnidad;
import com.tesla.colegio.model.Trabajador;
import com.tesla.colegio.util.Constante;
import com.tesla.frmk.common.exceptions.ServiceException;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row; 

@Service
public class CursoUnidadService {

	@Autowired
	private CursoUnidadDAO cursoUnidadDAO;

	@Autowired
	private TrabajadorDAO trabajadorDAO;
	
	@Autowired
	private CursoAnioDAO cursoAnioDAO;

	@Autowired
	private UniSubDAO uniSubDAO;
	
	public Row obtenerCabecera(Integer id_anio, Integer id_tra, Integer id) throws ServiceException {
		Row cabecera = new Row();
		CursoUnidad cursoUnidad = cursoUnidadDAO.getFull(id, new String[] { "cat_grad", "cat_nivel", "cat_curso" });
		Trabajador trabajador = trabajadorDAO.get(id_tra);

		 
		cabecera.put("nivel", cursoUnidad.getNivel().getNom());
		cabecera.put("grado", cursoUnidad.getGrad().getNom());
		cabecera.put("curso", cursoUnidad.getCurso().getNom().toUpperCase());
		cabecera.put("trabajador",
				trabajador.getApe_pat() + " " + trabajador.getApe_mat() + ", " + trabajador.getNom());
		cabecera.put("titulo", cursoUnidad.getNom().toUpperCase());
		cabecera.put("numero", cursoUnidad.getNum());
		
		//area
		Row area= cursoAnioDAO.getAreaxCurso(id_anio, cursoUnidad.getCurso().getId(), cursoUnidad.getGrad().getId(), cursoUnidad.getNivel().getId());
		cabecera.put("area", area.getString("area").toUpperCase());
		Date date = new Date(); // your date
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("America/Lima"));
		cal.setTime(date);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		String fecha = day + " de " + Constante.MES[month] + " de " + year;
		cabecera.put("fecha", fecha);
		return cabecera;

	}

	/**
	 * Obtiene las competencias, dentreo de tes las capacidaades y dentro de este los indicadores, para el PDF UNIDAD DE APRENDIZAJE
	 * @param id_anio
	 * @param id
	 * @param id_ccu id de col_curso_unidad
	 * @return
	 * @throws ServiceException
	 */
	public List<Row>  obtenerCompetencias(Integer id_anio, Integer id_uni) throws ServiceException {
		 
		List<Row> competencias = cursoUnidadDAO.listaCompetenciasxCursoUnidad(id_anio, id_uni);

		int c = 0;
		 for (Row row : competencias) {
			 Integer id_com =row.getInteger("id"); 
			 row.put("nro", ++c);
			 row.put("id", id_com);
			 row.put("competencia", row.getString("competencia"));
			 
			 Param param = new Param();
			 param.put("id_com", row.getInteger("id"));
			 List<Row> capacidades = cursoUnidadDAO.listaCapacidadesxCursoUnidad(id_anio, id_uni, id_com);
			
			 int nro_indicadores=0;
			 for (Row capacidad : capacidades) {
				 

				 Integer id_cap = capacidad.getInteger("id");
				 List<Row> indicadores = cursoUnidadDAO.listaIndicadoresXCapacidad(id_uni, id_cap);
				 
				 if(indicadores.size()<2){
					 capacidad.put("nro_indicadores", 1);
					 nro_indicadores ++;
				 }else{
					 capacidad.put("nro_indicadores", indicadores.size());
					 nro_indicadores += indicadores.size();
				 }
				 
				 capacidad.put("indicadores", indicadores);
				 
			}
			 row.put("nro_indicadores", nro_indicadores);
			 
			 row.put("capacidades", capacidades);
			//aca se tiene q obtener los subtemas x cada competencia
		}
		 
		return competencias;
	}
	
	

	/**
	 * para el PDF UNIDAD DE APRENDIZAJE
	 * @param id_anio
	 * @param id_uni
	 * @return
	 * @throws ServiceException
	 */
	public List<Row>  obtenerCamposTematicos(Integer id_anio,  Integer id_uni) throws ServiceException {
	 
		CursoUnidad cursoUnidad = cursoUnidadDAO.get(id_uni);
		
		List<Row> temas = 	uniSubDAO.listaTemasSubtemas(id_anio, cursoUnidad.getId_cur(), cursoUnidad.getId_niv(), cursoUnidad.getId_gra(),  id_uni);

		int c = 0;
		 for (Row row : temas) {
			 Integer id_tem =row.getInteger("id"); 
			 row.put("nro", ++c);
			 row.put("id", id_tem);
			 //row.put("tema", row.getString("tema"));
			  
			 @SuppressWarnings("unchecked")
			List<Row> subtemas =(List<Row>)row.get("subtemas");
			
			 int nro_indicadores=0;
			 for (Row subtema : subtemas) {

				 Integer id_sub = subtema.getInteger("id");
				 List<Row> indicadores = cursoUnidadDAO.listaIndicadoresxSubtemaUnidad(id_uni, id_sub);
				 
				 if(indicadores.size()<2){
					 subtema.put("nro_indicadores", 1);
					 nro_indicadores ++;
				 }else{
					 subtema.put("nro_indicadores", indicadores.size());
					 nro_indicadores += indicadores.size();
				 }
				 
				 subtema.put("indicadores", indicadores);
				 
			}
			 row.put("nro_indicadores", nro_indicadores);
			 
			 //row.put("subtemas", subtemas);
			//aca se tiene q obtener los subtemas x cada competencia
		}
		 
		return temas;
	}
	
	/**
	 * Obtener las sesiones para el pdf UNIDAD DE APRENDIZAJE
	 * @param id_anio
	 * @param id_uni
	 * @return
	 * @throws ServiceException
	 */
	/*public List<Row>  obtenerSesiones(Integer id_anio, Integer id_uni) throws ServiceException {
	 
		List<Row> sesiones = cursoUnidadDAO.listaSesionesxUnidad(id_uni);

		 for (Row row : sesiones) {
			 Integer id_ses =row.getInteger("id_ses"); 
			 row.put("nro", row.getInteger("nro")); 
			 row.put("id", id_ses);
			 row.put("tipo", row.getString("tipo"));

			 // indicadores por sesion
			 List<Row> indicadores = cursoUnidadDAO.listaIndicadoresxSesion(id_ses);
			 row.put("indicadores", indicadores);

			 //temas-subtemas por sesion
			 List<Row> temas= cursoUnidadDAO.listaSubtemasxSesion(id_ses);
			 row.put("temas", temas);
		}
		 
		return sesiones;
	}*/
	
}
