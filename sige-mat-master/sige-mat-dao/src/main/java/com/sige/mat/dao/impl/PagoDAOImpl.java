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
import com.tesla.colegio.model.Pago;

import com.tesla.colegio.model.PagoRealizado;
import com.tesla.colegio.model.PagoProgramacion;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface PagoDAO.
 * @author MV
 *
 */

public class PagoDAOImpl{
	final static Logger logger = Logger.getLogger(PagoDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(Pago pago) {
		if (pago.getId() > 0) {
			// update
			String sql = "UPDATE pag_pago "
						+ "SET id_pre=?, "
						+ "id_ppr=?, "
						+ "id_pbco=?, "
						+ "monto=?, "
						+ "fec=?, "
						+ "est=?,usr_act=?,fec_act=? "
					+ "WHERE id=?";
			
			//logger.info(sql);

			return jdbcTemplate.update(sql, 
						pago.getId_pre(),
						pago.getId_ppr(),
						pago.getId_pbco(),
						pago.getMonto(),
						pago.getFec(),
						pago.getEst(),
						pago.getUsr_act(),
						new java.util.Date(),
						pago.getId()); 

		} else {
			// insert
			String sql = "insert into pag_pago ("
						+ "id_pre, "
						+ "id_ppr, "
						+ "id_pbco, "
						+ "monto, "
						+ "fec, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				pago.getId_pre(),
				pago.getId_ppr(),
				pago.getId_pbco(),
				pago.getMonto(),
				pago.getFec(),
				pago.getEst(),
				pago.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	
	public void delete(int id) {
		String sql = "delete from pag_pago where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	
	public List<Pago> list() {
		String sql = "select * from pag_pago";
		
		//logger.info(sql);
		
		List<Pago> listPago = jdbcTemplate.query(sql, new RowMapper<Pago>() {

			
			public Pago mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listPago;
	}

	
	public Pago get(int id) {
		String sql = "select * from pag_pago WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Pago>() {

			
			public Pago extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	
	public Pago getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select ppa.id ppa_id, ppa.id_pre ppa_id_pre , ppa.id_ppr ppa_id_ppr , ppa.id_pbco ppa_id_pbco , ppa.monto ppa_monto , ppa.fec ppa_fec  ,ppa.est ppa_est ";
		if (aTablas.contains("pag_pago_realizado"))
			sql = sql + ", pag_rea.id pag_rea_id  , pag_rea.num_rec pag_rea_num_rec , pag_rea.id_mat pag_rea_id_mat  ";
		if (aTablas.contains("pag_pago_programacion"))
			sql = sql + ", ppr.id ppr_id  , ppr.id_cpa ppr_id_cpa , ppr.id_per ppr_id_per , ppr.monto ppr_monto , ppr.mes ppr_mes , ppr.fec ppr_fec  ";
	
		sql = sql + " from pag_pago ppa "; 
		if (aTablas.contains("pag_pago_realizado"))
			sql = sql + " left join pag_pago_realizado pag_rea on pag_rea.id = ppa.id_pre ";
		if (aTablas.contains("pag_pago_programacion"))
			sql = sql + " left join pag_pago_programacion ppr on ppr.id = ppa.id_ppr ";
		sql = sql + " where ppa.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<Pago>() {
		
			
			public Pago extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Pago pago= rsToEntity(rs,"ppa_");
					if (aTablas.contains("pag_pago_realizado")){
						PagoRealizado pagorealizado = new PagoRealizado();  
							pagorealizado.setId(rs.getInt("pag_rea_id")) ;  
							pagorealizado.setNum_rec(rs.getString("pag_rea_num_rec")) ;  
							pagorealizado.setId_mat(rs.getInt("pag_rea_id_mat")) ;  
							pago.setPagoRealizado(pagorealizado);
					}
					if (aTablas.contains("pag_pago_programacion")){
						PagoProgramacion pagoprogramacion = new PagoProgramacion();  
							pagoprogramacion.setId(rs.getInt("ppr_id")) ;  
							pagoprogramacion.setId_cpa(rs.getInt("ppr_id_cpa")) ;  
						//	pagoprogramacion.setId_per(rs.getString("ppr_id_per")) ;  
							pagoprogramacion.setMonto(rs.getBigDecimal("ppr_monto")) ;  
							pagoprogramacion.setMes(rs.getString("ppr_mes")) ;  
							pagoprogramacion.setFec(rs.getDate("ppr_fec")) ;  
							pago.setPagoProgramacion(pagoprogramacion);
					}
							return pago;
				}
				
				return null;
			}
			
		});


	}		
	
	
	public Pago getByParams(Param param) {

		String sql = "select * from pag_pago " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Pago>() {
			
			public Pago extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	
	public List<Pago> listByParams(Param param, String[] order) {

		String sql = "select * from pag_pago " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Pago>() {

			
			public Pago mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	
	public List<Pago> listFullByParams(Pago pago, String[] order) {
	
		return listFullByParams(Param.toParam("ppa",pago), order);
	
	}	
	
	
	public List<Pago> listFullByParams(Param param, String[] order) {

		String sql = "select ppa.id ppa_id, ppa.id_pre ppa_id_pre , ppa.id_ppr ppa_id_ppr , ppa.id_pbco ppa_id_pbco , ppa.monto ppa_monto , ppa.fec ppa_fec  ,ppa.est ppa_est ";
		sql = sql + ", pag_rea.id pag_rea_id  , pag_rea.num_rec pag_rea_num_rec , pag_rea.id_mat pag_rea_id_mat  ";
		sql = sql + ", ppr.id ppr_id  , ppr.id_cpa ppr_id_cpa , ppr.id_per ppr_id_per , ppr.monto ppr_monto , ppr.mes ppr_mes , ppr.fec ppr_fec  ";
		sql = sql + " from pag_pago ppa";
		sql = sql + " left join pag_pago_realizado pag_rea on pag_rea.id = ppa.id_pre ";
		sql = sql + " left join pag_pago_programacion ppr on ppr.id = ppa.id_ppr ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Pago>() {

			
			public Pago mapRow(ResultSet rs, int rowNum) throws SQLException {
				Pago pago= rsToEntity(rs,"ppa_");
				PagoRealizado pagorealizado = new PagoRealizado();  
				pagorealizado.setId(rs.getInt("pag_rea_id")) ;  
				pagorealizado.setNum_rec(rs.getString("pag_rea_num_rec")) ;  
				pagorealizado.setId_mat(rs.getInt("pag_rea_id_mat")) ;  
				pago.setPagoRealizado(pagorealizado);
				PagoProgramacion pagoprogramacion = new PagoProgramacion();  
				pagoprogramacion.setId(rs.getInt("ppr_id")) ;  
				pagoprogramacion.setId_cpa(rs.getInt("ppr_id_cpa")) ;  
				//pagoprogramacion.setId_per(rs.getString("ppr_id_per")) ;  
				pagoprogramacion.setMonto(rs.getBigDecimal("ppr_monto")) ;  
				pagoprogramacion.setMes(rs.getString("ppr_mes")) ;  
				pagoprogramacion.setFec(rs.getDate("ppr_fec")) ;  
				pago.setPagoProgramacion(pagoprogramacion);
				return pago;
			}

		});

	}	




	// funciones privadas utilitarias para Pago

	private Pago rsToEntity(ResultSet rs,String alias) throws SQLException {
		Pago pago = new Pago();

		pago.setId(rs.getInt( alias + "id"));
		pago.setId_pre(rs.getString( alias + "id_pre"));
		pago.setId_ppr(rs.getInt( alias + "id_ppr"));
		pago.setId_pbco(rs.getInt( alias + "id_pbco"));
		pago.setMonto(rs.getBigDecimal( alias + "monto"));
		pago.setFec(rs.getDate( alias + "fec"));
		pago.setEst(rs.getString( alias + "est"));
								
		return pago;

	}
	
	
	
}
