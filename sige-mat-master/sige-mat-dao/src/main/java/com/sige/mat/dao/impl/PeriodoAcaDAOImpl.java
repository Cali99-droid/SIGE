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
import com.tesla.colegio.model.PeriodoAca;

import com.tesla.colegio.model.PerAcaNivel;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface PeriodoAcaDAO.
 * @author MV
 *
 */
public class PeriodoAcaDAOImpl{
	final static Logger logger = Logger.getLogger(PeriodoAcaDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(PeriodoAca periodo_aca) {
		if (periodo_aca.getId() != null) {
			// update
			String sql = "UPDATE cat_periodo_aca "
						+ "SET nom=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						periodo_aca.getNom(),
						periodo_aca.getEst(),
						periodo_aca.getUsr_act(),
						new java.util.Date(),
						periodo_aca.getId()); 
			return periodo_aca.getId();

		} else {
			// insert
			String sql = "insert into cat_periodo_aca ("
						+ "nom, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				periodo_aca.getNom(),
				periodo_aca.getEst(),
				periodo_aca.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from cat_periodo_aca where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<PeriodoAca> list() {
		String sql = "select * from cat_periodo_aca";
		
		//logger.info(sql);
		
		List<PeriodoAca> listPeriodoAca = jdbcTemplate.query(sql, new RowMapper<PeriodoAca>() {

			@Override
			public PeriodoAca mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listPeriodoAca;
	}

	public PeriodoAca get(int id) {
		String sql = "select * from cat_periodo_aca WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<PeriodoAca>() {

			@Override
			public PeriodoAca extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public PeriodoAca getFull(int id, String tablas[]) {
		String sql = "select cpa.id cpa_id, cpa.nom cpa_nom  ,cpa.est cpa_est ";
	
		sql = sql + " from cat_periodo_aca cpa "; 
		sql = sql + " where cpa.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<PeriodoAca>() {
		
			@Override
			public PeriodoAca extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					PeriodoAca periodoaca= rsToEntity(rs,"cpa_");
							return periodoaca;
				}
				
				return null;
			}
			
		});


	}		
	
	public PeriodoAca getByParams(Param param) {

		String sql = "select * from cat_periodo_aca " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<PeriodoAca>() {
			@Override
			public PeriodoAca extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<PeriodoAca> listByParams(Param param, String[] order) {

		String sql = "select * from cat_periodo_aca " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<PeriodoAca>() {

			@Override
			public PeriodoAca mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<PeriodoAca> listFullByParams(PeriodoAca periodoaca, String[] order) {
	
		return listFullByParams(Param.toParam("cpa",periodoaca), order);
	
	}	
	
	public List<PeriodoAca> listFullByParams(Param param, String[] order) {

		String sql = "select cpa.id cpa_id, cpa.nom cpa_nom  ,cpa.est cpa_est ";
		sql = sql + " from cat_periodo_aca cpa";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<PeriodoAca>() {

			@Override
			public PeriodoAca mapRow(ResultSet rs, int rowNum) throws SQLException {
				PeriodoAca periodoaca= rsToEntity(rs,"cpa_");
				return periodoaca;
			}

		});

	}	


	public List<PerAcaNivel> getListPerAcaNivel(Param param, String[] order) {
		String sql = "select * from cat_per_aca_nivel " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<PerAcaNivel>() {

			@Override
			public PerAcaNivel mapRow(ResultSet rs, int rowNum) throws SQLException {
				PerAcaNivel per_aca_nivel = new PerAcaNivel();

				per_aca_nivel.setId(rs.getInt("id"));
				per_aca_nivel.setId_niv(rs.getInt("id_niv"));
				per_aca_nivel.setId_cpa(rs.getInt("id_cpa"));
				per_aca_nivel.setEst(rs.getString("est"));
												
				return per_aca_nivel;
			}

		});	
	}


	// funciones privadas utilitarias para PeriodoAca

	private PeriodoAca rsToEntity(ResultSet rs,String alias) throws SQLException {
		PeriodoAca periodo_aca = new PeriodoAca();

		periodo_aca.setId(rs.getInt( alias + "id"));
		periodo_aca.setNom(rs.getString( alias + "nom"));
		periodo_aca.setEst(rs.getString( alias + "est"));
								
		return periodo_aca;

	}
	
}
