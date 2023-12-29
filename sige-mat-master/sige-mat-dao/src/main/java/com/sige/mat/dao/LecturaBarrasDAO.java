package com.sige.mat.dao;


import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.LecturaBarrasDAOImpl;
import com.tesla.colegio.model.LecturaBarras;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;
import com.tesla.frmk.util.FechaUtil;

/**
 * Define m�todos DAO operations para la entidad lectura_barras.
 * 
 * @author MV
 *
 */
@Repository
public class LecturaBarrasDAO extends LecturaBarrasDAOImpl {
	final static Logger logger = Logger.getLogger(LecturaBarrasDAO.class);

	@Autowired
	private SQLUtil sqlUtil;
	
	public void actualizarAsistencia(String asistencia, Integer id) {
		String sql = "UPDATE asi_lectura_barras " + "SET asistencia=?" + "WHERE id=?";

		//logger.info(sql);

		jdbcTemplate.update(sql, asistencia, id);
	}
	
	/**
	 * Lista todos los alumnos que han realizado maracaci�n por dia
	 * @param date
	 * @return
	 */
	public List<LecturaBarras> alumnosPorFecha(Date date){
		
		String sql = "select * from asi_lectura_barras where date(fecha)=?";
		return jdbcTemplate.query(sql, new Object[]{FechaUtil.toStringMYQL(date)},new BeanPropertyRowMapper(LecturaBarras.class));

	}
	
	public List<Row> listAsistencia(Integer id_suc, Integer id_anio,Integer id_niv,Integer id_gra,Integer id_au, Date fec_inicial, String asis, Integer id_tra, Integer id_cic) {
		String q_au=" ";
		String q_niv=" ";
		String q_asis=" ";
		String q_gra=" ";
		String q_tra=" ";
		if(id_au!=null){
			q_au=" AND mat.id_au_asi="+id_au;
		 }
		if(id_niv!=null){
			q_niv=" AND niv.id ="+id_niv;
		 }
		
		if(id_gra!=null){
			q_gra=" AND gra.id="+id_gra;
		 }
		
		if(!"Y".equals(asis)){
			q_asis=" AND asi.asistencia='"+asis+"'";
		} 
		
		if(id_tra!=null){
			q_tra=" AND cta.id_tra="+id_tra;
		 }
		
		if(id_cic!=null){
			q_tra=" AND au.id_cic="+id_cic;
		 }

		String sql=" SELECT *   FROM ("
			    + " SELECT CONCAT(pera.ape_pat,' ', pera.ape_mat,' ', pera.nom) alumno, alu.cod,  gra.`nom` grado, au.`id`, au.`secc`, DATE_FORMAT(asi.fecha, '%Y/%m/%d') fecha, DATE_FORMAT(asi.fecha, '%H:%i:%s') fecha_ori, perf.`cel` celular," 
				+ " CASE  WHEN asi.asistencia='T' THEN 'TARDANZA' WHEN asi.asistencia='A' THEN 'PUNTUAL' ELSE 'FALTA' END AS asistencia_nom, IFNULL(asi.`asistencia`,'F') asistencia,  au.`id_per` , gra.id id_gra, niv.id id_niv, asi.id id_asi,"
				+ " asi.observacion observacion"
				+ " FROM  mat_matricula mat" 
				+ " LEFT JOIN col_aula au ON mat.id_au_asi=au.id" 
				+ " LEFT JOIN col_tutor_aula cta ON cta.id_au=au.id"
				+ " LEFT JOIN cat_grad gra ON  au.id_grad= gra.id" 
				+ " LEFT JOIN cat_nivel niv ON  gra.id_nvl= niv.id AND mat.id_niv=niv.id" 
				+ " LEFT JOIN alu_alumno alu ON mat.id_alu= alu.id" 
				+ " LEFT JOIN col_persona pera ON alu.id_per= pera.id" 
				+ " LEFT JOIN alu_familiar fam ON mat.id_fam=fam.id"
				+ " LEFT JOIN col_persona perf ON fam.id_per=perf.id"
				+ " LEFT JOIN asi_lectura_barras asi ON asi.codigo=alu.cod  AND DATE(asi.fecha)= :fecha "
				+ " WHERE 1=1"+q_gra+" "+q_au+" "+q_asis+" "+q_niv+" "+q_tra+" and :fecha>=mat.fecha and mat.id not in (select id_mat from col_situacion_mat col_sit INNER JOIN cat_col_situacion sit ON col_sit.id_sit=sit.id WHERE sit.cod='T' OR sit.cod='R' OR sit.cod='F')) t" 
				+ " LEFT JOIN `per_periodo` per  ON t.`id_per`=per.`id` AND per.`id_anio`="+id_anio
				+ " LEFT JOIN `ges_servicio` ser ON per.`id_srv`=ser.`id`"
				+ " WHERE ser.id_suc="+id_suc+" AND t.id_niv="+id_niv
				+ " ORDER BY t.id_gra ASC, t.`secc` ASC,  t.alumno ASC";
		//logger.info(sql);
		Param param = new Param();
		param.put("fecha", fec_inicial);
		List<Row> listAsistencia = sqlUtil.query(sql,param);
		
		return listAsistencia;
	} 
	
