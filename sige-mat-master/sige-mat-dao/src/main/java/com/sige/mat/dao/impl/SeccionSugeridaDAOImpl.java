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
import com.tesla.colegio.model.SeccionSugerida;

import com.tesla.colegio.model.Matricula;
import com.tesla.colegio.model.Aula;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface SeccionSugeridaDAO.
 * @author MV
 *
 */
public class SeccionSugeridaDAOImpl{
	final static Logger logger = Logger.getLogger(SeccionSugeridaDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(SeccionSugerida seccion_sugerida) {
		if (seccion_sugerida.getId() != null) {
			// update
			String sql = "UPDATE mat_seccion_sugerida "
						+ "SET id_mat=?, "
						+ "id_au_nue=?, "
						+ "id_gra_nue=?, "
						+ "id_suc_nue=?, "
						+ "id_anio=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						seccion_sugerida.getId_mat(),
						seccion_sugerida.getId_au_nue(),
						seccion_sugerida.getId_gra_nue(),
						seccion_sugerida.getId_suc_nue(),
						seccion_sugerida.getId_anio(),
						seccion_sugerida.getEst(),
						seccion_sugerida.getUsr_act(),
						new java.util.Date(),
						seccion_sugerida.getId()); 
			return seccion_sugerida.getId();

		} else {
			// insert
			String sql = "insert into mat_seccion_sugerida ("
						+ "id_mat, "
						+ "id_au_nue, "
						+ "id_gra_nue, "
						+ "id_suc_nue, "
						+ "id_anio, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?,?, ?, ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				seccion_sugerida.getId_mat(),
				seccion_sugerida.getId_au_nue(),
				seccion_sugerida.getId_gra_nue(),
				seccion_sugerida.getId_suc_nue(),
				seccion_sugerida.getId_anio(),
				seccion_sugerida.getEst(),
				seccion_sugerida.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from mat_seccion_sugerida where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<SeccionSugerida> list() {
		String sql = "select * from mat_seccion_sugerida";
		
		//logger.info(sql);
		
		List<SeccionSugerida> listSeccionSugerida = jdbcTemplate.query(sql, new RowMapper<SeccionSugerida>() {

			@Override
			public SeccionSugerida mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listSeccionSugerida;
	}

	public SeccionSugerida get(int id) {
		String sql = "select * from mat_seccion_sugerida WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<SeccionSugerida>() {

			@Override
			public SeccionSugerida extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public SeccionSugerida getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select mss.id mss_id, mss.id_mat mss_id_mat , mss.id_au_nue mss_id_au_nue  ,mss.est mss_est ";
		if (aTablas.contains("mat_matricula"))
			sql = sql + ", mat.id mat_id  , mat.id_alu mat_id_alu , mat.id_fam mat_id_fam , mat.id_enc mat_id_enc , mat.id_con mat_id_con , mat.id_sit mat_id_sit , mat.id_cli mat_id_cli , mat.id_per mat_id_per , mat.id_au mat_id_au , mat.id_gra mat_id_gra , mat.id_niv mat_id_niv , mat.fecha mat_fecha , mat.car_pod mat_car_pod , mat.num_cont mat_num_cont , mat.obs mat_obs  ";
		if (aTablas.contains("col_aula"))
			sql = sql + ", aula.id aula_id  , aula.id_per aula_id_per , aula.id_grad aula_id_grad , aula.id_secc_ant aula_id_secc_ant , aula.id_tur aula_id_tur , aula.secc aula_secc , aula.cap aula_cap  ";
	
		sql = sql + " from mat_seccion_sugerida mss "; 
		if (aTablas.contains("mat_matricula"))
			sql = sql + " left join mat_matricula mat on mat.id = mss.id_mat ";
		if (aTablas.contains("col_aula"))
			sql = sql + " left join col_aula aula on aula.id = mss.id_au_nue ";
		sql = sql + " where mss.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<SeccionSugerida>() {
		
			@Override
			public SeccionSugerida extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					SeccionSugerida seccionsugerida= rsToEntity(rs,"mss_");
					
					if (aTablas.contains("col_aula")){
						Aula aula = new Aula();  
							aula.setId(rs.getInt("aula_id")) ;  
							aula.setId_per(rs.getInt("aula_id_per")) ;  
							aula.setId_grad(rs.getInt("aula_id_grad")) ;  
							aula.setId_secc_ant(rs.getInt("aula_id_secc_ant")) ;  
							aula.setId_tur(rs.getInt("aula_id_tur")) ;  
							aula.setSecc(rs.getString("aula_secc")) ;  
							aula.setCap(rs.getInt("aula_cap")) ;  
							seccionsugerida.setAula(aula);
					}
							return seccionsugerida;
				}
				
				return null;
			}
			
		});


	}		
	
