package com.sige.spring.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sige.common.enums.EnumTipoException;
import com.sige.common.enums.EnumTipoSesion;
import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.AreaAnioDAO;
import com.sige.mat.dao.CapacidadDAO;
import com.sige.mat.dao.ConfSemanasDAO;
import com.sige.mat.dao.CursoSesionDAO;
import com.sige.mat.dao.CursoUnidadDAO;
import com.sige.mat.dao.DesempenioDAO;
import com.sige.mat.dao.EvaPadreDAO;
import com.sige.mat.dao.EvaluacionDAO;
import com.sige.mat.dao.IndEvaDAO;
import com.sige.mat.dao.IndicadorDAO;
import com.sige.mat.dao.PerUniDetDAO;
import com.sige.mat.dao.SesionDesempenioDAO;
import com.sige.mat.dao.SesionTipoDAO;
import com.sige.mat.dao.SubtemaDAO;
import com.sige.mat.dao.UnidadSesionDAO;
import com.tesla.colegio.model.CursoSesion;
import com.tesla.colegio.model.CursoUnidad;
import com.tesla.colegio.model.Evaluacion;
import com.tesla.colegio.model.Indicador;
import com.tesla.colegio.model.SesionDesempenio;
import com.tesla.colegio.model.SesionTipo;
import com.tesla.colegio.model.UnidadSesion;
import com.tesla.colegio.util.Constante;
import com.tesla.frmk.common.exceptions.ControllerException;
import com.tesla.frmk.common.exceptions.DAOException;
import com.tesla.frmk.common.exceptions.ServiceException;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;
import com.tesla.frmk.util.FechaUtil;
import com.tesla.frmk.util.StringUtil;
 


@Service
public class UnidadSesionService {

	final static Logger logger = Logger.getLogger(UnidadSesionService.class);

	@Autowired
	private SQLUtil sqlUtil;
	
	@Autowired
	private CursoUnidadDAO cursoUnidadDAO;
	
	@Autowired
	private PerUniDetDAO perUniDetDAO;
	
	@Autowired
	private CursoSesionDAO cursoSesionDAO;
	
	@Autowired
	private AreaAnioDAO areaAnioDAO;
	
	@Autowired
	private UnidadSesionDAO unidadSesionDAO;
	
	@Autowired
	private SesionTipoDAO sesionTipoDAO;
	
	@Autowired
	private SubtemaDAO subtemaDAO;
	
	@Autowired
	private DesempenioDAO desempenioDAO;

	@Autowired
	private IndicadorDAO indicadorDAO;

	@Autowired
	private UnidadSesionDAO unidad_sesionDAO;
	
	@Autowired
	private SesionDesempenioDAO sesionDesempenioDAO;
	
	@Autowired
	private CapacidadDAO capacidadDAO;
	
	@Autowired
	private EvaluacionDAO evaluacionDAO;
	
	@Autowired
	private EvaPadreDAO evaPadreDAO;
	
	@Autowired
	private IndEvaDAO indEvaDAO;
	
	@Autowired
	private ConfSemanasDAO confSemanasDAO;
	
