package com.sige.mat.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import com.tesla.colegio.model.GrupoConfig;

import com.tesla.colegio.model.GrupoAulaVirtual;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface GrupoConfigDAO.
 * @author MV
 *
 */
public class GrupoConfigDAOImpl{
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(GrupoConfig grupo_config) {
		if (grupo_config.getId() != null) {
			// update
			String sql = "UPDATE cvi_grupo_config "
						+ "SET cap=?, "
						+ "des=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//System.out.println(sql);

			jdbcTemplate.update(sql, 
						grupo_config.getCap(),
						grupo_config.getDes(),
						grupo_config.getEst(),
						grupo_config.getUsr_act(),
						new java.util.Date(),
						grupo_config.getId()); 
			return grupo_config.getId();

		} else {
			// insert
			String sql = "insert into cvi_grupo_config ("
						+ "cap, "
						+ "des, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?)";

				//System.out.println(sql);

				jdbcTemplate.update(sql, 
				grupo_config.getCap(),
				grupo_config.getDes(),
				grupo_config.getEst(),
				grupo_config.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from cvi_grupo_config where id=?";
		
		//System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<GrupoConfig> list() {
		String sql = "select * from cvi_grupo_config";
		
		//System.out.println(sql);
		
		List<GrupoConfig> listGrupoConfig = jdbcTemplate.query(sql, new RowMapper<GrupoConfig>() {

			@Override
			public GrupoConfig mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listGrupoConfig;
	}

	public GrupoConfig get(int id) {
		String sql = "select * from cvi_grupo_config WHERE id=" + id;
		
		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<GrupoConfig>() {

			@Override
			public GrupoConfig extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public GrupoConfig getFull(int id, String tablas[]) {
		String sql = "select cgc.id cgc_id, cgc.cap cgc_cap , cgc.des cgc_des  ,cgc.est cgc_est ";
	
		sql = sql + " from cvi_grupo_config cgc "; 
		sql = sql + " where cgc.id= " + id; 
				
		//System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<GrupoConfig>() {
		
			@Override
			public GrupoConfig extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					GrupoConfig grupoconfig= rsToEntity(rs,"cgc_");
							return grupoconfig;
				}
				
				return null;
			}
			
		});


	}		
	
	public GrupoConfig getByParams(Param param) {

		String sql = "select * from cvi_grupo_config " + SQLFrmkUtil.getWhere(param);
		
		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<GrupoConfig>() {
			@Override
			public GrupoConfig extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<GrupoConfig> listByParams(Param param, String[] order) {

		String sql = "select * from cvi_grupo_config " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<GrupoConfig>() {

			@Override
			public GrupoConfig mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<GrupoConfig> listFullByParams(GrupoConfig grupoconfig, String[] order) {
	
		return listFullByParams(Param.toParam("cgc",grupoconfig), order);
	
	}	
	
	public List<GrupoConfig> listFullByParams(Param param, String[] order) {

		String sql = "select cgc.id cgc_id, cgc.cap cgc_cap , cgc.des cgc_des  ,cgc.est cgc_est ";
		sql = sql + " from cvi_grupo_config cgc";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<GrupoConfig>() {

			@Override
			public GrupoConfig mapRow(ResultSet rs, int rowNum) throws SQLException {
				GrupoConfig grupoconfig= rsToEntity(rs,"cgc_");
				return grupoconfig;
			}

		});

	}	


	public List<GrupoAulaVirtual> getListGrupo(Param param, String[] order) {
		String sql = "select * from cvi_grupo " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<GrupoAulaVirtual>() {

			@Override
			public GrupoAulaVirtual mapRow(ResultSet rs, int rowNum) throws SQLException {
				GrupoAulaVirtual grupo = new GrupoAulaVirtual();

				grupo.setId(rs.getInt("id"));
				grupo.setId_gra(rs.getInt("id_gra"));
				grupo.setId_cgc(rs.getInt("id_cgc"));
				grupo.setEst(rs.getString("est"));
												
				return grupo;
			}

		});	
	}


	// funciones privadas utilitarias para GrupoConfig

	private GrupoConfig rsToEntity(ResultSet rs,String alias) throws SQLException {
		GrupoConfig grupo_config = new GrupoConfig();

		grupo_config.setId(rs.getInt( alias + "id"));
		grupo_config.setCap(rs.getString( alias + "cap"));
		grupo_config.setDes(rs.getString( alias + "des"));
		grupo_config.setEst(rs.getString( alias + "est"));
								
		return grupo_config;

	}
	
}
