package com.sige.spring.service;

import java.util.Date;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sige.common.enums.EnumSituacionFinal;
import com.sige.mat.dao.AnioDAO;
import com.sige.mat.dao.AulaDAO;
import com.sige.mat.dao.CicloDAO;
import com.sige.mat.dao.ConfFechasDAO;
import com.sige.mat.dao.CronogramaDAO;
import com.sige.mat.dao.MatriculaDAO;
import com.sige.mat.dao.PeriodoDAO;
import com.sige.mat.dao.ReglasNegocioDAO;
import com.sige.mat.dao.ReservaDAO;
import com.sige.mat.dao.SeccionSugeridaDAO;
import com.sige.mat.dao.VacanteDAO;
import com.tesla.colegio.model.Anio;
import com.tesla.colegio.model.Aula;
import com.tesla.colegio.model.Ciclo;
import com.tesla.colegio.model.ConfFechas;
import com.tesla.colegio.model.Cronograma;
import com.tesla.colegio.model.Matricula;
import com.tesla.colegio.model.Periodo;
import com.tesla.colegio.model.ReglasNegocio;
import com.tesla.colegio.model.Reserva;
import com.tesla.colegio.model.SeccionSugerida;
import com.tesla.colegio.model.bean.AulaCapacidad;
import com.tesla.colegio.util.Constante;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;

@Service
public class VacanteService {

	Logger logger;
	
	@Autowired
	private VacanteDAO vacanteDAO;
	
	@Autowired
	private AulaDAO aulaDAO;
	
	@Autowired
	private AnioDAO anioDAO;
	
	@Autowired
	private CronogramaDAO cronogramaDAO;
	
	@Autowired
	private PeriodoDAO periodoDAO;
	
	@Autowired
	private ConfFechasDAO confFechasDAO;
	
	@Autowired
	private SeccionSugeridaDAO seccionSugeridaDAO;
	
	@Autowired
	private MatriculaDAO matriculaDAO;
	
	@Autowired
	private CicloDAO cicloDAO;
	
	@Autowired
	private ReglasNegocioDAO reglasNegocioDAO;

	@Autowired
	private ReservaDAO reservaDAO;
	
	/**
	 * Capacidad del grado
	 * @param id_au
	 * @return
	 */
	public Integer getCapacidadxGrado(Integer anio, Integer id_grad, Integer id_suc){
		Integer capacidad=vacanteDAO.capacidadGrado(anio, id_grad, id_suc);
		//logger.debug("getCapacidadxGrado:" + zzz);
		return capacidad;
	}
	
	public Integer getCapacidadxGradoCiclo(Integer id_cic, Integer id_grad){
		Integer capacidad=vacanteDAO.capacidadGradoCiclo(id_cic, id_grad);
		//logger.debug("getCapacidadxGrado:" + zzz);
		return capacidad;
	}
	
	
	public Integer getCapacidadxGradoCicloyModalidad(Integer id_cic, Integer id_grad, Integer id_cme){
		Integer capacidad=vacanteDAO.capacidadGradoCicloyModalidad(id_cic, id_grad, id_cme);
		//logger.debug("getCapacidadxGrado:" + zzz);
		return capacidad;
	}
	
	/**
	 * Capacidad fisica del aula 
	 * @param id_au
	 * @return
	 */
	public Integer getCapacidadxAula(Integer id_au){
		Integer capacidad=aulaDAO.getByParams(new Param("id",id_au)).getCap();
		return capacidad;
	}
	

	/**
	 * Alumnos matriculados por aula que no tengan situacion
	 * @param id_au
	 * @return
	 */
	public Integer getAlumnosMatriculadosxAula(Integer id_au){
		Integer matriculados=vacanteDAO.getTotalMatriculadosAula(id_au);
		return matriculados;
	}

	/**
	 * Alumnos matriculados por grado
	 * @param id_au
	 * @return
	 */
	public Integer getAlumnosMatriculadosxGrado(Integer id_anio, Integer id_gra, Integer id_suc){
		Integer matriculados = vacanteDAO.getTotalMatriculadosGrado(id_anio, id_gra, id_suc);
		return matriculados;
	}
	
	/**
	 * Alumnos desistieron por grado
	 * @param id_au
	 * @return
	 */
	public Integer getAlumnosNoRatificaronxGrado(Integer id_anio_ant,Integer id_anio_act, Integer id_gra, Integer id_suc){
		Integer no_ratificaron = vacanteDAO.getTotalNoRatificaronxGrado(id_anio_ant, id_anio_act, id_gra, id_suc);
		return no_ratificaron;
	}
	

	/**
	 * Total de matriculados que no esten matriculados en el año actual
	 * @param id_au
	 * @return
	 */
	public Integer getTotalMatriculasAnteriosNoMatriculados(Integer id_anio, Integer id_gra, Integer id_suc, Integer id_anio_act){
		Integer matriculados = vacanteDAO.getTotalMatriculasAnteriosNoMatriculados(id_anio, id_gra, id_suc, id_anio_act);
		return matriculados;
	}
	
	/**
	 * Total de alumnos que no ratificaron su matricula x grado y  local
	 * @param id_anio_ant
	 * @param id_anio_act
	 * @param id_gra
	 * @param id_suc
	 * @return
	 */
	public Integer getTotalNoRatificaronxGrado(Integer id_anio_ant, Integer id_anio_act, Integer id_gra, Integer id_suc) {
		Integer no_ratificaron = vacanteDAO.getTotalNoRatificaronxGrado(id_anio_ant, id_anio_act, id_gra, id_suc);
		return no_ratificaron;
	}
	
	

	/**
	 * Total de matriculados que no esten matriculados en el año actual son anteriores y no estan sugeridos
	 * @param id_au
	 * @return
	 */
	public Integer getTotalMatriculasAnteriosNoMatriculadosNosugeridos(Integer id_anio, Integer id_gra, Integer id_suc, Integer id_anio_act){
		Integer matriculados = vacanteDAO.getTotalMatriculasAnteriosNoMatriculadosNosugeridos(id_anio, id_gra, id_suc, id_anio_act);
		return matriculados;
	}
	
	
	/**
	 * Alumnos matriculados por grado
	 * @param id_au
	 * @return
	 */
	public Integer getAlumnosMatriculadosxGradoyModalidad(Integer id_anio, Integer id_gra, Integer id_suc, Integer id_cme){
		Integer matriculados = vacanteDAO.getTotalMatriculadosGradoyModalidad(id_anio, id_gra, id_suc, id_cme);
		return matriculados;
	}
	
	/**
	 * Total de desaprobados
	 * @param id_anio
	 * @param id_gra
	 * @param id_suc
	 * @return
	 */
	public Integer getDesaprobados(Integer id_anio, Integer id_gra, Integer id_suc){
		Integer desaprobados = vacanteDAO.getTotalDesaprobados(id_anio, id_gra, id_suc);
		return desaprobados;
	}
	
	/**
	 * Desaprobados por Aula
	 * @param id_au
	 * @return
	 */
	public Integer getDesaprobadosxAula(Integer id_au){
		Integer desaprobados = vacanteDAO.getTotalDesaprobadosxAula(id_au);
		return desaprobados;
	}
	
	/**
	 * Desaprobados por grado, seccion y anio
	 * @param id_grad
	 * @param secc
	 * @param id_anio
	 * @return
	 */
	public Integer getDesaprobadosxGradoySecc(Integer id_grad, String secc, Integer id_anio){
		Integer desaprobados = vacanteDAO.getTotalDesaprobadosxGradoSecc(id_grad, secc, id_anio);
		return desaprobados;
	}
	
	/**
	 * Desaprobados por grado, seccion y anio
	 * @param id_grad
	 * @param secc
	 * @param id_anio
	 * @return
	 */
	public Integer getDesaprobadosxGrado(Integer id_grad, Integer id_anio){
		Integer desaprobados = vacanteDAO.getTotalDesaprobadosxGrado(id_grad, id_anio);
		return desaprobados;
	}
	/**
	 * Alumnos matriculados por aula
	 * @param id_au
	 * @return
	 */
	private Integer getAlumnosMatriculadosxAulaSituacion(Integer id_au, List<Integer> situacion){
		//sql = 'select * from tabla where alu.id_sit in (:situacion)';
		//sqlUtil.query(sql, param);
		return 0;
	}
	
	/**
	 * Cantidad de Reservas por Grado
	 * @param id_per
	 * @param id_gra
	 * @return
	 */
	public Integer getReservasxGrado(Integer id_per, Integer id_gra){
		Integer reservas = vacanteDAO.getReservasGrado(id_per, id_gra);
		return reservas;
	}
	
	
	/**
	 * Cantidad de Reservas por Grado
	 * @param id_per
	 * @param id_gra
	 * @return
	 */
	public Integer getReservasxGradoLocal(Integer id_anio, Integer id_gra, Integer id_suc){
		Integer reservas = vacanteDAO.getReservasGradoxLocal(id_anio, id_gra, id_suc);
		return reservas;
	}
	
