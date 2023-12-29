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
import com.tesla.colegio.model.DescPagoAdelantado;

import com.tesla.colegio.model.Periodo;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface DescPagoAdelantadoDAO.
 * @author MV
 *
 */
public class DescPagoAdelantadoDAOImpl{
	final static Logger logger = Logger.getLogger(DescPagoAdelantadoDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(DescPagoAdelantado desc_pago_adelantado) {
		if (desc_pago_adelantado.getId() != null) {
			// update
			String sql = "UPDATE mat_desc_pago_adelantado "
						+ "SET id_per=?, "
						+ "desc_dic=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						desc_pago_adelantado.getId_per(),
						desc_pago_adelantado.getDesc_dic(),
						desc_pago_adelantado.getEst(),
						desc_pago_adelantado.getUsr_act(),
						new java.util.Date(),
						desc_pago_adelantado.getId()); 
			return desc_pago_adelantado.getId();

		} else {
			// insert
			String sql = "insert into mat_desc_pago_adelantado ("
						+ "id_per, "
						+ "desc_dic, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				desc_pago_adelantado.getId_per(),
				desc_pago_adelantado.getDesc_dic(),
				desc_pago_adelantado.getEst(),
				desc_pago_adelantado.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from mat_desc_pago_adelantado where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<DescPagoAdelantado> list() {
		String sql = "select * from mat_desc_pago_adelantado";
		
		//logger.info(sql);
		
		List<DescPagoAdelantado> listDescPagoAdelantado = jdbcTemplate.query(sql, new RowMapper<DescPagoAdelantado>() {

			@Override
			public DescPagoAdelantado mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listDescPagoAdelantado;
	}

	public DescPagoAdelantado get(int id) {
		String sql = "select * from mat_desc_pago_adelantado WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<DescPagoAdelantado>() {

			@Override
			public DescPagoAdelantado extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public DescPagoAdelantado getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select dxh.id dxh_id, dxh.id_per dxh_id_per , dxh.desc_dic dxh_desc_dic  ,dxh.est dxh_est ";
		if (aTablas.contains("per_periodo"))
			sql = sql + ", pee.id pee_id  , pee.id_anio pee_id_anio , pee.id_srv pee_id_srv , pee.id_tpe pee_id_tpe , pee.fec_ini pee_fec_ini , pee.fec_fin pee_fec_fin , pee.fec_cie_mat pee_fec_cie_mat  ";
	
		sql = sql + " from mat_desc_pago_adelantado dxh "; 
		if (aTablas.contains("per_periodo"))
			sql = sql + " left join per_periodo pee on pee.id = dxh.id_per ";
		sql = sql + " where dxh.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<DescPagoAdelantado>() {
		
			@Override
			public DescPagoAdelantado extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					DescPagoAdelantado descpagoadelantado= rsToEntity(rs,"dxh_");
					if (aTablas.contains("per_periodo")){
						Periodo periodo = new Periodo();  
							periodo.setId(rs.getInt("pee_id")) ;  
							periodo.setId_anio(rs.getInt("pee_id_anio")) ;  
							periodo.setId_srv(rs.getInt("pee_id_srv")) ;  
							periodo.setId_tpe(rs.getInt("pee_id_tpe")) ;  
							periodo.setFec_ini(rs.getDate("pee_fec_ini")) ;  
							periodo.setFec_fin(rs.getDate("pee_fec_fin")) ;  
							periodo.setFec_cie_mat(rs.getDate("pee_fec_cie_mat")) ;  
							descpagoadelantado.setPeriodo(periodo);
					}
							return descpagoadelantado;
				}
				
				return null;
			}
			
		});


	}		
	
	public DescPagoAdelantado getByParams(Param param) {

		String sql = "select * from mat_desc_pago_adelantado " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<DescPagoAdelantado>() {
			@Override
			public DescPagoAdelantado extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<DescPagoAdelantado> listByParams(Param param, String[] order) {

		String sql = "select * from mat_desc_pago_adelantado " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<DescPagoAdelantado>() {

			@Override
			public DescPagoAdelantado mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<DescPagoAdelantado> listFullByParams(DescPagoAdelantado descpagoadelantado, String[] order) {
	
		return listFullByParams(Param.toParam("dxh",descpagoadelantado), order);
	
	}	
	
	public List<DescPagoAdelantado> listFullByParams(Param param, String[] order) {

		String sql = "select dxh.id dxh_id, dxh.id_per dxh_id_per , dxh.desc_dic dxh_desc_dic  ,dxh.est dxh_est ";
		sql = sql + ", pee.id pee_id  , pee.id_anio pee_id_anio , pee.id_srv pee_id_srv , pee.id_tpe pee_id_tpe , pee.fec_ini pee_fec_ini , pee.fec_fin pee_fec_fin , pee.fec_cie_mat pee_fec_cie_mat  ";
		sql = sql + " from mat_desc_pago_adelantado dxh";
		sql = sql + " left join per_periodo pee on pee.id = dxh.id_per ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<DescPagoAdelantado>() {

			@Override
			public DescPagoAdelantado mapRow(ResultSet rs, int rowNum) throws SQLException {
				DescPagoAdelantado descpagoadelantado= rsToEntity(rs,"dxh_");
				Periodo periodo = new Periodo();  
				periodo.setId(rs.getInt("pee_id")) ;  
				periodo.setId_anio(rs.getInt("pee_id_anio")) ;  
				periodo.setId_srv(rs.getInt("pee_id_srv")) ;  
				periodo.setId_tpe(rs.getInt("pee_id_tpe")) ;  
				periodo.setFec_ini(rs.getDate("pee_fec_ini")) ;  
				periodo.setFec_fin(rs.getDate("pee_fec_fin")) ;  
				periodo.setFec_cie_mat(rs.getDate("pee_fec_cie_mat")) ;  
				descpagoadelantado.setPeriodo(periodo);
				return descpagoadelantado;
			}

		});

	}	




	// funciones privadas utilitarias para DescPagoAdelantado

	private DescPagoAdelantado rsToEntity(ResultSet rs,String alias) throws SQLException {
		DescPagoAdelantado desc_pago_adelantado = new DescPagoAdelantado();

		desc_pago_adelantado.setId(rs.getInt( alias + "id"));
		desc_pago_adelantado.setId_per(rs.getInt( alias + "id_per"));
		desc_pago_adelantado.setDesc_dic(rs.getBigDecimal( alias + "desc_dic"));
		desc_pago_adelantado.setEst(rs.getString( alias + "est"));
								
		return desc_pago_adelantado;

	}
	
}
