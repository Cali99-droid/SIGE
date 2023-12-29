package com.sige.mat.dao;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sige.common.enums.EnumFormaPago;
import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.MovimientoDAOImpl;
import com.sige.web.security.TokenSeguridad;
import com.tesla.colegio.model.Movimiento;
import com.tesla.colegio.model.bean.NotaCredito;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;


/**
 * Define m�todos DAO operations para la entidad movimiento.
 * @author MV
 *
 */
@Repository
public class MovimientoDAO extends MovimientoDAOImpl{
	final static Logger logger = Logger.getLogger(MovimientoDAO.class);

	@Autowired
	private TokenSeguridad tokenSeguridad;

	@Autowired
	private SQLUtil sqlUtil;

	public List<Map<String,Object>> consultarReporteCaja_OLD(Date fec_ini, Date fec_fin, Integer id_suc){//TODO MEJORAR ESTA PARAMETRIZACION DE CONCEPTOS
		String sql ="select t.* from (select DISTINCT mov.nro_rec, m.monto_total , IF(ISNULL(mov.id_mat), mov.obs, concat( alu.ape_pat,' ' , alu.ape_mat, ', ', alu.nom) ) obs, con.nom as concepto, mov.tipo, "
				+ "  mov.id_suc,g.nom nivel, gr.nom grado, au.secc, 0 mes, mov.fec fecha "
				+ " from fac_movimiento_detalle m inner join fac_movimiento mov on mov.id= m.id_fmo "
				+ " inner join fac_concepto con on con.id= m.id_fco "
				+ " left join ( mat_matricula mat join alu_alumno alu on alu.id = mat.id_alu )  on mov.id_mat = mat.id "
				+ " left join col_aula au on au.id = mat.id_au"
				+ " left join cat_grad gr on au.id_grad = gr.id"
				+ " left join per_periodo p on p.id = au.id_per "
				+ " left join ges_servicio g on g.id=p.id_srv "
				+ " where mov.fec between ? and ? "
				+ " union all "
				+ " select c.nro_rec, c.monto_total , concat( alu.ape_pat,' ' , alu.ape_mat, ', ', alu.nom) obs, concat('MENSUALIDAD' , ' - ' , m.nom ) as concepto , 'I' tipo , g.id_suc,g.nom nivel, gr.nom grado, au.secc, mens mes, c.fec_pago fecha"
				+ " from  fac_academico_pago c "
				+ " inner join mat_matricula mat on mat.id= c.id_mat "
				+ " inner join col_aula au on au.id = mat.id_au"
				+ " inner join cat_grad gr on au.id_grad = gr.id"
				+ " inner join alu_alumno alu on alu.id= mat.id_alu "
				+ " inner join per_periodo p on p.id = au.id_per "
				+ " inner join ges_servicio g on g.id=p.id_srv "
				+ " left join cat_mes m on c.mens=m.id "
				+ " where c.fec_pago between ? and ?  and canc='1' and tip='MEN' and c.banco is null "
				+ " union all "
				+ " select c.nro_rec, c.monto_total,concat( alu.ape_pat,' ' , alu.ape_mat, ', ', alu.nom) obs, "
				+ " if(c.tip='MAT' , 'MATRICULA','CUOTA DE INGRESO') as concepto , 'I' tipo, g.id_suc,g.nom nivel, gr.nom grado, au.secc, 0 mes, c.fec_pago fecha "
				+ " from  fac_academico_pago c "
				+ " inner join mat_matricula mat on mat.id= c.id_mat "
				+ " inner join col_aula au on au.id = mat.id_au"
				+ " inner join cat_grad gr on au.id_grad = gr.id"
				+ " inner join alu_alumno alu on alu.id= mat.id_alu "
				+ " inner join per_periodo p on p.id =  au.id_per "
				+ " inner join ges_servicio g on g.id=p.id_srv  "
				+ " where c.fec_pago between ? and ? and canc='1' and tip!='MEN'"
				+ " union all"
				+ " SELECT DISTINCT c.nro_recibo nro_rec, c.monto monto_total, CONCAT( alu.ape_pat,' ' , alu.ape_mat, ', ', alu.nom) obs, 'RESERVA' AS concepto, 'I' tipo ," 
				+ " g.id_suc, g.nom nivel, gr.nom grado, au.secc, 0 mes, r.`fec` fecha"
				+ " FROM `fac_reserva_cuota` c"
				+ " INNER JOIN mat_reserva r ON c.id_res=r.id"
				+ " INNER JOIN col_aula au ON au.id = r.id_au"
				+ " INNER JOIN cat_grad gr ON au.id_grad = gr.id"
				+ " INNER JOIN alu_alumno alu ON r.`id_alu`=alu.id"
				+ " INNER JOIN per_periodo p ON p.id = r.id_per"
				+ " INNER JOIN ges_servicio g ON g.id=p.id_srv"  
				+ " WHERE r.`fec` between ? and ?"
				+ ")t ";
		
		if (id_suc!=null && id_suc!=0 )
			sql = sql + " where t.id_suc = " + id_suc;
		sql = sql + " order by 1,mes ";
		
		return jdbcTemplate.queryForList(sql, new Object[]{fec_ini,fec_fin,fec_ini,fec_fin,fec_ini,fec_fin,fec_ini,fec_fin} );
		
		
	}
	
