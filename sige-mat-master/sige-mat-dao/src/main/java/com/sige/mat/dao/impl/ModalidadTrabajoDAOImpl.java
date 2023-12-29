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
import com.tesla.colegio.model.ModalidadTrabajo;


import com.sige.web.security.TokenSeguridad;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface ModalidadTrabajoDAO.
 * @author MV
 *
 */
public class ModalidadTrabajoDAOImpl{
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
	public int saveOrUpdate(ModalidadTrabajo modalidad_trabajo) {
		if (modalidad_trabajo.getId() != null) {
			// update
			String sql = "UPDATE cat_modalidad_trabajo "
						+ "SET nom=?, "
						+ "des=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			System.out.println(sql);

			jdbcTemplate.update(sql, 
						modalidad_trabajo.getNom(),
						modalidad_trabajo.getDes(),
						modalidad_trabajo.getEst(),
						tokenSeguridad.getId(),
						new java.util.Date(),
						modalidad_trabajo.getId()); 
			return modalidad_trabajo.getId();

		} else {
			// insert
			String sql = "insert into cat_modalidad_trabajo ("
						+ "nom, "
						+ "des, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?)";

				System.out.println(sql);

				jdbcTemplate.update(sql, 
				modalidad_trabajo.getNom(),
				modalidad_trabajo.getDes(),
				modalidad_trabajo.getEst(),
				tokenSeguridad.getId(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from cat_modalidad_trabajo where id=?";
		
		System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<ModalidadTrabajo> list() {
		String sql = "select * from cat_modalidad_trabajo";
		
		System.out.println(sql);
		
		List<ModalidadTrabajo> listModalidadTrabajo = jdbcTemplate.query(sql, new RowMapper<ModalidadTrabajo>() {

			@Override
			public ModalidadTrabajo mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listModalidadTrabajo;
	}

	public ModalidadTrabajo get(int id) {
		String sql = "select * from cat_modalidad_trabajo WHERE id=" + id;
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<ModalidadTrabajo>() {

			@Override
			public ModalidadTrabajo extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public ModalidadTrabajo getFull(int id, String tablas[]) {
		String sql = "select mtrab.id mtrab_id, mtrab.nom mtrab_nom , mtrab.des mtrab_des  ,mtrab.est mtrab_est ";
	
		sql = sql + " from cat_modalidad_trabajo mtrab "; 
		sql = sql + " where mtrab.id= " + id; 
				
		System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<ModalidadTrabajo>() {
		
			@Override
			public ModalidadTrabajo extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					ModalidadTrabajo modalidadtrabajo= rsToEntity(rs,"mtrab_");
							return modalidadtrabajo;
				}
				
				return null;
			}
			
		});


	}		
	
	public ModalidadTrabajo getByParams(Param param) {

		String sql = "select * from cat_modalidad_trabajo " + SQLFrmkUtil.getWhere(param);
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<ModalidadTrabajo>() {
			@Override
			public ModalidadTrabajo extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<ModalidadTrabajo> listByParams(Param param, String[] order) {

		String sql = "select * from cat_modalidad_trabajo " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<ModalidadTrabajo>() {

			@Override
			public ModalidadTrabajo mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<ModalidadTrabajo> listFullByParams(ModalidadTrabajo modalidadtrabajo, String[] order) {
	
		return listFullByParams(Param.toParam("mtrab",modalidadtrabajo), order);
	
	}	
	
	public List<ModalidadTrabajo> listFullByParams(Param param, String[] order) {

		String sql = "select mtrab.id mtrab_id, mtrab.nom mtrab_nom , mtrab.des mtrab_des  ,mtrab.est mtrab_est ";
		sql = sql + " from cat_modalidad_trabajo mtrab";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<ModalidadTrabajo>() {

			@Override
			public ModalidadTrabajo mapRow(ResultSet rs, int rowNum) throws SQLException {
				ModalidadTrabajo modalidadtrabajo= rsToEntity(rs,"mtrab_");
				return modalidadtrabajo;
			}

		});

	}	




	// funciones privadas utilitarias para ModalidadTrabajo

	private ModalidadTrabajo rsToEntity(ResultSet rs,String alias) throws SQLException {
		ModalidadTrabajo modalidad_trabajo = new ModalidadTrabajo();

		modalidad_trabajo.setId(rs.getInt( alias + "id"));
		modalidad_trabajo.setNom(rs.getString( alias + "nom"));
		modalidad_trabajo.setDes(rs.getString( alias + "des"));
		modalidad_trabajo.setEst(rs.getString( alias + "est"));
								
		return modalidad_trabajo;

	}
	
}
