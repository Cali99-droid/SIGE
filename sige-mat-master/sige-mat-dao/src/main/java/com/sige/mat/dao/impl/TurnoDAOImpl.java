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
import com.tesla.colegio.model.Turno;

import com.tesla.colegio.model.TurServicio;
import com.tesla.colegio.model.Aula;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface TurnoDAO.
 * @author MV
 *
 */
public class TurnoDAOImpl{
	final static Logger logger = Logger.getLogger(TurnoDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(Turno turno) {
		if (turno.getId() != null) {
			// update
			String sql = "UPDATE col_turno "
						+ "SET nom=?, "
						+ "cod=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			return jdbcTemplate.update(sql, 
						turno.getNom(),
						turno.getCod(),
						turno.getEst(),
						turno.getUsr_act(),
						new java.util.Date(),
						turno.getId()); 

		} else {
			// insert
			String sql = "insert into col_turno ("
						+ "nom, "
						+ "cod, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				turno.getNom(),
				turno.getCod(),
				turno.getEst(),
				turno.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	
	public void delete(int id) {
		String sql = "delete from col_turno where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	
	public List<Turno> list() {
		String sql = "select * from col_turno";
		
		//logger.info(sql);
		
		List<Turno> listTurno = jdbcTemplate.query(sql, new RowMapper<Turno>() {

			
			public Turno mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listTurno;
	}

	
	public Turno get(int id) {
		String sql = "select * from col_turno WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Turno>() {

			
			public Turno extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	
	public Turno getFull(int id, String tablas[]) {
		String sql = "select turno.id turno_id, turno.nom turno_nom , turno.cod turno_cod  ,turno.est turno_est ";
	
		sql = sql + " from col_turno turno "; 
		sql = sql + " where turno.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<Turno>() {
		
			
			public Turno extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Turno turno= rsToEntity(rs,"turno_");
							return turno;
				}
				
				return null;
			}
			
		});


	}		
	
	
	public Turno getByParams(Param param) {

		String sql = "select * from col_turno " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Turno>() {
			
			public Turno extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	
	public List<Turno> listByParams(Param param, String[] order) {

		String sql = "select * from col_turno " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Turno>() {

			
			public Turno mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	
	public List<Turno> listFullByParams(Turno turno, String[] order) {
	
		return listFullByParams(Param.toParam("turno",turno), order);
	
	}	
	
	
	public List<Turno> listFullByParams(Param param, String[] order) {

		String sql = "select turno.id turno_id, turno.nom turno_nom , turno.cod turno_cod  ,turno.est turno_est ";
		sql = sql + " from col_turno turno";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Turno>() {

			
			public Turno mapRow(ResultSet rs, int rowNum) throws SQLException {
				Turno turno= rsToEntity(rs,"turno_");
				return turno;
			}

		});

	}	


	public List<TurServicio> getListTurServicio(Param param, String[] order) {
		String sql = "select * from col_tur_servicio " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<TurServicio>() {

			
			public TurServicio mapRow(ResultSet rs, int rowNum) throws SQLException {
				TurServicio tur_servicio = new TurServicio();

				tur_servicio.setId(rs.getInt("id"));
				tur_servicio.setId_tur(rs.getInt("id_tur"));
				tur_servicio.setId_srv(rs.getInt("id_srv"));
				tur_servicio.setEst(rs.getString("est"));
												
				return tur_servicio;
			}

		});	
	}
	public List<Aula> getListAula(Param param, String[] order) {
		String sql = "select * from col_aula " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<Aula>() {

			
			public Aula mapRow(ResultSet rs, int rowNum) throws SQLException {
				Aula aula = new Aula();

				aula.setId(rs.getInt("id"));
				aula.setId_per(rs.getInt("id_per"));
				aula.setId_grad(rs.getInt("id_grad"));
				aula.setId_secc_ant(rs.getInt("id_secc_ant"));
				aula.setId_tur(rs.getInt("id_tur"));
				aula.setSecc(rs.getString("secc"));
				aula.setCap(rs.getInt("cap"));
				aula.setEst(rs.getString("est"));
												
				return aula;
			}

		});	
	}


	// funciones privadas utilitarias para Turno

	private Turno rsToEntity(ResultSet rs,String alias) throws SQLException {
		Turno turno = new Turno();

		turno.setId(rs.getInt( alias + "id"));
		turno.setNom(rs.getString( alias + "nom"));
		turno.setCod(rs.getString( alias + "cod"));
		turno.setEst(rs.getString( alias + "est"));
								
		return turno;

	}
	
}