	public List<Map<String,Object>> consultarReporteCaja(Date fec_ini, Date fec_fin, Integer id_suc, Integer id_usr){
		String sql ="select t.* from (select DISTINCT mov.id, usr.login, mov.nro_rec, case when mov.tipo='I' then 'BOLETA' when mov.tipo='N' then 'NOTA DE CREDITO' else '' end as doc, ";
				sql += " if (mov.tipo='I', m.monto_total,m.monto_total*-1  ) monto_total, ";
				sql += " IF(ISNULL(mov.id_mat), m.obs, concat( pa.ape_pat,' ' , pa.ape_mat, ', ', pa.nom) ) obs, con.nom as concepto, mov.tipo, ";
				sql += "  mov.id_suc,g.nom nivel, gr.nom grado, au.secc, 0 mes, mov.fec fecha, b.est baja_est, b.motivo, b.id baja_id ";
				sql += " from fac_movimiento_detalle m inner join fac_movimiento mov on mov.id= m.id_fmo ";
				sql += " inner join fac_concepto con on con.id= m.id_fco ";
				sql += " inner join seg_usuario usr on mov.usr_ins=usr.id ";
				sql += " left join ( mat_matricula mat join alu_alumno alu on alu.id = mat.id_alu join col_persona pa on pa.id=alu.id_per)  on mov.id_mat = mat.id ";
				sql += " left join col_aula au on au.id = mat.id_au_asi";
				sql += " left join cat_grad gr on au.id_grad = gr.id";
				sql += " left join per_periodo p on p.id = au.id_per ";
				sql += " left join ges_servicio g on g.id=p.id_srv ";
				sql += " left join fac_comunicacion_baja b on b.id_fmo=mov.id ";
				sql += " where mov.fec between ? and ? and mov.id_fpa=1 "; //QUE NO SEA DEPOSITO EFECTIVO EN CAJA -- and mov.usr_ins=?
				if(id_usr!=null)
					sql += " and mov.usr_ins="+id_usr;
				sql += ")t ";
		
		if (id_suc!=null && id_suc!=0 )
			sql = sql + " where t.id_suc = " + id_suc;
		sql = sql + " order by 1,mes ";
		
		return jdbcTemplate.queryForList(sql, new Object[]{fec_ini,fec_fin} );
		
		
	}
	
