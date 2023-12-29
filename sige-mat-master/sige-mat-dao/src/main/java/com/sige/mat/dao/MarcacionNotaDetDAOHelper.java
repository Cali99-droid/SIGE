package com.sige.mat.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;



/**
 * Define m�todos DAO operations para la entidad nota_vacante_det.
 * @author MV
 *
 */
@Repository
public class MarcacionNotaDetDAOHelper {
	final static Logger logger = Logger.getLogger(MarcacionNotaDetDAOHelper.class);

	@Autowired
	private DataSource dataSource;
	
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	/*@Override
	public List<Map<String,Object>> Notas_Vacante(Integer id_mat) {
		String sql = "SELECT a.id AS  'id_area', a.nom AS  'area', ex.pje_pre_cor, ex.pje_pre_inc,e.preg_favor, e.preg_contra, "
				+ "e.ptje,ev.ptje_total, m.id AS  'id_matri', m.id_alu, m.id_exa, ev.cond FROM eva_area_eva a"
				+ " LEFT JOIN eva_matr_vacante m ON ( m.ID ="+id_mat+") LEFT JOIN eva_marcacion_nota_det e ON ( a.id = e.id_eae and e.id_env = m.id)"
				+ " LEFT JOIN eva_examen ex ON (ex.id=m.id_exa) LEFT JOIN eva_marcacion_nota ev ON (ev.id_mat_vac=m.id)";
		List<Map<String,Object>> NotAlum = jdbcTemplate.queryForList(sql);			
		return NotAlum;
	}*/
	
	public List<Map<String,Object>> Notas_Vacante(Integer id_mat) {
		String sql = "SELECT DISTINCT a.id AS  'id_area', a.nom AS  'area',ev.preg_favor, ev.preg_contra, ev.ptje,m.id AS  'id_matri', m.id_alu, m.id_eva, "
				+ " ex_conf.pje_pre_cor, ex_conf.pje_pre_inc, ex_conf.id id_exa_mar, ex_conf.num_pre, eva.ptje_apro,ev.id id_mar_nota, e.fec_not"
				+ " FROM eva_area_eva a LEFT JOIN eva_matr_vacante m ON ( m.ID ="+id_mat+") "
				+ " LEFT JOIN eva_evaluacion_vac eva ON (eva.id=m.id_eva)"
				+ " LEFT JOIN eva_evaluacion_vac_examen e ON ( a.id = e.id_eae  AND e.id_eva=m.id_eva AND eva.id=e.id_eva)"
				+ " LEFT JOIN eva_exa_conf_marcacion ex_conf ON (e.id=ex_conf.id_eva_ex)"
				+ " LEFT JOIN eva_marcacion_nota ev ON (ev.id_mat_vac=m.id AND ex_conf.id=ev.id_exa_mar) WHERE a.id<>'3'";
		List<Map<String,Object>> NotAlum = jdbcTemplate.queryForList(sql);	
		logger.info(sql);
		return NotAlum;
	}
	
	public List<Map<String,Object>> EvaluacionList() {
		String sql = "SELECT distinct eva.id, eva.des, suc.nom local, ser.nom servicio"
				+ " FROM eva_evaluacion_vac eva, eva_tip_eva teva,eva_evaluacion_vac_examen eva_ex,eva_area_eva area, per_periodo p,ges_servicio ser, ges_sucursal suc"
				+ " where eva.id=eva_ex.id_eva and area.id=eva_ex.id_eae and eva.id_per=p.id and p.id_srv=ser.id and ser.id_suc=suc.id"
				+ " and eva_ex.id_tae=teva.id and teva.id<>'2'";
		List<Map<String,Object>> EvaluacionList = jdbcTemplate.queryForList(sql);			
		return EvaluacionList;
	}
	
	/*Lista de evaluaciones para psicologico y economico*/
	public List<Map<String,Object>> EvaluacionListfull() {
		String sql = "SELECT distinct eva.id, eva.des, suc.nom local, ser.nom servicio"
				+ " FROM eva_evaluacion_vac eva, eva_tip_eva teva,eva_evaluacion_vac_examen eva_ex,eva_area_eva area, per_periodo p,ges_servicio ser, ges_sucursal suc"
				+ " where eva.id=eva_ex.id_eva and area.id=eva_ex.id_eae and eva.id_per=p.id and p.id_srv=ser.id and ser.id_suc=suc.id"
				+ " and eva_ex.id_tae=teva.id order by suc.nom desc, ser.nom asc";
		List<Map<String,Object>> EvaluacionListfull = jdbcTemplate.queryForList(sql);			
		return EvaluacionListfull;
	}
	

}
