package com.sige.mat.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Arrays;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import com.tesla.colegio.model.UsuarioNivel;

import com.tesla.colegio.model.Usuario;
import com.tesla.colegio.model.Nivel;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface UsuarioNivelDAO.
 * @author MV
 *
 */
public class UsuarioNivelDAOImpl{
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(UsuarioNivel usuario_nivel) {
		if (usuario_nivel.getId() != null) {
			// update
			String sql = "UPDATE seg_usuario_nivel "
						+ "SET id_usr=?, "
						+ "id_niv=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			

			jdbcTemplate.update(sql, 
						usuario_nivel.getId_usr(),
						usuario_nivel.getId_niv(),
						usuario_nivel.getEst(),
						usuario_nivel.getUsr_act(),
						new java.util.Date(),
						usuario_nivel.getId()); 
			return usuario_nivel.getId();

		} else {
			// insert
			String sql = "insert into seg_usuario_nivel ("
						+ "id_usr, "
						+ "id_niv, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?)";

				

				jdbcTemplate.update(sql, 
				usuario_nivel.getId_usr(),
				usuario_nivel.getId_niv(),
				usuario_nivel.getEst(),
				usuario_nivel.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id_usr) {
		String sql = "delete from seg_usuario_nivel where id_usr=?";
		
		
		
		jdbcTemplate.update(sql, id_usr);
	}

	public List<UsuarioNivel> list() {
		String sql = "select * from seg_usuario_nivel";
		
		
		
		List<UsuarioNivel> listUsuarioNivel = jdbcTemplate.query(sql, new RowMapper<UsuarioNivel>() {

			@Override
			public UsuarioNivel mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listUsuarioNivel;
	}

	public UsuarioNivel get(int id) {
		String sql = "select * from seg_usuario_nivel WHERE id=" + id;
		
		
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<UsuarioNivel>() {

			@Override
			public UsuarioNivel extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public UsuarioNivel getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select sun.id sun_id, sun.id_usr sun_id_usr , sun.id_niv sun_id_niv  ,sun.est sun_est ";
		if (aTablas.contains("seg_usuario"))
			sql = sql + ", usr.id usr_id  , usr.id_per usr_id_per , usr.id_tra usr_id_tra , usr.login usr_login , usr.password usr_password , usr.id_suc usr_id_suc , usr.ini usr_ini  ";
		if (aTablas.contains("cat_nivel"))
			sql = sql + ", niv.id niv_id  , niv.cod_mod niv_cod_mod , niv.nom niv_nom  ";
	
		sql = sql + " from seg_usuario_nivel sun "; 
		if (aTablas.contains("seg_usuario"))
			sql = sql + " left join seg_usuario usr on usr.id = sun.id_usr ";
		if (aTablas.contains("cat_nivel"))
			sql = sql + " left join cat_nivel niv on niv.id = sun.id_niv ";
		sql = sql + " where sun.id= " + id; 
				
		

		return jdbcTemplate.query(sql, new ResultSetExtractor<UsuarioNivel>() {
		
			@Override
			public UsuarioNivel extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					UsuarioNivel usuarionivel= rsToEntity(rs,"sun_");
					if (aTablas.contains("seg_usuario")){
						Usuario usuario = new Usuario();  
							usuario.setId(rs.getInt("usr_id")) ;  
							usuario.setId_per(rs.getInt("usr_id_per")) ;  
							usuario.setId_tra(rs.getInt("usr_id_tra")) ;  
							usuario.setLogin(rs.getString("usr_login")) ;  
							usuario.setPassword(rs.getString("usr_password")) ;  
							usuario.setId_suc(rs.getInt("usr_id_suc")) ;  
							//usuario.setIni(rs.getInt("usr_ini")) ;  
							usuarionivel.setUsuario(usuario);
					}
					if (aTablas.contains("cat_nivel")){
						Nivel nivel = new Nivel();  
							nivel.setId(rs.getInt("niv_id")) ;  
							nivel.setCod_mod(rs.getString("niv_cod_mod")) ;  
							nivel.setNom(rs.getString("niv_nom")) ;  
							usuarionivel.setNivel(nivel);
					}
							return usuarionivel;
				}
				
				return null;
			}
			
		});


	}		
	
	public UsuarioNivel getByParams(Param param) {

		String sql = "select * from seg_usuario_nivel " + SQLFrmkUtil.getWhere(param);
		
		
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<UsuarioNivel>() {
			@Override
			public UsuarioNivel extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<UsuarioNivel> listByParams(Param param, String[] order) {

		String sql = "select * from seg_usuario_nivel " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		

		return jdbcTemplate.query(sql, new RowMapper<UsuarioNivel>() {

			@Override
			public UsuarioNivel mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<UsuarioNivel> listFullByParams(UsuarioNivel usuarionivel, String[] order) {
	
		return listFullByParams(Param.toParam("sun",usuarionivel), order);
	
	}	
	
	public List<UsuarioNivel> listFullByParams(Param param, String[] order) {

		String sql = "select sun.id sun_id, sun.id_usr sun_id_usr , sun.id_niv sun_id_niv  ,sun.est sun_est ";
		sql = sql + ", usr.id usr_id  , usr.id_per usr_id_per , usr.id_tra usr_id_tra , usr.login usr_login , usr.password usr_password , usr.id_suc usr_id_suc , usr.ini usr_ini  ";
		sql = sql + ", niv.id niv_id  , niv.cod_mod niv_cod_mod , niv.nom niv_nom  ";
		sql = sql + " from seg_usuario_nivel sun";
		sql = sql + " left join seg_usuario usr on usr.id = sun.id_usr ";
		sql = sql + " left join cat_nivel niv on niv.id = sun.id_niv ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		

		return jdbcTemplate.query(sql, new RowMapper<UsuarioNivel>() {

			@Override
			public UsuarioNivel mapRow(ResultSet rs, int rowNum) throws SQLException {
				UsuarioNivel usuarionivel= rsToEntity(rs,"sun_");
				Usuario usuario = new Usuario();  
				usuario.setId(rs.getInt("usr_id")) ;  
				usuario.setId_per(rs.getInt("usr_id_per")) ;  
				usuario.setId_tra(rs.getInt("usr_id_tra")) ;  
				usuario.setLogin(rs.getString("usr_login")) ;  
				usuario.setPassword(rs.getString("usr_password")) ;  
				usuario.setId_suc(rs.getInt("usr_id_suc")) ;  
				//usuario.setIni(rs.getInt("usr_ini")) ;  
				usuarionivel.setUsuario(usuario);
				Nivel nivel = new Nivel();  
				nivel.setId(rs.getInt("niv_id")) ;  
				nivel.setCod_mod(rs.getString("niv_cod_mod")) ;  
				nivel.setNom(rs.getString("niv_nom")) ;  
				usuarionivel.setNivel(nivel);
				return usuarionivel;
			}

		});

	}	




	// funciones privadas utilitarias para UsuarioNivel

	protected UsuarioNivel rsToEntity(ResultSet rs,String alias) throws SQLException {
		UsuarioNivel usuario_nivel = new UsuarioNivel();

		usuario_nivel.setId(rs.getInt( alias + "id"));
		usuario_nivel.setId_usr(rs.getInt( alias + "id_usr"));
		usuario_nivel.setId_niv(rs.getInt( alias + "id_niv"));
		usuario_nivel.setEst(rs.getString( alias + "est"));
								
		return usuario_nivel;

	}
	
}
