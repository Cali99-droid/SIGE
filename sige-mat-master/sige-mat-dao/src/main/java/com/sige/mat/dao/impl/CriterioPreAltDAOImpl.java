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
import com.tesla.colegio.model.CriterioPreAlt;

import com.tesla.colegio.model.CriterioNota;
import com.tesla.colegio.model.CriterioPregunta;
import com.tesla.colegio.model.CriterioAlternativa;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface CriterioPreAltDAO.
 * @author MV
 *
 */
public class CriterioPreAltDAOImpl{
	final static Logger logger = Logger.getLogger(CriterioPreAltDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(CriterioPreAlt criterio_pre_alt) {
		if (criterio_pre_alt.getId() != null) {
			// update
			String sql = "UPDATE eva_criterio_pre_alt "
						+ "SET id_cri_not=?, "
						+ "id_pre=?, "
						+ "id_alt=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			return jdbcTemplate.update(sql, 
						criterio_pre_alt.getId_cri_not(),
						criterio_pre_alt.getId_pre(),
						criterio_pre_alt.getId_alt(),
						criterio_pre_alt.getEst(),
						criterio_pre_alt.getUsr_act(),
						new java.util.Date(),
						criterio_pre_alt.getId()); 

		} else {
			// insert
			String sql = "insert into eva_criterio_pre_alt ("
						+ "id_cri_not, "
						+ "id_pre, "
						+ "id_alt, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				criterio_pre_alt.getId_cri_not(),
				criterio_pre_alt.getId_pre(),
				criterio_pre_alt.getId_alt(),
				criterio_pre_alt.getEst(),
				criterio_pre_alt.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from eva_criterio_pre_alt where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<CriterioPreAlt> list() {
		String sql = "select * from eva_criterio_pre_alt";
		
		//logger.info(sql);
		
		List<CriterioPreAlt> listCriterioPreAlt = jdbcTemplate.query(sql, new RowMapper<CriterioPreAlt>() {

			public CriterioPreAlt mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listCriterioPreAlt;
	}

	public CriterioPreAlt get(int id) {
		String sql = "select * from eva_criterio_pre_alt WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<CriterioPreAlt>() {

			public CriterioPreAlt extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public CriterioPreAlt getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select eva_pre_alt.id eva_pre_alt_id, eva_pre_alt.id_cri_not eva_pre_alt_id_cri_not , eva_pre_alt.id_pre eva_pre_alt_id_pre , eva_pre_alt.id_alt eva_pre_alt_id_alt  ,eva_pre_alt.est eva_pre_alt_est ";
		if (aTablas.contains("eva_criterio_nota"))
			sql = sql + ", eva_exa.id eva_exa_id  , eva_exa.id_ex_cri eva_exa_id_ex_cri , eva_exa.id_mat_vac eva_exa_id_mat_vac , eva_exa.num eva_exa_num , eva_exa.puntaje eva_exa_puntaje , eva_exa.resultado eva_exa_resultado , eva_exa.apto eva_exa_apto  ";
		if (aTablas.contains("eva_criterio_pregunta"))
			sql = sql + ", ex_cri_pre.id ex_cri_pre_id  , ex_cri_pre.id_per ex_cri_pre_id_per , ex_cri_pre.pre ex_cri_pre_pre , ex_cri_pre.ord ex_cri_pre_ord  ";
		if (aTablas.contains("eva_criterio_alternativa"))
			sql = sql + ", ex_cri_alt.id ex_cri_alt_id  , ex_cri_alt.id_pre ex_cri_alt_id_pre , ex_cri_alt.alt ex_cri_alt_alt , ex_cri_alt.punt ex_cri_alt_punt  ";
	
		sql = sql + " from eva_criterio_pre_alt eva_pre_alt "; 
		if (aTablas.contains("eva_criterio_nota"))
			sql = sql + " left join eva_criterio_nota eva_exa on eva_exa.id = eva_pre_alt.id_cri_not ";
		if (aTablas.contains("eva_criterio_pregunta"))
			sql = sql + " left join eva_criterio_pregunta ex_cri_pre on ex_cri_pre.id = eva_pre_alt.id_pre ";
		if (aTablas.contains("eva_criterio_alternativa"))
			sql = sql + " left join eva_criterio_alternativa ex_cri_alt on ex_cri_alt.id = eva_pre_alt.id_alt ";
		sql = sql + " where eva_pre_alt.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<CriterioPreAlt>() {
		
			public CriterioPreAlt extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					CriterioPreAlt criterioprealt= rsToEntity(rs,"eva_pre_alt_");
					if (aTablas.contains("eva_criterio_nota")){
						CriterioNota criterionota = new CriterioNota();  
							criterionota.setId(rs.getInt("eva_exa_id")) ;  
							criterionota.setId_ex_cri(rs.getInt("eva_exa_id_ex_cri")) ;  
							criterionota.setId_mat_vac(rs.getInt("eva_exa_id_mat_vac")) ;  
							criterionota.setNum(rs.getInt("eva_exa_num")) ;  
							criterionota.setPuntaje(rs.getInt("eva_exa_puntaje")) ;  
							criterionota.setResultado(rs.getString("eva_exa_resultado")) ;  
							criterionota.setApto(rs.getString("eva_exa_apto")) ;  
							criterioprealt.setCriterioNota(criterionota);
					}
					if (aTablas.contains("eva_criterio_pregunta")){
						CriterioPregunta criteriopregunta = new CriterioPregunta();  
							criteriopregunta.setId(rs.getInt("ex_cri_pre_id")) ;  
							criteriopregunta.setId_per(rs.getInt("ex_cri_pre_id_per")) ;  
							criteriopregunta.setPre(rs.getString("ex_cri_pre_pre")) ;  
							criteriopregunta.setOrd(rs.getInt("ex_cri_pre_ord")) ;  
							criterioprealt.setCriterioPregunta(criteriopregunta);
					}
					if (aTablas.contains("eva_criterio_alternativa")){
						CriterioAlternativa criterioalternativa = new CriterioAlternativa();  
							criterioalternativa.setId(rs.getInt("ex_cri_alt_id")) ;  
							criterioalternativa.setId_pre(rs.getInt("ex_cri_alt_id_pre")) ;  
							criterioalternativa.setAlt(rs.getString("ex_cri_alt_alt")) ;  
							criterioalternativa.setPunt(rs.getInt("ex_cri_alt_punt")) ;  
							criterioprealt.setCriterioAlternativa(criterioalternativa);
					}
							return criterioprealt;
				}
				
				return null;
			}
			
		});


	}		
	
