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
import com.tesla.colegio.model.ColSituacion;



import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface ColSituacionDAO.
 * @author MV
 *
 */
public class ColSituacionDAOImpl{
	final static Logger logger = Logger.getLogger(ColSituacionDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(ColSituacion col_situacion) {
		if (col_situacion.getId() != null) {
			// update
			String sql = "UPDATE cat_col_situacion "
						+ "SET cod=?, "
						+ "nom=?, "
						+ "des=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						col_situacion.getCod(),
						col_situacion.getNom(),
						col_situacion.getDes(),
						col_situacion.getEst(),
						col_situacion.getUsr_act(),
						new java.util.Date(),
						col_situacion.getId()); 
			return col_situacion.getId();

		} else {
			// insert
			String sql = "insert into cat_col_situacion ("
						+ "cod, "
						+ "nom, "
						+ "des, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				col_situacion.getCod(),
				col_situacion.getNom(),
				col_situacion.getDes(),
				col_situacion.getEst(),
				col_situacion.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from cat_col_situacion where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<ColSituacion> list() {
		String sql = "select * from cat_col_situacion";
		
		//logger.info(sql);
		
		List<ColSituacion> listColSituacion = jdbcTemplate.query(sql, new RowMapper<ColSituacion>() {

			@Override
			public ColSituacion mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listColSituacion;
	}

	public ColSituacion get(int id) {
		String sql = "select * from cat_col_situacion WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<ColSituacion>() {

			@Override
			public ColSituacion extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public ColSituacion getFull(int id, String tablas[]) {
		String sql = "select cma.id cma_id, cma.cod cma_cod , cma.nom cma_nom , cma.des cma_des  ,cma.est cma_est ";
	
		sql = sql + " from cat_col_situacion cma "; 
		sql = sql + " where cma.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<ColSituacion>() {
		
			@Override
			public ColSituacion extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					ColSituacion colsituacion= rsToEntity(rs,"cma_");
							return colsituacion;
				}
				
				return null;
			}
			
		});


	}		
	
	public ColSituacion getByParams(Param param) {

		String sql = "select * from cat_col_situacion " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<ColSituacion>() {
			@Override
			public ColSituacion extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<ColSituacion> listByParams(Param param, String[] order) {

		String sql = "select * from cat_col_situacion " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<ColSituacion>() {

			@Override
			public ColSituacion mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<ColSituacion> listFullByParams(ColSituacion colsituacion, String[] order) {
	
		return listFullByParams(Param.toParam("cma",colsituacion), order);
	
	}	
	
	public List<ColSituacion> listFullByParams(Param param, String[] order) {

		String sql = "select cma.id cma_id, cma.cod cma_cod , cma.nom cma_nom , cma.des cma_des  ,cma.est cma_est ";
		sql = sql + " from cat_col_situacion cma";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<ColSituacion>() {

			@Override
			public ColSituacion mapRow(ResultSet rs, int rowNum) throws SQLException {
				ColSituacion colsituacion= rsToEntity(rs,"cma_");
				return colsituacion;
			}

		});

	}	




	// funciones privadas utilitarias para ColSituacion

	private ColSituacion rsToEntity(ResultSet rs,String alias) throws SQLException {
		ColSituacion col_situacion = new ColSituacion();

		col_situacion.setId(rs.getInt( alias + "id"));
		col_situacion.setCod(rs.getString( alias + "cod"));
		col_situacion.setNom(rs.getString( alias + "nom"));
		col_situacion.setDes(rs.getString( alias + "des"));
		col_situacion.setEst(rs.getString( alias + "est"));
								
		return col_situacion;

	}
	
}
