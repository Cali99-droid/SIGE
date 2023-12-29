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
import com.tesla.colegio.model.GiroNegocio;

import com.tesla.colegio.model.Empresa;
import com.tesla.colegio.model.GiroSucursal;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface GiroNegocioDAO.
 * @author MV
 *
 */
public class GiroNegocioDAOImpl{
	final static Logger logger = Logger.getLogger(GiroNegocioDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(GiroNegocio giro_negocio) {
		if (giro_negocio.getId() !=null) {
			// update
			String sql = "UPDATE ges_giro_negocio "
						+ "SET id_emp=?, "
						+ "nom=?, "
						+ "des=?, "
						+ "est=?,usr_act=?,fec_act=? "
					+ "WHERE id=?";
			
			//logger.info(sql);

			return jdbcTemplate.update(sql, 
						giro_negocio.getId_emp(),
						giro_negocio.getNom(),
						giro_negocio.getDes(),
						giro_negocio.getEst(),
						giro_negocio.getUsr_act(),
						new java.util.Date(),
						giro_negocio.getId()); 

		} else {
			// insert
			String sql = "insert into ges_giro_negocio ("
						+ "id_emp, "
						+ "nom, "
						+ "des, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				giro_negocio.getId_emp(),
				giro_negocio.getNom(),
				giro_negocio.getDes(),
				giro_negocio.getEst(),
				giro_negocio.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	
	public void delete(int id) {
		String sql = "delete from ges_giro_negocio where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	
	public List<GiroNegocio> list() {
		String sql = "select * from ges_giro_negocio";
		
		//logger.info(sql);
		
		List<GiroNegocio> listGiroNegocio = jdbcTemplate.query(sql, new RowMapper<GiroNegocio>() {

			
			public GiroNegocio mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listGiroNegocio;
	}

	
	public GiroNegocio get(int id) {
		String sql = "select * from ges_giro_negocio WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<GiroNegocio>() {

			
			public GiroNegocio extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	
	public GiroNegocio getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select gir.id gir_id, gir.id_emp gir_id_emp , gir.nom gir_nom , gir.des gir_des  ,gir.est gir_est ";
		if (aTablas.contains("ges_empresa"))
			sql = sql + ", emp.id emp_id  , emp.nom emp_nom , emp.raz_soc emp_raz_soc , emp.rep_leg emp_rep_leg , emp.ruc emp_ruc , emp.dir emp_dir , emp.tel emp_tel  ";
	
		sql = sql + " from ges_giro_negocio gir "; 
		if (aTablas.contains("ges_empresa"))
			sql = sql + " left join ges_empresa emp on emp.id = gir.id_emp ";
		sql = sql + " where gir.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<GiroNegocio>() {
		
			
			public GiroNegocio extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					GiroNegocio giroNegocio= rsToEntity(rs,"gir_");
					if (aTablas.contains("ges_empresa")){
						Empresa empresa = new Empresa();  
							empresa.setId(rs.getInt("emp_id")) ;  
							empresa.setNom(rs.getString("emp_nom")) ;  
							empresa.setRaz_soc(rs.getString("emp_raz_soc")) ;  
							empresa.setRep_leg(rs.getString("emp_rep_leg")) ;  
							empresa.setRuc(rs.getString("emp_ruc")) ;  
							empresa.setDir(rs.getString("emp_dir")) ;  
							empresa.setTel(rs.getString("emp_tel")) ;  
							giroNegocio.setEmpresa(empresa);
					}
							return giroNegocio;
				}
				
				return null;
			}
			
		});


	}		
	
	
	public GiroNegocio getByParams(Param param) {

		String sql = "select * from ges_giro_negocio " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<GiroNegocio>() {
			
			public GiroNegocio extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	
	public List<GiroNegocio> listByParams(Param param, String[] order) {

		String sql = "select * from ges_giro_negocio " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<GiroNegocio>() {

			
			public GiroNegocio mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	
	public List<GiroNegocio> listFullByParams(GiroNegocio giroNegocio, String[] order) {
	
		return listFullByParams(Param.toParam("gir",giroNegocio), order);
	
	}	
	
	
	public List<GiroNegocio> listFullByParams(Param param, String[] order) {

		String sql = "select gir.id gir_id, gir.id_emp gir_id_emp , gir.nom gir_nom , gir.des gir_des  ,gir.est gir_est ";
		sql = sql + ", emp.id emp_id  , emp.nom emp_nom , emp.raz_soc emp_raz_soc , emp.rep_leg emp_rep_leg , emp.ruc emp_ruc , emp.dir emp_dir , emp.tel emp_tel  ";
		sql = sql + " from ges_giro_negocio gir";
		sql = sql + " left join ges_empresa emp on emp.id = gir.id_emp ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<GiroNegocio>() {

			
			public GiroNegocio mapRow(ResultSet rs, int rowNum) throws SQLException {
				GiroNegocio giroNegocio= rsToEntity(rs,"gir_");
				Empresa empresa = new Empresa();  
				empresa.setId(rs.getInt("emp_id")) ;  
				empresa.setNom(rs.getString("emp_nom")) ;  
				empresa.setRaz_soc(rs.getString("emp_raz_soc")) ;  
				empresa.setRep_leg(rs.getString("emp_rep_leg")) ;  
				empresa.setRuc(rs.getString("emp_ruc")) ;  
				empresa.setDir(rs.getString("emp_dir")) ;  
				empresa.setTel(rs.getString("emp_tel")) ;  
				giroNegocio.setEmpresa(empresa);
				return giroNegocio;
			}

		});

	}	


	public List<GiroSucursal> getListGiroSucursal(Param param, String[] order) {
		String sql = "select * from ges_giro_sucursal " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<GiroSucursal>() {

			
			public GiroSucursal mapRow(ResultSet rs, int rowNum) throws SQLException {
				GiroSucursal giro_sucursal = new GiroSucursal();

				giro_sucursal.setId(rs.getInt("id"));
				giro_sucursal.setId_gir(rs.getInt("id_gir"));
				giro_sucursal.setId_suc(rs.getInt("id_suc"));
				giro_sucursal.setEst(rs.getString("est"));
												
				return giro_sucursal;
			}

		});	
	}


	// funciones privadas utilitarias para GiroNegocio

	private GiroNegocio rsToEntity(ResultSet rs,String alias) throws SQLException {
		GiroNegocio giro_negocio = new GiroNegocio();

		giro_negocio.setId(rs.getInt( alias + "id"));
		giro_negocio.setId_emp(rs.getInt( alias + "id_emp"));
		giro_negocio.setNom(rs.getString( alias + "nom"));
		giro_negocio.setDes(rs.getString( alias + "des"));
		giro_negocio.setEst(rs.getString( alias + "est"));
								
		return giro_negocio;

	}
	
}
