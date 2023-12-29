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
import com.tesla.colegio.model.Denominacion;


import com.sige.web.security.TokenSeguridad;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface DenominacionDAO.
 * @author MV
 *
 */
public class DenominacionDAOImpl{
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
	public int saveOrUpdate(Denominacion denominacion) {
		if (denominacion.getId() != null) {
			// update
			String sql = "UPDATE cat_denominacion "
						+ "SET nom=?, "
						+ "des=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			System.out.println(sql);

			jdbcTemplate.update(sql, 
						denominacion.getNom(),
						denominacion.getDes(),
						denominacion.getEst(),
						tokenSeguridad.getId(),
						new java.util.Date(),
						denominacion.getId()); 
			return denominacion.getId();

		} else {
			// insert
			String sql = "insert into cat_denominacion ("
						+ "nom, "
						+ "des, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?)";

				System.out.println(sql);

				jdbcTemplate.update(sql, 
				denominacion.getNom(),
				denominacion.getDes(),
				denominacion.getEst(),
				tokenSeguridad.getId(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from cat_denominacion where id=?";
		
		System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<Denominacion> list() {
		String sql = "select * from cat_denominacion";
		
		System.out.println(sql);
		
		List<Denominacion> listDenominacion = jdbcTemplate.query(sql, new RowMapper<Denominacion>() {

			@Override
			public Denominacion mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listDenominacion;
	}

	public Denominacion get(int id) {
		String sql = "select * from cat_denominacion WHERE id=" + id;
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Denominacion>() {

			@Override
			public Denominacion extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public Denominacion getFull(int id, String tablas[]) {
		String sql = "select cden.id cden_id, cden.nom cden_nom , cden.des cden_des  ,cden.est cden_est ";
	
		sql = sql + " from cat_denominacion cden "; 
		sql = sql + " where cden.id= " + id; 
				
		System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<Denominacion>() {
		
			@Override
			public Denominacion extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Denominacion denominacion= rsToEntity(rs,"cden_");
							return denominacion;
				}
				
				return null;
			}
			
		});


	}		
	
	public Denominacion getByParams(Param param) {

		String sql = "select * from cat_denominacion " + SQLFrmkUtil.getWhere(param);
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Denominacion>() {
			@Override
			public Denominacion extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<Denominacion> listByParams(Param param, String[] order) {

		String sql = "select * from cat_denominacion " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<Denominacion>() {

			@Override
			public Denominacion mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<Denominacion> listFullByParams(Denominacion denominacion, String[] order) {
	
		return listFullByParams(Param.toParam("cden",denominacion), order);
	
	}	
	
	public List<Denominacion> listFullByParams(Param param, String[] order) {

		String sql = "select cden.id cden_id, cden.nom cden_nom , cden.des cden_des  ,cden.est cden_est ";
		sql = sql + " from cat_denominacion cden";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<Denominacion>() {

			@Override
			public Denominacion mapRow(ResultSet rs, int rowNum) throws SQLException {
				Denominacion denominacion= rsToEntity(rs,"cden_");
				return denominacion;
			}

		});

	}	




	// funciones privadas utilitarias para Denominacion

	private Denominacion rsToEntity(ResultSet rs,String alias) throws SQLException {
		Denominacion denominacion = new Denominacion();

		denominacion.setId(rs.getInt( alias + "id"));
		denominacion.setNom(rs.getString( alias + "nom"));
		denominacion.setDes(rs.getString( alias + "des"));
		denominacion.setEst(rs.getString( alias + "est"));
								
		return denominacion;

	}
	
}
