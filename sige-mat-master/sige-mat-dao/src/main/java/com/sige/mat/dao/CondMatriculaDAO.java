package com.sige.mat.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.CondMatriculaDAOImpl;
import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Row;

/**
 * Define m�todos DAO operations para la entidad cond_matricula.
 * @author MV
 *
 */
@Repository
public class CondMatriculaDAO extends CondMatriculaDAOImpl{
	final static Logger logger = Logger.getLogger(CondMatriculaDAO.class);

	@Autowired
	private SQLUtil sqlUtil;
	
	public List<Row> listarAlumnos(int id_anio, String apelllidosNombres, Integer tip) {
		
		/*String sql = "SELECT DISTINCT CONCAT(alu.`ape_pat`,' ', alu.`ape_mat`,' ', alu.`nom`) alumno, niv.`nom` nivel, gra.`nom` grado, au.`secc` seccion, mat.id id_mat, cond.`id` id_mat_cond, suc.`nom` sucursal, cond.`nom` condicion"
				+ " FROM `col_historial_eco` hist INNER JOIN `mat_matricula` mat ON hist.`id_mat`=mat.`id`"
				+ " INNER JOIN `alu_alumno` alu ON mat.`id_alu`=alu.id"
				+ " INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.`id`"
				+ " INNER JOIN `per_periodo` per ON au.`id_per`=per.id"
				+ " INNER JOIN `ges_sucursal` suc ON per.`id_suc`=suc.`id` "
				+ " INNER JOIN `cat_grad` gra ON au.`id_grad`=gra.`id`"
				+ " INNER JOIN `cat_nivel` niv ON gra.`id_nvl`=niv.`id`"
				+ " LEFT JOIN `mat_condicion` mc ON mc.`id_mat`=mat.`id`"
				+ " LEFT JOIN `cat_cond_alumno` cond ON mc.`id_cond`=cond.`id`"
				+ " WHERE per.`id_anio`=?"
				+ " ORDER BY suc.`nom`, niv.`id`, gra.`id`, au.`secc`, alu.`ape_pat`, alu.`ape_mat`, alu.`nom`;";*/
		String sql ="SELECT * FROM (SELECT DISTINCT CONCAT(persa.`ape_pat`,' ', persa.`ape_mat`,' ', persa.`nom`) alumno, niv.`nom` nivel, gra.`nom` grado, au.`secc` seccion, mat.id id_mat, mc.`id` id_mat_cond, suc.`nom` sucursal, cond.`nom` condicion,"
				+ " sit.nom situacion, (SELECT hist.id FROM `col_historial_eco` hist  WHERE hist.`id_mat`=mat.`id` LIMIT 1) hist, concat(perf.ape_pat,' ', perf.ape_mat,' ', perf.nom) apoderado, perf.cel, perf.nro_doc"
				+ " FROM mat_matricula mat"
				+ " INNER JOIN `alu_alumno` alu ON mat.`id_alu`=alu.id"
				+ " INNER JOIN `col_persona` persa ON persa.`id`=alu.id_per"
				+ " INNER JOIN alu_familiar fam on mat.id_fam=fam.id"
				+ " INNER JOIN col_persona perf on perf.id=fam.id_per"
				+ " INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.`id`"
				+ " INNER JOIN `per_periodo` per ON au.`id_per`=per.id"
				+ " INNER JOIN `ges_sucursal` suc ON per.`id_suc`=suc.`id` "
				+ " INNER JOIN `cat_grad` gra ON au.`id_grad`=gra.`id`"
				+ " INNER JOIN `cat_nivel` niv ON gra.`id_nvl`=niv.`id`"
				+ " LEFT JOIN `mat_condicion` mc ON mc.`id_mat`=mat.`id`"
				+ " LEFT JOIN `cat_cond_alumno` cond ON mc.`id_cond`=cond.`id` "
				+ " LEFT JOIN  `cat_tip_cond` tip ON cond.`id_ctc`=tip.`id`"
				+ " LEFT JOIN col_situacion_mat sit_mat ON sit_mat.id_mat=mat.id"
				+ " LEFT JOIN cat_col_situacion sit ON sit_mat.id_sit=sit.id"
				+ " WHERE per.`id_anio`=? AND (tip.`id`=1 OR tip.`id` IS NULL)"
				+ " and ( upper(CONCAT(persa.ape_pat,' ',trim(persa.ape_mat), ' ', persa.nom)) LIKE '%"+ apelllidosNombres.toUpperCase()+"%'  ) "
				+ ") t";
				if(tip!=0)
				sql += " where hist IS NOT NULL";
				sql += " ORDER BY t.hist desc";
				//+ " ORDER BY suc.`nom`, niv.`id`, gra.`id`, au.`secc`, alu.`ape_pat`, alu.`ape_mat`, alu.`nom`";
		logger.info(sql);
		return sqlUtil.query(sql, new Object[] {id_anio});

	}
	
public List<Row> obtenerDatosCond(int id_mat) {
		
		/*String sql = "SELECT hist.`id` id_hist, mat.`id` id_mat, cond.id mat_cond  FROM `col_historial_eco` hist INNER JOIN `mat_matricula` mat ON hist.`id_mat`=mat.`id`"
				+ " LEFT JOIN `mat_condicion` cond ON mat.`id`=cond.`id_mat`"
				+ " WHERE mat.id=?";*/
		String sql = "SELECT hist.`id` id_hist, mat.`id` id_mat, mc.id mat_cond  "
				+ " FROM `col_historial_eco` hist "
				+ " INNER JOIN `mat_matricula` mat ON hist.`id_mat`=mat.`id` "
				+ " INNER JOIN `mat_condicion` mc ON mat.`id`=mc.`id_mat`"
				+ " INNER JOIN `cat_cond_alumno` cond ON mc.`id_cond`=cond.`id` "
				+ " INNER JOIN  `cat_tip_cond` tip ON cond.`id_ctc`=tip.`id`"
				+ " WHERE (tip.`id`=1 OR mc.`tip_blo`='E') AND  mat.id=?";
		logger.info(sql);
		return sqlUtil.query(sql, new Object[] { id_mat });

	}
	
}
