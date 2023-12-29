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
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import com.tesla.colegio.model.IndEva;

import com.tesla.colegio.model.Evaluacion;
import com.tesla.colegio.model.IndSub;
import com.tesla.colegio.model.Indicador;
import com.tesla.frmk.common.exceptions.DAOException;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface IndEvaDAO.
 * @author MV
 *
 */
public class IndEvaDAOImpl{
	final static Logger logger = Logger.getLogger(IndEvaDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	protected  NamedParameterJdbcTemplate namedParameterJdbcTemplate; 
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
		namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(IndEva ind_eva) {
		if (ind_eva.getId() != null) {
			// update
			String sql = "UPDATE not_ind_eva "
						+ "SET id_ne=?, "
						+ "id_ind=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						ind_eva.getId_ne(),
						ind_eva.getId_ind(),
						ind_eva.getEst(),
						ind_eva.getUsr_act(),
						new java.util.Date(),
						ind_eva.getId()); 
			return ind_eva.getId();

		} else {
			// insert
			String sql = "insert into not_ind_eva ("
						+ "id_ne, "
						+ "id_ind, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				ind_eva.getId_ne(),
				ind_eva.getId_ind(),
				ind_eva.getEst(),
				ind_eva.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}
	
	public void delete(int id) throws DAOException{
		String sql = "delete from not_ind_eva where id_ne=? ";
		
		//logger.info(sql);
		
		try {
			jdbcTemplate.update(sql, id);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new DAOException("Tiene notas relacionada, id_ne:" + id);
		}
	}
	
	public void deleteInd(int id_ne, int id_ind) {
		String sql = "delete from not_ind_eva where id_ne=? and id_ind=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id_ne, id_ind);
	}

	public List<IndEva> list() {
		String sql = "select * from not_ind_eva";
		
		//logger.info(sql);
		
		List<IndEva> listIndEva = jdbcTemplate.query(sql, new RowMapper<IndEva>() {

			@Override
			public IndEva mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listIndEva;
	}

	public IndEva get(int id) {
		String sql = "select * from not_ind_eva WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<IndEva>() {

			@Override
			public IndEva extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public IndEva getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select nie.id nie_id, nie.id_ne nie_id_ne , nie.id_ind nie_id_ind  ,nie.est nie_est ";
		if (aTablas.contains("not_evaluacion"))
			sql = sql + ", ne.id ne_id  , ne.id_cca ne_id_cca , ne.id_nte ne_id_nte , ne.ins ne_ins , ne.evi ne_evi , ne.fec_ini ne_fec_ini , ne.fec_fin ne_fec_fin  ";
		if (aTablas.contains("col_indicador"))
			sql = sql + ",ind.id ind_id   ";
	
		sql = sql + " from not_ind_eva nie "; 
		if (aTablas.contains("not_evaluacion"))
			sql = sql + " left join not_evaluacion ne on ne.id = nie.id_ne ";
		if (aTablas.contains("col_ind_sub"))
			sql = sql + " left join col_ind_sub cis on cis.id = nie.id_cis ";
		sql = sql + " where nie.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<IndEva>() {
		
			@Override
			public IndEva extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					IndEva indeva= rsToEntity(rs,"nie_");
					if (aTablas.contains("not_evaluacion")){
						Evaluacion evaluacion = new Evaluacion();  
							evaluacion.setId(rs.getInt("ne_id")) ;  
							evaluacion.setId_cca(rs.getInt("ne_id_cca")) ;  
							evaluacion.setId_nte(rs.getInt("ne_id_nte")) ;  
							evaluacion.setIns(rs.getString("ne_ins")) ;  
							evaluacion.setEvi(rs.getString("ne_evi")) ;  
							evaluacion.setFec_ini(rs.getDate("ne_fec_ini")) ;  
							evaluacion.setFec_fin(rs.getDate("ne_fec_fin")) ;  
							indeva.setEvaluacion(evaluacion);
					}
					if (aTablas.contains("col_indicador")){
						    Indicador indicador = new Indicador();
						    indicador.setId(rs.getInt("ind_id")) ;  
							//indeva.setIndSub(indsub);
					}
							return indeva;
				}
				
				return null;
			}
			
		});


	}		
	
	public IndEva getByParams(Param param) {

		String sql = "select * from not_ind_eva " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<IndEva>() {
			@Override
			public IndEva extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<IndEva> listByParams(Param param, String[] order) {

		String sql = "select * from not_ind_eva " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<IndEva>() {

			@Override
			public IndEva mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<IndEva> listFullByParams(IndEva indeva, String[] order) {
	
		return listFullByParams(Param.toParam("nie",indeva), order);
	
	}	
	
	public List<IndEva> listFullByParams(Param param, String[] order) {

		String sql = "select nie.id nie_id, nie.id_ne nie_id_ne , nie.id_ind nie_id_ind  ,nie.est nie_est ";
		sql = sql + ", ne.id ne_id  , ne.id_cca ne_id_cca , ne.id_nte ne_id_nte , ne.ins ne_ins , ne.evi ne_evi , ne.fec_ini ne_fec_ini , ne.fec_fin ne_fec_fin  ";
		sql = sql + ", ind.id ind_id ";
		sql = sql + " from not_ind_eva nie";
		sql = sql + " left join not_evaluacion ne on ne.id = nie.id_ne ";
		sql = sql + " left join col_indicador ind on ind.id = nie.id_ind ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<IndEva>() {

			@Override
			public IndEva mapRow(ResultSet rs, int rowNum) throws SQLException {
				IndEva indeva= rsToEntity(rs,"nie_");
				Evaluacion evaluacion = new Evaluacion();  
				evaluacion.setId(rs.getInt("ne_id")) ;  
				evaluacion.setId_cca(rs.getInt("ne_id_cca")) ;  
				evaluacion.setId_nte(rs.getInt("ne_id_nte")) ;  
				evaluacion.setIns(rs.getString("ne_ins")) ;  
				evaluacion.setEvi(rs.getString("ne_evi")) ;  
				evaluacion.setFec_ini(rs.getDate("ne_fec_ini")) ;  
				evaluacion.setFec_fin(rs.getDate("ne_fec_fin")) ;  
				indeva.setEvaluacion(evaluacion);
				Indicador indicador = new Indicador();  
				indicador.setId(rs.getInt("ind_id")) ;  
				return indeva;
			}

		});

	}	




	// funciones privadas utilitarias para IndEva

	private IndEva rsToEntity(ResultSet rs,String alias) throws SQLException {
		IndEva ind_eva = new IndEva();

		ind_eva.setId(rs.getInt( alias + "id"));
		ind_eva.setId_ne(rs.getInt( alias + "id_ne"));
		ind_eva.setId_ind(rs.getInt( alias + "id_ind"));
		ind_eva.setEst(rs.getString( alias + "est"));
								
		return ind_eva;

	}
	
}
