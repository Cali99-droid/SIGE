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
import com.tesla.colegio.model.TurServicio;

import com.tesla.colegio.model.Turno;
import com.tesla.colegio.model.Servicio;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface TurServicioDAO.
 * @author MV
 *
 */
public class TurServicioDAOImpl{
	final static Logger logger = Logger.getLogger(TurServicioDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(TurServicio tur_servicio) {
		if (tur_servicio.getId() != null) {
			// update
			String sql = "UPDATE col_tur_servicio "
						+ "SET id_tur=?, "
						+ "id_srv=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			return jdbcTemplate.update(sql, 
						tur_servicio.getId_tur(),
						tur_servicio.getId_srv(),
						tur_servicio.getEst(),
						tur_servicio.getUsr_act(),
						new java.util.Date(),
						tur_servicio.getId()); 

		} else {
			// insert
			String sql = "insert into col_tur_servicio ("
						+ "id_tur, "
						+ "id_srv, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				tur_servicio.getId_tur(),
				tur_servicio.getId_srv(),
				tur_servicio.getEst(),
				tur_servicio.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	
	public void delete(int id) {
		String sql = "delete from col_tur_servicio where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	
	public List<TurServicio> list() {
		String sql = "select * from col_tur_servicio";
		
		//logger.info(sql);
		
		List<TurServicio> listTurServicio = jdbcTemplate.query(sql, new RowMapper<TurServicio>() {

			
			public TurServicio mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listTurServicio;
	}

	
	public TurServicio get(int id) {
		String sql = "select * from col_tur_servicio WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<TurServicio>() {

			
			public TurServicio extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	
	public TurServicio getFull(int id, String tablas[]) {
		final  List<String> aTablas = Arrays.asList(tablas);
		String sql = "select tur_servicio.id tur_servicio_id, tur_servicio.id_tur tur_servicio_id_tur , tur_servicio.id_srv tur_servicio_id_srv  ,tur_servicio.est tur_servicio_est ";
		if (aTablas.contains("col_turno"))
			sql = sql + ", turno.id turno_id  , turno.nom turno_nom , turno.cod turno_cod  ";
		if (aTablas.contains("ges_servicio"))
			sql = sql + ", srv.id srv_id  , srv.id_suc srv_id_suc , srv.id_srv srv_id_srv , srv.nom srv_nom  ";
	
		sql = sql + " from col_tur_servicio tur_servicio "; 
		if (aTablas.contains("col_turno"))
			sql = sql + " left join col_turno turno on turno.id = tur_servicio.id_tur ";
		if (aTablas.contains("ges_servicio"))
			sql = sql + " left join ges_servicio srv on srv.id = tur_servicio.id_srv ";
		sql = sql + " where tur_servicio.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<TurServicio>() {
		
			
			public TurServicio extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					TurServicio turservicio= rsToEntity(rs,"tur_servicio_");
					if (aTablas.contains("col_turno")){
						Turno turno = new Turno();  
							turno.setId(rs.getInt("turno_id")) ;  
							turno.setNom(rs.getString("turno_nom")) ;  
							turno.setCod(rs.getString("turno_cod")) ;  
							turservicio.setTurno(turno);
					}
					if (aTablas.contains("ges_servicio")){
						Servicio servicio = new Servicio();  
							servicio.setId(rs.getInt("srv_id")) ;  
							servicio.setId_suc(rs.getInt("srv_id_suc")) ;  
							//servicio.setId_srv(rs.getInt("srv_id_srv")) ;  
							servicio.setNom(rs.getString("srv_nom")) ;  
							turservicio.setServicio(servicio);
					}
							return turservicio;
				}
				
				return null;
			}
			
		});


	}		
	
	
	public TurServicio getByParams(Param param) {

		String sql = "select * from col_tur_servicio " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<TurServicio>() {
			
			public TurServicio extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	
	public List<TurServicio> listByParams(Param param, String[] order) {

		String sql = "select * from col_tur_servicio " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<TurServicio>() {

			
			public TurServicio mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	
	public List<TurServicio> listFullByParams(TurServicio turservicio, String[] order) {
	
		return listFullByParams(Param.toParam("tur_servicio",turservicio), order);
	
	}	
	
	
	public List<TurServicio> listFullByParams(Param param, String[] order) {

		String sql = "select tur_servicio.id tur_servicio_id, tur_servicio.id_tur tur_servicio_id_tur , tur_servicio.id_srv tur_servicio_id_srv  ,tur_servicio.est tur_servicio_est ";
		sql = sql + ", turno.id turno_id  , turno.nom turno_nom , turno.cod turno_cod  ";
		sql = sql + ", srv.id srv_id  , srv.id_suc srv_id_suc , srv.id_srv srv_id_srv , srv.nom srv_nom  ";
		sql = sql + " from col_tur_servicio tur_servicio";
		sql = sql + " left join col_turno turno on turno.id = tur_servicio.id_tur ";
		sql = sql + " left join ges_servicio srv on srv.id = tur_servicio.id_srv ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<TurServicio>() {

			
			public TurServicio mapRow(ResultSet rs, int rowNum) throws SQLException {
				TurServicio turservicio= rsToEntity(rs,"tur_servicio_");
				Turno turno = new Turno();  
				turno.setId(rs.getInt("turno_id")) ;  
				turno.setNom(rs.getString("turno_nom")) ;  
				turno.setCod(rs.getString("turno_cod")) ;  
				turservicio.setTurno(turno);
				Servicio servicio = new Servicio();  
				servicio.setId(rs.getInt("srv_id")) ;  
				servicio.setId_suc(rs.getInt("srv_id_suc")) ;  
				//servicio.setId_srv(rs.getInt("srv_id_srv")) ;  
				servicio.setNom(rs.getString("srv_nom")) ;  
				turservicio.setServicio(servicio);
				return turservicio;
			}

		});

	}	




	// funciones privadas utilitarias para TurServicio

	private TurServicio rsToEntity(ResultSet rs,String alias) throws SQLException {
		TurServicio tur_servicio = new TurServicio();

		tur_servicio.setId(rs.getInt( alias + "id"));
		tur_servicio.setId_tur(rs.getInt( alias + "id_tur"));
		tur_servicio.setId_srv(rs.getInt( alias + "id_srv"));
		tur_servicio.setEst(rs.getString( alias + "est"));
								
		return tur_servicio;

	}
	
}
