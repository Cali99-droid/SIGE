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
import com.tesla.colegio.model.CriterioNota;

import com.tesla.colegio.model.ExaConfCriterio;
import com.tesla.colegio.model.MatrVacante;
import com.tesla.colegio.model.CriterioPreAlt;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface CriterioNotaDAO.
 * @author MV
 *
 */
public class CriterioNotaDAOImpl{
	final static Logger logger = Logger.getLogger(CriterioNotaDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(CriterioNota criterio_nota) {
		if (criterio_nota.getId() != null) {
			// update
			String sql = "UPDATE eva_criterio_nota "
						+ "SET id_ex_cri=?, "
						+ "id_mat_vac=?, "
						+ "num=?, "
						+ "puntaje=?, "
						+ "resultado=?, "
						+ "apto=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			return jdbcTemplate.update(sql, 
						criterio_nota.getId_ex_cri(),
						criterio_nota.getId_mat_vac(),
						criterio_nota.getNum(),
						criterio_nota.getPuntaje(),
						criterio_nota.getResultado(),
						criterio_nota.getApto(),
						criterio_nota.getEst(),
						criterio_nota.getUsr_act(),
						new java.util.Date(),
						criterio_nota.getId()); 

		} else {
			// insert
			String sql = "insert into eva_criterio_nota ("
						+ "id_ex_cri, "
						+ "id_mat_vac, "
						+ "num, "
						+ "puntaje, "
						+ "resultado, "
						+ "apto, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				criterio_nota.getId_ex_cri(),
				criterio_nota.getId_mat_vac(),
				criterio_nota.getNum(),
				criterio_nota.getPuntaje(),
				criterio_nota.getResultado(),
				criterio_nota.getApto(),
				criterio_nota.getEst(),
				criterio_nota.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from eva_criterio_nota where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<CriterioNota> list() {
		String sql = "select * from eva_criterio_nota";
		
		//logger.info(sql);
		
		List<CriterioNota> listCriterioNota = jdbcTemplate.query(sql, new RowMapper<CriterioNota>() {

			public CriterioNota mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listCriterioNota;
	}

	public CriterioNota get(int id) {
		String sql = "select * from eva_criterio_nota WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<CriterioNota>() {

			public CriterioNota extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public CriterioNota getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select eva_exa.id eva_exa_id, eva_exa.id_ex_cri eva_exa_id_ex_cri , eva_exa.id_mat_vac eva_exa_id_mat_vac , eva_exa.num eva_exa_num , eva_exa.puntaje eva_exa_puntaje , eva_exa.resultado eva_exa_resultado , eva_exa.apto eva_exa_apto  ,eva_exa.est eva_exa_est ";
		if (aTablas.contains("eva_exa_conf_criterio"))
			sql = sql + ", ex_cri.id ex_cri_id  , ex_cri.id_eva_ex ex_cri_id_eva_ex , ex_cri.dur ex_cri_dur , ex_cri.fec_ini_psi ex_cri_fec_ini_psi , ex_cri.fec_fin_psi ex_cri_fec_fin_psi  ";
		if (aTablas.contains("eva_matr_vacante"))
			sql = sql + ", matr_vac.id matr_vac_id  , matr_vac.id_alu matr_vac_id_alu , matr_vac.id_eva matr_vac_id_eva , matr_vac.id_gra matr_vac_id_gra , matr_vac.id_col matr_vac_id_col , matr_vac.id_cli matr_vac_id_cli , matr_vac.num_rec matr_vac_num_rec , matr_vac.num_cont matr_vac_num_cont  ";
	
		sql = sql + " from eva_criterio_nota eva_exa "; 
		if (aTablas.contains("eva_exa_conf_criterio"))
			sql = sql + " left join eva_exa_conf_criterio ex_cri on ex_cri.id = eva_exa.id_ex_cri ";
		if (aTablas.contains("eva_matr_vacante"))
			sql = sql + " left join eva_matr_vacante matr_vac on matr_vac.id = eva_exa.id_mat_vac ";
		sql = sql + " where eva_exa.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<CriterioNota>() {
		
			public CriterioNota extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					CriterioNota criterionota= rsToEntity(rs,"eva_exa_");
					if (aTablas.contains("eva_exa_conf_criterio")){
						ExaConfCriterio exaconfcriterio = new ExaConfCriterio();  
							exaconfcriterio.setId(rs.getInt("ex_cri_id")) ;  
							exaconfcriterio.setId_eva_ex(rs.getInt("ex_cri_id_eva_ex")) ;  
							exaconfcriterio.setDur(rs.getInt("ex_cri_dur")) ;  
							exaconfcriterio.setFec_ini_psi(rs.getDate("ex_cri_fec_ini_psi")) ;  
							exaconfcriterio.setFec_fin_psi(rs.getDate("ex_cri_fec_fin_psi")) ;  
							criterionota.setExaConfCriterio(exaconfcriterio);
					}
					if (aTablas.contains("eva_matr_vacante")){
						MatrVacante matrvacante = new MatrVacante();  
							matrvacante.setId(rs.getInt("matr_vac_id")) ;  
							matrvacante.setId_alu(rs.getInt("matr_vac_id_alu")) ;  
							matrvacante.setId_eva(rs.getInt("matr_vac_id_eva")) ;  
							matrvacante.setId_gra(rs.getInt("matr_vac_id_gra")) ;  
							matrvacante.setId_col(rs.getInt("matr_vac_id_col")) ;  
							matrvacante.setId_cli(rs.getInt("matr_vac_id_cli")) ;  
							matrvacante.setNum_rec(rs.getString("matr_vac_num_rec")) ;  
							matrvacante.setNum_cont(rs.getString("matr_vac_num_cont")) ;  
							criterionota.setMatrVacante(matrvacante);
					}
							return criterionota;
				}
				
				return null;
			}
			
		});


	}		
	
