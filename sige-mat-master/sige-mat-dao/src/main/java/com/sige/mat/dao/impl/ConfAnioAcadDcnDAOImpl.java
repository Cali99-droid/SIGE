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
import com.tesla.colegio.model.ConfAnioAcadDcn;

import com.tesla.colegio.model.Anio;
import com.tesla.colegio.model.DisenioCurricular;

import com.sige.web.security.TokenSeguridad;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface ConfAnioAcadDcnDAO.
 * @author MV
 *
 */
public class ConfAnioAcadDcnDAOImpl{
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
	public int saveOrUpdate(ConfAnioAcadDcn conf_anio_acad_dcn) {
		if (conf_anio_acad_dcn.getId() != null) {
			// update
			String sql = "UPDATE col_conf_anio_acad_dcn "
						+ "SET id_anio=?, "
						+ "id_dcn=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			System.out.println(sql);

			jdbcTemplate.update(sql, 
						conf_anio_acad_dcn.getId_anio(),
						conf_anio_acad_dcn.getId_dcn(),
						conf_anio_acad_dcn.getEst(),
						tokenSeguridad.getId(),
						new java.util.Date(),
						conf_anio_acad_dcn.getId()); 
			return conf_anio_acad_dcn.getId();

		} else {
			// insert
			String sql = "insert into col_conf_anio_acad_dcn ("
						+ "id_anio, "
						+ "id_dcn, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?)";

				System.out.println(sql);

				jdbcTemplate.update(sql, 
				conf_anio_acad_dcn.getId_anio(),
				conf_anio_acad_dcn.getId_dcn(),
				conf_anio_acad_dcn.getEst(),
				tokenSeguridad.getId(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from col_conf_anio_acad_dcn where id=?";
		
		System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<ConfAnioAcadDcn> list() {
		String sql = "select * from col_conf_anio_acad_dcn";
		
		System.out.println(sql);
		
		List<ConfAnioAcadDcn> listConfAnioAcadDcn = jdbcTemplate.query(sql, new RowMapper<ConfAnioAcadDcn>() {

			@Override
			public ConfAnioAcadDcn mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listConfAnioAcadDcn;
	}

	public ConfAnioAcadDcn get(int id) {
		String sql = "select * from col_conf_anio_acad_dcn WHERE id=" + id;
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<ConfAnioAcadDcn>() {

			@Override
			public ConfAnioAcadDcn extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public ConfAnioAcadDcn getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select cnf_dcn.id cnf_dcn_id, cnf_dcn.id_anio cnf_dcn_id_anio , cnf_dcn.id_dcn cnf_dcn_id_dcn  ,cnf_dcn.est cnf_dcn_est ";
		if (aTablas.contains("col_anio"))
			sql = sql + ", anio.id anio_id  , anio.nom anio_nom  ";
		if (aTablas.contains("col_disenio_curricular"))
			sql = sql + ", cdc.id cdc_id  , cdc.id_anio cdc_id_anio , cdc.nom cdc_nom  ";
	
		sql = sql + " from col_conf_anio_acad_dcn cnf_dcn "; 
		if (aTablas.contains("col_anio"))
			sql = sql + " left join col_anio anio on anio.id = cnf_dcn.id_anio ";
		if (aTablas.contains("col_disenio_curricular"))
			sql = sql + " left join col_disenio_curricular cdc on cdc.id = cnf_dcn.id_dcn ";
		sql = sql + " where cnf_dcn.id= " + id; 
				
		System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<ConfAnioAcadDcn>() {
		
			@Override
			public ConfAnioAcadDcn extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					ConfAnioAcadDcn confanioacaddcn= rsToEntity(rs,"cnf_dcn_");
					if (aTablas.contains("col_anio")){
						Anio anio = new Anio();  
							anio.setId(rs.getInt("anio_id")) ;  
							anio.setNom(rs.getString("anio_nom")) ;  
							confanioacaddcn.setAnio(anio);
					}
					if (aTablas.contains("col_disenio_curricular")){
						DisenioCurricular diseniocurricular = new DisenioCurricular();  
							diseniocurricular.setId(rs.getInt("cdc_id")) ;  
							diseniocurricular.setId_anio(rs.getInt("cdc_id_anio")) ;  
							diseniocurricular.setNom(rs.getString("cdc_nom")) ;  
							confanioacaddcn.setDisenioCurricular(diseniocurricular);
					}
							return confanioacaddcn;
				}
				
				return null;
			}
			
		});


	}		
	
	public ConfAnioAcadDcn getByParams(Param param) {

		String sql = "select * from col_conf_anio_acad_dcn " + SQLFrmkUtil.getWhere(param);
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<ConfAnioAcadDcn>() {
			@Override
			public ConfAnioAcadDcn extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<ConfAnioAcadDcn> listByParams(Param param, String[] order) {

		String sql = "select * from col_conf_anio_acad_dcn " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<ConfAnioAcadDcn>() {

			@Override
			public ConfAnioAcadDcn mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<ConfAnioAcadDcn> listFullByParams(ConfAnioAcadDcn confanioacaddcn, String[] order) {
	
		return listFullByParams(Param.toParam("cnf_dcn",confanioacaddcn), order);
	
	}	
	
	public List<ConfAnioAcadDcn> listFullByParams(Param param, String[] order) {

		String sql = "select cnf_dcn.id cnf_dcn_id, cnf_dcn.id_anio cnf_dcn_id_anio , cnf_dcn.id_dcn cnf_dcn_id_dcn  ,cnf_dcn.est cnf_dcn_est ";
		sql = sql + ", anio.id anio_id  , anio.nom anio_nom  ";
		sql = sql + ", cdc.id cdc_id  , cdc.id_anio cdc_id_anio , cdc.nom cdc_nom  ";
		sql = sql + " from col_conf_anio_acad_dcn cnf_dcn";
		sql = sql + " left join col_anio anio on anio.id = cnf_dcn.id_anio ";
		sql = sql + " left join col_disenio_curricular cdc on cdc.id = cnf_dcn.id_dcn ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<ConfAnioAcadDcn>() {

			@Override
			public ConfAnioAcadDcn mapRow(ResultSet rs, int rowNum) throws SQLException {
				ConfAnioAcadDcn confanioacaddcn= rsToEntity(rs,"cnf_dcn_");
				Anio anio = new Anio();  
				anio.setId(rs.getInt("anio_id")) ;  
				anio.setNom(rs.getString("anio_nom")) ;  
				confanioacaddcn.setAnio(anio);
				DisenioCurricular diseniocurricular = new DisenioCurricular();  
				diseniocurricular.setId(rs.getInt("cdc_id")) ;  
				diseniocurricular.setId_anio(rs.getInt("cdc_id_anio")) ;  
				diseniocurricular.setNom(rs.getString("cdc_nom")) ;  
				confanioacaddcn.setDisenioCurricular(diseniocurricular);
				return confanioacaddcn;
			}

		});

	}	




	// funciones privadas utilitarias para ConfAnioAcadDcn

	private ConfAnioAcadDcn rsToEntity(ResultSet rs,String alias) throws SQLException {
		ConfAnioAcadDcn conf_anio_acad_dcn = new ConfAnioAcadDcn();

		conf_anio_acad_dcn.setId(rs.getInt( alias + "id"));
		conf_anio_acad_dcn.setId_anio(rs.getInt( alias + "id_anio"));
		conf_anio_acad_dcn.setId_dcn(rs.getInt( alias + "id_dcn"));
		conf_anio_acad_dcn.setEst(rs.getString( alias + "est"));
								
		return conf_anio_acad_dcn;

	}
	
}