	/**
	 * Es llamado desde creacion de unidades
	 * 
	 * @param curso_unidad
	 * @param id_niv
	 * @param id_gra
	 * @param id_cur
	 * @param id_cpu
	 * @param id_anio
	 * @return
	 * @throws ServiceException
	 */
	@Transactional
	public List<Row> grabarSesionesxUnidad(CursoUnidad curso_unidad, Integer id_niv, Integer id_gra, Integer id_cur,Integer id_cpu, Integer id_anio) throws ServiceException{
		//grabo la unidad
		Integer id_uni=cursoUnidadDAO.saveOrUpdate(curso_unidad);
		List<Row> sesiones = new ArrayList<Row>();
		if(curso_unidad.getId()==null){
			//Obtengo el numero de unidad
			Integer nro_uni=cursoUnidadDAO.getByParams(new Param("id",id_uni)).getNum();
			Param param = new Param();
			param.put("id_cpu", id_cpu);
			param.put("ord", nro_uni);
			//Obtengo el numero de semanas para la unidad
			Integer nro_sem= perUniDetDAO.getByParams(param).getNro_sem();
			//Obtengo el id del curso_area_anio, para luego obtener el numero de veces q se lleva el curso en la semana
			Integer id_caa=areaAnioDAO.obtenerDatos(id_gra, id_cur, id_anio).get(0).getInteger("id");
			Param param1 = new Param();
			param1.put("id_gra", id_gra);
			param1.put("id_caa", id_caa);
			param1.put("id_cur", id_cur);
			CursoSesion sesion  =cursoSesionDAO.getByParams(param1);// nro de clases del curso a la semana
			Integer nro_cla=null;
			if(sesion==null){
				throw new ServiceException("Falta configurar el n�mero de sesiones del curso, porfavor comun�quese con el Administrador del Sistema!!", EnumTipoException.WARNING);
			} else {
				nro_cla=cursoSesionDAO.getByParams(param1).getNro_ses();// nro de clases del curso a la semana
			}				
			
			//Obtengo el numero de sesione
			Integer nro_ses=nro_sem*nro_cla;
		
			for (int i = 1; i <= nro_ses; i++) {
				Row row = new Row();
				UnidadSesion unidad = new UnidadSesion();
				unidad.setId_uni(id_uni);
				unidad.setNro(i);
				unidad.setEst("A");
				int id_uns = unidadSesionDAO.saveOrUpdate(unidad);
				
				row.put("nro", unidad.getNro());
				row.put("id_cus", id_uns);
				sesiones.add(row);
			}	
		}	
		
		return sesiones;
		
	}

	/**lista de sesiones
	 */
	public List<Row> listarSesionesxUnidad(Integer id_uni) throws ServiceException{
		
		Param param = new Param();
		param.put("id_uni", id_uni);
		param.put("est", "A");
		
		List<UnidadSesion> sesiones_list = unidadSesionDAO.listByParams(param, new String[]{"nro"});
		List<Row> sesiones = new ArrayList<Row>();
		
		int nro_session = 0;
		for (UnidadSesion unidadSesion : sesiones_list) {
			nro_session ++;
			
			logger.debug("nro_session:" + nro_session);
			
			Row row = new Row();
			row.put("id", unidadSesion.getId());
			row.put("nro", unidadSesion.getNro());
			row.put("nom", unidadSesion.getNom());

			List<Row> sesiones_tipo = sesionTipoDAO.listarSesionesTipo(unidadSesion.getId());
			row.put("clases", sesiones_tipo);
			sesiones.add(row);
			
			for (Row sesion_tipo : sesiones_tipo) {
				sesion_tipo.put("id_cts", sesion_tipo.get("id_cts"));
				
				int id_cts=sesion_tipo.getInt("id_cts");//ID DEL TIPO DE SESION EXAMEN, CLASE, REPASO				
				List<Row> grupo_subtema = new ArrayList<>();
				if(EnumTipoSesion.CLASE.getValue()==id_cts || EnumTipoSesion.REPASO.getValue()==id_cts){
					 grupo_subtema = sesionTipoDAO.listarGrupoSubtemaxClase(sesion_tipo.getInteger("id"));	
				} else if(EnumTipoSesion.EXAMEN.getValue()==id_cts){
					 grupo_subtema = sesionTipoDAO.listarGrupoSubtemaxExamen(sesion_tipo.getInteger("id"));
				}
				
				sesion_tipo.put("grupos", grupo_subtema);
				for (Row grupo : grupo_subtema) {

					//lista de subtemas
					List<Row> subtemas = subtemaDAO.listarSubtemasxGrupo(grupo.getInteger("id_cgsp"));
					grupo.put("subtemas", subtemas);
					
					//lista de capacidades
					List<Row> capacidades = null;
					if(EnumTipoSesion.CLASE.getValue()==id_cts || EnumTipoSesion.REPASO.getValue()==id_cts){
						capacidades = capacidadDAO.listaCapacidadxGrupo(sesion_tipo.getInteger("id"),grupo.getInteger("id_cgsp"));	
					} else if(EnumTipoSesion.EXAMEN.getValue()==id_cts){
						capacidades = capacidadDAO.listaCapacidadxGrupoparaExamen(sesion_tipo.getInteger("id"),grupo.getInteger("id_cgsp"));
					}
					if(capacidades!=null)
					for (Row rowCap : capacidades) {
						List<Row> desempenios = new ArrayList<>();
						if(EnumTipoSesion.CLASE.getValue()==id_cts || EnumTipoSesion.REPASO.getValue()==id_cts){
							desempenios = desempenioDAO.listaDesempeniosxGrupo(grupo.getInteger("id_cgsp"), rowCap.getInteger("id_cap"),sesion_tipo.getInteger("id"));	
						} else if(EnumTipoSesion.EXAMEN.getValue()==id_cts){
							desempenios = desempenioDAO.listaDesempeniosxGrupoparaExamen(grupo.getInteger("id_cgsp"), rowCap.getInteger("id_cap"),sesion_tipo.getInteger("id"));
						}
						for (Row row2 : desempenios) {
							param = new Param();
							param.put("id_csd", row2.getInteger("id"));
							param.put("est", "A");
							List<Indicador> indicadores = indicadorDAO.listByParams(param, new String[]{"id"});
							List<Row> rindicadores = new ArrayList<Row>();
							for (Indicador indicador : indicadores) {
								Row rIndidicador = new Row();
								rIndidicador.put("id", indicador.getId());
								rIndidicador.put("nom", indicador.getNom());
								rindicadores.add(rIndidicador);
							}
							row2.put("indicadores", rindicadores);
						}
						rowCap.put("desempenios", desempenios);

						
					}
					
					grupo.put("capacidades", capacidades);

				}
			}
		}
		
		
		
		//consulta de sesiones
		return sesiones;
	}