	public SeccionSugerida getByParams(Param param) {

		String sql = "select * from mat_seccion_sugerida " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<SeccionSugerida>() {
			@Override
			public SeccionSugerida extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<SeccionSugerida> listByParams(Param param, String[] order) {

		String sql = "select * from mat_seccion_sugerida " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<SeccionSugerida>() {

			@Override
			public SeccionSugerida mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<SeccionSugerida> listFullByParams(SeccionSugerida seccionsugerida, String[] order) {
	
		return listFullByParams(Param.toParam("mss",seccionsugerida), order);
	
	}	
	
	public List<SeccionSugerida> listFullByParams(Param param, String[] order) {

		String sql = "select mss.id mss_id, mss.id_mat mss_id_mat , mss.id_au_nue mss_id_au_nue  ,mss.est mss_est ";
		sql = sql + ", mat.id mat_id  , mat.id_alu mat_id_alu , mat.id_fam mat_id_fam , mat.id_enc mat_id_enc , mat.id_con mat_id_con , mat.id_sit mat_id_sit , mat.id_cli mat_id_cli , mat.id_per mat_id_per , mat.id_au mat_id_au , mat.id_gra mat_id_gra , mat.id_niv mat_id_niv , mat.fecha mat_fecha , mat.car_pod mat_car_pod , mat.num_cont mat_num_cont , mat.obs mat_obs  ";
		sql = sql + ", aula.id aula_id  , aula.id_per aula_id_per , aula.id_grad aula_id_grad , aula.id_secc_ant aula_id_secc_ant , aula.id_tur aula_id_tur , aula.secc aula_secc , aula.cap aula_cap  ";
		sql = sql + " from mat_seccion_sugerida mss";
		sql = sql + " left join mat_matricula mat on mat.id = mss.id_mat ";
		sql = sql + " left join col_aula aula on aula.id = mss.id_au_nue ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<SeccionSugerida>() {

			@Override
			public SeccionSugerida mapRow(ResultSet rs, int rowNum) throws SQLException {
				SeccionSugerida seccionsugerida= rsToEntity(rs,"mss_");
				Matricula matricula = new Matricula();  
				matricula.setId(rs.getInt("mat_id")) ;  
				matricula.setId_alu(rs.getInt("mat_id_alu")) ;  
				matricula.setId_fam(rs.getInt("mat_id_fam")) ;  
				matricula.setId_enc(rs.getInt("mat_id_enc")) ;  
				matricula.setId_con(rs.getInt("mat_id_con")) ;  
				matricula.setId_sit(rs.getInt("mat_id_sit")) ;  
				matricula.setId_cli(rs.getInt("mat_id_cli")) ;  
				matricula.setId_per(rs.getInt("mat_id_per")) ;  
				matricula.setId_au(rs.getInt("mat_id_au")) ;  
				matricula.setId_gra(rs.getInt("mat_id_gra")) ;  
				matricula.setId_niv(rs.getInt("mat_id_niv")) ;  
				matricula.setFecha(rs.getDate("mat_fecha")) ;  
				matricula.setCar_pod(rs.getString("mat_car_pod")) ;  
				//matricula.setNum_cont(rs.getInt("mat_num_cont")) ;  
				matricula.setObs(rs.getString("mat_obs")) ;  
				seccionsugerida.setMatricula(matricula);
				Aula aula = new Aula();  
				aula.setId(rs.getInt("aula_id")) ;  
				aula.setId_per(rs.getInt("aula_id_per")) ;  
				aula.setId_grad(rs.getInt("aula_id_grad")) ;  
				aula.setId_secc_ant(rs.getInt("aula_id_secc_ant")) ;  
				aula.setId_tur(rs.getInt("aula_id_tur")) ;  
				aula.setSecc(rs.getString("aula_secc")) ;  
				aula.setCap(rs.getInt("aula_cap")) ;  
				seccionsugerida.setAula(aula);
				return seccionsugerida;
			}

		});

	}	




	// funciones privadas utilitarias para SeccionSugerida

	private SeccionSugerida rsToEntity(ResultSet rs,String alias) throws SQLException {
		SeccionSugerida seccion_sugerida = new SeccionSugerida();

		seccion_sugerida.setId(rs.getInt( alias + "id"));
		seccion_sugerida.setId_mat(rs.getInt( alias + "id_mat"));
		seccion_sugerida.setId_au_nue(rs.getInt( alias + "id_au_nue"));
		seccion_sugerida.setId_gra_nue(rs.getInt( alias + "id_gra_nue"));
		seccion_sugerida.setId_suc_nue(rs.getInt( alias + "id_suc_nue"));
		seccion_sugerida.setEst(rs.getString( alias + "est"));
								
		return seccion_sugerida;

	}
	
}
