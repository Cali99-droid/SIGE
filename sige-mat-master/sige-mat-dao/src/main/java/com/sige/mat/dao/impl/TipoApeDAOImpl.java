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

import com.tesla.colegio.model.TipoApe;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface TipoApeDAO.
 * @author MV
 *
 */
public class TipoApeDAOImpl{
	final static Logger logger = Logger.getLogger(TipoApeDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public int saveOrUpdate(TipoApe tipo_ape) {
		if (tipo_ape.getId() > 0) {
			// update
			String sql = "UPDATE cat_tipo_ape "
						+ "SET nom=?, "
						+ "est=?,usr_act=?,fec_act=? "
					+ "WHERE id=?";

			return jdbcTemplate.update(sql, 
						tipo_ape.getNom(),
						tipo_ape.getEst(),
						tipo_ape.getUsr_act(),
						new java.util.Date(),
						tipo_ape.getId()); 

		} else {
			// insert
			String sql = "insert into cat_tipo_ape ("
						+ "nom, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?)";

			return jdbcTemplate.update(sql, 
				tipo_ape.getNom(),
				tipo_ape.getEst(),
				tipo_ape.getUsr_ins(),
				new java.util.Date());
		}
		
	}

	
	public void delete(int id) {
		String sql = "delete from cat_tipo_ape where id=?";
		jdbcTemplate.update(sql, id);
	}

	
	public List<TipoApe> list() {
		String sql = "select * from cat_tipo_ape";
		List<TipoApe> listTipoApe = jdbcTemplate.query(sql, new RowMapper<TipoApe>() {

			
			public TipoApe mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs);
			}
			
		});
		
		return listTipoApe;
	}

	
	public TipoApe get(int id) {
		String sql = "select * from cat_tipo_ape WHERE id=" + id;
		return jdbcTemplate.query(sql, new ResultSetExtractor<TipoApe>() {

			
			public TipoApe extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs);
				}
				
				return null;
			}
			
		});
	}

	
	public TipoApe getByParams(Param param) {

		String sql = "select * from cat_tipo_ape " + SQLFrmkUtil.getWhere(param);

		return jdbcTemplate.query(sql, new ResultSetExtractor<TipoApe>() {
			
			public TipoApe extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs);
				return null;
			}

		});
	}

	
	public List<TipoApe> listByParams(Param param, String[] order) {

		String sql = "select * from cat_tipo_ape " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		return jdbcTemplate.query(sql, new RowMapper<TipoApe>() {

			
			public TipoApe mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs);
			}

		});

	}	

	// funciones privadas utilitarias para TipoApe

	private TipoApe rsToEntity(ResultSet rs) throws SQLException {
		TipoApe tipo_ape = new TipoApe();

		tipo_ape.setId(rs.getInt("id"));
		tipo_ape.setNom(rs.getString("nom"));
		tipo_ape.setEst(rs.getString("est"));
								
		return tipo_ape;

	}
	
}
