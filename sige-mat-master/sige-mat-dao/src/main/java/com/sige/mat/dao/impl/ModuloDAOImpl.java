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
import com.tesla.colegio.model.Modulo;

import com.tesla.colegio.model.Parametro;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci&oacute;n de la interface ModuloDAO.
 * @author MV
 *
 */
public class ModuloDAOImpl{
	final static Logger logger = Logger.getLogger(ModuloDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(Modulo modulo) {
		if (modulo.getId() !=null ) {
			// update
			String sql = "UPDATE mod_modulo "
						+ "SET cod=?, "
						+ "nom=?, "
						+ "des=?, "
						+ "est=?,usr_act=?,fec_act=? "
					+ "WHERE id=?";
			
			//logger.info(sql);

			return jdbcTemplate.update(sql, 
						modulo.getCod(),
						modulo.getNom(),
						modulo.getDes(),
						modulo.getEst(),
						modulo.getUsr_act(),
						new java.util.Date(),
						modulo.getId()); 

		} else {
			// insert
			String sql = "insert into mod_modulo ("
						+ "cod, "
						+ "nom, "
						+ "des, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				modulo.getCod(),
				modulo.getNom(),
				modulo.getDes(),
				modulo.getEst(),
				modulo.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	
	public void delete(int id) {
		String sql = "delete from mod_modulo where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	
	public List<Modulo> list() {
		String sql = "select * from mod_modulo";
		
		//logger.info(sql);
		
		List<Modulo> listModulo = jdbcTemplate.query(sql, new RowMapper<Modulo>() {

			
			public Modulo mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listModulo;
	}

	
	public Modulo get(int id) {
		String sql = "select * from mod_modulo WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Modulo>() {

			
			public Modulo extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	
	public Modulo getFull(int id, String tablas[]) {
		String sql = "select mod.id mod_id, mod.cod mod_cod , mod.nom mod_nom , mod.des mod_des  ,mod.est mod_est ";
	
		sql = sql + " from mod_modulo mod "; 
		sql = sql + " where mod.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<Modulo>() {
		
			
			public Modulo extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Modulo modulo= rsToEntity(rs,"mod_");
							return modulo;
				}
				
				return null;
			}
			
		});


	}		
	
	
	public Modulo getByParams(Param param) {

		String sql = "select * from mod_modulo " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Modulo>() {
			
			public Modulo extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	
	public List<Modulo> listByParams(Param param, String[] order) {

		String sql = "select * from mod_modulo " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Modulo>() {

			
			public Modulo mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	
	/*public List<Modulo> listFullByParams(Modulo modulo, String[] order) {
	
		return listFullByParams(Param.toParam("mod",modulo), order);
	
	}*/	
	
	
	public List<Modulo> listFullByParams(Param param, String[] order) {

		String sql = "select mod.id mod_id, mod.cod mod_cod , mod.nom mod_nom , mod.des mod_des  ,mod.est mod_est ";
		sql = sql + " from mod_modulo mod";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Modulo>() {

			
			public Modulo mapRow(ResultSet rs, int rowNum) throws SQLException {
				Modulo modulo= rsToEntity(rs,"mod_");
				return modulo;
			}

		});

	}	


	public List<Parametro> getListParametro(Param param, String[] order) {
		String sql = "select * from mod_parametro " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<Parametro>() {

			
			public Parametro mapRow(ResultSet rs, int rowNum) throws SQLException {
				Parametro parametro = new Parametro();

				parametro.setId(rs.getInt("id"));
				parametro.setId_mod(rs.getInt("id_mod"));
				parametro.setNom(rs.getString("nom"));
				parametro.setDes(rs.getString("des"));
				parametro.setVal(rs.getString("val"));
				parametro.setEst(rs.getString("est"));
												
				return parametro;
			}

		});	
	}


	// funciones privadas utilitarias para Modulo

	private Modulo rsToEntity(ResultSet rs,String alias) throws SQLException {
		Modulo modulo = new Modulo();

		modulo.setId(rs.getInt( alias + "id"));
		modulo.setCod(rs.getString( alias + "cod"));
		modulo.setNom(rs.getString( alias + "nom"));
		modulo.setDes(rs.getString( alias + "des"));
		modulo.setEst(rs.getString( alias + "est"));
								
		return modulo;

	}
	
}
