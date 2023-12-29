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
import com.tesla.colegio.model.EstCivil;

import com.tesla.colegio.model.Familiar;
import com.tesla.colegio.model.Alumno;
import com.tesla.colegio.model.Trabajador;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementación de la interface EstCivilDAO.
 * @author MV
 *
 */
public class EstCivilDAOImpl{
	final static Logger logger = Logger.getLogger(EstCivilDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(EstCivil est_civil) {
		if (est_civil.getId() != null) {
			// update
			String sql = "UPDATE cat_est_civil "
						+ "SET nom=?, "
						+ "est=?,usr_act=?,fec_act=? "
					+ "WHERE id=?";
			
			//logger.info(sql);

			return jdbcTemplate.update(sql, 
						est_civil.getNom(),
						est_civil.getEst(),
						est_civil.getUsr_act(),
						new java.util.Date(),
						est_civil.getId()); 

		} else {
			// insert
			String sql = "insert into cat_est_civil ("
						+ "nom, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				est_civil.getNom(),
				est_civil.getEst(),
				est_civil.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	
	public void delete(int id) {
		String sql = "delete from cat_est_civil where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	
	public List<EstCivil> list() {
		String sql = "select * from cat_est_civil";
		
		//logger.info(sql);
		
		List<EstCivil> listEstCivil = jdbcTemplate.query(sql, new RowMapper<EstCivil>() {

			
			public EstCivil mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listEstCivil;
	}

	
	public EstCivil get(int id) {
		String sql = "select * from cat_est_civil WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<EstCivil>() {

			
			public EstCivil extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	
	public EstCivil getFull(int id, String tablas[]) {
		String sql = "select eci.id eci_id, eci.nom eci_nom  ,eci.est eci_est ";
	
		sql = sql + " from cat_est_civil eci "; 
		sql = sql + " where eci.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<EstCivil>() {
		
			
			public EstCivil extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					EstCivil estCivil= rsToEntity(rs,"eci_");
							return estCivil;
				}
				
				return null;
			}
			
		});


	}		
	
	
	public EstCivil getByParams(Param param) {

		String sql = "select * from cat_est_civil " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<EstCivil>() {
			
			public EstCivil extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	
	public List<EstCivil> listByParams(Param param, String[] order) {

		String sql = "select * from cat_est_civil " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<EstCivil>() {

			
			public EstCivil mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	
	public List<EstCivil> listFullByParams(EstCivil estCivil, String[] order) {
	
		return listFullByParams(Param.toParam("eci",estCivil), order);
	
	}	
	
	
	public List<EstCivil> listFullByParams(Param param, String[] order) {

		String sql = "select eci.id eci_id, eci.nom eci_nom  ,eci.est eci_est ";
		sql = sql + " from cat_est_civil eci";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<EstCivil>() {

			
			public EstCivil mapRow(ResultSet rs, int rowNum) throws SQLException {
				EstCivil estCivil= rsToEntity(rs,"eci_");
				return estCivil;
			}

		});

	}	


	public List<Familiar> getListFamiliar(Param param, String[] order) {
		String sql = "select * from alu_familiar " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<Familiar>() {

			
			public Familiar mapRow(ResultSet rs, int rowNum) throws SQLException {
				Familiar familiar = new Familiar();

				familiar.setId(rs.getInt("id"));
				familiar.setId_tdc(rs.getInt("id_tdc"));
				familiar.setId_par(rs.getInt("id_par"));
				familiar.setId_tap(rs.getString("id_tap"));
				familiar.setId_gen(rs.getString("id_gen"));
				familiar.setId_eci(rs.getInt("id_eci"));
				familiar.setId_gin(rs.getInt("id_gin"));
				familiar.setId_rel(rs.getInt("id_rel"));
				familiar.setId_dist(rs.getInt("id_dist"));
				familiar.setId_ocu(rs.getInt("id_ocu"));
				familiar.setNro_doc(rs.getString("nro_doc"));
				familiar.setNom(rs.getString("nom"));
				familiar.setApe_pat(rs.getString("ape_pat"));
				familiar.setApe_mat(rs.getString("ape_mat"));
				familiar.setHue(rs.getString("hue"));
				familiar.setFec_nac(rs.getDate("fec_nac"));
				familiar.setViv(rs.getString("viv"));
				familiar.setViv_alu(rs.getString("viv_alu"));
				familiar.setDir(rs.getString("dir"));
				familiar.setTlf(rs.getString("tlf"));
				familiar.setCorr(rs.getString("corr"));
				familiar.setCel(rs.getString("cel"));
				familiar.setPass(rs.getString("pass"));
				familiar.setCto_tra(rs.getString("cto_tra"));
				familiar.setEst(rs.getString("est"));
												
				return familiar;
			}

		});	
	}
	public List<Alumno> getListAlumno(Param param, String[] order) {
		String sql = "select * from alu_alumno " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<Alumno>() {

			
			public Alumno mapRow(ResultSet rs, int rowNum) throws SQLException {
				Alumno alumno = new Alumno();

				alumno.setId(rs.getInt("id"));
				alumno.setId_tdc(rs.getInt("id_tdc"));
				alumno.setId_idio1(rs.getInt("id_idio1"));
				alumno.setId_idio2(rs.getInt("id_idio2"));
				alumno.setId_eci(rs.getInt("id_eci"));
				alumno.setId_tap(rs.getString("id_tap"));
				alumno.setId_gen(rs.getString("id_gen"));
				alumno.setCod(rs.getString("cod"));
				alumno.setNro_doc(rs.getString("nro_doc"));
				alumno.setNom(rs.getString("nom"));
				alumno.setApe_pat(rs.getString("ape_pat"));
				alumno.setApe_mat(rs.getString("ape_mat"));
				alumno.setFec_nac(rs.getDate("fec_nac"));
				alumno.setNum_hij(rs.getInt("num_hij"));
				alumno.setDireccion(rs.getString("direccion"));
				alumno.setTelf(rs.getString("telf"));
				alumno.setCelular(rs.getString("celular"));
				alumno.setPass_educando(rs.getString("pass_educando"));
				//alumno.setFoto(rs.getString("foto"));
				alumno.setEst(rs.getString("est"));
												
				return alumno;
			}

		});	
	}
	public List<Trabajador> getListTrabajador(Param param, String[] order) {
		String sql = "select * from ges_trabajador " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<Trabajador>() {

			
			public Trabajador mapRow(ResultSet rs, int rowNum) throws SQLException {
				Trabajador trabajador = new Trabajador();

				trabajador.setId(rs.getInt("id"));
				trabajador.setId_tdc(rs.getInt("id_tdc"));
				trabajador.setNro_doc(rs.getString("nro_doc"));
				trabajador.setApe_pat(rs.getString("ape_pat"));
				trabajador.setApe_mat(rs.getString("ape_mat"));
				trabajador.setNom(rs.getString("nom"));
				trabajador.setFec_nac(rs.getDate("fec_nac"));
				trabajador.setGenero(rs.getString("genero"));
				trabajador.setId_eci(rs.getInt("id_eci"));
				trabajador.setDir(rs.getString("dir"));
				trabajador.setTel(rs.getString("tel"));
				trabajador.setCel(rs.getString("cel"));
				trabajador.setCorr(rs.getString("corr"));
				trabajador.setId_gin(rs.getInt("id_gin"));
				trabajador.setCarrera(rs.getString("carrera"));
				trabajador.setFot(rs.getBytes("fot"));
				trabajador.setNum_hij(rs.getInt("num_hij"));
				trabajador.setId_usr(rs.getInt("id_usr"));
				trabajador.setEst(rs.getString("est"));
												
				return trabajador;
			}

		});	
	}


	// funciones privadas utilitarias para EstCivil

	private EstCivil rsToEntity(ResultSet rs,String alias) throws SQLException {
		EstCivil est_civil = new EstCivil();

		est_civil.setId(rs.getInt( alias + "id"));
		est_civil.setNom(rs.getString( alias + "nom"));
		est_civil.setEst(rs.getString( alias + "est"));
								
		return est_civil;

	}
	
}
