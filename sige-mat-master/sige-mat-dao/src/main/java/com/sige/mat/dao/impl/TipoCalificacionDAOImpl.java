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
import com.tesla.colegio.model.TipoCalificacion;


import com.sige.web.security.TokenSeguridad;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface TipoCalificacionDAO.
 * @author MV
 *
 */
public class TipoCalificacionDAOImpl{
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
	public int saveOrUpdate(TipoCalificacion tipo_calificacion) {
		if (tipo_calificacion.getId() != null) {
			// update
			String sql = "UPDATE cat_tipo_calificacion "
						+ "SET nom=?, "
						+ "cod=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			System.out.println(sql);

			jdbcTemplate.update(sql, 
						tipo_calificacion.getNom(),
						tipo_calificacion.getCod(),
						tipo_calificacion.getEst(),
						tokenSeguridad.getId(),
						new java.util.Date(),
						tipo_calificacion.getId()); 
			return tipo_calificacion.getId();

		} else {
			// insert
			String sql = "insert into cat_tipo_calificacion ("
						+ "nom, "
						+ "cod, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?)";

				System.out.println(sql);

				jdbcTemplate.update(sql, 
				tipo_calificacion.getNom(),
				tipo_calificacion.getCod(),
				tipo_calificacion.getEst(),
				tokenSeguridad.getId(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from cat_tipo_calificacion where id=?";
		
		System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<TipoCalificacion> list() {
		String sql = "select * from cat_tipo_calificacion";
		
		System.out.println(sql);
		
		List<TipoCalificacion> listTipoCalificacion = jdbcTemplate.query(sql, new RowMapper<TipoCalificacion>() {

			@Override
			public TipoCalificacion mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listTipoCalificacion;
	}

	public TipoCalificacion get(int id) {
		String sql = "select * from cat_tipo_calificacion WHERE id=" + id;
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<TipoCalificacion>() {

			@Override
			public TipoCalificacion extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public TipoCalificacion getFull(int id, String tablas[]) {
		String sql = "select tca.id tca_id, tca.nom tca_nom , tca.cod tca_cod  ,tca.est tca_est ";
	
		sql = sql + " from cat_tipo_calificacion tca "; 
		sql = sql + " where tca.id= " + id; 
				
		System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<TipoCalificacion>() {
		
			@Override
			public TipoCalificacion extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					TipoCalificacion tipocalificacion= rsToEntity(rs,"tca_");
							return tipocalificacion;
				}
				
				return null;
			}
			
		});


	}		
	
	public TipoCalificacion getByParams(Param param) {

		String sql = "select * from cat_tipo_calificacion " + SQLFrmkUtil.getWhere(param);
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<TipoCalificacion>() {
			@Override
			public TipoCalificacion extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<TipoCalificacion> listByParams(Param param, String[] order) {

		String sql = "select * from cat_tipo_calificacion " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<TipoCalificacion>() {

			@Override
			public TipoCalificacion mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<TipoCalificacion> listFullByParams(TipoCalificacion tipocalificacion, String[] order) {
	
		return listFullByParams(Param.toParam("tca",tipocalificacion), order);
	
	}	
	
	public List<TipoCalificacion> listFullByParams(Param param, String[] order) {

		String sql = "select tca.id tca_id, tca.nom tca_nom , tca.cod tca_cod  ,tca.est tca_est ";
		sql = sql + " from cat_tipo_calificacion tca";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<TipoCalificacion>() {

			@Override
			public TipoCalificacion mapRow(ResultSet rs, int rowNum) throws SQLException {
				TipoCalificacion tipocalificacion= rsToEntity(rs,"tca_");
				return tipocalificacion;
			}

		});

	}	




	// funciones privadas utilitarias para TipoCalificacion

	private TipoCalificacion rsToEntity(ResultSet rs,String alias) throws SQLException {
		TipoCalificacion tipo_calificacion = new TipoCalificacion();

		tipo_calificacion.setId(rs.getInt( alias + "id"));
		tipo_calificacion.setNom(rs.getString( alias + "nom"));
		tipo_calificacion.setCod(rs.getString( alias + "cod"));
		tipo_calificacion.setEst(rs.getString( alias + "est"));
								
		return tipo_calificacion;

	}
	
}
