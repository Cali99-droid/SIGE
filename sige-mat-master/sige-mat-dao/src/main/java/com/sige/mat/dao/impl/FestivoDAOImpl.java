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
import com.tesla.colegio.model.Festivo;



import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface FestivoDAO.
 * @author MV
 *
 */
public class FestivoDAOImpl{
	final static Logger logger = Logger.getLogger(FestivoDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(Festivo festivo) {
		if (festivo.getId() != null) {
			// update
			String sql = "UPDATE aca_festivo "
						+ "SET dia=?, "
						+ "motivo=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						festivo.getDia(),
						festivo.getMotivo(),
						festivo.getEst(),
						festivo.getUsr_act(),
						new java.util.Date(),
						festivo.getId()); 
			return festivo.getId();

		} else {
			// insert
			String sql = "insert into aca_festivo ("
						+ "dia, "
						+ "motivo, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				festivo.getDia(),
				festivo.getMotivo(),
				festivo.getEst(),
				festivo.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from aca_festivo where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<Festivo> list() {
		String sql = "select * from aca_festivo";
		
		//logger.info(sql);
		
		List<Festivo> listFestivo = jdbcTemplate.query(sql, new RowMapper<Festivo>() {

			@Override
			public Festivo mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listFestivo;
	}

	public Festivo get(int id) {
		String sql = "select * from aca_festivo WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Festivo>() {

			@Override
			public Festivo extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public Festivo getFull(int id, String tablas[]) {
		String sql = "select afe.id afe_id, afe.dia afe_dia , afe.motivo afe_motivo  ,afe.est afe_est ";
	
		sql = sql + " from aca_festivo afe "; 
		sql = sql + " where afe.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<Festivo>() {
		
			@Override
			public Festivo extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Festivo festivo= rsToEntity(rs,"afe_");
							return festivo;
				}
				
				return null;
			}
			
		});


	}		
	
	public Festivo getByParams(Param param) {

		String sql = "select * from aca_festivo " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Festivo>() {
			@Override
			public Festivo extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<Festivo> listByParams(Param param, String[] order) {

		String sql = "select * from aca_festivo " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Festivo>() {

			@Override
			public Festivo mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<Festivo> listFullByParams(Festivo festivo, String[] order) {
	
		return listFullByParams(Param.toParam("afe",festivo), order);
	
	}	
	
	public List<Festivo> listFullByParams(Param param, String[] order) {

		String sql = "select afe.id afe_id, afe.dia afe_dia , afe.motivo afe_motivo  ,afe.est afe_est ";
		sql = sql + " from aca_festivo afe";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Festivo>() {

			@Override
			public Festivo mapRow(ResultSet rs, int rowNum) throws SQLException {
				Festivo festivo= rsToEntity(rs,"afe_");
				return festivo;
			}

		});

	}	




	// funciones privadas utilitarias para Festivo

	private Festivo rsToEntity(ResultSet rs,String alias) throws SQLException {
		Festivo festivo = new Festivo();

		festivo.setId(rs.getInt( alias + "id"));
		festivo.setDia(rs.getDate( alias + "dia"));
		festivo.setMotivo(rs.getString( alias + "motivo"));
		festivo.setEst(rs.getString( alias + "est"));
								
		return festivo;

	}
	
}
