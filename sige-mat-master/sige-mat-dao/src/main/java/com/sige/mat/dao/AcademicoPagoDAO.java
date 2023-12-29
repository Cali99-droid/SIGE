package com.sige.mat.dao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.AcademicoPagoDAOImpl;
import com.tesla.colegio.model.AcademicoPago;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;


/**
 * Define m�todos DAO operations para la entidad academico_pago.
 * @author MV
 *
 */
@Repository
public class AcademicoPagoDAO extends AcademicoPagoDAOImpl{

	final static Logger logger = Logger.getLogger(AcademicoPagoDAO.class);
	
	@Autowired
	private SQLUtil sqlUtil;

	public List<Map<String,Object>> listaPagosBanco(Integer id_suc,Date fec_ini,Date fec_fin, String nro_serie){
		String sql = "select fmo.id id_fmo, ap.nro_pe,"
				+ "\n CASE "
				+ "\n WHEN ap.tip='MAT' THEN 'MATRÍCULA'"
				+ "\n WHEN ap.tip='ING' THEN 'CUOTA DE INGRESO'"
				+ "\n WHEN ap.tip='MEN' THEN concat('MENSUALIDAD' , ' - ' , mes.nom )"
				+ "\n END as concepto,"
				+ "\n ap.mens, 'I' as tipo,ap.nro_rec, ap.fec_pago, ap.monto_total, s.nom nivel, g.nom grado, au.secc , m.num_cont, suc.nom local, concat(per.ape_pat, ' ' , per.ape_mat , ' ' , per.nom ) alumno "
				+ "\n from  fac_academico_pago  ap"
				+ "\n inner join fac_movimiento fmo on fmo.nro_rec = ap.nro_rec"
				+ "\n inner join mat_matricula m on m.id = ap.id_mat"
				+ "\n inner join alu_alumno a on m.id_alu = a.id"
				+ "\n inner join col_persona per on a.id_per = per.id"
				+ "\n inner join col_aula au on au.id = m.id_au_asi"
				+ "\n inner join cat_grad g on au.id_grad = g.id"
				+ "\n inner join per_periodo p on p.id = m.id_per"
				+ "\n inner join ges_servicio s on s.id = p.id_srv"
				+ "\n inner join ges_sucursal suc on suc.id = s.id_suc"
				+ "\n left join cat_mes mes on mes.id=ap.mens "
				+ "\n where ap.banco is not null ";

		if (fec_fin!=null)
			sql = sql + "\n and (ap.fec_pago between ? and ?) ";
		else 
			sql = sql + "\n and ap.fec_pago = ? ";
		
		if (!nro_serie.equals("") && nro_serie!=null)
			sql = sql + "\n and ap.nro_rec LIKE '"+nro_serie+"%' ";
		
		if (id_suc!=null && id_suc !=0)
			sql = sql + "\n and suc.id=" + id_suc;
			sql = sql + "\n order by ap.nro_rec,ap.mens";
		
			logger.debug(sql);
			
		if (fec_fin!=null)
			return jdbcTemplate.queryForList(sql, new Object[]{fec_ini,fec_fin});
		else
			return jdbcTemplate.queryForList(sql, new Object[]{fec_ini});
	
	}
	
	public Row obtenerDatosCliente(int id_pag) {
		
		String sql = "SELECT fam.nro_doc, td.nom tip_doc, CONCAT(fam.ape_pat,' ', fam.ape_mat,' ', fam.nom) nombres, fam.dir direccion"
				+ " FROM fac_academico_pago fac INNER JOIN mat_matricula mat ON fac.id_mat =mat.id"
				+ " INNER JOIN alu_familiar fam ON mat.id_fam=fam.id"
				+ " INNER JOIN cat_tipo_documento td ON td.id =fam.id_tdc"
				+ " WHERE fac.id=?";
		logger.info(sql);
		List<Row> list = sqlUtil.query(sql, new Object[] { id_pag });
		if(list.size()==0)
			return null;
		else
			return list.get(0);

	}
	
