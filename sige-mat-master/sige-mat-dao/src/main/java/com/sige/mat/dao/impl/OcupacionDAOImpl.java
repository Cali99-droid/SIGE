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
import com.tesla.colegio.model.Ocupacion;

import com.tesla.colegio.model.Familiar;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface OcupacionDAO.
 * @author MV
 *
 */
public class OcupacionDAOImpl{
	final static Logger logger = Logger.getLogger(OcupacionDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(Ocupacion ocupacion) {
		if (ocupacion.getId() > 0) {
			// update
			String sql = "UPDATE cat_ocupacion "
						+ "SET nom=?, "
						+ "est=?,usr_act=?,fec_act=? "
					+ "WHERE id=?";
			
			//logger.info(sql);

			return jdbcTemplate.update(sql, 
						ocupacion.getNom(),
						ocupacion.getEst(),
						ocupacion.getUsr_act(),
						new java.util.Date(),
						ocupacion.getId()); 

		} else {
			// insert
			String sql = "insert into cat_ocupacion ("
						+ "nom, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				ocupacion.getNom(),
				ocupacion.getEst(),
				ocupacion.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	
	public void delete(int id) {
		String sql = "delete from cat_ocupacion where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	
	public List<Ocupacion> list() {
		String sql = "select * from cat_ocupacion";
		
		//logger.info(sql);
		
		List<Ocupacion> listOcupacion = jdbcTemplate.query(sql, new RowMapper<Ocupacion>() {

			
			public Ocupacion mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listOcupacion;
	}

	
	public Ocupacion get(int id) {
		String sql = "select * from cat_ocupacion WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Ocupacion>() {

			
			public Ocupacion extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	
	public Ocupacion getFull(int id, String tablas[]) {
		String sql = "select ocu.id ocu_id, ocu.nom ocu_nom  ,ocu.est ocu_est ";
	
		sql = sql + " from cat_ocupacion ocu "; 
		sql = sql + " where ocu.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<Ocupacion>() {
		
			
			public Ocupacion extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Ocupacion ocupacion= rsToEntity(rs,"ocu_");
							return ocupacion;
				}
				
				return null;
			}
			
		});


	}		
	
	
	public Ocupacion getByParams(Param param) {

		String sql = "select * from cat_ocupacion " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Ocupacion>() {
			
			public Ocupacion extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	
	public List<Ocupacion> listByParams(Param param, String[] order) {

		String sql = "select * from cat_ocupacion " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Ocupacion>() {

			
			public Ocupacion mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	
	public List<Ocupacion> listFullByParams(Ocupacion ocupacion, String[] order) {
	
		return listFullByParams(Param.toParam("ocu",ocupacion), order);
	
	}	
	
	
	public List<Ocupacion> listFullByParams(Param param, String[] order) {

		String sql = "select ocu.id ocu_id, ocu.nom ocu_nom  ,ocu.est ocu_est ";
		sql = sql + " from cat_ocupacion ocu";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Ocupacion>() {

			
			public Ocupacion mapRow(ResultSet rs, int rowNum) throws SQLException {
				Ocupacion ocupacion= rsToEntity(rs,"ocu_");
				return ocupacion;
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


	// funciones privadas utilitarias para Ocupacion

	private Ocupacion rsToEntity(ResultSet rs,String alias) throws SQLException {
		Ocupacion ocupacion = new Ocupacion();

		ocupacion.setId(rs.getInt( alias + "id"));
		ocupacion.setNom(rs.getString( alias + "nom"));
		ocupacion.setEst(rs.getString( alias + "est"));
								
		return ocupacion;

	}
	
}
