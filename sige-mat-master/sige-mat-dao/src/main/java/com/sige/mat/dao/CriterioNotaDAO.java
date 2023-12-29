package com.sige.mat.dao;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.CriterioNotaDAOImpl;
import com.tesla.frmk.sql.Row;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Define m�todos DAO operations para la entidad area.
 * @author MV
 *
 */
@Repository
public class CriterioNotaDAO extends CriterioNotaDAOImpl{
	final static Logger logger = Logger.getLogger(CriterioNotaDAO.class);
	
	@Autowired
	private SQLUtil sqlUtil;

	/*Obtener el nivel*/
	public List<Map<String,Object>> Nivel(Integer id_eva) {
		String sql = "SELECT n.id FROM `eva_evaluacion_vac` eva, `per_periodo` per, `ges_servicio` ser, `cat_nivel` n"
				+ " WHERE eva.`id_per`=per.`id` AND per.`id_srv`=ser.`id` AND ser.`nom`=n.`nom` AND eva.id="+id_eva;
		List<Map<String,Object>> Nivel = jdbcTemplate.queryForList(sql);			
		return Nivel;
	}
	
	/*Alumnos matriculados de inicial, aptos para evaluacion psicologica */
	
	public List<Map<String,Object>> Alumnos_Inicial(Integer id_eva,String apellidosNombres, Integer id_gra) {		
		String q_aux=" ";
		if (apellidosNombres!=null){
			q_aux=" AND CONCAT(a.ape_pat,' ',a.ape_mat, ' ', a.nom) LIKE '%"+apellidosNombres+"%'";
		}
		
		String sql ="SELECT CONCAT(a.ape_pat,' ',a.ape_mat,' ',a.nom) AS 'alumno', m.id AS 'id_matri', a.id AS 'id_alu' ,g.nom AS 'grado', n.nom AS 'nivel',m.id_eva, ecn.id id_ecn "
				+ " FROM eva_matr_vacante m INNER JOIN alu_alumno a ON m.id_alu=a.id"
				+ " INNER JOIN cat_grad g ON m.id_gra=g.id"
				+ " INNER JOIN cat_nivel n ON g.id_nvl=n.id"
				+ " INNER JOIN eva_evaluacion_vac eva ON m.id_eva=eva.id"
				+ " LEFT JOIN eva_criterio_nota ecn ON ecn.id_mat_vac=m.id"
				+ " WHERE m.id_eva="+id_eva+" "+q_aux
				+ " ORDER BY a.ape_pat ASC";
		List<Map<String,Object>> AluInicial = jdbcTemplate.queryForList(sql);			
		return AluInicial;
	}
	
	/*Alumnos matriculados de primaria o secundaria, aptos para evaluacion psicologica */
	
	public List<Map<String,Object>> Alumnos_PrimSec(Integer id_eva,String apellidosNombres, Integer id_gra) {
		String q_aux=" ";
		if (apellidosNombres!=null){
			q_aux=" AND CONCAT(a.ape_pat,' ',a.ape_mat, ' ', a.nom) LIKE '%"+apellidosNombres+"%'";
		}
		String q_gra=" ";
		if(id_gra!=null){
			q_gra=" AND g.id="+id_gra;
		}
		String sql = "SELECT CONCAT(a.ape_pat,' ',a.ape_mat,' ',a.nom) alumno, m.id AS 'id_matri', a.id AS 'id_alu', g.nom AS 'grado', n.nom AS 'nivel',m.id_eva, ecn.id id_ecn "
				+ " FROM eva_matr_vacante m INNER JOIN alu_alumno a ON m.id_alu=a.id"
				+ " INNER JOIN cat_grad g ON m.id_gra=g.id"
				+ " INNER JOIN cat_nivel n ON g.id_nvl=n.id"
				+ " INNER JOIN eva_evaluacion_vac eva ON m.id_eva=eva.id"
				+ " INNER JOIN eva_matr_vacante_resultado res ON m.id=res.id_mat_vac"
				+ " LEFT JOIN eva_criterio_nota ecn ON ecn.id_mat_vac=m.id"
				+ " WHERE m.id_eva="+id_eva+" AND n.id!='1' AND res.res='APROBO' "+q_aux+" "+q_gra
				+ "ORDER BY a.ape_pat ASC";
		List<Map<String,Object>> AluPrimSec = jdbcTemplate.queryForList(sql);	
		//logger.info(sql);
		return AluPrimSec;
	} 
	
