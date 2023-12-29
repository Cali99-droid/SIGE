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
import com.tesla.colegio.model.Religion;

import com.tesla.colegio.model.Familiar;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface ReligionDAO.
 * @author MV
 *
 */
public class ReligionDAOImpl{
	final static Logger logger = Logger.getLogger(ReligionDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(Religion religion) {
		if (religion.getId()!=null) {
			// update
			String sql = "UPDATE cat_religion "
						+ "SET nom=?, "
						+ "est=?,usr_act=?,fec_act=? "
					+ "WHERE id=?";
			
			//logger.info(sql);

			return jdbcTemplate.update(sql, 
						religion.getNom(),
						religion.getEst(),
						religion.getUsr_act(),
						new java.util.Date(),
						religion.getId()); 

		} else {
			// insert
			String sql = "insert into cat_religion ("
						+ "nom, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				religion.getNom(),
				religion.getEst(),
				religion.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	
	public void delete(int id) {
		String sql = "delete from cat_religion where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	
	public List<Religion> list() {
		String sql = "select * from cat_religion";
		
		//logger.info(sql);
		
		List<Religion> listReligion = jdbcTemplate.query(sql, new RowMapper<Religion>() {

			
			public Religion mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listReligion;
	}

	
	public Religion get(int id) {
		String sql = "select * from cat_religion WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Religion>() {

			
			public Religion extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	
	public Religion getFull(int id, String tablas[]) {
		String sql = "select reli.id reli_id, reli.nom reli_nom  ,reli.est reli_est ";
	
		sql = sql + " from cat_religion reli "; 
		sql = sql + " where reli.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<Religion>() {
		
			
			public Religion extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Religion religion= rsToEntity(rs,"reli_");
							return religion;
				}
				
				return null;
			}
			
		});


	}		
	
	
	public Religion getByParams(Param param) {

		String sql = "select * from cat_religion " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Religion>() {
			
			public Religion extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	
	public List<Religion> listByParams(Param param, String[] order) {

		String sql = "select * from cat_religion " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Religion>() {

			
			public Religion mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	
	public List<Religion> listFullByParams(Religion religion, String[] order) {
	
		return listFullByParams(Param.toParam("reli",religion), order);
	
	}	
	
	
	public List<Religion> listFullByParams(Param param, String[] order) {

		String sql = "select reli.id reli_id, reli.nom reli_nom  ,reli.est reli_est ";
		sql = sql + " from cat_religion reli";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Religion>() {

			
			public Religion mapRow(ResultSet rs, int rowNum) throws SQLException {
				Religion religion= rsToEntity(rs,"reli_");
				return religion;
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


	// funciones privadas utilitarias para Religion

	private Religion rsToEntity(ResultSet rs,String alias) throws SQLException {
		Religion religion = new Religion();

		religion.setId(rs.getInt( alias + "id"));
		religion.setNom(rs.getString( alias + "nom"));
		religion.setEst(rs.getString( alias + "est"));
								
		return religion;

	}
	
}
