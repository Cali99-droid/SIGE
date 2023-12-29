package com.sige.mat.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.sige.mat.dao.impl.EvaluacionVacExamenDAOImpl;
import com.tesla.colegio.util.Constante;


/**
 * Define m�todos DAO operations para la entidad evaluacion_vac_examen.
 * @author MV
 *
 */
@Repository
public class EvaluacionVacExamenDAO extends EvaluacionVacExamenDAOImpl{
	
	public List<Map<String,Object>> obtenerDatos(Integer id_exa, Integer tip_exa) {
		
		if(tip_exa==Constante.TIPO_EVALUACION_MARCACION){
			String sql ="SELECT eva_ex.`id_tae`, eva_ex.`id_eae`, eva_ex.`fec_exa`, eva_ex.`fec_not`, `eva_mar`.`pje_pre_cor`, eva_mar.`pje_pre_inc`, `eva_mar`.`num_pre`, eva_mar.id id_marcacion"
					+ " FROM `eva_evaluacion_vac_examen` eva_ex INNER JOIN eva_exa_conf_marcacion eva_mar ON eva_ex.`id`=eva_mar.`id_eva_ex`"
					+ " WHERE eva_ex.`id`="+id_exa;
			
			List<Map<String,Object>> datosExa = jdbcTemplate.queryForList(sql);	

			return datosExa;
		} else if(tip_exa==Constante.TIPO_EVALUACION_CRITERIO){
			String sql ="SELECT eva_ex.`id_tae`, eva_ex.`id_eae`, cri.`dur`, cri.`fec_ini_psi`, cri.`fec_fin_psi`, eva_ex.`id`, cri.id id_criterio  "
					+ " FROM `eva_evaluacion_vac_examen` eva_ex INNER JOIN `eva_exa_conf_criterio` cri  ON eva_ex.`id`=cri.`id_eva_ex` "
					+ " WHERE eva_ex.`id`="+id_exa;
			List<Map<String,Object>> datosExa = jdbcTemplate.queryForList(sql);	

			return datosExa;
		} else{
			return null;
		}
	} 
}
