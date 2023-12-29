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
import com.tesla.colegio.model.AreaEva;

import com.tesla.colegio.model.EvaluacionVacExamen;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface AreaEvaDAO.
 * @author MV
 *
 */
public class AreaEvaDAOImpl{
	
	final static Logger logger = Logger.getLogger(AreaEvaDAOImpl.class);
	
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(AreaEva area_eva) {
		if (area_eva.getId() != null) {
			// update
			String sql = "UPDATE eva_area_eva "
						+ "SET nom=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			return jdbcTemplate.update(sql, 
						area_eva.getNom(),
						area_eva.getEst(),
						area_eva.getUsr_act(),
						new java.util.Date(),
						area_eva.getId()); 

		} else {
			// insert
			String sql = "insert into eva_area_eva ("
						+ "nom, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				area_eva.getNom(),
				area_eva.getEst(),
				area_eva.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	
	public void delete(int id) {
		String sql = "delete from eva_area_eva where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	
	public List<AreaEva> list() {
		String sql = "select * from eva_area_eva";
		
		//logger.info(sql);
		
		List<AreaEva> listAreaEva = jdbcTemplate.query(sql, new RowMapper<AreaEva>() {

			
			public AreaEva mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listAreaEva;
	}

	
	public AreaEva get(int id) {
		String sql = "select * from eva_area_eva WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<AreaEva>() {

			
			public AreaEva extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	
	public AreaEva getFull(int id, String tablas[]) {
		String sql = "select eae.id eae_id, eae.nom eae_nom  ,eae.est eae_est ";
	
		sql = sql + " from eva_area_eva eae "; 
		sql = sql + " where eae.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<AreaEva>() {
		
			
			public AreaEva extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					AreaEva areaeva= rsToEntity(rs,"eae_");
							return areaeva;
				}
				
				return null;
			}
			
		});


	}		
	
	
	public AreaEva getByParams(Param param) {

		String sql = "select * from eva_area_eva " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<AreaEva>() {
			
			public AreaEva extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	
	public List<AreaEva> listByParams(Param param, String[] order) {

		String sql = "select * from eva_area_eva " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<AreaEva>() {

			
			public AreaEva mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	
	public List<AreaEva> listFullByParams(AreaEva areaeva, String[] order) {
	
		return listFullByParams(Param.toParam("eae",areaeva), order);
	
	}	
	
	
	public List<AreaEva> listFullByParams(Param param, String[] order) {

		String sql = "select eae.id eae_id, eae.nom eae_nom  ,eae.est eae_est ";
		sql = sql + " from eva_area_eva eae";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<AreaEva>() {

			
			public AreaEva mapRow(ResultSet rs, int rowNum) throws SQLException {
				AreaEva areaeva= rsToEntity(rs,"eae_");
				return areaeva;
			}

		});

	}	


	public List<EvaluacionVacExamen> getListEvaluacionVacExamen(Param param, String[] order) {
		String sql = "select * from eva_evaluacion_vac_examen " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<EvaluacionVacExamen>() {

			
			public EvaluacionVacExamen mapRow(ResultSet rs, int rowNum) throws SQLException {
				EvaluacionVacExamen evaluacion_vac_examen = new EvaluacionVacExamen();

				evaluacion_vac_examen.setId(rs.getInt("id"));
				evaluacion_vac_examen.setId_eva(rs.getInt("id_eva"));
				evaluacion_vac_examen.setId_eae(rs.getInt("id_eae"));
				evaluacion_vac_examen.setId_tae(rs.getInt("id_tae"));
				evaluacion_vac_examen.setFec_exa(rs.getDate("fec_exa"));
				evaluacion_vac_examen.setFec_not(rs.getDate("fec_not"));
				evaluacion_vac_examen.setEst(rs.getString("est"));
												
				return evaluacion_vac_examen;
			}

		});	
	}
	
	// funciones privadas utilitarias para AreaEva

	private AreaEva rsToEntity(ResultSet rs,String alias) throws SQLException {
		AreaEva area_eva = new AreaEva();

		area_eva.setId(rs.getInt( alias + "id"));
		area_eva.setNom(rs.getString( alias + "nom"));
		area_eva.setEst(rs.getString( alias + "est"));
								
		return area_eva;

	}
	
}
