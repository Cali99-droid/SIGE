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

import com.tesla.colegio.model.Departamento;
import com.tesla.colegio.model.Empresa;

import com.tesla.colegio.model.GiroNegocio;
import com.tesla.colegio.model.Pais;
import com.tesla.colegio.model.Provincia;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface EmpresaDAO.
 * @author MV
 *
 */
public class EmpresaDAOImpl{
	final static Logger logger = Logger.getLogger(EmpresaDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(Empresa empresa) {
		if (empresa.getId()!= null) {
			// update
			String sql = "UPDATE ges_empresa "
						+ "SET nom=?, "
						+ "raz_soc=?, "
						+ "rep_leg=?, "
						+ "ruc=?, "
						+ "id_dist=?, "
						+ "id_rep_leg=?, "
						+ "abrv=?, "
						+ "corr=?, "
						+ "pag_web=?, "
						+ "dir=?, "
						+ "tel=?, "
						+ "est=?,usr_act=?,fec_act=? "
					+ "WHERE id=?";
			
			//logger.info(sql);

			return jdbcTemplate.update(sql, 
						empresa.getNom(),
						empresa.getRaz_soc(),
						empresa.getRep_leg(),
						empresa.getRuc(),
						empresa.getId_dist(),
						empresa.getId_rep_leg(),
						empresa.getAbrv(),
						empresa.getCorr(),
						empresa.getPag_web(),
						empresa.getDir(),
						empresa.getTel(),
						empresa.getEst(),
						empresa.getUsr_act(),
						new java.util.Date(),
						empresa.getId()); 

		} else {
			// insert
			String sql = "insert into ges_empresa ("
						+ "nom, "
						+ "raz_soc, "
						+ "rep_leg, "
						+ "id_rep_leg, "
						+ "ruc, "
						+ "id_dist, "
						+ "abrv, "
						+ "corr, "
						+ "pag_web, "
						+ "dir, "
						+ "tel, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				empresa.getNom(),
				empresa.getRaz_soc(),
				empresa.getRep_leg(),
				empresa.getId_rep_leg(),
				empresa.getRuc(),
				empresa.getId_dist(),
				empresa.getAbrv(),
				empresa.getCorr(),
				empresa.getPag_web(),
				empresa.getDir(),
				empresa.getTel(),
				empresa.getEst(),
				empresa.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	
	public void delete(int id) {
		String sql = "delete from ges_empresa where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	
	public List<Empresa> list() {
		String sql = "select * from ges_empresa";
		
		//logger.info(sql);
		
		List<Empresa> listEmpresa = jdbcTemplate.query(sql, new RowMapper<Empresa>() {

			
			public Empresa mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listEmpresa;
	}

	
	public Empresa get(int id) {
		String sql = "select * from ges_empresa WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Empresa>() {

			
			public Empresa extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	
	public Empresa getFull(int id, String tablas[]) {
		String sql = "select emp.id emp_id, emp.nom emp_nom , emp.raz_soc emp_raz_soc , emp.rep_leg emp_rep_leg , emp.ruc emp_ruc , emp.dir emp_dir , emp.tel emp_tel  ,emp.est emp_est ";
	
		sql = sql + " from ges_empresa emp "; 
		sql = sql + " where emp.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<Empresa>() {
		
			
			public Empresa extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Empresa empresa= rsToEntity(rs,"emp_");
							return empresa;
				}
				
				return null;
			}
			
		});


	}		
	
	
	public Empresa getByParams(Param param) {

		String sql = "select * from ges_empresa " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Empresa>() {
			
			public Empresa extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	
	public List<Empresa> listByParams(Param param, String[] order) {

		String sql = "select * from ges_empresa " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Empresa>() {

			
			public Empresa mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	
	public List<Empresa> listFullByParams(Empresa empresa, String[] order) {
	
		return listFullByParams(Param.toParam("emp",empresa), order);
	
	}	
	
	
	public List<Empresa> listFullByParams(Param param, String[] order) {

		String sql = "select emp.id emp_id, emp.nom emp_nom , emp.abrv emp_abrv, emp.id_dist emp_id_dist, emp.pag_web emp_pag_web, emp.id_rep_leg emp_id_rep_leg, emp.raz_soc emp_raz_soc , emp.rep_leg emp_rep_leg , emp.ruc emp_ruc , emp.corr emp_corr, emp.dir emp_dir , emp.tel emp_tel  ,emp.est emp_est, ";
		sql = sql + " pro.id pro_id, dep.id dep_id, pa.id pa_id";
		sql = sql + " from ges_empresa emp";
		sql = sql + " LEFT JOIN cat_distrito dist ON emp.id_dist=dist.id";
		sql = sql + " LEFT JOIN cat_provincia pro ON dist.id_pro=pro.id";
		sql = sql + " LEFT JOIN cat_departamento dep ON pro.id_dep=dep.id";
		sql = sql + " LEFT JOIN cat_pais pa ON dep.id_pais=pa.id";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Empresa>() {

			
			public Empresa mapRow(ResultSet rs, int rowNum) throws SQLException {
				Empresa empresa= rsToEntity(rs,"emp_");
				Provincia provincia = new Provincia();
				provincia.setId(rs.getInt("pro_id"));
				empresa.setProvincia(provincia);
				Departamento departamento = new Departamento();
				departamento.setId(rs.getInt("dep_id"));
				empresa.setDepartamento(departamento);
				Pais pais = new Pais();
				pais.setId(rs.getInt("pa_id"));
				empresa.setPais(pais);
				return empresa;
			}

		});

	}	


	public List<GiroNegocio> getListGiroNegocio(Param param, String[] order) {
		String sql = "select * from ges_giro_negocio " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<GiroNegocio>() {

			
			public GiroNegocio mapRow(ResultSet rs, int rowNum) throws SQLException {
				GiroNegocio giro_negocio = new GiroNegocio();

				giro_negocio.setId(rs.getInt("id"));
				giro_negocio.setId_emp(rs.getInt("id_emp"));
				giro_negocio.setNom(rs.getString("nom"));
				giro_negocio.setDes(rs.getString("des"));
				giro_negocio.setEst(rs.getString("est"));
												
				return giro_negocio;
			}

		});	
	}


	// funciones privadas utilitarias para Empresa

	private Empresa rsToEntity(ResultSet rs,String alias) throws SQLException {
		Empresa empresa = new Empresa();

		empresa.setId(rs.getInt( alias + "id"));
		empresa.setNom(rs.getString( alias + "nom"));
		empresa.setRaz_soc(rs.getString( alias + "raz_soc"));
		empresa.setRep_leg(rs.getString( alias + "rep_leg"));
		empresa.setId_rep_leg(rs.getInt( alias + "id_rep_leg"));
		empresa.setRuc(rs.getString( alias + "ruc"));
		empresa.setAbrv(rs.getString( alias + "abrv"));
		empresa.setId_dist(rs.getInt( alias + "id_dist"));
		empresa.setCorr(rs.getString( alias + "corr"));
		empresa.setDir(rs.getString( alias + "dir"));
		empresa.setTel(rs.getString( alias + "tel"));
		empresa.setPag_web(rs.getString( alias + "pag_web"));
		empresa.setEst(rs.getString( alias + "est"));
								
		return empresa;

	}
	
}