	public CriterioNota getByParams(Param param) {

		String sql = "select * from eva_criterio_nota " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<CriterioNota>() {
			@Override
			public CriterioNota extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<CriterioNota> listByParams(Param param, String[] order) {

		String sql = "select * from eva_criterio_nota " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<CriterioNota>() {

			public CriterioNota mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<CriterioNota> listFullByParams(CriterioNota criterionota, String[] order) {
	
		return listFullByParams(Param.toParam("eva_exa",criterionota), order);
	
	}	
	
	public List<CriterioNota> listFullByParams(Param param, String[] order) {

		String sql = "select eva_exa.id eva_exa_id, eva_exa.id_ex_cri eva_exa_id_ex_cri , eva_exa.id_mat_vac eva_exa_id_mat_vac , eva_exa.num eva_exa_num , eva_exa.puntaje eva_exa_puntaje , eva_exa.resultado eva_exa_resultado , eva_exa.apto eva_exa_apto  ,eva_exa.est eva_exa_est ";
		sql = sql + ", ex_cri.id ex_cri_id  , ex_cri.id_eva_ex ex_cri_id_eva_ex , ex_cri.dur ex_cri_dur , ex_cri.fec_ini_psi ex_cri_fec_ini_psi , ex_cri.fec_fin_psi ex_cri_fec_fin_psi  ";
		sql = sql + ", matr_vac.id matr_vac_id  , matr_vac.id_alu matr_vac_id_alu , matr_vac.id_eva matr_vac_id_eva , matr_vac.id_gra matr_vac_id_gra , matr_vac.id_col matr_vac_id_col , matr_vac.id_cli matr_vac_id_cli , matr_vac.num_rec matr_vac_num_rec , matr_vac.num_cont matr_vac_num_cont  ";
		sql = sql + " from eva_criterio_nota eva_exa";
		sql = sql + " left join eva_exa_conf_criterio ex_cri on ex_cri.id = eva_exa.id_ex_cri ";
		sql = sql + " left join eva_matr_vacante matr_vac on matr_vac.id = eva_exa.id_mat_vac ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<CriterioNota>() {

			public CriterioNota mapRow(ResultSet rs, int rowNum) throws SQLException {
				CriterioNota criterionota= rsToEntity(rs,"eva_exa_");
				ExaConfCriterio exaconfcriterio = new ExaConfCriterio();  
				exaconfcriterio.setId(rs.getInt("ex_cri_id")) ;  
				exaconfcriterio.setId_eva_ex(rs.getInt("ex_cri_id_eva_ex")) ;  
				exaconfcriterio.setDur(rs.getInt("ex_cri_dur")) ;  
				exaconfcriterio.setFec_ini_psi(rs.getDate("ex_cri_fec_ini_psi")) ;  
				exaconfcriterio.setFec_fin_psi(rs.getDate("ex_cri_fec_fin_psi")) ;  
				criterionota.setExaConfCriterio(exaconfcriterio);
				MatrVacante matrvacante = new MatrVacante();  
				matrvacante.setId(rs.getInt("matr_vac_id")) ;  
				matrvacante.setId_alu(rs.getInt("matr_vac_id_alu")) ;  
				matrvacante.setId_eva(rs.getInt("matr_vac_id_eva")) ;  
				matrvacante.setId_gra(rs.getInt("matr_vac_id_gra")) ;  
				matrvacante.setId_col(rs.getInt("matr_vac_id_col")) ;  
				matrvacante.setId_cli(rs.getInt("matr_vac_id_cli")) ;  
				matrvacante.setNum_rec(rs.getString("matr_vac_num_rec")) ;  
				matrvacante.setNum_cont(rs.getString("matr_vac_num_cont")) ;  
				criterionota.setMatrVacante(matrvacante);
				return criterionota;
			}

		});

	}	


	public List<CriterioPreAlt> getListCriterioPreAlt(Param param, String[] order) {
		String sql = "select * from eva_criterio_pre_alt " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<CriterioPreAlt>() {

			public CriterioPreAlt mapRow(ResultSet rs, int rowNum) throws SQLException {
				CriterioPreAlt criterio_pre_alt = new CriterioPreAlt();

				criterio_pre_alt.setId(rs.getInt("id"));
				criterio_pre_alt.setId_cri_not(rs.getInt("id_cri_not"));
				criterio_pre_alt.setId_pre(rs.getInt("id_pre"));
				criterio_pre_alt.setId_alt(rs.getInt("id_alt"));
				criterio_pre_alt.setEst(rs.getString("est"));
												
				return criterio_pre_alt;
			}

		});	
	}


	// funciones privadas utilitarias para CriterioNota

	private CriterioNota rsToEntity(ResultSet rs,String alias) throws SQLException {
		CriterioNota criterio_nota = new CriterioNota();

		criterio_nota.setId(rs.getInt( alias + "id"));
		criterio_nota.setId_ex_cri(rs.getInt( alias + "id_ex_cri"));
		criterio_nota.setId_mat_vac(rs.getInt( alias + "id_mat_vac"));
		criterio_nota.setNum(rs.getInt( alias + "num"));
		criterio_nota.setPuntaje(rs.getInt( alias + "puntaje"));
		criterio_nota.setResultado(rs.getString( alias + "resultado"));
		criterio_nota.setApto(rs.getString( alias + "apto"));
		criterio_nota.setEst(rs.getString( alias + "est"));
								
		return criterio_nota;

	}
	
}