	/**
	 * Cantidad de Reservas por Grado
	 * @param id_per
	 * @param id_gra
	 * @return
	 */
	public Integer getReservasxGradoyModalidad(Integer id_per, Integer id_gra, Integer id_cme){
		Integer reservas = vacanteDAO.getReservasGradoyModalidad(id_per, id_gra, id_cme);
		return reservas;
	}
	
	/**
	 * Reservas por Aula
	 * @param id_au
	 * @return
	 */
	public Integer getReservasxAula(Integer id_au, Integer id_anio){
		Integer reservas = vacanteDAO.getReservasAula(id_au, id_anio);
		return reservas;
	}
	
	
	public boolean tieneReservasxAula(Integer id_au,Integer id_alu){
		return  vacanteDAO.tieneReservasxAula(id_au, id_alu);
	}
	
	public boolean tieneReservasxGrado(Integer id_grad,Integer id_anio,Integer id_alu){
		return  vacanteDAO.tieneReservasxGrado(id_grad, id_anio, id_alu);
	}
	
	/**
	 * 
	 * @param id_au
	 * @return
	 */
	public Integer getMatriculadosNoSug(Integer id_au){
		Integer reservas = vacanteDAO.getMatriculadosNoSug(id_au);
		return reservas;
	}
	
	/**
	 * 
	 * @param id_gra
	 * @return
	 */
	public Integer getMatriculadosNoSugxGrado(Integer id_grad, Integer id_anio, Integer id_suc){
		Integer reservas = vacanteDAO.getMatriculadosNoSugxGrado(id_grad, id_anio, id_suc);
		return reservas;
	}
	
	/**
	 * Matriculas Vacante por evaluacion, grado y periodo
	 * @param id_eva
	 * @param id_gra
	 * @param id_per
	 * @return
	 */
	public Integer getMatriculasVacante(int id_eva,int id_gra, int id_per){
		Integer matriculas= vacanteDAO.getMatriculasVacante(id_eva, id_gra, id_per);
		return matriculas;
	}
	
	
	public Integer getMatriculasVacantexLocal(Integer id_suc,Integer id_gra, Integer id_anio){
		Integer matriculas= vacanteDAO.getMatriculasVacantexLocal(id_suc, id_gra, id_anio);
		return matriculas;
	}
	/**
	 * Alumnos obtienen vacante dentro de la fecha de vigencia
	 * @param anio
	 * @param id_grad
	 * @param id_suc
	 * @return
	 */
	public Integer alumnosObtieneVac(Integer anio, Integer id_grad, Integer id_suc){
		Integer reservas = vacanteDAO.alumnosObtieneVac(anio, id_grad, id_suc);
		return reservas;
	}
	
	/**
	 * Alumnos que tienen matricula vacante
	 * @param id_grad
	 * @param id_eva
	 * @return
	 */
	public Integer alumnosMatriculaVacante(Integer id_grad, Integer id_eva){
		Integer matriculas_vacante = vacanteDAO.vacOcupadas(id_grad, id_eva);
		return matriculas_vacante;
	}
	
	public Integer alumnosSugeridos(Integer id_au, Integer id_anio){
		Integer sugeridos = vacanteDAO.sugeridos(id_au, id_anio);
		return sugeridos;
	}
	
	public Integer alumnosSugeridosGrado(Integer id_gra,Integer id_anio, Integer id_suc){
		Integer sugeridos = vacanteDAO.sugeridosGrado(id_gra, id_anio, id_suc);
		return sugeridos;
	}
	
	
	/*public Integer getNroVacantesMatriculaVacantezzzzzzzzz(Integer id_per, Integer id_gra, Integer id_eva) throws Exception{
		
		//Calcul/o de Vacante
		//Obtengo mi a�o academico
		Calendar c1 = Calendar.getInstance();
		Integer anio_acad = c1.get(Calendar.YEAR);
		Integer id_anio_acad = anioDAO.getByParams(new Param("nom",anio_acad)).getId();
		//Obtengo el estado del a�o
		Date fec_act=new java.util.Date();
		Date fec_ini=anioDAO.getByParams(new Param("nom",anio_acad)).getFec_ini();
		Date fec_fin=anioDAO.getByParams(new Param("nom",anio_acad)).getFec_fin();
		String est="";
		if(fec_act.before(fec_ini)){
			est="N";//No hay clases
		} else if(fec_act.after(fec_fin)){
			est="F";//Finalizo las clases
		} else if(fec_act.after(fec_ini) && fec_act.before(fec_fin)){
			est="C";//Estan en clases
		}
		//Datos del Periodo Vacante
		Periodo periodoVacante = periodoDAO.get(id_per);
		Param param = new Param();
		param.put("id_anio", periodoVacante.getId_anio());
		param.put("id_srv", periodoVacante.getId_srv());
		param.put("id_tpe", Constante.TIPO_PERIODO_ESCOLAR);
		//Obtengo el periodo escolar
		Periodo periodoEscolar= periodoDAO.getByParams(param);

		Integer anio_eva=Integer.parseInt(anioDAO.getByParams(new Param("id",periodoVacante.getId_anio())).getNom());
		Integer id_anio_eva=anioDAO.getByParams(new Param("nom",anio_eva)).getId();
		Integer id_anio_ant=id_anio_eva-1;
		Integer id_gra_ant=id_gra-1;
		Integer id_suc=periodoVacante.getId_suc();
		//Obtengo datos del cronograma de los antiguos
		Param param2= new Param();
		param2.put("id_anio", periodoVacante.getId_anio());
		param2.put("tipo", "AC");
		ConfFechas cronograma=confFechasDAO.getByParams(param2);
		Integer nro_vac=0;
		if(anio_acad==anio_eva && est.equals("N") && cronograma==null){
			nro_vac=getCapacidadxGrado(anio_acad, id_gra, id_suc)-getAlumnosMatriculadosxGrado(id_anio_ant, id_gra_ant, id_suc)-getDesaprobados(id_anio_ant, id_gra, id_suc)-getMatriculasVacante(id_eva, id_gra, id_per)-getReservasxGrado(periodoEscolar.getId(), id_gra);
		}else if(anio_acad==anio_eva && est.equals("N") && cronograma!=null){
			nro_vac=getCapacidadxGrado(anio_acad, id_gra, id_suc)-getAlumnosMatriculadosxGrado(id_anio_eva, id_gra, id_suc)-getMatriculasVacante(id_eva, id_gra, id_per)-getReservasxGrado(periodoEscolar.getId(), id_gra);
		} else if(anio_acad==anio_eva && est.equals("C") && cronograma!=null){
			nro_vac=getCapacidadxGrado(anio_acad, id_gra, id_suc)-getAlumnosMatriculadosxGrado(id_anio_eva, id_gra, id_suc)-getMatriculasVacante(id_eva, id_gra, id_per)-getReservasxGrado(periodoEscolar.getId(), id_gra);
		}else if(anio_acad==anio_eva && est.equals("F") && cronograma!=null){
				throw new Exception("A�O ACADEMICO CERRADO!!");
		} else if(anio_eva>anio_acad && est.equals("N") && cronograma==null){
			throw new Exception("SOLO SE PUEDE OTROGAR VACANTES AL A�O SIGUIENTE!!");
		} else if(anio_eva>anio_acad && est.equals("C")){
			nro_vac=getCapacidadxGrado(anio_eva, id_gra, id_suc)-getAlumnosMatriculadosxGrado(id_anio_acad, id_gra_ant, id_suc)-getMatriculasVacante(id_eva, id_gra, id_per)-getReservasxGrado(periodoEscolar.getId(), id_gra);
		} else if(anio_eva>anio_acad && est.equals("F") && cronograma==null){
			nro_vac=getCapacidadxGrado(anio_eva, id_gra, id_suc)-getAlumnosMatriculadosxGrado(id_anio_acad, id_gra_ant, id_suc)-getMatriculasVacante(id_eva, id_gra, id_per)-getDesaprobados(id_anio_acad, id_gra, id_suc)-getReservasxGrado(periodoEscolar.getId(), id_gra);
		} else if(anio_eva>anio_acad && est.equals("F") && cronograma!=null){
			nro_vac=getCapacidadxGrado(anio_eva, id_gra, id_suc)-getAlumnosMatriculadosxGrado(id_anio_acad, id_gra_ant, id_suc)-getMatriculasVacante(id_eva, id_gra, id_per)-getDesaprobados(id_anio_acad, id_gra, id_suc)-getReservasxGrado(periodoEscolar.getId(), id_gra);
		}
		
		return nro_vac;
		
	}*/
	
