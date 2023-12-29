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
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import com.tesla.colegio.model.MatrVacanteResultado;

import com.tesla.colegio.model.MatrVacante;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface MatrVacanteResultadoDAO.
 * @author MV
 *
 */

public class MatrVacanteResultadoDAOImpl{
	final static Logger logger = Logger.getLogger(MatrVacanteResultadoDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(MatrVacanteResultado matr_vacante_resultado) {
		if (matr_vacante_resultado.getId() != null) {
			// update
			String sql = "UPDATE eva_matr_vacante_resultado "
						+ "SET id_mat_vac=?, "
						+ "notafinal=?, "
						+ "res=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			return jdbcTemplate.update(sql, 
						matr_vacante_resultado.getId_mat_vac(),
						matr_vacante_resultado.getNotafinal(),
						matr_vacante_resultado.getRes(),
						matr_vacante_resultado.getEst(),
						matr_vacante_resultado.getUsr_act(),
						new java.util.Date(),
						matr_vacante_resultado.getId()); 

		} else {
			// insert
			String sql = "insert into eva_matr_vacante_resultado ("
						+ "id_mat_vac, "
						+ "notafinal, "
						+ "res, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				matr_vacante_resultado.getId_mat_vac(),
				matr_vacante_resultado.getNotafinal(),
				matr_vacante_resultado.getRes(),
				matr_vacante_resultado.getEst(),
				matr_vacante_resultado.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	
	public void delete(int id) {
		String sql = "delete from eva_matr_vacante_resultado where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<MatrVacanteResultado> list() {
		String sql = "select * from eva_matr_vacante_resultado";
		
		//logger.info(sql);
		
		List<MatrVacanteResultado> listMatrVacanteResultado = jdbcTemplate.query(sql, new RowMapper<MatrVacanteResultado>() {

			@Override
			public MatrVacanteResultado mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listMatrVacanteResultado;
	}

	
	public MatrVacanteResultado get(int id) {
		String sql = "select * from eva_matr_vacante_resultado WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<MatrVacanteResultado>() {

			@Override
			public MatrVacanteResultado extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	
	public MatrVacanteResultado getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select mre.id mre_id, mre.id_mat_vac mre_id_mat_vac , mre.notafinal mre_notafinal , mre.res mre_res  ,mre.est mre_est ";
		if (aTablas.contains("eva_matr_vacante"))
			sql = sql + ", matr_vac.id matr_vac_id  , matr_vac.id_alu matr_vac_id_alu , matr_vac.id_eva matr_vac_id_eva , matr_vac.id_gra matr_vac_id_gra , matr_vac.id_col matr_vac_id_col , matr_vac.id_cli matr_vac_id_cli , matr_vac.num_rec matr_vac_num_rec , matr_vac.num_cont matr_vac_num_cont  ";
	
		sql = sql + " from eva_matr_vacante_resultado mre "; 
		if (aTablas.contains("eva_matr_vacante"))
			sql = sql + " left join eva_matr_vacante matr_vac on matr_vac.id = mre.id_mat_vac ";
		sql = sql + " where mre.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<MatrVacanteResultado>() {
		
			@Override
			public MatrVacanteResultado extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					MatrVacanteResultado matrvacanteresultado= rsToEntity(rs,"mre_");
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
							matrvacanteresultado.setMatrVacante(matrvacante);
					}
							return matrvacanteresultado;
				}
				
				return null;
			}
			
		});


	}		
	
	
	public MatrVacanteResultado getByParams(Param param) {

		String sql = "select * from eva_matr_vacante_resultado " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<MatrVacanteResultado>() {
			@Override
			public MatrVacanteResultado extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	
	public List<MatrVacanteResultado> listByParams(Param param, String[] order) {

		String sql = "select * from eva_matr_vacante_resultado " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<MatrVacanteResultado>() {

			@Override
			public MatrVacanteResultado mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	
	public List<MatrVacanteResultado> listFullByParams(MatrVacanteResultado matrvacanteresultado, String[] order) {
	
		return listFullByParams(Param.toParam("mre",matrvacanteresultado), order);
	
	}	
	
	
	public List<MatrVacanteResultado> listFullByParams(Param param, String[] order) {

		String sql = "select mre.id mre_id, mre.id_mat_vac mre_id_mat_vac , mre.notafinal mre_notafinal , mre.res mre_res  ,mre.est mre_est ";
		sql = sql + ", matr_vac.id matr_vac_id  , matr_vac.id_alu matr_vac_id_alu , matr_vac.id_eva matr_vac_id_eva , matr_vac.id_gra matr_vac_id_gra , matr_vac.id_col matr_vac_id_col , matr_vac.id_cli matr_vac_id_cli , matr_vac.num_rec matr_vac_num_rec , matr_vac.num_cont matr_vac_num_cont  ";
		sql = sql + " from eva_matr_vacante_resultado mre";
		sql = sql + " left join eva_matr_vacante matr_vac on matr_vac.id = mre.id_mat_vac ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<MatrVacanteResultado>() {

			@Override
			public MatrVacanteResultado mapRow(ResultSet rs, int rowNum) throws SQLException {
				MatrVacanteResultado matrvacanteresultado= rsToEntity(rs,"mre_");
				MatrVacante matrvacante = new MatrVacante();  
				matrvacante.setId(rs.getInt("matr_vac_id")) ;  
				matrvacante.setId_alu(rs.getInt("matr_vac_id_alu")) ;  
				matrvacante.setId_eva(rs.getInt("matr_vac_id_eva")) ;  
				matrvacante.setId_gra(rs.getInt("matr_vac_id_gra")) ;  
				matrvacante.setId_col(rs.getInt("matr_vac_id_col")) ;  
				matrvacante.setId_cli(rs.getInt("matr_vac_id_cli")) ;  
				matrvacante.setNum_rec(rs.getString("matr_vac_num_rec")) ;  
				matrvacante.setNum_cont(rs.getString("matr_vac_num_cont")) ;  
				matrvacanteresultado.setMatrVacante(matrvacante);
				return matrvacanteresultado;
			}

		});

	}	




	// funciones privadas utilitarias para MatrVacanteResultado

	private MatrVacanteResultado rsToEntity(ResultSet rs,String alias) throws SQLException {
		MatrVacanteResultado matr_vacante_resultado = new MatrVacanteResultado();

		matr_vacante_resultado.setId(rs.getInt( alias + "id"));
		matr_vacante_resultado.setId_mat_vac(rs.getInt( alias + "id_mat_vac"));
		matr_vacante_resultado.setNotafinal(rs.getBigDecimal( alias + "notafinal"));
		matr_vacante_resultado.setRes(rs.getString( alias + "res"));
		matr_vacante_resultado.setEst(rs.getString( alias + "est"));
								
		return matr_vacante_resultado;

	}
	
}