	/**Validar Sesiones Completas
	 */
	@Transactional
	public List<Row> listarSesionesCompletasxUnidad(Integer id_uni) throws ServiceException{
		
		Param param = new Param();
		param.put("id_uni", id_uni);
		param.put("est", "A");
		
		List<UnidadSesion> sesiones_list = unidadSesionDAO.listByParams(param, new String[]{"nro"});
		List<Row> sesiones = new ArrayList<Row>();
		
		int nro_session = 0;
		for (UnidadSesion unidadSesion : sesiones_list) {
			nro_session ++;
			
			logger.debug("nro_session:" + nro_session);
			
			Row row = new Row();
			row.put("id", unidadSesion.getId());
			row.put("nro", unidadSesion.getNro());
			row.put("nom", unidadSesion.getNom());

			List<Row> sesiones_tipo = sesionTipoDAO.listarSesionesTipo(unidadSesion.getId());
			row.put("clases", sesiones_tipo);
			sesiones.add(row);
			if(sesiones_tipo.size()==0)
				throw new ServiceException("Por favor completar las sesiones de la unidad, para poder generar el reporte!!",EnumTipoException.WARNING);
			for (Row sesion_tipo : sesiones_tipo) {
				Integer id_ses =sesion_tipo.getInteger("id"); 
				int id_cts=sesion_tipo.getInt("id_cts");
				/*sesion_tipo.put("nro", sesion_tipo.getInteger("nro")); 
				sesion_tipo.put("id", id_ses);
				sesion_tipo.put("tipo", sesion_tipo.getString("tipo"));*/

				if(EnumTipoSesion.CLASE.getValue()==id_cts || EnumTipoSesion.REPASO.getValue()==id_cts){
					 // indicadores por sesion
					 List<Row> indicadores = cursoUnidadDAO.listaIndicadoresxSesionClase(id_ses);
					 if (indicadores.size() <= 0)
							throw new ServiceException(
									"No se puede configurar la siguiente unidad, porque falta definir los indicadores de la unidad anterior!!",
									EnumTipoException.WARNING);
					 sesion_tipo.put("indicadores", indicadores);
					 //temas-subtemas por sesion
					 List<Row> temas= cursoUnidadDAO.listaSubtemasxSesionClase(id_ses);
					 sesion_tipo.put("temas", temas);
				} else if(EnumTipoSesion.EXAMEN.getValue()==id_cts){
					 // indicadores por sesion
					 List<Row> indicadores = cursoUnidadDAO.listaIndicadoresxSesionExamen(id_ses);
					 if (indicadores.size() <= 0)
							throw new ServiceException(
									"No se puede configurar la siguiente unidad, porque falta definir los indicadores de la unidad anterior!!",
									EnumTipoException.WARNING);
					 sesion_tipo.put("indicadores", indicadores);
					 //temas-subtemas por sesion
					 List<Row> temas= cursoUnidadDAO.listaSubtemasxSesionExamen(id_ses);
					 sesion_tipo.put("temas", temas);
				}
				

				
			}
		}
		//consulta de sesiones
		return sesiones;
	}
	
