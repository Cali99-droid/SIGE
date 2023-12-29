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
import com.tesla.colegio.model.TipoDescuento;

import com.tesla.colegio.model.Beca;

import com.sige.web.security.TokenSeguridad;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface TipoDescuentoDAO.
 * @author MV
 *
 */
public class TipoDescuentoDAOImpl{
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
	public int saveOrUpdate(TipoDescuento tipo_descuento) {
		if (tipo_descuento.getId() != null) {
			// update
			String sql = "UPDATE cat_tipo_descuento "
						+ "SET nom=?, "
						+ "cod=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			System.out.println(sql);

			jdbcTemplate.update(sql, 
						tipo_descuento.getNom(),
						tipo_descuento.getCod(),
						tipo_descuento.getEst(),
						tokenSeguridad.getId(),
						new java.util.Date(),
						tipo_descuento.getId()); 
			return tipo_descuento.getId();

		} else {
			// insert
			String sql = "insert into cat_tipo_descuento ("
						+ "nom, "
						+ "cod, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?)";

				System.out.println(sql);

				jdbcTemplate.update(sql, 
				tipo_descuento.getNom(),
				tipo_descuento.getCod(),
				tipo_descuento.getEst(),
				tokenSeguridad.getId(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from cat_tipo_descuento where id=?";
		
		System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<TipoDescuento> list() {
		String sql = "select * from cat_tipo_descuento";
		
		System.out.println(sql);
		
		List<TipoDescuento> listTipoDescuento = jdbcTemplate.query(sql, new RowMapper<TipoDescuento>() {

			@Override
			public TipoDescuento mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listTipoDescuento;
	}

	public TipoDescuento get(int id) {
		String sql = "select * from cat_tipo_descuento WHERE id=" + id;
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<TipoDescuento>() {

			@Override
			public TipoDescuento extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public TipoDescuento getFull(int id, String tablas[]) {
		String sql = "select tdes.id tdes_id, tdes.nom tdes_nom , tdes.cod tdes_cod  ,tdes.est tdes_est ";
	
		sql = sql + " from cat_tipo_descuento tdes "; 
		sql = sql + " where tdes.id= " + id; 
				
		System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<TipoDescuento>() {
		
			@Override
			public TipoDescuento extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					TipoDescuento tipodescuento= rsToEntity(rs,"tdes_");
							return tipodescuento;
				}
				
				return null;
			}
			
		});


	}		
	
	public TipoDescuento getByParams(Param param) {

		String sql = "select * from cat_tipo_descuento " + SQLFrmkUtil.getWhere(param);
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<TipoDescuento>() {
			@Override
			public TipoDescuento extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<TipoDescuento> listByParams(Param param, String[] order) {

		String sql = "select * from cat_tipo_descuento " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<TipoDescuento>() {

			@Override
			public TipoDescuento mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<TipoDescuento> listFullByParams(TipoDescuento tipodescuento, String[] order) {
	
		return listFullByParams(Param.toParam("tdes",tipodescuento), order);
	
	}	
	
	public List<TipoDescuento> listFullByParams(Param param, String[] order) {

		String sql = "select tdes.id tdes_id, tdes.nom tdes_nom , tdes.cod tdes_cod  ,tdes.est tdes_est ";
		sql = sql + " from cat_tipo_descuento tdes";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<TipoDescuento>() {

			@Override
			public TipoDescuento mapRow(ResultSet rs, int rowNum) throws SQLException {
				TipoDescuento tipodescuento= rsToEntity(rs,"tdes_");
				return tipodescuento;
			}

		});

	}	


	public List<Beca> getListBeca(Param param, String[] order) {
		String sql = "select * from col_beca " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<Beca>() {

			@Override
			public Beca mapRow(ResultSet rs, int rowNum) throws SQLException {
				Beca beca = new Beca();

				beca.setId(rs.getInt("id"));
				beca.setNom(rs.getString("nom"));
				beca.setVal(rs.getString("val"));
				beca.setAbrv(rs.getString("abrv"));
				beca.setId_tdes(rs.getInt("id_tdes"));
				beca.setEst(rs.getString("est"));
												
				return beca;
			}

		});	
	}


	// funciones privadas utilitarias para TipoDescuento

	private TipoDescuento rsToEntity(ResultSet rs,String alias) throws SQLException {
		TipoDescuento tipo_descuento = new TipoDescuento();

		tipo_descuento.setId(rs.getInt( alias + "id"));
		tipo_descuento.setNom(rs.getString( alias + "nom"));
		tipo_descuento.setCod(rs.getString( alias + "cod"));
		tipo_descuento.setEst(rs.getString( alias + "est"));
								
		return tipo_descuento;

	}
	
}
