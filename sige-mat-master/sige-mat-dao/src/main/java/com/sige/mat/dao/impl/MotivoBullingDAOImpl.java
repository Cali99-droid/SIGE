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
import com.tesla.colegio.model.MotivoBulling;

import com.tesla.colegio.model.RepBullingMotivo;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface MotivoBullingDAO.
 * @author MV
 *
 */
public class MotivoBullingDAOImpl{
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(MotivoBulling motivo_bulling) {
		if (motivo_bulling.getId() != null) {
			// update
			String sql = "UPDATE cat_motivo_bulling "
						+ "SET nom=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//System.out.println(sql);

			jdbcTemplate.update(sql, 
						motivo_bulling.getNom(),
						motivo_bulling.getEst(),
						motivo_bulling.getUsr_act(),
						new java.util.Date(),
						motivo_bulling.getId()); 
			return motivo_bulling.getId();

		} else {
			// insert
			String sql = "insert into cat_motivo_bulling ("
						+ "nom, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?)";

				//System.out.println(sql);

				jdbcTemplate.update(sql, 
				motivo_bulling.getNom(),
				motivo_bulling.getEst(),
				motivo_bulling.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from cat_motivo_bulling where id=?";
		
		//System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<MotivoBulling> list() {
		String sql = "select * from cat_motivo_bulling";
		
		//System.out.println(sql);
		
		List<MotivoBulling> listMotivoBulling = jdbcTemplate.query(sql, new RowMapper<MotivoBulling>() {

			@Override
			public MotivoBulling mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listMotivoBulling;
	}

	public MotivoBulling get(int id) {
		String sql = "select * from cat_motivo_bulling WHERE id=" + id;
		
		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<MotivoBulling>() {

			@Override
			public MotivoBulling extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public MotivoBulling getFull(int id, String tablas[]) {
		String sql = "select cmb.id cmb_id, cmb.nom cmb_nom  ,cmb.est cmb_est ";
	
		sql = sql + " from cat_motivo_bulling cmb "; 
		sql = sql + " where cmb.id= " + id; 
				
		//System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<MotivoBulling>() {
		
			@Override
			public MotivoBulling extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					MotivoBulling motivobulling= rsToEntity(rs,"cmb_");
							return motivobulling;
				}
				
				return null;
			}
			
		});


	}		
	
	public MotivoBulling getByParams(Param param) {

		String sql = "select * from cat_motivo_bulling " + SQLFrmkUtil.getWhere(param);
		
		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<MotivoBulling>() {
			@Override
			public MotivoBulling extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<MotivoBulling> listByParams(Param param, String[] order) {

		String sql = "select * from cat_motivo_bulling " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<MotivoBulling>() {

			@Override
			public MotivoBulling mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<MotivoBulling> listFullByParams(MotivoBulling motivobulling, String[] order) {
	
		return listFullByParams(Param.toParam("cmb",motivobulling), order);
	
	}	
	
	public List<MotivoBulling> listFullByParams(Param param, String[] order) {

		String sql = "select cmb.id cmb_id, cmb.nom cmb_nom  ,cmb.est cmb_est ";
		sql = sql + " from cat_motivo_bulling cmb";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<MotivoBulling>() {

			@Override
			public MotivoBulling mapRow(ResultSet rs, int rowNum) throws SQLException {
				MotivoBulling motivobulling= rsToEntity(rs,"cmb_");
				return motivobulling;
			}

		});

	}	


	public List<RepBullingMotivo> getListRepBullingMotivo(Param param, String[] order) {
		String sql = "select * from con_rep_bulling_motivo " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<RepBullingMotivo>() {

			@Override
			public RepBullingMotivo mapRow(ResultSet rs, int rowNum) throws SQLException {
				RepBullingMotivo rep_bulling_motivo = new RepBullingMotivo();

				rep_bulling_motivo.setId(rs.getInt("id"));
				rep_bulling_motivo.setId_crb(rs.getInt("id_crb"));
				rep_bulling_motivo.setId_cmb(rs.getInt("id_cmb"));
				rep_bulling_motivo.setOtro_motivo(rs.getString("otro_motivo"));
				rep_bulling_motivo.setEst(rs.getString("est"));
												
				return rep_bulling_motivo;
			}

		});	
	}


	// funciones privadas utilitarias para MotivoBulling

	private MotivoBulling rsToEntity(ResultSet rs,String alias) throws SQLException {
		MotivoBulling motivo_bulling = new MotivoBulling();

		motivo_bulling.setId(rs.getInt( alias + "id"));
		motivo_bulling.setNom(rs.getString( alias + "nom"));
		motivo_bulling.setEst(rs.getString( alias + "est"));
								
		return motivo_bulling;

	}
	
}
