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
import com.tesla.colegio.model.Nivel;

import com.tesla.colegio.model.Grad;
import com.tesla.colegio.model.Area;
import com.tesla.colegio.model.Curso;
import com.tesla.colegio.model.Matricula;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface NivelDAO.
 * @author MV
 *
 */
public class NivelDAOImpl{
	final static Logger logger = Logger.getLogger(NivelDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(Nivel nivel) {
		if (nivel.getId() !=null) {
			// update
			String sql = "UPDATE cat_nivel "
						+ "SET cod_mod=?, "
						+ "nom=?, "
						+ "est=?,usr_act=?,fec_act=? "
					+ "WHERE id=?";
			
			//logger.info(sql);

			return jdbcTemplate.update(sql, 
						nivel.getCod_mod(),
						nivel.getNom(),
						nivel.getEst(),
						nivel.getUsr_act(),
						new java.util.Date(),
						nivel.getId()); 

		} else {
			// insert
			String sql = "insert into cat_nivel ("
						+ "cod_mod, "
						+ "nom, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				nivel.getCod_mod(),
				nivel.getNom(),
				nivel.getEst(),
				nivel.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	
	public void delete(int id) {
		String sql = "delete from cat_nivel where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	
	public List<Nivel> list() {
		String sql = "select * from cat_nivel";
		
		//logger.info(sql);
		
		List<Nivel> listNivel = jdbcTemplate.query(sql, new RowMapper<Nivel>() {

			
			public Nivel mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listNivel;
	}

	
	public Nivel get(int id) {
		String sql = "select * from cat_nivel WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Nivel>() {

			
			public Nivel extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	
	public Nivel getFull(int id, String tablas[]) {
		String sql = "select nvl.id nvl_id, nvl.cod_mod nvl_cod_mod , nvl.nom nvl_nom  ,nvl.est nvl_est ";
	
		sql = sql + " from cat_nivel nvl "; 
		sql = sql + " where nvl.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<Nivel>() {
		
			
			public Nivel extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Nivel nivel= rsToEntity(rs,"nvl_");
							return nivel;
				}
				
				return null;
			}
			
		});


	}		
	
	
	public Nivel getByParams(Param param) {

		String sql = "select * from cat_nivel " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Nivel>() {
			
			public Nivel extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	
	public List<Nivel> listByParams(Param param, String[] order) {

		String sql = "select * from cat_nivel " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Nivel>() {

			
			public Nivel mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	
	public List<Nivel> listFullByParams(Nivel nivel, String[] order) {
	
		return listFullByParams(Param.toParam("nvl",nivel), order);
	
	}	
	
	
	public List<Nivel> listFullByParams(Param param, String[] order) {

		String sql = "select nvl.id nvl_id, nvl.cod_mod nvl_cod_mod , nvl.nom nvl_nom  ,nvl.est nvl_est ";
		sql = sql + " from cat_nivel nvl";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Nivel>() {

			
			public Nivel mapRow(ResultSet rs, int rowNum) throws SQLException {
				Nivel nivel= rsToEntity(rs,"nvl_");
				return nivel;
			}

		});

	}	


	public List<Grad> getListGrad(Param param, String[] order) {
		String sql = "select * from cat_grad " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<Grad>() {

			
			public Grad mapRow(ResultSet rs, int rowNum) throws SQLException {
				Grad grad = new Grad();

				grad.setId(rs.getInt("id"));
				grad.setId_nvl(rs.getInt("id_nvl"));
				grad.setNom(rs.getString("nom"));
				grad.setEst(rs.getString("est"));
												
				return grad;
			}

		});	
	}
	public List<Area> getListArea(Param param, String[] order) {
		String sql = "select * from col_area " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<Area>() {

			
			public Area mapRow(ResultSet rs, int rowNum) throws SQLException {
				Area area = new Area();

				area.setId(rs.getInt("id"));
				/*area.setId_ni(rs.getInt("id_ni"));
				area.setId_ci(rs.getInt("id_ci"));
				area.setNom(rs.getString("nom"));
				area.setAnio(rs.getInt("anio"));
				area.setOrd(rs.getInt("ord"));
				area.setCap(rs.getInt("cap"));
				area.setTall(rs.getString("tall"));*/
				area.setEst(rs.getString("est"));
												
				return area;
			}

		});	
	}
	public List<Curso> getListCurso(Param param, String[] order) {
		String sql = "select * from col_curso " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<Curso>() {

			
			public Curso mapRow(ResultSet rs, int rowNum) throws SQLException {
				Curso curso = new Curso();

				curso.setId(rs.getInt("id"));
				//curso.setId_ar(rs.getString("id_ar"));
				//curso.setNom(rs.getString("nom"));
				//curso.setId_nvl(rs.getInt("id_nvl"));
				//curso.setId_au(rs.getInt("id_au"));
				//curso.setId_prof(rs.getInt("id_prof"));
				//curso.setAnio(rs.getString("anio"));
				//curso.setEva(rs.getString("eva"));
				//curso.setCom(rs.getString("com"));
				//curso.setProm(rs.getString("prom"));
				//curso.setSim(rs.getString("sim"));
				curso.setEst(rs.getString("est"));
												
				return curso;
			}

		});	
	}
	public List<Matricula> getListMatricula(Param param, String[] order) {
		String sql = "select * from mat_matricula " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<Matricula>() {

			
			public Matricula mapRow(ResultSet rs, int rowNum) throws SQLException {
				Matricula matricula = new Matricula();

				matricula.setId(rs.getInt("id"));
				matricula.setId_alu(rs.getInt("id_alu"));
				matricula.setId_fam(rs.getInt("id_fam"));
				matricula.setId_enc(rs.getInt("id_enc"));
				matricula.setId_con(rs.getInt("id_con"));
				matricula.setId_cli(rs.getInt("id_cli"));
				matricula.setId_per(rs.getInt("id_per"));
				matricula.setId_au(rs.getInt("id_au"));
				matricula.setId_gra(rs.getInt("id_gra"));
				matricula.setId_niv(rs.getInt("id_niv"));
				matricula.setFecha(rs.getDate("fecha"));
				matricula.setCar_pod(rs.getString("car_pod"));
				matricula.setEst(rs.getString("est"));
												
				return matricula;
			}

		});	
	}


	// funciones privadas utilitarias para Nivel

	private Nivel rsToEntity(ResultSet rs,String alias) throws SQLException {
		Nivel nivel = new Nivel();

		nivel.setId(rs.getInt( alias + "id"));
		nivel.setCod_mod(rs.getString( alias + "cod_mod"));
		nivel.setNom(rs.getString( alias + "nom"));
		nivel.setEst(rs.getString( alias + "est"));
								
		return nivel;

	}
	
}
