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
import com.tesla.colegio.model.Distrito;

import com.tesla.colegio.model.Provincia;
import com.tesla.colegio.model.Familiar;
import com.tesla.colegio.model.Colegio;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface DistritoDAO.
 * @author MV
 *
 */
public class DistritoDAOImpl{
	final static Logger logger = Logger.getLogger(DistritoDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(Distrito distrito) {
		if (distrito.getId() > 0) {
			// update
			String sql = "UPDATE cat_distrito "
						+ "SET nom=?, "
						+ "id_pro=?, "
						+ "est=?,usr_act=?,fec_act=? "
					+ "WHERE id=?";
			
			//logger.info(sql);

			return jdbcTemplate.update(sql, 
						distrito.getNom(),
						distrito.getId_pro(),
						distrito.getEst(),
						distrito.getUsr_act(),
						new java.util.Date(),
						distrito.getId()); 

		} else {
			// insert
			String sql = "insert into cat_distrito ("
						+ "nom, "
						+ "id_pro, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				distrito.getNom(),
				distrito.getId_pro(),
				distrito.getEst(),
				distrito.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	
	public void delete(int id) {
		String sql = "delete from cat_distrito where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	
	public List<Distrito> list() {
		String sql = "select * from cat_distrito";
		
		//logger.info(sql);
		
		List<Distrito> listDistrito = jdbcTemplate.query(sql, new RowMapper<Distrito>() {

			
			public Distrito mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listDistrito;
	}

	
	public Distrito get(int id) {
		String sql = "select * from cat_distrito WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Distrito>() {

			
			public Distrito extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	
	public Distrito getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select dist.id dist_id, dist.nom dist_nom , dist.id_pro dist_id_pro  ,dist.est dist_est ";
		if (aTablas.contains("cat_provincia"))
			sql = sql + ", pro.id pro_id  , pro.nom pro_nom , pro.id_dep pro_id_dep  ";
	
		sql = sql + " from cat_distrito dist "; 
		if (aTablas.contains("cat_provincia"))
			sql = sql + " left join cat_provincia pro on pro.id = dist.id_pro ";
		sql = sql + " where dist.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<Distrito>() {
		
			
			public Distrito extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Distrito distrito= rsToEntity(rs,"dist_");
					if (aTablas.contains("cat_provincia")){
						Provincia provincia = new Provincia();  
							provincia.setId(rs.getInt("pro_id")) ;  
							provincia.setNom(rs.getString("pro_nom")) ;  
							provincia.setId_dep(rs.getInt("pro_id_dep")) ;  
							distrito.setProvincia(provincia);
					}
							return distrito;
				}
				
				return null;
			}
			
		});


	}		
	
	
	public Distrito getByParams(Param param) {

		String sql = "select * from cat_distrito " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Distrito>() {
			
			public Distrito extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	
	public List<Distrito> listByParams(Param param, String[] order) {

		String sql = "select * from cat_distrito " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Distrito>() {

			
			public Distrito mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	
	public List<Distrito> listFullByParams(Distrito distrito, String[] order) {
	
		return listFullByParams(Param.toParam("dist",distrito), order);
	
	}	
	
	
	public List<Distrito> listFullByParams(Param param, String[] order) {

		String sql = "select dist.id dist_id, dist.nom dist_nom , dist.id_pro dist_id_pro  ,dist.est dist_est ";
		sql = sql + ", pro.id pro_id  , pro.nom pro_nom , pro.id_dep pro_id_dep  ";
		sql = sql + " from cat_distrito dist";
		sql = sql + " left join cat_provincia pro on pro.id = dist.id_pro ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Distrito>() {

			
			public Distrito mapRow(ResultSet rs, int rowNum) throws SQLException {
				Distrito distrito= rsToEntity(rs,"dist_");
				Provincia provincia = new Provincia();  
				provincia.setId(rs.getInt("pro_id")) ;  
				provincia.setNom(rs.getString("pro_nom")) ;  
				provincia.setId_dep(rs.getInt("pro_id_dep")) ;  
				distrito.setProvincia(provincia);
				return distrito;
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
	public List<Colegio> getListColegio(Param param, String[] order) {
		String sql = "select * from col_colegio " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<Colegio>() {

			
			public Colegio mapRow(ResultSet rs, int rowNum) throws SQLException {
				Colegio colegio = new Colegio();

				colegio.setId(rs.getInt("id"));
				colegio.setId_dist(rs.getInt("id_dist"));
				colegio.setCod_mod(rs.getString("cod_mod"));
				colegio.setNom_niv(rs.getString("nom_niv"));
				colegio.setNom(rs.getString("nom"));
				colegio.setEstatal(rs.getString("estatal"));
				colegio.setDir(rs.getString("dir"));
				colegio.setTel(rs.getString("tel"));
				colegio.setEst(rs.getString("est"));
												
				return colegio;
			}

		});	
	}


	// funciones privadas utilitarias para Distrito

	private Distrito rsToEntity(ResultSet rs,String alias) throws SQLException {
		Distrito distrito = new Distrito();

		distrito.setId(rs.getInt( alias + "id"));
		distrito.setNom(rs.getString( alias + "nom"));
		distrito.setId_pro(rs.getInt( alias + "id_pro"));
		distrito.setEst(rs.getString( alias + "est"));
								
		return distrito;

	}
	
}
