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
import com.tesla.colegio.model.Vacante;

import com.tesla.colegio.model.Periodo;
import com.tesla.colegio.model.EvaluacionVac;
import com.tesla.colegio.model.Grad;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface VacanteDAO.
 * @author MV
 *
 */
public class VacanteDAOImpl{
	final static Logger logger = Logger.getLogger(VacanteDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(Vacante vacante) {
		if (vacante.getId() != 0) {
			// update
			String sql = "UPDATE eva_vacante "
						+ "SET id_per=?, "
						+ "id_eva=?, "
						+ "id_grad=?, "
						+ "nro_vac=?, "
						+ "vac_ofe=?, "
						+ "post=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						vacante.getId_per(),
						vacante.getId_eva(),
						vacante.getId_grad(),
						vacante.getNro_vac(),
						vacante.getVac_ofe(),
						vacante.getPost(),
						vacante.getEst(),
						vacante.getUsr_act(),
						new java.util.Date(),
						vacante.getId()); 
			return vacante.getId();

		} else {
			// insert
			String sql = "insert into eva_vacante ("
						+ "id_per, "
						+ "id_eva, "
						+ "id_grad, "
						+ "nro_vac, "
						+ "vac_ofe, "
						+ "post, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				vacante.getId_per(),
				vacante.getId_eva(),
				vacante.getId_grad(),
				vacante.getNro_vac(),
				vacante.getVac_ofe(),
				vacante.getPost(),
				vacante.getEst(),
				vacante.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from eva_vacante where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<Vacante> list() {
		String sql = "select * from eva_vacante";
		
		//logger.info(sql);
		
		List<Vacante> listVacante = jdbcTemplate.query(sql, new RowMapper<Vacante>() {

			@Override
			public Vacante mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listVacante;
	}

	public Vacante get(int id) {
		String sql = "select * from eva_vacante WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Vacante>() {

			@Override
			public Vacante extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public Vacante getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select vac.id vac_id, vac.id_per vac_id_per , vac.id_eva vac_id_eva , vac.id_grad vac_id_grad , vac.nro_vac vac_nro_vac , vac.vac_ofe vac_vac_ofe , vac.post vac_post  ,vac.est vac_est ";
		if (aTablas.contains("per_periodo"))
			sql = sql + ", pee.id pee_id  , pee.id_anio pee_id_anio , pee.id_srv pee_id_srv , pee.id_tpe pee_id_tpe , pee.fec_ini pee_fec_ini , pee.fec_fin pee_fec_fin , pee.fec_cie_mat pee_fec_cie_mat  ";
		if (aTablas.contains("eva_evaluacion_vac"))
			sql = sql + ", eva_vac.id eva_vac_id  , eva_vac.id_per eva_vac_id_per , eva_vac.des eva_vac_des , eva_vac.precio eva_vac_precio , eva_vac.ptje_apro eva_vac_ptje_apro , eva_vac.fec_ini eva_vac_fec_ini , eva_vac.fec_fin eva_vac_fec_fin  ";
		if (aTablas.contains("cat_grad"))
			sql = sql + ", grad.id grad_id  , grad.id_nvl grad_id_nvl , grad.nom grad_nom  ";
	
		sql = sql + " from eva_vacante vac "; 
		if (aTablas.contains("per_periodo"))
			sql = sql + " left join per_periodo pee on pee.id = vac.id_per ";
		if (aTablas.contains("eva_evaluacion_vac"))
			sql = sql + " left join eva_evaluacion_vac eva_vac on eva_vac.id = vac.id_eva ";
		if (aTablas.contains("cat_grad"))
			sql = sql + " left join cat_grad grad on grad.id = vac.id_grad ";
		sql = sql + " where vac.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<Vacante>() {
		
			@Override
			public Vacante extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Vacante vacante= rsToEntity(rs,"vac_");
					if (aTablas.contains("per_periodo")){
						Periodo periodo = new Periodo();  
							periodo.setId(rs.getInt("pee_id")) ;  
							periodo.setId_anio(rs.getInt("pee_id_anio")) ;  
							periodo.setId_srv(rs.getInt("pee_id_srv")) ;  
							periodo.setId_tpe(rs.getInt("pee_id_tpe")) ;  
							periodo.setFec_ini(rs.getDate("pee_fec_ini")) ;  
							periodo.setFec_fin(rs.getDate("pee_fec_fin")) ;  
							periodo.setFec_cie_mat(rs.getDate("pee_fec_cie_mat")) ;  
							vacante.setPeriodo(periodo);
					}
					if (aTablas.contains("eva_evaluacion_vac")){
						EvaluacionVac evaluacionvac = new EvaluacionVac();  
							evaluacionvac.setId(rs.getInt("eva_vac_id")) ;  
							evaluacionvac.setId_per(rs.getInt("eva_vac_id_per")) ;  
							evaluacionvac.setDes(rs.getString("eva_vac_des")) ;  
							evaluacionvac.setPrecio(rs.getBigDecimal("eva_vac_precio")) ;  
							evaluacionvac.setPtje_apro(rs.getBigDecimal("eva_vac_ptje_apro")) ;  
							evaluacionvac.setFec_ini(rs.getDate("eva_vac_fec_ini")) ;  
							evaluacionvac.setFec_fin(rs.getDate("eva_vac_fec_fin")) ;  
							vacante.setEvaluacionVac(evaluacionvac);
					}
					if (aTablas.contains("cat_grad")){
						Grad grad = new Grad();  
							grad.setId(rs.getInt("grad_id")) ;  
							grad.setId_nvl(rs.getInt("grad_id_nvl")) ;  
							grad.setNom(rs.getString("grad_nom")) ;  
							vacante.setGrad(grad);
					}
							return vacante;
				}
				
				return null;
			}
			
		});


	}		
	