	public List<Map<String,Object>> getAsistencia (String codigo, Date fec) {
		String sql = "SELECT * FROM `asi_lectura_barras` WHERE codigo='"+codigo+"' AND date(fecha)= ?";
		List<Map<String,Object>> asistencia = jdbcTemplate.queryForList(sql,fec);
		//logger.info(sql);
		return asistencia;
	}
	
	public Date getUltimaAsistencia(Integer id_mat){
		
		String sql = "select a.cod, au.id_per from mat_matricula m "
				+ "\n inner join alu_alumno a on m.id_alu= a.id "
				+ "\n inner join col_aula au on au.id = m.id_au_asi"
				+ "\n where m.id=?";
		
		List<Row> list = sqlUtil.query(sql, new Object[]{id_mat});
		Row row = list.get(0);
		
		sql = "SELECT fecha FROM asi_lectura_barras WHERE codigo= ? AND id_per=? and fecha_ori!='00:00:00' order by fecha desc LIMIT 1";
		List<Row> datosFecha =  sqlUtil.query(sql, new Object[]{row.getString("cod"),row.getInteger("id_per")});
		
		if(datosFecha.size()>0){	
			Date fecha = datosFecha.get(0).getDate("fecha");
			//logger.debug("fecha:" + fecha);
			return fecha;
		} else {
			return null;
		}
		
	}
	
	public List<Map<String, Object>> listAsistenciaAula(Integer id_anio, Integer id_au){
		String sql = "SELECT SUBSTRING(CONCAT(alu.`ape_pat`,' ', alu.`ape_mat`,' ', alu.`nom`),1,36) alumno , niv.nom nivel, gra.nom grado, au.secc, alu.cod codigo, mat.`id_au_asi` "
				+ " FROM `mat_matricula` mat INNER JOIN `col_aula` au ON mat.id_au_asi=au.`id`"
				+ " INNER JOIN `alu_alumno` alu ON mat.`id_alu`=alu.`id`"
				+ " INNER JOIN cat_grad gra ON au.id_grad=gra.id"
				+ " INNER JOIN `per_periodo` per ON au.`id_per`=per.`id`"
				+ " INNER JOIN cat_nivel niv ON per.id_niv=niv.id"
				+ " WHERE per.`id_anio`=? AND mat.`id_au_asi`=?"
				+ " ORDER BY alu.ape_pat, alu.ape_mat, alu.nom";
		
		List<Map<String,Object>> listAsistenciaAula = jdbcTemplate.queryForList(sql, new Object[]{id_anio, id_au});			
		return listAsistenciaAula;
		
	}
	
	public Row rowDiasAsistencia(Integer id_anio, String cod, String anio, String mes){
		String sql = "SELECT DATE_FORMAT(asl.`fecha`, '%d/%m/%Y') fecha, asl.asistencia FROM `asi_lectura_barras` asl INNER JOIN `alu_alumno` alu ON asl.`codigo`=alu.`cod`"
				+ " INNER JOIN `mat_matricula` mat ON alu.`id`=mat.`id_alu`"
				+ " INNER JOIN col_aula au ON mat.`id_au_asi`=au.`id`"
				+ " INNER JOIN `per_periodo` per ON au.`id_per`=per.`id`"
				+ " WHERE per.`id_anio`="+id_anio+" AND alu.`cod`='"+cod+"' AND asl.`fecha` LIKE '"+anio+"-"+mes+"%' "
				+ " ORDER BY asl.fecha ";
		
		List<Row> listFaltas = sqlUtil.query(sql);
		Row row = new Row();
		for (Row r : listFaltas) {
			row.put(r.getString("fecha"), r.getString("asistencia"));
		}
		return row;
		
	}
	
