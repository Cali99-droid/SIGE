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
import com.tesla.colegio.model.Remuneracion;


import com.sige.web.security.TokenSeguridad;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface RemuneracionDAO.
 * @author MV
 *
 */
public class RemuneracionDAOImpl{
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@Autowired
	private TokenSeguridad tokenSeguridad;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(Remuneracion remuneracion) {
		if (remuneracion.getId() != null) {
			// update
			String sql = "UPDATE cat_remuneracion "
						+ "SET nom=?, "
						+ "des=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			System.out.println(sql);

			jdbcTemplate.update(sql, 
						remuneracion.getNom(),
						remuneracion.getDes(),
						remuneracion.getEst(),
						tokenSeguridad.getId(),
						new java.util.Date(),
						remuneracion.getId()); 
			return remuneracion.getId();

		} else {
			// insert
			String sql = "insert into cat_remuneracion ("
						+ "nom, "
						+ "des, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?)";

				System.out.println(sql);

				jdbcTemplate.update(sql, 
				remuneracion.getNom(),
				remuneracion.getDes(),
				remuneracion.getEst(),
				tokenSeguridad.getId(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from cat_remuneracion where id=?";
		
		System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<Remuneracion> list() {
		String sql = "select * from cat_remuneracion";
		
		System.out.println(sql);
		
		List<Remuneracion> listRemuneracion = jdbcTemplate.query(sql, new RowMapper<Remuneracion>() {

			@Override
			public Remuneracion mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listRemuneracion;
	}

	public Remuneracion get(int id) {
		String sql = "select * from cat_remuneracion WHERE id=" + id;
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Remuneracion>() {

			@Override
			public Remuneracion extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public Remuneracion getFull(int id, String tablas[]) {
		String sql = "select rem.id rem_id, rem.nom rem_nom , rem.des rem_des  ,rem.est rem_est ";
	
		sql = sql + " from cat_remuneracion rem "; 
		sql = sql + " where rem.id= " + id; 
				
		System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<Remuneracion>() {
		
			@Override
			public Remuneracion extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Remuneracion remuneracion= rsToEntity(rs,"rem_");
							return remuneracion;
				}
				
				return null;
			}
			
		});


	}		
	
	public Remuneracion getByParams(Param param) {

		String sql = "select * from cat_remuneracion " + SQLFrmkUtil.getWhere(param);
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Remuneracion>() {
			@Override
			public Remuneracion extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<Remuneracion> listByParams(Param param, String[] order) {

		String sql = "select * from cat_remuneracion " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<Remuneracion>() {

			@Override
			public Remuneracion mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<Remuneracion> listFullByParams(Remuneracion remuneracion, String[] order) {
	
		return listFullByParams(Param.toParam("rem",remuneracion), order);
	
	}	
	
	public List<Remuneracion> listFullByParams(Param param, String[] order) {

		String sql = "select rem.id rem_id, rem.nom rem_nom , rem.des rem_des  ,rem.est rem_est ";
		sql = sql + " from cat_remuneracion rem";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<Remuneracion>() {

			@Override
			public Remuneracion mapRow(ResultSet rs, int rowNum) throws SQLException {
				Remuneracion remuneracion= rsToEntity(rs,"rem_");
				return remuneracion;
			}

		});

	}	




	// funciones privadas utilitarias para Remuneracion

	private Remuneracion rsToEntity(ResultSet rs,String alias) throws SQLException {
		Remuneracion remuneracion = new Remuneracion();

		remuneracion.setId(rs.getInt( alias + "id"));
		remuneracion.setNom(rs.getString( alias + "nom"));
		remuneracion.setDes(rs.getString( alias + "des"));
		remuneracion.setEst(rs.getString( alias + "est"));
								
		return remuneracion;

	}
	
}