	public void updatePago(int id_mat, int id_mes) {
		
		String sql = "UPDATE fac_academico_pago set est='I', canc=1 where id_mat=? and mens=?";
		logger.info(sql);
		 sqlUtil.update(sql, new Object[] { id_mat, id_mes });

	}
	
	public List<Row> pagosBanco(String fecha) {
		
		//String sql = "SELECT f.nro_doc, suc.id id_suc,if(m.id_fam_res_pag IS NULL THEN m.id_fam ELSE m.id_fam_res_pag) id_fam, " // Funcionara a partir del lunes
		String sql = "SELECT f.nro_doc, suc.id id_suc,m.id_fam, "
				+ " per.ape_pat alu_ape_pat, per.ape_mat alu_ape_mat, per.nom alu_nom, "
				+ " g.nom grad_nom,au.secc,s.nom niv,"
				+ " pa.* FROM `fac_academico_pago` pa "
				+ " inner join mat_matricula m on m.id=pa.id_mat "
				+ " inner join alu_familiar f on f.id=m.id_fam "
				+ " inner join alu_alumno a on m.id_alu = a.id"
				+ " inner join col_persona per on a.id_per = per.id"
				+ " inner join col_aula au on au.id = m.id_au_asi"
				+ " inner join cat_grad g on au.id_grad = g.id"
				+ " inner join per_periodo p on p.id = m.id_per"
				+ " inner join ges_servicio s on s.id = p.id_srv"
				+ " inner join ges_sucursal suc on suc.id = s.id_suc "
				+ " WHERE pa.banco IS NOT NULL and not exists(select 1 from fac_movimiento where nro_rec=pa.nro_rec) AND pa.`canc`=1 " // AND pa.fec_pago=?"
				// + " WHERE pa.banco IS NOT NULL AND NOT EXISTS(SELECT 1 FROM fac_movimiento WHERE nro_rec=pa.nro_rec)  AND pa.fec_pago='2019-12-03'" usado para actualizar pagos q no estan en movimiento
				+ " AND CURDATE()=DATE(pa.fec_act)" //AND p.id_anio=3 lo use para procesar faltantes 
				// + " AND p.id_anio=3" // usado para insertar en movimiento lo q falte
				+ " order by pa.nro_rec";
		logger.info(sql);
		return sqlUtil.query(sql);//, new Object[] {fecha}

	}
	
public List<Row> pagosBancoTodos() {
		//String sql = "SELECT f.nro_doc, suc.id id_suc,if(m.id_fam_res_pag IS NULL THEN m.id_fam ELSE m.id_fam_res_pag) id_fam, " // Funcionara a partir del lunes
		String sql = "SELECT f.nro_doc, suc.id id_suc,m.id_fam, "
				+ " per.ape_pat alu_ape_pat, per.ape_mat alu_ape_mat, per.nom alu_nom, "
				+ " g.nom grad_nom,au.secc,s.nom niv,"
				+ " pa.* FROM `fac_academico_pago` pa "
				+ " inner join mat_matricula m on m.id=pa.id_mat "
				+ " inner join alu_familiar f on f.id=m.id_fam "
				+ " inner join alu_alumno a on m.id_alu = a.id"
				+ " inner join col_persona per on a.id_per = per.id"
				+ " inner join col_aula au on au.id = m.id_au_asi"
				+ " inner join cat_grad g on au.id_grad = g.id"
				+ " inner join per_periodo p on p.id = au.id_per"
				+ " inner join ges_servicio s on s.id = p.id_srv"
				+ " inner join ges_sucursal suc on suc.id = s.id_suc "
				+ " WHERE pa.banco IS NOT NULL and pa.nro_rec like 'B004%' "
				+ " order by pa.nro_rec";
		logger.info(sql);
		return sqlUtil.query(sql);

	}
	