	/**
	 * Lista la estadistica de Asistencia
	 * @param id_suc
	 * @param id_anio
	 * @param id_niv
	 * @param fec_ini
	 * @param fec_fin
	 * @return
	 */
	public List<Row> listEstadisticaAsistencia(Integer id_suc, Integer id_anio,Integer id_niv, Integer id_gir, Date fec_ini, Date fec_fin) {
		Param param = new Param();
		param.put("fec_ini", fec_ini);
		param.put("fec_fin", fec_fin);
		String sql ="SELECT DISTINCT cap.sucursal, cap.nivel, cap.nom grado, cap.secc,cap.id_gra, cap.total_capacidad, IFNULL(punt.puntuales,0) puntuales, IFNULL(tard.tardanza,0) tardanza, IFNULL(falt.faltas,0) faltas, IFNULL(sint.sin_tarjeta,0) sin_tarjeta";
			   sql += "\n FROM";
			   sql += "\n (SELECT suc.nom sucursal, niv.nom nivel,id_grad id_gra, au.secc, g.nom, COUNT(asi.id) total_capacidad";
			   sql += "\n FROM col_aula au";
			   sql += "\n INNER JOIN cat_grad g ON g.id = au.id_grad";
			   sql += "\n INNER JOIN per_periodo per ON per.id = au.id_per";
			   sql += "\n INNER JOIN ges_servicio srv ON srv.id = per.id_srv";
			   sql += "\n INNER JOIN cat_nivel niv ON niv.id = srv.id_niv";
			   sql += "\n INNER JOIN ges_sucursal suc ON suc.id = srv.id_suc";
			   sql += "\n INNER JOIN `mat_matricula` m ON m.`id_au_asi`=au.`id`";
			   sql += "\n INNER JOIN `alu_alumno` alu ON m.`id_alu`=alu.`id`";
			   sql += "\n INNER JOIN col_persona pers ON pers.id=alu.id_per ";
			   sql += "\n INNER JOIN `asi_lectura_barras` asi ON asi.`codigo`=alu.`cod`";
			   sql += "\n WHERE per.id_anio="+id_anio+" AND (DATE(asi.`fecha`) BETWEEN :fec_ini AND :fec_fin) ";
			   if(id_suc!=null)
				  sql += "\n AND suc.id="+id_suc;
			   if(id_niv!=null)
				  sql += "\n AND niv.id="+id_niv;
			   if(id_gir!=null)
					  sql += "\n AND srv.id_gir="+id_gir;
			   sql += "\n GROUP BY 1,2,3,4,5 ) cap ";
			   sql += "\n LEFT JOIN";
			   sql += "\n (SELECT suc.nom sucursal, niv.nom nivel, m.id_gra, g.nom, au.secc, COUNT(asi.id) puntuales";
			   sql += "\n FROM mat_matricula m";
			   sql += "\n INNER JOIN cat_grad g ON g.id = m.id_gra";
			   sql += "\n INNER JOIN col_aula au ON au.id = m.id_au_asi";
			   sql += "\n INNER JOIN cat_nivel niv ON niv.id = g.id_nvl";
			   sql += "\n INNER JOIN per_periodo per ON per.id = au.id_per";
			   sql += "\n INNER JOIN ges_servicio srv ON srv.id = per.id_srv";
			   sql += "\n INNER JOIN ges_sucursal suc ON suc.id = srv.id_suc";
			   sql += "\n INNER JOIN `alu_alumno` alu ON m.`id_alu`=alu.`id`";
			   sql += "\n INNER JOIN col_persona pers ON pers.id=alu.id_per ";
			   sql += "\n INNER JOIN `asi_lectura_barras` asi ON asi.`codigo`=alu.`cod`";
			   sql += "\n WHERE per.id_anio="+id_anio+" AND asi.asistencia='A' AND  DATE_FORMAT(asi.fecha, '%H:%i:%s')<>'00:00:00' AND (DATE(asi.`fecha`) BETWEEN :fec_ini AND :fec_fin) ";
			   if(id_suc!=null)
					sql += "\n AND suc.id="+id_suc;
			   if(id_niv!=null)
					sql += "\n AND niv.id="+id_niv;	   
			   if(id_gir!=null)
					  sql += "\n AND srv.id_gir="+id_gir;
			   sql += "\n GROUP BY 1,2,3,4,5) punt ON cap.id_gra = punt.id_gra AND cap.nivel = punt.nivel AND cap.sucursal = punt.sucursal AND cap.secc = punt.secc";
			   sql += "\n LEFT JOIN";
			   sql += "\n (SELECT suc.nom sucursal, niv.nom nivel, m.id_gra, g.nom, au.secc, COUNT(asi.id) tardanza";
			   sql += "\n FROM mat_matricula m";
			   sql += "\n INNER JOIN cat_grad g ON g.id = m.id_gra";
			   sql += "\n INNER JOIN col_aula au ON au.id = m.id_au_asi";
			   sql += "\n INNER JOIN cat_nivel niv ON niv.id = g.id_nvl";
			   sql += "\n INNER JOIN per_periodo per ON per.id = au.id_per";
			   sql += "\n INNER JOIN ges_servicio srv ON srv.id = per.id_srv";
			   sql += "\n INNER JOIN ges_sucursal suc ON suc.id = srv.id_suc";
			   sql += "\n INNER JOIN `alu_alumno` alu ON m.`id_alu`=alu.`id`";
			   sql += "\n INNER JOIN col_persona pers ON pers.id=alu.id_per ";
			   sql += "\n INNER JOIN `asi_lectura_barras` asi ON asi.`codigo`=alu.`cod`";
			   sql += "\n WHERE per.id_anio="+id_anio+" AND asi.asistencia='T'  AND  DATE_FORMAT(asi.fecha, '%H:%i:%s')<>'00:00:00' AND (DATE(asi.`fecha`) BETWEEN :fec_ini AND :fec_fin) ";
			   if(id_suc!=null)
					sql += "\n AND suc.id="+id_suc;
			   if(id_niv!=null)
					sql += "\n AND niv.id="+id_niv;	 
			   if(id_gir!=null)
					  sql += "\n AND srv.id_gir="+id_gir;
			   sql += "\n GROUP BY 1,2,3,4,5)tard ON tard.id_gra = cap.id_gra AND tard.nivel = cap.nivel AND tard.sucursal = cap.sucursal AND tard.secc = cap.secc";
			   sql += "\n LEFT JOIN (SELECT suc.nom sucursal, niv.nom nivel, m.id_gra, g.nom, au.secc, COUNT(asi.id) faltas";
			   sql += "\n FROM mat_matricula m";
			   sql += "\n INNER JOIN cat_grad g ON g.id = m.id_gra";
			   sql += "\n INNER JOIN col_aula au ON au.id = m.id_au_asi";
			   sql += "\n INNER JOIN cat_nivel niv ON niv.id = g.id_nvl";
			   sql += "\n INNER JOIN per_periodo per ON per.id = au.id_per";
			   sql += "\n INNER JOIN ges_servicio srv ON srv.id = per.id_srv";
			   sql += "\n INNER JOIN ges_sucursal suc ON suc.id = srv.id_suc";
			   sql += "\n INNER JOIN `alu_alumno` alu ON m.`id_alu`=alu.`id`";
			   sql += "\n INNER JOIN col_persona pers ON pers.id=alu.id_per ";
			   sql += "\n INNER JOIN `asi_lectura_barras` asi ON asi.`codigo`=alu.`cod`";
			   sql += "\n WHERE per.id_anio="+id_anio+" AND asi.asistencia='F'  AND (DATE(asi.`fecha`) BETWEEN :fec_ini AND :fec_fin) ";
			   if(id_suc!=null)
					sql += "\n AND suc.id="+id_suc;
			   if(id_niv!=null)
					sql += "\n AND niv.id="+id_niv;	 
			   if(id_gir!=null)
					  sql += "\n AND srv.id_gir="+id_gir;
			   sql += "\n GROUP BY 1,2,3,4,5)falt ON cap.id_gra = falt.id_gra AND falt.nivel = cap.nivel AND falt.sucursal = cap.sucursal AND falt.secc = cap.secc";
			   sql += "\n LEFT JOIN (SELECT suc.nom sucursal, niv.nom nivel, m.id_gra, g.nom, au.secc, COUNT(asi.id) sin_tarjeta";
			   sql += "\n FROM mat_matricula m";
			   sql += "\n INNER JOIN cat_grad g ON g.id = m.id_gra";
			   sql += "\n INNER JOIN col_aula au ON au.id = m.id_au_asi";
			   sql += "\n INNER JOIN cat_nivel niv ON niv.id = g.id_nvl";
			   sql += "\n INNER JOIN per_periodo per ON per.id = au.id_per";
			   sql += "\n INNER JOIN ges_servicio srv ON srv.id = per.id_srv";
			   sql += "\n INNER JOIN ges_sucursal suc ON suc.id = srv.id_suc";
			   sql += "\n INNER JOIN `alu_alumno` alu ON m.`id_alu`=alu.`id`";
			   sql += "\n INNER JOIN `asi_lectura_barras` asi ON asi.`codigo`=alu.`cod`";
			   sql += "\n WHERE per.id_anio="+id_anio+" AND asi.asistencia<>'F' AND  DATE_FORMAT(asi.fecha, '%H:%i:%s')='00:00:00'  AND (DATE(asi.`fecha`) BETWEEN :fec_ini AND :fec_fin) ";
			   if(id_suc!=null)
					sql += "\n AND suc.id="+id_suc;
			   if(id_niv!=null)
					sql += "\n AND niv.id="+id_niv;	 
			   if(id_gir!=null)
					  sql += "\n AND srv.id_gir="+id_gir;
			   sql += "\n GROUP BY 1,2,3,4,5)sint ON sint.id_gra = cap.id_gra AND sint.nivel = cap.nivel AND sint.sucursal = cap.sucursal AND sint.secc = cap.secc ";
			   sql += "\n ORDER BY   cap.sucursal DESC, cap.id_gra ASC, cap.secc ASC";
			   
			   return sqlUtil.query(sql,param);
	}
	
