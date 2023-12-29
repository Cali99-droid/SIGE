package com.sige.mat.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Arrays;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import com.tesla.colegio.model.Beca;

import com.tesla.colegio.model.TipoDescuento;
import com.tesla.colegio.model.MotivoBeca;

import com.sige.web.security.TokenSeguridad;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface BecaDAO.
 * @author MV
 *
 */
public class BecaDAOImpl{
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
	public int saveOrUpdate(Beca beca) {
		if (beca.getId() != null) {
			// update
			String sql = "UPDATE col_beca "
						+ "SET nom=?, "
						+ "val=?, "
						+ "abrv=?, "
						+ "id_tdes=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			System.out.println(sql);

			jdbcTemplate.update(sql, 
						beca.getNom(),
						beca.getVal(),
						beca.getAbrv(),
						beca.getId_tdes(),
						beca.getEst(),
						tokenSeguridad.getId(),
						new java.util.Date(),
						beca.getId()); 
			return beca.getId();

		} else {
			// insert
			String sql = "insert into col_beca ("
						+ "nom, "
						+ "val, "
						+ "abrv, "
						+ "id_tdes, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?)";

				System.out.println(sql);

				jdbcTemplate.update(sql, 
				beca.getNom(),
				beca.getVal(),
				beca.getAbrv(),
				beca.getId_tdes(),
				beca.getEst(),
				tokenSeguridad.getId(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from col_beca where id=?";
		
		System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<Beca> list() {
		String sql = "select * from col_beca";
		
		System.out.println(sql);
		
		List<Beca> listBeca = jdbcTemplate.query(sql, new RowMapper<Beca>() {

			@Override
			public Beca mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listBeca;
	}

	public Beca get(int id) {
		String sql = "select * from col_beca WHERE id=" + id;
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Beca>() {

			@Override
			public Beca extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public Beca getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select bec.id bec_id, bec.nom bec_nom , bec.val bec_val , bec.abrv bec_abrv , bec.id_tdes bec_id_tdes  ,bec.est bec_est ";
		if (aTablas.contains("cat_tipo_descuento"))
			sql = sql + ", tdes.id tdes_id  , tdes.nom tdes_nom , tdes.cod tdes_cod  ";
	
		sql = sql + " from col_beca bec "; 
		if (aTablas.contains("cat_tipo_descuento"))
			sql = sql + " left join cat_tipo_descuento tdes on tdes.id = bec.id_tdes ";
		sql = sql + " where bec.id= " + id; 
				
		System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<Beca>() {
		
			@Override
			public Beca extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Beca beca= rsToEntity(rs,"bec_");
					if (aTablas.contains("cat_tipo_descuento")){
						TipoDescuento tipodescuento = new TipoDescuento();  
							tipodescuento.setId(rs.getInt("tdes_id")) ;  
							tipodescuento.setNom(rs.getString("tdes_nom")) ;  
							tipodescuento.setCod(rs.getString("tdes_cod")) ;  
							beca.setTipoDescuento(tipodescuento);
					}
							return beca;
				}
				
				return null;
			}
			
		});


	}		
	
	public Beca getByParams(Param param) {

		String sql = "select * from col_beca " + SQLFrmkUtil.getWhere(param);
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Beca>() {
			@Override
			public Beca extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<Beca> listByParams(Param param, String[] order) {

		String sql = "select * from col_beca " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<Beca>() {

			@Override
			public Beca mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<Beca> listFullByParams(Beca beca, String[] order) {
	
		return listFullByParams(Param.toParam("bec",beca), order);
	
	}	
	
	public List<Beca> listFullByParams(Param param, String[] order) {

		String sql = "select bec.id bec_id, bec.nom bec_nom , bec.val bec_val , bec.abrv bec_abrv , bec.id_tdes bec_id_tdes  ,bec.est bec_est ";
		sql = sql + ", tdes.id tdes_id  , tdes.nom tdes_nom , tdes.cod tdes_cod  ";
		sql = sql + " from col_beca bec";
		sql = sql + " left join cat_tipo_descuento tdes on tdes.id = bec.id_tdes ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<Beca>() {

			@Override
			public Beca mapRow(ResultSet rs, int rowNum) throws SQLException {
				Beca beca= rsToEntity(rs,"bec_");
				TipoDescuento tipodescuento = new TipoDescuento();  
				tipodescuento.setId(rs.getInt("tdes_id")) ;  
				tipodescuento.setNom(rs.getString("tdes_nom")) ;  
				tipodescuento.setCod(rs.getString("tdes_cod")) ;  
				beca.setTipoDescuento(tipodescuento);
				return beca;
			}

		});

	}	


	public List<MotivoBeca> getListMotivoBeca(Param param, String[] order) {
		String sql = "select * from col_motivo_beca " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<MotivoBeca>() {

			@Override
			public MotivoBeca mapRow(ResultSet rs, int rowNum) throws SQLException {
				MotivoBeca motivo_beca = new MotivoBeca();

				motivo_beca.setId(rs.getInt("id"));
				motivo_beca.setNom(rs.getString("nom"));
				motivo_beca.setId_bec(rs.getInt("id_bec"));
				motivo_beca.setEst(rs.getString("est"));
												
				return motivo_beca;
			}

		});	
	}


	// funciones privadas utilitarias para Beca

	private Beca rsToEntity(ResultSet rs,String alias) throws SQLException {
		Beca beca = new Beca();

		beca.setId(rs.getInt( alias + "id"));
		beca.setNom(rs.getString( alias + "nom"));
		beca.setVal(rs.getString( alias + "val"));
		beca.setAbrv(rs.getString( alias + "abrv"));
		beca.setId_tdes(rs.getInt( alias + "id_tdes"));
		beca.setEst(rs.getString( alias + "est"));
								
		return beca;

	}
	
}