	@Transactional
	public Integer validarSesionesVinculadas(Integer id_uni, Integer id_tra, Integer id_anio) throws ServiceException{
		
		Integer cant_ses_conf = cursoUnidadDAO.cantidadSesionesConfiguradas(id_uni, id_tra, id_anio);
		Integer cant_ses_vin=cursoUnidadDAO.cantidadSesionesVinculadas(id_uni, id_tra);
		if(cant_ses_conf.intValue()!=cant_ses_vin.intValue()){
			throw new ServiceException(
					"No ha vinculado todas las sesiones con el respectivo horario, no se puede generar el reporte de Unidad!!",
					EnumTipoException.WARNING);
		}
		return 1;
	}
	/**lista de sesiones
	 */
	@Transactional
	public List<Row> listarSesionesxUnidadPDF(Integer id_uni) throws ServiceException{
		
		Param param = new Param();
		param.put("id_uni", id_uni);
		param.put("est", "A");
		
		List<Row> sesiones_list = unidadSesionDAO.obtenerDatos(id_uni);
		List<Row> sesiones = new ArrayList<Row>();
		Integer cant_ses = sesiones_list.size();
		int sem_ini=sesiones_list.get(0).getInt("sem_ini");
		int sem_fin=sesiones_list.get(0).getInt("sem_fin");
		int dif=sem_fin-sem_ini+1;
		int nro_session = 0;
		if(cant_ses.intValue()==dif){
			for (Row unidadSesion : sesiones_list) {
				nro_session ++;
				//antes sem_ini ++;
				logger.debug("nro_session:" + nro_session);
				
				Row row = new Row();
				row.put("id", unidadSesion.getInt("id"));
				row.put("nro", unidadSesion.getInteger("nro"));
				row.put("nom", unidadSesion.get("nom"));
				
				Date fec_ini = confSemanasDAO.getByParams(new Param("id",sem_ini)).getFec_ini();
				Date fec_fin = confSemanasDAO.getByParams(new Param("id",sem_ini)).getFec_fin();
				row.put("fec_ini", FechaUtil.toString(fec_ini));
				row.put("fec_fin",  FechaUtil.toString(fec_fin));	 
				sem_ini ++;
				List<Row> sesiones_tipo = sesionTipoDAO.listarSesionesTipo(unidadSesion.getInt("id"));
				row.put("clases", sesiones_tipo);
				sesiones.add(row);
				
				for (Row sesion_tipo : sesiones_tipo) {
					Integer id_ses =sesion_tipo.getInteger("id"); 
					int id_cts=sesion_tipo.getInt("id_cts");
					/*sesion_tipo.put("nro", sesion_tipo.getInteger("nro")); 
					sesion_tipo.put("id", id_ses);
					sesion_tipo.put("tipo", sesion_tipo.getString("tipo"));*/

					if(EnumTipoSesion.CLASE.getValue()==id_cts || EnumTipoSesion.REPASO.getValue()==id_cts){
						 // indicadores por sesion
						 List<Row> indicadores = cursoUnidadDAO.listaIndicadoresxSesionClase(id_ses);
						 //capitalizar
						 for (Row row2 : indicadores) {
							 row2.put("indicador", StringUtil.capitalize(row2.getString("indicador")));
						}
						 if (indicadores.size() <= 0)
								throw new ServiceException(
										"No se puede generar el reporte de la Unidad, porque falta definir indicadores!!",
										EnumTipoException.WARNING);
						 sesion_tipo.put("indicadores", indicadores);
						 //temas-subtemas por sesion
						 List<Row> temas= cursoUnidadDAO.listaSubtemasxSesionClase(id_ses);
						 sesion_tipo.put("temas", temas);
					} else if(EnumTipoSesion.EXAMEN.getValue()==id_cts){
						 // indicadores por sesion
						 List<Row> indicadores = cursoUnidadDAO.listaIndicadoresxSesionExamen(id_ses);
						//capitalizar
						 for (Row row2 : indicadores) {
							 row2.put("indicador", StringUtil.capitalize(row2.getString("indicador")));
						}
						 if (indicadores.size() <= 0)
								throw new ServiceException(
										"No se puede generar el reporte de la Unidad, porque falta definir indicadores!!",
										EnumTipoException.WARNING);
						 sesion_tipo.put("indicadores", indicadores);
						 //temas-subtemas por sesion
						 List<Row> temas= cursoUnidadDAO.listaSubtemasxSesionExamen(id_ses);
						 sesion_tipo.put("temas", temas);
					}
					

					
				}
			}
					
		} else{
			int cant_ses_sem=cant_ses/dif;
			int cont=1;
			for (Row unidadSesion : sesiones_list) {
				nro_session ++;
				
				if(cont>cant_ses_sem){
					sem_ini ++;
					cont=1;
				}
				cont++;
				logger.debug("nro_session:" + nro_session);
				
				Row row = new Row();
				row.put("id", unidadSesion.getInt("id"));
				row.put("nro", unidadSesion.getInteger("nro"));
				row.put("nom", unidadSesion.get("nom"));
				
				Date fec_ini = confSemanasDAO.getByParams(new Param("id",sem_ini)).getFec_ini();
				Date fec_fin = confSemanasDAO.getByParams(new Param("id",sem_ini)).getFec_fin();
				row.put("fec_ini", FechaUtil.toString(fec_ini));
				row.put("fec_fin", FechaUtil.toString(fec_fin));	 

				List<Row> sesiones_tipo = sesionTipoDAO.listarSesionesTipo(unidadSesion.getInt("id"));
				row.put("clases", sesiones_tipo);
				sesiones.add(row);
				
				for (Row sesion_tipo : sesiones_tipo) {
					Integer id_ses =sesion_tipo.getInteger("id"); 
					int id_cts=sesion_tipo.getInt("id_cts");
					/*sesion_tipo.put("nro", sesion_tipo.getInteger("nro")); 
					sesion_tipo.put("id", id_ses);
					sesion_tipo.put("tipo", sesion_tipo.getString("tipo"));*/

					if(EnumTipoSesion.CLASE.getValue()==id_cts || EnumTipoSesion.REPASO.getValue()==id_cts){
						 // indicadores por sesion
						 List<Row> indicadores = cursoUnidadDAO.listaIndicadoresxSesionClase(id_ses);
						 //capitalizar
						 for (Row row2 : indicadores) {
							 row2.put("indicador", StringUtil.capitalize(row2.getString("indicador")));
						}
						 if (indicadores.size() <= 0)
								throw new ServiceException(
										"No se puede generar el reporte de la Unidad, porque falta definir indicadores!!",
										EnumTipoException.WARNING);
						 sesion_tipo.put("indicadores", indicadores);
						 //temas-subtemas por sesion
						 List<Row> temas= cursoUnidadDAO.listaSubtemasxSesionClase(id_ses);
						 sesion_tipo.put("temas", temas);
					} else if(EnumTipoSesion.EXAMEN.getValue()==id_cts){
						 // indicadores por sesion
						 List<Row> indicadores = cursoUnidadDAO.listaIndicadoresxSesionExamen(id_ses);
						//capitalizar
						 for (Row row2 : indicadores) {
							 row2.put("indicador", StringUtil.capitalize(row2.getString("indicador")));
						}
						 if (indicadores.size() <= 0)
								throw new ServiceException(
										"No se puede generar el reporte de la Unidad, porque falta definir indicadores!!",
										EnumTipoException.WARNING);
						 sesion_tipo.put("indicadores", indicadores);
						 //temas-subtemas por sesion
						 List<Row> temas= cursoUnidadDAO.listaSubtemasxSesionExamen(id_ses);
						 sesion_tipo.put("temas", temas);
					}
					

					
				}
			}
		}

		
		
		
		//consulta de sesiones
		return sesiones;
	}

	
	