	public Integer getNroVacantesMatriculaVacante(Integer id_per, Integer id_gra, Integer id_eva) throws Exception{
		//Calcul/o de Vacante
		//Obtengo mi a�o academico
		Calendar c1 = Calendar.getInstance();
		Integer anio_acad = c1.get(Calendar.YEAR);
		Integer id_anio_acad = anioDAO.getByParams(new Param("nom",anio_acad)).getId();
		//Obtengo el estado del a�o
		Date fec_act=new java.util.Date();
		Date fec_ini=anioDAO.getByParams(new Param("nom",anio_acad)).getFec_ini();
		Date fec_fin=anioDAO.getByParams(new Param("nom",anio_acad)).getFec_fin();
		String est="";
		if(fec_act.before(fec_ini)){
			est="N";//No hay clases
		} else if(fec_act.after(fec_fin)){
			est="F";//Finalizo las clases
		} else if(fec_act.after(fec_ini) && fec_act.before(fec_fin)){
			est="C";//Estan en clases
		}
		//Datos del Periodo Vacante
		Periodo periodoVacante = periodoDAO.get(id_per);
		Param param = new Param();
		param.put("id_anio", periodoVacante.getId_anio());
		param.put("id_srv", periodoVacante.getId_srv());
		param.put("id_tpe", Constante.TIPO_PERIODO_ESCOLAR);
		//Obtengo el periodo escolar
		Periodo periodoEscolar= periodoDAO.getByParams(param);
		Ciclo ciclo = cicloDAO.getByParams(new Param("id_per",periodoEscolar.getId()));

		Integer anio_eva=Integer.parseInt(anioDAO.getByParams(new Param("id",periodoVacante.getId_anio())).getNom());
		Integer id_anio_eva=anioDAO.getByParams(new Param("nom",anio_eva)).getId();
		Integer id_anio_ant=id_anio_eva-1;
		Integer id_gra_ant=id_gra-1;
		Integer id_suc=periodoVacante.getId_suc();

		//Obtengo datos del cronograma de los antiguos
				boolean antiguo_con_cronograma = cronogramaDAO.alumnosAntiguosTienenVigente(id_anio_eva);
				boolean antiguo_sin_cronograma = confFechasDAO.cronogramaVigente(id_anio_eva, "AS");
				boolean nuevo_con_cronograma = confFechasDAO.cronogramaVigente(id_anio_eva, "NC");
				boolean nuevos_sin_cronograma = confFechasDAO.cronogramaVigente(id_anio_eva, "NS");
		Integer nro_vac=0;
		if(anio_acad.intValue()==anio_eva.intValue() && est.equals("N") && !antiguo_con_cronograma && !antiguo_sin_cronograma && nuevo_con_cronograma){//actual y estan en cronogramas de nuevos
			nro_vac=getCapacidadxGrado(anio_acad, id_gra, id_suc)-getAlumnosMatriculadosxGrado(id_anio_eva, id_gra, id_suc)-getMatriculasVacante(id_eva, id_gra, id_anio_eva)-getReservasxGrado(periodoEscolar.getId(), id_gra);
		}else if(anio_acad.intValue()==anio_eva.intValue() && est.equals("N") && antiguo_sin_cronograma && nuevos_sin_cronograma){//actual y estan en sin cronograma de antiguos y nuevo sin cronogrma
			Integer cap=getCapacidadxGrado(anio_acad, id_gra, id_suc);
			Integer mat=getAlumnosMatriculadosxGrado(id_anio_eva, id_gra, id_suc);
			Integer mat_vac=getMatriculasVacante(id_eva, id_gra, id_anio_eva);
			Integer res=getReservasxGrado(periodoEscolar.getId(), id_gra);
			if(cap!=null)
			nro_vac=cap-mat-mat_vac-res;
			else nro_vac=0;
		}
		else if(anio_acad.intValue()==anio_eva.intValue() && est.equals("N") && !antiguo_con_cronograma){//aqui si quiero saber q sea falso esta bien asi??
			Integer cap = getCapacidadxGrado(anio_acad, id_gra, id_suc);
			Integer mat=getAlumnosMatriculadosxGrado(id_anio_acad, id_gra, id_suc);
			Integer no_rat=getAlumnosNoRatificaronxGrado(id_anio_ant, id_anio_acad, id_gra_ant, id_suc);
			Integer des=getDesaprobados(id_anio_ant, id_gra, id_suc);
			Integer mat_vac=getMatriculasVacante(id_eva, id_gra, id_anio_eva);
			Integer res = getReservasxGrado(periodoEscolar.getId(), id_gra);
			nro_vac=cap-mat-des-mat_vac-res;
		}else if(anio_acad.intValue()==anio_eva.intValue() && est.equals("N") && antiguo_con_cronograma){
			Integer cap=getCapacidadxGrado(anio_acad, id_gra, id_suc);
			Integer mat=getAlumnosMatriculadosxGrado(id_anio_eva, id_gra, id_suc);
			Integer mat_ant=getAlumnosMatriculadosxGrado(id_anio_ant, id_gra_ant, id_suc);
			Integer no_rat=getAlumnosNoRatificaronxGrado(id_anio_ant, id_anio_acad, id_gra_ant, id_suc);
			Integer sug=alumnosSugeridosGrado(id_gra,id_anio_eva,id_suc);
			Integer mat_vac=getMatriculasVacante(id_eva, id_gra, id_anio_eva);
			Integer res=getReservasxGrado(periodoEscolar.getId(), id_gra);
			nro_vac=cap-mat-mat_ant-sug-mat_vac-res+no_rat;
		} else if((anio_acad.intValue()==anio_eva.intValue() && est.equals("C") && antiguo_con_cronograma) || (anio_acad.intValue()==anio_eva.intValue() && est.equals("C")  && nuevos_sin_cronograma)){
			Integer cap =getCapacidadxGrado(anio_acad, id_gra, id_suc);
			Integer mat=getAlumnosMatriculadosxGrado(id_anio_eva, id_gra, id_suc);
			Integer mat_vac=getMatriculasVacante(id_eva, id_gra, id_anio_eva);
			Integer res=getReservasxGrado(periodoEscolar.getId(), id_gra);			
			nro_vac=cap-mat-mat_vac-res;
		}else if(anio_acad.intValue()==anio_eva.intValue() && est.equals("F") && antiguo_con_cronograma){
				throw new Exception("A�O ACADEMICO CERRADO!!");
		} else if(anio_eva.intValue()>anio_acad.intValue() && est.equals("N") && !antiguo_con_cronograma){
			throw new Exception("SOLO SE PUEDE OTROGAR VACANTES AL A�O SIGUIENTE!!");
		} else if(anio_eva.intValue()>anio_acad.intValue() && est.equals("C")){
			Integer cap=getCapacidadxGrado(anio_eva, id_gra, id_suc);
			Integer mat_ant=getAlumnosMatriculadosxGrado(id_anio_acad, id_gra_ant, id_suc);
			Integer no_rat=getAlumnosNoRatificaronxGrado(id_anio_acad, id_anio_eva, id_gra_ant, id_suc);
			Integer mat_vac=getMatriculasVacante(id_eva, id_gra, id_anio_eva);
			Integer res=getReservasxGrado(periodoEscolar.getId(), id_gra);
			Integer sug=alumnosSugeridosGrado(id_gra,id_anio_eva,id_suc);
			//nro_vac=-getAlumnosMatriculadosxGrado(id_anio_acad, id_gra_ant, id_suc)-getMatriculasVacante(id_eva, id_gra, id_anio_eva)-getReservasxGrado(periodoEscolar.getId(), id_gra);
			nro_vac=cap-mat_ant-mat_vac-res-sug+no_rat;
			//nro_vac=getCapacidadxGrado(anio_eva, id_gra, id_suc)-getAlumnosMatriculadosxGrado(id_anio_acad, id_gra_ant, id_suc)-getMatriculasVacante(id_eva, id_gra, id_anio_eva)-getReservasxGrado(periodoEscolar.getId(), id_gra);
		} else if(anio_eva.intValue()>anio_acad.intValue() && est.equals("F") && !antiguo_con_cronograma){
			//nro_vac=getCapacidadxGrado(anio_eva, id_gra, id_suc)-getAlumnosMatriculadosxGrado(id_anio_acad, id_gra_ant, id_suc)-getAlumnosNoRatificaronxGrado(id_anio_acad, id_anio_eva, id_gra_ant, id_suc)-getMatriculasVacante(id_eva, id_gra, id_anio_eva)-getDesaprobados(id_anio_acad, id_gra, id_suc)-getReservasxGrado(periodoEscolar.getId(), id_gra);
			Integer cap=getCapacidadxGrado(anio_eva, id_gra, id_suc);
			Integer mat_ant=getAlumnosMatriculadosxGrado(id_anio_acad, id_gra_ant, id_suc);
			Integer no_rat=getAlumnosNoRatificaronxGrado(id_anio_acad, id_anio_eva, id_gra_ant, id_suc);
			Integer mat_vac=getMatriculasVacante(id_eva, id_gra, id_anio_eva);
			Integer res=getReservasxGrado(periodoEscolar.getId(), id_gra);
			Integer sug=alumnosSugeridosGrado(id_gra,id_anio_eva,id_suc);
			//nro_vac=-getAlumnosMatriculadosxGrado(id_anio_acad, id_gra_ant, id_suc)-getMatriculasVacante(id_eva, id_gra, id_anio_eva)-getReservasxGrado(periodoEscolar.getId(), id_gra);
			nro_vac=cap-mat_ant-mat_vac-res-sug+no_rat;
		} else if(anio_eva.intValue()>anio_acad.intValue() && est.equals("F") && antiguo_con_cronograma){
			//nro_vac=getCapacidadxGrado(anio_eva, id_gra, id_suc)-getAlumnosMatriculadosxGrado(id_anio_acad, id_gra_ant, id_suc)-getMatriculasVacante(id_eva, id_gra, id_anio_eva)-getDesaprobados(id_anio_acad, id_gra, id_suc)-getReservasxGrado(periodoEscolar.getId(), id_gra);
			Integer cap=getCapacidadxGrado(anio_eva, id_gra, id_suc);
			Integer mat_ant=getAlumnosMatriculadosxGrado(id_anio_acad, id_gra_ant, id_suc);
			Integer no_rat=getAlumnosNoRatificaronxGrado(id_anio_acad, id_anio_eva, id_gra_ant, id_suc);
			Integer mat_vac=getMatriculasVacante(id_eva, id_gra, id_anio_eva);
			Integer res=getReservasxGrado(periodoEscolar.getId(), id_gra);
			Integer sug=alumnosSugeridosGrado(id_gra,id_anio_eva,id_suc);
			//nro_vac=-getAlumnosMatriculadosxGrado(id_anio_acad, id_gra_ant, id_suc)-getMatriculasVacante(id_eva, id_gra, id_anio_eva)-getReservasxGrado(periodoEscolar.getId(), id_gra);
			nro_vac=cap-mat_ant-mat_vac-res-sug+no_rat;
		}
		
		return nro_vac;
		
	}
	
