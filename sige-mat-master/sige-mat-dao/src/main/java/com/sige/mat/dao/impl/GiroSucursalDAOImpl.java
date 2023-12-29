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
import com.tesla.colegio.model.GiroSucursal;

import com.tesla.colegio.model.GiroNegocio;
import com.tesla.colegio.model.Sucursal;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface GiroSucursalDAO.
 * @author MV
 *
 */
public class GiroSucursalDAOImpl{
	final static Logger logger = Logger.getLogger(GiroSucursalDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(GiroSucursal giro_sucursal) {
		if (giro_sucursal.getId() > 0) {
			// update
			String sql = "UPDATE ges_giro_sucursal "
						+ "SET id_gir=?, "
						+ "id_suc=?, "
						+ "est=?,usr_act=?,fec_act=? "
					+ "WHERE id=?";
			
			//logger.info(sql);

			return jdbcTemplate.update(sql, 
						giro_sucursal.getId_gir(),
						giro_sucursal.getId_suc(),
						giro_sucursal.getEst(),
						giro_sucursal.getUsr_act(),
						new java.util.Date(),
						giro_sucursal.getId()); 

		} else {
			// insert
			String sql = "insert into ges_giro_sucursal ("
						+ "id_gir, "
						+ "id_suc, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				giro_sucursal.getId_gir(),
				giro_sucursal.getId_suc(),
				giro_sucursal.getEst(),
				giro_sucursal.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	
	public void delete(int id) {
		String sql = "delete from ges_giro_sucursal where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	
	public List<GiroSucursal> list() {
		String sql = "select * from ges_giro_sucursal";
		
		//logger.info(sql);
		
		List<GiroSucursal> listGiroSucursal = jdbcTemplate.query(sql, new RowMapper<GiroSucursal>() {

			
			public GiroSucursal mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listGiroSucursal;
	}

	
	public GiroSucursal get(int id) {
		String sql = "select * from ges_giro_sucursal WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<GiroSucursal>() {

			
			public GiroSucursal extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	
	public GiroSucursal getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select gsu.id gsu_id, gsu.id_gir gsu_id_gir , gsu.id_suc gsu_id_suc  ,gsu.est gsu_est ";
		if (aTablas.contains("ges_giro_negocio"))
			sql = sql + ", gir.id gir_id  , gir.id_emp gir_id_emp , gir.nom gir_nom , gir.des gir_des  ";
		if (aTablas.contains("ges_sucursal"))
			sql = sql + ", suc.id suc_id  , suc.nom suc_nom , suc.dir suc_dir , suc.tel suc_tel , suc.correo suc_correo  ";
	
		sql = sql + " from ges_giro_sucursal gsu "; 
		if (aTablas.contains("ges_giro_negocio"))
			sql = sql + " left join ges_giro_negocio gir on gir.id = gsu.id_gir ";
		if (aTablas.contains("ges_sucursal"))
			sql = sql + " left join ges_sucursal suc on suc.id = gsu.id_suc ";
		sql = sql + " where gsu.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<GiroSucursal>() {
		
			
			public GiroSucursal extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					GiroSucursal giroSucursal= rsToEntity(rs,"gsu_");
					if (aTablas.contains("ges_giro_negocio")){
						GiroNegocio giroNegocio = new GiroNegocio();  
							giroNegocio.setId(rs.getInt("gir_id")) ;  
							giroNegocio.setId_emp(rs.getInt("gir_id_emp")) ;  
							giroNegocio.setNom(rs.getString("gir_nom")) ;  
							giroNegocio.setDes(rs.getString("gir_des")) ;  
							giroSucursal.setGiroNegocio(giroNegocio);
					}
					if (aTablas.contains("ges_sucursal")){
						Sucursal sucursal = new Sucursal();  
							sucursal.setId(rs.getInt("suc_id")) ;  
							sucursal.setNom(rs.getString("suc_nom")) ;  
							sucursal.setDir(rs.getString("suc_dir")) ;  
							sucursal.setTel(rs.getString("suc_tel")) ;  
							sucursal.setCorreo(rs.getString("suc_correo")) ;  
							giroSucursal.setSucursal(sucursal);
					}
							return giroSucursal;
				}
				
				return null;
			}
			
		});


	}		
	
	
	public GiroSucursal getByParams(Param param) {

		String sql = "select * from ges_giro_sucursal " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<GiroSucursal>() {
			
			public GiroSucursal extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	
	public List<GiroSucursal> listByParams(Param param, String[] order) {

		String sql = "select * from ges_giro_sucursal " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<GiroSucursal>() {

			
			public GiroSucursal mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	
	public List<GiroSucursal> listFullByParams(GiroSucursal giroSucursal, String[] order) {
	
		return listFullByParams(Param.toParam("gsu",giroSucursal), order);
	
	}	
	
	
	public List<GiroSucursal> listFullByParams(Param param, String[] order) {

		String sql = "select gsu.id gsu_id, gsu.id_gir gsu_id_gir , gsu.id_suc gsu_id_suc  ,gsu.est gsu_est ";
		sql = sql + ", gir.id gir_id  , gir.id_emp gir_id_emp , gir.nom gir_nom , gir.des gir_des  ";
		sql = sql + ", suc.id suc_id  , suc.nom suc_nom , suc.dir suc_dir , suc.tel suc_tel , suc.correo suc_correo  ";
		sql = sql + " from ges_giro_sucursal gsu";
		sql = sql + " left join ges_giro_negocio gir on gir.id = gsu.id_gir ";
		sql = sql + " left join ges_sucursal suc on suc.id = gsu.id_suc ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<GiroSucursal>() {

			
			public GiroSucursal mapRow(ResultSet rs, int rowNum) throws SQLException {
				GiroSucursal giroSucursal= rsToEntity(rs,"gsu_");
				GiroNegocio giroNegocio = new GiroNegocio();  
				giroNegocio.setId(rs.getInt("gir_id")) ;  
				giroNegocio.setId_emp(rs.getInt("gir_id_emp")) ;  
				giroNegocio.setNom(rs.getString("gir_nom")) ;  
				giroNegocio.setDes(rs.getString("gir_des")) ;  
				giroSucursal.setGiroNegocio(giroNegocio);
				Sucursal sucursal = new Sucursal();  
				sucursal.setId(rs.getInt("suc_id")) ;  
				sucursal.setNom(rs.getString("suc_nom")) ;  
				sucursal.setDir(rs.getString("suc_dir")) ;  
				sucursal.setTel(rs.getString("suc_tel")) ;  
				sucursal.setCorreo(rs.getString("suc_correo")) ;  
				giroSucursal.setSucursal(sucursal);
				return giroSucursal;
			}

		});

	}	




	// funciones privadas utilitarias para GiroSucursal

	private GiroSucursal rsToEntity(ResultSet rs,String alias) throws SQLException {
		GiroSucursal giro_sucursal = new GiroSucursal();

		giro_sucursal.setId(rs.getInt( alias + "id"));
		giro_sucursal.setId_gir(rs.getInt( alias + "id_gir"));
		giro_sucursal.setId_suc(rs.getInt( alias + "id_suc"));
		giro_sucursal.setEst(rs.getString( alias + "est"));
								
		return giro_sucursal;

	}
	
}
