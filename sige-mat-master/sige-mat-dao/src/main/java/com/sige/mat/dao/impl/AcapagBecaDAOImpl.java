package com.sige.mat.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Arrays;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import com.tesla.colegio.model.AcapagBeca;

import com.tesla.colegio.model.AcademicoPago;
import com.tesla.colegio.model.Beca;
import com.tesla.colegio.model.MotivoBeca;

import com.sige.web.security.TokenSeguridad;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface AcapagBecaDAO.
 * @author MV
 *
 */
public class AcapagBecaDAOImpl{
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@Autowired
	private TokenSeguridad tokenSeguridad;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(AcapagBeca acapag_beca) {
		if (acapag_beca.getId() != null) {
			// update
			String sql = "UPDATE fac_acapag_beca "
						+ "SET id_fac=?, "
						+ "id_bec=?, "
						+ "id_mbec=?, "
						+ "monto_total=?, "
						+ "monto_afectado=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			System.out.println(sql);

			jdbcTemplate.update(sql, 
						acapag_beca.getId_fac(),
						acapag_beca.getId_bec(),
						acapag_beca.getId_mbec(),
						acapag_beca.getMonto_total(),
						acapag_beca.getMonto_afectado(),
						acapag_beca.getEst(),
						tokenSeguridad.getId(),
						new java.util.Date(),
						acapag_beca.getId()); 
			return acapag_beca.getId();

		} else {
			// insert
			String sql = "insert into fac_acapag_beca ("
						+ "id_fac, "
						+ "id_bec, "
						+ "id_mbec, "
						+ "monto_total, "
						+ "monto_afectado, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?)";

				System.out.println(sql);

				jdbcTemplate.update(sql, 
				acapag_beca.getId_fac(),
				acapag_beca.getId_bec(),
				acapag_beca.getId_mbec(),
				acapag_beca.getMonto_total(),
				acapag_beca.getMonto_afectado(),
				acapag_beca.getEst(),
				tokenSeguridad.getId(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from fac_acapag_beca where id=?";
		
		System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<AcapagBeca> list() {
		String sql = "select * from fac_acapag_beca";
		
		System.out.println(sql);
		
		List<AcapagBeca> listAcapagBeca = jdbcTemplate.query(sql, new RowMapper<AcapagBeca>() {

			@Override
			public AcapagBeca mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listAcapagBeca;
	}

	public AcapagBeca get(int id) {
		String sql = "select * from fac_acapag_beca WHERE id=" + id;
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<AcapagBeca>() {

			@Override
			public AcapagBeca extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public AcapagBeca getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select Academico Pago Beca.id Academico Pago Beca_id, Academico Pago Beca.id_fac Academico Pago Beca_id_fac , Academico Pago Beca.id_bec Academico Pago Beca_id_bec , Academico Pago Beca.id_mbec Academico Pago Beca_id_mbec , Academico Pago Beca.monto_total Academico Pago Beca_monto_total , Academico Pago Beca.monto_afectado Academico Pago Beca_monto_afectado  ,Academico Pago Beca.est Academico Pago Beca_est ";
		if (aTablas.contains("fac_academico_pago"))
			sql = sql + ", fac_acad.id fac_acad_id  , fac_acad.id_mat fac_acad_id_mat , fac_acad.tip fac_acad_tip , fac_acad.mens fac_acad_mens , fac_acad.monto fac_acad_monto , fac_acad.canc fac_acad_canc , fac_acad.nro_rec fac_acad_nro_rec , fac_acad.nro_pe fac_acad_nro_pe , fac_acad.banco fac_acad_banco , fac_acad.fec_pago fac_acad_fec_pago , fac_acad.desc_hermano fac_acad_desc_hermano , fac_acad.desc_pronto_pago fac_acad_desc_pronto_pago , fac_acad.desc_pago_adelantado fac_acad_desc_pago_adelantado  ";
		if (aTablas.contains("col_beca"))
			sql = sql + ", bec.id bec_id  , bec.nom bec_nom , bec.val bec_val , bec.abrv bec_abrv , bec.id_tdes bec_id_tdes  ";
		if (aTablas.contains("col_motivo_beca"))
			sql = sql + ", mbec.id mbec_id  , mbec.nom mbec_nom , mbec.id_bec mbec_id_bec  ";
	
		sql = sql + " from fac_acapag_beca Academico Pago Beca "; 
		if (aTablas.contains("fac_academico_pago"))
			sql = sql + " left join fac_academico_pago fac_acad on fac_acad.id = Academico Pago Beca.id_fac ";
		if (aTablas.contains("col_beca"))
			sql = sql + " left join col_beca bec on bec.id = Academico Pago Beca.id_bec ";
		if (aTablas.contains("col_motivo_beca"))
			sql = sql + " left join col_motivo_beca mbec on mbec.id = Academico Pago Beca.id_mbec ";
		sql = sql + " where Academico Pago Beca.id= " + id; 
				
		System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<AcapagBeca>() {
		
			@Override
			public AcapagBeca extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					AcapagBeca acapagbeca= rsToEntity(rs,"Academico Pago Beca_");
					if (aTablas.contains("fac_academico_pago")){
						AcademicoPago academicopago = new AcademicoPago();  
							academicopago.setId(rs.getInt("fac_acad_id")) ;  
							academicopago.setId_mat(rs.getInt("fac_acad_id_mat")) ;  
							academicopago.setTip(rs.getString("fac_acad_tip")) ;  
							academicopago.setMens(rs.getInt("fac_acad_mens")) ;  
							academicopago.setMonto(rs.getBigDecimal("fac_acad_monto")) ;  
							academicopago.setCanc(rs.getString("fac_acad_canc")) ;  
							academicopago.setNro_rec(rs.getString("fac_acad_nro_rec")) ;  
							academicopago.setNro_pe(rs.getString("fac_acad_nro_pe")) ;  
							academicopago.setBanco(rs.getString("fac_acad_banco")) ;  
							academicopago.setFec_pago(rs.getDate("fac_acad_fec_pago")) ;  
							academicopago.setDesc_hermano(rs.getBigDecimal("fac_acad_desc_hermano")) ;  
							academicopago.setDesc_pronto_pago(rs.getBigDecimal("fac_acad_desc_pronto_pago")) ;  
							academicopago.setDesc_pago_adelantado(rs.getBigDecimal("fac_acad_desc_pago_adelantado")) ;  
							acapagbeca.setAcademicoPago(academicopago);
					}
					if (aTablas.contains("col_beca")){
						Beca beca = new Beca();  
							beca.setId(rs.getInt("bec_id")) ;  
							beca.setNom(rs.getString("bec_nom")) ;  
							beca.setVal(rs.getString("bec_val")) ;  
							beca.setAbrv(rs.getString("bec_abrv")) ;  
							beca.setId_tdes(rs.getInt("bec_id_tdes")) ;  
							acapagbeca.setBeca(beca);
					}
					if (aTablas.contains("col_motivo_beca")){
						MotivoBeca motivobeca = new MotivoBeca();  
							motivobeca.setId(rs.getInt("mbec_id")) ;  
							motivobeca.setNom(rs.getString("mbec_nom")) ;  
							motivobeca.setId_bec(rs.getInt("mbec_id_bec")) ;  
							acapagbeca.setMotivoBeca(motivobeca);
					}
							return acapagbeca;
				}
				
				return null;
			}
			
		});


	}		
	
	public AcapagBeca getByParams(Param param) {

		String sql = "select * from fac_acapag_beca " + SQLFrmkUtil.getWhere(param);
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<AcapagBeca>() {
			@Override
			public AcapagBeca extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<AcapagBeca> listByParams(Param param, String[] order) {

		String sql = "select * from fac_acapag_beca " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<AcapagBeca>() {

			@Override
			public AcapagBeca mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<AcapagBeca> listFullByParams(AcapagBeca acapagbeca, String[] order) {
	
		return listFullByParams(Param.toParam("Academico Pago Beca",acapagbeca), order);
	
	}	
	
	public List<AcapagBeca> listFullByParams(Param param, String[] order) {

		String sql = "select Academico Pago Beca.id Academico Pago Beca_id, Academico Pago Beca.id_fac Academico Pago Beca_id_fac , Academico Pago Beca.id_bec Academico Pago Beca_id_bec , Academico Pago Beca.id_mbec Academico Pago Beca_id_mbec , Academico Pago Beca.monto_total Academico Pago Beca_monto_total , Academico Pago Beca.monto_afectado Academico Pago Beca_monto_afectado  ,Academico Pago Beca.est Academico Pago Beca_est ";
		sql = sql + ", fac_acad.id fac_acad_id  , fac_acad.id_mat fac_acad_id_mat , fac_acad.tip fac_acad_tip , fac_acad.mens fac_acad_mens , fac_acad.monto fac_acad_monto , fac_acad.canc fac_acad_canc , fac_acad.nro_rec fac_acad_nro_rec , fac_acad.nro_pe fac_acad_nro_pe , fac_acad.banco fac_acad_banco , fac_acad.fec_pago fac_acad_fec_pago , fac_acad.desc_hermano fac_acad_desc_hermano , fac_acad.desc_pronto_pago fac_acad_desc_pronto_pago , fac_acad.desc_pago_adelantado fac_acad_desc_pago_adelantado  ";
		sql = sql + ", bec.id bec_id  , bec.nom bec_nom , bec.val bec_val , bec.abrv bec_abrv , bec.id_tdes bec_id_tdes  ";
		sql = sql + ", mbec.id mbec_id  , mbec.nom mbec_nom , mbec.id_bec mbec_id_bec  ";
		sql = sql + " from fac_acapag_beca Academico Pago Beca";
		sql = sql + " left join fac_academico_pago fac_acad on fac_acad.id = Academico Pago Beca.id_fac ";
		sql = sql + " left join col_beca bec on bec.id = Academico Pago Beca.id_bec ";
		sql = sql + " left join col_motivo_beca mbec on mbec.id = Academico Pago Beca.id_mbec ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<AcapagBeca>() {

			@Override
			public AcapagBeca mapRow(ResultSet rs, int rowNum) throws SQLException {
				AcapagBeca acapagbeca= rsToEntity(rs,"Academico Pago Beca_");
				AcademicoPago academicopago = new AcademicoPago();  
				academicopago.setId(rs.getInt("fac_acad_id")) ;  
				academicopago.setId_mat(rs.getInt("fac_acad_id_mat")) ;  
				academicopago.setTip(rs.getString("fac_acad_tip")) ;  
				academicopago.setMens(rs.getInt("fac_acad_mens")) ;  
				academicopago.setMonto(rs.getBigDecimal("fac_acad_monto")) ;  
				academicopago.setCanc(rs.getString("fac_acad_canc")) ;  
				academicopago.setNro_rec(rs.getString("fac_acad_nro_rec")) ;  
				academicopago.setNro_pe(rs.getString("fac_acad_nro_pe")) ;  
				academicopago.setBanco(rs.getString("fac_acad_banco")) ;  
				academicopago.setFec_pago(rs.getDate("fac_acad_fec_pago")) ;  
				academicopago.setDesc_hermano(rs.getBigDecimal("fac_acad_desc_hermano")) ;  
				academicopago.setDesc_pronto_pago(rs.getBigDecimal("fac_acad_desc_pronto_pago")) ;  
				academicopago.setDesc_pago_adelantado(rs.getBigDecimal("fac_acad_desc_pago_adelantado")) ;  
				acapagbeca.setAcademicoPago(academicopago);
				Beca beca = new Beca();  
				beca.setId(rs.getInt("bec_id")) ;  
				beca.setNom(rs.getString("bec_nom")) ;  
				beca.setVal(rs.getString("bec_val")) ;  
				beca.setAbrv(rs.getString("bec_abrv")) ;  
				beca.setId_tdes(rs.getInt("bec_id_tdes")) ;  
				acapagbeca.setBeca(beca);
				MotivoBeca motivobeca = new MotivoBeca();  
				motivobeca.setId(rs.getInt("mbec_id")) ;  
				motivobeca.setNom(rs.getString("mbec_nom")) ;  
				motivobeca.setId_bec(rs.getInt("mbec_id_bec")) ;  
				acapagbeca.setMotivoBeca(motivobeca);
				return acapagbeca;
			}

		});

	}	




	// funciones privadas utilitarias para AcapagBeca

	private AcapagBeca rsToEntity(ResultSet rs,String alias) throws SQLException {
		AcapagBeca acapag_beca = new AcapagBeca();

		acapag_beca.setId(rs.getInt( alias + "id"));
		acapag_beca.setId_fac(rs.getInt( alias + "id_fac"));
		acapag_beca.setId_bec(rs.getInt( alias + "id_bec"));
		acapag_beca.setId_mbec(rs.getInt( alias + "id_mbec"));
		acapag_beca.setMonto_total(rs.getBigDecimal( alias + "monto_total"));
		acapag_beca.setMonto_afectado(rs.getBigDecimal( alias + "monto_afectado"));
		acapag_beca.setEst(rs.getString( alias + "est"));
								
		return acapag_beca;

	}
	
}
