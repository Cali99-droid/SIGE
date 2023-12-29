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
import com.tesla.colegio.model.TipCond;

import com.tesla.colegio.model.CondAlumno;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface TipCondDAO.
 * @author MV
 *
 */
public class TipCondDAOImpl{
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(TipCond tip_cond) {
		if (tip_cond.getId() != null) {
			// update
			String sql = "UPDATE cat_tip_cond "
						+ "SET nom=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			

			jdbcTemplate.update(sql, 
						tip_cond.getNom(),
						tip_cond.getEst(),
						tip_cond.getUsr_act(),
						new java.util.Date(),
						tip_cond.getId()); 
			return tip_cond.getId();

		} else {
			// insert
			String sql = "insert into cat_tip_cond ("
						+ "nom, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?)";

				

				jdbcTemplate.update(sql, 
				tip_cond.getNom(),
				tip_cond.getEst(),
				tip_cond.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from cat_tip_cond where id=?";
		
		
		
		jdbcTemplate.update(sql, id);
	}

	public List<TipCond> list() {
		String sql = "select * from cat_tip_cond";
		
		
		
		List<TipCond> listTipCond = jdbcTemplate.query(sql, new RowMapper<TipCond>() {

			@Override
			public TipCond mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listTipCond;
	}

	public TipCond get(int id) {
		String sql = "select * from cat_tip_cond WHERE id=" + id;
		
		
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<TipCond>() {

			@Override
			public TipCond extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public TipCond getFull(int id, String tablas[]) {
		String sql = "select ctc.id ctc_id, ctc.nom ctc_nom  ,ctc.est ctc_est ";
	
		sql = sql + " from cat_tip_cond ctc "; 
		sql = sql + " where ctc.id= " + id; 
				
		

		return jdbcTemplate.query(sql, new ResultSetExtractor<TipCond>() {
		
			@Override
			public TipCond extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					TipCond tipcond= rsToEntity(rs,"ctc_");
							return tipcond;
				}
				
				return null;
			}
			
		});


	}		
	
	public TipCond getByParams(Param param) {

		String sql = "select * from cat_tip_cond " + SQLFrmkUtil.getWhere(param);
		
		
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<TipCond>() {
			@Override
			public TipCond extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<TipCond> listByParams(Param param, String[] order) {

		String sql = "select * from cat_tip_cond " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		

		return jdbcTemplate.query(sql, new RowMapper<TipCond>() {

			@Override
			public TipCond mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<TipCond> listFullByParams(TipCond tipcond, String[] order) {
	
		return listFullByParams(Param.toParam("ctc",tipcond), order);
	
	}	
	
	public List<TipCond> listFullByParams(Param param, String[] order) {

		String sql = "select ctc.id ctc_id, ctc.nom ctc_nom  ,ctc.est ctc_est ";
		sql = sql + " from cat_tip_cond ctc";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		

		return jdbcTemplate.query(sql, new RowMapper<TipCond>() {

			@Override
			public TipCond mapRow(ResultSet rs, int rowNum) throws SQLException {
				TipCond tipcond= rsToEntity(rs,"ctc_");
				return tipcond;
			}

		});

	}	


	public List<CondAlumno> getListCondAlumno(Param param, String[] order) {
		String sql = "select * from cat_cond_alumno " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		
		
		return jdbcTemplate.query(sql, new RowMapper<CondAlumno>() {

			@Override
			public CondAlumno mapRow(ResultSet rs, int rowNum) throws SQLException {
				CondAlumno cond_alumno = new CondAlumno();

				cond_alumno.setId(rs.getInt("id"));
				cond_alumno.setId_ctc(rs.getInt("id_ctc"));
				cond_alumno.setNom(rs.getString("nom"));
				cond_alumno.setDes(rs.getString("des"));
				cond_alumno.setEst(rs.getString("est"));
												
				return cond_alumno;
			}

		});	
	}


	// funciones privadas utilitarias para TipCond

	private TipCond rsToEntity(ResultSet rs,String alias) throws SQLException {
		TipCond tip_cond = new TipCond();

		tip_cond.setId(rs.getInt( alias + "id"));
		tip_cond.setNom(rs.getString( alias + "nom"));
		tip_cond.setEst(rs.getString( alias + "est"));
								
		return tip_cond;

	}
	
}