	/*@Transactional
	public Integer grabarSubtemasCapacidad(Integer[] id_ccs,Integer id_cap,Integer id_anio) throws ServiceException{
		
		
		Param param = new Param();
		param.put("id_ccs", Arrays.asList(id_ccs));
		param.put("id_anio", id_anio);
		
		String sql = "select cgsp.id from col_grup_subtema cgs "
				+ " inner join col_grup_sub_padre cgsp on cgsp.id = cgs.id_cgsp "
				+ " where cgs.id_ccs in (:id_ccs) and cgsp.id_anio=:id_anio ";
		
		List<Row> agrupador_subtemas = sqlUtil.query(sql,param);
		
		Integer id_cgsp = null;
		if (agrupador_subtemas.size()>0){
		
			//validar que esten correctamente agrupados
			if (agrupador_subtemas.size()!= id_ccs.length){
				throw new ServiceException("Los subtemas seleccionados no corresponden a un grupo de sub-temas validos");
			}
			//validar que el papa tenga los mismos hijos
			sql  = "select cgsp.id, id_ccs, sub.nom subtema from col_grup_subtema cgs "
					+ " inner join col_grup_sub_padre cgsp on cgsp.id = cgs.id_cgsp "
					+ " inner join col_curso_subtema ccs on ccs.id = cgs.id_ccs"
					+ " inner join col_subtema sub on sub.id = ccs.id_sub"
					+ " where cgsp.id= ?";
			List<Row> hijosSubtema = sqlUtil.query(sql,new Object[]{agrupador_subtemas.get(0).getInteger("id")});
			for (Row row : hijosSubtema) { 
					if (!Arrays.stream(id_ccs).anyMatch(row.getInteger("id_ccs")::equals)){
						String grupoDeSubtemas = "";
						for (Row row1 : hijosSubtema) {
							grupoDeSubtemas += "," + row1.getString("subtema"); 
						}
						grupoDeSubtemas = grupoDeSubtemas.substring(1);
						throw new ServiceException("Los subtemas seleccionados no corresponden a un grupo de sub-temas creados anteriormente.<br>Ejemplo: Una agrupaci�n v�lida es [" + grupoDeSubtemas + "]");
					}
			}
			id_cgsp = agrupador_subtemas.get(0).getInteger("id");
			
		}else{

			/** 
			 * insertar col_grup_sub_padre
			 */
		/*	sql = "insert into col_grup_sub_padre(id_anio, est, usr_ins, fec_ins)values(?,'A',1,now())";
			
			sqlUtil.update(sql, new Object[]{id_anio});
			id_cgsp = sqlUtil.getLastInsertId();


			/**
			 * insertar col_grup_subtema
			 */
		/*	for (int i=0;i< id_ccs.length;i++) {
			
				sql = "insert into col_grup_subtema(id_ccs, id_cgsp, est, usr_ins, fec_ins)values(?,?,'A',1,now())";
				sqlUtil.update(sql, new Object[]{id_ccs[i],id_cgsp});	
			}
		}
		
		
		
		/**
		 * insertar col_grup_capacidad
		 */
	/*	sql = "insert into col_grup_capacidad(id_cap, id_cgsp, est, usr_ins, fec_ins)values(?,?,'A',1,now())";
		sqlUtil.update(sql, new Object[]{id_cap,id_cgsp});	
	
		return 1;
		
	}*/
	

