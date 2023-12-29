package com.sige.mat.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.sige.common.enums.EnumSituacionFinal;
import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.MatriculaDAOImpl;
import com.sige.web.security.TokenSeguridad;
import com.tesla.colegio.model.AcademicoPago;
import com.tesla.colegio.model.Aula;
import com.tesla.colegio.model.Matricula;
import com.tesla.colegio.util.Constante;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;
import com.tesla.frmk.sql.SQLFrmkUtil;
import com.tesla.frmk.util.FechaUtil; 

/**
 * Define m�todos DAO operations para la entidad matricula.
 * 
 * @author MV
 *
 */
@Repository
public class MatriculaDAO extends MatriculaDAOImpl {
	final static Logger logger = Logger.getLogger(MatriculaDAO.class);

	@Autowired
	private TokenSeguridad tokenSeguridad;

	@Autowired
	private SQLUtil sqlUtil;

	/**
	 * Consulta si existe un cronograma vigente (alumnos antiguos - extemporaneo
	 * y normal)
	 * 
	 * @param id_anio
	 * @param tipoCronograma
	 * @return
	 */
	public boolean alumnosAntiguosTienenVigente(Integer id_anio) {

		String sql = "select id from mat_cronograma where id_anio=? and curdate() = fec_mat and est=?";

		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, new Object[] { id_anio, "A" });

		sql = "select id from mat_conf_fechas where id_anio=? and CURRENT_TIMESTAMP() >=del and CURRENT_TIMESTAMP() <=al and tipo=?";

		List<Map<String, Object>> list2 = jdbcTemplate.queryForList(sql, new Object[] { id_anio, "AC" });

		return list.size() > 0 && list2.size() > 0;
	}

	public List<Map<String, Object>> listPagos(Integer id_per) {

		String sql = "SELECT  p.id, p.mes, p.monto, c.nom"
				+ " FROM pag_pago_programacion p left join cat_concepto_pago c on p.id_cpa = c.id" + " WHERE c.id="
				+ Constante.TIPO_CONCEPTO_MATRICULA + " or c.id=" + Constante.TIPO_CONCEPTO_CUOTA_ING + " and id_per="
				+ id_per;
		List<Map<String, Object>> listPagos = jdbcTemplate.queryForList(sql);
		return listPagos;
	}

	/* Devuelve la lista de pagos programados tipo mensualidad. */

	public List<Map<String, Object>> listPagosMens(Integer id_per) {

		String sql = "SELECT p.id, p.mes, p.monto, c.nom"
				+ " FROM pag_pago_programacion p left join cat_concepto_pago c on p.id_cpa = c.id" + " WHERE c.id="
				+ Constante.TIPO_CONCEPTO_MENSUALIDAD + " and id_per=" + id_per;
		List<Map<String, Object>> listPagosMens = jdbcTemplate.queryForList(sql);
		return listPagosMens;
	}

	/* Devuelve los matriculados del a�o anterior y grado anterior */
	public Integer getTotalMatriculadosGradoAnterior(Integer id_anio, Integer id_gra) {
		String sql = "SELECT COUNT(mat.`id`) matriculados FROM `mat_matricula` mat INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.`id`"
				+ " INNER JOIN `cat_grad` gra ON au.`id_grad`=gra.`id` INNER JOIN `per_periodo` per ON au.`id_per`=per.`id`"
				+ " WHERE per.`id_anio`=" + id_anio + " AND gra.`id`=" + id_gra
				+ " AND mat.`id` NOT IN (SELECT id_mat FROM `col_situacion_mat` sit INNER JOIN `cat_col_situacion` ccs ON sit.`id_sit`=ccs.`id` WHERE ccs.`cod`='T' OR ccs.`cod`='F' OR ccs.`cod`='R' OR ccs.`cod`='D')";
		List<Map<String, Object>> insctritos = jdbcTemplate.queryForList(sql);
		return Integer.parseInt(insctritos.get(0).get("matriculados").toString());
	}

	/* Devuelve los desaprobados del mismo grado pero del anio anterior */
	public Integer getRepitentes(Integer id_anio, Integer id_gra) {
		String sql = "SELECT COUNT(mat.`id`) repitentes FROM `mat_matricula` mat INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.`id`"
				+ " INNER JOIN `cat_grad` gra ON au.`id_grad`=gra.`id` INNER JOIN `per_periodo` per ON au.`id_per`=per.`id`"
				+ " WHERE per.`id_anio`=" + id_anio + " AND gra.`id`=" + id_gra
				+ " AND mat.`id` IN (SELECT id_mat FROM `col_situacion_mat` sit INNER JOIN `cat_col_situacion` ccs ON sit.`id_sit`=ccs.`id` WHERE ccs.`cod`='D')";
		List<Map<String, Object>> insctritos = jdbcTemplate.queryForList(sql);
		return Integer.parseInt(insctritos.get(0).get("repitentes").toString());
	}

	/* Devuelve datos del pago realizado por el alumno */
	public List<Map<String, Object>> PagoRealizado(Integer id_mat) {
		String sql = "select p.*, pd.* from pag_pago_realizado p, pag_pago_detalle pd where p.id=pd.id_pre and p.id_mat="
				+ id_mat;
		List<Map<String, Object>> Pago = jdbcTemplate.queryForList(sql);
		return Pago;
	}

	/* Devuelve los inscritos por aula */
	public Integer getTotalInscritos(Integer id_au) {
		String sql = "select count(id ) total from mat_matricula where id_au_asi=" + id_au + "";
		List<Map<String, Object>> insctritos = jdbcTemplate.queryForList(sql);
		return Integer.parseInt(insctritos.get(0).get("total").toString());
	}

	/* Devuelve los inscritos por grado */
	public Integer getTotalMatriculadosGrado(Integer id_per, Integer id_gra) {
		String sql = "select count(id) total from mat_matricula where id_per=" + id_per + " and id_gra=" + id_gra;
		List<Map<String, Object>> insctritos = jdbcTemplate.queryForList(sql);
		return Integer.parseInt(insctritos.get(0).get("total").toString());
	}

	/* Devuelve los inscritos por aula */
	public Integer getInscritosNuevos(Integer id_au) {
		String sql = "select count(id ) total from mat_matricula where id_au_asi=" + id_au //TODO SE CAMBIO ID_AU
				+ " and id not in (select id_alu from mat_reserva where id_au=" + id_au + ") ";
		List<Map<String, Object>> insctritos = jdbcTemplate.queryForList(sql);
		return Integer.parseInt(insctritos.get(0).get("total").toString());
	}

	/* Devuelve los inscritos NUEVOS por aula */
	public Integer getInscritosNuevos(Integer id_au, Integer id_au_ant) {
		// String sql = "select count(id ) total from mat_matricula where
		// id_au=" + id_au + " and id not in (select id_alu from mat_reserva
		// where id_au=" + id_au +")";
		String sql = null;
		Integer inscritow = 0;

		// sumar los que no estan en el aula sugerida
		if (id_au_ant != null && !id_au_ant.equals(0)) {
			sql = "select count(id ) total from mat_matricula where id_au_asi=" + id_au//TODO id_au
					+ " and id_alu not in (select ant.id_alu from mat_matricula ant where ant.id_au_asi=" + id_au_ant//TODO id_au
					+ ") ";
			List<Map<String, Object>> insctritos = jdbcTemplate.queryForList(sql);
			inscritow = Integer.parseInt(insctritos.get(0).get("total").toString());
			logger.info("umar los que no estan en el aula sugerida:" + sql);

		}

		// sumar los alumnos que no esten en sugeridos

		sql = "select count(m.id ) total from mat_matricula m where m.id_au_asi=" + id_au
				+ " and exists(select sug.id from mat_seccion_sugerida sug where sug.id_mat=m.id )";

		List<Map<String, Object>> insctritos = jdbcTemplate.queryForList(sql);
		inscritow = inscritow + Integer.parseInt(insctritos.get(0).get("total").toString());

		// sql = "select count(id ) total from mat_matricula where id_au=" +
		// id_au ;

		logger.info("umar los alumnos que no esten en sugeridos:" + sql);
		return inscritow;
	}

	/* Devuelve los que pasan de a�o por aula */
	/*
	 * public Integer getPasanAnio(Integer id_au) { String sql =
	 * "select count(id ) total from mat_matricula where id_au=" + id_au +
	 * " and id_sit=1"; List<Map<String,Object>> insctritos =
	 * jdbcTemplate.queryForList(sql); return
	 * Integer.parseInt(insctritos.get(0).get("total").toString()); }
	 */

	/* Devuelve el total de sugeridos de la seccion */
	public Integer getSugeridosSeccion(Integer id_au) {
		String sql = "select count(id ) total from mat_seccion_sugerida where id_au_nue=" + id_au;
		logger.info("getSugeridosSeccion:" + sql);
		List<Map<String, Object>> insctritos = jdbcTemplate.queryForList(sql);
		return Integer.parseInt(insctritos.get(0).get("total").toString());
	}

	/* Devuelve el total de sugeridos de la seccion */
	public Integer getAlumnosSeVAmOtraSeccion(Integer id_au, Integer id_au_ant) {
		String sql = "select count(mat.id) total from mat_matricula mat where mat.id=" + id_au_ant
				+ " and exists( select count(1) from mat_seccion_sugerida sug where sug.id_mat=mat.id and sug.id_au_nue!="
				+ id_au + ")";
		logger.info("getAlumnosSeVAmOtraSeccion:" + sql);

		List<Map<String, Object>> insctritos = jdbcTemplate.queryForList(sql);
		return Integer.parseInt(insctritos.get(0).get("total").toString());
	}

	/*
	 * Devuelve el total de alumnos que se van por ser sugeridos a otra seccion
	 */
	public Integer getPasanAnio(Integer id_au_ant) {
		// String sql = "select count(m.id ) total from mat_matricula m where
		// m.id_au=" + id_au_ant + " and m.id_sit=1 and not exists(select
		// count(s.id ) total from mat_seccion_sugerida s where s.id_mat=m.id)";
		String sql = "select count(m.id ) total from mat_matricula m where m.id_au_asi=" + id_au_ant + " and m.id_sit=1 ";
		logger.info("getPasanAnio:" + sql);
		List<Map<String, Object>> insctritos = jdbcTemplate.queryForList(sql);
		return Integer.parseInt(insctritos.get(0).get("total").toString());
	}

	public Integer getSeVanOtraSeccion(Integer id_au_ant) {
		 
		String sql = "select count(s.id ) total from mat_seccion_sugerida s where s.id_au_nue = " + id_au_ant;
		List<Map<String, Object>> insctritos = jdbcTemplate.queryForList(sql);
		return Integer.parseInt(insctritos.get(0).get("total").toString());
	}

	/* Actualiza la observacion de la matricula */

	public int UpdObs_Mat(Matricula matricula) {

		// update
		String sql = "UPDATE mat_matricula " + "SET obs=?, usr_act=?, fec_act=? " + "WHERE id=?";

		logger.info(sql);
		return jdbcTemplate.update(sql, matricula.getObs(), tokenSeguridad.getId(), matricula.getId(),new Date());

	}

	/**
	 * Lista de alumnos que puede matricularse
	 * 
	 * @param id_anio
	 * @param nomApeAlumno
	 * @return
	 */
	public List<Map<String, Object>> listQueTieneReserva(String alumno, Integer id_anio, String id_suc) {
		String sql = "select alu.id id_alu, alu.nro_doc, alu.ape_pat, alu.ape_mat, alu.nom, mat.id id_mat, srv.nom servicio, gra.nom grado ";
		sql += " from mat_reserva res";
		sql += " inner join alu_alumno alu on alu.id = res.id_alu";
		sql += " inner join per_periodo per on per.id = res.id_per";
		sql += " inner join ges_servicio srv on srv.id = per.id_srv";
		sql += " INNER JOIN `ges_sucursal` suc ON srv.`id_suc`=suc.id";
		sql += " inner join cat_grad gra on gra.id = res.id_gra";
		sql += " left join mat_matricula mat on (mat.id_alu=alu.id and mat.id_per= res.id_per) "; // @TODO
																									// falta
																									// probar
																									// cuando
																									// el
																									// alumno
																									// postula
																									// en
																									// dos
																									// a�os
		sql += " where per.id_anio=" + id_anio;
		if (id_suc != null)
			sql += " and suc.id=" + id_suc;
		sql += " order by alu.ape_pat asc";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		logger.info(sql);
		return list;
	}

	/**
	 * Lista de alumnos que tienen reserva para matricula (Alumnos nuevos que
	 * tienen reserva)
	 * 
	 * @param id_anio
	 * @param nomApeAlumno
	 * @return
	 */
	public List<Map<String, Object>> listAptosParaMatricula(String alumno, Integer id_anio, String id_suc) {
		String sql = "select alu.id id_alu, alu.nro_doc, alu.ape_pat, alu.ape_mat, alu.nom, mat1.id id_mat, srv.nom servicio, gra.nom grado ";
		sql += " from eva_matr_vacante mat";
		sql += " inner join eva_evaluacion_vac eva on eva.id = mat.id_eva";
		sql += " inner join alu_alumno alu on alu.id = mat.id_alu";
		sql += " inner join per_periodo per on per.id = eva.id_per";
		sql += " inner join ges_servicio srv on srv.id = per.id_srv";
		sql += " INNER JOIN `ges_sucursal` suc ON srv.`id_suc`=suc.id";
		sql += " inner join cat_grad gra on gra.id = mat.id_gra";
		sql += " left join mat_matricula mat1 on (mat1.id_alu=alu.id ) ";
		sql += " left join per_periodo permat on (permat.id = mat1.id_per and per.id_anio=permat.id_anio and per.id_srv= permat.id_srv)";
		// sql += " where mat.res='A' and per.id_anio=" + id_anio + " and
		// CONCAT(alu.ape_mat,' ',alu.ape_pat, ' ', alu.nom) LIKE '"+
		// alumno.toUpperCase()+"%'";
		sql += " where mat.res='A' and per.id_anio=" + id_anio;
		// sql += " and ( upper(alu.ape_pat) LIKE '"+ alumno.toUpperCase()+"%'
		// or upper(alu.ape_mat) LIKE '"+ alumno.toUpperCase()+"%' ) ";
		sql += " and ( upper(CONCAT(alu.ape_pat,' ',alu.ape_mat, ' ', alu.nom)) LIKE '" + alumno.toUpperCase()
				+ "%'  ) ";
		if (id_suc != null)
			sql += " and suc.id=" + id_suc;
		sql += " order by alu.ape_pat asc";
		logger.info(sql);

		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		return list;
	}

	/**
	 * Query para alumnos antiguos
	 * 
	 * @param alumno
	 * @param id_anio
	 * @param id_suc
	 * @return
	 */
	public List<Map<String, Object>> listaTodosAlumnos(String tipoCronogramaVigente, String alumno, Integer id_anio,
			Integer id_anio_anterior, Integer id_suc) {

		if (tipoCronogramaVigente.equals("AC")) {

			// alumnos del anio anterior
			StringBuilder sql = new StringBuilder(
					"select distinct alu.id id_alu, p.nro_doc, p.ape_pat, p.ape_mat, p.nom, mat_ant.id id_mat_ant, mat.id id_mat, 'A' tipo, gfa.id_gpf FROM `alu_alumno` alu");
			sql.append(" inner join `alu_gru_fam_alumno` gfa on alu.id=gfa.id_alu ");
			sql.append(" inner join `col_persona` p on alu.id_per=p.id ");
			sql.append(" inner join mat_matricula mat_ant on  mat_ant.id_alu = alu.id  ");
			sql.append(" inner join per_periodo per_ant on per_ant.id = mat_ant.id_per ");
			sql.append(" inner join ges_servicio srv_ant on srv_ant.id = per_ant.id_srv ");
			sql.append(" inner join mat_cronograma cro on  cro.id_anio = ? ");
			sql.append(" left join ( mat_matricula mat inner join per_periodo per  on mat.id_per= per.id and per.id_anio=? and per.id_tpe=1) on mat.id_alu = alu.id");//anidado
			sql.append(" left join mat_seccion_sugerida sug on  sug.id_mat = mat_ant.id and sug.id_anio=? ");
			sql.append(" left join col_aula au_sug on au_sug.id = sug.id_au_nue ");
			sql.append(" left join per_periodo per_sug on per_sug.id = au_sug.id_per ");
			sql.append(" left join ges_servicio srv_sug on srv_sug.id = per_sug.id_srv ");
			sql.append(" where UPPER(SUBSTR(p.ape_pat,1,CHAR_LENGTH(cro.del)))>=UPPER(cro.del) ");
			sql.append(" AND UPPER(SUBSTR(p.ape_pat,1,CHAR_LENGTH(cro.al)))<=UPPER(cro.al) and cro.fec_mat=current_date");
			sql.append(" and  upper(CONCAT(p.ape_pat,' ',p.ape_mat, ' ', p.nom)) LIKE '%" + alumno.toUpperCase() + "%'");
			sql.append(" and per_ant.id_anio=? ");
			//sql.append(" and ((srv_sug.id_suc is not null and srv_sug.id_suc=?) or (srv_sug.id_suc is null and srv_ant.id_suc= ?))");
			sql.append("and ( (mat.id is null && ((srv_sug.id_suc is not null and srv_sug.id_suc=?) or (srv_sug.id_suc is null and srv_ant.id_suc= ?))) || (mat.id is not null &&  per.id_suc=?) )");
			sql.append(" and mat_ant.id_sit in (1,3) and (mat_ant.id_gra!=14 OR (mat_ant.id_gra=14 and mat_ant.id_sit!=1))");// que

			sql.append(" order by p.ape_pat,p.ape_mat,p.nom");


			logger.info(sql);

			List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString(),
					new Object[] { id_anio, id_anio, id_anio, id_anio_anterior, id_suc, id_suc, id_suc });

			return list;

		}

		if (tipoCronogramaVigente.equals("AS")) {

			// alumnos del a�o anterior CON O sin matricula ( falta probar )
			StringBuilder sql = new StringBuilder();
			//MATRICULADOS SIN SOLICITUD
			sql.append("\n  select distinct alu.id id_alu, pers.nro_doc, pers.ape_pat, pers.ape_mat, pers.nom, mat_ant.id id_mat_ant, mat.id id_mat, 'A' tipo,  gfa.id_gpf "); 
			sql.append("\n FROM alu_alumno alu");
			sql.append("\n inner join col_persona pers on alu.id_per = pers.id ");
			sql.append("\n inner join `alu_gru_fam_alumno` gfa on alu.id=gfa.id_alu ");
			sql.append("\n inner join mat_matricula mat on  mat.id_alu = alu.id");  
			sql.append("\n inner join per_periodo per on per.id = mat.id_per and per.id_tpe=1"); 
			sql.append("\n left join ( mat_matricula mat_ant inner join col_aula au_ant on au_ant.id= mat_ant.id_au_asi inner join per_periodo per_ant  on au_ant.id_per= per_ant.id and per_ant.id_anio=" + id_anio_anterior + " )  on mat_ant.id_alu = alu.id");
			sql.append("\n where"); 
			sql.append("\n  upper(CONCAT(pers.ape_pat,' ',pers.ape_mat, ' ', pers.nom)) LIKE '%" + alumno.toUpperCase() + "%'");
			sql.append("\n and per.id_anio= " + id_anio+" and per.id_suc="+id_suc); //aqui le mande el id del local
			sql.append("\n and alu.id not in"); 
			sql.append("\n (select s.id_alu from mat_solicitud s where s.est='A' and s.id_anio=" + id_anio + " and s.id_suc_des=" + id_suc + " )");

			//ANTOGIO NO MATRICULADO SIN SOLICITUD
			StringBuilder sqlA = new StringBuilder("select distinct alu.id id_alu, pers.nro_doc, pers.ape_pat, pers.ape_mat, pers.nom, mat_ant.id id_mat_ant, mat.id id_mat, 'A' tipo, gfa.id_gpf "); 
			sqlA.append("\n FROM alu_alumno alu");
			sqlA.append("\n inner join col_persona pers on alu.id_per = pers.id ");
			sqlA.append("\n inner join `alu_gru_fam_alumno` gfa on alu.id=gfa.id_alu ");
			sqlA.append("\n inner join mat_matricula mat_ant on  mat_ant.id_alu = alu.id");  
			sqlA.append("\n inner join col_aula au_ant on au_ant.id = mat_ant.id_au_asi");
			sqlA.append("\n inner join per_periodo per_ant on per_ant.id = au_ant.id_per and per_ant.id_tpe=1");
			sqlA.append("\n inner join ges_servicio srv_ant on srv_ant.id = per_ant.id_srv");
			sqlA.append("\n left join ( mat_matricula mat inner join per_periodo per  on mat.id_per= per.id and per.id_anio=" + id_anio + " and per.id_tpe=1)  on mat.id_alu = alu.id");
			sqlA.append("\n where ");
			sqlA.append("\n upper(CONCAT(pers.ape_pat,' ',pers.ape_mat, ' ', pers.nom)) LIKE '%" + alumno.toUpperCase() + "%'");
			sqlA.append("\n and per_ant.id_anio=" + id_anio_anterior + " and srv_ant.id_suc= " + id_suc);
			sqlA.append("\n and mat_ant.id_sit in (1,3)");
			sqlA.append("\n and (mat_ant.id_gra!=14 OR (mat_ant.id_gra=14 and mat_ant.id_sit!=1))");
			sqlA.append("\n and mat.id is null");
			sqlA.append("\n and alu.id not in"); 
			sqlA.append("\n (select s.id_alu from mat_solicitud s where s.est='A' and s.id_anio=" + id_anio + " and s.id_suc_des="  + id_suc + " )");
			
//
			
			//matriculados CON SOLICITUD
			StringBuilder sqlSolicitud = new StringBuilder("select alu.id id_alu, pers.nro_doc, pers.ape_pat, pers.ape_mat, pers.nom, mat_ant.id id_mat_ant, mat.id id_mat, 'A' tipo, gfa.id_gpf ");   
			sqlSolicitud.append("\n from mat_solicitud sol  ");
			sqlSolicitud.append("\n inner join alu_alumno alu on alu.id = sol.id_alu ");
			sqlSolicitud.append("\n inner join col_persona pers on alu.id_per = pers.id ");
			sqlSolicitud.append("\n inner join `alu_gru_fam_alumno` gfa on alu.id=gfa.id_alu ");
			sqlSolicitud.append("\n inner join mat_matricula mat on mat.id_alu = sol.id_alu"); 
			sqlSolicitud.append("\n inner join per_periodo per on per.id = mat.id_per and per.id_tpe=1");
			sqlSolicitud.append("\n left join mat_matricula mat_ant on mat_ant.id_alu = sol.id_alu");
			sqlSolicitud.append("\n left join col_aula au_ant on au_ant.id = mat_ant.id_au_asi");
			sqlSolicitud.append("\n left join per_periodo  per_ant on per_ant.id = au_ant.id_per");  
			sqlSolicitud.append("\n where  upper(CONCAT(pers.ape_pat,' ',pers.ape_mat, ' ', pers.nom))  LIKE '%" + alumno.toUpperCase() + "%'"); 
			sqlSolicitud.append("\n and sol.id_suc_des =" + id_suc+" and sol.id_anio="+id_anio);
			sqlSolicitud.append("\n and sol.est='A' ");
			sqlSolicitud.append("\n and per.id_anio=" + id_anio);
			sqlSolicitud.append("\n and per_ant.id_anio=" + id_anio_anterior);

			//ANITUGOS NO matriculados CON SOLICITUD
			StringBuilder sqlNoMAtSoli = new StringBuilder("select alu.id id_alu, pers.nro_doc, pers.ape_pat, pers.ape_mat, pers.nom, mat_ant.id id_mat_ant, mat.id id_mat, 'A' tipo, gfa.id_gpf");   
			sqlNoMAtSoli.append("\n from mat_solicitud sol  ");
			sqlNoMAtSoli.append("\n inner join alu_alumno alu on alu.id = sol.id_alu ");
			sqlNoMAtSoli.append("\n inner join col_persona pers on alu.id_per = pers.id ");
			sqlNoMAtSoli.append("\n inner join `alu_gru_fam_alumno` gfa on alu.id=gfa.id_alu ");
			sqlNoMAtSoli.append("\n inner join mat_matricula mat_ant on mat_ant.id_alu = sol.id_alu"); 
			sqlNoMAtSoli.append("\n inner join col_aula au_ant on au_ant.id = mat_ant.id_au_asi");
			sqlNoMAtSoli.append("\n inner join per_periodo  per_ant on per_ant.id = au_ant.id_per");  
			sqlNoMAtSoli.append("\n left join (mat_matricula mat inner join per_periodo per on per.id = mat.id_per and per.id_anio=" + id_anio + " and per.id_tpe=1) on mat.id_alu = sol.id_alu");
			sqlNoMAtSoli.append("\n where  upper(CONCAT(pers.ape_pat,' ',pers.ape_mat, ' ', pers.nom))  LIKE '%" + alumno.toUpperCase() + "%'"); 
			sqlNoMAtSoli.append("\n and sol.id_suc_des =" + id_suc+" and sol.id_anio="+id_anio);
			sqlNoMAtSoli.append("\n and sol.est='A' ");
			sqlNoMAtSoli.append("\n and per_ant.id_anio=" + id_anio_anterior+" AND (mat_ant.id_gra!=14 OR (mat_ant.id_gra=14 and mat_ant.id_sit!=1))");
			sqlNoMAtSoli.append("\n and mat.id is null");



			//RESERVADOS CON SOLICITUD - ADEMAS APROBADOS (TODAVIA NO SE MATRICULAN )
			//si todavia no aprueba.. no debe aparecer
			StringBuilder sqlReservados = new StringBuilder("select alu.id id_alu, pers.nro_doc, pers.ape_pat, pers.ape_mat, pers.nom, mat_ant.id id_mat_ant,mat.id id_mat, 'A' tipo, gfa.id_gpf");   
			sqlReservados.append("\n from mat_solicitud sol  ");
			sqlReservados.append("\n inner join alu_alumno alu on alu.id = sol.id_alu ");
			sqlReservados.append("\n inner join col_persona pers on alu.id_per = pers.id ");
			sqlReservados.append("\n inner join `alu_gru_fam_alumno` gfa on alu.id=gfa.id_alu ");
			sqlReservados.append("\n inner join mat_reserva res on res.id_alu = sol.id_alu"); 
			sqlReservados.append("\n inner join mat_matricula mat_ant on mat_ant.id_alu = sol.id_alu"); 
			sqlReservados.append("\n inner join col_aula au_ant on au_ant.id = mat_ant.id_au_asi"); 
			sqlReservados.append("\n inner join per_periodo  per_ant on per_ant.id = au_ant.id_per"); 
			sqlReservados.append("\n inner join per_periodo per on per.id = res.id_per "); 
			sqlReservados.append("\n left join (mat_matricula mat inner join per_periodo per1 on per1.id = mat.id_per and per1.id_anio=" + id_anio + " and per1.id_tpe=1) on mat.id_alu = sol.id_alu");
			sqlReservados.append("\n where  upper(CONCAT(pers.ape_pat,' ',pers.ape_mat, ' ', pers.nom))  LIKE '%" + alumno.toUpperCase() + "%'"); 
			sqlReservados.append("\n and sol.id_suc_des =" + id_suc+" and sol.id_anio="+id_anio);
			sqlReservados.append("\n and sol.est='A' ");
			sqlReservados.append("\n and per.id_anio=" + id_anio);
			sqlReservados.append("\n and per_ant.id_anio=" + id_anio_anterior);
			sqlReservados.append("\n and NOT EXISTS ( SELECT m.id_alu FROM mat_matricula m INNER JOIN col_aula au ON m.id_au=au.id INNER JOIN per_periodo p ON au.id_per=p.id WHERE m.id_alu = res.id_alu AND p.id_anio="+id_anio+")");
			sqlReservados.append("\n and mat_ant.id_sit=" + EnumSituacionFinal.APROBADO.getValue());//APROBADO
			sqlReservados.append("\n and mat.id is null");

			
			logger.info(sql);
			logger.info(sqlReservados);
			logger.info(sqlSolicitud);

			String sqlFinal = "select  t.id_alu, t.nro_doc, t.ape_pat, t.ape_mat, t.nom, t.id_mat_ant, t.id_mat, t.tipo, t.id_gpf from (";
			sqlFinal = sqlFinal + sql  + "\n union \n" + sqlA + "\n union \n"  + sqlSolicitud  + "\n union \n" + sqlNoMAtSoli + "\n  union \n" + sqlReservados;
			sqlFinal = sqlFinal + " ) t  order by t.ape_pat,t.ape_mat,t.nom";

			List<Map<String, Object>> list = jdbcTemplate.queryForList(sqlFinal );

			return list;

		}

		if (tipoCronogramaVigente.equals("NC")) {
			// TODO pendiente probar para alumnos nuevos matricula
			/*
			String sql = "select alu.id id_alu, alu.nro_doc, alu.ape_pat, alu.ape_mat, alu.nom, null id_mat_ant, mat1.id id_mat, 'N' tipo ";
			sql += "\n from eva_matr_vacante mat";
			sql += "\n  inner join eva_evaluacion_vac eva on eva.id = mat.id_eva";
			sql += "\n  inner join alu_alumno alu on alu.id = mat.id_alu";
			sql += "\n  inner join per_periodo per on per.id = eva.id_per";
			sql += "\n  inner join ges_servicio srv on srv.id = per.id_srv";
			sql += "\n  INNER JOIN `ges_sucursal` suc ON srv.`id_suc`=suc.id";
			sql += "\n  inner join cat_grad gra on gra.id = mat.id_gra";
			sql += "\n  left join mat_matricula mat1 on (mat1.id_alu=alu.id ) ";
			sql += "\n  left join per_periodo permat on (permat.id = mat1.id_per and per.id_anio=permat.id_anio and per.id_srv= permat.id_srv)";
			sql += "\n  where mat.res='A' and per.id_anio=" + id_anio;
			sql += "\n  and ( upper(CONCAT(alu.ape_pat,' ',alu.ape_mat, ' ', alu.nom)) LIKE '" + alumno.toUpperCase()
					+ "%'  ) ";
			if (id_suc != null)
				sql += " and suc.id=" + id_suc;
			*/
 
			/*Nuevos con reserva*/
			String sql="select alu.id id_alu, alu.nro_doc, alu.ape_pat, alu.ape_mat, alu.nom, null id_mat_ant, mat_act.id id_mat, 'N' tipo, gfa.id_gpf "; 
			sql += "\n from mat_reserva res ";
			sql += "\n inner join alu_alumno alu on alu.id = res.id_alu ";
			sql += "\n inner join `alu_gru_fam_alumno` gfa on alu.id=gfa.id_alu ";
			sql += "\n inner join per_periodo per on per.id = res.id_per and per.id_anio=" + id_anio;
			sql += "\n inner join cat_grad gra on gra.id = res.id_gra ";
			sql += "\n left  join ( mat_matricula mat_act inner join  per_periodo per_act on per_act.id= mat_act.id_per and per_act.id_anio=" + id_anio + ") on mat_act.id_alu=alu.id ";  
			sql += "\n left  join ( mat_matricula mat_ant inner join  per_periodo per_ant on per_ant.id= mat_ant.id_per and per_ant.id_anio=" + id_anio_anterior + ") on mat_ant.id_alu=alu.id and mat_ant.id_sit!=5  ";
			sql += "\n  where  upper(CONCAT(alu.ape_pat,' ',alu.ape_mat, ' ', alu.nom)) LIKE '%" + alumno.toUpperCase()+ "%'";
			sql += "\n and per.id_suc=" + id_suc;
			//sql += "\n and NOT EXISTS ( SELECT m.id_alu FROM mat_matricula m INNER JOIN col_aula au ON m.id_au=au.id INNER JOIN per_periodo p ON au.id_per=p.id WHERE m.id_alu = res.id_alu AND p.id_anio="+id_anio+")";
			sql += "\n and mat_ant.id is null";
			/**nuevos con solicitud*/
			String sqlSolicitud = "select alu.id id_alu, alu.nro_doc, alu.ape_pat, alu.ape_mat, alu.nom, mat_ant.id id_mat_ant, mat.id id_mat, 'A' tipo , gfa.id_gpf "
					+ "\n  from mat_solicitud sol " + " inner join mat_matricula m on m.id = sol.id_mat"
					+ "\n  inner join alu_alumno alu on alu.id = m.id_alu"
					+ "\n inner join `alu_gru_fam_alumno` gfa on alu.id=gfa.id_alu "
					+ "\n  inner join mat_matricula mat_ant on  mat_ant.id_alu = alu.id   "
					+ "\n  inner join per_periodo per_ant on per_ant.id = mat_ant.id_per  "
					+ "\n  inner join ges_servicio srv_ant on srv_ant.id = per_ant.id_srv  "
					+ "\n  left join ( mat_matricula mat inner join per_periodo per  on mat.id_per= per.id and per.id_anio="
					+ id_anio + " ) on mat.id_alu = alu.id "
					+ "\n  where  upper(CONCAT(alu.ape_pat,' ',alu.ape_mat, ' ', alu.nom)) LIKE '%" + alumno.toUpperCase()
					+ "%'  " + " and per_ant.id_anio=" + id_anio_anterior
					+ "\n  and (mat_ant.id_gra!=14 OR (mat_ant.id_gra=14 and mat_ant.id_sit!=1)) "
					+ "\n  and sol.id_suc_des =" + id_suc +  " and sol.id_anio=" + id_anio + " and sol.est='A' ";
			/**Reservados nuevos con solicitud**/
			StringBuilder sqlReservados = new StringBuilder("select alu.id id_alu, alu.nro_doc, alu.ape_pat, alu.ape_mat, alu.nom, null id_mat_ant,mat.id id_mat, 'A' tipo, gfa.id_gpf");   
			sqlReservados.append("\n from mat_solicitud sol  ");
			sqlReservados.append("\n inner join alu_alumno alu on alu.id = sol.id_alu ");
			sqlReservados.append("\n inner join `alu_gru_fam_alumno` gfa on alu.id=gfa.id_alu ");
			sqlReservados.append("\n inner join mat_reserva res on res.id_alu = sol.id_alu"); 
			//sqlReservados.append("\n inner join mat_matricula mat_ant on mat_ant.id_alu = sol.id_alu"); 
			//sqlReservados.append("\n inner join col_aula au_ant on au_ant.id = mat_ant.id_au_asi"); 
			//sqlReservados.append("\n inner join per_periodo  per_ant on per_ant.id = au_ant.id_per"); 
			sqlReservados.append("\n inner join per_periodo per on per.id = res.id_per "); 
			sqlReservados.append("\n left join (mat_matricula mat inner join per_periodo per1 on per1.id = mat.id_per and per1.id_anio=" + id_anio + ") on mat.id_alu = sol.id_alu");
			sqlReservados.append("\n where  upper(CONCAT(alu.ape_pat,' ',alu.ape_mat, ' ', alu.nom))  LIKE '%" + alumno.toUpperCase() + "%'"); 
			sqlReservados.append("\n and sol.id_suc_des =" + id_suc+" and sol.id_anio="+id_anio);
			sqlReservados.append("\n and sol.est='A' ");
			sqlReservados.append("\n and per.id_anio=" + id_anio);
			//sqlReservados.append("\n and per_ant.id_anio=" + id_anio_anterior);
			sqlReservados.append("\n and NOT EXISTS ( SELECT m.id_alu FROM mat_matricula m INNER JOIN col_aula au ON m.id_au=au.id INNER JOIN per_periodo p ON au.id_per=p.id WHERE m.id_alu = res.id_alu AND p.id_anio="+id_anio+")");
			//sqlReservados.append("\n and mat_ant.id_sit=" + EnumSituacionFinal.APROBADO.getValue());//APROBADO
			//sqlReservados.append("\n and mat.id is null");

			String sqlFinal = "select  t.id_alu, t.nro_doc, t.ape_pat, t.ape_mat, t.nom, t.id_mat_ant, t.id_mat, t.tipo, t.id_gpf from (\n "
					+ sql.toString();
			sqlFinal = sqlFinal + "\n  union \n " + sqlSolicitud+"\n union \n"+sqlReservados;
			sqlFinal = sqlFinal
					+ " ) t\n  WHERE t.id_alu not in (select ss.id_alu from mat_solicitud ss where ss.id_suc_or='"
					//+ " ) t\n  WHERE t.id_mat is null and t.id_alu not in (select ss.id_alu from mat_solicitud ss where ss.id_suc_or='"
					+ id_suc + "' and ss.id_anio=" + id_anio + " and ss.est='A') order by t.ape_pat, t.ape_mat, t.nom";

			// sqlFinal = sqlFinal + " ) t order by t.ape_pat,t.ape_mat,t.nom";

			logger.info(sql);

			List<Map<String, Object>> list = jdbcTemplate.queryForList(sqlFinal);
			return list;

		}

		if (tipoCronogramaVigente.equals("ASNS")) {

			// ALUMNOS ANTIGUOS
			/*
			StringBuilder sqlA = new StringBuilder(
					"select distinct alu.id id_alu, alu.nro_doc, alu.ape_pat, alu.ape_mat, alu.nom, mat_ant.id id_mat_ant, mat.id id_mat, 'A' tipo FROM `alu_alumno`");
			sqlA.append(" alu inner join mat_matricula mat_ant on  mat_ant.id_alu = alu.id  ");
			sqlA.append(" inner join per_periodo per_ant on per_ant.id = mat_ant.id_per ");
			sqlA.append(" inner join ges_servicio srv_ant on srv_ant.id = per_ant.id_srv ");
			sqlA.append(
					" left join ( mat_matricula mat inner join per_periodo per  on mat.id_per= per.id and per.id_anio=? ) on mat.id_alu = alu.id");
			sqlA.append(" where upper(CONCAT(alu.ape_pat,' ',alu.ape_mat, ' ', alu.nom)) LIKE '%" + alumno.toUpperCase()
					+ "%'  and per_ant.id_anio=? and srv_ant.id_suc= ?");
			sqlA.append(" and (mat_ant.id_gra!=14 OR (mat_ant.id_gra=14 and mat_ant.id_sit!=1))");// que
																									 
			String sql = "select alu.id id_alu, alu.nro_doc, alu.ape_pat, alu.ape_mat, alu.nom, null id_mat_ant, mat1.id id_mat, null tipo ";
			sql += " from eva_matr_vacante mat";
			sql += " inner join eva_evaluacion_vac eva on eva.id = mat.id_eva";
			sql += " inner join alu_alumno alu on alu.id = mat.id_alu";
			sql += " inner join per_periodo per on per.id = eva.id_per";
			sql += " inner join ges_servicio srv on srv.id = per.id_srv";
			sql += " INNER JOIN `ges_sucursal` suc ON srv.`id_suc`=suc.id";
			sql += " inner join cat_grad gra on gra.id = mat.id_gra";
			sql += " left join mat_matricula mat1 on (mat1.id_alu=alu.id ) ";
			sql += " left join per_periodo permat on (permat.id = mat1.id_per and per.id_anio=permat.id_anio and per.id_srv= permat.id_srv)";
			sql += " where mat.res='A' and per.id_anio=" + id_anio;
			sql += " and ( upper(CONCAT(alu.ape_pat,' ',alu.ape_mat, ' ', alu.nom)) LIKE '" + alumno.toUpperCase()
					+ "%'  ) ";
			if (id_suc != null)
				sql += " and suc.id=" + id_suc;
			// sql += " order by alu.ape_pat asc";

			String sqlSolicitud = "select alu.id id_alu, alu.nro_doc, alu.ape_pat, alu.ape_mat, alu.nom, mat_ant.id id_mat_ant, mat.id id_mat, 'A' tipo  "
					+ " from mat_solicitud sol " + " inner join mat_matricula m on m.id_alu = sol.id_mat"
					+ " inner join alu_alumno alu on alu.id = m.id_alu"
					+ " inner join mat_matricula mat_ant on  mat_ant.id_alu = alu.id   "
					+ " inner join per_periodo per_ant on per_ant.id = mat_ant.id_per  "
					+ " inner join ges_servicio srv_ant on srv_ant.id = per_ant.id_srv  "
					+ " left join ( mat_matricula mat inner join per_periodo per  on mat.id_per= per.id and per.id_anio="
					+ id_anio + " ) on mat.id_alu = alu.id "
					+ " where  upper(CONCAT(alu.ape_pat,' ',alu.ape_mat, ' ', alu.nom)) LIKE '%" + alumno.toUpperCase()
					+ "%'  " + " and per_ant.id_anio=" + id_anio_anterior
					// + " and srv_ant.id_suc= " + id_suc
					+ " and (mat_ant.id_gra!=14 OR (mat_ant.id_gra=14 and mat_ant.id_sit!=1)) "
					+ " and sol.id_suc_des =" + id_suc;

			String sqlUnion = "select distinct t.id_alu, t.nro_doc, t.ape_pat, t.ape_mat, t.nom, t.id_mat_ant, t.id_mat, t.tipo from (" + sqlA.toString();
			sqlUnion += " union all ";
			sqlUnion += sql;
			sqlUnion += " union all " + sqlSolicitud
					+ ")t WHERE t.id_alu not in (select ss.id_mat from mat_solicitud ss where ss.id_suc_or='" + id_suc
					+ "') order by t.ape_pat, t.ape_mat, t.nom";

			logger.info(sqlUnion);

			List<Map<String, Object>> list = jdbcTemplate.queryForList(sqlUnion,
					new Object[] { id_anio, id_anio_anterior, id_suc });

			*/
			
			// union ANTIGUO SIN CRONOGRAMA

			StringBuilder sql = new StringBuilder();
			//MATRICULADOS SIN SOLICITUD
			sql.append("\n  select distinct alu.id id_alu, pera.nro_doc, pera.ape_pat, pera.ape_mat, pera.nom, mat_ant.id id_mat_ant, mat.id id_mat, 'A' tipo, gfa.id_gpf"); 
			sql.append("\n FROM alu_alumno alu");
			sql.append("\n inner join `col_persona` pera on alu.id_per=pera.id ");
			sql.append("\n inner join `alu_gru_fam_alumno` gfa on alu.id=gfa.id_alu ");
			sql.append("\n inner join mat_matricula mat on  mat.id_alu = alu.id");  
			sql.append("\n inner join per_periodo per on per.id = mat.id_per and per.id_tpe=1 "); 
			sql.append("\n left join ( mat_matricula mat_ant inner join col_aula au_ant on au_ant.id= mat_ant.id_au inner join per_periodo per_ant  on au_ant.id_per= per_ant.id and per_ant.id_anio=" + id_anio_anterior + " )  on mat_ant.id_alu = alu.id");
			sql.append("\n where"); 
			sql.append("\n  upper(CONCAT(pera.ape_pat,' ',pera.ape_mat, ' ', pera.nom)) LIKE '%" + alumno.toUpperCase() + "%'");
			sql.append("\n and per.id_anio= " + id_anio+" and per.id_suc="+id_suc+" and (mat.id_sit<>'5' OR mat.id_sit is null)");
			sql.append("\n and alu.id not in"); 
			sql.append("\n (select s.id_alu from mat_solicitud s where s.est='A' and s.id_anio=" + id_anio + " and s.id_suc_des=" + id_suc + " )");

			//ANTOGIO NO MATRICULADO SIN SOLICITUD
			StringBuilder sqlA = new StringBuilder("select distinct alu.id id_alu, pera.nro_doc, pera.ape_pat, pera.ape_mat, pera.nom, mat_ant.id id_mat_ant, mat.id id_mat, 'A' tipo, gfa.id_gpf"); 
			sqlA.append("\n FROM alu_alumno alu");
			sqlA.append("\n inner join `col_persona` pera on alu.id_per=pera.id ");
			sqlA.append("\n inner join `alu_gru_fam_alumno` gfa on alu.id=gfa.id_alu ");
			sqlA.append("\n inner join mat_matricula mat_ant on  mat_ant.id_alu = alu.id");  
			sqlA.append("\n inner join col_aula au_ant on au_ant.id = mat_ant.id_au");
			sqlA.append("\n inner join per_periodo per_ant on per_ant.id = au_ant.id_per");
			sqlA.append("\n inner join ges_servicio srv_ant on srv_ant.id = per_ant.id_srv");
			sqlA.append("\n left join ( mat_matricula mat inner join per_periodo per  on mat.id_per= per.id and per.id_anio=" + id_anio + " and per.id_tpe=1 )  on mat.id_alu = alu.id");
			sqlA.append("\n where ");
			sqlA.append("\n upper(CONCAT(pera.ape_pat,' ',pera.ape_mat, ' ', pera.nom)) LIKE '%" + alumno.toUpperCase() + "%'");
			sqlA.append("\n and per_ant.id_anio=" + id_anio_anterior + " and srv_ant.id_suc= " + id_suc);
			sqlA.append("\n and mat_ant.id_sit in (1,3)");
			sqlA.append("\n and (mat_ant.id_gra!=14 OR (mat_ant.id_gra=14 and mat_ant.id_sit!=1))");
			sqlA.append("\n and mat.id is null");
			sqlA.append("\n and alu.id not in"); 
			sqlA.append("\n (select s.id_alu from mat_solicitud s where s.est='A' and s.id_anio=" + id_anio + " and s.id_suc_des="  + id_suc + " )");
			
//
			
			//matriculados CON SOLICITUD
			StringBuilder sqlSolicitud = new StringBuilder("select alu.id id_alu, pera.nro_doc, pera.ape_pat, pera.ape_mat, pera.nom, mat_ant.id id_mat_ant, mat.id id_mat, 'A' tipo, gfa.id_gpf");   
			sqlSolicitud.append("\n from mat_solicitud sol  ");
			sqlSolicitud.append("\n inner join alu_alumno alu on alu.id = sol.id_alu ");
			sqlSolicitud.append("\n inner join `col_persona` pera on alu.id_per=pera.id ");
			sqlSolicitud.append("\n inner join `alu_gru_fam_alumno` gfa on alu.id=gfa.id_alu ");
			sqlSolicitud.append("\n inner join mat_matricula mat on mat.id_alu = sol.id_alu"); 
			sqlSolicitud.append("\n inner join per_periodo per on per.id = mat.id_per and per.id_tpe=1 ");
			sqlSolicitud.append("\n left join mat_matricula mat_ant on mat_ant.id_alu = sol.id_alu");
			sqlSolicitud.append("\n left join col_aula au_ant on au_ant.id = mat_ant.id_au_asi");
			sqlSolicitud.append("\n left join per_periodo  per_ant on per_ant.id = au_ant.id_per");  
			sqlSolicitud.append("\n where  upper(CONCAT(alu.ape_pat,' ',alu.ape_mat, ' ', alu.nom))  LIKE '%" + alumno.toUpperCase() + "%'"); 
			sqlSolicitud.append("\n and sol.id_suc_des =" + id_suc+" and sol.id_anio="+id_anio);
			sqlSolicitud.append("\n and sol.est='A' ");
			sqlSolicitud.append("\n and per.id_anio=" + id_anio);
			sqlSolicitud.append("\n and per_ant.id_anio=" + id_anio_anterior);

			//ANITUGOS NO matriculados CON SOLICITUD
			StringBuilder sqlNoMAtSoli = new StringBuilder("select alu.id id_alu, pera.nro_doc, pera.ape_pat, pera.ape_mat, pera.nom, mat_ant.id id_mat_ant, mat.id id_mat, 'A' tipo, gfa.id_gpf");   
			sqlNoMAtSoli.append("\n from mat_solicitud sol  ");
			sqlNoMAtSoli.append("\n inner join alu_alumno alu on alu.id = sol.id_alu ");
			sqlNoMAtSoli.append("\n inner join `col_persona` pera on alu.id_per=pera.id ");
			sqlNoMAtSoli.append("\n inner join `alu_gru_fam_alumno` gfa on alu.id=gfa.id_alu ");
			sqlNoMAtSoli.append("\n inner join mat_matricula mat_ant on mat_ant.id_alu = sol.id_alu"); 
			sqlNoMAtSoli.append("\n inner join col_aula au_ant on au_ant.id = mat_ant.id_au_asi");
			sqlNoMAtSoli.append("\n inner join per_periodo  per_ant on per_ant.id = au_ant.id_per");  
			sqlNoMAtSoli.append("\n left join (mat_matricula mat inner join per_periodo per on per.id = mat.id_per and per.id_anio=" + id_anio + " and per.id_tpe=1) on mat.id_alu = sol.id_alu");
			sqlNoMAtSoli.append("\n where  upper(CONCAT(pera.ape_pat,' ',pera.ape_mat, ' ', pera.nom))  LIKE '%" + alumno.toUpperCase() + "%'"); 
			sqlNoMAtSoli.append("\n and sol.id_suc_des =" + id_suc+" and sol.id_anio="+id_anio);
			sqlNoMAtSoli.append("\n and sol.est='A' ");
			sqlNoMAtSoli.append("\n and per_ant.id_anio=" + id_anio_anterior);
			sqlNoMAtSoli.append("\n and mat.id is null");



			//RESERVADOS CON SOLICITUD - ADEMAS APROBADOS (TODAVIA NO SE MATRICULAN )
			//si todavia no aprueba.. no debe aparecer
			StringBuilder sqlReservados = new StringBuilder("select alu.id id_alu, pera.nro_doc, pera.ape_pat, pera.ape_mat, pera.nom, mat_ant.id id_mat_ant,mat.id id_mat, 'A' tipo, gfa.id_gpf");   
			sqlReservados.append("\n from mat_solicitud sol  ");
			sqlReservados.append("\n inner join alu_alumno alu on alu.id = sol.id_alu ");
			sqlReservados.append("\n inner join `col_persona` pera on alu.id_per=pera.id ");
			sqlReservados.append("\n inner join `alu_gru_fam_alumno` gfa on alu.id=gfa.id_alu ");
			sqlReservados.append("\n inner join mat_reserva res on res.id_alu = sol.id_alu"); 
			sqlReservados.append("\n inner join mat_matricula mat_ant on mat_ant.id_alu = sol.id_alu"); 
			sqlReservados.append("\n inner join col_aula au_ant on au_ant.id = mat_ant.id_au_asi"); 
			sqlReservados.append("\n inner join per_periodo  per_ant on per_ant.id = au_ant.id_per"); 
			sqlReservados.append("\n inner join per_periodo per on per.id = res.id_per  and per.id_anio=" + id_anio); 
			sqlReservados.append("\n left join (mat_matricula mat inner join per_periodo per1 on per1.id = mat.id_per  and per1.id_tpe=1 and per1.id_anio=" + id_anio + " AND (mat.`id_sit`<>'5' OR mat.`id_sit` IS NULL)) on mat.id_alu = sol.id_alu");
			sqlReservados.append("\n where  upper(CONCAT(pera.ape_pat,' ',pera.ape_mat, ' ', pera.nom))  LIKE '%" + alumno.toUpperCase() + "%'"); 
			sqlReservados.append("\n and sol.id_suc_des =" + id_suc+" and sol.id_anio="+id_anio);
			sqlReservados.append("\n and sol.est='A' ");
			//sqlReservados.append("\n);
			sqlReservados.append("\n and per_ant.id_anio=" + id_anio_anterior);
			sqlReservados.append("\n and NOT EXISTS ( SELECT m.id_alu FROM mat_matricula m INNER JOIN col_aula au ON m.id_au=au.id INNER JOIN per_periodo p ON au.id_per=p.id WHERE m.id_alu = res.id_alu AND p.id_anio="+id_anio+" AND (m.`id_sit`<>'5' OR m.`id_sit` IS NULL))");
			sqlReservados.append("\n and mat_ant.id_sit=" + EnumSituacionFinal.APROBADO.getValue());//APROBADO
			sqlReservados.append("\n and mat.id is null");

			
			StringBuilder sqlReservadosSS = new StringBuilder("select alu.id id_alu, pera.nro_doc, pera.ape_pat, pera.ape_mat, pera.nom, mat_ant.id id_mat_ant,mat.id id_mat, 'A' tipo, gfa.id_gpf");   
			sqlReservadosSS.append("\n from mat_reserva res ");
			sqlReservadosSS.append("\n inner join alu_alumno alu on res.id_alu=alu.id ");
			sqlReservadosSS.append("\n inner join `col_persona` pera on alu.id_per=pera.id ");
			sqlReservadosSS.append("\n inner join `alu_gru_fam_alumno` gfa on alu.id=gfa.id_alu ");
			sqlReservadosSS.append("\n inner join mat_matricula mat_ant on mat_ant.id_alu = alu.id"); 
			sqlReservadosSS.append("\n inner join col_aula au_ant on au_ant.id = mat_ant.id_au_asi"); 
			sqlReservadosSS.append("\n inner join per_periodo  per_ant on per_ant.id = au_ant.id_per"); 
			sqlReservadosSS.append("\n inner join per_periodo per on per.id = res.id_per "); 
			sqlReservadosSS.append("\n left join (mat_matricula mat inner join per_periodo per1 on per1.id = mat.id_per and per1.id_tpe=1 and per1.id_anio=" + id_anio + " AND (mat.`id_sit`<>'5' OR mat.`id_sit` IS NULL)) on mat.id_alu = alu.id");
			sqlReservadosSS.append("\n where  upper(CONCAT(pera.ape_pat,' ',pera.ape_mat, ' ', pera.nom))  LIKE '%" + alumno.toUpperCase() + "%'"); 
			sqlReservadosSS.append("\n and per.id_anio=" + id_anio);
			sqlReservadosSS.append("\n and per_ant.id_anio=" + id_anio_anterior);
			sqlReservadosSS.append("\n and mat_ant.id_sit=" + EnumSituacionFinal.APROBADO.getValue());//APROBADO
			sqlReservadosSS.append("\n and mat.id is null");
			sqlReservadosSS.append("\n and NOT EXISTS ( SELECT m.id_alu FROM mat_matricula m INNER JOIN col_aula au ON m.id_au=au.id INNER JOIN per_periodo p ON au.id_per=p.id WHERE m.id_alu = res.id_alu and p.id_tpe=1 AND p.id_anio="+id_anio+" AND (m.`id_sit`<>'5' OR m.`id_sit` IS NULL))");
			sqlReservadosSS.append("\n and alu.id not in"); 
			sqlReservadosSS.append("\n (select s.id_alu from mat_solicitud s where s.est='A' and s.id_anio=" + id_anio + " and s.id_suc_or="  + id_suc + " )");
			
			
			logger.info(sql);
			logger.info(sqlReservados);
			logger.info(sqlSolicitud);

			/*
			String sqlFinal = "select  t.id_alu, t.nro_doc, t.ape_pat, t.ape_mat, t.nom, t.id_mat_ant, t.id_mat, t.tipo from (";
			sqlFinal = sqlFinal + sql  + "\n union \n" + sqlA + "\n union \n"  + sqlSolicitud  + "\n union \n" + sqlNoMAtSoli + "\n  union \n" + sqlReservados;
			sqlFinal = sqlFinal + " ) t  order by t.ape_pat,t.ape_mat,t.nom";
*/
			
			
			//NUEVO SIN CRONOGRAMA
			String sqlNS="select alu.id id_alu, pera.nro_doc, pera.ape_pat, pera.ape_mat, pera.nom, null id_mat_ant, mat_act.id id_mat, 'N' tipo, gfa.id_gpf "; 
			sqlNS += "\n from mat_reserva res ";
			sqlNS += "\n inner join alu_alumno alu on alu.id = res.id_alu ";
			sqlNS += "\n inner join `col_persona` pera on alu.id_per=pera.id ";
			sqlNS += "\n inner join `alu_gru_fam_alumno` gfa on alu.id=gfa.id_alu ";
			sqlNS += "\n inner join per_periodo per on per.id = res.id_per ";
			sqlNS += "\n inner join cat_grad gra on gra.id = res.id_gra ";
			sqlNS += "\n left  join ( mat_matricula mat_act inner join  per_periodo per_act on per_act.id= mat_act.id_per and per_act.id_anio=" + id_anio + " and per_act.id_tpe=1) on mat_act.id_alu=alu.id ";  
			sqlNS += "\n left  join ( mat_matricula mat_ant inner join  per_periodo per_ant on per_ant.id= mat_ant.id_per and per_ant.id_anio=" + id_anio_anterior + ") on mat_ant.id_alu=alu.id and mat_ant.id_sit!=5  ";
			sqlNS += "\n  where  upper(CONCAT(pera.ape_pat,' ',pera.ape_mat, ' ', pera.nom)) LIKE '%" + alumno.toUpperCase()+ "%'";
			sqlNS += "\n and per.id_suc=" + id_suc;
			sqlNS += "\n and per.id_anio=" + id_anio;
			sqlNS += "\n and NOT EXISTS ( SELECT m.id_alu FROM mat_matricula m INNER JOIN col_aula au ON m.id_au_asi=au.id INNER JOIN per_periodo p ON au.id_per=p.id WHERE m.id_alu = res.id_alu AND p.id_anio="+id_anio+" and p.id_tpe=1)";
			sqlNS += "\n and (mat_ant.id is null OR mat_ant.id_sit in (4,5))";
			//nuevos matriculados CON SOLICITUD
			StringBuilder sqlNuevosMatSol = new StringBuilder("select alu.id id_alu, pera.nro_doc, pera.ape_pat, pera.ape_mat, pera.nom, null id_mat_ant, mat.id id_mat, 'N' tipo, gfa.id_gpf");   
			sqlNuevosMatSol.append("\n from mat_solicitud sol  ");
			sqlNuevosMatSol.append("\n inner join alu_alumno alu on alu.id = sol.id_alu ");
			sqlNuevosMatSol.append("\n inner join `col_persona` pera on alu.id_per=pera.id ");
			sqlNuevosMatSol.append("\n inner join `alu_gru_fam_alumno` gfa on alu.id=gfa.id_alu ");
			sqlNuevosMatSol.append("\n inner join mat_matricula mat on mat.id_alu = sol.id_alu"); 
			sqlNuevosMatSol.append("\n inner join per_periodo per on per.id = mat.id_per and per.id_tpe=1 ");
			//sqlSolicitud.append("\n left join mat_matricula mat_ant on mat_ant.id_alu = sol.id_alu");
			//sqlSolicitud.append("\n left join col_aula au_ant on au_ant.id = mat_ant.id_au_asi");
			//sqlSolicitud.append("\n left join per_periodo  per_ant on per_ant.id = au_ant.id_per");  
			sqlNuevosMatSol.append("\n where  upper(CONCAT(pera.ape_pat,' ',pera.ape_mat, ' ', pera.nom))  LIKE '%" + alumno.toUpperCase() + "%'"); 
			sqlNuevosMatSol.append("\n and sol.id_suc_des =" + id_suc+" and sol.id_anio="+id_anio);
			sqlNuevosMatSol.append("\n and sol.est='A' ");
			sqlNuevosMatSol.append("\n and per.id_anio=" + id_anio);
			//sqlNuevosMatSol.append("\n and NOT EXISTS ( SELECT mn.id_alu FROM mat_matricula mn INNER JOIN col_aula au ON mn.id_au_asi=au.id INNER JOIN per_periodo p ON au.id_per=p.id WHERE mn.id_alu = mat.id_alu AND p.id_anio="+id_anio+" and p.id_suc="+id_suc+")");
			//sqlSolicitud.append("\n and per_ant.id_anio=" + id_anio_anterior);
			/**nuevos con solicitud*/
			String sqlSolicitudNS = "select alu.id id_alu, pera.nro_doc, pera.ape_pat, pera.ape_mat, pera.nom, mat_ant.id id_mat_ant, mat.id id_mat, 'A' tipo, gfa.id_gpf "
					+ "\n  from mat_solicitud sol " + " inner join mat_matricula m on m.id = sol.id_mat"
					+ "\n  inner join alu_alumno alu on alu.id = m.id_alu"
				    + "\n inner join `col_persona` pera on alu.id_per=pera.id "
					+ "\n  inner join `alu_gru_fam_alumno` gfa on alu.id=gfa.id_alu "
					+ "\n  inner join mat_matricula mat_ant on  mat_ant.id_alu = alu.id   "
					+ "\n  inner join per_periodo per_ant on per_ant.id = mat_ant.id_per  "
					+ "\n  inner join ges_servicio srv_ant on srv_ant.id = per_ant.id_srv  "
					+ "\n  left join ( mat_matricula mat inner join per_periodo per  on mat.id_per= per.id and per.id_anio="
					+ id_anio + " and per.id_tpe=1 ) on mat.id_alu = alu.id "
					+ "\n  where  upper(CONCAT(pera.ape_pat,' ',pera.ape_mat, ' ', pera.nom)) LIKE '%" + alumno.toUpperCase()
					+ "%'  " + " and per_ant.id_anio=" + id_anio_anterior
					+ "\n  and (mat_ant.id_gra!=14 OR (mat_ant.id_gra=14 and mat_ant.id_sit!=1)) "
					+ "\n  and sol.id_suc_des =" + id_suc +  " and sol.id_anio=" + id_anio + " and sol.est='A' ";
			
			/**Reservados nuevos con solicitud**/
			StringBuilder sqlReservadosSolicitud = new StringBuilder("select alu.id id_alu, pera.nro_doc, pera.ape_pat, pera.ape_mat, pera.nom, null id_mat_ant,mat.id id_mat, 'A' tipo, gfa.id_gpf");   
			sqlReservadosSolicitud.append("\n from mat_solicitud sol  ");
			sqlReservadosSolicitud.append("\n inner join alu_alumno alu on alu.id = sol.id_alu ");
			sqlReservadosSolicitud.append( "\n inner join `col_persona` pera on alu.id_per=pera.id ");
			sqlReservadosSolicitud.append("\n inner join `alu_gru_fam_alumno` gfa on alu.id=gfa.id_alu ");
			sqlReservadosSolicitud.append("\n inner join mat_reserva res on res.id_alu = sol.id_alu"); 
			//sqlReservados.append("\n inner join mat_matricula mat_ant on mat_ant.id_alu = sol.id_alu"); 
			//sqlReservados.append("\n inner join col_aula au_ant on au_ant.id = mat_ant.id_au_asi"); 
			//sqlReservados.append("\n inner join per_periodo  per_ant on per_ant.id = au_ant.id_per"); 
			sqlReservadosSolicitud.append("\n inner join per_periodo per on per.id = res.id_per AND per.id_anio="+id_anio); 
			sqlReservadosSolicitud.append("\n left join (mat_matricula mat inner join per_periodo per1 on per1.id = mat.id_per and per1.id_anio=" + id_anio + " and per1.id_tpe=1) on mat.id_alu = sol.id_alu");
			sqlReservadosSolicitud.append("\n where  upper(CONCAT(pera.ape_pat,' ',pera.ape_mat, ' ', pera.nom))  LIKE '%" + alumno.toUpperCase() + "%'"); 
			sqlReservadosSolicitud.append("\n and sol.id_suc_des =" + id_suc+" and sol.id_anio="+id_anio);
			sqlReservadosSolicitud.append("\n and sol.est='A' ");
			//sqlReservadosSolicitud.append("\n and per.id_anio=" + id_anio);
			//sqlReservados.append("\n and per_ant.id_anio=" + id_anio_anterior);
			sqlReservadosSolicitud.append("\n and NOT EXISTS ( SELECT m.id_alu FROM mat_matricula m INNER JOIN col_aula au ON m.id_au=au.id INNER JOIN per_periodo p ON au.id_per=p.id WHERE m.id_alu = res.id_alu AND p.id_anio="+id_anio+" and p.id_tpe=1)");
			//sqlReservados.append("\n and mat_ant.id_sit=" + EnumSituacionFinal.APROBADO.getValue());//APROBADO

			String sqlFinalNS = "select  t.id_alu, t.nro_doc, t.ape_pat, t.ape_mat, t.nom, t.id_mat_ant, t.id_mat, t.tipo, t.id_gpf from (\n "
					+ sqlNS.toString();
			sqlFinalNS = sqlFinalNS + "\n  union \n " +sqlNuevosMatSol+"\n union \n" +sqlSolicitudNS+ "\n union \n"+sqlReservadosSolicitud;
			sqlFinalNS = sqlFinalNS	+ " ) t\n  WHERE t.id_alu not in (select ss.id_alu from mat_solicitud ss where ss.id_suc_or='"	+ id_suc + "' and ss.id_anio=" + id_anio + " and ss.est='A') ";
			
			String sqlFinal = "select DISTINCT  t.id_alu, t.nro_doc, t.ape_pat, t.ape_mat, t.nom, t.id_mat_ant, t.id_mat, t.tipo, t.id_gpf from (";
			sqlFinal = sqlFinal + sql  + "\n union \n" + sqlA + "\n union \n"  + sqlSolicitud  + "\n union \n" + sqlNoMAtSoli + "\n  union \n" + sqlReservados + "\n  union \n" + sqlReservadosSS;//AS
			sqlFinal = sqlFinal + "\n union \n" + sqlFinalNS;//NS
			sqlFinal = sqlFinal + " ) t  order by t.ape_pat,t.ape_mat,t.nom";

			List<Map<String, Object>> list = jdbcTemplate.queryForList(sqlFinal);
			
			System.out.print(sqlFinal);

			return list;

		}

		return new ArrayList<Map<String, Object>>();
	}

	/**
	 * Datos de la vacante a porit del id del alumno y a�o
	 * 
	 * @param alumno
	 * @param id_anio
	 * @param id_suc
	 * @return
	 */
	public List<Map<String, Object>> listAlumnoMatricula(Integer id_alu, Integer id_anio) {
		String sql = "select alu.id id_alu, alu.nro_doc, alu.ape_pat, alu.ape_mat, alu.nom, mat1.id id_mat, srv.nom servicio, gra.id id_gra";
		sql += " from eva_matr_vacante mat";
		sql += " inner join eva_evaluacion_vac eva on eva.id = mat.id_eva";
		sql += " inner join alu_alumno alu on alu.id = mat.id_alu";
		sql += " inner join per_periodo per on per.id = eva.id_per";
		sql += " inner join ges_servicio srv on srv.id = per.id_srv";
		sql += " INNER JOIN `ges_sucursal` suc ON srv.`id_suc`=suc.id";
		sql += " inner join cat_grad gra on gra.id = mat.id_gra";
		sql += " left join mat_matricula mat1 on (mat1.id_alu=alu.id ) ";
		sql += " left join per_periodo permat on (permat.id = mat1.id_per and per.id_anio=permat.id_anio and per.id_srv= permat.id_srv)";
		sql += " order by alu.ape_pat asc";
		logger.info(sql);

		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		return list;
	}

	/**
	 * Valida si el padre ya tiene contrato
	 * 
	 * @TODO deberia tomar en cuenta el a�o
	 * @param id_fam
	 * @param id_per
	 * @return
	 */
	public Map<String, Object> getContrato(Integer id_fam, Integer id_anio) {

		String sql = "select p.id, p.id_anio,s.id_suc, mat.num_cont, mat.num_adenda  from mat_matricula mat "
				+ "inner join  per_periodo p on p.id = mat.id_per " + "inner join ges_servicio s on s.id= p.id_srv "
				+ "where id_fam=" + id_fam + " and mat.id_per = p.id and p.id_anio= " + id_anio+" and p.id_tpe=1";// and
																								// p.id="
																								// +
																								// id_per;
		logger.info(sql);

		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		if (list.size() == 0)
			return null;
		else
			return list.get(0);
	}
	
	public Map<String, Object> getContratoxGruFam(Integer id_gpf, Integer id_anio) {

		String sql = "SELECT mat.*\n" + 
				"FROM mat_matricula mat\n" + 
				"INNER JOIN alu_alumno alu ON mat.id_alu=alu.id \n" + 
				"INNER JOIN alu_gru_fam_alumno agfa ON agfa.id_alu=alu.id \n" + 
				"INNER JOIN col_aula au ON mat.`id_au_asi`=au.`id`\n" + 
				"INNER JOIN per_periodo per ON au.`id_per`=per.id\n" + 
				"WHERE agfa.id_gpf="+id_gpf+"  AND per.id_anio="+id_anio+" AND per.id_tpe=1";// and
																								// p.id="
																								// +
																								// id_per;
		logger.info(sql);

		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		if (list.size() == 0)
			return null;
		else
			return list.get(0);
	}

	// @TODO deberia tomar en cuenta el a�o
	public Map<String, Object> getContrato(String contrato, Integer id_per, Integer id_fam) {

		String sql = "select p.id, p.id_anio,s.id_suc, mat.num_cont  from mat_matricula mat "
				+ "inner join  per_periodo p on p.id = mat.id_per " + "inner join ges_servicio s on s.id= p.id_srv "
				+ "where mat.num_cont='" + contrato + "' and mat.id_fam!=" + id_fam;// "
																					// and
																					// p.id="
																					// +
																					// id_per;
		logger.info(sql);

		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		if (list.size() == 0)
			return null;
		else
			return list.get(0);
	}

	public Map<String, Object> getTotalContratosxLocal(Integer id_anio, Integer id_suc) {

	//	String sql = "select suc.cod, count(distinct num_cont) as total from mat_matricula mat "
//		String sql = "select suc.cod,  max(substr(num_cont,10,7)*1) as total from mat_matricula mat "
		String sql = "SELECT suc.cod,  IF(MAX(SUBSTR(num_cont,10,7)*1) IS NULL,0,MAX(SUBSTR(num_cont,10,7)*1)) AS total "
				+ " FROM mat_matricula mat  INNER JOIN  per_periodo p ON p.id = mat.id_per"
			//	+ " inner join ges_servicio s on s.id= p.id_srv "
				+ " inner join ges_sucursal suc on suc.id= p.id_suc "
				+ " where suc.id= " + id_suc + " and mat.id_per = p.id and p.id_anio= " + id_anio 
				+ " and mat.id_suc_con = " + id_suc;// and  p.id=  +  id_per;CONTRATO SE MANTIENE, NO TIENE CASO ESTA CONDICION
		logger.info(sql);

		Map<String, Object> map = jdbcTemplate.queryForMap(sql);
		return map;
	}
	
	public Map<String, Object> getTotalContratos(Integer id_anio) {

		//	String sql = "select suc.cod, count(distinct num_cont) as total from mat_matricula mat "
//			String sql = "select suc.cod,  max(substr(num_cont,10,7)*1) as total from mat_matricula mat "
			//String sql = "SELEC IF(MAX(SUBSTR(num_cont,10,7)*1) IS NULL,0,MAX(SUBSTR(num_cont,10,7)*1)) AS total "
			String sql = "SELECT IF(MAX(SUBSTR(num_cont,6,10)*1)  IS NULL,0,MAX(SUBSTR(num_cont,6,10)*1) ) AS total "
					+ " FROM mat_matricula mat  INNER JOIN  per_periodo p ON p.id = mat.id_per"
				//	+ " inner join ges_servicio s on s.id= p.id_srv "
				//	+ " inner join ges_sucursal suc on suc.id= p.id_suc "
					+ " where mat.id_per = p.id and p.id_anio= " + id_anio;
			logger.info(sql);

			Map<String, Object> map = jdbcTemplate.queryForMap(sql);
			return map;
		}

	public List<Map<String, Object>> lista_Secc(Integer id_grad, Integer id_anio) {

		String sql = "SELECT au.* FROM `col_aula` au, `per_periodo` per, ges_servicio ser, `ges_sucursal` suc, `col_anio` a"
				+ " WHERE au.`id_per`=per.`id` AND per.`id_srv`=ser.id AND ser.`id_suc`=suc.`id` AND per.`id_anio`=a.`id`"
				+ " AND au.`id_grad`=" + id_grad + " AND a.id='" + id_anio + "' ORDER BY au.`secc` ASC";
		List<Map<String, Object>> listSecciones = jdbcTemplate.queryForList(sql);
		logger.info(sql);
		return listSecciones;
	}

	/**
	 * Reporte de matriculados por a�o academico
	 * 
	 * @param id_au
	 * @return
	 */
	public List<Map<String, Object>> Reporte_Mat(Integer id_au) {
//nota se corrigio id_au_asi
		String sql = "SELECT DISTINCT m.id, fac.nro_rec, a.cod, pa.nro_doc 'DNI', CONCAT(pa.`ape_pat`,' ', pa.ape_mat,' ', pa.nom) 'Alumno', m.id_sit, s.nom, an.flag_migra, sug.id_au_nue,"
				+ " CASE  WHEN pa.id_gen='1' THEN 'M'  WHEN pa.id_gen='0' THEN 'F' END AS 'Genero', col.cod_mod,"
				+ " (SELECT pf.nro_doc FROM alu_familiar fam,alu_gru_fam_familiar fam_gpf,alu_gru_fam_alumno alu_gpf, col_persona pf  "
				+ "  WHERE pf.id_gen='1' AND alu_gpf.id_gpf=fam_gpf.id_gpf AND a.id=alu_gpf.id_alu AND fam_gpf.id_fam=fam.id AND fam.id_per=pf.id LIMIT 1) AS 'DNIPadre',"
				+ " (SELECT pf.nro_doc FROM alu_familiar fam,alu_gru_fam_familiar fam_gpf,alu_gru_fam_alumno alu_gpf, col_persona pf WHERE pf.id_gen='0'"
				+ " AND alu_gpf.id_gpf=fam_gpf.id_gpf AND a.id=alu_gpf.id_alu AND fam_gpf.id_fam=fam.id AND fam.id_per=pf.id LIMIT 1) AS 'DNIMadre'"
				+ " FROM mat_matricula m" 
				+ " inner join alu_alumno a on m.id_alu=a.id"
				+ " inner join col_persona pa on a.id_per=pa.id"
				+ " inner join col_aula au on m.id_au_asi=au.id" 
				+ " left join cat_col_situacion s on m.id_sit=s.id"
				+ " left join mat_seccion_sugerida sug on sug.id_mat=m.id"
				+ " LEFT JOIN fac_academico_pago fac ON fac.id_mat=m.id AND tip='mat' AND (fac.nro_cuota=1 AND fac.mens=0) OR (fac.nro_cuota=0 AND fac.mens=1) "
				+ " LEFT JOIN eva_matr_vacante mat_vac ON mat_vac.id_alu=a.id AND mat_vac.res='A'"
				+ " LEFT JOIN col_colegio col ON mat_vac.id_col=col.id" + " inner join per_periodo p on au.id_per=p.id"
				+ " inner join col_anio an on p.id_anio=an.id" 
				+ " WHERE  m.id_au_asi=" + id_au + " AND (m.id_sit IS NULL OR m.id_sit<>'5') "
				+ " order by pa.ape_pat,pa.ape_mat asc,pa.nom asc";

		List<Map<String, Object>> Reporte = jdbcTemplate.queryForList(sql);
		logger.info(sql);
		return Reporte;
	}

	/*
	 * Reporte de matriculados por a�o academico
	 * es una version mejorada de la consulta anterior
	 * @param id_au
	 * @return
	 */
	public List<Row> Reporte_Mat(Integer id_anio, Integer id_cic, Integer id_gra,Integer id_au, Integer id_gir, Integer id_niv, String rep_com, String tras) {
		int id_anio_ant=id_anio-1;
		StringBuilder sql = null;
		if(rep_com.equals("0")) {
			sql = new StringBuilder("SELECT DISTINCT a.id_classRoom,m.fecha,m.tipo, m.id, fac.nro_rec, a.cod, s.cod situacion, per.nro_doc 'nro_doc', CONCAT(per.`ape_pat`,' ', per.ape_mat,' ', per.nom) 'Alumno', DATE_FORMAT(per.fec_nac, \"%d \") dia, DATE_FORMAT(per.fec_nac, \"%M\") mes, ");
			sql.append("\n CONCAT(per2.`ape_pat`,' ', per2.ape_mat,' ', per2.nom) 'Persona_Responsable', ");
			sql.append("\n per2.cel 'Celular_Responsable', ");
			sql.append("\n per2.corr 'Correo_Responsable', ");
			sql.append("\n dist.nom 'distrito', ");
			sql.append("\n pro.nom 'provincia', ");
			sql.append("\n usr.login , ");
			sql.append("\n IF(t.id_par='','MISMO ALUMNO',t.par) parentesco,");
			sql.append("\n m.id_sit, s.nom, an.flag_migra, sug.id_au_nue, a.usuario, a.pass_google, a.id id_alu, ");
			sql.append("\n CASE  WHEN per.id_gen='1' THEN 'M'  WHEN per.id_gen='0' THEN 'F' END AS 'Genero',");
			sql.append("\n CASE  WHEN fac.canc='1' THEN 'Cancelado'  WHEN fac.canc='0' THEN 'Pendiente' END AS 'estado',");
			sql.append("\n fac.fec_venc,");
			sql.append("\n (SELECT col.cod_mod FROM `eva_matr_vacante` mat, `eva_evaluacion_vac` eva, per_periodo per_eva, col_colegio col  WHERE mat.id_eva=eva.id AND mat.id_alu=a.id"); 
			sql.append("\n AND eva.id_per=per_eva.id AND mat.id_col=col.id AND per_eva.id_anio="+id_anio+" AND mat.res='A' LIMIT 1) cod_mod,");
			sql.append("\n (SELECT per.nro_doc FROM alu_familiar fam,alu_gru_fam_familiar fam_gpf,alu_gru_fam_alumno alu_gpf, col_persona per ");
			sql.append("\n WHERE per.id_gen='1' AND alu_gpf.id_gpf=fam_gpf.id_gpf AND a.id=alu_gpf.id_alu AND fam_gpf.id_fam=fam.id AND fam.id_per=per.id and fam.id_par=2 LIMIT 1) AS 'DNIPadre',");
			sql.append("\n (SELECT per.nro_doc FROM alu_familiar fam,alu_gru_fam_familiar fam_gpf,alu_gru_fam_alumno alu_gpf, col_persona per WHERE per.id_gen='0'");
			sql.append("\n  AND alu_gpf.id_gpf=fam_gpf.id_gpf AND a.id=alu_gpf.id_alu AND fam_gpf.id_fam=fam.id AND fam.id_per=per.id AND fam.id_par=1 LIMIT 1) AS 'DNIMadre',");
			sql.append("\n  g.nom grado, au.secc, au.id id_au"); // , CONCAT( g_ant.nom,' ', au_ant.secc) aula_anterior , au_org.secc secc_org
			sql.append("\n  FROM mat_matricula m"); 
			sql.append("\n inner join alu_alumno a on m.id_alu=a.id");
			sql.append("\n inner join col_persona per on per.id=a.id_per");
			sql.append("\n inner join col_persona per2 on per2.id=m.id_per_res");
			sql.append("\n inner join alu_gru_fam_alumno agfa on agfa.id_alu=a.id");
			sql.append("\n inner join per_periodo p on m.id_per=p.id");
			sql.append("\n inner join ges_servicio srv on srv.id=p.id_srv");
			sql.append("\n inner join col_anio an on p.id_anio=an.id"); 
			//sql.append("\n inner join col_aula au_org on m.id_au=au_org.id");
			sql.append("\n inner join cat_grad g on g.id=m.id_gra");
			sql.append("\n INNER JOIN alu_gru_fam agf ON agfa.id_gpf=agf.id"); 
			
			sql.append("\n left join col_aula au on m.id_au_asi=au.id ");
			sql.append("\n left JOIN fac_academico_pago fac ON fac.id_mat=m.id AND tip='mat' AND fac.nro_cuota IN (0,1)");
			sql.append("\n left JOIN col_ciclo cic ON au.id_cic=cic.id ");
			sql.append("\n LEFT JOIN ( SELECT fam2.id_per, agff.id_gpf , par.id id_par, par.par FROM alu_familiar fam2 INNER JOIN cat_parentesco par ON par.id=fam2.id_par");
			sql.append("\n INNER JOIN alu_gru_fam_familiar agff ON fam2.id=agff.id_fam ) t ON t.id_per=per2.id AND t.id_gpf=agf.id");
			//sql.append("\n LEFT JOIN alu_familiar fam2 ON per2.id=fam2.id_per"); 
			//sql.append("\n LEFT JOIN alu_gru_fam_familiar agff ON  agff.id_gpf=agf.id AND fam2.id=agff.id_fam"); 
			//sql.append("\n LEFT JOIN cat_parentesco par ON fam2.id_par=par.id");
			//sql.append("\n left join alu_familiar fam2 on per2.id=fam2.id_per");
			//sql.append("\n left join alu_gru_fam_familiar agff on fam2.id=agff.id_fam and agff.id_gpf=agfa.id_gpf");
			//sql.append("\n left join alu_gru_fam agf on agff.id_gpf=agf.id and agfa.id_gpf=agf.id");
			sql.append("\n left join cat_distrito dist on agf.id_dist=dist.id ");
			sql.append("\n left join cat_provincia pro on dist.id_pro=pro.id ");
			//sql.append("\n left join mat_matricula  mat_ant on mat_ant.id_alu=a.id");
			//sql.append("\n inner join per_periodo  p_ant on mat_ant.id_per=p_ant.id and p_ant.id_anio="+id_anio_ant+" and p_ant.id_tpe=1");
			//sql.append("\n inner join col_aula  au_ant on mat_ant.id_au_asi=au_ant.id and au_ant.id_per=p_ant.id");
			//sql.append("\n inner join cat_grad  g_ant on au_ant.id_grad=g_ant.id");
		//	sql.append("\n left join alu_familiar fami on per2.id=fami.id_per");
			//sql.append("\n left join cat_parentesco par on fam2.id_par=par.id")
			sql.append("\n left join cat_col_situacion s on m.id_sit=s.id");
			sql.append("\n left join mat_seccion_sugerida sug on sug.id_mat=m.id");
			sql.append("\n LEFT JOIN seg_usuario usr ON m.usr_ins=usr.id");
			sql.append("\n WHERE  p.id_anio=" + id_anio+" and srv.id_gir="+id_gir+" and p.id_niv="+id_niv+" and m.id_cic="+id_cic );
			if (id_gra!=null)
				sql.append("\n and m.id_gra=" + id_gra);
			if (id_au!=null)
				sql.append("\n and m.id_au_asi=" + id_au);
			if (tras.equals("N"))
				sql.append("\n and (m.id_sit not in(5,4) OR m.id_sit is null ) ");
			else if(tras.equals("T"))
				sql.append("\n and m.id_sit='5'");
			else if(tras.equals("R"))
				sql.append("\n and m.id_sit='4'");
			
			sql.append("\n order by g.id, au.secc,per.ape_pat , per.ape_mat , per.nom ");
		} else {
			sql = new StringBuilder("SELECT DISTINCT a.id_classRoom,m.fecha,m.tipo, m.id, fac.nro_rec, a.cod, s.cod situacion, per.nro_doc 'nro_doc', CONCAT(per.`ape_pat`,' ', per.ape_mat,', ', per.nom) 'Alumno', DATE_FORMAT(per.fec_nac, \"%d \") dia, DATE_FORMAT(per.fec_nac, \"%M\") mes, ");
			sql.append("\n CONCAT(per2.`ape_pat`,' ', per2.ape_mat,' ', per2.nom) 'Persona_Responsable', ");
			sql.append("\n per2.cel 'Celular_Responsable', ");
			sql.append("\n per2.corr 'Correo_Responsable', ");
			sql.append("\n dist.nom 'distrito', ");
			sql.append("\n pro.nom 'provincia', ");
			sql.append("\n usr.login , ");
			sql.append("\n IF(t.id_par='','MISMO ALUMNO',t.par) parentesco,");
			sql.append("\n m.id_sit, s.nom, an.flag_migra, sug.id_au_nue, a.usuario, a.pass_google, a.id id_alu, ");
			sql.append("\n CASE  WHEN per.id_gen='1' THEN 'M'  WHEN per.id_gen='0' THEN 'F' END AS 'Genero',");
			sql.append("\n CASE  WHEN fac.canc='1' THEN 'Cancelado'  WHEN fac.canc='0' THEN 'Pendiente' END AS 'estado',");
			sql.append("\n fac.fec_venc,");
			sql.append("\n (SELECT col.cod_mod FROM `eva_matr_vacante` mat, `eva_evaluacion_vac` eva, per_periodo per_eva, col_colegio col  WHERE mat.id_eva=eva.id AND mat.id_alu=a.id"); 
			sql.append("\n AND eva.id_per=per_eva.id AND mat.id_col=col.id AND per_eva.id_anio="+id_anio+" AND mat.res='A' LIMIT 1) cod_mod,");
			sql.append("\n (SELECT per.nro_doc FROM alu_familiar fam,alu_gru_fam_familiar fam_gpf,alu_gru_fam_alumno alu_gpf, col_persona per ");
			sql.append("\n WHERE per.id_gen='1' AND alu_gpf.id_gpf=fam_gpf.id_gpf AND a.id=alu_gpf.id_alu AND fam_gpf.id_fam=fam.id AND fam.id_per=per.id and fam.id_par=2 LIMIT 1) AS 'DNIPadre',");
			sql.append("\n (SELECT per.nro_doc FROM alu_familiar fam,alu_gru_fam_familiar fam_gpf,alu_gru_fam_alumno alu_gpf, col_persona per WHERE per.id_gen='0'");
			sql.append("\n  AND alu_gpf.id_gpf=fam_gpf.id_gpf AND a.id=alu_gpf.id_alu AND fam_gpf.id_fam=fam.id AND fam.id_per=per.id AND fam.id_par=1 LIMIT 1) AS 'DNIMadre',");
			sql.append("\n  g.nom grado, au.secc, au.id id_au"); // , CONCAT( g_ant.nom,' ', au_ant.secc) aula_anterior , au_org.secc secc_org
			sql.append("\n  FROM mat_matricula m"); 
			sql.append("\n inner join alu_alumno a on m.id_alu=a.id");
			sql.append("\n inner join col_persona per on per.id=a.id_per");
			sql.append("\n inner join col_persona per2 on per2.id=m.id_per_res");
			sql.append("\n inner join alu_gru_fam_alumno agfa on agfa.id_alu=a.id");
			sql.append("\n inner join cat_grad g on g.id=m.id_gra");
			sql.append("\n inner join per_periodo p on m.id_per=p.id");
			sql.append("\n inner join ges_servicio srv on srv.id=p.id_srv");
			sql.append("\n inner join col_anio an on p.id_anio=an.id"); 
			//sql.append("\n inner join cat_grad g on g.id=au.id_grad");
			sql.append("\n INNER JOIN alu_gru_fam agf ON agfa.id_gpf=agf.id"); 
			sql.append("\n left join col_aula au on m.id_au_asi=au.id " );	
			sql.append("\n left JOIN col_ciclo cic ON au.id_cic=cic.id ");
			//sql.append("\n inner join col_aula au_org on m.id_au=au_org.id");
			sql.append("\n LEFT JOIN ( SELECT fam2.id_per, agff.id_gpf , par.id id_par, par.par FROM alu_familiar fam2 INNER JOIN cat_parentesco par ON par.id=fam2.id_par");
			sql.append("\n INNER JOIN alu_gru_fam_familiar agff ON fam2.id=agff.id_fam ) t ON t.id_per=per2.id AND t.id_gpf=agf.id");
			//sql.append("\n LEFT JOIN alu_familiar fam2 ON per2.id=fam2.id_per"); 
			//sql.append("\n LEFT JOIN alu_gru_fam_familiar agff ON  agff.id_gpf=agf.id AND fam2.id=agff.id_fam"); 
			//sql.append("\n LEFT JOIN cat_parentesco par ON fam2.id_par=par.id");
			//sql.append("\n left join alu_familiar fam2 on per2.id=fam2.id_per");
			//sql.append("\n left join alu_gru_fam_familiar agff on fam2.id=agff.id_fam");
			//sql.append("\n left join alu_gru_fam agf on agff.id_gpf=agf.id");
			sql.append("\n left join cat_distrito dist on agf.id_dist=dist.id ");
			sql.append("\n left join cat_provincia pro on dist.id_pro=pro.id ");
			//sql.append("\n left join mat_matricula  mat_ant on mat_ant.id_alu=a.id");
			//sql.append("\n inner join per_periodo  p_ant on mat_ant.id_per=p_ant.id and p_ant.id_anio="+id_anio_ant+" and p_ant.id_tpe=1");
			//sql.append("\n inner join col_aula  au_ant on mat_ant.id_au_asi=au_ant.id and au_ant.id_per=p_ant.id");
			//sql.append("\n inner join cat_grad  g_ant on au_ant.id_grad=g_ant.id");
		//	sql.append("\n left join alu_familiar fami on per2.id=fami.id_per");
			//sql.append("\n left join cat_parentesco par on fam2.id_par=par.id");
			
			sql.append("\n left join cat_col_situacion s on m.id_sit=s.id");
			sql.append("\n left join mat_seccion_sugerida sug on sug.id_mat=m.id");
			sql.append("\n inner JOIN fac_academico_pago fac ON fac.id_mat=m.id AND tip='mat' AND fac.nro_cuota IN (0,1) ");
			sql.append("\n LEFT JOIN seg_usuario usr ON m.usr_ins=usr.id");
			sql.append("\n WHERE  p.id_anio=" + id_anio+" and srv.id_gir="+id_gir+" and p.id_niv="+id_niv+ " and m.id_cic="+id_cic );
			if (id_gra!=null)
				sql.append("\n and m.id_gra=" + id_gra);
			if (id_au!=null)
				sql.append("\n and m.id_au_asi=" + id_au);
			if(tras!=null) {
				if (tras.equals("N"))
					sql.append("\n and (m.id_sit not in(5,4) OR m.id_sit is null ) ");
				else if(tras.equals("T"))
					sql.append("\n and m.id_sit='5'");
				else if(tras.equals("R"))
					sql.append("\n and m.id_sit='4'");
			}
			
			
			sql.append("\n order by g.id, au.secc,per.ape_pat , per.ape_pat , per.ape_mat , per.nom ");
		}
		


		return sqlUtil.query(sql.toString()); 
		
	}
	
	/**
	 * Reporte de Ratificacion
	 * @param id_anio
	 * @param id_cic
	 * @param id_gra
	 * @param id_au
	 * @param id_gir
	 * @param id_niv
	 * @param rat
	 * @param tras
	 * @return
	 */
	public List<Row> Reporte_Ratificacion(Integer id_anio, Integer id_cic, Integer id_gra,Integer id_au, Integer id_gir, Integer id_niv, String rat, String fec_ini, String fec_fin) {
		StringBuilder sql = null;
		try {
			
			sql = new StringBuilder("SELECT DISTINCT mat.`id`, CONCAT(pers.`ape_pat`,' ', pers.`ape_mat`,' ', pers.`nom`) alumno, CONCAT(perf.`ape_pat`,' ', perf.`ape_mat`,' ', perf.`nom`) familiar, perf.cel, perf.corr, niv.`nom` nivel, gra.`nom` grado, au.`secc`, rat.`res`, rat.`id` id_rat, peri.id_suc, gra.id id_grad \n"); 
					sql.append("FROM `mat_matricula` mat INNER JOIN `alu_alumno` alu ON mat.`id_alu`=alu.`id`\n");
					sql.append("INNER JOIN `col_persona` pers ON alu.`id_per`=pers.`id`\n");
					sql.append("INNER JOIN `alu_familiar` fam ON mat.`id_fam`=fam.`id`\n");
					sql.append("INNER JOIN `col_persona` perf ON fam.`id_per`=perf.`id`\n");
					sql.append("INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.`id`\n");
					sql.append("INNER JOIN `cat_grad` gra ON au.`id_grad`=gra.`id`\n"); 
					sql.append("INNER JOIN `cat_nivel` niv ON gra.`id_nvl`=niv.`id`\n");
					sql.append("INNER JOIN `col_ciclo` cic ON au.`id_cic`=cic.`id`\n");
					sql.append("INNER JOIN `per_periodo` peri ON au.`id_per`=peri.id AND cic.`id_per`=peri.`id`\n");
					sql.append("INNER JOIN `ges_servicio` srv ON peri.`id_srv`=srv.`id`\n"); 
					sql.append("LEFT JOIN `mat_ratificacion` rat ON mat.id=rat.`id_mat`\n"); 
					sql.append("WHERE peri.`id_anio`="+id_anio+" AND srv.`id_gir`="+id_gir+" AND gra.id<>14  AND niv.`id`="+id_niv+" AND cic.`id`="+id_cic+" AND (mat.`id_sit`NOT IN (5,4) OR mat.`id_sit` IS NULL) ");
					if(id_gra!=null) {
						sql.append(" AND au.id_grad="+id_gra);
					}
					if(id_au!=null) {
						sql.append(" AND au.id="+id_au);
					}
			if (rat!=null) {
				if(rat.equals("S")) {
					sql.append("\n and rat.res=1");
				} else if(rat.equals("N")) {
					sql.append("\n and rat.res=0");
				} else if(rat.equals("NR")) {
					sql.append("\n and rat.res IS NULL");
				}
				
			} 
			if(fec_ini!=null && fec_ini!="") {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String fec_mod=sdf.format(FechaUtil.toDate(fec_ini));
				sql.append("\n and DATE_FORMAT(rat.fec_ins,\"%Y-%m-%d\")>='"+fec_mod+"'");
			}
			
			if(fec_fin!=null && fec_fin!="") {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String fec_mod=sdf.format(FechaUtil.toDate(fec_fin));
				
				sql.append("\n and DATE_FORMAT(rat.fec_ins,\"%Y-%m-%d\")<='"+fec_mod+"'");
			}
				
			sql.append("\n order by gra.id, au.secc,pers.ape_pat , pers.ape_pat , pers.ape_mat , pers.nom ");
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return sqlUtil.query(sql.toString()); 
		
		
	}
	
	/*
	 * Reporte de matriculados por a�o academico
	 * es una version mejorada de la consulta anterior
	 * @param id_au
	 * @return
	 */
	public List<Row> Reporte_MatriculaAcademico(Integer id_anio, Integer id_cic, Integer id_gra,Integer id_au, Integer id_gir, Integer id_niv, String rep_com, String tras) {
		int id_anio_ant=id_anio-1;
		StringBuilder sql = null;
		if(rep_com.equals("0")) {
			sql = new StringBuilder("SELECT DISTINCT a.id_classRoom,m.fecha,m.tipo, m.id, fac.nro_rec, a.cod, s.cod situacion, per.nro_doc 'nro_doc', CONCAT(per.`ape_pat`,' ', per.ape_mat,' ', per.nom) 'Alumno', DATE_FORMAT(per.fec_nac, \"%d \") dia, DATE_FORMAT(per.fec_nac, \"%M\") mes, ");
			sql.append("\n CONCAT(per2.`ape_pat`,' ', per2.ape_mat,' ', per2.nom) 'Persona_Responsable', ");
			sql.append("\n per2.cel 'Celular_Responsable', ");
			sql.append("\n per2.corr 'Correo_Responsable', ");
			sql.append("\n dist.nom 'distrito', ");
			sql.append("\n pro.nom 'provincia', ");
			sql.append("\n usr.login , ");
			sql.append("\n IF(t.id_par='','MISMO ALUMNO',t.par) parentesco,");
			sql.append("\n m.id_sit, s.nom, an.flag_migra, sug.id_au_nue, a.usuario, a.pass_google, a.id id_alu, ");
			sql.append("\n CASE  WHEN per.id_gen='1' THEN 'M'  WHEN per.id_gen='0' THEN 'F' END AS 'Genero',");
			sql.append("\n CASE  WHEN fac.canc='1' THEN 'Cancelado'  WHEN fac.canc='0' THEN 'Pendiente' END AS 'estado',");
			sql.append("\n fac.fec_venc,");
			sql.append("\n (SELECT col.cod_mod FROM `eva_matr_vacante` mat, `eva_evaluacion_vac` eva, per_periodo per_eva, col_colegio col  WHERE mat.id_eva=eva.id AND mat.id_alu=a.id"); 
			sql.append("\n AND eva.id_per=per_eva.id AND mat.id_col=col.id AND per_eva.id_anio="+id_anio+" AND mat.res='A' LIMIT 1) cod_mod,");
			sql.append("\n (SELECT per.nro_doc FROM alu_familiar fam,alu_gru_fam_familiar fam_gpf,alu_gru_fam_alumno alu_gpf, col_persona per ");
			sql.append("\n WHERE per.id_gen='1' AND alu_gpf.id_gpf=fam_gpf.id_gpf AND a.id=alu_gpf.id_alu AND fam_gpf.id_fam=fam.id AND fam.id_per=per.id and fam.id_par=2 LIMIT 1) AS 'DNIPadre',");
			sql.append("\n (SELECT per.nro_doc FROM alu_familiar fam,alu_gru_fam_familiar fam_gpf,alu_gru_fam_alumno alu_gpf, col_persona per WHERE per.id_gen='0'");
			sql.append("\n  AND alu_gpf.id_gpf=fam_gpf.id_gpf AND a.id=alu_gpf.id_alu AND fam_gpf.id_fam=fam.id AND fam.id_per=per.id AND fam.id_par=1 LIMIT 1) AS 'DNIMadre',");
			sql.append("\n  g.nom grado, au.secc, au.id id_au"); // , CONCAT( g_ant.nom,' ', au_ant.secc) aula_anterior , au_org.secc secc_org
			sql.append("\n  FROM mat_matricula m"); 
			sql.append("\n inner join alu_alumno a on m.id_alu=a.id");
			sql.append("\n inner join col_persona per on per.id=a.id_per");
			if(id_gir.equals(1)) {
				sql.append("\n inner join alu_familiar fam on fam.id=m.id_fam_res_aca");
				sql.append("\n inner join col_persona per2 on per2.id=fam.id_per");
			} else if(id_gir.equals(2) || id_gir.equals(3)) {
				sql.append("\n inner join col_persona per2 on per2.id=m.id_per_res");
			}
			sql.append("\n inner join alu_gru_fam_alumno agfa on agfa.id_alu=a.id");
			sql.append("\n inner join per_periodo p on m.id_per=p.id");
			sql.append("\n inner join ges_servicio srv on srv.id=p.id_srv");
			sql.append("\n inner join col_anio an on p.id_anio=an.id"); 
			//sql.append("\n inner join col_aula au_org on m.id_au=au_org.id");
			sql.append("\n inner join cat_grad g on g.id=m.id_gra");
			sql.append("\n INNER JOIN alu_gru_fam agf ON agfa.id_gpf=agf.id"); 
			
			sql.append("\n left join col_aula au on m.id_au_asi=au.id ");
			sql.append("\n left JOIN fac_academico_pago fac ON fac.id_mat=m.id AND tip='mat' AND fac.nro_cuota IN (0,1) ");
			sql.append("\n left JOIN col_ciclo cic ON au.id_cic=cic.id ");
			sql.append("\n LEFT JOIN ( SELECT fam2.id_per, agff.id_gpf , par.id id_par, par.par FROM alu_familiar fam2 INNER JOIN cat_parentesco par ON par.id=fam2.id_par");
			sql.append("\n INNER JOIN alu_gru_fam_familiar agff ON fam2.id=agff.id_fam ) t ON t.id_per=per2.id AND t.id_gpf=agf.id");
			//sql.append("\n LEFT JOIN alu_familiar fam2 ON per2.id=fam2.id_per"); 
			//sql.append("\n LEFT JOIN alu_gru_fam_familiar agff ON  agff.id_gpf=agf.id AND fam2.id=agff.id_fam"); 
			//sql.append("\n LEFT JOIN cat_parentesco par ON fam2.id_par=par.id");
			//sql.append("\n left join alu_familiar fam2 on per2.id=fam2.id_per");
			//sql.append("\n left join alu_gru_fam_familiar agff on fam2.id=agff.id_fam and agff.id_gpf=agfa.id_gpf");
			//sql.append("\n left join alu_gru_fam agf on agff.id_gpf=agf.id and agfa.id_gpf=agf.id");
			sql.append("\n left join cat_distrito dist on agf.id_dist=dist.id ");
			sql.append("\n left join cat_provincia pro on dist.id_pro=pro.id ");
			//sql.append("\n left join mat_matricula  mat_ant on mat_ant.id_alu=a.id");
			//sql.append("\n inner join per_periodo  p_ant on mat_ant.id_per=p_ant.id and p_ant.id_anio="+id_anio_ant+" and p_ant.id_tpe=1");
			//sql.append("\n inner join col_aula  au_ant on mat_ant.id_au_asi=au_ant.id and au_ant.id_per=p_ant.id");
			//sql.append("\n inner join cat_grad  g_ant on au_ant.id_grad=g_ant.id");
		//	sql.append("\n left join alu_familiar fami on per2.id=fami.id_per");
			//sql.append("\n left join cat_parentesco par on fam2.id_par=par.id")
			sql.append("\n left join cat_col_situacion s on m.id_sit=s.id");
			sql.append("\n left join mat_seccion_sugerida sug on sug.id_mat=m.id");
			sql.append("\n LEFT JOIN seg_usuario usr ON m.usr_ins=usr.id");
			sql.append("\n WHERE  p.id_anio=" + id_anio+" and srv.id_gir="+id_gir+" and p.id_niv="+id_niv+" and m.id_cic="+id_cic );
			if (id_gra!=null)
				sql.append("\n and m.id_gra=" + id_gra);
			if (id_au!=null)
				sql.append("\n and m.id_au_asi=" + id_au);
			if (tras.equals("N"))
				sql.append("\n and (m.id_sit<>'5' OR m.id_sit is null ) ");
			else if(tras.equals("T"))
				sql.append("\n and m.id_sit='5'");
			
			sql.append("\n order by g.id, au.secc,per.ape_pat , per.ape_mat , per.nom ");
		} else {
			sql = new StringBuilder("SELECT DISTINCT a.id_classRoom,m.fecha,m.tipo, m.id, fac.nro_rec, a.cod, s.cod situacion, per.nro_doc 'nro_doc', CONCAT(per.`ape_pat`,' ', per.ape_mat,', ', per.nom) 'Alumno', DATE_FORMAT(per.fec_nac, \"%d \") dia, DATE_FORMAT(per.fec_nac, \"%M\") mes, ");
			sql.append("\n CONCAT(per2.`ape_pat`,' ', per2.ape_mat,' ', per2.nom) 'Persona_Responsable', ");
			sql.append("\n per2.cel 'Celular_Responsable', ");
			sql.append("\n per2.corr 'Correo_Responsable', ");
			sql.append("\n dist.nom 'distrito', ");
			sql.append("\n pro.nom 'provincia', ");
			sql.append("\n usr.login , ");
			sql.append("\n IF(t.id_par='','MISMO ALUMNO',t.par) parentesco,");
			sql.append("\n m.id_sit, s.nom, an.flag_migra, sug.id_au_nue, a.usuario, a.pass_google, a.id id_alu, ");
			sql.append("\n CASE  WHEN per.id_gen='1' THEN 'M'  WHEN per.id_gen='0' THEN 'F' END AS 'Genero',");
			sql.append("\n CASE  WHEN fac.canc='1' THEN 'Cancelado'  WHEN fac.canc='0' THEN 'Pendiente' END AS 'estado',");
			sql.append("\n fac.fec_venc,");
			sql.append("\n (SELECT col.cod_mod FROM `eva_matr_vacante` mat, `eva_evaluacion_vac` eva, per_periodo per_eva, col_colegio col  WHERE mat.id_eva=eva.id AND mat.id_alu=a.id"); 
			sql.append("\n AND eva.id_per=per_eva.id AND mat.id_col=col.id AND per_eva.id_anio="+id_anio+" AND mat.res='A' LIMIT 1) cod_mod,");
			sql.append("\n (SELECT per.nro_doc FROM alu_familiar fam,alu_gru_fam_familiar fam_gpf,alu_gru_fam_alumno alu_gpf, col_persona per ");
			sql.append("\n WHERE per.id_gen='1' AND alu_gpf.id_gpf=fam_gpf.id_gpf AND a.id=alu_gpf.id_alu AND fam_gpf.id_fam=fam.id AND fam.id_per=per.id and fam.id_par=2 LIMIT 1) AS 'DNIPadre',");
			sql.append("\n (SELECT per.nro_doc FROM alu_familiar fam,alu_gru_fam_familiar fam_gpf,alu_gru_fam_alumno alu_gpf, col_persona per WHERE per.id_gen='0'");
			sql.append("\n  AND alu_gpf.id_gpf=fam_gpf.id_gpf AND a.id=alu_gpf.id_alu AND fam_gpf.id_fam=fam.id AND fam.id_per=per.id AND fam.id_par=1 LIMIT 1) AS 'DNIMadre',");
			sql.append("\n  g.nom grado, au.secc, au.id id_au"); // , CONCAT( g_ant.nom,' ', au_ant.secc) aula_anterior , au_org.secc secc_org
			sql.append("\n  FROM mat_matricula m"); 
			sql.append("\n inner join alu_alumno a on m.id_alu=a.id");
			sql.append("\n inner join col_persona per on per.id=a.id_per");
			if(id_gir.equals(1)) {
				sql.append("\n inner join alu_familiar fam on fam.id=m.id_fam_res_aca");
				sql.append("\n inner join col_persona per2 on per2.id=fam.id_per");
			} else if(id_gir.equals(2) || id_gir.equals(3)) {
				sql.append("\n inner join col_persona per2 on per2.id=m.id_per_res");
			}
			sql.append("\n inner join alu_gru_fam_alumno agfa on agfa.id_alu=a.id");
			sql.append("\n inner join cat_grad g on g.id=m.id_gra");
			sql.append("\n inner join per_periodo p on m.id_per=p.id");
			sql.append("\n inner join ges_servicio srv on srv.id=p.id_srv");
			sql.append("\n inner join col_anio an on p.id_anio=an.id"); 
			//sql.append("\n inner join cat_grad g on g.id=au.id_grad");
			sql.append("\n INNER JOIN alu_gru_fam agf ON agfa.id_gpf=agf.id"); 
			sql.append("\n left join col_aula au on m.id_au_asi=au.id " );	
			sql.append("\n left JOIN col_ciclo cic ON au.id_cic=cic.id ");
			//sql.append("\n inner join col_aula au_org on m.id_au=au_org.id");
			sql.append("\n LEFT JOIN ( SELECT fam2.id_per, agff.id_gpf , par.id id_par, par.par FROM alu_familiar fam2 INNER JOIN cat_parentesco par ON par.id=fam2.id_par");
			sql.append("\n INNER JOIN alu_gru_fam_familiar agff ON fam2.id=agff.id_fam ) t ON t.id_per=per2.id AND t.id_gpf=agf.id");
			//sql.append("\n LEFT JOIN alu_familiar fam2 ON per2.id=fam2.id_per"); 
			//sql.append("\n LEFT JOIN alu_gru_fam_familiar agff ON  agff.id_gpf=agf.id AND fam2.id=agff.id_fam"); 
			//sql.append("\n LEFT JOIN cat_parentesco par ON fam2.id_par=par.id");
			//sql.append("\n left join alu_familiar fam2 on per2.id=fam2.id_per");
			//sql.append("\n left join alu_gru_fam_familiar agff on fam2.id=agff.id_fam");
			//sql.append("\n left join alu_gru_fam agf on agff.id_gpf=agf.id");
			sql.append("\n left join cat_distrito dist on agf.id_dist=dist.id ");
			sql.append("\n left join cat_provincia pro on dist.id_pro=pro.id ");
			//sql.append("\n left join mat_matricula  mat_ant on mat_ant.id_alu=a.id");
			//sql.append("\n inner join per_periodo  p_ant on mat_ant.id_per=p_ant.id and p_ant.id_anio="+id_anio_ant+" and p_ant.id_tpe=1");
			//sql.append("\n inner join col_aula  au_ant on mat_ant.id_au_asi=au_ant.id and au_ant.id_per=p_ant.id");
			//sql.append("\n inner join cat_grad  g_ant on au_ant.id_grad=g_ant.id");
		//	sql.append("\n left join alu_familiar fami on per2.id=fami.id_per");
			//sql.append("\n left join cat_parentesco par on fam2.id_par=par.id");
			
			sql.append("\n left join cat_col_situacion s on m.id_sit=s.id");
			sql.append("\n left join mat_seccion_sugerida sug on sug.id_mat=m.id");
			sql.append("\n inner JOIN fac_academico_pago fac ON fac.id_mat=m.id AND tip='mat' AND fac.nro_cuota IN (0,1) ");
			sql.append("\n LEFT JOIN seg_usuario usr ON m.usr_ins=usr.id");
			sql.append("\n WHERE  p.id_anio=" + id_anio+" and srv.id_gir="+id_gir+" and p.id_niv="+id_niv+ " and m.id_cic="+id_cic );
			if (id_gra!=null)
				sql.append("\n and m.id_gra=" + id_gra);
			if (id_au!=null)
				sql.append("\n and m.id_au_asi=" + id_au);
			if(tras!=null) {
				if (tras.equals("N"))
					sql.append("\n and (m.id_sit<>'5' OR m.id_sit is null ) ");
				else if(tras.equals("T"))
					sql.append("\n and m.id_sit='5'");
			}
			
			
			sql.append("\n order by g.id, au.secc,per.ape_pat , per.ape_pat , per.ape_mat , per.nom ");
		}
		


		return sqlUtil.query(sql.toString()); 
		
	}
	
	public List<Row> Reporte_Mat_Acad(Integer id_anio, Integer id_gra,Integer id_au, Integer id_gir, Integer id_niv) {
		int id_anio_ant=id_anio-1;
		StringBuilder sql = new StringBuilder("SELECT DISTINCT a.id_classRoom,m.fecha,m.tipo, m.id, fac.nro_rec, a.cod, per.nro_doc 'nro_doc', CONCAT(per.`ape_pat`,' ', per.ape_mat,' ', per.nom) 'Alumno', ");
		sql.append("\n CONCAT(per2.`ape_pat`,' ', per2.ape_mat,' ', per2.nom) 'Persona_Responsable', ");
		sql.append("\n per2.cel 'Celular_Responsable', ");
		sql.append("\n per2.corr 'Correo_Responsable', ");
		sql.append("\n IF(par.id='','MISMO ALUMNO',par.par) parentesco,");
		sql.append("\n m.id_sit, s.nom, an.flag_migra, sug.id_au_nue, a.usuario, a.pass_google, a.id id_alu, ");
		sql.append("\n CASE  WHEN per.id_gen='1' THEN 'M'  WHEN per.id_gen='0' THEN 'F' END AS 'Genero',");
		sql.append("\n CASE  WHEN fac.canc='1' THEN 'Cancelado'  WHEN fac.canc='0' THEN 'Pendiente' END AS 'estado',");
		sql.append("\n fac.fec_venc,");
		sql.append("\n (SELECT col.cod_mod FROM `eva_matr_vacante` mat, `eva_evaluacion_vac` eva, per_periodo per_eva, col_colegio col  WHERE mat.id_eva=eva.id AND mat.id_alu=a.id"); 
		sql.append("\n AND eva.id_per=per_eva.id AND mat.id_col=col.id AND per_eva.id_anio="+id_anio+" AND mat.res='A' LIMIT 1) cod_mod,");
		sql.append("\n (SELECT per.nro_doc FROM alu_familiar fam,alu_gru_fam_familiar fam_gpf,alu_gru_fam_alumno alu_gpf, col_persona per ");
		sql.append("\n WHERE per.id_gen='1' AND alu_gpf.id_gpf=fam_gpf.id_gpf AND a.id=alu_gpf.id_alu AND fam_gpf.id_fam=fam.id AND fam.id_per=per.id and fam.id_par=2 LIMIT 1) AS 'DNIPadre',");
		sql.append("\n (SELECT per.nro_doc FROM alu_familiar fam,alu_gru_fam_familiar fam_gpf,alu_gru_fam_alumno alu_gpf, col_persona per WHERE per.id_gen='0'");
		sql.append("\n  AND alu_gpf.id_gpf=fam_gpf.id_gpf AND a.id=alu_gpf.id_alu AND fam_gpf.id_fam=fam.id AND fam.id_per=per.id AND fam.id_par=1 LIMIT 1) AS 'DNIMadre',");
		sql.append("\n  g.nom grado, au.secc, au.id id_au, au_org.secc secc_org, CONCAT( t.nom,' ', t.secc) aula_anterior "); 
		sql.append("\n  FROM mat_matricula m"); 
		sql.append("\n inner join alu_alumno a on m.id_alu=a.id");
		sql.append("\n inner join col_persona per on per.id=a.id_per");
		sql.append("\n inner join col_persona per2 on per2.id=m.id_per_res");
		sql.append("\n LEFT JOIN ( SELECT g_ant.nom, au_ant.secc, mat_ant.id_alu FROM mat_matricula  mat_ant");
		sql.append("\n INNER JOIN per_periodo  p_ant ON mat_ant.id_per=p_ant.id AND p_ant.id_anio="+id_anio_ant+" AND p_ant.id_tpe=1"); 
	    sql.append("\n INNER JOIN col_aula  au_ant ON mat_ant.id_au_asi=au_ant.id  AND au_ant.id_per=p_ant.id");
		sql.append("\n INNER JOIN cat_grad  g_ant ON au_ant.id_grad=g_ant.id) t ON t.id_alu=a.id");
		sql.append("\n inner join alu_familiar fami on per2.id=fami.id_per");
		sql.append("\n inner join cat_parentesco par on fami.id_par=par.id");
		sql.append("\n inner join col_aula au on m.id_au_asi=au.id");
		sql.append("\n inner join col_aula au_org on m.id_au=au_org.id");
		sql.append("\n inner join cat_grad g on g.id=au.id_grad");
		sql.append("\n left join cat_col_situacion s on m.id_sit=s.id");
		sql.append("\n left join mat_seccion_sugerida sug on sug.id_mat=m.id");
		sql.append("\n inner JOIN fac_academico_pago fac ON fac.id_mat=m.id AND tip='mat' AND fac.nro_cuota IN (0,1) ");
		sql.append("\n inner JOIN col_ciclo cic ON au.id_cic=cic.id ");
		sql.append("\n inner join per_periodo p on cic.id_per=p.id");
		sql.append("\n inner join ges_servicio srv on srv.id=p.id_srv");
		sql.append("\n inner join col_anio an on p.id_anio=an.id"); 
		sql.append("\n WHERE  p.id_anio=" + id_anio+" and srv.id_gir="+id_gir+" and p.id_niv="+id_niv);
		if (id_gra!=null)
			sql.append("\n and m.id_gra=" + id_gra);
		if (id_au!=null)
			sql.append("\n and m.id_au_asi=" + id_au);
		
		sql.append("\n order by g.id, au.secc,a.ape_pat asc, a.ape_pat asc, a.ape_mat asc, a.nom asc");


		return sqlUtil.query(sql.toString()); 
		
	}
	
	public List<Row> reporteMatriculados(Integer id_anio,Integer id_niv, Integer id_gra,Integer id_au) {
		StringBuilder sql = new StringBuilder("SELECT alu.`id` id_alu, CONCAT(alu.`ape_pat`,' ', alu.`ape_mat`,' ', alu.`nom`) alumno, alu.`usuario`, alu.`pass_educando` psw"); 
		sql.append("\n FROM `mat_matricula` mat INNER JOIN `alu_alumno` alu ON mat.`id_alu`=alu.`id`");
		sql.append("\n INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.`id`");
		sql.append("\n INNER JOIN `cat_grad` gra ON au.`id_grad`=gra.`id`");
		sql.append("\n INNER JOIN `cat_nivel` niv ON gra.`id_nvl`=niv.`id`");
		sql.append("\n INNER JOIN per_periodo per ON au.id_per=per.id");
		sql.append("\n WHERE niv.`id`="+id_niv+" AND per.id_anio="+id_anio);
		if (id_gra!=null)
			sql.append("\n and au.id_grad=" + id_gra);
		if (id_au!=null)
			sql.append("\n and mat.id_au_asi=" + id_au);
		sql.append("\n ORDER BY alu.ape_pat, alu.ape_mat, alu.nom");
		return sqlUtil.query(sql.toString()); 
	}
	
	public List<Map<String, Object>> reporteCodigoBarras(Integer id_au) {

		String sql = "SELECT m.id, a.cod, pa.ape_pat,pa.ape_mat, pa.nom,p.id periodo, suc.nom local, m.tipo "
				+ " FROM mat_matricula m" 
				+ " inner join alu_alumno a on m.id_alu=a.id"
				+ " inner join col_persona pa on pa.id=a.id_per"
				+ " inner join col_aula au on au.id = m.id_au_asi"
				+ " left join mat_seccion_sugerida sug on sug.id_mat=m.id"
				+ " INNER JOIN fac_academico_pago fac ON fac.id_mat=m.id AND tip='mat'"
				+ " inner join per_periodo p on au.id_per=p.id" + " inner join ges_servicio ser on p.id_srv=ser.id"
				+ " inner join ges_sucursal suc on suc.id =ser.id_suc" + " inner join col_anio an on p.id_anio=an.id"
				+ " WHERE  m.id_au_asi=" + id_au + " AND (m.id_sit IS NULL OR m.id_sit NOT IN (5)) AND fac.tip='MAT' AND fac.canc=1 "
				+ " order by pa.ape_pat, pa.ape_mat, pa.nom";

		List<Map<String, Object>> Reporte = jdbcTemplate.queryForList(sql);
		logger.info(sql);
		return Reporte;
	}
	
	
	public List<Map<String, Object>> alumnoCodigoBarras(Integer id_mat) {

		String sql = "SELECT m.id, a.cod, pa.ape_pat,pa.ape_mat, pa.nom,p.id periodo, suc.nom local, au.secc, gr.nom grado, nivel.nom nivel, au.id id_au, srv.id_gir, au.id_cic  "
				+ " FROM mat_matricula m" 
				+ " inner join alu_alumno a on m.id_alu=a.id"
				+ " inner join col_persona pa on pa.id=a.id_per"
				+ " inner join col_aula au on au.id = m.id_au_asi"
				+ " inner join cat_grad gr on gr.id = au.id_grad"
				+ " inner join cat_nivel nivel on nivel.id = m.id_niv"
				+ " left join mat_seccion_sugerida sug on sug.id_mat=m.id"
				+ " LEFT JOIN fac_academico_pago fac ON fac.id_mat=m.id AND tip='mat'"
				+ " inner join per_periodo p on au.id_per=p.id" + " inner join ges_servicio ser on p.id_srv=ser.id"
				+ " inner join ges_sucursal suc on suc.id =ser.id_suc" + " inner join col_anio an on p.id_anio=an.id"
				+" inner join ges_servicio srv on p.id_srv=srv.id "
				+ " WHERE  m.id =" + id_mat 
				+ " order by pa.ape_pat, pa.ape_mat, pa.nom";

		List<Map<String, Object>> Reporte = jdbcTemplate.queryForList(sql);
		logger.info(sql);
		return Reporte;
	}

	public List<Map<String, Object>> Reporte_MatFecha(String del, String al, Integer id_anio) {

		String sql = "SELECT alu.`nro_doc`, CONCAT (alu.`ape_pat`,' ', alu.`ape_mat`,' ',alu.`nom`) alumno, ser.`nom` nivel, g.`nom` grado, au.`secc`, "
				+ " (SELECT nro_doc FROM alu_familiar fam,alu_gru_fam_familiar fam_gpf,alu_gru_fam_alumno alu_gpf "
				+ " WHERE fam.id_gen='1' AND alu_gpf.`id_gpf`=fam_gpf.`id_gpf` AND alu.id=alu_gpf.`id_alu` AND fam_gpf.`id_fam`=fam.`id` LIMIT 1) AS 'DNIPadre',"
				+ " (SELECT nro_doc FROM alu_familiar fam,alu_gru_fam_familiar fam_gpf,alu_gru_fam_alumno alu_gpf WHERE fam.id_gen='0' "
				+ " AND alu_gpf.`id_gpf`=fam_gpf.`id_gpf` AND alu.id=alu_gpf.`id_alu` AND fam_gpf.`id_fam`=fam.`id` LIMIT 1) AS 'DNIMadre'"
				+ " FROM `alu_alumno` alu, `mat_matricula` m,  `per_periodo` p, `ges_servicio` ser, `ges_sucursal` suc, col_anio a, `cat_grad` g, `col_aula` au"
				+ " WHERE m.`id_alu`=alu.`id` AND m.`id_per`=p.`id` AND p.`id_srv`=ser.`id` AND ser.`id_suc`=suc.`id` AND p.`id_anio`=a.`id` AND g.`id`=m.`id_gra`"
				+ " AND m.`id_au_asi`=au.id AND au.`id_grad`=g.`id` AND a.`id`=" + id_anio + " AND m.fecha BETWEEN '" + del//TODO ID_AU
				+ "' AND '" + al + "'"
				+ " ORDER BY ser.`nom` ASC, g.id ASC, au.`secc` ASC, alu.`ape_pat` ASC, alu.`ape_mat` ASC;";

		List<Map<String, Object>> ReporteMatFechas = jdbcTemplate.queryForList(sql);
		logger.info(sql);
		return ReporteMatFechas;
	}

	/**
	 * Tiene q ser matriculaa anterior valida
	 * @param id_alu
	 * @param id_anio
	 * @return
	 */
	public Matricula getMatriculaAnterior(Integer id_alu, Integer id_anio) {

		String sql = "SELECT m.*, p.id_suc, s.id_au_nue FROM `mat_matricula`  m "
				+ " inner JOIN per_periodo p on p.id = m.id_per "
				+ " left JOIN mat_seccion_sugerida s on s.id_mat = m.id and s.id_anio=?"
				+ " WHERE m.id_alu=? and p.id_anio=? and (m.id_sit<>'5' OR m.id_sit IS NULL) and p.id_tpe=1 order by p.fec_ini desc "; // and m.id_sit=1

		List<Matricula> matriculas = jdbcTemplate.query(sql, new Object[] {id_anio+1 ,id_alu, id_anio},
				new BeanPropertyRowMapper<Matricula>(Matricula.class));

		if (matriculas.size() == 0)
			return null;
		else
			return matriculas.get(0);
	}
	
	public Row getMatriculaAnteriorLocal(Integer id_alu, Integer id_anio) {

		String sql = "SELECT m.*, p.id_suc, s.id_suc_nue, s.id_gra_nue  FROM `mat_matricula`  m "
				+ " inner JOIN per_periodo p on p.id = m.id_per "
				+ " left JOIN mat_seccion_sugerida s on s.id_mat = m.id and s.id_anio=?"
				+ " WHERE m.id_alu=? and p.id_anio=? and (m.id_sit<>'5' OR m.id_sit IS NULL) and p.id_tpe=1 order by p.fec_ini desc "; // and m.id_sit=1

		List<Row> list = sqlUtil.query(sql,new Object[] {id_anio+1,id_alu, id_anio});
		if(list.size()>0)
		return list.get(0);
		else
			return null;

	}
	
	public Matricula getMatriculaAnteriorReserva(Integer id_alu, Integer id_anio) {

		String sql = "SELECT m.*, s.id_au_nue FROM `mat_matricula`  m "
				+ " inner JOIN per_periodo p on p.id = m.id_per "
				+ " left JOIN mat_seccion_sugerida s on s.id_mat = m.id and s.id_anio=?"
				+ " WHERE m.id_alu=? and p.id_anio=? and m.id_sit=2 order by p.fec_ini desc "; // and m.id_sit=1

		List<Matricula> matriculas = jdbcTemplate.query(sql, new Object[] {id_anio+1 ,id_alu, id_anio},
				new BeanPropertyRowMapper<Matricula>(Matricula.class));

		if (matriculas.size() == 0)
			return null;
		else
			return matriculas.get(0);
	}
	/**
	 * Tiene q ser matriculaa anterior valida
	 * @param id_alu
	 * @param id_anio
	 * @return
	 */
	public Matricula getMatriculaAnteriorParaReserva(Integer id_alu, Integer id_anio) {

		String sql = "SELECT m.*, s.id_au_nue FROM `mat_matricula`  m "
				+ " inner JOIN per_periodo p on p.id = m.id_per and p.id_tpe=1"
				+ " left JOIN mat_seccion_sugerida s on s.id_mat = m.id and s.id_anio=?"
				+ " WHERE m.id_alu=? and p.id_anio=? and m.id_sit in (1,2) order by p.fec_ini desc ";

		List<Matricula> matriculas = jdbcTemplate.query(sql, new Object[] {id_anio+1 ,id_alu, id_anio},
				new BeanPropertyRowMapper<Matricula>(Matricula.class));

		if (matriculas.size() == 0)
			return null;
		else
			return matriculas.get(0);
	}
	
	public Row getMatricula(Integer id_alu, Integer id_anio) {

		String sql = "SELECT m.*, s.id_au_nue , p.id_suc, au.secc, sit.nom sit_nom, m.id_gra, gra.nom grado, niv.nom nivel, gra.orden, cme.nom modalidad FROM mat_matricula m "
				+ " inner JOIN col_aula au on au.id = m.id_au_asi "
				+ " inner JOIN per_periodo p on p.id = m.id_per "
				+ " inner join cat_grad gra ON au.id_grad=gra.id"
				+ " inner join cat_nivel niv ON gra.id_nvl=niv.id"
				+ " left JOIN cat_col_situacion sit on sit.id = m.id_sit "
				+ " left join cat_modalidad_estudio cme ON au.id_cme=cme.id"
				+ " left JOIN mat_seccion_sugerida s on s.id_mat = m.id and s.id_anio=p.id_anio+1"
				+ " WHERE m.id_alu=? and p.id_anio=? and ((m.tipo<>'A' AND m.tipo<>'V') OR m.tipo IS NULL)"
				+ " order by p.fec_ini desc ";

		List<Row> matriculas = sqlUtil.query(sql, new Object[] { id_alu, id_anio });

		if (matriculas.size() == 0)
			return null;
		else
			return matriculas.get(0);
	}
	
	public Row getDatosMatriculaxId(Integer id_mat) {

		String sql = "SELECT m.*, s.id_au_nue , p.id_suc, au.secc, sit.nom sit_nom, m.id_gra, gra.nom grado, niv.nom nivel, gra.orden, cic.nom ciclo FROM mat_matricula m "
				+ " inner JOIN col_aula au on au.id = m.id_au_asi "
				+ " inner join col_ciclo cic ON cic.id=au.id_cic"
				+ " inner JOIN per_periodo p on p.id = cic.id_per "
				+ " inner join cat_grad gra ON au.id_grad=gra.id"
				+ " inner join cat_nivel niv ON gra.id_nvl=niv.id"
				+ " left JOIN cat_col_situacion sit on sit.id = m.id_sit "
				+ " left JOIN mat_seccion_sugerida s on s.id_mat = m.id and s.id_anio=p.id_anio+1"
				+ " WHERE m.id=?"
				+ " order by p.fec_ini desc ";

		List<Row> matriculas = sqlUtil.query(sql, new Object[] { id_mat });

		if (matriculas.size() == 0)
			return null;
		else
			return matriculas.get(0);
	}

	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int actualizarSituacion(Matricula matricula) {

		String sql = "UPDATE mat_matricula " + "SET id_sit=?, usr_act=?,fec_act=? " + "WHERE id=?";

		logger.info(sql);

		jdbcTemplate.update(sql, matricula.getId_sit(), matricula.getUsr_act(), new java.util.Date(),
				matricula.getId());

		// tambien reseta la seccion sugerida
		sql = "delete from mat_seccion_sugerida where id_mat=" + matricula.getId();
		jdbcTemplate.update(sql);

		return matricula.getId();

	}

	@Transactional(isolation = Isolation.READ_COMMITTED)
	public void actualizarSeccSugerida(Matricula matricula) {

		String sql = "select * from mat_seccion_sugerida where id_mat=" + matricula.getId();
		List<Map<String, Object>> map = jdbcTemplate.queryForList(sql);

		if (map.size() == 0) {
			sql = "insert into mat_seccion_sugerida(id_mat, id_au_nue,est,usr_ins,fec_ins) values (?,?,?,?,?)";
			jdbcTemplate.update(sql,
					new Object[] { matricula.getId(), matricula.getId_au(), "A", matricula.getUsrId(), new Date() });
		} else {
			sql = "update mat_seccion_sugerida set id_au_nue= ?, usr_act=?,fec_act=? where id_mat=?";
			jdbcTemplate.update(sql,
					new Object[] { matricula.getId_au(), matricula.getUsrId(), new Date(), matricula.getId() });
		}

		// return matricula.getId();

	}

	public int actualizarSeccionSugerida(Matricula matricula) {

		String sql = "UPDATE mat_matricula " + "SET id_au=?, usr_act=?,fec_act=? " + "WHERE id=?";

		logger.info(sql);

		jdbcTemplate.update(sql, matricula.getId_au(), matricula.getUsr_act(), new java.util.Date(), matricula.getId());
		return matricula.getId();

	}

	public int actualizarObs(Matricula matricula) {

		String sql = "UPDATE mat_matricula " + "SET obs=?, usr_act=?,fec_act=? " + "WHERE id=?";

		logger.info(sql);

		jdbcTemplate.update(sql, matricula.getObs(), matricula.getUsr_act(), new java.util.Date(), matricula.getId());
		return matricula.getId();

	}


public List<Map<String,Object>> reporteConsolidado(Integer id_anio, Integer id_suc, String tipo, Integer id_niv, Integer id_gir, Integer id_gra) {
		
		if (tipo.equals("AC")){
			String sql = "SELECT cap.ciclo, cap.giro, cap.sucursal, cap.nivel, cap.nom, cap.id_gra, cap.secc, cap.total_capacidad, IFNULL(mat.total_matriculados,0) matriculados, IFNULL(res.total_reserva,0) reservas, IFNULL(suge.total_sugeridos,0) sugeridos,";
			sql += "\n IFNULL(tras.total_traslados,0) trasladados, IFNULL(fa.total_fallecidos,0) fallecidos, IFNULL(re.total_retirados,0) retirados, ";
			sql += "\n total_capacidad - IFNULL(mat.total_matriculados,0) - IFNULL(suge.total_sugeridos,0) - IFNULL(res.total_reserva,0) + IFNULL(tras.total_traslados,0) + IFNULL(fa.total_fallecidos,0) + IFNULL(re.total_retirados,0) AS vacantes FROM";
			sql += "\n ( SELECT cic.nom ciclo, gir.nom giro, suc.nom sucursal, niv.nom nivel,id_grad id_gra, au.id id_au, au.secc, g.nom, SUM(cap)  total_capacidad";
			sql += "\n FROM col_aula au";
			sql += "\n INNER JOIN cat_grad g ON g.id = au.id_grad";
			sql += "\n INNER JOIN col_ciclo cic ON cic.id=au.id_cic";
			//sql += "\n INNER JOIN per_periodo per ON per.id = au.id_per";
			sql += "\n INNER JOIN per_periodo per ON per.id = cic.id_per";
			sql += "\n INNER JOIN cat_tip_periodo tpe ON per.id_tpe = tpe.id";
			sql += "\n INNER JOIN ges_servicio srv ON srv.id = per.id_srv";
			sql +="\n INNER JOIN ges_giro_negocio gir ON srv.id_gir=gir.id";
			sql += "\n INNER JOIN cat_nivel niv ON niv.id = srv.id_niv";
			sql += "\n INNER JOIN ges_sucursal suc ON suc.id = srv.id_suc";
			sql += "\n WHERE per.id_anio="+id_anio;
			if(id_suc!=null)
			sql += "\n AND suc.id="+id_suc;
			if(id_niv!=null)
				sql += "\n AND niv.id="+id_niv;
			if(id_gir!=null)
				sql += "\n AND gir.id="+id_gir;
			if(id_gra!=null)
				sql += "\n AND g.id="+id_gra;
			/*if(id_grad!=null)
			sql +="\n AND g.id="+id_grad;*/
			sql += "\n GROUP BY au.id ) cap ";
			sql += "\n LEFT JOIN (";
			sql += "\n SELECT cic.nom ciclo, gir.nom giro, suc.nom sucursal, niv.nom nivel, m.id_gra, g.nom, au.id id_au, au.secc, COUNT(m.id) total_matriculados";
			sql += "\n FROM mat_matricula m";
			sql += "\n INNER JOIN cat_grad g ON g.id = m.id_gra";
           			sql += "\n INNER JOIN col_aula au ON au.id = m.id_au_asi";
			sql += "\n INNER JOIN cat_nivel niv ON niv.id = g.id_nvl";
			//sql += " INNER JOIN per_periodo per ON per.id = m.id_per";
			sql += "\n INNER JOIN col_ciclo cic ON cic.id = au.id_cic";
			sql += "\n INNER JOIN per_periodo per ON per.id = cic.id_per";
			sql += "\n INNER JOIN cat_tip_periodo tpe ON per.id_tpe = tpe.id";
			sql += "\n INNER JOIN ges_servicio srv ON srv.id = per.id_srv";
			sql +="\n INNER JOIN ges_giro_negocio gir ON srv.id_gir=gir.id";
			sql += "\n INNER JOIN ges_sucursal suc ON suc.id = srv.id_suc";
			sql += "\n WHERE per.id_anio="+id_anio;
			if(id_suc!=null)
			sql += "\n AND suc.id="+id_suc;
			if(id_niv!=null)
				sql += "\n AND niv.id="+id_niv;
			if(id_gir!=null)
				sql += "\n AND gir.id="+id_gir;
			if(id_gra!=null)
				sql += "\n AND g.id="+id_gra;
			/*if(id_grad!=null)
			sql +="\n AND g.id="+id_grad;*/
			sql += "\n GROUP BY au.id";
			sql += "\n ) mat ON mat.id_gra = cap.id_gra AND mat.nivel = cap.nivel AND mat.sucursal = cap.sucursal AND cap.id_au = mat.id_au";
			sql += "\n LEFT JOIN(";
			sql += "\n SELECT cic.nom ciclo, gir.nom giro, suc.nom sucursal, niv.nom nivel, g.id id_gra, g.nom, au.id id_au, au.secc, COUNT(sug.id) total_sugeridos";
			sql += "\n FROM mat_seccion_sugerida sug";
			sql += "\n INNER JOIN mat_matricula m ON sug.id_mat=m.id";
			sql += "\n INNER JOIN col_aula au ON au.id = sug.id_au_nue";
			sql += "\n INNER JOIN `alu_alumno` alu ON m.id_alu=alu.id"; 
			sql	+= "\n INNER JOIN cat_grad g ON g.id = au.id_grad";
			sql	+= "\n INNER JOIN cat_nivel niv ON niv.id = g.id_nvl";
			sql += "\n INNER JOIN col_ciclo cic ON cic.id = au.id_cic";
			sql += "\n INNER JOIN per_periodo per ON per.id = cic.id_per";
			sql += "\n INNER JOIN cat_tip_periodo tpe ON per.id_tpe = tpe.id";
			sql += "\n INNER JOIN ges_servicio srv ON srv.id = per.id_srv";
			sql +="\n INNER JOIN ges_giro_negocio gir ON srv.id_gir=gir.id";
			sql += "\n INNER JOIN ges_sucursal suc ON suc.id = srv.id_suc";
			sql	+= "\n WHERE per.id_anio="+id_anio+" AND m.id_sit<>5";
			sql += "\n AND ( m.id_alu NOT IN (SELECT mat.`id_alu` FROM `mat_matricula` mat INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.`id` INNER JOIN col_ciclo cic ON au.id_cic=cic.id INNER JOIN `per_periodo` per ON cic.`id_per`=per.`id` WHERE per.`id_anio`="+id_anio+"))";
			sql += "\n AND"; 
			sql += "\n (m.`id_alu` NOT IN (SELECT res.id_alu FROM `mat_reserva` res INNER JOIN `col_aula` au ON res.`id_au`=au.`id` INNER JOIN `per_periodo` per ON au.`id_per`=per.`id` WHERE per.`id_anio`="+id_anio+"))";
			if (id_suc != null)
				sql += "\n AND suc.id=" + id_suc;
			if(id_niv!=null)
				sql += "\n AND niv.id="+id_niv;
			if(id_gir!=null)
				sql += "\n AND gir.id="+id_gir;
			if(id_gra!=null)
				sql += "\n AND g.id="+id_gra;
			/*if (id_grad != null)
				sql += "\n AND g.id=" + id_grad;*/
			sql += "\n GROUP BY au.id";
			sql += "\n ) suge ON suge.id_gra = cap.id_gra AND suge.nivel = cap.nivel AND suge.sucursal = cap.sucursal AND suge.id_au = cap.id_au";
			sql += "\n LEFT JOIN (";
			sql += "\n SELECT cic.nom ciclo,gir.nom giro, suc.nom sucursal, niv.nom nivel,g.id id_gra, g.nom, au.id id_au, au.secc, COUNT(r.id ) total_reserva ";
			sql += "\n FROM mat_reserva r ";
			sql += "\n INNER JOIN per_periodo per ON per.id = r.id_per";
			sql += "\n INNER JOIN cat_tip_periodo tpe ON per.id_tpe = tpe.id";
			sql += "\n INNER JOIN col_aula au ON au.id = r.id_au";
			sql += "\n INNER JOIN col_ciclo cic ON cic.id=au.id_cic and cic.id_per=per.id";
			sql += "\n INNER JOIN cat_grad g ON g.id = r.id_gra";
			sql += "\n INNER JOIN ges_servicio srv ON srv.id = per.id_srv";
			sql +="\n INNER JOIN ges_giro_negocio gir ON srv.id_gir=gir.id";
			sql += "\n INNER JOIN cat_nivel niv ON niv.id = srv.id_niv";
			sql += "\n INNER JOIN ges_sucursal suc ON suc.id = srv.id_suc";
			sql += "\n WHERE CURDATE()<=r.fec_lim AND NOT EXISTS( SELECT m.id_alu FROM mat_matricula m WHERE m.id_alu = r.id_alu  AND m.id_gra=g.id)";
			sql += "\n AND per.id_anio="+id_anio;
			if(id_suc!=null)
			sql += "\n AND suc.id="+id_suc;
			if(id_niv!=null)
				sql += "\n AND niv.id="+id_niv;
			if(id_gir!=null)
				sql += "\n AND gir.id="+id_gir;
			if(id_gra!=null)
				sql += "\n AND g.id="+id_gra;
			/*if(id_grad!=null)
			sql +="\n AND g.id="+id_grad;*/
			sql += "\n GROUP BY au.id) res ON res.id_gra = cap.id_gra AND res.nivel= cap.nivel AND res.sucursal = cap.sucursal AND res.id_au = cap.id_au";
			sql += "\n LEFT JOIN (";
			sql += "\n SELECT cic.nom ciclo,gir.nom giro, suc.nom sucursal, niv.nom nivel,g.id id_gra, g.nom, au.id id_au, au.secc, COUNT(1) total_traslados";
			sql += "\n FROM `mat_matricula` mat";
			sql += "\n INNER JOIN col_aula au ON au.id = mat.id_au_asi";
			sql += "\n INNER JOIN col_ciclo cic ON cic.id = au.id_cic";
			sql += "\n INNER JOIN per_periodo per ON per.id = cic.id_per";
			sql += "\n INNER JOIN cat_tip_periodo tpe ON per.id_tpe = tpe.id";
			sql += "\n INNER JOIN cat_grad g ON g.id = au.id_grad";
			sql += "\n INNER JOIN ges_servicio srv ON srv.id = per.id_srv";
			sql +="\n INNER JOIN ges_giro_negocio gir ON srv.id_gir=gir.id";
			sql += "\n INNER JOIN cat_nivel niv ON niv.id = srv.id_niv";
			sql += "\n INNER JOIN ges_sucursal suc ON suc.id = srv.id_suc";
			sql += "\n WHERE mat.id_sit='5' AND per.id_anio="+id_anio;
			if(id_suc!=null)
			sql += "\n AND suc.id="+id_suc;
			if(id_niv!=null)
				sql += "\n AND niv.id="+id_niv;
			if(id_gir!=null)
				sql += "\n AND gir.id="+id_gir;
			if(id_gra!=null)
				sql += "\n AND g.id="+id_gra;
			/*if(id_grad!=null)
			sql +="\n AND g.id="+id_grad;*/
			sql += "\n GROUP BY au.id) tras ON tras.id_gra = cap.id_gra AND tras.nivel= cap.nivel AND tras.sucursal = cap.sucursal AND tras.id_au = cap.id_au";
			sql += "\n LEFT JOIN (";
			sql += "\n SELECT cic.nom ciclo,gir.nom giro, suc.nom sucursal, niv.nom nivel,g.id id_gra, g.nom, au.id id_au, au.secc, COUNT(sit.id ) total_fallecidos"; 
			sql += "\n FROM col_situacion_mat sit";
			sql += "\n INNER JOIN `mat_matricula` mat ON sit.id_mat=mat.id";
			sql += "\n INNER JOIN cat_col_situacion s ON sit.id_sit=s.id";
			sql += "\n INNER JOIN col_aula au ON au.id = mat.id_au_asi";
			sql += "\n INNER JOIN col_ciclo cic ON cic.id=au.id_cic ";
			sql += "\n INNER JOIN per_periodo per ON per.id = au.id_per";
			sql += "\n INNER JOIN cat_tip_periodo tpe ON per.id_tpe = tpe.id";
			sql += "\n INNER JOIN cat_grad g ON g.id = au.id_grad";
			sql += "\n INNER JOIN ges_servicio srv ON srv.id = per.id_srv";
			sql +="\n INNER JOIN ges_giro_negocio gir ON srv.id_gir=gir.id";
			sql += "\n INNER JOIN cat_nivel niv ON niv.id = srv.id_niv";
			sql += "\n INNER JOIN ges_sucursal suc ON suc.id = srv.id_suc";
			sql += "\n WHERE s.cod='F' AND per.id_anio="+id_anio;
			if(id_suc!=null)
			sql += "\n AND suc.id="+id_suc;
			if(id_niv!=null)
				sql += "\n AND niv.id="+id_niv;
			if(id_gir!=null)
				sql += "\n AND gir.id="+id_gir;
			if(id_gra!=null)
				sql += "\n AND g.id="+id_gra;
			/*if(id_grad!=null)
			sql +="\n AND g.id="+id_grad;*/
			sql += "\n GROUP BY au.id) fa ON fa.id_gra = cap.id_gra AND fa.nivel= cap.nivel AND fa.sucursal = cap.sucursal AND fa.id_au = cap.id_au";
			sql += "\n LEFT JOIN (";
			sql += "\n SELECT cic.nom ciclo,gir.nom giro, suc.nom sucursal, niv.nom nivel,g.id id_gra, g.nom, au.id id_au, au.secc, COUNT(mat.id ) total_retirados"; 
			//sql += "\n FROM col_situacion_mat sit";
			//sql += "\n INNER JOIN `mat_matricula` mat ON sit.id_mat=mat.id";
			//sql += "\n INNER JOIN cat_col_situacion s ON sit.id_sit=s.id";
			sql += "\n FROM `mat_matricula` mat";
			sql += "\n INNER JOIN col_aula au ON au.id = mat.id_au_asi";
			sql += "\n INNER JOIN col_ciclo cic ON cic.id=au.id_cic ";
			sql += "\n INNER JOIN per_periodo per ON per.id = au.id_per";
			sql += "\n INNER JOIN cat_grad g ON g.id = au.id_grad";
			sql += "\n INNER JOIN ges_servicio srv ON srv.id = per.id_srv";
			sql +="\n INNER JOIN ges_giro_negocio gir ON srv.id_gir=gir.id";
			sql += "\n INNER JOIN cat_nivel niv ON niv.id = srv.id_niv";
			sql += "\n INNER JOIN ges_sucursal suc ON suc.id = srv.id_suc";
			sql += "\n WHERE mat.id_sit=4 AND per.id_anio="+id_anio;
			if(id_suc!=null)
			sql += "\n AND suc.id="+id_suc;
			if(id_niv!=null)
				sql += "\n AND niv.id="+id_niv;
			if(id_gir!=null)
				sql += "\n AND gir.id="+id_gir;
			if(id_gra!=null)
				sql += "\n AND g.id="+id_gra;
			/*if(id_grad!=null)
			sql +="\n AND g.id="+id_grad;*/
			sql += "\n GROUP BY au.id) re ON re.id_gra = cap.id_gra AND re.nivel= cap.nivel AND re.sucursal = cap.sucursal AND re.id_au = cap.id_au " ;
			sql += "\n ORDER BY  cap.ciclo, cap.giro, cap.sucursal, cap.id_gra ASC, cap.secc ASC";
	List<Map<String,Object>> reporteConsolidado = jdbcTemplate.queryForList(sql);

	return reporteConsolidado;
		} else if(tipo.equals("AS")){
			String sql = "SELECT cap.ciclo,cap.giro, cap.sucursal, cap.nivel, cap.nom, cap.id_gra, cap.secc, cap.total_capacidad, IFNULL(mat.total_matriculados,0) matriculados, IFNULL(res.total_reserva,0) reservas, 0 sugeridos,";
			sql += "\n IFNULL(tras.total_traslados,0) trasladados, IFNULL(fa.total_fallecidos,0) fallecidos, IFNULL(re.total_retirados,0) retirados, ";
			sql += "\n total_capacidad - IFNULL(mat.total_matriculados,0) - IFNULL(res.total_reserva,0) + IFNULL(tras.total_traslados,0) + IFNULL(fa.total_fallecidos,0) + IFNULL(re.total_retirados,0) AS vacantes FROM";
			sql += "\n ( SELECT cic.nom ciclo,gir.nom giro, suc.nom sucursal, niv.nom nivel,id_grad id_gra, au.id id_au, au.secc, g.nom, SUM(cap)  total_capacidad";
			sql += "\n FROM col_aula au";
			sql += "\n INNER JOIN cat_grad g ON g.id = au.id_grad";
			sql += "\n LEFT JOIN col_ciclo cic ON cic.id=au.id_cic";
			//sql += "\n INNER JOIN per_periodo per ON per.id = au.id_per";
			sql += "\n LEFT JOIN per_periodo per ON per.id = cic.id_per";
			sql += "\n INNER JOIN cat_tip_periodo tpe ON per.id_tpe = tpe.id";
			sql += "\n INNER JOIN ges_servicio srv ON srv.id = per.id_srv";
			sql +="\n INNER JOIN ges_giro_negocio gir ON srv.id_gir=gir.id";
			sql += "\n INNER JOIN cat_nivel niv ON niv.id = srv.id_niv";
			sql += "\n INNER JOIN ges_sucursal suc ON suc.id = srv.id_suc";
			sql += "\n WHERE per.id_anio="+id_anio;
			if(id_suc!=null)
			sql += "\n AND suc.id="+id_suc;
			if(id_niv!=null)
				sql += "\n AND niv.id="+id_niv;
			if(id_gir!=null)
				sql += "\n AND gir.id="+id_gir;
			if(id_gra!=null)
				sql += "\n AND g.id="+id_gra;
			/*if(id_grad!=null)
			sql +="\n AND g.id="+id_grad;*/
			sql += "\n GROUP BY au.id ) cap ";
			sql += "\n LEFT JOIN (";
			sql += "\n SELECT cic.nom ciclo, gir.nom giro, suc.nom sucursal, niv.nom nivel, m.id_gra, g.nom, au.id id_au, au.secc, COUNT(m.id) total_matriculados";
			sql += "\n FROM mat_matricula m";
			sql += "\n INNER JOIN cat_grad g ON g.id = m.id_gra";
			sql += "\n INNER JOIN col_aula au ON au.id = m.id_au_asi";
			sql += "\n INNER JOIN cat_nivel niv ON niv.id = g.id_nvl";
			sql += "\n INNER JOIN col_ciclo cic ON cic.id=au.id_cic";
			//sql += " INNER JOIN per_periodo per ON per.id = m.id_per";
			//sql += "\n INNER JOIN per_periodo per ON per.id = au.id_per";
			sql += "\n INNER JOIN per_periodo per ON per.id = cic.id_per";
			sql += "\n INNER JOIN cat_tip_periodo tpe ON per.id_tpe = tpe.id";
			sql += "\n INNER JOIN ges_servicio srv ON srv.id = per.id_srv";
			sql += "\n INNER JOIN ges_giro_negocio gir ON srv.id_gir=gir.id";
			sql += "\n INNER JOIN ges_sucursal suc ON suc.id = srv.id_suc";
			sql += "\n WHERE per.id_anio="+id_anio;
			if(id_suc!=null)
			sql += "\n AND suc.id="+id_suc;
			if(id_niv!=null)
				sql += "\n AND niv.id="+id_niv;
			if(id_gir!=null)
				sql += "\n AND gir.id="+id_gir;
			if(id_gra!=null)
				sql += "\n AND g.id="+id_gra;
			/*if(id_grad!=null)
			sql +="\n AND g.id="+id_grad;*/
			sql += "\n GROUP BY au.id";
			sql += "\n ) mat ON mat.id_gra = cap.id_gra AND mat.nivel = cap.nivel AND mat.sucursal = cap.sucursal AND cap.id_au = mat.id_au";
			sql += "\n LEFT JOIN (";
			sql += "\n SELECT cic.nom ciclo, gir.nom giro, suc.nom sucursal, niv.nom nivel,g.id id_gra, g.nom, au.id id_au, au.secc, COUNT(r.id ) total_reserva ";
			sql += "\n FROM mat_reserva r ";
			sql += "\n INNER JOIN per_periodo per ON per.id = r.id_per";
			sql += "\n INNER JOIN cat_tip_periodo tpe ON per.id_tpe = tpe.id";
			sql += "\n INNER JOIN col_aula au ON au.id = r.id_au";
			sql += "\n INNER JOIN col_ciclo cic ON cic.id=au.id_cic and cic.id_per=per.id";
			sql += "\n INNER JOIN cat_grad g ON g.id = r.id_gra";
			sql += "\n INNER JOIN ges_servicio srv ON srv.id = per.id_srv";
			sql += "\n INNER JOIN ges_giro_negocio gir ON srv.id_gir=gir.id";
			sql += "\n INNER JOIN cat_nivel niv ON niv.id = srv.id_niv";
			sql += "\n INNER JOIN ges_sucursal suc ON suc.id = srv.id_suc";
			sql += "\n WHERE CURDATE()<=r.fec_lim AND NOT EXISTS( SELECT m.id_alu FROM mat_matricula m INNER JOIN per_periodo p ON m.id_per=p.id WHERE m.id_alu = r.id_alu  AND m.id_gra=g.id AND p.id_tpe=per.id_tpe)";
			sql += "\n AND per.id_anio="+id_anio;
			if(id_suc!=null)
			sql += "\n AND suc.id="+id_suc;
			if(id_niv!=null)
				sql += "\n AND niv.id="+id_niv;
			if(id_gir!=null)
				sql += "\n AND gir.id="+id_gir;
			if(id_gra!=null)
				sql += "\n AND g.id="+id_gra;
			/*if(id_grad!=null)
			sql +="\n AND g.id="+id_grad;*/
			sql += "\n GROUP BY au.id) res ON res.id_gra = cap.id_gra AND res.nivel= cap.nivel AND res.sucursal = cap.sucursal AND res.id_au = cap.id_au";
			sql += "\n LEFT JOIN (";
			sql += "\n SELECT cic.nom ciclo, gir.nom giro, suc.nom sucursal, niv.nom nivel,g.id id_gra, g.nom, au.id id_au, au.secc, COUNT(1) total_traslados";
			sql += "\n FROM `mat_matricula` mat";
			sql += "\n INNER JOIN col_aula au ON au.id = mat.id_au_asi";
			sql += "\n LEFT JOIN col_ciclo cic ON cic.id=au.id_cic ";
			//sql += "\n INNER JOIN per_periodo per ON per.id = au.id_per";
			sql += "\n LEFT JOIN per_periodo per ON per.id = cic.id_per";
			sql += "\n INNER JOIN cat_grad g ON g.id = au.id_grad";
			sql += "\n INNER JOIN ges_servicio srv ON srv.id = per.id_srv";
			sql += "\n INNER JOIN ges_giro_negocio gir ON srv.id_gir=gir.id";
			sql += "\n INNER JOIN cat_nivel niv ON niv.id = srv.id_niv";
			sql += "\n INNER JOIN ges_sucursal suc ON suc.id = srv.id_suc";
			sql += "\n WHERE mat.id_sit='5' AND per.id_anio="+id_anio;
			if(id_suc!=null)
			sql += "\n AND suc.id="+id_suc;
			if(id_niv!=null)
				sql += "\n AND niv.id="+id_niv;
			if(id_gir!=null)
				sql += "\n AND gir.id="+id_gir;
			if(id_gra!=null)
				sql += "\n AND g.id="+id_gra;
			/*if(id_grad!=null)
			sql +="\n AND g.id="+id_grad;*/
			sql += "\n GROUP BY au.id) tras ON tras.id_gra = cap.id_gra AND tras.nivel= cap.nivel AND tras.sucursal = cap.sucursal AND tras.id_au = cap.id_au";
			sql += "\n LEFT JOIN (";
			sql += "\n SELECT cic.nom ciclo, gir.nom giro, suc.nom sucursal, niv.nom nivel,g.id id_gra, g.nom, au.id id_au, au.secc, COUNT(sit.id ) total_fallecidos"; 
			sql += "\n FROM col_situacion_mat sit";
			sql += "\n INNER JOIN `mat_matricula` mat ON sit.id_mat=mat.id";
			sql += "\n INNER JOIN cat_col_situacion s ON sit.id_sit=s.id";
			sql += "\n INNER JOIN col_aula au ON au.id = mat.id_au_asi";
			sql += "\n LEFT JOIN col_ciclo cic ON cic.id=au.id_cic ";
			//sql += "\n INNER JOIN per_periodo per ON per.id = au.id_per";
			sql += "\n LEFT JOIN per_periodo per ON per.id = cic.id_per";
			sql += "\n INNER JOIN cat_grad g ON g.id = au.id_grad";
			sql += "\n INNER JOIN ges_servicio srv ON srv.id = per.id_srv";
			sql += "\n INNER JOIN ges_giro_negocio gir ON srv.id_gir=gir.id";
			sql += "\n INNER JOIN cat_nivel niv ON niv.id = srv.id_niv";
			sql += "\n INNER JOIN ges_sucursal suc ON suc.id = srv.id_suc";
			sql += "\n WHERE s.cod='F' AND per.id_anio="+id_anio;
			if(id_suc!=null)
			sql += "\n AND suc.id="+id_suc;
			if(id_niv!=null)
				sql += "\n AND niv.id="+id_niv;
			if(id_gir!=null)
				sql += "\n AND gir.id="+id_gir;
			if(id_gra!=null)
				sql += "\n AND g.id="+id_gra;
			/*if(id_grad!=null)
			sql +="\n AND g.id="+id_grad;*/
			sql += "\n GROUP BY au.id) fa ON fa.id_gra = cap.id_gra AND fa.nivel= cap.nivel AND fa.sucursal = cap.sucursal AND fa.id_au = cap.id_au";
			sql += "\n LEFT JOIN (";
			sql += "\n SELECT cic.nom ciclo, gir.nom giro, suc.nom sucursal, niv.nom nivel,g.id id_gra, g.nom, au.id id_au, au.secc, COUNT(mat.id ) total_retirados"; 
			sql += "\n FROM `mat_matricula` mat";
			//sql += "\n FROM col_situacion_mat sit";
			//sql += "\n INNER JOIN `mat_matricula` mat ON sit.id_mat=mat.id";
			//sql += "\n INNER JOIN cat_col_situacion s ON sit.id_sit=s.id";
			sql += "\n INNER JOIN col_aula au ON au.id = mat.id_au_asi";
			sql += "\n LEFT JOIN col_ciclo cic ON cic.id=au.id_cic ";
			//sql += "\n INNER JOIN per_periodo per ON per.id = au.id_per";
			sql += "\n LEFT JOIN per_periodo per ON per.id = cic.id_per";
			sql += "\n INNER JOIN cat_grad g ON g.id = au.id_grad";
			sql += "\n INNER JOIN ges_servicio srv ON srv.id = per.id_srv";
			sql += "\n INNER JOIN ges_giro_negocio gir ON srv.id_gir=gir.id";
			sql += "\n INNER JOIN cat_nivel niv ON niv.id = srv.id_niv";
			sql += "\n INNER JOIN ges_sucursal suc ON suc.id = srv.id_suc";
			sql += "\n WHERE mat.id_sit=4 AND per.id_anio="+id_anio;
			if(id_suc!=null)
			sql += "\n AND suc.id="+id_suc;
			if(id_niv!=null)
				sql += "\n AND niv.id="+id_niv;
			if(id_gir!=null)
				sql += "\n AND gir.id="+id_gir;
			if(id_gra!=null)
				sql += "\n AND g.id="+id_gra;
			/*if(id_grad!=null)
			sql +="\n AND g.id="+id_grad;*/
			sql += "\n GROUP BY au.id) re ON re.id_gra = cap.id_gra AND re.nivel= cap.nivel AND re.sucursal = cap.sucursal AND re.id_au = cap.id_au " ;
			sql += "\n ORDER BY  cap.ciclo, cap.giro, cap.sucursal, cap.id_gra ASC, cap.secc ASC";
	List<Map<String,Object>> reporteConsolidado = jdbcTemplate.queryForList(sql);

	return reporteConsolidado;
		} else {
			return null;
		}
		
	} 


public List<Map<String,Object>> reporteConsolidadoGrado(Integer id_anio, Integer id_suc, String tipo, Integer id_niv, Integer id_gir, Integer id_gra) {
	Integer id_anio_ant=id_anio-1;
	if (tipo.equals("AC")){
		String sql = "SELECT cap.giro, cap.sucursal, cap.nivel, cap.nom, cap.id_gra, cap.total_capacidad, IFNULL(mat.total_matriculados,0) matriculados, IFNULL(res.total_reserva,0) reservas, IFNULL(suge.total_sugeridos,0) sugeridos,";
		sql += "\n IFNULL(mat_ant.total_matr_ant,0) matriculas_ant, IFNULL(tras.total_traslados,0) trasladados, IFNULL(fa.total_fallecidos,0) fallecidos, IFNULL(re.total_retirados,0) retirados, ";
		sql += "\n total_capacidad - IFNULL(mat.total_matriculados,0) - IFNULL(suge.total_sugeridos,0) - IFNULL(mat_ant.total_matr_ant,0) - IFNULL(res.total_reserva,0) + IFNULL(tras.total_traslados,0) + IFNULL(fa.total_fallecidos,0) + IFNULL(re.total_retirados,0) AS vacantes FROM";
		sql += "\n ( SELECT gir.nom giro, suc.nom sucursal, niv.nom nivel,id_grad id_gra, g.nom, tpe.id id_tpe, SUM(cap)  total_capacidad";
		sql += "\n FROM col_aula au";
		sql += "\n INNER JOIN cat_grad g ON g.id = au.id_grad";
		sql += "\n INNER JOIN col_ciclo cic ON cic.id=au.id_cic";
		//sql += "\n INNER JOIN per_periodo per ON per.id = au.id_per";
		sql += "\n INNER JOIN per_periodo per ON per.id = cic.id_per";
		sql += "\n INNER JOIN cat_tip_periodo tpe ON per.id_tpe = tpe.id";
		sql += "\n INNER JOIN ges_servicio srv ON srv.id = per.id_srv";
		sql +="\n INNER JOIN ges_giro_negocio gir ON srv.id_gir=gir.id";
		sql += "\n INNER JOIN cat_nivel niv ON niv.id = srv.id_niv";
		sql += "\n INNER JOIN ges_sucursal suc ON suc.id = srv.id_suc";
		sql += "\n WHERE per.id_anio="+id_anio;
		if(id_suc!=null)
			sql += "\n AND suc.id="+id_suc;
		if(id_niv!=null)
			sql += "\n AND niv.id="+id_niv;
		if(id_gir!=null)
			sql += "\n AND gir.id="+id_gir;
		if(id_gra!=null)
			sql += "\n AND g.id="+id_gra;
		/*if(id_grad!=null)
		sql +="\n AND g.id="+id_grad;*/
		sql += "\n GROUP BY g.id, per.id_tpe, suc.id ) cap ";
		sql += "\n LEFT JOIN (";
		sql += "\n SELECT gir.nom giro, suc.nom sucursal, niv.nom nivel, m.id_gra, g.nom, tpe.id id_tpe, COUNT(m.id) total_matriculados";
		sql += "\n FROM mat_matricula m";
		sql += "\n INNER JOIN cat_grad g ON g.id = m.id_gra";
       			sql += "\n INNER JOIN col_aula au ON au.id = m.id_au_asi";
		sql += "\n INNER JOIN cat_nivel niv ON niv.id = g.id_nvl";
		//sql += " INNER JOIN per_periodo per ON per.id = m.id_per";
		sql += "\n INNER JOIN col_ciclo cic ON cic.id = au.id_cic";
		sql += "\n INNER JOIN per_periodo per ON per.id = cic.id_per";
		sql += "\n INNER JOIN cat_tip_periodo tpe ON per.id_tpe = tpe.id";
		sql += "\n INNER JOIN ges_servicio srv ON srv.id = per.id_srv";
		sql +="\n INNER JOIN ges_giro_negocio gir ON srv.id_gir=gir.id";
		sql += "\n INNER JOIN ges_sucursal suc ON suc.id = srv.id_suc";
		sql += "\n WHERE per.id_anio="+id_anio;
		if(id_suc!=null)
		sql += "\n AND suc.id="+id_suc;
		if(id_niv!=null)
			sql += "\n AND niv.id="+id_niv;
		if(id_gir!=null)
			sql += "\n AND gir.id="+id_gir;
		if(id_gra!=null)
			sql += "\n AND g.id="+id_gra;
		/*if(id_grad!=null)
		sql +="\n AND g.id="+id_grad;*/
		sql += "\n GROUP BY g.id, per.id_tpe, suc.id";
		sql += "\n ) mat ON mat.id_gra = cap.id_gra AND mat.nivel = cap.nivel AND mat.sucursal = cap.sucursal AND cap.id_tpe=mat.id_tpe ";
		sql += "\n LEFT JOIN(";
		sql += "\n SELECT gir.nom giro, suc.nom sucursal, niv.nom nivel, g.id id_gra, g.nom,tpe.id id_tpe, COUNT(sug.id) total_sugeridos";
		sql += "\n FROM mat_seccion_sugerida sug";
		sql += "\n INNER JOIN mat_matricula m ON sug.id_mat=m.id";
		//sql += "\n INNER JOIN col_aula au ON au.id = sug.id_au_nue";
		sql += "\n INNER JOIN `alu_alumno` alu ON m.id_alu=alu.id"; 
		sql	+= "\n INNER JOIN cat_grad g ON g.id = sug.id_gra_nue";
		sql	+= "\n INNER JOIN cat_nivel niv ON niv.id = g.id_nvl";
		//sql += "\n INNER JOIN col_ciclo cic ON cic.id = au.id_cic";
		sql += "\n INNER JOIN per_periodo per ON per.id_anio = sug.id_anio and per.id_suc=sug.id_suc_nue and per.id_tpe=1 ";
		sql += "\n INNER JOIN cat_tip_periodo tpe ON per.id_tpe = tpe.id";
		sql += "\n INNER JOIN ges_servicio srv ON srv.id = per.id_srv";
		sql +="\n INNER JOIN ges_giro_negocio gir ON srv.id_gir=gir.id";
		sql += "\n INNER JOIN ges_sucursal suc ON suc.id = srv.id_suc";
		sql	+= "\n WHERE per.id_anio="+id_anio+" AND m.id_sit<>5";
		sql += "\n AND ( m.id_alu NOT IN (SELECT mat.`id_alu` FROM `mat_matricula` mat INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.`id` INNER JOIN col_ciclo cic ON au.id_cic=cic.id INNER JOIN `per_periodo` per ON cic.`id_per`=per.`id` WHERE per.id_tpe=1 AND per.`id_anio`="+id_anio+"))";
		sql += "\n AND"; 
		sql += "\n (m.`id_alu` NOT IN (SELECT res.id_alu FROM `mat_reserva` res INNER JOIN `per_periodo` per ON res.`id_per`=per.`id` WHERE per.`id_anio`="+id_anio+"))";
		if (id_suc != null)
			sql += "\n AND suc.id=" + id_suc;
		if(id_niv!=null)
			sql += "\n AND niv.id="+id_niv;
		if(id_gir!=null)
			sql += "\n AND gir.id="+id_gir;
		if(id_gra!=null)
			sql += "\n AND g.id="+id_gra;
		/*if (id_grad != null)
			sql += "\n AND g.id=" + id_grad;*/
		sql += "\n GROUP BY g.id, per.id_tpe, suc.id";
		sql += "\n ) suge ON suge.id_gra = cap.id_gra AND suge.nivel = cap.nivel AND suge.sucursal = cap.sucursal AND cap.id_tpe=suge.id_tpe ";
		sql += "\n LEFT JOIN (";
		sql += "\n SELECT gir.nom giro, suc.nom sucursal, niv_sig.nom nivel,gra_sig.id id_gra, gra_sig.nom , per.id_tpe, COUNT(mat.id ) total_matr_ant";
		sql += "\n FROM `mat_matricula` mat INNER JOIN col_aula au ON mat.`id_au_asi`=au.id";
		sql += "\n INNER JOIN `cat_grad` gra ON au.`id_grad`=gra.`id`"; 
		sql += "\n INNER JOIN cat_nivel niv ON gra.id_nvl=niv.id";
		sql += "\n INNER JOIN col_ciclo cic ON au.id_cic=cic.id"; 
		sql += "\n INNER JOIN `per_periodo` per ON cic.`id_per`=per.`id`";
		sql += "\n INNER JOIN `ges_servicio` srv ON per.id_srv=srv.id";
		sql += "\n INNER JOIN ges_sucursal suc ON per.id_suc=suc.id";
		sql += "\n INNER JOIN ges_giro_negocio gir ON srv.id_gir=gir.id";
		sql += "\n INNER JOIN cat_grad gra_sig ON gra_sig.`id`=gra.id+1";
		sql += "\n INNER JOIN cat_nivel niv_sig ON gra_sig.`id_nvl`=niv_sig.id";
		sql += "\n WHERE per.`id_anio`="+id_anio_ant+"  AND per.id_tpe=1";
		sql += "\n AND (mat.id_sit IN (1,3))"; 
		sql += "\n AND mat.id_alu NOT IN (SELECT id_alu FROM mat_matricula mat_act INNER JOIN per_periodo peri ON mat_act.id_per=peri.id WHERE peri.id_tpe='1' AND peri.id_anio="+id_anio+")";
		sql += "\n AND mat.id NOT IN (SELECT id_mat FROM mat_seccion_sugerida sug WHERE sug.id_mat=mat.id)";
		sql += "\n GROUP BY gra.id, per.id_tpe, per.id_suc";
		sql += "\n ) mat_ant ON mat_ant.id_gra = cap.id_gra AND mat_ant.nivel = cap.nivel AND mat_ant.sucursal = cap.sucursal AND cap.id_tpe=mat_ant.id_tpe ";
		sql += "\n LEFT JOIN (";
		sql += "\n SELECT gir.nom giro, suc.nom sucursal, niv.nom nivel,g.id id_gra, g.nom, tpe.id id_tpe, COUNT(r.id ) total_reserva ";
		sql += "\n FROM mat_reserva r ";
		sql += "\n INNER JOIN per_periodo per ON per.id = r.id_per";
		sql += "\n INNER JOIN cat_tip_periodo tpe ON per.id_tpe = tpe.id";
		//sql += "\n INNER JOIN col_aula au ON au.id = r.id_au";
		sql += "\n INNER JOIN cat_grad g ON g.id = r.id_gra";
		sql += "\n INNER JOIN ges_servicio srv ON srv.id = per.id_srv";
		sql +="\n INNER JOIN ges_giro_negocio gir ON srv.id_gir=gir.id";
		sql += "\n INNER JOIN cat_nivel niv ON niv.id = srv.id_niv";
		sql += "\n INNER JOIN ges_sucursal suc ON suc.id = srv.id_suc";
		sql += "\n WHERE CURDATE()<=r.fec_lim AND NOT EXISTS( SELECT m.id_alu FROM mat_matricula m INNER JOIN per_periodo p ON m.id_per=p.id WHERE m.id_alu = r.id_alu  AND m.id_gra=g.id AND p.id_tpe=1)";
		sql += "\n AND per.id_anio="+id_anio;
		if(id_suc!=null)
		sql += "\n AND suc.id="+id_suc;
		if(id_niv!=null)
			sql += "\n AND niv.id="+id_niv;
		if(id_gir!=null)
			sql += "\n AND gir.id="+id_gir;
		if(id_gra!=null)
			sql += "\n AND g.id="+id_gra;
		/*if(id_grad!=null)
		sql +="\n AND g.id="+id_grad;*/
		sql += "\n GROUP BY g.id, per.id_tpe, suc.id) res ON res.id_gra = cap.id_gra AND res.nivel= cap.nivel AND res.sucursal = cap.sucursal AND cap.id_tpe=res.id_tpe ";
		sql += "\n LEFT JOIN (";
		sql += "\n SELECT gir.nom giro, suc.nom sucursal, niv.nom nivel,g.id id_gra, g.nom, tpe.id id_tpe, COUNT(1) total_traslados";
		sql += "\n FROM `mat_matricula` mat";
		sql += "\n INNER JOIN col_aula au ON au.id = mat.id_au_asi";
		sql += "\n INNER JOIN col_ciclo cic ON cic.id = au.id_cic";
		sql += "\n INNER JOIN per_periodo per ON per.id = cic.id_per";
		sql += "\n INNER JOIN cat_tip_periodo tpe ON per.id_tpe = tpe.id";
		sql += "\n INNER JOIN cat_grad g ON g.id = au.id_grad";
		sql += "\n INNER JOIN ges_servicio srv ON srv.id = per.id_srv";
		sql +="\n INNER JOIN ges_giro_negocio gir ON srv.id_gir=gir.id";
		sql += "\n INNER JOIN cat_nivel niv ON niv.id = srv.id_niv";
		sql += "\n INNER JOIN ges_sucursal suc ON suc.id = srv.id_suc";
		sql += "\n WHERE mat.id_sit='5' AND per.id_anio="+id_anio;
		if(id_suc!=null)
		sql += "\n AND suc.id="+id_suc;
		if(id_niv!=null)
			sql += "\n AND niv.id="+id_niv;
		if(id_gir!=null)
			sql += "\n AND gir.id="+id_gir;
		if(id_gra!=null)
			sql += "\n AND g.id="+id_gra;
		/*if(id_grad!=null)
		sql +="\n AND g.id="+id_grad;*/
		sql += "\n GROUP BY g.id, per.id_tpe, suc.id) tras ON tras.id_gra = cap.id_gra AND tras.nivel= cap.nivel AND tras.sucursal = cap.sucursal AND cap.id_tpe=tras.id_tpe ";
		sql += "\n LEFT JOIN (";
		sql += "\n SELECT gir.nom giro, suc.nom sucursal, niv.nom nivel,g.id id_gra, g.nom, tpe.id id_tpe, COUNT(sit.id ) total_fallecidos"; 
		sql += "\n FROM col_situacion_mat sit";
		sql += "\n INNER JOIN `mat_matricula` mat ON sit.id_mat=mat.id";
		sql += "\n INNER JOIN cat_col_situacion s ON sit.id_sit=s.id";
		sql += "\n INNER JOIN col_aula au ON au.id = mat.id_au_asi";
		sql += "\n INNER JOIN per_periodo per ON per.id = au.id_per";
		sql += "\n INNER JOIN cat_tip_periodo tpe ON per.id_tpe = tpe.id";
		sql += "\n INNER JOIN cat_grad g ON g.id = au.id_grad";
		sql += "\n INNER JOIN ges_servicio srv ON srv.id = per.id_srv";
		sql +="\n INNER JOIN ges_giro_negocio gir ON srv.id_gir=gir.id";
		sql += "\n INNER JOIN cat_nivel niv ON niv.id = srv.id_niv";
		sql += "\n INNER JOIN ges_sucursal suc ON suc.id = srv.id_suc";
		sql += "\n WHERE s.cod='F' AND per.id_anio="+id_anio;
		if(id_suc!=null)
		sql += "\n AND suc.id="+id_suc;
		if(id_niv!=null)
			sql += "\n AND niv.id="+id_niv;
		if(id_gir!=null)
			sql += "\n AND gir.id="+id_gir;
		if(id_gra!=null)
			sql += "\n AND g.id="+id_gra;
		/*if(id_grad!=null)
		sql +="\n AND g.id="+id_grad;*/
		sql += "\n GROUP BY g.id, per.id_tpe, suc.id) fa ON fa.id_gra = cap.id_gra AND fa.nivel= cap.nivel AND fa.sucursal = cap.sucursal AND cap.id_tpe=fa.id_tpe ";
		sql += "\n LEFT JOIN (";
		sql += "\n SELECT gir.nom giro, suc.nom sucursal, niv.nom nivel,g.id id_gra, g.nom ,  per.id_tpe , COUNT(mat.id ) total_retirados"; 
		sql += "\n FROM `mat_matricula` mat";
		//sql += "\n FROM col_situacion_mat sit";
		//sql += "\n INNER JOIN `mat_matricula` mat ON sit.id_mat=mat.id";
		//sql += "\n INNER JOIN cat_col_situacion s ON sit.id_sit=s.id";
		sql += "\n INNER JOIN col_aula au ON au.id = mat.id_au_asi";
		sql += "\n INNER JOIN per_periodo per ON per.id = au.id_per";
		sql += "\n INNER JOIN cat_grad g ON g.id = au.id_grad";
		sql += "\n INNER JOIN ges_servicio srv ON srv.id = per.id_srv";
		sql +="\n INNER JOIN ges_giro_negocio gir ON srv.id_gir=gir.id";
		sql += "\n INNER JOIN cat_nivel niv ON niv.id = srv.id_niv";
		sql += "\n INNER JOIN ges_sucursal suc ON suc.id = srv.id_suc";
		sql += "\n WHERE mat.id_sit=4 AND per.id_anio="+id_anio;
		if(id_suc!=null)
		sql += "\n AND suc.id="+id_suc;
		if(id_niv!=null)
			sql += "\n AND niv.id="+id_niv;
		if(id_gir!=null)
			sql += "\n AND gir.id="+id_gir;
		if(id_gra!=null)
			sql += "\n AND g.id="+id_gra;
		/*if(id_grad!=null)
		sql +="\n AND g.id="+id_grad;*/
		sql += "\n GROUP BY g.id, per.id_tpe, suc.id) re ON re.id_gra = cap.id_gra AND re.nivel= cap.nivel AND re.sucursal = cap.sucursal  AND cap.id_tpe=re.id_tpe " ;
		sql += "\n ORDER BY   cap.giro, cap.sucursal, cap.id_gra ASC ";
List<Map<String,Object>> reporteConsolidado = jdbcTemplate.queryForList(sql);

return reporteConsolidado;
	} else if(tipo.equals("AS")){
		String sql = "SELECT cap.giro, cap.sucursal, cap.nivel, cap.nom, cap.id_gra, cap.total_capacidad, IFNULL(mat.total_matriculados,0) matriculados, IFNULL(res.total_reserva,0) reservas, 0 sugeridos, 0 matriculas_ant,";
		sql += "\n IFNULL(tras.total_traslados,0) trasladados, IFNULL(fa.total_fallecidos,0) fallecidos, IFNULL(re.total_retirados,0) retirados, ";
		sql += "\n total_capacidad - IFNULL(mat.total_matriculados,0) - IFNULL(res.total_reserva,0) + IFNULL(tras.total_traslados,0) + IFNULL(fa.total_fallecidos,0) + IFNULL(re.total_retirados,0) AS vacantes FROM";
		sql += "\n ( SELECT gir.nom giro, suc.nom sucursal, niv.nom nivel,id_grad id_gra, g.nom, tpe.id id_tpe, SUM(cap)  total_capacidad";
		sql += "\n FROM col_aula au";
		sql += "\n INNER JOIN cat_grad g ON g.id = au.id_grad";
		sql += "\n LEFT JOIN col_ciclo cic ON cic.id=au.id_cic";
		//sql += "\n INNER JOIN per_periodo per ON per.id = au.id_per";
		sql += "\n LEFT JOIN per_periodo per ON per.id = cic.id_per";
		sql += "\n INNER JOIN cat_tip_periodo tpe ON per.id_tpe = tpe.id";
		sql += "\n INNER JOIN ges_servicio srv ON srv.id = per.id_srv";
		sql +="\n INNER JOIN ges_giro_negocio gir ON srv.id_gir=gir.id";
		sql += "\n INNER JOIN cat_nivel niv ON niv.id = srv.id_niv";
		sql += "\n INNER JOIN ges_sucursal suc ON suc.id = srv.id_suc";
		sql += "\n WHERE per.id_anio="+id_anio;
		if(id_suc!=null)
		sql += "\n AND suc.id="+id_suc;
		if(id_niv!=null)
			sql += "\n AND niv.id="+id_niv;
		if(id_gir!=null)
			sql += "\n AND gir.id="+id_gir;
		if(id_gra!=null)
			sql += "\n AND g.id="+id_gra;
		/*if(id_grad!=null)
		sql +="\n AND g.id="+id_grad;*/
		sql += "\n GROUP BY g.id, per.id_tpe, suc.id) cap ";
		sql += "\n LEFT JOIN (";
		sql += "\n SELECT gir.nom giro, suc.nom sucursal, niv.nom nivel, m.id_gra, g.nom, tpe.id id_tpe, COUNT(m.id) total_matriculados";
		sql += "\n FROM mat_matricula m";
		sql += "\n INNER JOIN cat_grad g ON g.id = m.id_gra";
		sql += "\n INNER JOIN col_aula au ON au.id = m.id_au_asi";
		sql += "\n INNER JOIN cat_nivel niv ON niv.id = g.id_nvl";
		sql += "\n INNER JOIN col_ciclo cic ON cic.id=au.id_cic";
		//sql += " INNER JOIN per_periodo per ON per.id = m.id_per";
		//sql += "\n INNER JOIN per_periodo per ON per.id = au.id_per";
		sql += "\n INNER JOIN per_periodo per ON per.id = cic.id_per";
		sql += "\n INNER JOIN cat_tip_periodo tpe ON per.id_tpe = tpe.id";
		sql += "\n INNER JOIN ges_servicio srv ON srv.id = per.id_srv";
		sql += "\n INNER JOIN ges_giro_negocio gir ON srv.id_gir=gir.id";
		sql += "\n INNER JOIN ges_sucursal suc ON suc.id = srv.id_suc";
		sql += "\n WHERE per.id_anio="+id_anio;
		if(id_suc!=null)
		sql += "\n AND suc.id="+id_suc;
		if(id_niv!=null)
			sql += "\n AND niv.id="+id_niv;
		if(id_gir!=null)
			sql += "\n AND gir.id="+id_gir;
		if(id_gra!=null)
			sql += "\n AND g.id="+id_gra;
		/*if(id_grad!=null)
		sql +="\n AND g.id="+id_grad;*/
		sql += "\n GROUP BY g.id, per.id_tpe, suc.id";
		sql += "\n ) mat ON mat.id_gra = cap.id_gra AND mat.nivel = cap.nivel AND mat.sucursal = cap.sucursal AND cap.id_tpe=mat.id_tpe ";
		sql += "\n LEFT JOIN (";
		sql += "\n SELECT gir.nom giro, suc.nom sucursal, niv.nom nivel,g.id id_gra, g.nom, tpe.id id_tpe, COUNT(r.id ) total_reserva ";
		sql += "\n FROM mat_reserva r ";
		sql += "\n INNER JOIN per_periodo per ON per.id = r.id_per";
		sql += "\n INNER JOIN cat_tip_periodo tpe ON per.id_tpe = tpe.id";
		//sql += "\n INNER JOIN col_aula au ON au.id = r.id_au";
		sql += "\n INNER JOIN col_ciclo cic ON  cic.id_per=per.id";
		sql += "\n INNER JOIN cat_grad g ON g.id = r.id_gra";
		sql += "\n INNER JOIN ges_servicio srv ON srv.id = per.id_srv";
		sql += "\n INNER JOIN ges_giro_negocio gir ON srv.id_gir=gir.id";
		sql += "\n INNER JOIN cat_nivel niv ON niv.id = srv.id_niv";
		sql += "\n INNER JOIN ges_sucursal suc ON suc.id = srv.id_suc";
		sql += "\n WHERE CURDATE()<=r.fec_lim AND NOT EXISTS( SELECT m.id_alu FROM mat_matricula m INNER JOIN per_periodo p ON m.id_per=p.id WHERE m.id_alu = r.id_alu  AND m.id_gra=g.id AND p.id_tpe=per.id_tpe)";
		sql += "\n AND per.id_anio="+id_anio;
		if(id_suc!=null)
		sql += "\n AND suc.id="+id_suc;
		if(id_niv!=null)
			sql += "\n AND niv.id="+id_niv;
		if(id_gir!=null)
			sql += "\n AND gir.id="+id_gir;
		if(id_gra!=null)
			sql += "\n AND g.id="+id_gra;
		/*if(id_grad!=null)
		sql +="\n AND g.id="+id_grad;*/
		sql += "\n GROUP BY g.id, per.id_tpe, suc.id) res ON res.id_gra = cap.id_gra AND res.nivel= cap.nivel AND res.sucursal = cap.sucursal AND res.id_tpe=cap.id_tpe ";
		sql += "\n LEFT JOIN (";
		sql += "\n SELECT gir.nom giro, suc.nom sucursal, niv.nom nivel,g.id id_gra, g.nom, per.id_tpe,  COUNT(1) total_traslados";
		sql += "\n FROM `mat_matricula` mat";
		sql += "\n INNER JOIN col_aula au ON au.id = mat.id_au_asi";
		sql += "\n LEFT JOIN col_ciclo cic ON cic.id=au.id_cic ";
		//sql += "\n INNER JOIN per_periodo per ON per.id = au.id_per";
		sql += "\n LEFT JOIN per_periodo per ON per.id = cic.id_per";
		sql += "\n INNER JOIN cat_grad g ON g.id = au.id_grad";
		sql += "\n INNER JOIN ges_servicio srv ON srv.id = per.id_srv";
		sql += "\n INNER JOIN ges_giro_negocio gir ON srv.id_gir=gir.id";
		sql += "\n INNER JOIN cat_nivel niv ON niv.id = srv.id_niv";
		sql += "\n INNER JOIN ges_sucursal suc ON suc.id = srv.id_suc";
		sql += "\n WHERE mat.id_sit='5' AND per.id_anio="+id_anio;
		if(id_suc!=null)
		sql += "\n AND suc.id="+id_suc;
		if(id_niv!=null)
			sql += "\n AND niv.id="+id_niv;
		if(id_gir!=null)
			sql += "\n AND gir.id="+id_gir;
		if(id_gra!=null)
			sql += "\n AND g.id="+id_gra;
		/*if(id_grad!=null)
		sql +="\n AND g.id="+id_grad;*/
		sql += "\n GROUP BY g.id, per.id_tpe, suc.id) tras ON tras.id_gra = cap.id_gra AND tras.nivel= cap.nivel AND tras.sucursal = cap.sucursal AND tras.id_tpe=cap.id_tpe ";
		sql += "\n LEFT JOIN (";
		sql += "\n SELECT gir.nom giro, suc.nom sucursal, niv.nom nivel,g.id id_gra, g.nom, per.id_tpe, COUNT(sit.id ) total_fallecidos"; 
		sql += "\n FROM col_situacion_mat sit";
		sql += "\n INNER JOIN `mat_matricula` mat ON sit.id_mat=mat.id";
		sql += "\n INNER JOIN cat_col_situacion s ON sit.id_sit=s.id";
		sql += "\n INNER JOIN col_aula au ON au.id = mat.id_au_asi";
		sql += "\n LEFT JOIN col_ciclo cic ON cic.id=au.id_cic ";
		//sql += "\n INNER JOIN per_periodo per ON per.id = au.id_per";
		sql += "\n LEFT JOIN per_periodo per ON per.id = cic.id_per";
		sql += "\n INNER JOIN cat_grad g ON g.id = au.id_grad";
		sql += "\n INNER JOIN ges_servicio srv ON srv.id = per.id_srv";
		sql += "\n INNER JOIN ges_giro_negocio gir ON srv.id_gir=gir.id";
		sql += "\n INNER JOIN cat_nivel niv ON niv.id = srv.id_niv";
		sql += "\n INNER JOIN ges_sucursal suc ON suc.id = srv.id_suc";
		sql += "\n WHERE s.cod='F' AND per.id_anio="+id_anio;
		if(id_suc!=null)
		sql += "\n AND suc.id="+id_suc;
		if(id_niv!=null)
			sql += "\n AND niv.id="+id_niv;
		if(id_gir!=null)
			sql += "\n AND gir.id="+id_gir;
		if(id_gra!=null)
			sql += "\n AND g.id="+id_gra;
		/*if(id_grad!=null)
		sql +="\n AND g.id="+id_grad;*/
		sql += "\n GROUP BY g.id, per.id_tpe, suc.id) fa ON fa.id_gra = cap.id_gra AND fa.nivel= cap.nivel AND fa.sucursal = cap.sucursal AND fa.id_tpe=cap.id_tpe ";
		sql += "\n LEFT JOIN (";
		sql += "\n SELECT gir.nom giro, suc.nom sucursal, niv.nom nivel,g.id id_gra, g.nom, per.id_tpe, COUNT(mat.id ) total_retirados";
		sql += "\n FROM `mat_matricula` mat";
		//sql += "\n FROM col_situacion_mat sit";
		//sql += "\n INNER JOIN `mat_matricula` mat ON sit.id_mat=mat.id";
		//sql += "\n INNER JOIN cat_col_situacion s ON sit.id_sit=s.id";
		sql += "\n INNER JOIN col_aula au ON au.id = mat.id_au_asi";
		sql += "\n LEFT JOIN col_ciclo cic ON cic.id=au.id_cic ";
		//sql += "\n INNER JOIN per_periodo per ON per.id = au.id_per";
		sql += "\n LEFT JOIN per_periodo per ON per.id = cic.id_per";
		sql += "\n INNER JOIN cat_grad g ON g.id = au.id_grad";
		sql += "\n INNER JOIN ges_servicio srv ON srv.id = per.id_srv";
		sql += "\n INNER JOIN ges_giro_negocio gir ON srv.id_gir=gir.id";
		sql += "\n INNER JOIN cat_nivel niv ON niv.id = srv.id_niv";
		sql += "\n INNER JOIN ges_sucursal suc ON suc.id = srv.id_suc";
		sql += "\n WHERE mat.id_sit=4 AND per.id_anio="+id_anio;
		if(id_suc!=null)
		sql += "\n AND suc.id="+id_suc;
		if(id_niv!=null)
			sql += "\n AND niv.id="+id_niv;
		if(id_gir!=null)
			sql += "\n AND gir.id="+id_gir;
		if(id_gra!=null)
			sql += "\n AND g.id="+id_gra;
		/*if(id_grad!=null)
		sql +="\n AND g.id="+id_grad;*/
		sql += "\n GROUP BY g.id, per.id_tpe, suc.id) re ON re.id_gra = cap.id_gra AND re.nivel= cap.nivel AND re.sucursal = cap.sucursal AND re.id_tpe=cap.id_tpe " ;
		sql += "\n ORDER BY   cap.giro, cap.sucursal, cap.id_gra ASC";
List<Map<String,Object>> reporteConsolidado = jdbcTemplate.queryForList(sql);

return reporteConsolidado;
	} else {
		return null;
	}
	
} 


public List<Map<String,Object>> reporteConsolidadoGradoGeneral(Integer id_anio, Integer id_suc, String tipo, Integer id_niv, Integer id_gir, Integer id_gra) {
	Integer id_anio_ant=id_anio-1;
	
	//Integer id_niv_ant=id_niv-1;
	if (tipo.equals("C1")){
		String sql = "SELECT cap.giro, cap.sucursal, cap.nivel, cap.nom, cap.id_gra, cap.total_capacidad, IFNULL(eva_mat.total_matr_vacante,0) matriculas_vacante, IFNULL(mat.total_matriculados,0) matriculados, IFNULL(res.total_reserva,0) reservas, 0 sugeridos,";
		sql += "\n 0 matriculas_ant, 0 no_ratificados, IFNULL(tras.total_traslados,0) trasladados, IFNULL(fa.total_fallecidos,0) fallecidos, IFNULL(re.total_retirados,0) retirados, ";
		sql += "\n total_capacidad -IFNULL(eva_mat.total_matr_vacante,0)- IFNULL(mat.total_matriculados,0) - IFNULL(res.total_reserva,0) + IFNULL(tras.total_traslados,0) + IFNULL(fa.total_fallecidos,0) + IFNULL(re.total_retirados,0) AS vacantes FROM";
		sql += "\n ( SELECT gir.nom giro, suc.nom sucursal, niv.nom nivel,id_grad id_gra, g.nom, tpe.id id_tpe, SUM(cap)  total_capacidad";
		sql += "\n FROM col_aula au";
		sql += "\n INNER JOIN cat_grad g ON g.id = au.id_grad";
		sql += "\n INNER JOIN col_ciclo cic ON cic.id=au.id_cic";
		//sql += "\n INNER JOIN per_periodo per ON per.id = au.id_per";
		sql += "\n INNER JOIN per_periodo per ON per.id = cic.id_per";
		sql += "\n INNER JOIN cat_tip_periodo tpe ON per.id_tpe = tpe.id";
		sql += "\n INNER JOIN ges_servicio srv ON srv.id = per.id_srv";
		sql +="\n INNER JOIN ges_giro_negocio gir ON srv.id_gir=gir.id";
		sql += "\n INNER JOIN cat_nivel niv ON niv.id = srv.id_niv";
		sql += "\n INNER JOIN ges_sucursal suc ON suc.id = srv.id_suc";
		sql += "\n WHERE per.id_anio="+id_anio;
		if(id_suc!=null)
			sql += "\n AND suc.id="+id_suc;
		if(id_niv!=null)
			sql += "\n AND niv.id="+id_niv;
		if(id_gir!=null)
			sql += "\n AND gir.id="+id_gir;
		if(id_gra!=null)
			sql += "\n AND g.id="+id_gra;
		/*if(id_grad!=null)
		sql +="\n AND g.id="+id_grad;*/
		sql += "\n GROUP BY g.id, per.id_tpe, suc.id ) cap ";
		sql += "\n LEFT JOIN (";
		sql += "\n SELECT gir.nom giro, suc.nom sucursal, niv.nom nivel, eva_mat.id_gra, g.nom, 1 id_tpe, COUNT(eva_mat.id) total_matr_vacante";
		sql += "\n FROM `eva_matr_vacante` eva_mat ";
		sql += "\n INNER JOIN `eva_evaluacion_vac` eva ON `eva_mat`.`id_eva`=eva.`id` ";
		sql += "\n INNER JOIN cat_grad g ON g.id = eva_mat.id_gra";
		sql += "\n INNER JOIN per_periodo per ON per.id = eva.id_per";
		sql += "\n INNER JOIN cat_tip_periodo tpe ON per.id_tpe = tpe.id";
		sql += "\n INNER JOIN ges_servicio srv ON srv.id = per.id_srv";
		sql +="\n INNER JOIN ges_giro_negocio gir ON srv.id_gir=gir.id";
		sql += "\n INNER JOIN cat_nivel niv ON niv.id = srv.id_niv";
		sql += "\n INNER JOIN ges_sucursal suc ON suc.id = srv.id_suc";
		sql += "\n WHERE per.id_anio="+id_anio+" AND (eva_mat.res<>'N' OR eva_mat.`res` IS NULL) AND CURDATE()<=eva.fec_vig_vac AND `eva_mat`.`id_alu` NOT IN (SELECT res.id_alu FROM `mat_reserva` res INNER JOIN per_periodo per ON res.id_per=per.id AND per.id_anio="+id_anio+")";
		if(id_suc!=null)
		sql += "\n AND suc.id="+id_suc;
		if(id_niv!=null)
			sql += "\n AND niv.id="+id_niv;
		if(id_gir!=null)
			sql += "\n AND gir.id="+id_gir;
		if(id_gra!=null)
			sql += "\n AND g.id="+id_gra;
		/*if(id_grad!=null)
		sql +="\n AND g.id="+id_grad;*/
		sql += "\n GROUP BY g.id, per.id_tpe, suc.id";
		sql += "\n ) eva_mat ON eva_mat.id_gra = cap.id_gra AND eva_mat.nivel = cap.nivel AND eva_mat.sucursal = cap.sucursal AND cap.id_tpe=eva_mat.id_tpe ";
		sql += "\n LEFT JOIN (";
		sql += "\n SELECT gir.nom giro, suc.nom sucursal, niv.nom nivel, m.id_gra, g.nom, tpe.id id_tpe, COUNT(m.id) total_matriculados";
		sql += "\n FROM mat_matricula m";
		sql += "\n INNER JOIN cat_grad g ON g.id = m.id_gra";
		sql += "\n INNER JOIN col_aula au ON au.id = m.id_au_asi";
		sql += "\n INNER JOIN cat_nivel niv ON niv.id = g.id_nvl";
		sql += "\n INNER JOIN col_ciclo cic ON cic.id=au.id_cic";
		//sql += " INNER JOIN per_periodo per ON per.id = m.id_per";
		//sql += "\n INNER JOIN per_periodo per ON per.id = au.id_per";
		sql += "\n INNER JOIN per_periodo per ON per.id = cic.id_per";
		sql += "\n INNER JOIN cat_tip_periodo tpe ON per.id_tpe = tpe.id";
		sql += "\n INNER JOIN ges_servicio srv ON srv.id = per.id_srv";
		sql += "\n INNER JOIN ges_giro_negocio gir ON srv.id_gir=gir.id";
		sql += "\n INNER JOIN ges_sucursal suc ON suc.id = srv.id_suc";
		sql += "\n WHERE per.id_anio="+id_anio;
		if(id_suc!=null)
		sql += "\n AND suc.id="+id_suc;
		if(id_niv!=null)
			sql += "\n AND niv.id="+id_niv;
		if(id_gir!=null)
			sql += "\n AND gir.id="+id_gir;
		if(id_gra!=null)
			sql += "\n AND g.id="+id_gra;
		/*if(id_grad!=null)
		sql +="\n AND g.id="+id_grad;*/
		sql += "\n GROUP BY g.id, per.id_tpe, suc.id";
		sql += "\n ) mat ON mat.id_gra = cap.id_gra AND mat.nivel = cap.nivel AND mat.sucursal = cap.sucursal AND cap.id_tpe=mat.id_tpe ";
		sql += "\n LEFT JOIN (";
		sql += "\n SELECT gir.nom giro, suc.nom sucursal, niv.nom nivel,g.id id_gra, g.nom, tpe.id id_tpe, COUNT(r.id ) total_reserva ";
		sql += "\n FROM mat_reserva r ";
		sql += "\n INNER JOIN per_periodo per ON per.id = r.id_per";
		sql += "\n INNER JOIN cat_tip_periodo tpe ON per.id_tpe = tpe.id";
		//sql += "\n INNER JOIN col_aula au ON au.id = r.id_au";
		sql += "\n INNER JOIN col_ciclo cic ON  cic.id_per=per.id";
		sql += "\n INNER JOIN cat_grad g ON g.id = r.id_gra";
		sql += "\n INNER JOIN ges_servicio srv ON srv.id = per.id_srv";
		sql += "\n INNER JOIN ges_giro_negocio gir ON srv.id_gir=gir.id";
		sql += "\n INNER JOIN cat_nivel niv ON niv.id = srv.id_niv";
		sql += "\n INNER JOIN ges_sucursal suc ON suc.id = srv.id_suc";
		sql += "\n WHERE CURDATE()<=r.fec_lim AND NOT EXISTS( SELECT m.id_alu FROM mat_matricula m INNER JOIN per_periodo p ON m.id_per=p.id WHERE m.id_alu = r.id_alu  AND m.id_gra=g.id AND p.id_tpe=per.id_tpe AND p.id_anio="+id_anio+")";
		sql += "\n AND per.id_anio="+id_anio;
		if(id_suc!=null)
		sql += "\n AND suc.id="+id_suc;
		if(id_niv!=null)
			sql += "\n AND niv.id="+id_niv;
		if(id_gir!=null)
			sql += "\n AND gir.id="+id_gir;
		if(id_gra!=null)
			sql += "\n AND g.id="+id_gra;
		/*if(id_grad!=null)
		sql +="\n AND g.id="+id_grad;*/
		sql += "\n GROUP BY g.id, per.id_tpe, suc.id) res ON res.id_gra = cap.id_gra AND res.nivel= cap.nivel AND res.sucursal = cap.sucursal AND res.id_tpe=cap.id_tpe ";
		sql += "\n LEFT JOIN (";
		sql += "\n SELECT gir.nom giro, suc.nom sucursal, niv.nom nivel,g.id id_gra, g.nom, tpe.id id_tpe, COUNT(1) total_traslados";
		sql += "\n FROM `mat_matricula` mat";
		sql += "\n INNER JOIN col_aula au ON au.id = mat.id_au_asi";
		sql += "\n INNER JOIN col_ciclo cic ON cic.id = au.id_cic";
		sql += "\n INNER JOIN per_periodo per ON per.id = cic.id_per";
		sql += "\n INNER JOIN cat_tip_periodo tpe ON per.id_tpe = tpe.id";
		sql += "\n INNER JOIN cat_grad g ON g.id = au.id_grad";
		sql += "\n INNER JOIN ges_servicio srv ON srv.id = per.id_srv";
		sql +="\n INNER JOIN ges_giro_negocio gir ON srv.id_gir=gir.id";
		sql += "\n INNER JOIN cat_nivel niv ON niv.id = srv.id_niv";
		sql += "\n INNER JOIN ges_sucursal suc ON suc.id = srv.id_suc";
		sql += "\n WHERE mat.id_sit='5' AND per.id_anio="+id_anio;
		if(id_suc!=null)
		sql += "\n AND suc.id="+id_suc;
		if(id_niv!=null)
			sql += "\n AND niv.id="+id_niv;
		if(id_gir!=null)
			sql += "\n AND gir.id="+id_gir;
		if(id_gra!=null)
			sql += "\n AND g.id="+id_gra;
		/*if(id_grad!=null)
		sql +="\n AND g.id="+id_grad;*/
		sql += "\n GROUP BY g.id, per.id_tpe, suc.id) tras ON tras.id_gra = cap.id_gra AND tras.nivel= cap.nivel AND tras.sucursal = cap.sucursal AND cap.id_tpe=tras.id_tpe ";
		sql += "\n LEFT JOIN (";
		sql += "\n SELECT gir.nom giro, suc.nom sucursal, niv.nom nivel,g.id id_gra, g.nom, tpe.id id_tpe, COUNT(sit.id ) total_fallecidos"; 
		sql += "\n FROM col_situacion_mat sit";
		sql += "\n INNER JOIN `mat_matricula` mat ON sit.id_mat=mat.id";
		sql += "\n INNER JOIN cat_col_situacion s ON sit.id_sit=s.id";
		sql += "\n INNER JOIN col_aula au ON au.id = mat.id_au_asi";
		sql += "\n INNER JOIN per_periodo per ON per.id = au.id_per";
		sql += "\n INNER JOIN cat_tip_periodo tpe ON per.id_tpe = tpe.id";
		sql += "\n INNER JOIN cat_grad g ON g.id = au.id_grad";
		sql += "\n INNER JOIN ges_servicio srv ON srv.id = per.id_srv";
		sql +="\n INNER JOIN ges_giro_negocio gir ON srv.id_gir=gir.id";
		sql += "\n INNER JOIN cat_nivel niv ON niv.id = srv.id_niv";
		sql += "\n INNER JOIN ges_sucursal suc ON suc.id = srv.id_suc";
		sql += "\n WHERE s.cod='F' AND per.id_anio="+id_anio;
		if(id_suc!=null)
		sql += "\n AND suc.id="+id_suc;
		if(id_niv!=null)
			sql += "\n AND niv.id="+id_niv;
		if(id_gir!=null)
			sql += "\n AND gir.id="+id_gir;
		if(id_gra!=null)
			sql += "\n AND g.id="+id_gra;
		/*if(id_grad!=null)
		sql +="\n AND g.id="+id_grad;*/
		sql += "\n GROUP BY g.id, per.id_tpe, suc.id) fa ON fa.id_gra = cap.id_gra AND fa.nivel= cap.nivel AND fa.sucursal = cap.sucursal AND cap.id_tpe=fa.id_tpe ";
		sql += "\n LEFT JOIN (";
		sql += "\n SELECT gir.nom giro, suc.nom sucursal, niv.nom nivel,g.id id_gra, g.nom ,  per.id_tpe , COUNT(mat.id ) total_retirados"; 
		//sql += "\n FROM col_situacion_mat sit";
		sql += "\n FROM `mat_matricula` mat";
		//sql += "\n INNER JOIN  ON sit.id_mat=mat.id";
		//sql += "\n INNER JOIN cat_col_situacion s ON sit.id_sit=s.id";
		sql += "\n INNER JOIN col_aula au ON au.id = mat.id_au_asi";
		sql += "\n INNER JOIN per_periodo per ON per.id = au.id_per";
		sql += "\n INNER JOIN cat_grad g ON g.id = au.id_grad";
		sql += "\n INNER JOIN ges_servicio srv ON srv.id = per.id_srv";
		sql +="\n INNER JOIN ges_giro_negocio gir ON srv.id_gir=gir.id";
		sql += "\n INNER JOIN cat_nivel niv ON niv.id = srv.id_niv";
		sql += "\n INNER JOIN ges_sucursal suc ON suc.id = srv.id_suc";
		sql += "\n WHERE mat.id_sit='4' AND per.id_anio="+id_anio;
		if(id_suc!=null)
		sql += "\n AND suc.id="+id_suc;
		if(id_niv!=null)
			sql += "\n AND niv.id="+id_niv;
		if(id_gir!=null)
			sql += "\n AND gir.id="+id_gir;
		if(id_gra!=null)
			sql += "\n AND g.id="+id_gra;
		/*if(id_grad!=null)
		sql +="\n AND g.id="+id_grad;*/
		sql += "\n GROUP BY g.id, per.id_tpe, suc.id) re ON re.id_gra = cap.id_gra AND re.nivel= cap.nivel AND re.sucursal = cap.sucursal  AND cap.id_tpe=re.id_tpe " ;
		sql += "\n ORDER BY   cap.giro, cap.sucursal, cap.id_gra ASC ";
List<Map<String,Object>> reporteConsolidado = jdbcTemplate.queryForList(sql);

return reporteConsolidado;
	} else if(tipo.equals("C2")){
		
		String sql = "SELECT cap.giro, cap.sucursal, cap.nivel, cap.nom, cap.id_gra, cap.total_capacidad, IFNULL(eva_mat.total_matr_vacante,0) matriculas_vacante, IFNULL(mat.total_matriculados,0) matriculados, IFNULL(res.total_reserva,0) reservas, 0 sugeridos,";
		sql += "\n 0 matriculas_ant, IFNULL(desa.total_desaprobados,0) desaprobados, IFNULL(tras.total_traslados,0) trasladados, IFNULL(rat.total_no_rat,0) no_ratificados, IFNULL(fa.total_fallecidos,0) fallecidos, IFNULL(re.total_retirados,0) retirados, ";
		sql += "\n total_capacidad - IFNULL(eva_mat.total_matr_vacante,0)- IFNULL(mat.total_matriculados,0) - IFNULL(desa.total_desaprobados,0) - IFNULL(res.total_reserva,0) + IFNULL(tras.total_traslados,0) + IFNULL(fa.total_fallecidos,0) + IFNULL(re.total_retirados,0) AS vacantes FROM";
		sql += "\n ( SELECT gir.nom giro, suc.nom sucursal, niv.nom nivel,id_grad id_gra, g.nom, tpe.id id_tpe, SUM(cap)  total_capacidad";
		sql += "\n FROM col_aula au";
		sql += "\n INNER JOIN cat_grad g ON g.id = au.id_grad";
		sql += "\n INNER JOIN col_ciclo cic ON cic.id=au.id_cic";
		//sql += "\n INNER JOIN per_periodo per ON per.id = au.id_per";
		sql += "\n INNER JOIN per_periodo per ON per.id = cic.id_per";
		sql += "\n INNER JOIN cat_tip_periodo tpe ON per.id_tpe = tpe.id";
		sql += "\n INNER JOIN ges_servicio srv ON srv.id = per.id_srv";
		sql +="\n INNER JOIN ges_giro_negocio gir ON srv.id_gir=gir.id";
		sql += "\n INNER JOIN cat_nivel niv ON niv.id = srv.id_niv";
		sql += "\n INNER JOIN ges_sucursal suc ON suc.id = srv.id_suc";
		sql += "\n WHERE per.id_anio="+id_anio;
		if(id_suc!=null)
			sql += "\n AND suc.id="+id_suc;
		if(id_niv!=null)
			sql += "\n AND niv.id="+id_niv;
		if(id_gir!=null)
			sql += "\n AND gir.id="+id_gir;
		if(id_gra!=null)
			sql += "\n AND g.id="+id_gra;
		/*if(id_grad!=null)
		sql +="\n AND g.id="+id_grad;*/
		sql += "\n GROUP BY g.id, per.id_tpe, suc.id ) cap ";
		sql += "\n LEFT JOIN (";
		sql += "\n SELECT gir.nom giro, suc.nom sucursal, niv.nom nivel, eva_mat.id_gra, g.nom, 1 id_tpe, COUNT(eva_mat.id) total_matr_vacante";
		sql += "\n FROM `eva_matr_vacante` eva_mat ";
		sql += "\n INNER JOIN `eva_evaluacion_vac` eva ON `eva_mat`.`id_eva`=eva.`id` ";
		sql += "\n INNER JOIN cat_grad g ON g.id = eva_mat.id_gra";
		sql += "\n INNER JOIN per_periodo per ON per.id = eva.id_per";
		sql += "\n INNER JOIN cat_tip_periodo tpe ON per.id_tpe = tpe.id";
		sql += "\n INNER JOIN ges_servicio srv ON srv.id = per.id_srv";
		sql +="\n INNER JOIN ges_giro_negocio gir ON srv.id_gir=gir.id";
		sql += "\n INNER JOIN cat_nivel niv ON niv.id = srv.id_niv";
		sql += "\n INNER JOIN ges_sucursal suc ON suc.id = srv.id_suc";
		sql += "\n WHERE per.id_anio="+id_anio+" AND (eva_mat.res<>'N' OR eva_mat.`res` IS NULL) AND CURDATE()<=eva.fec_vig_vac AND `eva_mat`.`id_alu` NOT IN (SELECT res.id_alu FROM `mat_reserva` res INNER JOIN per_periodo per ON res.id_per=per.id AND per.id_anio="+id_anio+")";
		if(id_suc!=null)
		sql += "\n AND suc.id="+id_suc;
		if(id_niv!=null)
			sql += "\n AND niv.id="+id_niv;
		if(id_gir!=null)
			sql += "\n AND gir.id="+id_gir;
		if(id_gra!=null)
			sql += "\n AND g.id="+id_gra;
		/*if(id_grad!=null)
		sql +="\n AND g.id="+id_grad;*/
		sql += "\n GROUP BY g.id, per.id_tpe, suc.id";
		sql += "\n ) eva_mat ON eva_mat.id_gra = cap.id_gra AND eva_mat.nivel = cap.nivel AND eva_mat.sucursal = cap.sucursal AND cap.id_tpe=eva_mat.id_tpe ";
		sql += "\n LEFT JOIN (";
		sql += "\n SELECT gir.nom giro, suc.nom sucursal, ";
		sql += "\n (CASE WHEN g.id=1 THEN 'Inicial'  WHEN g.id=1 THEN 'Inicial'" ;
		sql += "\n 	WHEN g.id=2 THEN 'Inicial' ";
		sql += "\n 	WHEN g.id=3 THEN 'Primaria' ";
		sql += "\n 	WHEN g.id=4 THEN 'Primaria'"; 
		sql += "\n 	WHEN g.id=5 THEN 'Primaria'"; 
		sql += "\n 	WHEN g.id=6 THEN 'Primaria'"; 
		sql += "\n 	WHEN g.id=7 THEN 'Primaria'"; 
		sql += "\n 	WHEN g.id=8 THEN 'Primaria'"; 
		sql += "\n 	WHEN g.id=9 THEN 'Secundaria'"; 
		sql += "\n 	WHEN g.id=10 THEN 'Secundaria'"; 
		sql += "\n 	WHEN g.id=11 THEN 'Secundaria'"; 
		sql += "\n 	WHEN g.id=12 THEN 'Secundaria'"; 
		sql += "\n 	WHEN g.id=13 THEN 'Secundaria'"; 
		sql += "\n 	ELSE '' END ) nivel, g.id+1 id_gra,"; 
		sql += "\n 	 (CASE WHEN g.id+1=2 THEN '4 AÑOS'"; 
		sql += "\n 	 WHEN g.id+1=3 THEN '5 AÑOS'"; 
		sql += "\n 	 WHEN g.id+1=4 THEN 'PRIMERO'"; 
		sql += "\n 	 WHEN g.id+1=5 THEN 'SEGUNDO'"; 
		sql += "\n 	 WHEN g.id+1=6 THEN 'TERCERO'"; 
		sql += "\n 	 WHEN g.id+1=7 THEN 'CUARTO'"; 
		sql += "\n 	 WHEN g.id+1=8 THEN 'QUINTO'"; 
		sql += "\n 	 WHEN g.id+1=9 THEN 'SEXTO'"; 
		sql += "\n 	 WHEN g.id+1=10 THEN 'PRIMERO'"; 
		sql += "\n 	 WHEN g.id+1=11 THEN 'SEGUNDO' ";
		sql += "\n 	 WHEN g.id+1=12 THEN 'TERCERO' ";
		sql += "\n 	 WHEN g.id+1=13 THEN 'CUARTO' ";
		sql += "\n 	 WHEN g.id+1=14 THEN 'QUINTO'"; 
		sql += "\n 	 ELSE '' END) nom, tpe.id id_tpe, COUNT(rat.id) total_no_rat";
		sql += "\n FROM `mat_ratificacion` rat ";
		sql += "\n INNER JOIN `mat_matricula` mat ON `rat`.`id_mat`=mat.`id` ";
		sql += "\n INNER JOIN col_aula au ON mat.`id_au_asi`=au.id";				
		sql += "\n INNER JOIN cat_grad g ON g.id = au.id_grad";
		sql += "\n INNER JOIN col_ciclo cic ON cic.id = au.id_cic";
		sql += "\n INNER JOIN per_periodo per ON per.id = cic.id_per";
		sql += "\n INNER JOIN cat_tip_periodo tpe ON per.id_tpe = tpe.id";
		sql += "\n INNER JOIN ges_servicio srv ON srv.id = per.id_srv";
		sql +="\n INNER JOIN ges_giro_negocio gir ON srv.id_gir=gir.id";
		sql += "\n INNER JOIN cat_nivel niv ON niv.id = srv.id_niv";
		sql += "\n INNER JOIN ges_sucursal suc ON suc.id = srv.id_suc";
		sql += "\n WHERE rat.id_anio_rat="+id_anio+" AND per.`id_anio`="+id_anio_ant+" AND rat.res=0 ";
		if(id_suc!=null)
		sql += "\n AND suc.id="+id_suc;
		/*if(id_niv_ant!=null)
			sql += "\n AND niv.id="+id_niv_ant;*/
		if(id_gir!=null)
			sql += "\n AND gir.id="+id_gir;
		if(id_gra!=null) {
			Integer id_gra_ant=id_gra-1;
			sql += "\n AND g.id="+id_gra_ant;
		}
			
		/*if(id_grad!=null)
		sql +="\n AND g.id="+id_grad;*/
		sql += "\n GROUP BY g.id, per.id_tpe, suc.id";
		sql += "\n ) rat ON rat.id_gra = cap.id_gra AND rat.nivel = cap.nivel AND rat.sucursal = cap.sucursal AND cap.id_tpe=rat.id_tpe ";
		sql += "\n LEFT JOIN (";
		sql += "\n SELECT gir.nom giro, suc.nom sucursal, niv.nom nivel, g.id id_gra, g.nom, tpe.id id_tpe, COUNT(m.id) total_matriculados";
		sql += "\n FROM mat_matricula m";
		sql += "\n INNER JOIN cat_grad g ON g.id = m.id_gra";
		sql += "\n INNER JOIN col_aula au ON au.id = m.id_au_asi";
		sql += "\n INNER JOIN cat_nivel niv ON niv.id = g.id_nvl";
		sql += "\n INNER JOIN col_ciclo cic ON cic.id=au.id_cic";
		//sql += " INNER JOIN per_periodo per ON per.id = m.id_per";
		//sql += "\n INNER JOIN per_periodo per ON per.id = au.id_per";
		sql += "\n INNER JOIN per_periodo per ON per.id = cic.id_per";
		sql += "\n INNER JOIN cat_tip_periodo tpe ON per.id_tpe = tpe.id";
		sql += "\n INNER JOIN ges_servicio srv ON srv.id = per.id_srv";
		sql += "\n INNER JOIN ges_giro_negocio gir ON srv.id_gir=gir.id";
		sql += "\n INNER JOIN ges_sucursal suc ON suc.id = srv.id_suc";
		sql += "\n WHERE per.id_anio="+id_anio;
		if(id_suc!=null)
		sql += "\n AND suc.id="+id_suc;
		if(id_niv!=null)
			sql += "\n AND niv.id="+id_niv;
		if(id_gir!=null)
			sql += "\n AND gir.id="+id_gir;
		if(id_gra!=null)
			sql += "\n AND g.id="+id_gra;
		/*if(id_grad!=null)
		sql +="\n AND g.id="+id_grad;*/
		sql += "\n GROUP BY g.id, per.id_tpe, suc.id";
		sql += "\n ) mat ON mat.id_gra = cap.id_gra AND mat.nivel = cap.nivel AND mat.sucursal = cap.sucursal AND cap.id_tpe=mat.id_tpe ";
		sql += "\n LEFT JOIN (";
		sql += "\n SELECT gir.nom giro, suc.nom sucursal, niv.nom nivel, g.id id_gra, g.nom, tpe.id id_tpe, COUNT(m.id) total_desaprobados";
		sql += "\n FROM mat_matricula m";
		sql += "\n INNER JOIN cat_grad g ON g.id = m.id_gra";
		sql += "\n INNER JOIN col_aula au ON au.id = m.id_au_asi";
		sql += "\n INNER JOIN cat_nivel niv ON niv.id = g.id_nvl";
		sql += "\n INNER JOIN col_ciclo cic ON cic.id=au.id_cic";
		//sql += " INNER JOIN per_periodo per ON per.id = m.id_per";
		//sql += "\n INNER JOIN per_periodo per ON per.id = au.id_per";
		sql += "\n INNER JOIN per_periodo per ON per.id = cic.id_per";
		sql += "\n INNER JOIN cat_tip_periodo tpe ON per.id_tpe = tpe.id";
		sql += "\n INNER JOIN ges_servicio srv ON srv.id = per.id_srv";
		sql += "\n INNER JOIN ges_giro_negocio gir ON srv.id_gir=gir.id";
		sql += "\n INNER JOIN ges_sucursal suc ON suc.id = srv.id_suc";
		sql += "\n WHERE per.id_anio="+id_anio_ant+" AND m.id_sit=3 ";
		if(id_suc!=null)
		sql += "\n AND suc.id="+id_suc;
		if(id_niv!=null)
			sql += "\n AND niv.id="+id_niv;
		if(id_gir!=null)
			sql += "\n AND gir.id="+id_gir;
		if(id_gra!=null)
			sql += "\n AND g.id="+id_gra;
		/*if(id_grad!=null)
		sql +="\n AND g.id="+id_grad;*/
		sql += "\n GROUP BY g.id, per.id_tpe, suc.id";
		sql += "\n ) desa ON desa.id_gra = cap.id_gra AND desa.nivel = cap.nivel AND desa.sucursal = cap.sucursal AND cap.id_tpe=desa.id_tpe ";
		sql += "\n LEFT JOIN (";
		sql += "\n SELECT gir.nom giro, suc.nom sucursal, niv.nom nivel,g.id id_gra, g.nom, tpe.id id_tpe, COUNT(r.id ) total_reserva ";
		sql += "\n FROM mat_reserva r ";
		sql += "\n INNER JOIN per_periodo per ON per.id = r.id_per";
		sql += "\n INNER JOIN cat_tip_periodo tpe ON per.id_tpe = tpe.id";
		//sql += "\n INNER JOIN col_aula au ON au.id = r.id_au";
		sql += "\n INNER JOIN cat_grad g ON g.id = r.id_gra";
		sql += "\n INNER JOIN ges_servicio srv ON srv.id = per.id_srv";
		sql +="\n INNER JOIN ges_giro_negocio gir ON srv.id_gir=gir.id";
		sql += "\n INNER JOIN cat_nivel niv ON niv.id = srv.id_niv";
		sql += "\n INNER JOIN ges_sucursal suc ON suc.id = srv.id_suc";
		sql += "\n WHERE CURDATE()<=r.fec_lim AND NOT EXISTS( SELECT m.id_alu FROM mat_matricula m INNER JOIN per_periodo p ON m.id_per=p.id WHERE m.id_alu = r.id_alu  AND m.id_gra=g.id AND p.id_tpe=1 AND p.id_anio="+id_anio+")";
		sql += "\n AND per.id_anio="+id_anio;
		if(id_suc!=null)
		sql += "\n AND suc.id="+id_suc;
		if(id_niv!=null)
			sql += "\n AND niv.id="+id_niv;
		if(id_gir!=null)
			sql += "\n AND gir.id="+id_gir;
		if(id_gra!=null)
			sql += "\n AND g.id="+id_gra;
		/*if(id_grad!=null)
		sql +="\n AND g.id="+id_grad;*/
		sql += "\n GROUP BY g.id, per.id_tpe, suc.id) res ON res.id_gra = cap.id_gra AND res.nivel= cap.nivel AND res.sucursal = cap.sucursal AND cap.id_tpe=res.id_tpe ";
		sql += "\n LEFT JOIN (";
		sql += "\n SELECT gir.nom giro, suc.nom sucursal, niv.nom nivel,g.id id_gra, g.nom, tpe.id id_tpe, COUNT(1) total_traslados";
		sql += "\n FROM `mat_matricula` mat";
		sql += "\n INNER JOIN col_aula au ON au.id = mat.id_au_asi";
		sql += "\n INNER JOIN col_ciclo cic ON cic.id = au.id_cic";
		sql += "\n INNER JOIN per_periodo per ON per.id = cic.id_per";
		sql += "\n INNER JOIN cat_tip_periodo tpe ON per.id_tpe = tpe.id";
		sql += "\n INNER JOIN cat_grad g ON g.id = au.id_grad";
		sql += "\n INNER JOIN ges_servicio srv ON srv.id = per.id_srv";
		sql +="\n INNER JOIN ges_giro_negocio gir ON srv.id_gir=gir.id";
		sql += "\n INNER JOIN cat_nivel niv ON niv.id = srv.id_niv";
		sql += "\n INNER JOIN ges_sucursal suc ON suc.id = srv.id_suc";
		sql += "\n WHERE mat.id_sit='5' AND per.id_anio="+id_anio;
		if(id_suc!=null)
		sql += "\n AND suc.id="+id_suc;
		if(id_niv!=null)
			sql += "\n AND niv.id="+id_niv;
		if(id_gir!=null)
			sql += "\n AND gir.id="+id_gir;
		if(id_gra!=null)
			sql += "\n AND g.id="+id_gra;
		/*if(id_grad!=null)
		sql +="\n AND g.id="+id_grad;*/
		sql += "\n GROUP BY g.id, per.id_tpe, suc.id) tras ON tras.id_gra = cap.id_gra AND tras.nivel= cap.nivel AND tras.sucursal = cap.sucursal AND cap.id_tpe=tras.id_tpe ";
		/*sql += "\n LEFT JOIN (";
		sql += "\n SELECT gir.nom giro, suc.nom sucursal, niv.nom nivel,g.id id_gra, g.nom, tpe.id id_tpe, COUNT(1) total_traslados";
		sql += "\n FROM `mat_matricula` mat";
		sql += "\n INNER JOIN col_aula au ON au.id = mat.id_au_asi";
		sql += "\n INNER JOIN col_ciclo cic ON cic.id = au.id_cic";
		sql += "\n INNER JOIN per_periodo per ON per.id = cic.id_per";
		sql += "\n INNER JOIN cat_tip_periodo tpe ON per.id_tpe = tpe.id";
		sql += "\n INNER JOIN cat_grad g ON g.id = au.id_grad";
		sql += "\n INNER JOIN ges_servicio srv ON srv.id = per.id_srv";
		sql +="\n INNER JOIN ges_giro_negocio gir ON srv.id_gir=gir.id";
		sql += "\n INNER JOIN cat_nivel niv ON niv.id = srv.id_niv";
		sql += "\n INNER JOIN ges_sucursal suc ON suc.id = srv.id_suc";
		sql += "\n WHERE mat.id_sit='5' AND per.id_anio="+id_anio_ant;
		if(id_suc!=null)
		sql += "\n AND suc.id="+id_suc;
		if(id_niv!=null)
			sql += "\n AND niv.id="+id_niv;
		if(id_gir!=null)
			sql += "\n AND gir.id="+id_gir;
		if(id_gra!=null)
			sql += "\n AND g.id="+id_gra;
		/*if(id_grad!=null)
		sql +="\n AND g.id="+id_grad;*/
		//sql += "\n GROUP BY g.id, per.id_tpe, suc.id) tras ON tras.id_gra = cap.id_gra AND tras.nivel= cap.nivel AND tras.sucursal = cap.sucursal AND cap.id_tpe=tras.id_tpe ";
		sql += "\n LEFT JOIN (";
		sql += "\n SELECT gir.nom giro, suc.nom sucursal, niv.nom nivel,g.id id_gra, g.nom, tpe.id id_tpe, COUNT(sit.id ) total_fallecidos"; 
		sql += "\n FROM col_situacion_mat sit";
		sql += "\n INNER JOIN `mat_matricula` mat ON sit.id_mat=mat.id";
		sql += "\n INNER JOIN cat_col_situacion s ON sit.id_sit=s.id";
		sql += "\n INNER JOIN col_aula au ON au.id = mat.id_au_asi";
		sql += "\n INNER JOIN per_periodo per ON per.id = au.id_per";
		sql += "\n INNER JOIN cat_tip_periodo tpe ON per.id_tpe = tpe.id";
		sql += "\n INNER JOIN cat_grad g ON g.id = au.id_grad";
		sql += "\n INNER JOIN ges_servicio srv ON srv.id = per.id_srv";
		sql +="\n INNER JOIN ges_giro_negocio gir ON srv.id_gir=gir.id";
		sql += "\n INNER JOIN cat_nivel niv ON niv.id = srv.id_niv";
		sql += "\n INNER JOIN ges_sucursal suc ON suc.id = srv.id_suc";
		sql += "\n WHERE s.cod='F' AND per.id_anio="+id_anio;
		if(id_suc!=null)
		sql += "\n AND suc.id="+id_suc;
		if(id_niv!=null)
			sql += "\n AND niv.id="+id_niv;
		if(id_gir!=null)
			sql += "\n AND gir.id="+id_gir;
		if(id_gra!=null)
			sql += "\n AND g.id="+id_gra;
		/*if(id_grad!=null)
		sql +="\n AND g.id="+id_grad;*/
		sql += "\n GROUP BY g.id, per.id_tpe, suc.id) fa ON fa.id_gra = cap.id_gra AND fa.nivel= cap.nivel AND fa.sucursal = cap.sucursal AND cap.id_tpe=fa.id_tpe ";
		sql += "\n LEFT JOIN (";
		sql += "\n SELECT gir.nom giro, suc.nom sucursal, niv.nom nivel,g.id id_gra, g.nom ,  per.id_tpe , COUNT(mat.id ) total_retirados"; 
		sql += "\n FROM `mat_matricula` mat";
		//sql += "\n FROM col_situacion_mat sit";
		//sql += "\n INNER JOIN `mat_matricula` mat ON sit.id_mat=mat.id";
		//sql += "\n INNER JOIN cat_col_situacion s ON sit.id_sit=s.id";
		sql += "\n INNER JOIN col_aula au ON au.id = mat.id_au_asi";
		sql += "\n INNER JOIN per_periodo per ON per.id = au.id_per";
		sql += "\n INNER JOIN cat_grad g ON g.id = au.id_grad";
		sql += "\n INNER JOIN ges_servicio srv ON srv.id = per.id_srv";
		sql +="\n INNER JOIN ges_giro_negocio gir ON srv.id_gir=gir.id";
		sql += "\n INNER JOIN cat_nivel niv ON niv.id = srv.id_niv";
		sql += "\n INNER JOIN ges_sucursal suc ON suc.id = srv.id_suc";
		sql += "\n WHERE mat.id_sit=4 AND per.id_anio="+id_anio;
		if(id_suc!=null)
		sql += "\n AND suc.id="+id_suc;
		if(id_niv!=null)
			sql += "\n AND niv.id="+id_niv;
		if(id_gir!=null)
			sql += "\n AND gir.id="+id_gir;
		if(id_gra!=null)
			sql += "\n AND g.id="+id_gra;
		/*if(id_grad!=null)
		sql +="\n AND g.id="+id_grad;*/
		sql += "\n GROUP BY g.id, per.id_tpe, suc.id) re ON re.id_gra = cap.id_gra AND re.nivel= cap.nivel AND re.sucursal = cap.sucursal  AND cap.id_tpe=re.id_tpe " ;
		sql += "\n ORDER BY   cap.giro, cap.sucursal, cap.id_gra ASC ";
List<Map<String,Object>> reporteConsolidado = jdbcTemplate.queryForList(sql);

return reporteConsolidado;
		
	} else if(tipo.equals("C3")){
		String sql = "SELECT cap.giro, cap.sucursal, cap.nivel, cap.nom, cap.id_gra, cap.total_capacidad, IFNULL(eva_mat.total_matr_vacante,0) matriculas_vacante, IFNULL(mat.total_matriculados,0) matriculados, IFNULL(res.total_reserva,0) reservas, IFNULL(suge.total_sugeridos,0) sugeridos,";
		sql += "\n IFNULL(mat_ant.total_matr_ant,0) matriculas_ant, IFNULL(tras.total_traslados,0) trasladados, IFNULL(fa.total_no_rat,0) no_ratificados, IFNULL(fa.total_fallecidos,0) fallecidos, IFNULL(re.total_retirados,0) retirados, ";
		sql += "\n total_capacidad - IFNULL(mat.total_matriculados,0) - IFNULL(suge.total_sugeridos,0) - IFNULL(mat_ant.total_matr_ant,0) - IFNULL(res.total_reserva,0)+ IFNULL(rat.total_no_rat,0) + IFNULL(tras.total_traslados,0) + IFNULL(fa.total_fallecidos,0) + IFNULL(re.total_retirados,0) AS vacantes FROM";
		sql += "\n ( SELECT gir.nom giro, suc.nom sucursal, niv.nom nivel,id_grad id_gra, g.nom, tpe.id id_tpe, SUM(cap)  total_capacidad";
		sql += "\n FROM col_aula au";
		sql += "\n INNER JOIN cat_grad g ON g.id = au.id_grad";
		sql += "\n INNER JOIN col_ciclo cic ON cic.id=au.id_cic";
		//sql += "\n INNER JOIN per_periodo per ON per.id = au.id_per";
		sql += "\n INNER JOIN per_periodo per ON per.id = cic.id_per";
		sql += "\n INNER JOIN cat_tip_periodo tpe ON per.id_tpe = tpe.id";
		sql += "\n INNER JOIN ges_servicio srv ON srv.id = per.id_srv";
		sql +="\n INNER JOIN ges_giro_negocio gir ON srv.id_gir=gir.id";
		sql += "\n INNER JOIN cat_nivel niv ON niv.id = srv.id_niv";
		sql += "\n INNER JOIN ges_sucursal suc ON suc.id = srv.id_suc";
		sql += "\n WHERE per.id_anio="+id_anio;
		if(id_suc!=null)
			sql += "\n AND suc.id="+id_suc;
		if(id_niv!=null)
			sql += "\n AND niv.id="+id_niv;
		if(id_gir!=null)
			sql += "\n AND gir.id="+id_gir;
		if(id_gra!=null)
			sql += "\n AND g.id="+id_gra;
		/*if(id_grad!=null)
		sql +="\n AND g.id="+id_grad;*/
		sql += "\n GROUP BY g.id, per.id_tpe, suc.id ) cap ";
		sql += "\n LEFT JOIN (";
		sql += "\n SELECT gir.nom giro, suc.nom sucursal, niv.nom nivel, g.id id_gra, g.nom, 1 id_tpe, COUNT(eva_mat.id) total_matr_vacante";
		sql += "\n FROM `eva_matr_vacante` eva_mat ";
		sql += "\n INNER JOIN `eva_evaluacion_vac` eva ON `eva_mat`.`id_eva`=eva.`id` ";
		sql += "\n INNER JOIN cat_grad g ON g.id = eva_mat.id_gra";
		sql += "\n INNER JOIN per_periodo per ON per.id = eva.id_per";
		sql += "\n INNER JOIN cat_tip_periodo tpe ON per.id_tpe = tpe.id";
		sql += "\n INNER JOIN ges_servicio srv ON srv.id = per.id_srv";
		sql +="\n INNER JOIN ges_giro_negocio gir ON srv.id_gir=gir.id";
		sql += "\n INNER JOIN cat_nivel niv ON niv.id = srv.id_niv";
		sql += "\n INNER JOIN ges_sucursal suc ON suc.id = srv.id_suc";
		sql += "\n WHERE per.id_anio="+id_anio+" AND (eva_mat.res<>'N' OR eva_mat.`res` IS NULL) AND CURDATE()<=eva.fec_vig_vac AND `eva_mat`.`id_alu` NOT IN (SELECT res.id_alu FROM `mat_reserva` res INNER JOIN per_periodo per ON res.id_per=per.id AND per.id_anio="+id_anio+")";
		if(id_suc!=null)
		sql += "\n AND suc.id="+id_suc;
		if(id_niv!=null)
			sql += "\n AND niv.id="+id_niv;
		if(id_gir!=null)
			sql += "\n AND gir.id="+id_gir;
		if(id_gra!=null)
			sql += "\n AND g.id="+id_gra;
		/*if(id_grad!=null)
		sql +="\n AND g.id="+id_grad;*/
		sql += "\n GROUP BY g.id, per.id_tpe, suc.id";
		sql += "\n ) eva_mat ON eva_mat.id_gra = cap.id_gra AND eva_mat.nivel = cap.nivel AND eva_mat.sucursal = cap.sucursal AND cap.id_tpe=eva_mat.id_tpe ";
		sql += "\n LEFT JOIN (";
		sql += "\n SELECT gir.nom giro, suc.nom sucursal, ";
		sql += "\n (CASE WHEN g.id=1 THEN 'Inicial'  WHEN g.id=1 THEN 'Inicial'" ;
		sql += "\n 	WHEN g.id=2 THEN 'Inicial' ";
		sql += "\n 	WHEN g.id=3 THEN 'Primaria' ";
		sql += "\n 	WHEN g.id=4 THEN 'Primaria'"; 
		sql += "\n 	WHEN g.id=5 THEN 'Primaria'"; 
		sql += "\n 	WHEN g.id=6 THEN 'Primaria'"; 
		sql += "\n 	WHEN g.id=7 THEN 'Primaria'"; 
		sql += "\n 	WHEN g.id=8 THEN 'Primaria'"; 
		sql += "\n 	WHEN g.id=9 THEN 'Secundaria'"; 
		sql += "\n 	WHEN g.id=10 THEN 'Secundaria'"; 
		sql += "\n 	WHEN g.id=11 THEN 'Secundaria'"; 
		sql += "\n 	WHEN g.id=12 THEN 'Secundaria'"; 
		sql += "\n 	WHEN g.id=13 THEN 'Secundaria'"; 
		sql += "\n 	ELSE '' END ) nivel, g.id+1 id_gra,"; 
		sql += "\n 	 (CASE WHEN g.id+1=2 THEN '4 AÑOS'"; 
		sql += "\n 	 WHEN g.id+1=3 THEN '5 AÑOS'"; 
		sql += "\n 	 WHEN g.id+1=4 THEN 'PRIMERO'"; 
		sql += "\n 	 WHEN g.id+1=5 THEN 'SEGUNDO'"; 
		sql += "\n 	 WHEN g.id+1=6 THEN 'TERCERO'"; 
		sql += "\n 	 WHEN g.id+1=7 THEN 'CUARTO'"; 
		sql += "\n 	 WHEN g.id+1=8 THEN 'QUINTO'"; 
		sql += "\n 	 WHEN g.id+1=9 THEN 'SEXTO'"; 
		sql += "\n 	 WHEN g.id+1=10 THEN 'PRIMERO'"; 
		sql += "\n 	 WHEN g.id+1=11 THEN 'SEGUNDO' ";
		sql += "\n 	 WHEN g.id+1=12 THEN 'TERCERO' ";
		sql += "\n 	 WHEN g.id+1=13 THEN 'CUARTO' ";
		sql += "\n 	 WHEN g.id+1=14 THEN 'QUINTO'"; 
		sql += "\n 	 ELSE '' END) nom, tpe.id id_tpe, COUNT(rat.id) total_no_rat";
		sql += "\n FROM `mat_ratificacion` rat ";
		sql += "\n INNER JOIN `mat_matricula` mat ON `rat`.`id_mat`=mat.`id` ";
		sql += "\n INNER JOIN col_aula au ON mat.`id_au_asi`=au.id";				
		sql += "\n INNER JOIN cat_grad g ON g.id = au.id_grad";
		sql += "\n INNER JOIN col_ciclo cic ON cic.id = au.id_cic";
		sql += "\n INNER JOIN per_periodo per ON per.id = cic.id_per";
		sql += "\n INNER JOIN cat_tip_periodo tpe ON per.id_tpe = tpe.id";
		sql += "\n INNER JOIN ges_servicio srv ON srv.id = per.id_srv";
		sql +="\n INNER JOIN ges_giro_negocio gir ON srv.id_gir=gir.id";
		sql += "\n INNER JOIN cat_nivel niv ON niv.id = srv.id_niv";
		sql += "\n INNER JOIN ges_sucursal suc ON suc.id = srv.id_suc";
		sql += "\n WHERE rat.id_anio_rat="+id_anio+" AND per.`id_anio`="+id_anio_ant+" AND rat.res=0 ";
		if(id_suc!=null)
		sql += "\n AND suc.id="+id_suc;
		/*if(id_niv_ant!=null)
			sql += "\n AND niv.id="+id_niv_ant;*/
		if(id_gir!=null)
			sql += "\n AND gir.id="+id_gir;
		if(id_gra!=null) {
			Integer id_gra_ant=id_gra-1;
			sql += "\n AND g.id="+id_gra_ant;
		}
		/*if(id_grad!=null)
		sql +="\n AND g.id="+id_grad;*/
		sql += "\n GROUP BY g.id, per.id_tpe, suc.id";
		sql += "\n ) rat ON rat.id_gra = cap.id_gra AND rat.nivel = cap.nivel AND rat.sucursal = cap.sucursal AND cap.id_tpe=rat.id_tpe ";
		sql += "\n LEFT JOIN (";
		sql += "\n SELECT gir.nom giro, suc.nom sucursal, niv.nom nivel, g.id id_gra, g.nom, tpe.id id_tpe, COUNT(m.id) total_matriculados";
		sql += "\n FROM mat_matricula m";
		sql += "\n INNER JOIN cat_grad g ON g.id = m.id_gra";
		sql += "\n INNER JOIN col_aula au ON au.id = m.id_au_asi";
		sql += "\n INNER JOIN cat_nivel niv ON niv.id = g.id_nvl";
		sql += "\n INNER JOIN col_ciclo cic ON cic.id=au.id_cic";
		//sql += " INNER JOIN per_periodo per ON per.id = m.id_per";
		//sql += "\n INNER JOIN per_periodo per ON per.id = au.id_per";
		sql += "\n INNER JOIN per_periodo per ON per.id = cic.id_per";
		sql += "\n INNER JOIN cat_tip_periodo tpe ON per.id_tpe = tpe.id";
		sql += "\n INNER JOIN ges_servicio srv ON srv.id = per.id_srv";
		sql += "\n INNER JOIN ges_giro_negocio gir ON srv.id_gir=gir.id";
		sql += "\n INNER JOIN ges_sucursal suc ON suc.id = srv.id_suc";
		sql += "\n WHERE per.id_anio="+id_anio;
		if(id_suc!=null)
		sql += "\n AND suc.id="+id_suc;
		if(id_niv!=null)
			sql += "\n AND niv.id="+id_niv;
		if(id_gir!=null)
			sql += "\n AND gir.id="+id_gir;
		if(id_gra!=null)
			sql += "\n AND g.id="+id_gra;
		/*if(id_grad!=null)
		sql +="\n AND g.id="+id_grad;*/
		sql += "\n GROUP BY g.id, per.id_tpe, suc.id";
		sql += "\n ) mat ON mat.id_gra = cap.id_gra AND mat.nivel = cap.nivel AND mat.sucursal = cap.sucursal AND cap.id_tpe=mat.id_tpe ";
		//sql += "\n LEFT JOIN (";
		/*sql += "\n SELECT gir.nom giro, suc.nom sucursal, niv.nom nivel,g.id id_gra, g.nom, tpe.id id_tpe, COUNT(r.id ) total_reserva ";
		sql += "\n FROM mat_reserva r ";
		sql += "\n INNER JOIN per_periodo per ON per.id = r.id_per";
		sql += "\n INNER JOIN cat_tip_periodo tpe ON per.id_tpe = tpe.id";
		//sql += "\n INNER JOIN col_aula au ON au.id = r.id_au";
		sql += "\n INNER JOIN col_ciclo cic ON  cic.id_per=per.id";
		sql += "\n INNER JOIN cat_grad g ON g.id = r.id_gra";
		sql += "\n INNER JOIN ges_servicio srv ON srv.id = per.id_srv";
		sql += "\n INNER JOIN ges_giro_negocio gir ON srv.id_gir=gir.id";
		sql += "\n INNER JOIN cat_nivel niv ON niv.id = srv.id_niv";
		sql += "\n INNER JOIN ges_sucursal suc ON suc.id = srv.id_suc";
		sql += "\n WHERE CURDATE()<=r.fec_lim AND NOT EXISTS( SELECT m.id_alu FROM mat_matricula m INNER JOIN per_periodo p ON m.id_per=p.id WHERE m.id_alu = r.id_alu  AND m.id_gra=g.id AND p.id_tpe=per.id_tpe AND p.id_anio="+id_anio+")";
		sql += "\n AND per.id_anio="+id_anio;
		if(id_suc!=null)
		sql += "\n AND suc.id="+id_suc;
		if(id_niv!=null)
			sql += "\n AND niv.id="+id_niv;
		if(id_gir!=null)
			sql += "\n AND gir.id="+id_gir;
		if(id_gra!=null)
			sql += "\n AND g.id="+id_gra;
		/*if(id_grad!=null)
		sql +="\n AND g.id="+id_grad;*/
		//sql += "\n GROUP BY g.id, per.id_tpe, suc.id) res ON res.id_gra = cap.id_gra AND res.nivel= cap.nivel AND res.sucursal = cap.sucursal AND res.id_tpe=cap.id_tpe ";
		/*sql += "\n LEFT JOIN (";
		sql += "\n SELECT gir.nom giro, suc.nom sucursal, niv.nom nivel, m.id_gra, g.nom, tpe.id id_tpe, COUNT(m.id) total_mat_ant";
		sql += "\n FROM mat_matricula m";
		sql += "\n INNER JOIN cat_grad g ON g.id = m.id_gra";
       			sql += "\n INNER JOIN col_aula au ON au.id = m.id_au_asi";
		sql += "\n INNER JOIN cat_nivel niv ON niv.id = g.id_nvl";
		//sql += " INNER JOIN per_periodo per ON per.id = m.id_per";
		sql += "\n INNER JOIN col_ciclo cic ON cic.id = au.id_cic";
		sql += "\n INNER JOIN per_periodo per ON per.id = cic.id_per";
		sql += "\n INNER JOIN cat_tip_periodo tpe ON per.id_tpe = tpe.id";
		sql += "\n INNER JOIN ges_servicio srv ON srv.id = per.id_srv";
		sql +="\n INNER JOIN ges_giro_negocio gir ON srv.id_gir=gir.id";
		sql += "\n INNER JOIN ges_sucursal suc ON suc.id = srv.id_suc";
		sql += "\n WHERE per.id_anio="+id_anio_ant;
		if(id_suc!=null)
		sql += "\n AND suc.id="+id_suc;
		if(id_niv!=null)
			sql += "\n AND niv.id="+id_niv;
		if(id_gir!=null)
			sql += "\n AND gir.id="+id_gir;
		if(id_gra!=null)
			sql += "\n AND g.id="+id_gra;
		/*if(id_grad!=null)
		sql +="\n AND g.id="+id_grad;*/
		/*sql += "\n GROUP BY g.id, per.id_tpe, suc.id";
		sql += "\n ) mat ON mat.id_gra = cap.id_gra AND mat.nivel = cap.nivel AND mat.sucursal = cap.sucursal AND cap.id_tpe=mat.id_tpe ";*/
		/*sql += "\n LEFT JOIN(";
		sql += "\n SELECT gir.nom giro, suc.nom sucursal, niv.nom nivel, g.id id_gra, g.nom, tpe.id id_tpe, COUNT(m.id) total_matriculados";
		sql += "\n FROM mat_matricula m";
		sql += "\n INNER JOIN cat_grad g ON g.id = m.id_gra";
		sql += "\n INNER JOIN col_aula au ON au.id = m.id_au_asi";
		sql += "\n INNER JOIN cat_nivel niv ON niv.id = g.id_nvl";
		sql += "\n INNER JOIN col_ciclo cic ON cic.id=au.id_cic";
		//sql += " INNER JOIN per_periodo per ON per.id = m.id_per";
		//sql += "\n INNER JOIN per_periodo per ON per.id = au.id_per";
		sql += "\n INNER JOIN per_periodo per ON per.id = cic.id_per";
		sql += "\n INNER JOIN cat_tip_periodo tpe ON per.id_tpe = tpe.id";
		sql += "\n INNER JOIN ges_servicio srv ON srv.id = per.id_srv";
		sql += "\n INNER JOIN ges_giro_negocio gir ON srv.id_gir=gir.id";
		sql += "\n INNER JOIN ges_sucursal suc ON suc.id = srv.id_suc";
		sql += "\n WHERE per.id_anio="+id_anio;
		if(id_suc!=null)
		sql += "\n AND suc.id="+id_suc;
		if(id_niv!=null)
			sql += "\n AND niv.id="+id_niv;
		if(id_gir!=null)
			sql += "\n AND gir.id="+id_gir;
		if(id_gra!=null)
			sql += "\n AND g.id="+id_gra;
		/*if(id_grad!=null)
		sql +="\n AND g.id="+id_grad;*/
		/*sql += "\n GROUP BY g.id, per.id_tpe, suc.id";
		sql += "\n ) mat ON mat.id_gra = cap.id_gra AND mat.nivel = cap.nivel AND mat.sucursal = cap.sucursal AND cap.id_tpe=mat.id_tpe ";*/
		sql += "\n LEFT JOIN(";
		sql += "\n SELECT gir.nom giro, suc.nom sucursal, niv.nom nivel, g.id id_gra, g.nom,tpe.id id_tpe, COUNT(sug.id) total_sugeridos";
		sql += "\n FROM mat_seccion_sugerida sug";
		sql += "\n INNER JOIN mat_matricula m ON sug.id_mat=m.id and m.tipo='C' ";
		//sql += "\n INNER JOIN col_aula au ON au.id = sug.id_au_nue";
		sql += "\n INNER JOIN `alu_alumno` alu ON m.id_alu=alu.id"; 
		sql	+= "\n INNER JOIN cat_grad g ON g.id = sug.id_gra_nue";
		sql	+= "\n INNER JOIN cat_nivel niv ON niv.id = g.id_nvl";
		//sql += "\n INNER JOIN col_ciclo cic ON cic.id = au.id_cic";
		sql += "\n INNER JOIN per_periodo per ON per.id_anio = sug.id_anio and per.id_suc=sug.id_suc_nue and per.id_tpe=1 ";
		sql += "\n INNER JOIN cat_tip_periodo tpe ON per.id_tpe = tpe.id";
		sql += "\n INNER JOIN ges_servicio srv ON srv.id = per.id_srv";
		sql +="\n INNER JOIN ges_giro_negocio gir ON srv.id_gir=gir.id";
		sql += "\n INNER JOIN ges_sucursal suc ON suc.id = srv.id_suc";
		sql	+= "\n WHERE per.id_anio="+id_anio+" AND (m.id_sit NOT IN (5,4) OR m.id_sit IS NULL)";
		sql += "\n AND ( m.id_alu NOT IN (SELECT mat.`id_alu` FROM `mat_matricula` mat INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.`id` INNER JOIN col_ciclo cic ON au.id_cic=cic.id INNER JOIN `per_periodo` per ON cic.`id_per`=per.`id` WHERE per.id_tpe=1 AND per.`id_anio`="+id_anio+"))";
		sql += "\n AND"; 
		sql += "\n (m.`id_alu` NOT IN (SELECT res.id_alu FROM `mat_reserva` res INNER JOIN `per_periodo` per ON res.`id_per`=per.`id` WHERE per.`id_anio`="+id_anio+"))";
		if (id_suc != null)
			sql += "\n AND suc.id=" + id_suc;
		if(id_niv!=null)
			sql += "\n AND niv.id="+id_niv;
		if(id_gir!=null)
			sql += "\n AND gir.id="+id_gir;
		if(id_gra!=null)
			sql += "\n AND g.id="+id_gra;
		/*if (id_grad != null)
			sql += "\n AND g.id=" + id_grad;*/
		sql += "\n GROUP BY g.id, per.id_tpe, suc.id";
		sql += "\n ) suge ON suge.id_gra = cap.id_gra AND suge.nivel = cap.nivel AND suge.sucursal = cap.sucursal AND cap.id_tpe=suge.id_tpe ";
		sql += "\n LEFT JOIN (";
		sql += "\n SELECT gir.nom giro, suc.nom sucursal, niv.nom nivel,g.id id_gra, g.nom, tpe.id id_tpe, COUNT(r.id ) total_reserva ";
		sql += "\n FROM mat_reserva r ";
		sql += "\n INNER JOIN per_periodo per ON per.id = r.id_per";
		sql += "\n INNER JOIN cat_tip_periodo tpe ON per.id_tpe = tpe.id";
		//sql += "\n INNER JOIN col_aula au ON au.id = r.id_au";
		sql += "\n INNER JOIN cat_grad g ON g.id = r.id_gra";
		sql += "\n INNER JOIN ges_servicio srv ON srv.id = per.id_srv";
		sql +="\n INNER JOIN ges_giro_negocio gir ON srv.id_gir=gir.id";
		sql += "\n INNER JOIN cat_nivel niv ON niv.id = srv.id_niv";
		sql += "\n INNER JOIN ges_sucursal suc ON suc.id = srv.id_suc";
		sql += "\n WHERE CURDATE()<=r.fec_lim AND NOT EXISTS( SELECT m.id_alu FROM mat_matricula m INNER JOIN per_periodo p ON m.id_per=p.id WHERE m.id_alu = r.id_alu  AND m.id_gra=g.id AND p.id_tpe=1 AND p.id_anio="+id_anio+")";
		sql += "\n AND per.id_anio="+id_anio;
		if(id_suc!=null)
		sql += "\n AND suc.id="+id_suc;
		if(id_niv!=null)
			sql += "\n AND niv.id="+id_niv;
		if(id_gir!=null)
			sql += "\n AND gir.id="+id_gir;
		if(id_gra!=null)
			sql += "\n AND g.id="+id_gra;
		/*if(id_grad!=null)
		sql +="\n AND g.id="+id_grad;*/
		sql += "\n GROUP BY g.id, per.id_tpe, suc.id) res ON res.id_gra = cap.id_gra AND res.nivel= cap.nivel AND res.sucursal = cap.sucursal AND cap.id_tpe=res.id_tpe ";
		sql += "\n LEFT JOIN (";
		sql += "\n SELECT gir.nom giro, suc.nom sucursal, niv.nom nivel,g.id id_gra, g.nom, tpe.id id_tpe, COUNT(1) total_traslados";
		sql += "\n FROM `mat_matricula` mat";
		sql += "\n INNER JOIN col_aula au ON au.id = mat.id_au_asi";
		sql += "\n INNER JOIN col_ciclo cic ON cic.id = au.id_cic";
		sql += "\n INNER JOIN per_periodo per ON per.id = cic.id_per";
		sql += "\n INNER JOIN cat_tip_periodo tpe ON per.id_tpe = tpe.id";
		sql += "\n INNER JOIN cat_grad g ON g.id = au.id_grad";
		sql += "\n INNER JOIN ges_servicio srv ON srv.id = per.id_srv";
		sql +="\n INNER JOIN ges_giro_negocio gir ON srv.id_gir=gir.id";
		sql += "\n INNER JOIN cat_nivel niv ON niv.id = srv.id_niv";
		sql += "\n INNER JOIN ges_sucursal suc ON suc.id = srv.id_suc";
		sql += "\n WHERE mat.id_sit='5' AND per.id_anio="+id_anio;
		if(id_suc!=null)
		sql += "\n AND suc.id="+id_suc;
		if(id_niv!=null)
			sql += "\n AND niv.id="+id_niv;
		if(id_gir!=null)
			sql += "\n AND gir.id="+id_gir;
		if(id_gra!=null)
			sql += "\n AND g.id="+id_gra;
		/*if(id_grad!=null)
		sql +="\n AND g.id="+id_grad;*/
		sql += "\n GROUP BY g.id, per.id_tpe, suc.id) tras ON tras.id_gra = cap.id_gra AND tras.nivel= cap.nivel AND tras.sucursal = cap.sucursal AND cap.id_tpe=tras.id_tpe ";
		/*sql += "\n LEFT JOIN (";
		sql += "\n SELECT gir.nom giro, suc.nom sucursal, niv.nom nivel,g.id id_gra, g.nom, tpe.id id_tpe, COUNT(1) total_traslados";
		sql += "\n FROM `mat_matricula` mat";
		sql += "\n INNER JOIN col_aula au ON au.id = mat.id_au_asi";
		sql += "\n INNER JOIN col_ciclo cic ON cic.id = au.id_cic";
		sql += "\n INNER JOIN per_periodo per ON per.id = cic.id_per";
		sql += "\n INNER JOIN cat_tip_periodo tpe ON per.id_tpe = tpe.id";
		sql += "\n INNER JOIN cat_grad g ON g.id = au.id_grad";
		sql += "\n INNER JOIN ges_servicio srv ON srv.id = per.id_srv";
		sql +="\n INNER JOIN ges_giro_negocio gir ON srv.id_gir=gir.id";
		sql += "\n INNER JOIN cat_nivel niv ON niv.id = srv.id_niv";
		sql += "\n INNER JOIN ges_sucursal suc ON suc.id = srv.id_suc";
		sql += "\n WHERE mat.id_sit='5' AND per.id_anio="+id_anio_ant;
		if(id_suc!=null)
		sql += "\n AND suc.id="+id_suc;
		if(id_niv!=null)
			sql += "\n AND niv.id="+id_niv;
		if(id_gir!=null)
			sql += "\n AND gir.id="+id_gir;
		if(id_gra!=null)
			sql += "\n AND g.id="+id_gra;
		/*if(id_grad!=null)
		sql +="\n AND g.id="+id_grad;*/
		//sql += "\n GROUP BY g.id, per.id_tpe, suc.id) tras ON tras.id_gra = cap.id_gra AND tras.nivel= cap.nivel AND tras.sucursal = cap.sucursal AND cap.id_tpe=tras.id_tpe ";
		sql += "\n LEFT JOIN (";
		sql += "\n SELECT gir.nom giro, suc.nom sucursal, niv.nom nivel,g.id id_gra, g.nom, tpe.id id_tpe, COUNT(sit.id ) total_fallecidos"; 
		sql += "\n FROM col_situacion_mat sit";
		sql += "\n INNER JOIN `mat_matricula` mat ON sit.id_mat=mat.id";
		sql += "\n INNER JOIN cat_col_situacion s ON sit.id_sit=s.id";
		sql += "\n INNER JOIN col_aula au ON au.id = mat.id_au_asi";
		sql += "\n INNER JOIN per_periodo per ON per.id = au.id_per";
		sql += "\n INNER JOIN cat_tip_periodo tpe ON per.id_tpe = tpe.id";
		sql += "\n INNER JOIN cat_grad g ON g.id = au.id_grad";
		sql += "\n INNER JOIN ges_servicio srv ON srv.id = per.id_srv";
		sql +="\n INNER JOIN ges_giro_negocio gir ON srv.id_gir=gir.id";
		sql += "\n INNER JOIN cat_nivel niv ON niv.id = srv.id_niv";
		sql += "\n INNER JOIN ges_sucursal suc ON suc.id = srv.id_suc";
		sql += "\n WHERE s.cod='F' AND per.id_anio="+id_anio;
		if(id_suc!=null)
		sql += "\n AND suc.id="+id_suc;
		if(id_niv!=null)
			sql += "\n AND niv.id="+id_niv;
		if(id_gir!=null)
			sql += "\n AND gir.id="+id_gir;
		if(id_gra!=null)
			sql += "\n AND g.id="+id_gra;
		/*if(id_grad!=null)
		sql +="\n AND g.id="+id_grad;*/
		sql += "\n GROUP BY g.id, per.id_tpe, suc.id) fa ON fa.id_gra = cap.id_gra AND fa.nivel= cap.nivel AND fa.sucursal = cap.sucursal AND cap.id_tpe=fa.id_tpe ";
		sql += "\n LEFT JOIN (";
		sql += "\n SELECT gir.nom giro, suc.nom sucursal, niv.nom nivel,g.id id_gra, g.nom ,  per.id_tpe , COUNT(mat.id ) total_retirados"; 
		sql += "\n FROM `mat_matricula` mat";
		//sql += "\n FROM col_situacion_mat sit";
		//sql += "\n INNER JOIN `mat_matricula` mat ON sit.id_mat=mat.id";
		//sql += "\n INNER JOIN cat_col_situacion s ON sit.id_sit=s.id";
		sql += "\n INNER JOIN col_aula au ON au.id = mat.id_au_asi";
		sql += "\n INNER JOIN per_periodo per ON per.id = au.id_per";
		sql += "\n INNER JOIN cat_grad g ON g.id = au.id_grad";
		sql += "\n INNER JOIN ges_servicio srv ON srv.id = per.id_srv";
		sql +="\n INNER JOIN ges_giro_negocio gir ON srv.id_gir=gir.id";
		sql += "\n INNER JOIN cat_nivel niv ON niv.id = srv.id_niv";
		sql += "\n INNER JOIN ges_sucursal suc ON suc.id = srv.id_suc";
		sql += "\n WHERE mat.id_sit=4 AND per.id_anio="+id_anio;
		if(id_suc!=null)
		sql += "\n AND suc.id="+id_suc;
		if(id_niv!=null)
			sql += "\n AND niv.id="+id_niv;
		if(id_gir!=null)
			sql += "\n AND gir.id="+id_gir;
		if(id_gra!=null)
			sql += "\n AND g.id="+id_gra;
		/*if(id_grad!=null)
		sql +="\n AND g.id="+id_grad;*/
		sql += "\n GROUP BY g.id, per.id_tpe, suc.id) re ON re.id_gra = cap.id_gra AND re.nivel= cap.nivel AND re.sucursal = cap.sucursal  AND cap.id_tpe=re.id_tpe " ;
		sql += "\n ORDER BY   cap.giro, cap.sucursal, cap.id_gra ASC ";
List<Map<String,Object>> reporteConsolidado = jdbcTemplate.queryForList(sql);

return reporteConsolidado;
	} else if(tipo.equals("C4")){
		String sql = "SELECT cap.giro, cap.sucursal, cap.nivel, cap.nom, cap.id_gra, cap.total_capacidad,IFNULL(eva_mat.total_matr_vacante,0) matriculas_vacante,  IFNULL(mat.total_matriculados,0) matriculados, IFNULL(res.total_reserva,0) reservas, IFNULL(suge.total_sugeridos,0) sugeridos,";
		sql += "\n IFNULL(mat_ant.total_matr_ant,0) matriculas_ant, IFNULL(tras.total_traslados,0) trasladados, IFNULL(rat.total_no_rat,0) no_ratificados, IFNULL(fa.total_fallecidos,0) fallecidos, IFNULL(re.total_retirados,0) retirados, ";
		sql += "\n total_capacidad - IFNULL(eva_mat.total_matr_vacante,0) - IFNULL(mat.total_matriculados,0) - IFNULL(mat_ant.total_matr_ant,0) - IFNULL(res.total_reserva,0) -IFNULL(suge.total_sugeridos,0) + IFNULL(rat.total_no_rat,0) + IFNULL(tras.total_traslados,0) + IFNULL(fa.total_fallecidos,0) + IFNULL(re.total_retirados,0) AS vacantes FROM";
		sql += "\n ( SELECT gir.nom giro, suc.nom sucursal, niv.nom nivel,id_grad id_gra, g.nom, tpe.id id_tpe, SUM(cap)  total_capacidad";
		sql += "\n FROM col_aula au";
		sql += "\n INNER JOIN cat_grad g ON g.id = au.id_grad";
		sql += "\n INNER JOIN col_ciclo cic ON cic.id=au.id_cic";
		//sql += "\n INNER JOIN per_periodo per ON per.id = au.id_per";
		sql += "\n INNER JOIN per_periodo per ON per.id = cic.id_per";
		sql += "\n INNER JOIN cat_tip_periodo tpe ON per.id_tpe = tpe.id";
		sql += "\n INNER JOIN ges_servicio srv ON srv.id = per.id_srv";
		sql +="\n INNER JOIN ges_giro_negocio gir ON srv.id_gir=gir.id";
		sql += "\n INNER JOIN cat_nivel niv ON niv.id = srv.id_niv";
		sql += "\n INNER JOIN ges_sucursal suc ON suc.id = srv.id_suc";
		sql += "\n WHERE per.id_anio="+id_anio;
		if(id_suc!=null)
			sql += "\n AND suc.id="+id_suc;
		if(id_niv!=null)
			sql += "\n AND niv.id="+id_niv;
		if(id_gir!=null)
			sql += "\n AND gir.id="+id_gir;
		if(id_gra!=null)
			sql += "\n AND g.id="+id_gra;
		/*if(id_grad!=null)
		sql +="\n AND g.id="+id_grad;*/
		sql += "\n GROUP BY g.id, per.id_tpe, suc.id ) cap ";
		sql += "\n LEFT JOIN (";
		sql += "\n SELECT gir.nom giro, suc.nom sucursal, niv.nom nivel, g.id id_gra, g.nom, 1 id_tpe, COUNT(eva_mat.id) total_matr_vacante";
		sql += "\n FROM `eva_matr_vacante` eva_mat ";
		sql += "\n INNER JOIN `eva_evaluacion_vac` eva ON `eva_mat`.`id_eva`=eva.`id` ";
		sql += "\n INNER JOIN cat_grad g ON g.id = eva_mat.id_gra";
		sql += "\n INNER JOIN per_periodo per ON per.id = eva.id_per";
		sql += "\n INNER JOIN cat_tip_periodo tpe ON per.id_tpe = tpe.id";
		sql += "\n INNER JOIN ges_servicio srv ON srv.id = per.id_srv";
		sql +="\n INNER JOIN ges_giro_negocio gir ON srv.id_gir=gir.id";
		sql += "\n INNER JOIN cat_nivel niv ON niv.id = srv.id_niv";
		sql += "\n INNER JOIN ges_sucursal suc ON suc.id = srv.id_suc";
		sql += "\n WHERE per.id_anio="+id_anio+" AND (eva_mat.res<>'N' OR eva_mat.`res` IS NULL) AND CURDATE()<=eva.fec_vig_vac AND `eva_mat`.`id_alu` NOT IN (SELECT res.id_alu FROM `mat_reserva` res INNER JOIN per_periodo per ON res.id_per=per.id AND per.id_anio="+id_anio+")";
		if(id_suc!=null)
		sql += "\n AND suc.id="+id_suc;
		if(id_niv!=null)
			sql += "\n AND niv.id="+id_niv;
		if(id_gir!=null)
			sql += "\n AND gir.id="+id_gir;
		if(id_gra!=null)
			sql += "\n AND g.id="+id_gra;
		/*if(id_grad!=null)
		sql +="\n AND g.id="+id_grad;*/
		sql += "\n GROUP BY g.id, per.id_tpe, suc.id";
		sql += "\n ) eva_mat ON eva_mat.id_gra = cap.id_gra AND eva_mat.nivel = cap.nivel AND eva_mat.sucursal = cap.sucursal AND cap.id_tpe=eva_mat.id_tpe ";
		sql += "\n LEFT JOIN (";
		sql += "\n SELECT gir.nom giro, suc.nom sucursal, ";
		sql += "\n (CASE WHEN g.id=1 THEN 'Inicial'  WHEN g.id=1 THEN 'Inicial'" ;
		sql += "\n 	WHEN g.id=2 THEN 'Inicial' ";
		sql += "\n 	WHEN g.id=3 THEN 'Primaria' ";
		sql += "\n 	WHEN g.id=4 THEN 'Primaria'"; 
		sql += "\n 	WHEN g.id=5 THEN 'Primaria'"; 
		sql += "\n 	WHEN g.id=6 THEN 'Primaria'"; 
		sql += "\n 	WHEN g.id=7 THEN 'Primaria'"; 
		sql += "\n 	WHEN g.id=8 THEN 'Primaria'"; 
		sql += "\n 	WHEN g.id=9 THEN 'Secundaria'"; 
		sql += "\n 	WHEN g.id=10 THEN 'Secundaria'"; 
		sql += "\n 	WHEN g.id=11 THEN 'Secundaria'"; 
		sql += "\n 	WHEN g.id=12 THEN 'Secundaria'"; 
		sql += "\n 	WHEN g.id=13 THEN 'Secundaria'"; 
		sql += "\n 	ELSE '' END ) nivel, g.id+1 id_gra,"; 
		sql += "\n 	 (CASE WHEN g.id+1=2 THEN '4 AÑOS'"; 
		sql += "\n 	 WHEN g.id+1=3 THEN '5 AÑOS'"; 
		sql += "\n 	 WHEN g.id+1=4 THEN 'PRIMERO'"; 
		sql += "\n 	 WHEN g.id+1=5 THEN 'SEGUNDO'"; 
		sql += "\n 	 WHEN g.id+1=6 THEN 'TERCERO'"; 
		sql += "\n 	 WHEN g.id+1=7 THEN 'CUARTO'"; 
		sql += "\n 	 WHEN g.id+1=8 THEN 'QUINTO'"; 
		sql += "\n 	 WHEN g.id+1=9 THEN 'SEXTO'"; 
		sql += "\n 	 WHEN g.id+1=10 THEN 'PRIMERO'"; 
		sql += "\n 	 WHEN g.id+1=11 THEN 'SEGUNDO' ";
		sql += "\n 	 WHEN g.id+1=12 THEN 'TERCERO' ";
		sql += "\n 	 WHEN g.id+1=13 THEN 'CUARTO' ";
		sql += "\n 	 WHEN g.id+1=14 THEN 'QUINTO'"; 
		sql += "\n 	 ELSE '' END) nom, tpe.id id_tpe, COUNT(rat.id) total_no_rat";
		sql += "\n FROM `mat_ratificacion` rat ";
		sql += "\n INNER JOIN `mat_matricula` mat ON `rat`.`id_mat`=mat.`id` ";
		sql += "\n INNER JOIN col_aula au ON mat.`id_au_asi`=au.id";				
		sql += "\n INNER JOIN cat_grad g ON g.id = au.id_grad";
		sql += "\n INNER JOIN col_ciclo cic ON cic.id = au.id_cic";
		sql += "\n INNER JOIN per_periodo per ON per.id = cic.id_per";
		sql += "\n INNER JOIN cat_tip_periodo tpe ON per.id_tpe = tpe.id";
		sql += "\n INNER JOIN ges_servicio srv ON srv.id = per.id_srv";
		sql +="\n INNER JOIN ges_giro_negocio gir ON srv.id_gir=gir.id";
		sql += "\n INNER JOIN cat_nivel niv ON niv.id = srv.id_niv";
		sql += "\n INNER JOIN ges_sucursal suc ON suc.id = srv.id_suc";
		sql += "\n WHERE rat.id_anio_rat="+id_anio+" AND per.`id_anio`="+id_anio_ant+" AND rat.res=0 ";
		if(id_suc!=null)
		sql += "\n AND suc.id="+id_suc;
		/*if(id_niv_ant!=null)
			sql += "\n AND niv.id="+id_niv_ant;*/
		if(id_gir!=null)
			sql += "\n AND gir.id="+id_gir;
		if(id_gra!=null) {
			
			sql += "\n AND g.id+1="+id_gra;
		}
		/*if(id_grad!=null)
		sql +="\n AND g.id="+id_grad;*/
		sql += "\n GROUP BY g.id, per.id_tpe, suc.id";
		sql += "\n ) rat ON rat.id_gra = cap.id_gra AND rat.nivel = cap.nivel AND rat.sucursal = cap.sucursal AND cap.id_tpe=rat.id_tpe ";
		sql += "\n LEFT JOIN (";
		sql += "\n SELECT gir.nom giro, suc.nom sucursal, niv_sig.nom nivel,gra_sig.id id_gra, gra_sig.nom , per.id_tpe, COUNT(mat.id ) total_matr_ant";
		sql += "\n FROM `mat_matricula` mat INNER JOIN col_aula au ON mat.`id_au_asi`=au.id";
		sql += "\n INNER JOIN `cat_grad` gra ON au.`id_grad`=gra.`id`"; 
		sql += "\n INNER JOIN cat_nivel niv ON gra.id_nvl=niv.id";
		sql += "\n INNER JOIN col_ciclo cic ON au.id_cic=cic.id"; 
		sql += "\n INNER JOIN `per_periodo` per ON cic.`id_per`=per.`id`";
		sql += "\n INNER JOIN `ges_servicio` srv ON per.id_srv=srv.id";
		sql += "\n INNER JOIN ges_sucursal suc ON per.id_suc=suc.id";
		sql += "\n INNER JOIN ges_giro_negocio gir ON srv.id_gir=gir.id";
		sql += "\n INNER JOIN cat_grad gra_sig ON gra_sig.`id`=gra.id+1";
		sql += "\n INNER JOIN cat_nivel niv_sig ON gra_sig.`id_nvl`=niv_sig.id";
		sql += "\n WHERE per.`id_anio`="+id_anio_ant+"  AND per.id_tpe=1";
		sql += "\n AND (mat.id_sit NOT IN (5,4) OR mat.id_sit IS NULL)"; 
		sql += "\n AND mat.id_alu NOT IN (SELECT id_alu FROM mat_matricula mat_act INNER JOIN per_periodo peri ON mat_act.id_per=peri.id WHERE peri.id_tpe='1' AND peri.id_anio="+id_anio+")";
		sql += "\n AND mat.id NOT IN (SELECT id_mat FROM mat_seccion_sugerida sug WHERE sug.id_mat=mat.id)";
		sql += "\n GROUP BY gra.id, per.id_tpe, per.id_suc";
		sql += "\n ) mat_ant ON mat_ant.id_gra = cap.id_gra AND mat_ant.nivel = cap.nivel AND mat_ant.sucursal = cap.sucursal AND cap.id_tpe=mat_ant.id_tpe ";
		sql += "\n LEFT JOIN (";
		sql += "\n SELECT gir.nom giro, suc.nom sucursal, niv.nom nivel, g.id id_gra, g.nom, tpe.id id_tpe, COUNT(m.id) total_matriculados";
		sql += "\n FROM mat_matricula m";
		sql += "\n INNER JOIN cat_grad g ON g.id = m.id_gra";
		sql += "\n INNER JOIN col_aula au ON au.id = m.id_au_asi";
		sql += "\n INNER JOIN cat_nivel niv ON niv.id = g.id_nvl";
		sql += "\n INNER JOIN col_ciclo cic ON cic.id=au.id_cic";
		//sql += " INNER JOIN per_periodo per ON per.id = m.id_per";
		//sql += "\n INNER JOIN per_periodo per ON per.id = au.id_per";
		sql += "\n INNER JOIN per_periodo per ON per.id = cic.id_per";
		sql += "\n INNER JOIN cat_tip_periodo tpe ON per.id_tpe = tpe.id";
		sql += "\n INNER JOIN ges_servicio srv ON srv.id = per.id_srv";
		sql += "\n INNER JOIN ges_giro_negocio gir ON srv.id_gir=gir.id";
		sql += "\n INNER JOIN ges_sucursal suc ON suc.id = srv.id_suc";
		sql += "\n WHERE per.id_anio="+id_anio;
		if(id_suc!=null)
		sql += "\n AND suc.id="+id_suc;
		if(id_niv!=null)
			sql += "\n AND niv.id="+id_niv;
		if(id_gir!=null)
			sql += "\n AND gir.id="+id_gir;
		if(id_gra!=null)
			sql += "\n AND g.id="+id_gra;
		/*if(id_grad!=null)
		sql +="\n AND g.id="+id_grad;*/
		sql += "\n GROUP BY g.id, per.id_tpe, suc.id";
		sql += "\n ) mat ON mat.id_gra = cap.id_gra AND mat.nivel = cap.nivel AND mat.sucursal = cap.sucursal AND cap.id_tpe=mat.id_tpe ";
		sql += "\n LEFT JOIN(";
		sql += "\n SELECT gir.nom giro, suc.nom sucursal, niv.nom nivel, g.id id_gra, g.nom,tpe.id id_tpe, COUNT(sug.id) total_sugeridos";
		sql += "\n FROM mat_seccion_sugerida sug";
		sql += "\n INNER JOIN mat_matricula m ON sug.id_mat=m.id and m.tipo='C' ";
		//sql += "\n INNER JOIN col_aula au ON au.id = sug.id_au_nue";
		sql += "\n INNER JOIN `alu_alumno` alu ON m.id_alu=alu.id"; 
		sql	+= "\n INNER JOIN cat_grad g ON g.id = sug.id_gra_nue";
		sql	+= "\n INNER JOIN cat_nivel niv ON niv.id = g.id_nvl";
		//sql += "\n INNER JOIN col_ciclo cic ON cic.id = au.id_cic";
		sql += "\n INNER JOIN per_periodo per ON per.id_anio = sug.id_anio and per.id_suc=sug.id_suc_nue and per.id_tpe=1 and per.id_niv=niv.id ";
		sql += "\n INNER JOIN cat_tip_periodo tpe ON per.id_tpe = tpe.id";
		sql += "\n INNER JOIN ges_servicio srv ON srv.id = per.id_srv";
		sql +="\n INNER JOIN ges_giro_negocio gir ON srv.id_gir=gir.id";
		sql += "\n INNER JOIN ges_sucursal suc ON suc.id = srv.id_suc";
		sql	+= "\n WHERE per.id_anio="+id_anio+" AND (m.id_sit NOT IN (5,4) OR m.id_sit IS NULL)";
		sql += "\n AND ( m.id_alu NOT IN (SELECT mat.`id_alu` FROM `mat_matricula` mat INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.`id` INNER JOIN col_ciclo cic ON au.id_cic=cic.id INNER JOIN `per_periodo` per ON cic.`id_per`=per.`id` WHERE per.id_tpe=1 AND per.`id_anio`="+id_anio+"))";
		sql += "\n AND"; 
		sql += "\n (m.`id_alu` NOT IN (SELECT res.id_alu FROM `mat_reserva` res INNER JOIN `per_periodo` per ON res.`id_per`=per.`id` WHERE per.`id_anio`="+id_anio+"))";
		if (id_suc != null)
			sql += "\n AND suc.id=" + id_suc;
		if(id_niv!=null)
			sql += "\n AND niv.id="+id_niv;
		if(id_gir!=null)
			sql += "\n AND gir.id="+id_gir;
		if(id_gra!=null)
			sql += "\n AND g.id="+id_gra;
		/*if (id_grad != null)
			sql += "\n AND g.id=" + id_grad;*/
		sql += "\n GROUP BY g.id, per.id_tpe, suc.id";
		sql += "\n ) suge ON suge.id_gra = cap.id_gra AND suge.nivel = cap.nivel AND suge.sucursal = cap.sucursal AND cap.id_tpe=suge.id_tpe ";
		sql += "\n LEFT JOIN (";
		sql += "\n SELECT gir.nom giro, suc.nom sucursal, niv.nom nivel,g.id id_gra, g.nom, tpe.id id_tpe, COUNT(r.id ) total_reserva ";
		sql += "\n FROM mat_reserva r ";
		sql += "\n INNER JOIN per_periodo per ON per.id = r.id_per";
		sql += "\n INNER JOIN cat_tip_periodo tpe ON per.id_tpe = tpe.id";
		//sql += "\n INNER JOIN col_aula au ON au.id = r.id_au";
		sql += "\n INNER JOIN col_ciclo cic ON  cic.id_per=per.id";
		sql += "\n INNER JOIN cat_grad g ON g.id = r.id_gra";
		sql += "\n INNER JOIN ges_servicio srv ON srv.id = per.id_srv";
		sql += "\n INNER JOIN ges_giro_negocio gir ON srv.id_gir=gir.id";
		sql += "\n INNER JOIN cat_nivel niv ON niv.id = srv.id_niv";
		sql += "\n INNER JOIN ges_sucursal suc ON suc.id = srv.id_suc";
		sql += "\n WHERE CURDATE()<=r.fec_lim AND NOT EXISTS( SELECT m.id_alu FROM mat_matricula m INNER JOIN per_periodo p ON m.id_per=p.id WHERE m.id_alu = r.id_alu  AND m.id_gra=g.id AND p.id_tpe=per.id_tpe AND p.id_anio="+id_anio+")";
		sql += "\n AND per.id_anio="+id_anio;
		if(id_suc!=null)
		sql += "\n AND suc.id="+id_suc;
		if(id_niv!=null)
			sql += "\n AND niv.id="+id_niv;
		if(id_gir!=null)
			sql += "\n AND gir.id="+id_gir;
		if(id_gra!=null)
			sql += "\n AND g.id="+id_gra;
		/*if(id_grad!=null)
		sql +="\n AND g.id="+id_grad;*/
		sql += "\n GROUP BY g.id, per.id_tpe, suc.id) res ON res.id_gra = cap.id_gra AND res.nivel= cap.nivel AND res.sucursal = cap.sucursal AND res.id_tpe=cap.id_tpe ";
		sql += "\n LEFT JOIN (";
		sql += "\n SELECT gir.nom giro, suc.nom sucursal, niv.nom nivel,g.id id_gra, g.nom, tpe.id id_tpe, COUNT(1) total_traslados";
		sql += "\n FROM `mat_matricula` mat";
		sql += "\n INNER JOIN col_aula au ON au.id = mat.id_au_asi";
		sql += "\n INNER JOIN col_ciclo cic ON cic.id = au.id_cic";
		sql += "\n INNER JOIN per_periodo per ON per.id = cic.id_per";
		sql += "\n INNER JOIN cat_tip_periodo tpe ON per.id_tpe = tpe.id";
		sql += "\n INNER JOIN cat_grad g ON g.id = au.id_grad";
		sql += "\n INNER JOIN ges_servicio srv ON srv.id = per.id_srv";
		sql +="\n INNER JOIN ges_giro_negocio gir ON srv.id_gir=gir.id";
		sql += "\n INNER JOIN cat_nivel niv ON niv.id = srv.id_niv";
		sql += "\n INNER JOIN ges_sucursal suc ON suc.id = srv.id_suc";
		sql += "\n WHERE mat.id_sit='5' AND per.id_anio="+id_anio;
		if(id_suc!=null)
		sql += "\n AND suc.id="+id_suc;
		if(id_niv!=null)
			sql += "\n AND niv.id="+id_niv;
		if(id_gir!=null)
			sql += "\n AND gir.id="+id_gir;
		if(id_gra!=null)
			sql += "\n AND g.id="+id_gra;
		/*if(id_grad!=null)
		sql +="\n AND g.id="+id_grad;*/
		sql += "\n GROUP BY g.id, per.id_tpe, suc.id) tras ON tras.id_gra = cap.id_gra AND tras.nivel= cap.nivel AND tras.sucursal = cap.sucursal AND cap.id_tpe=tras.id_tpe ";	
		sql += "\n LEFT JOIN (";
		sql += "\n SELECT gir.nom giro, suc.nom sucursal, niv.nom nivel,g.id id_gra, g.nom, tpe.id id_tpe, COUNT(sit.id ) total_fallecidos"; 
		sql += "\n FROM col_situacion_mat sit";
		sql += "\n INNER JOIN `mat_matricula` mat ON sit.id_mat=mat.id";
		sql += "\n INNER JOIN cat_col_situacion s ON sit.id_sit=s.id";
		sql += "\n INNER JOIN col_aula au ON au.id = mat.id_au_asi";
		sql += "\n INNER JOIN per_periodo per ON per.id = au.id_per";
		sql += "\n INNER JOIN cat_tip_periodo tpe ON per.id_tpe = tpe.id";
		sql += "\n INNER JOIN cat_grad g ON g.id = au.id_grad";
		sql += "\n INNER JOIN ges_servicio srv ON srv.id = per.id_srv";
		sql +="\n INNER JOIN ges_giro_negocio gir ON srv.id_gir=gir.id";
		sql += "\n INNER JOIN cat_nivel niv ON niv.id = srv.id_niv";
		sql += "\n INNER JOIN ges_sucursal suc ON suc.id = srv.id_suc";
		sql += "\n WHERE s.cod='F' AND per.id_anio="+id_anio;
		if(id_suc!=null)
		sql += "\n AND suc.id="+id_suc;
		if(id_niv!=null)
			sql += "\n AND niv.id="+id_niv;
		if(id_gir!=null)
			sql += "\n AND gir.id="+id_gir;
		if(id_gra!=null)
			sql += "\n AND g.id="+id_gra;
		/*if(id_grad!=null)
		sql +="\n AND g.id="+id_grad;*/
		sql += "\n GROUP BY g.id, per.id_tpe, suc.id) fa ON fa.id_gra = cap.id_gra AND fa.nivel= cap.nivel AND fa.sucursal = cap.sucursal AND cap.id_tpe=fa.id_tpe ";
		sql += "\n LEFT JOIN (";
		sql += "\n SELECT gir.nom giro, suc.nom sucursal, niv.nom nivel,g.id id_gra, g.nom ,  per.id_tpe , COUNT(mat.id ) total_retirados"; 
		sql += "\n FROM `mat_matricula` mat";
		//sql += "\n FROM col_situacion_mat sit";
		//sql += "\n INNER JOIN `mat_matricula` mat ON sit.id_mat=mat.id";
		//sql += "\n INNER JOIN cat_col_situacion s ON sit.id_sit=s.id";
		sql += "\n INNER JOIN col_aula au ON au.id = mat.id_au_asi";
		sql += "\n INNER JOIN per_periodo per ON per.id = au.id_per";
		sql += "\n INNER JOIN cat_grad g ON g.id = au.id_grad";
		sql += "\n INNER JOIN ges_servicio srv ON srv.id = per.id_srv";
		sql +="\n INNER JOIN ges_giro_negocio gir ON srv.id_gir=gir.id";
		sql += "\n INNER JOIN cat_nivel niv ON niv.id = srv.id_niv";
		sql += "\n INNER JOIN ges_sucursal suc ON suc.id = srv.id_suc";
		sql += "\n WHERE mat.id_sit=4 AND per.id_anio="+id_anio;
		if(id_suc!=null)
		sql += "\n AND suc.id="+id_suc;
		if(id_niv!=null)
			sql += "\n AND niv.id="+id_niv;
		if(id_gir!=null)
			sql += "\n AND gir.id="+id_gir;
		if(id_gra!=null)
			sql += "\n AND g.id="+id_gra;
		/*if(id_grad!=null)
		sql +="\n AND g.id="+id_grad;*/
		sql += "\n GROUP BY g.id, per.id_tpe, suc.id) re ON re.id_gra = cap.id_gra AND re.nivel= cap.nivel AND re.sucursal = cap.sucursal  AND cap.id_tpe=re.id_tpe " ;
		sql += "\n ORDER BY   cap.giro, cap.sucursal, cap.id_gra ASC ";
List<Map<String,Object>> reporteConsolidado = jdbcTemplate.queryForList(sql);
//System.out.println(sql);

return reporteConsolidado;
	}  else {	
		return null;
	}
	
} 

	/**
	 * Reporte de matriculados por a�o academico
	 * 
	 * @param id_au
	 * @return
	 */
	public List<Map<String, Object>> matriculadosLista(String apelllidosNombres, Integer anio, Integer id_suc) {

		String sql = "SELECT m.id, pers.nro_doc dni, CONCAT(pers.`ape_pat`,' ', pers.ape_mat,' ', pers.nom) 'alumno', m.id_sit, m.num_cont, m.num_adenda, a.id id_alu,"
				+ " suc.nom sucusal, n.nom nivel, gra.nom grado, au.secc seccion" 
				+ " FROM mat_matricula m"
				+ " inner join alu_alumno a on m.id_alu=a.id" 
				+ " inner join col_persona  pers on pers.id=a.id_per" 
				+ " inner join col_aula au on m.id_au_asi=au.id"
				+ " inner join alu_familiar f on m.id_fam=f.id" 
				+ " inner join per_periodo p on au.id_per=p.id"
				+ " inner join cat_nivel n on n.id=p.id_niv" 
				+ " inner join cat_grad gra on gra.id=au.id_grad"
				+ " inner join ges_servicio g on g.id=p.id_srv" 
				+ " inner join ges_sucursal suc on suc.id=p.id_suc" 
				+ " WHERE  p.id_anio=" + anio;

		if (id_suc!= null && id_suc != 0)
			sql += " and g.id_suc=" + id_suc;

		sql += " and ( upper(CONCAT(a.ape_pat,' ',trim(a.ape_mat), ' ', a.nom)) LIKE '%"
				+ apelllidosNombres.toUpperCase() + "%'  ) " + " order by a.ape_pat asc";

		List<Map<String, Object>> lista = jdbcTemplate.queryForList(sql);
		logger.info(sql);
		return lista;
	}

	public List<Map<String, Object>> reporteContratos(String apoderado, Integer id_anio, Integer local) {
		String q_suc = " ";
		if (local != null)
			q_suc = " and suc.id=" + local + " ";
		String sql = "SELECT DISTINCT f.id, CONCAT (per.ape_pat,' ',per.ape_mat) familia, CONCAT (perf.ape_pat,' ', perf.ape_mat, ' ', perf.nom) apoderado, perf.nro_doc,"
				+ " COUNT( ga.`id_gpf`) nro_hijos,  m.num_cont, m.num_adenda,  f.`cel`, suc.nom local" + " FROM mat_matricula m "
				+ " LEFT JOIN alu_familiar f ON f.id = m.id_fam" + " LEFT JOIN alu_alumno a ON a.id=m.id_alu"
				+ " INNER JOIN col_persona per ON a.id_per=per.id"
				+ " INNER JOIN col_persona perf ON f.id_per=perf.id"
				+ " LEFT JOIN alu_gru_fam_familiar  gf ON gf.id_fam=f.id"
				+ " INNER JOIN alu_gru_fam_alumno ga ON ga.id_alu=a.id AND ga.id_gpf=gf.id_gpf"
				+ " LEFT JOIN per_periodo p ON p.id = m.id_per" + " LEFT JOIN ges_servicio ser ON ser.id=p.id_srv"
				+ " LEFT JOIN ges_sucursal suc ON suc.id=ser.id_suc" + " WHERE p.id_anio=" + id_anio + "  " + q_suc
				+ " and ( upper(CONCAT(perf.ape_pat,' ',perf.ape_mat, ' ', perf.nom)) LIKE '" + apoderado.toUpperCase()
				+ "%'  ) " + " GROUP BY ga.id_gpf, m.id" + " ORDER BY perf.ape_pat ASC, perf.ape_mat ASC";
//
		/*SELECT  f.id, gf.`nom` familia, CONCAT (perf.ape_pat,' ', perf.ape_mat, ' ', perf.nom) apoderado, perf.nro_doc,
		  COUNT( f.id) nro_hijos,  
		 m.num_cont, m.num_adenda,  f.`cel`
		 , suc.nom LOCAL 
		 FROM mat_matricula m 
		 INNER JOIN alu_familiar f ON f.id = m.id_fam 
		 INNER JOIN alu_alumno a ON a.id=m.id_alu
		 INNER JOIN col_persona per ON a.id_per=per.id
		 INNER JOIN col_persona perf ON f.id_per=perf.id
		 INNER JOIN alu_gru_fam_familiar  agff ON agff.id_fam=f.id
		 INNER JOIN `alu_gru_fam` gf ON gf.id=agff.`id_gpf`
		INNER JOIN alu_gru_fam_alumno ga ON ga.id_alu=a.id AND gf.id=ga.id_gpf
		  INNER JOIN per_periodo p ON p.id = m.id_per 
		 INNER JOIN ges_sucursal suc ON suc.id=p.id_suc WHERE p.id_anio=5   AND suc.id=2
		 -- and ( upper(CONCAT(perf.ape_pat,' ',perf.ape_mat, ' ', perf.nom)) LIKE '%'  )  
		 GROUP BY f.id
		 ORDER BY perf.ape_pat ASC, perf.ape_mat ASC;*/

		List<Map<String, Object>> reporte = jdbcTemplate.queryForList(sql);
		logger.info(sql);
		return reporte;
	}

	public List<Matricula> listHermanos(Param param, String[] order) {

		String sql = "select id_alu , id_sit, id_niv from mat_matricula ";
		sql = sql + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		logger.info(sql);

		return jdbcTemplate.query(sql, new BeanPropertyRowMapper(Matricula.class));
	}

	public List<Map<String, Object>> registroAuxiliares(Integer id_anio, Integer id_aula, Integer id_suc) {

		String sql = "SELECT CONCAT(pa.ape_pat,' ',pa.ape_mat,' ', pa.nom) alumno, niv.nom nivel, gra.nom grado, au.secc, au.id id_au";
			   sql = sql +" FROM mat_matricula mat, alu_alumno alu, cat_grad gra, col_aula au, cat_nivel niv, per_periodo per, col_ciclo cic, ges_servicio ser, col_persona pa ";
			   sql = sql + " WHERE mat.id_alu=alu.id AND mat.id_gra=gra.id AND mat.id_au_asi=au.id AND au.id_cic=cic.id AND cic.id_per=per.id AND per.id_srv=ser.id AND ser.id_niv=niv.id AND pa.id=alu.id_per ";
			   sql = sql + " AND (mat.id_sit not in ("+EnumSituacionFinal.TRASLADADO.getValue()+","+EnumSituacionFinal.RETIRADO.getValue()+","+EnumSituacionFinal.FALLECIDO.getValue()+") OR mat.id_sit IS NULL)";
			   sql = sql + " AND id_au_asi=" + id_aula + " AND per.id_anio=" +id_anio; 
					if ( id_suc.intValue()!=0)
			   sql = sql + " AND per.id_suc="+id_suc;
			   sql = sql + " ORDER BY niv.nom ASC, gra.id ASC, au.secc ASC, pa.`ape_pat` ASC, pa.`ape_mat` ASC, pa.nom ASC";

		List<Map<String, Object>> registro_aux = jdbcTemplate.queryForList(sql);
		logger.info(sql);
		return registro_aux;
	}

	public List<Aula> listAulas(Integer id_anio, Integer id_suc, Integer id_gir, Integer id_cit) {

		String sql = "SELECT a.*" + " FROM col_aula a, per_periodo per " ;
				   sql = sql + " ,ges_servicio srv, col_ciclo cic, col_turno_aula cta ";
		       sql = sql + " WHERE per.id = cic.id_per AND per.id_srv=srv.id AND a.id_cic=cic.id AND a.id=cta.id_au ";
			   sql = sql + " AND per.id_anio =" + id_anio+" and srv.id_gir="+id_gir+" AND cta.id_cit="+id_cit ;

				if (id_suc.intValue()!=0)
			   sql = sql + " AND per.id_suc =" +id_suc ;
			   sql = sql + " ORDER BY a.id_grad asc, a.secc asc";

		return jdbcTemplate.query(sql, new BeanPropertyRowMapper(Aula.class));

	}

	/**
	 * Reporte de matriculados por a�o academico
	 * 
	 * @param id_au
	 * @return
	 */
	public List<Map<String, Object>> matriculadosCombo(String apellidosNombres, Integer anio) {

		String sql = "SELECT m.id, m.id_alu, CONCAT((CONCAT(a.`ape_pat`,' ', a.ape_mat,' ', a.nom)),'( ', g.nom, ' - ',gr.nom,' - ',au.secc,' )') label, g.id_niv,g.nom nivel, au.secc, gr.nom grado "
				+ " FROM mat_matricula m" + " inner join alu_alumno a on m.id_alu=a.id"
				+ " inner join col_aula au on m.id_au_asi=au.id" + " inner join per_periodo p on au.id_per=p.id"
				+ " inner join ges_servicio g on g.id=p.id_srv" + " inner join cat_grad gr on gr.id=au.id_grad"
				+ " WHERE  p.id_anio=" + anio + " and ( upper(CONCAT(a.ape_pat,' ',a.ape_mat, ' ', a.nom)) LIKE '%"
				+ apellidosNombres.toUpperCase() + "%'  ) " + " order by a.ape_pat,a.ape_mat asc";

		List<Map<String, Object>> lista = jdbcTemplate.queryForList(sql);
		logger.info(sql);
		return lista;
	}
	
	/**
	 * Reporte de matriculados en toda la data del colegio
	 * 
	 * @param id_au
	 * @return
	 */
	public List<Row> alumnosCombo(String apellidosNombres, Integer id_anio) {

		String sql = "SELECT a.id, m.id id_mat, if(m.id is null,CONCAT(per.`ape_pat`,' ', per.ape_mat,' ', per.nom), CONCAT((CONCAT(per.`ape_pat`,' ', per.ape_mat,' ', per.nom)),'( ', g.nom, ' - ',gr.nom,' - ',au.secc,' )')) label, g.id_niv,g.nom nivel, au.secc, gr.nom grado "
				+ "\n FROM alu_alumno a "
				+ "\n INNER JOIN col_persona per ON a.id_per=per.id"
				+ "\n left join (mat_matricula m " 
				+ "\n inner join col_aula au on m.id_au_asi=au.id "
				+ "\n inner  join per_periodo p on au.id_per=p.id " 
				+ "\n inner  join ges_servicio g on g.id=p.id_srv "
				+ "\n inner  join cat_grad gr on gr.id=au.id_grad and p.id_anio="+id_anio+") on m.id_alu=a.id"
				+ "\n WHERE  ( upper(CONCAT(per.ape_pat,' ',per.ape_mat, ' ', per.nom)) LIKE '%" + apellidosNombres.toUpperCase() + "%'  ) " 
				+ "\n order by per.ape_pat,per.ape_mat asc";
		
		return sqlUtil.query(sql);
	}

	/**
	 * Reporte Matriculados segun rol del usuario para la condicion conductual
	 */
	public List<Map<String, Object>> matriculadosxUsuario(String apellidosNombres, Integer anio, Integer id_rol,
			Integer id_usr) {

		if (id_rol == 5) {
			String sql = "SELECT m.id, m.id_alu,CONCAT((CONCAT(per.`ape_pat`,' ', per.ape_mat,' ', per.nom)),'( ', g.nom, ' - ',gr.nom,' - ',au.secc,' )') label, g.nom nivel, au.secc, gr.nom grado "
					+ " FROM mat_matricula m" + " INNER JOIN alu_alumno a ON m.id_alu=a.id"
					+ " INNER JOIN col_persona per ON per.id=a.id_per "
					+ " INNER JOIN col_aula au ON m.id_au_asi=au.id"
					+ " INNER JOIN `col_tutor_aula` cta ON cta.`id_au`=au.`id`"
					+ " INNER JOIN per_periodo p ON au.id_per=p.id" + " INNER JOIN ges_servicio g ON g.id=p.id_srv"
					+ " INNER JOIN cat_grad gr ON gr.id=au.`id_grad`"
					+ " INNER JOIN `aeedu_asistencia`.`ges_trabajador` tra ON tra.`id`=cta.`id_tra`"
					+ " INNER JOIN `seg_usuario` usr ON tra.`id`=usr.`id_tra`" + " WHERE  p.id_anio=" + anio
					+ " AND usr.`id`=" + id_usr + " AND ( UPPER(CONCAT(per.ape_pat,' ',per.ape_mat, ' ', per.nom)) LIKE '%"
					+ apellidosNombres.toUpperCase() + "%'   ) " + " AND tra.est='A' ORDER BY per.ape_pat,per.ape_mat ASC";

			List<Map<String, Object>> lista = jdbcTemplate.queryForList(sql);
			logger.info(sql);
			return lista;
		} else if (id_rol == 6) {
			String sql = "SELECT m.id,m.id_alu, CONCAT((CONCAT(per.`ape_pat`,' ', per.ape_mat,' ', per.nom)),'( ', g.nom, ' - ',gr.nom,' - ',au.secc,' )') label, g.nom nivel, au.secc, gr.nom grado "
					+ " FROM mat_matricula m" + " INNER JOIN alu_alumno a ON m.id_alu=a.id"
					+ " INNER JOIN col_persona per ON per.id=a.id_per "
					+ " INNER JOIN col_aula au ON m.id_au_asi=au.id"
					+ " INNER JOIN `col_curso_aula` cca ON cca.`id_au`=au.`id`"
					+ " INNER JOIN per_periodo p ON au.id_per=p.id" + " INNER JOIN ges_servicio g ON g.id=p.id_srv"
					+ " INNER JOIN cat_grad gr ON gr.id=au.`id_grad`"
					+ " INNER JOIN `aeedu_asistencia`.`ges_trabajador` tra ON tra.`id`=cca.`id_tra`"
					+ " INNER JOIN `seg_usuario` usr ON tra.`id`=usr.`id_tra`" + " WHERE  p.id_anio=" + anio
					+ " AND usr.`id`=" + id_usr + " AND ( UPPER(CONCAT(per.ape_pat,' ',per.ape_mat, ' ', per.nom)) LIKE '%"
					+ apellidosNombres.toUpperCase() + "%'   ) " + " AND tra.est='A' ORDER BY per.ape_pat,per.ape_mat ASC";
			List<Map<String, Object>> lista = jdbcTemplate.queryForList(sql);
			logger.info(sql);
			return lista;
		} else if (id_rol == 2 || id_rol==22) {
			String sql = "SELECT m.id,m.id_alu, CONCAT((CONCAT(per.`ape_pat`,' ', per.ape_mat,' ', per.nom)),'( ', g.nom, ' - ',gr.nom,' - ',au.secc,' )') label, g.nom nivel, au.secc, gr.nom grado "
					+ " FROM mat_matricula m INNER JOIN alu_alumno a ON m.id_alu=a.id"
					+ " INNER JOIN col_persona per ON per.id=a.id_per "
					+ " INNER JOIN col_aula au ON m.id_au_asi=au.id"
					+ " INNER JOIN per_periodo p ON au.id_per=p.id INNER JOIN ges_servicio g ON g.id=p.id_srv"
					+ " INNER JOIN cat_grad gr ON gr.id=au.`id_grad`"
					+ " INNER JOIN `seg_usuario` usr ON usr.`id_suc`=p.`id_suc`"
					+ " INNER JOIN `aeedu_asistencia`.`ges_trabajador` tra ON tra.`id`=usr.`id_tra` WHERE  p.id_anio="+anio
					+ " AND usr.`id`="+id_usr+" AND ( UPPER(CONCAT(per.ape_pat,' ',per.ape_mat, ' ', per.nom)) LIKE '%"
					+ apellidosNombres.toUpperCase() + "%'   ) " + "  ORDER BY per.ape_pat,per.ape_mat ASC";
			List<Map<String, Object>> lista = jdbcTemplate.queryForList(sql);
			logger.info(sql);
			return lista;
		} else {
			String sql = "SELECT m.id,m.id_alu, CONCAT((CONCAT(per.`ape_pat`,' ', per.ape_mat,' ', per.nom)),'( ', g.nom, ' - ',gr.nom,' - ',au.secc,' )') label, g.nom nivel, au.secc, gr.nom grado "
					+ " FROM mat_matricula m" + " inner join alu_alumno a on m.id_alu=a.id"
					+ " INNER JOIN col_persona per ON per.id=a.id_per "
					+ " inner join col_aula au on m.id_au_asi=au.id" + " inner join per_periodo p on au.id_per=p.id"
					+ " inner join ges_servicio g on g.id=p.id_srv" + " inner join cat_grad gr on gr.id=au.id_grad"
					+ " WHERE  p.id_anio=" + anio + " and ( upper(CONCAT(per.ape_pat,' ',per.ape_mat, ' ', per.nom)) LIKE '%"
					+ apellidosNombres.toUpperCase() + "%'  ) " + " order by per.ape_pat,per.ape_mat asc";
			List<Map<String, Object>> lista = jdbcTemplate.queryForList(sql);
			logger.info(sql);
			return lista;
		}

	}

	/**
	 * Reporte de matriculados por a�o academico
	 * 
	 * @param id_au
	 * @return
	 */
	public List<Map<String, Object>> matriculadosCombo(Integer id_au) {

		String sql = "SELECT m.id, CONCAT(a.`ape_pat`,' ', a.ape_mat,' ', a.nom) value " + " FROM mat_matricula m"
				+ " inner join alu_alumno a on m.id_alu=a.id" + " inner join per_periodo p on m.id_per=p.id"
				+ " inner join ges_servicio g on g.id=p.id_srv" + " WHERE  m.id_au_asi=" + id_au
				+ " order by a.ape_pat,a.ape_mat asc";

		List<Map<String, Object>> lista = jdbcTemplate.queryForList(sql);
		logger.info(sql);
		return lista;
	}

	/**
	 * Reporte de matriculados por a�o academico
	 * 
	 * @param id_au
	 * @return
	 */
	public List<Map<String, Object>> matriculadosParaDescuentoCombo(String apelllidosNombres, Integer anio,
			Integer id_suc) {

		String sql = "SELECT m.id, CONCAT(per.`ape_pat`,' ', per.ape_mat,' ', per.nom) label " + " FROM mat_matricula m"
				+ " inner join alu_alumno a on m.id_alu=a.id" + " inner join alu_familiar f on m.id_fam=f.id"
				+ " inner join col_persona per on a.id_per=per.id "
				+ " inner join per_periodo p on m.id_per=p.id" + " inner join ges_servicio g on g.id=p.id_srv"
				+ " WHERE  p.id_anio=" + anio;

		if (id_suc != 0)
			sql += " and g.id_suc=" + id_suc;

		sql += " and ( upper(CONCAT(per.ape_pat,' ',per.ape_mat, ' ', per.nom)) LIKE '" + apelllidosNombres.toUpperCase()
				+ "%'  ) " + " and m.id not in(select id_mat from fac_alumno_descuento ) " + " order by per.ape_pat asc";

		List<Map<String, Object>> lista = jdbcTemplate.queryForList(sql);
		logger.info(sql);
		return lista;
	}

	/**
	 * Reporte de matriculados por a�o academico
	 * 
	 * @param id_au
	 * @return
	 */
	public List<Map<String, Object>> matriculados(String apelllidosNombres, Integer anio, Integer id_suc) {

		String sql = "SELECT m.id, CONCAT(a.`ape_pat`,' ', a.ape_mat,' ', a.nom) label " + " FROM mat_matricula m"
				+ " inner join alu_alumno a on m.id_alu=a.id" + " inner join alu_familiar f on m.id_fam=f.id"
				+ " inner join per_periodo p on m.id_per=p.id" + " inner join ges_servicio g on g.id=p.id_srv"
				+ " WHERE  p.id_anio=" + anio;

		if (id_suc != 0)
			sql += " and g.id_suc=" + id_suc;

		sql += " and ( upper(CONCAT(a.ape_pat,' ',a.ape_mat, ' ', a.nom)) LIKE '" + apelllidosNombres.toUpperCase()
				+ "%'  ) " + " order by a.ape_pat asc";

		List<Map<String, Object>> lista = jdbcTemplate.queryForList(sql);
		logger.info(sql);
		return lista;
	}

	public List<Map<String, Object>> matriculadosNivLocal(Integer id_anio) {

		String sql = "SELECT mat.* , alu.cod codigo FROM `mat_matricula` mat"
				+ " LEFT JOIN `per_periodo` per ON mat.`id_per`=per.`id`"
				+ " LEFT JOIN `ges_servicio` ser ON per.`id_srv`=ser.`id`"
				+ " LEFT JOIN `alu_alumno` alu ON mat.`id_alu`=alu.id" + " WHERE per.`id_anio`=" + id_anio
				+ " ORDER BY mat.`id_alu` ASC";

		List<Map<String, Object>> lista = jdbcTemplate.queryForList(sql);
		logger.info(sql);
		return lista;
	}

	public Integer periodoCambioLocal(Integer id_mat) {
		String sql = "select pe.id from mat_matricula m" + " inner join col_aula au on au.id = m.id_au_asi"
				+ " inner join per_periodo pe on pe.id = au.id_per" + " where m.id= " + id_mat;
		return jdbcTemplate.queryForObject(sql, Integer.class);
	}

	public Row getDatosMatriculaLight(Integer id_mat) {
		String sql = "select m.id_alu, m.id_au_asi, pe.id, pe.id_niv from mat_matricula m"
				+ " inner join col_aula au on au.id = m.id_au_asi" + " inner join per_periodo pe on pe.id = au.id_per"
				+ " where m.id= " + id_mat;

		List<Row> list = sqlUtil.query(sql);
		return list.get(0);
	}

	public Row getDatosAlumno(Integer id_mat) {
		String sql = "SELECT mat.`id` ,mat.id_fam, au.id_per, CONCAT(per.ape_pat,' ',per.ape_mat, ', ' , per.nom) alumno, niv.nom nivel, gra.`nom` grado, au.`secc` secc, cic.nom ciclo"
				+ " FROM `mat_matricula` mat INNER JOIN `alu_alumno` alu ON mat.`id_alu`=alu.`id` "
				+ " INNER JOIN col_persona per ON alu.id_per=per.id"
				+ " INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.`id` "
				+ " INNER JOIN `cat_grad` gra ON au.`id_grad`=gra.`id` "
				+ " LEFT JOIN col_ciclo cic ON mat.id_cic=cic.id "
				+ " INNER JOIN `cat_nivel` niv ON gra.`id_nvl`=niv.`id` " + " WHERE mat.`id`=" + id_mat;
		logger.debug(sql);
		List<Row> list = sqlUtil.query(sql);
		if (list.size() > 0)
			return list.get(0);// esto da vacio
		else
			return null;
	}

	public Row getDatosApoderado(Integer id_mat) {
		String sql = "SELECT pf.nro_doc, td.nom tip_doc, CONCAT(pf.ape_pat,' ', pf.ape_mat,' ', pf.nom) nombres, pf.dir direccion, pf.cel celular"
				+ " FROM mat_matricula mat " + " INNER JOIN alu_familiar fam ON mat.id_fam=fam.id"
				+ " INNER JOIN col_persona pf ON fam.id_per=pf.id "
				+ " INNER JOIN cat_tipo_documento td ON td.id =pf.id_tdc" + " WHERE mat.id=" + id_mat;

		List<Row> list = sqlUtil.query(sql);
		return list.get(0);
	}
	
	public List<Map<String, Object>> listaSituacionFinal(Integer id_au) {

		String sql = "SELECT alu.id id_alu, mat.id id_mat, CONCAT(pers.`ape_pat`,' ', pers.`ape_mat`,' ', pers.`nom`) alumno, niv.`nom` nivel, gra.`nom` grado, au.`secc` seccion, sit.`nom`, sit.id id_sit "
				+ " FROM `mat_matricula` mat INNER JOIN `alu_alumno` alu ON mat.`id_alu`=alu.`id`"
				+ " INNER JOIN `col_persona` pers ON alu.`id_per`=pers.`id`"
				+ " INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.`id`"
				+ " INNER JOIN `cat_grad`  gra ON au.`id_grad`=gra.`id`"
				+ " INNER JOIN `cat_nivel` niv ON gra.`id_nvl`=niv.`id`"
				+ " LEFT JOIN `cat_col_situacion` sit ON mat.`id_sit`=sit.`id`"
				+ " WHERE mat.`id_au_asi`="+id_au
				+ " ORDER BY alu.ape_pat, alu.ape_mat, alu.nom";

		List<Map<String, Object>> lista = jdbcTemplate.queryForList(sql);
		logger.info(sql);
		return lista;
	}

	public Integer  actualizarSituacion(Integer id_mat, Integer id_sit){
		String sql = "update mat_matricula set id_sit=? , fec_act=curdate() where id = ?";
		return sqlUtil.update(sql, new Object[]{id_sit, id_mat});
	}
	
	public void actualizarSeccion(Integer id_mat, Integer id_au){
		String sql = "update mat_matricula set id_au_asi=?, fec_act=curdate() , usr_act=? where id = ?"; // Si cambio de secci�n, a mi parecer solo se debe de cambiar id_au_asi y debe permanecer su aula original
		sqlUtil.update(sql, new Object[]{id_au, tokenSeguridad.getId(), id_mat});
	}
	
	public void actualizarSeccionxPrimeraVez(Integer id_mat, Integer id_au){
		String sql = "update mat_matricula set mat_val=1, id_au=?, id_au_asi=?, fec_act=curdate() , usr_act=? where id = ?"; // Si cambio de secci�n, a mi parecer solo se debe de cambiar id_au_asi y debe permanecer su aula original
		sqlUtil.update(sql, new Object[]{id_au,id_au, tokenSeguridad.getId(), id_mat});
	}

	public void actualizarLocalYSeccion(Param param){
		String sql = "update mat_matricula set id_au_asi=:id_au_asi , id_per=:id_per, id_cic=:id_cic, id_cct=:id_cct, fec_act=curdate() , usr_act=:id_usr, obs=:obs where id = :id_mat";
		sqlUtil.update(sql, param);
	}

	
	public List<Row> matriculadosxContrato(String num_cont){
		String sql = "SELECT alu.id id_alu, mat.id id_mat, mat.fecha, mat.num_cont num_cont, mat.num_adenda, CONCAT(pers.`ape_pat`,' ', pers.`ape_mat`,' ', pers.`nom`) alumno, niv.`nom` nivel, gra.`nom` grado, au.`secc` seccion, au.id_per"
				+ " FROM `mat_matricula` mat INNER JOIN `alu_alumno` alu ON mat.`id_alu`=alu.`id`"
				+ " INNER JOIN col_persona pers ON alu.id_per=pers.id "
				+ " INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.`id`"
				+ " INNER JOIN `cat_grad`  gra ON au.`id_grad`=gra.`id`"
				+ " INNER JOIN `cat_nivel` niv ON gra.`id_nvl`=niv.`id`"
				+ " WHERE mat.num_cont=? and mat.mat_val=1"
				+ " ORDER BY alu.ape_pat, alu.ape_mat, alu.nom";
		
			return sqlUtil.query(sql, new Object[]{num_cont});

	}
	
	public List<Row> matriculadosxContratoparaWeb(String num_cont){
		String sql = "SELECT alu.id id_alu, mat.id id_mat, mat.fecha, mat.num_cont num_cont, mat.num_adenda, CONCAT(per.`ape_pat`,' ', per.`ape_mat`,' ', per.`nom`) alumno, niv.`nom` nivel, gra.`nom` grado, peri.id id_per "
				+ " FROM `mat_matricula` mat INNER JOIN `alu_alumno` alu ON mat.`id_alu`=alu.`id`"
				+ " INNER JOIN per_periodo peri ON mat.id_per=peri.id"
				+ " INNER JOIN col_persona per ON alu.id_per=per.id"
				+ " INNER JOIN `cat_grad`  gra ON mat.`id_gra`=gra.`id`"
				+ " INNER JOIN `cat_nivel` niv ON gra.`id_nvl`=niv.`id`"
				+ " WHERE mat.num_cont=? "
				+ " ORDER BY alu.ape_pat, alu.ape_mat, alu.nom";
		
			return sqlUtil.query(sql, new Object[]{num_cont});

	}
	
	public List<Row> matriculadosxLocalyApoderado(Integer id_suc, Integer id_fam, Integer id_anio){
		String sql = "SELECT mat.id id_mat, mat.`num_cont`, mat.`num_adenda`, CONCAT(p.`ape_pat`,' ', p.`ape_mat`,' ', p.`nom`) alumno, gra.nom grado, niv.nom nivel, au.secc seccion"
				+ "\n FROM `mat_matricula` mat INNER JOIN `alu_familiar` fam ON mat.`id_fam`=fam.`id`"
				+ "\n INNER JOIN col_aula au ON mat.`id_au_asi`=au.id"
				+ "\n INNER JOIN col_ciclo cic ON cic.id=au.id_cic"
				+ "\n INNER JOIN per_periodo per ON cic.id_per=per.id"
				+ "\n INNER JOIN `alu_alumno` alu ON mat.`id_alu`=alu.`id`"
				+ "\n INNER JOIN col_persona p ON alu.id_per=p.id"
				+ "\n INNER JOIN cat_grad gra ON au.id_grad=gra.id"
				+ "\n INNER JOIN cat_nivel niv ON gra.id_nvl=niv.id"
				+ "\n WHERE per.id_suc=? AND mat.`id_fam`=? and per.id_anio=?";
		
			return sqlUtil.query(sql, new Object[]{id_suc, id_fam, id_anio});

	}
	
	public List<Row> matriculadosxLocalyApoderadoValidadas(Integer id_suc, Integer id_fam, Integer id_anio){
		String sql = "SELECT mat.id id_mat, mat.`num_cont`, mat.`num_adenda`, CONCAT(alu.`ape_pat`,' ', alu.`ape_mat`,' ', alu.`nom`) alumno, gra.nom grado, niv.nom nivel, au.secc seccion"
				+ " FROM `mat_matricula` mat INNER JOIN `alu_familiar` fam ON mat.`id_fam`=fam.`id`"
				+ " INNER JOIN col_aula au ON mat.`id_au_asi`=au.id"
				+ " INNER JOIN per_periodo per ON au.id_per=per.id"
				+ " INNER JOIN `alu_alumno` alu ON mat.`id_alu`=alu.`id`"
				+ " INNER JOIN cat_grad gra ON au.id_grad=gra.id"
				+ " INNER JOIN cat_nivel niv ON gra.id_nvl=niv.id"
				+ " WHERE per.id_suc=? AND mat.`id_fam`=? and mat.mat_val=1 AND per.id_anio=?";
		
			return sqlUtil.query(sql, new Object[]{id_suc, id_fam, id_anio});

	}
		
	
	public List<Row> matriculadosWebxApoderado(Integer id_fam, Integer id_anio){
		String sql = "SELECT mat.id id_mat, mat.`num_cont`, mat.`num_adenda`, CONCAT(pers.`ape_pat`,' ', pers.`ape_mat`,' ', pers.`nom`) alumno, gra.nom grado, niv.nom nivel, au.secc seccion"
				+ " FROM `mat_matricula` mat INNER JOIN `alu_familiar` fam ON mat.`id_fam`=fam.`id`"
				+ " INNER JOIN col_aula au ON mat.`id_au_asi`=au.id"
				+ " INNER JOIN col_ciclo cic ON au.id_cic=cic.id"
				+ " INNER JOIN per_periodo per ON cic.id_per=per.id"
				+ " INNER JOIN `alu_alumno` alu ON mat.`id_alu`=alu.`id`"
				+ " INNER JOIN `col_persona` pers ON alu.`id_per`=pers.`id`"
				+ " INNER JOIN cat_grad gra ON au.id_grad=gra.id"
				+ " INNER JOIN cat_nivel niv ON gra.id_nvl=niv.id"
				+ " WHERE mat.`id_fam`=? AND per.id_anio=?";
		
			return sqlUtil.query(sql, new Object[]{ id_fam, id_anio});

	}
	
	public List<Row> listarCostosLocal(Integer id_suc, Integer id_anio){
		String sql = "SELECT  mcc.`id`, niv.nom nivel, suc.nom sucursal, mcc.matricula, mcc.cuota cuota_ingreso, mcc.cuota*10 AS 'cuota_mensual'"
				+ " FROM `mat_conf_cuota` mcc"
				+ " INNER JOIN per_periodo per ON mcc.`id_per`=per.id"
				+ " INNER JOIN ges_sucursal suc ON per.id_suc=suc.id"
				+ " INNER JOIN cat_nivel niv ON per.id_niv=niv.id"
				+ " WHERE per.id_suc="+id_suc+" AND per.id_anio="+id_anio;
		
			return sqlUtil.query(sql);

	}
	
	public List<Row> costosMatricula(Integer id_suc, Integer id_anio){
		String sql = "SELECT  mcc.`id`, niv.nom nivel, suc.nom sucursal, mcc.matricula, mcc.cuota cuota_ingreso, mcc.cuota*10 AS 'cuota_mensual'"
				+ " FROM `mat_conf_cuota` mcc"
				+ " INNER JOIN per_periodo per ON mcc.`id_per`=per.id"
				+ " INNER JOIN ges_sucursal suc ON per.id_suc=suc.id"
				+ " INNER JOIN cat_nivel niv ON per.id_niv=niv.id"
				+ " WHERE per.id_suc="+id_suc+" AND per.id_anio="+id_anio;
		
			return sqlUtil.query(sql);
	}

	public List<Row> aniosList(Integer id_alu){
		String sql = "select p.id_anio id , a.nom anio"
		+ " from mat_matricula m"
		+ " inner join per_periodo p on p.id= m.id_per"
		+ " inner join col_anio a on a.id = p.id_anio"
		+ " where m.id_alu=" + id_alu + " order by id_anio";
		
		return sqlUtil.query(sql);
	}
	
	public List<Row> noMatriculadosAnioAnterior(Integer id_anio_ant, Integer id_anio){
		String sql = "SELECT a.`id`, CONCAT(p.`ape_pat`,' ', p.`ape_mat`,' ', p.`nom`) alumno , CONCAT(pf.`ape_pat`,' ', pf.`ape_mat`,' ', pf.`nom`) familiar, pf.cel, pf.corr , p.`nro_doc`, gra.`nom` grado, niv.`nom` nivel, au_ant.`secc`, suc_ant.nom sucursal_ant, (CASE WHEN res=1 THEN 'SI' WHEN res=0 THEN 'NO' ELSE 'NO RESPONDE' END) ratifico"
				+ " FROM `alu_alumno` a INNER JOIN `mat_matricula` mat_ant ON a.`id`=mat_ant.`id_alu`"
				+ " INNER JOIN col_persona p ON a.id_per=p.id "
				+ " INNER JOIN alu_familiar fam ON fam.id=mat_ant.id_fam "
				+ " INNER JOIN col_persona pf ON pf.id=fam.id_per "
				+ " INNER JOIN `col_aula` au_ant ON mat_ant.`id_au_asi`=au_ant.`id`"
				+ " INNER JOIN `per_periodo` per_ant ON au_ant.`id_per`=per_ant.`id` AND per_ant.`id_anio`="+id_anio_ant+ " and per_ant.id_tpe=1"
				+ " INNER JOIN ges_sucursal suc_ant ON per_ant.id_suc=suc_ant.id"
				+ " INNER JOIN `cat_grad` gra ON au_ant.`id_grad`=gra.`id`"
				+ " INNER JOIN `cat_nivel` niv ON gra.`id_nvl`=niv.`id`"
				+ " LEFT JOIN `mat_ratificacion` rat ON rat.`id_mat`=mat_ant.`id`"
				+ " WHERE a.`id` NOT IN (SELECT alu.`id` FROM `mat_matricula` mat INNER JOIN `alu_alumno` alu ON mat.`id_alu`=alu.`id` "
				+ " INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.`id`"
				+ " inner join col_ciclo cic ON au.id_cic=cic.id"
				+ " INNER JOIN `per_periodo` per ON cic.`id_per`=per.`id` AND per.`id_anio`="+id_anio+" and per.id_tpe=1)"
				+ " AND mat_ant.`id_sit` NOT IN (5,4,6) AND mat_ant.id_gra<>'14'"
				+ " ORDER BY gra.id, au_ant.`secc`, a.`ape_pat`, a.`ape_mat`, a.`nom`";
		
		return sqlUtil.query(sql);
	}
	
	/**
	 * Actualizar el pago de matricula y/o Ingreso
	 * @param academicoPago
	 * @return
	 */
	public int updatePagoMatriculaIngreso(AcademicoPago academicoPago) {

		// update
		String sql = "UPDATE fac_academico_pago " + "SET canc=?, nro_rec=?, fec_pago=?, monto_total=?, usr_act=?, fec_act=? " + "WHERE id=? ";

		logger.info(sql);
		return jdbcTemplate.update(sql, academicoPago.getCanc(),academicoPago.getNro_rec(),academicoPago.getFec_pago(), academicoPago.getMontoTotal(), tokenSeguridad.getId(),new Date(), academicoPago.getId());

	}
	
	/**
	 * Obtener Pago Matricula
	 * @param id_mat
	 * @return
	 */
	public Row getPagoMatricula(Integer id_mat) {
		
		String sql = "SELECT fac.* FROM `fac_academico_pago` fac INNER JOIN `mat_matricula` mat ON fac.`id_mat`=mat.`id` WHERE fac.`tip`='MAT' AND fac.`id_mat`="+id_mat;
		List<Row> list = sqlUtil.query(sql);
		return list.get(0);
	}
	
	public Row getPagoCuotaIngreso(Integer id_mat) {
		
		String sql = "SELECT fac.* FROM `fac_academico_pago` fac INNER JOIN `mat_matricula` mat ON fac.`id_mat`=mat.`id` WHERE fac.`tip`='ING' AND fac.`id_mat`="+id_mat;
		List<Row> list = sqlUtil.query(sql);
		if(list.size()>0)
		return list.get(0);
		else
			return null;
	}
	
	/**
	 * Obtener datos del contrato por el numero de contrato
	 * @param nro_doc
	 * @return
	 */
	public List<Row> cabeceraContrato(Integer id_fam, Integer id_anio){
		String sql = "select distinct mat.num_cont, mat.num_adenda, fam.id, concat(p.`ape_pat`,' ', p.`ape_mat`,' ', p.`nom`) familiar, p.`nro_doc`, p.cel, p.corr, ag.direccion, dist.nom distrito, pro.nom provincia, dep.nom departamento  "
				+ " from `mat_matricula` mat inner join `alu_alumno` alu ON mat.`id_alu`=alu.`id`"
				+ " inner join `alu_familiar` fam on mat.`id_fam`=fam.`id`"
				+ " inner join alu_gru_fam_familiar agff on fam.id=agff.id_fam"
				+ " inner join alu_gru_fam ag on agff.id_gpf=ag.id"
				+ " inner join col_persona p on fam.id_per=p.id"
				+ " inner join col_aula au on mat.id_au_asi=au.id"
				+ " inner join col_ciclo cic on au.id_cic=cic.id"
				+ " inner join per_periodo per on cic.id_per=per.id"
				//+ " left join cat_distrito dist on fam.`id_dist`=dist.id"
				+ " left join cat_distrito dist on ag.`id_dist`=dist.id"
				+ " left join cat_provincia pro on pro.id=dist.id_pro"
				+ " left join cat_departamento dep on pro.id_dep=dep.id"
				+ " where mat.id_fam=? and per.id_anio=?";
		
		return sqlUtil.query(sql, new Object[]{id_fam, id_anio});
	}
	
	public List<Row> getFamiliarSugeridoMatricula(Integer id_anio, Integer id_gpf) {
		
		String sql = "SELECT DISTINCT mat.`id_fam`, p.id id_per, CONCAT(p.ape_pat,' ',p.ape_mat,' ', p.nom) familiar, fac.id_bco_pag "
				+ " FROM `mat_matricula` mat INNER JOIN `alu_alumno` alu ON mat.`id_alu`=alu.`id`"
				+ " inner join fac_academico_pago fac ON mat.id=fac.id_mat and fac.tip='MAT' "
				+ " INNER JOIN alu_familiar fam ON mat.id_fam=fam.id"
				+ " INNER JOIN col_persona p ON fam.id_per=p.id"
				+ " INNER JOIN `alu_gru_fam_alumno` agfa ON alu.`id`= agfa.`id_alu`"
				+ " INNER JOIN col_aula au ON mat.`id_au_asi`=au.id"
				+ " INNER JOIN col_ciclo cic ON au.id_cic=cic.id"
				+ " INNER JOIN per_periodo per ON cic.id_per=per.id"
				+ " WHERE per.id_anio=? AND agfa.`id_gpf`=?";
		return sqlUtil.query(sql, new Object[]{id_anio, id_gpf});
	}
	
	public List<Row> getFamiliarSugeridoMatriculaxUsuario(Integer id_anio, Integer id_usr) {
		
		String sql = "SELECT DISTINCT mat.`id_fam`, p.id id_per, CONCAT(p.ape_pat,' ',p.ape_mat,' ', p.nom) familiar, fac.id_bco_pag "
				+ " FROM `mat_matricula` mat INNER JOIN `alu_alumno` alu ON mat.`id_alu`=alu.`id`"
				+ " INNER JOIN alu_familiar fam ON mat.id_fam=fam.id"
				+ " INNER JOIN col_persona p ON fam.id_per=p.id"
				+ " INNER JOIN `alu_gru_fam_alumno` agfa ON alu.`id`= agfa.`id_alu`"
				+ " INNER JOIN `alu_gru_fam` agf ON agfa.id_gpf=agf.id"
				+ " INNER JOIN col_aula au ON mat.`id_au_asi`=au.id"
				+ " INNER JOIN col_ciclo cic ON au.id_cic=cic.id"
				+ " INNER JOIN per_periodo per ON cic.id_per=per.id and per.id_tpe=1"
				+ " INNER JOIN fac_academico_pago fac ON mat.id=fac.id_mat and fac.tip='MAT'"
				+ " WHERE per.id_anio=? AND agf.id_usr=?";
		return sqlUtil.query(sql, new Object[]{id_anio, id_usr});
	}
	
	
	public List<Row> getResponsablePagoSugeridoMatriculaxUsuario(Integer id_anio, Integer id_usr) {
		
		String sql = "SELECT DISTINCT mat.`id_fam_res_pag`, p.id id_per, fam.id id_fam,  CONCAT(p.ape_pat,' ',p.ape_mat,' ', p.nom) familiar, fac.id_bco_pag "
				+ " FROM `mat_matricula` mat INNER JOIN `alu_alumno` alu ON mat.`id_alu`=alu.`id`"
				+ " INNER JOIN alu_familiar fam ON mat.id_fam_res_pag=fam.id"
				+ " INNER JOIN col_persona p ON fam.id_per=p.id"
				+ " INNER JOIN `alu_gru_fam_alumno` agfa ON alu.`id`= agfa.`id_alu`"
				+ " INNER JOIN `alu_gru_fam` agf ON agfa.id_gpf=agf.id"
				+ " INNER JOIN col_aula au ON mat.`id_au_asi`=au.id"
				+ " INNER JOIN col_ciclo cic ON au.id_cic=cic.id"
				+ " INNER JOIN per_periodo per ON cic.id_per=per.id and per.id_tpe=1"
				+ " INNER JOIN fac_academico_pago fac ON mat.id=fac.id_mat and fac.tip='MAT'"
				+ " WHERE per.id_anio=? AND agf.id_usr=?";
		return sqlUtil.query(sql, new Object[]{id_anio, id_usr});
	}
	
public List<Row> getResponsableAcademicoSugeridoMatriculaxUsuario(Integer id_anio, Integer id_usr) {
		
		String sql = "SELECT DISTINCT mat.`id_fam_res_aca`, p.id id_per, fam.id id_fam, CONCAT(p.ape_pat,' ',p.ape_mat,' ', p.nom) familiar, fac.id_bco_pag "
				+ " FROM `mat_matricula` mat INNER JOIN `alu_alumno` alu ON mat.`id_alu`=alu.`id`"
				+ " INNER JOIN alu_familiar fam ON mat.id_fam_res_aca=fam.id"
				+ " INNER JOIN col_persona p ON fam.id_per=p.id"
				+ " INNER JOIN `alu_gru_fam_alumno` agfa ON alu.`id`= agfa.`id_alu`"
				+ " INNER JOIN `alu_gru_fam` agf ON agfa.id_gpf=agf.id"
				+ " INNER JOIN col_aula au ON mat.`id_au_asi`=au.id"
				+ " INNER JOIN col_ciclo cic ON au.id_cic=cic.id"
				+ " INNER JOIN per_periodo per ON cic.id_per=per.id and per.id_tpe=1"
				+ " INNER JOIN fac_academico_pago fac ON mat.id=fac.id_mat and fac.tip='MAT'"
				+ " WHERE per.id_anio=? AND agf.id_usr=?";
		return sqlUtil.query(sql, new Object[]{id_anio, id_usr});
	}
	
	/**
	 * Actualizar el estado de la matricula a validado
	 * @param id_mat
	 * @return
	 */
	public int updateEstadoMatricula(Integer id_mat) {

		// update
		String sql = "UPDATE mat_matricula SET mat_val=1 " + "WHERE id=? ";

		logger.info(sql);
		return jdbcTemplate.update(sql, id_mat);

	}
	
	/**
	 * Inhabilitamos la matricula a estado 0, cuando se hiz un cambio de local, asi se haya hech el reembolso
	 * @param id_mat
	 * @return
	 */
	public int inhabilitarMatricula(Integer id_mat) {

		// update
		String sql = "UPDATE mat_matricula SET mat_val=0 " + "WHERE id=? ";

		logger.info(sql);
		return jdbcTemplate.update(sql, id_mat);

	}
	
	/**
	 * Lista de matriculas donde pago Cuota de Ingreso
	 * @param id_alu
	 * @return
	 */
	public List<Row> listaMatriculasCuotaIngreso(Integer id_alu, Integer id_anio) {
		
		String sql = "SELECT fac_ant.* "
				+ " FROM `mat_matricula` mat_ant INNER JOIN `fac_academico_pago` fac_ant ON mat_ant.`id`=fac_ant.`id_mat`"
				+ " WHERE mat_ant.`id_alu`=? AND fac_ant.`tip`=? AND fac_ant.`canc`=1 and mat_ant.id not in (select mat.id from mat_matricula mat inner join per_periodo per on mat.id_per=per.id where mat.id_alu="+id_alu+" and per.id_anio="+id_anio+")";
		return sqlUtil.query(sql, new Object[]{id_alu, Constante.PAGO_MATRICULA});
	}
	
	/**
	 * Listar Datos de Comunicaci�n de los padres de familia de los alumnos por aula
	 * @param id_au
	 * @param id_anio
	 * @return
	 */
	public List<Row> listaDatosComunicacionPadres(Integer id_au, Integer id_niv, Integer id_grad, Integer id_anio, Integer id_gir, Integer id_suc, Integer id_cic) {
		
		String sql = "SELECT alu.`id`, CONCAT(pera.`ape_pat`,' ', pera.`ape_mat`,' ', pera.`nom` ) alumno, CONCAT(perf.ape_pat,' ', perf.ape_mat,' ', perf.nom) familiar, perf.nro_doc dni_fam, cic.nom ciclo, ";
			   sql += "\n fam.id id_fam, mat.id_fam_res_aca id_apod, perf.cel fam_cel, par.par parentesco, agf.`direccion` fam_dir , perf.`corr` fam_corr, fam.`viv_alu`, niv.nom nivel, gra.nom grado, au.secc seccion";
			   sql += "\n FROM `alu_alumno` alu INNER JOIN `mat_matricula` mat ON alu.`id`=mat.`id_alu`";
			   sql += "\n INNER JOIN `col_persona` pera ON alu.id_per=pera.id";
			   sql += "\n INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.`id`";
			   sql += "\n INNER JOIN `col_ciclo` cic ON au.id_cic=cic.`id`";
			   sql += "\n INNER JOIN cat_grad gra ON au.id_grad=gra.id";
			   sql += "\n INNER JOIN cat_nivel niv ON gra.id_nvl=niv.id";
			   sql += "\n INNER JOIN `per_periodo` per ON au.`id_per`=per.`id` and cic.id_per=per.id ";
			   sql += "\n INNER JOIN `ges_servicio` ser ON per.id_srv=ser.`id`";
			   sql += "\n INNER JOIN `alu_gru_fam_alumno` agfa ON alu.`id`=agfa.`id_alu`";
			   sql += "\n INNER JOIN `alu_gru_fam_familiar` agff ON agfa.`id_gpf`=agff.`id_gpf`";
			   sql += "\n INNER JOIN `alu_gru_fam` agf ON agff.`id_gpf`=agf.id AND agfa.`id_gpf`=agf.id";
			   sql += "\n INNER JOIN `alu_familiar` fam ON agff.`id_fam`=fam.`id`";
			   sql += "\n INNER JOIN `col_persona` perf ON fam.`id_per`=perf.`id`";
			   sql += "\n INNER JOIN `cat_parentesco` par ON fam.`id_par`=par.`id`";
			   sql += "\n WHERE niv.id=? AND ser.id_gir=? AND (mat.id_sit<>'5' OR mat.id_sit IS NULL) AND par.`id` IN ("+Constante.PARENTESCO_PAPA+","+Constante.PARENTESCO_MAMA+") AND per.`id_anio`=?";
			   if(id_grad!=null)
					sql += "\n AND au.id_grad="+id_grad;
			   if (id_au!=null)
				    sql += "\n AND au.id="+id_au;
			   if (id_suc!=null)
				    sql += "\n AND per.id_suc="+id_suc;
			   if (id_cic!=null)
				    sql += "\n AND cic.id="+id_cic;
			   sql += "\n ORDER BY cic.id, gra.id, au.secc, pera.ape_pat, pera.ape_mat, pera.nom";
		return sqlUtil.query(sql, new Object[]{id_niv, id_gir, id_anio});
	}
	
	/**
	 * Listar Alumnos por Periodo
	 * @param id_per
	 * @return
	 */
	public List<Row> listarAlumnosxGradoAnio(Integer id_gra, Integer id_anio){

		String sql = "SELECT  mat.id id_mat, alu.`id` id_alu, pera.nro_doc, pera.ape_pat, pera.ape_mat, pera.nom, alu.usuario, alu.pass_educando, CONCAT(pera.ape_pat,' ', pera.ape_mat,' ', pera.nom) alumno, gra.nom grado, gra.abrv_classroom, niv.nom nivel, au.secc, "
				+ " fam.id id_fam, perf.ape_pat fam_ape_pat, perf.ape_mat fam_ape_mat, perf.nom fam_nom, perf.corr fam_corr, perf.cel fam_cel"
				+ " FROM `per_periodo` per INNER JOIN `col_aula` au ON per.`id`=au.`id_per`"
				+ " INNER JOIN `mat_matricula` mat ON au.`id`=mat.`id_au_asi`"
				//+ " INNER JOIN alu_familiar fam ON mat.id_fam=fam.id "
				+ " INNER JOIN `alu_alumno` alu ON mat.`id_alu`=alu.`id`"
				+ " INNER JOIN `col_persona` pera ON alu.`id_per`=pera.`id`"
				+ " INNER JOIN alu_gru_fam_alumno agfa ON alu.id=agfa.id_alu"
				+ " INNER JOIN alu_gru_fam_familiar agff ON agfa.id_gpf=agff.id_gpf"
				+ " INNER JOIN alu_familiar fam ON agff.id_fam=fam.id"
				+ " INNER JOIN col_persona perf ON fam.id_per=perf.id"
				+ " INNER JOIN cat_grad gra ON au.id_grad=gra.id"
				+ " INNER JOIN cat_nivel niv ON gra.id_nvl=niv.id"
				+ " WHERE per.`id_anio`=? AND au.id_grad=? and au.id=373 and alu.id=4080 " //  and alu.id=3416
				+ " ORDER BY alu.ape_pat, alu.ape_mat, alu.nom";
		
		return SQLFrmkUtil.listToRows(jdbcTemplate.queryForList(sql, new Object[]{ id_anio, id_gra}));
	}
	
	public List<Row> listarAlumnosxGradoAnioSinPF(Integer id_gra, Integer id_anio, Integer id_gir){

		String sql = "SELECT distinct  mat.id id_mat, alu.`id` id_alu, pera.nro_doc, pera.ape_pat, pera.ape_mat, pera.nom, alu.usuario, alu.pass_educando, CONCAT(pera.ape_pat,' ', pera.ape_mat,' ', pera.nom) alumno, gra.nom grado, gra.abrv_classroom, niv.nom nivel, au.secc "
				+ " FROM `per_periodo` per INNER JOIN `col_aula` au ON per.`id`=au.`id_per`"
				+ " INNER JOIN `mat_matricula` mat ON au.`id`=mat.`id_au_asi`"
				//+ " INNER JOIN alu_familiar fam ON mat.id_fam=fam.id "
				+ " INNER JOIN `ges_sucursal` suc ON per.`id_suc`=suc.`id`"
				+ " INNER JOIN `ges_servicio` srv ON per.`id_srv`=srv.`id`"
				+ " INNER JOIN `alu_alumno` alu ON mat.`id_alu`=alu.`id`"
				+ " INNER JOIN `col_persona` pera ON alu.`id_per`=pera.`id`"
				+ " INNER JOIN cat_grad gra ON au.id_grad=gra.id"
				+ " INNER JOIN cat_nivel niv ON gra.id_nvl=niv.id"
				+ " WHERE per.`id_anio`=? AND au.id_grad=? AND srv.id_gir=? " //  and alu.id=3416
				+ " ORDER BY pera.ape_pat, pera.ape_mat, pera.nom";
		
		return SQLFrmkUtil.listToRows(jdbcTemplate.queryForList(sql, new Object[]{ id_anio, id_gra, id_gir}));
	}
	
	public List<Row> listarApoderadosxAnio(Integer id_gra, Integer id_anio){

		String sql = "SELECT distinct"
				+ " fam.id id_fam, fam.ape_pat fam_ape_pat, fam.ape_mat fam_ape_mat, fam.nom fam_nom, fam.nro_doc fam_nro_doc, fam.corr fam_corr, fam.cel fam_cel, fam.pass fam_pass"
				+ " FROM `per_periodo` per INNER JOIN `col_aula` au ON per.`id`=au.`id_per`"
				+ " INNER JOIN `mat_matricula` mat ON au.`id`=mat.`id_au_asi`"
				+ " INNER JOIN `alu_alumno` alu ON mat.`id_alu`=alu.`id`"
				+ " INNER JOIN alu_familiar fam ON mat.id_fam=fam.id"
				+ " INNER JOIN cat_grad gra ON au.id_grad=gra.id"
				+ " INNER JOIN cat_nivel niv ON gra.id_nvl=niv.id"
				+ " WHERE per.`id_anio`=? AND au.id_grad=? " // and alu.id=3416
				+ " ORDER BY alu.ape_pat, alu.ape_mat, alu.nom";
		
		return SQLFrmkUtil.listToRows(jdbcTemplate.queryForList(sql, new Object[]{ id_anio, id_gra}));
	}
	
	/**
	 * lista todos los alumnos
	 * @param id_per
	 * @return
	 */
	public List<Row> listarAlumnosxPeriodo(Integer id_per){

		String sql = "SELECT mat.id id_mat, alu.`id` id_alu, alu.ape_pat, alu.ape_mat, alu.nom, alu.usuario, alu.pass_educando, CONCAT(alu.ape_pat,' ', alu.ape_mat,' ', alu.nom) alumno, gra.nom grado, niv.nom nivel"
				//+ " fam.id id_fam, fam.ape_pat fam_ape_pat, fam.ape_mat fam_ape_mat, fam.nom fam_nom, fam.corr fam_corr, fam.cel fam_cel"
				+ " FROM `per_periodo` per INNER JOIN `col_aula` au ON per.`id`=au.`id_per`"
				+ " INNER JOIN `mat_matricula` mat ON au.`id`=mat.`id_au_asi`"
				+ " INNER JOIN `alu_alumno` alu ON mat.`id_alu`=alu.`id`"
				+ " INNER JOIN cat_grad gra ON au.id_grad=gra.id"
				+ " INNER JOIN cat_nivel niv ON gra.id_nvl=niv.id"
				+ " INNER JOIN cvi_inscripcion_campus ic on ic.id_alu= alu.id and  tc_acept=1"
				+ " WHERE per.id=?"
				+ " ORDER BY alu.ape_pat, alu.ape_mat, alu.nom";
		
		return SQLFrmkUtil.listToRows(jdbcTemplate.queryForList(sql, new Object[]{ id_per}));
	}

	public List<Row> listarAlumnosxAnio(Integer id_anio){

		String sql = "SELECT mat.id id_mat, alu.`id` id_alu, alu.ape_pat, alu.ape_mat, alu.nom, alu.usuario, alu.pass_educando, CONCAT(alu.ape_pat,' ', alu.ape_mat,' ', alu.nom) alumno, gra.nom grado, niv.nom nivel"
				//+ " fam.id id_fam, fam.ape_pat fam_ape_pat, fam.ape_mat fam_ape_mat, fam.nom fam_nom, fam.corr fam_corr, fam.cel fam_cel"
				+ " FROM `per_periodo` per INNER JOIN `col_aula` au ON per.`id`=au.`id_per`"
				+ " INNER JOIN `mat_matricula` mat ON au.`id`=mat.`id_au_asi`"
				+ " INNER JOIN `alu_alumno` alu ON mat.`id_alu`=alu.`id`"
				+ " INNER JOIN cat_grad gra ON au.id_grad=gra.id"
				+ " INNER JOIN cat_nivel niv ON gra.id_nvl=niv.id"
				+ " INNER JOIN cvi_inscripcion_campus ic on ic.id_alu= alu.id and  tc_acept=1"
				+ " WHERE per.id_anio=? "
				+ " ORDER BY alu.ape_pat, alu.ape_mat, alu.nom";
		
		return SQLFrmkUtil.listToRows(jdbcTemplate.queryForList(sql, new Object[]{ id_anio}));
	}
	
	public List<Row> listarMatriculasxAnio(Integer id_anio){

		String sql = "SELECT mat.id id_mat, alu.`id` id_alu, fam.id id_fam, p.ape_pat, p.ape_mat, p.nom, alu.usuario, alu.pass_educando, CONCAT(p.ape_pat,' ', p.ape_mat,' ', p.nom) alumno, pf.corr, gra.nom grado, niv.nom nivel, suc.nom sucursal, tur.nom turno "
				+ "\n FROM `per_periodo` per INNER JOIN `col_aula` au ON per.`id`=au.`id_per`"
				+ "\n INNER JOIN col_turno_aula cta ON au.id=cta.id_au"
				+ "\n INNER JOIN col_ciclo_turno cit ON cit.id=cta.id_cit and au.id_cic=cit.id_cic"
				+ "\n INNER JOIN col_turno tur ON cit.id_tur=tur.id"
				+ "\n INNER JOIN `mat_matricula` mat ON au.`id`=mat.`id_au_asi`"
				+ "\n INNER JOIN `alu_alumno` alu ON mat.`id_alu`=alu.`id`"
				+ "\n INNER JOIN col_persona p ON alu.id_per=p.id"
				+ "\n INNER JOIN alu_familiar fam ON mat.id_fam=fam.id "
				+ "\n INNER JOIN col_persona pf ON fam.id_per=pf.id"
				+ "\n INNER JOIN cat_grad gra ON au.id_grad=gra.id"
				+ "\n INNER JOIN cat_nivel niv ON gra.id_nvl=niv.id"
				+ "\n INNER JOIN ges_sucursal suc ON per.id_suc=suc.id "
				+ "\n INNER JOIN `fac_academico_pago` fac ON mat.`id`=fac.`id_mat` AND fac.`tip`='MAT' " 
				+ "\n WHERE per.id_anio=? AND fac.`canc`=1 and per.id_tpe=1 AND (mat.corr_env_val NOT IN (1) OR mat.`corr_env_val` IS NULL) AND (mat.id_sit NOT IN (5) OR mat.id_sit IS NULL)"
				+ "\n ORDER BY p.ape_pat, p.ape_mat, p.nom";
		
		return SQLFrmkUtil.listToRows(jdbcTemplate.queryForList(sql, new Object[]{ id_anio}));
	}
	
	public List<Row> listarMatriculasxAnioPendientesEnvioTutorial(Integer id_anio){

		String sql = "SELECT mat.id id_mat, alu.`id` id_alu, fam.id id_fam, p.ape_pat, p.ape_mat, p.nom, alu.usuario, alu.pass_educando, CONCAT(p.ape_pat,' ', p.ape_mat,' ', p.nom) alumno, pf.corr, gra.nom grado, niv.nom nivel, suc.nom sucursal, tur.nom turno, fac.id_bco_pag "
				+ "\n FROM `per_periodo` per INNER JOIN `col_aula` au ON per.`id`=au.`id_per`"
				+ "\n INNER JOIN col_turno_aula cta ON au.id=cta.id_au"
				+ "\n INNER JOIN col_ciclo_turno cit ON cit.id=cta.id_cit and au.id_cic=cit.id_cic"
				+ "\n INNER JOIN col_turno tur ON cit.id_tur=tur.id"
				+ "\n INNER JOIN `mat_matricula` mat ON au.`id`=mat.`id_au_asi`"
				+ "\n INNER JOIN `alu_alumno` alu ON mat.`id_alu`=alu.`id`"
				+ "\n INNER JOIN col_persona p ON alu.id_per=p.id"
				+ "\n INNER JOIN alu_familiar fam ON mat.id_fam=fam.id "
				+ "\n INNER JOIN col_persona pf ON fam.id_per=pf.id"
				+ "\n INNER JOIN cat_grad gra ON au.id_grad=gra.id"
				+ "\n INNER JOIN cat_nivel niv ON gra.id_nvl=niv.id"
				+ "\n INNER JOIN ges_sucursal suc ON per.id_suc=suc.id "
				+ "\n INNER JOIN `fac_academico_pago` fac ON mat.`id`=fac.`id_mat` AND fac.`tip`='MAT' " 
				+ "\n WHERE per.id_anio=? AND fac.`canc`=1 and per.id_tpe=1 AND (mat.corr_env_tut NOT IN (1) OR mat.`corr_env_tut` IS NULL) " //AND mat.id=17928
				+ "\n ORDER BY p.ape_pat, p.ape_mat, p.nom";
		
		return SQLFrmkUtil.listToRows(jdbcTemplate.queryForList(sql, new Object[]{ id_anio}));
	}
	
	public List<Row> listarMatriculasxAnioPendientesEnvioUsrPsw(Integer id_anio){

		String sql = "SELECT mat.id id_mat, alu.`id` id_alu, fam.id id_fam, p.ape_pat, p.ape_mat, p.nom, alu.usuario, alu.pass_educando, alu.pass_google, CONCAT(p.ape_pat,' ', p.ape_mat,' ', p.nom) alumno, pf.corr, gra.nom grado, niv.nom nivel, suc.nom sucursal, tur.nom turno "
				+ "\n FROM `per_periodo` per INNER JOIN `col_aula` au ON per.`id`=au.`id_per`"
				+ "\n INNER JOIN col_turno_aula cta ON au.id=cta.id_au"
				+ "\n INNER JOIN col_ciclo_turno cit ON cit.id=cta.id_cit and au.id_cic=cit.id_cic"
				+ "\n INNER JOIN col_turno tur ON cit.id_tur=tur.id"
				+ "\n INNER JOIN `mat_matricula` mat ON au.`id`=mat.`id_au_asi`"
				+ "\n INNER JOIN `alu_alumno` alu ON mat.`id_alu`=alu.`id`"
				+ "\n INNER JOIN col_persona p ON alu.id_per=p.id"
				+ "\n INNER JOIN alu_familiar fam ON mat.id_fam=fam.id "
				+ "\n INNER JOIN col_persona pf ON fam.id_per=pf.id"
				+ "\n INNER JOIN cat_grad gra ON au.id_grad=gra.id"
				+ "\n INNER JOIN cat_nivel niv ON gra.id_nvl=niv.id"
				+ "\n INNER JOIN ges_sucursal suc ON per.id_suc=suc.id "
				+ "\n INNER JOIN `fac_academico_pago` fac ON mat.`id`=fac.`id_mat` AND fac.`tip`='MAT' " 
				+ "\n WHERE per.id_anio=? AND fac.`canc`=1 AND alu.pass_google IS NOT NULL and per.id_tpe=1 AND (mat.corr_env_usr NOT IN (1) OR mat.`corr_env_usr` IS NULL) " //AND mat.id=17928
				+ "\n ORDER BY p.ape_pat, p.ape_mat, p.nom";
		
		return SQLFrmkUtil.listToRows(jdbcTemplate.queryForList(sql, new Object[]{ id_anio}));
	}
	
	public List<Row> listarMatriculasxAnioCiclo(Integer id_cic){

		String sql = "SELECT mat.id id_mat, alu.`id` id_alu, fam.id id_fam, p.ape_pat, p.ape_mat, p.nom, alu.usuario, alu.pass_educando, CONCAT(p.ape_pat,' ', p.ape_mat,' ', p.nom) alumno, pf.corr, gra.nom grado, niv.nom nivel, suc.nom sucursal, tur.nom turno "
				+ "\n FROM `per_periodo` per INNER JOIN `col_aula` au ON per.`id`=au.`id_per`"
				+ "\n INNER JOIN col_turno_aula cta ON au.id=cta.id_au"
				+ "\n INNER JOIN col_ciclo_turno cit ON cit.id=cta.id_cit and au.id_cic=cit.id_cic"
				+ "\n INNER JOIN col_turno tur ON cit.id_tur=tur.id"
				+ "\n INNER JOIN `mat_matricula` mat ON au.`id`=mat.`id_au_asi`"
				+ "\n INNER JOIN `alu_alumno` alu ON mat.`id_alu`=alu.`id`"
				+ "\n INNER JOIN col_persona p ON alu.id_per=p.id"
				+ "\n INNER JOIN alu_familiar fam ON mat.id_fam=fam.id "
				+ "\n INNER JOIN col_persona pf ON fam.id_per=pf.id"
				+ "\n INNER JOIN cat_grad gra ON au.id_grad=gra.id"
				+ "\n INNER JOIN cat_nivel niv ON gra.id_nvl=niv.id"
				+ "\n INNER JOIN ges_sucursal suc ON per.id_suc=suc.id "
				+ "\n INNER JOIN `fac_academico_pago` fac ON mat.`id`=fac.`id_mat` AND fac.`tip`='MAT' " 
				+ "\n WHERE au.id_cic=? AND fac.`canc`=1 and per.id_tpe=1 "
				+ "\n ORDER BY p.ape_pat, p.ape_mat, p.nom";
		
		return SQLFrmkUtil.listToRows(jdbcTemplate.queryForList(sql, new Object[]{ id_cic}));
	}
	
	public List<Row> listarMatriculasxAnioCicloVU(Integer id_cic){

		String sql = "SELECT mat.id id_mat, alu.`id` id_alu, fam.id id_fam, p.ape_pat, p.ape_mat, p.nom, alu.usuario, alu.pass_educando, CONCAT(p.ape_pat,' ', p.ape_mat,' ', p.nom) alumno, pf.corr, gra.nom grado, niv.nom nivel, suc.nom sucursal, tur.nom turno "
				+ "\n FROM `per_periodo` per INNER JOIN `col_aula` au ON per.`id`=au.`id_per`"
				+ "\n INNER JOIN col_turno_aula cta ON au.id=cta.id_au"
				+ "\n INNER JOIN col_ciclo_turno cit ON cit.id=cta.id_cit and au.id_cic=cit.id_cic"
				+ "\n INNER JOIN col_turno tur ON cit.id_tur=tur.id"
				+ "\n INNER JOIN `mat_matricula` mat ON au.`id`=mat.`id_au_asi`"
				+ "\n INNER JOIN `alu_alumno` alu ON mat.`id_alu`=alu.`id`"
				+ "\n INNER JOIN col_persona p ON alu.id_per=p.id"
				+ "\n INNER JOIN cat_grad gra ON au.id_grad=gra.id"
				+ "\n INNER JOIN cat_nivel niv ON gra.id_nvl=niv.id"
				+ "\n INNER JOIN ges_sucursal suc ON per.id_suc=suc.id "
				+ "\n INNER JOIN `fac_academico_pago` fac ON mat.`id`=fac.`id_mat` AND fac.`tip`='MAT' " 
				+ "\n LEFT JOIN alu_familiar fam ON mat.id_fam=fam.id "
				+ "\n LEFT JOIN col_persona pf ON fam.id_per=pf.id"	
				+ "\n WHERE au.id_cic=? AND fac.`canc`=1 and per.id_tpe=5 "
				+ "\n ORDER BY p.ape_pat, p.ape_mat, p.nom";
		
		return SQLFrmkUtil.listToRows(jdbcTemplate.queryForList(sql, new Object[]{ id_cic}));
	}
	
	public List<Row> listarMatriculasValidadasxAula(Integer id_anio, Integer id_au){

		String sql = "SELECT mat.id id_mat, alu.`id` id_alu, fam.id id_fam, p.ape_pat, p.ape_mat, p.nom, alu.usuario, alu.pass_educando, alu.id_classRoom, CONCAT(p.ape_pat,' ', p.ape_mat,' ', p.nom) alumno, pf.corr, gra.nom grado, niv.nom nivel, suc.nom sucursal, tur.nom turno "
				+ "\n FROM `per_periodo` per INNER JOIN `col_aula` au ON per.`id`=au.`id_per`"
				+ "\n INNER JOIN col_turno_aula cta ON au.id=cta.id_au"
				+ "\n INNER JOIN col_ciclo_turno cit ON cit.id=cta.id_cit and au.id_cic=cit.id_cic"
				+ "\n INNER JOIN col_turno tur ON cit.id_tur=tur.id"
				+ "\n INNER JOIN `mat_matricula` mat ON au.`id`=mat.`id_au_asi`"
				+ "\n INNER JOIN `alu_alumno` alu ON mat.`id_alu`=alu.`id`"
				+ "\n INNER JOIN col_persona p ON alu.id_per=p.id"
				+ "\n INNER JOIN alu_familiar fam ON mat.id_fam=fam.id "
				+ "\n INNER JOIN col_persona pf ON fam.id_per=pf.id"
				+ "\n INNER JOIN cat_grad gra ON au.id_grad=gra.id"
				+ "\n INNER JOIN cat_nivel niv ON gra.id_nvl=niv.id"
				+ "\n INNER JOIN ges_sucursal suc ON per.id_suc=suc.id "
				+ "\n INNER JOIN `fac_academico_pago` fac ON mat.`id`=fac.`id_mat` AND fac.`tip`='MAT' " 
				+ "\n WHERE per.id_anio=? AND mat.id_au_asi=? AND fac.`canc`=1 and per.id_tpe=1 AND alu.pass_google IS NOT NULL  " // AND alu.id_classRoom='115340919219883252181'
				+ "\n ORDER BY p.ape_pat, p.ape_mat, p.nom";
		
		return SQLFrmkUtil.listToRows(jdbcTemplate.queryForList(sql, new Object[]{ id_anio, id_au}));
	}
	
	public List<Row> listarMatriculasValidadasxAulaVU(Integer id_anio, Integer id_au){

		String sql = "SELECT mat.id id_mat, alu.`id` id_alu, fam.id id_fam, p.ape_pat, p.ape_mat, p.nom, alu.usuario, alu.pass_educando, alu.id_classRoom, CONCAT(p.ape_pat,' ', p.ape_mat,' ', p.nom) alumno, pf.corr, gra.nom grado, niv.nom nivel, suc.nom sucursal, tur.nom turno "
				+ "\n FROM `per_periodo` per INNER JOIN `col_aula` au ON per.`id`=au.`id_per`"
				+ "\n INNER JOIN col_turno_aula cta ON au.id=cta.id_au"
				+ "\n INNER JOIN col_ciclo_turno cit ON cit.id=cta.id_cit and au.id_cic=cit.id_cic"
				+ "\n INNER JOIN col_turno tur ON cit.id_tur=tur.id"
				+ "\n INNER JOIN `mat_matricula` mat ON au.`id`=mat.`id_au_asi`"
				+ "\n INNER JOIN `alu_alumno` alu ON mat.`id_alu`=alu.`id`"
				+ "\n INNER JOIN col_persona p ON alu.id_per=p.id"
				+ "\n INNER JOIN cat_grad gra ON au.id_grad=gra.id"
				+ "\n INNER JOIN cat_nivel niv ON gra.id_nvl=niv.id"
				+ "\n INNER JOIN ges_sucursal suc ON per.id_suc=suc.id "
				+ "\n INNER JOIN `fac_academico_pago` fac ON mat.`id`=fac.`id_mat` AND fac.`tip`='MAT' " 
				+ "\n LEFT JOIN alu_familiar fam ON mat.id_fam=fam.id "
				+ "\n LEFT JOIN col_persona pf ON fam.id_per=pf.id"		
				+ "\n WHERE per.id_anio=? AND mat.id_au_asi=? AND fac.`canc`=1 and per.id_tpe=5 AND alu.pass_google IS NOT NULL  " // AND alu.id_classRoom='115340919219883252181'
				+ "\n ORDER BY p.ape_pat, p.ape_mat, p.nom";
		
		return SQLFrmkUtil.listToRows(jdbcTemplate.queryForList(sql, new Object[]{ id_anio, id_au}));
	}
	
	public List<Row> listarMatriculasValidadasxAulaAcademia(Integer id_anio, Integer id_au){

		String sql = "SELECT mat.id id_mat, alu.`id` id_alu, p.ape_pat, p.ape_mat, p.nom, alu.usuario, alu.pass_educando, alu.id_classRoom, CONCAT(p.ape_pat,' ', p.ape_mat,' ', p.nom) alumno, gra.nom grado, niv.nom nivel, suc.nom sucursal, tur.nom turno "
				+ "\n FROM `per_periodo` per INNER JOIN `col_aula` au ON per.`id`=au.`id_per`"
				+ "\n INNER JOIN col_turno_aula cta ON au.id=cta.id_au"
				+ "\n INNER JOIN col_ciclo_turno cit ON cit.id=cta.id_cit and au.id_cic=cit.id_cic"
				+ "\n INNER JOIN col_turno tur ON cit.id_tur=tur.id"
				+ "\n INNER JOIN `mat_matricula` mat ON au.`id`=mat.`id_au_asi`"
				+ "\n INNER JOIN `alu_alumno` alu ON mat.`id_alu`=alu.`id`"
				+ "\n INNER JOIN col_persona p ON alu.id_per=p.id"
				//+ "\n INNER JOIN alu_familiar fam ON mat.id_fam=fam.id "
				//+ "\n INNER JOIN col_persona pf ON fam.id_per=pf.id"
				+ "\n INNER JOIN cat_grad gra ON au.id_grad=gra.id"
				+ "\n INNER JOIN cat_nivel niv ON gra.id_nvl=niv.id"
				+ "\n INNER JOIN ges_sucursal suc ON per.id_suc=suc.id "
				+ "\n INNER JOIN `fac_academico_pago` fac ON mat.`id`=fac.`id_mat` AND fac.`tip`='MAT' " 
				+ "\n WHERE per.id_anio=? AND mat.id_au_asi=? AND fac.`canc`=1  AND alu.pass_google IS NOT NULL  " // AND alu.id_classRoom='115340919219883252181'
				+ "\n ORDER BY p.ape_pat, p.ape_mat, p.nom";
		
		return SQLFrmkUtil.listToRows(jdbcTemplate.queryForList(sql, new Object[]{ id_anio, id_au}));
	}
	
	public Row obtenerMatriculaAlumno(Integer id_mat){

		String sql = "SELECT mat.id id_mat, alu.`id` id_alu, alu.ape_pat, alu.ape_mat, alu.nom, alu.usuario, alu.pass_educando, CONCAT(alu.ape_pat,' ', alu.ape_mat,' ', alu.nom) alumno, gra.nom grado, niv.nom nivel"
				//+ " fam.id id_fam, fam.ape_pat fam_ape_pat, fam.ape_mat fam_ape_mat, fam.nom fam_nom, fam.corr fam_corr, fam.cel fam_cel"
				+ " FROM `per_periodo` per INNER JOIN `col_aula` au ON per.`id`=au.`id_per`"
				+ " INNER JOIN `mat_matricula` mat ON au.`id`=mat.`id_au_asi`"
				+ " INNER JOIN `alu_alumno` alu ON mat.`id_alu`=alu.`id`"
				+ " INNER JOIN cat_grad gra ON au.id_grad=gra.id"
				+ " INNER JOIN cat_nivel niv ON gra.id_nvl=niv.id"
				+ " INNER JOIN cvi_inscripcion_campus ic on ic.id_alu= alu.id and  tc_acept=1"
				+ " WHERE mat.id="+id_mat;
				//+ " ORDER BY alu.ape_pat, alu.ape_mat, alu.nom";
		
		List<Row> list = sqlUtil.query(sql);
		return list.get(0);
		
		//return SQLFrmkUtil.listToRows(jdbcTemplate.queryForList(sql, new Object[]{ id_mat}));
	}
	
	public List<Row> obtenerMatriculaColegioxAlumnoAnio(Integer id_alu, Integer id_anio){

		String sql = "SELECT mat.id id_mat, mat.num_cont, mat.id_per, mat.id_gra, mat.id_niv,  alu.`id` id_alu, alu.ape_pat, alu.ape_mat, alu.nom, alu.usuario, alu.pass_educando, CONCAT(alu.ape_pat,' ', alu.ape_mat,' ', alu.nom) alumno, gra.nom grado, niv.nom nivel"
				//+ " fam.id id_fam, fam.ape_pat fam_ape_pat, fam.ape_mat fam_ape_mat, fam.nom fam_nom, fam.corr fam_corr, fam.cel fam_cel"
				+ " FROM `per_periodo` per INNER JOIN `col_aula` au ON per.`id`=au.`id_per`"
				+ " INNER JOIN `mat_matricula` mat ON au.`id`=mat.`id_au_asi`"
				+ " INNER JOIN `alu_alumno` alu ON mat.`id_alu`=alu.`id`"
				+ " INNER JOIN cat_grad gra ON au.id_grad=gra.id"
				+ " INNER JOIN cat_nivel niv ON gra.id_nvl=niv.id"
			//	+ " INNER JOIN cvi_inscripcion_campus ic on ic.id_alu= alu.id and  tc_acept=1"
				+ " WHERE mat.id_alu="+id_alu+" AND per.id_anio="+id_anio+" and (mat.id_sit not in (4,5) OR mat.id_sit IS NULL) AND ( mat.tipo NOT IN('A','V') OR mat.tipo IS NULL)"; // Para el proximo año validar q solo sea de colegio AND mat.tipo NOT IN('A','V') OR mat.tipo IS NULL
				//+ " ORDER BY alu.ape_pat, alu.ape_mat, alu.nom";
		
		return SQLFrmkUtil.listToRows(jdbcTemplate.queryForList(sql));
		
		//return SQLFrmkUtil.listToRows(jdbcTemplate.queryForList(sql, new Object[]{ id_mat}));
	}
	

	public Row getInscripcionAulaVirtual(Integer id_alu) {
		
		String sql = "SELECT alu.`id` id_alu, alu.`ape_pat` alu_ape_pat, alu.`ape_mat` alu_ape_mat, alu.`nom` alu_nom, "
				+ " fam.id id_fam, fam.ape_pat fam_ape_pat, fam.`ape_mat` fam_ape_pat, fam.`nom` fam_nom"
				+ " FROM `mat_matricula` mat INNER JOIN `alu_alumno` alu ON mat.`id_alu`=alu.`id`"
				+ " INNER JOIN `alu_familiar` fam ON mat.`id_fam`=fam.`id`"
				+ " INNER JOIN `cvi_inscripcion_campus` cvi ON cvi.`id_alu`=alu.`id` AND mat.`id_fam`=cvi.`id_fam`"
				+ " WHERE mat.`id_alu`="+id_alu ;
		List<Row> list = sqlUtil.query(sql);
		return list.get(0);
	}
	
	/**
	 * Listar a los no matriculados
	 * @param id_anio
	 * @return
	 */
	public List<Row> listarNoMatriculados(String alumno,Integer id_anio){

		String sql = "SELECT distinct alu.`id` id_alu, alu.`nro_doc`, alu.`ape_pat`, alu.`ape_mat`, alu.`nom`, NULL id_mat, NULL tipo, agfa.id_gpf "
				+ "\n FROM `alu_alumno` alu INNER JOIN `alu_gru_fam_alumno` agfa ON alu.id=agfa.`id_alu`"
				+ "\n WHERE alu.id NOT IN (SELECT mat.`id_alu` FROM `mat_matricula` mat INNER JOIN `per_periodo` per ON mat.`id_per`=per.`id` WHERE per.`id_anio`=?)"
				+ "\n AND  UPPER(CONCAT(alu.ape_pat,' ',alu.ape_mat, ' ', alu.nom)) LIKE '%" + alumno.toUpperCase() + "%'"
				+ "\n ORDER BY alu.ape_pat, alu.ape_mat, alu.nom";
		
		return SQLFrmkUtil.listToRows(jdbcTemplate.queryForList(sql, new Object[]{ id_anio}));
	}
	
	public void actualizarFamiliarEmergencia(Integer id_mat, Integer id_fam_emer){
		String sql = "update mat_matricula set id_fam_emer=?, fec_act=curdate() , usr_act=? where id = ?"; 
		sqlUtil.update(sql, new Object[]{id_fam_emer, tokenSeguridad.getId(), id_mat});
	}
	
	public List<Row> listarMatriculaCiclo(Integer id_alu, Integer id_anio){

		String sql = "SELECT DISTINCT mat.`id`, alu.`id` id_alu, CONCAT(pers.ape_pat,' ', pers.ape_mat,' ', pers.nom) alumno, mat.id_au_asi, niv.`nom` nivel, gra.`nom` grado, au.`secc`, tur.`nom` turno,\r\n" + 
				" IF(mat.`tipo`='A','Academia','Vacaciones') giro, cic.`nom` ciclo, fac.fec_venc, IF(fac.canc=1,'Cancelado Matrícula','Pendiente') situacion, fac.canc \r\n" + 
				" FROM  `mat_matricula` mat INNER JOIN `alu_alumno` alu ON mat.`id_alu`=alu.`id`\r\n" + 
				" INNER JOIN col_persona pers ON alu.id_per=pers.id "+
				" INNER JOIN `col_ciclo` cic ON mat.`id_cic`=cic.`id`\r\n" +
				" INNER JOIN `per_periodo` per ON cic.`id_per`=per.`id`\r\n" + 
				" INNER JOIN `col_ciclo_turno` cct ON cic.`id`=cct.`id_cic` and mat.id_cct=cct.id\r\n" + 
				" INNER JOIN `col_turno` tur ON cct.`id_tur`=tur.`id`\r\n" + 
				" LEFT JOIN `col_aula` au ON mat.`id_au_asi`=au.`id`\r\n" + 
				" LEFT JOIN `col_turno_aula` cta ON cta.`id_au`=au.`id` AND cta.`id_cit`=cct.`id`\r\n" + 
				" INNER JOIN `cat_grad` gra ON mat.`id_gra`=gra.`id`\r\n" + 
				" INNER JOIN `cat_nivel` niv ON gra.`id_nvl`=niv.`id`\r\n" + 
				" INNER JOIN fac_academico_pago fac ON mat.id=fac.id_mat"+
				" WHERE mat.`id_alu`=? AND fac.nro_cuota=1 and (mat.tipo='A' OR mat.tipo='V') AND per.id_anio=? "+
				" UNION ALL "+
				"SELECT DISTINCT mat.`id`, alu.`id` id_alu, CONCAT(pers.ape_pat,' ', pers.ape_mat,' ', pers.nom) alumno, mat.id_au_asi, niv.`nom` nivel, gra.`nom` grado, au.`secc`, tur.`nom` turno,\r\n" + 
				" IF(mat.`tipo`='C','Colegio','') giro, cic.`nom` ciclo, fac.fec_venc, IF(fac.canc=1,'Cancelado Matrícula','Pendiente') situacion, fac.canc \r\n" + 
				" FROM  `mat_matricula` mat INNER JOIN `alu_alumno` alu ON mat.`id_alu`=alu.`id`\r\n" + 
				" INNER JOIN col_persona pers ON alu.id_per=pers.id "+
				" INNER JOIN `col_ciclo` cic ON mat.`id_cic`=cic.`id`\r\n" + 
				" INNER JOIN `per_periodo` per ON cic.`id_per`=per.`id`\r\n" +
				" INNER JOIN `col_ciclo_turno` cct ON cic.`id`=cct.`id_cic` \r\n" + 
				" INNER JOIN `col_turno` tur ON cct.`id_tur`=tur.`id`\r\n" + 
				" INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.`id`\r\n" + 
				" INNER JOIN `col_turno_aula` cta ON cta.`id_au`=au.`id` AND cta.`id_cit`=cct.`id`\r\n" + 
				" INNER JOIN `cat_grad` gra ON mat.`id_gra`=gra.`id`\r\n" + 
				" INNER JOIN `cat_nivel` niv ON gra.`id_nvl`=niv.`id`\r\n" + 
				" INNER JOIN fac_academico_pago fac ON mat.id=fac.id_mat"+
				" WHERE mat.`id_alu`=? AND fac.tip='MAT' AND mat.tipo='C' AND per.id_anio=? ";
		
		return SQLFrmkUtil.listToRows(jdbcTemplate.queryForList(sql, new Object[]{id_alu,id_anio,id_alu, id_anio}));
	}
	
	public Row obtenerDatosMatriculaAcadVac(Integer id_mat) {
		
		String sql = "SELECT mat.*, per.`id_suc` , srv.`id_gir`, cct.`id_tur`, gra.nom grado, niv.nom nivel \n" + 
				"FROM `mat_matricula` mat INNER JOIN `col_ciclo` cic ON mat.`id_cic`=cic.`id`\n" + 
				" INNER JOIN cat_grad gra ON mat.id_gra=gra.id \n"+
				" INNER JOIN cat_nivel niv ON gra.id_nvl=niv.id \n"+
				"INNER JOIN `per_periodo` per ON cic.`id_per`=per.`id`\n" + 
				"INNER JOIN `col_ciclo_turno` cct ON cic.`id`=cct.`id_cic`\n" + 
				"INNER JOIN `ges_servicio` srv ON per.`id_srv`=srv.`id`\n" + 
				"WHERE mat.`id`="+id_mat;
		List<Row> list = sqlUtil.query(sql);
		if(list.size()>0)
		return list.get(0);
		else
			return null;
	}
	
	public Row obtenerDatosMatriculaColegio(Integer id_mat) {
		
		String sql = "SELECT mat.*,p.corr, per.`id_suc`,alu.id id_alu,pa.ape_pat, pa.ape_mat, pa.nom , srv.`id_gir`, cct.`id_tur`, gra.nom grado, niv.nom nivel, suc.nom sucursal \n" + 
				"FROM `mat_matricula` mat INNER JOIN `col_ciclo` cic ON mat.`id_cic`=cic.`id`\n" + 
				" INNER JOIN cat_grad gra ON mat.id_gra=gra.id \n"+
				" INNER JOIN cat_nivel niv ON gra.id_nvl=niv.id \n"+
				"INNER JOIN `per_periodo` per ON cic.`id_per`=per.`id`\n" + 
				"INNER JOIN `col_ciclo_turno` cct ON cic.`id`=cct.`id_cic`\n" + 
				"INNER JOIN `ges_servicio` srv ON per.`id_srv`=srv.`id`\n" + 
				"INNER JOIN alu_familiar fam ON mat.id_fam=fam.id \n"+
				"INNER JOIN col_persona p ON fam.id_per=p.id \n"+
				"INNER JOIN alu_alumno alu ON mat.id_alu=alu.id \n"+
				"INNER JOIN col_persona pa ON alu.id_per=pa.id \n"+
				"INNER JOIN ges_sucursal suc ON per.id_suc=suc.id \n"+
				"WHERE mat.`id`="+id_mat;
		List<Row> list = sqlUtil.query(sql);
		if(list.size()>0)
		return list.get(0);
		else
			return null;
	}		
	
	public void actualizarMatriculaGradoAula(Integer id_mat, Integer id_grad, Integer id_au){
		String sql = "update mat_matricula set id_gra=?, id_au_asi=?, fec_act=curdate() , usr_act=? where id = ?"; 
		sqlUtil.update(sql, new Object[]{id_grad, id_au, tokenSeguridad.getId(), id_mat});
	}
	
	public void validarContrato(Integer id_mat){
		String sql = "update mat_matricula set con_val=1, fec_act=curdate() , usr_act=? where id = ?"; 
		sqlUtil.update(sql, new Object[]{tokenSeguridad.getId(), id_mat});
	}
	
	public void validarMatricula(Integer id_mat){
		String sql = "update mat_matricula set mat_val=1, fec_act=curdate() , usr_act=? where id = ?"; 
		sqlUtil.update(sql, new Object[]{tokenSeguridad.getId(), id_mat});
	}
	
	public void validarContratoxUsuario(Integer id_mat){
		String sql = "update mat_matricula set con_val=1, fec_act=curdate() , usr_act=?, usr_val_con=? where id = ?"; 
		sqlUtil.update(sql, new Object[]{tokenSeguridad.getId(),  tokenSeguridad.getId(), id_mat});
	}
	
	public void validarContratoxUsuarioCarga(Integer id_mat){
		String sql = "update mat_matricula set con_val=1, fec_act=curdate()  where id = ?"; 
		sqlUtil.update(sql, new Object[]{ id_mat});
	}
	
	public List<Row> listaFamiliasxAnio(Integer id_anio){

		String sql = "SELECT DISTINCT usr.`login`, usr.`password`, p.corr,p.`ape_pat`,p.`ape_mat`,p.`nom`, agf.* FROM `alu_gru_fam` agf INNER JOIN `alu_gru_fam_alumno` agfa ON agf.`id`=agfa.`id_gpf`\r\n" + 
				" INNER JOIN `mat_matricula` mat ON agfa.`id_alu`=mat.`id_alu`\r\n" + 
				" INNER JOIN `per_periodo` per ON mat.`id_per`=per.`id`\r\n" + 
				" INNER JOIN `cvi_inscripcion_campus` cic ON cic.`id_alu`=mat.`id_alu` AND cic.`id_anio`=per.`id_anio`\r\n" + 
				" INNER JOIN `seg_usuario` usr ON agf.`id_usr`=usr.`id`\r\n" + 
				" INNER JOIN `alu_familiar` fam ON mat.`id_fam`=fam.`id`\r\n" + 
				" INNER JOIN `col_persona` p ON fam.`id_per`=p.id\r\n" + 
				" WHERE mat.`id_sit`=1 AND per.`id_anio`="+id_anio+" ORDER BY agf.nom"; //AND agf.id=2629
		
		return SQLFrmkUtil.listToRows(jdbcTemplate.queryForList(sql));
	}
	
	public List<Row> listaDatosFamiliasxAnio(Integer id_anio, Integer id_gir){

		String sql = "SELECT DISTINCT agf.id, per.`cel`, per.`corr`, agf.`nom`, usr.`login`, usr.`password`, agf.`corr_env_usr`\r\n" + 
				"FROM `mat_matricula` mat INNER JOIN `alu_alumno` alu ON mat.`id_alu`=alu.`id`\r\n" + 
				"INNER JOIN `alu_gru_fam_alumno` agfa ON alu.`id`=agfa.`id_alu`\r\n" + 
				"INNER JOIN `alu_gru_fam` agf ON agfa.`id_gpf`=agf.`id`\r\n" + 
				"INNER JOIN `alu_familiar` fam ON mat.`id_fam`=fam.`id`\r\n" + 
				"INNER JOIN `col_persona` per ON fam.`id_per`=per.`id`\r\n" + 
				"INNER JOIN `alu_gru_fam_familiar` agff ON agff.`id_fam`=fam.`id` AND agff.`id_gpf`=agfa.`id_gpf`\r\n" + 
				"INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.`id`\r\n" + 
				"INNER JOIN `per_periodo` peri ON au.`id_per`=peri.`id`\r\n" + 
				"INNER JOIN `ges_servicio` srv ON srv.`id`=peri.`id_srv`\r\n" + 
				"INNER JOIN `seg_usuario` usr ON agf.`id_usr`=usr.`id`\r\n" +  //AND mat.id=17930
				"WHERE peri.`id_anio`="+id_anio+" AND srv.`id_gir`="+id_gir+"  AND (mat.`id_sit`<>5 OR mat.`id_sit` IS NULL) AND mat.`id_gra`<>14 AND agf.`corr_env_usr` IS NULL\r\n" + 
				"ORDER BY agf.`nom`;"; //AND agf.id=2629
		
		return SQLFrmkUtil.listToRows(jdbcTemplate.queryForList(sql));
	}
	
	public List<Row> listaMatriculasValidasNoValidadas(Integer id_anio, Integer id_gir, Integer id_tip){

		String sql = "SELECT DISTINCT fam.id id_fam, mat.`num_cont`, mat.env_doc, mat.env_decla, mat.env_pro, CONCAT(p.ape_pat,' ', p.`ape_mat`,' ', p.`nom`) familiar, agf.nom familia, concat(pert.ape_pat,' ', pert.ape_mat,' ', pert.nom) trabajador, p.`nro_doc`,agf.direccion dir, bco.`nom` banco, p.`corr`, p.`cel`, mat.`con_val`, usr.`login` \n" ; 
				sql += "FROM `mat_matricula` mat INNER JOIN alu_familiar fam ON mat.`id_fam`=fam.`id`\n" ;
				sql += "INNER JOIN `alu_alumno` alu ON mat.`id_alu`=alu.`id` \n";
				sql += "INNER JOIN `alu_gru_fam_alumno` agfa ON agfa.`id_alu`=alu.`id` \n";
				sql += "INNER JOIN `per_periodo` per ON mat.`id_per`=per.`id` \n" ; 
				sql += "INNER JOIN alu_gru_fam_familiar agff ON fam.id=agff.id_fam AND agff.`id_gpf`=agfa.`id_gpf` \n";
				sql += "INNER JOIN alu_gru_fam agf ON agff.id_gpf=agf.id \n";
				sql += "INNER JOIN ges_servicio srv ON per.id_srv=srv.id \n";
				sql += "INNER JOIN `col_persona` p ON fam.`id_per`=p.`id`\n" ; 
				sql += "INNER JOIN `fac_academico_pago` fac ON mat.id=fac.`id_mat` AND fac.`tip`='MAT'\n"; 
				sql += "INNER JOIN `fac_banco` bco ON fac.`id_bco_pag`=bco.`id`\n" ;
				sql += "LEFT JOIN `seg_usuario` usr ON mat.usr_val_con=usr.id \n" ; 
				sql += " LEFT JOIN ges_trabajador tra on usr.id_tra=tra.id ";
				sql += " LEFT JOIN col_persona pert ON tra.id_per=pert.id ";
				sql += "WHERE per.`id_anio`="+id_anio+" and srv.id_gir="+id_gir+" \n" ; 
				if(id_tip!=null) {
					if(id_tip.equals(1)) {
						sql += "AND mat.con_val=1 ";
					} else if(id_tip.equals(0)) {
						sql += "AND mat.con_val IS NULL ";
					}
				}
				sql += "ORDER BY mat.`num_cont`";
		
		return SQLFrmkUtil.listToRows(jdbcTemplate.queryForList(sql));
	}
	
	public void actualizarestadoEnviadoCorreo(Integer id_mat){
		String sql = "update mat_matricula set corr_env_val=1, mat_val=1, fec_act=curdate() , usr_act=? where id = ?"; 
		sqlUtil.update(sql, new Object[]{tokenSeguridad.getId(), id_mat});
	}
	
	public void actualizarestadoEnviadoTutorial(Integer id_mat){
		String sql = "update mat_matricula set corr_env_tut=1, fec_act=curdate() , usr_act=? where id = ?"; 
		sqlUtil.update(sql, new Object[]{tokenSeguridad.getId(), id_mat});
	}
	
	public void actualizarestadoEnvioAccesos(Integer id_mat){
		String sql = "update mat_matricula set corr_env_usr=1, fec_act=curdate() , usr_act=? where id = ?"; 
		sqlUtil.update(sql, new Object[]{tokenSeguridad.getId(), id_mat});
	}
	
	public void actualizarestadoEnviadoCorreoFamilia(Integer id_gpf){
		String sql = "update alu_gru_fam set corr_env_usr=1, fec_act=curdate() , usr_act=? where id = ?"; 
		sqlUtil.update(sql, new Object[]{tokenSeguridad.getId(), id_gpf});
	}
	
	public void actualizarestadoEnvioDoc(Integer id_mat){
		String sql = "update mat_matricula set env_doc=1, fec_act=curdate()  where id = ?"; 
		sqlUtil.update(sql, new Object[]{id_mat});
	}
	
	public void actualizarestadoEnvioDecla(Integer id_mat){
		String sql = "update mat_matricula set env_decla=1, fec_act=curdate()  where id = ?"; 
		sqlUtil.update(sql, new Object[]{id_mat});
	}
	
	public void actualizarestadoEnvioPro(Integer id_mat){
		String sql = "update mat_matricula set env_pro=1, fec_act=curdate()  where id = ?"; 
		sqlUtil.update(sql, new Object[]{id_mat});
	}
	
	public void actualizarRatificacion(Integer id_rat, Integer res){
		String sql = "update mat_ratificacion set res=?, fec_act=curdate()  where id = ?"; 
		sqlUtil.update(sql, new Object[]{res,id_rat});
	}
	
	public void inactivarMatricula(Integer id_mat){
		String sql = "update mat_matricula set id_sit=5, est='I', fec_act=curdate()  where id = ?"; 
		sqlUtil.update(sql, new Object[]{id_mat});
	}
	
	public List<Row> listarMatriculasContratoEnv(Integer id_gpf, Integer id_anio){

		String sql = "SELECT * from mat_matricula mat inner join alu_alumno alu on mat.id_alu=alu.id inner join col_aula au on mat.id_au_asi=au.id inner join per_periodo per on au.id_per=per.id inner join alu_gru_fam_alumno agfa on agfa.id_alu=alu.id where agfa.id_gpf='"+id_gpf+"' and mat.env_doc=1 and per.id_anio="+id_anio;
		
		return SQLFrmkUtil.listToRows(jdbcTemplate.queryForList(sql));
	}
	
	public List<Row> listarMatriculasDeclaEnv(Integer id_gpf, Integer id_anio){

		String sql = "SELECT * from mat_matricula mat inner join alu_alumno alu on mat.id_alu=alu.id inner join col_aula au on mat.id_au_asi=au.id inner join per_periodo per on au.id_per=per.id inner join alu_gru_fam_alumno agfa on agfa.id_alu=alu.id where agfa.id_gpf='"+id_gpf+"' and mat.env_decla=1 and per.id_anio="+id_anio;
		
		return SQLFrmkUtil.listToRows(jdbcTemplate.queryForList(sql));
	}
	
	public List<Row> listarMatriculasProtocoloEnv(Integer id_gpf, Integer id_anio){

		String sql = "SELECT * from mat_matricula mat inner join alu_alumno alu on mat.id_alu=alu.id inner join col_aula au on mat.id_au_asi=au.id inner join per_periodo per on au.id_per=per.id inner join alu_gru_fam_alumno agfa on agfa.id_alu=alu.id where agfa.id_gpf='"+id_gpf+"' and mat.env_pro=1 and per.id_anio="+id_anio;
		
		return SQLFrmkUtil.listToRows(jdbcTemplate.queryForList(sql));
	}
	
	public List<Row> validarFechaRatificacion() {

		//String sql = "select id from mat_cronograma_ratificacion where id_anio=? and curdate()>=fec_ini AND CURDATE()<=fec_fin ";
		String sql = "select * from mat_cronograma_ratificacion where curdate()>=fec_ini AND CURDATE()<=fec_fin ";

		return SQLFrmkUtil.listToRows(jdbcTemplate.queryForList(sql));
	}
	
	public  List<Row> cantidadMatriculas(Integer id_anio_ant,Integer id_usr) {
		//Buscar la cantidad de matriculas
		String sql = "SELECT mat.* FROM `mat_matricula` mat INNER JOIN `alu_alumno` alu ON mat.id_alu=alu.`id`\n" + 
				"INNER JOIN `alu_gru_fam_alumno` agfa ON alu.`id`=agfa.`id_alu`\n" + 
				"INNER JOIN `alu_gru_fam` agf ON agfa.`id_gpf`=agf.`id`\n" + 
				"INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.`id`\n" + 
				"INNER JOIN `per_periodo` per ON au.`id_per`=per.`id`\n" + 
				"WHERE per.`id_anio`=? AND per.`id_tpe`=1 AND agf.id_usr=? AND mat.`id_gra`<>'14' AND (mat.id_sit IS NULL OR mat.id_sit NOT IN(4,5,6)) AND (mat.id_sit IS NULL OR mat.id_sit NOT IN(4,5,6)) ";

		return SQLFrmkUtil.listToRows(jdbcTemplate.queryForList(sql, new Object[]{id_anio_ant, id_usr}));
	}
	
	public  List<Row> cantidadMatriculasRatificadas(Integer id_anio_ant,Integer id_usr, Integer id_anio) {
		//Buscar la cantidad de matriculas
		String sql = "SELECT mat.* FROM `mat_matricula` mat INNER JOIN `alu_alumno` alu ON mat.id_alu=alu.`id`\n" + 
				"INNER JOIN `alu_gru_fam_alumno` agfa ON alu.`id`=agfa.`id_alu`\n" + 
				"INNER JOIN `alu_gru_fam` agf ON agfa.`id_gpf`=agf.`id`\n" + 
				"INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.`id`\n" + 
				"INNER JOIN `per_periodo` per ON au.`id_per`=per.`id`\n" + 
				"INNER JOIN mat_ratificacion rat ON mat.id=rat.id_mat \n"+
				"WHERE per.`id_anio`=? AND per.`id_tpe`=1 AND agf.id_usr=? AND mat.`id_gra`<>'14' AND rat.id_anio_rat=? AND (mat.id_sit IS NULL OR mat.id_sit NOT IN(4,5,6)) ";

		return SQLFrmkUtil.listToRows(jdbcTemplate.queryForList(sql, new Object[]{id_anio_ant, id_usr, id_anio}));
	}
	
	public  List<Row> hijosNoRatificados(Integer id_anio_ant,Integer id_usr, Integer id_anio) {
		//Buscar la cantidad de matriculas
		String sql = "SELECT rat.id id_rat, concat(pers.ape_pat,' ', pers.ape_mat,' ', pers.nom) alumno, rat.res FROM `mat_matricula` mat INNER JOIN `alu_alumno` alu ON mat.id_alu=alu.`id`\n" + 
				"INNER JOIN col_persona pers ON alu.id_per=pers.`id`\n" + 
				"INNER JOIN `alu_gru_fam_alumno` agfa ON alu.`id`=agfa.`id_alu`\n" + 
				"INNER JOIN `alu_gru_fam` agf ON agfa.`id_gpf`=agf.`id`\n" + 
				"INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.`id`\n" + 
				"INNER JOIN `per_periodo` per ON au.`id_per`=per.`id`\n" + 
				"INNER JOIN mat_ratificacion rat ON mat.id=rat.id_mat \n"+
				"WHERE per.`id_anio`=? AND per.`id_tpe`=1 AND agf.id_usr=? AND mat.`id_gra`<>'14' AND rat.id_anio_rat=? AND rat.res=0 ";

		return SQLFrmkUtil.listToRows(jdbcTemplate.queryForList(sql, new Object[]{id_anio_ant, id_usr, id_anio}));
	}
}
