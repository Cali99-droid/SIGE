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
import com.tesla.colegio.model.PagoRealizado;

import com.tesla.colegio.model.Matricula;
import com.tesla.colegio.model.PagoDetalle;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface PagoRealizadoDAO.
 * @author MV
 *
 */
public class PagoRealizadoDAOImpl{
	final static Logger logger = Logger.getLogger(PagoRealizadoDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(PagoRealizado pago_realizado) {
		if (pago_realizado.getId() != null) {
			// update
			String sql = "UPDATE pag_pago_realizado "
						+ "SET num_rec=?, "
						+ "total=?, "
						+ "id_mat=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			return jdbcTemplate.update(sql, 
						pago_realizado.getNum_rec(),
						pago_realizado.getTotal(),
						pago_realizado.getId_mat(),
						pago_realizado.getEst(),
						pago_realizado.getUsr_act(),
						new java.util.Date(),
						pago_realizado.getId()); 

		} else {
			// insert
			String sql = "insert into pag_pago_realizado ("
						+ "num_rec, "
						+ "total, "
						+ "id_mat, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				pago_realizado.getNum_rec(),
				pago_realizado.getTotal(),
				pago_realizado.getId_mat(),
				pago_realizado.getEst(),
				pago_realizado.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	
	public void delete(int id) {
		String sql = "delete from pag_pago_realizado where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	
	public List<PagoRealizado> list() {
		String sql = "select * from pag_pago_realizado";
		
		//logger.info(sql);
		
		List<PagoRealizado> listPagoRealizado = jdbcTemplate.query(sql, new RowMapper<PagoRealizado>() {

			
			public PagoRealizado mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listPagoRealizado;
	}

	
	public PagoRealizado get(int id) {
		String sql = "select * from pag_pago_realizado WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<PagoRealizado>() {

			
			public PagoRealizado extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	
	public PagoRealizado getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select pag_rea.id pag_rea_id, pag_rea.num_rec pag_rea_num_rec , pag_rea.total pag_rea_total , pag_rea.id_mat pag_rea_id_mat  ,pag_rea.est pag_rea_est ";
		if (aTablas.contains("mat_matricula"))
			sql = sql + ", mat.id mat_id  , mat.id_alu mat_id_alu , mat.id_fam mat_id_fam , mat.id_enc mat_id_enc , mat.id_con mat_id_con , mat.id_cli mat_id_cli , mat.id_per mat_id_per , mat.id_au mat_id_au , mat.id_gra mat_id_gra , mat.id_niv mat_id_niv , mat.fecha mat_fecha , mat.car_pod mat_car_pod , mat.num_cont mat_num_cont  ";
	
		sql = sql + " from pag_pago_realizado pag_rea "; 
		if (aTablas.contains("mat_matricula"))
			sql = sql + " left join mat_matricula mat on mat.id = pag_rea.id_mat ";
		sql = sql + " where pag_rea.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<PagoRealizado>() {
		
			
			public PagoRealizado extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					PagoRealizado pagorealizado= rsToEntity(rs,"pag_rea_");
					if (aTablas.contains("mat_matricula")){
						Matricula matricula = new Matricula();  
							matricula.setId(rs.getInt("mat_id")) ;  
							matricula.setId_alu(rs.getInt("mat_id_alu")) ;  
							matricula.setId_fam(rs.getInt("mat_id_fam")) ;  
							matricula.setId_enc(rs.getInt("mat_id_enc")) ;  
							matricula.setId_con(rs.getInt("mat_id_con")) ;  
							matricula.setId_cli(rs.getInt("mat_id_cli")) ;  
							matricula.setId_per(rs.getInt("mat_id_per")) ;  
							matricula.setId_au(rs.getInt("mat_id_au")) ;  
							matricula.setId_gra(rs.getInt("mat_id_gra")) ;  
							matricula.setId_niv(rs.getInt("mat_id_niv")) ;  
							matricula.setFecha(rs.getDate("mat_fecha")) ;  
							matricula.setCar_pod(rs.getString("mat_car_pod")) ;  
							matricula.setNum_cont(rs.getString("mat_num_cont")) ;  
							pagorealizado.setMatricula(matricula);
					}
							return pagorealizado;
				}
				
				return null;
			}
			
		});


	}		
	
	
	public PagoRealizado getByParams(Param param) {

		String sql = "select * from pag_pago_realizado " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<PagoRealizado>() {
			
			public PagoRealizado extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	
	public List<PagoRealizado> listByParams(Param param, String[] order) {

		String sql = "select * from pag_pago_realizado " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<PagoRealizado>() {

			
			public PagoRealizado mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	
	public List<PagoRealizado> listFullByParams(PagoRealizado pagorealizado, String[] order) {
	
		return listFullByParams(Param.toParam("pag_rea",pagorealizado), order);
	
	}	
	
	
	public List<PagoRealizado> listFullByParams(Param param, String[] order) {

		String sql = "select pag_rea.id pag_rea_id, pag_rea.num_rec pag_rea_num_rec , pag_rea.total pag_rea_total , pag_rea.id_mat pag_rea_id_mat  ,pag_rea.est pag_rea_est ";
		sql = sql + ", mat.id mat_id  , mat.id_alu mat_id_alu , mat.id_fam mat_id_fam , mat.id_enc mat_id_enc , mat.id_con mat_id_con , mat.id_cli mat_id_cli , mat.id_per mat_id_per , mat.id_au mat_id_au , mat.id_gra mat_id_gra , mat.id_niv mat_id_niv , mat.fecha mat_fecha , mat.car_pod mat_car_pod , mat.num_cont mat_num_cont  ";
		sql = sql + " from pag_pago_realizado pag_rea";
		sql = sql + " left join mat_matricula mat on mat.id = pag_rea.id_mat ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<PagoRealizado>() {

			
			public PagoRealizado mapRow(ResultSet rs, int rowNum) throws SQLException {
				PagoRealizado pagorealizado= rsToEntity(rs,"pag_rea_");
				Matricula matricula = new Matricula();  
				matricula.setId(rs.getInt("mat_id")) ;  
				matricula.setId_alu(rs.getInt("mat_id_alu")) ;  
				matricula.setId_fam(rs.getInt("mat_id_fam")) ;  
				matricula.setId_enc(rs.getInt("mat_id_enc")) ;  
				matricula.setId_con(rs.getInt("mat_id_con")) ;  
				matricula.setId_cli(rs.getInt("mat_id_cli")) ;  
				matricula.setId_per(rs.getInt("mat_id_per")) ;  
				matricula.setId_au(rs.getInt("mat_id_au")) ;  
				matricula.setId_gra(rs.getInt("mat_id_gra")) ;  
				matricula.setId_niv(rs.getInt("mat_id_niv")) ;  
				matricula.setFecha(rs.getDate("mat_fecha")) ;  
				matricula.setCar_pod(rs.getString("mat_car_pod")) ;  
				matricula.setNum_cont(rs.getString("mat_num_cont")) ;  
				pagorealizado.setMatricula(matricula);
				return pagorealizado;
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


	// funciones privadas utilitarias para PagoRealizado

	private PagoRealizado rsToEntity(ResultSet rs,String alias) throws SQLException {
		PagoRealizado pago_realizado = new PagoRealizado();

		pago_realizado.setId(rs.getInt( alias + "id"));
		pago_realizado.setNum_rec(rs.getString( alias + "num_rec"));
		pago_realizado.setTotal(rs.getBigDecimal( alias + "total"));
		pago_realizado.setId_mat(rs.getInt( alias + "id_mat"));
		pago_realizado.setEst(rs.getString( alias + "est"));
								
		return pago_realizado;

	}
	
}
