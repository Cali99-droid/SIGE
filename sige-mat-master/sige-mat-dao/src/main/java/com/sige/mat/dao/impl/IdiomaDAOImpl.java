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
import com.tesla.colegio.model.Idioma;

import com.tesla.colegio.model.Alumno;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface IdiomaDAO.
 * @author MV
 *
 */
public class IdiomaDAOImpl{
	final static Logger logger = Logger.getLogger(IdiomaDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(Idioma idioma) {
		if (idioma.getId() != null) {
			// update
			String sql = "UPDATE cat_idioma "
						+ "SET nom=?, "
						+ "est=?,usr_act=?,fec_act=? "
					+ "WHERE id=?";
			
			//logger.info(sql);

			return jdbcTemplate.update(sql, 
						idioma.getNom(),
						idioma.getEst(),
						idioma.getUsr_act(),
						new java.util.Date(),
						idioma.getId()); 

		} else {
			// insert
			String sql = "insert into cat_idioma ("
						+ "nom, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				idioma.getNom(),
				idioma.getEst(),
				idioma.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	
	public void delete(int id) {
		String sql = "delete from cat_idioma where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	
	public List<Idioma> list() {
		String sql = "select * from cat_idioma";
		
		//logger.info(sql);
		
		List<Idioma> listIdioma = jdbcTemplate.query(sql, new RowMapper<Idioma>() {

			
			public Idioma mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listIdioma;
	}

	
	public Idioma get(int id) {
		String sql = "select * from cat_idioma WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Idioma>() {

			
			public Idioma extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	
	public Idioma getFull(int id, String tablas[]) {
		String sql = "select idio.id idio_id, idio.nom idio_nom  ,idio.est idio_est ";
	
		sql = sql + " from cat_idioma idio "; 
		sql = sql + " where idio.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<Idioma>() {
		
			
			public Idioma extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Idioma idioma= rsToEntity(rs,"idio_");
							return idioma;
				}
				
				return null;
			}
			
		});


	}		
	
	
	public Idioma getByParams(Param param) {

		String sql = "select * from cat_idioma " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Idioma>() {
			
			public Idioma extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	
	public List<Idioma> listByParams(Param param, String[] order) {

		String sql = "select * from cat_idioma " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Idioma>() {

			
			public Idioma mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	
	public List<Idioma> listFullByParams(Idioma idioma, String[] order) {
	
		return listFullByParams(Param.toParam("idio",idioma), order);
	
	}	
	
	
	public List<Idioma> listFullByParams(Param param, String[] order) {

		String sql = "select idio.id idio_id, idio.nom idio_nom  ,idio.est idio_est ";
		sql = sql + " from cat_idioma idio";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Idioma>() {

			
			public Idioma mapRow(ResultSet rs, int rowNum) throws SQLException {
				Idioma idioma= rsToEntity(rs,"idio_");
				return idioma;
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


	// funciones privadas utilitarias para Idioma

	private Idioma rsToEntity(ResultSet rs,String alias) throws SQLException {
		Idioma idioma = new Idioma();

		idioma.setId(rs.getInt( alias + "id"));
		idioma.setNom(rs.getString( alias + "nom"));
		idioma.setEst(rs.getString( alias + "est"));
								
		return idioma;

	}
	
}