	/**
	 * Lista la asistencia por alumno
	 * @param fec_ini
	 * @param fec_fin
	 * @param asistencia
	 * @param alumno
	 * @return
	 */
	public List<Row> listAsistenciaxAlumno(Date fec_ini, Date fec_fin, String asis,Integer id_alu) {
		String q_asis="";
		if(!"Y".equals(asis)){
			q_asis=" AND asi.asistencia='"+asis+"'";
		} 
		Param param = new Param();
		param.put("fec_ini", fec_ini);
		param.put("fec_fin", fec_fin);
		param.put("id_alu", id_alu);
		
		String sql="SELECT  DISTINCT DATE_FORMAT(asi.fecha, '%d/%m/%Y') fecha, DATE_FORMAT(asi.fecha, '%H:%i:%s') fecha_ori, gir.nom giro,  CASE  WHEN asi.asistencia='T' THEN 'TARDANZA' WHEN asi.asistencia='A' THEN 'PUNTUAL' ELSE 'FALTA' END AS asistencia_nom";
			   sql += "\n FROM `asi_lectura_barras` asi INNER JOIN `alu_alumno` alu ON asi.`codigo`=alu.`cod`";
			   sql += "\n INNER JOIN mat_matricula mat ON alu.id=mat.id_alu ";
			   sql += "\n INNER JOIN per_periodo per ON mat.id_per=per.id AND asi.id_per=per.id ";
			   sql += "\n INNER JOIN ges_servicio srv ON per.id_srv=srv.id ";
			   sql += "\n INNER JOIN ges_giro_negocio gir ON gir.id=srv.id_gir ";
			   sql += "\n WHERE alu.id=:id_alu AND (DATE(asi.`fecha`) BETWEEN :fec_ini AND :fec_fin) "+q_asis;
			   sql += "ORDER BY asi.fecha";
			   return sqlUtil.query(sql,param);
	}
	