	public Integer getNroVacantesReserva(Integer id_au, Integer id_anio, Integer id_alu) throws Exception{
		Calendar c1 = Calendar.getInstance();
		Integer anio_acad = c1.get(Calendar.YEAR);
		Aula aula = aulaDAO.getByParams(new Param("id",id_au));
		Integer anio_res =Integer.parseInt(anioDAO.getByParams(new Param("id",id_anio)).getNom()); 
		Integer anio_ant=anio_res-1;
		Integer id_anio_ant=anioDAO.getByParams(new Param("nom",anio_ant)).getId();
		Integer id_au_ant=aula.getId_secc_ant();
		Integer desaprobados=getDesaprobadosxGradoySecc(aula.getId_grad(), aula.getSecc(), id_anio_ant);
		Matricula mat_ant= matriculaDAO.getMatriculaAnteriorReserva(id_alu, id_anio_ant);
		Integer id_au_sug= null;
		if (mat_ant !=null)
			id_au_sug=mat_ant.getId_au_nue();
		
		Integer nro_vac=0;
		//Obtengo datos del cronograma de los antiguos
				/*Param param2= new Param();
				param2.put("id_anio", id_anio);
				param2.put("tipo", "AC");
				ConfFechas cronograma=confFechasDAO.getByParams(param2);*/
		//Obtengo datos del cronograma de los antiguos
		boolean antiguo_con_cronograma = cronogramaDAO.alumnosAntiguosTienenVigente(id_anio);
		boolean antiguo_sin_cronograma = confFechasDAO.cronogramaVigente(id_anio, "AS");
		boolean nuevo_con_cronograma = confFechasDAO.cronogramaVigente(id_anio, "NC");
		boolean nuevos_sin_cronograma = confFechasDAO.cronogramaVigente(id_anio, "NS");
		
		//Obtengo el estado del año
				Date fec_act=new java.util.Date();
				Date fec_ini=anioDAO.getByParams(new Param("nom",anio_acad)).getFec_ini();
				Date fec_fin=anioDAO.getByParams(new Param("nom",anio_acad)).getFec_fin();
				String est="";
				if(fec_act.before(fec_ini)){
					est="N";//No hay clases
				} else if(fec_act.after(fec_fin)){
					est="F";//Finalizo las clases
				} else if(fec_act.after(fec_ini) && fec_act.before(fec_fin)){
					est="C";//Estan en clases
				}
				
		/**
		 * Si quiere hacer una reserva para el proximo a�o cuando no haya clases o haya finalizado o estan en clases y no existe cronograma de matriculas
		 */
		if((anio_acad<anio_res && est.equals("N") || anio_acad<anio_res && est.equals("F") || anio_acad<anio_res && est.equals("C")) && ( !antiguo_con_cronograma && !antiguo_sin_cronograma)){
			nro_vac=getCapacidadxAula(id_au)-getAlumnosMatriculadosxAula(id_au_ant)-desaprobados-getReservasxAula(id_au, id_anio);
			if(id_au_sug!=null && id_au_sug.equals(id_au)){
				nro_vac=nro_vac+1;
			}

		} else if((anio_acad.equals(anio_res)  && est.equals("N") && antiguo_con_cronograma==true) ){
			
			if (nuevo_con_cronograma){
				int cap =getCapacidadxAula(id_au);
				int mat =getAlumnosMatriculadosxAula (id_au) ;
				int res =getReservasxAula(id_au,id_anio);
				
				nro_vac= cap-mat-res ;

			}else{
				Integer  sugeridos = alumnosSugeridos(id_au, id_anio);
				Integer matriculadosNoSugeridos = getMatriculadosNoSug(id_au);
				int mat =getAlumnosMatriculadosxAula (id_au) ;
				int res = getReservasxAula(id_au,id_anio);
				nro_vac=getCapacidadxAula(id_au)-mat-sugeridos-matriculadosNoSugeridos-res;

				if(id_au_sug!=null && id_au_sug.equals(id_au)){
					nro_vac=nro_vac+1;
				}
				
			}
		} else if((anio_acad.equals(anio_res)  && est.equals("N") && !antiguo_con_cronograma && !antiguo_sin_cronograma)) {
			if (nuevo_con_cronograma){
				int cap =getCapacidadxAula(id_au);
				int mat =getAlumnosMatriculadosxAula (id_au) ;
				int res =getReservasxAula(id_au,id_anio);
				
				nro_vac= cap-mat-res ;

			}else{
				//Integer  sugeridos = alumnosSugeridos(id_au, id_anio);
				//Integer matriculadosNoSugeridos = getMatriculadosNoSug(id_au);
				int mat =getAlumnosMatriculadosxAula (id_au) ;
				int res = getReservasxAula(id_au,id_anio);
				nro_vac=getCapacidadxAula(id_au)-mat-res;

				if(id_au_sug!=null && id_au_sug.equals(id_au)){
					nro_vac=nro_vac+1;
				}
			}
		}
			else if((anio_acad.equals(anio_res) && est.equals("C") && !antiguo_con_cronograma && !antiguo_sin_cronograma ) || (anio_acad.equals(anio_res) && est.equals("C") && nuevos_sin_cronograma )){
		
			/**
			 * revisar. cuiando hay clases
			 */
			nro_vac=getCapacidadxAula(id_au)-getAlumnosMatriculadosxAula(id_au)-getReservasxAula(id_au,id_anio);
		}  else if(antiguo_sin_cronograma && anio_acad.equals(anio_res) && est.equals("N") ){
			nro_vac=getCapacidadxAula(id_au)-getAlumnosMatriculadosxAula(id_au)-getReservasxAula(id_au, id_anio);
		}
	return 	nro_vac;
	}
	
	
	public Integer getNroVacantesReservaxGrado(Integer id_anio, Integer id_alu, Integer id_grad, Integer id_suc) throws Exception{
		Calendar c1 = Calendar.getInstance();
		Integer anio_acad = c1.get(Calendar.YEAR);
		//Aula aula = aulaDAO.getByParams(new Param("id",id_au));
		Integer anio_res =Integer.parseInt(anioDAO.getByParams(new Param("id",id_anio)).getNom()); 
		Integer anio_ant=anio_res-1;
		Integer id_anio_ant=anioDAO.getByParams(new Param("nom",anio_ant)).getId();
		//Integer id_au_ant=aula.getId_secc_ant();
		Integer desaprobados=getDesaprobadosxGrado(id_grad, id_anio_ant);
		Matricula mat_ant= matriculaDAO.getMatriculaAnteriorReserva(id_alu, id_anio_ant);
		Integer id_au_sug= null;
		if (mat_ant !=null)
			id_au_sug=mat_ant.getId_au_nue();
		
		Integer nro_vac=0;
		//Obtengo datos del cronograma de los antiguos
				/*Param param2= new Param();
				param2.put("id_anio", id_anio);
				param2.put("tipo", "AC");
				ConfFechas cronograma=confFechasDAO.getByParams(param2);*/
		//Obtengo datos del cronograma de los antiguos
		boolean antiguo_con_cronograma = cronogramaDAO.alumnosAntiguosTienenVigente(id_anio);
		boolean antiguo_sin_cronograma = confFechasDAO.cronogramaVigente(id_anio, "AS");
		boolean nuevo_con_cronograma = confFechasDAO.cronogramaVigente(id_anio, "NC");
		boolean nuevos_sin_cronograma = confFechasDAO.cronogramaVigente(id_anio, "NS");
		
		//Obtengo el estado del año
				Date fec_act=new java.util.Date();
				Date fec_ini=anioDAO.getByParams(new Param("nom",anio_acad)).getFec_ini();
				Date fec_fin=anioDAO.getByParams(new Param("nom",anio_acad)).getFec_fin();
				String est="";
				if(fec_act.before(fec_ini)){
					est="N";//No hay clases
				} else if(fec_act.after(fec_fin)){
					est="F";//Finalizo las clases
				} else if(fec_act.after(fec_ini) && fec_act.before(fec_fin)){
					est="C";//Estan en clases
				}
				
		/**
		 * Si quiere hacer una reserva para el proximo a�o cuando no haya clases o haya finalizado o estan en clases y no existe cronograma de matriculas
		 */
		if((anio_acad<anio_res && est.equals("N") || anio_acad<anio_res && est.equals("F") || anio_acad<anio_res && est.equals("C")) && ( !antiguo_con_cronograma && !antiguo_sin_cronograma)){// Hay clases y aun no hay matriculas
			//nro_vac=getCapacidadxAula(id_au)-getAlumnosMatriculadosxAula(id_au_ant)-desaprobados-getReservasxAula(id_au, id_anio);
			int cap=getCapacidadxGrado(anio_res, id_grad, id_suc);
			int mat=getAlumnosMatriculadosxGrado(id_anio, id_grad, id_suc);
			int res=getReservasxGradoLocal(id_anio, id_grad, id_suc);
			Integer mat_anteriores=getTotalMatriculasAnteriosNoMatriculados(id_anio_ant, id_grad-1, id_suc, id_anio);
			Integer no_rat=getTotalNoRatificaronxGrado(id_anio_ant, id_anio,  id_grad-1, id_suc);
			nro_vac=cap-mat-desaprobados-res-mat_anteriores+no_rat;
			/*if(id_au_sug!=null && id_au_sug.equals(id_au)){
				nro_vac=nro_vac+1;
			}*/

		} if((anio_acad<anio_res && est.equals("F")) && ( antiguo_con_cronograma )){// Hay clases y aun no hay matriculas
			//nro_vac=getCapacidadxAula(id_au)-getAlumnosMatriculadosxAula(id_au_ant)-desaprobados-getReservasxAula(id_au, id_anio);
			int cap=getCapacidadxGrado(anio_res, id_grad, id_suc);
			int mat=getAlumnosMatriculadosxGrado(id_anio, id_grad, id_suc);
			int res=getReservasxGradoLocal(id_anio, id_grad, id_suc);
			Integer mat_anteriores=getTotalMatriculasAnteriosNoMatriculados(id_anio_ant, id_grad-1, id_suc, id_anio);
			Integer no_rat=getTotalNoRatificaronxGrado(id_anio_ant, id_anio, id_grad-1, id_suc);
			nro_vac=cap-mat-desaprobados-res-mat_anteriores+no_rat;
			/*if(id_au_sug!=null && id_au_sug.equals(id_au)){
				nro_vac=nro_vac+1;
			}*/

		} else if((anio_acad.equals(anio_res)  && est.equals("N") && antiguo_con_cronograma==true) ){//Tiempo de matriculas
			
			if (nuevo_con_cronograma){
				//int cap =getCapacidadxAula(id_au);
				int cap =getCapacidadxGrado(anio_res, id_grad, id_suc);
				//int mat =getAlumnosMatriculadosxAula (id_au) ;
				int mat =getAlumnosMatriculadosxGrado(id_anio, id_grad, id_suc);
				//int res =getReservasxAula(id_au,id_anio);
				int res =getReservasxGradoLocal(id_anio, id_grad, id_suc);
				
				nro_vac= cap-mat-res;

			}else{
				//Integer  sugeridos = alumnosSugeridosGrado(id_grad, id_anio, id_suc); dEJOS DE FUNCIONAR 2022
				//Integer matriculadosNoSugeridos = getMatriculadosNoSugxGrado(id_grad, id_anio, id_suc); //Dejo de funcionar 2022
				int mat =getAlumnosMatriculadosxGrado(id_anio, id_grad, id_suc) ;
				int res = getReservasxGradoLocal(id_anio, id_grad, id_suc);
				Integer mat_anteriores=getTotalMatriculasAnteriosNoMatriculados(id_anio_ant, id_grad-1, id_suc, id_anio);
				Integer no_rat=getTotalNoRatificaronxGrado(id_anio_ant, id_anio, id_grad-1, id_suc);
				//nro_vac=getCapacidadxGrado(anio_res, id_grad, id_suc)-mat-sugeridos-matriculadosNoSugeridos-res; // Dejo de funcionar 2022
				nro_vac=getCapacidadxGrado(anio_res, id_grad, id_suc)-mat-mat_anteriores-res+no_rat;
				/*if(id_au_sug!=null && id_au_sug.equals(id_au)){
					nro_vac=nro_vac+1;
				}*/
				
			}
		} else if((anio_acad.equals(anio_res)  && est.equals("N") && !antiguo_con_cronograma && !antiguo_sin_cronograma)) {
			if (nuevo_con_cronograma){
				int cap =getCapacidadxGrado(anio_res, id_grad, id_suc);
				int mat =getAlumnosMatriculadosxGrado(id_anio, id_grad, id_suc) ;
				int res =getReservasxGradoLocal(id_anio, id_grad, id_suc);
				
				nro_vac= cap-mat-res ;

			}else{
				//Integer  sugeridos = alumnosSugeridos(id_au, id_anio);
				//Integer matriculadosNoSugeridos = getMatriculadosNoSug(id_au);
				int mat =getAlumnosMatriculadosxGrado(id_anio, id_grad, id_suc) ;
				int res = getReservasxGradoLocal(id_anio, id_grad, id_suc);
				
				nro_vac=getCapacidadxGrado(anio_res, id_grad, id_suc)-mat-res;

				/*if(id_au_sug!=null && id_au_sug.equals(id_au)){
					nro_vac=nro_vac+1;
				}*/
			}
		}
			else if((anio_acad.equals(anio_res) && est.equals("C") && !antiguo_con_cronograma && !antiguo_sin_cronograma ) || (anio_acad.equals(anio_res) && est.equals("C") && nuevos_sin_cronograma )){
		
			/**
			 * revisar. cuiando hay clases
			 */
			nro_vac=getCapacidadxGrado(anio_res, id_grad, id_suc)-getAlumnosMatriculadosxGrado(id_anio, id_grad, id_suc)-getReservasxGradoLocal(id_anio, id_grad, id_suc);
		}  else if(antiguo_sin_cronograma && anio_acad.equals(anio_res) && est.equals("N") ){
			nro_vac=getCapacidadxGrado(anio_res, id_grad, id_suc)-getAlumnosMatriculadosxGrado(id_anio, id_grad, id_suc)-getReservasxGradoLocal(id_anio, id_grad, id_suc);
		}
	return 	nro_vac;
	}
	
