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
import com.tesla.colegio.model.CategoriaOcupacional;


import com.sige.web.security.TokenSeguridad;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface CategoriaOcupacionalDAO.
 * @author MV
 *
 */
public class CategoriaOcupacionalDAOImpl{
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
	public int saveOrUpdate(CategoriaOcupacional categoria_ocupacional) {
		if (categoria_ocupacional.getId() != null) {
			// update
			String sql = "UPDATE cat_categoria_ocupacional "
						+ "SET nom=?, "
						+ "des=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			System.out.println(sql);

			jdbcTemplate.update(sql, 
						categoria_ocupacional.getNom(),
						categoria_ocupacional.getDes(),
						categoria_ocupacional.getEst(),
						tokenSeguridad.getId(),
						new java.util.Date(),
						categoria_ocupacional.getId()); 
			return categoria_ocupacional.getId();

		} else {
			// insert
			String sql = "insert into cat_categoria_ocupacional ("
						+ "nom, "
						+ "des, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?)";

				System.out.println(sql);

				jdbcTemplate.update(sql, 
				categoria_ocupacional.getNom(),
				categoria_ocupacional.getDes(),
				categoria_ocupacional.getEst(),
				tokenSeguridad.getId(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from cat_categoria_ocupacional where id=?";
		
		System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<CategoriaOcupacional> list() {
		String sql = "select * from cat_categoria_ocupacional";
		
		System.out.println(sql);
		
		List<CategoriaOcupacional> listCategoriaOcupacional = jdbcTemplate.query(sql, new RowMapper<CategoriaOcupacional>() {

			@Override
			public CategoriaOcupacional mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listCategoriaOcupacional;
	}

	public CategoriaOcupacional get(int id) {
		String sql = "select * from cat_categoria_ocupacional WHERE id=" + id;
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<CategoriaOcupacional>() {

			@Override
			public CategoriaOcupacional extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public CategoriaOcupacional getFull(int id, String tablas[]) {
		String sql = "select cocu.id cocu_id, cocu.nom cocu_nom , cocu.des cocu_des  ,cocu.est cocu_est ";
	
		sql = sql + " from cat_categoria_ocupacional cocu "; 
		sql = sql + " where cocu.id= " + id; 
				
		System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<CategoriaOcupacional>() {
		
			@Override
			public CategoriaOcupacional extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					CategoriaOcupacional categoriaocupacional= rsToEntity(rs,"cocu_");
							return categoriaocupacional;
				}
				
				return null;
			}
			
		});


	}		
	
	public CategoriaOcupacional getByParams(Param param) {

		String sql = "select * from cat_categoria_ocupacional " + SQLFrmkUtil.getWhere(param);
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<CategoriaOcupacional>() {
			@Override
			public CategoriaOcupacional extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<CategoriaOcupacional> listByParams(Param param, String[] order) {

		String sql = "select * from cat_categoria_ocupacional " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<CategoriaOcupacional>() {

			@Override
			public CategoriaOcupacional mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<CategoriaOcupacional> listFullByParams(CategoriaOcupacional categoriaocupacional, String[] order) {
	
		return listFullByParams(Param.toParam("cocu",categoriaocupacional), order);
	
	}	
	
	public List<CategoriaOcupacional> listFullByParams(Param param, String[] order) {

		String sql = "select cocu.id cocu_id, cocu.nom cocu_nom , cocu.des cocu_des  ,cocu.est cocu_est ";
		sql = sql + " from cat_categoria_ocupacional cocu";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<CategoriaOcupacional>() {

			@Override
			public CategoriaOcupacional mapRow(ResultSet rs, int rowNum) throws SQLException {
				CategoriaOcupacional categoriaocupacional= rsToEntity(rs,"cocu_");
				return categoriaocupacional;
			}

		});

	}	




	// funciones privadas utilitarias para CategoriaOcupacional

	private CategoriaOcupacional rsToEntity(ResultSet rs,String alias) throws SQLException {
		CategoriaOcupacional categoria_ocupacional = new CategoriaOcupacional();

		categoria_ocupacional.setId(rs.getInt( alias + "id"));
		categoria_ocupacional.setNom(rs.getString( alias + "nom"));
		categoria_ocupacional.setDes(rs.getString( alias + "des"));
		categoria_ocupacional.setEst(rs.getString( alias + "est"));
								
		return categoria_ocupacional;

	}
	
}
