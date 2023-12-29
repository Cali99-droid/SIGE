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
import com.tesla.colegio.model.CapacidadSetup;

import com.tesla.colegio.model.Periodo;
import com.tesla.colegio.model.Grad;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface CapacidadSetupDAO.
 * @author MV
 *
 */
public class CapacidadSetupDAOImpl{
	
	final static Logger logger = Logger.getLogger(CapacidadSetupDAOImpl.class);
	
	
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(CapacidadSetup capacidad_setup) {
		if (capacidad_setup.getId() != null) {
			// update
			String sql = "UPDATE col_capacidad_setup "
						+ "SET id_per=?, "
						+ "id_grad=?, "
						+ "cant=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			return jdbcTemplate.update(sql, 
						capacidad_setup.getId_per(),
						capacidad_setup.getId_grad(),
						capacidad_setup.getCant(),
						capacidad_setup.getEst(),
						capacidad_setup.getUsr_act(),
						new java.util.Date(),
						capacidad_setup.getId()); 

		} else {
			// insert
			String sql = "insert into col_capacidad_setup ("
						+ "id_per, "
						+ "id_grad, "
						+ "cant, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				capacidad_setup.getId_per(),
				capacidad_setup.getId_grad(),
				capacidad_setup.getCant(),
				capacidad_setup.getEst(),
				capacidad_setup.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	
	public void delete(int id) {
		String sql = "delete from col_capacidad_setup where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	
	public List<CapacidadSetup> list() {
		String sql = "select * from col_capacidad_setup";
		
		//logger.info(sql);
		
		List<CapacidadSetup> listCapacidadSetup = jdbcTemplate.query(sql, new RowMapper<CapacidadSetup>() {

			
			public CapacidadSetup mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listCapacidadSetup;
	}

	
	public CapacidadSetup get(int id) {
		String sql = "select * from col_capacidad_setup WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<CapacidadSetup>() {

			
			public CapacidadSetup extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	
	public CapacidadSetup getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select cap.id cap_id, cap.id_per cap_id_per , cap.id_grad cap_id_grad , cap.cant cap_cant  ,cap.est cap_est ";
		if (aTablas.contains("per_periodo"))
			sql = sql + ", pee.id pee_id  , pee.id_anio pee_id_anio , pee.id_srv pee_id_srv , pee.id_tpe pee_id_tpe , pee.fec_ini pee_fec_ini , pee.fec_fin pee_fec_fin , pee.fec_cie_mat pee_fec_cie_mat  ";
		if (aTablas.contains("cat_grad"))
			sql = sql + ", grad.id grad_id  , grad.id_nvl grad_id_nvl , grad.nom grad_nom  ";
	
		sql = sql + " from col_capacidad_setup cap "; 
		if (aTablas.contains("per_periodo"))
			sql = sql + " left join per_periodo pee on pee.id = cap.id_per ";
		if (aTablas.contains("cat_grad"))
			sql = sql + " left join cat_grad grad on grad.id = cap.id_grad ";
		sql = sql + " where cap.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<CapacidadSetup>() {
		
			
			public CapacidadSetup extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					CapacidadSetup capacidadsetup= rsToEntity(rs,"cap_");
					if (aTablas.contains("per_periodo")){
						Periodo periodo = new Periodo();  
							periodo.setId(rs.getInt("pee_id")) ;  
							periodo.setId_anio(rs.getInt("pee_id_anio")) ;  
							periodo.setId_srv(rs.getInt("pee_id_srv")) ;  
							periodo.setId_tpe(rs.getInt("pee_id_tpe")) ;  
							periodo.setFec_ini(rs.getDate("pee_fec_ini")) ;  
							periodo.setFec_fin(rs.getDate("pee_fec_fin")) ;  
							periodo.setFec_cie_mat(rs.getDate("pee_fec_cie_mat")) ;  
							capacidadsetup.setPeriodo(periodo);
					}
					if (aTablas.contains("cat_grad")){
						Grad grad = new Grad();  
							grad.setId(rs.getInt("grad_id")) ;  
							grad.setId_nvl(rs.getInt("grad_id_nvl")) ;  
							grad.setNom(rs.getString("grad_nom")) ;  
							capacidadsetup.setGrad(grad);
					}
							return capacidadsetup;
				}
				
				return null;
			}
			
		});


	}		
	
	
	public CapacidadSetup getByParams(Param param) {

		String sql = "select * from col_capacidad_setup " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<CapacidadSetup>() {
			
			public CapacidadSetup extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	
	public List<CapacidadSetup> listByParams(Param param, String[] order) {

		String sql = "select * from col_capacidad_setup " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<CapacidadSetup>() {

			
			public CapacidadSetup mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	
	public List<CapacidadSetup> listFullByParams(CapacidadSetup capacidadsetup, String[] order) {
	
		return listFullByParams(Param.toParam("cap",capacidadsetup), order);
	
	}	
	
	
	public List<CapacidadSetup> listFullByParams(Param param, String[] order) {

		String sql = "select cap.id cap_id, cap.id_per cap_id_per , cap.id_grad cap_id_grad , cap.cant cap_cant  ,cap.est cap_est ";
		sql = sql + ", pee.id pee_id  , pee.id_anio pee_id_anio , pee.id_srv pee_id_srv , pee.id_tpe pee_id_tpe , pee.fec_ini pee_fec_ini , pee.fec_fin pee_fec_fin , pee.fec_cie_mat pee_fec_cie_mat  ";
		sql = sql + ", grad.id grad_id  , grad.id_nvl grad_id_nvl , grad.nom grad_nom  ";
		sql = sql + " from col_capacidad_setup cap";
		sql = sql + " left join per_periodo pee on pee.id = cap.id_per ";
		sql = sql + " left join cat_grad grad on grad.id = cap.id_grad ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<CapacidadSetup>() {

			
			public CapacidadSetup mapRow(ResultSet rs, int rowNum) throws SQLException {
				CapacidadSetup capacidadsetup= rsToEntity(rs,"cap_");
				Periodo periodo = new Periodo();  
				periodo.setId(rs.getInt("pee_id")) ;  
				periodo.setId_anio(rs.getInt("pee_id_anio")) ;  
				periodo.setId_srv(rs.getInt("pee_id_srv")) ;  
				periodo.setId_tpe(rs.getInt("pee_id_tpe")) ;  
				periodo.setFec_ini(rs.getDate("pee_fec_ini")) ;  
				periodo.setFec_fin(rs.getDate("pee_fec_fin")) ;  
				periodo.setFec_cie_mat(rs.getDate("pee_fec_cie_mat")) ;  
				capacidadsetup.setPeriodo(periodo);
				Grad grad = new Grad();  
				grad.setId(rs.getInt("grad_id")) ;  
				grad.setId_nvl(rs.getInt("grad_id_nvl")) ;  
				grad.setNom(rs.getString("grad_nom")) ;  
				capacidadsetup.setGrad(grad);
				return capacidadsetup;
			}

		});

	}	




	// funciones privadas utilitarias para CapacidadSetup

	private CapacidadSetup rsToEntity(ResultSet rs,String alias) throws SQLException {
		CapacidadSetup capacidad_setup = new CapacidadSetup();

		capacidad_setup.setId(rs.getInt( alias + "id"));
		capacidad_setup.setId_per(rs.getInt( alias + "id_per"));
		capacidad_setup.setId_grad(rs.getInt( alias + "id_grad"));
		capacidad_setup.setCant(rs.getInt( alias + "cant"));
		capacidad_setup.setEst(rs.getString( alias + "est"));
								
		return capacidad_setup;

	}
	
}
