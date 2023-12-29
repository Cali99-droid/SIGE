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
import com.tesla.colegio.model.TipEva;

import com.tesla.colegio.model.EvaluacionVacExamen;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface TipEvaDAO.
 * @author MV
 *
 */
public class TipEvaDAOImpl{
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(TipEva tip_eva) {
		if (tip_eva.getId() != null) {
			// update
			String sql = "UPDATE eva_tip_eva "
						+ "SET nom=?, "
						+ "tabla=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			

			jdbcTemplate.update(sql, 
						tip_eva.getNom(),
						tip_eva.getTabla(),
						tip_eva.getEst(),
						tip_eva.getUsr_act(),
						new java.util.Date(),
						tip_eva.getId()); 
			return tip_eva.getId();

		} else {
			// insert
			String sql = "insert into eva_tip_eva ("
						+ "nom, "
						+ "tabla, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?)";

				

				jdbcTemplate.update(sql, 
				tip_eva.getNom(),
				tip_eva.getTabla(),
				tip_eva.getEst(),
				tip_eva.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from eva_tip_eva where id=?";
		
		
		
		jdbcTemplate.update(sql, id);
	}

	public List<TipEva> list() {
		String sql = "select * from eva_tip_eva";
		
		
		
		List<TipEva> listTipEva = jdbcTemplate.query(sql, new RowMapper<TipEva>() {

			@Override
			public TipEva mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listTipEva;
	}

	public TipEva get(int id) {
		String sql = "select * from eva_tip_eva WHERE id=" + id;
		
		
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<TipEva>() {

			@Override
			public TipEva extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public TipEva getFull(int id, String tablas[]) {
		String sql = "select tae.id tae_id, tae.nom tae_nom , tae.tabla tae_tabla  ,tae.est tae_est ";
	
		sql = sql + " from eva_tip_eva tae "; 
		sql = sql + " where tae.id= " + id; 
				
		

		return jdbcTemplate.query(sql, new ResultSetExtractor<TipEva>() {
		
			@Override
			public TipEva extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					TipEva tipeva= rsToEntity(rs,"tae_");
							return tipeva;
				}
				
				return null;
			}
			
		});


	}		
	
	public TipEva getByParams(Param param) {

		String sql = "select * from eva_tip_eva " + SQLFrmkUtil.getWhere(param);
		
		
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<TipEva>() {
			@Override
			public TipEva extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<TipEva> listByParams(Param param, String[] order) {

		String sql = "select * from eva_tip_eva " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		

		return jdbcTemplate.query(sql, new RowMapper<TipEva>() {

			@Override
			public TipEva mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<TipEva> listFullByParams(TipEva tipeva, String[] order) {
	
		return listFullByParams(Param.toParam("tae",tipeva), order);
	
	}	
	
	public List<TipEva> listFullByParams(Param param, String[] order) {

		String sql = "select tae.id tae_id, tae.nom tae_nom , tae.tabla tae_tabla  ,tae.est tae_est ";
		sql = sql + " from eva_tip_eva tae";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		

		return jdbcTemplate.query(sql, new RowMapper<TipEva>() {

			@Override
			public TipEva mapRow(ResultSet rs, int rowNum) throws SQLException {
				TipEva tipeva= rsToEntity(rs,"tae_");
				return tipeva;
			}

		});

	}	


	public List<EvaluacionVacExamen> getListEvaluacionVacExamen(Param param, String[] order) {
		String sql = "select * from eva_evaluacion_vac_examen " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		
		
		return jdbcTemplate.query(sql, new RowMapper<EvaluacionVacExamen>() {

			@Override
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


	// funciones privadas utilitarias para TipEva

	private TipEva rsToEntity(ResultSet rs,String alias) throws SQLException {
		TipEva tip_eva = new TipEva();

		tip_eva.setId(rs.getInt( alias + "id"));
		tip_eva.setNom(rs.getString( alias + "nom"));
		tip_eva.setTabla(rs.getString( alias + "tabla"));
		tip_eva.setEst(rs.getString( alias + "est"));
								
		return tip_eva;

	}
	
}
