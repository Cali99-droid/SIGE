package com.sige.mat.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
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
import com.tesla.colegio.model.CalendarioClases;



import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface CalendarioClasesDAO.
 * @author MV
 *
 */
public class CalendarioClasesDAOImpl{
	
	final static Logger logger = Logger.getLogger(CalendarioClasesDAOImpl.class);
	
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(CalendarioClases calendario_clases) {
		if (calendario_clases.getId() != null) {
			// update
			String sql = "UPDATE aca_calendario_clases "
						+ "SET dia=?, "
						+ "motivo=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						calendario_clases.getDia(),
						calendario_clases.getMotivo(),
						calendario_clases.getEst(),
						calendario_clases.getUsr_act(),
						new java.util.Date(),
						calendario_clases.getId()); 
			return calendario_clases.getId();

		} else {
			// insert
			String sql = "insert into aca_calendario_clases ("
						+ "dia, "
						+ "motivo, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				calendario_clases.getDia(),
				calendario_clases.getMotivo(),
				calendario_clases.getEst(),
				calendario_clases.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from aca_calendario_clases where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<CalendarioClases> list() {
		String sql = "select * from aca_calendario_clases";
		
		//logger.info(sql);
		
		List<CalendarioClases> listCalendarioClases = jdbcTemplate.query(sql, new RowMapper<CalendarioClases>() {

			@Override
			public CalendarioClases mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listCalendarioClases;
	}

	public CalendarioClases get(int id) {
		String sql = "select * from aca_calendario_clases WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<CalendarioClases>() {

			@Override
			public CalendarioClases extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public CalendarioClases getFull(int id, String tablas[]) {
		String sql = "select ccl.id ccl_id, ccl.dia ccl_dia , ccl.motivo ccl_motivo  ,ccl.est ccl_est ";
	
		sql = sql + " from aca_calendario_clases ccl "; 
		sql = sql + " where ccl.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<CalendarioClases>() {
		
			@Override
			public CalendarioClases extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					CalendarioClases calendarioclases= rsToEntity(rs,"ccl_");
							return calendarioclases;
				}
				
				return null;
			}
			
		});


	}		
	
	public CalendarioClases getByParams(Param param) {

		String sql = "select * from aca_calendario_clases " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<CalendarioClases>() {
			@Override
			public CalendarioClases extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<CalendarioClases> listByParams(Param param, String[] order) {

		String sql = "select * from aca_calendario_clases " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<CalendarioClases>() {

			@Override
			public CalendarioClases mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<CalendarioClases> listFullByParams(CalendarioClases calendarioclases, String[] order) {
	
		return listFullByParams(Param.toParam("ccl",calendarioclases), order);
	
	}	
	
	public List<CalendarioClases> listFullByParams(Param param, String[] order) {

		String sql = "select ccl.id ccl_id, ccl.dia ccl_dia , ccl.motivo ccl_motivo  ,ccl.est ccl_est ";
		sql = sql + " from aca_calendario_clases ccl";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<CalendarioClases>() {

			@Override
			public CalendarioClases mapRow(ResultSet rs, int rowNum) throws SQLException {
				CalendarioClases calendarioclases= rsToEntity(rs,"ccl_");
				return calendarioclases;
			}

		});

	}	




	// funciones privadas utilitarias para CalendarioClases

	private CalendarioClases rsToEntity(ResultSet rs,String alias) throws SQLException {
		CalendarioClases calendario_clases = new CalendarioClases();

		calendario_clases.setId(rs.getInt( alias + "id"));
		calendario_clases.setDia(rs.getDate( alias + "dia"));
		calendario_clases.setMotivo(rs.getString( alias + "motivo"));
		calendario_clases.setEst(rs.getString( alias + "est"));
								
		return calendario_clases;

	}
	
}
