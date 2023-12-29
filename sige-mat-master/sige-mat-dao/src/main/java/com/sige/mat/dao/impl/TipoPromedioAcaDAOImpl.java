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
import com.tesla.colegio.model.TipoPromedioAca;


import com.sige.web.security.TokenSeguridad;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface TipoPromedioAcaDAO.
 * @author MV
 *
 */
public class TipoPromedioAcaDAOImpl{
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
	public int saveOrUpdate(TipoPromedioAca tipo_promedio_aca) {
		if (tipo_promedio_aca.getId() != null) {
			// update
			String sql = "UPDATE cat_tipo_promedio_aca "
						+ "SET nom=?, "
						+ "cod=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			System.out.println(sql);

			jdbcTemplate.update(sql, 
						tipo_promedio_aca.getNom(),
						tipo_promedio_aca.getCod(),
						tipo_promedio_aca.getEst(),
						tokenSeguridad.getId(),
						new java.util.Date(),
						tipo_promedio_aca.getId()); 
			return tipo_promedio_aca.getId();

		} else {
			// insert
			String sql = "insert into cat_tipo_promedio_aca ("
						+ "nom, "
						+ "cod, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?)";

				System.out.println(sql);

				jdbcTemplate.update(sql, 
				tipo_promedio_aca.getNom(),
				tipo_promedio_aca.getCod(),
				tipo_promedio_aca.getEst(),
				tokenSeguridad.getId(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from cat_tipo_promedio_aca where id=?";
		
		System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<TipoPromedioAca> list() {
		String sql = "select * from cat_tipo_promedio_aca";
		
		System.out.println(sql);
		
		List<TipoPromedioAca> listTipoPromedioAca = jdbcTemplate.query(sql, new RowMapper<TipoPromedioAca>() {

			@Override
			public TipoPromedioAca mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listTipoPromedioAca;
	}

	public TipoPromedioAca get(int id) {
		String sql = "select * from cat_tipo_promedio_aca WHERE id=" + id;
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<TipoPromedioAca>() {

			@Override
			public TipoPromedioAca extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public TipoPromedioAca getFull(int id, String tablas[]) {
		String sql = "select tpa.id tpa_id, tpa.nom tpa_nom , tpa.cod tpa_cod  ,tpa.est tpa_est ";
	
		sql = sql + " from cat_tipo_promedio_aca tpa "; 
		sql = sql + " where tpa.id= " + id; 
				
		System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<TipoPromedioAca>() {
		
			@Override
			public TipoPromedioAca extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					TipoPromedioAca tipopromedioaca= rsToEntity(rs,"tpa_");
							return tipopromedioaca;
				}
				
				return null;
			}
			
		});


	}		
	
	public TipoPromedioAca getByParams(Param param) {

		String sql = "select * from cat_tipo_promedio_aca " + SQLFrmkUtil.getWhere(param);
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<TipoPromedioAca>() {
			@Override
			public TipoPromedioAca extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<TipoPromedioAca> listByParams(Param param, String[] order) {

		String sql = "select * from cat_tipo_promedio_aca " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<TipoPromedioAca>() {

			@Override
			public TipoPromedioAca mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<TipoPromedioAca> listFullByParams(TipoPromedioAca tipopromedioaca, String[] order) {
	
		return listFullByParams(Param.toParam("tpa",tipopromedioaca), order);
	
	}	
	
	public List<TipoPromedioAca> listFullByParams(Param param, String[] order) {

		String sql = "select tpa.id tpa_id, tpa.nom tpa_nom , tpa.cod tpa_cod  ,tpa.est tpa_est ";
		sql = sql + " from cat_tipo_promedio_aca tpa";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<TipoPromedioAca>() {

			@Override
			public TipoPromedioAca mapRow(ResultSet rs, int rowNum) throws SQLException {
				TipoPromedioAca tipopromedioaca= rsToEntity(rs,"tpa_");
				return tipopromedioaca;
			}

		});

	}	




	// funciones privadas utilitarias para TipoPromedioAca

	private TipoPromedioAca rsToEntity(ResultSet rs,String alias) throws SQLException {
		TipoPromedioAca tipo_promedio_aca = new TipoPromedioAca();

		tipo_promedio_aca.setId(rs.getInt( alias + "id"));
		tipo_promedio_aca.setNom(rs.getString( alias + "nom"));
		tipo_promedio_aca.setCod(rs.getString( alias + "cod"));
		tipo_promedio_aca.setEst(rs.getString( alias + "est"));
								
		return tipo_promedio_aca;

	}
	
}