	/*public Integer getNroVacantesMatricula(Integer id_au, Integer id_anio){
		
		return null;
	}*/

	/*public Integer getNroVacantes(Integer id_anio){
		return getAlumnosMatriculadosxAula(id_au) + getAlumnosMatriculadosxAulaSituacion( +, situacion);
		//obtener situaxcion del a�o
		/*
		if (mismo a�o){
			
		}else{
			
		}*/
		
	//}
	
	

	/**
	 * Obtener las vacantes
	 * 
	 * @param id_au
	 * @param id_alu
	 * @return
	 * @throws Exception
	 */
	public AulaCapacidad getNroVacantesMatricula(Integer id_au, Integer id_alu) throws Exception{
		
		if(id_au!=null && id_au!=null){
			Periodo periodo=aulaDAO.listFullByParams(new Param("aula.id",id_au),null).get(0).getPeriodo();
			Integer id_anio=periodo.getId_anio();//anio de matricula
			Integer id_anio_ant= id_anio-1;// anio anterior
			//Integer id_anio=3;
			//Obtengo datos del cronograma de los antiguos
			boolean antiguo_con_cronograma = cronogramaDAO.alumnosAntiguosTienenVigente(id_anio);
			boolean antiguo_sin_cronograma = confFechasDAO.cronogramaVigente(id_anio, "AS");
			boolean nuevo_con_cronograma = confFechasDAO.cronogramaVigente(id_anio, "NC");
			boolean nuevo_sin_cronograma = confFechasDAO.cronogramaVigente(id_anio, "NS");
			String tipoCronograma = null;
			Param param = new Param();
			param.put("id_alu", id_alu);
			Matricula mat_ant= matriculaDAO.getMatriculaAnterior(id_alu, id_anio_ant);
			Integer id_au_sug= (mat_ant==null)?null:mat_ant.getId_au_nue();
			Integer capacidad=getCapacidadxAula(id_au);
			Integer nro_vac=0;
			if(antiguo_con_cronograma){
				//ObTENGO LOS ALUMNOS CON  AULA SUGERIDA y que no esten matriculados no reservados
				Integer  sugeridos = alumnosSugeridos(id_au, id_anio);
				Integer  reservasxAula = getReservasxAula(id_au,id_anio);
				Integer matriculadosNoSugeridos = getMatriculadosNoSug(id_au);
				Integer matriculados =getAlumnosMatriculadosxAula(id_au);
				//nro_vac=capacidad -sugeridos -reservasxAula-matriculadosNoSugeridos;
				nro_vac=capacidad-matriculados-sugeridos -reservasxAula;
				
				if(id_au_sug!=null && id_au_sug.equals(id_au)){
					nro_vac=nro_vac+1;
				}
				tipoCronograma = "AC";
			}  else if(antiguo_sin_cronograma){
				Integer matriculados =getAlumnosMatriculadosxAula(id_au);
				Integer reseva = getReservasxAula(id_au, id_anio);
				nro_vac=capacidad -matriculados -reseva;
				boolean tieneReserva = tieneReservasxAula(id_au,id_alu);
				if (tieneReserva)
					nro_vac = nro_vac + 1;
				tipoCronograma = "AS";
			}
			else if(nuevo_con_cronograma || nuevo_sin_cronograma){
				Integer matriculados =getAlumnosMatriculadosxAula(id_au);
				Integer reseva = getReservasxAula(id_au, id_anio);
				boolean tieneReserva = tieneReservasxAula(id_au,id_alu);
				nro_vac=capacidad -matriculados -reseva;
				if (tieneReserva)
					nro_vac = nro_vac + 1;
				tipoCronograma = "NC";
			}
					
			AulaCapacidad aulaCapacidad = new AulaCapacidad();
			aulaCapacidad.setCapacidad(capacidad);
			aulaCapacidad.setNro_vac(nro_vac);
			aulaCapacidad.setId_au_sug(id_au_sug);
			aulaCapacidad.setTip_cronograma(tipoCronograma);
			return aulaCapacidad;
		} else{
			return null;
		}
		
		
	}
	