	public Row datos_Alumnos(Integer id_alu) {
		String sql = "select concat (a.ape_pat,' ',a.ape_mat,' ',a.nom) alumno, date_format(a.fec_nac,'%d-%m-%Y') AS 'fec_nac',"
				+ " TIMESTAMPDIFF(YEAR, date_format(a.fec_nac,'%Y-%m-%d'), CURDATE()) as 'edad', date_format(CURDATE(),'%d-%m-%Y') fecha, "
				+ " g.nom as 'grado',n.nom as 'nivel',a.id, e.id_eva from alu_alumno a, eva_matr_vacante e, cat_grad g, cat_nivel n where g.id_nvl=n.id "
				+ " and e.id_gra=g.id and e.id_alu=a.id and a.id="+id_alu;
		List<Row> datosAlum = sqlUtil.query(sql);		
		if(datosAlum.size()>0)
			return datosAlum.get(0);
		else
			return null;
	}
	
	public List<Map<String,Object>> Datos_Padres(Integer id_alu) {
		String sql = "SELECT concat (f.ape_pat,' ',f.ape_mat,' ',f.nom) as 'Familiar',f.tlf,f.corr FROM alu_familiar f, "
				+ " alu_alumno a,alu_gru_fam_alumno ga, alu_gru_fam_familiar gf where ga.id_gpf=gf.id_gpf and ga.id_alu=a.id "
				+ " and f.id=gf.id_fam and (f.id_par='1' || f.id_par='2') and a.id="+id_alu;
		List<Map<String,Object>> DatosPad = jdbcTemplate.queryForList(sql);			
		return DatosPad;
	}
	
	/*Lista de instrumentos usados*/
	public List<Row> instrumentosEvaluacion(Integer id_eva) {
		String sql = "select ins.nom, ex_cri.id id_ex_cri from eva_instrumento ins, eva_ins_exa_cri ins_cri, eva_exa_conf_criterio ex_cri,"
				+ " eva_evaluacion_vac_examen ex_eva, eva_evaluacion_vac eva where ins.id= ins_cri.id_ins "
				+ " and ex_cri.id=ins_cri.id_excri and ex_eva.id=ex_cri.id_eva_ex and ex_eva.id_eva=eva.id"
				+ " and eva.id="+id_eva+" and ins_cri.est='A'";
		return SQLFrmkUtil.listToRows(jdbcTemplate.queryForList(sql));
	}
	
	public List<Row> numero() {
		String sql = "select max(num)+1 as numero from eva_criterio_nota";
		return SQLFrmkUtil.listToRows(jdbcTemplate.queryForList(sql));
	}
	
	public List<Map<String,Object>> Preguntas(Integer id_pre) {
		String sql = "select alt.id, alt.alt, alt.punt, pre_alt.id_alt alternativa"
				+ " from eva_criterio_alternativa alt"
				+ " left join eva_criterio_pre_alt pre_alt on alt.id=pre_alt.id_alt where alt.id_pre="+id_pre;
		List<Map<String,Object>> Preguntas = jdbcTemplate.queryForList(sql);
		//logger.info(sql);
		return Preguntas;
	}
	
	public List<Row> conf_criterio(Integer id_eva) {
		String sql = "SELECT cri.* FROM `eva_evaluacion_vac_examen` eva_ex, `eva_exa_conf_criterio` cri"
				+ " WHERE eva_ex.id=cri.`id_eva_ex` AND eva_ex.`id_eva`="+id_eva+" AND eva_ex.`id_tae`='2'";
		List<Map<String,Object>> Conf_criterio = jdbcTemplate.queryForList(sql);			
		//return Conf_criterio;
		return SQLFrmkUtil.listToRows(jdbcTemplate.queryForList(sql));
	}
	
public boolean tieneEva(Integer id_mat){
		
		String sql = "SELECT * FROM `eva_criterio_nota` where id_mat_vac="+id_mat;
	 	
		List<Map<String,Object>> evaluado = jdbcTemplate.queryForList(sql);
		
		//logger.info(sql);
		
		return (evaluado.size()!=0);
	}
	
	/*Tiene configuracion de examen tipo marcacion*/
	public boolean existeExamen(Integer id_anio, Integer id_eva) {

		String sql = "SELECT eva.* FROM `eva_evaluacion_vac` eva INNER JOIN `eva_evaluacion_vac_examen` eva_ex ON eva.`id`= eva_ex.id_eva "
			+ " INNER JOIN `per_periodo` per ON eva.`id_per`=per.id "
			+ " WHERE per.id_anio="+id_anio+" AND eva.id="+id_eva+" AND eva_ex.id IN ( SELECT id_eva_ex FROM eva_exa_conf_marcacion)";

		//System.out.println(sql);
		List<Map<String,Object>> evaluaciones = jdbcTemplate.queryForList(sql);
		return (evaluaciones.size()>0);
	}
	
}
