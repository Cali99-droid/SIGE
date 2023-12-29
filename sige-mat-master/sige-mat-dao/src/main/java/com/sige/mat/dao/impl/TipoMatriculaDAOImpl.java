package com.sige.mat.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import com.tesla.colegio.model.TipoMatricula;


import com.sige.web.security.TokenSeguridad;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface TipoMatriculaDAO.
 * @author MV
 *
 */
public class TipoMatriculaDAOImpl{
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
	public int saveOrUpdate(TipoMatricula tipo_matricula) {
		if (tipo_matricula.getId() != null) {
			// update
			String sql = "UPDATE cat_tipo_matricula "
						+ "SET nom=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//System.out.println(sql);

			jdbcTemplate.update(sql, 
						tipo_matricula.getNom(),
						tipo_matricula.getEst(),
						tokenSeguridad.getId(),
						new java.util.Date(),
						tipo_matricula.getId()); 
			return tipo_matricula.getId();

		} else {
			// insert
			String sql = "insert into cat_tipo_matricula ("
						+ "nom, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?)";

				//System.out.println(sql);

				jdbcTemplate.update(sql, 
				tipo_matricula.getNom(),
				tipo_matricula.getEst(),
				tokenSeguridad.getId(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from cat_tipo_matricula where id=?";
		
		//System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<TipoMatricula> list() {
		String sql = "select * from cat_tipo_matricula";
		
		//System.out.println(sql);
		
		List<TipoMatricula> listTipoMatricula = jdbcTemplate.query(sql, new RowMapper<TipoMatricula>() {

			@Override
			public TipoMatricula mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listTipoMatricula;
	}

	public TipoMatricula get(int id) {
		String sql = "select * from cat_tipo_matricula WHERE id=" + id;
		
		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<TipoMatricula>() {

			@Override
			public TipoMatricula extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public TipoMatricula getFull(int id, String tablas[]) {
		String sql = "select tip_mat.id tip_mat_id, tip_mat.nom tip_mat_nom  ,tip_mat.est tip_mat_est ";
	
		sql = sql + " from cat_tipo_matricula tip_mat "; 
		sql = sql + " where tip_mat.id= " + id; 
				
		//System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<TipoMatricula>() {
		
			@Override
			public TipoMatricula extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					TipoMatricula tipomatricula= rsToEntity(rs,"tip_mat_");
							return tipomatricula;
				}
				
				return null;
			}
			
		});


	}		
	
	public TipoMatricula getByParams(Param param) {

		String sql = "select * from cat_tipo_matricula " + SQLFrmkUtil.getWhere(param);
		
		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<TipoMatricula>() {
			@Override
			public TipoMatricula extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<TipoMatricula> listByParams(Param param, String[] order) {

		String sql = "select * from cat_tipo_matricula " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<TipoMatricula>() {

			@Override
			public TipoMatricula mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<TipoMatricula> listFullByParams(TipoMatricula tipomatricula, String[] order) {
	
		return listFullByParams(Param.toParam("tip_mat",tipomatricula), order);
	
	}	
	
	public List<TipoMatricula> listFullByParams(Param param, String[] order) {

		String sql = "select tip_mat.id tip_mat_id, tip_mat.nom tip_mat_nom  ,tip_mat.est tip_mat_est ";
		sql = sql + " from cat_tipo_matricula tip_mat";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<TipoMatricula>() {

			@Override
			public TipoMatricula mapRow(ResultSet rs, int rowNum) throws SQLException {
				TipoMatricula tipomatricula= rsToEntity(rs,"tip_mat_");
				return tipomatricula;
			}

		});

	}	




	// funciones privadas utilitarias para TipoMatricula

	private TipoMatricula rsToEntity(ResultSet rs,String alias) throws SQLException {
		TipoMatricula tipo_matricula = new TipoMatricula();

		tipo_matricula.setId(rs.getInt( alias + "id"));
		tipo_matricula.setNom(rs.getString( alias + "nom"));
		tipo_matricula.setEst(rs.getString( alias + "est"));
								
		return tipo_matricula;

	}
	
}
