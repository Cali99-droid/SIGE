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
import com.tesla.colegio.model.Perfil;

import com.tesla.colegio.model.Usuario;
import com.tesla.colegio.model.PerfilOpcion;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface PerfilDAO.
 * @author MV
 *
 */
public class PerfilDAOImpl{
	final static Logger logger = Logger.getLogger(PerfilDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(Perfil perfil) {
		if (perfil.getId() !=null ) {
			// update
			String sql = "UPDATE seg_perfil "
						+ "SET nom=?, "
						+ "des=?, "
						+ "est=?,usr_act=?,fec_act=? "
					+ "WHERE id=?";
			
			//logger.info(sql);

			return jdbcTemplate.update(sql, 
						perfil.getNom(),
						perfil.getDes(),
						perfil.getEst(),
						perfil.getUsr_act(),
						new java.util.Date(),
						perfil.getId()); 

		} else {
			// insert
			String sql = "insert into seg_perfil ("
						+ "nom, "
						+ "des, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				perfil.getNom(),
				perfil.getDes(),
				perfil.getEst(),
				perfil.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	
	public void delete(int id) {
		String sql = "delete from seg_perfil where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	
	public List<Perfil> list() {
		String sql = "select * from seg_perfil";
		
		//logger.info(sql);
		
		List<Perfil> listPerfil = jdbcTemplate.query(sql, new RowMapper<Perfil>() {

			
			public Perfil mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listPerfil;
	}

	
	public Perfil get(int id) {
		String sql = "select * from seg_perfil WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Perfil>() {

			
			public Perfil extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	
	public Perfil getFull(int id, String tablas[]) {
		String sql = "select per.id per_id, per.nom per_nom , per.des per_des  ,per.est per_est, per.dias_adi_login per_dias_adi_login ";
	
		sql = sql + " from seg_perfil per "; 
		sql = sql + " where per.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<Perfil>() {
		
			
			public Perfil extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Perfil perfil= rsToEntity(rs,"per_");
							return perfil;
				}
				
				return null;
			}
			
		});


	}		
	
	
	public Perfil getByParams(Param param) {

		String sql = "select * from seg_perfil " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Perfil>() {
			
			public Perfil extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	
	public List<Perfil> listByParams(Param param, String[] order) {

		String sql = "select * from seg_perfil " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Perfil>() {

			
			public Perfil mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	
	public List<Perfil> listFullByParams(Perfil perfil, String[] order) {
	
		return listFullByParams(Param.toParam("per",perfil), order);
	
	}	
	
	
	public List<Perfil> listFullByParams(Param param, String[] order) {

		String sql = "select per.id per_id, per.nom per_nom , per.des per_des  ,per.est per_est, per.dias_adi_login per_dias_adi_login ";
		sql = sql + " from seg_perfil per";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Perfil>() {

			
			public Perfil mapRow(ResultSet rs, int rowNum) throws SQLException {
				Perfil perfil= rsToEntity(rs,"per_");
				return perfil;
			}

		});

	}	


	public List<Usuario> getListUsuario(Param param, String[] order) {
		String sql = "select * from seg_usuario " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<Usuario>() {

			
			public Usuario mapRow(ResultSet rs, int rowNum) throws SQLException {
				Usuario usuario = new Usuario();

				usuario.setId(rs.getInt("id"));
				usuario.setId_per(rs.getInt("id_per"));
				usuario.setLogin(rs.getString("login"));
				usuario.setPassword(rs.getString("password"));
				usuario.setEst(rs.getString("est"));
												
				return usuario;
			}

		});	
	}
	public List<PerfilOpcion> getListPerfilOpcion(Param param, String[] order) {
		String sql = "select * from seg_perfil_opcion " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<PerfilOpcion>() {

			
			public PerfilOpcion mapRow(ResultSet rs, int rowNum) throws SQLException {
				PerfilOpcion perfil_opcion = new PerfilOpcion();

				perfil_opcion.setId(rs.getInt("id"));
				perfil_opcion.setId_per(rs.getInt("id_per"));
				perfil_opcion.setId_opc(rs.getInt("id_opc"));
				perfil_opcion.setEst(rs.getString("est"));
												
				return perfil_opcion;
			}

		});	
	}


	// funciones privadas utilitarias para Perfil

	private Perfil rsToEntity(ResultSet rs,String alias) throws SQLException {
		Perfil perfil = new Perfil();

		perfil.setId(rs.getInt( alias + "id"));
		perfil.setNom(rs.getString( alias + "nom"));
		perfil.setDes(rs.getString( alias + "des"));
		perfil.setEst(rs.getString( alias + "est"));
		perfil.setDias_adi_login(rs.getInt(alias + "dias_adi_login"));						
		return perfil;

	}
	
}
