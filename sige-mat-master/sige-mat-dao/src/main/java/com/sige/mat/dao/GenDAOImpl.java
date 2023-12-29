package com.sige.mat.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import com.tesla.colegio.model.Gen;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface GenDAO.
 * @author MV
 *
 */
public class GenDAOImpl implements GenDAO{
	final static Logger logger = Logger.getLogger(GenDAOImpl.class);

	protected JdbcTemplate jdbcTemplate;
	
	public GenDAOImpl(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public int saveOrUpdate(Gen gen) {
		if (gen.getId() > 0) {
			// update
			String sql = "UPDATE cat_gen "
						+ "SET nom=?, "
						+ "est=?,usr_act=?,fec_act=? "
					+ "WHERE id=?";

			return jdbcTemplate.update(sql, 
						gen.getNom(),
						gen.getEst(),
						gen.getUsr_act(),
						new java.util.Date(),
						gen.getId()); 

		} else {
			// insert
			String sql = "insert into cat_gen ("
						+ "nom, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?)";

			return jdbcTemplate.update(sql, 
				gen.getNom(),
				gen.getEst(),
				gen.getUsr_ins(),
				new java.util.Date());
		}
		
	}

	@Override
	public void delete(int id) {
		String sql = "delete from cat_gen where id=?";
		jdbcTemplate.update(sql, id);
	}

	@Override
	public List<Gen> list() {
		String sql = "select * from cat_gen";
		List<Gen> listGen = jdbcTemplate.query(sql, new RowMapper<Gen>() {

			@Override
			public Gen mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs);
			}
			
		});
		
		return listGen;
	}

	@Override
	public Gen get(int id) {
		String sql = "select * from cat_gen WHERE id=" + id;
		return jdbcTemplate.query(sql, new ResultSetExtractor<Gen>() {

			@Override
			public Gen extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs);
				}
				
				return null;
			}
			
		});
	}

	@Override
	public Gen getByParams(Param param) {

		String sql = "select * from cat_gen " + SQLFrmkUtil.getWhere(param);

		return jdbcTemplate.query(sql, new ResultSetExtractor<Gen>() {
			@Override
			public Gen extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs);
				return null;
			}

		});
	}

	@Override
	public List<Gen> listByParams(Param param, String[] order) {

		String sql = "select * from cat_gen " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		return jdbcTemplate.query(sql, new RowMapper<Gen>() {

			@Override
			public Gen mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs);
			}

		});

	}	

	// funciones privadas utilitarias para Gen

	private Gen rsToEntity(ResultSet rs) throws SQLException {
		Gen gen = new Gen();

		gen.setId(rs.getInt("id"));
		gen.setNom(rs.getString("nom"));
		gen.setEst(rs.getString("est"));
								
		return gen;

	}
	
}
