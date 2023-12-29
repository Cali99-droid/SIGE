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

import com.tesla.colegio.model.Rec;
import com.tesla.colegio.model.Pago;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface RecDAO.
 * @author MV
 *
 */
public class RecDAOImpl{
	final static Logger logger = Logger.getLogger(RecDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(Rec rec) {
		if (rec.getId() > 0) {
			// update
			String sql = "UPDATE pag_rec "
						+ "SET id_pag=?, "
						+ "num_rec=?, "
						+ "loc=?, "
						+ "tipo=?, "
						+ "est=?,usr_act=?,fec_act=? "
					+ "WHERE id=?";
			
			//logger.info(sql);

			return jdbcTemplate.update(sql, 
						rec.getId_pag(),
						rec.getNum_rec(),
						rec.getLoc(),
						rec.getTipo(),
						rec.getEst(),
						rec.getUsr_act(),
						new java.util.Date(),
						rec.getId()); 

		} else {
			// insert
			String sql = "insert into pag_rec ("
						+ "id_pag, "
						+ "num_rec, "
						+ "loc, "
						+ "tipo, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				rec.getId_pag(),
				rec.getNum_rec(),
				rec.getLoc(),
				rec.getTipo(),
				rec.getEst(),
				rec.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	
	public void delete(int id) {
		String sql = "delete from pag_rec where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	
	public List<Rec> list() {
		String sql = "select * from pag_rec";
		
		//logger.info(sql);
		
		List<Rec> listRec = jdbcTemplate.query(sql, new RowMapper<Rec>() {

			
			public Rec mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listRec;
	}

	
	public Rec get(int id) {
		String sql = "select * from pag_rec WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Rec>() {

			
			public Rec extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	
	public Rec getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);

		String sql = "select rec.id rec_id, rec.id_pag rec_id_pag , rec.num_rec rec_num_rec , rec.loc rec_loc , rec.tipo rec_tipo  ,rec.est rec_est ";
		if (aTablas.contains("pag_pago"))
			sql = sql + ", ppa.id ppa_id  , ppa.id_ppr ppa_id_ppr , ppa.id_pbco ppa_id_pbco , ppa.monto ppa_monto , ppa.fec ppa_fec  ";
		sql = sql + " from pag_rec rec";
		if (aTablas.contains("pag_pago"))
			sql = sql + " left join pag_pago ppa on ppa.id = rec.id_pag ";

		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<Rec>() {
		
			
			public Rec extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Rec rec= rsToEntity(rs,"rec_");
					if (aTablas.contains("pag_pago")){
						Pago pago = new Pago();  
							pago.setId(rs.getInt("ppa_id")) ;  
							pago.setId_ppr(rs.getInt("ppa_id_ppr")) ;  
							pago.setId_pbco(rs.getInt("ppa_id_pbco")) ;  
							pago.setMonto(rs.getBigDecimal("ppa_monto")) ;  
							pago.setFec(rs.getDate("ppa_fec")) ;  
							rec.setPago(pago);
					}
							return rec;
				}
				
				return null;
			}
			
		});


	}		
	
	
	public Rec getByParams(Param param) {

		String sql = "select * from pag_rec " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Rec>() {
			
			public Rec extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	
	public List<Rec> listByParams(Param param, String[] order) {

		String sql = "select * from pag_rec " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Rec>() {

			
			public Rec mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	
	public List<Rec> listFullByParams(Rec rec, String[] order) {
	
		return listFullByParams(Param.toParam("rec",rec), order);
	
	}	
	
	
	public List<Rec> listFullByParams(Param param, String[] order) {

		String sql = "select rec.id rec_id, rec.id_pag rec_id_pag , rec.num_rec rec_num_rec , rec.loc rec_loc , rec.tipo rec_tipo  ,rec.est rec_est ";
		sql = sql + ", ppa.id ppa_id  , ppa.id_ppr ppa_id_ppr , ppa.id_pbco ppa_id_pbco , ppa.monto ppa_monto , ppa.fec ppa_fec  ";
		sql = sql + " from pag_rec rec";
		sql = sql + " left join pag_pago ppa on ppa.id = rec.id_pag ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Rec>() {

			
			public Rec mapRow(ResultSet rs, int rowNum) throws SQLException {
				Rec rec= rsToEntity(rs,"rec_");
				Pago pago = new Pago();  
				pago.setId(rs.getInt("ppa_id")) ;  
				pago.setId_ppr(rs.getInt("ppa_id_ppr")) ;  
				pago.setId_pbco(rs.getInt("ppa_id_pbco")) ;  
				pago.setMonto(rs.getBigDecimal("ppa_monto")) ;  
				pago.setFec(rs.getDate("ppa_fec")) ;  
				rec.setPago(pago);
				return rec;
			}

		});

	}	




	// funciones privadas utilitarias para Rec

	private Rec rsToEntity(ResultSet rs,String alias) throws SQLException {
		Rec rec = new Rec();

		rec.setId(rs.getInt( alias + "id"));
		rec.setId_pag(rs.getInt( alias + "id_pag"));
		rec.setNum_rec(rs.getString( alias + "num_rec"));
		rec.setLoc(rs.getString( alias + "loc"));
		rec.setTipo(rs.getString( alias + "tipo"));
		rec.setEst(rs.getString( alias + "est"));
								
		return rec;

	}
	
}