	/**
	 * Debe enviar una estrucrta da detos para la realcion de subtemas, capacidades y desemepe�os
	 * Pantalla col_subtema_capacidad
	 * @param id_anio
	 * @param id_tem
	 * @return
	 */
	public List<Row> listSubtemaCapacidades(Integer id_anio,Integer id_tem ){
	
		
		/**
		 * GRUPO DE SUBTEMAS
		 */
		String sql = "select cgsp.id id_cgsp, count(*) cant"
				+ " from col_grup_subtema cgs "
				+ " inner join col_grup_sub_padre cgsp on cgsp.id= cgs.id_cgsp"
				+ " inner join col_curso_subtema ccs on ccs.id = cgs.id_ccs"
				+ " inner join col_subtema sub on sub.id = ccs.id_sub"
				+ " where cgsp.id_anio=? and sub.id_tem=?"
				+ " group by 1";
		
		List<Row> gruposSubTemas = sqlUtil.query(sql, new Object[]{id_anio,id_tem});
		
		for (Row grupoSubtema : gruposSubTemas) {
			
			/**
			 * SUBTEMAS
			 */
			sql = "select gs.id_ccs, sub.nom subtema from  col_grup_subtema gs "
					+ " inner join col_curso_subtema ccs on ccs.id = gs.id_ccs"
					+ " inner join col_subtema sub on ccs.id_sub = sub.id"
					+ " inner join col_tema tem on sub.id_tem= tem.id"
					+ " where gs.id_cgsp=?";
			
			List<Row> listSubtemas = sqlUtil.query(sql, new Object[]{grupoSubtema.getInteger("id_cgsp")});
			grupoSubtema.put("subtemas", listSubtemas);
			
			/**
			 * CAPACIDADES
			 */
			sql = "select gc.id, gc.id_cap, cap.nom capacidad, com.nom competencia, com.id id_com from  col_grup_capacidad gc "
					+ " inner join col_capacidad cap on cap.id = gc.id_cap"
					+ " inner join col_competencia com on com.id = cap.id_com"
					+ " where gc.id_cgsp=?";
			
			List<Row> listCapacidades = sqlUtil.query(sql, new Object[]{grupoSubtema.getInteger("id_cgsp")});
			
			for (Row rowCapacidad : listCapacidades) {
				
				/**
				 * DESEMPE�OS
				 */
				sql = "select d.id id_des, d.nom desempenio"
						+ " from  col_desempenio d "
						 	+ " where d.id_cgc=?";
			
				List<Row> listDesempenios = sqlUtil.query(sql, new Object[]{rowCapacidad.getInteger("id")});
				rowCapacidad.put("desempenios", listDesempenios);

			}
			
			grupoSubtema.put("capacidades", listCapacidades);
		}
		
		return gruposSubTemas;
	}
	
