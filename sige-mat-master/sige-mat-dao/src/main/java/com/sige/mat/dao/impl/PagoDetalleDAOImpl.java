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
import com.tesla.colegio.model.PagoDetalle;

import com.tesla.colegio.model.PagoRealizado;
import com.tesla.colegio.model.PagoProgramacion;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface PagoDetalleDAO.
 * @author MV
 *
 */
public class PagoDetalleDAOImpl{
	final static Logger logger = Logger.getLogger(PagoDetalleDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(PagoDetalle pago_detalle) {
		if (pago_detalle.getId() != null) {
			// update
			String sql = "UPDATE pag_pago_detalle "
						+ "SET id_pre=?, "
						+ "id_ppr=?, "
						+ "id_pbco=?, "
						+ "monto=?, "
						+ "fec=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			return jdbcTemplate.update(sql, 
						pago_detalle.getId_pre(),
						pago_detalle.getId_ppr(),
						pago_detalle.getId_pbco(),
						pago_detalle.getMonto(),
						pago_detalle.getFec(),
						pago_detalle.getEst(),
						pago_detalle.getUsr_act(),
						new java.util.Date(),
						pago_detalle.getId()); 

		} else {
			// insert
			String sql = "insert into pag_pago_detalle ("
						+ "id_pre, "
						+ "id_ppr, "
						+ "id_pbco, "
						+ "monto, "
						+ "fec, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				pago_detalle.getId_pre(),
				pago_detalle.getId_ppr(),
				pago_detalle.getId_pbco(),
				pago_detalle.getMonto(),
				pago_detalle.getFec(),
				pago_detalle.getEst(),
				pago_detalle.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	
	public void delete(int id) {
		String sql = "delete from pag_pago_detalle where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	
	public List<PagoDetalle> list() {
		String sql = "select * from pag_pago_detalle";
		
		//logger.info(sql);
		
		List<PagoDetalle> listPagoDetalle = jdbcTemplate.query(sql, new RowMapper<PagoDetalle>() {

			
			public PagoDetalle mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listPagoDetalle;
	}

	
	public PagoDetalle get(int id) {
		String sql = "select * from pag_pago_detalle WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<PagoDetalle>() {

			
			public PagoDetalle extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	
	public PagoDetalle getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select ppd.id ppd_id, ppd.id_pre ppd_id_pre , ppd.id_ppr ppd_id_ppr , ppd.id_pbco ppd_id_pbco , ppd.monto ppd_monto , ppd.fec ppd_fec  ,ppd.est ppd_est ";
		if (aTablas.contains("pag_pago_realizado"))
			sql = sql + ", pag_rea.id pag_rea_id  , pag_rea.num_rec pag_rea_num_rec , pag_rea.id_mat pag_rea_id_mat  ";
		if (aTablas.contains("pag_pago_programacion"))
			sql = sql + ", ppr.id ppr_id  , ppr.id_cpa ppr_id_cpa , ppr.id_per ppr_id_per , ppr.monto ppr_monto , ppr.mes ppr_mes , ppr.fec ppr_fec  ";
	
		sql = sql + " from pag_pago_detalle ppd "; 
		if (aTablas.contains("pag_pago_realizado"))
			sql = sql + " left join pag_pago_realizado pag_rea on pag_rea.id = ppd.id_pre ";
		if (aTablas.contains("pag_pago_programacion"))
			sql = sql + " left join pag_pago_programacion ppr on ppr.id = ppd.id_ppr ";
		sql = sql + " where ppd.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<PagoDetalle>() {
		
			
			public PagoDetalle extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					PagoDetalle pagodetalle= rsToEntity(rs,"ppd_");
					if (aTablas.contains("pag_pago_realizado")){
						PagoRealizado pagorealizado = new PagoRealizado();  
							pagorealizado.setId(rs.getInt("pag_rea_id")) ;  
							pagorealizado.setNum_rec(rs.getString("pag_rea_num_rec")) ;  
							pagorealizado.setId_mat(rs.getInt("pag_rea_id_mat")) ;  
							pagodetalle.setPagoRealizado(pagorealizado);
					}
					if (aTablas.contains("pag_pago_programacion")){
						PagoProgramacion pagoprogramacion = new PagoProgramacion();  
							pagoprogramacion.setId(rs.getInt("ppr_id")) ;  
							pagoprogramacion.setId_cpa(rs.getInt("ppr_id_cpa")) ;  
							pagoprogramacion.setId_per(rs.getInt("ppr_id_per")) ;  
							pagoprogramacion.setMonto(rs.getBigDecimal("ppr_monto")) ;  
							pagoprogramacion.setMes(rs.getString("ppr_mes")) ;  
							pagoprogramacion.setFec(rs.getDate("ppr_fec")) ;  
							pagodetalle.setPagoProgramacion(pagoprogramacion);
					}
							return pagodetalle;
				}
				
				return null;
			}
			
		});


	}		
	
	
	public PagoDetalle getByParams(Param param) {

		String sql = "select * from pag_pago_detalle " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<PagoDetalle>() {
			
			public PagoDetalle extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	
	public List<PagoDetalle> listByParams(Param param, String[] order) {

		String sql = "select * from pag_pago_detalle " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<PagoDetalle>() {

			
			public PagoDetalle mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	
	public List<PagoDetalle> listFullByParams(PagoDetalle pagodetalle, String[] order) {
	
		return listFullByParams(Param.toParam("ppd",pagodetalle), order);
	
	}	
	
	
	public List<PagoDetalle> listFullByParams(Param param, String[] order) {

		String sql = "select ppd.id ppd_id, ppd.id_pre ppd_id_pre , ppd.id_ppr ppd_id_ppr , ppd.id_pbco ppd_id_pbco , ppd.monto ppd_monto , ppd.fec ppd_fec  ,ppd.est ppd_est ";
		sql = sql + ", pag_rea.id pag_rea_id  , pag_rea.num_rec pag_rea_num_rec , pag_rea.id_mat pag_rea_id_mat  ";
		sql = sql + ", ppr.id ppr_id  , ppr.id_cpa ppr_id_cpa , ppr.id_per ppr_id_per , ppr.monto ppr_monto , ppr.mes ppr_mes , ppr.fec ppr_fec  ";
		sql = sql + " from pag_pago_detalle ppd";
		sql = sql + " left join pag_pago_realizado pag_rea on pag_rea.id = ppd.id_pre ";
		sql = sql + " left join pag_pago_programacion ppr on ppr.id = ppd.id_ppr ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<PagoDetalle>() {

			
			public PagoDetalle mapRow(ResultSet rs, int rowNum) throws SQLException {
				PagoDetalle pagodetalle= rsToEntity(rs,"ppd_");
				PagoRealizado pagorealizado = new PagoRealizado();  
				pagorealizado.setId(rs.getInt("pag_rea_id")) ;  
				pagorealizado.setNum_rec(rs.getString("pag_rea_num_rec")) ;  
				pagorealizado.setId_mat(rs.getInt("pag_rea_id_mat")) ;  
				pagodetalle.setPagoRealizado(pagorealizado);
				PagoProgramacion pagoprogramacion = new PagoProgramacion();  
				pagoprogramacion.setId(rs.getInt("ppr_id")) ;  
				pagoprogramacion.setId_cpa(rs.getInt("ppr_id_cpa")) ;  
				pagoprogramacion.setId_per(rs.getInt("ppr_id_per")) ;  
				pagoprogramacion.setMonto(rs.getBigDecimal("ppr_monto")) ;  
				pagoprogramacion.setMes(rs.getString("ppr_mes")) ;  
				pagoprogramacion.setFec(rs.getDate("ppr_fec")) ;  
				pagodetalle.setPagoProgramacion(pagoprogramacion);
				return pagodetalle;
			}

		});

	}	




	// funciones privadas utilitarias para PagoDetalle

	private PagoDetalle rsToEntity(ResultSet rs,String alias) throws SQLException {
		PagoDetalle pago_detalle = new PagoDetalle();

		pago_detalle.setId(rs.getInt( alias + "id"));
		pago_detalle.setId_pre(rs.getInt( alias + "id_pre"));
		pago_detalle.setId_ppr(rs.getInt( alias + "id_ppr"));
		pago_detalle.setId_pbco(rs.getInt( alias + "id_pbco"));
		pago_detalle.setMonto(rs.getBigDecimal( alias + "monto"));
		pago_detalle.setFec(rs.getDate( alias + "fec"));
		pago_detalle.setEst(rs.getString( alias + "est"));
								
		return pago_detalle;

	}
	
}
