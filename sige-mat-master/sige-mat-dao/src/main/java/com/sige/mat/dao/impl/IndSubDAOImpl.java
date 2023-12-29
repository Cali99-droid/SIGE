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
import com.tesla.colegio.model.IndSub;

import com.tesla.colegio.model.Indicador;
import com.tesla.colegio.model.CursoSubtema;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface IndSubDAO.
 * @author MV
 *
 */
public class IndSubDAOImpl{
	final static Logger logger = Logger.getLogger(IndSubDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(IndSub ind_sub) {
		if (ind_sub.getId() != null) {
			// update
			String sql = "UPDATE col_ind_sub "
						+ "SET id_ind=?, "
						+ "id_sub=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						ind_sub.getId_ind(),
						ind_sub.getId_sub(),
						ind_sub.getEst(),
						ind_sub.getUsr_act(),
						new java.util.Date(),
						ind_sub.getId()); 
			return ind_sub.getId();

		} else {
			// insert
			String sql = "insert into col_ind_sub ("
						+ "id_ind, "
						+ "id_sub, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				ind_sub.getId_ind(),
				ind_sub.getId_sub(),
				ind_sub.getEst(),
				ind_sub.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from col_ind_sub where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<IndSub> list() {
		String sql = "select * from col_ind_sub";
		
		//logger.info(sql);
		
		List<IndSub> listIndSub = jdbcTemplate.query(sql, new RowMapper<IndSub>() {

			@Override
			public IndSub mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listIndSub;
	}

	public IndSub get(int id) {
		String sql = "select * from col_ind_sub WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<IndSub>() {

			@Override
			public IndSub extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public IndSub getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select cis.id cis_id, cis.id_ind cis_id_ind , cis.id_sub cis_id_sub  ,cis.est cis_est ";
		if (aTablas.contains("col_indicador"))
			sql = sql + ", ind.id ind_id  , ind.id_anio ind_id_anio , ind.id_gra ind_id_gra , ind.id_cap ind_id_cap , ind.nom ind_nom  ";
		if (aTablas.contains("col_curso_subtema"))
			sql = sql + ", ccs.id ccs_id  , ccs.id_anio ccs_id_anio , ccs.id_niv ccs_id_niv , ccs.id_gra ccs_id_gra , ccs.id_cur ccs_id_cur , ccs.id_sub ccs_id_sub , ccs.dur ccs_dur  ";
	
		sql = sql + " from col_ind_sub cis "; 
		if (aTablas.contains("col_indicador"))
			sql = sql + " left join col_indicador ind on ind.id = cis.id_ind ";
		if (aTablas.contains("col_curso_subtema"))
			sql = sql + " left join col_curso_subtema ccs on ccs.id = cis.id_sub ";
		sql = sql + " where cis.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<IndSub>() {
		
			@Override
			public IndSub extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					IndSub indsub= rsToEntity(rs,"cis_");
					if (aTablas.contains("col_indicador")){
						Indicador indicador = new Indicador();  
							indicador.setId(rs.getInt("ind_id")) ;  
							indicador.setNom(rs.getString("ind_nom")) ;  
							indsub.setIndicador(indicador);
					}
					if (aTablas.contains("col_curso_subtema")){
						CursoSubtema cursosubtema = new CursoSubtema();  
							cursosubtema.setId(rs.getInt("ccs_id")) ;  
							cursosubtema.setId_anio(rs.getInt("ccs_id_anio")) ;  
							cursosubtema.setId_niv(rs.getInt("ccs_id_niv")) ;  
							cursosubtema.setId_gra(rs.getInt("ccs_id_gra")) ;  
							cursosubtema.setId_cur(rs.getInt("ccs_id_cur")) ;  
							cursosubtema.setId_sub(rs.getInt("ccs_id_sub")) ;  
							cursosubtema.setDur(rs.getBigDecimal("ccs_dur")) ;  
							indsub.setCursoSubtema(cursosubtema);
					}
							return indsub;
				}
				
				return null;
			}
			
		});


	}		
	
	public IndSub getByParams(Param param) {

		String sql = "select * from col_ind_sub " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<IndSub>() {
			@Override
			public IndSub extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<IndSub> listByParams(Param param, String[] order) {

		String sql = "select * from col_ind_sub " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<IndSub>() {

			@Override
			public IndSub mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<IndSub> listFullByParams(IndSub indsub, String[] order) {
	
		return listFullByParams(Param.toParam("cis",indsub), order);
	
	}	
	
	public List<IndSub> listFullByParams(Param param, String[] order) {

		String sql = "select cis.id cis_id, cis.id_ind cis_id_ind , cis.id_sub cis_id_sub  ,cis.est cis_est ";
		sql = sql + ", ind.id ind_id  , ind.id_anio ind_id_anio , ind.id_gra ind_id_gra , ind.id_cap ind_id_cap , ind.nom ind_nom  ";
		sql = sql + ", ccs.id ccs_id  , ccs.id_anio ccs_id_anio , ccs.id_niv ccs_id_niv , ccs.id_gra ccs_id_gra , ccs.id_cur ccs_id_cur , ccs.id_sub ccs_id_sub , ccs.dur ccs_dur  ";
		sql = sql + " from col_ind_sub cis";
		sql = sql + " left join col_indicador ind on ind.id = cis.id_ind ";
		sql = sql + " left join col_curso_subtema ccs on ccs.id = cis.id_sub ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<IndSub>() {

			@Override
			public IndSub mapRow(ResultSet rs, int rowNum) throws SQLException {
				IndSub indsub= rsToEntity(rs,"cis_");
				Indicador indicador = new Indicador();  
				indicador.setId(rs.getInt("ind_id")) ;    
				indicador.setNom(rs.getString("ind_nom")) ;  
				indsub.setIndicador(indicador);
				CursoSubtema cursosubtema = new CursoSubtema();  
				cursosubtema.setId(rs.getInt("ccs_id")) ;  
				cursosubtema.setId_anio(rs.getInt("ccs_id_anio")) ;  
				cursosubtema.setId_niv(rs.getInt("ccs_id_niv")) ;  
				cursosubtema.setId_gra(rs.getInt("ccs_id_gra")) ;  
				cursosubtema.setId_cur(rs.getInt("ccs_id_cur")) ;  
				cursosubtema.setId_sub(rs.getInt("ccs_id_sub")) ;  
				cursosubtema.setDur(rs.getBigDecimal("ccs_dur")) ;  
				indsub.setCursoSubtema(cursosubtema);
				return indsub;
			}

		});

	}	




	// funciones privadas utilitarias para IndSub

	private IndSub rsToEntity(ResultSet rs,String alias) throws SQLException {
		IndSub ind_sub = new IndSub();

		ind_sub.setId(rs.getInt( alias + "id"));
		ind_sub.setId_ind(rs.getInt( alias + "id_ind"));
		ind_sub.setId_sub(rs.getInt( alias + "id_sub"));
		ind_sub.setEst(rs.getString( alias + "est"));
								
		return ind_sub;

	}
	
}
