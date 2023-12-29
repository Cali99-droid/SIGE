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
import com.tesla.colegio.model.TipoSeguro;

import com.tesla.colegio.model.GruFam;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface TipoSeguroDAO.
 * @author MV
 *
 */
public class TipoSeguroDAOImpl{
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(TipoSeguro tipo_seguro) {
		if (tipo_seguro.getId() != null) {
			// update
			String sql = "UPDATE cat_tipo_seguro "
						+ "SET nom=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//System.out.println(sql);

			jdbcTemplate.update(sql, 
						tipo_seguro.getNom(),
						tipo_seguro.getEst(),
						tipo_seguro.getUsr_act(),
						new java.util.Date(),
						tipo_seguro.getId()); 
			return tipo_seguro.getId();

		} else {
			// insert
			String sql = "insert into cat_tipo_seguro ("
						+ "nom, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?)";

				//System.out.println(sql);

				jdbcTemplate.update(sql, 
				tipo_seguro.getNom(),
				tipo_seguro.getEst(),
				tipo_seguro.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from cat_tipo_seguro where id=?";
		
		//System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<TipoSeguro> list() {
		String sql = "select * from cat_tipo_seguro";
		
		//System.out.println(sql);
		
		List<TipoSeguro> listTipoSeguro = jdbcTemplate.query(sql, new RowMapper<TipoSeguro>() {

			@Override
			public TipoSeguro mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listTipoSeguro;
	}

	public TipoSeguro get(int id) {
		String sql = "select * from cat_tipo_seguro WHERE id=" + id;
		
		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<TipoSeguro>() {

			@Override
			public TipoSeguro extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public TipoSeguro getFull(int id, String tablas[]) {
		String sql = "select seg.id seg_id, seg.nom seg_nom  ,seg.est seg_est ";
	
		sql = sql + " from cat_tipo_seguro seg "; 
		sql = sql + " where seg.id= " + id; 
				
		//System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<TipoSeguro>() {
		
			@Override
			public TipoSeguro extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					TipoSeguro tiposeguro= rsToEntity(rs,"seg_");
							return tiposeguro;
				}
				
				return null;
			}
			
		});


	}		
	
	public TipoSeguro getByParams(Param param) {

		String sql = "select * from cat_tipo_seguro " + SQLFrmkUtil.getWhere(param);
		
		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<TipoSeguro>() {
			@Override
			public TipoSeguro extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<TipoSeguro> listByParams(Param param, String[] order) {

		String sql = "select * from cat_tipo_seguro " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<TipoSeguro>() {

			@Override
			public TipoSeguro mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<TipoSeguro> listFullByParams(TipoSeguro tiposeguro, String[] order) {
	
		return listFullByParams(Param.toParam("seg",tiposeguro), order);
	
	}	
	
	public List<TipoSeguro> listFullByParams(Param param, String[] order) {

		String sql = "select seg.id seg_id, seg.nom seg_nom  ,seg.est seg_est ";
		sql = sql + " from cat_tipo_seguro seg";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<TipoSeguro>() {

			@Override
			public TipoSeguro mapRow(ResultSet rs, int rowNum) throws SQLException {
				TipoSeguro tiposeguro= rsToEntity(rs,"seg_");
				return tiposeguro;
			}

		});

	}	


	public List<GruFam> getListGruFam(Param param, String[] order) {
		String sql = "select * from alu_gru_fam " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<GruFam>() {

			@Override
			public GruFam mapRow(ResultSet rs, int rowNum) throws SQLException {
				GruFam gru_fam = new GruFam();

				gru_fam.setId(rs.getInt("id"));
				gru_fam.setNom(rs.getString("nom"));
				gru_fam.setCod(rs.getString("cod"));
				gru_fam.setDes(rs.getString("des"));
				gru_fam.setId_dist(rs.getInt("id_dist"));
				gru_fam.setDireccion(rs.getString("direccion"));
				gru_fam.setReferencia(rs.getString("referencia"));
				gru_fam.setId_seg(rs.getInt("id_seg"));
				gru_fam.setId_csal(rs.getInt("id_csal"));
				gru_fam.setEst(rs.getString("est"));
												
				return gru_fam;
			}

		});	
	}


	// funciones privadas utilitarias para TipoSeguro

	private TipoSeguro rsToEntity(ResultSet rs,String alias) throws SQLException {
		TipoSeguro tipo_seguro = new TipoSeguro();

		tipo_seguro.setId(rs.getInt( alias + "id"));
		tipo_seguro.setNom(rs.getString( alias + "nom"));
		tipo_seguro.setEst(rs.getString( alias + "est"));
								
		return tipo_seguro;

	}
	
}
