package com.sige.mat.dao;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.sige.mat.dao.impl.HabilitacionDAOImpl;

/**
 * Define m�todos DAO operations para la entidad habilitacion.
 * @author MV
 *
 */
@Repository
public class HabilitacionDAO extends HabilitacionDAOImpl{
	final static Logger logger = Logger.getLogger(MatriculaDAO.class);

	public List<Map<String,Object>> listaAlumnos( String apellidosNombres,Integer id_anio,Integer id_suc) {
		String sql_suc="";
		if (id_suc!=null){
			sql_suc=" and per.id_suc="+id_suc;
		}
		String sql = "SELECT alu.`id` id_alu, CONCAT(alu.ape_pat,' ', alu.ape_mat,' ', alu.nom) label ,per.`id_anio` "
				+ " FROM `eva_matr_vacante` eva_mat INNER JOIN `eva_evaluacion_vac` eva ON `eva_mat`.`id_eva`=eva.`id`"
				+ " INNER JOIN `per_periodo` per ON eva.`id_per`=per.`id`"
				+ " INNER JOIN `alu_alumno` alu ON eva_mat.`id_alu`=alu.`id`"
				+ " WHERE per.`id_anio`="+id_anio+" "+sql_suc 
				+ " and ( upper(CONCAT(alu.ape_pat,' ',alu.ape_mat, ' ', alu.nom)) LIKE '%"+ apellidosNombres.toUpperCase()+"%'  ) "
				+ " order by alu.ape_pat,alu.ape_mat asc";
		
		List<Map<String,Object>> lista = jdbcTemplate.queryForList(sql);
		logger.info(sql);
		return lista;
	} 	
	
}
