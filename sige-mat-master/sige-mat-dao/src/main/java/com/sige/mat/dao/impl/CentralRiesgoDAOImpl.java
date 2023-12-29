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
import com.tesla.colegio.model.CentralRiesgo;

import com.tesla.colegio.model.HistorialEco;
import com.tesla.colegio.model.HistorialEco;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface CentralRiesgoDAO.
 * @author MV
 *
 */
public class CentralRiesgoDAOImpl{
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(CentralRiesgo central_riesgo) {
		if (central_riesgo.getId() != null) {
			// update
			String sql = "UPDATE cat_central_riesgo "
						+ "SET nom=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			

			jdbcTemplate.update(sql, 
						central_riesgo.getNom(),
						central_riesgo.getEst(),
						central_riesgo.getUsr_act(),
						new java.util.Date(),
						central_riesgo.getId()); 
			return central_riesgo.getId();

		} else {
			// insert
			String sql = "insert into cat_central_riesgo ("
						+ "nom, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?)";

				

				jdbcTemplate.update(sql, 
				central_riesgo.getNom(),
				central_riesgo.getEst(),
				central_riesgo.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from cat_central_riesgo where id=?";
		
		
		
		jdbcTemplate.update(sql, id);
	}

	public List<CentralRiesgo> list() {
		String sql = "select * from cat_central_riesgo";
		
		
		
		List<CentralRiesgo> listCentralRiesgo = jdbcTemplate.query(sql, new RowMapper<CentralRiesgo>() {

			@Override
			public CentralRiesgo mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listCentralRiesgo;
	}

	public CentralRiesgo get(int id) {
		String sql = "select * from cat_central_riesgo WHERE id=" + id;
		
		
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<CentralRiesgo>() {

			@Override
			public CentralRiesgo extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public CentralRiesgo getFull(int id, String tablas[]) {
		String sql = "select ccr.id ccr_id, ccr.nom ccr_nom  ,ccr.est ccr_est ";
	
		sql = sql + " from cat_central_riesgo ccr "; 
		sql = sql + " where ccr.id= " + id; 
				
		

		return jdbcTemplate.query(sql, new ResultSetExtractor<CentralRiesgo>() {
		
			@Override
			public CentralRiesgo extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					CentralRiesgo centralriesgo= rsToEntity(rs,"ccr_");
							return centralriesgo;
				}
				
				return null;
			}
			
		});


	}		
	
	public CentralRiesgo getByParams(Param param) {

		String sql = "select * from cat_central_riesgo " + SQLFrmkUtil.getWhere(param);
		
		
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<CentralRiesgo>() {
			@Override
			public CentralRiesgo extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<CentralRiesgo> listByParams(Param param, String[] order) {

		String sql = "select * from cat_central_riesgo " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		

		return jdbcTemplate.query(sql, new RowMapper<CentralRiesgo>() {

			@Override
			public CentralRiesgo mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<CentralRiesgo> listFullByParams(CentralRiesgo centralriesgo, String[] order) {
	
		return listFullByParams(Param.toParam("ccr",centralriesgo), order);
	
	}	
	
	public List<CentralRiesgo> listFullByParams(Param param, String[] order) {

		String sql = "select ccr.id ccr_id, ccr.nom ccr_nom  ,ccr.est ccr_est ";
		sql = sql + " from cat_central_riesgo ccr";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		

		return jdbcTemplate.query(sql, new RowMapper<CentralRiesgo>() {

			@Override
			public CentralRiesgo mapRow(ResultSet rs, int rowNum) throws SQLException {
				CentralRiesgo centralriesgo= rsToEntity(rs,"ccr_");
				return centralriesgo;
			}

		});

	}	


	public List<HistorialEco> getListHistorialEco(Param param, String[] order) {
		String sql = "select * from col_historial_eco " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		
		
		return jdbcTemplate.query(sql, new RowMapper<HistorialEco>() {

			@Override
			public HistorialEco mapRow(ResultSet rs, int rowNum) throws SQLException {
				HistorialEco historial_eco = new HistorialEco();

				historial_eco.setId(rs.getInt("id"));
				historial_eco.setId_mat(rs.getInt("id_mat"));
				historial_eco.setId_ccr_pad(rs.getInt("id_ccr_pad"));
				historial_eco.setId_ccr_mad(rs.getInt("id_ccr_mad"));
				historial_eco.setUlt_men(rs.getString("ult_men"));
				historial_eco.setNro_mens(rs.getInt("nro_mens"));
				historial_eco.setIng_fam(rs.getBigDecimal("ing_fam"));
				historial_eco.setPuntaje(rs.getBigDecimal("puntaje"));
				historial_eco.setEst(rs.getString("est"));
												
				return historial_eco;
			}

		});	
	}
	
	// funciones privadas utilitarias para CentralRiesgo

	private CentralRiesgo rsToEntity(ResultSet rs,String alias) throws SQLException {
		CentralRiesgo central_riesgo = new CentralRiesgo();

		central_riesgo.setId(rs.getInt( alias + "id"));
		central_riesgo.setNom(rs.getString( alias + "nom"));
		central_riesgo.setEst(rs.getString( alias + "est"));
								
		return central_riesgo;

	}
	
}
