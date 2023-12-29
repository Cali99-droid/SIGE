package com.sige.mat.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
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
import com.tesla.colegio.model.Banco;



import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface BancoDAO.
 * @author MV
 *
 */
public class BancoDAOImpl{
	
	final static Logger logger = Logger.getLogger(BancoDAOImpl.class);
	
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(Banco banco) {
		if (banco.getId() != null) {
			// update
			String sql = "UPDATE fac_banco "
						+ "SET nom=?, "
						+ "cod=?, "
						+ "nro_cta=?, "
						+ "moneda=?, "
						+ "tip_cta=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						banco.getNom(),
						banco.getCod(),
						banco.getNro_cta(),
						banco.getMoneda(),
						banco.getTip_cta(),
						banco.getEst(),
						banco.getUsr_act(),
						new java.util.Date(),
						banco.getId()); 
			return banco.getId();

		} else {
			// insert
			String sql = "insert into fac_banco ("
						+ "nom, "
						+ "cod, "
						+ "nro_cta, "
						+ "moneda, "
						+ "tip_cta, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				banco.getNom(),
				banco.getCod(),
				banco.getNro_cta(),
				banco.getMoneda(),
				banco.getTip_cta(),
				banco.getEst(),
				banco.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from fac_banco where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<Banco> list() {
		String sql = "select * from fac_banco";
		
		//logger.info(sql);
		
		List<Banco> listBanco = jdbcTemplate.query(sql, new RowMapper<Banco>() {

			@Override
			public Banco mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listBanco;
	}

	public Banco get(int id) {
		String sql = "select * from fac_banco WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Banco>() {

			@Override
			public Banco extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public Banco getFull(int id, String tablas[]) {
		String sql = "select fba.id fba_id, fba.nom fba_nom , fba.cod fba_cod , fba.nro_cta fba_nro_cta , fba.moneda fba_moneda , fba.tip_cta fba_tip_cta  ,fba.est fba_est ";
	
		sql = sql + " from fac_banco fba "; 
		sql = sql + " where fba.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<Banco>() {
		
			@Override
			public Banco extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Banco banco= rsToEntity(rs,"fba_");
							return banco;
				}
				
				return null;
			}
			
		});


	}		
	
	public Banco getByParams(Param param) {

		String sql = "select * from fac_banco " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Banco>() {
			@Override
			public Banco extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<Banco> listByParams(Param param, String[] order) {

		String sql = "select * from fac_banco " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Banco>() {

			@Override
			public Banco mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<Banco> listFullByParams(Banco banco, String[] order) {
	
		return listFullByParams(Param.toParam("fba",banco), order);
	
	}	
	
	public List<Banco> listFullByParams(Param param, String[] order) {

		String sql = "select fba.id fba_id, fba.nom fba_nom , fba.cod fba_cod , fba.nro_cta fba_nro_cta , fba.moneda fba_moneda , fba.tip_cta fba_tip_cta  ,fba.est fba_est ";
		sql = sql + " from fac_banco fba";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Banco>() {

			@Override
			public Banco mapRow(ResultSet rs, int rowNum) throws SQLException {
				Banco banco= rsToEntity(rs,"fba_");
				return banco;
			}

		});

	}	




	// funciones privadas utilitarias para Banco

	private Banco rsToEntity(ResultSet rs,String alias) throws SQLException {
		Banco banco = new Banco();

		banco.setId(rs.getInt( alias + "id"));
		banco.setNom(rs.getString( alias + "nom"));
		banco.setCod(rs.getString( alias + "cod"));
		banco.setNro_cta(rs.getString( alias + "nro_cta"));
		banco.setMoneda(rs.getString( alias + "moneda"));
		banco.setTip_cta(rs.getString( alias + "tip_cta"));
		banco.setEst(rs.getString( alias + "est"));
								
		return banco;

	}
	
}
