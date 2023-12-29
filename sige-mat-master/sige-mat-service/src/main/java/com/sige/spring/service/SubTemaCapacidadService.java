package com.sige.spring.service;


import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sige.common.enums.EnumTipoException;
import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.DesempenioDAO;
import com.sige.mat.dao.IndicadorDAO;
import com.sige.mat.dao.SesionDesempenioDAO;
import com.tesla.frmk.common.exceptions.ControllerException;
import com.tesla.frmk.common.exceptions.ServiceException;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;


@Service
public class SubTemaCapacidadService {

	
	@Autowired
	private SQLUtil sqlUtil;
	
	@Autowired
	private IndicadorDAO indicadorDAO;
	
	@Autowired
	private DesempenioDAO desempenioDAO;
	
	@Autowired
	private SesionDesempenioDAO sesionDesempenioDAO;
	
	@Transactional
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
			sql = "insert into col_grup_sub_padre(id_anio, est, usr_ins, fec_ins)values(?,'A',1,now())";
			
			sqlUtil.update(sql, new Object[]{id_anio});
			id_cgsp = sqlUtil.getLastInsertId();


			/**
			 * insertar col_grup_subtema
			 */
			for (int i=0;i< id_ccs.length;i++) {
			
				sql = "insert into col_grup_subtema(id_ccs, id_cgsp, est, usr_ins, fec_ins)values(?,?,'A',1,now())";
				sqlUtil.update(sql, new Object[]{id_ccs[i],id_cgsp});	
			}
		}
		
		
		
		/**
		 * insertar col_grup_capacidad
		 */
		sql = "insert into col_grup_capacidad(id_cap, id_cgsp, est, usr_ins, fec_ins)values(?,?,'A',1,now())";
		sqlUtil.update(sql, new Object[]{id_cap,id_cgsp});	
	
		return 1;
		
	}
	

	/**
	 * Debe enviar una estrucrta da detos para la realcion de subtemas, capacidades y desemepe�os
	 * Pantalla col_subtema_capacidad
	 * @param id_anio
	 * @param id_tem
	 * @return
	 */
	public List<Row> listSubtemaCapacidades(Integer id_anio,Integer id_tem, Integer id_niv, Integer id_gra, Integer id_cur ){
	
		
		/**
		 * GRUPO DE SUBTEMAS
		 */
		String sql = "select cgsp.id id_cgsp, count(*) cant"
				+ " from col_grup_subtema cgs "
				+ " inner join col_grup_sub_padre cgsp on cgsp.id= cgs.id_cgsp"
				+ " inner join col_curso_subtema ccs on ccs.id = cgs.id_ccs"
				+ " inner join col_subtema sub on sub.id = ccs.id_sub"
				+ " where cgsp.id_anio=? and sub.id_tem=? and ccs.id_niv=? and ccs.id_gra=? and ccs.id_cur=?"
				+ " group by 1";
		
		List<Row> gruposSubTemas = sqlUtil.query(sql, new Object[]{id_anio,id_tem,id_niv,id_gra,id_cur});
		
		for (Row grupoSubtema : gruposSubTemas) {
			
			/**
			 * SUBTEMAS
			 */
			sql = "select gs.id_ccs, sub.nom subtema from  col_grup_subtema gs "
					+ " inner join col_curso_subtema ccs on ccs.id = gs.id_ccs"
					+ " inner join col_subtema sub on ccs.id_sub = sub.id"
					+ " inner join col_tema tem on sub.id_tem= tem.id"
					+ " where gs.id_cgsp=? and ccs.id_niv=? and ccs.id_gra=? and ccs.id_cur=?";
			
			List<Row> listSubtemas = sqlUtil.query(sql, new Object[]{grupoSubtema.getInteger("id_cgsp"),id_niv,id_gra,id_cur});
			grupoSubtema.put("subtemas", listSubtemas);
			
			/**
			 * CAPACIDADES
			 */
			sql = "select gc.id, gc.id_cap, cap.nom capacidad, com.nom competencia, com.id id_com, gc.id_cgsp from  col_grup_capacidad gc "
					+ " inner join col_capacidad cap on cap.id = gc.id_cap"
					+ " inner join col_competencia com on com.id = cap.id_com"
					+ " where gc.id_cgsp=? and com.est='A' ";
			
			List<Row> listCapacidades = sqlUtil.query(sql, new Object[]{grupoSubtema.getInteger("id_cgsp")});
			
			for (Row rowCapacidad : listCapacidades) {
				
				/**
				 * DESEMPE�OS
				 */
				sql = "select d.id id_des, d.nom desempenio"
						+ " from  col_desempenio d inner join col_grup_capacidad cgc on d.id_cgc=cgc.id"
						+ " inner join col_grup_sub_padre cgsp on cgc.id_cgsp=cgsp.id "
						 	+ " where d.id_cgc=? and cgsp.id=?";
			
				List<Row> listDesempenios = sqlUtil.query(sql, new Object[]{rowCapacidad.getInteger("id"), rowCapacidad.getInteger("id_cgsp")});
				rowCapacidad.put("desempenios", listDesempenios);

			}
			
			grupoSubtema.put("capacidades", listCapacidades);
		}
		
		return gruposSubTemas;
	}
	
	@Transactional
	public Integer eliminarAgrupacion(Integer id_cgc) throws Exception{
		List<Row> sesiones_unidad = sesionDesempenioDAO.listaSesionesxGrupoCapacidad(id_cgc);
		if(sesiones_unidad.size()==0){
			//Eliminamos los desempenios q tienen el grupo capacidad
			desempenioDAO.deletexGrupoCapacidad(id_cgc);;
			//Sacamos el id de col_grup_sub_padre
			Param param = new Param();
			param.put("id", id_cgc);
			String sql = "select id_cgsp from col_grup_capacidad where id=:id";
			Integer id_cgsp=sqlUtil.queryForObject(sql, param, Integer.class);
			//Eliminamos col_grup_subtema y col_grup_capacidad
			sql="delete from col_grup_capacidad where id_cgsp="+id_cgsp;
			sqlUtil.update(sql);
			sql="delete from col_grup_subtema where id_cgsp="+id_cgsp;
			sqlUtil.update(sql);
			//Eliminamos col_grup_sub_padre
			sql="delete from col_grup_sub_padre where id="+id_cgsp;
			sqlUtil.update(sql);
		}
		else
			throw new ControllerException("Primero debe eliminar las sesiones e indicadores asigandos a este grupo", EnumTipoException.WARNING);
		return 1;
	}
	
}
