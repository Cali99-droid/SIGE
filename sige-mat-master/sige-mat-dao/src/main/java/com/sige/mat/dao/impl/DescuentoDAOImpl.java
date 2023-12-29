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
import com.tesla.colegio.model.Descuento;



import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface DescuentoDAO.
 * @author MV
 *
 */
public class DescuentoDAOImpl{
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(Descuento descuento) {
		if (descuento.getId() != null) {
			// update
			String sql = "UPDATE cat_descuento "
						+ "SET nom=?, "
						+ "des=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//System.out.println(sql);

			jdbcTemplate.update(sql, 
						descuento.getNom(),
						descuento.getDes(),
						descuento.getEst(),
						descuento.getUsr_act(),
						new java.util.Date(),
						descuento.getId()); 
			return descuento.getId();

		} else {
			// insert
			String sql = "insert into cat_descuento ("
						+ "nom, "
						+ "des, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?)";

				//System.out.println(sql);

				jdbcTemplate.update(sql, 
				descuento.getNom(),
				descuento.getDes(),
				descuento.getEst(),
				descuento.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from cat_descuento where id=?";
		
		//System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<Descuento> list() {
		String sql = "select * from cat_descuento";
		
		//System.out.println(sql);
		
		List<Descuento> listDescuento = jdbcTemplate.query(sql, new RowMapper<Descuento>() {

			@Override
			public Descuento mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listDescuento;
	}

	public Descuento get(int id) {
		String sql = "select * from cat_descuento WHERE id=" + id;
		
		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Descuento>() {

			@Override
			public Descuento extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public Descuento getFull(int id, String tablas[]) {
		String sql = "select des.id des_id, des.nom des_nom , des.des des_des  ,des.est des_est ";
	
		sql = sql + " from cat_descuento des "; 
		sql = sql + " where des.id= " + id; 
				
		//System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<Descuento>() {
		
			@Override
			public Descuento extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Descuento descuento= rsToEntity(rs,"des_");
							return descuento;
				}
				
				return null;
			}
			
		});


	}		
	
	public Descuento getByParams(Param param) {

		String sql = "select * from cat_descuento " + SQLFrmkUtil.getWhere(param);
		
		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Descuento>() {
			@Override
			public Descuento extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<Descuento> listByParams(Param param, String[] order) {

		String sql = "select * from cat_descuento " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<Descuento>() {

			@Override
			public Descuento mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<Descuento> listFullByParams(Descuento descuento, String[] order) {
	
		return listFullByParams(Param.toParam("des",descuento), order);
	
	}	
	
	public List<Descuento> listFullByParams(Param param, String[] order) {

		String sql = "select des.id des_id, des.nom des_nom , des.des des_des  ,des.est des_est ";
		sql = sql + " from cat_descuento des";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<Descuento>() {

			@Override
			public Descuento mapRow(ResultSet rs, int rowNum) throws SQLException {
				Descuento descuento= rsToEntity(rs,"des_");
				return descuento;
			}

		});

	}	




	// funciones privadas utilitarias para Descuento

	private Descuento rsToEntity(ResultSet rs,String alias) throws SQLException {
		Descuento descuento = new Descuento();

		descuento.setId(rs.getInt( alias + "id"));
		descuento.setNom(rs.getString( alias + "nom"));
		descuento.setDes(rs.getString( alias + "des"));
		descuento.setEst(rs.getString( alias + "est"));
								
		return descuento;

	}
	
}