	public List<Map<String,Object>> consultarBoletasAptosNotaCredito(String cliente , String alumno, Integer id_suc){//TODO MEJORAR ESTA PARAMETRIZACION DE CONCEPTOS
		String sql ="select DISTINCT t.* from (select DISTINCT mov.id, mov.nro_rec, case when mov.tipo='I' then 'BOLETA' when mov.tipo='N' then 'NOTA DE CREDITO' else '' end as doc, mat.id id_mat, null id_res,  "
				+ "\n if (mov.tipo='I', mov.monto_total,mov.monto_total*-1  ) monto_total, "
				+ "\n IF(ISNULL(mov.id_mat), m.obs, CONCAT( pera.ape_pat,' ' , pera.ape_mat, ', ', pera.nom) ) obs, con.nom as concepto, mov.tipo, mov.est_env, "
				+ "\n mov.id_suc,g.nom nivel, gr.nom grado, au.secc, 0 mes, mov.fec fecha, nc.est nc_est, nc.motivo, nc.id nc_id, nc.monto nc_monto "
				+ "\n from fac_movimiento_detalle m "
				+ "\n inner join fac_movimiento mov on mov.id= m.id_fmo "
				+ "\n inner join fac_concepto con on con.id= m.id_fco "
				+ "\n LEFT JOIN ( mat_matricula mat JOIN alu_alumno alu ON alu.id = mat.id_alu INNER JOIN col_persona pera ON alu.id_per=pera.id INNER JOIN `fac_academico_pago` fac ON fac.id_mat=mat.id)  ON mov.nro_rec = fac.nro_rec "
				//+ "\n inner join alu_familiar f on f.id =mov.id_fam "
				+ " INNER JOIN col_persona perf ON mov.id_per=perf.id "
				+ "\n left join col_aula au on au.id = mat.id_au"
				+ "\n left join cat_grad gr on au.id_grad = gr.id"
				+ "\n left join per_periodo p on p.id = au.id_per "
				+ "\n left join ges_servicio g on g.id=p.id_srv "
				+ "\n left join fac_nota_credito nc on nc.id_fmo=mov.id "
				+ "\n where mov.tipo = 'I' ";
		
		if(cliente!=null && !"".equals(cliente.trim()))
				sql += "\n and CONCAT(perf.ape_pat,' ' , perf.ape_mat,' ' ,perf.nom) like '%" + cliente.toUpperCase() + "%'";
		
		if(alumno!=null && !"".equals(alumno.trim()))
				sql += "\n and CONCAT(pera.ape_pat,' ' , pera.ape_mat,' ' ,pera.nom) like '%" + alumno.toUpperCase() + "%'";
		sql += "\n UNION ALL ";
		sql += " select DISTINCT mov.id, mov.nro_rec, case when mov.tipo='I' then 'BOLETA' when mov.tipo='N' then 'NOTA DE CREDITO' else '' end as doc, mat.id id_mat, null id_res, ";
		sql += "\n if (mov.tipo='I', mov.monto_total,mov.monto_total*-1  ) monto_total, ";
		sql += "\n IF(ISNULL(mov.id_mat), m.obs, CONCAT( pera.ape_pat,' ' , pera.ape_mat, ', ', pera.nom) ) obs, con.nom as concepto, mov.tipo, mov.est_env, ";
		sql +=  "\n mov.id_suc,g.nom nivel, gr.nom grado, au.secc, 0 mes, mov.fec fecha, nc.est nc_est, nc.motivo, nc.id nc_id, nc.monto nc_monto ";
		sql += "\n from fac_movimiento_detalle m ";
		sql += "\n inner join fac_movimiento mov on mov.id= m.id_fmo ";
		sql += "\n inner join fac_concepto con on con.id= m.id_fco ";
		sql += "\n LEFT JOIN ( mat_matricula mat JOIN alu_alumno alu ON alu.id = mat.id_alu INNER JOIN col_persona pera ON alu.id_per=pera.id )  ON mov.id_mat = mat.id ";
		//+ "\n inner join alu_familiar f on f.id =mov.id_fam "
		sql += " INNER JOIN col_persona perf ON mov.id_per=perf.id ";
		sql += "\n left join col_aula au on au.id = mat.id_au";
		sql += "\n left join cat_grad gr on au.id_grad = gr.id";
		sql += "\n left join per_periodo p on p.id = au.id_per ";
		sql += "\n left join ges_servicio g on g.id=p.id_srv ";
		sql += "\n left join fac_nota_credito nc on nc.id_fmo=mov.id ";
		sql += "\n where mov.tipo = 'I' ";
		if(cliente!=null && !"".equals(cliente.trim()))
			sql += "\n and CONCAT(perf.ape_pat,' ' , perf.ape_mat,' ' ,perf.nom) like '%" + cliente.toUpperCase() + "%'";
		if(alumno!=null && !"".equals(alumno.trim()))
			sql += "\n and CONCAT(pera.ape_pat,' ' , pera.ape_mat,' ' ,pera.nom) like '%" + alumno.toUpperCase() + "%'";
		
		sql += "\n UNION ALL ";
		sql += "\n SELECT DISTINCT mov.id, mov.nro_rec, CASE WHEN mov.tipo='I' THEN 'BOLETA' WHEN mov.tipo='N' THEN 'NOTA DE CREDITO' ELSE '' END AS doc, mat.id id_mat, null id_res, ";
		sql += "\n IF (mov.tipo='I', mov.monto_total,mov.monto_total*-1  ) monto_total, ";
		sql += "\n IF(ISNULL(mov.id_mat), m.obs, CONCAT( pera.ape_pat,' ' , pera.ape_mat, ', ', pera.nom) ) obs, con.nom AS concepto, mov.tipo, mov.est_env,";
		sql += "\n mov.id_suc,g.nom nivel, gr.nom grado, au.secc, 0 mes, mov.fec fecha, nc.est nc_est, nc.motivo, nc.id nc_id, nc.monto nc_monto ";
		sql += "\n FROM fac_movimiento_detalle m ";
		sql += "\n INNER JOIN fac_movimiento mov ON mov.id= m.id_fmo ";
		sql += "\n INNER JOIN fac_concepto con ON con.id= m.id_fco ";
		sql += "\n LEFT JOIN ( mat_matricula mat JOIN alu_alumno alu ON alu.id = mat.id_alu INNER JOIN col_persona pera ON alu.id_per=pera.id )  ON mov.id_mat = mat.id  INNER JOIN col_persona perf ON mat.id_per_res=perf.id ";
		sql += "\n LEFT JOIN col_aula au ON au.id = mat.id_au ";
		sql += "\n LEFT JOIN cat_grad gr ON au.id_grad = gr.id ";
		sql += "\n LEFT JOIN per_periodo p ON p.id = au.id_per ";
		sql += "\n LEFT JOIN ges_servicio g ON g.id=p.id_srv ";
		sql += "\n LEFT JOIN fac_nota_credito nc ON nc.id_fmo=mov.id ";
		sql += "\n WHERE mov.tipo = 'I' ";
		if(cliente!=null && !"".equals(cliente.trim()))
			sql += "\n and CONCAT(perf.ape_pat,' ' , perf.ape_mat,' ' ,perf.nom) like '%" + cliente.toUpperCase() + "%'";	
		if(alumno!=null && !"".equals(alumno.trim()))
			sql += "\n and CONCAT(pera.ape_pat,' ' , pera.ape_mat,' ' ,pera.nom) like '%" + alumno.toUpperCase() + "%'";
		
		sql += "\n UNION ALL ";
		sql += "\n SELECT DISTINCT mov.id, mov.nro_rec, CASE WHEN mov.tipo='I' THEN 'BOLETA' WHEN mov.tipo='N' THEN 'NOTA DE CREDITO' ELSE '' END AS doc, mat.id id_mat,null id_res, ";
		sql += "\n IF (mov.tipo='I', mov.monto_total,mov.monto_total*-1  ) monto_total, ";
		sql += "\n IF(ISNULL(mov.id_mat), m.obs, CONCAT( pera.ape_pat,' ' , pera.ape_mat, ', ', pera.nom) ) obs, con.nom AS concepto, mov.tipo, mov.est_env,";
		sql += "\n mov.id_suc,g.nom nivel, gr.nom grado, au.secc, 0 mes, mov.fec fecha, nc.est nc_est, nc.motivo, nc.id nc_id, nc.monto nc_monto ";
		sql += "\n FROM fac_movimiento_detalle m ";
		sql += "\n INNER JOIN fac_movimiento mov ON mov.id= m.id_fmo ";
		sql += "\n INNER JOIN fac_concepto con ON con.id= m.id_fco ";
		sql += "\n LEFT JOIN ( mat_matricula mat JOIN alu_alumno alu ON alu.id = mat.id_alu INNER JOIN col_persona pera ON alu.id_per=pera.id INNER JOIN alu_familiar fam ON mat.id_fam=fam.id )  ON mov.id_mat = mat.id  INNER JOIN col_persona perf ON fam.id_per=perf.id ";
		sql += "\n LEFT JOIN col_aula au ON au.id = mat.id_au ";
		sql += "\n LEFT JOIN cat_grad gr ON au.id_grad = gr.id ";
		sql += "\n LEFT JOIN per_periodo p ON p.id = au.id_per ";
		sql += "\n LEFT JOIN ges_servicio g ON g.id=p.id_srv ";
		sql += "\n LEFT JOIN fac_nota_credito nc ON nc.id_fmo=mov.id ";
		sql += "\n WHERE mov.tipo = 'I' ";
		if(cliente!=null && !"".equals(cliente.trim()))
			sql += "\n and CONCAT(perf.ape_pat,' ' , perf.ape_mat,' ' ,perf.nom) like '%" + cliente.toUpperCase() + "%'";	
		if(alumno!=null && !"".equals(alumno.trim()))
			sql += "\n and CONCAT(pera.ape_pat,' ' , pera.ape_mat,' ' ,pera.nom) like '%" + alumno.toUpperCase() + "%'";
		sql +="\n UNION ALL ";
		sql += "SELECT DISTINCT mov.id, mov.nro_rec, CASE WHEN mov.tipo='I' THEN 'BOLETA' WHEN mov.tipo='N' THEN 'NOTA DE CREDITO' ELSE '' END AS doc, NULL id_mat,mat.id id_res,  ";
		sql += "\n IF (mov.tipo='I', mov.monto_total,mov.monto_total*-1  ) monto_total, ";
		sql += "\n IF(ISNULL(mov.id_mat), m.obs, CONCAT( pera.ape_pat,' ' , pera.ape_mat, ', ', pera.nom) ) obs, con.nom AS concepto, mov.tipo, mov.est_env, ";  
		sql += "\n mov.id_suc,g.nom nivel, gr.nom grado, NULL secc, 0 mes, mov.fec fecha, nc.est nc_est, nc.motivo, nc.id nc_id, nc.monto nc_monto ";  
		sql += "\n FROM fac_movimiento_detalle m ";  
		sql += "\n INNER JOIN fac_movimiento mov ON mov.id= m.id_fmo ";  
		sql += "\n INNER JOIN fac_concepto con ON con.id= m.id_fco ";  
		sql += "\n LEFT JOIN ( mat_reserva mat JOIN alu_alumno alu ON alu.id = mat.id_alu INNER JOIN col_persona pera ON alu.id_per=pera.id "; 
		sql += "\n INNER JOIN alu_familiar fam ON mat.id_fam=fam.id )  ON mov.id_fam = fam.id ";  
		sql += "\n INNER JOIN col_persona perf ON fam.id_per=perf.id ";  
		sql += "\n LEFT JOIN cat_grad gr ON mat.id_gra = gr.id ";  
		sql += "\n LEFT JOIN per_periodo p ON p.id = mat.id_per ";  
		sql += "\n LEFT JOIN ges_servicio g ON g.id=p.id_srv ";  
		sql += "\n LEFT JOIN fac_nota_credito nc ON nc.id_fmo=mov.id  WHERE mov.tipo = 'I' "; 
		if(cliente!=null && !"".equals(cliente.trim()))
			sql += "\n and CONCAT(perf.ape_pat,' ' , perf.ape_mat,' ' ,perf.nom) like '%" + cliente.toUpperCase() + "%'";	
		if(alumno!=null && !"".equals(alumno.trim()))
			sql += "\n and CONCAT(pera.ape_pat,' ' , pera.ape_mat,' ' ,pera.nom) like '%" + alumno.toUpperCase() + "%'";
				sql += ")t ";
		
		if (id_suc!=null && id_suc!=0 )
			sql = sql + " where t.id_suc = " + id_suc;
		sql = sql + " order by 1,mes ";
		
		return jdbcTemplate.queryForList(sql );
		
		
	}
	
	
	public List<Map<String,Object>> consultarReporteCajaxAlumno(Integer id_anio, Integer id_alu){//TODO MEJORAR ESTA PARAMETRIZACION DE CONCEPTOS
		String sql ="select t.* from (select DISTINCT mov.id id_fmo, m.id, mov.nro_rec, m.monto_total , IF(ISNULL(mov.id_mat), mov.obs, concat( alu.ape_pat,' ' , alu.ape_mat, ', ', alu.nom) ) obs, con.nom as concepto, mov.tipo, "
				+ "  mov.id_suc,g.nom nivel, gr.nom grado, au.secc, 0 mes, DATE_FORMAT(mov.fec,'%Y-%m-%d') fecha, 'M' tabla "
				+ " from fac_movimiento_detalle m inner join fac_movimiento mov on mov.id= m.id_fmo "
				+ " inner join fac_concepto con on con.id= m.id_fco "
				+ " left join ( mat_matricula mat join alu_alumno alu on alu.id = mat.id_alu )  on mov.id_mat = mat.id "
				+ " left join col_aula au on au.id = mat.id_au"
				+ " left join cat_grad gr on au.id_grad = gr.id"
				+ " left join per_periodo p on p.id = au.id_per "
				+ " left join ges_servicio g on g.id=p.id_srv "
				+ " where p.id_anio=? and mov.tipo='I' and alu.id = ? "
				+ " union all "
				+ " select fmo.id id_fmo, c.id, c.nro_rec, c.monto_total , concat( alu.ape_pat,' ' , alu.ape_mat, ', ', alu.nom) obs, concat('MENSUALIDAD' , ' - ' , m.nom ) as concepto , 'I' tipo , g.id_suc,g.nom nivel, "
				+ " gr.nom grado, au.secc, mens mes, DATE_FORMAT(c.fec_pago,'%Y-%m-%d') fecha, 'E' tabla"
				+ " from  fac_academico_pago c "
				+ " inner join fac_movimiento fmo on fmo.nro_rec= c.nro_rec"
				+ " inner join mat_matricula mat on mat.id= c.id_mat "
				+ " inner join col_aula au on au.id = mat.id_au"
				+ " inner join cat_grad gr on au.id_grad = gr.id"
				+ " inner join alu_alumno alu on alu.id= mat.id_alu "
				+ " inner join per_periodo p on p.id = au.id_per "
				+ " inner join ges_servicio g on g.id=p.id_srv "
				+ " left join cat_mes m on c.mens=m.id "
				+ " where p.id_anio=? and alu.id = ? and canc='1' and tip='MEN' and c.banco is null "
				+ " union all "
				+ " select fmo.id id_fmo, c.id,c.nro_rec, c.monto_total,concat( alu.ape_pat,' ' , alu.ape_mat, ', ', alu.nom) obs, "
				+ " if(c.tip='MAT' , 'MATRICULA','CUOTA DE INGRESO') as concepto , 'I' tipo, g.id_suc,g.nom nivel, gr.nom grado, au.secc, 0 mes, DATE_FORMAT(c.fec_pago,'%Y-%m-%d') fecha, 'T' tabla "
				+ " from  fac_academico_pago c "
				+ " inner join mat_matricula mat on mat.id= c.id_mat "
				+ " inner join fac_movimiento fmo on fmo.nro_rec= c.nro_rec"
				+ " inner join col_aula au on au.id = mat.id_au"
				+ " inner join cat_grad gr on au.id_grad = gr.id"
				+ " inner join alu_alumno alu on alu.id= mat.id_alu "
				+ " inner join per_periodo p on p.id =  au.id_per "
				+ " inner join ges_servicio g on g.id=p.id_srv  "
				+ " where p.id_anio=? and alu.id = ?  and canc='1' and tip!='MEN'"
				+ " union all"
				+ " SELECT DISTINCT fmo.id id_fmo,c.id, c.nro_recibo nro_rec, c.monto monto_total, CONCAT( alu.ape_pat,' ' , alu.ape_mat, ', ', alu.nom) obs, 'RESERVA' AS concepto, 'I' tipo ," 
				+ " g.id_suc, g.nom nivel, gr.nom grado, au.secc, 0 mes,DATE_FORMAT(c.fec_ins,'%Y-%m-%d') fecha , 'R' tabla"
				+ " FROM `fac_reserva_cuota` c"
				+ " inner join fac_movimiento fmo on fmo.nro_rec= c.nro_recibo"
				+ " INNER JOIN mat_reserva r ON c.id_res=r.id"
				+ " INNER JOIN col_aula au ON au.id = r.id_au"
				+ " INNER JOIN cat_grad gr ON au.id_grad = gr.id"
				+ " INNER JOIN alu_alumno alu ON r.`id_alu`=alu.id"
				+ " INNER JOIN per_periodo p ON p.id = r.id_per"
				+ " INNER JOIN ges_servicio g ON g.id=p.id_srv"  
				+ " WHERE p.id_anio=? and alu.id = ? "
				+ ")t ";
		
		sql = sql + " order by 1,mes ";
		
		return jdbcTemplate.queryForList(sql, new Object[]{ id_anio, id_alu,id_anio, id_alu,id_anio, id_alu,id_anio, id_alu} );
		
		
	}