	public Row obtenerDatosPagoMensualidad(int id_pag) {
		
		String sql = "SELECT fam.nro_doc, td.nom tip_doc, CONCAT(fam.ape_pat,' ', fam.ape_mat,' ', fam.nom) nombres, fam.dir direccion"
				+ " FROM fac_academico_pago fac INNER JOIN mat_matricula mat ON fac.id_mat =mat.id"
				+ " INNER JOIN alu_familiar fam ON mat.id_fam=fam.id"
				+ " INNER JOIN cat_tipo_documento td ON td.id =fam.id_tdc"
				+ " WHERE fac.id=?";
		logger.info(sql);
		List<Row> list = sqlUtil.query(sql, new Object[] { id_pag });
		if(list.size()==0)
			return null;
		else
			return list.get(0);

	}
	
	public List<Row> datosAlumnoPago(Integer id_mat) {
		
		String sql = "SELECT (SELECT MAX(mens) FROM `fac_academico_pago` pag WHERE pag.id_mat=mat.id AND pag.canc=1) ultimo_pago, "
				+ " (SELECT COUNT(mens) FROM `fac_academico_pago` pag WHERE pag.id_mat=mat.id AND pag.canc=0 AND (mens BETWEEN 3 AND 12)) meses_deuda, "
				+ " (SELECT nro_doc FROM alu_familiar fam,alu_gru_fam_familiar fam_gpf,alu_gru_fam_alumno alu_gpf WHERE fam.id_gen='1' AND alu_gpf.id_gpf=fam_gpf.id_gpf AND alu.id=alu_gpf.id_alu AND fam_gpf.id_fam=fam.id LIMIT 1) AS dni_padre,"
				+ " (SELECT nro_doc FROM alu_familiar fam,alu_gru_fam_familiar fam_gpf,alu_gru_fam_alumno alu_gpf WHERE fam.id_gen='0' AND alu_gpf.id_gpf=fam_gpf.id_gpf AND alu.id=alu_gpf.id_alu AND fam_gpf.id_fam=fam.id LIMIT 1) AS dni_madre"
				+ " FROM `mat_matricula` mat "
				+ " INNER JOIN `alu_alumno` alu ON mat.id_alu=alu.id"
				+ " WHERE mat.id=?";
		logger.info(sql);
		return sqlUtil.query(sql, new Object[] {id_mat});

	}

	public Row obtenerAnioPeriodo(int id_mat) {
		
		String sql = "SELECT a.`nom` anio FROM `mat_matricula` mat INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.`id`"
				+ " INNER JOIN `per_periodo` per ON au.`id_per`=per.`id`"
				+ " INNER JOIN `col_anio` a ON per.`id_anio`=a.`id`"
				+ " WHERE mat.`id`=?";
		logger.info(sql);
		List<Row> list = sqlUtil.query(sql, new Object[] { id_mat });
		if(list.size()==0)
			return null;
		else
			return list.get(0);

	}
	
	public List<Row> obtenerPagosMensualidad(Integer id_mat){
		Param param = new Param();
		param.put("tip", "MEN");
		param.put("id_mat", id_mat);
		param.put("est", "A");
		param.put("canc", "1");
		
		String sql = "select m.id id_fmo, c.nom mes, a.* from fac_academico_pago a "
				+ " inner join fac_movimiento m on m.nro_rec = a.nro_rec "
				+ " inner join cat_mes c on c.id= a.mens"
				+ " where a.tip=:tip and a.id_mat=:id_mat and a.est=:est and a.canc=:canc "
				+ " order by a.mens";
		return sqlUtil.query(sql, param);
	}
	
