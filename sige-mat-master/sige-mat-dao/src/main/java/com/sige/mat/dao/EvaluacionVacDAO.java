package com.sige.mat.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.EvaluacionVacDAOImpl;
import com.tesla.frmk.sql.Row;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Define m�todos DAO operations para la entidad evaluacion_vac.
 * @author MV
 *
 */
@Repository
public class EvaluacionVacDAO extends EvaluacionVacDAOImpl{
	final static Logger logger = Logger.getLogger(EvaluacionVacDAO.class);
	
	@Autowired
	private SQLUtil sqlUtil;

	private String q_va;

	public List<Map<String,Object>> listExcriIns(Integer id_exa) {
		
		String sql ="select distinct ins.id, ins.nom, ex_ins.id_ins from eva_instrumento ins "
				+ "left join "
				+ "(eva_ins_exa_cri ex_ins "
				+ "right join eva_exa_conf_criterio ex_cri on (ex_ins.id_excri= ex_cri.id and ex_cri.id_eva_ex=" + id_exa + ")"
				+ ") on (ins.id=ex_ins.id_ins and ex_ins.est='A')";
		
		List<Map<String,Object>> listInstrumentos = jdbcTemplate.queryForList(sql);	
		//logger.info(sql);
		return listInstrumentos;
	} 
	
	/*Lista de evaluaciones por local y nivel*/
	public List<Row> EvaluacionVacList(Integer id_niv, Integer id_suc, Integer id_anio) {
		String q_aux="";
		if (id_niv!= null && id_suc!=null){
		 q_aux=" AND per.`id_niv`="+id_niv+" AND per.`id_suc`="+id_suc;
		}
		String sql = "SELECT eva.`id`, CONCAT(eva.des,' ', niv.nom,' ', suc.nom) evaluacion  "
				+ " FROM `eva_evaluacion_vac` eva INNER JOIN `per_periodo` per ON eva.`id_per`=per.`id`"
				+ " INNER JOIN ges_sucursal suc ON per.`id_suc`=suc.id"
				+ " INNER JOIN cat_nivel niv ON per.`id_niv`=niv.id"
				+ " WHERE per.`id_anio`="+id_anio+" "+q_aux;
		return sqlUtil.query(sql);
	}
		
	public List<Map<String,Object>> listaEvaluacionesVigentes(){
		String fecha = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		String sql = "select eva_vac.id eva_vac_id, eva_vac.id_per eva_vac_id_per , eva_vac.des eva_vac_des , eva_vac.precio eva_vac_precio,eva_vac.ptje_apro eva_vac_ptje_apro  ,eva_vac.est eva_vac_est, eva_vac.fec_ini eva_vac_fec_ini, eva_vac.fec_fin eva_vac_fec_fin , pee.id pee_id  , pee.id_anio pee_id_anio , pee.id_srv pee_id_srv , pee.id_tpe pee_id_tpe , pee.fec_ini pee_fec_ini , pee.fec_fin pee_fec_fin , pee.fec_cie_mat pee_fec_cie_mat  , ani.nom ani_nom, srv.nom srv_nom, suc.nom suc_nom from eva_evaluacion_vac eva_vac left join per_periodo pee on pee.id = eva_vac.id_per  left join col_anio ani on pee.id_anio = ani.id  left join ges_servicio srv on srv.id = pee.id_srv  left join ges_sucursal suc on suc.id = srv.id_suc   where  eva_vac.fec_ini<='" + fecha + "' and  eva_vac.fec_fin>='" + fecha + "' order by suc.nom desc, srv.nom asc";
		List<Map<String,Object>> evaList = jdbcTemplate.queryForList(sql);	
		
		return evaList;
	}
	
	/**
	 * Listar todas las evaluaciones vigentes
	 * @param id_niv
	 * @param id_suc
	 * @return
	 */
	public List<Row> listarEvaluacionesVigentes(Integer id_niv, Integer id_suc, Integer id_anio) {
		String q_aux="";
		String q_local="";
		if (id_niv!= null){
			q_aux="and niv.id="+id_niv;
		}
		if(id_suc!=null){
			if(id_suc!=0){
				q_local=" and suc.id="+id_suc;	
			}
			
		}
		String sql = "SELECT e.id, e.id_per,e.precio, CONCAT(e.des,' -  ',suc.nom,' - ', niv.nom) evaluacion "
				+ " FROM eva_evaluacion_vac e INNER JOIN per_periodo per ON e.`id_per`=per.`id`"
				+ " INNER JOIN `ges_sucursal` suc ON per.`id_suc`=suc.`id`"
				+ " INNER JOIN `cat_nivel` niv ON per.id_niv=niv.id"
				+ " WHERE (CURDATE() BETWEEN e.fec_ini AND e.fec_fin) AND per.id_anio="+id_anio+" AND e.est='A'"+q_aux+""+q_local;
		
		return SQLFrmkUtil.listToRows(jdbcTemplate.queryForList(sql));
	}
	