	public List<Row> pagosSecretaria(String fecha){
		//String sql = "SELECT f.nro_doc, p.* FROM `fac_academico_pago` p inner join mat_matricula m on m.id=p.id_mat inner join alu_familiar f on f.id=m.id_fam WHERE p.banco IS NOT NULL AND p.fec_pago=? order by p.nro_rec";
		String sql = "SELECT f.nro_doc, m.* FROM fac_movimiento m inner join alu_familiar f on f.id=m.id_fam "
				+ "WHERE m.fec=? and m.tipo='I' and m.est='A' order by m.nro_rec";
		logger.info(sql);
		return sqlUtil.query(sql, new Object[] {fecha});
	
	}	
		
	public List<Row> pagosFacturaElectronica(Date fec_ini, Date fec_fin, Date fec_ini_env, Date fec_fin_env, String tip_com, String nro_serie, String enviadoSunat){
		Param param = new Param();
		String sql ="";
		if(tip_com.equals("B")) {
			 sql = "SELECT DISTINCT * FROM (SELECT per.nro_doc, concat(per.ape_pat,' ' ,per.ape_mat, ', ' ,per.nom) cliente, SUBSTRING(nro_rec,1,4) serie, SUBSTRING(nro_rec,6) recibo,";
				sql += " m.fec fecha,m.fec_sunat fecha_sunat, per.id_tdc, per.ape_pat, per.ape_mat, per.nom,per.corr, per.cel, m.* ";
				sql	+= " FROM fac_movimiento m ";
				sql	+= " left join alu_familiar f on f.id=m.id_fam ";
				sql	+= " INNER JOIN col_persona per ON f.id_per=per.id";
					//+ " WHERE DATE_FORMAT(m.fec_ins,'%d-%m-%Y') like '"+fec_ini+"%' and m.tipo='I' and m.est='A' ";
				if(fec_ini!=null && fec_fin!=null)
					sql	+= " WHERE m.fec_ins between :fec_ini and :fec_fin and m.tipo='I' and m.est='A' ";
				if(fec_ini_env!=null && fec_fin_env!=null)
					sql	+= " WHERE m.fec_env between :fec_ini_env and :fec_fin_env and m.tipo='I' and m.est='A' ";
					if(nro_serie!=null && !"".equals(nro_serie) )
				sql += " and  substring(m.nro_rec,1,4) IN (:nro_series)  ";
			
			if("1".equals(enviadoSunat))
				sql += " and m.cod_res is not null";
			else if("2".equals(enviadoSunat))
				sql += " and m.cod_res is null";
			sql +=" UNION ALL";
			sql +=" SELECT per.nro_doc, CONCAT(per.ape_pat,' ' ,per.ape_mat, ', ' ,per.nom) cliente, SUBSTRING(nro_rec,1,4) serie, SUBSTRING(nro_rec,6) recibo, m.fec fecha,m.fec_sunat fecha_sunat, per.id_tdc, per.ape_pat, per.ape_mat, per.nom,per.corr, per.cel, m.* " ; 
			sql += " FROM fac_movimiento m";    
			sql += " INNER JOIN col_persona per ON m.id_per=per.id ";
			if(fec_ini!=null && fec_fin!=null)
				sql += " WHERE m.fec_ins between :fec_ini and :fec_fin and m.tipo='I' and m.est='A' ";
			if(fec_ini_env!=null && fec_fin_env!=null)
				sql += " WHERE m.fec_env between :fec_ini_env and :fec_fin_env and m.tipo='I' and m.est='A' ";
			//sql += " WHERE DATE_FORMAT(m.fec_ins,'%d-%m-%Y') like '"+fec_ini+"%' and m.tipo='I' and m.est='A'  ";
			if(nro_serie!=null && !"".equals(nro_serie) )
				sql += " and  substring(m.nro_rec,1,4) IN (:nro_series)  ";
			if("1".equals(enviadoSunat))
				sql += " and m.cod_res is not null";
			else if("2".equals(enviadoSunat))
				sql += " and m.cod_res is null";
			sql +=" )t"	;
			sql += " order by t.nro_rec";
			logger.info(sql);
			param.put("fec_ini", fec_ini);
			param.put("fec_fin", fec_fin);
			param.put("fec_ini_env", fec_ini_env);
			param.put("fec_fin_env", fec_fin_env);
			
			List nro_series = Arrays.asList(nro_serie.split(","));
			if(nro_serie!=null && !"".equals(nro_serie) )
			param.put("nro_series", nro_series);
		} else if (tip_com.equals("N")) {
			 sql ="SELECT DISTINCT * FROM (\n" ; 
					sql +=" SELECT per.nro_doc, CONCAT(per.ape_pat,' ' ,per.ape_mat, ', ' ,per.nom) cliente, fmafec.nro_rec nro_rec_afec, fmafec.fec fec_rec_env, SUBSTRING(m.nro_rec,1,4) serie, SUBSTRING(m.nro_rec,6) recibo,\n" ;
					sql +=" m.fec fecha,m.fec_sunat fecha_sunat, per.id_tdc, per.ape_pat, per.ape_mat, per.nom,per.corr, per.cel, m.* \n" ;
					sql +=" FROM fac_movimiento m \n" ; 
					sql +=" INNER JOIN `fac_nota_credito` nc ON m.`id`=nc.id_fmo_nc\n" ; 
					sql +=" INNER JOIN `fac_movimiento` fmafec ON nc.id_fmo=fmafec.id\n" ; 
					sql +=" INNER JOIN `mat_matricula` mat ON m.`id_mat`=mat.`id`\n" ; 
					sql +=" LEFT JOIN alu_familiar f ON f.id=mat.id_fam \n" ; 
					sql +=" INNER JOIN col_persona per ON f.id_per=per.id\n" ; 
					if(fec_ini!=null && fec_fin!=null)
						sql +=" WHERE m.fec_ins BETWEEN :fec_ini AND :fec_fin AND m.tipo='S' AND m.est='A'\n" ; 
					if(fec_ini_env!=null && fec_fin_env!=null)
						sql +=" WHERE m.fec_ins BETWEEN :fec_ini_env AND :fec_fin_env AND m.tipo='S' AND m.est='A'\n" ; 
					if(nro_serie!=null && !"".equals(nro_serie) )
						sql += " and  substring(m.nro_rec,1,4) IN (:nro_series)  ";				
					if("1".equals(enviadoSunat))
						sql += " and m.cod_res is not null";
					else if("2".equals(enviadoSunat))
						sql += " and m.cod_res is null";
					sql +=" UNION ALL\n" ; 
					sql +=" SELECT per.nro_doc, CONCAT(per.ape_pat,' ' ,per.ape_mat, ', ' ,per.nom) cliente, fmafec.nro_rec nro_rec_afec, fmafec.fec fec_rec_env, SUBSTRING(m.nro_rec,1,4) serie, SUBSTRING(m.nro_rec,6) recibo,\n" ; 
					sql +=" m.fec fecha,m.fec_sunat fecha_sunat, per.id_tdc, per.ape_pat, per.ape_mat, per.nom,per.corr, per.cel, m.* \n" ; 
					sql +=" FROM fac_movimiento m \n" ; 
					sql +=" INNER JOIN `fac_nota_credito` nc ON m.`id`=nc.id_fmo_nc\n" ; 
					sql +=" INNER JOIN `fac_movimiento` fmafec ON nc.id_fmo=fmafec.id\n" ; 
					sql +=" INNER JOIN `mat_matricula` mat ON m.`id_mat`=mat.`id`\n" ; 
					sql +=" INNER JOIN col_persona per ON mat.id_per_res=per.id\n" ; 
					if(fec_ini!=null && fec_fin!=null)
						sql +=" WHERE m.fec_ins BETWEEN :fec_ini AND :fec_fin AND m.tipo='S' AND m.est='A'\n" ; 
					if(fec_ini_env!=null && fec_fin_env!=null)
						sql +=" WHERE m.fec_ins BETWEEN :fec_ini_env AND :fec_fin_env AND m.tipo='S' AND m.est='A'\n" ; 
					if(nro_serie!=null && !"".equals(nro_serie) )
						sql += " and  substring(m.nro_rec,1,4) IN (:nro_series)  ";				
					if("1".equals(enviadoSunat))
						sql += " and m.cod_res is not null";
					else if("2".equals(enviadoSunat))
						sql += " and m.cod_res is null";
					sql +=" UNION ALL\n" ; 
					sql +=" SELECT per.nro_doc, CONCAT(per.ape_pat,' ' ,per.ape_mat, ', ' ,per.nom) cliente,mafe.nro_rec nro_rec_afec, mafe.fec fec_rec_env, SUBSTRING(m.nro_rec,1,4) serie, SUBSTRING(m.nro_rec,6) recibo,\n" ; 
					sql +=" m.fec fecha,m.fec_sunat fecha_sunat, per.id_tdc, per.ape_pat, per.ape_mat, per.nom,per.corr, per.cel, m.* \n" ; 
					sql +=" FROM fac_movimiento m \n" ; 
					sql +=" INNER JOIN `fac_nota_credito` nc ON m.`id`=nc.id_fmo_nc\n" ; 
					sql +=" INNER JOIN `fac_movimiento` mafe ON nc.`id_fmo`=mafe.id\n" ; 
					sql +=" INNER JOIN `fac_reserva_cuota` rcuo ON mafe.nro_rec=rcuo.nro_recibo\n" ; 
					sql +=" INNER JOIN `mat_reserva` res ON rcuo.id_res=res.id\n" ; 
					sql +=" INNER JOIN `alu_familiar` fam ON res.id_fam=fam.id\n" ; 
					sql +=" INNER JOIN col_persona per ON fam.id_per=per.id\n" ; 
					if(fec_ini!=null && fec_fin!=null)
						sql +=" WHERE m.fec_ins BETWEEN :fec_ini AND :fec_fin AND m.tipo='S' AND m.est='A'\n" ; 
					if(fec_ini_env!=null && fec_fin_env!=null)
						sql +=" WHERE m.fec_ins BETWEEN :fec_ini_env AND :fec_fin_env AND m.tipo='S' AND m.est='A'\n" ; 
					if(nro_serie!=null && !"".equals(nro_serie) )
						sql += " and  substring(m.nro_rec,1,4) IN (:nro_series)  ";				
					if("1".equals(enviadoSunat))
						sql += " and m.cod_res is not null";
					else if("2".equals(enviadoSunat))
						sql += " and m.cod_res is null";
					sql +=" )t ";
					sql += " order by t.nro_rec";
					logger.info(sql);
					param.put("fec_ini", fec_ini);
					param.put("fec_fin", fec_fin);
					param.put("fec_ini_env", fec_ini_env);
					param.put("fec_fin_env", fec_fin_env);
					List nro_series = Arrays.asList(nro_serie.split(","));
					if(nro_serie!=null && !"".equals(nro_serie) )
					param.put("nro_series", nro_series);
		}
		

		logger.info(param);

		return sqlUtil.query(sql, param);
		

	}	
	
public List<Row> pagosFacturaElectronica2(String fec_ini, Date fec_fin, String nro_serie, String enviadoSunat){
		
		String sql = "SELECT DISTINCT * FROM (SELECT per.nro_doc, concat(per.ape_pat,' ' ,per.ape_mat, ', ' ,per.nom) cliente, SUBSTRING(nro_rec,1,4) serie, SUBSTRING(nro_rec,6) recibo,"
				+ " m.fec fecha,DATE(m.fec_env) fecha_envio , m.fec_sunat fecha_sunat, per.id_tdc, per.ape_pat, per.ape_mat, per.nom,per.corr, per.cel, m.* "
				+ " FROM fac_movimiento m "
				+ " left join alu_familiar f on f.id=m.id_fam "
				+ " INNER JOIN col_persona per ON f.id_per=per.id"
				+ " WHERE DATE_FORMAT(m.fec_ins,'%d/%m/%Y') like '"+fec_ini+"%' and m.tipo='I' and m.est='A' ";
				//+ " WHERE m.fec between :fec_ini and :fec_fin and m.tipo='I' and m.est='A' ";
				if(nro_serie!=null && !"-".equals(nro_serie) )
				sql += " and  substring(m.nro_rec,1,4) IN (:nro_series)  ";
		
		if("1".equals(enviadoSunat))
			sql += " and m.cod_res is not null";
		else if("2".equals(enviadoSunat))
			sql += " and m.cod_res is null";
		sql +=" UNION ALL";
		sql +=" SELECT per.nro_doc, CONCAT(per.ape_pat,' ' ,per.ape_mat, ', ' ,per.nom) cliente, SUBSTRING(nro_rec,1,4) serie, SUBSTRING(nro_rec,6) recibo, m.fec fecha,DATE(m.fec_env) fecha_envio,m.fec_sunat fecha_sunat, per.id_tdc, per.ape_pat, per.ape_mat, per.nom,per.corr, per.cel, m.* " ; 
		sql += " FROM fac_movimiento m";    
		sql += " INNER JOIN col_persona per ON m.id_per=per.id ";
		//sql += " WHERE m.fec between :fec_ini and :fec_fin and m.tipo='I' and m.est='A' ";
		sql += " WHERE DATE_FORMAT(m.fec_ins,'%d/%m/%Y') like '"+fec_ini+"%' and m.tipo='I' and m.est='A'  ";
		if(nro_serie!=null && !"-".equals(nro_serie) )
			sql += " and  substring(m.nro_rec,1,4) IN (:nro_series)  ";
		if("1".equals(enviadoSunat))
			sql += " and m.cod_res is not null";
		else if("2".equals(enviadoSunat))
			sql += " and m.cod_res is null";
		sql +=" )t"	;
		sql += " order by t.nro_rec ";
		logger.info(sql);
		Param param = new Param();
		param.put("fec_ini", fec_ini);
		param.put("fec_fin", fec_fin);
		
		List nro_series = Arrays.asList(nro_serie.split(","));
		if(nro_serie!=null && !"-".equals(nro_serie) )
		param.put("nro_series", nro_series);

		logger.info(param);

		return sqlUtil.query(sql, param);
		

	}	

public List<Row> pagosFacturaElectronica3(String fec_ini, Date fec_fin, String nro_serie, String enviadoSunat){
	
	String sql = "SELECT DISTINCT * FROM (SELECT per.nro_doc, concat(per.ape_pat,' ' ,per.ape_mat, ', ' ,per.nom) cliente, SUBSTRING(nro_rec,1,4) serie, SUBSTRING(nro_rec,6) recibo,"
			+ " m.fec fecha,m.fec_sunat fecha_sunat, per.id_tdc, per.ape_pat, per.ape_mat, per.nom,per.corr, per.cel, m.* "
			+ " FROM fac_movimiento m "
			+ " left join alu_familiar f on f.id=m.id_fam "
			+ " INNER JOIN col_persona per ON f.id_per=per.id"
			+ " WHERE DATE_FORMAT(m.fec_ins,'%d/%m/%Y') like '"+fec_ini+"%' and m.tipo='I' and m.est='A' and m.est_env IS NULL";
			//+ " WHERE m.fec between :fec_ini and :fec_fin and m.tipo='I' and m.est='A' ";
			if(nro_serie!=null && !"-".equals(nro_serie) )
			sql += " and  substring(m.nro_rec,1,4) IN (:nro_series)  ";
	
	if("1".equals(enviadoSunat))
		sql += " and m.cod_res is not null";
	else if("2".equals(enviadoSunat))
		sql += " and m.cod_res is null";
	sql +=" UNION ALL";
	sql +=" SELECT per.nro_doc, CONCAT(per.ape_pat,' ' ,per.ape_mat, ', ' ,per.nom) cliente, SUBSTRING(nro_rec,1,4) serie, SUBSTRING(nro_rec,6) recibo, m.fec fecha,m.fec_sunat fecha_sunat, per.id_tdc, per.ape_pat, per.ape_mat, per.nom,per.corr, per.cel, m.* " ; 
	sql += " FROM fac_movimiento m";    
	sql += " INNER JOIN col_persona per ON m.id_per=per.id ";
	//sql += " WHERE m.fec between :fec_ini and :fec_fin and m.tipo='I' and m.est='A' ";
	sql += " WHERE DATE_FORMAT(m.fec_ins,'%d/%m/%Y') like '"+fec_ini+"%' and m.tipo='I' and m.est='A'  and m.est_env IS NULL";
	if(nro_serie!=null && !"-".equals(nro_serie) )
		sql += " and  substring(m.nro_rec,1,4) IN (:nro_series)  ";
	if("1".equals(enviadoSunat))
		sql += " and m.cod_res is not null";
	else if("2".equals(enviadoSunat))
		sql += " and m.cod_res is null";
	sql +=" )t"	;
	sql += " order by t.nro_rec limit 499 ";
	logger.info(sql);
	Param param = new Param();
	param.put("fec_ini", fec_ini);
	param.put("fec_fin", fec_fin);
	
	List nro_series = Arrays.asList(nro_serie.split(","));
	if(nro_serie!=null && !"-".equals(nro_serie) )
	param.put("nro_series", nro_series);

	logger.info(param);

	return sqlUtil.query(sql, param);
	

}	
	
	
	/**
	 * Detalle de la boleta para realizar la nota de credito
	 * @param id_fmo
	 * @param enviadoSunat  1 : NO ENVIADO
	 * 						2: ENVIADO
	 * @return
	 */
	public List<Row> pagosFacturaElectronica(Integer id_fmo, String enviadoSunat){
		
		String sql = "SELECT p.nro_doc, concat(p.ape_pat,' ' ,p.ape_mat, ', ' ,p.nom) cliente, p.dir,"
				+ " m.fec,m.fec_sunat, p.id_tdc, p.ape_pat, p.ape_mat, p.nom, p.corr, p.cel, m.*, tra.id tra_id  , pt.ape_pat tra_ape_pat, pt.ape_mat tra_ape_mat , pt.nom tra_nom "
				+ " , suc.id suc_id  , suc.nom suc_nom , suc.dir suc_dir , suc.tel suc_tel , suc.correo suc_correo"
				+ " FROM fac_movimiento m "
				+ " left join alu_familiar f on f.id=m.id_fam "
				+ " left join col_persona p on f.id_per=p.id "
				+ " left join seg_usuario usu on usu.id = m.usr_ins"
				+ " left join ges_trabajador tra on tra.id = usu.id_tra"
				+ " left join col_persona pt on f.id_per=pt.id "
				+ " left join ges_sucursal suc on suc.id = m.id_suc"
				+ " WHERE m.id=? and m.tipo='I' and m.est='A' ";
		
		if("1".equals(enviadoSunat))
			sql += " and m.cod_res is not null";
			//sql += " and m.cod_res is null";
		else if("2".equals(enviadoSunat))
			sql += " and m.cod_res is null";
		
			sql += " order by m.nro_rec";
		logger.info(sql);
		
		return sqlUtil.query(sql, new Object[]{id_fmo});
		

	}	
	
public List<Row> pagosFacturaElectronica2(Integer id_fmo, String enviadoSunat){
		
		String sql = "SELECT p.nro_doc, concat(p.ape_pat,' ' ,p.ape_mat, ', ' ,p.nom) cliente, p.dir,"
				+ " m.fec,m.fec_sunat, p.id_tdc, p.ape_pat, p.ape_mat, p.nom, p.corr, p.cel, m.*, tra.id tra_id  , pt.ape_pat tra_ape_pat, pt.ape_mat tra_ape_mat , pt.nom tra_nom "
				+ " , suc.id suc_id  , suc.nom suc_nom , suc.dir suc_dir , suc.tel suc_tel , suc.correo suc_correo"
				+ " FROM fac_movimiento m "
				+ " left join alu_familiar f on f.id=m.id_fam "
				+ " left join col_persona p on f.id_per=p.id "
				+ " left join seg_usuario usu on usu.id = m.usr_ins"
				+ " left join ges_trabajador tra on tra.id = usu.id_tra"
				+ " left join col_persona pt on f.id_per=pt.id "
				+ " left join ges_sucursal suc on suc.id = m.id_suc"
				+ " WHERE m.id=? and m.tipo='I' and m.est='A' ";
		//Por ahora hasta que se arregle el envio a la SUNAT, no importaria esto
		/*if("1".equals(enviadoSunat))
			////sql += " and m.cod_res is not null";
			sql += " and m.cod_res is null";
		else if("2".equals(enviadoSunat))
			sql += " and m.cod_res is null";*/
		
			sql += " order by m.nro_rec";
		logger.info(sql);
		
		return sqlUtil.query(sql, new Object[]{id_fmo});
		

	}	