	public List<Row> obtenerPagosAlumno(Integer id_alu){
		Param param = new Param();
		param.put("id_alu", id_alu);
		
		String sql = "SELECT DISTINCT * FROM (SELECT mov.`id` id_fmo, a.`nom` anio,"+
					" case  "+  
					" when mat.tipo IS NULL then mov.obs "+  
					" when mat.tipo='C' AND fac.tip='MAT' then 'MATRÍCULA' "+  
					" when mat.tipo='C' AND fac.tip='ING' then 'CUOTA DE INGRESO' "+ 
					" when mat.tipo='C' AND fac.tip='MEN' then 'Mensualidad' "+ 
					" when mat.tipo='A' then CONCAT('Cuota Nro. ',fac.`nro_cuota`,' - ', gra.`nom`, au.`secc`) "+  
					" when mat.tipo='V' then CONCAT('Cuota Nro. ',fac.`nro_cuota`,' - ', gra.`nom`, au.`secc`) "+  
					" end as concepto, "+
				//+ "CONCAT('Cuota Nro. ',fac.`nro_cuota`,' - ', gra.`nom`, au.`secc`)) concepto,\r\n" + 
				"fac.`nro_rec`, fac.fec_pago, fac.`monto`,mov.descuento, fac.`desc_hermano`, fac.`desc_pago_adelantado`, fac.`desc_personalizado`, fac.`desc_pronto_pago`, fac.`monto_total`, fac.`mens`, mat.`tipo`  \r\n" + 
				"FROM `fac_academico_pago` fac INNER JOIN `mat_matricula` mat ON fac.`id_mat`=mat.`id`\r\n" + 
				"INNER JOIN `fac_movimiento` mov ON fac.`nro_rec`=mov.`nro_rec`\r\n" + 
				"INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.`id`\r\n" + 
				"INNER JOIN `cat_grad` gra ON mat.`id_gra`=gra.`id`\r\n" + 
				"INNER JOIN `cat_nivel` niv ON gra.`id_nvl`=niv.`id`\r\n" + 
				"INNER JOIN `per_periodo` per ON mat.`id_per`=per.`id`\r\n" + 
				"INNER JOIN `col_anio` a ON per.`id_anio`=a.`id`\r\n" + 
				"WHERE mat.`id_alu`=:id_alu AND fac.`canc`='1'\r\n "+
				//\"ORDER BY fac.`id` DESC;\";
				"UNION ALL \r\n " + 
				"SELECT mov.`id` id_fmo, a.`nom` anio, \r\n " + 
				" SUBSTR(movd.obs,1,25) concepto, mov.`nro_rec`, mov.fec fec_pago, mov.`monto`,mov.descuento, NULL `desc_hermano`,\r\n " +  
				"NULL `desc_pago_adelantado`, NULL `desc_personalizado`, NULL `desc_pronto_pago`, mov.`monto_total`, 0 `mens`, 'C' `tipo` FROM \r\n " + 
				"`fac_movimiento` mov INNER JOIN `mat_matricula` mat ON mov.id_mat=mat.id \r\n " + 
				"INNER JOIN `fac_movimiento_detalle` movd ON mov.id=movd.id_fmo \r\n " + 
				"INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.`id` \r\n " + 
				"INNER JOIN `cat_grad` gra ON mat.`id_gra`=gra.`id` \r\n " + 
				"INNER JOIN `cat_nivel` niv ON gra.`id_nvl`=niv.`id` \r\n " + 
				"INNER JOIN `per_periodo` per ON mat.`id_per`=per.`id` \r\n " + 
				"INNER JOIN `col_anio` a ON per.`id_anio`=a.`id` \r\n " + 
				"WHERE mat.`id_alu`=:id_alu AND movd.id_fco<>'3' AND mov.tipo<>'S' \r\n " + 
				" )t ORDER BY t.`id_fmo` DESC";
		return sqlUtil.query(sql, param);
	}
	
