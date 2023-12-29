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
import com.tesla.colegio.model.UsuarioRol;

import com.tesla.colegio.model.Usuario;
import com.tesla.colegio.model.Persona;
import com.tesla.colegio.model.Rol;
import com.tesla.colegio.model.Trabajador;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface UsuarioRolDAO.
 * @author MV
 *
 */
public class UsuarioRolDAOImpl{
	final static Logger logger = Logger.getLogger(UsuarioRolDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(UsuarioRol usuario_rol) {
		if (usuario_rol.getId() != null) {
			// update
			String sql = "UPDATE seg_usuario_rol "
						+ "SET id_usr=?, "
						+ "id_rol=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						usuario_rol.getId_usr(),
						usuario_rol.getId_rol(),
						usuario_rol.getEst(),
						usuario_rol.getUsr_act(),
						new java.util.Date(),
						usuario_rol.getId()); 
			return usuario_rol.getId();

		} else {
			// insert
			String sql = "insert into seg_usuario_rol ("
						+ "id_usr, "
						+ "id_rol, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				usuario_rol.getId_usr(),
				usuario_rol.getId_rol(),
				usuario_rol.getEst(),
				usuario_rol.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from seg_usuario_rol where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<UsuarioRol> list() {
		String sql = "select * from seg_usuario_rol";
		
		//logger.info(sql);
		
		List<UsuarioRol> listUsuarioRol = jdbcTemplate.query(sql, new RowMapper<UsuarioRol>() {

			@Override
			public UsuarioRol mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listUsuarioRol;
	}

	public UsuarioRol get(int id) {
		String sql = "select * from seg_usuario_rol WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<UsuarioRol>() {

			@Override
			public UsuarioRol extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public UsuarioRol getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select uro.id uro_id, uro.id_usr uro_id_usr , uro.id_rol uro_id_rol  ,uro.est uro_est ";
		if (aTablas.contains("seg_usuario"))
			sql = sql + ", usr.id usr_id  , usr.id_per usr_id_per , usr.login usr_login , usr.password usr_password  ";
		if (aTablas.contains("seg_rol"))
			sql = sql + ", rol.id rol_id  , rol.nom rol_nom  ";
	
		sql = sql + " from seg_usuario_rol uro "; 
		if (aTablas.contains("seg_usuario"))
			sql = sql + " left join seg_usuario usr on usr.id = uro.id_usr ";
		if (aTablas.contains("seg_rol"))
			sql = sql + " left join seg_rol rol on rol.id = uro.id_rol ";
		sql = sql + " where uro.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<UsuarioRol>() {
		
			@Override
			public UsuarioRol extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					UsuarioRol usuariorol= rsToEntity(rs,"uro_");
					if (aTablas.contains("seg_usuario")){
						Usuario usuario = new Usuario();  
							usuario.setId(rs.getInt("usr_id")) ;  
							usuario.setId_per(rs.getInt("usr_id_per")) ;  
							usuario.setLogin(rs.getString("usr_login")) ;  
							usuario.setPassword(rs.getString("usr_password")) ;  
							usuariorol.setUsuario(usuario);
					}
					if (aTablas.contains("seg_rol")){
						Rol rol = new Rol();  
							rol.setId(rs.getInt("rol_id")) ;  
							rol.setNom(rs.getString("rol_nom")) ;  
							usuariorol.setRol(rol);
					}
							return usuariorol;
				}
				
				return null;
			}
			
		});


	}		
	
	public UsuarioRol getByParams(Param param) {

		String sql = "select * from seg_usuario_rol " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<UsuarioRol>() {
			@Override
			public UsuarioRol extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<UsuarioRol> listByParams(Param param, String[] order) {

		String sql = "select * from seg_usuario_rol " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<UsuarioRol>() {

			@Override
			public UsuarioRol mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<UsuarioRol> listFullByParams(UsuarioRol usuariorol, String[] order) {
	
		return listFullByParams(Param.toParam("uro",usuariorol), order);
	
	}	
	
	public List<UsuarioRol> listFullByParams(Param param, String[] order) {

		String sql = "select uro.id uro_id, uro.id_usr uro_id_usr , uro.id_rol uro_id_rol  ,uro.est uro_est ";
		sql = sql + ", usr.id usr_id  , usr.id_per usr_id_per , usr.login usr_login , usr.password usr_password  ";
		sql = sql + ", rol.id rol_id  , rol.nom rol_nom  ";
		sql = sql + ", tra.id tra_id , tra.nom tra_nom, tra.ape_pat tra_ape_pat, tra.ape_mat tra_ape_mat";
		sql = sql + ", tra.id per_id , per.nom per_nom, per.ape_pat per_ape_pat, per.ape_mat per_ape_mat";
		sql = sql + " from seg_usuario_rol uro";
		sql = sql + " inner join seg_usuario usr on usr.id = uro.id_usr ";
		sql = sql + " inner join seg_rol rol on rol.id = uro.id_rol ";
		sql = sql + " inner join ges_trabajador tra on tra.id=usr.id_tra";
		sql = sql + " inner join col_persona per on tra.id_per=per.id";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<UsuarioRol>() {

			@Override
			public UsuarioRol mapRow(ResultSet rs, int rowNum) throws SQLException {
				UsuarioRol usuariorol= rsToEntity(rs,"uro_");
				Usuario usuario = new Usuario();  
				usuario.setId(rs.getInt("usr_id")) ;  
				usuario.setId_per(rs.getInt("usr_id_per")) ;  
				usuario.setLogin(rs.getString("usr_login")) ;  
				usuario.setPassword(rs.getString("usr_password")) ;  
				usuariorol.setUsuario(usuario);
				Rol rol = new Rol();  
				rol.setId(rs.getInt("rol_id")) ;  
				rol.setNom(rs.getString("rol_nom")) ;  
				usuariorol.setRol(rol);
				Trabajador trabajador = new Trabajador();
				trabajador.setId(rs.getInt("tra_id"));
				trabajador.setNom(rs.getString("tra_nom")) ; 
				trabajador.setApe_pat(rs.getString("tra_ape_pat")) ; 
				trabajador.setApe_mat(rs.getString("tra_ape_mat")) ; 
				Persona persona = new Persona();
				persona.setId(rs.getInt("per_id"));
				persona.setNom(rs.getString("per_nom")) ; 
				persona.setApe_pat(rs.getString("per_ape_pat")) ; 
				persona.setApe_mat(rs.getString("per_ape_mat")) ; 
				usuariorol.setPersona(persona);
				return usuariorol;
			}

		});

	}	




	// funciones privadas utilitarias para UsuarioRol

	private UsuarioRol rsToEntity(ResultSet rs,String alias) throws SQLException {
		UsuarioRol usuario_rol = new UsuarioRol();

		usuario_rol.setId(rs.getInt( alias + "id"));
		usuario_rol.setId_usr(rs.getInt( alias + "id_usr"));
		usuario_rol.setId_rol(rs.getInt( alias + "id_rol"));
		usuario_rol.setEst(rs.getString( alias + "est"));
								
		return usuario_rol;

	}
	
}
