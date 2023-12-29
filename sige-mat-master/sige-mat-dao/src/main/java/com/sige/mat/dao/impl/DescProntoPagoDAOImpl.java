package com.sige.mat.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Arrays;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import com.tesla.colegio.model.DescProntoPago;

import com.tesla.colegio.model.Periodo;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface DescProntoPagoDAO.
 * @author MV
 *
 */
public class DescProntoPagoDAOImpl{
	final static Logger logger = Logger.getLogger(DescProntoPagoDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(DescProntoPago desc_pronto_pago) {
		if (desc_pronto_pago.getId() != null) {
			// update
			String sql = "UPDATE mat_desc_pronto_pago "
						+ "SET id_per=?, "
						+ "banco=?, "
						+ "secretaria=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						desc_pronto_pago.getId_per(),
						desc_pronto_pago.getBanco(),
						desc_pronto_pago.getSecretaria(),
						desc_pronto_pago.getEst(),
						desc_pronto_pago.getUsr_act(),
						new java.util.Date(),
						desc_pronto_pago.getId()); 
			return desc_pronto_pago.getId();

		} else {
			// insert
			String sql = "insert into mat_desc_pronto_pago ("
						+ "id_per, "
						+ "banco, "
						+ "secretaria, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				desc_pronto_pago.getId_per(),
				desc_pronto_pago.getBanco(),
				desc_pronto_pago.getSecretaria(),
				desc_pronto_pago.getEst(),
				desc_pronto_pago.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from mat_desc_pronto_pago where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<DescProntoPago> list() {
		String sql = "select * from mat_desc_pronto_pago";
		
		//logger.info(sql);
		
		List<DescProntoPago> listDescProntoPago = jdbcTemplate.query(sql, new RowMapper<DescProntoPago>() {

			@Override
			public DescProntoPago mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listDescProntoPago;
	}

	public DescProntoPago get(int id) {
		String sql = "select * from mat_desc_pronto_pago WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<DescProntoPago>() {

			@Override
			public DescProntoPago extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public DescProntoPago getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select dxh.id dxh_id, dxh.id_per dxh_id_per , dxh.banco dxh_banco , dxh.secretaria dxh_secretaria  ,dxh.est dxh_est ";
		if (aTablas.contains("per_periodo"))
			sql = sql + ", pee.id pee_id  , pee.id_anio pee_id_anio , pee.id_srv pee_id_srv , pee.id_tpe pee_id_tpe , pee.fec_ini pee_fec_ini , pee.fec_fin pee_fec_fin , pee.fec_cie_mat pee_fec_cie_mat  ";
	
		sql = sql + " from mat_desc_pronto_pago dxh "; 
		if (aTablas.contains("per_periodo"))
			sql = sql + " left join per_periodo pee on pee.id = dxh.id_per ";
		sql = sql + " where dxh.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<DescProntoPago>() {
		
			@Override
			public DescProntoPago extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					DescProntoPago descprontopago= rsToEntity(rs,"dxh_");
					if (aTablas.contains("per_periodo")){
						Periodo periodo = new Periodo();  
							periodo.setId(rs.getInt("pee_id")) ;  
							periodo.setId_anio(rs.getInt("pee_id_anio")) ;  
							periodo.setId_srv(rs.getInt("pee_id_srv")) ;  
							periodo.setId_tpe(rs.getInt("pee_id_tpe")) ;  
							periodo.setFec_ini(rs.getDate("pee_fec_ini")) ;  
							periodo.setFec_fin(rs.getDate("pee_fec_fin")) ;  
							periodo.setFec_cie_mat(rs.getDate("pee_fec_cie_mat")) ;  
							descprontopago.setPeriodo(periodo);
					}
							return descprontopago;
				}
				
				return null;
			}
			
		});


	}		
	
	public DescProntoPago getByParams(Param param) {

		String sql = "select * from mat_desc_pronto_pago " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<DescProntoPago>() {
			@Override
			public DescProntoPago extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<DescProntoPago> listByParams(Param param, String[] order) {

		String sql = "select * from mat_desc_pronto_pago " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<DescProntoPago>() {

			@Override
			public DescProntoPago mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<DescProntoPago> listFullByParams(DescProntoPago descprontopago, String[] order) {
	
		return listFullByParams(Param.toParam("dxh",descprontopago), order);
	
	}	
	
	public List<DescProntoPago> listFullByParams(Param param, String[] order) {

		String sql = "select dxh.id dxh_id, dxh.id_per dxh_id_per , dxh.banco dxh_banco , dxh.secretaria dxh_secretaria  ,dxh.est dxh_est ";
		sql = sql + ", pee.id pee_id  , pee.id_anio pee_id_anio , pee.id_srv pee_id_srv , pee.id_tpe pee_id_tpe , pee.fec_ini pee_fec_ini , pee.fec_fin pee_fec_fin , pee.fec_cie_mat pee_fec_cie_mat  ";
		sql = sql + " from mat_desc_pronto_pago dxh";
		sql = sql + " left join per_periodo pee on pee.id = dxh.id_per ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<DescProntoPago>() {

			@Override
			public DescProntoPago mapRow(ResultSet rs, int rowNum) throws SQLException {
				DescProntoPago descprontopago= rsToEntity(rs,"dxh_");
				Periodo periodo = new Periodo();  
				periodo.setId(rs.getInt("pee_id")) ;  
				periodo.setId_anio(rs.getInt("pee_id_anio")) ;  
				periodo.setId_srv(rs.getInt("pee_id_srv")) ;  
				periodo.setId_tpe(rs.getInt("pee_id_tpe")) ;  
				periodo.setFec_ini(rs.getDate("pee_fec_ini")) ;  
				periodo.setFec_fin(rs.getDate("pee_fec_fin")) ;  
				periodo.setFec_cie_mat(rs.getDate("pee_fec_cie_mat")) ;  
				descprontopago.setPeriodo(periodo);
				return descprontopago;
			}

		});

	}	




	// funciones privadas utilitarias para DescProntoPago

	private DescProntoPago rsToEntity(ResultSet rs,String alias) throws SQLException {
		DescProntoPago desc_pronto_pago = new DescProntoPago();

		desc_pronto_pago.setId(rs.getInt( alias + "id"));
		desc_pronto_pago.setId_per(rs.getInt( alias + "id_per"));
		desc_pronto_pago.setBanco(rs.getBigDecimal( alias + "banco"));
		desc_pronto_pago.setSecretaria(rs.getBigDecimal( alias + "secretaria"));
		desc_pronto_pago.setEst(rs.getString( alias + "est"));
								
		return desc_pronto_pago;

	}
	
}
