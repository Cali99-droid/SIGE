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
import com.tesla.colegio.model.Correlativo;

import com.tesla.colegio.model.Sucursal;
import com.tesla.colegio.model.Anio;

import com.sige.web.security.TokenSeguridad;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface CorrelativoDAO.
 * @author MV
 *
 */
public class CorrelativoDAOImpl{
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
	public int saveOrUpdate(Correlativo correlativo) {
		if (correlativo.getId() != null) {
			// update
			String sql = "UPDATE col_correlativo "
						+ "SET id_suc=?, "
						+ "id_anio=?, "
						+ "tipo=?, "
						+ "numero=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//System.out.println(sql);

			jdbcTemplate.update(sql, 
						correlativo.getId_suc(),
						correlativo.getId_anio(),
						correlativo.getTipo(),
						correlativo.getNumero(),
						correlativo.getEst(),
						tokenSeguridad.getId(),
						new java.util.Date(),
						correlativo.getId()); 
			return correlativo.getId();

		} else {
			// insert
			String sql = "insert into col_correlativo ("
						+ "id_suc, "
						+ "id_anio, "
						+ "tipo, "
						+ "numero, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?)";

				//System.out.println(sql);

				jdbcTemplate.update(sql, 
				correlativo.getId_suc(),
				correlativo.getId_anio(),
				correlativo.getTipo(),
				correlativo.getNumero(),
				correlativo.getEst(),
				tokenSeguridad.getId(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from col_correlativo where id=?";
		
		//System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<Correlativo> list() {
		String sql = "select * from col_correlativo";
		
		//System.out.println(sql);
		
		List<Correlativo> listCorrelativo = jdbcTemplate.query(sql, new RowMapper<Correlativo>() {

			@Override
			public Correlativo mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listCorrelativo;
	}

	public Correlativo get(int id) {
		String sql = "select * from col_correlativo WHERE id=" + id;
		
		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Correlativo>() {

			@Override
			public Correlativo extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public Correlativo getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select cc.id cc_id, cc.id_suc cc_id_suc , cc.id_anio cc_id_anio , cc.tipo cc_tipo , cc.numero cc_numero  ,cc.est cc_est ";
		if (aTablas.contains("ges_sucursal"))
			sql = sql + ", suc.id suc_id  , suc.nom suc_nom , suc.dir suc_dir , suc.tel suc_tel , suc.correo suc_correo  ";
		if (aTablas.contains("col_anio"))
			sql = sql + ", anio.id anio_id  , anio.nom anio_nom  ";
	
		sql = sql + " from col_correlativo cc "; 
		if (aTablas.contains("ges_sucursal"))
			sql = sql + " left join ges_sucursal suc on suc.id = cc.id_suc ";
		if (aTablas.contains("col_anio"))
			sql = sql + " left join col_anio anio on anio.id = cc.id_anio ";
		sql = sql + " where cc.id= " + id; 
				
		//System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<Correlativo>() {
		
			@Override
			public Correlativo extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Correlativo correlativo= rsToEntity(rs,"cc_");
					if (aTablas.contains("ges_sucursal")){
						Sucursal sucursal = new Sucursal();  
							sucursal.setId(rs.getInt("suc_id")) ;  
							sucursal.setNom(rs.getString("suc_nom")) ;  
							sucursal.setDir(rs.getString("suc_dir")) ;  
							sucursal.setTel(rs.getString("suc_tel")) ;  
							sucursal.setCorreo(rs.getString("suc_correo")) ;  
							correlativo.setSucursal(sucursal);
					}
					if (aTablas.contains("col_anio")){
						Anio anio = new Anio();  
							anio.setId(rs.getInt("anio_id")) ;  
							anio.setNom(rs.getString("anio_nom")) ;  
							correlativo.setAnio(anio);
					}
							return correlativo;
				}
				
				return null;
			}
			
		});


	}		
	
	public Correlativo getByParams(Param param) {

		String sql = "select * from col_correlativo " + SQLFrmkUtil.getWhere(param);
		
		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Correlativo>() {
			@Override
			public Correlativo extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<Correlativo> listByParams(Param param, String[] order) {

		String sql = "select * from col_correlativo " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<Correlativo>() {

			@Override
			public Correlativo mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<Correlativo> listFullByParams(Correlativo correlativo, String[] order) {
	
		return listFullByParams(Param.toParam("cc",correlativo), order);
	
	}	
	
	public List<Correlativo> listFullByParams(Param param, String[] order) {

		String sql = "select cc.id cc_id, cc.id_suc cc_id_suc , cc.id_anio cc_id_anio , cc.tipo cc_tipo , cc.numero cc_numero  ,cc.est cc_est ";
		sql = sql + ", suc.id suc_id  , suc.nom suc_nom , suc.dir suc_dir , suc.tel suc_tel , suc.correo suc_correo  ";
		sql = sql + ", anio.id anio_id  , anio.nom anio_nom  ";
		sql = sql + " from col_correlativo cc";
		sql = sql + " left join ges_sucursal suc on suc.id = cc.id_suc ";
		sql = sql + " left join col_anio anio on anio.id = cc.id_anio ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<Correlativo>() {

			@Override
			public Correlativo mapRow(ResultSet rs, int rowNum) throws SQLException {
				Correlativo correlativo= rsToEntity(rs,"cc_");
				Sucursal sucursal = new Sucursal();  
				sucursal.setId(rs.getInt("suc_id")) ;  
				sucursal.setNom(rs.getString("suc_nom")) ;  
				sucursal.setDir(rs.getString("suc_dir")) ;  
				sucursal.setTel(rs.getString("suc_tel")) ;  
				sucursal.setCorreo(rs.getString("suc_correo")) ;  
				correlativo.setSucursal(sucursal);
				Anio anio = new Anio();  
				anio.setId(rs.getInt("anio_id")) ;  
				anio.setNom(rs.getString("anio_nom")) ;  
				correlativo.setAnio(anio);
				return correlativo;
			}

		});

	}	




	// funciones privadas utilitarias para Correlativo

	private Correlativo rsToEntity(ResultSet rs,String alias) throws SQLException {
		Correlativo correlativo = new Correlativo();

		correlativo.setId(rs.getInt( alias + "id"));
		correlativo.setId_suc(rs.getInt( alias + "id_suc"));
		correlativo.setId_anio(rs.getInt( alias + "id_anio"));
		correlativo.setTipo(rs.getString( alias + "tipo"));
		correlativo.setNumero(rs.getInt( alias + "numero"));
		correlativo.setEst(rs.getString( alias + "est"));
								
		return correlativo;

	}
	
}
