package com.sige.mat.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import com.tesla.colegio.model.Error;



import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface ErrorDAO.
 * @author MV
 *
 */
public class ErrorDAOImpl{
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(Error error) {
		if (error.getId() != null) {
			// update
			String sql = "UPDATE cvi_error "
						+ "SET sql_code=?, "
						+ "sqlerrm=?, "
						+ "error=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//System.out.println(sql);

			jdbcTemplate.update(sql, 
						error.getSql_code(),
						error.getSqlerrm(),
						error.getError(),
						error.getEst(),
						error.getUsr_act(),
						new java.util.Date(),
						error.getId()); 
			return error.getId();

		} else {
			// insert
			String sql = "insert into cvi_error ("
						+ "sql_code, "
						+ "sqlerrm, "
						+ "error, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?)";

				//System.out.println(sql);

				jdbcTemplate.update(sql, 
				error.getSql_code(),
				error.getSqlerrm(),
				error.getError(),
				error.getEst(),
				error.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from cvi_error where id=?";
		
		//System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<Error> list() {
		String sql = "select * from cvi_error";
		
		//System.out.println(sql);
		
		List<Error> listError = jdbcTemplate.query(sql, new RowMapper<Error>() {

			@Override
			public Error mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listError;
	}

	public Error get(int id) {
		String sql = "select * from cvi_error WHERE id=" + id;
		
		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Error>() {

			@Override
			public Error extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public Error getFull(int id, String tablas[]) {
		String sql = "select cvie.id cvie_id, cvie.sql_code cvie_sql_code , cvie.sqlerrm cvie_sqlerrm , cvie.error cvie_error  ,cvie.est cvie_est ";
	
		sql = sql + " from cvi_error cvie "; 
		sql = sql + " where cvie.id= " + id; 
				
		//System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<Error>() {
		
			@Override
			public Error extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Error error= rsToEntity(rs,"cvie_");
							return error;
				}
				
				return null;
			}
			
		});


	}		
	
	public Error getByParams(Param param) {

		String sql = "select * from cvi_error " + SQLFrmkUtil.getWhere(param);
		
		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Error>() {
			@Override
			public Error extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<Error> listByParams(Param param, String[] order) {

		String sql = "select * from cvi_error " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<Error>() {

			@Override
			public Error mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<Error> listFullByParams(Error error, String[] order) {
	
		return listFullByParams(Param.toParam("cvie",error), order);
	
	}	
	
	public List<Error> listFullByParams(Param param, String[] order) {

		String sql = "select cvie.id cvie_id, cvie.sql_code cvie_sql_code , cvie.sqlerrm cvie_sqlerrm , cvie.error cvie_error  ,cvie.est cvie_est ";
		sql = sql + " from cvi_error cvie";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<Error>() {

			@Override
			public Error mapRow(ResultSet rs, int rowNum) throws SQLException {
				Error error= rsToEntity(rs,"cvie_");
				return error;
			}

		});

	}	




	// funciones privadas utilitarias para Error

	private Error rsToEntity(ResultSet rs,String alias) throws SQLException {
		Error error = new Error();

		error.setId(rs.getInt( alias + "id"));
		error.setSql_code(rs.getString( alias + "sql_code"));
		error.setSqlerrm(rs.getString( alias + "sqlerrm"));
		error.setError(rs.getString( alias + "error"));
		error.setEst(rs.getString( alias + "est"));
								
		return error;

	}
	
}
