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
import com.tesla.colegio.model.RegimenLaboral;



import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface RegimenLaboralDAO.
 * @author MV
 *
 */
public class RegimenLaboralDAOImpl{
	final static Logger logger = Logger.getLogger(RegimenLaboralDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(RegimenLaboral regimen_laboral) {
		if (regimen_laboral.getId() > 0) {
			// update
			String sql = "UPDATE cat_regimen_laboral "
						+ "SET nom=?, "
						+ "des=?, "
						+ "est=?,usr_act=?,fec_act=? "
					+ "WHERE id=?";
			
			//logger.info(sql);

			return jdbcTemplate.update(sql, 
						regimen_laboral.getNom(),
						regimen_laboral.getDes(),
						regimen_laboral.getEst(),
						regimen_laboral.getUsr_act(),
						new java.util.Date(),
						regimen_laboral.getId()); 

		} else {
			// insert
			String sql = "insert into cat_regimen_laboral ("
						+ "nom, "
						+ "des, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				regimen_laboral.getNom(),
				regimen_laboral.getDes(),
				regimen_laboral.getEst(),
				regimen_laboral.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	
	public void delete(int id) {
		String sql = "delete from cat_regimen_laboral where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	
	public List<RegimenLaboral> list() {
		String sql = "select * from cat_regimen_laboral";
		
		//logger.info(sql);
		
		List<RegimenLaboral> listRegimenLaboral = jdbcTemplate.query(sql, new RowMapper<RegimenLaboral>() {

			
			public RegimenLaboral mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listRegimenLaboral;
	}

	
	public RegimenLaboral get(int id) {
		String sql = "select * from cat_regimen_laboral WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<RegimenLaboral>() {

			
			public RegimenLaboral extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	
	public RegimenLaboral getFull(int id, String tablas[]) {
		String sql = "select reg.id reg_id, reg.nom reg_nom , reg.des reg_des  ,reg.est reg_est ";
	
		sql = sql + " from cat_regimen_laboral reg "; 
		sql = sql + " where reg.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<RegimenLaboral>() {
		
			
			public RegimenLaboral extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					RegimenLaboral regimenLaboral= rsToEntity(rs,"reg_");
							return regimenLaboral;
				}
				
				return null;
			}
			
		});


	}		
	
	
	public RegimenLaboral getByParams(Param param) {

		String sql = "select * from cat_regimen_laboral " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<RegimenLaboral>() {
			
			public RegimenLaboral extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	
	public List<RegimenLaboral> listByParams(Param param, String[] order) {

		String sql = "select * from cat_regimen_laboral " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<RegimenLaboral>() {

			
			public RegimenLaboral mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	
	public List<RegimenLaboral> listFullByParams(RegimenLaboral regimenLaboral, String[] order) {
	
		return listFullByParams(Param.toParam("reg",regimenLaboral), order);
	
	}	
	
	
	public List<RegimenLaboral> listFullByParams(Param param, String[] order) {

		String sql = "select reg.id reg_id, reg.nom reg_nom , reg.des reg_des  ,reg.est reg_est ";
		sql = sql + " from cat_regimen_laboral reg";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<RegimenLaboral>() {

			
			public RegimenLaboral mapRow(ResultSet rs, int rowNum) throws SQLException {
				RegimenLaboral regimenLaboral= rsToEntity(rs,"reg_");
				return regimenLaboral;
			}

		});

	}	




	// funciones privadas utilitarias para RegimenLaboral

	private RegimenLaboral rsToEntity(ResultSet rs,String alias) throws SQLException {
		RegimenLaboral regimen_laboral = new RegimenLaboral();

		regimen_laboral.setId(rs.getInt( alias + "id"));
		regimen_laboral.setNom(rs.getString( alias + "nom"));
		regimen_laboral.setDes(rs.getString( alias + "des"));
		regimen_laboral.setEst(rs.getString( alias + "est"));
								
		return regimen_laboral;

	}
	
}