	public List<Row> obtenerNCxAlumno(Integer id_alu){
		Param param = new Param();
		param.put("id_alu", id_alu);
		
		String sql = "\n" + 
				"SELECT DISTINCT * FROM (SELECT  fmoa.`id` id_fmo, a.`nom` anio, fmoa.`obs`, fmo.`nro_rec`, fmoa.nro_rec nro_rec_afec, fmoa.fec fec_pago, fmoa.`monto`,fmoa.descuento, NULL `desc_hermano`, NULL `desc_pago_adelantado`, NULL `desc_personalizado`, NULL `desc_pronto_pago`, fmoa.`monto_total`, NULL `mens`, NULL `tipo` \r\n" + 
				"FROM `fac_nota_credito` nc \r\n" + 
				"INNER JOIN `fac_movimiento` fmoa ON nc.id_fmo_nc=fmoa.id \r\n" + 
				"INNER JOIN `mat_matricula` mat ON fmoa.`id_mat`=mat.`id` \r\n" + 
				"INNER JOIN `alu_alumno` alu ON mat.`id_alu`=alu.`id` \r\n" + 
				"INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.`id` \r\n" + 
				"INNER JOIN `per_periodo` per ON au.`id_per`=per.`id` \r\n" + 
				"INNER JOIN `col_anio` a ON per.`id_anio`=a.`id` \r\n" + 
				"INNER JOIN `fac_movimiento` fmo ON nc.`id_fmo`=fmo.id -- AND (fmo.id_mat=fmoa.id_mat ) \r\n" + 
				"WHERE alu.id=:id_alu \r\n" + 
				"UNION ALL \r\n"+
				"SELECT  fmoa.`id` id_fmo, a.`nom` anio, fmoa.`obs`, fmo.`nro_rec`, fmoa.nro_rec nro_rec_afec, fmoa.fec fec_pago, fmoa.`monto`,fmoa.descuento, NULL `desc_hermano`, NULL `desc_pago_adelantado`, NULL `desc_personalizado`, NULL `desc_pronto_pago`, fmoa.`monto_total`, NULL `mens`, NULL `tipo` "+
				"FROM `fac_nota_credito` nc " +
				"INNER JOIN `fac_movimiento` fmoa ON nc.id_fmo_nc=fmoa.id " +
				"INNER JOIN `fac_movimiento` fmo ON nc.`id_fmo`=fmo.id " +
				"INNER JOIN `fac_reserva_cuota` rcuo ON rcuo.nro_recibo=fmo.nro_rec "+
				"INNER JOIN `mat_reserva` res ON rcuo.`id_res`=res.`id` " +
				"INNER JOIN `alu_alumno` alu ON res.`id_alu`=alu.`id` " +
				"INNER JOIN `per_periodo` per ON res.`id_per`=per.`id` " +
				"INNER JOIN `col_anio` a ON per.`id_anio`=a.`id` " +
				"WHERE alu.id=:id_alu) t "+
				"ORDER BY t.id_fmo \r\n" ;
		return sqlUtil.query(sql, param);
		
	}
	
	public void updateMontoPago(BigDecimal monto, int id_fac) {
		
		String sql = "UPDATE fac_academico_pago set monto=?  where id=?";
		logger.info(sql);
		 sqlUtil.update(sql, new Object[] { monto,id_fac });

	}
	
public void updateMontoPagoYtotal(BigDecimal monto, BigDecimal monto_total, BigDecimal saldo, int id_fac,Date fec_venc) {
		
		String sql = "UPDATE fac_academico_pago set monto=:monto , monto_total=:monto_total, saldo_favor=:saldo_favor,fec_venc=:fec_venc where id=:id_fac";
		logger.info(sql);
		Param param = new Param();
		param.put("monto", monto);
		param.put("monto_total", monto_total);
		param.put("saldo_favor", saldo);
		param.put("id_fac", id_fac);
		param.put("fec_venc", fec_venc); 
		
		 sqlUtil.update(sql, param);

	}
	
	
	public void updateDesctoPagoAdelantado(BigDecimal monto, int id_fac) {
		
		String sql = "UPDATE fac_academico_pago set desc_pago_adelantado=?  where id=?";
		logger.info(sql);
		 sqlUtil.update(sql, new Object[] { monto,id_fac });

	}
	
	public void updateMontoPagoDesc(BigDecimal monto, BigDecimal desc_hermano, Date fec_ven, int id_fac) {
		
		String sql = "UPDATE fac_academico_pago set monto=?, desc_hermano=?, fec_venc=?  where id=?";
		logger.info(sql);
		 sqlUtil.update(sql, new Object[] { monto,desc_hermano,fec_ven,id_fac });

	}
	
