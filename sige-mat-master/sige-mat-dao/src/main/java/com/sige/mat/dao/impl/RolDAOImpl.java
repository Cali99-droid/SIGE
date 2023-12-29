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

import com.sige.web.security.TokenSeguridad;
import com.tesla.colegio.model.Rol;

import com.tesla.colegio.model.UsuarioRol;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface RolDAO.
 * @author MV
 *
 */
public class RolDAOImpl{
	final static Logger logger = Logger.getLogger(RolDAOImpl.class);
	
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
	public int saveOrUpdate(Rol rol) {
		

		if (rol.getId() != null) {
			// update
			String sql = "UPDATE seg_rol "
						+ "SET nom=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);
			jdbcTemplate.update(sql, 
						rol.getNom(),
						rol.getEst(),
						tokenSeguridad.getId(),
						new java.util.Date(),
						rol.getId()); 
			return rol.getId();

		} else {
			// insert
			String sql = "insert into seg_rol ("
						+ "nom, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				rol.getNom(),
				rol.getEst(),
				tokenSeguridad.getId(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from seg_rol where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<Rol> list() {
		String sql = "select * from seg_rol";
		
		//logger.info(sql);
		
		List<Rol> listRol = jdbcTemplate.query(sql, new RowMapper<Rol>() {

			@Override
			public Rol mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listRol;
	}

	public Rol get(int id) {
		String sql = "select * from seg_rol WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Rol>() {

			@Override
			public Rol extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public Rol getFull(int id, String tablas[]) {
		String sql = "select rol.id rol_id, rol.nom rol_nom  ,rol.est rol_est ";
	
		sql = sql + " from seg_rol rol "; 
		sql = sql + " where rol.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<Rol>() {
		
			@Override
			public Rol extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Rol rol= rsToEntity(rs,"rol_");
							return rol;
				}
				
				return null;
			}
			
		});


	}		
	
	public Rol getByParams(Param param) {

		String sql = "select * from seg_rol " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Rol>() {
			@Override
			public Rol extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<Rol> listByParams(Param param, String[] order) {

		String sql = "select * from seg_rol " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Rol>() {

			@Override
			public Rol mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<Rol> listFullByParams(Rol rol, String[] order) {
	
		return listFullByParams(Param.toParam("rol",rol), order);
	
	}	
	
	public List<Rol> listFullByParams(Param param, String[] order) {

		String sql = "select rol.id rol_id, rol.nom rol_nom  ,rol.est rol_est ";
		sql = sql + " from seg_rol rol";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Rol>() {

			@Override
			public Rol mapRow(ResultSet rs, int rowNum) throws SQLException {
				Rol rol= rsToEntity(rs,"rol_");
				return rol;
			}

		});

	}	


	public List<UsuarioRol> getListUsuarioRol(Param param, String[] order) {
		String sql = "select * from seg_usuario_rol " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<UsuarioRol>() {

			@Override
			public UsuarioRol mapRow(ResultSet rs, int rowNum) throws SQLException {
				UsuarioRol usuario_rol = new UsuarioRol();

				usuario_rol.setId(rs.getInt("id"));
				usuario_rol.setId_usr(rs.getInt("id_usr"));
				usuario_rol.setId_rol(rs.getInt("id_rol"));
				usuario_rol.setEst(rs.getString("est"));
												
				return usuario_rol;
			}

		});	
	}


	// funciones privadas utilitarias para Rol

	private Rol rsToEntity(ResultSet rs,String alias) throws SQLException {
		Rol rol = new Rol();

		rol.setId(rs.getInt( alias + "id"));
		rol.setNom(rs.getString( alias + "nom"));
		rol.setEst(rs.getString( alias + "est"));
								
		return rol;

	}
	
}
