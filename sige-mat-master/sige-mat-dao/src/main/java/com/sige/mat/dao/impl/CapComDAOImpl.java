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
import com.tesla.colegio.model.CapCom;

import com.tesla.colegio.model.Comportamiento;
import com.tesla.colegio.model.Capacidad;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface CapComDAO.
 * @author MV
 *
 */
public class CapComDAOImpl{
	final static Logger logger = Logger.getLogger(CapComDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(CapCom cap_com) {
		if (cap_com.getId() != null) {
			// update
			String sql = "UPDATE not_cap_com "
						+ "SET id_nc=?, "
						+ "id_cap=?, "
						+ "nota=?, "
						+ "fec=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						cap_com.getId_nc(),
						cap_com.getId_cap(),
						cap_com.getNota(),
						cap_com.getFec(),
						cap_com.getEst(),
						cap_com.getUsr_act(),
						new java.util.Date(),
						cap_com.getId()); 
			return cap_com.getId();

		} else {
			// insert
			String sql = "insert into not_cap_com ("
						+ "id_nc, "
						+ "id_cap, "
						+ "nota, "
						+ "fec, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				cap_com.getId_nc(),
				cap_com.getId_cap(),
				cap_com.getNota(),
				cap_com.getFec(),
				cap_com.getEst(),
				cap_com.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id_au, int id_cpu) {
		String sql = "delete from not_cap_com where id_nc in (select id from not_comportamiento where id_au=? and id_cpu=?)";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id_au, id_cpu);
	}

	public List<CapCom> list() {
		String sql = "select * from not_cap_com";
		
		//logger.info(sql);
		
		List<CapCom> listCapCom = jdbcTemplate.query(sql, new RowMapper<CapCom>() {

			@Override
			public CapCom mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listCapCom;
	}

	public CapCom get(int id) {
		String sql = "select * from not_cap_com WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<CapCom>() {

			@Override
			public CapCom extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public CapCom getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select ncc.id ncc_id, ncc.id_nc ncc_id_nc , ncc.id_cap ncc_id_cap , ncc.nota ncc_nota , ncc.fec ncc_fec  ,ncc.est ncc_est ";
		if (aTablas.contains("not_comportamiento"))
			sql = sql + ", nc.id nc_id  , nc.id_tra nc_id_tra , nc.id_alu nc_id_alu , nc.id_au nc_id_au , nc.id_cpu nc_id_cpu , nc.prom nc_prom  ";
		if (aTablas.contains("col_capacidad"))
			sql = sql + ", cap.id cap_id  , cap.id_com cap_id_com , cap.nom cap_nom  ";
	
		sql = sql + " from not_cap_com ncc "; 
		if (aTablas.contains("not_comportamiento"))
			sql = sql + " left join not_comportamiento nc on nc.id = ncc.id_nc ";
		if (aTablas.contains("col_capacidad"))
			sql = sql + " left join col_capacidad cap on cap.id = ncc.id_cap ";
		sql = sql + " where ncc.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<CapCom>() {
		
			@Override
			public CapCom extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					CapCom capcom= rsToEntity(rs,"ncc_");
					if (aTablas.contains("not_comportamiento")){
						Comportamiento comportamiento = new Comportamiento();  
							comportamiento.setId(rs.getInt("nc_id")) ;  
							comportamiento.setId_tra(rs.getInt("nc_id_tra")) ;  
							comportamiento.setId_alu(rs.getInt("nc_id_alu")) ;  
							comportamiento.setId_au(rs.getInt("nc_id_au")) ;  
							comportamiento.setId_cpu(rs.getInt("nc_id_cpu")) ;  
							comportamiento.setProm(rs.getBigDecimal("nc_prom")) ;  
							capcom.setComportamiento(comportamiento);
					}
					if (aTablas.contains("col_capacidad")){
						Capacidad capacidad = new Capacidad();  
							capacidad.setId(rs.getInt("cap_id")) ;  
							capacidad.setId_com(rs.getInt("cap_id_com")) ;  
							capacidad.setNom(rs.getString("cap_nom")) ;  
							capcom.setCapacidad(capacidad);
					}
							return capcom;
				}
				
				return null;
			}
			
		});


	}		
	
	public CapCom getByParams(Param param) {

		String sql = "select * from not_cap_com " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<CapCom>() {
			@Override
			public CapCom extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<CapCom> listByParams(Param param, String[] order) {

		String sql = "select * from not_cap_com " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<CapCom>() {

			@Override
			public CapCom mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<CapCom> listFullByParams(CapCom capcom, String[] order) {
	
		return listFullByParams(Param.toParam("ncc",capcom), order);
	
	}	
	
	public List<CapCom> listFullByParams(Param param, String[] order) {

		String sql = "select ncc.id ncc_id, ncc.id_nc ncc_id_nc , ncc.id_cap ncc_id_cap , ncc.nota ncc_nota , ncc.fec ncc_fec  ,ncc.est ncc_est ";
		sql = sql + ", nc.id nc_id  , nc.id_tra nc_id_tra , nc.id_alu nc_id_alu , nc.id_au nc_id_au , nc.id_cpu nc_id_cpu , nc.prom nc_prom  ";
		sql = sql + ", cap.id cap_id  , cap.id_com cap_id_com , cap.nom cap_nom  ";
		sql = sql + " from not_cap_com ncc";
		sql = sql + " left join not_comportamiento nc on nc.id = ncc.id_nc ";
		sql = sql + " left join col_capacidad cap on cap.id = ncc.id_cap ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<CapCom>() {

			@Override
			public CapCom mapRow(ResultSet rs, int rowNum) throws SQLException {
				CapCom capcom= rsToEntity(rs,"ncc_");
				Comportamiento comportamiento = new Comportamiento();  
				comportamiento.setId(rs.getInt("nc_id")) ;  
				comportamiento.setId_tra(rs.getInt("nc_id_tra")) ;  
				comportamiento.setId_alu(rs.getInt("nc_id_alu")) ;  
				comportamiento.setId_au(rs.getInt("nc_id_au")) ;  
				comportamiento.setId_cpu(rs.getInt("nc_id_cpu")) ;  
				comportamiento.setProm(rs.getBigDecimal("nc_prom")) ;  
				capcom.setComportamiento(comportamiento);
				Capacidad capacidad = new Capacidad();  
				capacidad.setId(rs.getInt("cap_id")) ;  
				capacidad.setId_com(rs.getInt("cap_id_com")) ;  
				capacidad.setNom(rs.getString("cap_nom")) ;  
				capcom.setCapacidad(capacidad);
				return capcom;
			}

		});

	}	




	// funciones privadas utilitarias para CapCom

	private CapCom rsToEntity(ResultSet rs,String alias) throws SQLException {
		CapCom cap_com = new CapCom();

		cap_com.setId(rs.getInt( alias + "id"));
		cap_com.setId_nc(rs.getInt( alias + "id_nc"));
		cap_com.setId_cap(rs.getInt( alias + "id_cap"));
		cap_com.setNota(rs.getInt( alias + "nota"));
		cap_com.setFec(rs.getDate( alias + "fec"));
		cap_com.setEst(rs.getString( alias + "est"));
								
		return cap_com;

	}
	
}