	public AulaCapacidad getNroVacantesMatriculaxGrado(Integer id_grad,Integer id_suc, Integer id_niv, Integer id_anio, Integer id_alu) throws Exception{
		
			if(id_grad!=null ){
				//Periodo periodo=aulaDAO.listFullByParams(new Param("aula.id",id_au),null).get(0).getPeriodo();
				Param param = new Param();
				param.put("id_niv", id_niv);
				param.put("id_suc", id_suc);
				param.put("id_anio", id_anio);
				param.put("id_tpe", 1);
				Periodo periodo = periodoDAO.getByParams(param);
				if(periodo==null) {
					//throw new Exception("No existe el grado sugerido para este local, selecccionar otro!!");
					AulaCapacidad aulaCapacidad = new AulaCapacidad();
					aulaCapacidad.setCapacidad(0);
					aulaCapacidad.setNro_vac(0);
					aulaCapacidad.setId_au_sug(0);
					aulaCapacidad.setTip_cronograma("N");
					return aulaCapacidad;
				} 
				Ciclo ciclo = cicloDAO.getByParams(new Param("id_per",periodo.getId()));
				Integer id_anio_ant= id_anio-1;// anio anterior
				//Integer id_anio=3;
				//Obtengo datos del cronograma de los antiguos
				boolean antiguo_con_cronograma = cronogramaDAO.alumnosAntiguosTienenVigente(id_anio);
				boolean antiguo_sin_cronograma = confFechasDAO.cronogramaVigente(id_anio, "AS");
				boolean nuevo_con_cronograma = confFechasDAO.cronogramaVigente(id_anio, "NC");
				boolean nuevo_sin_cronograma = confFechasDAO.cronogramaVigente(id_anio, "NS");
				String tipoCronograma = null;
				Param para_regla= new Param();
				para_regla.put("nom", "MATR_AULA_SUG");
				ReglasNegocio reglasNegocio = reglasNegocioDAO.getByParams(para_regla);
				Integer id_gra_ant=id_grad-1;
				if(reglasNegocio.getVal().equals("1")) {
					Param param2 = new Param();
					param2.put("id_alu", id_alu);
					Matricula mat_ant= matriculaDAO.getMatriculaAnterior(id_alu, id_anio_ant);
					Integer id_au_sug=null;
					Aula aula_sugerida = null;
					if(mat_ant.getId_au_nue()!=null) {
						id_au_sug= (mat_ant==null)?null:mat_ant.getId_au_nue();
						aula_sugerida=aulaDAO.get(id_au_sug);
					}
					
					Integer capacidad=getCapacidadxGradoCiclo(ciclo.getId(), id_grad);
					if(capacidad==null) {
						AulaCapacidad aulaCapacidad = new AulaCapacidad();
						aulaCapacidad.setCapacidad(0);
						aulaCapacidad.setNro_vac(0);
						aulaCapacidad.setId_au_sug(0);
						aulaCapacidad.setTip_cronograma("N");
						return aulaCapacidad;
					}
					Integer nro_vac=0;
					if(antiguo_con_cronograma){
						//ObTENGO LOS ALUMNOS CON  AULA SUGERIDA y que no esten matriculados no reservados
						Integer  sugeridos = alumnosSugeridosGrado(id_grad, ciclo.getId(), id_anio);
						Integer  reservasxGrado = getReservasxGrado(periodo.getId(), id_grad);
						Integer mat_vac=getMatriculasVacantexLocal(id_suc, id_grad, id_anio);
						Integer no_rat=getAlumnosNoRatificaronxGrado(id_anio_ant, id_anio, id_gra_ant, id_suc);
						//Integer matriculadosNoSugeridos = getMatriculadosNoSugxGrado(id_grad);
						Integer matriculados =getAlumnosMatriculadosxGrado(id_anio, id_grad, id_suc);
						//nro_vac=capacidad -sugeridos -reservasxAula-matriculadosNoSugeridos;
						nro_vac=capacidad-matriculados-sugeridos-mat_vac-reservasxGrado+no_rat;
						if(id_au_sug!=null) {
							if(id_au_sug!=null && aula_sugerida.getId_grad().equals(id_grad)){
								nro_vac=nro_vac+1;
							}
						}
						
						tipoCronograma = "AC";
					}  else if(antiguo_sin_cronograma){
						Integer matriculados =getAlumnosMatriculadosxGrado(id_anio, id_grad, id_suc);
						Integer  reservasxGrado = getReservasxGrado(periodo.getId(), id_grad);
						Integer no_rat=getAlumnosNoRatificaronxGrado(id_anio_ant, id_anio, id_gra_ant, id_suc);
						nro_vac=capacidad -matriculados -reservasxGrado + no_rat;
						boolean tieneReserva = tieneReservasxGrado(id_grad, id_anio, id_alu);
						if (tieneReserva)
							nro_vac = nro_vac + 1;
						tipoCronograma = "AS";
					}
					else if(nuevo_con_cronograma || nuevo_sin_cronograma){
						/*Integer matriculados =getAlumnosMatriculadosxAula(id_au);
						Integer reseva = getReservasxAula(id_au, id_anio);
						boolean tieneReserva = tieneReservasxAula(id_au,id_alu);
						nro_vac=capacidad -matriculados -reseva;
						if (tieneReserva)
							nro_vac = nro_vac + 1;
						tipoCronograma = "NC";*/
					}
					
							
					AulaCapacidad aulaCapacidad = new AulaCapacidad();
					aulaCapacidad.setCapacidad(capacidad);
					aulaCapacidad.setNro_vac(nro_vac);
					aulaCapacidad.setId_au_sug(id_au_sug);
					aulaCapacidad.setTip_cronograma(tipoCronograma);
					return aulaCapacidad;
				} else { //Hayamos la capacidad en el local solo por grado y no aula sugerida
					Integer capacidad=getCapacidadxGradoCiclo(ciclo.getId(), id_grad);
					//Buscamos si el alumno está dentro del local sugerido por tener matricula anterior en el local respectivo
					Param param2 = new Param();
					param2.put("id_alu", id_alu);
					Row mat_ant= matriculaDAO.getMatriculaAnteriorLocal(id_alu, id_anio_ant);
					
					//Integer id_gra_sig=null;
					Integer id_suc_ant=null;
					Integer id_suc_sug=null;
					Integer id_gra_sug=null;
					//Integer id_au_sug=null;
					//Aula aula_sugerida = null;
					if(mat_ant!=null) {
						id_suc_ant=mat_ant.getInteger("id_suc");
						id_suc_sug=mat_ant.getInteger("id_suc_nue");
						id_gra_sug=mat_ant.getInteger("id_gra_nue");
					}
					
					if(capacidad==null) {
						AulaCapacidad aulaCapacidad = new AulaCapacidad();
						aulaCapacidad.setCapacidad(0);
						aulaCapacidad.setNro_vac(0);
						aulaCapacidad.setId_au_sug(0);
						aulaCapacidad.setTip_cronograma("N");
						return aulaCapacidad;
					}
					Integer nro_vac=0;
					if(antiguo_con_cronograma){
						//ObTENGO LOS ALUMNOS CON  AULA SUGERIDA y que no esten matriculados no reservados
						Integer  reservasxGrado = getReservasxGrado(periodo.getId(), id_grad);
						Integer mat_vac=getMatriculasVacantexLocal(id_suc, id_grad, id_anio);
						Integer matriculados =getAlumnosMatriculadosxGrado(id_anio, id_grad, id_suc);
						Integer matriculas_anteriores = getTotalMatriculasAnteriosNoMatriculadosNosugeridos(id_anio_ant, id_gra_ant, id_suc, id_anio);
						Integer sugeridosxGrado = alumnosSugeridosGrado(id_grad, id_anio, id_suc);
						Integer no_rat=getAlumnosNoRatificaronxGrado(id_anio_ant, id_anio, id_gra_ant, id_suc);
						//nro_vac=capacidad -sugeridos -reservasxAula-matriculadosNoSugeridos;
						nro_vac=capacidad-matriculados-mat_vac-reservasxGrado-matriculas_anteriores-sugeridosxGrado+no_rat;					
						if(id_suc_ant.equals(id_suc)) {
							nro_vac=nro_vac + 1;
						} else {
							if(id_suc_sug!=null) {
								if(id_suc_sug.equals(id_suc) && id_gra_sug.equals(id_grad)) {
									nro_vac=nro_vac + 1;
								}
							}
						}
						tipoCronograma = "AC";
					}  else if(antiguo_sin_cronograma && !nuevo_sin_cronograma ){
						Integer  reservasxGrado = getReservasxGrado(periodo.getId(), id_grad);
						Integer mat_vac=getMatriculasVacantexLocal(id_suc, id_grad, id_anio);
						Integer matriculados =getAlumnosMatriculadosxGrado(id_anio, id_grad, id_suc);
						//Integer no_rat=getAlumnosNoRatificaronxGrado(id_anio_ant, id_anio, id_gra_ant, id_suc);
						nro_vac=capacidad -matriculados -reservasxGrado - mat_vac;
						//Por ahora no sabemos como se va a manejar las reservas
						//boolean tieneReserva = tieneReservasxGrado(id_grad, id_anio, id_alu);
						//if (tieneReserva)
						//	nro_vac = nro_vac + 1;
						tipoCronograma = "AS";
					}
					else if(nuevo_con_cronograma || nuevo_sin_cronograma && !antiguo_sin_cronograma){
						Integer  reservasxGrado = getReservasxGrado(periodo.getId(), id_grad);
						Integer mat_vac=getMatriculasVacantexLocal(id_suc, id_grad, id_anio);
						Integer matriculados =getAlumnosMatriculadosxGrado(id_anio, id_grad, id_suc);
						//Integer no_rat=getAlumnosNoRatificaronxGrado(id_anio_ant, id_anio, id_gra_ant, id_suc);
						nro_vac=capacidad -matriculados -reservasxGrado - mat_vac ;
						tipoCronograma = "NC";
						/*Integer matriculados =getAlumnosMatriculadosxAula(id_au);
						Integer reseva = getReservasxAula(id_au, id_anio);
						boolean tieneReserva = tieneReservasxAula(id_au,id_alu);
						nro_vac=capacidad -matriculados -reseva;
						if (tieneReserva)
							nro_vac = nro_vac + 1;
						tipoCronograma = "NC";*/
					} else if(nuevo_sin_cronograma && antiguo_sin_cronograma){
						if(mat_ant!=null) {
							Integer matriculados =getAlumnosMatriculadosxGrado(id_anio, id_grad, id_suc);
							Integer  reservasxGrado = getReservasxGrado(periodo.getId(), id_grad);
							Integer mat_vac=getMatriculasVacantexLocal(id_suc, id_grad, id_anio);
							//Integer no_rat=getAlumnosNoRatificaronxGrado(id_anio_ant, id_anio, id_gra_ant, id_suc);
							nro_vac=capacidad -matriculados -reservasxGrado - mat_vac ;
							boolean tieneReserva = tieneReservasxGrado(id_grad, id_anio, id_alu);
							if (tieneReserva)
								nro_vac = nro_vac + 1;
							tipoCronograma = "ASNS";
						} else {
							//Buscamos la reserva del alumno
							Row reserva = reservaDAO.obtenerReservaAlumno(id_anio, id_alu);
							Integer id_suc_res=null;
							Integer id_gra_res=null;
							//Date fec_limit_res = reserva.getDate("fec_lim");
							if(reserva.size()>0) {
								Integer id_per_res= reserva.getInteger("id_per");
								Periodo periodo_reserva = periodoDAO.get(id_per_res);
								id_suc_res=periodo_reserva.getId_suc();
								id_gra_res=reserva.getInteger("id_gra");
							}
							
							Integer  reservasxGrado = getReservasxGrado(periodo.getId(), id_grad);
							Integer mat_vac=getMatriculasVacantexLocal(id_suc, id_grad, id_anio);
							Integer matriculados =getAlumnosMatriculadosxGrado(id_anio, id_grad, id_suc);
							//Integer no_rat=getAlumnosNoRatificaronxGrado(id_anio_ant, id_anio, id_gra_ant, id_suc);
							nro_vac=capacidad -matriculados -reservasxGrado - mat_vac ;
							
							boolean tieneReserva = tieneReservasxGrado(id_grad, id_anio, id_alu);
							if(tieneReserva) {
								if(id_suc_res!=null) {
									if(id_suc_res.equals(id_suc) && id_gra_res.equals(id_grad)) {
										nro_vac=nro_vac + 1;
									}
								}
							}
							tipoCronograma = "ASNS";
						}
						
						
					}
					
							
					AulaCapacidad aulaCapacidad = new AulaCapacidad();
					aulaCapacidad.setCapacidad(capacidad);
					aulaCapacidad.setNro_vac(nro_vac);
					//aulaCapacidad.setId_au_sug(id_au_sug);
					aulaCapacidad.setTip_cronograma(tipoCronograma);
					return aulaCapacidad;
				}
				
		
			} else{
				return null;
			}	
	}
	
