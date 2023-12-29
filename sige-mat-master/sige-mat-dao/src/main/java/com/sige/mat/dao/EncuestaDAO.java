package com.sige.mat.dao;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.sige.mat.dao.impl.EncuestaDAOImpl;
import com.tesla.colegio.model.EncuestaPreg;
import com.tesla.colegio.model.bean.AlumnoNotaBean;
import com.tesla.colegio.model.bean.AlumnoNotaCursoBean;
import com.tesla.colegio.model.bean.PreguntaAlternativaBean;
import com.tesla.colegio.model.bean.PromedioBean;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;
import com.tesla.frmk.sql.SQLFrmkUtil;

/**
 * Define m�todos DAO operations para la entidad encuesta.
 * @author MV
 *
 */
@Repository
public class EncuestaDAO extends EncuestaDAOImpl{
	
	@Autowired
	private EncuestaPregDAO encuestaPregDAO;

	/** Listar familias*/
	public List<Row> listarEncuestasxGiro(Integer id_gir, Integer id_anio) {
		String sql="SELECT id, nom value FROM col_encuesta enc WHERE enc.id_gir=? AND enc.id_anio=? ORDER BY enc.id ";

		return SQLFrmkUtil.listToRows(jdbcTemplate.queryForList(sql, new Object[]{id_gir, id_anio}));
	}
	
