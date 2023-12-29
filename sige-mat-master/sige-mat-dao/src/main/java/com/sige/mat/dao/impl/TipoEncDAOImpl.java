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
import com.tesla.colegio.model.TipoPre;

import com.tesla.colegio.model.EncuestaPreg;

import com.sige.web.security.TokenSeguridad;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface TipoEncDAO.
 * @author MV
 *
 */
public class TipoEncDAOImpl{
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
	public int saveOrUpdate(TipoPre tipo_enc) {
		if (tipo_enc.getId() != null) {
			// update
			String sql = "UPDATE cat_tipo_enc "
						+ "SET nom=?, "
						+ "cod=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			System.out.println(sql);

			jdbcTemplate.update(sql, 
						tipo_enc.getNom(),
						tipo_enc.getCod(),
						tipo_enc.getEst(),
						tokenSeguridad.getId(),
						new java.util.Date(),
						tipo_enc.getId()); 
			return tipo_enc.getId();

		} else {
			// insert
			String sql = "insert into cat_tipo_enc ("
						+ "nom, "
						+ "cod, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?)";

				System.out.println(sql);

				jdbcTemplate.update(sql, 
				tipo_enc.getNom(),
				tipo_enc.getCod(),
				tipo_enc.getEst(),
				tokenSeguridad.getId(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from cat_tipo_enc where id=?";
		
		System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<TipoPre> list() {
		String sql = "select * from cat_tipo_enc";
		
		System.out.println(sql);
		
		List<TipoPre> listTipoEnc = jdbcTemplate.query(sql, new RowMapper<TipoPre>() {

			@Override
			public TipoPre mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listTipoEnc;
	}

	public TipoPre get(int id) {
		String sql = "select * from cat_tipo_enc WHERE id=" + id;
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<TipoPre>() {

			@Override
			public TipoPre extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public TipoPre getFull(int id, String tablas[]) {
		String sql = "select cte.id cte_id, cte.nom cte_nom , cte.cod cte_cod  ,cte.est cte_est ";
	
		sql = sql + " from cat_tipo_enc cte "; 
		sql = sql + " where cte.id= " + id; 
				
		System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<TipoPre>() {
		
			@Override
			public TipoPre extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					TipoPre tipoenc= rsToEntity(rs,"cte_");
							return tipoenc;
				}
				
				return null;
			}
			
		});


	}		
	
	public TipoPre getByParams(Param param) {

		String sql = "select * from cat_tipo_enc " + SQLFrmkUtil.getWhere(param);
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<TipoPre>() {
			@Override
			public TipoPre extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<TipoPre> listByParams(Param param, String[] order) {

		String sql = "select * from cat_tipo_enc " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<TipoPre>() {

			@Override
			public TipoPre mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<TipoPre> listFullByParams(TipoPre tipoenc, String[] order) {
	
		return listFullByParams(Param.toParam("cte",tipoenc), order);
	
	}	
	
	public List<TipoPre> listFullByParams(Param param, String[] order) {

		String sql = "select cte.id cte_id, cte.nom cte_nom , cte.cod cte_cod  ,cte.est cte_est ";
		sql = sql + " from cat_tipo_enc cte";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<TipoPre>() {

			@Override
			public TipoPre mapRow(ResultSet rs, int rowNum) throws SQLException {
				TipoPre tipoenc= rsToEntity(rs,"cte_");
				return tipoenc;
			}

		});

	}	


	public List<EncuestaPreg> getListEncuestaPreg(Param param, String[] order) {
		String sql = "select * from col_encuesta_preg " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<EncuestaPreg>() {

			@Override
			public EncuestaPreg mapRow(ResultSet rs, int rowNum) throws SQLException {
				EncuestaPreg encuesta_preg = new EncuestaPreg();

				encuesta_preg.setId(rs.getInt("id"));
				encuesta_preg.setId_enc(rs.getInt("id_enc"));
				encuesta_preg.setId_ctp(rs.getInt("id_ctp"));
				encuesta_preg.setPre(rs.getString("pre"));
				encuesta_preg.setOrd(rs.getInt("ord"));
				encuesta_preg.setDep(rs.getString("dep"));
				encuesta_preg.setEst(rs.getString("est"));
												
				return encuesta_preg;
			}

		});	
	}


	// funciones privadas utilitarias para TipoEnc

	private TipoPre rsToEntity(ResultSet rs,String alias) throws SQLException {
		TipoPre tipo_enc = new TipoPre();

		tipo_enc.setId(rs.getInt( alias + "id"));
		tipo_enc.setNom(rs.getString( alias + "nom"));
		tipo_enc.setCod(rs.getString( alias + "cod"));
		tipo_enc.setEst(rs.getString( alias + "est"));
								
		return tipo_enc;

	}
	
}
