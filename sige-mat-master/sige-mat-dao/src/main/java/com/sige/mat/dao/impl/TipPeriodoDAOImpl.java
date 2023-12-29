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
import com.tesla.colegio.model.TipPeriodo;

import com.tesla.colegio.model.Periodo;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface TipPeriodoDAO.
 * @author MV
 *
 */
public class TipPeriodoDAOImpl{
	final static Logger logger = Logger.getLogger(TipPeriodoDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(TipPeriodo tip_periodo) {
		if (tip_periodo.getId() != null) {
			// update
			String sql = "UPDATE cat_tip_periodo "
						+ "SET nom=?, "
						+ "des=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			return jdbcTemplate.update(sql, 
						tip_periodo.getNom(),
						tip_periodo.getDes(),
						tip_periodo.getEst(),
						tip_periodo.getUsr_act(),
						new java.util.Date(),
						tip_periodo.getId()); 

		} else {
			// insert
			String sql = "insert into cat_tip_periodo ("
						+ "nom, "
						+ "des, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				tip_periodo.getNom(),
				tip_periodo.getDes(),
				tip_periodo.getEst(),
				tip_periodo.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	
	public void delete(int id) {
		String sql = "delete from cat_tip_periodo where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	
	public List<TipPeriodo> list() {
		String sql = "select * from cat_tip_periodo";
		
		//logger.info(sql);
		
		List<TipPeriodo> listTipPeriodo = jdbcTemplate.query(sql, new RowMapper<TipPeriodo>() {

			
			public TipPeriodo mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listTipPeriodo;
	}

	
	public TipPeriodo get(int id) {
		String sql = "select * from cat_tip_periodo WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<TipPeriodo>() {

			
			public TipPeriodo extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	
	public TipPeriodo getFull(int id, String tablas[]) {
		String sql = "select tpe.id tpe_id, tpe.nom tpe_nom , tpe.des tpe_des  ,tpe.est tpe_est ";
	
		sql = sql + " from cat_tip_periodo tpe "; 
		sql = sql + " where tpe.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<TipPeriodo>() {
		
			
			public TipPeriodo extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					TipPeriodo tipperiodo= rsToEntity(rs,"tpe_");
							return tipperiodo;
				}
				
				return null;
			}
			
		});


	}		
	
	
	public TipPeriodo getByParams(Param param) {

		String sql = "select * from cat_tip_periodo " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<TipPeriodo>() {
			
			public TipPeriodo extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	
	public List<TipPeriodo> listByParams(Param param, String[] order) {

		String sql = "select * from cat_tip_periodo " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<TipPeriodo>() {

			
			public TipPeriodo mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	
	public List<TipPeriodo> listFullByParams(TipPeriodo tipperiodo, String[] order) {
	
		return listFullByParams(Param.toParam("tpe",tipperiodo), order);
	
	}	
	
	
	public List<TipPeriodo> listFullByParams(Param param, String[] order) {

		String sql = "select tpe.id tpe_id, tpe.nom tpe_nom , tpe.des tpe_des  ,tpe.est tpe_est ";
		sql = sql + " from cat_tip_periodo tpe";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<TipPeriodo>() {

			
			public TipPeriodo mapRow(ResultSet rs, int rowNum) throws SQLException {
				TipPeriodo tipperiodo= rsToEntity(rs,"tpe_");
				return tipperiodo;
			}

		});

	}	


	public List<Periodo> getListPeriodo(Param param, String[] order) {
		String sql = "select * from per_periodo " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<Periodo>() {

			
			public Periodo mapRow(ResultSet rs, int rowNum) throws SQLException {
				Periodo periodo = new Periodo();

				periodo.setId(rs.getInt("id"));
				periodo.setId_srv(rs.getInt("id_srv"));
				periodo.setId_tpe(rs.getInt("id_tpe"));
				periodo.setFec_ini(rs.getDate("fec_ini"));
				periodo.setFec_fin(rs.getDate("fec_fin"));
				periodo.setFec_cie_mat(rs.getDate("fec_cie_mat"));
				//periodo.setAnio(rs.getInt("anio"));
				periodo.setEst(rs.getString("est"));
												
				return periodo;
			}

		});	
	}


	// funciones privadas utilitarias para TipPeriodo

	private TipPeriodo rsToEntity(ResultSet rs,String alias) throws SQLException {
		TipPeriodo tip_periodo = new TipPeriodo();

		tip_periodo.setId(rs.getInt( alias + "id"));
		tip_periodo.setNom(rs.getString( alias + "nom"));
		tip_periodo.setDes(rs.getString( alias + "des"));
		tip_periodo.setEst(rs.getString( alias + "est"));
								
		return tip_periodo;

	}
	
}