	/**
	 * Para agregar el tipo de sesio, junto con los desempe�os, usado en Unidad Sesion
	 * @param unidad_sesion
	 * @param id_uns
	 * @param id_cde
	 * @param sesionTipo
	 * @param sesionDesempenio
	 * @return
	 */
	@Transactional
	public Integer agregarClase(UnidadSesion unidad_sesion, Integer id_uns, Integer id_cde[],SesionTipo sesionTipo, SesionDesempenio sesionDesempenio){
		//Grababamos la sesi�n
		unidad_sesion.setId(id_uns);
		unidad_sesionDAO.saveOrUpdate(unidad_sesion);
		Integer id_ses_tip=null;
		if(id_uns!=null){
			Param param = new Param();
			param.put("uns.id", id_uns);
			param.put("ses.id_cts", sesionTipo.getId_cts());
			List<UnidadSesion> sesion_tipo=unidad_sesionDAO.listFullByParams(param, null);
			if(sesion_tipo.size()>0)
			id_ses_tip=sesion_tipo.get(0).getSesionTipo().getId();
		}
		
		if(id_ses_tip!=null)
		sesionTipo.setId(id_ses_tip);
		sesionTipo.setId_uns(id_uns);
		sesionTipo.setEst("A");
		int id_ses=sesionTipoDAO.saveOrUpdate(sesionTipo);
		if(sesionTipo.getId_cts()!=Constante.TIPO_SESION_EXAMEN){
			for (int i = 0; i < id_cde.length; i++) {
				sesionDesempenio.setId_ses(id_ses);
				sesionDesempenio.setId_cde(id_cde[i]);
				sesionDesempenio.setEst("A");
				sesionDesempenioDAO.saveOrUpdate(sesionDesempenio);
			}	
		}		
		return 1;
	}
	