	public List<Row> obtenerFacturasElectronicaMatriculaCI(Integer id_mat){
		
		String sql = "SELECT DISTINCT fam.`nro_doc`, CONCAT(fam.`ape_pat`, ' ', fam.`ape_mat`,' ',fam.`nom`) cliente, fam.dir, fam.`id_tdc`, fam.`cel`, mov.`fec`, mov.`fec_sunat` "
				+ " FROM `fac_academico_pago` fac INNER JOIN `mat_matricula` mat ON fac.`id_mat`=mat.`id`"
				+ " INNER JOIN `fac_movimiento` mov ON mat.`id`=mov.`id_mat`"
				+ " INNER JOIN `alu_familiar` fam ON mat.`id_fam`=fam.`id`"
				+ " WHERE mat.id=? AND fac.`tip` IN ('ING','MAT') and mov.cod_res is not null";
		
		return sqlUtil.query(sql, new Object[]{id_mat});
		

	}	

	/**
	 * Detalle de la boleta para realizar la nota de credito
	 * @param id_fmo
	 * @param enviadoSunat  1 : NO ENVIADO
	 * 						2: ENVIADO
	 * @return
	 */
	public List<Row> pagosByNCElectronica(Integer id_fmo, String enviadoSunat){
		
		String sql = "SELECT f.nro_doc, concat(f.ape_pat,' ' ,f.ape_mat, ', ' ,f.nom) cliente, f.dir,"
				+ " m.fec,m.fec_sunat, f.id_tdc, f.ape_pat, f.ape_mat, f.nom,f.corr, f.cel, m.* "
				+ " FROM fac_movimiento m "
				+ " left join alu_familiar f on f.id=m.id_fam "
				+ " WHERE m.id=? and m.tipo in ('I','N') and m.est='A' ";
		
		if("1".equals(enviadoSunat))
			sql += " and m.cod_res is not null";
		else if("2".equals(enviadoSunat))
			sql += " and m.cod_res is null";
		
			sql += " order by m.nro_rec";
		logger.info(sql);
		
		return sqlUtil.query(sql, new Object[]{id_fmo});
		

	}	

