package com.sige.mat.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Arrays;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import com.tesla.colegio.model.DisenioCurricular;

import com.tesla.colegio.model.Anio;
import com.tesla.colegio.model.DcnNivel;

import com.sige.web.security.TokenSeguridad;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface DisenioCurricularDAO.
 * @author MV
 *
 */
public class DisenioCurricularDAOImpl{
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@Autowired
	private TokenSeguridad tokenSeguridad;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(DisenioCurricular disenio_curricular) {
		if (disenio_curricular.getId() != null) {
			// update
			String sql = "UPDATE col_disenio_curricular "
						+ "SET id_anio=?, "
						+ "nom=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			System.out.println(sql);

			jdbcTemplate.update(sql, 
						disenio_curricular.getId_anio(),
						disenio_curricular.getNom(),
						disenio_curricular.getEst(),
						tokenSeguridad.getId(),
						new java.util.Date(),
						disenio_curricular.getId()); 
			return disenio_curricular.getId();

		} else {
			// insert
			String sql = "insert into col_disenio_curricular ("
						+ "id_anio, "
						+ "nom, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?)";

				System.out.println(sql);

				jdbcTemplate.update(sql, 
				disenio_curricular.getId_anio(),
				disenio_curricular.getNom(),
				disenio_curricular.getEst(),
				tokenSeguridad.getId(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from col_disenio_curricular where id=?";
		
		System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<DisenioCurricular> list() {
		String sql = "select * from col_disenio_curricular";
		
		System.out.println(sql);
		
		List<DisenioCurricular> listDisenioCurricular = jdbcTemplate.query(sql, new RowMapper<DisenioCurricular>() {

			@Override
			public DisenioCurricular mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listDisenioCurricular;
	}

	public DisenioCurricular get(int id) {
		String sql = "select * from col_disenio_curricular WHERE id=" + id;
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<DisenioCurricular>() {

			@Override
			public DisenioCurricular extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public DisenioCurricular getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select cdc.id cdc_id, cdc.id_anio cdc_id_anio , cdc.nom cdc_nom  ,cdc.est cdc_est ";
		if (aTablas.contains("col_anio"))
			sql = sql + ", anio.id anio_id  , anio.nom anio_nom  ";
	
		sql = sql + " from col_disenio_curricular cdc "; 
		if (aTablas.contains("col_anio"))
			sql = sql + " left join col_anio anio on anio.id = cdc.id_anio ";
		sql = sql + " where cdc.id= " + id; 
				
		System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<DisenioCurricular>() {
		
			@Override
			public DisenioCurricular extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					DisenioCurricular diseniocurricular= rsToEntity(rs,"cdc_");
					if (aTablas.contains("col_anio")){
						Anio anio = new Anio();  
							anio.setId(rs.getInt("anio_id")) ;  
							anio.setNom(rs.getString("anio_nom")) ;  
							diseniocurricular.setAnio(anio);
					}
							return diseniocurricular;
				}
				
				return null;
			}
			
		});


	}		
	
	public DisenioCurricular getByParams(Param param) {

		String sql = "select * from col_disenio_curricular " + SQLFrmkUtil.getWhere(param);
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<DisenioCurricular>() {
			@Override
			public DisenioCurricular extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<DisenioCurricular> listByParams(Param param, String[] order) {

		String sql = "select * from col_disenio_curricular " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<DisenioCurricular>() {

			@Override
			public DisenioCurricular mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<DisenioCurricular> listFullByParams(DisenioCurricular diseniocurricular, String[] order) {
	
		return listFullByParams(Param.toParam("cdc",diseniocurricular), order);
	
	}	
	
	public List<DisenioCurricular> listFullByParams(Param param, String[] order) {

		String sql = "select cdc.id cdc_id, cdc.id_anio cdc_id_anio , cdc.nom cdc_nom  ,cdc.est cdc_est ";
		sql = sql + ", anio.id anio_id  , anio.nom anio_nom  ";
		sql = sql + " from col_disenio_curricular cdc";
		sql = sql + " left join col_anio anio on anio.id = cdc.id_anio ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<DisenioCurricular>() {

			@Override
			public DisenioCurricular mapRow(ResultSet rs, int rowNum) throws SQLException {
				DisenioCurricular diseniocurricular= rsToEntity(rs,"cdc_");
				Anio anio = new Anio();  
				anio.setId(rs.getInt("anio_id")) ;  
				anio.setNom(rs.getString("anio_nom")) ;  
				diseniocurricular.setAnio(anio);
				return diseniocurricular;
			}

		});

	}	


	public List<DcnNivel> getListDcnNivel(Param param, String[] order) {
		String sql = "select * from aca_dcn_nivel " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<DcnNivel>() {

			@Override
			public DcnNivel mapRow(ResultSet rs, int rowNum) throws SQLException {
				DcnNivel dcn_nivel = new DcnNivel();

				dcn_nivel.setId(rs.getInt("id"));
				dcn_nivel.setId_niv(rs.getInt("id_niv"));
				dcn_nivel.setId_dcn(rs.getInt("id_dcn"));
				dcn_nivel.setEst(rs.getString("est"));
												
				return dcn_nivel;
			}

		});	
	}


	// funciones privadas utilitarias para DisenioCurricular

	private DisenioCurricular rsToEntity(ResultSet rs,String alias) throws SQLException {
		DisenioCurricular disenio_curricular = new DisenioCurricular();

		disenio_curricular.setId(rs.getInt( alias + "id"));
		disenio_curricular.setId_anio(rs.getInt( alias + "id_anio"));
		disenio_curricular.setNom(rs.getString( alias + "nom"));
		disenio_curricular.setEst(rs.getString( alias + "est"));
								
		return disenio_curricular;

	}
	
}
