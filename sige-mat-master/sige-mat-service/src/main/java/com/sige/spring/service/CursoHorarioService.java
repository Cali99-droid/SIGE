package com.sige.spring.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sige.common.enums.EnumTipoSesion;
import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.ConfSemanasDAO;
import com.sige.mat.dao.CursoHorarioDAO;
import com.sige.mat.dao.CursoHorarioPadDAO;
import com.sige.mat.dao.CursoHorarioSesDAO;
import com.sige.mat.dao.EvaluacionDAO;
import com.sige.mat.dao.SesionTipoDAO;
import com.sige.rest.request.SemanaRq;
import com.tesla.colegio.model.ConfSemanas;
import com.tesla.colegio.model.CursoHorario;
import com.tesla.colegio.model.CursoHorarioPad;
import com.tesla.colegio.model.CursoHorarioSes;
import com.tesla.colegio.model.Evaluacion;
import com.tesla.colegio.model.SesionTipo;
import com.tesla.frmk.common.exceptions.ServiceException;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;
import com.tesla.frmk.util.FechaUtil;


@Service
public class CursoHorarioService {
	
	@Autowired
	private CursoHorarioDAO curso_horarioDAO;
	
	@Autowired
	private ConfSemanasDAO confSemanasDAO;
	
	@Autowired
	private CursoHorarioSesDAO curso_horario_sesDAO;
	
	@Autowired
	private SesionTipoDAO sesionTipoDAO;
	
	@Autowired
	private EvaluacionDAO evaluacionDAO;

	@Autowired
	private CursoHorarioPadDAO cursoHorarioPadDAO;
	
	@Autowired
	private SQLUtil sqlUtil;
	
	/**
	 * Grabar Curso Horario por el coordinador general
	 * @param semanaRq
	 * @return
	 */
	@Transactional
	public List<Row> saveOrUpdate(SemanaRq semanaRq) throws ServiceException{
		
		for (int i = 0; i < semanaRq.getHorarios().length; i++) {
			CursoHorario curso_horario = new CursoHorario();
			curso_horario.setId(semanaRq.getHorarios()[i].getId());
			curso_horario.setId_anio(semanaRq.getId_anio());
			curso_horario.setId_cchp(semanaRq.getId_cchp());
			curso_horario.setDia(semanaRq.getHorarios()[i].getDia());
			curso_horario.setId_cca(semanaRq.getHorarios()[i].getId_cca());
			curso_horario.setHora_ini(getHoraBD(semanaRq.getHorarios()[i].getHora_dia()));
			curso_horario.setHora_fin(getHoraBD(semanaRq.getHorarios()[i].getHora_fin()));
			curso_horario.setEst("A");
			
			//ajustar los valores de las horas y el dia
			//dia de la semana ()1er lunes del anio vigente
			
			curso_horarioDAO.saveOrUpdate(curso_horario); 
		}
		
		Integer anio_act = semanaRq.getId_anio();
		return listarHorariosxAula(semanaRq.getId_au(),semanaRq.getId_cchp(), anio_act);

	}
	 

	/**
	 * Listar Sesiones
	 * @param id_cua
	 * @return
	 * @throws ServiceException
	 */
	public List<Row> listarHorariosxAula(Integer id_au, Integer anio_act) throws ServiceException{
		List<Row> horarios=curso_horarioDAO.listarHorariosxAula(id_au, anio_act);
		return horarios;
	}
	
	
	/**TODO col_curso_horario_pad
	 * Listar horararios registrados por aula y horario (idcchp)
	 * @param id_cua
	 * @return
	 * @throws ServiceException
	 */
	public List<Row> listarHorariosxAula(Integer id_au, Integer id_cchp, Integer anio_act) throws ServiceException{
		List<Row> horarios=curso_horarioDAO.listarHorariosxAula(id_au, id_cchp, anio_act);
		return horarios;
	}
	
	/**
	 * Lista los horarios del docente por semana, local y nivel
	 * @param id_tra
	 * @param id_suc
	 * @param id_niv
	 * @return
	 * @throws ServiceException
	 */
	public Row listarHorariosxSemana(Integer id_tra, Integer id_suc, Integer id_niv,Integer id_sem) throws ServiceException{
		
		ConfSemanas semanas = confSemanasDAO.get(id_sem);
		Date fecIni = semanas.getFec_ini();
		String aaaamm = FechaUtil.toString(fecIni, "yyyy-MM"); 
		String dd = FechaUtil.toString(fecIni, "dd");

		Calendar cal = Calendar.getInstance();
        cal.setTime(fecIni);
  
        Integer primer_dia = cal.get(Calendar.DAY_OF_WEEK) -1;
        
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(fecIni);
        cal1.add(Calendar.MONTH, 1);
        cal1.set(Calendar.DAY_OF_MONTH, 1);
        Integer primer_dia_mes_siguiente=cal1.get(Calendar.DAY_OF_WEEK) -1;
        
        //Obtener el ultimo dia del mes
        Calendar cal2= Calendar.getInstance();
        cal2.setTime(fecIni);
        cal2.add(Calendar.MONTH, 1);  
        String anio_mes_sig= FechaUtil.toString( cal2.getTime(), "yyyy-MM");
        String aaaamm_siguiente= FechaUtil.toString( cal2.getTime(), "yyyyMM");
        
        cal2.set(Calendar.DAY_OF_MONTH, 1);  
        cal2.add(Calendar.DATE, -1);  
        //System.out.println(cal2.getTime());
        Integer ultimo_dia_mes_actual =cal2.get(Calendar.DAY_OF_MONTH);

               
        Row result = new Row();
		result.put("fecha_inicio", aaaamm + "-" + dd);
		result.put("primer_dia", primer_dia);
		
	 
        if(primer_dia>1){
        	//LA SEMANA NO INICIA EL LUNES
            cal.add(Calendar.DATE, -1* (primer_dia - 1)); //minus number would decrement the days
            dd = FechaUtil.toString(cal.getTime(), "dd");
        }
        
       // String aaaamm_siguiente = 
		
		List<Row> horarios=curso_horarioDAO.listarHorariosxSemana(aaaamm,anio_mes_sig, dd,id_tra, id_suc, id_niv, id_sem,primer_dia,primer_dia_mes_siguiente,ultimo_dia_mes_actual);
		result.put("horarios", horarios);
		
		
		return result;
	}
	
