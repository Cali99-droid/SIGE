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
import com.tesla.colegio.model.InsExaCri;

import com.tesla.colegio.model.ExaConfCriterio;
import com.tesla.colegio.model.Instrumento;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface InsExaCriDAO.
 * @author MV
 *
 */
public class InsExaCriDAOImpl{
	final static Logger logger = Logger.getLogger(InsExaCriDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(InsExaCri ins_exa_cri) {
		if (ins_exa_cri.getId() != null) {
			// update
			String sql = "UPDATE eva_ins_exa_cri "
						+ "SET id_excri=?, "
						+ "id_ins=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			return jdbcTemplate.update(sql, 
						ins_exa_cri.getId_excri(),
						ins_exa_cri.getId_ins(),
						ins_exa_cri.getEst(),
						ins_exa_cri.getUsr_act(),
						new java.util.Date(),
						ins_exa_cri.getId()); 

		} else {
			// insert
			String sql = "insert into eva_ins_exa_cri ("
						+ "id_excri, "
						+ "id_ins, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				ins_exa_cri.getId_excri(),
				ins_exa_cri.getId_ins(),
				ins_exa_cri.getEst(),
				ins_exa_cri.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	
	public void delete(int id) {
		String sql = "delete from eva_ins_exa_cri where id_excri=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	
	public List<InsExaCri> list() {
		String sql = "select * from eva_ins_exa_cri";
		
		//logger.info(sql);
		
		List<InsExaCri> listInsExaCri = jdbcTemplate.query(sql, new RowMapper<InsExaCri>() {

			
			public InsExaCri mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listInsExaCri;
	}

	
	public InsExaCri get(int id) {
		String sql = "select * from eva_ins_exa_cri WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<InsExaCri>() {

			
			public InsExaCri extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	
	public InsExaCri getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select eva_ins.id eva_ins_id, eva_ins.id_excri eva_ins_id_excri , eva_ins.id_ins eva_ins_id_ins  ,eva_ins.est eva_ins_est ";
		if (aTablas.contains("eva_exa_conf_criterio"))
			sql = sql + ", ex_cri.id ex_cri_id  , ex_cri.id_eva_ex ex_cri_id_eva_ex , ex_cri.dur ex_cri_dur , ex_cri.fec_ini_psi ex_cri_fec_ini_psi , ex_cri.fec_fin_psi ex_cri_fec_fin_psi  ";
		if (aTablas.contains("eva_instrumento"))
			sql = sql + ", ins.id ins_id  , ins.nom ins_nom  ";
	
		sql = sql + " from eva_ins_exa_cri eva_ins "; 
		if (aTablas.contains("eva_exa_conf_criterio"))
			sql = sql + " left join eva_exa_conf_criterio ex_cri on ex_cri.id = eva_ins.id_excri ";
		if (aTablas.contains("eva_instrumento"))
			sql = sql + " left join eva_instrumento ins on ins.id = eva_ins.id_ins ";
		sql = sql + " where eva_ins.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<InsExaCri>() {
		
			
			public InsExaCri extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					InsExaCri insexacri= rsToEntity(rs,"eva_ins_");
					if (aTablas.contains("eva_exa_conf_criterio")){
						ExaConfCriterio exaconfcriterio = new ExaConfCriterio();  
							exaconfcriterio.setId(rs.getInt("ex_cri_id")) ;  
							exaconfcriterio.setId_eva_ex(rs.getInt("ex_cri_id_eva_ex")) ;  
							exaconfcriterio.setDur(rs.getInt("ex_cri_dur")) ;  
							exaconfcriterio.setFec_ini_psi(rs.getDate("ex_cri_fec_ini_psi")) ;  
							exaconfcriterio.setFec_fin_psi(rs.getDate("ex_cri_fec_fin_psi")) ;  
							insexacri.setExaConfCriterio(exaconfcriterio);
					}
					if (aTablas.contains("eva_instrumento")){
						Instrumento instrumento = new Instrumento();  
							instrumento.setId(rs.getInt("ins_id")) ;  
							instrumento.setNom(rs.getString("ins_nom")) ;  
							insexacri.setInstrumento(instrumento);
					}
							return insexacri;
				}
				
				return null;
			}
			
		});


	}		
	
	
	public InsExaCri getByParams(Param param) {

		String sql = "select * from eva_ins_exa_cri " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<InsExaCri>() {
			
			public InsExaCri extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	
	public List<InsExaCri> listByParams(Param param, String[] order) {

		String sql = "select * from eva_ins_exa_cri " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<InsExaCri>() {

			
			public InsExaCri mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	
	public List<InsExaCri> listFullByParams(InsExaCri insexacri, String[] order) {
	
		return listFullByParams(Param.toParam("eva_ins",insexacri), order);
	
	}	
	
	
	public List<InsExaCri> listFullByParams(Param param, String[] order) {

		String sql = "select eva_ins.id eva_ins_id, eva_ins.id_excri eva_ins_id_excri , eva_ins.id_ins eva_ins_id_ins  ,eva_ins.est eva_ins_est ";
		sql = sql + ", ex_cri.id ex_cri_id  , ex_cri.id_eva_ex ex_cri_id_eva_ex , ex_cri.dur ex_cri_dur , ex_cri.fec_ini_psi ex_cri_fec_ini_psi , ex_cri.fec_fin_psi ex_cri_fec_fin_psi  ";
		sql = sql + ", ins.id ins_id  , ins.nom ins_nom  ";
		sql = sql + " from eva_ins_exa_cri eva_ins";
		sql = sql + " left join eva_exa_conf_criterio ex_cri on ex_cri.id = eva_ins.id_excri ";
		sql = sql + " left join eva_instrumento ins on ins.id = eva_ins.id_ins ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<InsExaCri>() {

			
			public InsExaCri mapRow(ResultSet rs, int rowNum) throws SQLException {
				InsExaCri insexacri= rsToEntity(rs,"eva_ins_");
				ExaConfCriterio exaconfcriterio = new ExaConfCriterio();  
				exaconfcriterio.setId(rs.getInt("ex_cri_id")) ;  
				exaconfcriterio.setId_eva_ex(rs.getInt("ex_cri_id_eva_ex")) ;  
				exaconfcriterio.setDur(rs.getInt("ex_cri_dur")) ;  
				exaconfcriterio.setFec_ini_psi(rs.getDate("ex_cri_fec_ini_psi")) ;  
				exaconfcriterio.setFec_fin_psi(rs.getDate("ex_cri_fec_fin_psi")) ;  
				insexacri.setExaConfCriterio(exaconfcriterio);
				Instrumento instrumento = new Instrumento();  
				instrumento.setId(rs.getInt("ins_id")) ;  
				instrumento.setNom(rs.getString("ins_nom")) ;  
				insexacri.setInstrumento(instrumento);
				return insexacri;
			}

		});

	}	




	// funciones privadas utilitarias para InsExaCri

	private InsExaCri rsToEntity(ResultSet rs,String alias) throws SQLException {
		InsExaCri ins_exa_cri = new InsExaCri();

		ins_exa_cri.setId(rs.getInt( alias + "id"));
		ins_exa_cri.setId_excri(rs.getInt( alias + "id_excri"));
		ins_exa_cri.setId_ins(rs.getInt( alias + "id_ins"));
		ins_exa_cri.setEst(rs.getString( alias + "est"));
								
		return ins_exa_cri;

	}
	
}
