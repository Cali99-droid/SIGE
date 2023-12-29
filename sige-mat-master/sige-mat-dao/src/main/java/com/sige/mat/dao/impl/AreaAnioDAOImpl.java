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
import com.tesla.colegio.model.AreaAnio;

import com.tesla.colegio.model.Anio;
import com.tesla.colegio.model.Nivel;
import com.tesla.colegio.model.Area;
import com.tesla.colegio.model.CursoAnio;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface AreaAnioDAO.
 * @author MV
 *
 */
public class AreaAnioDAOImpl{
	final static Logger logger = Logger.getLogger(AreaAnioDAOImpl.class);
	
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(AreaAnio area_anio) {
		if (area_anio.getId() != null) {
			// update
			String sql = "UPDATE col_area_anio "
						+ "SET id_anio=?, "
						+ "id_niv=?, "
						+ "id_area=?, "
						+ "id_gra=?, "
						+ "id_adc=?, "
						+ "id_gir=?, "
						+ "id_tca=?, "
						+ "id_pro_per=?, "
						+ "id_pro_anu=?, "
						+ "ord=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						area_anio.getId_anio(),
						area_anio.getId_niv(),
						area_anio.getId_area(),
						area_anio.getId_gra(),
						area_anio.getId_adc(),
						area_anio.getId_gir(),
						area_anio.getId_tca(),
						area_anio.getId_pro_per(),
						area_anio.getId_pro_anu(),
						area_anio.getOrd(),
						area_anio.getEst(),
						area_anio.getUsr_act(),
						new java.util.Date(),
						area_anio.getId()); 
			return area_anio.getId();

		} else {
			// insert
			String sql = "insert into col_area_anio ("
						+ "id_anio, "
						+ "id_niv, "
						+ "id_area, "
						+ "id_gra, "
						+ "id_adc, "
						+ "id_gir, "
						+ "id_tca, "
						+ "id_pro_per, "
						+ "id_pro_anu, "
						+ "ord, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				area_anio.getId_anio(),
				area_anio.getId_niv(),
				area_anio.getId_area(),
				area_anio.getId_gra(),
				area_anio.getId_adc(),
				area_anio.getId_gir(),
				area_anio.getId_tca(),
				area_anio.getId_pro_per(),
				area_anio.getId_pro_anu(),
				area_anio.getOrd(),
				area_anio.getEst(),
				area_anio.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from col_area_anio where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<AreaAnio> list() {
		String sql = "select * from col_area_anio";
		
		//logger.info(sql);
		
		List<AreaAnio> listAreaAnio = jdbcTemplate.query(sql, new RowMapper<AreaAnio>() {

			@Override
			public AreaAnio mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listAreaAnio;
	}

	public AreaAnio get(int id) {
		String sql = "select * from col_area_anio WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<AreaAnio>() {

			@Override
			public AreaAnio extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public AreaAnio getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select caa.id caa_id, caa.id_anio caa_id_anio , caa.id_niv caa_id_niv , caa.id_area caa_id_area , caa.ord caa_ord  ,caa.est caa_est ";
		if (aTablas.contains("col_anio"))
			sql = sql + ", anio.id anio_id  , anio.nom anio_nom  ";
		if (aTablas.contains("cat_nivel"))
			sql = sql + ", niv.id niv_id  , niv.cod_mod niv_cod_mod , niv.nom niv_nom  ";
		if (aTablas.contains("cat_area"))
			sql = sql + ", area.id area_id  , area.nom area_nom , area.des area_des  ";
	
		sql = sql + " from col_area_anio caa "; 
		if (aTablas.contains("col_anio"))
			sql = sql + " left join col_anio anio on anio.id = caa.id_anio ";
		if (aTablas.contains("cat_nivel"))
			sql = sql + " left join cat_nivel niv on niv.id = caa.id_niv ";
		if (aTablas.contains("cat_area"))
			sql = sql + " left join cat_area area on area.id = caa.id_area ";
		sql = sql + " where caa.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<AreaAnio>() {
		
			@Override
			public AreaAnio extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					AreaAnio areaanio= rsToEntity(rs,"caa_");
					if (aTablas.contains("col_anio")){
						Anio anio = new Anio();  
							anio.setId(rs.getInt("anio_id")) ;  
							anio.setNom(rs.getString("anio_nom")) ;  
							areaanio.setAnio(anio);
					}
					if (aTablas.contains("cat_nivel")){
						Nivel nivel = new Nivel();  
							nivel.setId(rs.getInt("niv_id")) ;  
							nivel.setCod_mod(rs.getString("niv_cod_mod")) ;  
							nivel.setNom(rs.getString("niv_nom")) ;  
							areaanio.setNivel(nivel);
					}
					if (aTablas.contains("cat_area")){
						Area area = new Area();  
							area.setId(rs.getInt("area_id")) ;  
							area.setNom(rs.getString("area_nom")) ;  
							area.setDes(rs.getString("area_des")) ;  
							areaanio.setArea(area);
					}
							return areaanio;
				}
				
				return null;
			}
			
		});


	}		
	
	public AreaAnio getByParams(Param param) {

		String sql = "select * from col_area_anio " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<AreaAnio>() {
			@Override
			public AreaAnio extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<AreaAnio> listByParams(Param param, String[] order) {

		String sql = "select * from col_area_anio " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<AreaAnio>() {

			@Override
			public AreaAnio mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<AreaAnio> listFullByParams(AreaAnio areaanio, String[] order) {
	
		return listFullByParams(Param.toParam("caa",areaanio), order);
	
	}	
	
	public List<AreaAnio> listFullByParams(Param param, String[] order) {

		String sql = "select caa.id caa_id, caa.id_anio caa_id_anio , caa.id_niv caa_id_niv , caa.id_area caa_id_area , caa.ord caa_ord  ,caa.est caa_est ";
		sql = sql + ", anio.id anio_id  , anio.nom anio_nom  ";
		sql = sql + ", niv.id niv_id  , niv.cod_mod niv_cod_mod , niv.nom niv_nom  ";
		sql = sql + ", area.id area_id  , area.nom area_nom , area.des area_des  ";
		sql = sql + " from col_area_anio caa";
		sql = sql + " left join col_anio anio on anio.id = caa.id_anio ";
		sql = sql + " left join cat_nivel niv on niv.id = caa.id_niv ";
		sql = sql + " left join cat_area area on area.id = caa.id_area ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<AreaAnio>() {

			@Override
			public AreaAnio mapRow(ResultSet rs, int rowNum) throws SQLException {
				AreaAnio areaanio= rsToEntity(rs,"caa_");
				Anio anio = new Anio();  
				anio.setId(rs.getInt("anio_id")) ;  
				anio.setNom(rs.getString("anio_nom")) ;  
				areaanio.setAnio(anio);
				Nivel nivel = new Nivel();  
				nivel.setId(rs.getInt("niv_id")) ;  
				nivel.setCod_mod(rs.getString("niv_cod_mod")) ;  
				nivel.setNom(rs.getString("niv_nom")) ;  
				areaanio.setNivel(nivel);
				Area area = new Area();  
				area.setId(rs.getInt("area_id")) ;  
				area.setNom(rs.getString("area_nom")) ;  
				area.setDes(rs.getString("area_des")) ;  
				areaanio.setArea(area);
				return areaanio;
			}

		});

	}	


	public List<CursoAnio> getListCursoAnio(Param param, String[] order) {
		String sql = "select * from col_curso_anio " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<CursoAnio>() {

			@Override
			public CursoAnio mapRow(ResultSet rs, int rowNum) throws SQLException {
				CursoAnio curso_anio = new CursoAnio();

				curso_anio.setId(rs.getInt("id"));
				curso_anio.setId_per(rs.getInt("id_per"));
				curso_anio.setId_gra(rs.getInt("id_gra"));
				curso_anio.setId_caa(rs.getInt("id_caa"));
				curso_anio.setId_cur(rs.getInt("id_cur"));
				curso_anio.setPeso(rs.getInt("peso"));
				curso_anio.setOrden(rs.getInt("orden"));
				curso_anio.setFlg_prom(rs.getString("flg_prom"));
				curso_anio.setEst(rs.getString("est"));
												
				return curso_anio;
			}

		});	
	}


	// funciones privadas utilitarias para AreaAnio

	private AreaAnio rsToEntity(ResultSet rs,String alias) throws SQLException {
		AreaAnio area_anio = new AreaAnio();

		area_anio.setId(rs.getInt( alias + "id"));
		area_anio.setId_anio(rs.getInt( alias + "id_anio"));
		area_anio.setId_niv(rs.getInt( alias + "id_niv"));
		area_anio.setId_area(rs.getInt( alias + "id_area"));
		area_anio.setId_gra(rs.getInt( alias + "id_gra"));
		area_anio.setId_adc(rs.getInt( alias + "id_adc"));
		area_anio.setId_gir(rs.getInt( alias + "id_gir"));
		area_anio.setId_tca(rs.getInt( alias + "id_tca"));
		area_anio.setId_pro_per(rs.getInt( alias + "id_pro_per"));
		area_anio.setId_pro_anu(rs.getInt( alias + "id_pro_anu"));
		area_anio.setOrd(rs.getInt( alias + "ord"));
		area_anio.setEst(rs.getString( alias + "est"));
								
		return area_anio;

	}
	
}
