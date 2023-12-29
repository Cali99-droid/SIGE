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
import com.tesla.colegio.model.Parentesco;

import com.tesla.colegio.model.Familiar;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface ParentescoDAO.
 * @author MV
 *
 */
public class ParentescoDAOImpl{
	final static Logger logger = Logger.getLogger(ParentescoDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(Parentesco parentesco) {
		if (parentesco.getId()!=null) {
			// update
			String sql = "UPDATE cat_parentesco "
						+ "SET par=?, "
						+ "est=?,usr_act=?,fec_act=? "
					+ "WHERE id=?";
			
			//logger.info(sql);

			return jdbcTemplate.update(sql, 
						parentesco.getPar(),
						parentesco.getEst(),
						parentesco.getUsr_act(),
						new java.util.Date(),
						parentesco.getId()); 

		} else {
			// insert
			String sql = "insert into cat_parentesco ("
						+ "par, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				parentesco.getPar(),
				parentesco.getEst(),
				parentesco.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	
	public void delete(int id) {
		String sql = "delete from cat_parentesco where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	
	public List<Parentesco> list() {
		String sql = "select * from cat_parentesco";
		
		//logger.info(sql);
		
		List<Parentesco> listParentesco = jdbcTemplate.query(sql, new RowMapper<Parentesco>() {

			
			public Parentesco mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listParentesco;
	}

	
	public Parentesco get(int id) {
		String sql = "select * from cat_parentesco WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Parentesco>() {

			
			public Parentesco extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	
	public Parentesco getFull(int id, String tablas[]) {
		String sql = "select pare.id pare_id, pare.par pare_par  ,pare.est pare_est ";
	
		sql = sql + " from cat_parentesco pare "; 
		sql = sql + " where pare.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<Parentesco>() {
		
			
			public Parentesco extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Parentesco parentesco= rsToEntity(rs,"pare_");
							return parentesco;
				}
				
				return null;
			}
			
		});


	}		
	
	
	public Parentesco getByParams(Param param) {

		String sql = "select * from cat_parentesco " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Parentesco>() {
			
			public Parentesco extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	
	public List<Parentesco> listByParams(Param param, String[] order) {

		String sql = "select * from cat_parentesco " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Parentesco>() {

			
			public Parentesco mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	
	public List<Parentesco> listFullByParams(Parentesco parentesco, String[] order) {
	
		return listFullByParams(Param.toParam("pare",parentesco), order);
	
	}	
	
	
	public List<Parentesco> listFullByParams(Param param, String[] order) {

		String sql = "select pare.id pare_id, pare.par pare_par  ,pare.est pare_est ";
		sql = sql + " from cat_parentesco pare";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Parentesco>() {

			
			public Parentesco mapRow(ResultSet rs, int rowNum) throws SQLException {
				Parentesco parentesco= rsToEntity(rs,"pare_");
				return parentesco;
			}

		});

	}	


	public List<Familiar> getListFamiliar(Param param, String[] order) {
		String sql = "select * from alu_familiar " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<Familiar>() {

			
			public Familiar mapRow(ResultSet rs, int rowNum) throws SQLException {
				Familiar familiar = new Familiar();

				familiar.setId(rs.getInt("id"));
				familiar.setId_tdc(rs.getInt("id_tdc"));
				familiar.setId_par(rs.getInt("id_par"));
				familiar.setId_tap(rs.getString("id_tap"));
				familiar.setId_gen(rs.getString("id_gen"));
				familiar.setId_eci(rs.getInt("id_eci"));
				familiar.setId_gin(rs.getInt("id_gin"));
				familiar.setId_rel(rs.getInt("id_rel"));
				familiar.setId_dist(rs.getInt("id_dist"));
				familiar.setId_ocu(rs.getInt("id_ocu"));
				familiar.setNro_doc(rs.getString("nro_doc"));
				familiar.setNom(rs.getString("nom"));
				familiar.setApe_pat(rs.getString("ape_pat"));
				familiar.setApe_mat(rs.getString("ape_mat"));
				familiar.setHue(rs.getString("hue"));
				familiar.setFec_nac(rs.getDate("fec_nac"));
				familiar.setViv(rs.getString("viv"));
				familiar.setViv_alu(rs.getString("viv_alu"));
				familiar.setDir(rs.getString("dir"));
				familiar.setTlf(rs.getString("tlf"));
				familiar.setCorr(rs.getString("corr"));
				familiar.setCel(rs.getString("cel"));
				familiar.setPass(rs.getString("pass"));
				familiar.setCto_tra(rs.getString("cto_tra"));
				familiar.setEst(rs.getString("est"));
												
				return familiar;
			}

		});	
	}


	// funciones privadas utilitarias para Parentesco

	private Parentesco rsToEntity(ResultSet rs,String alias) throws SQLException {
		Parentesco parentesco = new Parentesco();

		parentesco.setId(rs.getInt( alias + "id"));
		parentesco.setPar(rs.getString( alias + "par"));
		parentesco.setEst(rs.getString( alias + "est"));
								
		return parentesco;

	}
	
}
