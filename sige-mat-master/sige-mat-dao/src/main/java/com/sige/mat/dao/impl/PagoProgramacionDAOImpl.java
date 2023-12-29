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
import com.tesla.colegio.model.PagoProgramacion;

import com.tesla.colegio.model.ConceptoPago;
import com.tesla.colegio.model.Periodo;
import com.tesla.colegio.model.PagoDetalle;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface PagoProgramacionDAO.
 * @author MV
 *
 */
public class PagoProgramacionDAOImpl{
	final static Logger logger = Logger.getLogger(PagoProgramacionDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(PagoProgramacion pago_programacion) {
		if (pago_programacion.getId() != null) {
			// update
			String sql = "UPDATE pag_pago_programacion "
						+ "SET id_cpa=?, "
						+ "id_per=?, "
						+ "monto=?, "
						+ "mes=?, "
						+ "fec=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			return jdbcTemplate.update(sql, 
						pago_programacion.getId_cpa(),
						pago_programacion.getId_per(),
						pago_programacion.getMonto(),
						pago_programacion.getMes(),
						pago_programacion.getFec(),
						pago_programacion.getEst(),
						pago_programacion.getUsr_act(),
						new java.util.Date(),
						pago_programacion.getId()); 

		} else {
			// insert
			String sql = "insert into pag_pago_programacion ("
						+ "id_cpa, "
						+ "id_per, "
						+ "monto, "
						+ "mes, "
						+ "fec, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				pago_programacion.getId_cpa(),
				pago_programacion.getId_per(),
				pago_programacion.getMonto(),
				pago_programacion.getMes(),
				pago_programacion.getFec(),
				pago_programacion.getEst(),
				pago_programacion.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	
	public void delete(int id) {
		String sql = "delete from pag_pago_programacion where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	
	public List<PagoProgramacion> list() {
		String sql = "select * from pag_pago_programacion";
		
		//logger.info(sql);
		
		List<PagoProgramacion> listPagoProgramacion = jdbcTemplate.query(sql, new RowMapper<PagoProgramacion>() {

			
			public PagoProgramacion mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listPagoProgramacion;
	}

	
	public PagoProgramacion get(int id) {
		String sql = "select * from pag_pago_programacion WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<PagoProgramacion>() {

			
			public PagoProgramacion extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	
	public PagoProgramacion getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select ppr.id ppr_id, ppr.id_cpa ppr_id_cpa , ppr.id_per ppr_id_per , ppr.monto ppr_monto , ppr.mes ppr_mes , ppr.fec ppr_fec  ,ppr.est ppr_est ";
		if (aTablas.contains("cat_concepto_pago"))
			sql = sql + ", cpa.id cpa_id  , cpa.nom cpa_nom  ";
		if (aTablas.contains("per_periodo"))
			sql = sql + ", pee.id pee_id  , pee.id_srv pee_id_srv , pee.id_tpe pee_id_tpe , pee.fec_ini pee_fec_ini , pee.fec_fin pee_fec_fin , pee.fec_cie_mat pee_fec_cie_mat , pee.anio pee_anio  ";
	
		sql = sql + " from pag_pago_programacion ppr "; 
		if (aTablas.contains("cat_concepto_pago"))
			sql = sql + " left join cat_concepto_pago cpa on cpa.id = ppr.id_cpa ";
		if (aTablas.contains("per_periodo"))
			sql = sql + " left join per_periodo pee on pee.id = ppr.id_per ";
		sql = sql + " where ppr.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<PagoProgramacion>() {
		
			
			public PagoProgramacion extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					PagoProgramacion pagoprogramacion= rsToEntity(rs,"ppr_");
					if (aTablas.contains("cat_concepto_pago")){
						ConceptoPago conceptopago = new ConceptoPago();  
							conceptopago.setId(rs.getInt("cpa_id")) ;  
							conceptopago.setNom(rs.getString("cpa_nom")) ;  
							pagoprogramacion.setConceptoPago(conceptopago);
					}
					if (aTablas.contains("per_periodo")){
						Periodo periodo = new Periodo();  
							periodo.setId(rs.getInt("pee_id")) ;  
							periodo.setId_srv(rs.getInt("pee_id_srv")) ;  
							periodo.setId_tpe(rs.getInt("pee_id_tpe")) ;  
							periodo.setFec_ini(rs.getDate("pee_fec_ini")) ;  
							periodo.setFec_fin(rs.getDate("pee_fec_fin")) ;  
							periodo.setFec_cie_mat(rs.getDate("pee_fec_cie_mat")) ;  
							//periodo.setAnio(rs.getInt("pee_anio")) ;  
							pagoprogramacion.setPeriodo(periodo);
					}
							return pagoprogramacion;
				}
				
				return null;
			}
			
		});


	}		
	
	
	public PagoProgramacion getByParams(Param param) {

		String sql = "select * from pag_pago_programacion " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<PagoProgramacion>() {
			
			public PagoProgramacion extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	
	public List<PagoProgramacion> listByParams(Param param, String[] order) {

		String sql = "select * from pag_pago_programacion " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<PagoProgramacion>() {

			
			public PagoProgramacion mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	
	public List<PagoProgramacion> listFullByParams(PagoProgramacion pagoprogramacion, String[] order) {
	
		return listFullByParams(Param.toParam("ppr",pagoprogramacion), order);
	
	}	
	
	
	public List<PagoProgramacion> listFullByParams(Param param, String[] order) {

		String sql = "select ppr.id ppr_id, ppr.id_cpa ppr_id_cpa , ppr.id_per ppr_id_per , ppr.monto ppr_monto , ppr.mes ppr_mes , ppr.fec ppr_fec  ,ppr.est ppr_est ";
		sql = sql + ", cpa.id cpa_id  , cpa.nom cpa_nom  ";
		sql = sql + ", pee.id pee_id  , pee.id_srv pee_id_srv , pee.id_tpe pee_id_tpe , pee.fec_ini pee_fec_ini , pee.fec_fin pee_fec_fin , pee.fec_cie_mat pee_fec_cie_mat , pee.anio pee_anio  ";
		sql = sql + " from pag_pago_programacion ppr";
		sql = sql + " left join cat_concepto_pago cpa on cpa.id = ppr.id_cpa ";
		sql = sql + " left join per_periodo pee on pee.id = ppr.id_per ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<PagoProgramacion>() {

			
			public PagoProgramacion mapRow(ResultSet rs, int rowNum) throws SQLException {
				PagoProgramacion pagoprogramacion= rsToEntity(rs,"ppr_");
				ConceptoPago conceptopago = new ConceptoPago();  
				conceptopago.setId(rs.getInt("cpa_id")) ;  
				conceptopago.setNom(rs.getString("cpa_nom")) ;  
				pagoprogramacion.setConceptoPago(conceptopago);
				Periodo periodo = new Periodo();  
				periodo.setId(rs.getInt("pee_id")) ;  
				periodo.setId_srv(rs.getInt("pee_id_srv")) ;  
				periodo.setId_tpe(rs.getInt("pee_id_tpe")) ;  
				periodo.setFec_ini(rs.getDate("pee_fec_ini")) ;  
				periodo.setFec_fin(rs.getDate("pee_fec_fin")) ;  
				periodo.setFec_cie_mat(rs.getDate("pee_fec_cie_mat")) ;  
				//periodo.setAnio(rs.getInt("pee_anio")) ;  
				pagoprogramacion.setPeriodo(periodo);
				return pagoprogramacion;
			}

		});

	}	


	public List<PagoDetalle> getListPagoDetalle(Param param, String[] order) {
		String sql = "select * from pag_pago_detalle " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<PagoDetalle>() {

			
			public PagoDetalle mapRow(ResultSet rs, int rowNum) throws SQLException {
				PagoDetalle pago_detalle = new PagoDetalle();

				pago_detalle.setId(rs.getInt("id"));
				pago_detalle.setId_pre(rs.getInt("id_pre"));
				pago_detalle.setId_ppr(rs.getInt("id_ppr"));
				pago_detalle.setId_pbco(rs.getInt("id_pbco"));
				pago_detalle.setMonto(rs.getBigDecimal("monto"));
				pago_detalle.setFec(rs.getDate("fec"));
				pago_detalle.setEst(rs.getString("est"));
												
				return pago_detalle;
			}

		});	
	}


	// funciones privadas utilitarias para PagoProgramacion

	private PagoProgramacion rsToEntity(ResultSet rs,String alias) throws SQLException {
		PagoProgramacion pago_programacion = new PagoProgramacion();

		pago_programacion.setId(rs.getInt( alias + "id"));
		pago_programacion.setId_cpa(rs.getInt( alias + "id_cpa"));
		pago_programacion.setId_per(rs.getInt( alias + "id_per"));
		pago_programacion.setMonto(rs.getBigDecimal( alias + "monto"));
		pago_programacion.setMes(rs.getString( alias + "mes"));
		pago_programacion.setFec(rs.getDate( alias + "fec"));
		pago_programacion.setEst(rs.getString( alias + "est"));
								
		return pago_programacion;

	}
	
}