	public CriterioPreAlt getByParams(Param param) {

		String sql = "select * from eva_criterio_pre_alt " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<CriterioPreAlt>() {
			@Override
			public CriterioPreAlt extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<CriterioPreAlt> listByParams(Param param, String[] order) {

		String sql = "select * from eva_criterio_pre_alt " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<CriterioPreAlt>() {

			public CriterioPreAlt mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<CriterioPreAlt> listFullByParams(CriterioPreAlt criterioprealt, String[] order) {
	
		return listFullByParams(Param.toParam("eva_pre_alt",criterioprealt), order);
	
	}	
	
	public List<CriterioPreAlt> listFullByParams(Param param, String[] order) {

		String sql = "select eva_pre_alt.id eva_pre_alt_id, eva_pre_alt.id_cri_not eva_pre_alt_id_cri_not , eva_pre_alt.id_pre eva_pre_alt_id_pre , eva_pre_alt.id_alt eva_pre_alt_id_alt  ,eva_pre_alt.est eva_pre_alt_est ";
		sql = sql + ", eva_exa.id eva_exa_id  , eva_exa.id_ex_cri eva_exa_id_ex_cri , eva_exa.id_mat_vac eva_exa_id_mat_vac , eva_exa.num eva_exa_num , eva_exa.puntaje eva_exa_puntaje , eva_exa.resultado eva_exa_resultado , eva_exa.apto eva_exa_apto  ";
		sql = sql + ", ex_cri_pre.id ex_cri_pre_id  , ex_cri_pre.id_per ex_cri_pre_id_per , ex_cri_pre.pre ex_cri_pre_pre , ex_cri_pre.ord ex_cri_pre_ord  ";
		sql = sql + ", ex_cri_alt.id ex_cri_alt_id  , ex_cri_alt.id_pre ex_cri_alt_id_pre , ex_cri_alt.alt ex_cri_alt_alt , ex_cri_alt.punt ex_cri_alt_punt  ";
		sql = sql + " from eva_criterio_pre_alt eva_pre_alt";
		sql = sql + " left join eva_criterio_nota eva_exa on eva_exa.id = eva_pre_alt.id_cri_not ";
		sql = sql + " left join eva_criterio_pregunta ex_cri_pre on ex_cri_pre.id = eva_pre_alt.id_pre ";
		sql = sql + " left join eva_criterio_alternativa ex_cri_alt on ex_cri_alt.id = eva_pre_alt.id_alt ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<CriterioPreAlt>() {

			public CriterioPreAlt mapRow(ResultSet rs, int rowNum) throws SQLException {
				CriterioPreAlt criterioprealt= rsToEntity(rs,"eva_pre_alt_");
				CriterioNota criterionota = new CriterioNota();  
				criterionota.setId(rs.getInt("eva_exa_id")) ;  
				criterionota.setId_ex_cri(rs.getInt("eva_exa_id_ex_cri")) ;  
				criterionota.setId_mat_vac(rs.getInt("eva_exa_id_mat_vac")) ;  
				criterionota.setNum(rs.getInt("eva_exa_num")) ;  
				criterionota.setPuntaje(rs.getInt("eva_exa_puntaje")) ;  
				criterionota.setResultado(rs.getString("eva_exa_resultado")) ;  
				criterionota.setApto(rs.getString("eva_exa_apto")) ;  
				criterioprealt.setCriterioNota(criterionota);
				CriterioPregunta criteriopregunta = new CriterioPregunta();  
				criteriopregunta.setId(rs.getInt("ex_cri_pre_id")) ;  
				criteriopregunta.setId_per(rs.getInt("ex_cri_pre_id_per")) ;  
				criteriopregunta.setPre(rs.getString("ex_cri_pre_pre")) ;  
				criteriopregunta.setOrd(rs.getInt("ex_cri_pre_ord")) ;  
				criterioprealt.setCriterioPregunta(criteriopregunta);
				CriterioAlternativa criterioalternativa = new CriterioAlternativa();  
				criterioalternativa.setId(rs.getInt("ex_cri_alt_id")) ;  
				criterioalternativa.setId_pre(rs.getInt("ex_cri_alt_id_pre")) ;  
				criterioalternativa.setAlt(rs.getString("ex_cri_alt_alt")) ;  
				criterioalternativa.setPunt(rs.getInt("ex_cri_alt_punt")) ;  
				criterioprealt.setCriterioAlternativa(criterioalternativa);
				return criterioprealt;
			}

		});

	}	




	// funciones privadas utilitarias para CriterioPreAlt

	private CriterioPreAlt rsToEntity(ResultSet rs,String alias) throws SQLException {
		CriterioPreAlt criterio_pre_alt = new CriterioPreAlt();

		criterio_pre_alt.setId(rs.getInt( alias + "id"));
		criterio_pre_alt.setId_cri_not(rs.getInt( alias + "id_cri_not"));
		criterio_pre_alt.setId_pre(rs.getInt( alias + "id_pre"));
		criterio_pre_alt.setId_alt(rs.getInt( alias + "id_alt"));
		criterio_pre_alt.setEst(rs.getString( alias + "est"));
								
		return criterio_pre_alt;

	}
	
}