	public Vacante getByParams(Param param) {

		String sql = "select * from eva_vacante " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Vacante>() {
			@Override
			public Vacante extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<Vacante> listByParams(Param param, String[] order) {

		String sql = "select * from eva_vacante " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Vacante>() {

			@Override
			public Vacante mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<Vacante> listFullByParams(Vacante vacante, String[] order) {
	
		return listFullByParams(Param.toParam("vac",vacante), order);
	
	}	
	
	public List<Vacante> listFullByParams(Param param, String[] order) {

		String sql = "select vac.id vac_id, vac.id_per vac_id_per , vac.id_eva vac_id_eva , vac.id_grad vac_id_grad , vac.nro_vac vac_nro_vac , vac.vac_ofe vac_vac_ofe , vac.post vac_post  ,vac.est vac_est ";
		sql = sql + ", pee.id pee_id  , pee.id_anio pee_id_anio , pee.id_srv pee_id_srv , pee.id_tpe pee_id_tpe , pee.fec_ini pee_fec_ini , pee.fec_fin pee_fec_fin , pee.fec_cie_mat pee_fec_cie_mat  ";
		sql = sql + ", eva_vac.id eva_vac_id  , eva_vac.id_per eva_vac_id_per , eva_vac.des eva_vac_des , eva_vac.precio eva_vac_precio , eva_vac.ptje_apro eva_vac_ptje_apro , eva_vac.fec_ini eva_vac_fec_ini , eva_vac.fec_fin eva_vac_fec_fin  ";
		sql = sql + ", grad.id grad_id  , grad.id_nvl grad_id_nvl , grad.nom grad_nom  ";
		sql = sql + " from eva_vacante vac";
		sql = sql + " left join per_periodo pee on pee.id = vac.id_per ";
		sql = sql + " left join eva_evaluacion_vac eva_vac on eva_vac.id = vac.id_eva ";
		sql = sql + " left join cat_grad grad on grad.id = vac.id_grad ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Vacante>() {

			@Override
			public Vacante mapRow(ResultSet rs, int rowNum) throws SQLException {
				Vacante vacante= rsToEntity(rs,"vac_");
				Periodo periodo = new Periodo();  
				periodo.setId(rs.getInt("pee_id")) ;  
				periodo.setId_anio(rs.getInt("pee_id_anio")) ;  
				periodo.setId_srv(rs.getInt("pee_id_srv")) ;  
				periodo.setId_tpe(rs.getInt("pee_id_tpe")) ;  
				periodo.setFec_ini(rs.getDate("pee_fec_ini")) ;  
				periodo.setFec_fin(rs.getDate("pee_fec_fin")) ;  
				periodo.setFec_cie_mat(rs.getDate("pee_fec_cie_mat")) ;  
				vacante.setPeriodo(periodo);
				EvaluacionVac evaluacionvac = new EvaluacionVac();  
				evaluacionvac.setId(rs.getInt("eva_vac_id")) ;  
				evaluacionvac.setId_per(rs.getInt("eva_vac_id_per")) ;  
				evaluacionvac.setDes(rs.getString("eva_vac_des")) ;  
				evaluacionvac.setPrecio(rs.getBigDecimal("eva_vac_precio")) ;  
				evaluacionvac.setPtje_apro(rs.getBigDecimal("eva_vac_ptje_apro")) ;  
				evaluacionvac.setFec_ini(rs.getDate("eva_vac_fec_ini")) ;  
				evaluacionvac.setFec_fin(rs.getDate("eva_vac_fec_fin")) ;  
				vacante.setEvaluacionVac(evaluacionvac);
				Grad grad = new Grad();  
				grad.setId(rs.getInt("grad_id")) ;  
				grad.setId_nvl(rs.getInt("grad_id_nvl")) ;  
				grad.setNom(rs.getString("grad_nom")) ;  
				vacante.setGrad(grad);
				return vacante;
			}

		});

	}	




	// funciones privadas utilitarias para Vacante

	private Vacante rsToEntity(ResultSet rs,String alias) throws SQLException {
		Vacante vacante = new Vacante();

		vacante.setId(rs.getInt( alias + "id"));
		vacante.setId_per(rs.getInt( alias + "id_per"));
		vacante.setId_eva(rs.getInt( alias + "id_eva"));
		vacante.setId_grad(rs.getInt( alias + "id_grad"));
		vacante.setNro_vac(rs.getInt( alias + "nro_vac"));
		vacante.setVac_ofe(rs.getInt( alias + "vac_ofe"));
		vacante.setPost(rs.getInt( alias + "post"));
		vacante.setEst(rs.getString( alias + "est"));
								
		return vacante;

	}
	
}
