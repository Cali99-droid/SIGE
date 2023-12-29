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
import com.tesla.colegio.model.UsuarioToken;

import com.tesla.colegio.model.Usuario;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface UsuarioTokenDAO.
 * @author MV
 *
 */
public class UsuarioTokenDAOImpl{
	final static Logger logger = Logger.getLogger(UsuarioTokenDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(UsuarioToken usuario_token) {
		if (usuario_token.getId() != null) {
			// update
			String sql = "UPDATE seg_usuario_token "
						+ "SET token=?, "
						+ "id_usr=?, "
						+ "id_fam=?, "
						+ "id_per=?, "
						+ "fecha=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						usuario_token.getToken(),
						usuario_token.getId_usr(),
						usuario_token.getId_fam(),
						usuario_token.getId_per(),
						usuario_token.getFecha(),
						usuario_token.getEst(),
						usuario_token.getUsr_act(),
						new java.util.Date(),
						usuario_token.getId()); 
			return usuario_token.getId();

		} else {
			// insert
			String sql = "insert into seg_usuario_token ("
						+ "token, "
						+ "id_usr, id_fam, id_per,"
						+ "fecha, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				usuario_token.getToken(),
				usuario_token.getId_usr(),
				usuario_token.getId_fam(),
				usuario_token.getId_per(),
				usuario_token.getFecha(),
				usuario_token.getEst(),
				usuario_token.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from seg_usuario_token where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}
	
	public void deleteUsuario(int id_usr) {
		String sql = "delete from seg_usuario_token where id_usr=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id_usr);
	}

	public List<UsuarioToken> list() {
		String sql = "select * from seg_usuario_token";
		
		//logger.info(sql);
		
		List<UsuarioToken> listUsuarioToken = jdbcTemplate.query(sql, new RowMapper<UsuarioToken>() {

			@Override
			public UsuarioToken mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listUsuarioToken;
	}

	public UsuarioToken get(int id) {
		String sql = "select * from seg_usuario_token WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<UsuarioToken>() {

			@Override
			public UsuarioToken extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public UsuarioToken getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select $table.alias.id $table.alias_id, $table.alias.token $table.alias_token , $table.alias.id_usr $table.alias_id_usr , $table.alias.fecha $table.alias_fecha  ,$table.alias.est $table.alias_est ";
		if (aTablas.contains("seg_usuario"))
			sql = sql + ", usr.id usr_id  , usr.id_per usr_id_per , usr.id_tra usr_id_tra , usr.login usr_login , usr.password usr_password , usr.id_suc usr_id_suc , usr.ini usr_ini  ";
	
		sql = sql + " from seg_usuario_token $table.alias "; 
		if (aTablas.contains("seg_usuario"))
			sql = sql + " left join seg_usuario usr on usr.id = $table.alias.id_usr ";
		sql = sql + " where $table.alias.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<UsuarioToken>() {
		
			@Override
			public UsuarioToken extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					UsuarioToken usuariotoken= rsToEntity(rs,"$table.alias_");
					if (aTablas.contains("seg_usuario")){
						Usuario usuario = new Usuario();  
							usuario.setId(rs.getInt("usr_id")) ;  
							usuario.setId_per(rs.getInt("usr_id_per")) ;  
							usuario.setId_tra(rs.getInt("usr_id_tra")) ;  
							usuario.setLogin(rs.getString("usr_login")) ;  
							usuario.setPassword(rs.getString("usr_password")) ;  
							usuario.setId_suc(rs.getInt("usr_id_suc")) ;  
							usuario.setIni(rs.getString("usr_ini")) ;  
							usuariotoken.setUsuario(usuario);
					}
							return usuariotoken;
				}
				
				return null;
			}
			
		});


	}		
	
	public UsuarioToken getByParams(Param param) {

		String sql = "select * from seg_usuario_token " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<UsuarioToken>() {
			@Override
			public UsuarioToken extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<UsuarioToken> listByParams(Param param, String[] order) {

		String sql = "select * from seg_usuario_token " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<UsuarioToken>() {

			@Override
			public UsuarioToken mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<UsuarioToken> listFullByParams(UsuarioToken usuariotoken, String[] order) {
	
		return listFullByParams(Param.toParam("$table.alias",usuariotoken), order);
	
	}	
	
	public List<UsuarioToken> listFullByParams(Param param, String[] order) {

		String sql = "select $table.alias.id $table.alias_id, $table.alias.token $table.alias_token , $table.alias.id_usr $table.alias_id_usr , $table.alias.fecha $table.alias_fecha  ,$table.alias.est $table.alias_est ";
		sql = sql + ", usr.id usr_id  , usr.id_per usr_id_per , usr.id_tra usr_id_tra , usr.login usr_login , usr.password usr_password , usr.id_suc usr_id_suc , usr.ini usr_ini  ";
		sql = sql + " from seg_usuario_token $table.alias";
		sql = sql + " left join seg_usuario usr on usr.id = $table.alias.id_usr ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<UsuarioToken>() {

			@Override
			public UsuarioToken mapRow(ResultSet rs, int rowNum) throws SQLException {
				UsuarioToken usuariotoken= rsToEntity(rs,"$table.alias_");
				Usuario usuario = new Usuario();  
				usuario.setId(rs.getInt("usr_id")) ;  
				usuario.setId_per(rs.getInt("usr_id_per")) ;  
				usuario.setId_tra(rs.getInt("usr_id_tra")) ;  
				usuario.setLogin(rs.getString("usr_login")) ;  
				usuario.setPassword(rs.getString("usr_password")) ;  
				usuario.setId_suc(rs.getInt("usr_id_suc")) ;  
				usuario.setIni(rs.getString("usr_ini")) ;  
				usuariotoken.setUsuario(usuario);
				return usuariotoken;
			}

		});

	}	




	// funciones privadas utilitarias para UsuarioToken

	private UsuarioToken rsToEntity(ResultSet rs,String alias) throws SQLException {
		UsuarioToken usuario_token = new UsuarioToken();

		usuario_token.setId(rs.getInt( alias + "id"));
		usuario_token.setToken(rs.getString( alias + "token"));
		usuario_token.setId_usr(rs.getInt( alias + "id_usr"));
		usuario_token.setFecha(rs.getDate( alias + "fecha"));
		usuario_token.setEst(rs.getString( alias + "est"));
								
		return usuario_token;

	}
	
}
