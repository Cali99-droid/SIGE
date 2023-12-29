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
import com.tesla.colegio.model.Sucursal;
import com.tesla.colegio.model.Departamento;
import com.tesla.colegio.model.GiroSucursal;
import com.tesla.colegio.model.Pais;
import com.tesla.colegio.model.Provincia;
import com.tesla.colegio.model.Servicio;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface SucursalDAO.
 * @author MV
 *
 */
public class SucursalDAOImpl{
	final static Logger logger = Logger.getLogger(SucursalDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(Sucursal sucursal) {
		if (sucursal.getId() != null) {
			// update
			String sql = "UPDATE ges_sucursal "
						+ "SET nom=?, "
						+ "abrv=?, "
						+ "pag_web=?, "
						+ "cod=?, "
						+ "dir=?, "
						+ "tel=?, "
						+ "correo=?, "
						+ "tot_au=?, "
						+ "tot_ofi=?, "
						+ "id_dist=?, "
						+ "id_emp=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			return jdbcTemplate.update(sql, 
						sucursal.getNom(),
						sucursal.getAbrv(),
						sucursal.getPag_web(),
						sucursal.getCod(),
						sucursal.getDir(),
						sucursal.getTel(),
						sucursal.getCorreo(),
						sucursal.getTot_au(),
						sucursal.getTot_ofi(),
						sucursal.getId_dist(),
						sucursal.getId_emp(),
						sucursal.getEst(),
						sucursal.getUsr_act(),
						new java.util.Date(),
						sucursal.getId()); 

		} else {
			// insert
			String sql = "insert into ges_sucursal ("
						+ "nom, "
						+ "abrv, "
						+ "pag_web, "
						+ "cod, "
						+ "tot_au, "
						+ "tot_ofi, "
						+ "id_dist, "
						+ "id_emp, "
						+ "id_ggn, "
						+ "dir, "
						+ "tel, "
						+ "correo, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ? , ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				sucursal.getNom(),
				sucursal.getAbrv(),
				sucursal.getPag_web(),
				sucursal.getCod(),
				sucursal.getTot_au(),
				sucursal.getTot_ofi(),
				sucursal.getId_dist(),
				sucursal.getId_emp(),
				sucursal.getId_ggn(),				
				sucursal.getDir(),
				sucursal.getTel(),
				sucursal.getCorreo(),
				sucursal.getEst(),
				sucursal.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	
	public void delete(int id) {
		String sql = "delete from ges_sucursal where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	
	public List<Sucursal> list() {
		String sql = "select * from ges_sucursal";
		
		//logger.info(sql);
		
		List<Sucursal> listSucursal = jdbcTemplate.query(sql, new RowMapper<Sucursal>() {

			
			public Sucursal mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listSucursal;
	}

	
	public Sucursal get(int id) {
		String sql = "select * from ges_sucursal WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Sucursal>() {

			
			public Sucursal extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	
	public Sucursal getFull(int id, String tablas[]) {
		String sql = "select suc.id suc_id, suc.nom suc_nom , suc.dir suc_dir , suc.tel suc_tel , suc.correo suc_correo  ,suc.est suc_est ";
	
		sql = sql + " from ges_sucursal suc "; 
		sql = sql + " where suc.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<Sucursal>() {
		
			
			public Sucursal extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Sucursal sucursal= rsToEntity(rs,"suc_");
							return sucursal;
				}
				
				return null;
			}
			
		});


	}		
	
	
	public Sucursal getByParams(Param param) {

		String sql = "select * from ges_sucursal " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Sucursal>() {
			
			public Sucursal extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	
	public List<Sucursal> listByParams(Param param, String[] order) {

		String sql = "select * from ges_sucursal " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Sucursal>() {

			
			public Sucursal mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	
	public List<Sucursal> listFullByParams(Sucursal sucursal, String[] order) {
	
		return listFullByParams(Param.toParam("suc",sucursal), order);
	
	}	
	
	
	public List<Sucursal> listFullByParams(Param param, String[] order) {

		String sql = "select suc.id suc_id, suc.id_dist suc_id_dist, suc.id_emp suc_id_emp, suc.id_ggn suc_id_ggn, suc.nom suc_nom , suc.abrv suc_abrv, suc.pag_web suc_pag_web, suc.tot_au suc_tot_au, suc.tot_ofi suc_tot_ofi, suc.cod suc_cod, suc.dir suc_dir , suc.tel suc_tel , suc.correo suc_correo  ,suc.est suc_est, ";
		sql = sql + " pro.id pro_id, dep.id dep_id, pa.id pa_id";
		sql = sql + " from ges_sucursal suc";
		sql = sql + " LEFT JOIN cat_distrito dist ON suc.id_dist=dist.id";
		sql = sql + " LEFT JOIN cat_provincia pro ON dist.id_pro=pro.id";
		sql = sql + " LEFT JOIN cat_departamento dep ON pro.id_dep=dep.id";
		sql = sql + " LEFT JOIN cat_pais pa ON dep.id_pais=pa.id";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Sucursal>() {

			
			public Sucursal mapRow(ResultSet rs, int rowNum) throws SQLException {
				Sucursal sucursal= rsToEntity(rs,"suc_");
				Provincia provincia = new Provincia();
				provincia.setId(rs.getInt("pro_id"));
				sucursal.setProvincia(provincia);
				Departamento departamento = new Departamento();
				departamento.setId(rs.getInt("dep_id"));
				sucursal.setDepartamento(departamento);
				Pais pais = new Pais();
				pais.setId(rs.getInt("pa_id"));
				sucursal.setPais(pais);
				return sucursal;
			}

		});

	}	


	public List<GiroSucursal> getListGiroSucursal(Param param, String[] order) {
		String sql = "select * from ges_giro_sucursal " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<GiroSucursal>() {

			
			public GiroSucursal mapRow(ResultSet rs, int rowNum) throws SQLException {
				GiroSucursal giro_sucursal = new GiroSucursal();

				giro_sucursal.setId(rs.getInt("id"));
				giro_sucursal.setId_gir(rs.getInt("id_gir"));
				giro_sucursal.setId_suc(rs.getInt("id_suc"));
				giro_sucursal.setEst(rs.getString("est"));
												
				return giro_sucursal;
			}

		});	
	}
	public List<Servicio> getListServicio(Param param, String[] order) {
		String sql = "select * from ges_servicio " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<Servicio>() {

			
			public Servicio mapRow(ResultSet rs, int rowNum) throws SQLException {
				Servicio servicio = new Servicio();

				servicio.setId(rs.getInt("id"));
				servicio.setId_suc(rs.getInt("id_suc"));
				servicio.setNom(rs.getString("nom"));
				servicio.setEst(rs.getString("est"));
												
				return servicio;
			}

		});	
	}


	// funciones privadas utilitarias para Sucursal

	private Sucursal rsToEntity(ResultSet rs,String alias) throws SQLException {
		Sucursal sucursal = new Sucursal();

		sucursal.setId(rs.getInt( alias + "id"));
		sucursal.setId_ggn(rs.getInt( alias + "id_ggn"));
		sucursal.setId_emp(rs.getInt(alias + "id_emp"));
		sucursal.setId_dist(rs.getInt( alias + "id_dist"));
		sucursal.setNom(rs.getString( alias + "nom"));
		sucursal.setAbrv(rs.getString( alias + "abrv"));
		sucursal.setCod(rs.getString( alias + "cod"));
		sucursal.setDir(rs.getString( alias + "dir"));
		sucursal.setTel(rs.getString( alias + "tel"));
		sucursal.setPag_web(rs.getString(alias + "pag_web"));
		sucursal.setCorreo(rs.getString( alias + "correo"));
		sucursal.setEst(rs.getString( alias + "est"));
		sucursal.setTot_au(rs.getInt(alias + "tot_au"));
		sucursal.setTot_ofi(rs.getInt(alias + "tot_ofi"));
		return sucursal;

	}
	
}
