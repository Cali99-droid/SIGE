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
import com.tesla.colegio.model.ConfContenido;

import com.tesla.colegio.model.Empresa;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface ConfContenidoDAO.
 * @author MV
 *
 */
public class ConfContenidoDAOImpl{
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(ConfContenido conf_contenido) {
		if (conf_contenido.getId() != null) {
			// update
			String sql = "UPDATE ges_conf_contenido "
						+ "SET id_emp=?, "
						+ "insignia=?, "
						+ "logotipo=?, "
						+ "lema=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//System.out.println(sql);

			jdbcTemplate.update(sql, 
						conf_contenido.getId_emp(),
						conf_contenido.getInsignia(),
						conf_contenido.getLogotipo(),
						conf_contenido.getLema(),
						conf_contenido.getEst(),
						conf_contenido.getUsr_act(),
						new java.util.Date(),
						conf_contenido.getId()); 
			return conf_contenido.getId();

		} else {
			// insert
			String sql = "insert into ges_conf_contenido ("
						+ "id_emp, "
						+ "insignia, "
						+ "logotipo, "
						+ "lema, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?)";

				//System.out.println(sql);

				jdbcTemplate.update(sql, 
				conf_contenido.getId_emp(),
				conf_contenido.getInsignia(),
				conf_contenido.getLogotipo(),
				conf_contenido.getLema(),
				conf_contenido.getEst(),
				conf_contenido.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from ges_conf_contenido where id=?";
		
		//System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<ConfContenido> list() {
		String sql = "select * from ges_conf_contenido";
		
		//System.out.println(sql);
		
		List<ConfContenido> listConfContenido = jdbcTemplate.query(sql, new RowMapper<ConfContenido>() {

			@Override
			public ConfContenido mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listConfContenido;
	}

	public ConfContenido get(int id) {
		String sql = "select * from ges_conf_contenido WHERE id=" + id;
		
		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<ConfContenido>() {

			@Override
			public ConfContenido extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public ConfContenido getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select cnf_contenido.id cnf_contenido_id, cnf_contenido.id_emp cnf_contenido_id_emp , cnf_contenido.insignia cnf_contenido_insignia , cnf_contenido.logotipo cnf_contenido_logotipo , cnf_contenido.lema cnf_contenido_lema  ,cnf_contenido.est cnf_contenido_est ";
		if (aTablas.contains("ges_empresa"))
			sql = sql + ", emp.id emp_id  , emp.nom emp_nom , emp.raz_soc emp_raz_soc , emp.rep_leg emp_rep_leg , emp.ruc emp_ruc , emp.dir emp_dir , emp.tel emp_tel , emp.pagina_web emp_pagina_web  ";
	
		sql = sql + " from ges_conf_contenido cnf_contenido "; 
		if (aTablas.contains("ges_empresa"))
			sql = sql + " left join ges_empresa emp on emp.id = cnf_contenido.id_emp ";
		sql = sql + " where cnf_contenido.id= " + id; 
				
		//System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<ConfContenido>() {
		
			@Override
			public ConfContenido extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					ConfContenido confcontenido= rsToEntity(rs,"cnf_contenido_");
					if (aTablas.contains("ges_empresa")){
						Empresa empresa = new Empresa();  
							empresa.setId(rs.getInt("emp_id")) ;  
							empresa.setNom(rs.getString("emp_nom")) ;  
							empresa.setRaz_soc(rs.getString("emp_raz_soc")) ;  
							empresa.setRep_leg(rs.getString("emp_rep_leg")) ;  
							empresa.setRuc(rs.getString("emp_ruc")) ;  
							empresa.setDir(rs.getString("emp_dir")) ;  
							empresa.setTel(rs.getString("emp_tel")) ;  
							//empresa.setPagina_web(rs.getString("emp_pagina_web")) ;  
							confcontenido.setEmpresa(empresa);
					}
							return confcontenido;
				}
				
				return null;
			}
			
		});


	}		
	
	public ConfContenido getByParams(Param param) {

		String sql = "select * from ges_conf_contenido " + SQLFrmkUtil.getWhere(param);
		
		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<ConfContenido>() {
			@Override
			public ConfContenido extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<ConfContenido> listByParams(Param param, String[] order) {

		String sql = "select * from ges_conf_contenido " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<ConfContenido>() {

			@Override
			public ConfContenido mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<ConfContenido> listFullByParams(ConfContenido confcontenido, String[] order) {
	
		return listFullByParams(Param.toParam("cnf_contenido",confcontenido), order);
	
	}	
	
	public List<ConfContenido> listFullByParams(Param param, String[] order) {

		String sql = "select cnf_contenido.id cnf_contenido_id, cnf_contenido.id_emp cnf_contenido_id_emp , cnf_contenido.insignia cnf_contenido_insignia , cnf_contenido.logotipo cnf_contenido_logotipo , cnf_contenido.lema cnf_contenido_lema  ,cnf_contenido.est cnf_contenido_est ";
		sql = sql + ", emp.id emp_id  , emp.nom emp_nom , emp.raz_soc emp_raz_soc , emp.rep_leg emp_rep_leg , emp.ruc emp_ruc , emp.dir emp_dir , emp.tel emp_tel , emp.pagina_web emp_pagina_web  ";
		sql = sql + " from ges_conf_contenido cnf_contenido";
		sql = sql + " left join ges_empresa emp on emp.id = cnf_contenido.id_emp ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<ConfContenido>() {

			@Override
			public ConfContenido mapRow(ResultSet rs, int rowNum) throws SQLException {
				ConfContenido confcontenido= rsToEntity(rs,"cnf_contenido_");
				Empresa empresa = new Empresa();  
				empresa.setId(rs.getInt("emp_id")) ;  
				empresa.setNom(rs.getString("emp_nom")) ;  
				empresa.setRaz_soc(rs.getString("emp_raz_soc")) ;  
				empresa.setRep_leg(rs.getString("emp_rep_leg")) ;  
				empresa.setRuc(rs.getString("emp_ruc")) ;  
				empresa.setDir(rs.getString("emp_dir")) ;  
				empresa.setTel(rs.getString("emp_tel")) ;  
				//empresa.setPagina_web(rs.getString("emp_pagina_web")) ;  
				confcontenido.setEmpresa(empresa);
				return confcontenido;
			}

		});

	}	




	// funciones privadas utilitarias para ConfContenido

	private ConfContenido rsToEntity(ResultSet rs,String alias) throws SQLException {
		ConfContenido conf_contenido = new ConfContenido();

		conf_contenido.setId(rs.getInt( alias + "id"));
		conf_contenido.setId_emp(rs.getInt( alias + "id_emp"));
		conf_contenido.setInsignia(rs.getString( alias + "insignia"));
		conf_contenido.setLogotipo(rs.getString( alias + "logotipo"));
		conf_contenido.setLema(rs.getString( alias + "lema"));
		conf_contenido.setEst(rs.getString( alias + "est"));
								
		return conf_contenido;

	}
	
}