	public void updateMensualidadCanc(int id_fac) {
		
		String sql = "UPDATE fac_academico_pago set canc=1 where id=?";
		logger.info(sql);
		 sqlUtil.update(sql, new Object[] {id_fac });

	}
	
	public void updateSAldoFavor(int id_fac,BigDecimal saldo_favor, int canc) {
		
		String sql = "UPDATE fac_academico_pago set canc=?, saldo_favor=? where id=?";
		logger.info(sql);
		sqlUtil.update(sql, new Object[] {canc,saldo_favor,id_fac });

	}

	public List<AcademicoPago> obtenerPagosxMatricula(Integer id_mat, Integer mes){
		
		String sql = "SELECT fac.* "
				+ " FROM fac_academico_pago fac INNER JOIN mat_matricula mat ON fac.id_mat=mat.id"
				+ " WHERE mat.id="+id_mat+" AND fac.mens NOT IN("+mes+") and fac.tip='MEN'";
		List<AcademicoPago> listAcademicoPago = jdbcTemplate.query(sql, new RowMapper<AcademicoPago>() {

			@Override
			public AcademicoPago mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listAcademicoPago;
	}
	
	private AcademicoPago rsToEntity(ResultSet rs,String alias) throws SQLException {
		AcademicoPago academico_pago = new AcademicoPago();

		academico_pago.setId(rs.getInt( alias + "id"));
		academico_pago.setId_mat(rs.getInt( alias + "id_mat"));
		academico_pago.setTip(rs.getString( alias + "tip"));
		academico_pago.setMens(rs.getInt( alias + "mens"));
		academico_pago.setMonto(rs.getBigDecimal( alias + "monto"));
		academico_pago.setMontoTotal(rs.getBigDecimal( alias + "monto_total"));
		academico_pago.setCanc(rs.getString( alias + "canc"));
		academico_pago.setNro_rec(rs.getString( alias + "nro_rec"));
		academico_pago.setNro_pe(rs.getString( alias + "nro_pe"));
		academico_pago.setBanco(rs.getString( alias + "banco"));
		academico_pago.setFec_pago(rs.getDate( alias + "fec_pago"));
		academico_pago.setFec_venc(rs.getDate( alias + "fec_venc"));
		academico_pago.setDesc_hermano(rs.getBigDecimal( alias + "desc_hermano"));
		academico_pago.setDesc_pronto_pago(rs.getBigDecimal( alias + "desc_pronto_pago"));
		academico_pago.setDesc_pago_adelantado(rs.getBigDecimal( alias + "desc_pago_adelantado"));
		academico_pago.setDesc_personalizado(rs.getBigDecimal( alias + "desc_personalizado"));
		academico_pago.setEst(rs.getString( alias + "est"));
								
		return academico_pago;

	}
	
	public Row obtenerInformacionPago(Integer id_fac) {
		
		String sql = "SELECT fac.id id_fac, mat.`id_fam`, CONCAT(fam.`ape_pat`,' ', fam.`ape_mat`,' ', fam.`nom`) familiar, fam.corr correo, fam.nro_doc , fac.`monto_total`, fac.`mens`, fac.`fec_venc`, "
				+ " suc.`nom` sucursal, CONCAT(alu.ape_pat,' ', alu.ape_mat,' ', alu.nom) alumno  "
				+ " FROM `fac_academico_pago` fac INNER JOIN `mat_matricula` mat ON fac.id_mat=mat.`id`"
				+ " INNER JOIN `alu_alumno` alu ON mat.`id_alu`=alu.`id`"
				+ " INNER JOIN `alu_familiar` fam ON mat.`id_fam`=fam.`id`"
				+ " INNER JOIN `per_periodo` per ON mat.`id_per`=per.`id`"
				+ " INNER JOIN `ges_sucursal` suc ON per.`id_suc`=suc.`id`"
				+ " WHERE fac.`id`=?";
		logger.info(sql);
		List<Row> list = sqlUtil.query(sql, new Object[] {id_fac});
		if(list.size()==0)
			return null;
		else
			return list.get(0);

	}
	
	public List<Row> listarPagosParaBeca(Integer id_mat, String order) {
		
		String sql = "select * , 1 mes from fac_academico_pago where id_mat=? and canc=0 and est='A' and id_bec IS NULL ORDER BY "+order;
		
		logger.info(sql);
		return sqlUtil.query(sql, new Object[] {id_mat});

	}
	
	public void exonerarMensualidad(Integer id_fac){
		String sql = "update fac_academico_pago set canc=1, est='I' where id=?";
		sqlUtil.update(sql,new Object[]{id_fac});		
	}
	
	public List<Row> listarDescuentosAlumnosxAnio(Integer id_anio, Integer id_gir) {
		
		String sql = "SELECT DISTINCT gir.`nom` giro, suc.`nom` sucursal, CONCAT(pera.`ape_pat`,' ', pera.`ape_mat`,' ', pera.`nom`) alumno, niv.`nom` nivel, gra.`nom` grado, au.`secc`, fac.`monto`, fmd.`descuento`, fac.`monto_total`, fmd.`des`, fac.`mens` \r\n" + 
				"-- , fad.`descuento`, fad.`motivo`, per.`id_anio` -- , des.`nom` \r\n" + 
				"FROM `mat_matricula` mat INNER JOIN `alu_alumno` alu ON mat.`id_alu`=alu.`id` \r\n" + 
				"INNER JOIN col_persona pera ON alu.`id_per`=pera.`id` \r\n" + 
				"INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.`id` \r\n" + 
				"INNER JOIN `cat_grad` gra ON au.`id_grad`=gra.`id` \r\n" + 
				"INNER JOIN `cat_nivel` niv ON gra.`id_nvl`=niv.`id` \r\n" + 
				"INNER JOIN per_periodo per ON au.id_per=per.id \r\n" + 
				"INNER JOIN `ges_sucursal` suc ON per.id_suc=suc.id \r\n" + 
				"INNER JOIN `ges_servicio` srv ON per.id_srv=srv.id AND srv.`id_suc`=suc.id \r\n" + 
				"INNER JOIN `ges_giro_negocio` gir ON srv.`id_gir`=gir.`id` \r\n" + 
				"INNER JOIN `fac_academico_pago` fac ON mat.id=fac.`id_mat` \r\n" + 
				"INNER JOIN `fac_movimiento` mov ON fac.`nro_rec`=mov.`nro_rec` \r\n" + 
				"INNER JOIN `fac_movimiento_detalle` det ON mov.`id`=det.`id_fmo` \r\n" + 
				"INNER JOIN `fac_movimiento_descuento` fmd ON det.`id`=fmd.`id_fmd` \r\n" + 
				"WHERE per.id_anio=? AND srv.`id_gir`=? "+
				"ORDER BY pera.ape_pat, pera.ape_mat, pera.nom ";
		
		logger.info(sql);
		return sqlUtil.query(sql, new Object[] {id_anio, id_gir});

	}
	
	public List<Row> listarSeriesBoletas() {
		
		String sql = "SELECT serie id, serie value FROM `fac_conf_recibo` \n" + 
				"UNION ALL \n" + 
				"SELECT serie id, serie value FROM `fac_conf_recibo_banco`; ";
		
		logger.info(sql);
		return sqlUtil.query(sql);

	}
		
	public List<Row> listarSeriesNC() {
		
		String sql = "SELECT serie_nc id, serie_nc value FROM `fac_conf_recibo` \n" + 
				"UNION ALL \n" + 
				"SELECT serie_nc id, serie_nc value FROM `fac_conf_recibo_banco`; ";
		
		logger.info(sql);
		return sqlUtil.query(sql);

	}

}
