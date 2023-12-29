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
import com.tesla.colegio.model.DescPunt;

import com.tesla.colegio.model.Periodo;
import com.tesla.colegio.model.Servicio;
import com.tesla.colegio.model.Sucursal;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface DescPuntDAO.
 * @author MV
 *
 */
public class DescPuntDAOImpl{
	final static Logger logger = Logger.getLogger(DescPuntDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(DescPunt desc_punt) {
		if (desc_punt.getId() != null) {
			// update
			String sql = "UPDATE mat_desc_punt "
						+ "SET id_per=?, "
						+ "monto=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						desc_punt.getId_per(),
						desc_punt.getMonto(),
						desc_punt.getEst(),
						desc_punt.getUsr_act(),
						new java.util.Date(),
						desc_punt.getId()); 
			return desc_punt.getId();

		} else {
			// insert
			String sql = "insert into mat_desc_punt ("
						+ "id_per, "
						+ "monto, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				desc_punt.getId_per(),
				desc_punt.getMonto(),
				desc_punt.getEst(),
				desc_punt.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from mat_desc_punt where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<DescPunt> list() {
		String sql = "select * from mat_desc_punt";
		
		//logger.info(sql);
		
		List<DescPunt> listDescPunt = jdbcTemplate.query(sql, new RowMapper<DescPunt>() {

			@Override
			public DescPunt mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listDescPunt;
	}

	public DescPunt get(int id) {
		String sql = "select * from mat_desc_punt WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<DescPunt>() {

			@Override
			public DescPunt extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public DescPunt getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select dpp.id dpp_id, dpp.id_per dpp_id_per , dpp.monto dpp_monto  ,dpp.est dpp_est ";
		if (aTablas.contains("per_periodo"))
			sql = sql + ", pee.id pee_id  , pee.id_anio pee_id_anio , pee.id_srv pee_id_srv , pee.id_tpe pee_id_tpe , pee.fec_ini pee_fec_ini , pee.fec_fin pee_fec_fin , pee.fec_cie_mat pee_fec_cie_mat  ";
	
		sql = sql + " from mat_desc_punt dpp "; 
		if (aTablas.contains("per_periodo"))
			sql = sql + " left join per_periodo pee on pee.id = dpp.id_per ";
		sql = sql + " where dpp.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<DescPunt>() {
		
			@Override
			public DescPunt extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					DescPunt descpunt= rsToEntity(rs,"dpp_");
					if (aTablas.contains("per_periodo")){
						Periodo periodo = new Periodo();  
							periodo.setId(rs.getInt("pee_id")) ;  
							periodo.setId_anio(rs.getInt("pee_id_anio")) ;  
							periodo.setId_srv(rs.getInt("pee_id_srv")) ;  
							periodo.setId_tpe(rs.getInt("pee_id_tpe")) ;  
							periodo.setFec_ini(rs.getDate("pee_fec_ini")) ;  
							periodo.setFec_fin(rs.getDate("pee_fec_fin")) ;  
							periodo.setFec_cie_mat(rs.getDate("pee_fec_cie_mat")) ;  
							descpunt.setPeriodo(periodo);
					}
							return descpunt;
				}
				
				return null;
			}
			
		});


	}		
	
	public DescPunt getByParams(Param param) {

		String sql = "select * from mat_desc_punt " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<DescPunt>() {
			@Override
			public DescPunt extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<DescPunt> listByParams(Param param, String[] order) {

		String sql = "select * from mat_desc_punt " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<DescPunt>() {

			@Override
			public DescPunt mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<DescPunt> listFullByParams(DescPunt descpunt, String[] order) {
	
		return listFullByParams(Param.toParam("dpp",descpunt), order);
	
	}	
	
	public List<DescPunt> listFullByParams(Param param, String[] order) {

		String sql = "select dpp.id dpp_id, dpp.id_per dpp_id_per , dpp.monto dpp_monto  ,dpp.est dpp_est ";
		sql = sql + ", pee.id pee_id  , pee.id_anio pee_id_anio , pee.id_srv pee_id_srv , pee.id_tpe pee_id_tpe , pee.fec_ini pee_fec_ini , pee.fec_fin pee_fec_fin , pee.fec_cie_mat pee_fec_cie_mat  ";
		sql = sql + ", srv.id srv_id  , srv.nom srv_nom  ";
		sql = sql + ", suc.id suc_id  , suc.nom suc_nom  ";
		sql = sql + " from mat_desc_punt dpp";
		sql = sql + " left join per_periodo pee on pee.id = dpp.id_per ";
		sql = sql + " left join ges_servicio srv on srv.id = pee.id_srv ";
		sql = sql + " left join ges_sucursal suc on suc.id = srv.id_suc ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<DescPunt>() {

			@Override
			public DescPunt mapRow(ResultSet rs, int rowNum) throws SQLException {
				DescPunt descpunt= rsToEntity(rs,"dpp_");
				Periodo periodo = new Periodo();  
				periodo.setId(rs.getInt("pee_id")) ;  
				periodo.setId_anio(rs.getInt("pee_id_anio")) ;  
				periodo.setId_srv(rs.getInt("pee_id_srv")) ;  
				periodo.setId_tpe(rs.getInt("pee_id_tpe")) ;  
				periodo.setFec_ini(rs.getDate("pee_fec_ini")) ;  
				periodo.setFec_fin(rs.getDate("pee_fec_fin")) ;  
				periodo.setFec_cie_mat(rs.getDate("pee_fec_cie_mat")) ;  
				
				Servicio servicio = new Servicio();
				servicio.setId(rs.getInt("srv_id")) ;
				servicio.setNom(rs.getString("srv_nom"));
				Sucursal sucursal = new Sucursal();
				sucursal.setId(rs.getInt("suc_id")) ;
				sucursal.setNom(rs.getString("suc_nom"));
				servicio.setSucursal(sucursal);
				periodo.setServicio(servicio);
				
				descpunt.setPeriodo(periodo);
				return descpunt;
			}

		});

	}	




	// funciones privadas utilitarias para DescPunt

	private DescPunt rsToEntity(ResultSet rs,String alias) throws SQLException {
		DescPunt desc_punt = new DescPunt();

		desc_punt.setId(rs.getInt( alias + "id"));
		desc_punt.setId_per(rs.getInt( alias + "id_per"));
		desc_punt.setMonto(rs.getBigDecimal( alias + "monto"));
		desc_punt.setEst(rs.getString( alias + "est"));
								
		return desc_punt;

	}
	
}
