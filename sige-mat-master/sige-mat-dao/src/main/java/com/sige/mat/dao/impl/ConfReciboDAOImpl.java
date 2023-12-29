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
import com.tesla.colegio.model.ConfRecibo;

import com.tesla.colegio.model.Sucursal;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface ConfReciboDAO.
 * @author MV
 *
 */
public class ConfReciboDAOImpl{
	final static Logger logger = Logger.getLogger(ConfReciboDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	//@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(ConfRecibo conf_recibo) {
		if (conf_recibo.getId() != null) {
			// update
			String sql = "UPDATE fac_conf_recibo "
						+ "SET id_suc=?, "
						+ "tipo=?, "
						+ "serie=?, "
						+ "numero=?, "
						+ "numero_nc=?, "
						+ "hasta=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						conf_recibo.getId_suc(),
						conf_recibo.getTipo(),
						conf_recibo.getSerie(),
						conf_recibo.getNumero(),
						conf_recibo.getNumero_nc(),
						conf_recibo.getHasta(),
						conf_recibo.getEst(),
						conf_recibo.getUsr_act(),
						new java.util.Date(),
						conf_recibo.getId()); 
			return conf_recibo.getId();

		} else {
			// insert
			String sql = "insert into fac_conf_recibo ("
						+ "id_suc, "
						+ "tipo, "
						+ "serie, "
						+ "numero, "
						+ "numero_nc, "
						+ "hasta, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				conf_recibo.getId_suc(),
				conf_recibo.getTipo(),
				conf_recibo.getSerie(),
				conf_recibo.getNumero(),
				conf_recibo.getNumero_nc(),
				conf_recibo.getHasta(),
				conf_recibo.getEst(),
				conf_recibo.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from fac_conf_recibo where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<ConfRecibo> list() {
		String sql = "select * from fac_conf_recibo";
		
		//logger.info(sql);
		
		List<ConfRecibo> listConfRecibo = jdbcTemplate.query(sql, new RowMapper<ConfRecibo>() {

			@Override
			public ConfRecibo mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listConfRecibo;
	}

	public ConfRecibo get(int id) {
		String sql = "select * from fac_conf_recibo WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<ConfRecibo>() {

			@Override
			public ConfRecibo extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public ConfRecibo getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select rco.id rco_id, rco.id_suc rco_id_suc , rco.tipo rco_tipo , rco.serie rco_serie , rco.numero rco_numero, rco.numero rco_numero_nc , rco.hasta rco_hasta  ,rco.est rco_est ";
		if (aTablas.contains("ges_sucursal"))
			sql = sql + ", suc.id suc_id  , suc.nom suc_nom , suc.dir suc_dir , suc.tel suc_tel , suc.correo suc_correo  ";
	
		sql = sql + " from fac_conf_recibo rco "; 
		if (aTablas.contains("ges_sucursal"))
			sql = sql + " left join ges_sucursal suc on suc.id = rco.id_suc ";
		sql = sql + " where rco.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<ConfRecibo>() {
		
			@Override
			public ConfRecibo extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					ConfRecibo confrecibo= rsToEntity(rs,"rco_");
					if (aTablas.contains("ges_sucursal")){
						Sucursal sucursal = new Sucursal();  
							sucursal.setId(rs.getInt("suc_id")) ;  
							sucursal.setNom(rs.getString("suc_nom")) ;  
							sucursal.setDir(rs.getString("suc_dir")) ;  
							sucursal.setTel(rs.getString("suc_tel")) ;  
							sucursal.setCorreo(rs.getString("suc_correo")) ;  
							confrecibo.setSucursal(sucursal);
					}
							return confrecibo;
				}
				
				return null;
			}
			
		});


	}		
	
	public ConfRecibo getByParams(Param param) {

		String sql = "select * from fac_conf_recibo " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<ConfRecibo>() {
			@Override
			public ConfRecibo extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<ConfRecibo> listByParams(Param param, String[] order) {

		String sql = "select * from fac_conf_recibo " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<ConfRecibo>() {

			@Override
			public ConfRecibo mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<ConfRecibo> listFullByParams(ConfRecibo confrecibo, String[] order) {
	
		return listFullByParams(Param.toParam("rco",confrecibo), order);
	
	}	
	
	public List<ConfRecibo> listFullByParams(Param param, String[] order) {

		String sql = "select rco.id rco_id, rco.id_suc rco_id_suc , rco.tipo rco_tipo , rco.serie rco_serie , rco.numero rco_numero, rco.numero rco_numero_nc , rco.hasta rco_hasta  ,rco.est rco_est  ";
		sql = sql + ", suc.id suc_id  , suc.nom suc_nom , suc.dir suc_dir , suc.tel suc_tel , suc.correo suc_correo  ";
		sql = sql + " from fac_conf_recibo rco";
		sql = sql + " left join ges_sucursal suc on suc.id = rco.id_suc ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<ConfRecibo>() {

			@Override
			public ConfRecibo mapRow(ResultSet rs, int rowNum) throws SQLException {
				ConfRecibo confrecibo= rsToEntity(rs,"rco_");
				Sucursal sucursal = new Sucursal();  
				sucursal.setId(rs.getInt("suc_id")) ;  
				sucursal.setNom(rs.getString("suc_nom")) ;  
				sucursal.setDir(rs.getString("suc_dir")) ;  
				sucursal.setTel(rs.getString("suc_tel")) ;  
				sucursal.setCorreo(rs.getString("suc_correo")) ;  
				confrecibo.setSucursal(sucursal);
				return confrecibo;
			}

		});

	}	




	// funciones privadas utilitarias para ConfRecibo

	private ConfRecibo rsToEntity(ResultSet rs,String alias) throws SQLException {
		ConfRecibo conf_recibo = new ConfRecibo();

		conf_recibo.setId(rs.getInt( alias + "id"));
		conf_recibo.setId_suc(rs.getInt( alias + "id_suc"));
		conf_recibo.setTipo(rs.getString( alias + "tipo"));
		conf_recibo.setSerie(rs.getString( alias + "serie"));
		conf_recibo.setNumero(rs.getInt( alias + "numero"));
		conf_recibo.setNumero_nc(rs.getInt( alias + "numero_nc"));
		conf_recibo.setHasta(rs.getInt( alias + "hasta"));
		conf_recibo.setEst(rs.getString( alias + "est"));
								
		return conf_recibo;

	}
	
}
