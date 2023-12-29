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
import com.tesla.colegio.model.Pais;

import com.tesla.colegio.model.Departamento;

import com.sige.web.security.TokenSeguridad;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface PaisDAO.
 * @author MV
 *
 */
public class PaisDAOImpl{
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
	public int saveOrUpdate(Pais pais) {
		if (pais.getId() != null) {
			// update
			String sql = "UPDATE cat_pais "
						+ "SET nom=?, "
						+ "cod=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//System.out.println(sql);

			jdbcTemplate.update(sql, 
						pais.getNom(),
						pais.getCod(),
						pais.getEst(),
						tokenSeguridad.getId(),
						new java.util.Date(),
						pais.getId()); 
			return pais.getId();

		} else {
			// insert
			String sql = "insert into cat_pais ("
						+ "nom, "
						+ "cod, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?)";

				//System.out.println(sql);

				jdbcTemplate.update(sql, 
				pais.getNom(),
				pais.getCod(),
				pais.getEst(),
				tokenSeguridad.getId(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from cat_pais where id=?";
		
		//System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<Pais> list() {
		String sql = "select * from cat_pais";
		
		//System.out.println(sql);
		
		List<Pais> listPais = jdbcTemplate.query(sql, new RowMapper<Pais>() {

			@Override
			public Pais mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listPais;
	}

	public Pais get(int id) {
		String sql = "select * from cat_pais WHERE id=" + id;
		
		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Pais>() {

			@Override
			public Pais extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public Pais getFull(int id, String tablas[]) {
		String sql = "select pais.id pais_id, pais.nom pais_nom , pais.cod pais_cod  ,pais.est pais_est ";
	
		sql = sql + " from cat_pais pais "; 
		sql = sql + " where pais.id= " + id; 
				
		//System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<Pais>() {
		
			@Override
			public Pais extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Pais pais= rsToEntity(rs,"pais_");
							return pais;
				}
				
				return null;
			}
			
		});


	}		
	
	public Pais getByParams(Param param) {

		String sql = "select * from cat_pais " + SQLFrmkUtil.getWhere(param);
		
		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Pais>() {
			@Override
			public Pais extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<Pais> listByParams(Param param, String[] order) {

		String sql = "select * from cat_pais " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<Pais>() {

			@Override
			public Pais mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<Pais> listFullByParams(Pais pais, String[] order) {
	
		return listFullByParams(Param.toParam("pais",pais), order);
	
	}	
	
	public List<Pais> listFullByParams(Param param, String[] order) {

		String sql = "select pais.id pais_id, pais.nom pais_nom , pais.cod pais_cod  ,pais.est pais_est ";
		sql = sql + " from cat_pais pais";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<Pais>() {

			@Override
			public Pais mapRow(ResultSet rs, int rowNum) throws SQLException {
				Pais pais= rsToEntity(rs,"pais_");
				return pais;
			}

		});

	}	


	public List<Departamento> getListDepartamento(Param param, String[] order) {
		String sql = "select * from cat_departamento " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<Departamento>() {

			@Override
			public Departamento mapRow(ResultSet rs, int rowNum) throws SQLException {
				Departamento departamento = new Departamento();

				departamento.setId(rs.getInt("id"));
				departamento.setNom(rs.getString("nom"));
				/*departamento.setCod(rs.getString("cod"));
				departamento.setId_pais(rs.getInt("id_pais"));*/
				departamento.setEst(rs.getString("est"));
												
				return departamento;
			}

		});	
	}


	// funciones privadas utilitarias para Pais

	private Pais rsToEntity(ResultSet rs,String alias) throws SQLException {
		Pais pais = new Pais();

		pais.setId(rs.getInt( alias + "id"));
		pais.setNom(rs.getString( alias + "nom"));
		pais.setCod(rs.getString( alias + "cod"));
		pais.setEst(rs.getString( alias + "est"));
								
		return pais;

	}
	
}