	/**
	 * Lista de sesiones por curso_horario y semana
	 * @param id_cch
	 * @param id_sem
	 * @return
	 */
	public List<Row> listarSessiones(Integer id_cch, Integer id_sem){
		
		return curso_horarioDAO.listarSesiones(id_cch, id_sem);
	}
	

	@Transactional
	public Integer grabar(CursoHorarioSes curso_horario_ses) {

		//revisar si ya existe
		Param param = new Param();
		param.put("id_cch", curso_horario_ses.getId_cch());
		param.put("id_ccs", curso_horario_ses.getId_ccs());
		
		Integer id_cca=curso_horarioDAO.getByParams(new Param("id",curso_horario_ses.getId_cch())).getId_cca();
		
		CursoHorarioSes cuHorarioSes = curso_horario_sesDAO.getByParams(param);
		Param param2 = new Param();
		param2.put("id_uns", curso_horario_ses.getId_uns());
		param2.put("id_cts", EnumTipoSesion.EXAMEN.getValue());
		SesionTipo sesion_tipo = sesionTipoDAO.getByParams(param2);
		if(sesion_tipo!=null){
			Integer id_ses=sesion_tipo.getId();
			evaluacionDAO.upFecEva(id_ses, curso_horario_ses.getFec(),curso_horario_ses.getFec(), id_cca);	
		}
				
		 if(cuHorarioSes!=null){
			 //actualizar
			 cuHorarioSes.setEst(curso_horario_ses.getEst());
			 cuHorarioSes.setFec(curso_horario_ses.getFec());
			 cuHorarioSes.setId_uns(curso_horario_ses.getId_uns());
			 
			 return curso_horario_sesDAO.saveOrUpdate(cuHorarioSes) ;
		 }else 
			 return curso_horario_sesDAO.saveOrUpdate(curso_horario_ses) ;
	}
	
	
	@Transactional
	public void eliminar(Integer id_cchs,Integer id_cca){

		//eliminar el horario sesion
		CursoHorarioSes cursoHorario =curso_horario_sesDAO.get(id_cchs); 
		curso_horario_sesDAO.delete(id_cchs);
		
		//actualizar la fecha del examen en nota_evaluacion en caso existiese
		Param param = new Param();
		param.put("id_uns", cursoHorario.getId_uns());
		param.put("id_cts", EnumTipoSesion.EXAMEN.getValue());
		
		SesionTipo sesionTipo = sesionTipoDAO.getByParams(param);
		if(sesionTipo!=null){
			//SIGNIFICA QUE TIENE EVALUACION
			param = new Param();
			param.put("id_cca", id_cca);
			param.put("id_ses", sesionTipo.getId());
			Evaluacion evaluacion = evaluacionDAO.getByParams(param) ;
			
			if(evaluacion!=null){
				evaluacion.setFec_ini(null);
				evaluacion.setFec_fin(null);
				evaluacionDAO.saveOrUpdate(evaluacion);
			}
			
			
		}
		//SELECT * FROM not_evaluacion WHERE id_ses=192 AND id_cca=1252; -- limpiar fec_ini y fec_fin 
	}
	
	
	private String getHoraBD(String hm){
		String arr[] = hm.split(":");
		String minutos = pad(arr[1]);
		String horas = pad( String.valueOf(Integer.parseInt(arr[0]) +  5)); //GTM-5
		
		return horas + ":" + minutos;
	} 
	
	//strings
	private String pad(String cadena) {
		return "00".substring(0, "00".length() - cadena.length()) + cadena;
	}

	/**
	 * El aula tiene un nuevo horario a partir de una fecha
	 * @return
	 */
	@Transactional
	public Integer nuevoHorarioPadre(CursoHorarioPad cursoHorarioPad) throws ServiceException{
		
		//actualizar horario actual
		String sql = "select * from col_curso_horario_pad where fec_ini_vig<? and id_anio=? and id_au=? order by fec_ini_vig desc";
		
		List<CursoHorarioPad> listaHorarioAnterior = sqlUtil.query(sql, new Object[]{cursoHorarioPad.getFec_ini_vig(), cursoHorarioPad.getId_anio(),cursoHorarioPad.getId_au() }, CursoHorarioPad.class);

		if(listaHorarioAnterior.isEmpty())
			throw new ServiceException("No se puede agregar un horario anterior a la fecha de vigencia que ya existe");
		
		//actualizar la fecha de vigencia a -1
		CursoHorarioPad cursoHorarioPadAnt  =  listaHorarioAnterior.get(0);
		Calendar calFechaAnterior = Calendar.getInstance();
		calFechaAnterior.setTime(cursoHorarioPad.getFec_ini_vig());
		calFechaAnterior.add(Calendar.DAY_OF_MONTH, -1);
		cursoHorarioPadAnt.setFec_fin_vig(calFechaAnterior.getTime());
		cursoHorarioPadDAO.saveOrUpdate(cursoHorarioPadAnt);
		
		//nuevo horario
		return cursoHorarioPadDAO.saveOrUpdate(cursoHorarioPad);
	}
}
