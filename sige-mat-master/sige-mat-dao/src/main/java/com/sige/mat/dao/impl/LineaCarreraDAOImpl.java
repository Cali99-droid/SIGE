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
import com.tesla.colegio.model.LineaCarrera;


import com.sige.web.security.TokenSeguridad;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface LineaCarreraDAO.
 * @author MV
 *
 */
public class LineaCarreraDAOImpl{
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
	public int saveOrUpdate(LineaCarrera linea_carrera) {
		if (linea_carrera.getId() != null) {
			// update
			String sql = "UPDATE cat_linea_carrera "
						+ "SET nom=?, "
						+ "des=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			System.out.println(sql);

			jdbcTemplate.update(sql, 
						linea_carrera.getNom(),
						linea_carrera.getDes(),
						linea_carrera.getEst(),
						tokenSeguridad.getId(),
						new java.util.Date(),
						linea_carrera.getId()); 
			return linea_carrera.getId();

		} else {
			// insert
			String sql = "insert into cat_linea_carrera ("
						+ "nom, "
						+ "des, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?)";

				System.out.println(sql);

				jdbcTemplate.update(sql, 
				linea_carrera.getNom(),
				linea_carrera.getDes(),
				linea_carrera.getEst(),
				tokenSeguridad.getId(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from cat_linea_carrera where id=?";
		
		System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<LineaCarrera> list() {
		String sql = "select * from cat_linea_carrera";
		
		System.out.println(sql);
		
		List<LineaCarrera> listLineaCarrera = jdbcTemplate.query(sql, new RowMapper<LineaCarrera>() {

			@Override
			public LineaCarrera mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listLineaCarrera;
	}

	public LineaCarrera get(int id) {
		String sql = "select * from cat_linea_carrera WHERE id=" + id;
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<LineaCarrera>() {

			@Override
			public LineaCarrera extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public LineaCarrera getFull(int id, String tablas[]) {
		String sql = "select lcarr.id lcarr_id, lcarr.nom lcarr_nom , lcarr.des lcarr_des  ,lcarr.est lcarr_est ";
	
		sql = sql + " from cat_linea_carrera lcarr "; 
		sql = sql + " where lcarr.id= " + id; 
				
		System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<LineaCarrera>() {
		
			@Override
			public LineaCarrera extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					LineaCarrera lineacarrera= rsToEntity(rs,"lcarr_");
							return lineacarrera;
				}
				
				return null;
			}
			
		});


	}		
	
	public LineaCarrera getByParams(Param param) {

		String sql = "select * from cat_linea_carrera " + SQLFrmkUtil.getWhere(param);
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<LineaCarrera>() {
			@Override
			public LineaCarrera extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<LineaCarrera> listByParams(Param param, String[] order) {

		String sql = "select * from cat_linea_carrera " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<LineaCarrera>() {

			@Override
			public LineaCarrera mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<LineaCarrera> listFullByParams(LineaCarrera lineacarrera, String[] order) {
	
		return listFullByParams(Param.toParam("lcarr",lineacarrera), order);
	
	}	
	
	public List<LineaCarrera> listFullByParams(Param param, String[] order) {

		String sql = "select lcarr.id lcarr_id, lcarr.nom lcarr_nom , lcarr.des lcarr_des  ,lcarr.est lcarr_est ";
		sql = sql + " from cat_linea_carrera lcarr";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<LineaCarrera>() {

			@Override
			public LineaCarrera mapRow(ResultSet rs, int rowNum) throws SQLException {
				LineaCarrera lineacarrera= rsToEntity(rs,"lcarr_");
				return lineacarrera;
			}

		});

	}	




	// funciones privadas utilitarias para LineaCarrera

	private LineaCarrera rsToEntity(ResultSet rs,String alias) throws SQLException {
		LineaCarrera linea_carrera = new LineaCarrera();

		linea_carrera.setId(rs.getInt( alias + "id"));
		linea_carrera.setNom(rs.getString( alias + "nom"));
		linea_carrera.setDes(rs.getString( alias + "des"));
		linea_carrera.setEst(rs.getString( alias + "est"));
								
		return linea_carrera;

	}
	
}