	public AulaCapacidad getNroVacantesMatriculaxGradoyModalidad(Integer id_grad,Integer id_suc, Integer id_niv, Integer id_anio, Integer id_alu, Integer id_cme) throws Exception{
		
		if(id_grad!=null ){
			//Periodo periodo=aulaDAO.listFullByParams(new Param("aula.id",id_au),null).get(0).getPeriodo();
			Param param = new Param();
			param.put("id_niv", id_niv);
			param.put("id_suc", id_suc);
			param.put("id_anio", id_anio);
			param.put("id_tpe", 1);
			Periodo periodo = periodoDAO.getByParams(param);
			if(periodo==null) {
				//throw new Exception("No existe el grado sugerido para este local, selecccionar otro!!");
				AulaCapacidad aulaCapacidad = new AulaCapacidad();
				aulaCapacidad.setCapacidad(0);
				aulaCapacidad.setNro_vac(0);
				aulaCapacidad.setId_au_sug(0);
				aulaCapacidad.setTip_cronograma("N");
				return aulaCapacidad;
			} 
			Ciclo ciclo = cicloDAO.getByParams(new Param("id_per",periodo.getId()));
			Integer id_anio_ant= id_anio-1;// anio anterior
			//Integer id_anio=3;
			//Obtengo datos del cronograma de los antiguos
			boolean antiguo_con_cronograma = cronogramaDAO.alumnosAntiguosTienenVigente(id_anio);
			boolean antiguo_sin_cronograma = confFechasDAO.cronogramaVigente(id_anio, "AS");
			boolean nuevo_con_cronograma = confFechasDAO.cronogramaVigente(id_anio, "NC");
			boolean nuevo_sin_cronograma = confFechasDAO.cronogramaVigente(id_anio, "NS");
			String tipoCronograma = null;
			Param para_regla= new Param();
			para_regla.put("nom", "MATR_AULA_SUG");
			ReglasNegocio reglasNegocio = reglasNegocioDAO.getByParams(para_regla);
			if(reglasNegocio.getVal().equals("1")) {
				Param param2 = new Param();
				param2.put("id_alu", id_alu);
				Matricula mat_ant= matriculaDAO.getMatriculaAnterior(id_alu, id_anio_ant);
				Integer id_au_sug=null;
				Aula aula_sugerida = null;
				if(mat_ant.getId_au_nue()!=null) {
					id_au_sug= (mat_ant==null)?null:mat_ant.getId_au_nue();
					aula_sugerida=aulaDAO.get(id_au_sug);
				}
				
				Integer capacidad=getCapacidadxGradoCiclo(ciclo.getId(), id_grad);
				if(capacidad==null) {
					AulaCapacidad aulaCapacidad = new AulaCapacidad();
					aulaCapacidad.setCapacidad(0);
					aulaCapacidad.setNro_vac(0);
					aulaCapacidad.setId_au_sug(0);
					aulaCapacidad.setTip_cronograma("N");
					return aulaCapacidad;
				}
				Integer nro_vac=0;
				if(antiguo_con_cronograma){
					//ObTENGO LOS ALUMNOS CON  AULA SUGERIDA y que no esten matriculados no reservados
					Integer  sugeridos = alumnosSugeridosGrado(id_grad, ciclo.getId(), id_anio);
					Integer  reservasxGrado = getReservasxGrado(periodo.getId(), id_grad);
					Integer mat_vac=getMatriculasVacantexLocal(id_suc, id_grad, id_anio);
					//Integer matriculadosNoSugeridos = getMatriculadosNoSugxGrado(id_grad);
					Integer matriculados =getAlumnosMatriculadosxGrado(id_anio, id_grad, id_suc);
					//nro_vac=capacidad -sugeridos -reservasxAula-matriculadosNoSugeridos;
					nro_vac=capacidad-matriculados-sugeridos -reservasxGrado-mat_vac;
					if(id_au_sug!=null) {
						if(id_au_sug!=null && aula_sugerida.getId_grad().equals(id_grad)){
							nro_vac=nro_vac+1;
						}
					}
					
					tipoCronograma = "AC";
				}  else if(antiguo_sin_cronograma){
					Integer matriculados =getAlumnosMatriculadosxGrado(id_anio, id_grad, id_suc);
					Integer  reservasxGrado = getReservasxGrado(periodo.getId(), id_grad);
					Integer mat_vac=getMatriculasVacantexLocal(id_suc, id_grad, id_anio);
					nro_vac=capacidad -matriculados -reservasxGrado-mat_vac;
					boolean tieneReserva = tieneReservasxGrado(id_grad, id_anio, id_alu);
					if (tieneReserva)
						nro_vac = nro_vac + 1;
					tipoCronograma = "AS";
				}
				else if(nuevo_con_cronograma || nuevo_sin_cronograma){
					/*Integer matriculados =getAlumnosMatriculadosxAula(id_au);
					Integer reseva = getReservasxAula(id_au, id_anio);
					boolean tieneReserva = tieneReservasxAula(id_au,id_alu);
					nro_vac=capacidad -matriculados -reseva;
					if (tieneReserva)
						nro_vac = nro_vac + 1;
					tipoCronograma = "NC";*/
				}
				
						
				AulaCapacidad aulaCapacidad = new AulaCapacidad();
				aulaCapacidad.setCapacidad(capacidad);
				aulaCapacidad.setNro_vac(nro_vac);
				aulaCapacidad.setId_au_sug(id_au_sug);
				aulaCapacidad.setTip_cronograma(tipoCronograma);
				return aulaCapacidad;
			} else { //Hayamos la capacidad en base a la modalidad
				Integer capacidad=getCapacidadxGradoCicloyModalidad(ciclo.getId(), id_grad, id_cme);
				if(capacidad==null) {
					AulaCapacidad aulaCapacidad = new AulaCapacidad();
					aulaCapacidad.setCapacidad(0);
					aulaCapacidad.setNro_vac(0);
					aulaCapacidad.setId_au_sug(0);
					aulaCapacidad.setTip_cronograma("N");
					return aulaCapacidad;
				}
				Integer nro_vac=0;
				if(antiguo_con_cronograma){
					//ObTENGO LOS ALUMNOS CON  AULA SUGERIDA y que no esten matriculados no reservados
					//Integer  reservasxGrado = getReservasxGradoyModalidad(periodo.getId(), id_grad, id_cme);
					Integer matriculados =getAlumnosMatriculadosxGradoyModalidad(id_anio, id_grad, id_suc,id_cme);
					//nro_vac=capacidad -sugeridos -reservasxAula-matriculadosNoSugeridos;
					nro_vac=capacidad-matriculados;					
					tipoCronograma = "AC";
				}  else if(antiguo_sin_cronograma){
					//Integer  reservasxGrado = getReservasxGradoyModalidad(periodo.getId(), id_grad, id_cme);
					Integer matriculados =getAlumnosMatriculadosxGradoyModalidad(id_anio, id_grad, id_suc,id_cme);
					nro_vac=capacidad -matriculados ;
					//Por ahora no sabemos como se va a manejar las reservas
					//boolean tieneReserva = tieneReservasxGrado(id_grad, id_anio, id_alu);
					//if (tieneReserva)
					//	nro_vac = nro_vac + 1;
					tipoCronograma = "AS";
				}
				else if(nuevo_con_cronograma || nuevo_sin_cronograma){
					//Integer  reservasxGrado = getReservasxGradoyModalidad(periodo.getId(), id_grad, id_cme);
					Integer matriculados =getAlumnosMatriculadosxGradoyModalidad(id_anio, id_grad, id_suc,id_cme);
					nro_vac=capacidad -matriculados;
					tipoCronograma = "NC";
					/*Integer matriculados =getAlumnosMatriculadosxAula(id_au);
					Integer reseva = getReservasxAula(id_au, id_anio);
					boolean tieneReserva = tieneReservasxAula(id_au,id_alu);
					nro_vac=capacidad -matriculados -reseva;
					if (tieneReserva)
						nro_vac = nro_vac + 1;
					tipoCronograma = "NC";*/
				}
				
						
				AulaCapacidad aulaCapacidad = new AulaCapacidad();
				aulaCapacidad.setCapacidad(capacidad);
				aulaCapacidad.setNro_vac(nro_vac);
				//aulaCapacidad.setId_au_sug(id_au_sug);
				aulaCapacidad.setTip_cronograma(tipoCronograma);
				return aulaCapacidad;
			}
			
	
		} else{
			return null;
		}	
}
	
	public Integer getNroVacantesMatriculaAcadVac(Integer id_cic,Integer id_au, Integer id_grad) throws Exception{
		Integer nro_vac=0;
		if(id_au!=null) {
			Integer cap=getCapacidadxAula(id_au);
			Integer matriculados=vacanteDAO.getTotalMatriculadosAula(id_au);
			if(cap!=null && matriculados!=null)
				nro_vac=cap-matriculados;
		} else {
			Integer cap=getCapacidadxGradoCiclo(id_cic, id_grad);
			Integer matriculados=vacanteDAO.getTotalMatriculadosGradoCiclo(id_cic, id_grad);
			if(cap!=null && matriculados!=null)
			nro_vac=cap-matriculados;
		}		
		
		return nro_vac;
	}
	
	
}
