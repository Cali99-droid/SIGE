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
import com.tesla.colegio.model.PeriodoPrueba;


import com.sige.web.security.TokenSeguridad;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface PeriodoPruebaDAO.
 * @author MV
 *
 */
public class PeriodoPruebaDAOImpl{
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
	public int saveOrUpdate(PeriodoPrueba periodo_prueba) {
		if (periodo_prueba.getId() != null) {
			// update
			String sql = "UPDATE cat_periodo_prueba "
						+ "SET nom=?, "
						+ "des=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			System.out.println(sql);

			jdbcTemplate.update(sql, 
						periodo_prueba.getNom(),
						periodo_prueba.getDes(),
						periodo_prueba.getEst(),
						tokenSeguridad.getId(),
						new java.util.Date(),
						periodo_prueba.getId()); 
			return periodo_prueba.getId();

		} else {
			// insert
			String sql = "insert into cat_periodo_prueba ("
						+ "nom, "
						+ "des, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?)";

				System.out.println(sql);

				jdbcTemplate.update(sql, 
				periodo_prueba.getNom(),
				periodo_prueba.getDes(),
				periodo_prueba.getEst(),
				tokenSeguridad.getId(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from cat_periodo_prueba where id=?";
		
		System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<PeriodoPrueba> list() {
		String sql = "select * from cat_periodo_prueba";
		
		System.out.println(sql);
		
		List<PeriodoPrueba> listPeriodoPrueba = jdbcTemplate.query(sql, new RowMapper<PeriodoPrueba>() {

			@Override
			public PeriodoPrueba mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listPeriodoPrueba;
	}

	public PeriodoPrueba get(int id) {
		String sql = "select * from cat_periodo_prueba WHERE id=" + id;
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<PeriodoPrueba>() {

			@Override
			public PeriodoPrueba extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public PeriodoPrueba getFull(int id, String tablas[]) {
		String sql = "select pprue.id pprue_id, pprue.nom pprue_nom , pprue.des pprue_des  ,pprue.est pprue_est ";
	
		sql = sql + " from cat_periodo_prueba pprue "; 
		sql = sql + " where pprue.id= " + id; 
				
		System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<PeriodoPrueba>() {
		
			@Override
			public PeriodoPrueba extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					PeriodoPrueba periodoprueba= rsToEntity(rs,"pprue_");
							return periodoprueba;
				}
				
				return null;
			}
			
		});


	}		
	
	public PeriodoPrueba getByParams(Param param) {

		String sql = "select * from cat_periodo_prueba " + SQLFrmkUtil.getWhere(param);
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<PeriodoPrueba>() {
			@Override
			public PeriodoPrueba extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<PeriodoPrueba> listByParams(Param param, String[] order) {

		String sql = "select * from cat_periodo_prueba " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<PeriodoPrueba>() {

			@Override
			public PeriodoPrueba mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<PeriodoPrueba> listFullByParams(PeriodoPrueba periodoprueba, String[] order) {
	
		return listFullByParams(Param.toParam("pprue",periodoprueba), order);
	
	}	
	
	public List<PeriodoPrueba> listFullByParams(Param param, String[] order) {

		String sql = "select pprue.id pprue_id, pprue.nom pprue_nom , pprue.des pprue_des  ,pprue.est pprue_est ";
		sql = sql + " from cat_periodo_prueba pprue";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<PeriodoPrueba>() {

			@Override
			public PeriodoPrueba mapRow(ResultSet rs, int rowNum) throws SQLException {
				PeriodoPrueba periodoprueba= rsToEntity(rs,"pprue_");
				return periodoprueba;
			}

		});

	}	




	// funciones privadas utilitarias para PeriodoPrueba

	private PeriodoPrueba rsToEntity(ResultSet rs,String alias) throws SQLException {
		PeriodoPrueba periodo_prueba = new PeriodoPrueba();

		periodo_prueba.setId(rs.getInt( alias + "id"));
		periodo_prueba.setNom(rs.getString( alias + "nom"));
		periodo_prueba.setDes(rs.getString( alias + "des"));
		periodo_prueba.setEst(rs.getString( alias + "est"));
								
		return periodo_prueba;

	}
	
}