	/**
	 * Actualizar la tabla de movimiento en base a la respuesta de la factura electronica
	 * 
	 * @param id_fmos
	 * @param archivo
	 * @param id_eiv
	 * @param id_usr
	 */
	public void actualizarCodRes(List<Integer> id_fmos,String archivo,String id_eiv, String respuesta_sunat,Date fec_sunat){
		Param param = new Param();
		param.put("usr_act", tokenSeguridad.getId());
		param.put("cod_res", archivo);
		param.put("id_eiv", id_eiv);
		param.put("id_fmos", id_fmos);
		param.put("fec_sunat", fec_sunat);
		param.put("respuesta_sunat", respuesta_sunat);
		
		sqlUtil.update("update fac_movimiento set fec_act=CURDATE(),fec_sunat=:fec_sunat, usr_act=:usr_act, cod_res=:cod_res, id_eiv=:id_eiv, respuesta_sunat=:respuesta_sunat where id in (:id_fmos)",param);
	}
	
	
	
	public void actualizarEstadoMovimiento(List<Integer> id_fmos){
		Param param = new Param();
		param.put("usr_act", tokenSeguridad.getId());		
		param.put("id_fmos", id_fmos);

		
		sqlUtil.update("update fac_movimiento set fec_act=CURDATE(), fec_env=CURDATE(),usr_act=:usr_act, est_env='E' where id in (:id_fmos)",param);
	}
	
	
	public List<Row> pagoCambioLocal(Integer id_mat){
		//String sql = "SELECT f.nro_doc, p.* FROM `fac_academico_pago` p inner join mat_matricula m on m.id=p.id_mat inner join alu_familiar f on f.id=m.id_fam WHERE p.banco IS NOT NULL AND p.fec_pago=? order by p.nro_rec";
		String sql = "SELECT * FROM `fac_movimiento` WHERE id_mat=? AND obs LIKE '%CAMBIO DE LOCAL%';";
		logger.info(sql);
		return sqlUtil.query(sql, new Object[] {id_mat});
	
	}
		
}
