package com.sige.mat.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Arrays;

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
import com.tesla.colegio.model.UsuarioSucursal;

import com.tesla.colegio.model.Usuario;
import com.tesla.colegio.model.Sucursal;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface UsuarioSucursalDAO.
 * @author MV
 *
 */
public class UsuarioSucursalDAOImpl{
	final static Logger logger = Logger.getLogger(UsuarioSucursalDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(UsuarioSucursal usuario_sucursal) {
		if (usuario_sucursal.getId() != null) {
			// update
			String sql = "UPDATE seg_usuario_sucursal "
						+ "SET id_usr=?, "
						+ "id_suc=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			return jdbcTemplate.update(sql, 
						usuario_sucursal.getId_usr(),
						usuario_sucursal.getId_suc(),
						usuario_sucursal.getEst(),
						usuario_sucursal.getUsr_act(),
						new java.util.Date(),
						usuario_sucursal.getId()); 

		} else {
			// insert
			String sql = "insert into seg_usuario_sucursal ("
						+ "id_usr, "
						+ "id_suc, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				usuario_sucursal.getId_usr(),
				usuario_sucursal.getId_suc(),
				usuario_sucursal.getEst(),
				usuario_sucursal.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	
	public void delete(int id) {
		String sql = "delete from seg_usuario_sucursal where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	
	public List<UsuarioSucursal> list() {
		String sql = "select * from seg_usuario_sucursal";
		
		//logger.info(sql);
		
		List<UsuarioSucursal> listUsuarioSucursal = jdbcTemplate.query(sql, new RowMapper<UsuarioSucursal>() {

			
			public UsuarioSucursal mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listUsuarioSucursal;
	}

	
	public UsuarioSucursal get(int id) {
		String sql = "select * from seg_usuario_sucursal WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<UsuarioSucursal>() {

			
			public UsuarioSucursal extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	
	public UsuarioSucursal getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select usu_suc.id usu_suc_id, usu_suc.id_usr usu_suc_id_usr , usu_suc.id_suc usu_suc_id_suc  ,usu_suc.est usu_suc_est ";
		if (aTablas.contains("seg_usuario"))
			sql = sql + ", usr.id usr_id  , usr.id_per usr_id_per , usr.login usr_login , usr.password usr_password  ";
		if (aTablas.contains("ges_sucursal"))
			sql = sql + ", suc.id suc_id  , suc.nom suc_nom , suc.dir suc_dir , suc.tel suc_tel , suc.correo suc_correo  ";
	
		sql = sql + " from seg_usuario_sucursal usu_suc "; 
		if (aTablas.contains("seg_usuario"))
			sql = sql + " left join seg_usuario usr on usr.id = usu_suc.id_usr ";
		if (aTablas.contains("ges_sucursal"))
			sql = sql + " left join ges_sucursal suc on suc.id = usu_suc.id_suc ";
		sql = sql + " where usu_suc.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<UsuarioSucursal>() {
		
			
			public UsuarioSucursal extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					UsuarioSucursal usuariosucursal= rsToEntity(rs,"usu_suc_");
					if (aTablas.contains("seg_usuario")){
						Usuario usuario = new Usuario();  
							usuario.setId(rs.getInt("usr_id")) ;  
							usuario.setId_per(rs.getInt("usr_id_per")) ;  
							usuario.setLogin(rs.getString("usr_login")) ;  
							usuario.setPassword(rs.getString("usr_password")) ;  
							usuariosucursal.setUsuario(usuario);
					}
					if (aTablas.contains("ges_sucursal")){
						Sucursal sucursal = new Sucursal();  
							sucursal.setId(rs.getInt("suc_id")) ;  
							sucursal.setNom(rs.getString("suc_nom")) ;  
							sucursal.setDir(rs.getString("suc_dir")) ;  
							sucursal.setTel(rs.getString("suc_tel")) ;  
							sucursal.setCorreo(rs.getString("suc_correo")) ;  
							usuariosucursal.setSucursal(sucursal);
					}
							return usuariosucursal;
				}
				
				return null;
			}
			
		});


	}		
	
	
	public UsuarioSucursal getByParams(Param param) {

		String sql = "select * from seg_usuario_sucursal " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<UsuarioSucursal>() {
			
			public UsuarioSucursal extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	
	public List<UsuarioSucursal> listByParams(Param param, String[] order) {

		String sql = "select * from seg_usuario_sucursal " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<UsuarioSucursal>() {

			
			public UsuarioSucursal mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	
	public List<UsuarioSucursal> listFullByParams(UsuarioSucursal usuariosucursal, String[] order) {
	
		return listFullByParams(Param.toParam("usu_suc",usuariosucursal), order);
	
	}	
	
	
	public List<UsuarioSucursal> listFullByParams(Param param, String[] order) {

		String sql = "select usu_suc.id usu_suc_id, usu_suc.id_usr usu_suc_id_usr , usu_suc.id_suc usu_suc_id_suc  ,usu_suc.est usu_suc_est ";
		sql = sql + ", usr.id usr_id  , usr.id_per usr_id_per , usr.login usr_login , usr.password usr_password  ";
		sql = sql + ", suc.id suc_id  , suc.nom suc_nom , suc.dir suc_dir , suc.tel suc_tel , suc.correo suc_correo  ";
		sql = sql + " from seg_usuario_sucursal usu_suc";
		sql = sql + " left join seg_usuario usr on usr.id = usu_suc.id_usr ";
		sql = sql + " left join ges_sucursal suc on suc.id = usu_suc.id_suc ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<UsuarioSucursal>() {

			
			public UsuarioSucursal mapRow(ResultSet rs, int rowNum) throws SQLException {
				UsuarioSucursal usuariosucursal= rsToEntity(rs,"usu_suc_");
				Usuario usuario = new Usuario();  
				usuario.setId(rs.getInt("usr_id")) ;  
				usuario.setId_per(rs.getInt("usr_id_per")) ;  
				usuario.setLogin(rs.getString("usr_login")) ;  
				usuario.setPassword(rs.getString("usr_password")) ;  
				usuariosucursal.setUsuario(usuario);
				Sucursal sucursal = new Sucursal();  
				sucursal.setId(rs.getInt("suc_id")) ;  
				sucursal.setNom(rs.getString("suc_nom")) ;  
				sucursal.setDir(rs.getString("suc_dir")) ;  
				sucursal.setTel(rs.getString("suc_tel")) ;  
				sucursal.setCorreo(rs.getString("suc_correo")) ;  
				usuariosucursal.setSucursal(sucursal);
				return usuariosucursal;
			}

		});

	}	




	// funciones privadas utilitarias para UsuarioSucursal

	private UsuarioSucursal rsToEntity(ResultSet rs,String alias) throws SQLException {
		UsuarioSucursal usuario_sucursal = new UsuarioSucursal();

		usuario_sucursal.setId(rs.getInt( alias + "id"));
		usuario_sucursal.setId_usr(rs.getInt( alias + "id_usr"));
		usuario_sucursal.setId_suc(rs.getInt( alias + "id_suc"));
		usuario_sucursal.setEst(rs.getString( alias + "est"));
								
		return usuario_sucursal;

	}
	
}