	/**
	 * Eliminamos indicadores, desempe�os y tipo para una unidad_sesion respectiva, usada en la pantalla de unidad sesion de indicadores
	 * @param id_uns
	 * @return
	 */
	@Transactional
	public Integer eliminarClase(Integer id_ses){
		List<SesionDesempenio> sesion_desempenio = sesionDesempenioDAO.listByParams(new Param("id_ses",id_ses), null);
		for (SesionDesempenio sesionDesempenio : sesion_desempenio) {
			//Por cada desempe�o eliminamos el indicador
			indicadorDAO.deletexSesion(sesionDesempenio.getId());
			//Eliminamos el desempe�o
			sesionDesempenioDAO.delete(sesionDesempenio.getId());
		}
		//Eliminamos el tipo
		sesionTipoDAO.delete(id_ses);
		return 1;
	}
	
	/**
	 * Elimar los indicadores de la evaluaci�n, la evaluacion y el padre
	 * @param id_ses
	 * @return
	 */
	@Transactional
	public Integer eliminarEvaluaciones(Integer id_ses)throws DAOException{
		List<Evaluacion> evaluacion = evaluacionDAO.listByParams(new Param("id_ses", id_ses),null);
		if(evaluacion.size()>0){
			Integer id_nep=evaluacion.get(0).getId_nep();
			//Borramos los indicadores por evaluacion
			for (Evaluacion evaluacion2 : evaluacion) {
				indEvaDAO.delete(evaluacion2.getId());
			}
			//Borramos las evaluaciones
			evaluacionDAO.delete(id_ses);
			//Borramos el papa de la evaluacion
			evaPadreDAO.delete(id_nep);
			//Borramos el TIPO Examen a la sesi�n
		}		
		sesionTipoDAO.delete(id_ses);
		
		return 1;
	}
	
	
	/**
	 * Agregar Desempe�o
	 * @param id_ses
	 * @param id_cde
	 * @param sesionDesempenio
	 * @return
	 */
	@Transactional
	public Integer agregarDesempenio(Integer id_ses, Integer id_cde[],SesionDesempenio sesionDesempenio){
		for (int i = 0; i < id_cde.length; i++) {
			sesionDesempenio.setId_ses(id_ses);
			sesionDesempenio.setId_cde(id_cde[i]);
			sesionDesempenio.setEst("A");
			sesionDesempenioDAO.saveOrUpdate(sesionDesempenio);
		}
		return 1;
	}
	
	/**
	 * Eliminar DesempeniosxCapacidadyGrado
	 * @param id_cap
	 * @param id_cgsp
	 * @return
	 */
	@Transactional
	public Integer eliminarDesempenioxCapacidayGrupo(Integer id_cap, Integer id_cgsp) throws Exception{
		List<Row> indicadores = indicadorDAO.listaIndicadoresxCapcidadyGrupo(id_cap, id_cgsp);
		if(indicadores.size()==0)
		sesionDesempenioDAO.deleteDesempeniosxCapacidadyGrupo(id_cap, id_cgsp);
		else
			throw new ControllerException("Primero debe eliminar los indicadores asignados a esta capacidad, porfavor proceda a elimar.", EnumTipoException.WARNING);
		return 1;
	}
	
	
}