	/*Reporte General*/
	public List<Map<String,Object>> reporte_General(Integer id_eva, Integer id_grad, String ex_esc, String ex_psi, String vac) {
		String q_grad=" ";
		String q_esc=" ";
		String q_psico=" ";
		String q_vac=" ";
		if(id_grad!=null){
			q_grad=" AND g.`id`="+id_grad;
		}
		
		if(!vac.equals("")){
			q_vac=" AND mat.`res`='"+vac+"'";
		}
		
		if(ex_esc.equals("NR")){
			q_esc=" AND ex_esc.res is null";
		} else if(ex_esc!=""){
			q_esc=" AND ex_esc.res='"+ex_esc+"'";
		}
		
		if(ex_psi.equals("NR")){
			q_psico=" AND cri_nota.apto is null";
		} else if(ex_psi!=""){
			q_psico=" AND cri_nota.apto='"+ex_psi+"'";
		}
		String sql = "SELECT DISTINCT p.`nro_doc` AS 'DNI',CONCAT(p.`ape_pat`,' ',p.`ape_mat`,' ',p.`nom`) AS 'alumno', suc.`nom` AS 'local', col.nom colegio, DATE_FORMAT(mat.fec_ins,' %Y-%m-%d %H:%i:%s') fecha_mat, "
				+ " CONCAT(ser.`nom`,' - ', g.`nom` ) AS 'nivel_grado', (SELECT p.nro_doc FROM alu_familiar fam,alu_gru_fam_familiar fam_gpf,alu_gru_fam_alumno alu_gpf, col_persona p "
				+ " WHERE fam.id_par='2' AND p.id=fam.id_per AND alu_gpf.`id_gpf`=fam_gpf.`id_gpf` AND alu.id=alu_gpf.`id_alu` AND fam_gpf.`id_fam`=fam.`id` LIMIT 1) AS 'DNIPadre',"
				+ " (SELECT p.nro_doc FROM alu_familiar fam,alu_gru_fam_familiar fam_gpf,alu_gru_fam_alumno alu_gpf, col_persona p"
				+ " WHERE fam.id_par='1' AND p.id=fam.id_per AND alu_gpf.`id_gpf`=fam_gpf.`id_gpf` AND alu.id=alu_gpf.`id_alu` AND fam_gpf.`id_fam`=fam.`id` LIMIT 1) AS 'DNIMadre',"
				+ " IFNULL(ex_esc.res,'NR') AS 'ex_esc', "
				+ " IFNULL(apto,'NR') AS 'examen_psico_res',"
				+ " cri_nota.`puntaje` AS 'examen_psico_punt',"
				+ " mat.`res` AS 'res_final',"
				+ " mat.`id` AS 'id_mat'"
				+ " FROM eva_matr_vacante mat LEFT JOIN alu_alumno alu ON mat.`id_alu`=alu.`id`"
				+ " INNER JOIN col_persona p ON alu.id_per=p.id"
				+ " LEFT JOIN eva_matr_vacante_resultado ex_esc ON ex_esc.`id_mat_vac`=mat.`id`"
				+ " LEFT JOIN eva_evaluacion_vac eva ON mat.`id_eva`=eva.`id`"
				+ " LEFT JOIN eva_criterio_nota cri_nota ON cri_nota.`id_mat_vac`=mat.`id`"
				+ " LEFT JOIN per_periodo per ON eva.`id_per`=per.`id`"
				+ " LEFT JOIN ges_servicio ser ON per.`id_srv`=ser.`id`"
				+ " LEFT JOIN ges_sucursal suc ON ser.`id_suc`=suc.`id`"
				+ " LEFT JOIN `cat_grad` g ON mat.`id_gra`=g.`id`"
				+ " LEFT JOIN `col_colegio` col ON mat.`id_col`=col.`id`"
				+ " WHERE eva.`id`="+id_eva+" "+q_grad+" "+q_esc+" "+q_psico+" "+q_vac+" "
				+ " ORDER BY g.id asc, alu.ape_pat ASC";
		List<Map<String,Object>> Reporte = jdbcTemplate.queryForList(sql);
		logger.info(sql);
		return Reporte;
	}
	
	public List<Map<String,Object>> listarEvaluaciones(Integer id_anio) {		
		String sql =" SELECT eva_vac.id ,suc.`nom` sucursal, ser.`nom` servicio, eva_vac.`des`, eva_vac.`fec_ini`, eva_vac.`fec_fin`, eva_vac.fec_vig_vac, "
				+ " (SELECT eva_ex.id FROM `eva_evaluacion_vac_examen` eva_ex WHERE eva_vac.`id`=eva_ex.`id_eva` LIMIT 1) id_exa"
				+ " FROM `eva_evaluacion_vac` eva_vac INNER JOIN `per_periodo` per ON eva_vac.`id_per`=per.`id`"
				+ " INNER JOIN `ges_sucursal` suc ON per.`id_suc`=suc.`id`"
				+ " INNER JOIN `ges_servicio` ser ON per.`id_srv`=ser.`id`"
				+ " WHERE per.`id_anio`="+id_anio
				+ " ORDER BY eva_vac.fec_fin DESC, suc.nom DESC, ser.nom ASC";
		
		List<Map<String,Object>> listEvaluaciones = jdbcTemplate.queryForList(sql);	
		//logger.info(sql);
		return listEvaluaciones;
	} 
	
}
