package com.sige.mat.dao;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.sige.mat.dao.impl.GradoHorarioDAOImpl;

/**
 * Define m�todos DAO operations para la entidad grado_horario.
 * 
 * @author MV
 *
 */
@Repository
public class GradoHorarioDAO extends GradoHorarioDAOImpl {
	final static Logger logger = Logger.getLogger(GradoHorarioDAO.class);

	public List<Map<String, Object>> horarioList(Integer id_anio) {

		String sql = "SELECT agh.id,anio.nom anio,suc.nom local, niv.nom nivel, concat(gra.nom,' - ', au.secc) aula, agh.hora_ini, agh.hora_fin, agh.hora_ini_aux, agh.hora_fin_aux"
				+ " FROM asi_grado_horario agh" + " LEFT JOIN col_aula au ON agh.id_au=au.id"
				+ " LEFT JOIN col_anio anio ON agh.id_anio=anio.id" + " LEFT JOIN cat_grad gra ON au.id_grad=gra.id"
				+ " LEFT JOIN per_periodo per ON per.id_anio=anio.id AND au.id_per=per.id"
				+ " LEFT JOIN ges_servicio ser ON per.id_srv= ser.id"
				+ " LEFT JOIN ges_sucursal suc ON ser.id_suc=suc.id" + " LEFT JOIN cat_nivel niv ON ser.id_niv=niv.id"
				+ " WHERE agh.id_anio=" + id_anio + " ORDER BY niv.nom ASC, gra.id ASC, au.secc ASC";
		List<Map<String, Object>> aulaHorarioList = jdbcTemplate.queryForList(sql);
		logger.info(sql);
		return aulaHorarioList;
		
		/*
		 * 
		String sql = "SELECT agh.id,anio.nom anio,suc.nom local, niv.nom nivel, concat(gra.nom,' - ', au.secc) aula, agh.hora_ini, agh.hora_fin, agh.hora_ini_aux, agh.hora_fin_aux"
					+ " FROM asi_grado_horario agh"
					+ " LEFT JOIN col_aula au ON agh.id_au=au.id"
					+ " LEFT JOIN col_anio anio ON agh.id_anio=anio.id"
					+ " LEFT JOIN cat_grad gra ON au.id_grad=gra.id"
					+ " LEFT JOIN per_periodo per ON per.id_anio=anio.id AND au.id_per=per.id"
					+ " LEFT JOIN ges_servicio ser ON per.id_srv= ser.id"
					+ " LEFT JOIN ges_sucursal suc ON ser.id_suc=suc.id"
					+ " LEFT JOIN cat_nivel niv ON ser.id_niv=niv.id"
					+ " WHERE agh.id_anio="+id_anio
					+ " ORDER BY niv.nom ASC, gra.id ASC, au.secc ASC";
		 * 
		 */
	}
}
