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
import com.tesla.colegio.model.VacantesSetup;

import com.tesla.colegio.model.Nivel;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface VacantesSetupDAO.
 * @author MV
 *
 */
public class VacantesSetupDAOImpl{
	final static Logger logger = Logger.getLogger(VacantesSetupDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(VacantesSetup vacantes_setup) {
		if (vacantes_setup.getId() != null) {
			// update
			String sql = "UPDATE mat_vacantes_setup "
						+ "SET id_au=?, "
						+ "vacantes=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						vacantes_setup.getId_au(),
						vacantes_setup.getVacantes(),
						vacantes_setup.getEst(),
						vacantes_setup.getUsr_act(),
						new java.util.Date(),
						vacantes_setup.getId()); 
			return vacantes_setup.getId();

		} else {
			// insert
			String sql = "insert into mat_vacantes_setup ("
						+ "id_au, "
						+ "vacantes, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				vacantes_setup.getId_au(),
				vacantes_setup.getVacantes(),
				vacantes_setup.getEst(),
				vacantes_setup.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from mat_vacantes_setup where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<VacantesSetup> list() {
		String sql = "select * from mat_vacantes_setup";
		
		//logger.info(sql);
		
		List<VacantesSetup> listVacantesSetup = jdbcTemplate.query(sql, new RowMapper<VacantesSetup>() {

			@Override
			public VacantesSetup mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listVacantesSetup;
	}

	public VacantesSetup get(int id) {
		String sql = "select * from mat_vacantes_setup WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<VacantesSetup>() {

			@Override
			public VacantesSetup extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public VacantesSetup getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select mci.id mci_id, mci.id_au mci_id_au , mci.vacantes mci_vacantes  ,mci.est mci_est ";
		if (aTablas.contains("cat_nivel"))
			sql = sql + ", nvl.id nvl_id  , nvl.cod_mod nvl_cod_mod , nvl.nom nvl_nom  ";
	
		sql = sql + " from mat_vacantes_setup mci "; 
		if (aTablas.contains("cat_nivel"))
			sql = sql + " left join cat_nivel nvl on nvl.id = mci.id_au ";
		sql = sql + " where mci.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<VacantesSetup>() {
		
			@Override
			public VacantesSetup extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					VacantesSetup vacantessetup= rsToEntity(rs,"mci_");
					if (aTablas.contains("cat_nivel")){
						Nivel nivel = new Nivel();  
							nivel.setId(rs.getInt("nvl_id")) ;  
							nivel.setCod_mod(rs.getString("nvl_cod_mod")) ;  
							nivel.setNom(rs.getString("nvl_nom")) ;  
							vacantessetup.setNivel(nivel);
					}
							return vacantessetup;
				}
				
				return null;
			}
			
		});


	}		
	
	public VacantesSetup getByParams(Param param) {

		String sql = "select * from mat_vacantes_setup " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<VacantesSetup>() {
			@Override
			public VacantesSetup extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<VacantesSetup> listByParams(Param param, String[] order) {

		String sql = "select * from mat_vacantes_setup " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<VacantesSetup>() {

			@Override
			public VacantesSetup mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<VacantesSetup> listFullByParams(VacantesSetup vacantessetup, String[] order) {
	
		return listFullByParams(Param.toParam("mci",vacantessetup), order);
	
	}	
	
	public List<VacantesSetup> listFullByParams(Param param, String[] order) {

		String sql = "select mci.id mci_id, mci.id_au mci_id_au , mci.vacantes mci_vacantes  ,mci.est mci_est ";
		sql = sql + ", nvl.id nvl_id  , nvl.cod_mod nvl_cod_mod , nvl.nom nvl_nom  ";
		sql = sql + " from mat_vacantes_setup mci";
		sql = sql + " left join cat_nivel nvl on nvl.id = mci.id_au ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<VacantesSetup>() {

			@Override
			public VacantesSetup mapRow(ResultSet rs, int rowNum) throws SQLException {
				VacantesSetup vacantessetup= rsToEntity(rs,"mci_");
				Nivel nivel = new Nivel();  
				nivel.setId(rs.getInt("nvl_id")) ;  
				nivel.setCod_mod(rs.getString("nvl_cod_mod")) ;  
				nivel.setNom(rs.getString("nvl_nom")) ;  
				vacantessetup.setNivel(nivel);
				return vacantessetup;
			}

		});

	}	




	// funciones privadas utilitarias para VacantesSetup

	private VacantesSetup rsToEntity(ResultSet rs,String alias) throws SQLException {
		VacantesSetup vacantes_setup = new VacantesSetup();

		vacantes_setup.setId(rs.getInt( alias + "id"));
		vacantes_setup.setId_au(rs.getInt( alias + "id_au"));
		vacantes_setup.setVacantes(rs.getInt( alias + "vacantes"));
		vacantes_setup.setEst(rs.getString( alias + "est"));
								
		return vacantes_setup;

	}
	
}
