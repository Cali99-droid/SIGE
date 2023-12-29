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
import com.tesla.colegio.model.Area;

import com.tesla.colegio.model.AreaAnio;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface AreaDAO.
 * @author MV
 *
 */
public class AreaDAOImpl{
	
	final static Logger logger = Logger.getLogger(AreaDAOImpl.class);
	
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(Area area) {
		if (area.getId() != null) {
			// update
			String sql = "UPDATE cat_area "
						+ "SET nom=?, "
						+ "des=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						area.getNom(),
						area.getDes(),
						area.getEst(),
						area.getUsr_act(),
						new java.util.Date(),
						area.getId()); 
			return area.getId();

		} else {
			// insert
			String sql = "insert into cat_area ("
						+ "nom, "
						+ "des, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				area.getNom(),
				area.getDes(),
				area.getEst(),
				area.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from cat_area where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<Area> list() {
		String sql = "select * from cat_area";
		
		//logger.info(sql);
		
		List<Area> listArea = jdbcTemplate.query(sql, new RowMapper<Area>() {

			@Override
			public Area mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listArea;
	}

	public Area get(int id) {
		String sql = "select * from cat_area WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Area>() {

			@Override
			public Area extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public Area getFull(int id, String tablas[]) {
		String sql = "select area.id area_id, area.nom area_nom , area.des area_des  ,area.est area_est ";
	
		sql = sql + " from cat_area area "; 
		sql = sql + " where area.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<Area>() {
		
			@Override
			public Area extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Area area= rsToEntity(rs,"area_");
							return area;
				}
				
				return null;
			}
			
		});


	}		
	
	public Area getByParams(Param param) {

		String sql = "select * from cat_area " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Area>() {
			@Override
			public Area extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<Area> listByParams(Param param, String[] order) {

		String sql = "select * from cat_area " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Area>() {

			@Override
			public Area mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<Area> listFullByParams(Area area, String[] order) {
	
		return listFullByParams(Param.toParam("area",area), order);
	
	}	
	
	public List<Area> listFullByParams(Param param, String[] order) {

		String sql = "select area.id area_id, area.nom area_nom , area.des area_des  ,area.est area_est ";
		sql = sql + " from cat_area area";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Area>() {

			@Override
			public Area mapRow(ResultSet rs, int rowNum) throws SQLException {
				Area area= rsToEntity(rs,"area_");
				return area;
			}

		});

	}	


	public List<AreaAnio> getListAreaAnio(Param param, String[] order) {
		String sql = "select * from col_area_anio " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<AreaAnio>() {

			@Override
			public AreaAnio mapRow(ResultSet rs, int rowNum) throws SQLException {
				AreaAnio area_anio = new AreaAnio();

				area_anio.setId(rs.getInt("id"));
				area_anio.setId_anio(rs.getInt("id_anio"));
				area_anio.setId_niv(rs.getInt("id_niv"));
				area_anio.setId_area(rs.getInt("id_area"));
				area_anio.setOrd(rs.getInt("ord"));
				area_anio.setEst(rs.getString("est"));
												
				return area_anio;
			}

		});	
	}


	// funciones privadas utilitarias para Area

	private Area rsToEntity(ResultSet rs,String alias) throws SQLException {
		Area area = new Area();

		area.setId(rs.getInt( alias + "id"));
		area.setNom(rs.getString( alias + "nom"));
		area.setDes(rs.getString( alias + "des"));
		area.setEst(rs.getString( alias + "est"));
								
		return area;

	}
	
}
