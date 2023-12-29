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
import com.tesla.colegio.model.ReglasNegocio;

import com.tesla.colegio.model.Empresa;
import com.tesla.colegio.model.Modulo;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface ReglasNegocioDAO.
 * @author MV
 *
 */
public class ReglasNegocioDAOImpl{
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(ReglasNegocio reglas_negocio) {
		if (reglas_negocio.getId() != null) {
			// update
			String sql = "UPDATE ges_reglas_negocio "
						+ "SET id_emp=?, "
						+ "id_mod=?, "
						+ "nom=?, "
						+ "cod=?, "
						+ "tipo_html=?, "
						+ "val=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//System.out.println(sql);

			jdbcTemplate.update(sql, 
						reglas_negocio.getId_emp(),
						reglas_negocio.getId_mod(),
						reglas_negocio.getNom(),
						reglas_negocio.getCod(),
						reglas_negocio.getTipo_html(),
						reglas_negocio.getVal(),
						reglas_negocio.getEst(),
						reglas_negocio.getUsr_act(),
						new java.util.Date(),
						reglas_negocio.getId()); 
			return reglas_negocio.getId();

		} else {
			// insert
			String sql = "insert into ges_reglas_negocio ("
						+ "id_emp, "
						+ "id_mod, "
						+ "nom, "
						+ "cod, "
						+ "tipo_html, "
						+ "val, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?, ?)";

				//System.out.println(sql);

				jdbcTemplate.update(sql, 
				reglas_negocio.getId_emp(),
				reglas_negocio.getId_mod(),
				reglas_negocio.getNom(),
				reglas_negocio.getCod(),
				reglas_negocio.getTipo_html(),
				reglas_negocio.getVal(),
				reglas_negocio.getEst(),
				reglas_negocio.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from ges_reglas_negocio where id=?";
		
		//System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<ReglasNegocio> list() {
		String sql = "select * from ges_reglas_negocio";
		
		//System.out.println(sql);
		
		List<ReglasNegocio> listReglasNegocio = jdbcTemplate.query(sql, new RowMapper<ReglasNegocio>() {

			@Override
			public ReglasNegocio mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listReglasNegocio;
	}

	public ReglasNegocio get(int id) {
		String sql = "select * from ges_reglas_negocio WHERE id=" + id;
		
		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<ReglasNegocio>() {

			@Override
			public ReglasNegocio extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public ReglasNegocio getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select col_reg.id col_reg_id, col_reg.id_emp col_reg_id_emp , col_reg.id_mod col_reg_id_mod , col_reg.nom col_reg_nom , col_reg.cod col_reg_cod , col_reg.tipo_html col_reg_tipo_html , col_reg.val col_reg_val  ,col_reg.est col_reg_est ";
		if (aTablas.contains("ges_empresa"))
			sql = sql + ", emp.id emp_id  , emp.nom emp_nom , emp.raz_soc emp_raz_soc , emp.rep_leg emp_rep_leg , emp.ruc emp_ruc , emp.dir emp_dir , emp.tel emp_tel , emp.corr emp_corr , emp.dominio emp_dominio , emp.pagina_web emp_pagina_web  ";
		if (aTablas.contains("mod_modulo"))
			sql = sql + ", md_modulo.id md_modulo_id  , md_modulo.nom md_modulo_nom , md_modulo.des md_modulo_des , md_modulo.cod md_modulo_cod  ";
	
		sql = sql + " from ges_reglas_negocio col_reg "; 
		if (aTablas.contains("ges_empresa"))
			sql = sql + " left join ges_empresa emp on emp.id = col_reg.id_emp ";
		if (aTablas.contains("mod_modulo"))
			sql = sql + " left join mod_modulo md_modulo on md_modulo.id = col_reg.id_mod ";
		sql = sql + " where col_reg.id= " + id; 
				
		//System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<ReglasNegocio>() {
		
			@Override
			public ReglasNegocio extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					ReglasNegocio reglasnegocio= rsToEntity(rs,"col_reg_");
					if (aTablas.contains("ges_empresa")){
						Empresa empresa = new Empresa();  
							empresa.setId(rs.getInt("emp_id")) ;  
							empresa.setNom(rs.getString("emp_nom")) ;  
							empresa.setRaz_soc(rs.getString("emp_raz_soc")) ;  
							empresa.setRep_leg(rs.getString("emp_rep_leg")) ;  
							empresa.setRuc(rs.getString("emp_ruc")) ;  
							empresa.setDir(rs.getString("emp_dir")) ;  
							empresa.setTel(rs.getString("emp_tel")) ;  
							empresa.setCorr(rs.getString("emp_corr")) ;  
							empresa.setDominio(rs.getString("emp_dominio")) ;  
							//empresa.setPagina_web(rs.getString("emp_pagina_web")) ;  
							reglasnegocio.setEmpresa(empresa);
					}
					if (aTablas.contains("mod_modulo")){
						Modulo modulo = new Modulo();  
							modulo.setId(rs.getInt("md_modulo_id")) ;  
							modulo.setNom(rs.getString("md_modulo_nom")) ;  
							modulo.setDes(rs.getString("md_modulo_des")) ;  
							modulo.setCod(rs.getString("md_modulo_cod")) ;  
							reglasnegocio.setModulo(modulo);
					}
							return reglasnegocio;
				}
				
				return null;
			}
			
		});


	}		
	
	public ReglasNegocio getByParams(Param param) {

		String sql = "select * from ges_reglas_negocio " + SQLFrmkUtil.getWhere(param);
		
		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<ReglasNegocio>() {
			@Override
			public ReglasNegocio extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<ReglasNegocio> listByParams(Param param, String[] order) {

		String sql = "select * from ges_reglas_negocio " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<ReglasNegocio>() {

			@Override
			public ReglasNegocio mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<ReglasNegocio> listFullByParams(ReglasNegocio reglasnegocio, String[] order) {
	
		return listFullByParams(Param.toParam("col_reg",reglasnegocio), order);
	
	}	
	
	public List<ReglasNegocio> listFullByParams(Param param, String[] order) {

		String sql = "select col_reg.id col_reg_id, col_reg.id_emp col_reg_id_emp , col_reg.id_mod col_reg_id_mod , col_reg.nom col_reg_nom , col_reg.cod col_reg_cod , col_reg.tipo_html col_reg_tipo_html , col_reg.val col_reg_val  ,col_reg.est col_reg_est ";
		sql = sql + ", emp.id emp_id  , emp.nom emp_nom , emp.raz_soc emp_raz_soc , emp.rep_leg emp_rep_leg , emp.ruc emp_ruc , emp.dir emp_dir , emp.tel emp_tel , emp.corr emp_corr , emp.dominio emp_dominio , emp.pagina_web emp_pagina_web  ";
		sql = sql + ", md_modulo.id md_modulo_id  , md_modulo.nom md_modulo_nom , md_modulo.des md_modulo_des , md_modulo.cod md_modulo_cod  ";
		sql = sql + " from ges_reglas_negocio col_reg";
		sql = sql + " left join ges_empresa emp on emp.id = col_reg.id_emp ";
		sql = sql + " left join mod_modulo md_modulo on md_modulo.id = col_reg.id_mod ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<ReglasNegocio>() {

			@Override
			public ReglasNegocio mapRow(ResultSet rs, int rowNum) throws SQLException {
				ReglasNegocio reglasnegocio= rsToEntity(rs,"col_reg_");
				Empresa empresa = new Empresa();  
				empresa.setId(rs.getInt("emp_id")) ;  
				empresa.setNom(rs.getString("emp_nom")) ;  
				empresa.setRaz_soc(rs.getString("emp_raz_soc")) ;  
				empresa.setRep_leg(rs.getString("emp_rep_leg")) ;  
				empresa.setRuc(rs.getString("emp_ruc")) ;  
				empresa.setDir(rs.getString("emp_dir")) ;  
				empresa.setTel(rs.getString("emp_tel")) ;  
				empresa.setCorr(rs.getString("emp_corr")) ;  
				empresa.setDominio(rs.getString("emp_dominio")) ;  
				//empresa.setPagina_web(rs.getString("emp_pagina_web")) ;  
				reglasnegocio.setEmpresa(empresa);
				Modulo modulo = new Modulo();  
				modulo.setId(rs.getInt("md_modulo_id")) ;  
				modulo.setNom(rs.getString("md_modulo_nom")) ;  
				modulo.setDes(rs.getString("md_modulo_des")) ;  
				modulo.setCod(rs.getString("md_modulo_cod")) ;  
				reglasnegocio.setModulo(modulo);
				return reglasnegocio;
			}

		});

	}	




	// funciones privadas utilitarias para ReglasNegocio

	private ReglasNegocio rsToEntity(ResultSet rs,String alias) throws SQLException {
		ReglasNegocio reglas_negocio = new ReglasNegocio();

		reglas_negocio.setId(rs.getInt( alias + "id"));
		reglas_negocio.setId_emp(rs.getInt( alias + "id_emp"));
		reglas_negocio.setId_mod(rs.getInt( alias + "id_mod"));
		reglas_negocio.setNom(rs.getString( alias + "nom"));
		reglas_negocio.setCod(rs.getString( alias + "cod"));
		reglas_negocio.setTipo_html(rs.getString( alias + "tipo_html"));
		reglas_negocio.setVal(rs.getString( alias + "val"));
		reglas_negocio.setEst(rs.getString( alias + "est"));
								
		return reglas_negocio;

	}
	
}
