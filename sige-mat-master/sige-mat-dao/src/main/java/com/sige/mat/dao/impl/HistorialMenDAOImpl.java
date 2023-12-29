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
import com.tesla.colegio.model.HistorialMen;

import com.tesla.colegio.model.AcademicoPago;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface HistorialMenDAO.
 * @author MV
 *
 */
public class HistorialMenDAOImpl{
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(HistorialMen historial_men) {
		if (historial_men.getId() != null) {
			// update
			String sql = "UPDATE fac_historial_men "
						+ "SET id_fac=?, "
						+ "monto_anterior=?, "
						+ "monto_actual=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//System.out.println(sql);

			jdbcTemplate.update(sql, 
						historial_men.getId_fac(),
						historial_men.getMonto_anterior(),
						historial_men.getMonto_actual(),
						historial_men.getEst(),
						historial_men.getUsr_act(),
						new java.util.Date(),
						historial_men.getId()); 
			return historial_men.getId();

		} else {
			// insert
			String sql = "insert into fac_historial_men ("
						+ "id_fac, "
						+ "monto_anterior, "
						+ "monto_actual, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?)";

				//System.out.println(sql);

				jdbcTemplate.update(sql, 
				historial_men.getId_fac(),
				historial_men.getMonto_anterior(),
				historial_men.getMonto_actual(),
				historial_men.getEst(),
				historial_men.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from fac_historial_men where id=?";
		
		//System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<HistorialMen> list() {
		String sql = "select * from fac_historial_men";
		
		//System.out.println(sql);
		
		List<HistorialMen> listHistorialMen = jdbcTemplate.query(sql, new RowMapper<HistorialMen>() {

			@Override
			public HistorialMen mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listHistorialMen;
	}

	public HistorialMen get(int id) {
		String sql = "select * from fac_historial_men WHERE id=" + id;
		
		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<HistorialMen>() {

			@Override
			public HistorialMen extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public HistorialMen getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select Historial Mensualidades.id Historial Mensualidades_id, Historial Mensualidades.id_fac Historial Mensualidades_id_fac , Historial Mensualidades.monto_anterior Historial Mensualidades_monto_anterior , Historial Mensualidades.monto_actual Historial Mensualidades_monto_actual  ,Historial Mensualidades.est Historial Mensualidades_est ";
		if (aTablas.contains("fac_academico_pago"))
			sql = sql + ", fac_acad.id fac_acad_id  , fac_acad.id_mat fac_acad_id_mat , fac_acad.tip fac_acad_tip , fac_acad.mens fac_acad_mens , fac_acad.monto fac_acad_monto , fac_acad.canc fac_acad_canc , fac_acad.nro_rec fac_acad_nro_rec , fac_acad.nro_pe fac_acad_nro_pe , fac_acad.banco fac_acad_banco , fac_acad.fec_pago fac_acad_fec_pago , fac_acad.desc_hermano fac_acad_desc_hermano , fac_acad.desc_pronto_pago fac_acad_desc_pronto_pago , fac_acad.desc_pago_adelantado fac_acad_desc_pago_adelantado  ";
	
		sql = sql + " from fac_historial_men Historial Mensualidades "; 
		if (aTablas.contains("fac_academico_pago"))
			sql = sql + " left join fac_academico_pago fac_acad on fac_acad.id = Historial Mensualidades.id_fac ";
		sql = sql + " where Historial Mensualidades.id= " + id; 
				
		//System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<HistorialMen>() {
		
			@Override
			public HistorialMen extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					HistorialMen historialmen= rsToEntity(rs,"Historial Mensualidades_");
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
							historialmen.setAcademicoPago(academicopago);
					}
							return historialmen;
				}
				
				return null;
			}
			
		});


	}		
	
	public HistorialMen getByParams(Param param) {

		String sql = "select * from fac_historial_men " + SQLFrmkUtil.getWhere(param);
		
		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<HistorialMen>() {
			@Override
			public HistorialMen extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<HistorialMen> listByParams(Param param, String[] order) {

		String sql = "select * from fac_historial_men " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<HistorialMen>() {

			@Override
			public HistorialMen mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<HistorialMen> listFullByParams(HistorialMen historialmen, String[] order) {
	
		return listFullByParams(Param.toParam("Historial Mensualidades",historialmen), order);
	
	}	
	
	public List<HistorialMen> listFullByParams(Param param, String[] order) {

		String sql = "select Historial Mensualidades.id Historial Mensualidades_id, Historial Mensualidades.id_fac Historial Mensualidades_id_fac , Historial Mensualidades.monto_anterior Historial Mensualidades_monto_anterior , Historial Mensualidades.monto_actual Historial Mensualidades_monto_actual  ,Historial Mensualidades.est Historial Mensualidades_est ";
		sql = sql + ", fac_acad.id fac_acad_id  , fac_acad.id_mat fac_acad_id_mat , fac_acad.tip fac_acad_tip , fac_acad.mens fac_acad_mens , fac_acad.monto fac_acad_monto , fac_acad.canc fac_acad_canc , fac_acad.nro_rec fac_acad_nro_rec , fac_acad.nro_pe fac_acad_nro_pe , fac_acad.banco fac_acad_banco , fac_acad.fec_pago fac_acad_fec_pago , fac_acad.desc_hermano fac_acad_desc_hermano , fac_acad.desc_pronto_pago fac_acad_desc_pronto_pago , fac_acad.desc_pago_adelantado fac_acad_desc_pago_adelantado  ";
		sql = sql + " from fac_historial_men Historial Mensualidades";
		sql = sql + " left join fac_academico_pago fac_acad on fac_acad.id = Historial Mensualidades.id_fac ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<HistorialMen>() {

			@Override
			public HistorialMen mapRow(ResultSet rs, int rowNum) throws SQLException {
				HistorialMen historialmen= rsToEntity(rs,"Historial Mensualidades_");
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
				historialmen.setAcademicoPago(academicopago);
				return historialmen;
			}

		});

	}	




	// funciones privadas utilitarias para HistorialMen

	private HistorialMen rsToEntity(ResultSet rs,String alias) throws SQLException {
		HistorialMen historial_men = new HistorialMen();

		historial_men.setId(rs.getInt( alias + "id"));
		historial_men.setId_fac(rs.getInt( alias + "id_fac"));
		historial_men.setMonto_anterior(rs.getBigDecimal( alias + "monto_anterior"));
		historial_men.setMonto_actual(rs.getBigDecimal( alias + "monto_actual"));
		historial_men.setEst(rs.getString( alias + "est"));
								
		return historial_men;

	}
	
}
