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
import com.tesla.colegio.model.DuracionHora;



import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface DuracionHoraDAO.
 * @author MV
 *
 */
public class DuracionHoraDAOImpl{
	final static Logger logger = Logger.getLogger(DuracionHoraDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(DuracionHora duracion_hora) {
		if (duracion_hora.getId() > 0) {
			// update
			String sql = "UPDATE asi_duracion_hora "
						+ "SET nom=?, "
						+ "des=?, "
						+ "val=?, "
						+ "est=?,usr_act=?,fec_act=? "
					+ "WHERE id=?";
			
			//logger.info(sql);

			return jdbcTemplate.update(sql, 
						duracion_hora.getNom(),
						duracion_hora.getDes(),
						duracion_hora.getVal(),
						duracion_hora.getEst(),
						duracion_hora.getUsr_act(),
						new java.util.Date(),
						duracion_hora.getId()); 

		} else {
			// insert
			String sql = "insert into asi_duracion_hora ("
						+ "nom, "
						+ "des, "
						+ "val, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				duracion_hora.getNom(),
				duracion_hora.getDes(),
				duracion_hora.getVal(),
				duracion_hora.getEst(),
				duracion_hora.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	
	public void delete(int id) {
		String sql = "delete from asi_duracion_hora where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	
	public List<DuracionHora> list() {
		String sql = "select * from asi_duracion_hora";
		
		//logger.info(sql);
		
		List<DuracionHora> listDuracionHora = jdbcTemplate.query(sql, new RowMapper<DuracionHora>() {

			
			public DuracionHora mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listDuracionHora;
	}

	
	public DuracionHora get(int id) {
		String sql = "select * from asi_duracion_hora WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<DuracionHora>() {

			
			public DuracionHora extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	
	public DuracionHora getFull(int id, String tablas[]) {
		String sql = "select dho.id dho_id, dho.nom dho_nom , dho.des dho_des , dho.val dho_val  ,dho.est dho_est ";
	
		sql = sql + " from asi_duracion_hora dho "; 
		sql = sql + " where dho.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<DuracionHora>() {
		
			
			public DuracionHora extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					DuracionHora duracionHora= rsToEntity(rs,"dho_");
							return duracionHora;
				}
				
				return null;
			}
			
		});


	}		
	
	
	public DuracionHora getByParams(Param param) {

		String sql = "select * from asi_duracion_hora " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<DuracionHora>() {
			
			public DuracionHora extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	
	public List<DuracionHora> listByParams(Param param, String[] order) {

		String sql = "select * from asi_duracion_hora " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<DuracionHora>() {

			
			public DuracionHora mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	
	public List<DuracionHora> listFullByParams(DuracionHora duracionHora, String[] order) {
	
		return listFullByParams(Param.toParam("dho",duracionHora), order);
	
	}	
	
	
	public List<DuracionHora> listFullByParams(Param param, String[] order) {

		String sql = "select dho.id dho_id, dho.nom dho_nom , dho.des dho_des , dho.val dho_val  ,dho.est dho_est ";
		sql = sql + " from asi_duracion_hora dho";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<DuracionHora>() {

			
			public DuracionHora mapRow(ResultSet rs, int rowNum) throws SQLException {
				DuracionHora duracionHora= rsToEntity(rs,"dho_");
				return duracionHora;
			}

		});

	}	




	// funciones privadas utilitarias para DuracionHora

	private DuracionHora rsToEntity(ResultSet rs,String alias) throws SQLException {
		DuracionHora duracion_hora = new DuracionHora();

		duracion_hora.setId(rs.getInt( alias + "id"));
		duracion_hora.setNom(rs.getString( alias + "nom"));
		duracion_hora.setDes(rs.getString( alias + "des"));
		duracion_hora.setVal(rs.getInt( alias + "val"));
		duracion_hora.setEst(rs.getString( alias + "est"));
								
		return duracion_hora;

	}
	
}
