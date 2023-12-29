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
import com.tesla.colegio.model.GradoInstruccion;

import com.tesla.colegio.model.Familiar;
import com.tesla.colegio.model.Trabajador;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface GradoInstruccionDAO.
 * @author MV
 *
 */
public class GradoInstruccionDAOImpl{
	final static Logger logger = Logger.getLogger(GradoInstruccionDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(GradoInstruccion grado_instruccion) {
		if (grado_instruccion.getId() != null) {
			// update
			String sql = "UPDATE cat_grado_instruccion "
						+ "SET nom=?, "
						+ "est=?,usr_act=?,fec_act=? "
					+ "WHERE id=?";
			
			//logger.info(sql);

			return jdbcTemplate.update(sql, 
						grado_instruccion.getNom(),
						grado_instruccion.getEst(),
						grado_instruccion.getUsr_act(),
						new java.util.Date(),
						grado_instruccion.getId()); 

		} else {
			// insert
			String sql = "insert into cat_grado_instruccion ("
						+ "nom, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				grado_instruccion.getNom(),
				grado_instruccion.getEst(),
				grado_instruccion.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	
	public void delete(int id) {
		String sql = "delete from cat_grado_instruccion where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	
	public List<GradoInstruccion> list() {
		String sql = "select * from cat_grado_instruccion";
		
		//logger.info(sql);
		
		List<GradoInstruccion> listGradoInstruccion = jdbcTemplate.query(sql, new RowMapper<GradoInstruccion>() {

			
			public GradoInstruccion mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listGradoInstruccion;
	}

	
	public GradoInstruccion get(int id) {
		String sql = "select * from cat_grado_instruccion WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<GradoInstruccion>() {

			
			public GradoInstruccion extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	
	public GradoInstruccion getFull(int id, String tablas[]) {
		String sql = "select gin.id gin_id, gin.nom gin_nom  ,gin.est gin_est ";
	
		sql = sql + " from cat_grado_instruccion gin "; 
		sql = sql + " where gin.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<GradoInstruccion>() {
		
			
			public GradoInstruccion extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					GradoInstruccion gradoInstruccion= rsToEntity(rs,"gin_");
							return gradoInstruccion;
				}
				
				return null;
			}
			
		});


	}		
	
	
	public GradoInstruccion getByParams(Param param) {

		String sql = "select * from cat_grado_instruccion " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<GradoInstruccion>() {
			
			public GradoInstruccion extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	
	public List<GradoInstruccion> listByParams(Param param, String[] order) {

		String sql = "select * from cat_grado_instruccion " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<GradoInstruccion>() {

			
			public GradoInstruccion mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	
	public List<GradoInstruccion> listFullByParams(GradoInstruccion gradoInstruccion, String[] order) {
	
		return listFullByParams(Param.toParam("gin",gradoInstruccion), order);
	
	}	
	
	
	public List<GradoInstruccion> listFullByParams(Param param, String[] order) {

		String sql = "select gin.id gin_id, gin.nom gin_nom  ,gin.est gin_est ";
		sql = sql + " from cat_grado_instruccion gin";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<GradoInstruccion>() {

			
			public GradoInstruccion mapRow(ResultSet rs, int rowNum) throws SQLException {
				GradoInstruccion gradoInstruccion= rsToEntity(rs,"gin_");
				return gradoInstruccion;
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
				//trabajador.setFot(rs.getString("fot"));
				trabajador.setNum_hij(rs.getInt("num_hij"));
				trabajador.setId_usr(rs.getInt("id_usr"));
				trabajador.setEst(rs.getString("est"));
												
				return trabajador;
			}

		});	
	}


	// funciones privadas utilitarias para GradoInstruccion

	private GradoInstruccion rsToEntity(ResultSet rs,String alias) throws SQLException {
		GradoInstruccion grado_instruccion = new GradoInstruccion();

		grado_instruccion.setId(rs.getInt( alias + "id"));
		grado_instruccion.setNom(rs.getString( alias + "nom"));
		grado_instruccion.setEst(rs.getString( alias + "est"));
								
		return grado_instruccion;

	}
	
}
