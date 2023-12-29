package com.sige.mat.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Arrays;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import com.tesla.colegio.model.CondAlumno;

import com.tesla.colegio.model.TipCond;
import com.tesla.colegio.model.Condicion;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface CondAlumnoDAO.
 * @author MV
 *
 */
public class CondAlumnoDAOImpl{
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(CondAlumno cond_alumno) {
		if (cond_alumno.getId() != null) {
			// update
			String sql = "UPDATE cat_cond_alumno "
						+ "SET id_ctc=?, "
						+ "nom=?, "
						+ "des=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			

			jdbcTemplate.update(sql, 
						cond_alumno.getId_ctc(),
						cond_alumno.getNom(),
						cond_alumno.getDes(),
						cond_alumno.getEst(),
						cond_alumno.getUsr_act(),
						new java.util.Date(),
						cond_alumno.getId()); 
			return cond_alumno.getId();

		} else {
			// insert
			String sql = "insert into cat_cond_alumno ("
						+ "id_ctc, "
						+ "nom, "
						+ "des, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?)";

				

				jdbcTemplate.update(sql, 
				cond_alumno.getId_ctc(),
				cond_alumno.getNom(),
				cond_alumno.getDes(),
				cond_alumno.getEst(),
				cond_alumno.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from cat_cond_alumno where id=?";
		
		
		
		jdbcTemplate.update(sql, id);
	}

	public List<CondAlumno> list() {
		String sql = "select * from cat_cond_alumno";
		
		
		
		List<CondAlumno> listCondAlumno = jdbcTemplate.query(sql, new RowMapper<CondAlumno>() {

			@Override
			public CondAlumno mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listCondAlumno;
	}

	public CondAlumno get(int id) {
		String sql = "select * from cat_cond_alumno WHERE id=" + id;
		
		
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<CondAlumno>() {

			@Override
			public CondAlumno extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public CondAlumno getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select cond.id cond_id, cond.id_ctc cond_id_ctc , cond.nom cond_nom , cond.des cond_des  ,cond.est cond_est ";
		if (aTablas.contains("cat_tip_cond"))
			sql = sql + ", ctc.id ctc_id  , ctc.nom ctc_nom  ";
	
		sql = sql + " from cat_cond_alumno cond "; 
		if (aTablas.contains("cat_tip_cond"))
			sql = sql + " left join cat_tip_cond ctc on ctc.id = cond.id_ctc ";
		sql = sql + " where cond.id= " + id; 
				
		

		return jdbcTemplate.query(sql, new ResultSetExtractor<CondAlumno>() {
		
			@Override
			public CondAlumno extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					CondAlumno condalumno= rsToEntity(rs,"cond_");
					if (aTablas.contains("cat_tip_cond")){
						TipCond tipcond = new TipCond();  
							tipcond.setId(rs.getInt("ctc_id")) ;  
							tipcond.setNom(rs.getString("ctc_nom")) ;  
							condalumno.setTipCond(tipcond);
					}
							return condalumno;
				}
				
				return null;
			}
			
		});


	}		
	
	public CondAlumno getByParams(Param param) {

		String sql = "select * from cat_cond_alumno " + SQLFrmkUtil.getWhere(param);
		
		
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<CondAlumno>() {
			@Override
			public CondAlumno extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<CondAlumno> listByParams(Param param, String[] order) {

		String sql = "select * from cat_cond_alumno " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		

		return jdbcTemplate.query(sql, new RowMapper<CondAlumno>() {

			@Override
			public CondAlumno mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<CondAlumno> listFullByParams(CondAlumno condalumno, String[] order) {
	
		return listFullByParams(Param.toParam("cond",condalumno), order);
	
	}	
	
	public List<CondAlumno> listFullByParams(Param param, String[] order) {

		String sql = "select cond.id cond_id, cond.id_ctc cond_id_ctc , cond.nom cond_nom , cond.des cond_des  ,cond.est cond_est ";
		sql = sql + ", ctc.id ctc_id  , ctc.nom ctc_nom  ";
		sql = sql + " from cat_cond_alumno cond";
		sql = sql + " left join cat_tip_cond ctc on ctc.id = cond.id_ctc ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		

		return jdbcTemplate.query(sql, new RowMapper<CondAlumno>() {

			@Override
			public CondAlumno mapRow(ResultSet rs, int rowNum) throws SQLException {
				CondAlumno condalumno= rsToEntity(rs,"cond_");
				TipCond tipcond = new TipCond();  
				tipcond.setId(rs.getInt("ctc_id")) ;  
				tipcond.setNom(rs.getString("ctc_nom")) ;  
				condalumno.setTipCond(tipcond);
				return condalumno;
			}

		});

	}	


	public List<Condicion> getListCondicion(Param param, String[] order) {
		String sql = "select * from mat_condicion " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		
		
		return jdbcTemplate.query(sql, new RowMapper<Condicion>() {

			@Override
			public Condicion mapRow(ResultSet rs, int rowNum) throws SQLException {
				Condicion condicion = new Condicion();

				condicion.setId(rs.getInt("id"));
				condicion.setId_cond(rs.getInt("id_cond"));
				condicion.setId_mat(rs.getInt("id_mat"));
				condicion.setEst(rs.getString("est"));
												
				return condicion;
			}

		});	
	}


	// funciones privadas utilitarias para CondAlumno

	private CondAlumno rsToEntity(ResultSet rs,String alias) throws SQLException {
		CondAlumno cond_alumno = new CondAlumno();

		cond_alumno.setId(rs.getInt( alias + "id"));
		cond_alumno.setId_ctc(rs.getInt( alias + "id_ctc"));
		cond_alumno.setNom(rs.getString( alias + "nom"));
		cond_alumno.setDes(rs.getString( alias + "des"));
		cond_alumno.setEst(rs.getString( alias + "est"));
								
		return cond_alumno;

	}
	
}