	public Map<String, Object> listRespuestasEncuesta(Integer id_niv, Integer id_gra, Integer id_enc, Integer id_anio, Integer id_gir, String res, String resp_letras) {

		// competencias
		/*Param param = new Param();
		param.put("id_enc", id_enc);*/
		String sqlPreguntas ="SELECT id, SUBSTRING(pre, 1, 60) pre, pre pre_com FROM col_encuesta_preg WHERE id_enc="+id_enc+" ORDER BY ord;";
		
		List<Map<String, Object>> lista_preguntas = jdbcTemplate.queryForList(sqlPreguntas);
		

		List<Map<String, Object>> alumnos = new ArrayList<Map<String, Object>>();
		String sqlAlumnos ="";
		if(res.equals("T")) {
			 sqlAlumnos += "SELECT alu.`id`, CONCAT(per.ape_pat,' ',per.ape_mat,' ',per.nom) alumno, perf.cel, per.nro_doc nro_doc, niv.nom nivel, gra.nom grado, suc.nom sucursal, ";
				sqlAlumnos += " ( CASE WHEN gra.id=1 THEN 'Inicial' ";
				sqlAlumnos += " WHEN gra.id=1 THEN 'Inicial'  ";
				sqlAlumnos += " WHEN gra.id=2 THEN 'Inicial' ";
				sqlAlumnos += " WHEN gra.id=3 THEN 'Primaria' "; 
				sqlAlumnos += " WHEN gra.id=4 THEN 'Primaria' ";
				sqlAlumnos += " WHEN gra.id=5 THEN 'Primaria' ";
				sqlAlumnos += " WHEN gra.id=6 THEN 'Primaria' "; 
				sqlAlumnos += " WHEN gra.id=7 THEN 'Primaria' "; 
				sqlAlumnos += " WHEN gra.id=8 THEN 'Primaria' "; 
				sqlAlumnos += " WHEN gra.id=9 THEN 'Secundaria' "; 
				sqlAlumnos += " WHEN gra.id=10 THEN 'Secundaria' "; 
				sqlAlumnos += " WHEN gra.id=11 THEN 'Secundaria' "; 
				sqlAlumnos += " WHEN gra.id=12 THEN 'Secundaria' ";
				sqlAlumnos += " WHEN gra.id=13 THEN 'Secundaria' "; 
				sqlAlumnos += " ELSE '' END ) niv_sig , ";
				sqlAlumnos += " ( CASE WHEN gra.id+1=2 THEN '4 AÑOS' ";
				sqlAlumnos += " WHEN gra.id+1=3 THEN '5 AÑOS' "; 
				sqlAlumnos += " WHEN gra.id+1=4 THEN 'PRIMERO' "; 
				sqlAlumnos += " WHEN gra.id+1=5 THEN 'SEGUNDO' "; 
				sqlAlumnos += " WHEN gra.id+1=6 THEN 'TERCERO' "; 
				sqlAlumnos += " WHEN gra.id+1=7 THEN 'CUARTO' "; 
				sqlAlumnos += " WHEN gra.id+1=8 THEN 'QUINTO' "; 
				sqlAlumnos += " WHEN gra.id+1=9 THEN 'SEXTO' "; 
				sqlAlumnos += " WHEN gra.id+1=10 THEN 'PRIMERO' "; 
				sqlAlumnos += " WHEN gra.id+1=11 THEN 'SEGUNDO' "; 
				sqlAlumnos += " WHEN gra.id+1=12 THEN 'TERCERO' "; 
				sqlAlumnos += " WHEN gra.id+1=13 THEN 'CUARTO' ";
				sqlAlumnos += " WHEN gra.id+1=14 THEN 'QUINTO' ";
				sqlAlumnos += " ELSE '' END ) gra_sig  ";
				sqlAlumnos += " FROM `mat_matricula` mat INNER JOIN `alu_alumno` alu ON  mat.`id_alu`=alu.`id` ";
				sqlAlumnos +=  " INNER JOIN col_persona per ON per.id=alu.id_per";
				sqlAlumnos +=  " INNER JOIN alu_familiar fam ON fam.id=mat.id_fam ";
				sqlAlumnos +=  " INNER JOIN col_persona perf ON perf.id=fam.id_per";
				sqlAlumnos +=  " INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.`id`";
				sqlAlumnos +=  " INNER JOIN `cat_nivel` niv ON mat.`id_niv`=niv.`id`";
				sqlAlumnos +=  " INNER JOIN `cat_grad` gra ON au.`id_grad`=gra.`id` AND gra.`id_nvl`=niv.`id`";
				sqlAlumnos +=  " INNER JOIN `per_periodo` peri ON au.`id_per`=peri.`id`";
				sqlAlumnos +=  " INNER JOIN `ges_sucursal` suc ON peri.id_suc=suc.`id` ";
				sqlAlumnos +=  " INNER JOIN `ges_servicio` srv ON peri.id_srv=srv.`id` ";
				sqlAlumnos += " WHERE niv.id =?  and peri.id_anio=? and srv.id_gir=? AND gra.id<>14 AND (mat.id_sit<>'5' OR mat.id_sit IS NULL) ";// alumno and alu.id=2714
				if(id_gra!=null)	{
					sqlAlumnos += " AND mat.id_gra="+id_gra;
				}																												
				sqlAlumnos += " ORDER BY niv.id, gra.id, per.ape_pat, per.ape_mat, per.nom";
		} else if(res.equals("S")) {
			 sqlAlumnos += "SELECT alu.`id`, CONCAT(per.ape_pat,' ',per.ape_mat,' ',per.nom) alumno, perf.cel, per.nro_doc nro_doc, niv.nom nivel, gra.nom grado, suc.nom sucursal, ";
				sqlAlumnos += " ( CASE WHEN gra.id=1 THEN 'Inicial' ";
				sqlAlumnos += " WHEN gra.id=1 THEN 'Inicial'  ";
				sqlAlumnos += " WHEN gra.id=2 THEN 'Inicial' ";
				sqlAlumnos += " WHEN gra.id=3 THEN 'Primaria' "; 
				sqlAlumnos += " WHEN gra.id=4 THEN 'Primaria' ";
				sqlAlumnos += " WHEN gra.id=5 THEN 'Primaria' ";
				sqlAlumnos += " WHEN gra.id=6 THEN 'Primaria' "; 
				sqlAlumnos += " WHEN gra.id=7 THEN 'Primaria' "; 
				sqlAlumnos += " WHEN gra.id=8 THEN 'Primaria' "; 
				sqlAlumnos += " WHEN gra.id=9 THEN 'Secundaria' "; 
				sqlAlumnos += " WHEN gra.id=10 THEN 'Secundaria' "; 
				sqlAlumnos += " WHEN gra.id=11 THEN 'Secundaria' "; 
				sqlAlumnos += " WHEN gra.id=12 THEN 'Secundaria' ";
				sqlAlumnos += " WHEN gra.id=13 THEN 'Secundaria' "; 
				sqlAlumnos += " ELSE '' END ) niv_sig , ";
				sqlAlumnos += " ( CASE WHEN gra.id+1=2 THEN '4 AÑOS' ";
				sqlAlumnos += " WHEN gra.id+1=3 THEN '5 AÑOS' "; 
				sqlAlumnos += " WHEN gra.id+1=4 THEN 'PRIMERO' "; 
				sqlAlumnos += " WHEN gra.id+1=5 THEN 'SEGUNDO' "; 
				sqlAlumnos += " WHEN gra.id+1=6 THEN 'TERCERO' "; 
				sqlAlumnos += " WHEN gra.id+1=7 THEN 'CUARTO' "; 
				sqlAlumnos += " WHEN gra.id+1=8 THEN 'QUINTO' "; 
				sqlAlumnos += " WHEN gra.id+1=9 THEN 'SEXTO' "; 
				sqlAlumnos += " WHEN gra.id+1=10 THEN 'PRIMERO' "; 
				sqlAlumnos += " WHEN gra.id+1=11 THEN 'SEGUNDO' "; 
				sqlAlumnos += " WHEN gra.id+1=12 THEN 'TERCERO' "; 
				sqlAlumnos += " WHEN gra.id+1=13 THEN 'CUARTO' ";
				sqlAlumnos += " WHEN gra.id+1=14 THEN 'QUINTO' ";
				sqlAlumnos += " ELSE '' END ) gra_sig  ";
				sqlAlumnos += " FROM `mat_matricula` mat INNER JOIN `alu_alumno` alu ON  mat.`id_alu`=alu.`id` ";
				sqlAlumnos +=  " INNER JOIN col_persona per ON per.id=alu.id_per";
				sqlAlumnos +=  " INNER JOIN alu_familiar fam ON fam.id=mat.id_fam ";
				sqlAlumnos +=  " INNER JOIN col_persona perf ON perf.id=fam.id_per";
				sqlAlumnos +=  " INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.`id`";
				sqlAlumnos +=  " INNER JOIN `cat_nivel` niv ON mat.`id_niv`=niv.`id`";
				sqlAlumnos +=  " INNER JOIN `cat_grad` gra ON au.`id_grad`=gra.`id` AND gra.`id_nvl`=niv.`id`";
				sqlAlumnos +=  " INNER JOIN `per_periodo` peri ON au.`id_per`=peri.`id`";
				sqlAlumnos +=  " INNER JOIN `ges_sucursal` suc ON peri.id_suc=suc.`id` ";
				sqlAlumnos +=  " INNER JOIN `ges_servicio` srv ON peri.id_srv=srv.`id` ";
				sqlAlumnos +=  " INNER JOIN `col_encuesta_alumno` enca ON enca.id_mat=mat.id ";
				sqlAlumnos += " WHERE niv.id =?  and peri.id_anio=? and srv.id_gir=? AND gra.id<>14 AND (mat.id_sit<>'5' OR mat.id_sit IS NULL) AND enca.id_enc="+id_enc;// alumno and alu.id=2714
				if(id_gra!=null)	{
					sqlAlumnos += " AND mat.id_gra="+id_gra;
				}																												
				sqlAlumnos += " ORDER BY niv.id, gra.id, per.ape_pat, per.ape_mat, per.nom";
		} else if(res.equals("N")) {
			 sqlAlumnos += "SELECT * FROM (SELECT alu.`id`, CONCAT(per.ape_pat,' ',per.ape_mat,' ',per.nom) alumno, perf.cel, per.nro_doc nro_doc, niv.nom nivel, gra.nom grado, suc.nom sucursal, enca.id id_enca, ";
				sqlAlumnos += " ( CASE WHEN gra.id=1 THEN 'Inicial' ";
				sqlAlumnos += " WHEN gra.id=1 THEN 'Inicial'  ";
				sqlAlumnos += " WHEN gra.id=2 THEN 'Inicial' ";
				sqlAlumnos += " WHEN gra.id=3 THEN 'Primaria' "; 
				sqlAlumnos += " WHEN gra.id=4 THEN 'Primaria' ";
				sqlAlumnos += " WHEN gra.id=5 THEN 'Primaria' ";
				sqlAlumnos += " WHEN gra.id=6 THEN 'Primaria' "; 
				sqlAlumnos += " WHEN gra.id=7 THEN 'Primaria' "; 
				sqlAlumnos += " WHEN gra.id=8 THEN 'Primaria' "; 
				sqlAlumnos += " WHEN gra.id=9 THEN 'Secundaria' "; 
				sqlAlumnos += " WHEN gra.id=10 THEN 'Secundaria' "; 
				sqlAlumnos += " WHEN gra.id=11 THEN 'Secundaria' "; 
				sqlAlumnos += " WHEN gra.id=12 THEN 'Secundaria' ";
				sqlAlumnos += " WHEN gra.id=13 THEN 'Secundaria' "; 
				sqlAlumnos += " ELSE '' END ) niv_sig , ";
				sqlAlumnos += " ( CASE WHEN gra.id+1=2 THEN '4 AÑOS' ";
				sqlAlumnos += " WHEN gra.id+1=3 THEN '5 AÑOS' "; 
				sqlAlumnos += " WHEN gra.id+1=4 THEN 'PRIMERO' "; 
				sqlAlumnos += " WHEN gra.id+1=5 THEN 'SEGUNDO' "; 
				sqlAlumnos += " WHEN gra.id+1=6 THEN 'TERCERO' "; 
				sqlAlumnos += " WHEN gra.id+1=7 THEN 'CUARTO' "; 
				sqlAlumnos += " WHEN gra.id+1=8 THEN 'QUINTO' "; 
				sqlAlumnos += " WHEN gra.id+1=9 THEN 'SEXTO' "; 
				sqlAlumnos += " WHEN gra.id+1=10 THEN 'PRIMERO' "; 
				sqlAlumnos += " WHEN gra.id+1=11 THEN 'SEGUNDO' "; 
				sqlAlumnos += " WHEN gra.id+1=12 THEN 'TERCERO' "; 
				sqlAlumnos += " WHEN gra.id+1=13 THEN 'CUARTO' ";
				sqlAlumnos += " WHEN gra.id+1=14 THEN 'QUINTO' ";
				sqlAlumnos += " ELSE '' END ) gra_sig  ";
				sqlAlumnos += " FROM `mat_matricula` mat INNER JOIN `alu_alumno` alu ON  mat.`id_alu`=alu.`id` ";
				sqlAlumnos +=  " INNER JOIN col_persona per ON per.id=alu.id_per";
				sqlAlumnos +=  " INNER JOIN alu_familiar fam ON fam.id=mat.id_fam ";
				sqlAlumnos +=  " INNER JOIN col_persona perf ON perf.id=fam.id_per";
				sqlAlumnos +=  " INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.`id`";
				sqlAlumnos +=  " INNER JOIN `cat_nivel` niv ON mat.`id_niv`=niv.`id`";
				sqlAlumnos +=  " INNER JOIN `cat_grad` gra ON au.`id_grad`=gra.`id` AND gra.`id_nvl`=niv.`id`";
				sqlAlumnos +=  " INNER JOIN `per_periodo` peri ON au.`id_per`=peri.`id`";
				sqlAlumnos +=  " INNER JOIN `ges_sucursal` suc ON peri.id_suc=suc.`id` ";
				sqlAlumnos +=  " INNER JOIN `ges_servicio` srv ON peri.id_srv=srv.`id` ";
				sqlAlumnos +=  " LEFT JOIN `col_encuesta_alumno` enca ON enca.id_mat=mat.id AND enca.id_enc="+id_enc;
				sqlAlumnos += " WHERE niv.id =?  and peri.id_anio=? and srv.id_gir=? AND gra.id<>14 AND (mat.id_sit<>'5' OR mat.id_sit IS NULL) ";// alumno and alu.id=2714
				if(id_gra!=null)	{
					sqlAlumnos += " AND mat.id_gra="+id_gra;
				}																												
				sqlAlumnos += " ORDER BY niv.id, gra.id, per.ape_pat, per.ape_mat, per.nom) t WHERE t.id_enca IS NULL";
		}
		
		//logger.info(sqlAlumnos);
		alumnos = jdbcTemplate.queryForList(sqlAlumnos, new Object[] { id_niv, id_anio, id_gir });

		LinkedHashMap<String, Map<String, Object>> linkalumnos = new LinkedHashMap<String, Map<String, Object>>();
		for (Map<String, Object> map : alumnos) {
			Map<String, Object> detalle = new HashMap<String, Object>();
			detalle.put("alumno", map.get("alumno"));
			detalle.put("nro_doc", map.get("nro_doc"));
			detalle.put("id_alu", map.get("id"));
			detalle.put("nivel", map.get("nivel"));
			detalle.put("local", map.get("sucursal"));
			detalle.put("cel", map.get("cel"));
			detalle.put("grado", map.get("grado"));
			detalle.put("nivel_sig", map.get("niv_sig"));
			detalle.put("grado_sig", map.get("gra_sig"));
			detalle.put("respuesta", null);
			detalle.put("promedio", null);

			linkalumnos.put(map.get("id").toString(), detalle);

		}

		List<Map<String, Object>> respuestas = new ArrayList<Map<String, Object>>();
		String sqlRespuestas="";
		if(res.equals("T")) {
			sqlRespuestas += "SELECT DISTINCT alu.id id_alu, enca.`id_mat`, CONCAT( per.`ape_pat`,' ', per.`ape_mat`,' ', per.`nom`) alumno, perf.cel, niv.`nom` nivel, gra.nom grado, suc.nom sucursal,\r\n" ; 
			if(resp_letras.equals("1"))
				sqlRespuestas += " encalt.letra alt, ";
			else if(resp_letras.equals("0"))
				sqlRespuestas += " encalt.alt, ";
			sqlRespuestas += "encd.id_enc_alt, encp.id id_enc_pre\r\n";
			sqlRespuestas += " FROM  `mat_matricula` mat \r\n" ; 
			sqlRespuestas += " INNER JOIN `alu_alumno` alu ON mat.`id_alu`=alu.`id`\r\n" ; 
			sqlRespuestas += " INNER JOIN `col_persona` per ON alu.`id_per`=per.`id`\r\n" ; 
			sqlRespuestas +=  " INNER JOIN alu_familiar fam ON fam.id=mat.id_fam ";
			sqlRespuestas +=  " INNER JOIN col_persona perf ON perf.id=fam.id_per";
			sqlRespuestas += " INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.`id`\r\n" ; 
			sqlRespuestas += " INNER JOIN `cat_nivel` niv ON mat.`id_niv`=niv.`id`\r\n" ; 
			sqlRespuestas += " INNER JOIN `cat_grad` gra ON au.`id_grad`=gra.`id` AND gra.`id_nvl`=niv.`id`\r\n" ; 
			sqlRespuestas += " INNER JOIN `per_periodo` peri ON au.`id_per`=peri.`id`\r\n" ; 
			sqlRespuestas += " INNER JOIN `ges_sucursal` suc ON peri.id_suc=suc.`id`\r\n" ;
			sqlRespuestas += " INNER JOIN `ges_servicio` srv ON peri.id_srv=srv.id\r\n" ; 
			sqlRespuestas += " INNER JOIN `col_encuesta` enc ON enc.`id_anio`=peri.id_anio AND enc.`id_gir`=srv.`id_gir`\r\n" ;
			sqlRespuestas += " INNER JOIN `col_encuesta_preg` encp ON enc.id=encp.id_enc \r\n" ;
			sqlRespuestas += " LEFT JOIN `col_encuesta_alumno` enca  ON enca.`id_mat`=mat.`id` AND enc.`id`=enca.`id_enc` AND enca.id_enc=\r\n"+id_enc ;
			sqlRespuestas += " LEFT JOIN `col_encuesta_alumno_det` encd ON enca.`id`=encd.`id_enc_alu` AND encd.`id_enc_pre`=encp.id \r\n" ;
			sqlRespuestas += " LEFT JOIN `col_encuesta_alt` encalt ON encd.id_enc_alt=encalt.id\r\n" ; 
			sqlRespuestas += " WHERE mat.id_niv=?  and peri.id_anio=? and srv.id_gir=? AND gra.id<>14 AND (mat.id_sit<>'5' OR mat.id_sit IS NULL) \r\n" ;
			if(id_gra!=null) {
				sqlRespuestas += " AND mat.id_gra="+id_gra;
			}
			sqlRespuestas += " ORDER BY niv.id, gra.id, per.ape_pat, per.ape_mat, per.nom ";
		} else if(res.equals("S")) {
			sqlRespuestas += "SELECT DISTINCT alu.id id_alu, enca.`id_mat`, CONCAT( per.`ape_pat`,' ', per.`ape_mat`,' ', per.`nom`) alumno, perf.cel, niv.`nom` nivel, gra.nom grado, suc.nom sucursal,\r\n" ; 
			if(resp_letras.equals("1"))
				sqlRespuestas += " encalt.letra alt, ";
			else if(resp_letras.equals("0"))
				sqlRespuestas += " encalt.alt, ";
			sqlRespuestas += "encd.id_enc_alt, encp.id id_enc_pre\r\n";
			sqlRespuestas += " FROM  `mat_matricula` mat \r\n" ; 
			sqlRespuestas += " INNER JOIN `alu_alumno` alu ON mat.`id_alu`=alu.`id`\r\n" ; 
			sqlRespuestas += " INNER JOIN `col_persona` per ON alu.`id_per`=per.`id`\r\n" ;
			sqlRespuestas +=  " INNER JOIN alu_familiar fam ON fam.id=mat.id_fam ";
			sqlRespuestas +=  " INNER JOIN col_persona perf ON perf.id=fam.id_per";
			sqlRespuestas += " INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.`id`\r\n" ; 
			sqlRespuestas += " INNER JOIN `cat_nivel` niv ON mat.`id_niv`=niv.`id`\r\n" ; 
			sqlRespuestas += " INNER JOIN `cat_grad` gra ON au.`id_grad`=gra.`id` AND gra.`id_nvl`=niv.`id`\r\n" ; 
			sqlRespuestas += " INNER JOIN `per_periodo` peri ON au.`id_per`=peri.`id`\r\n" ; 
			sqlRespuestas += " INNER JOIN `ges_sucursal` suc ON peri.id_suc=suc.`id`\r\n" ;
			sqlRespuestas += " INNER JOIN `ges_servicio` srv ON peri.id_srv=srv.id\r\n" ; 
			sqlRespuestas += " INNER JOIN `col_encuesta` enc ON enc.`id_anio`=peri.id_anio AND enc.`id_gir`=srv.`id_gir`\r\n" ;
			sqlRespuestas += " INNER JOIN `col_encuesta_preg` encp ON enc.id=encp.id_enc \r\n" ;
			sqlRespuestas += " INNER JOIN `col_encuesta_alumno` enca  ON enca.`id_mat`=mat.`id` AND enc.`id`=enca.`id_enc`\r\n" ;
			sqlRespuestas += " INNER JOIN `col_encuesta_alumno_det` encd ON enca.`id`=encd.`id_enc_alu` AND encd.`id_enc_pre`=encp.id \r\n" ;
			sqlRespuestas += " INNER JOIN `col_encuesta_alt` encalt ON encd.id_enc_alt=encalt.id\r\n" ; 
			sqlRespuestas += " WHERE mat.id_niv=?  and peri.id_anio=? and srv.id_gir=? AND gra.id<>14 AND (mat.id_sit<>'5' OR mat.id_sit IS NULL) AND enca.id_enc=\r\n"+id_enc ;
			if(id_gra!=null) {
				sqlRespuestas += " AND mat.id_gra="+id_gra;
			}
			sqlRespuestas += " ORDER BY niv.id, gra.id, per.ape_pat, per.ape_mat, per.nom ";
		} else if(res.equals("N")) {
			sqlRespuestas += "SELECT * FROM (SELECT DISTINCT alu.id id_alu, enca.`id_mat`, CONCAT( per.`ape_pat`,' ', per.`ape_mat`,' ', per.`nom`) alumno, perf.cel, niv.`nom` nivel, gra.nom grado, suc.nom sucursal, enca.id id_enca, \r\n" ; 
			if(resp_letras.equals("1"))
				sqlRespuestas += " encalt.letra alt, ";
			else if(resp_letras.equals("0"))
				sqlRespuestas += " encalt.alt, ";
			sqlRespuestas += "encd.id_enc_alt, encp.id id_enc_pre\r\n";
			sqlRespuestas += " FROM  `mat_matricula` mat \r\n" ; 
			sqlRespuestas += " INNER JOIN `alu_alumno` alu ON mat.`id_alu`=alu.`id`\r\n" ; 
			sqlRespuestas += " INNER JOIN `col_persona` per ON alu.`id_per`=per.`id`\r\n" ; 
			sqlRespuestas +=  " INNER JOIN alu_familiar fam ON fam.id=mat.id_fam ";
			sqlRespuestas +=  " INNER JOIN col_persona perf ON perf.id=fam.id_per";
			sqlRespuestas += " INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.`id`\r\n" ; 
			sqlRespuestas += " INNER JOIN `cat_nivel` niv ON mat.`id_niv`=niv.`id`\r\n" ; 
			sqlRespuestas += " INNER JOIN `cat_grad` gra ON au.`id_grad`=gra.`id` AND gra.`id_nvl`=niv.`id`\r\n" ; 
			sqlRespuestas += " INNER JOIN `per_periodo` peri ON au.`id_per`=peri.`id`\r\n" ; 
			sqlRespuestas += " INNER JOIN `ges_sucursal` suc ON peri.id_suc=suc.`id`\r\n" ;
			sqlRespuestas += " INNER JOIN `ges_servicio` srv ON peri.id_srv=srv.id\r\n" ; 
			sqlRespuestas += " INNER JOIN `col_encuesta` enc ON enc.`id_anio`=peri.id_anio AND enc.`id_gir`=srv.`id_gir`\r\n" ;
			sqlRespuestas += " INNER JOIN `col_encuesta_preg` encp ON enc.id=encp.id_enc \r\n" ;
			sqlRespuestas += " LEFT JOIN `col_encuesta_alumno` enca  ON enca.`id_mat`=mat.`id` AND enc.`id`=enca.`id_enc` AND enca.id_enc=\r\n"+id_enc ;
			sqlRespuestas += " LEFT JOIN `col_encuesta_alumno_det` encd ON enca.`id`=encd.`id_enc_alu` AND encd.`id_enc_pre`=encp.id \r\n" ;
			sqlRespuestas += " LEFT JOIN `col_encuesta_alt` encalt ON encd.id_enc_alt=encalt.id\r\n" ; 
			sqlRespuestas += " WHERE mat.id_niv=?  and peri.id_anio=? and srv.id_gir=? AND gra.id<>14 AND (mat.id_sit<>'5' OR mat.id_sit IS NULL) \r\n" ;
			if(id_gra!=null) {
				sqlRespuestas += " AND mat.id_gra="+id_gra;
			}
			sqlRespuestas += " ORDER BY niv.id, gra.id, per.ape_pat, per.ape_mat, per.nom) t WHERE t.id_enca IS NULL ";
		}
		
		respuestas = jdbcTemplate.queryForList(sqlRespuestas, new Object[] { id_niv, id_anio, id_gir});

		for (Map<String, Object> map : respuestas) {

			Map<String, Object> alumno = linkalumnos.get(map.get("id_alu").toString());
			// notas en ese momento
			Object notaObject = alumno.get("respuesta");

			if (notaObject == null) { //
				List<PreguntaAlternativaBean> respuestaAlumno = new ArrayList<PreguntaAlternativaBean>();
				//List<AlumnoNotaBean> notaAlumno = new ArrayList<AlumnoNotaBean>();
				//List<PromedioBean> prom = new ArrayList<PromedioBean>();
				PreguntaAlternativaBean respuesta = new PreguntaAlternativaBean();
				respuesta.setId_alu((Integer) map.get("id_alu"));
				respuesta.setId_mat((Integer) map.get("id_mat"));
				respuesta.setId_enc_pre((Integer) map.get("id_enc_pre"));
				respuesta.setId_enc_alt((Integer) map.get("id_enc_alt"));
				if(map.get("alt")!=null)
				respuesta.setAlt(map.get("alt").toString());
				respuestaAlumno.add(respuesta);
				alumno.put("respuesta", respuestaAlumno);
			} else {
				List<PreguntaAlternativaBean> respuestaAlumno = (List<PreguntaAlternativaBean>) notaObject;
				//List<PromedioBean> prom = (List<PromedioBean>) notaObject;
				PreguntaAlternativaBean respuesta = new PreguntaAlternativaBean();
				respuesta.setId_alu((Integer) map.get("id_alu"));
				respuesta.setId_mat((Integer) map.get("id_mat"));
				respuesta.setId_enc_pre((Integer) map.get("id_enc_pre"));
				respuesta.setId_enc_alt((Integer) map.get("id_enc_alt"));
				if(map.get("alt")!=null)
				respuesta.setAlt(map.get("alt").toString());
				//alumno.put("respuesta", respuesta);
				respuestaAlumno.add(respuesta);
				alumno.put("respuesta", respuestaAlumno);
			}

		}

		// itera los alumnos
		LinkedHashMap<String, Map<String, Object>> linkalumnos1 = new LinkedHashMap<String, Map<String, Object>>();
		List<Map<String, Object>> listAlumnos = new ArrayList<>();
		for (Map.Entry<String, Map<String, Object>> entry : linkalumnos.entrySet()) {
			String key = entry.getKey();// id del alumno
			Map<String, Object> respuestaMap = entry.getValue();
			//List<PreguntaAlternativaBean> respuestaAlumno = (List<PreguntaAlternativaBean>) respuestaMap.get("respuesta");// aca
																							// tenemos
																							// las
																							// notas
																							// del
																							// alumno
			//Map<Integer, BigDecimal> competenciaSuma = new HashMap<Integer, BigDecimal>();
			//Map<Integer, BigDecimal> notasCursos = new HashMap<Integer, BigDecimal>();
			//Map<Integer, BigDecimal> competenciaCont = new HashMap<Integer, BigDecimal>();
			//BigDecimal promedioGeneral = new BigDecimal(0);
			//List<AlumnoNotaCursoBean> notas_cursos=new ArrayList<AlumnoNotaCursoBean>();
	

			
			
			listAlumnos.add(respuestaMap);
			// notaMap.put("competenciaSuma", competenciaSuma.GET);
			// notaMap.put("promediogeneral", promedioGeneral);//purebalo por
			// faovr quiero ver el json final

		} // linkalumnos

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("preguntas", lista_preguntas);
		//map.put("alumnos", listAlumnos);
		map.put("alumnos", listAlumnos);
		return map;
	}
}
