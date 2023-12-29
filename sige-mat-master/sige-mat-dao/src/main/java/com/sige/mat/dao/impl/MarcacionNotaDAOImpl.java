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
import com.tesla.colegio.model.MarcacionNota;

import com.tesla.colegio.model.MatrVacante;
import com.tesla.colegio.model.ExaConfMarcacion;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface MarcacionNotaDAO.
 * @author MV
 *
 */
public class MarcacionNotaDAOImpl{
	final static Logger logger = Logger.getLogger(MarcacionNotaDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(MarcacionNota marcacion_nota) {
		if (marcacion_nota.getId() != null) {
			// update
			String sql = "UPDATE eva_marcacion_nota "
						+ "SET id_mat_vac=?, "
						+ "id_exa_mar=?, "
						+ "preg_favor=?, "
						+ "preg_contra=?, "
						+ "ptje=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			return jdbcTemplate.update(sql, 
						marcacion_nota.getId_mat_vac(),
						marcacion_nota.getId_exa_mar(),
						marcacion_nota.getPreg_favor(),
						marcacion_nota.getPreg_contra(),
						marcacion_nota.getPtje(),
						marcacion_nota.getEst(),
						marcacion_nota.getUsr_act(),
						new java.util.Date(),
						marcacion_nota.getId()); 

		} else {
			// insert
			String sql = "insert into eva_marcacion_nota ("
						+ "id_mat_vac, "
						+ "id_exa_mar, "
						+ "preg_favor, "
						+ "preg_contra, "
						+ "ptje, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				marcacion_nota.getId_mat_vac(),
				marcacion_nota.getId_exa_mar(),
				marcacion_nota.getPreg_favor(),
				marcacion_nota.getPreg_contra(),
				marcacion_nota.getPtje(),
				marcacion_nota.getEst(),
				marcacion_nota.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	
	public void delete(int id) {
		String sql = "delete from eva_marcacion_nota where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	
	public List<MarcacionNota> list() {
		String sql = "select * from eva_marcacion_nota";
		
		//logger.info(sql);
		
		List<MarcacionNota> listMarcacionNota = jdbcTemplate.query(sql, new RowMapper<MarcacionNota>() {

			
			public MarcacionNota mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listMarcacionNota;
	}

	
	public MarcacionNota get(int id) {
		String sql = "select * from eva_marcacion_nota WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<MarcacionNota>() {

			
			public MarcacionNota extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	
	public MarcacionNota getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select mar_nota.id mar_nota_id, mar_nota.id_mat_vac mar_nota_id_mat_vac , mar_nota.id_exa_mar mar_nota_id_exa_mar , mar_nota.preg_favor mar_nota_preg_favor , mar_nota.preg_contra mar_nota_preg_contra , mar_nota.ptje mar_nota_ptje  ,mar_nota.est mar_nota_est ";
		if (aTablas.contains("eva_matr_vacante"))
			sql = sql + ", matr_vac.id matr_vac_id  , matr_vac.id_alu matr_vac_id_alu , matr_vac.id_eva matr_vac_id_eva , matr_vac.id_gra matr_vac_id_gra , matr_vac.id_col matr_vac_id_col , matr_vac.id_cli matr_vac_id_cli , matr_vac.num_rec matr_vac_num_rec , matr_vac.num_cont matr_vac_num_cont  ";
		if (aTablas.contains("eva_exa_conf_marcacion"))
			sql = sql + ", ex_mar.id ex_mar_id  , ex_mar.id_eva_ex ex_mar_id_eva_ex , ex_mar.num_pre ex_mar_num_pre , ex_mar.pje_pre_cor ex_mar_pje_pre_cor , ex_mar.pje_pre_inc ex_mar_pje_pre_inc  ";
	
		sql = sql + " from eva_marcacion_nota mar_nota "; 
		if (aTablas.contains("eva_matr_vacante"))
			sql = sql + " left join eva_matr_vacante matr_vac on matr_vac.id = mar_nota.id_mat_vac ";
		if (aTablas.contains("eva_exa_conf_marcacion"))
			sql = sql + " left join eva_exa_conf_marcacion ex_mar on ex_mar.id = mar_nota.id_exa_mar ";
		sql = sql + " where mar_nota.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<MarcacionNota>() {
		
			
			public MarcacionNota extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					MarcacionNota marcacionnota= rsToEntity(rs,"mar_nota_");
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
							marcacionnota.setMatrVacante(matrvacante);
					}
					if (aTablas.contains("eva_exa_conf_marcacion")){
						ExaConfMarcacion exaconfmarcacion = new ExaConfMarcacion();  
							exaconfmarcacion.setId(rs.getInt("ex_mar_id")) ;  
							exaconfmarcacion.setId_eva_ex(rs.getInt("ex_mar_id_eva_ex")) ;  
							exaconfmarcacion.setNum_pre(rs.getInt("ex_mar_num_pre")) ;  
							exaconfmarcacion.setPje_pre_cor(rs.getBigDecimal("ex_mar_pje_pre_cor")) ;  
							exaconfmarcacion.setPje_pre_inc(rs.getBigDecimal("ex_mar_pje_pre_inc")) ;  
							marcacionnota.setExaConfMarcacion(exaconfmarcacion);
					}
							return marcacionnota;
				}
				
				return null;
			}
			
		});


	}		
	
	
	public MarcacionNota getByParams(Param param) {

		String sql = "select * from eva_marcacion_nota " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<MarcacionNota>() {
			
			public MarcacionNota extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	
	public List<MarcacionNota> listByParams(Param param, String[] order) {

		String sql = "select * from eva_marcacion_nota " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<MarcacionNota>() {

			
			public MarcacionNota mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	
	public List<MarcacionNota> listFullByParams(MarcacionNota marcacionnota, String[] order) {
	
		return listFullByParams(Param.toParam("mar_nota",marcacionnota), order);
	
	}	
	
	
	public List<MarcacionNota> listFullByParams(Param param, String[] order) {

		String sql = "select mar_nota.id mar_nota_id, mar_nota.id_mat_vac mar_nota_id_mat_vac , mar_nota.id_exa_mar mar_nota_id_exa_mar , mar_nota.preg_favor mar_nota_preg_favor , mar_nota.preg_contra mar_nota_preg_contra , mar_nota.ptje mar_nota_ptje  ,mar_nota.est mar_nota_est ";
		sql = sql + ", matr_vac.id matr_vac_id  , matr_vac.id_alu matr_vac_id_alu , matr_vac.id_eva matr_vac_id_eva , matr_vac.id_gra matr_vac_id_gra , matr_vac.id_col matr_vac_id_col , matr_vac.id_cli matr_vac_id_cli , matr_vac.num_rec matr_vac_num_rec , matr_vac.num_cont matr_vac_num_cont  ";
		sql = sql + ", ex_mar.id ex_mar_id  , ex_mar.id_eva_ex ex_mar_id_eva_ex , ex_mar.num_pre ex_mar_num_pre , ex_mar.pje_pre_cor ex_mar_pje_pre_cor , ex_mar.pje_pre_inc ex_mar_pje_pre_inc  ";
		sql = sql + " from eva_marcacion_nota mar_nota";
		sql = sql + " left join eva_matr_vacante matr_vac on matr_vac.id = mar_nota.id_mat_vac ";
		sql = sql + " left join eva_exa_conf_marcacion ex_mar on ex_mar.id = mar_nota.id_exa_mar ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<MarcacionNota>() {

			
			public MarcacionNota mapRow(ResultSet rs, int rowNum) throws SQLException {
				MarcacionNota marcacionnota= rsToEntity(rs,"mar_nota_");
				MatrVacante matrvacante = new MatrVacante();  
				matrvacante.setId(rs.getInt("matr_vac_id")) ;  
				matrvacante.setId_alu(rs.getInt("matr_vac_id_alu")) ;  
				matrvacante.setId_eva(rs.getInt("matr_vac_id_eva")) ;  
				matrvacante.setId_gra(rs.getInt("matr_vac_id_gra")) ;  
				matrvacante.setId_col(rs.getInt("matr_vac_id_col")) ;  
				matrvacante.setId_cli(rs.getInt("matr_vac_id_cli")) ;  
				matrvacante.setNum_rec(rs.getString("matr_vac_num_rec")) ;  
				matrvacante.setNum_cont(rs.getString("matr_vac_num_cont")) ;  
				marcacionnota.setMatrVacante(matrvacante);
				ExaConfMarcacion exaconfmarcacion = new ExaConfMarcacion();  
				exaconfmarcacion.setId(rs.getInt("ex_mar_id")) ;  
				exaconfmarcacion.setId_eva_ex(rs.getInt("ex_mar_id_eva_ex")) ;  
				exaconfmarcacion.setNum_pre(rs.getInt("ex_mar_num_pre")) ;  
				exaconfmarcacion.setPje_pre_cor(rs.getBigDecimal("ex_mar_pje_pre_cor")) ;  
				exaconfmarcacion.setPje_pre_inc(rs.getBigDecimal("ex_mar_pje_pre_inc")) ;  
				marcacionnota.setExaConfMarcacion(exaconfmarcacion);
				return marcacionnota;
			}

		});

	}	




	// funciones privadas utilitarias para MarcacionNota

	private MarcacionNota rsToEntity(ResultSet rs,String alias) throws SQLException {
		MarcacionNota marcacion_nota = new MarcacionNota();

		marcacion_nota.setId(rs.getInt( alias + "id"));
		marcacion_nota.setId_mat_vac(rs.getInt( alias + "id_mat_vac"));
		marcacion_nota.setId_exa_mar(rs.getInt( alias + "id_exa_mar"));
		marcacion_nota.setPreg_favor(rs.getInt( alias + "preg_favor"));
		marcacion_nota.setPreg_contra(rs.getInt( alias + "preg_contra"));
		marcacion_nota.setPtje(rs.getBigDecimal( alias + "ptje"));
		marcacion_nota.setEst(rs.getString( alias + "est"));
								
		return marcacion_nota;

	}
	
}