	public List<Row> ranqueoAsistencia(Date fec_ini, Date fec_fin, String asis,Integer id_anio, Integer id_suc, Integer id_niv, Integer id_au) {

		Param param = new Param();
		param.put("fec_ini", fec_ini);
		param.put("fec_fin", fec_fin);
		
		String sql="SELECT * FROM (SELECT CONCAT(pers.ape_pat,' ', pers.ape_mat,' ', pers.nom) alumno, niv.`nom` nivel, g.`nom` grado, au.`secc`, COUNT(asi.id) cantidad";
			   sql += " FROM mat_matricula m";
			   sql += " INNER JOIN cat_grad g ON g.id = m.id_gra";
			   sql += " INNER JOIN col_aula au ON au.id = m.id_au_asi";
			   sql += " INNER JOIN cat_nivel niv ON niv.id = g.id_nvl";
			   sql += " INNER JOIN per_periodo per ON per.id = au.id_per";
			   sql += " INNER JOIN ges_servicio srv ON srv.id = per.id_srv";
			   sql += " INNER JOIN ges_sucursal suc ON suc.id = srv.id_suc";
			   sql += " INNER JOIN `alu_alumno` alu ON m.`id_alu`=alu.`id`";
			   sql += " INNER JOIN col_persona pers on alu.id_per=pers.id";
			   sql += " INNER JOIN `asi_lectura_barras` asi ON asi.`codigo`=alu.`cod`";
			   sql += " WHERE per.id_anio="+id_anio+" AND asi.asistencia='"+asis+"' AND (DATE(asi.`fecha`) BETWEEN :fec_ini AND :fec_fin)";
			if(id_suc!=null)
				sql += "\n AND suc.id="+id_suc;	
			if(id_niv!=null)
				sql += "\n AND niv.id="+id_niv;	
			if(id_au!=null)
				sql += "\n AND au.id="+id_au;	
			   sql += " GROUP BY alu.`id`)t";
			   sql += " ORDER BY cantidad DESC";
			  return sqlUtil.query(sql,param);
	}
	
}
