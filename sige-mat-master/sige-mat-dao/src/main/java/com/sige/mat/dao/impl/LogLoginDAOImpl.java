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
import com.tesla.colegio.model.LogLogin;

import com.tesla.colegio.model.Perfil;

import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface LogLoginDAO.
 * @author MV
 *
 */
public class LogLoginDAOImpl{
	final static Logger logger = Logger.getLogger(LogLoginDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(LogLogin log_login) {
		if (log_login.getId() != null) {
			// update
			String sql = "UPDATE seg_log_login "
						+ "SET id_usr=?, "
						+ "id_per=?, "
						+ "ip=?, "
						+ "exito=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						log_login.getId_usr(),
						log_login.getId_per(),
						log_login.getIp(),
						log_login.getExito(),
						log_login.getEst(),
						log_login.getUsr_act(),
						new java.util.Date(),
						log_login.getId()); 
			return log_login.getId();

		} else {
			// insert
			String sql = "insert into seg_log_login ("
						+ "id_usr, "
						+ "id_per, "
						+ "ip, "
						+ "exito, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				log_login.getId_usr(),
				log_login.getId_per(),
				log_login.getIp(),
				log_login.getExito(),
				log_login.getEst(),
				log_login.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from seg_log_login where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<LogLogin> list() {
		String sql = "select * from seg_log_login";
		
		//logger.info(sql);
		
		List<LogLogin> listLogLogin = jdbcTemplate.query(sql, new RowMapper<LogLogin>() {

			@Override
			public LogLogin mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listLogLogin;
	}

	public LogLogin get(int id) {
		String sql = "select * from seg_log_login WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<LogLogin>() {

			@Override
			public LogLogin extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public LogLogin getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select llo.id llo_id, llo.id_usr llo_id_usr , llo.id_per llo_id_per , llo.ip llo_ip , llo.exito llo_exito  ,llo.est llo_est ";
		if (aTablas.contains("seg_perfil"))
			sql = sql + ", per.id per_id  , per.nom per_nom , per.des per_des  ";
	
		sql = sql + " from seg_log_login llo "; 
		if (aTablas.contains("seg_perfil"))
			sql = sql + " left join seg_perfil per on per.id = llo.id_per ";
		sql = sql + " where llo.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<LogLogin>() {
		
			@Override
			public LogLogin extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					LogLogin loglogin= rsToEntity(rs,"llo_");
					if (aTablas.contains("seg_perfil")){
						Perfil perfil = new Perfil();  
							perfil.setId(rs.getInt("per_id")) ;  
							perfil.setNom(rs.getString("per_nom")) ;  
							perfil.setDes(rs.getString("per_des")) ;  
							loglogin.setPerfil(perfil);
					}
							return loglogin;
				}
				
				return null;
			}
			
		});


	}		
	
	public LogLogin getByParams(Param param) {

		String sql = "select * from seg_log_login " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<LogLogin>() {
			@Override
			public LogLogin extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<LogLogin> listByParams(Param param, String[] order) {

		String sql = "select * from seg_log_login " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<LogLogin>() {

			@Override
			public LogLogin mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<LogLogin> listFullByParams(LogLogin loglogin, String[] order) {
	
		return listFullByParams(Param.toParam("llo",loglogin), order);
	
	}	
	
	public List<LogLogin> listFullByParams(Param param, String[] order) {

		String sql = "select llo.id llo_id, llo.id_usr llo_id_usr , llo.id_per llo_id_per , llo.ip llo_ip , llo.exito llo_exito  ,llo.est llo_est ";
		sql = sql + ", per.id per_id  , per.nom per_nom , per.des per_des  ";
		sql = sql + " from seg_log_login llo";
		sql = sql + " left join seg_perfil per on per.id = llo.id_per ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<LogLogin>() {

			@Override
			public LogLogin mapRow(ResultSet rs, int rowNum) throws SQLException {
				LogLogin loglogin= rsToEntity(rs,"llo_");
				Perfil perfil = new Perfil();  
				perfil.setId(rs.getInt("per_id")) ;
				perfil.setNom(rs.getString("per_nom")) ;
				perfil.setDes(rs.getString("per_des")) ;
				loglogin.setPerfil(perfil);
				return loglogin;
			}

		});

	}	



	// funciones privadas utilitarias para LogLogin

	private LogLogin rsToEntity(ResultSet rs,String alias) throws SQLException {
		LogLogin log_login = new LogLogin();

		log_login.setId(rs.getInt( alias + "id"));
		log_login.setId_usr(rs.getInt( alias + "id_usr"));
		log_login.setId_per(rs.getInt( alias + "id_per"));
		log_login.setIp(rs.getString( alias + "ip"));
		log_login.setExito(rs.getString( alias + "exito"));
		log_login.setEst(rs.getString( alias + "est"));
								
		return log_login;

	}
	
}
