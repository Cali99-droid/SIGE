package com.sige.mat.dao.impl;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import com.sige.core.dao.SQLUtil;
import com.sige.web.security.TokenSeguridad;
import com.tesla.colegio.model.AcademicoPago;
import com.tesla.colegio.model.Familiar;
import com.tesla.colegio.model.Matricula;
import com.tesla.colegio.model.TipoDocumento;
import com.tesla.colegio.model.Trabajador;
import com.tesla.colegio.model.Usuario;
import com.tesla.colegio.model.bean.FamiliarDeudaBean;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;
import com.tesla.frmk.sql.SQLFrmkUtil;

/**
 * Implementaci�n de la interface AcademicoPagoDAO.
 * @author MV
 *
 */
public class AcademicoPagoDAOImpl{
	
	final static Logger logger = Logger.getLogger(AcademicoPagoDAOImpl.class);

	@Autowired
	private TokenSeguridad tokenSeguridad;
	
	@Autowired
	private DataSource dataSource;
	
	protected JdbcTemplate jdbcTemplate;
	
	@Autowired
	private SQLUtil sqlUtil;
	
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public int saveOrUpdate(AcademicoPago academico_pago) {
		
		if (academico_pago.getId() != null) {
			// update
			String sql = "UPDATE fac_academico_pago "
						+ "SET id_mat=?, "
						+ "id_bco_pag=?, "
						+ "id_bec=?, "
						+ "tip=?, "
						+ "mens=?, "
						+ "nro_cuota=?, "
						+ "tip_pag=?, "
						+ "monto=?, "
						+ "monto_total=?, "
						+ "canc=?, "
						+ "nro_rec=?, "
						+ "nro_pe=?, "
						+ "banco=?, "
						+ "fec_pago=?, "
						+ "fec_venc=?, "
						+ "desc_beca=?, "
						+ "desc_hermano=?, "
						+ "desc_pronto_pago=?, "
						+ "desc_personalizado=?, "
						+ "desc_pago_adelantado=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						academico_pago.getId_mat(),
						academico_pago.getId_bco_pag(),
						academico_pago.getId_bec(),
						academico_pago.getTip(),
						academico_pago.getMens(),
						academico_pago.getNro_cuota(),
						academico_pago.getTip_pag(),
						academico_pago.getMonto(),
						academico_pago.getMontoTotal(),
						academico_pago.getCanc(),
						academico_pago.getNro_rec(),
						academico_pago.getNro_pe(),
						academico_pago.getBanco(),
						academico_pago.getFec_pago(),
						academico_pago.getFec_venc(),
						academico_pago.getDesc_beca(),
						academico_pago.getDesc_hermano(),
						academico_pago.getDesc_pronto_pago(),
						academico_pago.getDesc_personalizado(),
						academico_pago.getDesc_pago_adelantado(),
						academico_pago.getEst(),
						academico_pago.getBanco()==null ? tokenSeguridad.getId():academico_pago.getUsr_ins(),
						new java.util.Date(),
						academico_pago.getId()); 
			return academico_pago.getId();

		} else {
			// insert
			String sql = "insert into fac_academico_pago ("
						+ "id_mat, "
						+ "id_bco_pag, "
						+ "id_bec, "
						+ "tip, "
						+ "mens, "
						+ "nro_cuota, "
						+ "tip_pag, "
						+ "monto, "
						+ "monto_total, "
						+ "canc, "
						+ "nro_rec, "
						+ "nro_pe, "
						+ "banco, "
						+ "fec_pago, "
						+ "fec_venc, "
						+ "desc_beca, "
						+ "desc_hermano, "
						+ "desc_pronto_pago, "
						+ "desc_pago_adelantado, "
						+ "desc_personalizado, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,  ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				academico_pago.getId_mat(),
				academico_pago.getId_bco_pag(),
				academico_pago.getId_bec(),
				academico_pago.getTip(),
				academico_pago.getMens(),
				academico_pago.getNro_cuota(),
				academico_pago.getTip_pag(),
				academico_pago.getMonto(),
				academico_pago.getMontoTotal(),
				academico_pago.getCanc(),
				academico_pago.getNro_rec(),
				academico_pago.getNro_pe(),
				academico_pago.getBanco(),
				academico_pago.getFec_pago(),
				academico_pago.getFec_venc(),
				academico_pago.getDesc_beca(),
				academico_pago.getDesc_hermano(),
				academico_pago.getDesc_pronto_pago(),
				academico_pago.getDesc_pago_adelantado(),
				academico_pago.getDesc_personalizado(),
				academico_pago.getEst(),
				tokenSeguridad.getId(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from fac_academico_pago where id_mat=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<AcademicoPago> list() {
		String sql = "select * from fac_academico_pago";
		
		//logger.info(sql);
		
		List<AcademicoPago> listAcademicoPago = jdbcTemplate.query(sql, new RowMapper<AcademicoPago>() {

			@Override
			public AcademicoPago mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listAcademicoPago;
	}

	public AcademicoPago get(int id) {
		String sql = "select * from fac_academico_pago WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<AcademicoPago>() {

			@Override
			public AcademicoPago extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public AcademicoPago getFullByNroRecibo(String nro_rec, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select fac_acad.id fac_acad_id, fac_acad.id_mat fac_acad_id_mat , fac_acad.tip fac_acad_tip , fac_acad.mens fac_acad_mens , fac_acad.tip_pag fac_acad_tip_pag, "
				+ " fac_acad.monto fac_acad_monto , fac_acad.monto_total fac_acad_monto_total , fac_acad.canc fac_acad_canc , fac_acad.nro_rec fac_acad_nro_rec , fac_acad.nro_pe fac_acad_nro_pe , "
				+ " fac_acad.banco fac_acad_banco , fac_acad.fec_pago fac_acad_fec_pago, fac_acad.fec_venc fac_acad_fec_venc , fac_acad.desc_beca fac_acad_desc_beca, fac_acad.desc_hermano fac_acad_desc_hermano , fac_acad.desc_pronto_pago fac_acad_desc_pronto_pago , "
				+ "fac_acad.desc_pago_adelantado fac_acad_desc_pago_adelantado  ,fac_acad.desc_personalizado fac_acad_desc_personalizado  ,fac_acad.est fac_acad_est ";
		if (aTablas.contains("mat_matricula"))
			sql = sql + ", mat.id mat_id  , mat.id_alu mat_id_alu , mat.id_fam mat_id_fam , mat.id_enc mat_id_enc , mat.id_con mat_id_con , mat.id_cli mat_id_cli , mat.id_per mat_id_per , mat.id_au mat_id_au , mat.id_au_asi mat_id_au_asi , mat.id_gra mat_id_gra , mat.id_niv mat_id_niv , mat.fecha mat_fecha , mat.car_pod mat_car_pod , mat.num_cont mat_num_cont , mat.obs mat_obs  ";
		if (aTablas.contains("alu_familiar")){
			sql = sql + ", fam.id fam_id  , fam.id_tdc fam_id_tdc , fam.nro_doc fam_nro_doc, fam.ape_pat fam_ape_pat,  fam.ape_mat fam_ape_mat, fam.nom fam_nom ";
			sql = sql + ", tdc.nom tdc_nom ";
		}
		if (aTablas.contains("seg_usuario"))
			sql = sql + ", tra.id tra_id  , tra.ape_pat tra_ape_pat, tra.ape_mat tra_ape_mat , tra.nom tra_nom ";
	
	
		
		sql = sql + " from fac_academico_pago fac_acad "; 
		if (aTablas.contains("mat_matricula"))
			sql = sql + " left join mat_matricula mat on mat.id = fac_acad.id_mat ";
		if (aTablas.contains("alu_familiar")){
			sql = sql + " left join alu_familiar fam on fam.id = mat.id_fam ";
			sql = sql + " left join cat_tipo_documento tdc on tdc.id = fam.id_tdc";
		}
		if (aTablas.contains("seg_usuario")){
			sql = sql + " left join seg_usuario usu on usu.id = fmo.usr_ins ";
			sql = sql + " left join ges_trabajador tra on tra.id = usu.id_tra ";
		}
			
		sql = sql + " where fac_acad.nro_rec= '" + nro_rec + "'"; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<AcademicoPago>() {
		
			@Override
			public AcademicoPago extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					AcademicoPago academicopago= rsToEntity(rs,"fac_acad_");
					if (aTablas.contains("mat_matricula")){
						Matricula matricula = new Matricula();  
							matricula.setId(rs.getInt("mat_id")) ;  
							matricula.setId_alu(rs.getInt("mat_id_alu")) ;  
							matricula.setId_fam(rs.getInt("mat_id_fam")) ;  
							matricula.setId_enc(rs.getInt("mat_id_enc")) ;  
							matricula.setId_con(rs.getInt("mat_id_con")) ;  
							matricula.setId_cli(rs.getInt("mat_id_cli")) ;  
							matricula.setId_per(rs.getInt("mat_id_per")) ;  
							matricula.setId_au(rs.getInt("mat_id_au")) ;
							matricula.setId_au_nue(rs.getInt("mat_id_au_asi"));
							matricula.setId_gra(rs.getInt("mat_id_gra")) ;  
							matricula.setId_niv(rs.getInt("mat_id_niv")) ;  
							matricula.setFecha(rs.getDate("mat_fecha")) ;  
							matricula.setCar_pod(rs.getString("mat_car_pod")) ;  
							matricula.setNum_cont(rs.getString("mat_num_cont")) ;  
							matricula.setObs(rs.getString("mat_obs")) ;  
							
							
							if (aTablas.contains("alu_familiar")){
								Familiar familiar= new Familiar();  
								familiar.setId(rs.getInt("fam_id")) ;  
								familiar.setId_tdc(rs.getInt("fam_id_tdc")) ;
								familiar.setApe_mat(rs.getString("fam_ape_mat")) ;
								familiar.setApe_pat(rs.getString("fam_ape_pat")) ;
								familiar.setNro_doc(rs.getString("fam_nro_doc")) ;

								familiar.setNom(rs.getString("fam_nom")) ;
								
								TipoDocumento tipoDocumento = new TipoDocumento();
								tipoDocumento.setNom(rs.getString("tdc_nom"));
								familiar.setTipoDocumento(tipoDocumento);
								matricula.setFamiliar(familiar);
								
							}
							if (aTablas.contains("seg_usuario")){
								Usuario usuario = new Usuario();
								Trabajador trabajador = new Trabajador();
								trabajador.setApe_pat(rs.getString("tra_ape_pat"));
								trabajador.setApe_mat(rs.getString("tra_ape_mat"));
								trabajador.setNom(rs.getString("tra_nom"));
								usuario.setTrabajador(trabajador);
								academicopago.setUsuario(usuario);
							}
							
							
							academicopago.setMatricula(matricula);
					}
							return academicopago;
				}
				
				return null;
			}
			
		});


	}		
	
	public AcademicoPago getByParams(Param param) {

		String sql = "select * from fac_academico_pago " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<AcademicoPago>() {
			@Override
			public AcademicoPago extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<AcademicoPago> listByParams(Param param, String[] order) {

		String sql = "select * from fac_academico_pago " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<AcademicoPago>() {

			@Override
			public AcademicoPago mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<AcademicoPago> listFullByParams(AcademicoPago academicopago, String[] order) {
	
		return listFullByParams(Param.toParam("fac_acad",academicopago), order);
	
	}	
	
	public List<AcademicoPago> listFullByParams(Param param, String[] order) {

		String sql = "select fac_acad.id fac_acad_id, fac_acad.id_mat fac_acad_id_mat , fac_acad.id_bco_pag fac_acad_id_bco_pag, fac_acad.id_bec fac_acad_id_bec, fac_acad.tip fac_acad_tip , fac_acad.mens fac_acad_mens , fac_acad.saldo_favor fac_acad_saldo_favor, fac_acad.nro_cuota fac_acad_nro_cuota, fac_acad.tip_pag fac_acad_tip_pag, fac_acad.monto fac_acad_monto , fac_acad.canc fac_acad_canc , fac_acad.nro_rec fac_acad_nro_rec , fac_acad.nro_pe fac_acad_nro_pe , fac_acad.banco fac_acad_banco , fac_acad.fec_pago fac_acad_fec_pago, fac_acad.fec_venc fac_acad_fec_venc , fac_acad.desc_beca fac_acad_desc_beca, fac_acad.desc_hermano fac_acad_desc_hermano , fac_acad.desc_pronto_pago fac_acad_desc_pronto_pago , fac_acad.desc_pago_adelantado fac_acad_desc_pago_adelantado, fac_acad.desc_personalizado fac_acad_desc_personalizado, fac_acad.monto_total fac_acad_monto_total  ,fac_acad.est fac_acad_est ";
		sql = sql + ", mat.id mat_id  , mat.id_alu mat_id_alu , mat.id_fam mat_id_fam , mat.id_enc mat_id_enc , mat.id_con mat_id_con , mat.id_cli mat_id_cli , mat.id_per mat_id_per , mat.id_au mat_id_au , mat.id_gra mat_id_gra , mat.id_niv mat_id_niv , mat.fecha mat_fecha , mat.car_pod mat_car_pod , mat.num_cont mat_num_cont , mat.obs mat_obs  ";
		sql = sql + " from fac_academico_pago fac_acad";
		sql = sql + " left join mat_matricula mat on mat.id = fac_acad.id_mat ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<AcademicoPago>() {

			@Override
			public AcademicoPago mapRow(ResultSet rs, int rowNum) throws SQLException {
				AcademicoPago academicopago= rsToEntity(rs,"fac_acad_");
				Matricula matricula = new Matricula();  
				matricula.setId(rs.getInt("mat_id")) ;  
				matricula.setId_alu(rs.getInt("mat_id_alu")) ;  
				matricula.setId_fam(rs.getInt("mat_id_fam")) ;  
				matricula.setId_enc(rs.getInt("mat_id_enc")) ;  
				matricula.setId_con(rs.getInt("mat_id_con")) ;  
				matricula.setId_cli(rs.getInt("mat_id_cli")) ;  
				matricula.setId_per(rs.getInt("mat_id_per")) ;  
				matricula.setId_au(rs.getInt("mat_id_au")) ;  
				matricula.setId_gra(rs.getInt("mat_id_gra")) ;  
				matricula.setId_niv(rs.getInt("mat_id_niv")) ;  
				matricula.setFecha(rs.getDate("mat_fecha")) ;  
				matricula.setCar_pod(rs.getString("mat_car_pod")) ;  
				matricula.setNum_cont(rs.getString("mat_num_cont")) ;  
				matricula.setObs(rs.getString("mat_obs")) ;  
				academicopago.setMatricula(matricula);
				return academicopago;
			}

		});

	}	




	// funciones privadas utilitarias para AcademicoPago

	private AcademicoPago rsToEntity(ResultSet rs,String alias) throws SQLException {
		AcademicoPago academico_pago = new AcademicoPago();

		academico_pago.setId(rs.getInt( alias + "id"));
		academico_pago.setId_mat(rs.getInt( alias + "id_mat"));
		academico_pago.setId_bco_pag(rs.getInt( alias + "id_bco_pag"));
		academico_pago.setId_bec(rs.getInt( alias + "id_bec"));
		academico_pago.setTip(rs.getString( alias + "tip"));
		academico_pago.setMens(rs.getInt( alias + "mens"));
		academico_pago.setNro_cuota(rs.getInt( alias + "nro_cuota"));
		academico_pago.setTip_pag(rs.getString( alias + "tip_pag"));
		academico_pago.setMonto(rs.getBigDecimal( alias + "monto"));
		academico_pago.setMontoTotal(rs.getBigDecimal( alias + "monto_total"));
		academico_pago.setCanc(rs.getString( alias + "canc"));
		academico_pago.setNro_rec(rs.getString( alias + "nro_rec"));
		academico_pago.setNro_pe(rs.getString( alias + "nro_pe"));
		academico_pago.setBanco(rs.getString( alias + "banco"));
		academico_pago.setFec_pago(rs.getDate( alias + "fec_pago"));
		academico_pago.setFec_venc(rs.getDate( alias + "fec_venc"));
		academico_pago.setDesc_beca(rs.getBigDecimal( alias + "desc_beca"));
		academico_pago.setDesc_hermano(rs.getBigDecimal( alias + "desc_hermano"));
		academico_pago.setDesc_pronto_pago(rs.getBigDecimal( alias + "desc_pronto_pago"));
		academico_pago.setDesc_pago_adelantado(rs.getBigDecimal( alias + "desc_pago_adelantado"));
		academico_pago.setDesc_personalizado(rs.getBigDecimal( alias + "desc_personalizado"));
		academico_pago.setEst(rs.getString( alias + "est"));
		//academico_pago.setConcepto(null);
		//academico_pago.setAnio(null);
		//academico_pago.setNombre_mes(null);
		
		if(rs.getBigDecimal( alias + "saldo_favor")==null)
			academico_pago.setSaldo_favor(BigDecimal.ZERO);
		else
			academico_pago.setSaldo_favor(rs.getBigDecimal( alias + "saldo_favor"));

		return academico_pago;

	}
	
	public List<Map<String, Object>> reportePagosInfocorp(Integer anio, int id_anio,int mes_fin, int id_mes, Integer id_suc) {
		int anio_sig=anio+1;
		if(id_suc!=null){
			String sql =" SELECT o.fec_con, o.id_fam , CONCAT(o.ape_pat,' ',o.ape_mat,' ', o.nom) familiar ,o.id_alu, o.aula, o.direccion, o.dis_nom, o.dep_nom, o.`id`, o.`id_tdc`, o.numdoc, o.tip_per, o.codigo, o.tip_deudor, o.doc_cre,  "
					+ " o.tip_mo, o.cantidad, o.mens, o.monto, DATE_FORMAT( o.fecha_ven,'%d/%m/%Y') fecha_ven, o.num_cont FROM "
					+ " (SELECT  DATE_FORMAT(CURDATE(),'%d/%m/%Y') fec_con, f.ape_pat,f.ape_mat,f.nom, alu.id id_alu, CONCAT(gra.`nom`,' ', au.`secc`,' - ',niv.nom) aula, alu.`ape_pat` alu_ape_pat, alu.`ape_mat` alu_ape_mat, alu.`nom` alu_nom, "
					+ " f.direccion, f.dis_nom, f.dep_nom, fac.`id`, f.`id_tdc`,f.`nro_doc` numdoc,"
					+ " IF(f.id_tdc=1,1,0) tip_per,  '094490' codigo, 1 tip_deudor, 'PA' doc_cre, '01' tip_mo,  f.id_fam, f.cantidad, fac.`mens`,fac.`monto`, "
					+ " IF (fac.mens=12,DATE_FORMAT( CONCAT('"+anio_sig+"-01-',conf.`dia_mora`),'%Y/%m/%d'), DATE_FORMAT( CONCAT('"+anio+"-',fac.`mens`+1,'-',conf.`dia_mora`),'%Y/%m/%d')) AS fecha_ven, mat.`num_cont` "
					+ " FROM `fac_academico_pago` fac INNER JOIN `mat_matricula` mat ON fac.`id_mat`=mat.`id` AND (mat.id_sit is null OR mat.id_sit<>5)"
					+ " INNER JOIN `alu_alumno` alu ON mat.`id_alu`=alu.`id` "
					+ " INNER JOIN ("
					+ " SELECT t.* ,COUNT(*) cantidad  "
					+ " FROM (SELECT fam.`ape_pat`, fam.`ape_mat`, fam.`nom` , fam.id id_fam, fam.`id_tdc`, g.`direccion`, dis.`nom` dis_nom, dep.`nom` dep_nom, fam.`nro_doc`,"
					+ " IF (fac.mens=12,DATE_FORMAT( CONCAT('"+anio_sig+"-01-',conf.`dia_mora`),'%Y/%m/%d'), DATE_FORMAT( CONCAT('"+anio+"-',fac.`mens`+1,'-',conf.`dia_mora`),'%Y/%m/%d')) AS fecha_ven "
					+ " FROM `fac_academico_pago` fac INNER JOIN `mat_matricula` mat ON fac.`id_mat`=mat.`id` "
					+ " INNER JOIN `alu_familiar` fam ON mat.`id_fam`=fam.id "
					+ " INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.`id` "
					+ " INNER JOIN `per_periodo` per ON au.`id_per`=per.id "
					+ " INNER JOIN `alu_gru_fam_familiar` gf ON fam.id=gf.`id_fam` "
					+ " INNER JOIN `alu_gru_fam` g ON gf.`id_gpf`=g.`id` "
					+ " INNER JOIN `alu_gru_fam_alumno` gfa ON gfa.`id_alu`=mat.`id_alu` AND gfa.`id_gpf`=g.`id`  "
					+ " LEFT JOIN `cat_distrito` dis ON g.`id_dist`=dis.`id` "
					+ " LEFT JOIN `cat_provincia` pro ON dis.`id_pro`=pro.`id` "
					+ " LEFT JOIN `cat_departamento` dep ON pro.`id_dep`=dep.`id` "
					+ " INNER JOIN `mat_conf_mensualidad` conf  ON conf.`id_per`=per.`id`"
					+ " WHERE fac.`canc`='0' AND per.id_suc="+id_suc+" AND per.`id_anio`="+id_anio+" AND fac.`tip`='MEN' and (mat.id_sit is null or mat.id_sit not in (4,5,6) )"
					+ " AND (mens BETWEEN 3 AND "+mes_fin+")) t "
					+ " WHERE  CURDATE()>t.fecha_ven"
					+ " GROUP BY t.id_fam  HAVING COUNT(t.id_fam)>=1) f"
					+ " ON mat.`id_fam`=f.id_fam "
					+ " INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.`id` "
					+ " INNER JOIN `cat_grad` gra ON au.`id_grad`=gra.`id` "
					+ " INNER JOIN cat_nivel niv ON gra.id_nvl=niv.id "
					+ " INNER JOIN `per_periodo` per ON au.`id_per`=per.`id` "
					+ " INNER JOIN `mat_conf_mensualidad` conf ON conf.`id_per`=per.`id` "
					+ " WHERE fac.`canc`=0 AND per.`id_suc`="+id_suc+" AND (mens BETWEEN 3 AND "+mes_fin+"))o "
					+ " WHERE CURDATE()>o.fecha_ven AND o.cantidad>="+id_mes
					+ " ORDER BY o.`ape_pat`, o.`ape_mat`, o.fecha_ven";
			//logger.info(sql);
			return jdbcTemplate.queryForList(sql);
		} else {
			String sql =" SELECT o.fec_con, o.id_fam , CONCAT(o.ape_pat,' ',o.ape_mat,' ', o.nom) familiar,o.id_alu, o.aula, o.direccion, o.dis_nom, o.dep_nom, o.`id`, o.`id_tdc`, o.numdoc, o.tip_per, o.codigo, o.tip_deudor, o.doc_cre,  "
					+ " o.tip_mo, o.cantidad, o.mens, o.monto, DATE_FORMAT( o.fecha_ven,'%d/%m/%Y') fecha_ven, o.num_cont FROM "
					+ " (SELECT  DATE_FORMAT(CURDATE(),'%d/%m/%Y') fec_con, f.ape_pat,f.ape_mat,f.nom, alu.id id_alu, CONCAT(gra.`nom`,' ', au.`secc`,' - ',niv.nom) aula, alu.`ape_pat` alu_ape_pat, alu.`ape_mat` alu_ape_mat, alu.`nom` alu_nom, "
					+ " f.direccion, f.dis_nom, f.dep_nom, fac.`id`, f.`id_tdc`,f.`nro_doc` numdoc,"
					+ " IF(f.id_tdc=1,1,0) tip_per,  '094490' codigo, 1 tip_deudor, 'PA' doc_cre, '01' tip_mo,  f.id_fam, f.cantidad, fac.`mens`,fac.`monto`, "
					+ " IF (fac.mens=12,DATE_FORMAT( CONCAT('"+anio_sig+"-01-',conf.`dia_mora`),'%Y/%m/%d'), DATE_FORMAT( CONCAT('"+anio+"-',fac.`mens`+1,'-',conf.`dia_mora`),'%Y/%m/%d')) AS fecha_ven, mat.`num_cont` "
					+ " FROM `fac_academico_pago` fac INNER JOIN `mat_matricula` mat ON fac.`id_mat`=mat.`id` AND (mat.id_sit is null OR mat.id_sit<>5)"
					+ " INNER JOIN `alu_alumno` alu ON mat.`id_alu`=alu.`id` "
					+ " INNER JOIN ("
					+ " SELECT t.* ,COUNT(*) cantidad  "
					+ " FROM (SELECT fam.`ape_pat`, fam.`ape_mat`, fam.`nom` , fam.id id_fam, fam.`id_tdc`, g.`direccion`, dis.`nom` dis_nom, dep.`nom` dep_nom, fam.`nro_doc`,"
					+ " IF (fac.mens=12,DATE_FORMAT( CONCAT('"+anio_sig+"-01-',conf.`dia_mora`),'%Y/%m/%d'), DATE_FORMAT( CONCAT('"+anio+"-',fac.`mens`+1,'-',conf.`dia_mora`),'%Y/%m/%d')) AS fecha_ven "
					+ " FROM `fac_academico_pago` fac INNER JOIN `mat_matricula` mat ON fac.`id_mat`=mat.`id` "
					+ " INNER JOIN `alu_familiar` fam ON mat.`id_fam`=fam.id "
					+ " INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.`id` "
					+ " INNER JOIN `per_periodo` per ON au.`id_per`=per.id "
					+ " INNER JOIN `alu_gru_fam_familiar` gf ON fam.id=gf.`id_fam` "
					+ " INNER JOIN `alu_gru_fam` g ON gf.`id_gpf`=g.`id` "
					+ " INNER JOIN `alu_gru_fam_alumno` gfa ON gfa.`id_alu`=mat.`id_alu` AND gfa.`id_gpf`=g.`id`  "
					+ " LEFT JOIN `cat_distrito` dis ON g.`id_dist`=dis.`id` "
					+ " LEFT JOIN `cat_provincia` pro ON dis.`id_pro`=pro.`id` "
					+ " LEFT JOIN `cat_departamento` dep ON pro.`id_dep`=dep.`id` "
					+ " INNER JOIN `mat_conf_mensualidad` conf  ON conf.`id_per`=per.`id`"
					+ " WHERE fac.`canc`='0' AND per.`id_anio`="+id_anio+" AND fac.`tip`='MEN' and (mat.id_sit is null or mat.id_sit not in (4,5,6) ) "
					+ " AND (mens BETWEEN 3 AND "+mes_fin+")) t "
					+ " WHERE  CURDATE()>t.fecha_ven"
					+ " GROUP BY t.id_fam  HAVING COUNT(t.id_fam)>=1) f"
					+ " ON mat.`id_fam`=f.id_fam "
					+ " INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.`id` "
					+ " INNER JOIN `cat_grad` gra ON au.`id_grad`=gra.`id` "
					+ " INNER JOIN cat_nivel niv ON gra.id_nvl=niv.id "
					+ " INNER JOIN `per_periodo` per ON au.`id_per`=per.`id` "
					+ " INNER JOIN `mat_conf_mensualidad` conf ON conf.`id_per`=per.`id` "
					+ " WHERE fac.`canc`=0 AND (mens BETWEEN 3 AND "+mes_fin+"))o "
					+ " WHERE CURDATE()>o.fecha_ven AND o.cantidad>="+id_mes
					+ " ORDER BY o.`ape_pat`, o.`ape_mat`, o.fecha_ven";
			//logger.info(sql);
			return jdbcTemplate.queryForList(sql);
		}
		

	}
	
	public List<Map<String, Object>> cartaCobranza(Integer anio, int id_anio,int mes_fin, int id_mes, Integer id_suc){
		
		List<Map<String,Object>>  familiares = new ArrayList<Map<String,Object>>();	
		int anio_sig=anio+1;
		if(id_suc!=null){
			/*String sqlFamiliares="SELECT * FROM (SELECT concat(fam.ape_pat,' ', fam.ape_mat,' ', fam.nom) familiar, fam.id id, fam.nro_doc, mat.num_cont, COUNT(*) cantidad, SUM(fac.monto) deuda_total"
					+ " FROM `fac_academico_pago` fac INNER JOIN `mat_matricula` mat ON fac.`id_mat`=mat.`id`"
					+ " INNER JOIN `alu_familiar` fam ON mat.`id_fam`=fam.id"
					+ " INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.`id`"
					+ " INNER JOIN `per_periodo` per ON au.`id_per`=per.id"
					+ " INNER JOIN `alu_gru_fam_familiar` gf ON fam.id=gf.`id_fam`"
					+ " INNER JOIN `alu_gru_fam` g ON gf.`id_gpf`=g.`id`"
					+ " INNER JOIN `cat_distrito` dis ON g.`id_dist`=dis.`id`"
					+ " INNER JOIN `cat_provincia` pro ON dis.`id_pro`=pro.`id`"
					+ " INNER JOIN `cat_departamento` dep ON pro.`id_dep`=dep.`id`"
					+ " WHERE fac.`canc`='0' AND per.id_suc="+id_suc+" AND per.`id_anio`="+id_anio+" AND fac.`tip`='MEN' AND (mens BETWEEN 3 AND "+mes_fin+")"
					+ " GROUP BY fam.id "
					+ " HAVING COUNT(fam.id)>=1 order by fam.ape_pat, fam.ape_mat) t WHERE t.cantidad>="+id_mes;*/
			String sqlFamiliares="SELECT * FROM (SELECT t.*, COUNT(*) cantidad , SUM(t.monto) deuda_total FROM "
					+ " (SELECT CONCAT(fam.ape_pat,' ', fam.ape_mat,' ', fam.nom) familiar, fam.id id, fam.nro_doc, mat.num_cont,"
					+ " IF (fac.mens=12,DATE_FORMAT( CONCAT('"+anio_sig+"-01-',conf.`dia_mora`),'%Y/%m/%d'), DATE_FORMAT( CONCAT('"+anio+"-',fac.`mens`+1,'-',conf.`dia_mora`),'%Y/%m/%d')) AS fecha_ven, fac.monto"
					+ " FROM `fac_academico_pago` fac INNER JOIN `mat_matricula` mat ON fac.`id_mat`=mat.`id` AND (mat.id_sit is null OR mat.id_sit<>5)"
					+ " INNER JOIN `alu_familiar` fam ON mat.`id_fam`=fam.id"
					+ " INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.`id`"
					+ " INNER JOIN `per_periodo` per ON au.`id_per`=per.id"
					+ " INNER JOIN `mat_conf_mensualidad` conf  ON conf.`id_per`=per.`id`"
					+ " INNER JOIN `alu_gru_fam_familiar` gf ON fam.id=gf.`id_fam`"
					+ " INNER JOIN `alu_gru_fam` g ON gf.`id_gpf`=g.`id`"
					+ " INNER JOIN `alu_gru_fam_alumno` gfa ON gfa.`id_alu`=mat.`id_alu` AND gfa.`id_gpf`=g.`id`  "
					+ " LEFT JOIN `cat_distrito` dis ON g.`id_dist`=dis.`id`"
					+ " LEFT JOIN `cat_provincia` pro ON dis.`id_pro`=pro.`id`"
					+ " LEFT JOIN `cat_departamento` dep ON pro.`id_dep`=dep.`id`"
					+ " WHERE fac.`canc`='0' AND per.id_suc="+id_suc+" AND per.`id_anio`="+id_anio+" AND fac.`tip`='MEN' and (mat.id_sit is null or mat.id_sit not in (4,5,6)) AND (mens BETWEEN 3 AND "+mes_fin+")"
					+ " ORDER BY fam.ape_pat, fam.ape_mat) t"
					+ " WHERE  CURDATE()>t.fecha_ven"
					+ " GROUP BY t.id "
					+ " HAVING COUNT(t.id)>=1) o WHERE o.cantidad>="+id_mes;
			//logger.info(sqlFamiliares);
			familiares = jdbcTemplate.queryForList(sqlFamiliares);
		} else{
			String sqlFamiliares="SELECT * FROM (SELECT t.*, COUNT(*) cantidad , SUM(t.monto) deuda_total FROM "
					+ " (SELECT CONCAT(fam.ape_pat,' ', fam.ape_mat,' ', fam.nom) familiar, fam.id id, fam.nro_doc, mat.num_cont,"
					+ " IF (fac.mens=12,DATE_FORMAT( CONCAT('"+anio_sig+"-01-',conf.`dia_mora`),'%Y/%m/%d'), DATE_FORMAT( CONCAT('"+anio+"-',fac.`mens`+1,'-',conf.`dia_mora`),'%Y/%m/%d')) AS fecha_ven, fac.monto"
					+ " FROM `fac_academico_pago` fac INNER JOIN `mat_matricula` mat ON fac.`id_mat`=mat.`id` AND (mat.id_sit is null OR mat.id_sit<>5)"
					+ " INNER JOIN `alu_familiar` fam ON mat.`id_fam`=fam.id"
					+ " INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.`id`"
					+ " INNER JOIN `per_periodo` per ON au.`id_per`=per.id"
					+ " INNER JOIN `mat_conf_mensualidad` conf  ON conf.`id_per`=per.`id`"
					+ " INNER JOIN `alu_gru_fam_familiar` gf ON fam.id=gf.`id_fam`"
					+ " INNER JOIN `alu_gru_fam` g ON gf.`id_gpf`=g.`id`"
					+ " INNER JOIN `alu_gru_fam_alumno` gfa ON gfa.`id_alu`=mat.`id_alu` AND gfa.`id_gpf`=g.`id`  "
					+ " LEFT JOIN `cat_distrito` dis ON g.`id_dist`=dis.`id`"
					+ " LEFT JOIN `cat_provincia` pro ON dis.`id_pro`=pro.`id`"
					+ " LEFT JOIN `cat_departamento` dep ON pro.`id_dep`=dep.`id`"
					+ " WHERE fac.`canc`='0' AND per.`id_anio`="+id_anio+" AND fac.`tip`='MEN' and (mat.id_sit is null or mat.id_sit not in (4,5,6)) AND (mens BETWEEN 3 AND "+mes_fin+")"
					+ " ORDER BY fam.ape_pat, fam.ape_mat) t"
					+ " WHERE  CURDATE()>t.fecha_ven"
					+ " GROUP BY t.id "
					+ " HAVING COUNT(t.id)>=1) o WHERE o.cantidad>="+id_mes;
			//logger.info(sqlFamiliares);
			familiares = jdbcTemplate.queryForList(sqlFamiliares);
		}
		LinkedHashMap<String,Map<String, Object>> linkfamiliar = new LinkedHashMap<String,Map<String, Object>>();
		for (Map<String, Object> map : familiares) {
			Map<String, Object> detalle = new HashMap<String,Object>();
			detalle.put("familiar", map.get("familiar"));
			detalle.put("dni", map.get("nro_doc"));
			detalle.put("deuda", map.get("deuda_total"));
			detalle.put("numero", map.get("num_cont"));
			//detalle.put("hijos",  null);
			//detalle.put("aulas",  null);
			//detalle.put("grado_secc",  null);
			detalle.put("meses",  null);
			linkfamiliar.put(map.get("id").toString(), detalle);			
		}	
		
		String sql="";
		List<Map<String,Object>>  familiaresMorosos = new ArrayList<Map<String,Object>>();
		
		if(id_suc!=null){
			
		sql =" SELECT o.fec_con, o.id_fam , CONCAT(o.alu_ape_pat,' ',o.alu_ape_mat,' ', o.alu_nom) alumno,o.id_alu, o.aula, o.direccion, o.dis_nom, o.dep_nom, o.`id`, o.`id_tdc`, o.numdoc, o.tip_per, o.codigo, o.tip_deudor, o.doc_cre,  "
					+ " o.tip_mo, o.cantidad, o.mens, o.monto, DATE_FORMAT( o.fecha_ven,'%d/%m/%Y') fecha_ven, o.num_cont FROM "
					+ " (SELECT  DATE_FORMAT(CURDATE(),'%d/%m/%Y') fec_con, f.ape_pat,f.ape_mat,f.nom, alu.id id_alu, CONCAT(gra.`nom`,' ', au.`secc`,' - ',niv.nom) aula, alu.`ape_pat` alu_ape_pat, alu.`ape_mat` alu_ape_mat, alu.`nom` alu_nom, "
					+ " f.direccion, f.dis_nom, f.dep_nom, fac.`id`, f.`id_tdc`,f.`nro_doc` numdoc,"
					+ " IF(f.id_tdc=1,1,0) tip_per,  '094490' codigo, 1 tip_deudor, 'PA' doc_cre, '01' tip_mo,  f.id_fam, f.cantidad, fac.`mens`,fac.`monto`, "
					+ " IF (fac.mens=12,DATE_FORMAT( CONCAT('"+anio_sig+"-01-',conf.`dia_mora`),'%Y/%m/%d'), DATE_FORMAT( CONCAT('"+anio+"-',fac.`mens`+1,'-',conf.`dia_mora`),'%Y/%m/%d')) AS fecha_ven, mat.`num_cont` "
					+ " FROM `fac_academico_pago` fac INNER JOIN `mat_matricula` mat ON fac.`id_mat`=mat.`id` AND (mat.id_sit is null OR mat.id_sit<>5) "
					+ " INNER JOIN `alu_alumno` alu ON mat.`id_alu`=alu.`id` "
					+ " INNER JOIN ("
					+ " SELECT t.* ,COUNT(*) cantidad  "
					+ " FROM (SELECT fam.`ape_pat`, fam.`ape_mat`, fam.`nom` , fam.id id_fam, fam.`id_tdc`, g.`direccion`, dis.`nom` dis_nom, dep.`nom` dep_nom, fam.`nro_doc`,"
					+ " IF (fac.mens=12,DATE_FORMAT( CONCAT('"+anio_sig+"-01-',conf.`dia_mora`),'%Y/%m/%d'), DATE_FORMAT( CONCAT('"+anio+"-',fac.`mens`+1,'-',conf.`dia_mora`),'%Y/%m/%d')) AS fecha_ven "
					+ " FROM `fac_academico_pago` fac INNER JOIN `mat_matricula` mat ON fac.`id_mat`=mat.`id` "
					+ " INNER JOIN `alu_familiar` fam ON mat.`id_fam`=fam.id "
					+ " INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.`id` "
					+ " INNER JOIN `per_periodo` per ON au.`id_per`=per.id "
					+ " INNER JOIN `alu_gru_fam_familiar` gf ON fam.id=gf.`id_fam` "
					+ " INNER JOIN `alu_gru_fam` g ON gf.`id_gpf`=g.`id` "
					+ " INNER JOIN `alu_gru_fam_alumno` gfa ON gfa.`id_alu`=mat.`id_alu` AND gfa.`id_gpf`=g.`id`  "
					+ " LEFT JOIN `cat_distrito` dis ON g.`id_dist`=dis.`id` "
					+ " LEFT JOIN `cat_provincia` pro ON dis.`id_pro`=pro.`id` "
					+ " LEFT JOIN `cat_departamento` dep ON pro.`id_dep`=dep.`id` "
					+ " INNER JOIN `mat_conf_mensualidad` conf  ON conf.`id_per`=per.`id`"
					+ " WHERE fac.`canc`='0' AND per.id_suc="+id_suc+" AND per.`id_anio`="+id_anio+" AND fac.`tip`='MEN' and (mat.id_sit is null or mat.id_sit not in (4,5,6) ) "
					+ " AND (mens BETWEEN 3 AND "+mes_fin+")) t "
					+ " WHERE  CURDATE()>t.fecha_ven"
					+ " GROUP BY t.id_fam  HAVING COUNT(t.id_fam)>=1) f"
					+ " ON mat.`id_fam`=f.id_fam "
					+ " INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.`id` "
					+ " INNER JOIN `cat_grad` gra ON au.`id_grad`=gra.`id` "
					+ " INNER JOIN cat_nivel niv ON gra.id_nvl=niv.id "
					+ " INNER JOIN `per_periodo` per ON au.`id_per`=per.`id` "
					+ " INNER JOIN `mat_conf_mensualidad` conf ON conf.`id_per`=per.`id` "
					+ " WHERE fac.`canc`=0 AND per.`id_suc`="+id_suc+" AND (mens BETWEEN 3 AND "+mes_fin+"))o "
					+ " WHERE CURDATE()>o.fecha_ven AND o.cantidad>="+id_mes
					+ " ORDER BY o.`alu_ape_pat`, o.`alu_ape_mat`, o.fecha_ven";
			//logger.info(sql);
			
			familiaresMorosos = jdbcTemplate.queryForList(sql);
			
		} else {//LISTA DE LOS FAMILIARES MOROSOS
			 sql =" SELECT o.fec_con, o.id_fam , CONCAT(o.alu_ape_pat,' ',o.alu_ape_mat,' ', o.alu_nom) alumno,o.id_alu, o.aula, o.direccion, o.dis_nom, o.dep_nom, o.`id`, o.`id_tdc`, o.numdoc, o.tip_per, o.codigo, o.tip_deudor, o.doc_cre,  "
						+ " o.tip_mo, o.cantidad, o.mens, o.monto, DATE_FORMAT( o.fecha_ven,'%d/%m/%Y') fecha_ven, o.num_cont FROM "
						+ " (SELECT  DATE_FORMAT(CURDATE(),'%d/%m/%Y') fec_con, f.ape_pat,f.ape_mat,f.nom, alu.id id_alu, CONCAT(gra.`nom`,' ', au.`secc`,' - ',niv.nom) aula, alu.`ape_pat` alu_ape_pat, alu.`ape_mat` alu_ape_mat, alu.`nom` alu_nom, "
						+ " f.direccion, f.dis_nom, f.dep_nom, fac.`id`, f.`id_tdc`,f.`nro_doc` numdoc,"
						+ " IF(f.id_tdc=1,1,0) tip_per,  '094490' codigo, 1 tip_deudor, 'PA' doc_cre, '01' tip_mo,  f.id_fam, f.cantidad, fac.`mens`,fac.`monto`, "
						+ " IF (fac.mens=12,DATE_FORMAT( CONCAT('"+anio_sig+"-01-',conf.`dia_mora`),'%Y/%m/%d'), DATE_FORMAT( CONCAT('"+anio+"-',fac.`mens`+1,'-',conf.`dia_mora`),'%Y/%m/%d')) AS fecha_ven, mat.`num_cont` "
						+ " FROM `fac_academico_pago` fac INNER JOIN `mat_matricula` mat ON fac.`id_mat`=mat.`id` AND (mat.id_sit is null OR mat.id_sit<>5) "
						+ " INNER JOIN `alu_alumno` alu ON mat.`id_alu`=alu.`id` "
						+ " INNER JOIN ("
						+ " SELECT t.* ,COUNT(*) cantidad  "
						+ " FROM (SELECT fam.`ape_pat`, fam.`ape_mat`, fam.`nom` , fam.id id_fam, fam.`id_tdc`, g.`direccion`, dis.`nom` dis_nom, dep.`nom` dep_nom, fam.`nro_doc`,"
						+ " IF (fac.mens=12,DATE_FORMAT( CONCAT('"+anio_sig+"-01-',conf.`dia_mora`),'%Y/%m/%d'), DATE_FORMAT( CONCAT('"+anio+"-',fac.`mens`+1,'-',conf.`dia_mora`),'%Y/%m/%d')) AS fecha_ven "
						+ " FROM `fac_academico_pago` fac INNER JOIN `mat_matricula` mat ON fac.`id_mat`=mat.`id` "
						+ " INNER JOIN `alu_familiar` fam ON mat.`id_fam`=fam.id "
						+ " INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.`id` "
						+ " INNER JOIN `per_periodo` per ON au.`id_per`=per.id "
						+ " INNER JOIN `alu_gru_fam_familiar` gf ON fam.id=gf.`id_fam` "
						+ " INNER JOIN `alu_gru_fam` g ON gf.`id_gpf`=g.`id` "
						+ " INNER JOIN `alu_gru_fam_alumno` gfa ON gfa.`id_alu`=mat.`id_alu` AND gfa.`id_gpf`=g.`id`  "
						+ " LEFT JOIN `cat_distrito` dis ON g.`id_dist`=dis.`id` "
						+ " LEFT JOIN `cat_provincia` pro ON dis.`id_pro`=pro.`id` "
						+ " LEFT JOIN `cat_departamento` dep ON pro.`id_dep`=dep.`id` "
						+ " INNER JOIN `mat_conf_mensualidad` conf  ON conf.`id_per`=per.`id`"
						+ " WHERE fac.`canc`='0'  AND per.`id_anio`="+id_anio+" AND fac.`tip`='MEN' and (mat.id_sit is null or mat.id_sit not in (4,5,6))"
						+ " AND (mens BETWEEN 3 AND "+mes_fin+")) t "
						+ " WHERE  CURDATE()>t.fecha_ven"
						+ " GROUP BY t.id_fam  HAVING COUNT(t.id_fam)>=1) f"
						+ " ON mat.`id_fam`=f.id_fam "
						+ " INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.`id` "
						+ " INNER JOIN `cat_grad` gra ON au.`id_grad`=gra.`id` "
						+ " INNER JOIN cat_nivel niv ON gra.id_nvl=niv.id "
						+ " INNER JOIN `per_periodo` per ON au.`id_per`=per.`id` "
						+ " INNER JOIN `mat_conf_mensualidad` conf ON conf.`id_per`=per.`id` "
						+ " WHERE fac.`canc`=0 AND (mens BETWEEN 3 AND "+mes_fin+"))o "
						+ " WHERE CURDATE()>o.fecha_ven AND o.cantidad>="+id_mes
						+ " ORDER BY o.`alu_ape_pat`, o.`alu_ape_mat`, o.fecha_ven";
			//logger.info(sql);
			
			familiaresMorosos = jdbcTemplate.queryForList(sql);

		}
		
		List<Map<String,Object>>  hijosMorosos = new ArrayList<Map<String,Object>>();
		String sqlhijos = "SELECT DISTINCT m.id_fam, m.alumno, m.id_alu, m.aula FROM ("
				+ sql +") m";
		hijosMorosos = jdbcTemplate.queryForList(sqlhijos);
		for (Map<String, Object> map : hijosMorosos) {
			Map<String, Object> familiar =linkfamiliar.get(map.get("id_fam").toString());
			Object hijosObject = familiar.get("hijo");//null
			if (hijosObject==null ){				
				List<FamiliarDeudaBean> hijos= new ArrayList<FamiliarDeudaBean>();
				FamiliarDeudaBean pago = new FamiliarDeudaBean();
				pago.setHijos((String)map.get("alumno"));
				pago.setAulas((String)map.get("aula"));
				hijos.add(pago);
				familiar.put("hijo", hijos);
			}else{
				List<FamiliarDeudaBean> hijos = (List<FamiliarDeudaBean>)hijosObject;
				FamiliarDeudaBean pago = new FamiliarDeudaBean();
				pago.setHijos((String)map.get("alumno"));
				pago.setAulas((String)map.get("aula"));
				hijos.add(pago);
				familiar.put("hijo", hijos);
			}
		}
		
		for (Map<String, Object> map : familiaresMorosos) {
			Map<String, Object> familiar =linkfamiliar.get(map.get("id_fam").toString());
			Object mesesObject = familiar.get("meses");		
			if (mesesObject==null ){				
				List<FamiliarDeudaBean> meses_deuda= new ArrayList<FamiliarDeudaBean>();
				FamiliarDeudaBean pago = new FamiliarDeudaBean();
				pago.setMes_deuda((Integer)map.get("mens"));
				meses_deuda.add(pago);
				familiar.put("meses", meses_deuda);
			}else{
				List<FamiliarDeudaBean> meses_deuda = (List<FamiliarDeudaBean>)mesesObject;
				FamiliarDeudaBean pago = new FamiliarDeudaBean();
				pago.setMes_deuda((Integer)map.get("mens"));
				meses_deuda.add(pago);
				familiar.put("meses", meses_deuda);
			}	
		}
		
		Map<String, Object> map = new HashMap<String, Object>();		
		map.put("familiares", linkfamiliar);	
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		list.add(map);
		return list;	
	}
	
	public  Row getByMes(Integer id_mat,String tipo, Integer mes,Integer canc) {

		String sql = "select * from fac_academico_pago " ;
		sql += "where id_mat=:id_mat "
				+ " and tip=:tipo "
				+ " and canc=:canc";
		
		if(mes!=null){
			sql += " and mens=:mes ";			
		}
		
		Param param = new Param();
		param.put("id_mat", id_mat);
		param.put("tipo", tipo);
		param.put("canc", canc);
		param.put("mes", mes);
		
		List<Row> pagos = sqlUtil.query(sql,param);
		 
		if(pagos.size()==0)
			return null;
		else
			return pagos.get(0);

	}	
}
